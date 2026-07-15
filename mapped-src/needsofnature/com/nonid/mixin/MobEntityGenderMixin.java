/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.world.World
 *  net.minecraft.entity.data.TrackedData
 *  net.minecraft.entity.data.TrackedDataHandler
 *  net.minecraft.entity.data.TrackedDataHandlerRegistry
 *  net.minecraft.entity.data.DataTracker
 *  net.minecraft.entity.data.DataTracker$Builder
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.GenderHolder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={MobEntity.class})
public abstract class MobEntityGenderMixin
extends LivingEntity
implements GenderHolder {
    @Unique
    private static final TrackedData<Integer> NON_GENDER = DataTracker.registerData(MobEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.INTEGER);

    protected MobEntityGenderMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method={"initDataTracker"}, at={@At(value="TAIL")})
    private void needsOfNature$initGenderTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(NON_GENDER, (Object)0);
    }

    @Override
    public int getGenderMask() {
        return (Integer)this.dataTracker.get(NON_GENDER);
    }

    @Override
    public void setGenderMask(int mask) {
        this.dataTracker.set(NON_GENDER, (Object)(mask & 3));
    }
}

