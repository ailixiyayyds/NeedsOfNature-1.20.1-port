/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1299
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1937
 *  net.minecraft.class_2940
 *  net.minecraft.class_2941
 *  net.minecraft.class_2943
 *  net.minecraft.class_2945
 *  net.minecraft.class_2945$class_9222
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.GenderHolder;
import net.minecraft.class_1299;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_2940;
import net.minecraft.class_2941;
import net.minecraft.class_2943;
import net.minecraft.class_2945;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1308.class})
public abstract class MobEntityGenderMixin
extends class_1309
implements GenderHolder {
    @Unique
    private static final class_2940<Integer> NON_GENDER = class_2945.method_12791(class_1308.class, (class_2941)class_2943.field_13327);

    protected MobEntityGenderMixin(class_1299<? extends class_1309> entityType, class_1937 world) {
        super(entityType, world);
    }

    @Inject(method={"method_5693"}, at={@At(value="TAIL")})
    private void needsOfNature$initGenderTracker(class_2945.class_9222 builder, CallbackInfo ci) {
        builder.method_56912(NON_GENDER, (Object)0);
    }

    @Override
    public int getGenderMask() {
        return (Integer)this.field_6011.method_12789(NON_GENDER);
    }

    @Override
    public void setGenderMask(int mask) {
        this.field_6011.method_12778(NON_GENDER, (Object)(mask & 3));
    }
}

