/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.Item$Settings
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.particle.ParticleTypes
 *  net.minecraft.particle.SimpleParticleType
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.sound.SoundEvent
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 */
package com.nonid.item;

import com.nonid.EnergyHolder;
import com.nonid.particle.NonParticles;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;

public class EnergyGainAdjustItem
extends Item {
    private final float delta;

    public EnergyGainAdjustItem(Item.Settings settings, float delta) {
        super(settings);
        this.delta = delta;
    }

    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World class_19372;
        float current;
        float target;
        MobEntity mob;
        if (!(entity instanceof MobEntity) || !((mob = (MobEntity)entity) instanceof EnergyHolder)) {
            return ActionResult.PASS;
        }
        EnergyHolder holder = (EnergyHolder)mob;
        if (user.getEntityWorld().isClient()) {
            return ActionResult.SUCCESS;
        }
        boolean removedStabilization = false;
        if (this.delta > 0.0f && mob.getCommandTags().contains("non.energy_stabilized")) {
            mob.removeScoreboardTag("non.energy_stabilized");
            removedStabilization = true;
        }
        if (Math.abs((target = EnergyGainAdjustItem.clamp((current = holder.getEnergyGainMultiplier()) + this.delta, 0.4f, 6.0f)) - current) < 1.0E-6f && !removedStabilization) {
            return ActionResult.FAIL;
        }
        if (Math.abs(target - current) >= 1.0E-6f) {
            holder.setEnergyGainMultiplier(target);
        }
        if ((class_19372 = user.getEntityWorld()) instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)class_19372;
            this.playFeedback(serverWorld, mob, removedStabilization, target);
        }
        if (!user.isCreative()) {
            stack.decrement(1);
        }
        return ActionResult.SUCCESS;
    }

    private void playFeedback(ServerWorld world, MobEntity mob, boolean removedStabilization, float appliedMultiplier) {
        if (removedStabilization) {
            world.spawnParticles((ParticleEffect)ParticleTypes.WITCH, mob.getX(), mob.getBodyY(0.6), mob.getZ(), 12, 0.35, 0.35, 0.35, 0.01);
            world.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.NEUTRAL, 0.9f, 0.95f);
            return;
        }
        boolean augmenter = this.delta > 0.0f;
        DefaultParticleType particle = augmenter ? NonParticles.SMALLHEART : ParticleTypes.SMOKE;
        SoundEvent sound = augmenter ? SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP : SoundEvents.BLOCK_BREWING_STAND_BREW;
        float pitch = EnergyGainAdjustItem.pitchForMultiplier(appliedMultiplier);
        world.spawnParticles((ParticleEffect)particle, mob.getX(), mob.getBodyY(0.6), mob.getZ(), 10, 0.35, 0.35, 0.35, 0.01);
        world.playSound(null, mob.getX(), mob.getY(), mob.getZ(), sound, SoundCategory.NEUTRAL, 0.9f, pitch);
    }

    private static float pitchForMultiplier(float multiplier) {
        float clamped = EnergyGainAdjustItem.clamp(multiplier, 0.4f, 6.0f);
        float span = 5.6f;
        float t = (clamped - 0.4f) / span;
        return 0.65f + 0.9f * t;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}

