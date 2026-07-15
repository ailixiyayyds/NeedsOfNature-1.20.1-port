/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ModInitializer
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
 *  net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.class_11560
 *  net.minecraft.class_124
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_2561
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_5250
 *  net.minecraft.class_8710
 *  net.minecraft.server.MinecraftServer
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.afwid;

import com.afwid.AfwDebugChatCategory;
import com.afwid.AfwDebugChatMode;
import com.afwid.api.AfwDamageBehavior;
import com.afwid.data.AfwAnimationDefinitions;
import com.afwid.network.AdjustAnimationSpeedC2SPayload;
import com.afwid.network.AdvanceAnimationStageS2CPayload;
import com.afwid.network.AnimationSpeedUpdateS2CPayload;
import com.afwid.network.AnimationStageInfo;
import com.afwid.network.DebugAdvanceStageC2SPayload;
import com.afwid.network.DebugChatPreferenceC2SPayload;
import com.afwid.network.DebugJoinAnimationC2SPayload;
import com.afwid.network.DebugStartAnimationC2SPayload;
import com.afwid.network.DebugStopAllAnimationsC2SPayload;
import com.afwid.network.DebugStopAnimationC2SPayload;
import com.afwid.network.StartAnimationS2CPayload;
import com.afwid.network.StopAllAnimationsS2CPayload;
import com.afwid.network.StopAnimationS2CPayload;
import com.afwid.server.AfwServerAnimationController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_11560;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5250;
import net.minecraft.class_8710;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimationFramework
implements ModInitializer {
    public static final String MOD_ID = "animationframework";
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"animationframework");
    private static final Map<UUID, AfwDebugChatMode> DEBUG_CHAT_PREF = new ConcurrentHashMap<UUID, AfwDebugChatMode>();
    private static final int MAX_PENDING_SETUP_CHAT_MESSAGES = 100;
    private static final List<PendingSetupChatMessage> PENDING_SETUP_CHAT = new ArrayList<PendingSetupChatMessage>();

    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(DebugStartAnimationC2SPayload.ID, DebugStartAnimationC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DebugStopAllAnimationsC2SPayload.ID, DebugStopAllAnimationsC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DebugStopAnimationC2SPayload.ID, DebugStopAnimationC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DebugChatPreferenceC2SPayload.ID, DebugChatPreferenceC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DebugJoinAnimationC2SPayload.ID, DebugJoinAnimationC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DebugAdvanceStageC2SPayload.ID, DebugAdvanceStageC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(AdjustAnimationSpeedC2SPayload.ID, AdjustAnimationSpeedC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StartAnimationS2CPayload.ID, StartAnimationS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AdvanceAnimationStageS2CPayload.ID, AdvanceAnimationStageS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StopAllAnimationsS2CPayload.ID, StopAllAnimationsS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StopAnimationS2CPayload.ID, StopAnimationS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AnimationSpeedUpdateS2CPayload.ID, AnimationSpeedUpdateS2CPayload.CODEC);
        AfwAnimationDefinitions.registerReloadListener();
        AfwServerAnimationController.init();
        ServerPlayNetworking.registerGlobalReceiver(DebugStartAnimationC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            class_3222 player = context.player();
            if (!AnimationFramework.requireDebugControlPermission(player, "start")) {
                return;
            }
            AfwDamageBehavior behavior = AfwDamageBehavior.fromId(payload.damageBehaviorId(), AfwDamageBehavior.STOP_ON_DAMAGE);
            AnimationFramework.handleDebugStartRequest(player, payload.actorEntityIds(), behavior, payload.ignoreAttackers(), payload.anchorEntityId());
        }));
        ServerPlayNetworking.registerGlobalReceiver(DebugStopAllAnimationsC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            class_3222 player = context.player();
            if (!AnimationFramework.requireDebugControlPermission(player, "stop_all")) {
                return;
            }
            class_3218 patt0$temp = player.method_51469();
            if (!(patt0$temp instanceof class_3218)) {
                return;
            }
            class_3218 world = patt0$temp;
            long now = world.method_75260();
            long stopTick = now + 1L;
            for (class_3222 p : context.server().method_3760().method_14571()) {
                if (p.method_51469() != world) continue;
                ServerPlayNetworking.send((class_3222)p, (class_8710)new StopAllAnimationsS2CPayload(stopTick));
            }
            AfwServerAnimationController.clearAllInstancesInWorld(world);
            AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.ALWAYS, (class_2561)class_2561.method_43471((String)"debug.animationframework.stop_all_broadcast_queued"), true);
        }));
        ServerPlayNetworking.registerGlobalReceiver(DebugStopAnimationC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            class_3222 player = context.player();
            if (!AnimationFramework.requireDebugControlPermission(player, "stop_instance")) {
                return;
            }
            AfwServerAnimationController.stopInstanceAndBroadcast(player, payload.instanceId());
        }));
        ServerPlayNetworking.registerGlobalReceiver(DebugChatPreferenceC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            UUID uuid = context.player().method_5667();
            AfwDebugChatMode mode = AfwDebugChatMode.fromId(payload.modeId(), AfwDebugChatMode.SETUP_ERRORS);
            DEBUG_CHAT_PREF.put(uuid, mode);
        }));
        ServerPlayNetworking.registerGlobalReceiver(DebugJoinAnimationC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            class_3222 player = context.player();
            if (!AnimationFramework.requireDebugControlPermission(player, "join")) {
                return;
            }
            AnimationFramework.handleDebugJoinRequest(player, payload.instanceId(), payload.actorEntityIds());
        }));
        ServerPlayNetworking.registerGlobalReceiver(DebugAdvanceStageC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            class_3222 player = context.player();
            if (!AnimationFramework.requireDebugControlPermission(player, "advance_stage")) {
                return;
            }
            class_3218 patt0$temp = player.method_51469();
            if (!(patt0$temp instanceof class_3218)) {
                return;
            }
            class_3218 world = patt0$temp;
            AfwServerAnimationController.advanceStage(world, payload.instanceId());
        }));
        ServerPlayNetworking.registerGlobalReceiver(AdjustAnimationSpeedC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            class_3222 player = context.player();
            if (!AnimationFramework.requireDebugControlPermission(player, "adjust_speed")) {
                return;
            }
            class_3218 patt0$temp = player.method_51469();
            if (!(patt0$temp instanceof class_3218)) {
                return;
            }
            class_3218 world = patt0$temp;
            AfwServerAnimationController.adjustSpeed(world, payload.instanceId(), payload.multiplier(), player);
        }));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> server.execute(() -> AfwServerAnimationController.sendActiveInstancesToPlayer(handler.field_14140)));
        ServerTickEvents.END_SERVER_TICK.register(AnimationFramework::flushPendingSetupChat);
        LOGGER.info("[{}] Initialized", (Object)MOD_ID);
    }

    public static void logSetupWarning(String template, Object ... args) {
        LOGGER.warn(template, args);
        AnimationFramework.queueSetupChat(AnimationFramework.formatLogTemplate(template, args));
    }

    public static void logSetupError(String template, Object ... args) {
        LOGGER.error(template, args);
        AnimationFramework.queueSetupChat(AnimationFramework.formatLogTemplate(template, args));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void queueSetupChat(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        List<PendingSetupChatMessage> list = PENDING_SETUP_CHAT;
        synchronized (list) {
            if (PENDING_SETUP_CHAT.size() >= 100) {
                PENDING_SETUP_CHAT.remove(0);
            }
            PENDING_SETUP_CHAT.add(new PendingSetupChatMessage((class_2561)class_2561.method_43470((String)message), new HashSet<UUID>()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void flushPendingSetupChat(MinecraftServer server) {
        if (server == null) {
            return;
        }
        List players = server.method_3760().method_14571();
        if (players.isEmpty()) {
            return;
        }
        List<PendingSetupChatMessage> list = PENDING_SETUP_CHAT;
        synchronized (list) {
            PENDING_SETUP_CHAT.removeIf(message -> {
                for (class_3222 player : players) {
                    if (player == null || message.sentTo().contains(player.method_5667())) continue;
                    if (AnimationFramework.shouldSendDebugChat(player, AfwDebugChatCategory.SETUP)) {
                        AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.SETUP, message.message(), false);
                    }
                    message.sentTo().add(player.method_5667());
                }
                return !message.sentTo().isEmpty() && message.sentTo().containsAll(players.stream().map(class_1297::method_5667).toList());
            });
        }
    }

    public static String formatLogTemplate(String template, Object ... args) {
        if (template == null) {
            return "";
        }
        if (args == null || args.length == 0) {
            return template;
        }
        StringBuilder out = new StringBuilder(template.length() + args.length * 8);
        int argIndex = 0;
        for (int i = 0; i < template.length(); ++i) {
            char c = template.charAt(i);
            if (c == '{' && i + 1 < template.length() && template.charAt(i + 1) == '}' && argIndex < args.length) {
                Object arg;
                if (!((arg = args[argIndex++]) instanceof Throwable)) {
                    out.append(String.valueOf(arg));
                }
                ++i;
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }

    public static boolean shouldSendDebugChat(class_3222 player, AfwDebugChatCategory category) {
        if (player == null) {
            return false;
        }
        AfwDebugChatMode mode = DEBUG_CHAT_PREF.getOrDefault(player.method_5667(), AfwDebugChatMode.SETUP_ERRORS);
        return mode.allows(category);
    }

    public static boolean shouldSendDebugChat(class_3222 player, AfwDebugChatCategory category, boolean force) {
        return force || AnimationFramework.shouldSendDebugChat(player, category);
    }

    public static void sendDebugChat(class_3222 player, AfwDebugChatCategory category, String message, boolean force) {
        AnimationFramework.sendDebugChat(player, category, (class_2561)class_2561.method_43470((String)message), force);
    }

    public static void sendDebugChat(class_3222 player, AfwDebugChatCategory category, class_2561 message, boolean force) {
        if (!AnimationFramework.shouldSendDebugChat(player, category, force)) {
            return;
        }
        player.method_7353((class_2561)message.method_27661().method_27692(AnimationFramework.chatColor(category)), false);
    }

    private static class_124 chatColor(AfwDebugChatCategory category) {
        if (category == null) {
            return class_124.field_1068;
        }
        return switch (category) {
            default -> throw new MatchException(null, null);
            case AfwDebugChatCategory.ALWAYS -> class_124.field_1063;
            case AfwDebugChatCategory.SETUP -> class_124.field_1054;
            case AfwDebugChatCategory.WARNING -> class_124.field_1076;
            case AfwDebugChatCategory.ERROR -> class_124.field_1061;
            case AfwDebugChatCategory.INFO -> class_124.field_1068;
        };
    }

    private static boolean requireDebugControlPermission(class_3222 player, String action) {
        class_3218 world;
        class_3218 class_32182;
        if (player != null && (class_32182 = player.method_51469()) instanceof class_3218 && (world = class_32182).method_8503().method_3760().method_14569(new class_11560(player.method_7334()))) {
            return true;
        }
        if (player != null) {
            LOGGER.warn("Denied AFW debug control '{}' from non-OP player {}", (Object)action, (Object)player.method_5477().getString());
            AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.WARNING, (class_2561)class_2561.method_43470((String)"[AFW] Debug controls require operator permissions."), true);
        }
        return false;
    }

    private static void handleDebugStartRequest(class_3222 player, List<Integer> requestedActorIds, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, int anchorEntityId) {
        if (requestedActorIds.isEmpty()) {
            return;
        }
        ArrayList<class_1297> resolved = new ArrayList<class_1297>();
        class_1297 anchorEntity = null;
        for (int entityId : requestedActorIds) {
            class_1297 entity = player.method_51469().method_8469(entityId);
            if (!(entity instanceof class_1309)) continue;
            resolved.add(entity);
            if (anchorEntity != null || entityId != anchorEntityId) continue;
            anchorEntity = entity;
        }
        if (resolved.isEmpty()) {
            return;
        }
        resolved.sort(Comparator.comparingInt(class_1297::method_5628));
        AfwDamageBehavior safeBehavior = damageBehavior == null ? AfwDamageBehavior.STOP_ON_DAMAGE : damageBehavior;
        UUID anchorUuid = anchorEntity != null ? anchorEntity.method_5667() : null;
        AfwServerAnimationController.MatchedStartRequest start = AfwServerAnimationController.startEligibleMatchedNow(player.method_51469(), player, resolved, Set.of(), safeBehavior, ignoreAttackers, anchorUuid, Map.of(), true);
        String chosenStr = start == null ? "(none)" : start.animationId().method_12832();
        List<Object> actorKeys = start == null ? List.of() : start.actorKeys();
        String keysStr = actorKeys.isEmpty() ? "none" : actorKeys.toString();
        class_5250 msg = class_2561.method_43469((String)"debug.animationframework.server_eval", (Object[])new Object[]{resolved.size(), chosenStr, keysStr});
        AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.ALWAYS, (class_2561)msg, true);
        if (start == null) {
            return;
        }
        LOGGER.info("Debug request: player={} chosen={} actors={}", new Object[]{player.method_5477().getString(), start.animationId(), resolved.size()});
    }

    private static void handleDebugJoinRequest(class_3222 player, UUID instanceId, List<Integer> requestedActorIds) {
        if (player == null || requestedActorIds == null || requestedActorIds.isEmpty()) {
            return;
        }
        class_3218 class_32182 = player.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            return;
        }
        class_3218 world = class_32182;
        AfwServerAnimationController.ActiveInstanceSnapshot snapshot = AfwServerAnimationController.getActiveInstanceSnapshot(world, instanceId);
        if (snapshot == null) {
            AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.WARNING, (class_2561)class_2561.method_43471((String)"debug.animationframework.join_failed_no_active_instance"), true);
            return;
        }
        AnimationStageInfo currentStage = AfwServerAnimationController.getCurrentStage(world, instanceId);
        if (currentStage == null || !currentStage.allowJoin()) {
            AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.WARNING, (class_2561)class_2561.method_43471((String)"debug.animationframework.join_denied_stage_disallows"), true);
            return;
        }
        ArrayList<class_1297> combined = new ArrayList<class_1297>();
        HashSet<UUID> existingActorUuids = new HashSet<UUID>(snapshot.actorUuids());
        for (UUID uUID : snapshot.actorUuids()) {
            class_1297 actor = world.method_66347(uUID);
            if (actor == null) continue;
            combined.add(actor);
        }
        if (combined.size() != snapshot.actorUuids().size()) {
            AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.WARNING, (class_2561)class_2561.method_43471((String)"debug.animationframework.join_failed_no_active_instance"), true);
            return;
        }
        ArrayList<class_1297> additions = new ArrayList<class_1297>();
        for (int id : requestedActorIds) {
            class_1297 e = world.method_8469(id);
            if (!(e instanceof class_1308) || !existingActorUuids.add(e.method_5667())) continue;
            additions.add(e);
        }
        if (additions.isEmpty()) {
            return;
        }
        combined.addAll(additions);
        combined.sort(Comparator.comparingInt(class_1297::method_5628));
        AfwAnimationDefinitions.MatchResult matchResult = AfwAnimationDefinitions.match(combined);
        AfwAnimationDefinitions.Definition definition = matchResult.chosen();
        if (definition == null) {
            AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.WARNING, (class_2561)class_2561.method_43471((String)"debug.animationframework.join_failed_no_expanded_definition"), true);
            return;
        }
        List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(definition, combined, world.method_8409());
        if (actorKeys == null || actorKeys.size() != combined.size()) {
            AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.WARNING, (class_2561)class_2561.method_43471((String)"debug.animationframework.join_failed_no_expanded_definition"), true);
            return;
        }
        LinkedHashMap<String, String> metadata = new LinkedHashMap<String, String>(snapshot.metadata());
        metadata.put("afw.join_replace", "true");
        metadata.put("afw.join_replace_from", snapshot.animationId().toString());
        if (!AfwServerAnimationController.stopInstance(world, instanceId, false)) {
            AnimationFramework.sendDebugChat(player, AfwDebugChatCategory.WARNING, (class_2561)class_2561.method_43471((String)"debug.animationframework.join_failed_no_active_instance"), true);
            return;
        }
        AfwServerAnimationController.startNow(world, player, definition.id(), combined, actorKeys, definition.stages(), snapshot.damageBehavior(), snapshot.ignoreAttackers(), null, metadata, true);
    }

    private record PendingSetupChatMessage(class_2561 message, Set<UUID> sentTo) {
    }
}

