/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_2338
 *  net.minecraft.class_2394
 *  net.minecraft.class_2400
 *  net.minecraft.class_3414
 *  net.minecraft.class_3419
 *  net.minecraft.class_3486
 *  net.minecraft.class_3532
 *  net.minecraft.class_3611
 *  net.minecraft.class_3612
 *  net.minecraft.class_4002
 *  net.minecraft.class_5819
 *  net.minecraft.class_638
 *  net.minecraft.class_703
 *  net.minecraft.class_707
 *  net.minecraft.class_7923
 */
package com.nonid.client.particle;

import com.nonid.NeedsOfNature;
import com.nonid.client.particle.LiquidLeakParticle;
import com.nonid.particle.NonParticles;
import net.minecraft.class_1058;
import net.minecraft.class_2338;
import net.minecraft.class_2394;
import net.minecraft.class_2400;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_3611;
import net.minecraft.class_3612;
import net.minecraft.class_4002;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_703;
import net.minecraft.class_707;
import net.minecraft.class_7923;

public class LiquidFallingParticle
extends LiquidLeakParticle {
    public LiquidFallingParticle(class_638 world, double x, double y, double z, class_3611 fluid, class_2394 nextParticle, class_1058 sprite) {
        super(world, x, y, z, fluid, sprite);
        this.field_3847 = (int)(64.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    protected void updateVelocity() {
        class_2338 pos = class_2338.method_49637((double)this.field_3874, (double)this.field_3854, (double)this.field_3871);
        if (this.field_3851.method_8316(pos).method_15767(class_3486.field_15517)) {
            this.method_3085();
            double offsetX = (this.field_3840.method_43058() - 0.5) * 0.2;
            double offsetZ = (this.field_3840.method_43058() - 0.5) * 0.2;
            double offsetY = class_3532.method_32750((class_5819)this.field_3840, (float)0.01f, (float)0.04f);
            this.field_3851.method_8406((class_2394)NonParticles.LIQUID_PARTICLE_WATER, this.field_3874 + offsetX, this.field_3854 + offsetY, this.field_3871 + offsetZ, this.field_3852 * 0.02, this.field_3869 * 0.02, this.field_3850 * 0.02);
            return;
        }
        if (this.field_3845) {
            class_3414 sound = (class_3414)class_7923.field_41172.method_63535(NeedsOfNature.id("wet04"));
            float pitch = class_3532.method_32750((class_5819)this.field_3840, (float)0.3f, (float)1.0f);
            this.field_3851.method_8486(this.field_3874, this.field_3854, this.field_3871, sound, class_3419.field_15245, 0.6f, pitch, false);
            this.method_3085();
            double offsetX = (this.field_3840.method_43058() - 0.5) * 0.3;
            double offsetZ = (this.field_3840.method_43058() - 0.5) * 0.3;
            double offsetY = class_3532.method_32750((class_5819)this.field_3840, (float)0.005f, (float)0.02f);
            this.field_3851.method_8406((class_2394)NonParticles.LIQUID_PARTICLE_PUDDLE, this.field_3874 + offsetX, this.field_3854 + offsetY, this.field_3871 + offsetZ, 0.0, 0.0, 0.0);
        }
    }

    public static final class Factory
    implements class_707<class_2400> {
        private final class_4002 spriteProvider;

        public Factory(class_4002 spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public class_703 createParticle(class_2400 type, class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_5819 random) {
            return new LiquidFallingParticle(world, x, y, z, (class_3611)class_3612.field_15908, (class_2394)NonParticles.LIQUID_PARTICLE_PUDDLE, this.spriteProvider.method_18139(random));
        }
    }
}

