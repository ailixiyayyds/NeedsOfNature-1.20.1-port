/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public interface LiquidHolder {
    public int getLiquidStored();

    public int getLiquidCapacity();

    public void setLiquidStored(int var1);

    public LiquidComposition getLiquidComposition();

    @Nullable
    public class_2960 getLiquidEntityTypeId();

    public void setLiquidCompositionMixed();

    public void setLiquidCompositionEntity(class_2960 var1);

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

