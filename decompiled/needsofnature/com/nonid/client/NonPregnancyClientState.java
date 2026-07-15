/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.client;

import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public final class NonPregnancyClientState {
    @Nullable
    private static class_2960 pregnantEntityTypeId;

    private NonPregnancyClientState() {
    }

    public static void setPregnantEntityTypeId(@Nullable class_2960 entityTypeId) {
        pregnantEntityTypeId = entityTypeId;
    }

    @Nullable
    public static class_2960 getPregnantEntityTypeId() {
        return pregnantEntityTypeId;
    }

    public static void clear() {
        pregnantEntityTypeId = null;
    }
}

