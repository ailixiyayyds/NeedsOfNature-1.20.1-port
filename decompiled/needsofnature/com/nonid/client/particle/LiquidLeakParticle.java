/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_3610
 *  net.minecraft.class_3611
 *  net.minecraft.class_3612
 *  net.minecraft.class_3940
 *  net.minecraft.class_3940$class_11941
 *  net.minecraft.class_638
 */
package com.nonid.client.particle;

import com.nonid.client.NonHudOverlay;
import net.minecraft.class_1058;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_3610;
import net.minecraft.class_3611;
import net.minecraft.class_3612;
import net.minecraft.class_3940;
import net.minecraft.class_638;

abstract class LiquidLeakParticle
extends class_3940 {
    protected static final float BASE_GRAVITY = 0.06f;
    protected static final double DAMPING = 0.98;
    protected final class_3611 fluid;

    protected LiquidLeakParticle(class_638 world, double x, double y, double z, class_3611 fluid, class_1058 sprite) {
        super(world, x, y, z, sprite);
        this.fluid = fluid;
        this.method_3080(0.01f, 0.01f);
        this.field_3844 = 0.06f;
        this.applyTankColor();
    }

    public class_3940.class_11941 method_74255() {
        return class_3940.class_11941.field_62640;
    }

    public void method_3070() {
        double surface;
        this.field_3858 = this.field_3874;
        this.field_3838 = this.field_3854;
        this.field_3856 = this.field_3871;
        this.updateAge();
        if (this.field_3843) {
            return;
        }
        this.field_3869 -= (double)this.field_3844;
        this.method_3069(this.field_3852, this.field_3869, this.field_3850);
        this.updateVelocity();
        if (this.field_3843) {
            return;
        }
        this.field_3852 *= 0.98;
        this.field_3869 *= 0.98;
        this.field_3850 *= 0.98;
        if (this.fluid == class_3612.field_15906) {
            return;
        }
        class_2338 pos = class_2338.method_49637((double)this.field_3874, (double)this.field_3854, (double)this.field_3871);
        class_3610 fluidState = this.field_3851.method_8316(pos);
        if (fluidState.method_15772() == this.fluid && this.field_3854 < (surface = (double)((float)pos.method_10264() + fluidState.method_15763((class_1922)this.field_3851, pos)))) {
            this.method_3085();
        }
    }

    protected void updateAge() {
        if (--this.field_3847 <= 0) {
            this.method_3085();
        }
    }

    protected void updateVelocity() {
    }

    private void applyTankColor() {
        int rgb = NonHudOverlay.getLiquidTintRgb();
        float r = (float)(rgb >> 16 & 0xFF) / 255.0f;
        float g = (float)(rgb >> 8 & 0xFF) / 255.0f;
        float b = (float)(rgb & 0xFF) / 255.0f;
        this.method_74305(r, g, b);
    }
}

