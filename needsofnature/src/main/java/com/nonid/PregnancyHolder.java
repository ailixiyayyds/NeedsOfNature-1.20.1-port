/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import com.nonid.NeedsOfNature;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface PregnancyHolder {
    @Nullable
    public Identifier getPregnantEntityTypeId();

    public long getPregnancyEndTick();

    public boolean isPregnancyPendingHatch();

    @Nullable
    public NeedsOfNature.PregnancyVariantData getPregnancyVariantData();

    public int getPregnancyOffspringCount();

    public void setPregnancyOffspringCount(int var1);

    public void setPregnancyState(@Nullable Identifier var1, long var2, boolean var4, @Nullable NeedsOfNature.PregnancyVariantData var5);

    default public void setPregnancyState(@Nullable Identifier entityTypeId, long endTick, boolean pendingHatch) {
        this.setPregnancyState(entityTypeId, endTick, pendingHatch, null);
    }

    public void clearPregnancyState();

    default public boolean hasPregnancyState() {
        return this.getPregnantEntityTypeId() != null;
    }
}

