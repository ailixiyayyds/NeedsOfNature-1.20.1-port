/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.texture.Sprite
 *  net.minecraft.world.BlockView
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.fluid.FluidState
 *  net.minecraft.fluid.Fluid
 *  net.minecraft.fluid.Fluids
 *  net.minecraft.client.particle.BillboardParticle
 *  net.minecraft.client.particle.BillboardParticle$RenderType
 *  net.minecraft.client.world.ClientWorld
 */
package com.nonid.client.particle;

import com.nonid.client.NonHudOverlay;
import net.minecraft.client.texture.Sprite;
import net.minecraft.world.BlockView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.world.ClientWorld;

abstract class LiquidLeakParticle
extends BillboardParticle {
    protected static final float BASE_GRAVITY = 0.06f;
    protected static final double DAMPING = 0.98;
    protected final Fluid fluid;

    protected LiquidLeakParticle(ClientWorld world, double x, double y, double z, Fluid fluid, Sprite sprite) {
        super(world, x, y, z, sprite);
        this.fluid = fluid;
        this.setBoundingBoxSpacing(0.01f, 0.01f);
        this.gravityStrength = 0.06f;
        this.applyTankColor();
    }

    public BillboardParticle.RenderType getRenderType() {
        return BillboardParticle.RenderType.PARTICLE_ATLAS_OPAQUE;
    }

    public void tick() {
        double surface;
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        this.updateAge();
        if (this.dead) {
            return;
        }
        this.velocityY -= (double)this.gravityStrength;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.updateVelocity();
        if (this.dead) {
            return;
        }
        this.velocityX *= 0.98;
        this.velocityY *= 0.98;
        this.velocityZ *= 0.98;
        if (this.fluid == Fluids.EMPTY) {
            return;
        }
        BlockPos pos = BlockPos.ofFloored((double)this.x, (double)this.y, (double)this.z);
        FluidState fluidState = this.world.getFluidState(pos);
        if (fluidState.getFluid() == this.fluid && this.y < (surface = (double)((float)pos.getY() + fluidState.getHeight((BlockView)this.world, pos)))) {
            this.markDead();
        }
    }

    protected void updateAge() {
        if (--this.maxAge <= 0) {
            this.markDead();
        }
    }

    protected void updateVelocity() {
    }

    private void applyTankColor() {
        int rgb = NonHudOverlay.getLiquidTintRgb();
        float r = (float)(rgb >> 16 & 0xFF) / 255.0f;
        float g = (float)(rgb >> 8 & 0xFF) / 255.0f;
        float b = (float)(rgb & 0xFF) / 255.0f;
        this.setColor(r, g, b);
    }
}

