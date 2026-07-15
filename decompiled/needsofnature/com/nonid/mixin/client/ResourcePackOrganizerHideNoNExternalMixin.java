/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_3288
 *  net.minecraft.class_5369
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
import net.minecraft.class_3288;
import net.minecraft.class_5369;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_5369.class})
public abstract class ResourcePackOrganizerHideNoNExternalMixin {
    @Shadow
    @Final
    private List<class_3288> field_25455;
    @Shadow
    @Final
    private List<class_3288> field_25456;

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void non$hideExternalNoNPacksOnInit(CallbackInfo ci) {
        this.non$filterExternalNoNPacks();
    }

    @Inject(method={"method_29981"}, at={@At(value="RETURN")})
    private void non$hideExternalNoNPacksOnRefresh(CallbackInfo ci) {
        this.non$filterExternalNoNPacks();
    }

    @Unique
    private void non$filterExternalNoNPacks() {
        this.field_25455.removeIf(ResourcePackOrganizerHideNoNExternalMixin::non$isNoNExternalPackProfile);
        this.field_25456.removeIf(ResourcePackOrganizerHideNoNExternalMixin::non$isNoNExternalPackProfile);
    }

    @Unique
    private static boolean non$isNoNExternalPackProfile(class_3288 profile) {
        if (profile == null) {
            return false;
        }
        String id = profile.method_14463();
        if (id == null || id.isBlank()) {
            return false;
        }
        return id.startsWith("needsofnature/client/") || id.startsWith("needsofnature/server/");
    }
}

