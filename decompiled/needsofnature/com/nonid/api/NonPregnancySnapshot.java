/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.api;

import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public record NonPregnancySnapshot(@Nullable class_2960 entityTypeId, long endTick, boolean pendingHatch) {
    public boolean active() {
        return this.entityTypeId != null;
    }
}

