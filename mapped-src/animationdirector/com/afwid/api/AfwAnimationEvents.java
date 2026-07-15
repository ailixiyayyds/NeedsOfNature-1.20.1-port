/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.Event
 *  net.fabricmc.fabric.api.event.EventFactory
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
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
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
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
        public void onStageAdvance(ServerWorld var1, UUID var2, Identifier var3, List<UUID> var4, List<String> var5, long var6);
    }

    @FunctionalInterface
    public static interface Stop {
        public void onStop(ServerWorld var1, UUID var2, Identifier var3, List<UUID> var4, List<String> var5, List<AnimationStageInfo> var6);
    }

    @FunctionalInterface
    public static interface AllowStart {
        public boolean allowStart(ServerWorld var1, Identifier var2, List<UUID> var3, List<String> var4, List<AnimationStageInfo> var5, @Nullable ServerPlayerEntity var6, AfwDamageBehavior var7, boolean var8, Map<String, String> var9);
    }

    @FunctionalInterface
    public static interface Start {
        public void onStart(ServerWorld var1, UUID var2, Identifier var3, List<UUID> var4, List<String> var5, List<AnimationStageInfo> var6, long var7, @Nullable ServerPlayerEntity var9, AfwDamageBehavior var10, boolean var11);
    }
}

