/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.emi.trinkets.api.SlotReference
 *  dev.emi.trinkets.api.Trinket
 *  dev.emi.trinkets.api.TrinketComponent
 *  dev.emi.trinkets.api.TrinketsApi
 *  net.fabricmc.fabric.api.util.TriState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.particle.ParticleTypes
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.util.Pair
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.Registries
 *  net.minecraft.component.DataComponentTypes
 */
package com.nonid;

import com.nonid.NeedsOfNature;
import com.nonid.NonAccessoryEffects;
import com.nonid.NonAccessoryShedSystem;
import com.nonid.NonDataAccessoryTrinket;
import com.nonid.NonGenderSystem;
import com.nonid.NonTrinketsIntegration;
import com.nonid.data.NonAccessoryBehavior;
import com.nonid.data.NonAccessoryDefinitions;
import com.nonid.data.NonAccessoryItemRegistry;
import com.nonid.data.NonLiquidRoles;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Pair;
import net.minecraft.registry.Registries;

final class NonTrinketsCompat {
    private static final Map<UUID, NonAccessoryEffects> LAST_SYNCED_EFFECTS = new ConcurrentHashMap<UUID, NonAccessoryEffects>();
    private static final Map<UUID, Set<String>> PENDING_UNEQUIPPED_SLOTS = new ConcurrentHashMap<UUID, Set<String>>();

    private NonTrinketsCompat() {
    }

    static void registerPredicates() {
        TrinketsApi.registerTrinketPredicate(NeedsOfNature.id("female"), (stack, slot, entity) -> NonAccessoryBehavior.isSlot(slotId(slot), "legs/v") && !NonGenderSystem.hasFemaleGender(entity) ? TriState.FALSE : TriState.DEFAULT);
        TrinketsApi.registerTrinketPredicate(NeedsOfNature.id("male"), (stack, slot, entity) -> NonAccessoryBehavior.isSlot(slotId(slot), "legs/d") && !NonGenderSystem.hasMaleGender(entity) ? TriState.FALSE : TriState.DEFAULT);
    }

    static void registerTrinketItem(Item item) {
        if (item != null) {
            TrinketsApi.registerTrinket((Item)item, (Trinket)NonDataAccessoryTrinket.INSTANCE);
        }
    }

    static NonAccessoryEffects getGlobalAccessoryEffects(LivingEntity entity) {
        if (entity == null) {
            return NonAccessoryEffects.NEUTRAL;
        }
        return TrinketsApi.getTrinketComponent((LivingEntity)entity).map(TrinketComponent::getAllEquipped).orElse(List.of()).stream().filter(pair -> !NonTrinketsCompat.isPendingUnequippedSlot(entity, (SlotReference)pair.getLeft())).filter(pair -> {
            Identifier itemId = Registries.ITEM.getId(pair.getRight().getItem());
            return !NonAccessoryShedSystem.isSlotEffectSuppressed(entity.getUuid(), slotId(pair.getLeft()), itemId);
        }).map(pair -> Registries.ITEM.getId(pair.getRight().getItem())).map(NonAccessoryDefinitions::getEffects).map(NonAccessoryEffects::globalOnly).reduce(NonAccessoryEffects.NEUTRAL, NonAccessoryEffects::combine);
    }

    static NonAccessoryEffects getActiveTankAccessoryEffects(LivingEntity entity) {
        if (entity == null) {
            return NonAccessoryEffects.NEUTRAL;
        }
        String activeSlotId = NonTrinketsIntegration.getActiveTankSlotId(entity);
        return TrinketsApi.getTrinketComponent(entity).map(TrinketComponent::getAllEquipped).orElse(List.of()).stream().filter(pair -> !isPendingUnequippedSlot(entity, pair.getLeft())).filter(pair -> NonAccessoryBehavior.isSlot(slotId(pair.getLeft()), activeSlotId)).filter(pair -> {
            Identifier itemId = Registries.ITEM.getId(pair.getRight().getItem());
            return !NonAccessoryShedSystem.isSlotEffectSuppressed(entity.getUuid(), slotId(pair.getLeft()), itemId);
        }).map(pair -> Registries.ITEM.getId(pair.getRight().getItem())).map(NonAccessoryDefinitions::getEffects).map(NonAccessoryEffects::tankScopedOnly).reduce(NonAccessoryEffects.NEUTRAL, NonAccessoryEffects::combine);
    }

