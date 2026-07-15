/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 */
package com.nonid;

import com.nonid.NonAccessoryEffects;
import com.nonid.NonGenderSystem;
import com.nonid.NonTrinketsCompat;
import com.nonid.data.NonAccessoryBehavior;
import com.nonid.data.NonLiquidRoles;
import java.util.List;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;

public final class NonTrinketsIntegration {
    private static final String TRINKETS_MOD_ID = "trinkets";
    private static final boolean TRINKETS_LOADED = FabricLoader.getInstance().isModLoaded("trinkets");

    private NonTrinketsIntegration() {
    }

    public static boolean isTrinketsLoaded() {
        return TRINKETS_LOADED;
    }

    public static void registerPredicates() {
        if (TRINKETS_LOADED) {
            NonTrinketsCompat.registerPredicates();
        }
    }

    public static void registerTrinketItem(Item item) {
        if (TRINKETS_LOADED) {
            NonTrinketsCompat.registerTrinketItem(item);
        }
    }

    public static boolean isSlotAvailableFor(LivingEntity entity, String slotId) {
        if (entity == null || slotId == null) {
            return false;
        }
        if (NonAccessoryBehavior.isSlot(slotId, "legs/v")) {
            return NonGenderSystem.hasFemaleGender((Entity)entity);
        }
        if (NonAccessoryBehavior.isSlot(slotId, "legs/d")) {
            return NonGenderSystem.hasMaleGender((Entity)entity);
        }
        return true;
    }

    public static NonAccessoryEffects getAccessoryEffects(LivingEntity entity) {
        return NonTrinketsIntegration.getGlobalAccessoryEffects(entity);
    }

    public static NonAccessoryEffects getGlobalAccessoryEffects(LivingEntity entity) {
        if (!TRINKETS_LOADED || entity == null) {
            return NonAccessoryEffects.NEUTRAL;
        }
        return NonTrinketsCompat.getGlobalAccessoryEffects(entity);
    }

    public static NonAccessoryEffects getActiveTankAccessoryEffects(LivingEntity entity) {
        if (!TRINKETS_LOADED || entity == null) {
            return NonAccessoryEffects.NEUTRAL;
        }
        return NonTrinketsCompat.getActiveTankAccessoryEffects(entity);
    }

    public static String getActiveTankSlotId(LivingEntity entity) {
        return NonGenderSystem.getLiquidTankType((Entity)entity).trinketsSlotId();
    }

    public static boolean isActiveTankSlot(LivingEntity entity, String slotId) {
        if (entity == null || slotId == null) {
            return false;
        }
        return NonAccessoryBehavior.isSlot(slotId, NonTrinketsIntegration.getActiveTankSlotId(entity));
    }

    public static boolean blocksPregnancy(ServerPlayerEntity player) {
        if (!TRINKETS_LOADED || player == null) {
            return false;
        }
        return NonTrinketsCompat.blocksPregnancy(player);
    }

    public static boolean hasProtectorFor(ServerPlayerEntity player, NonLiquidRoles.InjectorRole role) {
        if (!TRINKETS_LOADED || player == null || role == null || role == NonLiquidRoles.InjectorRole.NONE) {
            return false;
        }
        return NonTrinketsCompat.hasProtectorFor(player, role);
    }

    public static boolean consumeProtectorFor(ServerWorld world, ServerPlayerEntity player, NonLiquidRoles.InjectorRole role) {
        if (!TRINKETS_LOADED || world == null || player == null || role == null || role == NonLiquidRoles.InjectorRole.NONE) {
            return false;
        }
        return NonTrinketsCompat.consumeProtectorFor(world, player, role);
    }

    public static void applyInjectionDurabilityCost(ServerWorld world, ServerPlayerEntity player, NonLiquidRoles.InjectorRole role) {
        if (!TRINKETS_LOADED || world == null || player == null || role == null) {
            return;
        }
        NonTrinketsCompat.applyInjectionDurabilityCost(world, player, role);
    }

    public static void syncAccessoryStateIfChanged(ServerPlayerEntity player) {
        if (!TRINKETS_LOADED || player == null) {
            return;
        }
        NonTrinketsCompat.syncAccessoryStateIfChanged(player);
    }

    public static void forceAccessorySync(ServerPlayerEntity player) {
        if (!TRINKETS_LOADED || player == null) {
            return;
        }
        NonTrinketsCompat.forceAccessorySync(player);
    }

    public static void clearAccessoryState(UUID playerUuid) {
        if (!TRINKETS_LOADED || playerUuid == null) {
            return;
        }
        NonTrinketsCompat.clearAccessoryState(playerUuid);
    }

    public static List<Identifier> resolveEquippedSkinOverlays(LivingEntity entity) {
        if (!TRINKETS_LOADED || entity == null) {
            return List.of();
        }
        return NonTrinketsCompat.resolveEquippedSkinOverlays(entity);
    }

    public static List<ItemStack> getVisuallyShedAccessoryStacks(LivingEntity entity) {
        if (!TRINKETS_LOADED || entity == null) {
            return List.of();
        }
        return NonTrinketsCompat.getVisuallyShedAccessoryStacks(entity);
    }

    public static TankScopeStatus resolveTankScopeStatus(LivingEntity entity, ItemStack stack) {
        if (!TRINKETS_LOADED || entity == null || stack == null || stack.isEmpty()) {
            return TankScopeStatus.NOT_EQUIPPED;
        }
        return NonTrinketsCompat.resolveTankScopeStatus(entity, stack);
    }

    public static enum TankScopeStatus {
        NOT_EQUIPPED,
        ACTIVE_EQUIPPED,
        INACTIVE_EQUIPPED;

    }
}

