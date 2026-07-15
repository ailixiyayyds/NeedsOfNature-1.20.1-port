/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.play.PickItemFromBlockC2SPacket
 *  net.minecraft.network.packet.c2s.play.PickItemFromEntityC2SPacket
 *  net.minecraft.network.packet.c2s.play.ButtonClickC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.server.network.ServerPlayNetworkHandler
 *  net.minecraft.network.packet.c2s.play.SlotChangedStateC2SPacket
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin;

import com.afwid.server.AfwServerAnimationController;
import net.minecraft.network.packet.c2s.play.PickItemFromBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PickItemFromEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.ButtonClickC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.SlotChangedStateC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ServerPlayNetworkHandler.class})
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Unique
    private boolean afw$shouldBlock() {
        if (this.player == null) {
            return false;
        }
        ServerWorld class_32182 = this.player.getEntityWorld();
        if (!(class_32182 instanceof ServerWorld)) {
            return false;
        }
        ServerWorld world = class_32182;
        return AfwServerAnimationController.isActorActive(world, this.player.getUuid());
    }

    @Inject(method={"onPlayerAction"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onPlayerInteractBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onPlayerInteractItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockInteractItem(PlayerInteractItemC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onPlayerInteractEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockInteractEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onClickSlot"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockClickSlot(ClickSlotC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onButtonClick"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockButtonClick(ButtonClickC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onCreativeInventoryAction"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockCreative(CreativeInventoryActionC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onSlotChangedState"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockSlotState(SlotChangedStateC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onUpdateSelectedSlot"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockSelectedSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onPickItemFromBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockPickFromBlock(PickItemFromBlockC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"onPickItemFromEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockPickFromEntity(PickItemFromEntityC2SPacket packet, CallbackInfo ci) {
        if (this.afw$shouldBlock()) {
            ci.cancel();
        }
    }
}

