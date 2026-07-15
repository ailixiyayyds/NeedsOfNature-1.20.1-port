/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_2400
 *  net.minecraft.class_3612
 *  net.minecraft.class_3940$class_11941
 *  net.minecraft.class_4002
 *  net.minecraft.class_5819
 *  net.minecraft.class_638
 *  net.minecraft.class_703
 *  net.minecraft.class_707
 */
package com.nonid.client.particle;

import com.nonid.client.particle.LiquidLeakParticle;
import net.minecraft.class_1058;
import net.minecraft.class_2400;
import net.minecraft.class_3612;
import net.minecraft.class_3940;
import net.minecraft.class_4002;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_703;
import net.minecraft.class_707;

public class LiquidWaterParticle
extends LiquidLeakParticle {
    private static final int WATER_LIFE_TICKS = 100;
    private static final float WATER_GRAVITY = 0.0018f;
    private static final double WATER_DAMPING = 0.6;
    private static final int WATER_FRAMES = 5;

    public LiquidWaterParticle(class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_1058 sprite, int maxAge) {
        super(world, x, y, z, class_3612.field_15906, sprite);
        this.field_3847 = Math.max(1, maxAge);
        this.field_3844 = 0.0018f;
        this.field_3852 = velocityX;
        this.field_3869 = velocityY;
        this.field_3850 = velocityZ;
    }

    @Override
    public class_3940.class_11941 method_74255() {
        return class_3940.class_11941.field_62640;
    }

    @Override
    public void method_3070() {
        this.field_3858 = this.field_3874;
        this.field_3838 = this.field_3854;
        this.field_3856 = this.field_3871;
        if (this.field_3866++ >= this.field_3847) {
            this.method_3085();
            return;
        }
        this.field_3869 -= (double)this.field_3844;
        this.method_3069(this.field_3852, this.field_3869, this.field_3850);
        this.field_3852 *= 0.6;
        this.field_3869 *= 0.6;
        this.field_3850 *= 0.6;
    }

    protected float method_18135() {
        float minV = this.field_62632.method_4593();
        float maxV = this.field_62632.method_4575();
        float frameSize = (maxV - minV) / 5.0f;
        int frame = Math.min(4, (int)((float)this.field_3866 / (float)this.field_3847 * 5.0f));
        return minV + (float)frame * frameSize;
    }

    protected float method_18136() {
        float minV = this.field_62632.method_4593();
        float maxV = this.field_62632.method_4575();
        float frameSize = (maxV - minV) / 5.0f;
        int frame = Math.min(4, (int)((float)this.field_3866 / (float)this.field_3847 * 5.0f));
        return minV + (float)(frame + 1) * frameSize;
    }

    public static final class Factory
    implements class_707<class_2400> {
        private final class_4002 spriteProvider;

        public Factory(class_4002 spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public class_703 createParticle(class_2400 type, class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_5819 random) {
            int variance = random.method_43048(21) - 10;
            int age = 100 + variance;
            return new LiquidWaterParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider.method_18139(random), age);
        }
    }
}

