/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.minecraft.class_124
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_239
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_304
 *  net.minecraft.class_304$class_11900
 *  net.minecraft.class_310
 *  net.minecraft.class_3675
 *  net.minecraft.class_3675$class_307
 *  net.minecraft.class_3966
 *  net.minecraft.class_5498
 *  net.minecraft.class_746
 *  net.minecraft.class_7923
 *  net.minecraft.class_8710
 */
package com.afwid;

import com.afwid.AfwDebugChatCategory;
import com.afwid.client.camera.AfwAnimatedCameraPoseTracker;
import com.afwid.client.camera.AfwAnimationCameraZoom;
import com.afwid.client.config.AfwClientConfig;
import com.afwid.client.diagnostics.AfwAnimationAssetDiagnostics;
import com.afwid.client.render.gecko.AfwBoneTextureOverrides;
import com.afwid.client.render.gecko.AfwVanillaTextureResolver;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import com.afwid.client.sound.AfwSoundEffects;
import com.afwid.network.AdjustAnimationSpeedC2SPayload;
import com.afwid.network.AdvanceAnimationStageS2CPayload;
import com.afwid.network.AnimationSpeedUpdateS2CPayload;
import com.afwid.network.DebugAdvanceStageC2SPayload;
import com.afwid.network.DebugChatPreferenceC2SPayload;
import com.afwid.network.DebugJoinAnimationC2SPayload;
import com.afwid.network.DebugStartAnimationC2SPayload;
import com.afwid.network.DebugStopAllAnimationsC2SPayload;
import com.afwid.network.DebugStopAnimationC2SPayload;
import com.afwid.network.StartAnimationS2CPayload;
import com.afwid.network.StopAllAnimationsS2CPayload;
import com.afwid.network.StopAnimationS2CPayload;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_3966;
import net.minecraft.class_5498;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import net.minecraft.class_8710;

