/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11368
 *  net.minecraft.class_11372
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_2960
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.LiquidHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.NonTrinketsIntegration;
import net.minecraft.class_11368;
import net.minecraft.class_11372;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1297.class})
public abstract class PlayerLiquidMixin
implements LiquidHolder {
    @Unique
    private int nonLiquidStored = 0;
    @Unique
    private boolean nonLiquidMixed = false;
    @Unique
    private class_2960 nonLiquidEntityType = null;

    @Override
    public int getLiquidStored() {
        return this.nonLiquidStored;
    }

    @Override
    public int getLiquidCapacity() {
        NonConfig config = NeedsOfNature.getConfig();
        if (config != null && !config.isLiquidTankEnabled()) {
            return 0;
        }
        int baseCapacity = config != null ? config.getLiquidTankCapacityMl() : 200;
        PlayerLiquidMixin playerLiquidMixin = this;
        if (playerLiquidMixin instanceof class_1657) {
            class_1657 player = (class_1657)playerLiquidMixin;
            baseCapacity += NonTrinketsIntegration.getActiveTankAccessoryEffects((class_1309)player).liquidCapacityAdd();
        }
        return Math.max(0, baseCapacity);
    }

    @Override
    public void setLiquidStored(int value) {
        int cap = this.getLiquidCapacity();
        if (cap <= 0) {
            this.nonLiquidStored = 0;
            this.clearLiquidComposition();
            return;
        }
        this.nonLiquidStored = Math.max(0, Math.min(cap, value));
        if (this.nonLiquidStored == 0) {
            this.clearLiquidComposition();
        }
    }

    @Override
    public LiquidHolder.LiquidComposition getLiquidComposition() {
        if (this.nonLiquidStored == 0) {
            return LiquidHolder.LiquidComposition.EMPTY;
        }
        if (this.nonLiquidMixed) {
            return LiquidHolder.LiquidComposition.MIXED;
        }
        if (this.nonLiquidEntityType != null) {
            return LiquidHolder.LiquidComposition.ENTITY;
        }
        return LiquidHolder.LiquidComposition.MIXED;
    }

    @Override
    public class_2960 getLiquidEntityTypeId() {
        return this.nonLiquidEntityType;
    }

    @Override
    public void setLiquidCompositionMixed() {
        this.nonLiquidMixed = true;
        this.nonLiquidEntityType = null;
    }

    @Override
    public void setLiquidCompositionEntity(class_2960 entityTypeId) {
        if (entityTypeId == null) {
            this.setLiquidCompositionMixed();
            return;
        }
        this.nonLiquidMixed = false;
        this.nonLiquidEntityType = entityTypeId;
    }

    @Override
    public void clearLiquidComposition() {
        this.nonLiquidMixed = false;
        this.nonLiquidEntityType = null;
    }

    @Inject(method={"method_5647"}, at={@At(value="TAIL")})
    private void needsOfNature$writeLiquid(class_11372 storage, CallbackInfo ci) {
        if (!(this instanceof class_1657)) {
            return;
        }
        if (this.nonLiquidStored <= 0) {
            return;
        }
        storage.method_71465("NeedsOfNatureLiquid", this.nonLiquidStored);
        if (this.nonLiquidMixed) {
            storage.method_71469("NeedsOfNatureLiquidType", "mixed");
        } else if (this.nonLiquidEntityType != null) {
            storage.method_71469("NeedsOfNatureLiquidType", this.nonLiquidEntityType.toString());
        }
    }

    @Inject(method={"method_5651"}, at={@At(value="TAIL")})
    private void needsOfNature$readLiquid(class_11368 storage, CallbackInfo ci) {
        if (!(this instanceof class_1657)) {
            return;
        }
        int stored = storage.method_71439("NeedsOfNatureLiquid").orElse(0);
        this.setLiquidStored(stored);
        if (stored <= 0) {
            return;
        }
        String rawType = storage.method_71441("NeedsOfNatureLiquidType").orElse("");
        if (rawType.isBlank() || "mixed".equalsIgnoreCase(rawType)) {
            this.setLiquidCompositionMixed();
            return;
        }
        class_2960 entityTypeId = class_2960.method_12829((String)rawType);
        if (entityTypeId != null) {
            this.setLiquidCompositionEntity(entityTypeId);
        } else {
            this.setLiquidCompositionMixed();
        }
    }
}

