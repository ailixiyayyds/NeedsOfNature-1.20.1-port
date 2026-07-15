/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_2960
 *  net.minecraft.class_3222
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.api;

import com.nonid.NonApiInternals;
import com.nonid.api.NonAccessoryEffectsSnapshot;
import com.nonid.api.NonChangeSource;
import com.nonid.api.NonDestroyedSkinSnapshot;
import com.nonid.api.NonGender;
import com.nonid.api.NonLiquidTankSnapshot;
import com.nonid.api.NonMessSnapshot;
import com.nonid.api.NonMessType;
import com.nonid.api.NonPlayerStateSnapshot;
import com.nonid.api.NonPregnancySnapshot;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import org.jetbrains.annotations.Nullable;

public final class NonPlayerApi {
    private NonPlayerApi() {
    }

    public static NonPlayerStateSnapshot getState(class_1657 player) {
        return NonApiInternals.snapshotPlayerState(player);
    }

    public static NonGender getGender(class_1657 player) {
        return NonApiInternals.getGender(player);
    }

    public static boolean setGender(class_3222 player, NonGender gender, NonChangeSource source) {
        return NonApiInternals.setGender(player, gender, source);
    }

    public static boolean canChangeGender(class_3222 player, NonChangeSource source) {
        return NonApiInternals.canChangeGender(player, source);
    }

    public static NonLiquidTankSnapshot getLiquidTank(class_1657 player) {
        return NonApiInternals.snapshotLiquid(player);
    }

    public static int addLiquid(class_3222 player, @Nullable class_2960 sourceEntityType, int amountMl) {
        return NonApiInternals.addLiquid(player, sourceEntityType, amountMl);
    }

    public static int drainLiquid(class_3222 player, int amountMl) {
        return NonApiInternals.drainLiquid(player, amountMl);
    }

    public static NonMessSnapshot getMess(class_1657 player) {
        return NonApiInternals.snapshotMess(player);
    }

    public static boolean addMess(class_3222 player, NonMessType type, int amount) {
        return NonApiInternals.addMess(player, type, amount);
    }

    public static boolean cleanMess(class_3222 player, NonMessType type, int amount) {
        return NonApiInternals.cleanMess(player, type, amount);
    }

    public static boolean clearMess(class_3222 player) {
        return NonApiInternals.clearMess(player);
    }

    public static NonDestroyedSkinSnapshot getDestroyedSkin(class_1657 player) {
        return NonApiInternals.snapshotDestroyedSkin(player);
    }

    public static boolean damageDestroyedSkin(class_3222 player, int amount) {
        return NonApiInternals.damageDestroyedSkin(player, amount);
    }

    public static boolean repairDestroyedSkin(class_3222 player, int amount) {
        return NonApiInternals.repairDestroyedSkin(player, amount);
    }

    public static NonPregnancySnapshot getPregnancy(class_1657 player) {
        return NonApiInternals.snapshotPregnancy(player);
    }

    public static boolean beginPregnancy(class_3222 player, class_2960 entityTypeId, long endTick) {
        return NonApiInternals.beginPregnancy(player, entityTypeId, endTick);
    }

    public static boolean clearPregnancy(class_3222 player) {
        return NonApiInternals.clearPregnancy(player);
    }

    public static NonAccessoryEffectsSnapshot getAccessoryEffects(class_1657 player) {
        return NonApiInternals.snapshotAccessoryEffects(player);
    }

    public static boolean isMessSystemEnabled() {
        return NonApiInternals.isMessSystemEnabled();
    }

    public static boolean isDestroyedSkinSystemEnabled() {
        return NonApiInternals.isDestroyedSkinSystemEnabled();
    }

    public static boolean isLiquidTankEnabled() {
        return NonApiInternals.isLiquidTankEnabled();
    }

    public static boolean isPregnancyEnabled() {
        return NonApiInternals.isPregnancyEnabled();
    }
}

