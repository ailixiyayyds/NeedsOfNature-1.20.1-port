/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.Event
 *  net.fabricmc.fabric.api.event.EventFactory
 *  net.minecraft.class_1309
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.api;

import java.util.UUID;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_1309;
import net.minecraft.class_2960;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AfwSoundCueEvents {
    public static final Event<@NotNull SoundResolver> RESOLVE = EventFactory.createArrayBacked(SoundResolver.class, callbacks -> context -> {
        for (SoundResolver callback : callbacks) {
            SoundOverride override = callback.resolve(context);
            if (override == null) continue;
            return override;
        }
        return null;
    });

    private AfwSoundCueEvents() {
    }

    @FunctionalInterface
    public static interface SoundResolver {
        @Nullable
        public SoundOverride resolve(SoundContext var1);
    }

    public record SoundContext(class_1309 anchor, class_2960 animationId, class_2960 animationResource, String animationKey, String effect, double timeSeconds, float volume, float pitch, UUID instanceId, int stageIndex, boolean loop, double cueTimeTicks, double cycleTicks, double animationSpeed, double nextSameEffectCueDelayTicks, double nextSameEffectCueDelayMs) {
    }

    public record SoundOverride(@Nullable class_2960 soundId, float volume, float pitch, boolean cancel) {
        public static SoundOverride play(class_2960 soundId, float volume, float pitch) {
            return new SoundOverride(soundId, volume, pitch, false);
        }

        public static SoundOverride play(class_2960 soundId) {
            return new SoundOverride(soundId, 1.0f, 1.0f, false);
        }

        public static SoundOverride skip() {
            return new SoundOverride(null, 1.0f, 1.0f, true);
        }
    }
}

