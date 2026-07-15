/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Formatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.particle.ParticleType
 *  net.minecraft.particle.SimpleParticleType
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.registry.Registries
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animatable.manager.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.RawAnimation
 *  software.bernie.geckolib.animation.object.LoopType
 *  software.bernie.geckolib.animation.object.PlayState
 *  software.bernie.geckolib.animation.state.AnimationTimeline
 *  software.bernie.geckolib.animation.state.KeyFrameEvent
 *  software.bernie.geckolib.cache.animation.keyframeevent.ParticleKeyframeData
 *  software.bernie.geckolib.renderer.base.GeoRenderState
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package com.afwid.client.render.gecko;

import com.afwid.AfwDebugChatCategory;
import com.afwid.client.config.AfwClientConfig;
import com.afwid.client.render.gecko.AfwGeckoResourceResolver;
import com.afwid.client.render.gecko.AfwGeckoTickets;
import com.afwid.client.render.gecko.AfwParticleKeyframes;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.Formatting;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.object.LoopType;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.animation.state.AnimationTimeline;
import software.bernie.geckolib.animation.state.KeyFrameEvent;
import software.bernie.geckolib.cache.animation.keyframeevent.ParticleKeyframeData;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class AfwActorAnimatable
implements GeoAnimatable {
    public static final AfwActorAnimatable INSTANCE = new AfwActorAnimatable();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
    private static final Map<String, RawAnimation> LOOP_CACHE = new ConcurrentHashMap<String, RawAnimation>();
    private static final Map<String, RawAnimation> PLAY_ONCE_CACHE = new ConcurrentHashMap<String, RawAnimation>();
    private static final Set<Identifier> MISSING_ANIMATION_WARNED = Collections.newSetFromMap(new ConcurrentHashMap());
    private static final double LOOP_WRAP_EPSILON_SECONDS = 0.004166666666666667;
    private static final LoopType AFW_LOOP_NO_ZERO_WRAP = LoopType.register((String)"afw_loop_no_zero_wrap", (animatable, animationPoint, timelineStage, renderState, controller) -> {
        double wrapped = timelineStage.startTime() + (controller.getCurrentTimelineTime() - timelineStage.endTime());
        double start = timelineStage.startTime();
        double end = timelineStage.endTime();
        if (wrapped <= start) {
            wrapped = start + 0.004166666666666667;
        }
        if (wrapped >= end) {
            wrapped = Math.nextDown(end);
        }
        controller.setTimelineTime(wrapped);
        return true;
    });

    private AfwActorAnimatable() {
    }

    /*
     * Issues handling annotations - annotations may be inaccurate
     */
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        @NotNull AnimationController controller = new AnimationController("afw_main", test -> {
            double holdTime;
            AnimationTimeline timeline;
            RawAnimation toPlay;
            Double s;
            GeoRenderState renderState = test.renderState();
            if (!renderState.hasGeckolibData(AfwGeckoTickets.AFW_ANIMATION_ID)) {
                test.manager().setAnimatableData(AfwGeckoTickets.LAST_AFW_INSTANCE_ID, null);
                test.manager().setAnimatableData(AfwGeckoTickets.LAST_AFW_ANIMATION_RESOURCE_ID, null);
                return PlayState.STOP;
            }
            Identifier afwAnimationId = (Identifier)renderState.getGeckolibData(AfwGeckoTickets.AFW_ANIMATION_ID);
            if (afwAnimationId == null) {
                test.manager().setAnimatableData(AfwGeckoTickets.LAST_AFW_INSTANCE_ID, null);
                test.manager().setAnimatableData(AfwGeckoTickets.LAST_AFW_ANIMATION_RESOURCE_ID, null);
                return PlayState.STOP;
            }
            Identifier animationResource = (Identifier)renderState.getGeckolibData(AfwGeckoTickets.AFW_ANIMATION_RESOURCE_ID);
            if (animationResource == null) {
                animationResource = AfwGeckoResourceResolver.toAnimationResource(afwAnimationId);
            }
            if (!AfwGeckoResourceResolver.hasBakedAnimation(animationResource)) {
                AfwActorAnimatable.warnMissingAnimation(animationResource);
                return PlayState.STOP;
            }
            UUID instanceId = renderState.hasGeckolibData(AfwGeckoTickets.AFW_INSTANCE_ID) ? (UUID)renderState.getGeckolibData(AfwGeckoTickets.AFW_INSTANCE_ID) : null;
            UUID lastInstanceId = (UUID)test.manager().getAnimatableData(AfwGeckoTickets.LAST_AFW_INSTANCE_ID);
            Identifier lastAnimationResource = (Identifier)test.manager().getAnimatableData(AfwGeckoTickets.LAST_AFW_ANIMATION_RESOURCE_ID);
            boolean reloadController = false;
            if (instanceId != null && !instanceId.equals(lastInstanceId)) {
                reloadController = true;
                test.manager().setAnimatableData(AfwGeckoTickets.LAST_AFW_INSTANCE_ID, (Object)instanceId);
            }
            if (lastAnimationResource == null || !lastAnimationResource.equals((Object)animationResource)) {
                reloadController = true;
                test.manager().setAnimatableData(AfwGeckoTickets.LAST_AFW_ANIMATION_RESOURCE_ID, (Object)animationResource);
            }
            if (reloadController) {
                test.controller().reset();
            }
            double speed = 1.0;
            if (renderState.hasGeckolibData(AfwGeckoTickets.ANIMATION_SPEED) && (s = (Double)renderState.getGeckolibData(AfwGeckoTickets.ANIMATION_SPEED)) != null && Double.isFinite(s) && s > 0.0) {
                speed = Math.max(0.1, Math.min(4.0, s));
            }
            boolean loop = AfwClientAnimationRuntime.isAnimationLooping(afwAnimationId);
            String animationKey = AfwGeckoResourceResolver.resolveAnimationKey(animationResource);
            RawAnimation rawAnimation = toPlay = loop ? AfwActorAnimatable.loopAnimation(animationKey) : AfwActorAnimatable.playOnceAnimation(animationKey);
            if (!loop && test.isCurrentAnimation(toPlay) && test.controller().hasAnimationFinished() && (timeline = test.controller().getTimeline()) != null && Double.isFinite(holdTime = Math.nextDown(timeline.lastAnimationEndTime())) && holdTime >= 0.0) {
                test.controller().setTimelineTime(holdTime);
            }
            PlayState playState = test.setAndContinue(toPlay);
            test.setControllerSpeed((float)speed);
            Double timelineSeconds = (Double)renderState.getGeckolibData(AfwGeckoTickets.ANIMATION_TIMELINE_SECONDS);
            if (timelineSeconds != null && Double.isFinite(timelineSeconds) && timelineSeconds >= 0.0) {
                test.controller().setTimelineTime(timelineSeconds.doubleValue());
            }
            return playState;
        });
        controller.setSoundKeyframeHandler(event -> {});
        controller.setParticleKeyframeHandler(AfwActorAnimatable::handleParticleKeyframe);
        controllers.add(controller);
    }

    @NotNull
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private static RawAnimation loopAnimation(String key) {
        return LOOP_CACHE.computeIfAbsent(key, k -> RawAnimation.begin().then(k, AFW_LOOP_NO_ZERO_WRAP));
    }

    private static RawAnimation playOnceAnimation(String key) {
        return PLAY_ONCE_CACHE.computeIfAbsent(key, k -> RawAnimation.begin().thenPlay(k));
    }

    private static void handleParticleKeyframe(KeyFrameEvent<AfwActorAnimatable, ParticleKeyframeData> event) {
        if (event == null || event.keyframeData() == null || event.renderState() == null) {
            return;
        }
        String effect = ((ParticleKeyframeData)event.keyframeData()).getEffect();
        if (effect == null || effect.isBlank()) {
            return;
        }
        Identifier particleId = Identifier.tryParse((String)effect.trim());
        if (particleId == null || !Registries.PARTICLE_TYPE.containsId(particleId)) {
            return;
        }
        ParticleType type = (ParticleType)Registries.PARTICLE_TYPE.get(particleId);
        if (!(type instanceof SimpleParticleType)) {
            return;
        }
        SimpleParticleType simpleParticle = (SimpleParticleType)type;
        UUID actorUuid = (UUID)event.renderState().getGeckolibData(AfwGeckoTickets.ACTOR_UUID);
        if (actorUuid == null) {
            return;
        }
        String locator = ((ParticleKeyframeData)event.keyframeData()).getLocator();
        Vec3d locatorPosition = AfwParticleKeyframes.findLocatorPosition(actorUuid, locator);
        if (locatorPosition != null) {
            AfwActorAnimatable.spawnParticle(actorUuid, simpleParticle, locatorPosition);
            return;
        }
        AfwActorAnimatable.spawnAtActorFallback(actorUuid, simpleParticle);
    }

    private static void spawnAtActorFallback(UUID actorUuid, SimpleParticleType particle) {
        ClientWorld clientWorld;
        Entity actor = AfwActorAnimatable.resolveActor(actorUuid);
        if (actor == null) {
            return;
        }
        World class_19372 = actor.getEntityWorld();
        ClientWorld world = class_19372 instanceof ClientWorld ? (clientWorld = (ClientWorld)class_19372) : null;
        AfwActorAnimatable.spawnParticle(world, particle, new Vec3d(actor.getX(), actor.getBodyY(0.5), actor.getZ()));
    }

    private static void spawnParticle(UUID actorUuid, SimpleParticleType particle, Vec3d pos) {
        ClientWorld clientWorld;
        World class_19372;
        ClientWorld world;
        Entity actor = AfwActorAnimatable.resolveActor(actorUuid);
        ClientWorld NarrationMessageBuilder = world = actor != null && (class_19372 = actor.getEntityWorld()) instanceof ClientWorld ? (clientWorld = (ClientWorld)class_19372) : null;
        if (world == null) {
            MinecraftClient client = MinecraftClient.getInstance();
            world = client == null ? null : client.world;
        }
        AfwActorAnimatable.spawnParticle(world, particle, pos);
    }

    private static void spawnParticle(ClientWorld world, SimpleParticleType particle, Vec3d pos) {
        if (world == null || particle == null || pos == null || !pos.isFinite()) {
            return;
        }
        world.addParticleClient((ParticleEffect)particle, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
    }

    private static Entity resolveActor(UUID actorUuid) {
        if (actorUuid == null) {
            return null;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) {
            return null;
        }
        return client.world.getEntity(actorUuid);
    }

    private static void warnMissingAnimation(Identifier animationResource) {
        if (!MISSING_ANIMATION_WARNED.add(animationResource)) {
            return;
        }
        if (!AfwClientConfig.get().allowsDebugChat(AfwDebugChatCategory.SETUP)) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return;
        }
        client.player.sendMessage((Text)Text.literal((String)("[AFW] Missing animation file on client: " + animationResource.getPath() + " (skipping)")).formatted(Formatting.YELLOW), false);
    }
}

