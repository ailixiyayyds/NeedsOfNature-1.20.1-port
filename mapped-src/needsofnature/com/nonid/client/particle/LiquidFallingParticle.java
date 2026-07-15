/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.texture.Sprite
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.particle.SimpleParticleType
 *  net.minecraft.sound.SoundEvent
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.registry.tag.FluidTags
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.fluid.Fluid
 *  net.minecraft.fluid.Fluids
 *  net.minecraft.client.particle.SpriteProvider
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleFactory
 *  net.minecraft.registry.Registries
 */
package com.nonid.client.particle;

import com.nonid.NeedsOfNature;
import com.nonid.client.particle.LiquidLeakParticle;
import com.nonid.particle.NonParticles;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundCategory;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.registry.Registries;

public class LiquidFallingParticle
extends LiquidLeakParticle {
    public LiquidFallingParticle(ClientWorld world, double x, double y, double z, Fluid fluid, ParticleEffect nextParticle, Sprite sprite) {
        super(world, x, y, z, fluid, sprite);
        this.maxAge = (int)(64.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    protected void updateVelocity() {
        BlockPos pos = BlockPos.ofFloored((double)this.x, (double)this.y, (double)this.z);
        if (this.world.getFluidState(pos).isIn(FluidTags.WATER)) {
            this.markDead();
            double offsetX = (this.random.nextDouble() - 0.5) * 0.2;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.2;
            double offsetY = MathHelper.nextBetween((Random)this.random, (float)0.01f, (float)0.04f);
            this.world.addParticleClient((ParticleEffect)NonParticles.LIQUID_PARTICLE_WATER, this.x + offsetX, this.y + offsetY, this.z + offsetZ, this.velocityX * 0.02, this.velocityY * 0.02, this.velocityZ * 0.02);
            return;
        }
        if (this.onGround) {
            SoundEvent sound = (SoundEvent)Registries.SOUND_EVENT.get(NeedsOfNature.id("wet04"));
            float pitch = MathHelper.nextBetween((Random)this.random, (float)0.3f, (float)1.0f);
            this.world.playSoundClient(this.x, this.y, this.z, sound, SoundCategory.BLOCKS, 0.6f, pitch, false);
            this.markDead();
            double offsetX = (this.random.nextDouble() - 0.5) * 0.3;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.3;
            double offsetY = MathHelper.nextBetween((Random)this.random, (float)0.005f, (float)0.02f);
            this.world.addParticleClient((ParticleEffect)NonParticles.LIQUID_PARTICLE_PUDDLE, this.x + offsetX, this.y + offsetY, this.z + offsetZ, 0.0, 0.0, 0.0);
        }
    }

    public static final class Factory
    implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new LiquidFallingParticle(world, x, y, z, (Fluid)Fluids.LAVA, (ParticleEffect)NonParticles.LIQUID_PARTICLE_PUDDLE, this.spriteProvider.getSprite(random));
        }
    }
}

