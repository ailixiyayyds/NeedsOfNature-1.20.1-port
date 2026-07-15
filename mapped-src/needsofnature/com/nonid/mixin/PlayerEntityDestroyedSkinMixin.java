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
    private void non$initDestroyedSkinTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(NON_DESTROYED_SKIN_STAGE, (Object)0);
        builder.add(NON_DESTROYED_SKIN_DAMAGE, (Object)0);
        builder.add(NON_MESS_V, (Object)0);
        builder.add(NON_MESS_A, (Object)0);
        builder.add(NON_MESS_M, (Object)0);
    }

    @Override
    public int getDestroyedSkinStage() {
        PlayerEntity self = (PlayerEntity)this;
        return (Integer)self.getDataTracker().get(NON_DESTROYED_SKIN_STAGE);
    }

    @Override
    public void setDestroyedSkinStage(int stage) {
        PlayerEntity self = (PlayerEntity)this;
        self.getDataTracker().set(NON_DESTROYED_SKIN_STAGE, (Object)Math.max(0, Math.min(4, stage)));
    }

    @Override
    public int getDestroyedSkinDamage() {
        PlayerEntity self = (PlayerEntity)this;
        return (Integer)self.getDataTracker().get(NON_DESTROYED_SKIN_DAMAGE);
    }

    @Override
    public void setDestroyedSkinDamage(int damage) {
        PlayerEntity self = (PlayerEntity)this;
        self.getDataTracker().set(NON_DESTROYED_SKIN_DAMAGE, (Object)Math.max(0, Math.min(10, damage)));
    }

    @Override
    public int getVMess() {
        PlayerEntity self = (PlayerEntity)this;
        return (Integer)self.getDataTracker().get(NON_MESS_V);
    }

    @Override
    public void setVMess(int value) {
        PlayerEntity self = (PlayerEntity)this;
        self.getDataTracker().set(NON_MESS_V, (Object)PlayerEntityDestroyedSkinMixin.clampMess(value));
    }

    @Override
    public int getAMess() {
        PlayerEntity self = (PlayerEntity)this;
        return (Integer)self.getDataTracker().get(NON_MESS_A);
    }

    @Override
    public void setAMess(int value) {
        PlayerEntity self = (PlayerEntity)this;
        self.getDataTracker().set(NON_MESS_A, (Object)PlayerEntityDestroyedSkinMixin.clampMess(value));
    }

    @Override
    public int getMMess() {
        PlayerEntity self = (PlayerEntity)this;
        return (Integer)self.getDataTracker().get(NON_MESS_M);
    }

    @Override
    public void setMMess(int value) {
        PlayerEntity self = (PlayerEntity)this;
        self.getDataTracker().set(NON_MESS_M, (Object)PlayerEntityDestroyedSkinMixin.clampMess(value));
    }

    @Unique
    private static int clampMess(int value) {
        return Math.max(0, Math.min(10, value));
    }
}

