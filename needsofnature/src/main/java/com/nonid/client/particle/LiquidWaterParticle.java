/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.texture.Sprite
 *  net.minecraft.particle.SimpleParticleType
 *  net.minecraft.fluid.Fluids
 *  net.minecraft.client.particle.BillboardParticle$RenderType
 *  net.minecraft.client.particle.SpriteProvider
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleFactory
 */
package com.nonid.client.particle;

import com.nonid.client.particle.LiquidLeakParticle;
import net.minecraft.client.texture.Sprite;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.fluid.Fluids;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;

public class LiquidWaterParticle
extends LiquidLeakParticle {
    private static final int WATER_LIFE_TICKS = 100;
    private static final float WATER_GRAVITY = 0.0018f;
    private static final double WATER_DAMPING = 0.6;
    private static final int WATER_FRAMES = 5;

    public LiquidWaterParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Sprite sprite, int maxAge) {
        super(world, x, y, z, Fluids.EMPTY, sprite);
        this.maxAge = Math.max(1, maxAge);
        this.gravityStrength = 0.0018f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }
        this.velocityY -= (double)this.gravityStrength;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= 0.6;
        this.velocityY *= 0.6;
        this.velocityZ *= 0.6;
    }

    protected float getMinV() {
        float minV = this.sprite.getMinV();
        float maxV = this.sprite.getMaxV();
        float frameSize = (maxV - minV) / 5.0f;
        int frame = Math.min(4, (int)((float)this.age / (float)this.maxAge * 5.0f));
        return minV + (float)frame * frameSize;
    }

    protected float getMaxV() {
        float minV = this.sprite.getMinV();
        float maxV = this.sprite.getMaxV();
        float frameSize = (maxV - minV) / 5.0f;
        int frame = Math.min(4, (int)((float)this.age / (float)this.maxAge * 5.0f));
        return minV + (float)(frame + 1) * frameSize;
    }

    public static final class Factory
    implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            Random random = world.getRandom();
            int variance = random.nextInt(21) - 10;
            int age = 100 + variance;
            return new LiquidWaterParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider.getSprite(random), age);
        }
    }
}

