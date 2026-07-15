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
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LiquidPuddleParticle
extends SpriteBillboardParticle {
    private static final int DEFAULT_MAX_AGE_TICKS = 800;
    private static final int MAX_FADE_TICKS = 200;
    private final int holdTicks;
    private final int fadeTicks;

    public LiquidPuddleParticle(ClientWorld world, double x, double y, double z, Sprite sprite, float scaleFactor) {
        super(world, x, y, z);
        this.setSprite(sprite);
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

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void buildGeometry(VertexConsumer vertices, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        float renderX = (float)(MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.x);
        float renderY = (float)(MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.y);
        float renderZ = (float)(MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.z);
        float size = this.getSize(tickDelta);
        Quaternionf rotation = new Quaternionf().rotationXYZ(-(float)Math.PI / 2.0f, 0.0f, 0.0f);
        Vector3f[] corners = new Vector3f[]{
            new Vector3f(-1.0f, -1.0f, 0.0f),
            new Vector3f(-1.0f, 1.0f, 0.0f),
            new Vector3f(1.0f, 1.0f, 0.0f),
            new Vector3f(1.0f, -1.0f, 0.0f)
        };
        for (Vector3f corner : corners) {
            corner.rotate(rotation).mul(size).add(renderX, renderY, renderZ);
        }
        int light = this.getBrightness(tickDelta);
        this.vertex(vertices, corners[0], this.getMaxU(), this.getMaxV(), light);
        this.vertex(vertices, corners[1], this.getMaxU(), this.getMinV(), light);
        this.vertex(vertices, corners[2], this.getMinU(), this.getMinV(), light);
        this.vertex(vertices, corners[3], this.getMinU(), this.getMaxV(), light);
    }

    private void vertex(VertexConsumer vertices, Vector3f position, float u, float v, int light) {
        vertices.vertex(position.x(), position.y(), position.z())
            .texture(u, v)
            .color(this.red, this.green, this.blue, this.alpha)
            .light(light)
            .next();
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
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
    implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            float scale = NonPuddleSizing.scaleForPosition(world, x, y, z);
            return new LiquidPuddleParticle(world, x, y, z, this.spriteProvider.getSprite(world.getRandom()), scale);
        }
    }
}

