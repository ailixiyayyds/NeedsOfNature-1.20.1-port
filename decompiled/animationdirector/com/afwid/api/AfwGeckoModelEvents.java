/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.Event
 *  net.fabricmc.fabric.api.event.EventFactory
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 *  net.minecraft.class_811
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_811;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AfwGeckoModelEvents {
    public static final Event<@NotNull ModelResolver> RESOLVE = EventFactory.createArrayBacked(ModelResolver.class, callbacks -> (entity, entityTypeId, currentModel, currentTexture) -> {
        for (ModelResolver callback : callbacks) {
            ModelOverride resolved = callback.resolve(entity, entityTypeId, currentModel, currentTexture);
            if (resolved == null) continue;
            return resolved;
        }
        return null;
    });
    public static final Event<@NotNull RenderResolver> RESOLVE_RENDER = EventFactory.createArrayBacked(RenderResolver.class, callbacks -> (entity, entityTypeId, currentModel, currentTexture) -> {
        for (RenderResolver callback : callbacks) {
            RenderOverride resolved = callback.resolve(entity, entityTypeId, currentModel, currentTexture);
            if (resolved == null) continue;
            return resolved;
        }
        return null;
    });

    private AfwGeckoModelEvents() {
    }

    @FunctionalInterface
    public static interface RenderResolver {
        @Nullable
        public RenderOverride resolve(class_1309 var1, @Nullable class_2960 var2, class_2960 var3, class_2960 var4);
    }

    public record RenderOverride(@Nullable class_2960 model, @Nullable class_2960 texture, @Nullable List<class_2960> layerTextures, @Nullable Map<String, class_2960> boneTextures, @Nullable Map<String, BoneItemProp> boneItems, @Nullable Map<String, Boolean> boneVisibility, @Nullable Map<String, Set<Integer>> hiddenBoneCubeIndices) {
        public RenderOverride(@Nullable class_2960 model, @Nullable class_2960 texture) {
            this(model, texture, null, null, null, null, null);
        }

        public RenderOverride(@Nullable List<class_2960> layerTextures, @Nullable Map<String, class_2960> boneTextures) {
            this(null, null, layerTextures, boneTextures, null, null, null);
        }

        public RenderOverride(@Nullable List<class_2960> layerTextures, @Nullable Map<String, class_2960> boneTextures, @Nullable Map<String, BoneItemProp> boneItems) {
            this(null, null, layerTextures, boneTextures, boneItems, null, null);
        }

        public RenderOverride(@Nullable class_2960 model, @Nullable class_2960 texture, @Nullable List<class_2960> layerTextures, @Nullable Map<String, class_2960> boneTextures, @Nullable Map<String, BoneItemProp> boneItems, @Nullable Map<String, Boolean> boneVisibility) {
            this(model, texture, layerTextures, boneTextures, boneItems, boneVisibility, null);
        }
    }

    @FunctionalInterface
    public static interface ModelResolver {
        @Nullable
        public ModelOverride resolve(class_1309 var1, @Nullable class_2960 var2, class_2960 var3, class_2960 var4);
    }

    public record ModelOverride(@Nullable class_2960 model, @Nullable class_2960 texture) {
    }

    public record BoneItemProp(class_1799 stack, class_811 displayContext) {
        public BoneItemProp(class_1799 stack) {
            this(stack, class_811.field_4320);
        }

        public BoneItemProp {
            stack = stack == null ? class_1799.field_8037 : stack.method_7972();
            displayContext = displayContext == null ? class_811.field_4320 : displayContext;
        }
    }
}

