package com.afwid.client.render.gecko;

import com.afwid.AnimationFramework;
import com.afwid.api.AfwGeckoModelEvents;
import com.afwid.client.render.gecko.layer.AfwEmissiveTextureGeoLayer;
import com.afwid.client.render.gecko.layer.AfwLayerTextureGeoLayer;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import java.util.List;
import com.afwid.util.AfwEntityVariants;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * Minecraft 1.20.1 replacement-render bridge.
 *
 * <p>The 1.21 build prepared a render-state object and submitted commands in a
 * second phase. 1.20.1 renders entities immediately, so resource resolution
 * and GeckoLib rendering intentionally live in one call.</p>
 */
public final class AfwGeckoReplacedRender {
    private static final Map<Identifier, AfwReplacedEntityRenderer> RENDERERS = new ConcurrentHashMap<>();
    private static final Identifier UNKNOWN_RENDERER_KEY =
            new Identifier("animationframework", "unknown_renderer");
    private static final Identifier PLAYER_TYPE_ID = new Identifier("minecraft", "player");
    private static final float ACTIVE_MODEL_SCALE = 0.96f;

    private AfwGeckoReplacedRender() {
    }

    public static boolean render(LivingEntity entity, float entityYaw, float tickDelta,
                                 MatrixStack matrices, VertexConsumerProvider vertices,
                                 int packedLight) {
        if (entity == null || !AfwClientAnimationRuntime.isActorActive(entity.getUuid())) {
            return false;
        }

        Identifier entityTypeId = Registries.ENTITY_TYPE.getId(entity.getType());
        Identifier animationId =
                AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining(entity.getUuid());
        if (animationId == null) {
            return false;
        }
        UUID instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(entity.getUuid());
        String actorKey = AfwClientAnimationRuntime.findLatestActiveActorKey(entity.getUuid());
        Identifier animationResource =
                AfwGeckoResourceResolver.resolveAnimationResource(animationId, actorKey, entityTypeId);
        double animationSpeed = AfwClientAnimationRuntime.findLatestSpeedForActor(entity.getUuid());
        Double timelineSeconds =
                AfwClientAnimationRuntime.findWarpedAuthoredTimelineSeconds(entity.getUuid(), tickDelta);

        ResolvedResources resources = resolveResources(entity, entityTypeId);
        AfwReplacedEntityRenderer renderer = getOrCreateRenderer(entityTypeId, resources.model());
        if (renderer == null) {
            return false;
        }

        AfwActorAnimatable.RenderContext context = new AfwActorAnimatable.RenderContext(
                entity.getUuid(), instanceId, animationId, animationResource,
                resources.model(), resources.texture(), animationSpeed, timelineSeconds,
                resources.translucent(), resources.layerTextures(), resources.emissiveTextures());
        AfwClientAnimationRuntime.LockedOrientation locked =
                AfwClientAnimationRuntime.getLockedOrientation(entity.getUuid());
        OrientationSnapshot orientation = null;
        float renderYaw = entityYaw;
        if (locked != null) {
            orientation = new OrientationSnapshot(
                    entity.bodyYaw, entity.prevBodyYaw, entity.headYaw, entity.prevHeadYaw,
                    entity.getPitch(), entity.prevPitch);
            entity.bodyYaw = locked.bodyYaw();
            entity.prevBodyYaw = locked.bodyYaw();
            entity.headYaw = locked.headYaw();
            entity.prevHeadYaw = locked.headYaw();
            entity.setPitch(locked.pitch());
            entity.prevPitch = locked.pitch();
            renderYaw = locked.bodyYaw();
        }
        AfwActorAnimatable.INSTANCE.beginRender(context);
        try {
            renderer.render(entity, renderYaw, tickDelta, matrices, vertices, packedLight);
            return true;
        } catch (RuntimeException exception) {
            AnimationFramework.LOGGER.error("[AFW] GeckoLib 1.20.1 render failed", exception);
            return false;
        } finally {
            AfwActorAnimatable.INSTANCE.endRender();
            if (orientation != null) {
                entity.bodyYaw = orientation.bodyYaw();
                entity.prevBodyYaw = orientation.prevBodyYaw();
                entity.headYaw = orientation.headYaw();
                entity.prevHeadYaw = orientation.prevHeadYaw();
                entity.setPitch(orientation.pitch());
                entity.prevPitch = orientation.prevPitch();
            }
        }
    }

    private record OrientationSnapshot(float bodyYaw, float prevBodyYaw,
                                       float headYaw, float prevHeadYaw,
                                       float pitch, float prevPitch) {
    }

    private static AfwReplacedEntityRenderer getOrCreateRenderer(Identifier entityTypeId,
                                                                  Identifier modelId) {
        Identifier key = modelId != null ? modelId
                : entityTypeId != null ? entityTypeId : UNKNOWN_RENDERER_KEY;
        AfwReplacedEntityRenderer existing = RENDERERS.get(key);
        if (existing != null) {
            return existing;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return null;
        }
        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        EntityRendererFactory.Context factoryContext = new EntityRendererFactory.Context(
                dispatcher,
                client.getItemRenderer(),
                client.getBlockRenderManager(),
                dispatcher.getHeldItemRenderer(),
                client.getResourceManager(),
                client.getEntityModelLoader(),
                client.textRenderer);
        AfwReplacedEntityRenderer created = new AfwReplacedEntityRenderer(
                factoryContext, new AfwActorGeoModel(), AfwActorAnimatable.INSTANCE);
        created.withScale(ACTIVE_MODEL_SCALE);
        created.addRenderLayer(new AfwLayerTextureGeoLayer(created));
        created.addRenderLayer(new AfwEmissiveTextureGeoLayer(created));
        AfwReplacedEntityRenderer prior = RENDERERS.putIfAbsent(key, created);
        return prior != null ? prior : created;
    }

