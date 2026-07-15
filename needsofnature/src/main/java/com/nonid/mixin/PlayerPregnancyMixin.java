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

import com.nonid.NeedsOfNature;
import com.nonid.PregnancyHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method={"writeNbt"}, at={@At(value="TAIL")})
    private void non$writePregnancy(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        String variantJson;
        if (!((Object)this instanceof PlayerEntity)) {
            return;
        }
        if (this.nonPregnancyEntityType == null) {
            return;
        }
        nbt.putString("NeedsOfNaturePregnancyType", this.nonPregnancyEntityType.toString());
        nbt.putLong("NeedsOfNaturePregnancyEndTick", Math.max(0L, this.nonPregnancyEndTick));
        nbt.putBoolean("NeedsOfNaturePregnancyPending", this.nonPregnancyPendingHatch);
        if (this.nonPregnancyOffspringCount > 0) {
            nbt.putInt("NeedsOfNaturePregnancyOffspringCount", this.nonPregnancyOffspringCount);
        }
        if ((variantJson = NeedsOfNature.encodePregnancyVariantData(this.nonPregnancyVariantData)) != null) {
            nbt.putString("NeedsOfNaturePregnancyVariantData", variantJson);
        }
    }

    @Inject(method={"readNbt"}, at={@At(value="TAIL")})
    private void non$readPregnancy(NbtCompound nbt, CallbackInfo ci) {
        Identifier entityTypeId;
        if (!((Object)this instanceof PlayerEntity)) {
            return;
        }
        String rawType = nbt.contains("NeedsOfNaturePregnancyType", NbtElement.STRING_TYPE) ? nbt.getString("NeedsOfNaturePregnancyType") : "";
        Identifier class_29602 = entityTypeId = rawType.isBlank() ? null : Identifier.tryParse((String)rawType);
        if (entityTypeId == null) {
            this.clearPregnancyState();
            return;
        }
        long endTick = Math.max(0L, nbt.getLong("NeedsOfNaturePregnancyEndTick"));
        boolean pending = nbt.getBoolean("NeedsOfNaturePregnancyPending");
        String variantJson = nbt.contains("NeedsOfNaturePregnancyVariantData", NbtElement.STRING_TYPE) ? nbt.getString("NeedsOfNaturePregnancyVariantData") : "";
        NeedsOfNature.PregnancyVariantData variantData = NeedsOfNature.decodePregnancyVariantData(variantJson);
        this.setPregnancyState(entityTypeId, endTick, pending, variantData);
        this.setPregnancyOffspringCount(nbt.getInt("NeedsOfNaturePregnancyOffspringCount"));
    }
}

