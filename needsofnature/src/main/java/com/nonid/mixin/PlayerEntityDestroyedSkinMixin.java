/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.data.TrackedData
 *  net.minecraft.entity.data.TrackedDataHandler
 *  net.minecraft.entity.data.TrackedDataHandlerRegistry
 *  net.minecraft.entity.data.DataTracker
 *  net.minecraft.entity.data.DataTracker$Builder
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.DestroyedSkinHolder;
import com.nonid.MessHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerEntity.class})
public abstract class PlayerEntityDestroyedSkinMixin
implements DestroyedSkinHolder,
MessHolder {
    @Unique
    private static final TrackedData<Integer> NON_DESTROYED_SKIN_STAGE = DataTracker.registerData(PlayerEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> NON_DESTROYED_SKIN_DAMAGE = DataTracker.registerData(PlayerEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> NON_MESS_V = DataTracker.registerData(PlayerEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> NON_MESS_A = DataTracker.registerData(PlayerEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> NON_MESS_M = DataTracker.registerData(PlayerEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.INTEGER);

    @Inject(method={"initDataTracker"}, at={@At(value="TAIL")})
    private void non$initDestroyedSkinTracker(CallbackInfo ci) {
        DataTracker tracker = this.non$getDataTracker();
        tracker.startTracking(NON_DESTROYED_SKIN_STAGE, 0);
        tracker.startTracking(NON_DESTROYED_SKIN_DAMAGE, 0);
        tracker.startTracking(NON_MESS_V, 0);
        tracker.startTracking(NON_MESS_A, 0);
        tracker.startTracking(NON_MESS_M, 0);
    }

    @Unique
    private DataTracker non$getDataTracker() {
        return ((PlayerEntity)(Object)this).getDataTracker();
    }

    @Override
    public int getDestroyedSkinStage() {
        return this.non$getDataTracker().get(NON_DESTROYED_SKIN_STAGE);
    }

    @Override
    public void setDestroyedSkinStage(int stage) {
        this.non$getDataTracker().set(NON_DESTROYED_SKIN_STAGE, Math.max(0, Math.min(4, stage)));
    }

    @Override
    public int getDestroyedSkinDamage() {
        return this.non$getDataTracker().get(NON_DESTROYED_SKIN_DAMAGE);
    }

    @Override
    public void setDestroyedSkinDamage(int damage) {
        this.non$getDataTracker().set(NON_DESTROYED_SKIN_DAMAGE, Math.max(0, Math.min(10, damage)));
    }

    @Override
    public int getVMess() {
        return this.non$getDataTracker().get(NON_MESS_V);
    }

    @Override
    public void setVMess(int value) {
        this.non$getDataTracker().set(NON_MESS_V, PlayerEntityDestroyedSkinMixin.clampMess(value));
    }

    @Override
    public int getAMess() {
        return this.non$getDataTracker().get(NON_MESS_A);
    }

    @Override
    public void setAMess(int value) {
        this.non$getDataTracker().set(NON_MESS_A, PlayerEntityDestroyedSkinMixin.clampMess(value));
    }

    @Override
    public int getMMess() {
        return this.non$getDataTracker().get(NON_MESS_M);
    }

    @Override
    public void setMMess(int value) {
        this.non$getDataTracker().set(NON_MESS_M, PlayerEntityDestroyedSkinMixin.clampMess(value));
    }

    @Unique
    private static int clampMess(int value) {
        return Math.max(0, Math.min(10, value));
    }
}

