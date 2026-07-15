/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.Event
 *  net.fabricmc.fabric.api.event.EventFactory
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.Identifier
 *  net.minecraft.item.ItemDisplayContext
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemDisplayContext;
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
        public RenderOverride resolve(LivingEntity var1, @Nullable Identifier var2, Identifier var3, Identifier var4);
    }

    public record RenderOverride(@Nullable Identifier model, @Nullable Identifier texture, @Nullable List<Identifier> layerTextures, @Nullable Map<String, Identifier> boneTextures, @Nullable Map<String, BoneItemProp> boneItems, @Nullable Map<String, Boolean> boneVisibility, @Nullable Map<String, Set<Integer>> hiddenBoneCubeIndices) {
        public RenderOverride(@Nullable Identifier model, @Nullable Identifier texture) {
            this(model, texture, null, null, null, null, null);
        }

        public RenderOverride(@Nullable List<Identifier> layerTextures, @Nullable Map<String, Identifier> boneTextures) {
            this(null, null, layerTextures, boneTextures, null, null, null);
        }

        public RenderOverride(@Nullable List<Identifier> layerTextures, @Nullable Map<String, Identifier> boneTextures, @Nullable Map<String, BoneItemProp> boneItems) {
            this(null, null, layerTextures, boneTextures, boneItems, null, null);
        }

        public RenderOverride(@Nullable Identifier model, @Nullable Identifier texture, @Nullable List<Identifier> layerTextures, @Nullable Map<String, Identifier> boneTextures, @Nullable Map<String, BoneItemProp> boneItems, @Nullable Map<String, Boolean> boneVisibility) {
            this(model, texture, layerTextures, boneTextures, boneItems, boneVisibility, null);
        }
    }

    @FunctionalInterface
    public static interface ModelResolver {
        @Nullable
        public ModelOverride resolve(LivingEntity var1, @Nullable Identifier var2, Identifier var3, Identifier var4);
    }

    public record ModelOverride(@Nullable Identifier model, @Nullable Identifier texture) {
    }

    public record BoneItemProp(ItemStack stack, ItemDisplayContext displayContext) {
        public BoneItemProp(ItemStack stack) {
            this(stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
        }

        public BoneItemProp {
            stack = stack == null ? ItemStack.EMPTY : stack.copy();
            displayContext = displayContext == null ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : displayContext;
        }
    }
}

