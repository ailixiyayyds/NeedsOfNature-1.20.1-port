/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.texture.Sprite
 *  net.minecraft.world.BlockView
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.particle.SimpleParticleType
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.particle.BillboardParticle
 *  net.minecraft.client.particle.BillboardParticle$RenderType
 *  net.minecraft.client.particle.BillboardParticle$Rotator
 *  net.minecraft.client.particle.SpriteProvider
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleFactory
 */
package com.nonid.client.particle;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.client.NonHudOverlay;
import com.nonid.client.NonPuddleSizing;
import net.minecraft.client.texture.Sprite;
import net.minecraft.world.BlockView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;

public class LiquidPuddleParticle
extends BillboardParticle {
    private static final int DEFAULT_MAX_AGE_TICKS = 800;
    private static final int MAX_FADE_TICKS = 200;
    private static final BillboardParticle.Rotator PUDDLE_ROTATOR = (quaternion, camera, tickDelta) -> quaternion.rotationXYZ(-1.5707964f, 0.0f, 0.0f);
    private final int holdTicks;
    private final int fadeTicks;

    public LiquidPuddleParticle(ClientWorld world, double x, double y, double z, Sprite sprite, float scaleFactor) {
        super(world, x, y, z, sprite);
        this.maxAge = LiquidPuddleParticle.resolvePuddleMaxAgeTicks();
        this.fadeTicks = Math.min(200, this.maxAge);
        this.holdTicks = Math.max(0, this.maxAge - this.fadeTicks);
        this.gravityStrength = 0.0f;
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.velocityZ = 0.0;
        float sizeJitter = MathHelper.nextBetween((Random)this.random, (float)0.8f, (float)1.0f);
        this.scale = 0.5f * sizeJitter * Math.max(1.0f, scaleFactor);
        this.alpha = 1.0f;
        this.applyTankColor();
    }

    public BillboardParticle.Rotator getRotator() {
        return PUDDLE_ROTATOR;
    }

    protected BillboardParticle.RenderType getRenderType() {
        return BillboardParticle.RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }

    public void tick() {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        if (!this.hasSupportBlock()) {
            this.markDead();
            return;
        }
        if (++this.age >= this.maxAge) {
            this.markDead();
            return;
        }
        if (this.age > this.holdTicks && this.fadeTicks > 0) {
            float fade = (float)(this.age - this.holdTicks) / (float)this.fadeTicks;
            this.alpha = MathHelper.clamp((float)(1.0f - fade), (float)0.0f, (float)1.0f);
        } else {
            this.alpha = 1.0f;
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
        BlockPos supportPos = BlockPos.ofFloored((double)this.x, (double)(this.y - 0.05), (double)this.z);
        return !this.world.getBlockState(supportPos).getCollisionShape((BlockView)this.world, supportPos).isEmpty();
    }

    private void applyTankColor() {
        int rgb = NonHudOverlay.getLiquidTintRgb();
        float r = (float)(rgb >> 16 & 0xFF) / 255.0f;
        float g = (float)(rgb >> 8 & 0xFF) / 255.0f;
        float b = (float)(rgb & 0xFF) / 255.0f;
        this.setColor(r, g, b);
    }

    public static final class Factory
    implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            float scale = NonPuddleSizing.scaleForPosition(world, x, y, z);
            return new LiquidPuddleParticle(world, x, y, z, this.spriteProvider.getSprite(random), scale);
        }
    }
}

