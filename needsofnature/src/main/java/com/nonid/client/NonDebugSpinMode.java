/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 */
package com.nonid.client;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import net.minecraft.util.Identifier;

public final class NonDebugSpinMode {
    public static final Identifier DEBUG_SPIN_ANIMATION_RESOURCE = Identifier.of((String)"needsofnature", (String)"afw/debug_spin/debug_spin");
    public static final Identifier DEBUG_SPIN_MODEL_RESOURCE = Identifier.of((String)"needsofnature", (String)"fish");
    public static final Identifier DEBUG_SPIN_TEXTURE_RESOURCE = new Identifier("minecraft", "textures/entity/fish/salmon.png");

    private NonDebugSpinMode() {
    }

    public static boolean isEnabled() {
        NonConfig config = NeedsOfNature.getConfig();
        return config != null && config.isDebugSpinMode();
    }
}

