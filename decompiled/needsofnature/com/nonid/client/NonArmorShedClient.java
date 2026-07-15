/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.client.runtime.AfwClientAnimationRuntime
 *  net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
 *  net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents
 *  net.minecraft.class_10428
 *  net.minecraft.class_11659
 *  net.minecraft.class_1297
 *  net.minecraft.class_1304
 *  net.minecraft.class_1309
 *  net.minecraft.class_1542
 *  net.minecraft.class_1799
 *  net.minecraft.class_1922
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_243
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_5819
 *  net.minecraft.class_742
 *  net.minecraft.class_7833
 *  net.minecraft.class_916
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
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.class_10428;
import net.minecraft.class_11659;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_1922;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_5819;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import net.minecraft.class_916;
import org.joml.Quaternionfc;

public final class NonArmorShedClient {
    private static final class_1304[] ARMOR_SLOTS = new class_1304[]{class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166};
    private static final int FLYING_TICKS = 24;
    private static final int GROUND_TICKS = 40;
    private static final int GROUND_FADE_TICKS = 30;
    private static final double GRAVITY = 0.035;
    private static final double AIR_DRAG = 0.96;
    private static final double GROUND_DRAG = 0.78;
    private static final float ITEM_SCALE = 1.0f;
    private static final long FRESH_START_MAX_AGE_TICKS = 5L;
    private static final long QUEUED_HANDOFF_SKIP_TICKS = 12L;
    private static final class_2960 PLAYER_DEFEATED = class_2960.method_60655((String)"animationframework", (String)"player_defeated-belly");
    private static final class_2960 PLAYER_BIRTH = class_2960.method_60655((String)"animationframework", (String)"player_birth");
    private static final List<ArmorProp> PROPS = new ArrayList<ArmorProp>();
    private static final Map<UUID, Long> LAST_SPAWNED_START_TICK = new HashMap<UUID, Long>();
    private static final Map<UUID, Long> LAST_ACTIVE_TICK = new HashMap<UUID, Long>();

    private NonArmorShedClient() {
    }

    public static void register() {
        WorldRenderEvents.AFTER_ENTITIES.register(NonArmorShedClient::render);
    }

    public static void clientTick(class_310 client) {
        if (!NeedsOfNature.getConfig().isArmorShedEffectEnabled()) {
            NonArmorShedClient.clear();
            return;
        }
        NonArmorShedClient.tickProps(client);
        if (client == null || client.field_1687 == null) {
            NonArmorShedClient.clear();
            return;
        }
        long now = client.field_1687.method_75260();
        for (class_742 player : client.field_1687.method_18456()) {
            if (player == null || player.method_31481()) continue;
            UUID uuid = player.method_5667();
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
        class_2960 activeAnimation = AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining((UUID)playerUuid);
        return !NonArmorShedClient.isQueuedFollowUpAnimation(activeAnimation);
    }

    private static boolean isQueuedFollowUpAnimation(class_2960 animationId) {
        if (animationId == null) {
            return false;
        }
        return PLAYER_DEFEATED.equals((Object)animationId) || PLAYER_BIRTH.equals((Object)animationId);
    }

    private static void spawnForPlayer(class_742 player, long startTick) {
        class_243 origin = new class_243(player.method_23317(), player.method_23318() + (double)player.method_17682() * 0.72, player.method_23321());
        long seedBase = player.method_5667().getMostSignificantBits() ^ player.method_5667().getLeastSignificantBits() ^ startTick;
        Random random = new Random(seedBase);
        float yawRad = player.method_36454() * ((float)Math.PI / 180);
        class_243 forward = new class_243((double)(-class_3532.method_15374((double)yawRad)), 0.0, (double)class_3532.method_15362((double)yawRad));
        class_243 right = new class_243(forward.field_1350, 0.0, -forward.field_1352);
        for (int i = 0; i < ARMOR_SLOTS.length; ++i) {
            class_1304 slot = ARMOR_SLOTS[i];
            class_1799 stack = player.method_6118(slot);
            if (stack == null || stack.method_7960()) continue;
            double side = ((double)i - 1.5) * 0.18;
            class_243 spawnPos = origin.method_1019(right.method_1021(side)).method_1031(0.0, -0.08 * (double)i, 0.0);
            NonArmorShedClient.addProp(stack, spawnPos, forward, right, random, seedBase, i, ARMOR_SLOTS.length);
        }
        NonArmorShedClient.spawnShedAccessories(player, origin, forward, right, random, seedBase);
    }

    private static void spawnShedAccessories(class_742 player, class_243 origin, class_243 forward, class_243 right, Random random, long seedBase) {
        NonAccessoryShedSystem.ShedState shedState = NonAccessoryShedSystem.getShedState(player.method_5667());
        if (shedState.isEmpty()) {
            return;
        }
        List<class_1799> stacks = NonTrinketsIntegration.getVisuallyShedAccessoryStacks((class_1309)player);
        for (int index = 0; index < stacks.size(); ++index) {
            class_1799 stack = stacks.get(index);
            if (stack == null || stack.method_7960()) continue;
            double side = ((double)index - 0.5) * 0.24;
            class_243 spawnPos = origin.method_1019(right.method_1021(side)).method_1031(0.0, -0.34, 0.0);
            NonArmorShedClient.addProp(stack, spawnPos, forward, right, random, seedBase, 20 + index, 2);
        }
    }

    private static void addProp(class_1799 stack, class_243 spawnPos, class_243 forward, class_243 right, Random random, long seedBase, int index, int count) {
        class_1799 visualStack = stack.method_7972();
        visualStack.method_7939(1);
        double angle = Math.PI * 2 * (double)index / (double)Math.max(1, count) + (random.nextDouble() - 0.5) * 0.55;
        class_243 outward = right.method_1021(Math.cos(angle)).method_1019(forward.method_1021(Math.sin(angle))).method_1029();
        double speed = 0.105 + random.nextDouble() * 0.035;
        class_243 velocity = outward.method_1021(speed).method_1031(0.0, 0.15 + random.nextDouble() * 0.13, 0.0);
        PROPS.add(new ArmorProp(visualStack, spawnPos, velocity, random.nextFloat() * 360.0f, random.nextFloat() * 360.0f, 7.0f + random.nextFloat() * 7.0f, 10.0f + random.nextFloat() * 12.0f, (int)(seedBase + (long)index * 31L)));
    }

    private static void tickProps(class_310 client) {
        if (PROPS.isEmpty()) {
            return;
        }
        if (client == null || client.field_1687 == null) {
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
                prop.velocity = prop.velocity.method_18805(0.78, 0.0, 0.78);
                if (prop.groundAge <= 40) continue;
                iterator.remove();
                continue;
            }
            prop.velocity = prop.velocity.method_1031(0.0, -0.035, 0.0).method_18805(0.96, 0.98, 0.96);
            class_243 next = prop.position.method_1019(prop.velocity);
            GroundHit hit = NonArmorShedClient.findGround(client, next);
            if (hit.hit() && prop.velocity.field_1351 <= 0.0 && prop.age > 3) {
                prop.position = new class_243(next.field_1352, hit.y(), next.field_1350);
                prop.velocity = class_243.field_1353;
                prop.landed = true;
                continue;
            }
            prop.position = next;
            if (prop.age <= 64) continue;
            iterator.remove();
        }
    }

