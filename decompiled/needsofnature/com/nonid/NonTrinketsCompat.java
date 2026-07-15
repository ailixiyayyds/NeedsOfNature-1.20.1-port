/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.emi.trinkets.api.SlotReference
 *  dev.emi.trinkets.api.Trinket
 *  dev.emi.trinkets.api.TrinketComponent
 *  dev.emi.trinkets.api.TrinketsApi
 *  net.fabricmc.fabric.api.util.TriState
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_2394
 *  net.minecraft.class_2398
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 *  net.minecraft.class_3545
 *  net.minecraft.class_6880
 *  net.minecraft.class_7923
 *  net.minecraft.class_9334
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
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3545;
import net.minecraft.class_6880;
import net.minecraft.class_7923;
import net.minecraft.class_9334;

final class NonTrinketsCompat {
    private static final Map<UUID, NonAccessoryEffects> LAST_SYNCED_EFFECTS = new ConcurrentHashMap<UUID, NonAccessoryEffects>();
    private static final Map<UUID, Set<String>> PENDING_UNEQUIPPED_SLOTS = new ConcurrentHashMap<UUID, Set<String>>();

    private NonTrinketsCompat() {
    }

    static void registerPredicates() {
        TrinketsApi.registerTrinketPredicate((class_2960)NeedsOfNature.id("female"), (stack, slot, entity) -> NonAccessoryBehavior.isSlot(slot.getId(), "legs/v") && !NonGenderSystem.hasFemaleGender((class_1297)entity) ? TriState.FALSE : TriState.DEFAULT);
        TrinketsApi.registerTrinketPredicate((class_2960)NeedsOfNature.id("male"), (stack, slot, entity) -> NonAccessoryBehavior.isSlot(slot.getId(), "legs/d") && !NonGenderSystem.hasMaleGender((class_1297)entity) ? TriState.FALSE : TriState.DEFAULT);
    }

    static void registerTrinketItem(class_1792 item) {
        if (item != null) {
            TrinketsApi.registerTrinket((class_1792)item, (Trinket)NonDataAccessoryTrinket.INSTANCE);
        }
    }

    static NonAccessoryEffects getGlobalAccessoryEffects(class_1309 entity) {
        if (entity == null) {
            return NonAccessoryEffects.NEUTRAL;
        }
        return TrinketsApi.getTrinketComponent((class_1309)entity).map(TrinketComponent::getAllEquipped).orElse(List.of()).stream().filter(pair -> !NonTrinketsCompat.isPendingUnequippedSlot(entity, (SlotReference)pair.method_15442())).filter(pair -> {
            class_2960 itemId = class_7923.field_41178.method_10221((Object)((class_1799)pair.method_15441()).method_7909());
            return !NonAccessoryShedSystem.isSlotEffectSuppressed(entity.method_5667(), ((SlotReference)pair.method_15442()).getId(), itemId);
        }).map(pair -> class_7923.field_41178.method_10221((Object)((class_1799)pair.method_15441()).method_7909())).map(NonAccessoryDefinitions::getEffects).map(NonAccessoryEffects::globalOnly).reduce(NonAccessoryEffects.NEUTRAL, NonAccessoryEffects::combine);
    }

    static NonAccessoryEffects getActiveTankAccessoryEffects(class_1309 entity) {
        if (entity == null) {
            return NonAccessoryEffects.NEUTRAL;
        }
        String activeSlotId = NonTrinketsIntegration.getActiveTankSlotId(entity);
        return TrinketsApi.getTrinketComponent((class_1309)entity).map(TrinketComponent::getAllEquipped).orElse(List.of()).stream().filter(pair -> !NonTrinketsCompat.isPendingUnequippedSlot(entity, (SlotReference)pair.method_15442())).filter(pair -> NonAccessoryBehavior.isSlot(((SlotReference)pair.method_15442()).getId(), activeSlotId)).filter(pair -> {
            class_2960 itemId = class_7923.field_41178.method_10221((Object)((class_1799)pair.method_15441()).method_7909());
            return !NonAccessoryShedSystem.isSlotEffectSuppressed(entity.method_5667(), ((SlotReference)pair.method_15442()).getId(), itemId);
        }).map(pair -> class_7923.field_41178.method_10221((Object)((class_1799)pair.method_15441()).method_7909())).map(NonAccessoryDefinitions::getEffects).map(NonAccessoryEffects::tankScopedOnly).reduce(NonAccessoryEffects.NEUTRAL, NonAccessoryEffects::combine);
    }

