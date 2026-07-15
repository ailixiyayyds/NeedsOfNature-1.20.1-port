/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10017
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_897
 *  net.minecraft.class_9064
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin.client;

import com.nonid.EnergyHolder;
import com.nonid.client.NonDebugNameHelper;
import net.minecraft.class_10017;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_897;
import net.minecraft.class_9064;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_897.class})
public abstract class EntityRendererDebugNameMixin {
    @Inject(method={"method_3921"}, at={@At(value="HEAD")}, cancellable=true)
    private void needsOfNature$showDebugLabel(class_1297 entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        if (NonDebugNameHelper.shouldShowDebugLabel(entity)) {
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(method={"method_62426"}, at={@At(value="HEAD")}, cancellable=true)
    private void needsOfNature$debugDisplayName(class_1297 entity, CallbackInfoReturnable<class_2561> cir) {
        if (!NonDebugNameHelper.shouldShowDebugLabel(entity)) {
            return;
        }
        if (!(entity instanceof EnergyHolder)) {
            return;
        }
        EnergyHolder holder = (EnergyHolder)entity;
        String energy = holder.getEnergy() + "/" + holder.getMaxEnergy();
        cir.setReturnValue((Object)class_2561.method_43470((String)("T:" + energy + " " + NonDebugNameHelper.resolveGenderSymbol(entity))));
    }

    @Inject(method={"method_62354"}, at={@At(value="TAIL")})
    private void needsOfNature$debugLabelState(class_1297 entity, class_10017 state, float tickDelta, CallbackInfo ci) {
        if (!NonDebugNameHelper.shouldShowDebugLabel(entity)) {
            return;
        }
        if (!(entity instanceof EnergyHolder)) {
            return;
        }
        EnergyHolder holder = (EnergyHolder)entity;
        String energy = holder.getEnergy() + "/" + holder.getMaxEnergy();
        state.field_53337 = class_2561.method_43470((String)("T:" + energy + " " + NonDebugNameHelper.resolveGenderSymbol(entity)));
        class_243 labelPos = entity.method_56072().method_55675(class_9064.field_47745, 0, entity.method_61415(tickDelta));
        if (labelPos == null) {
            labelPos = new class_243(entity.method_23317(), entity.method_23318() + (double)entity.method_17682() + 0.5, entity.method_23321());
        }
        state.field_53338 = labelPos;
    }
}

