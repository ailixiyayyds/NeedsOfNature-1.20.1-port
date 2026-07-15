package com.afwid.mixin.client;

import com.afwid.client.render.gecko.AfwGeckoReplacedRender;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderManagerMixin {
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private <E extends Entity> void afw$swapRenderer(
            EntityRenderer<? super E> vanillaRenderer,
            E entity,
            float entityYaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertices,
            int packedLight) {
        if (entity instanceof LivingEntity living
                && AfwClientAnimationRuntime.hasActiveInstances()
                && AfwClientAnimationRuntime.isActorActive(entity.getUuid())
                && AfwGeckoReplacedRender.render(
                        living, entityYaw, tickDelta, matrices, vertices, packedLight)) {
            return;
        }
        vanillaRenderer.render(entity, entityYaw, tickDelta, matrices, vertices, packedLight);
    }
}
