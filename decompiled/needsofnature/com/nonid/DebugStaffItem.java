/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1792
 *  net.minecraft.class_1792$class_1793
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_7923
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
import net.minecraft.class_124;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_7923;

public class DebugStaffItem
extends class_1792 {
    public DebugStaffItem(class_1792.class_1793 settings) {
        super(settings);
    }

    public class_1269 method_7847(class_1799 stack, class_1657 user, class_1309 entity, class_1268 hand) {
        if (!(entity instanceof EnergyHolder)) {
            return class_1269.field_5811;
        }
        EnergyHolder holder = (EnergyHolder)entity;
        if (user.method_73183().method_8608()) {
            return class_1269.field_5812;
        }
        class_3222 serverPlayer = (class_3222)user;
        if (user.method_5715()) {
            int current = holder.getEnergy();
            int max = holder.getMaxEnergy();
            int next = Math.min(max, current + 25);
            holder.setEnergy(next);
            return class_1269.field_5812;
        }
        this.sendDebugReport(serverPlayer, entity, holder);
        return class_1269.field_5812;
    }

    private void sendDebugReport(class_3222 player, class_1309 entity, EnergyHolder holder) {
        class_2960 entityId = class_7923.field_41177.method_10221((Object)entity.method_5864());
        player.method_7353((class_2561)class_2561.method_43470((String)("[NoN Debug] " + entity.method_5476().getString() + " (" + String.valueOf(entityId) + ")")).method_27692(class_124.field_1060), false);
        player.method_7353((class_2561)class_2561.method_43470((String)("UUID: " + String.valueOf(entity.method_5667()))).method_27692(class_124.field_1080), false);
        player.method_7353((class_2561)class_2561.method_43470((String)("Gender: " + this.resolveGender(entity))).method_27692(class_124.field_1080), false);
        player.method_7353((class_2561)class_2561.method_43470((String)("Energy: " + holder.getEnergy() + "/" + holder.getMaxEnergy() + " | initialized: " + holder.isEnergyInitialized())).method_27692(class_124.field_1080), false);
        player.method_7353((class_2561)class_2561.method_43470((String)("Energy gain: mult " + this.format(holder.getEnergyGainMultiplier()) + " | drift " + this.format(holder.getEnergyGainDrift()))).method_27692(class_124.field_1080), false);
        class_1937 class_19372 = entity.method_73183();
        if (class_19372 instanceof class_3218) {
            class_3218 world = (class_3218)class_19372;
            boolean activeOrPending = NeedsOfNature.isActorPendingOrActive(world, entity.method_5667());
            player.method_7353((class_2561)class_2561.method_43470((String)("Animation: active/pending " + activeOrPending)).method_27692(class_124.field_1080), false);
            this.sendAuraReport(player, world, entity, holder);
            this.sendCooldownReport(player, world, entity, holder);
        }
        if (entity instanceof LiquidHolder) {
            LiquidHolder liquidHolder = (LiquidHolder)entity;
            class_2960 liquidType = liquidHolder.getLiquidEntityTypeId();
            String liquidInfo = liquidHolder.getLiquidStored() + "/" + liquidHolder.getLiquidCapacity() + "ml | composition " + String.valueOf((Object)liquidHolder.getLiquidComposition()) + (String)(liquidType == null ? "" : " | entity " + String.valueOf(liquidType));
            if (entity instanceof class_3222) {
                liquidInfo = liquidInfo + " | tank type " + String.valueOf((Object)NonGenderSystem.getLiquidTankType((class_1297)entity));
            }
            player.method_7353((class_2561)class_2561.method_43470((String)("Liquid: " + liquidInfo)).method_27692(class_124.field_1080), false);
        }
        if (entity instanceof PregnancyHolder) {
            PregnancyHolder pregnancyHolder = (PregnancyHolder)entity;
            class_2960 pregnancyType = pregnancyHolder.getPregnantEntityTypeId();
            String pregnancyInfo = pregnancyType == null ? "none" : String.valueOf(pregnancyType) + " | end tick " + pregnancyHolder.getPregnancyEndTick() + " | pending hatch " + pregnancyHolder.isPregnancyPendingHatch();
            player.method_7353((class_2561)class_2561.method_43470((String)("Pregnancy: " + pregnancyInfo)).method_27692(class_124.field_1080), false);
        }
    }

    private void sendAuraReport(class_3222 player, class_3218 world, class_1309 entity, EnergyHolder holder) {
        if (!(entity instanceof class_1308)) {
            player.method_7353((class_2561)class_2561.method_43470((String)"Aura: none").method_27692(class_124.field_1080), false);
            return;
        }
        class_1308 mob = (class_1308)entity;
        NeedsOfNature.PlayerEnergyAuraDebug aura = NeedsOfNature.getPlayerEnergyAuraDebug(world, mob, holder);
        if (aura == null) {
            player.method_7353((class_2561)class_2561.method_43470((String)("Aura: none | carry " + this.format(holder.getEnergyAuraCarry()))).method_27692(class_124.field_1080), false);
            return;
        }
        String sourceName = aura.player().method_5477().getString();
        player.method_7353((class_2561)class_2561.method_43470((String)("Aura: " + this.format(aura.totalMultiplier()) + "x from " + sourceName + " | energy " + aura.playerEnergy() + " | destroyed " + aura.destroyedSkinStage() + " | mess " + aura.messTotal() + " | radius " + this.format(aura.radius()) + " | carry " + this.format(holder.getEnergyAuraCarry()))).method_27692(class_124.field_1080), false);
    }

    private void sendCooldownReport(class_3222 player, class_3218 world, class_1309 entity, EnergyHolder holder) {
        ArrayList<String> cooldowns = new ArrayList<String>();
        String attackFailsafe = NeedsOfNature.getMobAttackFailsafeDebug(world, entity.method_5667());
        if (attackFailsafe != null && !attackFailsafe.isBlank()) {
            cooldowns.add(attackFailsafe);
        }
        cooldowns.addAll(holder.getActiveCooldownDebugLines(world));
        if (cooldowns.isEmpty()) {
            player.method_7353((class_2561)class_2561.method_43470((String)"Cooldowns: none").method_27692(class_124.field_1080), false);
            return;
        }
        player.method_7353((class_2561)class_2561.method_43470((String)"Cooldowns:").method_27692(class_124.field_1080), false);
        for (String line : cooldowns) {
            player.method_7353((class_2561)class_2561.method_43470((String)("- " + line)).method_27692(class_124.field_1063), false);
        }
    }

    private String resolveGender(class_1309 entity) {
        if (entity instanceof GenderHolder) {
            GenderHolder genderHolder = (GenderHolder)entity;
            return this.formatGenderMask(genderHolder.getGenderMask());
        }
        boolean hasMale = entity.method_5752().contains("gender.male");
        boolean hasFemale = entity.method_5752().contains("gender.female");
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

