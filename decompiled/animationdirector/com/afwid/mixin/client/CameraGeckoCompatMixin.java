/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 *  net.minecraft.class_4184
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 */
package com.afwid.mixin.client;

import com.afwid.client.camera.AfwCameraPosAccess;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={class_4184.class})
public abstract class CameraGeckoCompatMixin
implements AfwCameraPosAccess {
    @Shadow
    private class_243 field_18712;

    @Unique
    public class_243 method_19326() {
        return this.field_18712;
    }

    @Override
    @Unique
    public class_243 afw$getPos() {
        return this.field_18712;
    }
}

