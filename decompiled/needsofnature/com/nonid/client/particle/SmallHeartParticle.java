/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_2400
 *  net.minecraft.class_3532
 *  net.minecraft.class_3940
 *  net.minecraft.class_3940$class_11941
 *  net.minecraft.class_4002
 *  net.minecraft.class_5819
 *  net.minecraft.class_638
 *  net.minecraft.class_703
 *  net.minecraft.class_707
 */
package com.nonid.client.particle;

import net.minecraft.class_1058;
import net.minecraft.class_2400;
import net.minecraft.class_3532;
import net.minecraft.class_3940;
import net.minecraft.class_4002;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_703;
import net.minecraft.class_707;

public class SmallHeartParticle
extends class_3940 {
    protected SmallHeartParticle(class_638 world, double x, double y, double z, class_1058 sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0, sprite);
        this.field_28787 = true;
        this.field_28786 = 0.86f;
        this.field_3852 *= (double)0.01f;
        this.field_3869 *= (double)0.01f;
        this.field_3850 *= (double)0.01f;
        this.field_3869 += 0.1;
        this.field_17867 = 0.145f + this.field_3840.method_43057() * 0.08f;
        this.field_3847 = 16;
        this.field_3862 = false;
    }

    public class_3940.class_11941 method_74255() {
        return class_3940.class_11941.field_62640;
    }

    public float method_18132(float tickProgress) {
        float life = ((float)this.field_3866 + tickProgress) / (float)this.field_3847;
        float base = this.field_17867 * class_3532.method_15363((float)(life * 32.0f), (float)0.0f, (float)1.0f);
        if (life > 0.8f) {
            float fade = class_3532.method_15363((float)((life - 0.8f) / 0.2f), (float)0.0f, (float)1.0f);
            base *= class_3532.method_16439((float)fade, (float)1.0f, (float)0.8f);
        }
        return base;
    }

    public static final class Factory
    implements class_707<class_2400> {
        private final class_4002 spriteProvider;

        public Factory(class_4002 spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public class_703 createParticle(class_2400 type, class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_5819 random) {
            return new SmallHeartParticle(world, x, y, z, this.spriteProvider.method_18139(random));
        }
    }
}

