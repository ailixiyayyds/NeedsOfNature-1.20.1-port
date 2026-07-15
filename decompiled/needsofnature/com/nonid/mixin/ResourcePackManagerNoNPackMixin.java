/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  net.minecraft.class_3264
 *  net.minecraft.class_3279
 *  net.minecraft.class_3283
 *  net.minecraft.class_3285
 *  net.minecraft.class_8580
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

import com.google.common.collect.ImmutableSet;
import com.nonid.mixin.FileResourcePackProviderAccessor;
import com.nonid.pack.NonExternalPackProvider;
import com.nonid.pack.NonGeneratedAccessoryModelPackProvider;
import com.nonid.pack.NonGeneratedAccessoryTagPackProvider;
import com.nonid.pack.NonGeneratedCemPackProvider;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.class_3264;
import net.minecraft.class_3279;
import net.minecraft.class_3283;
import net.minecraft.class_3285;
import net.minecraft.class_8580;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_3283.class})
public abstract class ResourcePackManagerNoNPackMixin {
    @Shadow
    @Final
    @Mutable
    private Set<class_3285> field_14227;

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void non$injectExternalNoNPacks(class_3285[] originalProviders, CallbackInfo ci) {
        boolean needsClientProvider = false;
        boolean needsServerProvider = false;
        class_8580 clientSymlinkFinder = null;
        class_8580 serverSymlinkFinder = null;
        for (class_3285 provider : this.field_14227) {
            if (!(provider instanceof class_3279)) continue;
            class_3279 fileProvider = (class_3279)provider;
            FileResourcePackProviderAccessor accessor = (FileResourcePackProviderAccessor)fileProvider;
            class_3264 type = accessor.non$getType();
            if (type == class_3264.field_14188) {
                needsClientProvider = true;
                if (clientSymlinkFinder != null) continue;
                clientSymlinkFinder = accessor.non$getSymlinkFinder();
                continue;
            }
            if (type != class_3264.field_14190) continue;
            needsServerProvider = true;
            if (serverSymlinkFinder != null) continue;
            serverSymlinkFinder = accessor.non$getSymlinkFinder();
        }
        if (!needsClientProvider && !needsServerProvider) {
            return;
        }
        Path nonPackDir = NonExternalPackProvider.resolveDefaultPacksDir();
        LinkedHashSet<class_3285> updated = new LinkedHashSet<class_3285>(this.field_14227);
        if (needsClientProvider && !ResourcePackManagerNoNPackMixin.containsNoNProvider(updated, class_3264.field_14188)) {
            updated.add(new NonExternalPackProvider(nonPackDir, class_3264.field_14188, clientSymlinkFinder != null ? clientSymlinkFinder : new class_8580(path -> true)));
        }
        if (needsClientProvider && !ResourcePackManagerNoNPackMixin.containsGeneratedCemProvider(updated)) {
            updated.add(new NonGeneratedCemPackProvider(nonPackDir));
        }
        if (needsClientProvider && !ResourcePackManagerNoNPackMixin.containsGeneratedAccessoryModelProvider(updated)) {
            updated.add(new NonGeneratedAccessoryModelPackProvider());
        }
        if (needsServerProvider && !ResourcePackManagerNoNPackMixin.containsNoNProvider(updated, class_3264.field_14190)) {
            updated.add(new NonExternalPackProvider(nonPackDir, class_3264.field_14190, serverSymlinkFinder != null ? serverSymlinkFinder : new class_8580(path -> true)));
        }
        if (needsServerProvider && !ResourcePackManagerNoNPackMixin.containsGeneratedAccessoryTagProvider(updated)) {
            updated.add(new NonGeneratedAccessoryTagPackProvider());
        }
        this.field_14227 = ImmutableSet.copyOf(updated);
    }

    @Unique
    private static boolean containsNoNProvider(Set<class_3285> providers, class_3264 type) {
        for (class_3285 provider : providers) {
            NonExternalPackProvider nonProvider;
            if (!(provider instanceof NonExternalPackProvider) || (nonProvider = (NonExternalPackProvider)provider).getType() != type) continue;
            return true;
        }
        return false;
    }

    @Unique
    private static boolean containsGeneratedCemProvider(Set<class_3285> providers) {
        for (class_3285 provider : providers) {
            if (!(provider instanceof NonGeneratedCemPackProvider)) continue;
            return true;
        }
        return false;
    }

    @Unique
    private static boolean containsGeneratedAccessoryTagProvider(Set<class_3285> providers) {
        for (class_3285 provider : providers) {
            if (!(provider instanceof NonGeneratedAccessoryTagPackProvider)) continue;
            return true;
        }
        return false;
    }

    @Unique
    private static boolean containsGeneratedAccessoryModelProvider(Set<class_3285> providers) {
        for (class_3285 provider : providers) {
            if (!(provider instanceof NonGeneratedAccessoryModelPackProvider)) continue;
            return true;
        }
        return false;
    }
}

