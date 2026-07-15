/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_1799
 *  net.minecraft.class_310
 *  net.minecraft.class_746
 */
package com.nonid.client;

import com.nonid.EnergyHolder;
import com.nonid.GenderHolder;
import com.nonid.NeedsOfNature;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_746;

public final class NonDebugNameHelper {
    private NonDebugNameHelper() {
    }

    public static boolean shouldShowDebugLabel(class_1297 entity) {
        if (!(entity instanceof class_1308)) {
            return false;
        }
        if (!(entity instanceof EnergyHolder)) {
            return false;
        }
        class_310 client = class_310.method_1551();
        class_746 player = client.field_1724;
        if (player == null) {
            return false;
        }
        class_1799 main = player.method_6047();
        class_1799 off = player.method_6079();
        return main.method_7909() == NeedsOfNature.DEBUG_STAFF || off.method_7909() == NeedsOfNature.DEBUG_STAFF;
    }

    public static String resolveGender(class_1297 entity) {
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
        boolean hasMale = entity.method_5752().contains("gender.male");
        boolean hasFemale = entity.method_5752().contains("gender.female");
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

    public static String resolveGenderSymbol(class_1297 entity) {
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
        boolean hasMale = entity.method_5752().contains("gender.male");
        boolean hasFemale = entity.method_5752().contains("gender.female");
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

