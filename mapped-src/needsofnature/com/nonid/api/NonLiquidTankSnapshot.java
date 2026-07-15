/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.api;

import com.nonid.api.NonLiquidComposition;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record NonLiquidTankSnapshot(int storedMl, int capacityMl, NonLiquidComposition composition, @Nullable Identifier entityTypeId, int tintRgb) {
    public float fillRatio() {
        return this.capacityMl <= 0 ? 0.0f : Math.max(0.0f, Math.min(1.0f, (float)this.storedMl / (float)this.capacityMl));
    }
}

