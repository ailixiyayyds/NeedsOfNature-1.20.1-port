/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10298
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1799
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_310
 *  net.minecraft.class_3965
 *  net.minecraft.class_3966
 *  net.minecraft.class_636
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.class_10298;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_636.class})
public abstract class ClientPlayerInteractionManagerMixin {
    @Unique
    private static boolean shouldBlock() {
        class_746 player;
        class_310 client = class_310.method_1551();
        class_746 class_7462 = player = client == null ? null : client.field_1724;
        if (player == null) {
            return false;
        }
        return AfwClientAnimationRuntime.isActorActive(player.method_5667());
    }

    @Inject(method={"method_2899"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockBreak(class_2338 pos, CallbackInfoReturnable<Boolean> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"method_2910"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockAttack(class_2338 pos, class_2350 direction, CallbackInfoReturnable<Boolean> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"method_2902"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockBreakProgress(class_2338 pos, class_2350 direction, CallbackInfoReturnable<Boolean> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"method_2896"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$interactBlock(class_746 player, class_1268 hand, class_3965 hit, CallbackInfoReturnable<class_1269> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)class_1269.field_5814);
        }
    }

    @Inject(method={"method_2919"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$interactItem(class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)class_1269.field_5814);
        }
    }

    @Inject(method={"method_2905"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$interactEntity(class_1657 player, class_1297 entity, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)class_1269.field_5814);
        }
    }

    @Inject(method={"method_2917"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$interactEntityAt(class_1657 player, class_1297 entity, class_3966 hit, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)class_1269.field_5814);
        }
    }

    @Inject(method={"method_2918"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$attackEntity(class_1657 player, class_1297 target, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_2906"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$clickSlot(int syncId, int slotId, int button, class_1713 actionType, class_1657 player, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_2912"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$clickRecipe(int syncId, class_10298 recipeId, boolean craftAll, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_2900"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$clickButton(int syncId, int buttonId, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_2909"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$clickCreative(class_1799 stack, int slotId, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_2915"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$dropCreative(class_1799 stack, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_2897"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$stopUsingItem(class_1657 player, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_65193"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$pickItemFromBlock(class_2338 pos, boolean includeData, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_2916"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$pickItemFromEntity(class_1297 entity, boolean includeData, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_54634"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$slotChangedState(int syncId, int revision, boolean newState, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }
}

