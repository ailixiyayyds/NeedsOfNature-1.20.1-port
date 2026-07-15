/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.EntityRenderState
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.text.Text
 *  net.minecraft.client.render.entity.EntityRenderer
 *  net.minecraft.entity.EntityAttachmentType
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin.client;

import com.nonid.EnergyHolder;
import com.nonid.client.NonDebugNameHelper;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityAttachmentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityRenderer.class})
public abstract class EntityRendererDebugNameMixin {
    @Inject(method={"hasLabel"}, at={@At(value="HEAD")}, cancellable=true)
    private void needsOfNature$showDebugLabel(Entity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        if (NonDebugNameHelper.shouldShowDebugLabel(entity)) {
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(method={"getDisplayName"}, at={@At(value="HEAD")}, cancellable=true)
    private void needsOfNature$debugDisplayName(Entity entity, CallbackInfoReturnable<Text> cir) {
        if (!NonDebugNameHelper.shouldShowDebugLabel(entity)) {
            return;
        }
        if (!(entity instanceof EnergyHolder)) {
            return;
        }
        EnergyHolder holder = (EnergyHolder)entity;
        String energy = holder.getEnergy() + "/" + holder.getMaxEnergy();
        cir.setReturnValue((Object)Text.literal((String)("T:" + energy + " " + NonDebugNameHelper.resolveGenderSymbol(entity))));
    }

    @Inject(method={"updateRenderState"}, at={@At(value="TAIL")})
    private void needsOfNature$debugLabelState(Entity entity, EntityRenderState state, float tickDelta, CallbackInfo ci) {
        if (!NonDebugNameHelper.shouldShowDebugLabel(entity)) {
            return;
        }
        if (!(entity instanceof EnergyHolder)) {
            return;
        }
        EnergyHolder holder = (EnergyHolder)entity;
        String energy = holder.getEnergy() + "/" + holder.getMaxEnergy();
        state.displayName = Text.literal((String)("T:" + energy + " " + NonDebugNameHelper.resolveGenderSymbol(entity)));
        Vec3d labelPos = entity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, entity.getLerpedYaw(tickDelta));
        if (labelPos == null) {
            labelPos = new Vec3d(entity.getX(), entity.getY() + (double)entity.getHeight() + 0.5, entity.getZ());
        }
        state.nameLabelPos = labelPos;
    }
}

