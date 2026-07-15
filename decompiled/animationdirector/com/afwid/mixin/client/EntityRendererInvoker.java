/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10017
 *  net.minecraft.class_11659
 *  net.minecraft.class_12075
 *  net.minecraft.class_4587
 *  net.minecraft.class_897
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package com.afwid.mixin.client;

import net.minecraft.class_10017;
import net.minecraft.class_11659;
import net.minecraft.class_12075;
import net.minecraft.class_4587;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_897.class})
public interface EntityRendererInvoker {
    @Invoker(value="method_3936")
    public void afw$render(class_10017 var1, class_4587 var2, class_11659 var3, class_12075 var4);
}

