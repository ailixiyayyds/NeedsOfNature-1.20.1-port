/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.EntityRenderState
 *  net.minecraft.client.render.RenderLayers
 *  net.minecraft.entity.Entity
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.render.entity.EntityRendererFactory$Context
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
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.entity.Entity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.entity.EntityRendererFactory;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.base.BoneSnapshots;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

public class AfwReplacedEntityRenderer<T extends GeoAnimatable, E extends Entity, R extends EntityRenderState>
extends GeoReplacedEntityRenderer<T, E, R> {
    private static final Identifier PLAYER_TYPE_ID = Identifier.ofVanilla((String)"player");

    public AfwReplacedEntityRenderer(EntityRendererFactory.Context context, GeoModel<@NotNull T> model, T animatable) {
        super(context, model, animatable);
    }

    public RenderLayer getRenderType(R renderState, Identifier texture) {
        AfwRenderStateAccess access;
        boolean translucent = (Boolean)renderState.getOrDefaultGeckolibData(AfwGeckoTickets.TRANSLUCENT_RENDER, (Object)false);
        if (translucent) {
            return RenderLayers.entityTranslucent((Identifier)texture);
        }
        if (renderState instanceof AfwRenderStateAccess && PLAYER_TYPE_ID.equals((Object)(access = (AfwRenderStateAccess)renderState).afw$getEntityTypeId())) {
            return RenderLayers.entityTranslucent((Identifier)texture);
        }
        return super.getRenderType(renderState, texture);
    }

    public void adjustModelBonesForRender(@NotNull @NotNull RenderPassInfo<@NotNull R> renderPassInfo, @NotNull BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots);
        EntityRenderState renderState = (EntityRenderState)renderPassInfo.renderState();
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

