/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.FileResourcePackProvider
 *  net.minecraft.resource.ResourcePackManager
 *  net.minecraft.resource.ResourcePackProvider
 *  net.minecraft.util.path.SymlinkFinder
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin;

import com.nonid.mixin.FileResourcePackProviderAccessor;
import com.nonid.pack.NonExternalPackProvider;
import com.nonid.pack.NonGeneratedAccessoryModelPackProvider;
import com.nonid.pack.NonGeneratedAccessoryTagPackProvider;
import com.nonid.pack.NonGeneratedCemPackProvider;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.FileResourcePackProvider;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.util.path.SymlinkFinder;
import net.minecraft.util.path.AllowedSymlinkPathMatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ResourcePackManager.class})
public abstract class ResourcePackManagerNoNPackMixin {
    @Shadow
    @Final
    @Mutable
    private Set<ResourcePackProvider> providers;

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void non$injectExternalNoNPacks(ResourcePackProvider[] originalProviders, CallbackInfo ci) {
        boolean needsClientProvider = false;
        boolean needsServerProvider = false;
        SymlinkFinder fallbackSymlinkFinder = new SymlinkFinder(new AllowedSymlinkPathMatcher(List.of()));
        for (ResourcePackProvider provider : this.providers) {
            if (!(provider instanceof FileResourcePackProvider)) continue;
            FileResourcePackProvider fileProvider = (FileResourcePackProvider)provider;
            FileResourcePackProviderAccessor accessor = (FileResourcePackProviderAccessor)fileProvider;
            ResourceType type = accessor.non$getType();
            if (type == ResourceType.CLIENT_RESOURCES) {
                needsClientProvider = true;
                continue;
            }
            if (type != ResourceType.SERVER_DATA) continue;
            needsServerProvider = true;
        }
        if (!needsClientProvider && !needsServerProvider) {
            return;
        }
        Path nonPackDir = NonExternalPackProvider.resolveDefaultPacksDir();
        LinkedHashSet<ResourcePackProvider> updated = new LinkedHashSet<ResourcePackProvider>(this.providers);
        if (needsClientProvider && !ResourcePackManagerNoNPackMixin.containsNoNProvider(updated, ResourceType.CLIENT_RESOURCES)) {
            updated.add(new NonExternalPackProvider(nonPackDir, ResourceType.CLIENT_RESOURCES, fallbackSymlinkFinder));
        }
        if (needsClientProvider && !ResourcePackManagerNoNPackMixin.containsGeneratedCemProvider(updated)) {
            updated.add(new NonGeneratedCemPackProvider(nonPackDir));
        }
        if (needsClientProvider && !ResourcePackManagerNoNPackMixin.containsGeneratedAccessoryModelProvider(updated)) {
            updated.add(new NonGeneratedAccessoryModelPackProvider());
        }
        if (needsServerProvider && !ResourcePackManagerNoNPackMixin.containsNoNProvider(updated, ResourceType.SERVER_DATA)) {
            updated.add(new NonExternalPackProvider(nonPackDir, ResourceType.SERVER_DATA, fallbackSymlinkFinder));
        }
        if (needsServerProvider && !ResourcePackManagerNoNPackMixin.containsGeneratedAccessoryTagProvider(updated)) {
            updated.add(new NonGeneratedAccessoryTagPackProvider());
        }
        // Forge adds its own pack finders after the vanilla constructor returns.
        // Keep this mutable when Connector runs the Fabric mixin on Forge.
        this.providers = updated;
    }

    @Unique
    private static boolean containsNoNProvider(Set<ResourcePackProvider> providers, ResourceType type) {
        for (ResourcePackProvider provider : providers) {
            NonExternalPackProvider nonProvider;
            if (!(provider instanceof NonExternalPackProvider) || (nonProvider = (NonExternalPackProvider)provider).getType() != type) continue;
            return true;
        }
        return false;
    }

    @Unique
    private static boolean containsGeneratedCemProvider(Set<ResourcePackProvider> providers) {
        for (ResourcePackProvider provider : providers) {
            if (!(provider instanceof NonGeneratedCemPackProvider)) continue;
            return true;
        }
        return false;
    }

    @Unique
    private static boolean containsGeneratedAccessoryTagProvider(Set<ResourcePackProvider> providers) {
        for (ResourcePackProvider provider : providers) {
            if (!(provider instanceof NonGeneratedAccessoryTagPackProvider)) continue;
            return true;
        }
        return false;
    }

    @Unique
    private static boolean containsGeneratedAccessoryModelProvider(Set<ResourcePackProvider> providers) {
        for (ResourcePackProvider provider : providers) {
            if (!(provider instanceof NonGeneratedAccessoryModelPackProvider)) continue;
            return true;
        }
        return false;
    }
}