    static boolean blocksPregnancy(class_3222 player) {
        if (player == null) {
            return false;
        }
        String activeSlotId = NonTrinketsIntegration.getActiveTankSlotId((class_1309)player);
        return TrinketsApi.getTrinketComponent((class_1309)player).map(TrinketComponent::getAllEquipped).orElse(List.of()).stream().filter(pair -> !NonTrinketsCompat.isPendingUnequippedSlot((class_1309)player, (SlotReference)pair.method_15442())).anyMatch(pair -> {
            class_1799 stack = (class_1799)pair.method_15441();
            if (stack == null || stack.method_7960()) {
                return false;
            }
            String slotId = ((SlotReference)pair.method_15442()).getId();
            if (!NonAccessoryBehavior.isSlot(slotId, activeSlotId)) {
                return false;
            }
            class_2960 itemId = class_7923.field_41178.method_10221((Object)stack.method_7909());
            if (NonAccessoryShedSystem.isSlotEffectSuppressed(player.method_5667(), slotId, itemId)) {
                return false;
            }
            return NonAccessoryDefinitions.getBehavior(itemId).blocksPregnancy();
        });
    }

    static boolean hasProtectorFor(class_3222 player, NonLiquidRoles.InjectorRole role) {
        if (player == null || role == null || role == NonLiquidRoles.InjectorRole.NONE) {
            return false;
        }
        return TrinketsApi.getTrinketComponent((class_1309)player).map(TrinketComponent::getAllEquipped).orElse(List.of()).stream().filter(pair -> !NonTrinketsCompat.isPendingUnequippedSlot((class_1309)player, (SlotReference)pair.method_15442())).anyMatch(pair -> {
            class_1799 stack = (class_1799)pair.method_15441();
            if (stack == null || stack.method_7960()) {
                return false;
            }
            return NonAccessoryDefinitions.getBehavior(class_7923.field_41178.method_10221((Object)stack.method_7909())).blocks(role);
        });
    }

    static boolean consumeProtectorFor(class_3218 world, class_3222 player, NonLiquidRoles.InjectorRole role) {
        if (world == null || player == null || role == null || role == NonLiquidRoles.InjectorRole.NONE) {
            return false;
        }
        TrinketComponent component = TrinketsApi.getTrinketComponent((class_1309)player).orElse(null);
        if (component == null) {
            return false;
        }
        for (class_3545 pair : component.getAllEquipped()) {
            NonAccessoryBehavior behavior;
            class_1799 stack;
            if (NonTrinketsCompat.isPendingUnequippedSlot((class_1309)player, (SlotReference)pair.method_15442()) || (stack = (class_1799)pair.method_15441()) == null || stack.method_7960() || !(behavior = NonAccessoryDefinitions.getBehavior(class_7923.field_41178.method_10221((Object)stack.method_7909()))).blocks(role)) continue;
            if (Math.max(stack.method_7936(), behavior.maxDurability()) <= 0) {
                return false;
            }
            int cost = Math.max(1, behavior.protectionDurabilityCost());
            NonTrinketsCompat.applyDurabilityCost(world, player, (SlotReference)pair.method_15442(), stack, cost);
            world.method_60511(null, player.method_23317(), player.method_23318(), player.method_23321(), (class_6880)class_3417.field_15150, class_3419.field_15248, 0.75f, 0.9f + world.method_8409().method_43057() * 0.2f);
            world.method_65096((class_2394)class_2398.field_11205, player.method_23317(), player.method_23323(0.55), player.method_23321(), 8, 0.25, 0.35, 0.25, 0.08);
            NonTrinketsCompat.forceAccessorySync(player);
            return true;
        }
        return false;
    }

