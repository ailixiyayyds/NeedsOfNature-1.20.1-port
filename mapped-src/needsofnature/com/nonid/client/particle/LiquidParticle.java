/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.texture.Sprite
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.particle.SimpleParticleType
 *  net.minecraft.fluid.Fluid
 *  net.minecraft.fluid.Fluids
 *  net.minecraft.client.particle.SpriteProvider
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleFactory
 */
package com.nonid.client.particle;

import com.nonid.client.particle.LiquidLeakParticle;
import com.nonid.particle.NonParticles;
import net.minecraft.client.texture.Sprite;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;

public class LiquidParticle
extends LiquidLeakParticle {
    private static final float GRAVITY_SCALE = 0.02f;
    private static final int MAX_AGE_TICKS = 20;
    private static final double VELOCITY_SCALE = 0.02;
    private final ParticleEffect nextParticle;

    public LiquidParticle(ClientWorld world, double x, double y, double z, Fluid fluid, ParticleEffect nextParticle, Sprite sprite) {
        super(world, x, y, z, fluid, sprite);
        this.nextParticle = nextParticle;
        this.gravityStrength *= 0.02f;
        this.maxAge = 20;
    }

    @Override
    protected void updateAge() {
        if (--this.maxAge <= 0) {
            this.markDead();
            this.world.addParticleClient(this.nextParticle, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
        }
    }

    @Override
    protected void updateVelocity() {
        this.velocityX *= 0.02;
        this.velocityY *= 0.02;
        this.velocityZ *= 0.02;
    }

    public static final class Factory
    implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new LiquidParticle(world, x, y, z, (Fluid)Fluids.LAVA, (ParticleEffect)NonParticles.LIQUID_PARTICLE_FALLING, this.spriteProvider.getSprite(random));
        }
    }
}

