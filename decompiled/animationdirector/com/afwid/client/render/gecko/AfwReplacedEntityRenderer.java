/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10017
 *  net.minecraft.class_12249
 *  net.minecraft.class_1297
 *  net.minecraft.class_1921
 *  net.minecraft.class_2960
 *  net.minecraft.class_5617$class_5618
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoReplacedEntityRenderer
 *  software.bernie.geckolib.renderer.base.BoneSnapshots
 *  software.bernie.geckolib.renderer.base.RenderPassInfo
 */
package com.afwid.client.render.gecko;

import com.afwid.client.render.AfwRenderStateAccess;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import java.util.Map;
import net.minecraft.class_10017;
import net.minecraft.class_12249;
import net.minecraft.class_1297;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_5617;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.base.BoneSnapshots;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

public class AfwReplacedEntityRenderer<T extends GeoAnimatable, E extends class_1297, R extends class_10017>
extends GeoReplacedEntityRenderer<T, E, R> {
    private static final class_2960 PLAYER_TYPE_ID = class_2960.method_60656((String)"player");

    public AfwReplacedEntityRenderer(class_5617.class_5618 context, GeoModel<@NotNull T> model, T animatable) {
        super(context, model, animatable);
    }

    public class_1921 getRenderType(R renderState, class_2960 texture) {
        AfwRenderStateAccess access;
        boolean translucent = (Boolean)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.TRANSLUCENT_RENDER, (Object)false);
        if (translucent) {
            return class_12249.method_76000((class_2960)texture);
        }
        if (renderState instanceof AfwRenderStateAccess && PLAYER_TYPE_ID.equals((Object)(access = (AfwRenderStateAccess)renderState).afw$getEntityTypeId())) {
            return class_12249.method_76000((class_2960)texture);
        }
        return super.getRenderType(renderState, texture);
    }

    public void adjustModelBonesForRender(@NotNull @NotNull RenderPassInfo<@NotNull R> renderPassInfo, @NotNull BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots);
        class_10017 renderState = (class_10017)renderPassInfo.renderState();
        Map boneTextures = (Map)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.BONE_TEXTURES, Map.of());
        Map boneVisibility = (Map)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.BONE_VISIBILITY, Map.of());
        if (boneTextures.isEmpty() && boneVisibility.isEmpty()) {
            return;
        }
        for (String string : boneTextures.keySet()) {
            if (string == null || string.isBlank()) continue;
            snapshots.ifPresent(string, snapshot -> {
                snapshot.skipRender(true);
                snapshot.skipChildrenRender(false);
            });
        }
        for (Map.Entry entry : boneVisibility.entrySet()) {
            String boneName = (String)entry.getKey();
            if (boneName == null || boneName.isBlank() || !Boolean.FALSE.equals(entry.getValue())) continue;
            snapshots.ifPresent(boneName, snapshot -> {
                snapshot.skipRender(true);
                snapshot.skipChildrenRender(false);
            });
        }
    }
}

