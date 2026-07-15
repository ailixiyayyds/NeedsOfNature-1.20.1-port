/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1308
 *  net.minecraft.class_1321
 *  net.minecraft.class_4019
 *  net.minecraft.class_7689
 */
package com.nonid;

import net.minecraft.class_1308;
import net.minecraft.class_1321;
import net.minecraft.class_4019;
import net.minecraft.class_7689;

public final class NonMobGatherEligibility {
    private NonMobGatherEligibility() {
    }

    public static boolean canAutoGather(class_1308 mob) {
        class_7689 camel;
        class_4019 fox;
        class_1321 tameable;
        if (mob == null || !mob.method_5805() || mob.method_31481()) {
            return false;
        }
        if (mob.method_5987() || mob.method_6113()) {
            return false;
        }
        if (mob instanceof class_1321 && ((tameable = (class_1321)mob).method_24345() || tameable.method_6172())) {
            return false;
        }
        if (mob instanceof class_4019 && ((fox = (class_4019)mob).method_18272() || fox.method_6113())) {
            return false;
        }
        return !(mob instanceof class_7689) || !(camel = (class_7689)mob).method_45350();
    }
}

