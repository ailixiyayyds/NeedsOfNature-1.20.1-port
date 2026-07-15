/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.class_11368
 *  net.minecraft.class_11372
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_2960
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
import net.minecraft.class_11368;
import net.minecraft.class_11372;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1297.class})
public abstract class PlayerPregnancyMixin
implements PregnancyHolder {
    @Unique
    @Nullable
    private class_2960 nonPregnancyEntityType;
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
    public class_2960 getPregnantEntityTypeId() {
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
    public void setPregnancyState(@Nullable class_2960 entityTypeId, long endTick, boolean pendingHatch, @Nullable NeedsOfNature.PregnancyVariantData variantData) {
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

    @Inject(method={"method_5647"}, at={@At(value="TAIL")})
    private void non$writePregnancy(class_11372 storage, CallbackInfo ci) {
        String variantJson;
        if (!(this instanceof class_1657)) {
            return;
        }
        if (this.nonPregnancyEntityType == null) {
            return;
        }
        storage.method_71469("NeedsOfNaturePregnancyType", this.nonPregnancyEntityType.toString());
        storage.method_71468("NeedsOfNaturePregnancyEndTick", (Codec)Codec.LONG, (Object)Math.max(0L, this.nonPregnancyEndTick));
        storage.method_71468("NeedsOfNaturePregnancyPending", (Codec)Codec.BOOL, (Object)this.nonPregnancyPendingHatch);
        if (this.nonPregnancyOffspringCount > 0) {
            storage.method_71465("NeedsOfNaturePregnancyOffspringCount", this.nonPregnancyOffspringCount);
        }
        if ((variantJson = NeedsOfNature.encodePregnancyVariantData(this.nonPregnancyVariantData)) != null) {
            storage.method_71469("NeedsOfNaturePregnancyVariantData", variantJson);
        }
    }

    @Inject(method={"method_5651"}, at={@At(value="TAIL")})
    private void non$readPregnancy(class_11368 storage, CallbackInfo ci) {
        class_2960 entityTypeId;
        if (!(this instanceof class_1657)) {
            return;
        }
        String rawType = storage.method_71441("NeedsOfNaturePregnancyType").orElse("");
        class_2960 class_29602 = entityTypeId = rawType.isBlank() ? null : class_2960.method_12829((String)rawType);
        if (entityTypeId == null) {
            this.clearPregnancyState();
            return;
        }
        long endTick = Math.max(0L, storage.method_71426("NeedsOfNaturePregnancyEndTick", (Codec)Codec.LONG).orElse(0L));
        boolean pending = storage.method_71426("NeedsOfNaturePregnancyPending", (Codec)Codec.BOOL).orElse(false);
        NeedsOfNature.PregnancyVariantData variantData = NeedsOfNature.decodePregnancyVariantData(storage.method_71441("NeedsOfNaturePregnancyVariantData").orElse(""));
        this.setPregnancyState(entityTypeId, endTick, pending, variantData);
        this.setPregnancyOffspringCount(storage.method_71439("NeedsOfNaturePregnancyOffspringCount").orElse(0));
    }
}

