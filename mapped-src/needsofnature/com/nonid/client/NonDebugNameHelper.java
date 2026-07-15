/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.nonid.client;

import com.nonid.EnergyHolder;
import com.nonid.GenderHolder;
import com.nonid.NeedsOfNature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public final class NonDebugNameHelper {
    private NonDebugNameHelper() {
    }

    public static boolean shouldShowDebugLabel(Entity entity) {
        if (!(entity instanceof MobEntity)) {
            return false;
        }
        if (!(entity instanceof EnergyHolder)) {
            return false;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return false;
        }
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        return main.getItem() == NeedsOfNature.DEBUG_STAFF || off.getItem() == NeedsOfNature.DEBUG_STAFF;
    }

    public static String resolveGender(Entity entity) {
        if (entity instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)entity;
            boolean male = holder.hasGender(1);
            boolean female = holder.hasGender(2);
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
        boolean hasMale = entity.getCommandTags().contains("gender.male");
        boolean hasFemale = entity.getCommandTags().contains("gender.female");
        if (hasMale && hasFemale) {
            return "male & female";
        }
        if (hasMale) {
            return "male";
        }
        if (hasFemale) {
            return "female";
        }
        return "none";
    }

    public static String resolveGenderSymbol(Entity entity) {
        if (entity instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)entity;
            boolean male = holder.hasGender(1);
            boolean female = holder.hasGender(2);
            if (male && female) {
                return "\u2642\u2640";
            }
            if (male) {
                return "\u2642";
            }
            if (female) {
                return "\u2640";
            }
            return "";
        }
        boolean hasMale = entity.getCommandTags().contains("gender.male");
        boolean hasFemale = entity.getCommandTags().contains("gender.female");
        if (hasMale && hasFemale) {
            return "\u2642\u2640";
        }
        if (hasMale) {
            return "\u2642";
        }
        if (hasFemale) {
            return "\u2640";
        }
        return "";
    }
}

