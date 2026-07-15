/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 */
package com.nonid;

import com.nonid.NonConfig;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;

public final class NonSkinRepairBlocks {
    private NonSkinRepairBlocks() {
    }

    public static boolean isNearRepairBlock(class_1937 world, class_2338 center, NonConfig config) {
        if (world == null || center == null) {
            return false;
        }
        for (class_2338 pos : class_2338.method_25996((class_2338)center, (int)1, (int)1, (int)1)) {
            if (!NonSkinRepairBlocks.isRepairBlock(world.method_8320(pos), config)) continue;
            return true;
        }
        return false;
    }

    public static boolean isRepairBlock(class_2680 state, NonConfig config) {
        if (state == null) {
            return false;
        }
        if (state.method_27852(class_2246.field_10083)) {
            return true;
        }
        return config != null && config.isCraftingTableSkinRepairAllowed() && state.method_27852(class_2246.field_9980);
    }
}

