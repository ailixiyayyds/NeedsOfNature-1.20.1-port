/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.Event
 *  net.fabricmc.fabric.api.event.EventFactory
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.api;

import com.afwid.api.AfwDamageBehavior;
import com.afwid.network.AnimationStageInfo;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AfwAnimationEvents {
    public static final Event<@NotNull Start> START = EventFactory.createArrayBacked(Start.class, callbacks -> (world, instanceId, animationId, actorUuids, actorKeys, stages, startTick, requester, damageBehavior, ignoreAttackers) -> {
        for (Start callback : callbacks) {
            callback.onStart(world, instanceId, animationId, actorUuids, actorKeys, stages, startTick, requester, damageBehavior, ignoreAttackers);
        }
    });
    public static final Event<@NotNull AllowStart> ALLOW_START = EventFactory.createArrayBacked(AllowStart.class, callbacks -> (world, animationId, actorUuids, actorKeys, stages, requester, damageBehavior, ignoreAttackers, metadata) -> {
        for (AllowStart callback : callbacks) {
            if (callback.allowStart(world, animationId, actorUuids, actorKeys, stages, requester, damageBehavior, ignoreAttackers, metadata)) continue;
            return false;
        }
        return true;
    });
    public static final Event<@NotNull Stop> STOP = EventFactory.createArrayBacked(Stop.class, callbacks -> (world, instanceId, animationId, actorUuids, actorKeys, stages) -> {
        for (Stop callback : callbacks) {
            callback.onStop(world, instanceId, animationId, actorUuids, actorKeys, stages);
        }
    });
    public static final Event<@NotNull StageAdvance> STAGE_ADVANCE = EventFactory.createArrayBacked(StageAdvance.class, callbacks -> (world, instanceId, animationId, actorUuids, actorKeys, advanceTick) -> {
        for (StageAdvance callback : callbacks) {
            callback.onStageAdvance(world, instanceId, animationId, actorUuids, actorKeys, advanceTick);
        }
    });

    private AfwAnimationEvents() {
    }

    @FunctionalInterface
    public static interface StageAdvance {
        public void onStageAdvance(class_3218 var1, UUID var2, class_2960 var3, List<UUID> var4, List<String> var5, long var6);
    }

    @FunctionalInterface
    public static interface Stop {
        public void onStop(class_3218 var1, UUID var2, class_2960 var3, List<UUID> var4, List<String> var5, List<AnimationStageInfo> var6);
    }

    @FunctionalInterface
    public static interface AllowStart {
        public boolean allowStart(class_3218 var1, class_2960 var2, List<UUID> var3, List<String> var4, List<AnimationStageInfo> var5, @Nullable class_3222 var6, AfwDamageBehavior var7, boolean var8, Map<String, String> var9);
    }

    @FunctionalInterface
    public static interface Start {
        public void onStart(class_3218 var1, UUID var2, class_2960 var3, List<UUID> var4, List<String> var5, List<AnimationStageInfo> var6, long var7, @Nullable class_3222 var9, AfwDamageBehavior var10, boolean var11);
    }
}

