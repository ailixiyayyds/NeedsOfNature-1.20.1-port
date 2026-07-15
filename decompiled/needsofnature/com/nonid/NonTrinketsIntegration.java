/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
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
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;

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

    public static void registerTrinketItem(class_1792 item) {
        if (TRINKETS_LOADED) {
            NonTrinketsCompat.registerTrinketItem(item);
        }
    }

    public static boolean isSlotAvailableFor(class_1309 entity, String slotId) {
        if (entity == null || slotId == null) {
            return false;
        }
        if (NonAccessoryBehavior.isSlot(slotId, "legs/v")) {
            return NonGenderSystem.hasFemaleGender((class_1297)entity);
        }
        if (NonAccessoryBehavior.isSlot(slotId, "legs/d")) {
            return NonGenderSystem.hasMaleGender((class_1297)entity);
        }
        return true;
    }

    public static NonAccessoryEffects getAccessoryEffects(class_1309 entity) {
        return NonTrinketsIntegration.getGlobalAccessoryEffects(entity);
    }

    public static NonAccessoryEffects getGlobalAccessoryEffects(class_1309 entity) {
        if (!TRINKETS_LOADED || entity == null) {
            return NonAccessoryEffects.NEUTRAL;
        }
        return NonTrinketsCompat.getGlobalAccessoryEffects(entity);
    }

    public static NonAccessoryEffects getActiveTankAccessoryEffects(class_1309 entity) {
        if (!TRINKETS_LOADED || entity == null) {
            return NonAccessoryEffects.NEUTRAL;
        }
        return NonTrinketsCompat.getActiveTankAccessoryEffects(entity);
    }

    public static String getActiveTankSlotId(class_1309 entity) {
        return NonGenderSystem.getLiquidTankType((class_1297)entity).trinketsSlotId();
    }

    public static boolean isActiveTankSlot(class_1309 entity, String slotId) {
        if (entity == null || slotId == null) {
            return false;
        }
        return NonAccessoryBehavior.isSlot(slotId, NonTrinketsIntegration.getActiveTankSlotId(entity));
    }

    public static boolean blocksPregnancy(class_3222 player) {
        if (!TRINKETS_LOADED || player == null) {
            return false;
        }
        return NonTrinketsCompat.blocksPregnancy(player);
    }

    public static boolean hasProtectorFor(class_3222 player, NonLiquidRoles.InjectorRole role) {
        if (!TRINKETS_LOADED || player == null || role == null || role == NonLiquidRoles.InjectorRole.NONE) {
            return false;
        }
        return NonTrinketsCompat.hasProtectorFor(player, role);
    }

    public static boolean consumeProtectorFor(class_3218 world, class_3222 player, NonLiquidRoles.InjectorRole role) {
        if (!TRINKETS_LOADED || world == null || player == null || role == null || role == NonLiquidRoles.InjectorRole.NONE) {
            return false;
        }
        return NonTrinketsCompat.consumeProtectorFor(world, player, role);
    }

    public static void applyInjectionDurabilityCost(class_3218 world, class_3222 player, NonLiquidRoles.InjectorRole role) {
        if (!TRINKETS_LOADED || world == null || player == null || role == null) {
            return;
        }
        NonTrinketsCompat.applyInjectionDurabilityCost(world, player, role);
    }

    public static void syncAccessoryStateIfChanged(class_3222 player) {
        if (!TRINKETS_LOADED || player == null) {
            return;
        }
        NonTrinketsCompat.syncAccessoryStateIfChanged(player);
    }

    public static void forceAccessorySync(class_3222 player) {
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

    public static List<class_2960> resolveEquippedSkinOverlays(class_1309 entity) {
        if (!TRINKETS_LOADED || entity == null) {
            return List.of();
        }
        return NonTrinketsCompat.resolveEquippedSkinOverlays(entity);
    }

    public static List<class_1799> getVisuallyShedAccessoryStacks(class_1309 entity) {
        if (!TRINKETS_LOADED || entity == null) {
            return List.of();
        }
        return NonTrinketsCompat.getVisuallyShedAccessoryStacks(entity);
    }

    public static TankScopeStatus resolveTankScopeStatus(class_1309 entity, class_1799 stack) {
        if (!TRINKETS_LOADED || entity == null || stack == null || stack.method_7960()) {
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

