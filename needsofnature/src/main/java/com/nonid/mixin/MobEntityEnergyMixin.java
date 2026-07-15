/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  com.afwid.network.AnimationStageInfo
 *  net.minecraft.world.LocalDifficulty
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.EntityData
 *  net.minecraft.world.World
 *  net.minecraft.util.math.Box
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.text.Text
 *  net.minecraft.entity.data.TrackedData
 *  net.minecraft.entity.data.TrackedDataHandler
 *  net.minecraft.entity.data.TrackedDataHandlerRegistry
 *  net.minecraft.entity.data.DataTracker
 *  net.minecraft.entity.data.DataTracker$Builder
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.entity.SpawnReason
 *  net.minecraft.world.ServerWorldAccess
 *  net.minecraft.registry.Registries
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.nonid.mixin;

import com.afwid.api.AfwAnimationApi;
import com.afwid.network.AnimationStageInfo;
import com.nonid.EnergyHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonDebugChatCategory;
import com.nonid.NonGatherSystem;
import com.nonid.NonInjectorMatchPolicy;
import com.nonid.NonMobEnergySystem;
import com.nonid.NonMobGatherEligibility;
import com.nonid.data.NonAnimationEligibility;
import com.nonid.particle.NonParticles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.text.Text;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.registry.Registries;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={MobEntity.class})
public abstract class MobEntityEnergyMixin
extends LivingEntity
implements EnergyHolder {
    @Unique
    private static final TrackedData<Integer> ENERGY = DataTracker.registerData(MobEntity.class, (TrackedDataHandler)TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private int non$energyTickCounter;
    @Unique
    private boolean non$energyInitialized;
    @Unique
    private float non$energyGainMult = -1.0f;
    @Unique
    private float non$energyGainDrift = -1.0f;
    @Unique
    private double non$energyAuraCarry;
    @Unique
    private int non$nearAnimCheckCooldown;
    @Unique
    private boolean non$nearActiveAnimation;
    @Unique
    private double non$nearActiveAnimationGainMultiplier = 1.0;
    @Unique
    private int non$heartParticleCooldown;
    @Unique
    private int non$playerSearchCooldown;
    @Unique
    private int non$mobSearchCooldown;
    @Unique
    private long non$lastEnergyProcessedTick = Long.MIN_VALUE;
    @Unique
    private boolean non$attackFailsafeWarned;
    @Unique
    private final Map<UUID, Long> non$partnerCooldowns = new HashMap<UUID, Long>();
    @Unique
    private final Map<UUID, Long> non$bypassablePartnerCooldowns = new HashMap<UUID, Long>();
    @Unique
    private final Map<UUID, Long> non$multiActorJoinCooldowns = new HashMap<UUID, Long>();

    protected MobEntityEnergyMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method={"initDataTracker"}, at={@At(value="TAIL")})
    private void needsOfNature$initEnergyTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(ENERGY, 0);
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void needsOfNature$tickEnergy(CallbackInfo ci) {
        World world = this.getEntityWorld();
        if (!(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld serverWorld = (ServerWorld)world;
        long worldTick = serverWorld.getTime();
        if (this.non$lastEnergyProcessedTick == worldTick) {
            return;
        }
        this.non$lastEnergyProcessedTick = worldTick;
        this.non$ensureEnergyInitialized(serverWorld);
        boolean non$hasAnimationSupport = NonMobEnergySystem.hasGenericAnimationSupport(this.getType(), this.isBaby());
        boolean attackFailsafeActive = NeedsOfNature.isMobAttackFailsafeActive(serverWorld, this.getUuid());
        if (attackFailsafeActive) {
            if (!this.non$attackFailsafeWarned) {
                NeedsOfNature.sendDebugChatToNearby(serverWorld, (Entity)this, 20.0, NonDebugChatCategory.WARNING, (Text)Text.translatable((String)"debug.needsofnature.energy_locked_attack_cooldown", (Object[])new Object[]{Text.translatable((String)this.getType().getTranslationKey())}));
                this.non$attackFailsafeWarned = true;
            }
            if (this.getEnergy() != 0) {
                this.setEnergy(0);
            }
            this.non$energyTickCounter = 0;
            this.non$heartParticleCooldown = 0;
            this.non$playerSearchCooldown = 40;
            this.non$mobSearchCooldown = 40;
            return;
        }
        this.non$attackFailsafeWarned = false;
        if (!non$hasAnimationSupport) {
            this.non$nearActiveAnimation = false;
            this.non$nearActiveAnimationGainMultiplier = 1.0;
            if (this.getEnergy() >= 200) {
                this.setEnergy(199);
            }
            this.non$energyTickCounter = 0;
            this.non$heartParticleCooldown = 0;
        } else if (this.non$nearAnimCheckCooldown-- <= 0) {
            this.non$nearActiveAnimationGainMultiplier = NeedsOfNature.getNearbyVisibleAnimationGainMultiplier(serverWorld, (MobEntity)(Object)this);
            this.non$nearActiveAnimation = this.non$nearActiveAnimationGainMultiplier > 1.0;
            this.non$nearAnimCheckCooldown = 20;
        }
        if (!non$hasAnimationSupport) {
            return;
        }
        int energy = this.getEnergy();
        if (energy >= 200 && NonMobEnergySystem.isEnergyAttackStabilized((Entity)this)) {
            this.setEnergy(199);
            energy = this.getEnergy();
        }
        int ticksPerGain = NonMobEnergySystem.ticksPerGain(this.getEnergyGainMultiplier(), this.getEnergyGainDrift(), this.non$nearActiveAnimation, this.non$nearActiveAnimationGainMultiplier);
        if (energy < 200) {
            ++this.non$energyTickCounter;
            if (this.non$energyTickCounter >= ticksPerGain) {
                this.non$energyTickCounter = 0;
                this.setEnergy(Math.min(200, energy + 1));
            }
            this.non$heartParticleCooldown = 0;
        } else {
            this.non$energyTickCounter = 0;
            this.non$spawnMaxEnergyHeartParticle(serverWorld);
        }
        this.non$maybeStartEnergyAnimation(serverWorld, energy);
    }

    @Inject(method={"initialize"}, at={@At(value="TAIL")})
    private void needsOfNature$initEnergyOnSpawn(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            this.non$ensureEnergyInitialized(serverWorld);
        }
    }

    @Override
    public int getEnergy() {
        return (Integer)this.dataTracker.get(ENERGY);
    }

    @Override
    public void setEnergy(int value) {
        int maxEnergy = NonMobEnergySystem.isEnergyAttackStabilized((Entity)this) ? 199 : 200;
        this.dataTracker.set(ENERGY, Math.min(maxEnergy, Math.max(0, value)));
    }

    @Override
    public float getEnergyGainMultiplier() {
        return this.non$energyGainMult <= 0.0f ? 1.0f : this.non$energyGainMult;
    }

    @Override
    public void setEnergyGainMultiplier(float value) {
        if (!Float.isFinite(value)) {
            return;
        }
        this.non$energyGainMult = Math.max(0.4f, Math.min(6.0f, value));
    }

    @Override
    public float getEnergyGainDrift() {
        return this.non$energyGainDrift <= 0.0f ? 1.0f : this.non$energyGainDrift;
    }

    @Override
    public void setEnergyGainDrift(float value) {
        if (!Float.isFinite(value)) {
            return;
        }
        this.non$energyGainDrift = Math.max(0.8f, Math.min(1.2f, value));
    }

    @Override
    public double getEnergyAuraCarry() {
        return this.non$energyAuraCarry > 0.0 ? this.non$energyAuraCarry : 0.0;
    }

    @Override
    public void setEnergyAuraCarry(double value) {
        if (!Double.isFinite(value)) {
            this.non$energyAuraCarry = 0.0;
            return;
        }
        this.non$energyAuraCarry = Math.max(0.0, value);
    }

    @Override
    public boolean isEnergyInitialized() {
        return this.non$energyInitialized;
    }

    @Override
    public void markEnergyInitialized() {
        this.non$energyInitialized = true;
    }

    @Override
    public void ensureEnergyInitialized(ServerWorld world) {
        if (world == null) {
            return;
        }
        this.non$ensureEnergyInitialized(world);
    }

    @Override
    public int getMaxEnergy() {
        return 200;
    }

    @Override
    public void onNonAnimationStarted() {
        this.non$partnerCooldowns.clear();
        this.non$bypassablePartnerCooldowns.clear();
        this.non$multiActorJoinCooldowns.clear();
    }

    @Override
    public void applyPartnerCooldown(UUID partnerId, long nowTick, int cooldownTicks) {
        if (partnerId == null) {
            return;
        }
        if (cooldownTicks <= 0) {
            return;
        }
        long until = nowTick + (long)cooldownTicks;
        this.non$partnerCooldowns.put(partnerId, until);
        this.non$bypassablePartnerCooldowns.put(partnerId, until);
    }

    @Override
    public List<String> getActiveCooldownDebugLines(ServerWorld world) {
        ArrayList<String> lines = new ArrayList<String>();
        if (world == null) {
            return lines;
        }
        long nowTick = world.getTime();
        this.non$pruneCooldownMaps(nowTick);
        if (this.non$playerSearchCooldown > 0) {
            lines.add("player search delay: " + NeedsOfNature.formatTicksForDebug(this.non$playerSearchCooldown));
        }
        if (this.non$mobSearchCooldown > 0) {
            lines.add("mob search delay: " + NeedsOfNature.formatTicksForDebug(this.non$mobSearchCooldown));
        }
        NonMobEnergySystem.appendCooldownMapDebug(lines, world, nowTick, "partner", this.non$partnerCooldowns, this.non$bypassablePartnerCooldowns, true);
        NonMobEnergySystem.appendCooldownMapDebug(lines, world, nowTick, "3+ join", this.non$multiActorJoinCooldowns, this.non$bypassablePartnerCooldowns, false);
        return lines;
    }

    @Override
    public void requestAttackOnPlayer(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        if (this.getEntityWorld() != world) {
            return;
        }
        if (NonMobEnergySystem.isEnergyAttackStabilized((Entity)this) && this.getEnergy() >= 200) {
            return;
        }
        if (!NonMobEnergySystem.hasGenericAnimationSupport(this.getType(), this.isBaby())) {
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather((MobEntity)(Object)this)) {
            return;
        }
        if (!NeedsOfNature.canMobAttackPlayer(player)) {
            return;
        }
        if (NeedsOfNature.isActorPendingOrActive(world, this.getUuid())) {
            return;
        }
        if (NeedsOfNature.isMobAttackFailsafeActive(world, this.getUuid())) {
            return;
        }
        this.non$playerSearchCooldown = 0;
        this.non$partnerCooldowns.remove(player.getUuid());
        this.non$bypassablePartnerCooldowns.remove(player.getUuid());
        this.non$multiActorJoinCooldowns.remove(player.getUuid());
        this.non$tryPlayerInteraction(world, this.getEnergy(), true, true, player);
    }

    @Unique
    private void non$maybeStartEnergyAnimation(ServerWorld world, int currentEnergy) {
        boolean selfIsBaby;
        boolean isAttack;
        if (!NonMobEnergySystem.hasGenericAnimationSupport(this.getType(), this.isBaby())) {
            return;
        }
        MobEntity self = (MobEntity)(Object)this;
        if (!NonMobGatherEligibility.canAutoGather(self)) {
            return;
        }
        long nowTick = world.getTime();
        this.non$pruneCooldownMaps(nowTick);
        if (NeedsOfNature.isActorPendingOrActive(world, this.getUuid())) {
            return;
        }
        boolean canJoin = currentEnergy >= 70;
        boolean canMobMatch = currentEnergy >= 120 && currentEnergy < 200;
        boolean bl = isAttack = currentEnergy >= 200;
        if (!canJoin) {
            return;
        }
        ServerPlayerEntity lingerPlayer = null;
        lingerPlayer = this.non$findNearestActiveAnimationPlayerForLinger(world);
        if (lingerPlayer != null) {
            this.non$applyPlayerAnimationLinger(lingerPlayer);
        }
        if (this.non$playerSearchCooldown > 0) {
            --this.non$playerSearchCooldown;
        } else {
            this.non$playerSearchCooldown = 40;
            if (NeedsOfNature.consumeScanBudget(world) && this.non$tryPlayerInteraction(world, currentEnergy, true, isAttack, null)) {
                this.non$playerSearchCooldown = 100;
                return;
            }
        }
        if (this.non$mobSearchCooldown > 0) {
            --this.non$mobSearchCooldown;
            return;
        }
        this.non$mobSearchCooldown = 40;
        if (!NeedsOfNature.consumeScanBudget(world)) {
            return;
        }
        if (lingerPlayer != null) {
            return;
        }
        if (!canMobMatch && !isAttack) {
            return;
        }
        Identifier selfTypeId = Registries.ENTITY_TYPE.getId(this.getType());
        if (!NonAnimationEligibility.hasMobPairAnimationForEntityType(selfTypeId, selfIsBaby = this.isBaby())) {
            return;
        }
        Box searchBox = this.getBoundingBox().expand(20.0);
        List<MobEntity> candidates = world.getEntitiesByClass(MobEntity.class, searchBox, mob -> !mob.getUuid().equals(this.getUuid()) && NonMobGatherEligibility.canAutoGather(mob) && mob.isAlive() && !mob.isRemoved() && mob.getEntityWorld() == world && NonAnimationEligibility.isMobPairCompatible(selfTypeId, selfIsBaby, Registries.ENTITY_TYPE.getId(mob.getType()), mob.isBaby()));
        if (candidates.isEmpty()) {
            return;
        }
        candidates.sort(Comparator.comparing(candidate -> !NonMobEnergySystem.bypassesEntityEntityRoll((Entity)this, (Entity)candidate)));
        boolean entityEntityRollChecked = false;
        boolean entityEntityRollPassed = false;
        for (MobEntity candidate2 : candidates) {
            NonGatherSystem.GatherCandidate gatherCandidate;
            boolean bypassEntityEntityRoll = NonMobEnergySystem.bypassesEntityEntityRoll((Entity)this, (Entity)candidate2);
            if (!bypassEntityEntityRoll && !entityEntityRollChecked) {
                entityEntityRollChecked = true;
                entityEntityRollPassed = NonMobEnergySystem.passesEntityEntityRoll(world, (Entity)this);
                if (!entityEntityRollPassed) {
                    this.non$mobSearchCooldown = 1200;
                    return;
                }
            }
            if (!(candidate2 instanceof EnergyHolder)) continue;
            EnergyHolder holder = (EnergyHolder)candidate2;
            if (NeedsOfNature.isActorPendingOrActive(world, candidate2.getUuid())) continue;
            int candidateEnergy = holder.getEnergy();
            if (!isAttack && candidateEnergy < 70) continue;
            ArrayList<Entity> actors = new ArrayList<Entity>(2);
            actors.add((Entity)this);
            actors.add((Entity)candidate2);
            actors.sort(Comparator.comparingInt(Entity::getId));
            if (this.non$partnerCooldowns.containsKey(candidate2.getUuid()) || (gatherCandidate = NonGatherSystem.findGatherCandidate(world, actors, isAttack, (Entity)this, null)) == null) continue;
            if (!NonMobEnergySystem.passesSameGenderRoll(world, (Entity)this, actors)) {
                this.non$applyRollCooldown(candidate2.getUuid(), nowTick);
                this.non$mobSearchCooldown = 100;
                break;
            }
            Text mobName = this.getDisplayName();
            Text partnerName = candidate2.getDisplayName();
            NeedsOfNature.sendDebugChatToNearby(world, (Entity)this, 20.0, NonDebugChatCategory.INFO, (Text)Text.translatable((String)"debug.needsofnature.mob_pair_attempt", (Object[])new Object[]{mobName, partnerName, currentEnergy, candidateEnergy, gatherCandidate.animationId().getPath()}));
            NonGatherSystem.startMobMobGather(world, (MobEntity)(Object)this, candidate2, gatherCandidate.animationId(), gatherCandidate.actorKeys(), gatherCandidate.stages(), isAttack);
            NeedsOfNature.sendDebugChatToNearby(world, (Entity)this, 20.0, NonDebugChatCategory.INFO, (Text)Text.translatable((String)"debug.needsofnature.mob_pair_gathering", (Object[])new Object[]{mobName, partnerName, gatherCandidate.animationId().getPath()}));
            this.non$mobSearchCooldown = 100;
            break;
        }
    }

    @Unique
    private boolean non$tryPlayerInteraction(ServerWorld world, int currentEnergy, boolean canJoin, boolean isAttack, @Nullable ServerPlayerEntity forcedPlayer) {
        AnimationStageInfo activeStage;
        boolean attackFailsafeActive;
        MobEntity self = (MobEntity)(Object)this;
        if (!NonMobGatherEligibility.canAutoGather(self)) {
            return false;
        }
        long nowTick = world.getTime();
        double maxDistSq = 400.0;
        Text mobName = this.getDisplayName();
        String mobTypeId = Registries.ENTITY_TYPE.getId(this.getType()).toString();
        boolean bl = attackFailsafeActive = isAttack && NeedsOfNature.isMobAttackFailsafeActive(world, this.getUuid());
        if (forcedPlayer == null && world.getPlayers().isEmpty()) {
            return false;
        }
        if (attackFailsafeActive) {
            return false;
        }
        int mobChunkX = (int)Math.floor(this.getX()) >> 4;
        int mobChunkZ = (int)Math.floor(this.getZ()) >> 4;
        boolean playerNearbyChunk = false;
        if (forcedPlayer == null) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                boolean interactionIsAttack;
                if (!player.isAlive() || player.isRemoved()) continue;
                UUID inst = NeedsOfNature.getActivePlayerInstance(player);
                boolean bl2 = interactionIsAttack = inst == null ? isAttack : NeedsOfNature.isInstanceAttack(inst);
                if (interactionIsAttack && !NeedsOfNature.canMobAttackPlayer(player)) continue;
                double distSq = player.squaredDistanceTo((Entity)this);
                boolean bypassCooldown = this.non$shouldBypassPlayerCooldown(player, distSq);
                if (this.non$partnerCooldowns.containsKey(player.getUuid()) && !bypassCooldown) continue;
                if (bypassCooldown) {
                    this.non$partnerCooldowns.remove(player.getUuid());
                    this.non$bypassablePartnerCooldowns.remove(player.getUuid());
                }
                int px = (int)Math.floor(player.getX()) >> 4;
                int pz = (int)Math.floor(player.getZ()) >> 4;
                if (Math.abs(px - mobChunkX) > 1 || Math.abs(pz - mobChunkZ) > 1) continue;
                playerNearbyChunk = true;
                break;
            }
            if (!playerNearbyChunk) {
                return false;
            }
        }
        ServerPlayerEntity closest = null;
        double closestDistSq = Double.MAX_VALUE;
        if (forcedPlayer != null) {
            boolean interactionIsAttack;
            if (!forcedPlayer.isAlive() || forcedPlayer.isRemoved() || forcedPlayer.getEntityWorld() != world) {
                return false;
            }
            UUID inst = NeedsOfNature.getActivePlayerInstance(forcedPlayer);
            boolean bl3 = interactionIsAttack = inst == null ? isAttack : NeedsOfNature.isInstanceAttack(inst);
            if (interactionIsAttack && !NeedsOfNature.canMobAttackPlayer(forcedPlayer)) {
                return false;
            }
            double distSq = forcedPlayer.squaredDistanceTo((Entity)this);
            if (distSq > maxDistSq) {
                return false;
            }
            closest = forcedPlayer;
            closestDistSq = distSq;
        } else {
            for (ServerPlayerEntity player : world.getPlayers()) {
                boolean interactionIsAttack;
                if (!player.isAlive() || player.isRemoved()) continue;
                UUID inst = NeedsOfNature.getActivePlayerInstance(player);
                boolean bl4 = interactionIsAttack = inst == null ? isAttack : NeedsOfNature.isInstanceAttack(inst);
                if (interactionIsAttack && !NeedsOfNature.canMobAttackPlayer(player)) continue;
                double distSq = player.squaredDistanceTo((Entity)this);
                boolean bypassCooldown = this.non$shouldBypassPlayerCooldown(player, distSq);
                if (this.non$partnerCooldowns.containsKey(player.getUuid()) && !bypassCooldown) continue;
                if (bypassCooldown) {
                    this.non$partnerCooldowns.remove(player.getUuid());
                    this.non$bypassablePartnerCooldowns.remove(player.getUuid());
                }
                if (distSq > maxDistSq || inst == null && !isAttack || !(distSq < closestDistSq)) continue;
                closestDistSq = distSq;
                closest = player;
            }
        }
        if (closest == null) {
            return false;
        }
        UUID activeInstance = NeedsOfNature.getActivePlayerInstance(closest);
        AnimationStageInfo animationStageInfo = activeStage = activeInstance == null ? null : AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)activeInstance);
        if (activeInstance != null) {
            boolean joinAsAttack;
            boolean activeIsAttack = NeedsOfNature.isInstanceAttack(activeInstance);
            boolean bl5 = joinAsAttack = isAttack && activeIsAttack;
            if (activeStage != null && !activeStage.allowJoin()) {
                return false;
            }
            List<Entity> combinedActors = this.non$resolveJoinActors(world, activeInstance);
            NeedsOfNature.AfwMatchedAnimation combinedMatch = NeedsOfNature.findAnimationForActors(world, combinedActors, Set.of(), (Entity)closest, true, joinAsAttack ? (Entity)this : null, Set.of(), NonInjectorMatchPolicy::allowsAutomaticNonMatch);
            if (combinedMatch == null) {
                NeedsOfNature.LOGGER.info("Energy join skipped: mobType={} player={} instance={} no expanded match", new Object[]{mobTypeId, closest.getName().getString(), activeInstance});
                NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.WARNING, (Text)Text.translatable((String)"debug.needsofnature.mob_join_no_expanded_match", (Object[])new Object[]{mobName}));
                return true;
            }
            String playerName = closest.getName().getString();
            String joinAnimationId = combinedMatch.animationId().getPath();
            NeedsOfNature.LOGGER.info("Energy join attempt: mobType={} energy={} -> player={} instance={} distanceSq={}", new Object[]{mobTypeId, currentEnergy, playerName, activeInstance, closestDistSq});
            NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.INFO, (Text)Text.translatable((String)"debug.needsofnature.mob_wants_join", (Object[])new Object[]{mobName, currentEnergy, joinAnimationId}));
            if (combinedActors.size() > 2 && this.non$multiActorJoinCooldowns.containsKey(closest.getUuid())) {
                return true;
            }
            if (!NonMobEnergySystem.passesMultiActorJoinRoll(world, (Entity)this, combinedActors.size())) {
                this.non$applyNonBypassRollCooldown(closest.getUuid(), nowTick);
                return true;
            }
            if (activeIsAttack) {
                NeedsOfNature.noteAttackJoin(world, combinedActors);
            }
            NonGatherSystem.startJoinGather(world, (MobEntity)(Object)this, closest, activeInstance, combinedMatch.animationId());
            NeedsOfNature.LOGGER.info("Energy join gathering: mobType={} -> player={} instance={} anim={}", new Object[]{mobTypeId, playerName, activeInstance, combinedMatch.animationId()});
            NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.INFO, (Text)Text.translatable((String)"debug.needsofnature.join_request_accepted", (Object[])new Object[]{mobName, joinAnimationId}));
            return true;
        }
        if (!isAttack || !canJoin) {
            return false;
        }
        ArrayList<Entity> actors = new ArrayList<Entity>(2);
        actors.add((Entity)this);
        actors.add((Entity)closest);
        actors.sort(Comparator.comparingInt(Entity::getId));
        NonGatherSystem.GatherCandidate gatherCandidate = NonGatherSystem.findGatherCandidate(world, actors, true, (Entity)this, (Entity)closest);
        if (gatherCandidate == null) {
            return false;
        }
        String playerName = closest.getName().getString();
        NeedsOfNature.LOGGER.info("Energy start attempt (200): mobType={} -> player={} anim={} distanceSq={}", new Object[]{mobTypeId, playerName, gatherCandidate.animationId(), closestDistSq});
        NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.INFO, (Text)Text.translatable((String)"debug.needsofnature.starting_animation_with_mob_200", (Object[])new Object[]{mobName}));
        boolean gatherStarted = NonGatherSystem.startMobPlayerGather(world, (MobEntity)(Object)this, closest, gatherCandidate.animationId(), gatherCandidate.actorKeys(), gatherCandidate.stages());
        if (!gatherStarted) {
            this.non$playerSearchCooldown = 100;
            return true;
        }
        NeedsOfNature.LOGGER.info("Energy start gathering: mobType={} player={} anim={}", new Object[]{mobTypeId, playerName, gatherCandidate.animationId()});
        NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.INFO, (Text)Text.translatable((String)"debug.needsofnature.gathering_with_mob", (Object[])new Object[]{mobName}));
        this.non$playerSearchCooldown = 100;
        return true;
    }

    @Unique
    private void non$spawnMaxEnergyHeartParticle(ServerWorld world) {
        if (world == null) {
            return;
        }
        ++this.non$heartParticleCooldown;
        if (this.non$heartParticleCooldown < 10) {
            return;
        }
        this.non$heartParticleCooldown = 0;
        world.spawnParticles((ParticleEffect)NonParticles.SMALLHEART, this.getX(), this.getBodyY(0.6), this.getZ(), 1, 0.3, 0.3, 0.3, 0.0);
    }

    @Unique
    private boolean non$shouldBypassPlayerCooldown(ServerPlayerEntity player, double distSq) {
        if (player == null) {
            return false;
        }
        if (!this.non$bypassablePartnerCooldowns.containsKey(player.getUuid())) {
            return false;
        }
        if (distSq > 64.0) {
            return false;
        }
        MobEntity self = (MobEntity)(Object)this;
        return self.canSee((Entity)player);
    }

    @Unique
    @Nullable
    private ServerPlayerEntity non$findNearestActiveAnimationPlayerForLinger(ServerWorld world) {
        if (world == null || world.getPlayers().isEmpty()) {
            return null;
        }
        ServerPlayerEntity nearest = null;
        double nearestDistSq = Double.MAX_VALUE;
        MobEntity self = (MobEntity)(Object)this;
        double maxDistSq = 400.0;
        for (ServerPlayerEntity player : world.getPlayers()) {
            UUID activeInstance;
            double distSq;
            if (player == null || !player.isAlive() || player.isRemoved() || player.isSpectator() || player.isCreative() || (distSq = player.squaredDistanceTo((Entity)self)) > maxDistSq || distSq >= nearestDistSq || (activeInstance = NeedsOfNature.getActivePlayerInstance(player)) == null || !this.non$hasPairAnimationWithPlayer(player)) continue;
            nearest = player;
            nearestDistSq = distSq;
        }
        return nearest;
    }

    @Unique
    private boolean non$hasPairAnimationWithPlayer(ServerPlayerEntity player) {
        if (player == null) {
            return false;
        }
        World playerWorld = player.getEntityWorld();
        if (!(playerWorld instanceof ServerWorld world)) {
            return false;
        }
        ArrayList<Entity> pair = new ArrayList<Entity>(2);
        pair.add((Entity)this);
        pair.add(player);
        pair.sort(Comparator.comparingInt(Entity::getId));
        return NeedsOfNature.findAnimationForActors(world, pair, Set.of(), null, false, null, Set.of(), NonInjectorMatchPolicy::allowsAutomaticNonMatch) != null;
    }

    @Unique
    private void non$applyPlayerAnimationLinger(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        MobEntity self = (MobEntity)(Object)this;
        double lookY = Math.max(player.getY() + 0.6, player.getEyeY() - 2.0);
        self.getLookControl().lookAt(player.getX(), lookY, player.getZ(), 30.0f, 30.0f);
        double distSq = self.squaredDistanceTo((Entity)player);
        if (distSq > 9.0 || distSq < 2.25) {
            double dz;
            double targetX = player.getX();
            double targetZ = player.getZ();
            double dx = self.getX() - player.getX();
            double length = Math.sqrt(dx * dx + (dz = self.getZ() - player.getZ()) * dz);
            if (length < 1.0E-4) {
                double angle = Math.toRadians(Math.floorMod(self.getId() * 47, 360));
                dx = Math.cos(angle);
                dz = Math.sin(angle);
                length = 1.0;
            }
            double ringDistance = 2.25;
            self.getNavigation().startMovingTo(targetX += dx / length * ringDistance, player.getY(), targetZ += dz / length * ringDistance, 1.1);
        }
    }

    @Unique
    private List<Entity> non$resolveJoinActors(ServerWorld world, UUID instanceId) {
        ArrayList<Entity> combined = new ArrayList<Entity>();
        if (world != null && instanceId != null) {
            List<UUID> existing = NeedsOfNature.getInstanceActors(instanceId);
            for (UUID uuid : existing) {
                Entity e2 = world.getEntity(uuid);
                if (e2 == null) continue;
                combined.add(e2);
            }
        }
        if (combined.stream().noneMatch(e -> e.getUuid().equals(this.getUuid()))) {
            combined.add((Entity)this);
        }
        combined.sort(Comparator.comparingInt(Entity::getId));
        return combined;
    }

    @Unique
    private void non$applyRollCooldown(UUID partnerId, long nowTick) {
        if (partnerId == null) {
            return;
        }
        long until = nowTick + 1200L;
        this.non$partnerCooldowns.put(partnerId, until);
        this.non$bypassablePartnerCooldowns.put(partnerId, until);
    }

    @Unique
    private void non$applyNonBypassRollCooldown(UUID partnerId, long nowTick) {
        if (partnerId == null) {
            return;
        }
        long until = nowTick + 1200L;
        this.non$multiActorJoinCooldowns.put(partnerId, until);
    }

    @Unique
    private void non$pruneCooldownMaps(long nowTick) {
        this.non$partnerCooldowns.entrySet().removeIf(e -> (Long)e.getValue() <= nowTick);
        this.non$bypassablePartnerCooldowns.entrySet().removeIf(e -> (Long)e.getValue() <= nowTick);
        this.non$multiActorJoinCooldowns.entrySet().removeIf(e -> (Long)e.getValue() <= nowTick);
    }

    @Unique
    private void non$ensureEnergyInitialized(ServerWorld world) {
        int maxInitial;
        if (this.non$energyInitialized) {
            return;
        }
        this.non$energyInitialized = true;
        if (this.non$energyGainMult <= 0.0f) {
            this.non$energyGainMult = NonMobEnergySystem.randomGainMultiplier(world);
        }
        if (this.non$energyGainDrift <= 0.0f) {
            this.non$energyGainDrift = NonMobEnergySystem.randomGainDrift(world);
        }
        if ((maxInitial = NeedsOfNature.getConfig().getInitialEnergyMax()) > 0 && this.getEnergy() == 0) {
            int initial = world.getRandom().nextInt(maxInitial + 1);
            this.setEnergy(initial);
        }
    }
}

