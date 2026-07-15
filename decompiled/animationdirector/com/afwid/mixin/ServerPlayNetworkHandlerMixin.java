/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10370
 *  net.minecraft.class_10371
 *  net.minecraft.class_2811
 *  net.minecraft.class_2813
 *  net.minecraft.class_2824
 *  net.minecraft.class_2846
 *  net.minecraft.class_2868
 *  net.minecraft.class_2873
 *  net.minecraft.class_2885
 *  net.minecraft.class_2886
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_3244
 *  net.minecraft.class_8875
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.class_10370;
import net.minecraft.class_10371;
import net.minecraft.class_2811;
import net.minecraft.class_2813;
import net.minecraft.class_2824;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_2873;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3244;
import net.minecraft.class_8875;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_3244.class})
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public class_3222 field_14140;

    @Unique
    private boolean afw$shouldBlock() {
        if (this.field_14140 == null) {
            return false;
        }
        class_3218 class_32182 = this.field_14140.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            return false;
        }
        class_3218 world = class_32182;
        return AfwServerAnimationController.isActorActive(world, this.field_14140.method_5667());
    }

    @Inject(method={"method_12066"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockPlayerAction(class_2846 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_12046"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockInteractBlock(class_2885 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_12065"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockInteractItem(class_2886 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_12062"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockInteractEntity(class_2824 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_12076"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockClickSlot(class_2813 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_12055"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockButtonClick(class_2811 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_12070"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockCreative(class_2873 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_54436"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockSlotState(class_8875 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_12056"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockSelectedSlot(class_2868 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_65085"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockPickFromBlock(class_10370 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_12084"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockPickFromEntity(class_10371 packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }
}