    static boolean blocksPregnancy(ServerPlayerEntity player) {
        if (player == null) {
            return false;
        }
        String activeSlotId = NonTrinketsIntegration.getActiveTankSlotId((LivingEntity)player);
        return TrinketsApi.getTrinketComponent((LivingEntity)player).map(TrinketComponent::getAllEquipped).orElse(List.of()).stream().filter(pair -> !NonTrinketsCompat.isPendingUnequippedSlot((LivingEntity)player, (SlotReference)pair.getLeft())).anyMatch(pair -> {
            ItemStack stack = (ItemStack)pair.getRight();
            if (stack == null || stack.isEmpty()) {
                return false;
            }
            String slotId = slotId(pair.getLeft());
            if (!NonAccessoryBehavior.isSlot(slotId, activeSlotId)) {
                return false;
            }
            Identifier itemId = Registries.ITEM.getId(stack.getItem());
            if (NonAccessoryShedSystem.isSlotEffectSuppressed(player.getUuid(), slotId, itemId)) {
                return false;
            }
            return NonAccessoryDefinitions.getBehavior(itemId).blocksPregnancy();
        });
    }

    static boolean hasProtectorFor(ServerPlayerEntity player, NonLiquidRoles.InjectorRole role) {
        if (player == null || role == null || role == NonLiquidRoles.InjectorRole.NONE) {
            return false;
        }
        return TrinketsApi.getTrinketComponent((LivingEntity)player).map(TrinketComponent::getAllEquipped).orElse(List.of()).stream().filter(pair -> !NonTrinketsCompat.isPendingUnequippedSlot((LivingEntity)player, (SlotReference)pair.getLeft())).anyMatch(pair -> {
            ItemStack stack = (ItemStack)pair.getRight();
            if (stack == null || stack.isEmpty()) {
                return false;
            }
            return NonAccessoryDefinitions.getBehavior(Registries.ITEM.getId(stack.getItem())).blocks(role);
        });
    }

