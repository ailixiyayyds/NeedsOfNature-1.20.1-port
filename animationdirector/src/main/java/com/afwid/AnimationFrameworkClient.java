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
 *  net.minecraft.util.Formatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.hit.HitResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.option.KeyBinding$Category
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.util.InputUtil
 *  net.minecraft.client.util.InputUtil$Type
 *  net.minecraft.util.hit.EntityHitResult
 *  net.minecraft.client.option.Perspective
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.registry.Registries
 *  net.minecraft.network.packet.CustomPayload
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
import com.afwid.network.AfwPacket;
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
import net.minecraft.util.Formatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.Registries;

public class AnimationFrameworkClient
implements ClientModInitializer {
    private static final String KEY_CATEGORY = "key.categories.animationframework";
    private static final KeyBinding SELECT_ACTOR_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.select_actor"));
    private static final KeyBinding SELECT_SELF_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.select_self"));
    private static final KeyBinding START_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.start_debug_animation"));
    private static final KeyBinding STOP_ALL_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.stop_all_debug"));
    private static final KeyBinding STOP_INSTANCE_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.stop_instance_debug"));
    private static final KeyBinding NEXT_STAGE_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.next_stage_debug"));
    private static final KeyBinding SPEED_UP_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.speed_up_debug"));
    private static final KeyBinding SPEED_DOWN_DEBUG_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)AnimationFrameworkClient.createUnboundDebugKey("key.animationframework.speed_down_debug"));
    private static final IntArrayList SELECTED_ACTOR_IDS = new IntArrayList();
    private static UUID SELECTED_INSTANCE_ID = null;
    private static boolean wasSelfAnimating = false;
    private static boolean ownsCameraPerspective = false;
    private static Perspective preAnimationPerspective = null;
    private static final double PLAYER_ANCHOR_EPSILON_SQ = 4.0E-4;

    private static boolean shouldShow(AfwDebugChatCategory category) {
        return AfwClientConfig.get().allowsDebugChat(category);
    }

    public static void sendClientDebugChat(AfwDebugChatCategory category, Text message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return;
        }
        if (!AnimationFrameworkClient.shouldShow(category)) {
            return;
        }
        client.player.sendMessage((Text)message.copy().formatted(AnimationFrameworkClient.chatColor(category)), false);
    }

    public static void sendClientSetupWarning(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        AnimationFrameworkClient.sendClientDebugChat(AfwDebugChatCategory.SETUP, (Text)Text.literal((String)message));
    }

    private static KeyBinding createUnboundDebugKey(String translationKey) {
        return new KeyBinding(translationKey, InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), KEY_CATEGORY);
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
        ClientPlayNetworking.registerGlobalReceiver(StartAnimationS2CPayload.ID, (client, handler, buf, responseSender) -> {
            StartAnimationS2CPayload payload = StartAnimationS2CPayload.read(buf);
            client.execute(() -> {
            AfwClientAnimationRuntime.queueStart(payload.animationId(), payload.instanceId(), payload.actorUuids(), payload.actorKeys(), payload.stages(), payload.startTick(), payload.speed(), payload.lockOrientation(), payload.lockedYaw(), payload.lockedHeadYaw(), payload.lockedPitch(), payload.cameraOrbitTarget());
            ClientPlayerEntity player = client.player;
            if (player != null && AnimationFrameworkClient.shouldShow(AfwDebugChatCategory.INFO)) {
                String keysStr = payload.actorKeys().isEmpty() ? "none" : payload.actorKeys().toString();
                player.sendMessage(AnimationFrameworkClient.tr("start_received", payload.animationId(), payload.actorUuids().size(), keysStr), false);
            }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(StopAllAnimationsS2CPayload.ID, (client, handler, buf, responseSender) -> {
            StopAllAnimationsS2CPayload payload = StopAllAnimationsS2CPayload.read(buf);
            client.execute(() -> {
            AfwClientAnimationRuntime.queueStopAll(payload.stopTick());
            ClientPlayerEntity player = client.player;
            if (player != null && AnimationFrameworkClient.shouldShow(AfwDebugChatCategory.INFO)) {
                player.sendMessage(AnimationFrameworkClient.tr("stop_all_received", new Object[0]), false);
            }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(AdvanceAnimationStageS2CPayload.ID, (client, handler, buf, responseSender) -> {
            AdvanceAnimationStageS2CPayload payload = AdvanceAnimationStageS2CPayload.read(buf);
            client.execute(() -> AfwClientAnimationRuntime.queueStageAdvance(payload.instanceId(), payload.advanceTick(), payload.stageIndex()));
        });
        ClientPlayNetworking.registerGlobalReceiver(AnimationSpeedUpdateS2CPayload.ID, (client, handler, buf, responseSender) -> {
            AnimationSpeedUpdateS2CPayload payload = AnimationSpeedUpdateS2CPayload.read(buf);
            client.execute(() -> AfwClientAnimationRuntime.queueSpeedUpdate(payload.instanceId(), payload.speed()));
        });
        ClientPlayNetworking.registerGlobalReceiver(StopAnimationS2CPayload.ID, (client, handler, buf, responseSender) -> {
            StopAnimationS2CPayload payload = StopAnimationS2CPayload.read(buf);
            client.execute(() -> {
            AfwClientAnimationRuntime.queueStop(payload.instanceId(), payload.stopTick());
            ClientPlayerEntity player = client.player;
            if (player != null && AnimationFrameworkClient.shouldShow(AfwDebugChatCategory.INFO)) {
                player.sendMessage(AnimationFrameworkClient.tr("stop_instance_received", new Object[0]), false);
            }
            });
        });
        ClientTickEvents.END_CLIENT_TICK.register(AnimationFrameworkClient::onClientTick);
    }

    public static void sendDebugChatPreference() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null && ClientPlayNetworking.canSend(DebugChatPreferenceC2SPayload.ID)) {
            AnimationFrameworkClient.sendPacket(new DebugChatPreferenceC2SPayload(AfwClientConfig.get().debugChatMode().id()));
        }
    }

    private static void sendPacket(AfwPacket packet) {
        ClientPlayNetworking.send(packet.id(), packet.toBuffer());
    }

    private static void onClientTick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            return;
        }
        if (client.currentScreen == null) {
            while (SELECT_ACTOR_KEY.wasPressed()) {
                AnimationFrameworkClient.toggleTargetedActorSelection(client);
            }
            while (SELECT_SELF_KEY.wasPressed()) {
                AnimationFrameworkClient.toggleSelfSelection(client);
            }
            while (START_DEBUG_KEY.wasPressed()) {
                AnimationFrameworkClient.requestDebugStart(client);
            }
            while (STOP_ALL_DEBUG_KEY.wasPressed()) {
                AnimationFrameworkClient.requestStopAll(client);
            }
            while (STOP_INSTANCE_DEBUG_KEY.wasPressed()) {
                AnimationFrameworkClient.requestStopInstance(client);
            }
            while (NEXT_STAGE_DEBUG_KEY.wasPressed()) {
                AnimationFrameworkClient.requestNextStage(client);
            }
            while (SPEED_UP_DEBUG_KEY.wasPressed()) {
                AnimationFrameworkClient.requestSpeedAdjust(client, 1.1);
            }
            while (SPEED_DOWN_DEBUG_KEY.wasPressed()) {
                AnimationFrameworkClient.requestSpeedAdjust(client, 0.9);
            }
        }
        AfwClientAnimationRuntime.clientTick(client);
        AfwAnimationCameraZoom.clientTick(client);
        AfwAnimationAssetDiagnostics.validateLoadedDefinitionsOnce();
        AfwAnimatedCameraPoseTracker.prune(client.world.getTime());
        AnimationFrameworkClient.handleCompletedStages(client);
        AnimationFrameworkClient.syncCameraPerspective(client);
        AnimationFrameworkClient.syncLocalPlayerToAnchor(client);
    }

    private static void requestStopAll(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        if (!ClientPlayNetworking.canSend(DebugStopAllAnimationsC2SPayload.ID)) {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("server_cannot_receive_stop_all", new Object[0]));
            AfwClientAnimationRuntime.stopAllNow();
            return;
        }
        AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("stop_all_requested", new Object[0]));
        AnimationFrameworkClient.sendPacket(new DebugStopAllAnimationsC2SPayload());
    }

    private static void requestStopInstance(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        UUID self = player.getUuid();
        UUID instanceToStop = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(self);
        if (instanceToStop == null) {
            instanceToStop = AfwClientAnimationRuntime.findLatestActiveInstance();
        }
        if (instanceToStop == null) {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("no_active_instance_to_stop", new Object[0]));
            return;
        }
        if (!ClientPlayNetworking.canSend(DebugStopAnimationC2SPayload.ID)) {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("server_cannot_receive_stop_instance", new Object[0]));
            return;
        }
        AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("stop_instance_requested", new Object[0]));
        AnimationFrameworkClient.sendPacket(new DebugStopAnimationC2SPayload(instanceToStop));
    }

    private static void toggleTargetedActorSelection(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof EntityHitResult)) {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("no_entity_targeted", new Object[0]));
            return;
        }
        EntityHitResult entityHit = (EntityHitResult)hit;
        Entity target = entityHit.getEntity();
        if (!(target instanceof LivingEntity)) {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("target_not_living", new Object[0]));
            return;
        }
        int id = target.getId();
        Identifier typeId = Registries.ENTITY_TYPE.getId(target.getType());
        UUID instId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(target.getUuid());
        if (instId != null) {
            if (SELECTED_INSTANCE_ID != null && SELECTED_INSTANCE_ID.equals(instId)) {
                SELECTED_INSTANCE_ID = null;
                SELECTED_ACTOR_IDS.clear();
                AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("animation_deselected", new Object[0]));
                return;
            }
            SELECTED_INSTANCE_ID = instId;
            SELECTED_ACTOR_IDS.clear();
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("animation_selected_add_actors_for_join", new Object[0]));
            return;
        }
        int existingIndex = AnimationFrameworkClient.indexOfSelectedActor(id);
        if (existingIndex >= 0) {
            SELECTED_ACTOR_IDS.removeInt(existingIndex);
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("entity_deselected_actor_count", typeId.getPath(), SELECTED_ACTOR_IDS.size()));
        } else {
            SELECTED_ACTOR_IDS.add(id);
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("entity_selected_actor_count", typeId.getPath(), SELECTED_ACTOR_IDS.size()));
        }
    }

    private static void toggleSelfSelection(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        int id = player.getId();
        int existingIndex = AnimationFrameworkClient.indexOfSelectedActor(id);
        if (existingIndex >= 0) {
            SELECTED_ACTOR_IDS.removeInt(existingIndex);
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("self_deselected_actor_count", SELECTED_ACTOR_IDS.size()));
        } else {
            SELECTED_ACTOR_IDS.add(id);
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("self_selected_actor_count", SELECTED_ACTOR_IDS.size()));
        }
    }

    private static void requestDebugStart(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        if (SELECTED_INSTANCE_ID == null && SELECTED_ACTOR_IDS.isEmpty()) {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("no_actors_selected", new Object[0]));
            return;
        }
        if (SELECTED_INSTANCE_ID != null) {
            ArrayList<Integer> actorIds = new ArrayList<Integer>(SELECTED_ACTOR_IDS.size());
            for (int i = 0; i < SELECTED_ACTOR_IDS.size(); ++i) {
                actorIds.add(SELECTED_ACTOR_IDS.getInt(i));
            }
            if (actorIds.isEmpty()) {
                AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("no_extra_actors_for_join", new Object[0]));
                return;
            }
            if (!ClientPlayNetworking.canSend(DebugJoinAnimationC2SPayload.ID)) {
                AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("server_cannot_receive_join", new Object[0]));
                return;
            }
            AnimationFrameworkClient.sendPacket(new DebugJoinAnimationC2SPayload(SELECTED_INSTANCE_ID, actorIds));
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("join_requested_instance_extras", SELECTED_INSTANCE_ID, actorIds.size()));
            SELECTED_INSTANCE_ID = null;
            SELECTED_ACTOR_IDS.clear();
            return;
        }
        if (!ClientPlayNetworking.canSend(DebugStartAnimationC2SPayload.ID)) {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("server_cannot_receive_debug_packets", new Object[0]));
            return;
        }
        ArrayList<Integer> actorIds = new ArrayList<Integer>(SELECTED_ACTOR_IDS.size());
        for (int i = 0; i < SELECTED_ACTOR_IDS.size(); ++i) {
            actorIds.add(SELECTED_ACTOR_IDS.getInt(i));
        }
        AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("start_requested_actor_count", actorIds.size()));
        int anchorId = -1;
        if (AfwClientConfig.get().anchorAtLastSelected() && actorIds.size() >= 2) {
            anchorId = (Integer)actorIds.get(actorIds.size() - 1);
        }
        String behaviorId = AfwClientConfig.get().debugDamageBehavior().id();
        boolean ignoreAttackers = AfwClientConfig.get().debugIgnoreAttackers();
        AnimationFrameworkClient.sendPacket(new DebugStartAnimationC2SPayload(actorIds, behaviorId, ignoreAttackers, anchorId));
        SELECTED_ACTOR_IDS.clear();
    }

    private static void requestNextStage(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        UUID instanceToAdvance = AfwClientAnimationRuntime.findLatestActiveInstance();
        if (instanceToAdvance == null) {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("no_active_instance_to_advance", new Object[0]));
            return;
        }
        if (ClientPlayNetworking.canSend(DebugAdvanceStageC2SPayload.ID)) {
            AnimationFrameworkClient.sendPacket(new DebugAdvanceStageC2SPayload(instanceToAdvance));
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("next_stage_requested_server", new Object[0]));
        } else {
            AfwClientAnimationRuntime.requestStageAdvance(instanceToAdvance);
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("next_stage_requested_local", new Object[0]));
        }
    }

    private static void requestSpeedAdjust(MinecraftClient client, double multiplier) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        UUID targetInstance = AfwClientAnimationRuntime.findLatestActiveInstanceContaining(player.getUuid());
        if (targetInstance == null) {
            targetInstance = AfwClientAnimationRuntime.findLatestActiveInstance();
        }
        if (targetInstance == null) {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("no_active_instance_to_adjust_speed", new Object[0]));
            return;
        }
        if (ClientPlayNetworking.canSend(AdjustAnimationSpeedC2SPayload.ID)) {
            AnimationFrameworkClient.sendPacket(new AdjustAnimationSpeedC2SPayload(targetInstance, multiplier));
            double percent = (multiplier - 1.0) * 100.0;
            String delta = (percent >= 0.0 ? "+" : "") + String.format(Locale.ROOT, "%.0f%%", percent);
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("speed_change_requested", delta));
        } else {
            AnimationFrameworkClient.sendManualDebug((PlayerEntity)player, AnimationFrameworkClient.tr("server_cannot_receive_speed", new Object[0]));
        }
    }

    private static void handleCompletedStages(MinecraftClient client) {
        List<UUID> completed = AfwClientAnimationRuntime.drainCompletedInstances();
        if (completed.isEmpty()) {
            return;
        }
        ClientPlayerEntity player = client.player;
        boolean canSend = ClientPlayNetworking.canSend(DebugStopAnimationC2SPayload.ID);
        for (UUID instanceId : completed) {
            if (canSend) {
                AnimationFrameworkClient.sendPacket(new DebugStopAnimationC2SPayload(instanceId));
                continue;
            }
            if (player == null || !AnimationFrameworkClient.shouldShow(AfwDebugChatCategory.ERROR)) continue;
            AnimationFrameworkClient.sendCategoryDebug((PlayerEntity)player, AfwDebugChatCategory.ERROR, AnimationFrameworkClient.tr("stage_sequence_ended_server_stop_unavailable", new Object[0]));
        }
    }

    private static void syncCameraPerspective(MinecraftClient client) {
        if (client.player == null || client.options == null || client.world == null) {
            return;
        }
        if (!AfwClientConfig.get().autoSwitchThirdPersonOnAnimationStart()) {
            AnimationFrameworkClient.restoreCameraPerspective(client);
            return;
        }
        UUID selfId = client.player.getUuid();
        boolean shouldThirdPerson = AfwClientAnimationRuntime.isActorPendingOrActive(selfId);
        if (shouldThirdPerson) {
            if (!wasSelfAnimating) {
                preAnimationPerspective = client.options.getPerspective();
                ownsCameraPerspective = true;
                if (client.options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
                    client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                }
            } else if (ownsCameraPerspective && client.options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
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

    private static void resetCameraPerspective(MinecraftClient client) {
        if (client == null || client.options == null) {
            return;
        }
        AnimationFrameworkClient.restoreCameraPerspective(client);
    }

    private static void restoreCameraPerspective(MinecraftClient client) {
        if (client == null || client.options == null) {
            return;
        }
        if (ownsCameraPerspective && preAnimationPerspective != null) {
            client.options.setPerspective(preAnimationPerspective);
        }
        wasSelfAnimating = false;
        ownsCameraPerspective = false;
        preAnimationPerspective = null;
    }

    private static void syncLocalPlayerToAnchor(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            return;
        }
        UUID selfId = player.getUuid();
        if (AfwClientAnimationRuntime.isActorPendingOrActive(selfId)) {
            double dz;
            double dy;
            UUID anchorId = AfwClientAnimationRuntime.findAnchorUuidForActor(selfId);
            if (anchorId == null) {
                return;
            }
            Entity anchor = null;
            for (Entity candidate : client.world.getEntities()) {
                if (anchorId.equals(candidate.getUuid())) {
                    anchor = candidate;
                    break;
                }
            }
            if (anchor == null) {
                return;
            }
            double dx = player.getX() - anchor.getX();
            if (dx * dx + (dy = player.getY() - anchor.getY()) * dy + (dz = player.getZ() - anchor.getZ()) * dz > 4.0E-4) {
                player.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
                player.setVelocity(Vec3d.ZERO);
                player.fallDistance = 0.0f;
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

    private static Text tr(String key, Object ... args) {
        return Text.translatable((String)("debug.animationframework." + key), (Object[])AnimationFrameworkClient.sanitizeTranslatableArgs(args));
    }

    private static Object[] sanitizeTranslatableArgs(Object ... args) {
        if (args == null || args.length == 0) {
            return new Object[0];
        }
        Object[] safe = new Object[args.length];
        for (int i = 0; i < args.length; ++i) {
            Object arg = args[i];
            if (arg instanceof Identifier) {
                Identifier id = (Identifier)arg;
                safe[i] = id.getPath();
                continue;
            }
            safe[i] = arg instanceof Text || arg == null ? arg : String.valueOf(arg);
        }
        return safe;
    }

    private static void sendManualDebug(PlayerEntity player, Text message) {
        if (player == null) {
            return;
        }
        player.sendMessage((Text)message.copy().formatted(Formatting.DARK_GRAY), false);
    }

    private static void sendCategoryDebug(PlayerEntity player, AfwDebugChatCategory category, Text message) {
        if (player == null) {
            return;
        }
        player.sendMessage((Text)message.copy().formatted(AnimationFrameworkClient.chatColor(category)), false);
    }

    private static Formatting chatColor(AfwDebugChatCategory category) {
        return switch (category) {
            case ALWAYS -> Formatting.DARK_GRAY;
            case SETUP -> Formatting.YELLOW;
            case WARNING -> Formatting.LIGHT_PURPLE;
            case ERROR -> Formatting.RED;
            case INFO -> Formatting.WHITE;
        };
    }
}

