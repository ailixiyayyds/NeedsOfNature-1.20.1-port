package com.afwid.client.render.gecko;

import com.afwid.AfwDebugChatCategory;
import com.afwid.client.config.AfwClientConfig;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.ParticleKeyframeEvent;
import software.bernie.geckolib.core.keyframe.event.data.ParticleKeyframeData;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * GeckoLib 4 animatable used by the replacement entity renderer.
 *
 * <p>GeckoLib 5 receives per-render data through GeoRenderState. Minecraft
 * 1.20.1 and GeckoLib 4 predate that API, so the direct renderer installs a
 * short-lived context while it renders an actor. The instance cache remains
 * shared, but the renderer supplies an actor-specific instance id.</p>
 */
public final class AfwActorAnimatable implements GeoAnimatable {
    public static final AfwActorAnimatable INSTANCE = new AfwActorAnimatable();

    private static final Identifier DEFAULT_MODEL = new Identifier("animationframework", "entity/missingmodel");
    private static final Identifier DEFAULT_TEXTURE = new Identifier("animationframework", "textures/missingmodel.png");
    private static final Identifier PLACEHOLDER_ANIMATIONS = new Identifier("animationframework", "afw/placeholder");
    private static final Map<String, RawAnimation> LOOP_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, RawAnimation> PLAY_ONCE_CACHE = new ConcurrentHashMap<>();
    private static final Set<Identifier> MISSING_ANIMATION_WARNED =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ThreadLocal<RenderContext> renderContext = new ThreadLocal<>();
    private final Map<UUID, PlaybackKey> playbackByActor = new ConcurrentHashMap<>();

    private AfwActorAnimatable() {
    }

    public void beginRender(RenderContext context) {
        this.renderContext.set(context);
    }

    public void endRender() {
        this.renderContext.remove();
    }

    public RenderContext context() {
        return this.renderContext.get();
    }

    public Identifier modelResource() {
        RenderContext context = context();
        return context != null && context.modelId() != null ? context.modelId() : DEFAULT_MODEL;
    }

    public Identifier textureResource() {
        RenderContext context = context();
        return context != null && context.textureId() != null ? context.textureId() : DEFAULT_TEXTURE;
    }

    public Identifier animationResource() {
        RenderContext context = context();
        return context != null && context.animationResourceId() != null
                ? context.animationResourceId() : PLACEHOLDER_ANIMATIONS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<AfwActorAnimatable> controller = new AnimationController<>(
                this, "afw_main", 0, this::animationPredicate);
        controller.setSoundKeyframeHandler(event -> { });
        controller.setParticleKeyframeHandler(AfwActorAnimatable::handleParticleKeyframe);
        controllers.add(controller);
    }

