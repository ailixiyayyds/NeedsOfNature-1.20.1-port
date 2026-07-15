/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.api;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record NonPregnancySnapshot(@Nullable Identifier entityTypeId, long endTick, boolean pendingHatch) {
    public boolean active() {
        return this.entityTypeId != null;
    }
}

