/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.client.runtime.AfwClientAnimationRuntime
 *  net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
 *  net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents
 *  net.minecraft.client.render.entity.state.ItemStackEntityRenderState
 *  net.minecraft.client.render.command.OrderedRenderCommandQueue
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.BlockView
 *  net.minecraft.world.World
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction$Axis
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.shape.VoxelShape
 *  net.minecraft.block.BlockState
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.util.math.RotationAxis
 *  net.minecraft.client.render.entity.ItemEntityRenderer
 *  org.joml.Quaternionfc
 */
package com.nonid.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import com.nonid.NeedsOfNature;
import com.nonid.NonAccessoryShedSystem;
import com.nonid.NonTrinketsIntegration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.RotationAxis;

public final class NonArmorShedClient {
    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final int FLYING_TICKS = 24;
    private static final int GROUND_TICKS = 40;
    private static final int GROUND_FADE_TICKS = 30;
    private static final double GRAVITY = 0.035;
    private static final double AIR_DRAG = 0.96;
    private static final double GROUND_DRAG = 0.78;
    private static final float ITEM_SCALE = 1.0f;
    private static final long FRESH_START_MAX_AGE_TICKS = 5L;
    private static final long QUEUED_HANDOFF_SKIP_TICKS = 12L;
    private static final Identifier PLAYER_DEFEATED = Identifier.of((String)"animationframework", (String)"player_defeated-belly");
    private static final Identifier PLAYER_BIRTH = Identifier.of((String)"animationframework", (String)"player_birth");
    private static final List<ArmorProp> PROPS = new ArrayList<ArmorProp>();
    private static final Map<UUID, Long> LAST_SPAWNED_START_TICK = new HashMap<UUID, Long>();
    private static final Map<UUID, Long> LAST_ACTIVE_TICK = new HashMap<UUID, Long>();

    private NonArmorShedClient() {
    }

    public static void register() {
        WorldRenderEvents.AFTER_ENTITIES.register(NonArmorShedClient::render);
    }

    public static void clientTick(MinecraftClient client) {
        if (!NeedsOfNature.getConfig().isArmorShedEffectEnabled()) {
            NonArmorShedClient.clear();
            return;
        }
        NonArmorShedClient.tickProps(client);
        if (client == null || client.world == null) {
            NonArmorShedClient.clear();
            return;
        }
        long now = client.world.getTime();
        for (AbstractClientPlayerEntity player : client.world.getPlayers()) {
            if (player == null || player.isRemoved()) continue;
            UUID uuid = player.getUuid();
            if (!AfwClientAnimationRuntime.isActorActive((UUID)uuid)) {
                LAST_SPAWNED_START_TICK.remove(uuid);
                continue;
            }
            Long previousActiveTick = LAST_ACTIVE_TICK.get(uuid);
            Long startTick = AfwClientAnimationRuntime.findStartTickForActor((UUID)uuid);
            if (startTick == null) {
                LAST_ACTIVE_TICK.put(uuid, now);
                continue;
            }
            Long lastSpawned = LAST_SPAWNED_START_TICK.get(uuid);
            if (lastSpawned != null && lastSpawned.equals(startTick)) {
                LAST_ACTIVE_TICK.put(uuid, now);
                continue;
            }
            if (NonArmorShedClient.shouldSpawnForStart(uuid, now, startTick, previousActiveTick)) {
                NonArmorShedClient.spawnForPlayer(player, startTick);
            }
            LAST_SPAWNED_START_TICK.put(uuid, startTick);
            LAST_ACTIVE_TICK.put(uuid, now);
        }
    }

    public static void clear() {
        PROPS.clear();
        LAST_SPAWNED_START_TICK.clear();
        LAST_ACTIVE_TICK.clear();
    }

    private static boolean shouldSpawnForStart(UUID playerUuid, long now, long startTick, Long previousActiveTick) {
        if (now - startTick > 5L) {
            return false;
        }
        if (previousActiveTick != null && now - previousActiveTick <= 12L) {
            return false;
        }
        Identifier activeAnimation = AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining((UUID)playerUuid);
        return !NonArmorShedClient.isQueuedFollowUpAnimation(activeAnimation);
    }