    private PlayState animationPredicate(AnimationState<AfwActorAnimatable> state) {
        RenderContext context = context();
        if (context == null || context.actorUuid() == null || context.animationId() == null) {
            return PlayState.STOP;
        }

        Identifier animationResource = context.animationResourceId();
        if (animationResource == null) {
            animationResource = AfwGeckoResourceResolver.toAnimationResource(context.animationId());
        }
        if (!AfwGeckoResourceResolver.hasBakedAnimation(animationResource)) {
            warnMissingAnimation(animationResource);
            return PlayState.STOP;
        }

        PlaybackKey next = new PlaybackKey(context.instanceId(), animationResource);
        PlaybackKey previous = this.playbackByActor.put(context.actorUuid(), next);
        if (!next.equals(previous)) {
            state.getController().forceAnimationReset();
        }

        double speed = context.animationSpeed();
        if (!Double.isFinite(speed) || speed <= 0.0) {
            speed = 1.0;
        }
        state.setControllerSpeed((float)Math.max(0.1, Math.min(4.0, speed)));

        String key = AfwGeckoResourceResolver.resolveAnimationKey(animationResource);
        boolean loop = AfwClientAnimationRuntime.isAnimationLooping(context.animationId());
        return state.setAndContinue(loop ? loopAnimation(key) : playOnceAnimation(key));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    /**
     * AFW already computes the authoritative warped animation timeline. Feeding
     * it to GeckoLib as ticks keeps all clients aligned without the GeckoLib 5
     * controller seek API, which did not exist in GeckoLib 4.
     */
    @Override
    public double getTick(Object relatedObject) {
        RenderContext context = context();
        if (context != null && context.timelineSeconds() != null
                && Double.isFinite(context.timelineSeconds()) && context.timelineSeconds() >= 0.0) {
            return context.timelineSeconds() * 20.0;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null && client.world != null ? client.world.getTime() : 0.0;
    }

    private static RawAnimation loopAnimation(String key) {
        return LOOP_CACHE.computeIfAbsent(key, value -> RawAnimation.begin().thenLoop(value));
    }

    private static RawAnimation playOnceAnimation(String key) {
        return PLAY_ONCE_CACHE.computeIfAbsent(key, value -> RawAnimation.begin().thenPlayAndHold(value));
    }

    private static void handleParticleKeyframe(ParticleKeyframeEvent<AfwActorAnimatable> event) {
        if (event == null || event.getKeyframeData() == null || event.getAnimatable() == null) {
            return;
        }
        RenderContext context = event.getAnimatable().context();
        if (context == null || context.actorUuid() == null) {
            return;
        }
        ParticleKeyframeData data = event.getKeyframeData();
        String effect = data.getEffect();
        if (effect == null || effect.isBlank()) {
            return;
        }
        Identifier particleId = Identifier.tryParse(effect.trim());
        if (particleId == null || !Registries.PARTICLE_TYPE.containsId(particleId)) {
            return;
        }
        ParticleType<?> type = Registries.PARTICLE_TYPE.get(particleId);
        if (!(type instanceof DefaultParticleType particle)) {
            return;
        }
        Vec3d locatorPosition = AfwParticleKeyframes.findLocatorPosition(context.actorUuid(), data.getLocator());
        if (locatorPosition != null) {
            spawnParticle(context.actorUuid(), particle, locatorPosition);
        } else {
            spawnAtActorFallback(context.actorUuid(), particle);
        }
    }

    private static void spawnAtActorFallback(UUID actorUuid, DefaultParticleType particle) {
        Entity actor = resolveActor(actorUuid);
        if (actor == null || !(actor.getWorld() instanceof ClientWorld world)) {
            return;
        }
        spawnParticle(world, particle, new Vec3d(actor.getX(), actor.getBodyY(0.5), actor.getZ()));
    }

    private static void spawnParticle(UUID actorUuid, DefaultParticleType particle, Vec3d pos) {
        Entity actor = resolveActor(actorUuid);
        ClientWorld world = actor != null && actor.getWorld() instanceof ClientWorld actorWorld
                ? actorWorld : MinecraftClient.getInstance().world;
        spawnParticle(world, particle, pos);
    }

    private static void spawnParticle(ClientWorld world, DefaultParticleType particle, Vec3d pos) {
        if (world == null || particle == null || pos == null
                || !Double.isFinite(pos.x) || !Double.isFinite(pos.y) || !Double.isFinite(pos.z)) {
            return;
        }
        world.addParticle((ParticleEffect)particle, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
    }

    private static Entity resolveActor(UUID actorUuid) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (actorUuid == null || client == null || client.world == null) {
            return null;
        }
        for (Entity entity : client.world.getEntities()) {
            if (actorUuid.equals(entity.getUuid())) {
                return entity;
            }
        }
        return null;
    }

    private static void warnMissingAnimation(Identifier animationResource) {
        if (animationResource == null || !MISSING_ANIMATION_WARNED.add(animationResource)
                || !AfwClientConfig.get().allowsDebugChat(AfwDebugChatCategory.SETUP)) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal("[AFW] Missing animation file on client: "
                    + animationResource.getPath() + " (skipping)").formatted(Formatting.YELLOW), false);
        }
    }

    private record PlaybackKey(UUID instanceId, Identifier animationResource) {
    }

    public record RenderContext(
            UUID actorUuid,
            UUID instanceId,
            Identifier animationId,
            Identifier animationResourceId,
            Identifier modelId,
            Identifier textureId,
            double animationSpeed,
            Double timelineSeconds,
            boolean translucent,
            List<Identifier> layerTextures,
            List<Identifier> emissiveTextures) {
        public RenderContext {
            layerTextures = layerTextures == null ? List.of() : List.copyOf(layerTextures);
            emissiveTextures = emissiveTextures == null ? List.of() : List.copyOf(emissiveTextures);
        }
    }
}