    static void applyInjectionDurabilityCost(class_3218 world, class_3222 player, NonLiquidRoles.InjectorRole role) {
        if (world == null || player == null || role == null) {
            return;
        }
        if (role != NonLiquidRoles.InjectorRole.V && role != NonLiquidRoles.InjectorRole.A) {
            return;
        }
        TrinketComponent component = TrinketsApi.getTrinketComponent((class_1309)player).orElse(null);
        if (component == null) {
            return;
        }
        for (class_3545 pair : component.getAllEquipped()) {
            int cost;
            class_1799 stack;
            if (NonTrinketsCompat.isPendingUnequippedSlot((class_1309)player, (SlotReference)pair.method_15442()) || (stack = (class_1799)pair.method_15441()) == null || stack.method_7960()) continue;
            String slotId = ((SlotReference)pair.method_15442()).getId();
            if (role == NonLiquidRoles.InjectorRole.V && !NonAccessoryBehavior.isSlot(slotId, "legs/v") || role == NonLiquidRoles.InjectorRole.A && !NonAccessoryBehavior.isSlot(slotId, "legs/a")) continue;
            NonAccessoryBehavior behavior = NonAccessoryDefinitions.getBehavior(class_7923.field_41178.method_10221((Object)stack.method_7909()));
            int n = cost = role == NonLiquidRoles.InjectorRole.V ? behavior.vInjectionDurabilityCost() : behavior.aInjectionDurabilityCost();
            if (cost <= 0) continue;
            NonTrinketsCompat.applyDurabilityCost(world, player, (SlotReference)pair.method_15442(), stack, cost);
        }
        NonTrinketsCompat.forceAccessorySync(player);
    }

    private static boolean applyDurabilityCost(class_3218 world, class_3222 player, SlotReference slot, class_1799 stack, int cost) {
        int nextDamage;
        if (world == null || player == null || slot == null || stack == null || stack.method_7960() || cost <= 0) {
            return false;
        }
        NonAccessoryBehavior behavior = NonAccessoryDefinitions.getBehavior(class_7923.field_41178.method_10221((Object)stack.method_7909()));
        int maxDamage = Math.max(stack.method_7936(), behavior.maxDurability());
        if (maxDamage <= 0) {
            return false;
        }
        if (stack.method_7936() <= 0) {
            stack.method_57379(class_9334.field_50072, (Object)maxDamage);
        }
        if (stack.method_58694(class_9334.field_49629) == null) {
            stack.method_57379(class_9334.field_49629, (Object)0);
        }
        if ((nextDamage = stack.method_7919() + cost) >= maxDamage) {
            TrinketsApi.onTrinketBroken((class_1799)stack, (SlotReference)slot, (class_1309)player);
            stack.method_7934(1);
        } else {
            stack.method_7974(nextDamage);
        }
        slot.inventory().markUpdate();
        return true;
    }

    static void onAccessoryEquipped(class_1309 entity, SlotReference slot) {
        NonTrinketsCompat.clearPendingUnequippedSlot(entity, slot);
        if (entity instanceof class_3222) {
            class_3222 player = (class_3222)entity;
            NonTrinketsCompat.forceAccessorySync(player);
        }
    }

    static void onAccessoryUnequipped(class_1309 entity, SlotReference slot) {
        NonTrinketsCompat.markPendingUnequippedSlot(entity, slot);
        if (entity instanceof class_3222) {
            class_3222 player = (class_3222)entity;
            NonTrinketsCompat.forceAccessorySync(player);
        }
    }

    static void syncAccessoryStateIfChanged(class_3222 player) {
        NonAccessoryEffects previous;
        if (player == null) {
            return;
        }
        NonAccessoryEffects effects = NonTrinketsCompat.getGlobalAccessoryEffects((class_1309)player);
        if ((effects = effects.combine(NonTrinketsCompat.getActiveTankAccessoryEffects((class_1309)player))).equals(previous = LAST_SYNCED_EFFECTS.put(player.method_5667(), effects))) {
            return;
        }
        NeedsOfNature.syncRuntimeGameplaySettings(player);
        NeedsOfNature.syncLiquidState(player);
    }

    static void forceAccessorySync(class_3222 player) {
        if (player == null) {
            return;
        }
        LAST_SYNCED_EFFECTS.remove(player.method_5667());
        NonTrinketsCompat.syncAccessoryStateIfChanged(player);
    }

    static void clearAccessoryState(UUID playerUuid) {
        if (playerUuid != null) {
            LAST_SYNCED_EFFECTS.remove(playerUuid);
            PENDING_UNEQUIPPED_SLOTS.remove(playerUuid);
        }
    }

