/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.class_11368
 *  net.minecraft.class_11372
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1308
 *  net.minecraft.class_1314
 *  net.minecraft.class_1324
 *  net.minecraft.class_1937
 *  net.minecraft.class_238
 *  net.minecraft.class_2394
 *  net.minecraft.class_2398
 *  net.minecraft.class_243
 *  net.minecraft.class_2940
 *  net.minecraft.class_2941
 *  net.minecraft.class_2943
 *  net.minecraft.class_2945
 *  net.minecraft.class_2945$class_9222
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 *  net.minecraft.class_3532
 *  net.minecraft.class_3730
 *  net.minecraft.class_4048
 *  net.minecraft.class_4050
 *  net.minecraft.class_5132$class_5133
 *  net.minecraft.class_5134
 *  net.minecraft.class_5425
 *  net.minecraft.class_7923
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoEntity
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package com.nonid.entity;

import com.mojang.serialization.Codec;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import java.util.List;
import java.util.UUID;
import net.minecraft.class_11368;
import net.minecraft.class_11372;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1308;
import net.minecraft.class_1314;
import net.minecraft.class_1324;
import net.minecraft.class_1937;
import net.minecraft.class_238;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_243;
import net.minecraft.class_2940;
import net.minecraft.class_2941;
import net.minecraft.class_2943;
import net.minecraft.class_2945;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3532;
import net.minecraft.class_3730;
import net.minecraft.class_4048;
import net.minecraft.class_4050;
import net.minecraft.class_5132;
import net.minecraft.class_5134;
import net.minecraft.class_5425;
import net.minecraft.class_7923;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PregnancyEggEntity
extends class_1314
implements GeoEntity {
    private static final class_2940<String> TARGET_ENTITY_TYPE_ID = class_2945.method_12791(PregnancyEggEntity.class, (class_2941)class_2943.field_13326);
    private static final class_2940<Long> HATCH_START_GLOBAL_TICK = class_2945.method_12791(PregnancyEggEntity.class, (class_2941)class_2943.field_39965);
    private static final class_2940<Long> HATCH_AT_GLOBAL_TICK = class_2945.method_12791(PregnancyEggEntity.class, (class_2941)class_2943.field_39965);
    private static final class_2940<Float> EGG_START_SIZE = class_2945.method_12791(PregnancyEggEntity.class, (class_2941)class_2943.field_13320);
    private static final class_2940<Float> EGG_END_SIZE = class_2945.method_12791(PregnancyEggEntity.class, (class_2941)class_2943.field_13320);
    private static final class_2940<String> EGG_TEXTURE_ID = class_2945.method_12791(PregnancyEggEntity.class, (class_2941)class_2943.field_13326);
    private static final class_2940<Float> EGG_HEALTH = class_2945.method_12791(PregnancyEggEntity.class, (class_2941)class_2943.field_13320);
    private static final String NBT_TARGET_ENTITY_TYPE = "NeedsOfNatureEggTargetType";
    private static final String NBT_HATCH_GLOBAL_TICK = "NeedsOfNatureEggHatchGlobalTick";
    private static final String NBT_HATCH_START_GLOBAL_TICK = "NeedsOfNatureEggHatchStartGlobalTick";
    private static final String NBT_OWNER_UUID = "NeedsOfNatureEggOwnerUuid";
    private static final String NBT_VARIANT_DATA = "NeedsOfNatureEggVariantData";
    private static final String NBT_EGG_START_SIZE = "NeedsOfNatureEggStartSize";
    private static final String NBT_EGG_END_SIZE = "NeedsOfNatureEggEndSize";
    private static final String NBT_EGG_TEXTURE_ID = "NeedsOfNatureEggTexture";
    private static final String NBT_EGG_HEALTH = "NeedsOfNatureEggHealth";
    private static final float BASE_DIMENSION = 0.3f;
    private AnimatableInstanceCache animatableCache;
    private float lastDimensionsScale = -1.0f;
    @Nullable
    private UUID ownerUuid;
    @Nullable
    private NeedsOfNature.PregnancyVariantData variantData;

    public PregnancyEggEntity(class_1299<? extends class_1314> entityType, class_1937 world) {
        super(entityType, world);
    }

    public static class_5132.class_5133 createAttributes() {
        return class_1308.method_26828().method_26868(class_5134.field_23716, 2.0).method_26868(class_5134.field_23719, 0.0);
    }

    protected void method_5959() {
    }

    protected void method_5693(class_2945.class_9222 builder) {
        super.method_5693(builder);
        builder.method_56912(TARGET_ENTITY_TYPE_ID, (Object)"");
        builder.method_56912(HATCH_START_GLOBAL_TICK, (Object)0L);
        builder.method_56912(HATCH_AT_GLOBAL_TICK, (Object)0L);
        builder.method_56912(EGG_START_SIZE, (Object)Float.valueOf(0.5f));
        builder.method_56912(EGG_END_SIZE, (Object)Float.valueOf(1.0f));
        builder.method_56912(EGG_TEXTURE_ID, (Object)"");
        builder.method_56912(EGG_HEALTH, (Object)Float.valueOf(2.0f));
    }

    public void configure(@Nullable UUID ownerPlayerUuid, @Nullable class_2960 targetEntityTypeId, long hatchAtTick) {
        this.configure(ownerPlayerUuid, targetEntityTypeId, hatchAtTick, null);
    }

    public void configure(@Nullable UUID ownerPlayerUuid, @Nullable class_2960 targetEntityTypeId, long hatchAtTick, @Nullable NeedsOfNature.PregnancyVariantData variantData) {
        this.configure(ownerPlayerUuid, targetEntityTypeId, hatchAtTick, variantData, null);
    }

    public void configure(@Nullable UUID ownerPlayerUuid, @Nullable class_2960 targetEntityTypeId, long hatchAtTick, @Nullable NeedsOfNature.PregnancyVariantData variantData, @Nullable NonConfig.EggProfile eggProfile) {
        this.ownerUuid = ownerPlayerUuid;
        this.variantData = NeedsOfNature.normalizePregnancyVariantData(variantData);
        this.setTargetEntityTypeId(targetEntityTypeId);
        this.applyEggProfile(eggProfile);
        long startTick = PregnancyEggEntity.resolveCurrentTick(this.method_73183());
        long endTick = Math.max(0L, hatchAtTick);
        if (endTick <= startTick) {
            endTick = startTick + 1L;
        }
        this.setHatchStartGlobalTick(startTick);
        this.setHatchAtGlobalTick(endTick);
        this.refreshEggDimensionsIfNeeded(true);
    }

    @Nullable
    public class_2960 getTargetEntityTypeId() {
        String raw = (String)this.field_6011.method_12789(TARGET_ENTITY_TYPE_ID);
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return class_2960.method_12829((String)raw);
    }

    public void setTargetEntityTypeId(@Nullable class_2960 entityTypeId) {
        this.field_6011.method_12778(TARGET_ENTITY_TYPE_ID, (Object)(entityTypeId == null ? "" : entityTypeId.toString()));
    }

    public long getHatchStartGlobalTick() {
        return Math.max(0L, (Long)this.field_6011.method_12789(HATCH_START_GLOBAL_TICK));
    }

    public void setHatchStartGlobalTick(long tick) {
        this.field_6011.method_12778(HATCH_START_GLOBAL_TICK, (Object)Math.max(0L, tick));
    }

    public long getHatchAtGlobalTick() {
        return Math.max(0L, (Long)this.field_6011.method_12789(HATCH_AT_GLOBAL_TICK));
    }

    public void setHatchAtGlobalTick(long tick) {
        this.field_6011.method_12778(HATCH_AT_GLOBAL_TICK, (Object)Math.max(0L, tick));
    }

    public float getEggStartSize() {
        return PregnancyEggEntity.clampEggSize(((Float)this.field_6011.method_12789(EGG_START_SIZE)).floatValue());
    }

    public void setEggStartSize(float size) {
        this.field_6011.method_12778(EGG_START_SIZE, (Object)Float.valueOf(PregnancyEggEntity.clampEggSize(size)));
        this.refreshEggDimensionsIfNeeded(true);
    }

    public float getEggEndSize() {
        return PregnancyEggEntity.clampEggSize(((Float)this.field_6011.method_12789(EGG_END_SIZE)).floatValue());
    }

    public void setEggEndSize(float size) {
        this.field_6011.method_12778(EGG_END_SIZE, (Object)Float.valueOf(PregnancyEggEntity.clampEggSize(size)));
        this.refreshEggDimensionsIfNeeded(true);
    }

    @Nullable
    public class_2960 getEggTextureId() {
        String raw = (String)this.field_6011.method_12789(EGG_TEXTURE_ID);
        if (raw == null || raw.isBlank()) {
            return null;
        }
        class_2960 parsed = class_2960.method_12829((String)raw);
        return parsed == null || parsed.method_12832().isBlank() ? null : parsed;
    }

    public void setEggTextureId(@Nullable class_2960 textureId) {
        this.field_6011.method_12778(EGG_TEXTURE_ID, (Object)(textureId == null || textureId.method_12832().isBlank() ? "" : textureId.toString()));
    }

    public float getEggHealth() {
        return PregnancyEggEntity.clampEggHealth(((Float)this.field_6011.method_12789(EGG_HEALTH)).floatValue());
    }

    public void setEggHealth(float health) {
        this.setEggHealth(health, false);
    }

    private void setEggHealth(float health, boolean healToMax) {
        float clamped = PregnancyEggEntity.clampEggHealth(health);
        this.field_6011.method_12778(EGG_HEALTH, (Object)Float.valueOf(clamped));
        this.applyEggHealth(clamped, healToMax);
    }

    public void method_6007() {
        super.method_6007();
        this.freezeStationaryState();
        this.refreshEggDimensionsIfNeeded(false);
    }

    public void method_5773() {
        super.method_5773();
        this.freezeStationaryState();
        this.refreshEggDimensionsIfNeeded(false);
        this.pushAwayFromNearbyEggs();
        class_1937 class_19372 = this.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            return;
        }
        class_3218 serverWorld = (class_3218)class_19372;
        class_2960 targetTypeId = this.getTargetEntityTypeId();
        if (targetTypeId == null) {
            this.method_31472();
            return;
        }
        if (PregnancyEggEntity.resolveGlobalTick(serverWorld) < this.getHatchAtGlobalTick()) {
            return;
        }
        this.hatch(serverWorld, targetTypeId);
    }

    public boolean method_5810() {
        return true;
    }

    public void method_5697(class_1297 entity) {
        if (entity instanceof PregnancyEggEntity) {
            super.method_5697(entity);
        }
    }

    public void method_5762(double deltaX, double deltaY, double deltaZ) {
        super.method_5762(deltaX, deltaY, deltaZ);
    }

    public void method_6005(double strength, double x, double z) {
    }

    public boolean method_5675() {
        return false;
    }

    public boolean method_5974(double distanceSquared) {
        return false;
    }

    public void method_5647(class_11372 storage) {
        String variantJson;
        super.method_5647(storage);
        class_2960 target = this.getTargetEntityTypeId();
        if (target != null) {
            storage.method_71469(NBT_TARGET_ENTITY_TYPE, target.toString());
        }
        storage.method_71468(NBT_HATCH_START_GLOBAL_TICK, (Codec)Codec.LONG, (Object)this.getHatchStartGlobalTick());
        storage.method_71468(NBT_HATCH_GLOBAL_TICK, (Codec)Codec.LONG, (Object)this.getHatchAtGlobalTick());
        if (this.ownerUuid != null) {
            storage.method_71469(NBT_OWNER_UUID, this.ownerUuid.toString());
        }
        if ((variantJson = NeedsOfNature.encodePregnancyVariantData(this.variantData)) != null) {
            storage.method_71469(NBT_VARIANT_DATA, variantJson);
        }
        storage.method_71464(NBT_EGG_START_SIZE, this.getEggStartSize());
        storage.method_71464(NBT_EGG_END_SIZE, this.getEggEndSize());
        class_2960 textureId = this.getEggTextureId();
        if (textureId != null) {
            storage.method_71469(NBT_EGG_TEXTURE_ID, textureId.toString());
        }
        storage.method_71464(NBT_EGG_HEALTH, this.getEggHealth());
    }

    public void method_5651(class_11368 storage) {
        super.method_5651(storage);
        String targetRaw = storage.method_71441(NBT_TARGET_ENTITY_TYPE).orElse("");
        this.setTargetEntityTypeId(class_2960.method_12829((String)targetRaw));
        long start = Math.max(0L, storage.method_71426(NBT_HATCH_START_GLOBAL_TICK, (Codec)Codec.LONG).orElse(0L));
        long end = Math.max(0L, storage.method_71426(NBT_HATCH_GLOBAL_TICK, (Codec)Codec.LONG).orElse(0L));
        if (end > 0L && start >= end) {
            start = Math.max(0L, end - 1L);
        }
        this.setHatchStartGlobalTick(start);
        this.setHatchAtGlobalTick(end);
        String ownerRaw = storage.method_71441(NBT_OWNER_UUID).orElse("");
        this.ownerUuid = PregnancyEggEntity.parseUuid(ownerRaw);
        this.variantData = NeedsOfNature.decodePregnancyVariantData(storage.method_71441(NBT_VARIANT_DATA).orElse(""));
        this.setEggStartSize(storage.method_71426(NBT_EGG_START_SIZE, (Codec)Codec.FLOAT).orElse(Float.valueOf(0.5f)).floatValue());
        this.setEggEndSize(storage.method_71426(NBT_EGG_END_SIZE, (Codec)Codec.FLOAT).orElse(Float.valueOf(1.0f)).floatValue());
        this.setEggTextureId(class_2960.method_12829((String)storage.method_71441(NBT_EGG_TEXTURE_ID).orElse("")));
        this.setEggHealth(storage.method_71426(NBT_EGG_HEALTH, (Codec)Codec.FLOAT).orElse(Float.valueOf(2.0f)).floatValue());
        this.refreshEggDimensionsIfNeeded(true);
    }

    @Nullable
    private static UUID parseUuid(@Nullable String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(raw);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void hatch(class_3218 world, class_2960 targetTypeId) {
        if (!class_7923.field_41177.method_10250(targetTypeId)) {
            this.method_31472();
            return;
        }
        class_1299 entityType = (class_1299)class_7923.field_41177.method_63535(targetTypeId);
        class_1297 entity = entityType.method_5883((class_1937)world, class_3730.field_16466);
        if (entity == null) {
            this.method_31472();
            return;
        }
        entity.method_5808(this.method_23317(), this.method_23318(), this.method_23321(), this.method_36454(), this.method_36455());
        if (entity instanceof class_1308) {
            class_1308 mob = (class_1308)entity;
            mob.method_5943((class_5425)world, world.method_8404(mob.method_24515()), class_3730.field_16466, null);
            NeedsOfNature.applyPregnancyVariantDataForEgg(world, (class_1297)mob, this.variantData);
            NeedsOfNature.applyPregnancyMobFlagsForEgg(mob, this.resolveOwnerPlayer(world));
        }
        if (world.method_8649(entity)) {
            this.playHatchEffects(world);
        }
        this.method_31472();
    }

    public float getGrowthScale(float partialTick) {
        long startTick = this.getHatchStartGlobalTick();
        long endTick = this.getHatchAtGlobalTick();
        float startSize = this.getEggStartSize();
        float endSize = this.getEggEndSize();
        if (endTick <= startTick) {
            return endSize;
        }
        double now = (float)PregnancyEggEntity.resolveCurrentTick(this.method_73183()) + Math.max(0.0f, partialTick);
        double progress = (now - (double)startTick) / (double)(endTick - startTick);
        progress = class_3532.method_15350((double)progress, (double)0.0, (double)1.0);
        return (float)((double)startSize + (double)(endSize - startSize) * progress);
    }

    protected class_4048 method_55694(class_4050 pose) {
        float scale = this.getGrowthScale(0.0f);
        return class_4048.method_18385((float)(0.3f * scale), (float)(0.3f * scale));
    }

    public void method_5674(class_2940<?> data) {
        super.method_5674(data);
        if (EGG_START_SIZE.equals(data) || EGG_END_SIZE.equals(data)) {
            this.refreshEggDimensionsIfNeeded(true);
        }
        if (EGG_HEALTH.equals(data)) {
            this.applyEggHealth(this.getEggHealth(), false);
        }
    }

    private void applyEggProfile(@Nullable NonConfig.EggProfile eggProfile) {
        NonConfig.EggProfile safe = eggProfile == null ? NonConfig.EggProfile.defaults() : eggProfile.resolvedOver(NonConfig.EggProfile.defaults());
        Float startSize = safe.startSize();
        Float endSize = safe.endSize();
        Float health = safe.health();
        this.setEggStartSize(startSize == null ? 0.5f : startSize.floatValue());
        this.setEggEndSize(endSize == null ? 1.0f : endSize.floatValue());
        String texture = safe.texture();
        this.setEggTextureId(texture == null ? null : class_2960.method_12829((String)texture));
        this.setEggHealth(health == null ? 2.0f : health.floatValue(), true);
    }

    private void applyEggHealth(float health, boolean healToMax) {
        class_1324 attribute = this.method_5996(class_5134.field_23716);
        if (attribute != null && Math.abs(attribute.method_6201() - (double)health) > 0.001) {
            attribute.method_6192((double)health);
        }
        if (healToMax || this.method_6032() > health || this.method_6032() <= 0.0f) {
            this.method_6033(health);
        }
    }

    private void refreshEggDimensionsIfNeeded(boolean force) {
        float scale = this.getGrowthScale(0.0f);
        if (!force && Math.abs(scale - this.lastDimensionsScale) < 0.01f) {
            return;
        }
        this.lastDimensionsScale = scale;
        this.method_18382();
    }

    private static float clampEggSize(float value) {
        if (!Float.isFinite(value)) {
            return 1.0f;
        }
        return class_3532.method_15363((float)value, (float)0.05f, (float)4.0f);
    }

    private static float clampEggHealth(float value) {
        if (!Float.isFinite(value)) {
            return 2.0f;
        }
        return class_3532.method_15363((float)value, (float)0.5f, (float)100.0f);
    }

    private void playHatchEffects(class_3218 world) {
        if (world == null) {
            return;
        }
        world.method_43128(null, this.method_23317(), this.method_23318(), this.method_23321(), class_3417.field_14902, class_3419.field_15254, 0.9f, 0.95f + world.method_8409().method_43057() * 0.1f);
        world.method_65096((class_2394)class_2398.field_11203, this.method_23317(), this.method_23318() + 0.25, this.method_23321(), 12, 0.25, 0.2, 0.25, 0.02);
        world.method_65096((class_2394)class_2398.field_11204, this.method_23317(), this.method_23318() + 0.2, this.method_23321(), 8, 0.18, 0.12, 0.18, 0.01);
    }

    @Nullable
    private class_3222 resolveOwnerPlayer(class_3218 world) {
        if (this.ownerUuid == null || world.method_8503() == null) {
            return null;
        }
        return world.method_8503().method_3760().method_14602(this.ownerUuid);
    }

    private static long resolveGlobalTick(class_3218 world) {
        if (world == null || world.method_8503() == null) {
            return 0L;
        }
        class_3218 overworld = world.method_8503().method_30002();
        return overworld == null ? world.method_75260() : overworld.method_75260();
    }

    private static long resolveCurrentTick(@Nullable class_1937 world) {
        if (world == null) {
            return 0L;
        }
        if (world instanceof class_3218) {
            class_3218 serverWorld = (class_3218)world;
            return PregnancyEggEntity.resolveGlobalTick(serverWorld);
        }
        return world.method_75260();
    }

    private void freezeStationaryState() {
        this.method_5803(true);
        this.method_5875(true);
        this.method_5977(true);
        this.method_5942().method_6340();
        class_243 velocity = this.method_18798();
        double x = class_3532.method_15350((double)(velocity.field_1352 * 0.65), (double)-0.08, (double)0.08);
        double z = class_3532.method_15350((double)(velocity.field_1350 * 0.65), (double)-0.08, (double)0.08);
        if (Math.abs(x) < 0.001) {
            x = 0.0;
        }
        if (Math.abs(z) < 0.001) {
            z = 0.0;
        }
        this.method_18800(x, 0.0, z);
        this.field_6017 = 0.0;
    }

    private void pushAwayFromNearbyEggs() {
        if (this.method_31481()) {
            return;
        }
        class_238 searchBox = this.method_5829().method_1009(0.05, 0.0, 0.05);
        List nearbyEggs = this.method_73183().method_8390(PregnancyEggEntity.class, searchBox, egg -> egg != this && !egg.method_31481());
        if (nearbyEggs.isEmpty()) {
            return;
        }
        double pushX = 0.0;
        double pushZ = 0.0;
        double ownRadius = Math.max(0.1, this.method_5829().method_17939() * 0.5);
        for (PregnancyEggEntity other : nearbyEggs) {
            double distance;
            double dz;
            double otherRadius = Math.max(0.1, other.method_5829().method_17939() * 0.5);
            double minDistance = ownRadius + otherRadius + 0.04;
            double dx = this.method_23317() - other.method_23317();
            double distanceSq = dx * dx + (dz = this.method_23321() - other.method_23321()) * dz;
            if (distanceSq >= minDistance * minDistance) continue;
            if (distanceSq < 1.0E-6) {
                double angle = this.deterministicSeparationAngle();
                dx = Math.cos(angle);
                dz = Math.sin(angle);
                distanceSq = 1.0;
            }
            if ((distance = Math.sqrt(distanceSq)) <= 0.0) continue;
            double strength = Math.min(0.08, (minDistance - distance) * 0.15);
            pushX += dx / distance * strength;
            pushZ += dz / distance * strength;
        }
        if (Math.abs(pushX) < 1.0E-5 && Math.abs(pushZ) < 1.0E-5) {
            return;
        }
        pushX = class_3532.method_15350((double)pushX, (double)-0.08, (double)0.08);
        pushZ = class_3532.method_15350((double)pushZ, (double)-0.08, (double)0.08);
        this.method_30634(this.method_23317() + pushX, this.method_23318(), this.method_23321() + pushZ);
        this.method_18800(pushX * 0.25, 0.0, pushZ * 0.25);
        this.applySettledPushRotation(pushX, pushZ);
    }

    private double deterministicSeparationAngle() {
        long bits = this.method_5667().getMostSignificantBits() ^ this.method_5667().getLeastSignificantBits();
        double unit = (double)(bits & 0xFFFFL) / 65536.0;
        return unit * Math.PI * 2.0;
    }

    private void applySettledPushRotation(double pushX, double pushZ) {
        if (Math.abs(pushX) < 1.0E-5 && Math.abs(pushZ) < 1.0E-5) {
            return;
        }
        float yaw = (float)(Math.atan2(pushZ, pushX) * 57.29577951308232) - 90.0f;
        this.method_36456(yaw);
        this.method_5847(yaw);
        this.method_5636(yaw);
        this.field_5982 = yaw;
    }

    public void registerControllers(// Could not load outer class - annotation placement on inner may be incorrect
     @NotNull AnimatableManager.ControllerRegistrar controllers) {
    }

    @NotNull
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        if (this.animatableCache == null) {
            this.animatableCache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
        }
        return this.animatableCache;
    }
}

