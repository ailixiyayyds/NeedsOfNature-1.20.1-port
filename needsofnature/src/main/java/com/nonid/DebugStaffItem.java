/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Formatting
 *  net.minecraft.util.Hand
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.Item$Settings
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.registry.Registries
 */
package com.nonid;

import com.nonid.EnergyHolder;
import com.nonid.GenderHolder;
import com.nonid.LiquidHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonGenderSystem;
import com.nonid.PregnancyHolder;
import java.util.ArrayList;
import java.util.Locale;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.Registries;

public class DebugStaffItem
extends Item {
    public DebugStaffItem(Item.Settings settings) {
        super(settings);
    }

    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!(entity instanceof EnergyHolder)) {
            return ActionResult.PASS;
        }
        EnergyHolder holder = (EnergyHolder)entity;
        if (user.getEntityWorld().isClient()) {
            return ActionResult.SUCCESS;
        }
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)user;
        if (user.isSneaking()) {
            int current = holder.getEnergy();
            int max = holder.getMaxEnergy();
            int next = Math.min(max, current + 25);
            holder.setEnergy(next);
            return ActionResult.SUCCESS;
        }
        this.sendDebugReport(serverPlayer, entity, holder);
        return ActionResult.SUCCESS;
    }

    private void sendDebugReport(ServerPlayerEntity player, LivingEntity entity, EnergyHolder holder) {
        Identifier entityId = Registries.ENTITY_TYPE.getId(entity.getType());
        player.sendMessage((Text)Text.literal((String)("[NoN Debug] " + entity.getDisplayName().getString() + " (" + String.valueOf(entityId) + ")")).formatted(Formatting.GREEN), false);
        player.sendMessage((Text)Text.literal((String)("UUID: " + String.valueOf(entity.getUuid()))).formatted(Formatting.GRAY), false);
        player.sendMessage((Text)Text.literal((String)("Gender: " + this.resolveGender(entity))).formatted(Formatting.GRAY), false);
        player.sendMessage((Text)Text.literal((String)("Energy: " + holder.getEnergy() + "/" + holder.getMaxEnergy() + " | initialized: " + holder.isEnergyInitialized())).formatted(Formatting.GRAY), false);
        player.sendMessage((Text)Text.literal((String)("Energy gain: mult " + this.format(holder.getEnergyGainMultiplier()) + " | drift " + this.format(holder.getEnergyGainDrift()))).formatted(Formatting.GRAY), false);
        World class_19372 = entity.getEntityWorld();
        if (class_19372 instanceof ServerWorld) {
            ServerWorld world = (ServerWorld)class_19372;
            boolean activeOrPending = NeedsOfNature.isActorPendingOrActive(world, entity.getUuid());
            player.sendMessage((Text)Text.literal((String)("Animation: active/pending " + activeOrPending)).formatted(Formatting.GRAY), false);
            this.sendAuraReport(player, world, entity, holder);
            this.sendCooldownReport(player, world, entity, holder);
        }
        if (entity instanceof LiquidHolder) {
            LiquidHolder liquidHolder = (LiquidHolder)entity;
            Identifier liquidType = liquidHolder.getLiquidEntityTypeId();
            String liquidInfo = liquidHolder.getLiquidStored() + "/" + liquidHolder.getLiquidCapacity() + "ml | composition " + String.valueOf((Object)liquidHolder.getLiquidComposition()) + (String)(liquidType == null ? "" : " | entity " + String.valueOf(liquidType));
            if (entity instanceof ServerPlayerEntity) {
                liquidInfo = liquidInfo + " | tank type " + String.valueOf((Object)NonGenderSystem.getLiquidTankType((Entity)entity));
            }
            player.sendMessage((Text)Text.literal((String)("Liquid: " + liquidInfo)).formatted(Formatting.GRAY), false);
        }
        if (entity instanceof PregnancyHolder) {
            PregnancyHolder pregnancyHolder = (PregnancyHolder)entity;
            Identifier pregnancyType = pregnancyHolder.getPregnantEntityTypeId();
            String pregnancyInfo = pregnancyType == null ? "none" : String.valueOf(pregnancyType) + " | end tick " + pregnancyHolder.getPregnancyEndTick() + " | pending hatch " + pregnancyHolder.isPregnancyPendingHatch();
            player.sendMessage((Text)Text.literal((String)("Pregnancy: " + pregnancyInfo)).formatted(Formatting.GRAY), false);
        }
    }

    private void sendAuraReport(ServerPlayerEntity player, ServerWorld world, LivingEntity entity, EnergyHolder holder) {
        if (!(entity instanceof MobEntity)) {
            player.sendMessage((Text)Text.literal((String)"Aura: none").formatted(Formatting.GRAY), false);
            return;
        }
        MobEntity mob = (MobEntity)entity;
        NeedsOfNature.PlayerEnergyAuraDebug aura = NeedsOfNature.getPlayerEnergyAuraDebug(world, mob, holder);
        if (aura == null) {
            player.sendMessage((Text)Text.literal((String)("Aura: none | carry " + this.format(holder.getEnergyAuraCarry()))).formatted(Formatting.GRAY), false);
            return;
        }
        String sourceName = aura.player().getName().getString();
        player.sendMessage((Text)Text.literal((String)("Aura: " + this.format(aura.totalMultiplier()) + "x from " + sourceName + " | energy " + aura.playerEnergy() + " | destroyed " + aura.destroyedSkinStage() + " | mess " + aura.messTotal() + " | radius " + this.format(aura.radius()) + " | carry " + this.format(holder.getEnergyAuraCarry()))).formatted(Formatting.GRAY), false);
    }

    private void sendCooldownReport(ServerPlayerEntity player, ServerWorld world, LivingEntity entity, EnergyHolder holder) {
        ArrayList<String> cooldowns = new ArrayList<String>();
        String attackFailsafe = NeedsOfNature.getMobAttackFailsafeDebug(world, entity.getUuid());
        if (attackFailsafe != null && !attackFailsafe.isBlank()) {
            cooldowns.add(attackFailsafe);
        }
        cooldowns.addAll(holder.getActiveCooldownDebugLines(world));
        if (cooldowns.isEmpty()) {
            player.sendMessage((Text)Text.literal((String)"Cooldowns: none").formatted(Formatting.GRAY), false);
            return;
        }
        player.sendMessage((Text)Text.literal((String)"Cooldowns:").formatted(Formatting.GRAY), false);
        for (String line : cooldowns) {
            player.sendMessage((Text)Text.literal((String)("- " + line)).formatted(Formatting.DARK_GRAY), false);
        }
    }

    private String resolveGender(LivingEntity entity) {
        if (entity instanceof GenderHolder) {
            GenderHolder genderHolder = (GenderHolder)entity;
            return this.formatGenderMask(genderHolder.getGenderMask());
        }
        boolean hasMale = entity.getCommandTags().contains("gender.male");
        boolean hasFemale = entity.getCommandTags().contains("gender.female");
        int mask = 0;
        if (hasMale) {
            mask |= 1;
        }
        if (hasFemale) {
            mask |= 2;
        }
        return this.formatGenderMask(mask);
    }

    private String formatGenderMask(int mask) {
        boolean female;
        boolean male = (mask & 1) != 0;
        boolean bl = female = (mask & 2) != 0;
        if (male && female) {
            return "male & female";
        }
        if (male) {
            return "male";
        }
        if (female) {
            return "female";
        }
        return "none";
    }

    private String format(float value) {
        return String.format(Locale.ROOT, "%.2f", Float.valueOf(value));
    }

    private String format(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }
}

