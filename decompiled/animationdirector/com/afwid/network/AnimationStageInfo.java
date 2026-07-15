/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.class_1799
 *  net.minecraft.class_1935
 *  net.minecraft.class_2960
 *  net.minecraft.class_7923
 *  net.minecraft.class_811
 *  net.minecraft.class_9129
 *  net.minecraft.class_9135
 *  net.minecraft.class_9139
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.network;

import com.afwid.api.AfwGeckoModelEvents;
import com.afwid.util.AfwStageTimeWarp;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.class_1799;
import net.minecraft.class_1935;
import net.minecraft.class_2960;
import net.minecraft.class_7923;
import net.minecraft.class_811;
import net.minecraft.class_9129;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import org.jetbrains.annotations.Nullable;

public record AnimationStageInfo(class_2960 animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed, class_2960 playbackAnimationId, List<ActorPropEntry> actorProps, double cycleMidpointOffsetSeconds, double cycleTicks) {
    public static final Codec<AnimationStageInfo> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group((App)class_2960.field_25139.fieldOf("id").forGetter(AnimationStageInfo::animationId), (App)Codec.BOOL.optionalFieldOf("loop", (Object)true).forGetter(AnimationStageInfo::loop), (App)Codec.DOUBLE.optionalFieldOf("cycle_seconds", (Object)0.0).forGetter(stage -> AnimationStageInfo.exactTicksToSeconds(stage.cycleTicks())), (App)Codec.BOOL.optionalFieldOf("allow_join", (Object)true).forGetter(AnimationStageInfo::allowJoin), (App)Codec.DOUBLE.optionalFieldOf("speed", (Object)1.0).forGetter(AnimationStageInfo::speed), (App)Codec.DOUBLE.optionalFieldOf("cycle_midpoint_offset_seconds", (Object)0.0).forGetter(AnimationStageInfo::cycleMidpointOffsetSeconds)).apply((Applicative)instance, (animationId, loop, cycleSeconds, allowJoin, speed, offset) -> new AnimationStageInfo((class_2960)animationId, (boolean)loop, AnimationStageInfo.secondsToTicks(cycleSeconds), (boolean)allowJoin, (double)speed, (class_2960)animationId, List.of(), (double)offset, AnimationStageInfo.secondsToExactTicks(cycleSeconds))));
    public static final class_9139<class_9129, AnimationStageInfo> CODEC = class_9139.method_67079((class_9139)class_2960.field_48267.method_56430(), AnimationStageInfo::animationId, (class_9139)class_9135.field_48547, AnimationStageInfo::loop, (class_9139)class_9135.field_54505, AnimationStageInfo::lengthTicks, (class_9139)class_9135.field_48547, AnimationStageInfo::allowJoin, (class_9139)class_9135.field_48553, AnimationStageInfo::speed, (class_9139)class_2960.field_48267.method_56430(), AnimationStageInfo::playbackAnimationId, (class_9139)ActorPropEntry.CODEC.method_56433(class_9135.method_56363()).method_56432(List::copyOf, list -> list).method_56430(), AnimationStageInfo::actorProps, (class_9139)class_9135.field_48553, AnimationStageInfo::cycleMidpointOffsetSeconds, (class_9139)class_9135.field_48553, AnimationStageInfo::cycleTicks, AnimationStageInfo::new);

    public AnimationStageInfo {
        if (animationId == null) {
            throw new IllegalArgumentException("animationId must not be null");
        }
        if (playbackAnimationId == null) {
            playbackAnimationId = animationId;
        }
        if (lengthTicks < 0L) {
            lengthTicks = 0L;
        }
        if (!Double.isFinite(cycleTicks) || cycleTicks <= 0.0) {
            cycleTicks = lengthTicks > 0L ? (double)lengthTicks : 0.0;
        }
        actorProps = AnimationStageInfo.sanitizeActorProps(actorProps);
        cycleMidpointOffsetSeconds = !loop || cycleTicks <= 0.0 || !Double.isFinite(cycleMidpointOffsetSeconds) ? 0.0 : AfwStageTimeWarp.clampOffsetSeconds(cycleTicks, cycleMidpointOffsetSeconds);
    }

    public AnimationStageInfo(class_2960 animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed) {
        this(animationId, loop, lengthTicks, allowJoin, speed, animationId, List.of(), 0.0, lengthTicks);
    }

    public AnimationStageInfo(class_2960 animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed, class_2960 playbackAnimationId) {
        this(animationId, loop, lengthTicks, allowJoin, speed, playbackAnimationId, List.of(), 0.0, lengthTicks);
    }

    public AnimationStageInfo(class_2960 animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed, class_2960 playbackAnimationId, List<ActorPropEntry> actorProps) {
        this(animationId, loop, lengthTicks, allowJoin, speed, playbackAnimationId, actorProps, 0.0, lengthTicks);
    }

    public AnimationStageInfo(class_2960 animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed, class_2960 playbackAnimationId, List<ActorPropEntry> actorProps, double cycleMidpointOffsetSeconds) {
        this(animationId, loop, lengthTicks, allowJoin, speed, playbackAnimationId, actorProps, cycleMidpointOffsetSeconds, lengthTicks);
    }

    private static long secondsToTicks(double seconds) {
        if (!Double.isFinite(seconds) || seconds <= 0.0) {
            return 0L;
        }
        return Math.max(1L, (long)Math.ceil(seconds * 20.0));
    }

    private static double ticksToSeconds(long ticks) {
        if (ticks <= 0L) {
            return 0.0;
        }
        return (double)ticks / 20.0;
    }

    private static double secondsToExactTicks(double seconds) {
        if (!Double.isFinite(seconds) || seconds <= 0.0) {
            return 0.0;
        }
        return seconds * 20.0;
    }

    private static double exactTicksToSeconds(double ticks) {
        if (!Double.isFinite(ticks) || ticks <= 0.0) {
            return 0.0;
        }
        return ticks / 20.0;
    }

    @Nullable
    public ActorPropEntry actorPropsForKey(@Nullable String actorKey) {
        if (actorKey == null || actorKey.isBlank() || this.actorProps.isEmpty()) {
            return null;
        }
        for (ActorPropEntry entry : this.actorProps) {
            if (entry == null || !actorKey.equals(entry.actorKey())) continue;
            return entry;
        }
        return null;
    }

    public class_2960 effectiveAnimationId() {
        return this.playbackAnimationId;
    }

    private static List<ActorPropEntry> sanitizeActorProps(List<ActorPropEntry> props) {
        if (props == null || props.isEmpty()) {
            return List.of();
        }
        LinkedHashMap<String, ActorPropEntry> dedup = new LinkedHashMap<String, ActorPropEntry>();
        for (ActorPropEntry entry : props) {
            String key;
            if (entry == null) continue;
            String string = key = entry.actorKey() == null ? "" : entry.actorKey().trim();
            if (key.isBlank()) continue;
            class_2960 left = entry.propLeftItemId();
            class_2960 right = entry.propRightItemId();
            if (left == null && right == null) continue;
            dedup.put(key, new ActorPropEntry(key, left, right));
        }
        if (dedup.isEmpty()) {
            return List.of();
        }
        ArrayList out = new ArrayList(dedup.values());
        return List.copyOf(out);
    }

    public record ActorPropEntry(String actorKey, @Nullable class_2960 propLeftItemId, @Nullable class_2960 propRightItemId) {
        private static final class_9139<class_9129, String> NULLABLE_IDENTIFIER_STRING_CODEC = class_9135.field_48554.method_56430();
        public static final class_9139<class_9129, ActorPropEntry> CODEC = class_9139.method_56436((class_9139)class_9135.field_48554, ActorPropEntry::actorKey, NULLABLE_IDENTIFIER_STRING_CODEC, entry -> ActorPropEntry.identifierToString(entry.propLeftItemId()), NULLABLE_IDENTIFIER_STRING_CODEC, entry -> ActorPropEntry.identifierToString(entry.propRightItemId()), (actorKey, leftId, rightId) -> new ActorPropEntry((String)actorKey, ActorPropEntry.parseIdentifier(leftId), ActorPropEntry.parseIdentifier(rightId)));

        public ActorPropEntry(String actorKey, @Nullable class_2960 propLeftItemId, @Nullable class_2960 propRightItemId) {
            this.actorKey = actorKey = actorKey == null ? "" : actorKey.trim();
            this.propLeftItemId = propLeftItemId;
            this.propRightItemId = propRightItemId;
        }

        public boolean isEmpty() {
            return this.propLeftItemId == null && this.propRightItemId == null;
        }

        @Nullable
        public class_1799 leftStack() {
            return ActorPropEntry.createStack(this.propLeftItemId);
        }

        @Nullable
        public class_1799 rightStack() {
            return ActorPropEntry.createStack(this.propRightItemId);
        }

        @Nullable
        public AfwGeckoModelEvents.BoneItemProp leftBoneProp() {
            class_1799 stack = this.leftStack();
            if (stack == null || stack.method_7960()) {
                return null;
            }
            return new AfwGeckoModelEvents.BoneItemProp(stack, class_811.field_4323);
        }

        @Nullable
        public AfwGeckoModelEvents.BoneItemProp rightBoneProp() {
            class_1799 stack = this.rightStack();
            if (stack == null || stack.method_7960()) {
                return null;
            }
            return new AfwGeckoModelEvents.BoneItemProp(stack, class_811.field_4320);
        }

        @Nullable
        private static class_1799 createStack(@Nullable class_2960 itemId) {
            if (itemId == null || !class_7923.field_41178.method_10250(itemId)) {
                return null;
            }
            return new class_1799((class_1935)class_7923.field_41178.method_63535(itemId));
        }

        @Nullable
        private static class_2960 parseIdentifier(String raw) {
            if (raw == null || raw.isBlank()) {
                return null;
            }
            class_2960 id = class_2960.method_12829((String)raw);
            return id != null && class_7923.field_41178.method_10250(id) ? id : null;
        }

        private static String identifierToString(@Nullable class_2960 id) {
            return id == null ? "" : id.toString();
        }
    }
}

