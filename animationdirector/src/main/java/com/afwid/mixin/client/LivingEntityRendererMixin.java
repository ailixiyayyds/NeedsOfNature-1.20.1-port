package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {
    @Unique
    private final ThreadLocal<OrientationSnapshot> afw$orientationSnapshot = new ThreadLocal<>();

    @Inject(method = "render", at = @At("HEAD"))
    private void afw$applyLockedOrientation(T entity, float entityYaw, float tickDelta,
                                            MatrixStack matrices, VertexConsumerProvider vertices,
                                            int packedLight, CallbackInfo ci) {
        AfwClientAnimationRuntime.LockedOrientation locked =
                AfwClientAnimationRuntime.getLockedOrientation(entity.getUuid());
        if (!AfwClientAnimationRuntime.hasActiveInstances() || locked == null) {
            return;
        }
        this.afw$orientationSnapshot.set(new OrientationSnapshot(
                entity.bodyYaw, entity.prevBodyYaw, entity.headYaw, entity.prevHeadYaw,
                entity.getPitch(), entity.prevPitch));
        entity.bodyYaw = locked.bodyYaw();
        entity.prevBodyYaw = locked.bodyYaw();
        entity.headYaw = locked.headYaw();
        entity.prevHeadYaw = locked.headYaw();
        entity.setPitch(locked.pitch());
        entity.prevPitch = locked.pitch();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void afw$restoreOrientation(T entity, float entityYaw, float tickDelta,
                                        MatrixStack matrices, VertexConsumerProvider vertices,
                                        int packedLight, CallbackInfo ci) {
        OrientationSnapshot snapshot = this.afw$orientationSnapshot.get();
        if (snapshot == null) {
            return;
        }
        this.afw$orientationSnapshot.remove();
        entity.bodyYaw = snapshot.bodyYaw();
        entity.prevBodyYaw = snapshot.prevBodyYaw();
        entity.headYaw = snapshot.headYaw();
        entity.prevHeadYaw = snapshot.prevHeadYaw();
        entity.setPitch(snapshot.pitch());
        entity.prevPitch = snapshot.prevPitch();
    }

    @Unique
    private record OrientationSnapshot(float bodyYaw, float prevBodyYaw,
                                       float headYaw, float prevHeadYaw,
                                       float pitch, float prevPitch) {
    }
}
