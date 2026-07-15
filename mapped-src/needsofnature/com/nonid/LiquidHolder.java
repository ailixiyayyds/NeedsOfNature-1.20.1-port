/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface LiquidHolder {
    public int getLiquidStored();

    public int getLiquidCapacity();

    public void setLiquidStored(int var1);

    public LiquidComposition getLiquidComposition();

    @Nullable
    public Identifier getLiquidEntityTypeId();

    public void setLiquidCompositionMixed();

    public void setLiquidCompositionEntity(Identifier var1);

    public void clearLiquidComposition();

    default public void addLiquidStored(int amount) {
        if (amount == 0) {
            return;
        }
        this.setLiquidStored(this.getLiquidStored() + amount);
    }

    public static enum LiquidComposition {
        EMPTY,
        MIXED,
        ENTITY;

    }
}