    private static boolean isQueuedFollowUpAnimation(Identifier animationId) {
        if (animationId == null) {
            return false;
        }
        return PLAYER_DEFEATED.equals((Object)animationId) || PLAYER_BIRTH.equals((Object)animationId);
    }

    private static void spawnForPlayer(AbstractClientPlayerEntity player, long startTick) {
        Vec3d origin = new Vec3d(player.getX(), player.getY() + (double)player.getHeight() * 0.72, player.getZ());
        long seedBase = player.getUuid().getMostSignificantBits() ^ player.getUuid().getLeastSignificantBits() ^ startTick;
        Random random = new Random(seedBase);
        float yawRad = player.getYaw() * ((float)Math.PI / 180);
        Vec3d forward = new Vec3d(-MathHelper.sin(yawRad), 0.0, MathHelper.cos(yawRad));
        Vec3d right = new Vec3d(forward.z, 0.0, -forward.x);
        for (int i = 0; i < ARMOR_SLOTS.length; ++i) {
            EquipmentSlot slot = ARMOR_SLOTS[i];
            ItemStack stack = player.getEquippedStack(slot);
            if (stack == null || stack.isEmpty()) continue;
            double side = ((double)i - 1.5) * 0.18;
            Vec3d spawnPos = origin.add(right.multiply(side)).add(0.0, -0.08 * (double)i, 0.0);
            NonArmorShedClient.addProp(stack, spawnPos, forward, right, random, seedBase, i, ARMOR_SLOTS.length);
        }
        NonArmorShedClient.spawnShedAccessories(player, origin, forward, right, random, seedBase);
    }

    private static void spawnShedAccessories(AbstractClientPlayerEntity player, Vec3d origin, Vec3d forward, Vec3d right, Random random, long seedBase) {
        NonAccessoryShedSystem.ShedState shedState = NonAccessoryShedSystem.getShedState(player.getUuid());
        if (shedState.isEmpty()) {
            return;
        }
        List<ItemStack> stacks = NonTrinketsIntegration.getVisuallyShedAccessoryStacks((LivingEntity)player);
        for (int index = 0; index < stacks.size(); ++index) {
            ItemStack stack = stacks.get(index);
            if (stack == null || stack.isEmpty()) continue;
            double side = ((double)index - 0.5) * 0.24;
            Vec3d spawnPos = origin.add(right.multiply(side)).add(0.0, -0.34, 0.0);
            NonArmorShedClient.addProp(stack, spawnPos, forward, right, random, seedBase, 20 + index, 2);
        }
    }

    private static void addProp(ItemStack stack, Vec3d spawnPos, Vec3d forward, Vec3d right, Random random, long seedBase, int index, int count) {
        ItemStack visualStack = stack.copy();
        visualStack.setCount(1);
        double angle = Math.PI * 2 * (double)index / (double)Math.max(1, count) + (random.nextDouble() - 0.5) * 0.55;
        Vec3d outward = right.multiply(Math.cos(angle)).add(forward.multiply(Math.sin(angle))).normalize();
        double speed = 0.105 + random.nextDouble() * 0.035;
        Vec3d velocity = outward.multiply(speed).add(0.0, 0.15 + random.nextDouble() * 0.13, 0.0);
        PROPS.add(new ArmorProp(visualStack, spawnPos, velocity, random.nextFloat() * 360.0f, random.nextFloat() * 360.0f, 7.0f + random.nextFloat() * 7.0f, 10.0f + random.nextFloat() * 12.0f, (int)(seedBase + (long)index * 31L)));
    }

    private static void tickProps(MinecraftClient client) {
        if (PROPS.isEmpty()) {
            return;
        }
        if (client == null || client.world == null) {
            NonArmorShedClient.clear();
            return;
        }
        Iterator<ArmorProp> iterator = PROPS.iterator();
        while (iterator.hasNext()) {
            ArmorProp prop = iterator.next();
            ++prop.age;
            prop.previousPosition = prop.position;
            if (prop.landed) {
                ++prop.groundAge;
                prop.velocity = prop.velocity.multiply(0.78, 0.0, 0.78);
                if (prop.groundAge <= 40) continue;
                iterator.remove();
                continue;
            }
            prop.velocity = prop.velocity.add(0.0, -0.035, 0.0).multiply(0.96, 0.98, 0.96);
            Vec3d next = prop.position.add(prop.velocity);
            GroundHit hit = NonArmorShedClient.findGround(client, next);
            if (hit.hit() && prop.velocity.y <= 0.0 && prop.age > 3) {
                prop.position = new Vec3d(next.x, hit.y(), next.z);
                prop.velocity = Vec3d.ZERO;
                prop.landed = true;
                continue;
            }
            prop.position = next;
            if (prop.age <= 64) continue;
            iterator.remove();
        }
    }

