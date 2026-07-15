/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.util.Identifier
 *  net.minecraft.registry.Registries
 *  net.minecraft.item.ItemDisplayContext
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodecs
 *  net.minecraft.network.codec.PacketCodec
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

public record AnimationStageInfo(Identifier animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed, Identifier playbackAnimationId, List<ActorPropEntry> actorProps, double cycleMidpointOffsetSeconds, double cycleTicks) {
    public static final Codec<AnimationStageInfo> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(AnimationStageInfo::animationId),
            Codec.BOOL.optionalFieldOf("loop", true).forGetter(AnimationStageInfo::loop),
            Codec.DOUBLE.optionalFieldOf("cycle_seconds", 0.0).forGetter(stage -> exactTicksToSeconds(stage.cycleTicks())),
            Codec.BOOL.optionalFieldOf("allow_join", true).forGetter(AnimationStageInfo::allowJoin),
            Codec.DOUBLE.optionalFieldOf("speed", 1.0).forGetter(AnimationStageInfo::speed),
            Codec.DOUBLE.optionalFieldOf("cycle_midpoint_offset_seconds", 0.0).forGetter(AnimationStageInfo::cycleMidpointOffsetSeconds)
    ).apply(instance, (animationId, loop, cycleSeconds, allowJoin, speed, offset) ->
            new AnimationStageInfo(animationId, loop, secondsToTicks(cycleSeconds), allowJoin, speed,
                    animationId, List.of(), offset, secondsToExactTicks(cycleSeconds))));

    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(animationId);
        buf.writeBoolean(loop);
        buf.writeLong(lengthTicks);
        buf.writeBoolean(allowJoin);
        buf.writeDouble(speed);
        buf.writeIdentifier(playbackAnimationId);
        buf.writeVarInt(actorProps.size());
        actorProps.forEach(entry -> entry.write(buf));
        buf.writeDouble(cycleMidpointOffsetSeconds);
        buf.writeDouble(cycleTicks);
    }

    public static AnimationStageInfo read(PacketByteBuf buf) {
        Identifier animationId = buf.readIdentifier();
        boolean loop = buf.readBoolean();
        long lengthTicks = buf.readLong();
        boolean allowJoin = buf.readBoolean();
        double speed = buf.readDouble();
        Identifier playbackAnimationId = buf.readIdentifier();
        int propCount = buf.readVarInt();
        if (propCount < 0 || propCount > 64) throw new IllegalArgumentException("Invalid actor prop count: " + propCount);
        List<ActorPropEntry> props = new ArrayList<>(propCount);
        for (int i = 0; i < propCount; i++) props.add(ActorPropEntry.read(buf));
        return new AnimationStageInfo(animationId, loop, lengthTicks, allowJoin, speed, playbackAnimationId,
                props, buf.readDouble(), buf.readDouble());
    }

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

    public AnimationStageInfo(Identifier animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed) {
        this(animationId, loop, lengthTicks, allowJoin, speed, animationId, List.of(), 0.0, lengthTicks);
    }

    public AnimationStageInfo(Identifier animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed, Identifier playbackAnimationId) {
        this(animationId, loop, lengthTicks, allowJoin, speed, playbackAnimationId, List.of(), 0.0, lengthTicks);
    }

    public AnimationStageInfo(Identifier animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed, Identifier playbackAnimationId, List<ActorPropEntry> actorProps) {
        this(animationId, loop, lengthTicks, allowJoin, speed, playbackAnimationId, actorProps, 0.0, lengthTicks);
    }

    public AnimationStageInfo(Identifier animationId, boolean loop, long lengthTicks, boolean allowJoin, double speed, Identifier playbackAnimationId, List<ActorPropEntry> actorProps, double cycleMidpointOffsetSeconds) {
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

    public Identifier effectiveAnimationId() {
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
            Identifier left = entry.propLeftItemId();
            Identifier right = entry.propRightItemId();
            if (left == null && right == null) continue;
            dedup.put(key, new ActorPropEntry(key, left, right));
        }
        if (dedup.isEmpty()) {
            return List.of();
        }
        ArrayList out = new ArrayList(dedup.values());
        return List.copyOf(out);
    }

    public record ActorPropEntry(String actorKey, @Nullable Identifier propLeftItemId, @Nullable Identifier propRightItemId) {
        public ActorPropEntry(String actorKey, @Nullable Identifier propLeftItemId, @Nullable Identifier propRightItemId) {
            this.actorKey = actorKey = actorKey == null ? "" : actorKey.trim();
            this.propLeftItemId = propLeftItemId;
            this.propRightItemId = propRightItemId;
        }

        public boolean isEmpty() {
            return this.propLeftItemId == null && this.propRightItemId == null;
        }

        public void write(PacketByteBuf buf) {
            buf.writeString(actorKey, 128);
            buf.writeBoolean(propLeftItemId != null);
            if (propLeftItemId != null) buf.writeIdentifier(propLeftItemId);
            buf.writeBoolean(propRightItemId != null);
            if (propRightItemId != null) buf.writeIdentifier(propRightItemId);
        }

        public static ActorPropEntry read(PacketByteBuf buf) {
            String actorKey = buf.readString(128);
            Identifier left = buf.readBoolean() ? buf.readIdentifier() : null;
            Identifier right = buf.readBoolean() ? buf.readIdentifier() : null;
            return new ActorPropEntry(actorKey, left, right);
        }

        @Nullable
        public ItemStack leftStack() {
            return ActorPropEntry.createStack(this.propLeftItemId);
        }

        @Nullable
        public ItemStack rightStack() {
            return ActorPropEntry.createStack(this.propRightItemId);
        }

        @Nullable
        public AfwGeckoModelEvents.BoneItemProp leftBoneProp() {
            ItemStack stack = this.leftStack();
            if (stack == null || stack.isEmpty()) {
                return null;
            }
            return new AfwGeckoModelEvents.BoneItemProp(stack, ModelTransformationMode.THIRD_PERSON_LEFT_HAND);
        }

        @Nullable
        public AfwGeckoModelEvents.BoneItemProp rightBoneProp() {
            ItemStack stack = this.rightStack();
            if (stack == null || stack.isEmpty()) {
                return null;
            }
            return new AfwGeckoModelEvents.BoneItemProp(stack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND);
        }

        @Nullable
        private static ItemStack createStack(@Nullable Identifier itemId) {
            if (itemId == null || !Registries.ITEM.containsId(itemId)) {
                return null;
            }
            return new ItemStack((ItemConvertible)Registries.ITEM.get(itemId));
        }

        @Nullable
        private static Identifier parseIdentifier(String raw) {
            if (raw == null || raw.isBlank()) {
                return null;
            }
            Identifier id = Identifier.tryParse((String)raw);
            return id != null && Registries.ITEM.containsId(id) ? id : null;
        }

        private static String identifierToString(@Nullable Identifier id) {
            return id == null ? "" : id.toString();
        }
    }
}

