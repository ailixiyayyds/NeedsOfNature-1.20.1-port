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

public class SmallHeartParticle
extends SpriteBillboardParticle {
    protected SmallHeartParticle(ClientWorld world, double x, double y, double z, Sprite sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.setSprite(sprite);
        this.ascending = true;
        this.velocityMultiplier = 0.86f;
        this.velocityX *= (double)0.01f;
        this.velocityY *= (double)0.01f;
        this.velocityZ *= (double)0.01f;
        this.velocityY += 0.1;
        this.scale = 0.145f + this.random.nextFloat() * 0.08f;
        this.maxAge = 16;
        this.collidesWithWorld = false;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public float getSize(float tickProgress) {
        float life = ((float)this.age + tickProgress) / (float)this.maxAge;
        float base = this.scale * MathHelper.clamp((float)(life * 32.0f), (float)0.0f, (float)1.0f);
        if (life > 0.8f) {
            float fade = MathHelper.clamp((float)((life - 0.8f) / 0.2f), (float)0.0f, (float)1.0f);
            base *= MathHelper.lerp((float)fade, (float)1.0f, (float)0.8f);
        }
        return base;
    }

    public static final class Factory
    implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SmallHeartParticle(world, x, y, z, this.spriteProvider.getSprite(world.getRandom()));
        }
    }
}

