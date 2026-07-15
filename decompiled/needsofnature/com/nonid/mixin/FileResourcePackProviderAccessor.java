/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_3264
 *  net.minecraft.class_3279
 *  net.minecraft.class_8580
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package com.nonid.mixin;

import net.minecraft.class_3264;
import net.minecraft.class_3279;
import net.minecraft.class_8580;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_3279.class})
public interface FileResourcePackProviderAccessor {
    @Accessor(value="field_40045")
    public class_3264 non$getType();

    @Accessor(value="field_45054")
    public class_8580 non$getSymlinkFinder();
}

