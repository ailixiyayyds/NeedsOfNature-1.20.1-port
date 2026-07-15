/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1058
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_2400
 *  net.minecraft.class_3532
 *  net.minecraft.class_3940
 *  net.minecraft.class_3940$class_11941
 *  net.minecraft.class_3940$class_8981
 *  net.minecraft.class_4002
 *  net.minecraft.class_5819
 *  net.minecraft.class_638
 *  net.minecraft.class_703
 *  net.minecraft.class_707
 */
package com.nonid.client.particle;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.client.NonHudOverlay;
import com.nonid.client.NonPuddleSizing;
import net.minecraft.class_1058;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2400;
import net.minecraft.class_3532;
import net.minecraft.class_3940;
import net.minecraft.class_4002;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_703;
import net.minecraft.class_707;

public class LiquidPuddleParticle
extends class_3940 {
    private static final int DEFAULT_MAX_AGE_TICKS = 800;
    private static final int MAX_FADE_TICKS = 200;
    private static final class_3940.class_8981 PUDDLE_ROTATOR = (quaternion, camera, tickDelta) -> quaternion.rotationXYZ(-1.5707964f, 0.0f, 0.0f);
    private final int holdTicks;
    private final int fadeTicks;

    public LiquidPuddleParticle(class_638 world, double x, double y, double z, class_1058 sprite, float scaleFactor) {
        super(world, x, y, z, sprite);
        this.field_3847 = LiquidPuddleParticle.resolvePuddleMaxAgeTicks();
        this.fadeTicks = Math.min(200, this.field_3847);
        this.holdTicks = Math.max(0, this.field_3847 - this.fadeTicks);
        this.field_3844 = 0.0f;
        this.field_3852 = 0.0;
        this.field_3869 = 0.0;
        this.field_3850 = 0.0;
        float sizeJitter = class_3532.method_32750((class_5819)this.field_3840, (float)0.8f, (float)1.0f);
        this.field_17867 = 0.5f * sizeJitter * Math.max(1.0f, scaleFactor);
        this.field_62636 = 1.0f;
        this.applyTankColor();
    }

    public class_3940.class_8981 method_55245() {
        return PUDDLE_ROTATOR;
    }

    protected class_3940.class_11941 method_74255() {
        return class_3940.class_11941.field_62641;
    }

    public void method_3070() {
        this.field_3858 = this.field_3874;
        this.field_3838 = this.field_3854;
        this.field_3856 = this.field_3871;
        if (!this.hasSupportBlock()) {
            this.method_3085();
            return;
        }
        if (++this.field_3866 >= this.field_3847) {
            this.method_3085();
            return;
        }
        if (this.field_3866 > this.holdTicks && this.fadeTicks > 0) {
            float fade = (float)(this.field_3866 - this.holdTicks) / (float)this.fadeTicks;
            this.field_62636 = class_3532.method_15363((float)(1.0f - fade), (float)0.0f, (float)1.0f);
        } else {
            this.field_62636 = 1.0f;
        }
    }

    private static int resolvePuddleMaxAgeTicks() {
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return 800;
        }
        return Math.max(1, config.getLiquidPuddleDespawnSeconds() * 20);
    }

    private boolean hasSupportBlock() {
        class_2338 supportPos = class_2338.method_49637((double)this.field_3874, (double)(this.field_3854 - 0.05), (double)this.field_3871);
        return !this.field_3851.method_8320(supportPos).method_26220((class_1922)this.field_3851, supportPos).method_1110();
    }

    private void applyTankColor() {
        int rgb = NonHudOverlay.getLiquidTintRgb();
        float r = (float)(rgb >> 16 & 0xFF) / 255.0f;
        float g = (float)(rgb >> 8 & 0xFF) / 255.0f;
        float b = (float)(rgb & 0xFF) / 255.0f;
        this.method_74305(r, g, b);
    }

    public static final class Factory
    implements class_707<class_2400> {
        private final class_4002 spriteProvider;

        public Factory(class_4002 spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public class_703 createParticle(class_2400 type, class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_5819 random) {
            float scale = NonPuddleSizing.scaleForPosition(world, x, y, z);
            return new LiquidPuddleParticle(world, x, y, z, this.spriteProvider.method_18139(random), scale);
        }
    }
}

