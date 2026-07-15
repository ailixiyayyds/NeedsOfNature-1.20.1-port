/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.storage.ReadView
 *  net.minecraft.storage.WriteView
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.mob.PathAwareEntity
 *  net.minecraft.entity.attribute.EntityAttributeInstance
 *  net.minecraft.world.World
 *  net.minecraft.util.math.Box
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.particle.ParticleTypes
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.entity.data.TrackedData
 *  net.minecraft.entity.data.TrackedDataHandler
 *  net.minecraft.entity.data.TrackedDataHandlerRegistry
 *  net.minecraft.entity.data.DataTracker
 *  net.minecraft.entity.data.DataTracker$Builder
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.entity.SpawnReason
 *  net.minecraft.entity.EntityDimensions
 *  net.minecraft.entity.EntityPose
 *  net.minecraft.entity.attribute.DefaultAttributeContainer$Builder
 *  net.minecraft.entity.attribute.EntityAttributes
 *  net.minecraft.world.ServerWorldAccess
 *  net.minecraft.registry.Registries
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoEntity
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package com.nonid.entity;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PregnancyEggEntity
extends PathAwareEntity
implements GeoEntity {
    private static final TrackedData<String> TARGET_ENTITY_TYPE_ID = DataTracker.registerData(PregnancyEggEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Long> HATCH_START_GLOBAL_TICK = DataTracker.registerData(PregnancyEggEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.LONG);
    private static final TrackedData<Long> HATCH_AT_GLOBAL_TICK = DataTracker.registerData(PregnancyEggEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.LONG);
    private static final TrackedData<Float> EGG_START_SIZE = DataTracker.registerData(PregnancyEggEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> EGG_END_SIZE = DataTracker.registerData(PregnancyEggEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<String> EGG_TEXTURE_ID = DataTracker.registerData(PregnancyEggEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Float> EGG_HEALTH = DataTracker.registerData(PregnancyEggEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.FLOAT);
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
    private final AnimatableInstanceCache animatableCache = GeckoLibUtil.createInstanceCache(this);
    private float lastDimensionsScale = -1.0f;
    @Nullable
    private UUID ownerUuid;
    @Nullable
    private NeedsOfNature.PregnancyVariantData variantData;

    public PregnancyEggEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0);
    }

    protected void initGoals() {
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TARGET_ENTITY_TYPE_ID, "");
        this.dataTracker.startTracking(HATCH_START_GLOBAL_TICK, 0L);
        this.dataTracker.startTracking(HATCH_AT_GLOBAL_TICK, 0L);
        this.dataTracker.startTracking(EGG_START_SIZE, 0.5f);
        this.dataTracker.startTracking(EGG_END_SIZE, 1.0f);
        this.dataTracker.startTracking(EGG_TEXTURE_ID, "");
        this.dataTracker.startTracking(EGG_HEALTH, 2.0f);
    }

    public void configure(@Nullable UUID ownerPlayerUuid, @Nullable Identifier targetEntityTypeId, long hatchAtTick) {
        this.configure(ownerPlayerUuid, targetEntityTypeId, hatchAtTick, null);
    }

    public void configure(@Nullable UUID ownerPlayerUuid, @Nullable Identifier targetEntityTypeId, long hatchAtTick, @Nullable NeedsOfNature.PregnancyVariantData variantData) {
        this.configure(ownerPlayerUuid, targetEntityTypeId, hatchAtTick, variantData, null);
    }

    public void configure(@Nullable UUID ownerPlayerUuid, @Nullable Identifier targetEntityTypeId, long hatchAtTick, @Nullable NeedsOfNature.PregnancyVariantData variantData, @Nullable NonConfig.EggProfile eggProfile) {
        this.ownerUuid = ownerPlayerUuid;
        this.variantData = NeedsOfNature.normalizePregnancyVariantData(variantData);
        this.setTargetEntityTypeId(targetEntityTypeId);
        this.applyEggProfile(eggProfile);
        long startTick = PregnancyEggEntity.resolveCurrentTick(this.getWorld());
        long endTick = Math.max(0L, hatchAtTick);
        if (endTick <= startTick) {
            endTick = startTick + 1L;
        }
        this.setHatchStartGlobalTick(startTick);
        this.setHatchAtGlobalTick(endTick);
        this.refreshEggDimensionsIfNeeded(true);
    }

    @Nullable
    public Identifier getTargetEntityTypeId() {
        String raw = (String)this.dataTracker.get(TARGET_ENTITY_TYPE_ID);
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return Identifier.tryParse((String)raw);
    }

    public void setTargetEntityTypeId(@Nullable Identifier entityTypeId) {
        this.dataTracker.set(TARGET_ENTITY_TYPE_ID, entityTypeId == null ? "" : entityTypeId.toString());
    }

    public long getHatchStartGlobalTick() {
        return Math.max(0L, (Long)this.dataTracker.get(HATCH_START_GLOBAL_TICK));
    }

    public void setHatchStartGlobalTick(long tick) {
        this.dataTracker.set(HATCH_START_GLOBAL_TICK, Math.max(0L, tick));
    }

    public long getHatchAtGlobalTick() {
        return Math.max(0L, (Long)this.dataTracker.get(HATCH_AT_GLOBAL_TICK));
    }

    public void setHatchAtGlobalTick(long tick) {
        this.dataTracker.set(HATCH_AT_GLOBAL_TICK, Math.max(0L, tick));
    }

    public float getEggStartSize() {
        return PregnancyEggEntity.clampEggSize(((Float)this.dataTracker.get(EGG_START_SIZE)).floatValue());
    }

    public void setEggStartSize(float size) {
        this.dataTracker.set(EGG_START_SIZE, PregnancyEggEntity.clampEggSize(size));
        this.refreshEggDimensionsIfNeeded(true);
    }

    public float getEggEndSize() {
        return PregnancyEggEntity.clampEggSize(((Float)this.dataTracker.get(EGG_END_SIZE)).floatValue());
    }

    public void setEggEndSize(float size) {
        this.dataTracker.set(EGG_END_SIZE, PregnancyEggEntity.clampEggSize(size));
        this.refreshEggDimensionsIfNeeded(true);
    }

    @Nullable
    public Identifier getEggTextureId() {
        String raw = (String)this.dataTracker.get(EGG_TEXTURE_ID);
        if (raw == null || raw.isBlank()) {
            return null;
        }
        Identifier parsed = Identifier.tryParse((String)raw);
        return parsed == null || parsed.getPath().isBlank() ? null : parsed;
    }

    public void setEggTextureId(@Nullable Identifier textureId) {
        this.dataTracker.set(EGG_TEXTURE_ID, textureId == null || textureId.getPath().isBlank() ? "" : textureId.toString());
    }

    public float getEggHealth() {
        return PregnancyEggEntity.clampEggHealth(((Float)this.dataTracker.get(EGG_HEALTH)).floatValue());
    }

    public void setEggHealth(float health) {
        this.setEggHealth(health, false);
    }

    private void setEggHealth(float health, boolean healToMax) {
        float clamped = PregnancyEggEntity.clampEggHealth(health);
        this.dataTracker.set(EGG_HEALTH, clamped);
        this.applyEggHealth(clamped, healToMax);
    }

    public void tickMovement() {
        super.tickMovement();
        this.freezeStationaryState();
        this.refreshEggDimensionsIfNeeded(false);
    }

    public void tick() {
        super.tick();
        this.freezeStationaryState();
        this.refreshEggDimensionsIfNeeded(false);
        this.pushAwayFromNearbyEggs();
        World class_19372 = this.getWorld();
        if (!(class_19372 instanceof ServerWorld)) {
            return;
        }
        ServerWorld serverWorld = (ServerWorld)class_19372;
        Identifier targetTypeId = this.getTargetEntityTypeId();
        if (targetTypeId == null) {
            this.discard();
            return;
        }
        if (PregnancyEggEntity.resolveGlobalTick(serverWorld) < this.getHatchAtGlobalTick()) {
            return;
        }
        this.hatch(serverWorld, targetTypeId);
    }

    public boolean isPushable() {
        return true;
    }

    public void pushAwayFrom(Entity entity) {
        if (entity instanceof PregnancyEggEntity) {
            super.pushAwayFrom(entity);
        }
    }

    public void addVelocity(double deltaX, double deltaY, double deltaZ) {
        super.addVelocity(deltaX, deltaY, deltaZ);
    }

    public void takeKnockback(double strength, double x, double z) {
    }

    public boolean isPushedByFluids() {
        return false;
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    public void writeCustomDataToNbt(NbtCompound storage) {
        String variantJson;
        super.writeCustomDataToNbt(storage);
        Identifier target = this.getTargetEntityTypeId();
        if (target != null) {
            storage.putString(NBT_TARGET_ENTITY_TYPE, target.toString());
        }
        storage.putLong(NBT_HATCH_START_GLOBAL_TICK, this.getHatchStartGlobalTick());
        storage.putLong(NBT_HATCH_GLOBAL_TICK, this.getHatchAtGlobalTick());
        if (this.ownerUuid != null) {
            storage.putString(NBT_OWNER_UUID, this.ownerUuid.toString());
        }
        if ((variantJson = NeedsOfNature.encodePregnancyVariantData(this.variantData)) != null) {
            storage.putString(NBT_VARIANT_DATA, variantJson);
        }
        storage.putFloat(NBT_EGG_START_SIZE, this.getEggStartSize());
        storage.putFloat(NBT_EGG_END_SIZE, this.getEggEndSize());
        Identifier textureId = this.getEggTextureId();
        if (textureId != null) {
            storage.putString(NBT_EGG_TEXTURE_ID, textureId.toString());
        }
        storage.putFloat(NBT_EGG_HEALTH, this.getEggHealth());
    }

    public void readCustomDataFromNbt(NbtCompound storage) {
        super.readCustomDataFromNbt(storage);
        String targetRaw = storage.getString(NBT_TARGET_ENTITY_TYPE);
        this.setTargetEntityTypeId(Identifier.tryParse((String)targetRaw));
        long start = Math.max(0L, storage.getLong(NBT_HATCH_START_GLOBAL_TICK));
        long end = Math.max(0L, storage.getLong(NBT_HATCH_GLOBAL_TICK));
        if (end > 0L && start >= end) {
            start = Math.max(0L, end - 1L);
        }
        this.setHatchStartGlobalTick(start);
        this.setHatchAtGlobalTick(end);
        String ownerRaw = storage.getString(NBT_OWNER_UUID);
        this.ownerUuid = PregnancyEggEntity.parseUuid(ownerRaw);
        this.variantData = NeedsOfNature.decodePregnancyVariantData(storage.getString(NBT_VARIANT_DATA));
        this.setEggStartSize(storage.contains(NBT_EGG_START_SIZE) ? storage.getFloat(NBT_EGG_START_SIZE) : 0.5f);
        this.setEggEndSize(storage.contains(NBT_EGG_END_SIZE) ? storage.getFloat(NBT_EGG_END_SIZE) : 1.0f);
        this.setEggTextureId(Identifier.tryParse(storage.getString(NBT_EGG_TEXTURE_ID)));
        this.setEggHealth(storage.contains(NBT_EGG_HEALTH) ? storage.getFloat(NBT_EGG_HEALTH) : 2.0f);
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

    private void hatch(ServerWorld world, Identifier targetTypeId) {
        if (!Registries.ENTITY_TYPE.containsId(targetTypeId)) {
            this.discard();
            return;
        }
        EntityType entityType = (EntityType)Registries.ENTITY_TYPE.get(targetTypeId);
        Entity entity = entityType.create(world);
        if (entity == null) {
            this.discard();
            return;
        }
        entity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
        if (entity instanceof MobEntity) {
            MobEntity mob = (MobEntity)entity;
            mob.initialize((ServerWorldAccess)world, world.getLocalDifficulty(mob.getBlockPos()), SpawnReason.BREEDING, null, null);
            NeedsOfNature.applyPregnancyVariantDataForEgg(world, (Entity)mob, this.variantData);
            NeedsOfNature.applyPregnancyMobFlagsForEgg(mob, this.resolveOwnerPlayer(world));
        }
        if (world.spawnEntity(entity)) {
            this.playHatchEffects(world);
        }
        this.discard();
    }

    public float getGrowthScale(float partialTick) {
        long startTick = this.getHatchStartGlobalTick();
        long endTick = this.getHatchAtGlobalTick();
        float startSize = this.getEggStartSize();
        float endSize = this.getEggEndSize();
        if (endTick <= startTick) {
            return endSize;
        }
        double now = (float)PregnancyEggEntity.resolveCurrentTick(this.getWorld()) + Math.max(0.0f, partialTick);
        double progress = (now - (double)startTick) / (double)(endTick - startTick);
        progress = MathHelper.clamp((double)progress, (double)0.0, (double)1.0);
        return (float)((double)startSize + (double)(endSize - startSize) * progress);
    }

    public EntityDimensions getDimensions(EntityPose pose) {
        float scale = this.getGrowthScale(0.0f);
        return EntityDimensions.fixed((float)(0.3f * scale), (float)(0.3f * scale));
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
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
        this.setEggTextureId(texture == null ? null : Identifier.tryParse((String)texture));
        this.setEggHealth(health == null ? 2.0f : health.floatValue(), true);
    }

    private void applyEggHealth(float health, boolean healToMax) {
        EntityAttributeInstance attribute = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attribute != null && Math.abs(attribute.getBaseValue() - (double)health) > 0.001) {
            attribute.setBaseValue((double)health);
        }
        if (healToMax || this.getHealth() > health || this.getHealth() <= 0.0f) {
            this.setHealth(health);
        }
    }

    private void refreshEggDimensionsIfNeeded(boolean force) {
        float scale = this.getGrowthScale(0.0f);
        if (!force && Math.abs(scale - this.lastDimensionsScale) < 0.01f) {
            return;
        }
        this.lastDimensionsScale = scale;
        this.calculateDimensions();
    }

    private static float clampEggSize(float value) {
        if (!Float.isFinite(value)) {
            return 1.0f;
        }
        return MathHelper.clamp((float)value, (float)0.05f, (float)4.0f);
    }

    private static float clampEggHealth(float value) {
        if (!Float.isFinite(value)) {
            return 2.0f;
        }
        return MathHelper.clamp((float)value, (float)0.5f, (float)100.0f);
    }

    private void playHatchEffects(ServerWorld world) {
        if (world == null) {
            return;
        }
        world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.NEUTRAL, 0.9f, 0.95f + world.getRandom().nextFloat() * 0.1f);
        world.spawnParticles((ParticleEffect)ParticleTypes.POOF, this.getX(), this.getY() + 0.25, this.getZ(), 12, 0.25, 0.2, 0.25, 0.02);
        world.spawnParticles((ParticleEffect)ParticleTypes.CLOUD, this.getX(), this.getY() + 0.2, this.getZ(), 8, 0.18, 0.12, 0.18, 0.01);
    }

    @Nullable
    private ServerPlayerEntity resolveOwnerPlayer(ServerWorld world) {
        if (this.ownerUuid == null || world.getServer() == null) {
            return null;
        }
        return world.getServer().getPlayerManager().getPlayer(this.ownerUuid);
    }

    private static long resolveGlobalTick(ServerWorld world) {
        if (world == null || world.getServer() == null) {
            return 0L;
        }
        ServerWorld overworld = world.getServer().getOverworld();
        return overworld == null ? world.getTime() : overworld.getTime();
    }

    private static long resolveCurrentTick(@Nullable World world) {
        if (world == null) {
            return 0L;
        }
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            return PregnancyEggEntity.resolveGlobalTick(serverWorld);
        }
        return world.getTime();
    }

    private void freezeStationaryState() {
        this.setSilent(true);
        this.setNoGravity(true);
        this.setAiDisabled(true);
        this.getNavigation().stop();
        Vec3d velocity = this.getVelocity();
        double x = MathHelper.clamp((double)(velocity.x * 0.65), (double)-0.08, (double)0.08);
        double z = MathHelper.clamp((double)(velocity.z * 0.65), (double)-0.08, (double)0.08);
        if (Math.abs(x) < 0.001) {
            x = 0.0;
        }
        if (Math.abs(z) < 0.001) {
            z = 0.0;
        }
        this.setVelocity(x, 0.0, z);
        this.fallDistance = 0.0f;
    }

    private void pushAwayFromNearbyEggs() {
        if (this.isRemoved()) {
            return;
        }
        Box searchBox = this.getBoundingBox().expand(0.05, 0.0, 0.05);
        List<PregnancyEggEntity> nearbyEggs = this.getWorld().getEntitiesByClass(PregnancyEggEntity.class, searchBox, egg -> egg != this && !egg.isRemoved());
        if (nearbyEggs.isEmpty()) {
            return;
        }
        double pushX = 0.0;
        double pushZ = 0.0;
        double ownRadius = Math.max(0.1, this.getBoundingBox().getXLength() * 0.5);
        for (PregnancyEggEntity other : nearbyEggs) {
            double distance;
            double dz;
            double otherRadius = Math.max(0.1, other.getBoundingBox().getXLength() * 0.5);
            double minDistance = ownRadius + otherRadius + 0.04;
            double dx = this.getX() - other.getX();
            double distanceSq = dx * dx + (dz = this.getZ() - other.getZ()) * dz;
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
        pushX = MathHelper.clamp((double)pushX, (double)-0.08, (double)0.08);
        pushZ = MathHelper.clamp((double)pushZ, (double)-0.08, (double)0.08);
        this.updatePosition(this.getX() + pushX, this.getY(), this.getZ() + pushZ);
        this.setVelocity(pushX * 0.25, 0.0, pushZ * 0.25);
        this.applySettledPushRotation(pushX, pushZ);
    }

    private double deterministicSeparationAngle() {
        long bits = this.getUuid().getMostSignificantBits() ^ this.getUuid().getLeastSignificantBits();
        double unit = (double)(bits & 0xFFFFL) / 65536.0;
        return unit * Math.PI * 2.0;
    }

    private void applySettledPushRotation(double pushX, double pushZ) {
        if (Math.abs(pushX) < 1.0E-5 && Math.abs(pushZ) < 1.0E-5) {
            return;
        }
        float yaw = (float)(Math.atan2(pushZ, pushX) * 57.29577951308232) - 90.0f;
        this.setYaw(yaw);
        this.setHeadYaw(yaw);
        this.setBodyYaw(yaw);
        this.prevYaw = yaw;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableCache;
    }
}

