/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resource.ResourcePackProfile
 *  net.minecraft.client.gui.screen.pack.ResourcePackOrganizer
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin.client;

import java.util.List;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ResourcePackOrganizer.class})
public abstract class ResourcePackOrganizerHideNoNExternalMixin {
    @Shadow
    @Final
    private List<ResourcePackProfile> enabledPacks;
    @Shadow
    @Final
    private List<ResourcePackProfile> disabledPacks;

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void non$hideExternalNoNPacksOnInit(CallbackInfo ci) {
        this.non$filterExternalNoNPacks();
    }

    @Inject(method={"refresh"}, at={@At(value="RETURN")})
    private void non$hideExternalNoNPacksOnRefresh(CallbackInfo ci) {
        this.non$filterExternalNoNPacks();
    }

    @Unique
    private void non$filterExternalNoNPacks() {
        this.enabledPacks.removeIf(ResourcePackOrganizerHideNoNExternalMixin::non$isNoNExternalPackProfile);
        this.disabledPacks.removeIf(ResourcePackOrganizerHideNoNExternalMixin::non$isNoNExternalPackProfile);
    }

    @Unique
    private static boolean non$isNoNExternalPackProfile(ResourcePackProfile profile) {
        if (profile == null) {
            return false;
        }
        String id = profile.getId();
        if (id == null || id.isBlank()) {
            return false;
        }
        return id.startsWith("needsofnature/client/") || id.startsWith("needsofnature/server/");
    }
}