    private static GroundHit findGround(MinecraftClient client, Vec3d pos) {
        if (client == null || client.world == null) {
            return GroundHit.MISS;
        }
        BlockPos blockPos = BlockPos.ofFloored((double)pos.x, (double)(pos.y - 0.04), (double)pos.z);
        BlockState state = client.world.getBlockState(blockPos);
        VoxelShape shape = state.getCollisionShape((BlockView)client.world, blockPos);
        if (shape.isEmpty()) {
            return GroundHit.MISS;
        }
        double top = (double)blockPos.getY() + shape.getMax(Direction.Axis.Y);
        if (pos.y < top + 0.12) {
            return new GroundHit(true, top + 0.03);
        }
        return GroundHit.MISS;
    }

    private static void render(WorldRenderContext context) {
        if (!NeedsOfNature.getConfig().isArmorShedEffectEnabled()) {
            return;
        }
        if (PROPS.isEmpty()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) {
            return;
        }
        Vec3d cameraPos = context.camera().getPos();
        if (cameraPos == null) {
            return;
        }
        float tickProgress = client.getTickDelta();
        MatrixStack matrices = context.matrixStack();
        for (ArmorProp prop : PROPS) {
            Vec3d renderPos = prop.previousPosition.lerp(prop.position, (double)tickProgress);
            matrices.push();
            matrices.translate(renderPos.x - cameraPos.x, renderPos.y - cameraPos.y, renderPos.z - cameraPos.z);
            float smoothAge = (float)prop.age + tickProgress;
            float yaw = prop.landed ? prop.groundYaw : prop.baseYaw + smoothAge * prop.yawSpeed;
            float pitch = prop.landed ? 90.0f : prop.basePitch + smoothAge * prop.pitchSpeed;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
            float fadeScale = prop.landed ? prop.groundFadeScale(tickProgress) : 1.0f;
            matrices.scale(1.0f * fadeScale, 1.0f * fadeScale, 1.0f * fadeScale);
            client.getItemRenderer().renderItem(prop.stack, ModelTransformationMode.GROUND, 0xF000F0, OverlayTexture.DEFAULT_UV, matrices, context.consumers(), client.world, prop.seed);
            matrices.pop();
        }
    }

    private static final class ArmorProp {
        final ItemStack stack;
        Vec3d position;
        Vec3d previousPosition;
        Vec3d velocity;
        final float baseYaw;
        final float basePitch;
        final float yawSpeed;
        final float pitchSpeed;
        final float groundYaw;
        final int seed;
        int age;
        int groundAge;
        boolean landed;

        ArmorProp(ItemStack stack, Vec3d position, Vec3d velocity, float baseYaw, float basePitch, float yawSpeed, float pitchSpeed, int seed) {
            this.stack = stack;
            this.position = position;
            this.previousPosition = position;
            this.velocity = velocity;
            this.baseYaw = baseYaw;
            this.basePitch = basePitch;
            this.yawSpeed = yawSpeed;
            this.pitchSpeed = pitchSpeed;
            this.groundYaw = baseYaw;
            this.seed = seed;
        }

        float groundFadeScale(float tickProgress) {
            float smoothGroundAge = (float)this.groundAge + tickProgress;
            int fadeStart = Math.max(0, 10);
            if (smoothGroundAge <= (float)fadeStart) {
                return 1.0f;
            }
            float remaining = (40.0f - smoothGroundAge) / 30.0f;
            return MathHelper.clamp((float)remaining, (float)0.0f, (float)1.0f);
        }
    }

    private record GroundHit(boolean hit, double y) {
        private static final GroundHit MISS = new GroundHit(false, 0.0);
    }
}