public class AnimationFrameworkClient
implements ClientModInitializer {
    private static final class_304.class_11900 KEY_CATEGORY = class_304.class_11900.method_74698((class_2960)class_2960.method_60655((String)"animationframework", (String)"main"));
    private static final class_304 SELECT_ACTOR_KEY = KeyBindingHelper.registerKeyBinding((class_304)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.select_actor"));
    private static final class_304 SELECT_SELF_KEY = KeyBindingHelper.registerKeyBinding((class_304)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.select_self"));
    private static final class_304 START_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((class_304)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.start_debug_animation"));
    private static final class_304 STOP_ALL_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((class_304)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.stop_all_debug"));
    private static final class_304 STOP_INSTANCE_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((class_304)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.stop_instance_debug"));
    private static final class_304 NEXT_STAGE_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((class_304)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.next_stage_debug"));
    private static final class_304 SPEED_UP_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((class_304)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.speed_up_debug"));
    private static final class_304 SPEED_DOWN_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((class_304)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.speed_down_debug"));
    private static final IntArrayList SELECTED_ACTOR_IDS = new IntArrayList();
    private static UUID SELECTED_INSTANCE_ID = null;
    private static boolean wasSelfAnimating = false;
    private static boolean ownsCameraPerspective = false;
    private static class_5498 preAnimationPerspective = null;
    private static final double PLAYER_ANCHOR_EPSILON_SQ = 4.0E-4;

    private static boolean shouldShow(AfwDebugChatCategory category) {
        return AfwClientConfig.get().allowsDebugChat(category);
    }

    public static void sendClientDebugChat(AfwDebugChatCategory category, class_2561 message) {
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return;
        }
        if (!AnimationFrameworkClient.shouldShow(category)) {
            return;
        }
        client.field_1724.method_7353((class_2561)message.method_27661().method_27692(AnimationFrameworkClient.chatColor(category)), false);
    }

    public static void sendClientSetupWarning(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        AnimationFrameworkClient.sendClientDebugChat(AfwDebugChatCategory.SETUP, (class_2561)class_2561.method_43470((String)message));
    }

    private static class_304 createUnboundDebugKey(String translationKey) {
        return new class_304(translationKey, class_3675.class_307.field_1668, class_3675.field_16237.method_1444(), KEY_CATEGORY);
    }

    public void onInitializeClient() {
        AfwClientConfig.reload();
        AfwVanillaTextureResolver.registerReloadListener();
        AfwBoneTextureOverrides.registerReloadListener();
        AfwSoundEffects.registerReloadListener();
        AfwAnimationAssetDiagnostics.registerReloadListener();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> AnimationFrameworkClient.sendDebugChatPreference());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            AfwClientAnimationRuntime.stopAllNow();
            AfwAnimatedCameraPoseTracker.clear();
            AfwAnimationCameraZoom.reset();
            AnimationFrameworkClient.resetCameraPerspective(client);
        });
        ClientPlayNetworking.registerGlobalReceiver(StartAnimationS2CPayload.ID, (payload, context) -> context.client().execute(() -> {
            AfwClientAnimationRuntime.queueStart(payload.animationId(), payload.instanceId(), payload.actorUuids(), payload.actorKeys(), payload.stages(), payload.startTick(), payload.speed(), payload.lockOrientation(), payload.lockedYaw(), payload.lockedHeadYaw(), payload.lockedPitch(), payload.cameraOrbitTarget());
            class_310 client = context.client();
            class_746 player = client.field_1724;
            if (player != null && AnimationFrameworkClient.shouldShow(AfwDebugChatCategory.INFO)) {
                String keysStr = payload.actorKeys().isEmpty() ? "none" : payload.actorKeys().toString();
                player.method_7353(AnimationFrameworkClient.tr("start_received", payload.animationId(), payload.actorUuids().size(), keysStr), false);
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(StopAllAnimationsS2CPayload.ID, (payload, context) -> context.client().execute(() -> {
            AfwClientAnimationRuntime.queueStopAll(payload.stopTick());
            class_310 client = context.client();
            class_746 player = client.field_1724;
            if (player != null && AnimationFrameworkClient.shouldShow(AfwDebugChatCategory.INFO)) {
                player.method_7353(AnimationFrameworkClient.tr("stop_all_received", new Object[0]), false);
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(AdvanceAnimationStageS2CPayload.ID, (payload, context) -> context.client().execute(() -> AfwClientAnimationRuntime.queueStageAdvance(payload.instanceId(), payload.advanceTick(), payload.stageIndex())));
        ClientPlayNetworking.registerGlobalReceiver(AnimationSpeedUpdateS2CPayload.ID, (payload, context) -> context.client().execute(() -> AfwClientAnimationRuntime.queueSpeedUpdate(payload.instanceId(), payload.speed())));
        ClientPlayNetworking.registerGlobalReceiver(StopAnimationS2CPayload.ID, (payload, context) -> context.client().execute(() -> {
            AfwClientAnimationRuntime.queueStop(payload.instanceId(), payload.stopTick());
            class_310 client = context.client();
            class_746 player = client.field_1724;
            if (player != null && AnimationFrameworkClient.shouldShow(AfwDebugChatCategory.INFO)) {
                player.method_7353(AnimationFrameworkClient.tr("stop_instance_received", new Object[0]), false);
            }
        }));
        ClientTickEvents.END_CLIENT_TICK.register(AnimationFrameworkClient::onClientTick);
    }

    public static void sendDebugChatPreference() {
        class_310 client = class_310.method_1551();
        if (client != null && client.field_1724 != null && ClientPlayNetworking.canSend(DebugChatPreferenceC2SPayload.ID)) {
            ClientPlayNetworking.send((class_8710)new DebugChatPreferenceC2SPayload(AfwClientConfig.get().debugChatMode().id()));
        }
    }

    private static void onClientTick(class_310 client) {
        if (client.field_1724 == null || client.field_1687 == null) {
            return;
        }
        if (client.field_1755 == null) {
            while (SELECT_ACTOR_KEY.method_1436()) {
                AnimationFrameworkClient.toggleTargetedActorSelection(client);
            }
            while (SELECT_SELF_KEY.method_1436()) {
                AnimationFrameworkClient.toggleSelfSelection(client);
            }
            while (START_DEBUG_KEY.method_1436()) {
                AnimationFrameworkClient.requestDebugStart(client);
            }
            while (STOP_ALL_DEBUG_KEY.method_1436()) {
                AnimationFrameworkClient.requestStopAll(client);
            }
            while (STOP_INSTANCE_DEBUG_KEY.method_1436()) {
                AnimationFrameworkClient.requestStopInstance(client);
            }
            while (NEXT_STAGE_DEBUG_KEY.method_1436()) {
                AnimationFrameworkClient.requestNextStage(client);
            }
            while (SPEED_UP_DEBUG_KEY.method_1436()) {
                AnimationFrameworkClient.requestSpeedAdjust(client, 1.1);
            }
            while (SPEED_DOWN_DEBUG_KEY.method_1436()) {
                AnimationFrameworkClient.requestSpeedAdjust(client, 0.9);
            }
        }
        AfwClientAnimationRuntime.clientTick(client);
        AfwAnimationCameraZoom.clientTick(client);
        AfwAnimationAssetDiagnostics.validateLoadedDefinitionsOnce();
        AfwAnimatedCameraPoseTracker.prune(client.field_1687.method_75260());
        AnimationFrameworkClient.handleCompletedStages(client);
        AnimationFrameworkClient.syncCameraPerspective(client);
        AnimationFrameworkClient.syncLocalPlayerToAnchor(client);
    }

    private static void requestStopAll(class_310 client) {
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        if (!ClientPlayNetworking.canSend(DebugStopAllAnimationsC2SPayload.ID)) {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("server_cannot_receive_stop_all", new Object[0]));
            AfwClientAnimationRuntime.stopAllNow();
            return;
        }
        AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("stop_all_requested", new Object[0]));
        ClientPlayNetworking.send((class_8710)new DebugStopAllAnimationsC2SPayload());
    }

    private static void requestStopInstance(class_310 client) {
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        UUID self = player.method_5667();
        UUID instanceToStop = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(self);
        if (instanceToStop == null) {
            instanceToStop = AfwClientAnimationRuntime.findLatestActiveInstance();
        }
        if (instanceToStop == null) {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("no_active_instance_to_stop", new Object[0]));
            return;
        }
        if (!ClientPlayNetworking.canSend(DebugStopAnimationC2SPayload.ID)) {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("server_cannot_receive_stop_instance", new Object[0]));
            return;
        }
        AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("stop_instance_requested", new Object[0]));
        ClientPlayNetworking.send((class_8710)new DebugStopAnimationC2SPayload(instanceToStop));
    }

    private static void toggleTargetedActorSelection(class_310 client) {
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        class_239 hit = client.field_1765;
        if (!(hit instanceof class_3966)) {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("no_entity_targeted", new Object[0]));
            return;
        }
        class_3966 entityHit = (class_3966)hit;
        class_1297 target = entityHit.method_17782();
        if (!(target instanceof class_1309)) {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("target_not_living", new Object[0]));
            return;
        }
        int id = target.method_5628();
        class_2960 typeId = class_7923.field_41177.method_10221((Object)target.method_5864());
        UUID instId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(target.method_5667());
        if (instId != null) {
            if (SELECTED_INSTANCE_ID != null && SELECTED_INSTANCE_ID.equals(instId)) {
                SELECTED_INSTANCE_ID = null;
                SELECTED_ACTOR_IDS.clear();
                AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("animation_deselected", new Object[0]));
                return;
            }
            SELECTED_INSTANCE_ID = instId;
            SELECTED_ACTOR_IDS.clear();
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("animation_selected_add_actors_for_join", new Object[0]));
            return;
        }
        int existingIndex = AnimationFrameworkClient.indexOfSelectedActor(id);
        if (existingIndex >= 0) {
            SELECTED_ACTOR_IDS.removeInt(existingIndex);
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("entity_deselected_actor_count", typeId.method_12832(), SELECTED_ACTOR_IDS.size()));
        } else {
            SELECTED_ACTOR_IDS.add(id);
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("entity_selected_actor_count", typeId.method_12832(), SELECTED_ACTOR_IDS.size()));
        }
    }

    private static void toggleSelfSelection(class_310 client) {
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        int id = player.method_5628();
        int existingIndex = AnimationFrameworkClient.indexOfSelectedActor(id);
        if (existingIndex >= 0) {
            SELECTED_ACTOR_IDS.removeInt(existingIndex);
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("self_deselected_actor_count", SELECTED_ACTOR_IDS.size()));
        } else {
            SELECTED_ACTOR_IDS.add(id);
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("self_selected_actor_count", SELECTED_ACTOR_IDS.size()));
        }
    }

    private static void requestDebugStart(class_310 client) {
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        if (SELECTED_INSTANCE_ID == null && SELECTED_ACTOR_IDS.isEmpty()) {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("no_actors_selected", new Object[0]));
            return;
        }
        if (SELECTED_INSTANCE_ID != null) {
            ArrayList<Integer> actorIds = new ArrayList<Integer>(SELECTED_ACTOR_IDS.size());
            for (int i = 0; i < SELECTED_ACTOR_IDS.size(); ++i) {
                actorIds.add(SELECTED_ACTOR_IDS.getInt(i));
            }
            if (actorIds.isEmpty()) {
                AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("no_extra_actors_for_join", new Object[0]));
                return;
            }
            if (!ClientPlayNetworking.canSend(DebugJoinAnimationC2SPayload.ID)) {
                AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("server_cannot_receive_join", new Object[0]));
                return;
            }
            ClientPlayNetworking.send((class_8710)new DebugJoinAnimationC2SPayload(SELECTED_INSTANCE_ID, actorIds));
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("join_requested_instance_extras", SELECTED_INSTANCE_ID, actorIds.size()));
            SELECTED_INSTANCE_ID = null;
            SELECTED_ACTOR_IDS.clear();
            return;
        }
        if (!ClientPlayNetworking.canSend(DebugStartAnimationC2SPayload.ID)) {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("server_cannot_receive_debug_packets", new Object[0]));
            return;
        }
        ArrayList<Integer> actorIds = new ArrayList<Integer>(SELECTED_ACTOR_IDS.size());
        for (int i = 0; i < SELECTED_ACTOR_IDS.size(); ++i) {
            actorIds.add(SELECTED_ACTOR_IDS.getInt(i));
        }
        AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("start_requested_actor_count", actorIds.size()));
        int anchorId = -1;
        if (AfwClientConfig.get().anchorAtLastSelected() && actorIds.size() >= 2) {
            anchorId = (Integer)actorIds.getLast();
        }
        String behaviorId = AfwClientConfig.get().debugDamageBehavior().id();
        boolean ignoreAttackers = AfwClientConfig.get().debugIgnoreAttackers();
        ClientPlayNetworking.send((class_8710)new DebugStartAnimationC2SPayload(actorIds, behaviorId, ignoreAttackers, anchorId));
        SELECTED_ACTOR_IDS.clear();
    }

    private static void requestNextStage(class_310 client) {
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        UUID instanceToAdvance = AfwClientAnimationRuntime.findLatestActiveInstance();
        if (instanceToAdvance == null) {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("no_active_instance_to_advance", new Object[0]));
            return;
        }
        if (ClientPlayNetworking.canSend(DebugAdvanceStageC2SPayload.ID)) {
            ClientPlayNetworking.send((class_8710)new DebugAdvanceStageC2SPayload(instanceToAdvance));
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("next_stage_requested_server", new Object[0]));
        } else {
            AfwClientAnimationRuntime.requestStageAdvance(instanceToAdvance);
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("next_stage_requested_local", new Object[0]));
        }
    }

    private static void requestSpeedAdjust(class_310 client, double multiplier) {
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        UUID targetInstance = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(player.method_5667());
        if (targetInstance == null) {
            targetInstance = AfwClientAnimationRuntime.findLatestActiveInstance();
        }
        if (targetInstance == null) {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("no_active_instance_to_adjust_speed", new Object[0]));
            return;
        }
        if (ClientPlayNetworking.canSend(AdjustAnimationSpeedC2SPayload.ID)) {
            ClientPlayNetworking.send((class_8710)new AdjustAnimationSpeedC2SPayload(targetInstance, multiplier));
            double percent = (multiplier - 1.0) * 100.0;
            String delta = (percent >= 0.0 ? "+" : "") + String.format(Locale.ROOT, "%.0f%%", percent);
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("speed_change_requested", delta));
        } else {
            AnimationFrameworkClient.sendManualDebug((class_1657)player, AnimationFrameworkClient.tr("server_cannot_receive_speed", new Object[0]));
        }
    }

    private static void handleCompletedStages(class_310 client) {
        List<UUID> completed = AfwClientAnimationRuntime.drainCompletedInstances();
        if (completed.isEmpty()) {
            return;
        }
        class_746 player = client.field_1724;
        boolean canSend = ClientPlayNetworking.canSend(DebugStopAnimationC2SPayload.ID);
        for (UUID instanceId : completed) {
            if (canSend) {
                ClientPlayNetworking.send((class_8710)new DebugStopAnimationC2SPayload(instanceId));
                continue;
            }
            if (player == null || !AnimationFrameworkClient.shouldShow(AfwDebugChatCategory.ERROR)) continue;
            AnimationFrameworkClient.sendCategoryDebug((class_1657)player, AfwDebugChatCategory.ERROR, AnimationFrameworkClient.tr("stage_sequence_ended_server_stop_unavailable", new Object[0]));
        }
    }

    private static void syncCameraPerspective(class_310 client) {
        if (client.field_1724 == null || client.field_1690 == null || client.field_1687 == null) {
            return;
        }
        if (!AfwClientConfig.get().autoSwitchThirdPersonOnAnimationStart()) {
            AnimationFrameworkClient.restoreCameraPerspective(client);
            return;
        }
        UUID selfId = client.field_1724.method_5667();
        boolean shouldThirdPerson = AfwClientAnimationRuntime.isActorPendingOrActive(selfId);
        if (shouldThirdPerson) {
            if (!wasSelfAnimating) {
                preAnimationPerspective = client.field_1690.method_31044();
                ownsCameraPerspective = true;
                if (client.field_1690.method_31044() != class_5498.field_26665) {
                    client.field_1690.method_31043(class_5498.field_26665);
                }
            } else if (ownsCameraPerspective && client.field_1690.method_31044() != class_5498.field_26665) {
                preAnimationPerspective = null;
                ownsCameraPerspective = false;
            }
            wasSelfAnimating = true;
            return;
        }
        if (ownsCameraPerspective || wasSelfAnimating) {
            AnimationFrameworkClient.restoreCameraPerspective(client);
        }
    }

    private static void resetCameraPerspective(class_310 client) {
        if (client == null || client.field_1690 == null) {
            return;
        }
        AnimationFrameworkClient.restoreCameraPerspective(client);
    }

    private static void restoreCameraPerspective(class_310 client) {
        if (client == null || client.field_1690 == null) {
            return;
        }
        if (ownsCameraPerspective && preAnimationPerspective != null) {
            client.field_1690.method_31043(preAnimationPerspective);
        }
        wasSelfAnimating = false;
        ownsCameraPerspective = false;
        preAnimationPerspective = null;
    }

    private static void syncLocalPlayerToAnchor(class_310 client) {
        class_746 player = client.field_1724;
        if (player == null || client.field_1687 == null) {
            return;
        }
        UUID selfId = player.method_5667();
        if (AfwClientAnimationRuntime.isActorPendingOrActive(selfId)) {
            double dz;
            double dy;
            UUID anchorId = AfwClientAnimationRuntime.findAnchorUuidForActor(selfId);
            if (anchorId == null) {
                return;
            }
            class_1297 anchor = client.field_1687.method_66347(anchorId);
            if (anchor == null) {
                return;
            }
            double dx = player.method_23317() - anchor.method_23317();
            if (dx * dx + (dy = player.method_23318() - anchor.method_23318()) * dy + (dz = player.method_23321() - anchor.method_23321()) * dz > 4.0E-4) {
                player.method_23327(anchor.method_23317(), anchor.method_23318(), anchor.method_23321());
                player.method_18799(class_243.field_1353);
                player.field_6017 = 0.0;
            }
        }
    }

    private static int indexOfSelectedActor(int value) {
        for (int i = 0; i < SELECTED_ACTOR_IDS.size(); ++i) {
            if (SELECTED_ACTOR_IDS.getInt(i) != value) continue;
            return i;
        }
        return -1;
    }

    private static class_2561 tr(String key, Object ... args) {
        return class_2561.method_43469((String)("debug.animationframework." + key), (Object[])AnimationFrameworkClient.sanitizeTranslatableArgs(args));
    }

    private static Object[] sanitizeTranslatableArgs(Object ... args) {
        if (args == null || args.length == 0) {
            return new Object[0];
        }
        Object[] safe = new Object[args.length];
        for (int i = 0; i < args.length; ++i) {
            Object arg = args[i];
            if (arg instanceof class_2960) {
                class_2960 id = (class_2960)arg;
                safe[i] = id.method_12832();
                continue;
            }
            safe[i] = arg instanceof class_2561 || arg == null ? arg : String.valueOf(arg);
        }
        return safe;
    }

    private static void sendManualDebug(class_1657 player, class_2561 message) {
        if (player == null) {
            return;
        }
        player.method_7353((class_2561)message.method_27661().method_27692(class_124.field_1063), false);
    }

    private static void sendCategoryDebug(class_1657 player, AfwDebugChatCategory category, class_2561 message) {
        if (player == null) {
            return;
        }
        player.method_7353((class_2561)message.method_27661().method_27692(AnimationFrameworkClient.chatColor(category)), false);
    }

    private static class_124 chatColor(AfwDebugChatCategory category) {
        return switch (category) {
            default -> throw new MatchException(null, null);
            case AfwDebugChatCategory.ALWAYS -> class_124.field_1063;
            case AfwDebugChatCategory.SETUP -> class_124.field_1054;
            case AfwDebugChatCategory.WARNING -> class_124.field_1076;
            case AfwDebugChatCategory.ERROR -> class_124.field_1061;
            case AfwDebugChatCategory.INFO -> class_124.field_1068;
        };
    }
}

