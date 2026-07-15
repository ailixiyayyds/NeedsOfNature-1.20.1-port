/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.FileResourcePackProvider
 *  net.minecraft.util.path.SymlinkFinder
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package com.nonid.mixin;

import net.minecraft.resource.ResourceType;
import net.minecraft.resource.FileResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={FileResourcePackProvider.class})
public interface FileResourcePackProviderAccessor {
    @Accessor(value="type")
    public ResourceType non$getType();

}

