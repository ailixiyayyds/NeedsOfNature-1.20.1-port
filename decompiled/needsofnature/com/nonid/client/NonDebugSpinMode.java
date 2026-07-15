/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 */
package com.nonid.client;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import net.minecraft.class_2960;

public final class NonDebugSpinMode {
    public static final class_2960 DEBUG_SPIN_ANIMATION_RESOURCE = class_2960.method_60655((String)"needsofnature", (String)"afw/debug_spin/debug_spin");
    public static final class_2960 DEBUG_SPIN_MODEL_RESOURCE = class_2960.method_60655((String)"needsofnature", (String)"fish");
    public static final class_2960 DEBUG_SPIN_TEXTURE_RESOURCE = class_2960.method_60656((String)"textures/entity/fish/salmon.png");

    private NonDebugSpinMode() {
    }

    public static boolean isEnabled() {
        NonConfig config = NeedsOfNature.getConfig();
        return config != null && config.isDebugSpinMode();
    }
}

