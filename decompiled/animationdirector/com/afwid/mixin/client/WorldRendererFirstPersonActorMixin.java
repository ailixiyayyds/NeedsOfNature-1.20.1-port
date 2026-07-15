/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_310
 *  net.minecraft.class_4184
 *  net.minecraft.class_761
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.afwid.mixin.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={class_761.class})
public class WorldRendererFirstPersonActorMixin {
    @Redirect(method={"method_72917"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_4184;method_19333()Z"))
    private boolean afw$keepFocusedActorRenderableInFirstPerson(class_4184 camera) {
        boolean thirdPerson = camera.method_19333();
        if (thirdPerson) {
            return true;
        }
        class_1297 class_12972 = camera.method_19331();
        if (!(class_12972 instanceof class_1657)) {
            return false;
        }
        class_1657 player = (class_1657)class_12972;
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return false;
        }
        if (!player.method_5667().equals(client.field_1724.method_5667())) {
            return false;
        }
        return AfwClientAnimationRuntime.isActorPendingOrActive(player.method_5667());
    }
}

