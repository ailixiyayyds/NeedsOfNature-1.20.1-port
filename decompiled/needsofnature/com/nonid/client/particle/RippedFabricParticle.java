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

public class RippedFabricParticle
extends class_3940 {
    private static final int FRAMES = 5;
    private static final int TICKS_PER_FRAME = 4;
    private static class_4002 sprites;

    public RippedFabricParticle(class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_1058 sprite, int rgb, int maxAge, float scale) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, sprite);
        this.field_28786 = 0.82f;
        this.field_3844 = 0.1f;
        this.field_3847 = Math.max(1, maxAge);
        this.field_17867 = scale;
        this.field_3862 = true;
        this.method_3080(0.01f, 0.01f);
        this.method_74305((float)(rgb >> 16 & 0xFF) / 255.0f, (float)(rgb >> 8 & 0xFF) / 255.0f, (float)(rgb & 0xFF) / 255.0f);
    }

    public static class_703 create(class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int rgb, class_5819 random) {
        class_1058 sprite;
        class_4002 provider = sprites;
        class_1058 class_10582 = sprite = provider != null ? provider.method_74304() : null;
        if (sprite == null) {
            return null;
        }
        int maxAge = 100 + random.method_43048(50);
        float scale = 0.075f + random.method_43057() * 0.045f;
        return new RippedFabricParticle(world, x, y, z, velocityX, velocityY, velocityZ, sprite, rgb, maxAge, scale);
    }

    public class_3940.class_11941 method_74255() {
        return class_3940.class_11941.field_62640;
    }

    public void method_3070() {
        super.method_3070();
        if (this.field_3845) {
            this.method_3085();
        }
    }

    public float method_18132(float tickProgress) {
        float life = ((float)this.field_3866 + tickProgress) / (float)this.field_3847;
        float size = this.field_17867 * class_3532.method_15363((float)(life * 10.0f), (float)0.0f, (float)1.0f);
        if (life > 0.75f) {
            size *= class_3532.method_16439((float)class_3532.method_15363((float)((life - 0.75f) / 0.25f), (float)0.0f, (float)1.0f), (float)1.0f, (float)0.15f);
        }
        return size;
    }

    protected float method_18135() {
        float minV = this.field_62632.method_4593();
        float maxV = this.field_62632.method_4575();
        float frameSize = (maxV - minV) / 5.0f;
        int frame = this.field_3866 / 4 % 5;
        return minV + (float)frame * frameSize;
    }

    protected float method_18136() {
        float minV = this.field_62632.method_4593();
        float maxV = this.field_62632.method_4575();
        float frameSize = (maxV - minV) / 5.0f;
        int frame = this.field_3866 / 4 % 5;
        return minV + (float)(frame + 1) * frameSize;
    }

    public static final class Factory
    implements class_707<class_2400> {
        public Factory(class_4002 spriteProvider) {
            sprites = spriteProvider;
        }

        public class_703 createParticle(class_2400 type, class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_5819 random) {
            return RippedFabricParticle.create(world, x, y, z, velocityX, velocityY, velocityZ, 15920872, random);
        }
    }
}