    private static ResolvedResources resolveResources(LivingEntity entity, Identifier entityTypeId) {
        AfwGeckoResourceResolver.ModelAndTexture base =
                AfwGeckoResourceResolver.resolveModelAndTexture(entityTypeId);
        Identifier model = base.model();
        Identifier texture = base.texture();
        boolean explicitTexture = false;
        List<Identifier> layerTextures = List.of();

        if (PLAYER_TYPE_ID.equals(entityTypeId) && entity instanceof AbstractClientPlayerEntity player
                && "slim".equals(player.getModel())) {
            Identifier slim = new Identifier("animationframework", "entity/player_slim");
            if (hasModelOrGenderedModel(slim)) {
                model = slim;
            }
        }

        Identifier variant = AfwEntityVariants.resolveVariantModel(entity, model);
        if (variant != null && hasModelOrGenderedModel(variant)) {
            model = variant;
        }

        AfwGeckoModelEvents.ModelOverride modelOverride =
                AfwGeckoModelEvents.RESOLVE.invoker().resolve(entity, entityTypeId, model, texture);
        if (modelOverride != null) {
            if (modelOverride.model() != null) {
                model = modelOverride.model();
            }
            if (modelOverride.texture() != null) {
                texture = modelOverride.texture();
                explicitTexture = true;
            }
        }

        AfwGeckoModelEvents.RenderOverride renderOverride =
                AfwGeckoModelEvents.RESOLVE_RENDER.invoker().resolve(entity, entityTypeId, model, texture);
        if (renderOverride != null) {
            if (renderOverride.model() != null) {
                model = renderOverride.model();
            }
            if (renderOverride.texture() != null) {
                texture = renderOverride.texture();
                explicitTexture = true;
            }
            if (renderOverride.layerTextures() != null) {
                layerTextures = renderOverride.layerTextures().stream()
                        .filter(value -> value != null)
                        .map(AfwVanillaTextureResolver::resolveTexture)
                        .toList();
            }
        }

        Identifier resolvedModelResource = AfwGeckoResourceResolver.resolveGeoModelResource(model);
        if (resolvedModelResource == null) {
            AfwGeckoResourceResolver.ModelAndTexture missing =
                    AfwGeckoResourceResolver.missingModelFallback(entityTypeId);
            model = missing.model();
            texture = missing.texture();
            explicitTexture = true;
        } else {
            model = resolvedModelResource;
        }

        if (!explicitTexture) {
            Identifier vanillaTexture = resolveVanillaTexture(entity);
            if (vanillaTexture != null) {
                texture = vanillaTexture;
            }
        }
        texture = AfwVanillaTextureResolver.resolveTexture(texture);
        boolean translucent = PLAYER_TYPE_ID.equals(entityTypeId)
                || AfwBoneTextureOverrides.isTranslucent(model);
        List<Identifier> configuredEmissiveTextures = AfwBoneTextureOverrides.getEmissiveTextures(model);
        List<Identifier> emissiveTextures = (configuredEmissiveTextures == null
                ? List.<Identifier>of()
                : configuredEmissiveTextures).stream()
                .filter(value -> value != null)
                .map(AfwVanillaTextureResolver::resolveTexture)
                .filter(value -> value != null)
                .toList();
        return new ResolvedResources(model, texture, translucent, layerTextures, emissiveTextures);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Identifier resolveVanillaTexture(LivingEntity entity) {
        if (entity instanceof AbstractClientPlayerEntity player) {
            return player.getSkinTexture();
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return null;
        }
        try {
            EntityRenderer renderer = client.getEntityRenderDispatcher().getRenderer(entity);
            return renderer == null ? null : renderer.getTexture(entity);
        } catch (RuntimeException exception) {
            AnimationFramework.LOGGER.debug("[AFW] Could not resolve vanilla entity texture", exception);
            return null;
        }
    }

    private static boolean hasModelOrGenderedModel(Identifier modelId) {
        if (modelId == null) {
            return false;
        }
        if (AfwGeckoResourceResolver.hasGeoModel(modelId)) {
            return true;
        }
        Identifier male = new Identifier(modelId.getNamespace(), modelId.getPath() + ".m");
        Identifier female = new Identifier(modelId.getNamespace(), modelId.getPath() + ".f");
        return AfwGeckoResourceResolver.hasGeoModel(male)
                || AfwGeckoResourceResolver.hasGeoModel(female);
    }

    private record ResolvedResources(Identifier model, Identifier texture, boolean translucent,
                                     List<Identifier> layerTextures,
                                     List<Identifier> emissiveTextures) {
    }
}
