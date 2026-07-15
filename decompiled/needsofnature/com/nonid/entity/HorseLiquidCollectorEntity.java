/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11368
 *  net.minecraft.class_11372
 *  net.minecraft.class_1282
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1308
 *  net.minecraft.class_1314
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1935
 *  net.minecraft.class_1937
 *  net.minecraft.class_2394
 *  net.minecraft.class_243
 *  net.minecraft.class_2940
 *  net.minecraft.class_2941
 *  net.minecraft.class_2943
 *  net.minecraft.class_2945
 *  net.minecraft.class_2945$class_9222
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_5132$class_5133
 *  net.minecraft.class_5134
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
import net.minecraft.class_11368;
import net.minecraft.class_11372;
import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1308;
import net.minecraft.class_1314;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1935;
import net.minecraft.class_1937;
import net.minecraft.class_2394;
import net.minecraft.class_243;
import net.minecraft.class_2940;
import net.minecraft.class_2941;
import net.minecraft.class_2943;
import net.minecraft.class_2945;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_5132;
import net.minecraft.class_5134;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HorseLiquidCollectorEntity
extends class_1314
implements GeoEntity {
    private static final class_2940<Boolean> COLLECTOR_FULL = class_2945.method_12791(HorseLiquidCollectorEntity.class, (class_2941)class_2943.field_13323);
    private static final String LIQUID_TYPE_NBT = "NeedsOfNatureCollectorLiquidType";
    private static final int FIXED_ENERGY_VALUE = 70;
    private static final int FULL_DRIP_INTERVAL_TICKS = 60;
    private static final int NATURAL_REGEN_INTERVAL_TICKS = 80;
    private static final float NATURAL_REGEN_AMOUNT = 1.0f;
    private static final class_243 FULL_DRIP_LOCAL_OFFSET = new class_243(0.0, 0.6, -0.55);
    private AnimatableInstanceCache animatableCache;
    @Nullable
    private class_2960 storedLiquidEntityTypeId;
    private boolean storedMixedLiquid;
    private boolean orientationLocked;
    private float lockedYaw;
    private float lockedPitch;
    private long nextFullDripTick = Long.MIN_VALUE;

    public HorseLiquidCollectorEntity(class_1299<? extends class_1314> entityType, class_1937 world) {
        super(entityType, world);
    }

    public static class_5132.class_5133 createAttributes() {
        return class_1308.method_26828().method_26868(class_5134.field_23716, 2.0).method_26868(class_5134.field_23719, 0.0);
    }

    protected void method_5959() {
    }

    protected void method_5693(class_2945.class_9222 builder) {
        super.method_5693(builder);
        builder.method_56912(COLLECTOR_FULL, (Object)false);
    }

    public void method_6007() {
        super.method_6007();
        this.freezeStationaryState();
    }

    public void method_5773() {
        super.method_5773();
        this.enforceFixedEnergy();
        if (!this.method_73183().method_8608() && this.field_6213 > 0) {
            this.method_31472();
            return;
        }
        this.tickNaturalRegen();
        this.freezeStationaryState();
        this.tickFullDripParticles();
    }

    public boolean method_64397(class_3218 world, class_1282 source, float amount) {
        if (!this.method_5805() || this.method_31481()) {
            return false;
        }
        if (this.method_5679(world, source) || amount <= 0.0f) {
            return false;
        }
        if (this.method_6032() - amount <= 0.0f) {
            class_1657 player;
            class_1297 attacker = source.method_5529();
            if (!(attacker instanceof class_1657) || !(player = (class_1657)attacker).method_68878()) {
                this.method_5775(world, new class_1799((class_1935)NeedsOfNature.HORSE_LIQUID_COLLECTOR));
            }
            this.method_31472();
            return true;
        }
        return super.method_64397(world, source, amount);
    }

    public boolean method_5810() {
        return false;
    }

    public void method_5697(class_1297 entity) {
    }

    public void method_5762(double deltaX, double deltaY, double deltaZ) {
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
        super.method_5647(storage);
        if (!this.isCollectorFull()) {
            return;
        }
        if (this.storedMixedLiquid) {
            storage.method_71469(LIQUID_TYPE_NBT, "mixed");
            return;
        }
        if (this.storedLiquidEntityTypeId != null) {
            storage.method_71469(LIQUID_TYPE_NBT, this.storedLiquidEntityTypeId.toString());
        }
    }

    public void method_5651(class_11368 storage) {
        super.method_5651(storage);
        String raw = storage.method_71441(LIQUID_TYPE_NBT).orElse("");
        if (raw.isBlank()) {
            this.setStoredEmpty();
            return;
        }
        if ("mixed".equalsIgnoreCase(raw)) {
            this.setStoredMixedLiquid();
            return;
        }
        class_2960 parsed = class_2960.method_12829((String)raw);
        if (parsed == null) {
            this.setStoredEmpty();
            return;
        }
        this.setStoredEntityLiquid(parsed);
        this.ensureOrientationLock();
    }

    public boolean isCollectorFull() {
        return (Boolean)this.field_6011.method_12789(COLLECTOR_FULL);
    }

    @Nullable
    public class_2960 getBottleLiquidEntityTypeId() {
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

    public boolean absorbEntityLiquid(@Nullable class_2960 donorTypeId) {
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

    private void setStoredEntityLiquid(class_2960 entityTypeId) {
        this.storedLiquidEntityTypeId = entityTypeId;
        this.storedMixedLiquid = false;
        this.field_6011.method_12778(COLLECTOR_FULL, (Object)true);
        this.resetFullDripTimer();
    }

    private void setStoredMixedLiquid() {
        this.storedLiquidEntityTypeId = null;
        this.storedMixedLiquid = true;
        this.field_6011.method_12778(COLLECTOR_FULL, (Object)true);
        this.resetFullDripTimer();
    }

    private void setStoredEmpty() {
        this.storedLiquidEntityTypeId = null;
        this.storedMixedLiquid = false;
        this.field_6011.method_12778(COLLECTOR_FULL, (Object)false);
        this.nextFullDripTick = Long.MIN_VALUE;
    }

    private void freezeStationaryState() {
        this.method_5803(true);
        this.method_5875(true);
        this.method_5977(true);
        this.method_5942().method_6340();
        this.method_18799(class_243.field_1353);
        this.field_6017 = 0.0;
        this.applyLockedOrientation();
    }

    private void ensureOrientationLock() {
        if (this.orientationLocked) {
            return;
        }
        this.orientationLocked = true;
        this.lockedYaw = this.method_36454();
        this.lockedPitch = this.method_36455();
    }

    private void applyLockedOrientation() {
        this.ensureOrientationLock();
        this.method_36456(this.lockedYaw);
        this.method_36457(this.lockedPitch);
        this.method_5847(this.lockedYaw);
        this.method_5636(this.lockedYaw);
    }

    private void tickFullDripParticles() {
        class_1937 class_19372 = this.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            return;
        }
        class_3218 world = (class_3218)class_19372;
        if (!this.isCollectorFull() || !this.method_5805() || this.method_31481()) {
            return;
        }
        long now = world.method_75260();
        if (this.nextFullDripTick == Long.MIN_VALUE) {
            this.nextFullDripTick = now + 60L;
        }
        if (now < this.nextFullDripTick) {
            return;
        }
        class_243 spawnPos = this.resolveFullDripSpawnPos();
        world.method_65096((class_2394)NonParticles.LIQUID_PARTICLE_FALLING, spawnPos.field_1352, spawnPos.field_1351, spawnPos.field_1350, 1, 0.0, 0.0, 0.0, 0.0);
        this.nextFullDripTick = now + 60L;
    }

    private void resetFullDripTimer() {
        class_1937 class_19372 = this.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            this.nextFullDripTick = Long.MIN_VALUE;
            return;
        }
        class_3218 world = (class_3218)class_19372;
        this.nextFullDripTick = world.method_75260() + 60L;
    }

    private void tickNaturalRegen() {
        class_1937 class_19372 = this.method_73183();
        if (!(class_19372 instanceof class_3218)) {
            return;
        }
        class_3218 world = (class_3218)class_19372;
        if (!this.method_5805() || this.method_31481()) {
            return;
        }
        if (this.field_6213 > 0 || this.field_6235 > 0) {
            return;
        }
        float maxHealth = this.method_6063();
        if (this.method_6032() >= maxHealth) {
            return;
        }
        if (world.method_75260() % 80L != 0L) {
            return;
        }
        this.method_6025(1.0f);
    }

    private void enforceFixedEnergy() {
        if (!(this.method_73183() instanceof class_3218)) {
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

    private class_243 resolveFullDripSpawnPos() {
        double yawRad = Math.toRadians(this.method_36454());
        double sin = Math.sin(yawRad);
        double cos = Math.cos(yawRad);
        class_243 forward = new class_243(-sin, 0.0, cos);
        class_243 right = new class_243(forward.field_1350, 0.0, -forward.field_1352);
        return new class_243(this.method_23317(), this.method_23318(), this.method_23321()).method_1019(right.method_1021(HorseLiquidCollectorEntity.FULL_DRIP_LOCAL_OFFSET.field_1352)).method_1031(0.0, HorseLiquidCollectorEntity.FULL_DRIP_LOCAL_OFFSET.field_1351, 0.0).method_1019(forward.method_1021(HorseLiquidCollectorEntity.FULL_DRIP_LOCAL_OFFSET.field_1350));
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

