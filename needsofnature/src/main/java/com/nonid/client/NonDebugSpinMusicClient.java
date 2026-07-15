/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.client.runtime.AfwClientAnimationRuntime
 *  net.minecraft.client.sound.MovingSoundInstance
 *  net.minecraft.client.sound.SoundInstance
 *  net.minecraft.client.sound.SoundInstance$AttenuationType
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.sound.SoundEvent
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.registry.Registries
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
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundCategory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Registries;

public final class NonDebugSpinMusicClient {
    private static final Identifier SOUND_ID = NeedsOfNature.id("funkytown");
    private static final float VOLUME = 0.025f;
    private static final float PITCH = 1.0f;
    private static final Map<UUID, SpinMusicSound> ACTIVE = new HashMap<UUID, SpinMusicSound>();

    private NonDebugSpinMusicClient() {
    }

    public static void clientTick(MinecraftClient client) {
        if (client == null) {
            return;
        }
        if (!NonDebugSpinMode.isEnabled() || client.world == null) {
            NonDebugSpinMusicClient.stopAll(client);
            return;
        }
        NonDebugSpinMusicClient.pruneStoppedSounds(client);
        Map<UUID, UUID> anchors = NonDebugSpinMusicClient.findActiveInstanceAnchors(client.world);
        NonDebugSpinMusicClient.stopInactiveSounds(client, anchors.keySet());
        for (Map.Entry<UUID, UUID> entry : anchors.entrySet()) {
            NonDebugSpinMusicClient.playForInstance(client, entry.getKey(), entry.getValue());
        }
    }

    public static void stopAll(MinecraftClient client) {
        if (ACTIVE.isEmpty()) {
            return;
        }
        for (SpinMusicSound sound : ACTIVE.values()) {
            sound.finish();
            if (client == null) continue;
            client.getSoundManager().stop((SoundInstance)sound);
        }
        ACTIVE.clear();
    }

    private static Map<UUID, UUID> findActiveInstanceAnchors(ClientWorld world) {
        if (world == null) {
            return Map.of();
        }
        HashMap<UUID, UUID> anchors = new HashMap<UUID, UUID>();
        for (Entity entity : world.getEntities()) {
            LivingEntity living;
            UUID actorUuid;
            UUID instanceId;
            if (!(entity instanceof LivingEntity) || (instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)(actorUuid = (living = (LivingEntity)entity).getUuid()))) == null || anchors.containsKey(instanceId)) continue;
            UUID anchorUuid = AfwClientAnimationRuntime.findAnchorUuidForActor((UUID)actorUuid);
            anchors.put(instanceId, anchorUuid != null ? anchorUuid : actorUuid);
        }
        return anchors.isEmpty() ? Map.of() : Map.copyOf(anchors);
    }

    private static void stopInactiveSounds(MinecraftClient client, Set<UUID> activeInstances) {
        Iterator<Map.Entry<UUID, SpinMusicSound>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, SpinMusicSound> entry = iterator.next();
            if (activeInstances.contains(entry.getKey())) continue;
            SpinMusicSound sound = entry.getValue();
            if (sound != null) {
                sound.finish();
                client.getSoundManager().stop((SoundInstance)sound);
            }
            iterator.remove();
        }
    }

    private static void pruneStoppedSounds(MinecraftClient client) {
        Iterator<Map.Entry<UUID, SpinMusicSound>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            SpinMusicSound sound = iterator.next().getValue();
            if (sound != null && !sound.isDone() && (client == null || client.getSoundManager().isPlaying((SoundInstance)sound))) continue;
            iterator.remove();
        }
    }

    private static void playForInstance(MinecraftClient client, UUID instanceId, UUID anchorUuid) {
        if (client == null || client.world == null || instanceId == null || anchorUuid == null) {
            return;
        }
        SpinMusicSound existing = ACTIVE.get(instanceId);
        if (existing != null && !existing.isDone()) {
            return;
        }
        Entity anchor = NonDebugSpinMusicClient.findEntity(client.world, anchorUuid);
        if (anchor == null) {
            return;
        }
        SpinMusicSound sound = new SpinMusicSound(instanceId, anchorUuid, anchor instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.NEUTRAL, 0.025f, 1.0f);
        ACTIVE.put(instanceId, sound);
        client.getSoundManager().play((SoundInstance)sound);
    }

    private static Entity findEntity(ClientWorld world, UUID uuid) {
        if (world == null || uuid == null) return null;
        for (Entity entity : world.getEntities()) {
            if (uuid.equals(entity.getUuid())) return entity;
        }
        return null;
    }

    private static final class SpinMusicSound
    extends MovingSoundInstance {
        private final UUID instanceId;
        private final UUID anchorUuid;

        private SpinMusicSound(UUID instanceId, UUID anchorUuid, SoundCategory category, float volume, float pitch) {
            super((SoundEvent)Registries.SOUND_EVENT.get(SOUND_ID), category, SoundInstance.createRandom());
            this.instanceId = instanceId;
            this.anchorUuid = anchorUuid;
            this.volume = volume;
            this.pitch = pitch;
            this.repeat = true;
            this.repeatDelay = 0;
            this.relative = false;
            this.attenuationType = SoundInstance.AttenuationType.LINEAR;
            this.updatePosition();
        }

        public void tick() {
            if (!NonDebugSpinMode.isEnabled() || AfwClientAnimationRuntime.findCurrentStage((UUID)this.instanceId) == null || !this.updatePosition()) {
                this.setDone();
            }
        }

        private boolean updatePosition() {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.world == null) {
                return false;
            }
            Entity anchor = NonDebugSpinMusicClient.findEntity(client.world, this.anchorUuid);
            if (anchor == null) {
                return false;
            }
            this.x = anchor.getX();
            this.y = anchor.getY();
            this.z = anchor.getZ();
            return true;
        }

        private void finish() {
            this.setDone();
        }
    }
}

