/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10042
 *  net.minecraft.class_1309
 *  net.minecraft.class_3532
 *  net.minecraft.class_7923
 *  net.minecraft.class_922
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.afwid.mixin.client;

import com.afwid.client.render.AfwRenderStateAccess;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.class_10042;
import net.minecraft.class_1309;
import net.minecraft.class_3532;
import net.minecraft.class_7923;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_922.class})
public abstract class LivingEntityRendererMixin<T extends class_1309, S extends class_10042> {
    @Inject(method={"method_62355"}, at={@At(value="TAIL")})
    private void afw$attachUuidAndType(T entity, S state, float tickDelta, CallbackInfo ci) {
        AfwClientAnimationRuntime.LockedOrientation locked;
        AfwRenderStateAccess access = (AfwRenderStateAccess)state;
        access.afw$setEntityUuid(entity.method_5667());
        access.afw$setEntityTypeId(class_7923.field_41177.method_10221((Object)entity.method_5864()));
        if (AfwClientAnimationRuntime.hasActiveInstances() && (locked = AfwClientAnimationRuntime.getLockedOrientation(entity.method_5667())) != null) {
            ((class_10042)state).field_53446 = locked.bodyYaw();
            ((class_10042)state).field_53447 = class_3532.method_15393((float)(locked.headYaw() - locked.bodyYaw()));
            ((class_10042)state).field_53448 = locked.pitch();
        }
    }
}

