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
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityRenderer.class})
public abstract class EntityRendererDebugNameMixin {
    @Inject(method={"hasLabel"}, at={@At(value="HEAD")}, cancellable=true)
    private void needsOfNature$showDebugLabel(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (NonDebugNameHelper.shouldShowDebugLabel(entity)) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;getDisplayName()Lnet/minecraft/text/Text;"))
    private Text needsOfNature$debugDisplayName(Entity entity) {
        if (!NonDebugNameHelper.shouldShowDebugLabel(entity)) {
            return entity.getDisplayName();
        }
        if (!(entity instanceof EnergyHolder)) {
            return entity.getDisplayName();
        }
        EnergyHolder holder = (EnergyHolder)entity;
        String energy = holder.getEnergy() + "/" + holder.getMaxEnergy();
        return Text.literal("T:" + energy + " " + NonDebugNameHelper.resolveGenderSymbol(entity));
    }
}

