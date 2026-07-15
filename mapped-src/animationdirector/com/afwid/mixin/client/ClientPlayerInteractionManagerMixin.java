/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.recipe.NetworkRecipeId
 *  net.minecraft.util.Hand
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.hit.EntityHitResult
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientPlayerInteractionManager.class})
public abstract class ClientPlayerInteractionManagerMixin {
    @Unique
    private static boolean shouldBlock() {
        ClientPlayerEntity player;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity class_7462 = player = client == null ? null : client.player;
        if (player == null) {
            return false;
        }
        return AfwClientAnimationRuntime.isActorActive(player.getUuid());
    }

    @Inject(method={"breakBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"attackBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockAttack(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"updateBlockBreakingProgress"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$blockBreakProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"interactBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)ActionResult.FAIL);
        }
    }

    @Inject(method={"interactItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)ActionResult.FAIL);
        }
    }

    @Inject(method={"interactEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$interactEntity(PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)ActionResult.FAIL);
        }
    }

    @Inject(method={"interactEntityAtLocation"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$interactEntityAt(PlayerEntity player, Entity entity, EntityHitResult hit, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            cir.setReturnValue((Object)ActionResult.FAIL);
        }
    }

    @Inject(method={"attackEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$attackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"clickSlot"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$clickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"clickRecipe"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$clickRecipe(int syncId, NetworkRecipeId recipeId, boolean craftAll, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"clickButton"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$clickButton(int syncId, int buttonId, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"clickCreativeStack"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$clickCreative(ItemStack stack, int slotId, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"dropCreativeStack"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$dropCreative(ItemStack stack, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"stopUsingItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$stopUsingItem(PlayerEntity player, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"pickItemFromBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$pickItemFromBlock(BlockPos pos, boolean includeData, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"pickItemFromEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$pickItemFromEntity(Entity entity, boolean includeData, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }

    @Inject(method={"slotChangedState"}, at={@At(value="HEAD")}, cancellable=true)
    private void afw$slotChangedState(int syncId, int revision, boolean newState, CallbackInfo ci) {
        if (ClientPlayerInteractionManagerMixin.shouldBlock()) {
            ci.cancel();
        }
    }
}