    static boolean consumeProtectorFor(ServerWorld world, ServerPlayerEntity player, NonLiquidRoles.InjectorRole role) {
        if (world == null || player == null || role == null || role == NonLiquidRoles.InjectorRole.NONE) {
            return false;
        }
        TrinketComponent component = TrinketsApi.getTrinketComponent((LivingEntity)player).orElse(null);
        if (component == null) {
            return false;
        }
        for (Pair<SlotReference, ItemStack> pair : component.getAllEquipped()) {
            NonAccessoryBehavior behavior;
            ItemStack stack;
            if (isPendingUnequippedSlot(player, pair.getLeft()) || (stack = pair.getRight()) == null || stack.isEmpty() || !(behavior = NonAccessoryDefinitions.getBehavior(Registries.ITEM.getId(stack.getItem()))).blocks(role)) continue;
            if (Math.max(stack.getMaxDamage(), behavior.maxDurability()) <= 0) {
                return false;
            }
            int cost = Math.max(1, behavior.protectionDurabilityCost());
            applyDurabilityCost(world, player, pair.getLeft(), stack, cost);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 0.75f, 0.9f + world.getRandom().nextFloat() * 0.2f);
            world.spawnParticles((ParticleEffect)ParticleTypes.CRIT, player.getX(), player.getBodyY(0.55), player.getZ(), 8, 0.25, 0.35, 0.25, 0.08);
            NonTrinketsCompat.forceAccessorySync(player);
            return true;
        }
        return false;
    }

    static void applyInjectionDurabilityCost(ServerWorld world, ServerPlayerEntity player, NonLiquidRoles.InjectorRole role) {
        if (world == null || player == null || role == null) {
            return;
        }
        if (role != NonLiquidRoles.InjectorRole.V && role != NonLiquidRoles.InjectorRole.A) {
            return;
        }
        TrinketComponent component = TrinketsApi.getTrinketComponent((LivingEntity)player).orElse(null);
        if (component == null) {
            return;
        }
        for (Pair<SlotReference, ItemStack> pair : component.getAllEquipped()) {
            int cost;
            ItemStack stack;
            if (isPendingUnequippedSlot(player, pair.getLeft()) || (stack = pair.getRight()) == null || stack.isEmpty()) continue;
            String slotId = slotId(pair.getLeft());
            if (role == NonLiquidRoles.InjectorRole.V && !NonAccessoryBehavior.isSlot(slotId, "legs/v") || role == NonLiquidRoles.InjectorRole.A && !NonAccessoryBehavior.isSlot(slotId, "legs/a")) continue;
            NonAccessoryBehavior behavior = NonAccessoryDefinitions.getBehavior(Registries.ITEM.getId(stack.getItem()));
            int n = cost = role == NonLiquidRoles.InjectorRole.V ? behavior.vInjectionDurabilityCost() : behavior.aInjectionDurabilityCost();
            if (cost <= 0) continue;
            applyDurabilityCost(world, player, pair.getLeft(), stack, cost);
        }
        NonTrinketsCompat.forceAccessorySync(player);
    }

    private static boolean applyDurabilityCost(ServerWorld world, ServerPlayerEntity player, SlotReference slot, ItemStack stack, int cost) {
        int nextDamage;
        if (world == null || player == null || slot == null || stack == null || stack.isEmpty() || cost <= 0) {
            return false;
        }
        NonAccessoryBehavior behavior = NonAccessoryDefinitions.getBehavior(Registries.ITEM.getId(stack.getItem()));
        int maxDamage = Math.max(stack.getMaxDamage(), behavior.maxDurability());
        if (maxDamage <= 0) {
            return false;
        }
        if ((nextDamage = stack.getDamage() + cost) >= maxDamage) {
            TrinketsApi.onTrinketBroken((ItemStack)stack, (SlotReference)slot, (LivingEntity)player);
            stack.decrement(1);
        } else {
            stack.setDamage(nextDamage);
        }
        slot.inventory().markUpdate();
        return true;
    }

    static void onAccessoryEquipped(LivingEntity entity, SlotReference slot) {
        NonTrinketsCompat.clearPendingUnequippedSlot(entity, slot);
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            NonTrinketsCompat.forceAccessorySync(player);
        }
    }

    static void onAccessoryUnequipped(LivingEntity entity, SlotReference slot) {
        NonTrinketsCompat.markPendingUnequippedSlot(entity, slot);
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            NonTrinketsCompat.forceAccessorySync(player);
        }
    }

    static void syncAccessoryStateIfChanged(ServerPlayerEntity player) {
        NonAccessoryEffects previous;
        if (player == null) {
            return;
        }
        NonAccessoryEffects effects = NonTrinketsCompat.getGlobalAccessoryEffects((LivingEntity)player);
        if ((effects = effects.combine(NonTrinketsCompat.getActiveTankAccessoryEffects((LivingEntity)player))).equals(previous = LAST_SYNCED_EFFECTS.put(player.getUuid(), effects))) {
            return;
        }
        NeedsOfNature.syncRuntimeGameplaySettings(player);
        NeedsOfNature.syncLiquidState(player);
    }

    static void forceAccessorySync(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        LAST_SYNCED_EFFECTS.remove(player.getUuid());
        NonTrinketsCompat.syncAccessoryStateIfChanged(player);
    }

    static void clearAccessoryState(UUID playerUuid) {
        if (playerUuid != null) {
            LAST_SYNCED_EFFECTS.remove(playerUuid);
            PENDING_UNEQUIPPED_SLOTS.remove(playerUuid);
        }
    }

    static List<Identifier> resolveEquippedSkinOverlays(LivingEntity entity) {
        if (entity == null) {
            return List.of();
        }
        return TrinketsApi.getTrinketComponent((LivingEntity)entity).map(component -> {
            ArrayList<Identifier> overlays = new ArrayList<Identifier>();
            for (Pair<SlotReference, ItemStack> pair : component.getAllEquipped()) {
                Identifier overlay;
                if (pair.getRight() == null || ((ItemStack)pair.getRight()).isEmpty()) continue;
                Identifier itemId = Registries.ITEM.getId(pair.getRight().getItem());
                if (NonAccessoryShedSystem.isSlotVisuallyShed(entity.getUuid(), slotId(pair.getLeft()), itemId) || (overlay = NonAccessoryItemRegistry.getSkinOverlays(itemId).forSlot(slotId(pair.getLeft()))) == null) continue;
                overlays.add(overlay);
            }
            return List.copyOf(overlays);
        }).orElse(List.of());
    }

    static List<ItemStack> getVisuallyShedAccessoryStacks(LivingEntity entity) {
        if (entity == null) {
            return List.of();
        }
        return TrinketsApi.getTrinketComponent((LivingEntity)entity).map(component -> {
            ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
            for (Pair<SlotReference, ItemStack> pair : component.getAllEquipped()) {
                if (pair.getRight() == null || ((ItemStack)pair.getRight()).isEmpty()) continue;
                Identifier itemId = Registries.ITEM.getId(pair.getRight().getItem());
                if (!NonAccessoryShedSystem.isSlotVisuallyShed(entity.getUuid(), slotId(pair.getLeft()), itemId)) continue;
                stacks.add((ItemStack)pair.getRight());
            }
            return List.copyOf(stacks);
        }).orElse(List.of());
    }

    static NonTrinketsIntegration.TankScopeStatus resolveTankScopeStatus(LivingEntity entity, ItemStack stack) {
        if (entity == null || stack == null || stack.isEmpty()) {
            return NonTrinketsIntegration.TankScopeStatus.NOT_EQUIPPED;
        }
        return TrinketsApi.getTrinketComponent((LivingEntity)entity).map(component -> {
            for (Pair<SlotReference, ItemStack> pair : component.getAllEquipped()) {
                if (pair.getRight() != stack) continue;
                return NonTrinketsIntegration.isActiveTankSlot(entity, slotId(pair.getLeft())) ? NonTrinketsIntegration.TankScopeStatus.ACTIVE_EQUIPPED : NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED;
            }
            return NonTrinketsIntegration.TankScopeStatus.NOT_EQUIPPED;
        }).orElse(NonTrinketsIntegration.TankScopeStatus.NOT_EQUIPPED);
    }

    private static void markPendingUnequippedSlot(LivingEntity entity, SlotReference slot) {
        String slotId = NonTrinketsCompat.slotId(slot);
        if (entity == null || slotId == null) {
            return;
        }
        PENDING_UNEQUIPPED_SLOTS.computeIfAbsent(entity.getUuid(), ignored -> ConcurrentHashMap.newKeySet()).add(slotId);
    }

    private static void clearPendingUnequippedSlot(LivingEntity entity, SlotReference slot) {
        String slotId = NonTrinketsCompat.slotId(slot);
        if (entity == null || slotId == null) {
            return;
        }
        Set<String> slots = PENDING_UNEQUIPPED_SLOTS.get(entity.getUuid());
        if (slots == null) {
            return;
        }
        slots.remove(slotId);
        if (slots.isEmpty()) {
            PENDING_UNEQUIPPED_SLOTS.remove(entity.getUuid());
        }
    }

    private static boolean isPendingUnequippedSlot(LivingEntity entity, SlotReference slot) {
        String slotId = NonTrinketsCompat.slotId(slot);
        if (entity == null || slotId == null) {
            return false;
        }
        Set<String> slots = PENDING_UNEQUIPPED_SLOTS.get(entity.getUuid());
        return slots != null && slots.contains(slotId);
    }

    static String slotId(SlotReference slot) {
        if (slot == null || slot.inventory() == null || slot.inventory().getSlotType() == null) {
            return null;
        }
        return slot.inventory().getSlotType().getGroup() + "/" + slot.inventory().getSlotType().getName();
    }
}

