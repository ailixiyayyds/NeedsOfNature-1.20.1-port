/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.network.ServerPlayerEntity
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public final class NonPlayerApi {
    private NonPlayerApi() {
    }

    public static NonPlayerStateSnapshot getState(PlayerEntity player) {
        return NonApiInternals.snapshotPlayerState(player);
    }

    public static NonGender getGender(PlayerEntity player) {
        return NonApiInternals.getGender(player);
    }

    public static boolean setGender(ServerPlayerEntity player, NonGender gender, NonChangeSource source) {
        return NonApiInternals.setGender(player, gender, source);
    }

    public static boolean canChangeGender(ServerPlayerEntity player, NonChangeSource source) {
        return NonApiInternals.canChangeGender(player, source);
    }

    public static NonLiquidTankSnapshot getLiquidTank(PlayerEntity player) {
        return NonApiInternals.snapshotLiquid(player);
    }

    public static int addLiquid(ServerPlayerEntity player, @Nullable Identifier sourceEntityType, int amountMl) {
        return NonApiInternals.addLiquid(player, sourceEntityType, amountMl);
    }

    public static int drainLiquid(ServerPlayerEntity player, int amountMl) {
        return NonApiInternals.drainLiquid(player, amountMl);
    }

    public static NonMessSnapshot getMess(PlayerEntity player) {
        return NonApiInternals.snapshotMess(player);
    }

    public static boolean addMess(ServerPlayerEntity player, NonMessType type, int amount) {
        return NonApiInternals.addMess(player, type, amount);
    }

    public static boolean cleanMess(ServerPlayerEntity player, NonMessType type, int amount) {
        return NonApiInternals.cleanMess(player, type, amount);
    }

    public static boolean clearMess(ServerPlayerEntity player) {
        return NonApiInternals.clearMess(player);
    }

    public static NonDestroyedSkinSnapshot getDestroyedSkin(PlayerEntity player) {
        return NonApiInternals.snapshotDestroyedSkin(player);
    }

    public static boolean damageDestroyedSkin(ServerPlayerEntity player, int amount) {
        return NonApiInternals.damageDestroyedSkin(player, amount);
    }

    public static boolean repairDestroyedSkin(ServerPlayerEntity player, int amount) {
        return NonApiInternals.repairDestroyedSkin(player, amount);
    }

    public static NonPregnancySnapshot getPregnancy(PlayerEntity player) {
        return NonApiInternals.snapshotPregnancy(player);
    }

    public static boolean beginPregnancy(ServerPlayerEntity player, Identifier entityTypeId, long endTick) {
        return NonApiInternals.beginPregnancy(player, entityTypeId, endTick);
    }

    public static boolean clearPregnancy(ServerPlayerEntity player) {
        return NonApiInternals.clearPregnancy(player);
    }

    public static NonAccessoryEffectsSnapshot getAccessoryEffects(PlayerEntity player) {
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

