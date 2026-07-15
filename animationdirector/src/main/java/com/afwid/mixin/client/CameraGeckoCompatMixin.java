/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.render.Camera
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 */
package com.afwid.mixin.client;

import com.afwid.client.camera.AfwCameraPosAccess;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={Camera.class})
public abstract class CameraGeckoCompatMixin
implements AfwCameraPosAccess {
    @Shadow
    private Vec3d pos;

    @Unique
    public Vec3d method_19326() {
        return this.pos;
    }

    @Override
    @Unique
    public Vec3d afw$getPos() {
        return this.pos;
    }
}

