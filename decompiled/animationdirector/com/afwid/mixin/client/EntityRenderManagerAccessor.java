/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10017
 *  net.minecraft.class_10201
 *  net.minecraft.class_897
 *  net.minecraft.class_898
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package com.afwid.mixin.client;

import net.minecraft.class_10017;
import net.minecraft.class_10201;
import net.minecraft.class_897;
import net.minecraft.class_898;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_898.class})
public interface EntityRenderManagerAccessor {
    @Accessor(value="field_55290")
    public class_10201 afw$getEquipmentModelLoader();

    @Invoker(value="method_68832")
    public class_897<?, ?> afw$getRenderer(class_10017 var1);
}

