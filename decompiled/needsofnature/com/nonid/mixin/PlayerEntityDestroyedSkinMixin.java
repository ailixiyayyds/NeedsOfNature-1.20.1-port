/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_2940
 *  net.minecraft.class_2941
 *  net.minecraft.class_2943
 *  net.minecraft.class_2945
 *  net.minecraft.class_2945$class_9222
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.DestroyedSkinHolder;
import com.nonid.MessHolder;
import net.minecraft.class_1657;
import net.minecraft.class_2940;
import net.minecraft.class_2941;
import net.minecraft.class_2943;
import net.minecraft.class_2945;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1657.class})
public abstract class PlayerEntityDestroyedSkinMixin
implements DestroyedSkinHolder,
MessHolder {
    @Unique
    private static final class_2940<Integer> NON_DESTROYED_SKIN_STAGE = class_2945.method_12791(class_1657.class, (class_2941)class_2943.field_13327);
    @Unique
    private static final class_2940<Integer> NON_DESTROYED_SKIN_DAMAGE = class_2945.method_12791(class_1657.class, (class_2941)class_2943.field_13327);
    @Unique
    private static final class_2940<Integer> NON_MESS_V = class_2945.method_12791(class_1657.class, (class_2941)class_2943.field_13327);
    @Unique
    private static final class_2940<Integer> NON_MESS_A = class_2945.method_12791(class_1657.class, (class_2941)class_2943.field_13327);
    @Unique
    private static final class_2940<Integer> NON_MESS_M = class_2945.method_12791(class_1657.class, (class_2941)class_2943.field_13327);

    @Inject(method={"method_5693"}, at={@At(value="TAIL")})
    private void non$initDestroyedSkinTracker(class_2945.class_9222 builder, CallbackInfo ci) {
        builder.method_56912(NON_DESTROYED_SKIN_STAGE, (Object)0);
        builder.method_56912(NON_DESTROYED_SKIN_DAMAGE, (Object)0);
        builder.method_56912(NON_MESS_V, (Object)0);
        builder.method_56912(NON_MESS_A, (Object)0);
        builder.method_56912(NON_MESS_M, (Object)0);
    }

    @Override
    public int getDestroyedSkinStage() {
        class_1657 self = (class_1657)this;
        return (Integer)self.method_5841().method_12789(NON_DESTROYED_SKIN_STAGE);
    }

    @Override
    public void setDestroyedSkinStage(int stage) {
        class_1657 self = (class_1657)this;
        self.method_5841().method_12778(NON_DESTROYED_SKIN_STAGE, (Object)Math.max(0, Math.min(4, stage)));
    }

    @Override
    public int getDestroyedSkinDamage() {
        class_1657 self = (class_1657)this;
        return (Integer)self.method_5841().method_12789(NON_DESTROYED_SKIN_DAMAGE);
    }

    @Override
    public void setDestroyedSkinDamage(int damage) {
        class_1657 self = (class_1657)this;
        self.method_5841().method_12778(NON_DESTROYED_SKIN_DAMAGE, (Object)Math.max(0, Math.min(10, damage)));
    }

    @Override
    public int getVMess() {
        class_1657 self = (class_1657)this;
        return (Integer)self.method_5841().method_12789(NON_MESS_V);
    }

    @Override
    public void setVMess(int value) {
        class_1657 self = (class_1657)this;
        self.method_5841().method_12778(NON_MESS_V, (Object)PlayerEntityDestroyedSkinMixin.clampMess(value));
    }

    @Override
    public int getAMess() {
        class_1657 self = (class_1657)this;
        return (Integer)self.method_5841().method_12789(NON_MESS_A);
    }

    @Override
    public void setAMess(int value) {
        class_1657 self = (class_1657)this;
        self.method_5841().method_12778(NON_MESS_A, (Object)PlayerEntityDestroyedSkinMixin.clampMess(value));
    }

    @Override
    public int getMMess() {
        class_1657 self = (class_1657)this;
        return (Integer)self.method_5841().method_12789(NON_MESS_M);
    }

    @Override
    public void setMMess(int value) {
        class_1657 self = (class_1657)this;
        self.method_5841().method_12778(NON_MESS_M, (Object)PlayerEntityDestroyedSkinMixin.clampMess(value));
    }

    @Unique
    private static int clampMess(int value) {
        return Math.max(0, Math.min(10, value));
    }
}

