/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_2394
 *  net.minecraft.class_2400
 *  net.minecraft.class_3611
 *  net.minecraft.class_3612
 *  net.minecraft.class_4002
 *  net.minecraft.class_5819
 *  net.minecraft.class_638
 *  net.minecraft.class_703
 *  net.minecraft.class_707
 */
package com.nonid.client.particle;

import com.nonid.client.particle.LiquidLeakParticle;
import com.nonid.particle.NonParticles;
import net.minecraft.class_1058;
import net.minecraft.class_2394;
import net.minecraft.class_2400;
import net.minecraft.class_3611;
import net.minecraft.class_3612;
import net.minecraft.class_4002;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_703;
import net.minecraft.class_707;

public class LiquidParticle
extends LiquidLeakParticle {
    private static final float GRAVITY_SCALE = 0.02f;
    private static final int MAX_AGE_TICKS = 20;
    private static final double VELOCITY_SCALE = 0.02;
    private final class_2394 nextParticle;

    public LiquidParticle(class_638 world, double x, double y, double z, class_3611 fluid, class_2394 nextParticle, class_1058 sprite) {
        super(world, x, y, z, fluid, sprite);
        this.nextParticle = nextParticle;
        this.field_3844 *= 0.02f;
        this.field_3847 = 20;
    }

    @Override
    protected void updateAge() {
        if (--this.field_3847 <= 0) {
            this.method_3085();
            this.field_3851.method_8406(this.nextParticle, this.field_3874, this.field_3854, this.field_3871, this.field_3852, this.field_3869, this.field_3850);
        }
    }

    @Override
    protected void updateVelocity() {
        this.field_3852 *= 0.02;
        this.field_3869 *= 0.02;
        this.field_3850 *= 0.02;
    }

    public static final class Factory
    implements class_707<class_2400> {
        private final class_4002 spriteProvider;

        public Factory(class_4002 spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public class_703 createParticle(class_2400 type, class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_5819 random) {
            return new LiquidParticle(world, x, y, z, (class_3611)class_3612.field_15908, (class_2394)NonParticles.LIQUID_PARTICLE_FALLING, this.spriteProvider.method_18139(random));
        }
    }
}

