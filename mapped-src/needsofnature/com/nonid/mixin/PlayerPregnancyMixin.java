/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.storage.ReadView
 *  net.minecraft.storage.WriteView
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.mojang.serialization.Codec;
import com.nonid.NeedsOfNature;
import com.nonid.PregnancyHolder;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class PlayerPregnancyMixin
implements PregnancyHolder {
    @Unique
    @Nullable
    private Identifier nonPregnancyEntityType;
    @Unique
    private long nonPregnancyEndTick;
    @Unique
    private boolean nonPregnancyPendingHatch;
    @Unique
    @Nullable
    private NeedsOfNature.PregnancyVariantData nonPregnancyVariantData;
    @Unique
    private int nonPregnancyOffspringCount;

    @Override
    @Nullable
    public Identifier getPregnantEntityTypeId() {
        return this.nonPregnancyEntityType;
    }

    @Override
    public long getPregnancyEndTick() {
        return this.nonPregnancyEndTick;
    }

    @Override
    public boolean isPregnancyPendingHatch() {
        return this.nonPregnancyPendingHatch;
    }

    @Override
    @Nullable
    public NeedsOfNature.PregnancyVariantData getPregnancyVariantData() {
        return this.nonPregnancyVariantData;
    }

    @Override
    public int getPregnancyOffspringCount() {
        return this.nonPregnancyOffspringCount;
    }

    @Override
    public void setPregnancyOffspringCount(int offspringCount) {
        this.nonPregnancyOffspringCount = Math.max(0, Math.min(16, offspringCount));
    }

    @Override
    public void setPregnancyState(@Nullable Identifier entityTypeId, long endTick, boolean pendingHatch, @Nullable NeedsOfNature.PregnancyVariantData variantData) {
        if (entityTypeId == null) {
            this.clearPregnancyState();
            return;
        }
        this.nonPregnancyEntityType = entityTypeId;
        this.nonPregnancyEndTick = Math.max(0L, endTick);
        this.nonPregnancyPendingHatch = pendingHatch;
        this.nonPregnancyVariantData = NeedsOfNature.normalizePregnancyVariantData(variantData);
    }

    @Override
    public void clearPregnancyState() {
        this.nonPregnancyEntityType = null;
        this.nonPregnancyEndTick = 0L;
        this.nonPregnancyPendingHatch = false;
        this.nonPregnancyVariantData = null;
        this.nonPregnancyOffspringCount = 0;
    }

    @Inject(method={"writeData"}, at={@At(value="TAIL")})
    private void non$writePregnancy(WriteView storage, CallbackInfo ci) {
        String variantJson;
        if (!(this instanceof PlayerEntity)) {
            return;
        }
        if (this.nonPregnancyEntityType == null) {
            return;
        }
        storage.putString("NeedsOfNaturePregnancyType", this.nonPregnancyEntityType.toString());
        storage.put("NeedsOfNaturePregnancyEndTick", (Codec)Codec.LONG, (Object)Math.max(0L, this.nonPregnancyEndTick));
        storage.put("NeedsOfNaturePregnancyPending", (Codec)Codec.BOOL, (Object)this.nonPregnancyPendingHatch);
        if (this.nonPregnancyOffspringCount > 0) {
            storage.putInt("NeedsOfNaturePregnancyOffspringCount", this.nonPregnancyOffspringCount);
        }
        if ((variantJson = NeedsOfNature.encodePregnancyVariantData(this.nonPregnancyVariantData)) != null) {
            storage.putString("NeedsOfNaturePregnancyVariantData", variantJson);
        }
    }

    @Inject(method={"readData"}, at={@At(value="TAIL")})
    private void non$readPregnancy(ReadView storage, CallbackInfo ci) {
        Identifier entityTypeId;
        if (!(this instanceof PlayerEntity)) {
            return;
        }
        String rawType = storage.getOptionalString("NeedsOfNaturePregnancyType").orElse("");
        Identifier class_29602 = entityTypeId = rawType.isBlank() ? null : Identifier.tryParse((String)rawType);
        if (entityTypeId == null) {
            this.clearPregnancyState();
            return;
        }
        long endTick = Math.max(0L, storage.read("NeedsOfNaturePregnancyEndTick", (Codec)Codec.LONG).orElse(0L));
        boolean pending = storage.read("NeedsOfNaturePregnancyPending", (Codec)Codec.BOOL).orElse(false);
        NeedsOfNature.PregnancyVariantData variantData = NeedsOfNature.decodePregnancyVariantData(storage.getOptionalString("NeedsOfNaturePregnancyVariantData").orElse(""));
        this.setPregnancyState(entityTypeId, endTick, pending, variantData);
        this.setPregnancyOffspringCount(storage.getOptionalInt("NeedsOfNaturePregnancyOffspringCount").orElse(0));
    }
}

