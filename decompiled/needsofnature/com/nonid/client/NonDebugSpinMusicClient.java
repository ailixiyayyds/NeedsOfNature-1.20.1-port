/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.client.runtime.AfwClientAnimationRuntime
 *  net.minecraft.class_1101
 *  net.minecraft.class_1113
 *  net.minecraft.class_1113$class_1114
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3414
 *  net.minecraft.class_3419
 *  net.minecraft.class_638
 *  net.minecraft.class_7923
 */
package com.nonid.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import com.nonid.NeedsOfNature;
import com.nonid.client.NonDebugSpinMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_1101;
import net.minecraft.class_1113;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_638;
import net.minecraft.class_7923;

public final class NonDebugSpinMusicClient {
    private static final class_2960 SOUND_ID = NeedsOfNature.id("funkytown");
    private static final float VOLUME = 0.025f;
    private static final float PITCH = 1.0f;
    private static final Map<UUID, SpinMusicSound> ACTIVE = new HashMap<UUID, SpinMusicSound>();

    private NonDebugSpinMusicClient() {
    }

    public static void clientTick(class_310 client) {
        if (client == null) {
            return;
        }
        if (!NonDebugSpinMode.isEnabled() || client.field_1687 == null) {
            NonDebugSpinMusicClient.stopAll(client);
            return;
        }
        NonDebugSpinMusicClient.pruneStoppedSounds(client);
        Map<UUID, UUID> anchors = NonDebugSpinMusicClient.findActiveInstanceAnchors(client.field_1687);
        NonDebugSpinMusicClient.stopInactiveSounds(client, anchors.keySet());
        for (Map.Entry<UUID, UUID> entry : anchors.entrySet()) {
            NonDebugSpinMusicClient.playForInstance(client, entry.getKey(), entry.getValue());
        }
    }

    public static void stopAll(class_310 client) {
        if (ACTIVE.isEmpty()) {
            return;
        }
        for (SpinMusicSound sound : ACTIVE.values()) {
            sound.finish();
            if (client == null) continue;
            client.method_1483().method_4870((class_1113)sound);
        }
        ACTIVE.clear();
    }

    private static Map<UUID, UUID> findActiveInstanceAnchors(class_638 world) {
        if (world == null) {
            return Map.of();
        }
        HashMap<UUID, UUID> anchors = new HashMap<UUID, UUID>();
        for (class_1297 entity : world.method_18112()) {
            class_1309 living;
            UUID actorUuid;
            UUID instanceId;
            if (!(entity instanceof class_1309) || (instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)(actorUuid = (living = (class_1309)entity).method_5667()))) == null || anchors.containsKey(instanceId)) continue;
            UUID anchorUuid = AfwClientAnimationRuntime.findAnchorUuidForActor((UUID)actorUuid);
            anchors.put(instanceId, anchorUuid != null ? anchorUuid : actorUuid);
        }
        return anchors.isEmpty() ? Map.of() : Map.copyOf(anchors);
    }

    private static void stopInactiveSounds(class_310 client, Set<UUID> activeInstances) {
        Iterator<Map.Entry<UUID, SpinMusicSound>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, SpinMusicSound> entry = iterator.next();
            if (activeInstances.contains(entry.getKey())) continue;
            SpinMusicSound sound = entry.getValue();
            if (sound != null) {
                sound.finish();
                client.method_1483().method_4870((class_1113)sound);
            }
            iterator.remove();
        }
    }

    private static void pruneStoppedSounds(class_310 client) {
        Iterator<Map.Entry<UUID, SpinMusicSound>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            SpinMusicSound sound = iterator.next().getValue();
            if (sound != null && !sound.method_4793() && (client == null || client.method_1483().method_4877((class_1113)sound))) continue;
            iterator.remove();
        }
    }

    private static void playForInstance(class_310 client, UUID instanceId, UUID anchorUuid) {
        if (client == null || client.field_1687 == null || instanceId == null || anchorUuid == null) {
            return;
        }
        SpinMusicSound existing = ACTIVE.get(instanceId);
        if (existing != null && !existing.method_4793()) {
            return;
        }
        class_1297 anchor = client.field_1687.method_66347(anchorUuid);
        if (anchor == null) {
            return;
        }
        SpinMusicSound sound = new SpinMusicSound(instanceId, anchorUuid, anchor instanceof class_1657 ? class_3419.field_15248 : class_3419.field_15254, 0.025f, 1.0f);
        ACTIVE.put(instanceId, sound);
        client.method_1483().method_4873((class_1113)sound);
    }

    private static final class SpinMusicSound
    extends class_1101 {
        private final UUID instanceId;
        private final UUID anchorUuid;

        private SpinMusicSound(UUID instanceId, UUID anchorUuid, class_3419 category, float volume, float pitch) {
            super((class_3414)class_7923.field_41172.method_63535(SOUND_ID), category, class_1113.method_43221());
            this.instanceId = instanceId;
            this.anchorUuid = anchorUuid;
            this.field_5442 = volume;
            this.field_5441 = pitch;
            this.field_5446 = true;
            this.field_5451 = 0;
            this.field_18936 = false;
            this.field_5440 = class_1113.class_1114.field_5476;
            this.updatePosition();
        }

        public void method_16896() {
            if (!NonDebugSpinMode.isEnabled() || AfwClientAnimationRuntime.findCurrentStage((UUID)this.instanceId) == null || !this.updatePosition()) {
                this.method_24876();
            }
        }

        private boolean updatePosition() {
            class_310 client = class_310.method_1551();
            if (client == null || client.field_1687 == null) {
                return false;
            }
            class_1297 anchor = client.field_1687.method_66347(this.anchorUuid);
            if (anchor == null) {
                return false;
            }
            this.field_5439 = anchor.method_23317();
            this.field_5450 = anchor.method_23318();
            this.field_5449 = anchor.method_23321();
            return true;
        }

        private void finish() {
            this.method_24876();
        }
    }
}

