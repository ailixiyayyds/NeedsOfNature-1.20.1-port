/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1297
 *  net.minecraft.class_1937
 *  net.minecraft.class_2394
 *  net.minecraft.class_2396
 *  net.minecraft.class_2400
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_638
 *  net.minecraft.class_7923
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
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2394;
import net.minecraft.class_2396;
import net.minecraft.class_2400;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_7923;
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
    private static final Set<class_2960> MISSING_ANIMATION_WARNED = Collections.newSetFromMap(new ConcurrentHashMap());
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
            class_2960 afwAnimationId = (class_2960)renderState.getGeckolibData(AfwGeckoTickets.AFW_ANIMATION_ID);
            if (afwAnimationId == null) {
                test.manager().setAnimatableData(AfwGeckoTickets.LAST_AFW_INSTANCE_ID, null);
                test.manager().setAnimatableData(AfwGeckoTickets.LAST_AFW_ANIMATION_RESOURCE_ID, null);
                return PlayState.STOP;
            }
            class_2960 animationResource = (class_2960)renderState.getGeckolibData(AfwGeckoTickets.AFW_ANIMATION_RESOURCE_ID);
            if (animationResource == null) {
                animationResource = AfwGeckoResourceResolver.toAnimationResource(afwAnimationId);
            }
            if (!AfwGeckoResourceResolver.hasBakedAnimation(animationResource)) {
                AfwActorAnimatable.warnMissingAnimation(animationResource);
                return PlayState.STOP;
            }
            UUID instanceId = renderState.hasGeckolibData(AfwGeckoTickets.AFW_INSTANCE_ID) ? (UUID)renderState.getGeckolibData(AfwGeckoTickets.AFW_INSTANCE_ID) : null;
            UUID lastInstanceId = (UUID)test.manager().getAnimatableData(AfwGeckoTickets.LAST_AFW_INSTANCE_ID);
            class_2960 lastAnimationResource = (class_2960)test.manager().getAnimatableData(AfwGeckoTickets.LAST_AFW_ANIMATION_RESOURCE_ID);
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
        class_2960 particleId = class_2960.method_12829((String)effect.trim());
        if (particleId == null || !class_7923.field_41180.method_10250(particleId)) {
            return;
        }
        class_2396 type = (class_2396)class_7923.field_41180.method_63535(particleId);
        if (!(type instanceof class_2400)) {
            return;
        }
        class_2400 simpleParticle = (class_2400)type;
        UUID actorUuid = (UUID)event.renderState().getGeckolibData(AfwGeckoTickets.ACTOR_UUID);
        if (actorUuid == null) {
            return;
        }
        String locator = ((ParticleKeyframeData)event.keyframeData()).getLocator();
        class_243 locatorPosition = AfwParticleKeyframes.findLocatorPosition(actorUuid, locator);
        if (locatorPosition != null) {
            AfwActorAnimatable.spawnParticle(actorUuid, simpleParticle, locatorPosition);
            return;
        }
        AfwActorAnimatable.spawnAtActorFallback(actorUuid, simpleParticle);
    }

    private static void spawnAtActorFallback(UUID actorUuid, class_2400 particle) {
        class_638 clientWorld;
        class_1297 actor = AfwActorAnimatable.resolveActor(actorUuid);
        if (actor == null) {
            return;
        }
        class_1937 class_19372 = actor.method_73183();
        class_638 world = class_19372 instanceof class_638 ? (clientWorld = (class_638)class_19372) : null;
        AfwActorAnimatable.spawnParticle(world, particle, new class_243(actor.method_23317(), actor.method_23323(0.5), actor.method_23321()));
    }

    private static void spawnParticle(UUID actorUuid, class_2400 particle, class_243 pos) {
        class_638 clientWorld;
        class_1937 class_19372;
        class_638 world;
        class_1297 actor = AfwActorAnimatable.resolveActor(actorUuid);
        class_638 class_6382 = world = actor != null && (class_19372 = actor.method_73183()) instanceof class_638 ? (clientWorld = (class_638)class_19372) : null;
        if (world == null) {
            class_310 client = class_310.method_1551();
            world = client == null ? null : client.field_1687;
        }
        AfwActorAnimatable.spawnParticle(world, particle, pos);
    }

    private static void spawnParticle(class_638 world, class_2400 particle, class_243 pos) {
        if (world == null || particle == null || pos == null || !pos.method_76470()) {
            return;
        }
        world.method_8406((class_2394)particle, pos.field_1352, pos.field_1351, pos.field_1350, 0.0, 0.0, 0.0);
    }

    private static class_1297 resolveActor(UUID actorUuid) {
        if (actorUuid == null) {
            return null;
        }
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1687 == null) {
            return null;
        }
        return client.field_1687.method_66347(actorUuid);
    }

    private static void warnMissingAnimation(class_2960 animationResource) {
        if (!MISSING_ANIMATION_WARNED.add(animationResource)) {
            return;
        }
        if (!AfwClientConfig.get().allowsDebugChat(AfwDebugChatCategory.SETUP)) {
            return;
        }
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return;
        }
        client.field_1724.method_7353((class_2561)class_2561.method_43470((String)("[AFW] Missing animation file on client: " + animationResource.method_12832() + " (skipping)")).method_27692(class_124.field_1054), false);
    }
}