    private static GroundHit findGround(class_310 client, class_243 pos) {
        if (client == null || client.field_1687 == null) {
            return GroundHit.MISS;
        }
        class_2338 blockPos = class_2338.method_49637((double)pos.field_1352, (double)(pos.field_1351 - 0.04), (double)pos.field_1350);
        class_2680 state = client.field_1687.method_8320(blockPos);
        class_265 shape = state.method_26220((class_1922)client.field_1687, blockPos);
        if (shape.method_1110()) {
            return GroundHit.MISS;
        }
        double top = (double)blockPos.method_10264() + shape.method_1105(class_2350.class_2351.field_11052);
        if (pos.field_1351 < top + 0.12) {
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
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1687 == null) {
            return;
        }
        class_243 cameraPos = context.worldState().field_63082.field_63078;
        if (cameraPos == null) {
            return;
        }
        float tickProgress = client.method_61966().method_60637(false);
        class_4587 matrices = context.matrices();
        for (ArmorProp prop : PROPS) {
            class_243 renderPos = prop.previousPosition.method_35590(prop.position, (double)tickProgress);
            matrices.method_22903();
            matrices.method_22904(renderPos.field_1352 - cameraPos.field_1352, renderPos.field_1351 - cameraPos.field_1351, renderPos.field_1350 - cameraPos.field_1350);
            float smoothAge = (float)prop.age + tickProgress;
            float yaw = prop.landed ? prop.groundYaw : prop.baseYaw + smoothAge * prop.yawSpeed;
            float pitch = prop.landed ? 90.0f : prop.basePitch + smoothAge * prop.pitchSpeed;
            matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(yaw));
            matrices.method_22907((Quaternionfc)class_7833.field_40714.rotationDegrees(pitch));
            float fadeScale = prop.landed ? prop.groundFadeScale(tickProgress) : 1.0f;
            matrices.method_22905(1.0f * fadeScale, 1.0f * fadeScale, 1.0f * fadeScale);
            class_10428 state = new class_10428();
            class_1542 fake = new class_1542((class_1937)client.field_1687, prop.position.field_1352, prop.position.field_1351, prop.position.field_1350, prop.stack);
            state.method_65581((class_1297)fake, prop.stack, client.method_65386());
            class_916.method_56858((class_4587)matrices, (class_11659)context.commandQueue(), (int)0xF000F0, (class_10428)state, (class_5819)class_5819.method_43049((long)prop.seed));
            matrices.method_22909();
        }
    }

    private static final class ArmorProp {
        final class_1799 stack;
        class_243 position;
        class_243 previousPosition;
        class_243 velocity;
        final float baseYaw;
        final float basePitch;
        final float yawSpeed;
        final float pitchSpeed;
        final float groundYaw;
        final int seed;
        int age;
        int groundAge;
        boolean landed;

        ArmorProp(class_1799 stack, class_243 position, class_243 velocity, float baseYaw, float basePitch, float yawSpeed, float pitchSpeed, int seed) {
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
            return class_3532.method_15363((float)remaining, (float)0.0f, (float)1.0f);
        }
    }

    private record GroundHit(boolean hit, double y) {
        private static final GroundHit MISS = new GroundHit(false, 0.0);
    }
}

