/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.storage.ReadView
 *  net.minecraft.storage.WriteView
 *  net.minecraft.entity.damage.DamageSource
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.mob.PathAwareEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.world.World
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.entity.data.TrackedData
 *  net.minecraft.entity.data.TrackedDataHandler
 *  net.minecraft.entity.data.TrackedDataHandlerRegistry
 *  net.minecraft.entity.data.DataTracker
 *  net.minecraft.entity.data.DataTracker$Builder
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.entity.attribute.DefaultAttributeContainer$Builder
 *  net.minecraft.entity.attribute.EntityAttributes
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoEntity
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package com.nonid.entity;

import com.nonid.EnergyHolder;
import com.nonid.NeedsOfNature;
import com.nonid.particle.NonParticles;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HorseLiquidCollectorEntity
extends PathAwareEntity
implements GeoEntity {
    private static final TrackedData<Boolean> COLLECTOR_FULL = DataTracker.registerData(HorseLiquidCollectorEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.BOOLEAN);
    private static final String LIQUID_TYPE_NBT = "NeedsOfNatureCollectorLiquidType";
    private static final int FIXED_ENERGY_VALUE = 70;
    private static final int FULL_DRIP_INTERVAL_TICKS = 60;
    private static final int NATURAL_REGEN_INTERVAL_TICKS = 80;
    private static final float NATURAL_REGEN_AMOUNT = 1.0f;
    private static final Vec3d FULL_DRIP_LOCAL_OFFSET = new Vec3d(0.0, 0.6, -0.55);
    private final AnimatableInstanceCache animatableCache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    private Identifier storedLiquidEntityTypeId;
    private boolean storedMixedLiquid;
    private boolean orientationLocked;
    private float lockedYaw;
    private float lockedPitch;
    private long nextFullDripTick = Long.MIN_VALUE;

    public HorseLiquidCollectorEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0);
    }

    protected void initGoals() {
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(COLLECTOR_FULL, false);
    }

    public void tickMovement() {
        super.tickMovement();
        this.freezeStationaryState();
    }

    public void tick() {
        super.tick();
        this.enforceFixedEnergy();
        if (!this.getWorld().isClient && this.deathTime > 0) {
            this.discard();
            return;
        }
        this.tickNaturalRegen();
        this.freezeStationaryState();
        this.tickFullDripParticles();
    }

    public boolean damage(DamageSource source, float amount) {
        if (!this.isAlive() || this.isRemoved()) {
            return false;
        }
        if (this.isInvulnerableTo(source) || amount <= 0.0f) {
            return false;
        }
        if (this.getHealth() - amount <= 0.0f) {
            PlayerEntity player;
            Entity attacker = source.getAttacker();
            if (!(attacker instanceof PlayerEntity) || !(player = (PlayerEntity)attacker).isCreative()) {
                this.dropStack(new ItemStack((ItemConvertible)NeedsOfNature.HORSE_LIQUID_COLLECTOR));
            }
            this.discard();
            return true;
        }
        return super.damage(source, amount);
    }

    public boolean isPushable() {
        return false;
    }

    public void pushAwayFrom(Entity entity) {
    }

    public void addVelocity(double deltaX, double deltaY, double deltaZ) {
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
        super.writeCustomDataToNbt(storage);
        if (!this.isCollectorFull()) {
            return;
        }
        if (this.storedMixedLiquid) {
            storage.putString(LIQUID_TYPE_NBT, "mixed");
            return;
        }
        if (this.storedLiquidEntityTypeId != null) {
            storage.putString(LIQUID_TYPE_NBT, this.storedLiquidEntityTypeId.toString());
        }
    }

    public void readCustomDataFromNbt(NbtCompound storage) {
        super.readCustomDataFromNbt(storage);
        String raw = storage.getString(LIQUID_TYPE_NBT);
        if (raw.isBlank()) {
            this.setStoredEmpty();
            return;
        }
        if ("mixed".equalsIgnoreCase(raw)) {
            this.setStoredMixedLiquid();
            return;
        }
        Identifier parsed = Identifier.tryParse((String)raw);
        if (parsed == null) {
            this.setStoredEmpty();
            return;
        }
        this.setStoredEntityLiquid(parsed);
        this.ensureOrientationLock();
    }

    public boolean isCollectorFull() {
        return (Boolean)this.dataTracker.get(COLLECTOR_FULL);
    }

    @Nullable
    public Identifier getBottleLiquidEntityTypeId() {
        if (!this.isCollectorFull() || this.storedMixedLiquid) {
            return null;
        }
        return this.storedLiquidEntityTypeId;
    }

    public void clearStoredLiquid() {
        this.setStoredEmpty();
    }

    public void setLockedOrientation(float yaw, float pitch) {
        this.orientationLocked = true;
        this.lockedYaw = yaw;
        this.lockedPitch = pitch;
        this.applyLockedOrientation();
    }

    public boolean absorbMixedLiquid() {
        if (this.isCollectorFull() && this.storedMixedLiquid) {
            return false;
        }
        this.setStoredMixedLiquid();
        return true;
    }

    public boolean absorbEntityLiquid(@Nullable Identifier donorTypeId) {
        if (donorTypeId == null) {
            return this.absorbMixedLiquid();
        }
        if (!this.isCollectorFull()) {
            this.setStoredEntityLiquid(donorTypeId);
            return true;
        }
        if (this.storedMixedLiquid) {
            return false;
        }
        if (donorTypeId.equals((Object)this.storedLiquidEntityTypeId)) {
            return false;
        }
        this.setStoredMixedLiquid();
        return true;
    }

    private void setStoredEntityLiquid(Identifier entityTypeId) {
        this.storedLiquidEntityTypeId = entityTypeId;
        this.storedMixedLiquid = false;
        this.dataTracker.set(COLLECTOR_FULL, true);
        this.resetFullDripTimer();
    }

    private void setStoredMixedLiquid() {
        this.storedLiquidEntityTypeId = null;
        this.storedMixedLiquid = true;
        this.dataTracker.set(COLLECTOR_FULL, true);
        this.resetFullDripTimer();
    }

    private void setStoredEmpty() {
        this.storedLiquidEntityTypeId = null;
        this.storedMixedLiquid = false;
        this.dataTracker.set(COLLECTOR_FULL, false);
        this.nextFullDripTick = Long.MIN_VALUE;
    }

    private void freezeStationaryState() {
        this.setSilent(true);
        this.setNoGravity(true);
        this.setAiDisabled(true);
        this.getNavigation().stop();
        this.setVelocity(Vec3d.ZERO);
        this.fallDistance = 0.0f;
        this.applyLockedOrientation();
    }

    private void ensureOrientationLock() {
        if (this.orientationLocked) {
            return;
        }
        this.orientationLocked = true;
        this.lockedYaw = this.getYaw();
        this.lockedPitch = this.getPitch();
    }

    private void applyLockedOrientation() {
        this.ensureOrientationLock();
        this.setYaw(this.lockedYaw);
        this.setPitch(this.lockedPitch);
        this.setHeadYaw(this.lockedYaw);
        this.setBodyYaw(this.lockedYaw);
    }

    private void tickFullDripParticles() {
        World class_19372 = this.getWorld();
        if (!(class_19372 instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = (ServerWorld)class_19372;
        if (!this.isCollectorFull() || !this.isAlive() || this.isRemoved()) {
            return;
        }
        long now = world.getTime();
        if (this.nextFullDripTick == Long.MIN_VALUE) {
            this.nextFullDripTick = now + 60L;
        }
        if (now < this.nextFullDripTick) {
            return;
        }
        Vec3d spawnPos = this.resolveFullDripSpawnPos();
        world.spawnParticles((ParticleEffect)NonParticles.LIQUID_PARTICLE_FALLING, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0.0);
        this.nextFullDripTick = now + 60L;
    }

    private void resetFullDripTimer() {
        World class_19372 = this.getWorld();
        if (!(class_19372 instanceof ServerWorld)) {
            this.nextFullDripTick = Long.MIN_VALUE;
            return;
        }
        ServerWorld world = (ServerWorld)class_19372;
        this.nextFullDripTick = world.getTime() + 60L;
    }

    private void tickNaturalRegen() {
        World class_19372 = this.getWorld();
        if (!(class_19372 instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = (ServerWorld)class_19372;
        if (!this.isAlive() || this.isRemoved()) {
            return;
        }
        if (this.deathTime > 0 || this.hurtTime > 0) {
            return;
        }
        float maxHealth = this.getMaxHealth();
        if (this.getHealth() >= maxHealth) {
            return;
        }
        if (world.getTime() % 80L != 0L) {
            return;
        }
        this.heal(1.0f);
    }

    private void enforceFixedEnergy() {
        if (!(this.getWorld() instanceof ServerWorld)) {
            return;
        }
        HorseLiquidCollectorEntity horseLiquidCollectorEntity = this;
        if (!(horseLiquidCollectorEntity instanceof EnergyHolder)) {
            return;
        }
        EnergyHolder holder = (EnergyHolder)((Object)horseLiquidCollectorEntity);
        if (holder.getEnergy() == 70) {
            return;
        }
        holder.setEnergy(70);
    }

    private Vec3d resolveFullDripSpawnPos() {
        double yawRad = Math.toRadians(this.getYaw());
        double sin = Math.sin(yawRad);
        double cos = Math.cos(yawRad);
        Vec3d forward = new Vec3d(-sin, 0.0, cos);
        Vec3d right = new Vec3d(forward.z, 0.0, -forward.x);
        return new Vec3d(this.getX(), this.getY(), this.getZ()).add(right.multiply(HorseLiquidCollectorEntity.FULL_DRIP_LOCAL_OFFSET.x)).add(0.0, HorseLiquidCollectorEntity.FULL_DRIP_LOCAL_OFFSET.y, 0.0).add(forward.multiply(HorseLiquidCollectorEntity.FULL_DRIP_LOCAL_OFFSET.z));
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @NotNull
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableCache;
    }
}

