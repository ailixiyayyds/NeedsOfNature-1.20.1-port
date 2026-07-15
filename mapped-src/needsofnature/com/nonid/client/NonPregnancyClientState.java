/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.client;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class NonPregnancyClientState {
    @Nullable
    private static Identifier pregnantEntityTypeId;

    private NonPregnancyClientState() {
    }

    public static void setPregnantEntityTypeId(@Nullable Identifier entityTypeId) {
        pregnantEntityTypeId = entityTypeId;
    }

    @Nullable
    public static Identifier getPregnantEntityTypeId() {
        return pregnantEntityTypeId;
    }

    public static void clear() {
        pregnantEntityTypeId = null;
    }
}

