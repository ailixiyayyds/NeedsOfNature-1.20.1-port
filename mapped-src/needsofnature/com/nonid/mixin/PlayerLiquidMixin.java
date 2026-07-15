/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.storage.ReadView
 *  net.minecraft.storage.WriteView
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
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
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class PlayerLiquidMixin
implements LiquidHolder {
    @Unique
    private int nonLiquidStored = 0;
    @Unique
    private boolean nonLiquidMixed = false;
    @Unique
    private Identifier nonLiquidEntityType = null;

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
        if (playerLiquidMixin instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)playerLiquidMixin;
            baseCapacity += NonTrinketsIntegration.getActiveTankAccessoryEffects((LivingEntity)player).liquidCapacityAdd();
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
    public Identifier getLiquidEntityTypeId() {
        return this.nonLiquidEntityType;
    }

    @Override
    public void setLiquidCompositionMixed() {
        this.nonLiquidMixed = true;
        this.nonLiquidEntityType = null;
    }

    @Override
    public void setLiquidCompositionEntity(Identifier entityTypeId) {
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

    @Inject(method={"writeData"}, at={@At(value="TAIL")})
    private void needsOfNature$writeLiquid(WriteView storage, CallbackInfo ci) {
        if (!(this instanceof PlayerEntity)) {
            return;
        }
        if (this.nonLiquidStored <= 0) {
            return;
        }
        storage.putInt("NeedsOfNatureLiquid", this.nonLiquidStored);
        if (this.nonLiquidMixed) {
            storage.putString("NeedsOfNatureLiquidType", "mixed");
        } else if (this.nonLiquidEntityType != null) {
            storage.putString("NeedsOfNatureLiquidType", this.nonLiquidEntityType.toString());
        }
    }

    @Inject(method={"readData"}, at={@At(value="TAIL")})
    private void needsOfNature$readLiquid(ReadView storage, CallbackInfo ci) {
        if (!(this instanceof PlayerEntity)) {
            return;
        }
        int stored = storage.getOptionalInt("NeedsOfNatureLiquid").orElse(0);
        this.setLiquidStored(stored);
        if (stored <= 0) {
            return;
        }
        String rawType = storage.getOptionalString("NeedsOfNatureLiquidType").orElse("");
        if (rawType.isBlank() || "mixed".equalsIgnoreCase(rawType)) {
            this.setLiquidCompositionMixed();
            return;
        }
        Identifier entityTypeId = Identifier.tryParse((String)rawType);
        if (entityTypeId != null) {
            this.setLiquidCompositionEntity(entityTypeId);
        } else {
            this.setLiquidCompositionMixed();
        }
    }
}

