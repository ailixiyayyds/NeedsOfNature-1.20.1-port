/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.texture.Sprite
 *  net.minecraft.particle.SimpleParticleType
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.particle.BillboardParticle
 *  net.minecraft.client.particle.BillboardParticle$RenderType
 *  net.minecraft.client.particle.SpriteProvider
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleFactory
 */
package com.nonid.client.particle;

import net.minecraft.client.texture.Sprite;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;

public class RippedFabricParticle
extends SpriteBillboardParticle {
    private static final int FRAMES = 5;
    private static final int TICKS_PER_FRAME = 4;
    private static SpriteProvider sprites;

    public RippedFabricParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Sprite sprite, int rgb, int maxAge, float scale) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.setSprite(sprite);
        this.velocityMultiplier = 0.82f;
        this.gravityStrength = 0.1f;
        this.maxAge = Math.max(1, maxAge);
        this.scale = scale;
        this.collidesWithWorld = true;
        this.setBoundingBoxSpacing(0.01f, 0.01f);
        this.setColor((float)(rgb >> 16 & 0xFF) / 255.0f, (float)(rgb >> 8 & 0xFF) / 255.0f, (float)(rgb & 0xFF) / 255.0f);
    }

    public static Particle create(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int rgb, Random random) {
        Sprite sprite;
        SpriteProvider provider = sprites;
        sprite = provider != null ? provider.getSprite(0, 1) : null;
        if (sprite == null) {
            return null;
        }
        int maxAge = 100 + random.nextInt(50);
        float scale = 0.075f + random.nextFloat() * 0.045f;
        return new RippedFabricParticle(world, x, y, z, velocityX, velocityY, velocityZ, sprite, rgb, maxAge, scale);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        super.tick();
        if (this.onGround) {
            this.markDead();
        }
    }

    public float getSize(float tickProgress) {
        float life = ((float)this.age + tickProgress) / (float)this.maxAge;
        float size = this.scale * MathHelper.clamp((float)(life * 10.0f), (float)0.0f, (float)1.0f);
        if (life > 0.75f) {
            size *= MathHelper.lerp((float)MathHelper.clamp((float)((life - 0.75f) / 0.25f), (float)0.0f, (float)1.0f), (float)1.0f, (float)0.15f);
        }
        return size;
    }

    protected float getMinV() {
        float minV = this.sprite.getMinV();
        float maxV = this.sprite.getMaxV();
        float frameSize = (maxV - minV) / 5.0f;
        int frame = this.age / 4 % 5;
        return minV + (float)frame * frameSize;
    }

    protected float getMaxV() {
        float minV = this.sprite.getMinV();
        float maxV = this.sprite.getMaxV();
        float frameSize = (maxV - minV) / 5.0f;
        int frame = this.age / 4 % 5;
        return minV + (float)(frame + 1) * frameSize;
    }

    public static final class Factory
    implements ParticleFactory<DefaultParticleType> {
        public Factory(SpriteProvider spriteProvider) {
            sprites = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return RippedFabricParticle.create(world, x, y, z, velocityX, velocityY, velocityZ, 15920872, world.getRandom());
        }
    }
}