    static List<class_2960> resolveEquippedSkinOverlays(class_1309 entity) {
        if (entity == null) {
            return List.of();
        }
        return TrinketsApi.getTrinketComponent((class_1309)entity).map(component -> {
            ArrayList<class_2960> overlays = new ArrayList<class_2960>();
            for (class_3545 pair : component.getAllEquipped()) {
                class_2960 overlay;
                if (pair.method_15441() == null || ((class_1799)pair.method_15441()).method_7960()) continue;
                class_2960 itemId = class_7923.field_41178.method_10221((Object)((class_1799)pair.method_15441()).method_7909());
                if (NonAccessoryShedSystem.isSlotVisuallyShed(entity.method_5667(), ((SlotReference)pair.method_15442()).getId(), itemId) || (overlay = NonAccessoryItemRegistry.getSkinOverlays(itemId).forSlot(((SlotReference)pair.method_15442()).getId())) == null) continue;
                overlays.add(overlay);
            }
            return overlays.isEmpty() ? List.of() : List.copyOf(overlays);
        }).orElse(List.of());
    }

    static List<class_1799> getVisuallyShedAccessoryStacks(class_1309 entity) {
        if (entity == null) {
            return List.of();
        }
        return TrinketsApi.getTrinketComponent((class_1309)entity).map(component -> {
            ArrayList<class_1799> stacks = new ArrayList<class_1799>();
            for (class_3545 pair : component.getAllEquipped()) {
                if (pair.method_15441() == null || ((class_1799)pair.method_15441()).method_7960()) continue;
                class_2960 itemId = class_7923.field_41178.method_10221((Object)((class_1799)pair.method_15441()).method_7909());
                if (!NonAccessoryShedSystem.isSlotVisuallyShed(entity.method_5667(), ((SlotReference)pair.method_15442()).getId(), itemId)) continue;
                stacks.add((class_1799)pair.method_15441());
            }
            return stacks.isEmpty() ? List.of() : List.copyOf(stacks);
        }).orElse(List.of());
    }

    static NonTrinketsIntegration.TankScopeStatus resolveTankScopeStatus(class_1309 entity, class_1799 stack) {
        if (entity == null || stack == null || stack.method_7960()) {
            return NonTrinketsIntegration.TankScopeStatus.NOT_EQUIPPED;
        }
        return TrinketsApi.getTrinketComponent((class_1309)entity).map(component -> {
            for (class_3545 pair : component.getAllEquipped()) {
                if (pair.method_15441() != stack) continue;
                return NonTrinketsIntegration.isActiveTankSlot(entity, ((SlotReference)pair.method_15442()).getId()) ? NonTrinketsIntegration.TankScopeStatus.ACTIVE_EQUIPPED : NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED;
            }
            return NonTrinketsIntegration.TankScopeStatus.NOT_EQUIPPED;
        }).orElse(NonTrinketsIntegration.TankScopeStatus.NOT_EQUIPPED);
    }

    private static void markPendingUnequippedSlot(class_1309 entity, SlotReference slot) {
        String slotId = NonTrinketsCompat.slotId(slot);
        if (entity == null || slotId == null) {
            return;
        }
        PENDING_UNEQUIPPED_SLOTS.computeIfAbsent(entity.method_5667(), ignored -> ConcurrentHashMap.newKeySet()).add(slotId);
    }

    private static void clearPendingUnequippedSlot(class_1309 entity, SlotReference slot) {
        String slotId = NonTrinketsCompat.slotId(slot);
        if (entity == null || slotId == null) {
            return;
        }
        Set<String> slots = PENDING_UNEQUIPPED_SLOTS.get(entity.method_5667());
        if (slots == null) {
            return;
        }
        slots.remove(slotId);
        if (slots.isEmpty()) {
            PENDING_UNEQUIPPED_SLOTS.remove(entity.method_5667());
        }
    }

    private static boolean isPendingUnequippedSlot(class_1309 entity, SlotReference slot) {
        String slotId = NonTrinketsCompat.slotId(slot);
        if (entity == null || slotId == null) {
            return false;
        }
        Set<String> slots = PENDING_UNEQUIPPED_SLOTS.get(entity.method_5667());
        return slots != null && slots.contains(slotId);
    }

    private static String slotId(SlotReference slot) {
        return slot == null ? null : slot.getId();
    }
}

