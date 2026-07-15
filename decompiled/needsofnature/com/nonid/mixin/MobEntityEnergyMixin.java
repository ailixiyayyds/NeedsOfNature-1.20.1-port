/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  com.afwid.network.AnimationStageInfo
 *  net.minecraft.class_1266
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1315
 *  net.minecraft.class_1937
 *  net.minecraft.class_238
 *  net.minecraft.class_2394
 *  net.minecraft.class_2561
 *  net.minecraft.class_2940
 *  net.minecraft.class_2941
 *  net.minecraft.class_2943
 *  net.minecraft.class_2945
 *  net.minecraft.class_2945$class_9222
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_3730
 *  net.minecraft.class_5425
 *  net.minecraft.class_7923
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
import net.minecraft.class_1266;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1315;
import net.minecraft.class_1937;
import net.minecraft.class_238;
import net.minecraft.class_2394;
import net.minecraft.class_2561;
import net.minecraft.class_2940;
import net.minecraft.class_2941;
import net.minecraft.class_2943;
import net.minecraft.class_2945;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3730;
import net.minecraft.class_5425;
import net.minecraft.class_7923;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1308.class})
public abstract class MobEntityEnergyMixin
extends class_1309
implements EnergyHolder {
    @Unique
    private static final class_2940<Integer> ENERGY = class_2945.method_12791(class_1308.class, (class_2941)class_2943.field_13327);
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

    protected MobEntityEnergyMixin(class_1299<? extends class_1309> entityType, class_1937 world) {
        super(entityType, world);
    }

    @Inject(method={"method_5693"}, at={@At(value="TAIL")})
    private void needsOfNature$initEnergyTracker(class_2945.class_9222 builder, CallbackInfo ci) {
        builder.method_56912(ENERGY, (Object)0);
    }

    @Inject(method={"method_5773"}, at={@At(value="TAIL")})
    private void needsOfNature$tickEnergy(CallbackInfo ci) {
        class_1937 world = this.method_73183();
        if (!(world instanceof class_3218)) {
            return;
        }
        class_3218 serverWorld = (class_3218)world;
        long worldTick = serverWorld.method_75260();
        if (this.non$lastEnergyProcessedTick == worldTick) {
            return;
        }
        this.non$lastEnergyProcessedTick = worldTick;
        this.non$ensureEnergyInitialized(serverWorld);
        boolean non$hasAnimationSupport = NonMobEnergySystem.hasGenericAnimationSupport(this.method_5864(), this.method_6109());
        boolean attackFailsafeActive = NeedsOfNature.isMobAttackFailsafeActive(serverWorld, this.method_5667());
        if (attackFailsafeActive) {
            if (!this.non$attackFailsafeWarned) {
                NeedsOfNature.sendDebugChatToNearby(serverWorld, (class_1297)this, 20.0, NonDebugChatCategory.WARNING, (class_2561)class_2561.method_43469((String)"debug.needsofnature.energy_locked_attack_cooldown", (Object[])new Object[]{class_2561.method_43471((String)this.method_5864().method_5882())}));
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
            this.non$nearActiveAnimationGainMultiplier = NeedsOfNature.getNearbyVisibleAnimationGainMultiplier(serverWorld, (class_1308)this);
            this.non$nearActiveAnimation = this.non$nearActiveAnimationGainMultiplier > 1.0;
            this.non$nearAnimCheckCooldown = 20;
        }
        if (!non$hasAnimationSupport) {
            return;
        }
        int energy = this.getEnergy();
        if (energy >= 200 && NonMobEnergySystem.isEnergyAttackStabilized((class_1297)this)) {
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

    @Inject(method={"method_5943"}, at={@At(value="TAIL")})
    private void needsOfNature$initEnergyOnSpawn(class_5425 world, class_1266 difficulty, class_3730 spawnReason, @Nullable class_1315 entityData, CallbackInfoReturnable<class_1315> cir) {
        if (world instanceof class_3218) {
            class_3218 serverWorld = (class_3218)world;
            this.non$ensureEnergyInitialized(serverWorld);
        }
    }

    @Override
    public int getEnergy() {
        return (Integer)this.field_6011.method_12789(ENERGY);
    }

    @Override
    public void setEnergy(int value) {
        int maxEnergy = NonMobEnergySystem.isEnergyAttackStabilized((class_1297)this) ? 199 : 200;
        this.field_6011.method_12778(ENERGY, (Object)Math.min(maxEnergy, Math.max(0, value)));
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
    public void ensureEnergyInitialized(class_3218 world) {
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
    public List<String> getActiveCooldownDebugLines(class_3218 world) {
        ArrayList<String> lines = new ArrayList<String>();
        if (world == null) {
            return lines;
        }
        long nowTick = world.method_75260();
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
    public void requestAttackOnPlayer(class_3218 world, class_3222 player) {
        if (world == null || player == null) {
            return;
        }
        if (this.method_73183() != world) {
            return;
        }
        if (NonMobEnergySystem.isEnergyAttackStabilized((class_1297)this) && this.getEnergy() >= 200) {
            return;
        }
        if (!NonMobEnergySystem.hasGenericAnimationSupport(this.method_5864(), this.method_6109())) {
            return;
        }
        if (!NonMobGatherEligibility.canAutoGather((class_1308)this)) {
            return;
        }
        if (!NeedsOfNature.canMobAttackPlayer(player)) {
            return;
        }
        if (NeedsOfNature.isActorPendingOrActive(world, this.method_5667())) {
            return;
        }
        if (NeedsOfNature.isMobAttackFailsafeActive(world, this.method_5667())) {
            return;
        }
        this.non$playerSearchCooldown = 0;
        this.non$partnerCooldowns.remove(player.method_5667());
        this.non$bypassablePartnerCooldowns.remove(player.method_5667());
        this.non$multiActorJoinCooldowns.remove(player.method_5667());
        this.non$tryPlayerInteraction(world, this.getEnergy(), true, true, player);
    }

    @Unique
    private void non$maybeStartEnergyAnimation(class_3218 world, int currentEnergy) {
        boolean selfIsBaby;
        boolean isAttack;
        if (!NonMobEnergySystem.hasGenericAnimationSupport(this.method_5864(), this.method_6109())) {
            return;
        }
        class_1308 self = (class_1308)this;
        if (!NonMobGatherEligibility.canAutoGather(self)) {
            return;
        }
        long nowTick = world.method_75260();
        this.non$pruneCooldownMaps(nowTick);
        if (NeedsOfNature.isActorPendingOrActive(world, this.method_5667())) {
            return;
        }
        boolean canJoin = currentEnergy >= 70;
        boolean canMobMatch = currentEnergy >= 120 && currentEnergy < 200;
        boolean bl = isAttack = currentEnergy >= 200;
        if (!canJoin) {
            return;
        }
        class_3222 lingerPlayer = null;
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
        class_2960 selfTypeId = class_7923.field_41177.method_10221((Object)this.method_5864());
        if (!NonAnimationEligibility.hasMobPairAnimationForEntityType(selfTypeId, selfIsBaby = this.method_6109())) {
            return;
        }
        class_238 searchBox = this.method_5829().method_1014(20.0);
        List candidates = world.method_8390(class_1308.class, searchBox, mob -> !mob.method_5667().equals(this.method_5667()) && NonMobGatherEligibility.canAutoGather(mob) && mob.method_5805() && !mob.method_31481() && mob.method_73183() == world && NonAnimationEligibility.isMobPairCompatible(selfTypeId, selfIsBaby, class_7923.field_41177.method_10221((Object)mob.method_5864()), mob.method_6109()));
        if (candidates.isEmpty()) {
            return;
        }
        candidates.sort(Comparator.comparing(candidate -> !NonMobEnergySystem.bypassesEntityEntityRoll((class_1297)this, (class_1297)candidate)));
        boolean entityEntityRollChecked = false;
        boolean entityEntityRollPassed = false;
        for (class_1308 candidate2 : candidates) {
            NonGatherSystem.GatherCandidate gatherCandidate;
            boolean bypassEntityEntityRoll = NonMobEnergySystem.bypassesEntityEntityRoll((class_1297)this, (class_1297)candidate2);
            if (!bypassEntityEntityRoll && !entityEntityRollChecked) {
                entityEntityRollChecked = true;
                entityEntityRollPassed = NonMobEnergySystem.passesEntityEntityRoll(world, (class_1297)this);
                if (!entityEntityRollPassed) {
                    this.non$mobSearchCooldown = 1200;
                    return;
                }
            }
            if (!(candidate2 instanceof EnergyHolder)) continue;
            EnergyHolder holder = (EnergyHolder)candidate2;
            if (NeedsOfNature.isActorPendingOrActive(world, candidate2.method_5667())) continue;
            int candidateEnergy = holder.getEnergy();
            if (!isAttack && candidateEnergy < 70) continue;
            ArrayList<class_1297> actors = new ArrayList<class_1297>(2);
            actors.add((class_1297)this);
            actors.add((class_1297)candidate2);
            actors.sort(Comparator.comparingInt(class_1297::method_5628));
            if (this.non$partnerCooldowns.containsKey(candidate2.method_5667()) || (gatherCandidate = NonGatherSystem.findGatherCandidate(world, actors, isAttack, (class_1297)this, null)) == null) continue;
            if (!NonMobEnergySystem.passesSameGenderRoll(world, (class_1297)this, actors)) {
                this.non$applyRollCooldown(candidate2.method_5667(), nowTick);
                this.non$mobSearchCooldown = 100;
                break;
            }
            class_2561 mobName = this.method_5476();
            class_2561 partnerName = candidate2.method_5476();
            NeedsOfNature.sendDebugChatToNearby(world, (class_1297)this, 20.0, NonDebugChatCategory.INFO, (class_2561)class_2561.method_43469((String)"debug.needsofnature.mob_pair_attempt", (Object[])new Object[]{mobName, partnerName, currentEnergy, candidateEnergy, gatherCandidate.animationId().method_12832()}));
            NonGatherSystem.startMobMobGather(world, (class_1308)this, candidate2, gatherCandidate.animationId(), gatherCandidate.actorKeys(), gatherCandidate.stages(), isAttack);
            NeedsOfNature.sendDebugChatToNearby(world, (class_1297)this, 20.0, NonDebugChatCategory.INFO, (class_2561)class_2561.method_43469((String)"debug.needsofnature.mob_pair_gathering", (Object[])new Object[]{mobName, partnerName, gatherCandidate.animationId().method_12832()}));
            this.non$mobSearchCooldown = 100;
            break;
        }
    }

    @Unique
    private boolean non$tryPlayerInteraction(class_3218 world, int currentEnergy, boolean canJoin, boolean isAttack, @Nullable class_3222 forcedPlayer) {
        AnimationStageInfo activeStage;
        boolean attackFailsafeActive;
        class_1308 self = (class_1308)this;
        if (!NonMobGatherEligibility.canAutoGather(self)) {
            return false;
        }
        long nowTick = world.method_75260();
        double maxDistSq = 400.0;
        class_2561 mobName = this.method_5476();
        String mobTypeId = class_7923.field_41177.method_10221((Object)this.method_5864()).toString();
        boolean bl = attackFailsafeActive = isAttack && NeedsOfNature.isMobAttackFailsafeActive(world, this.method_5667());
        if (forcedPlayer == null && world.method_18456().isEmpty()) {
            return false;
        }
        if (attackFailsafeActive) {
            return false;
        }
        int mobChunkX = (int)Math.floor(this.method_23317()) >> 4;
        int mobChunkZ = (int)Math.floor(this.method_23321()) >> 4;
        boolean playerNearbyChunk = false;
        if (forcedPlayer == null) {
            for (class_3222 player : world.method_18456()) {
                boolean interactionIsAttack;
                if (!player.method_5805() || player.method_31481()) continue;
                UUID inst = NeedsOfNature.getActivePlayerInstance(player);
                boolean bl2 = interactionIsAttack = inst == null ? isAttack : NeedsOfNature.isInstanceAttack(inst);
                if (interactionIsAttack && !NeedsOfNature.canMobAttackPlayer(player)) continue;
                double distSq = player.method_5858((class_1297)this);
                boolean bypassCooldown = this.non$shouldBypassPlayerCooldown(player, distSq);
                if (this.non$partnerCooldowns.containsKey(player.method_5667()) && !bypassCooldown) continue;
                if (bypassCooldown) {
                    this.non$partnerCooldowns.remove(player.method_5667());
                    this.non$bypassablePartnerCooldowns.remove(player.method_5667());
                }
                int px = (int)Math.floor(player.method_23317()) >> 4;
                int pz = (int)Math.floor(player.method_23321()) >> 4;
                if (Math.abs(px - mobChunkX) > 1 || Math.abs(pz - mobChunkZ) > 1) continue;
                playerNearbyChunk = true;
                break;
            }
            if (!playerNearbyChunk) {
                return false;
            }
        }
        class_3222 closest = null;
        double closestDistSq = Double.MAX_VALUE;
        if (forcedPlayer != null) {
            boolean interactionIsAttack;
            if (!forcedPlayer.method_5805() || forcedPlayer.method_31481() || forcedPlayer.method_51469() != world) {
                return false;
            }
            UUID inst = NeedsOfNature.getActivePlayerInstance(forcedPlayer);
            boolean bl3 = interactionIsAttack = inst == null ? isAttack : NeedsOfNature.isInstanceAttack(inst);
            if (interactionIsAttack && !NeedsOfNature.canMobAttackPlayer(forcedPlayer)) {
                return false;
            }
            double distSq = forcedPlayer.method_5858((class_1297)this);
            if (distSq > maxDistSq) {
                return false;
            }
            closest = forcedPlayer;
            closestDistSq = distSq;
        } else {
            for (class_3222 player : world.method_18456()) {
                boolean interactionIsAttack;
                if (!player.method_5805() || player.method_31481()) continue;
                UUID inst = NeedsOfNature.getActivePlayerInstance(player);
                boolean bl4 = interactionIsAttack = inst == null ? isAttack : NeedsOfNature.isInstanceAttack(inst);
                if (interactionIsAttack && !NeedsOfNature.canMobAttackPlayer(player)) continue;
                double distSq = player.method_5858((class_1297)this);
                boolean bypassCooldown = this.non$shouldBypassPlayerCooldown(player, distSq);
                if (this.non$partnerCooldowns.containsKey(player.method_5667()) && !bypassCooldown) continue;
                if (bypassCooldown) {
                    this.non$partnerCooldowns.remove(player.method_5667());
                    this.non$bypassablePartnerCooldowns.remove(player.method_5667());
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
        AnimationStageInfo animationStageInfo = activeStage = activeInstance == null ? null : AfwAnimationApi.getCurrentStage((class_3218)world, (UUID)activeInstance);
        if (activeInstance != null) {
            boolean joinAsAttack;
            boolean activeIsAttack = NeedsOfNature.isInstanceAttack(activeInstance);
            boolean bl5 = joinAsAttack = isAttack && activeIsAttack;
            if (activeStage != null && !activeStage.allowJoin()) {
                return false;
            }
            List<class_1297> combinedActors = this.non$resolveJoinActors(world, activeInstance);
            NeedsOfNature.AfwMatchedAnimation combinedMatch = NeedsOfNature.findAnimationForActors(world, combinedActors, Set.of(), (class_1297)closest, true, joinAsAttack ? (class_1297)this : null, Set.of(), NonInjectorMatchPolicy::allowsAutomaticNonMatch);
            if (combinedMatch == null) {
                NeedsOfNature.LOGGER.info("Energy join skipped: mobType={} player={} instance={} no expanded match", new Object[]{mobTypeId, closest.method_5477().getString(), activeInstance});
                NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.WARNING, (class_2561)class_2561.method_43469((String)"debug.needsofnature.mob_join_no_expanded_match", (Object[])new Object[]{mobName}));
                return true;
            }
            String playerName = closest.method_5477().getString();
            String joinAnimationId = combinedMatch.animationId().method_12832();
            NeedsOfNature.LOGGER.info("Energy join attempt: mobType={} energy={} -> player={} instance={} distanceSq={}", new Object[]{mobTypeId, currentEnergy, playerName, activeInstance, closestDistSq});
            NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.INFO, (class_2561)class_2561.method_43469((String)"debug.needsofnature.mob_wants_join", (Object[])new Object[]{mobName, currentEnergy, joinAnimationId}));
            if (combinedActors.size() > 2 && this.non$multiActorJoinCooldowns.containsKey(closest.method_5667())) {
                return true;
            }
            if (!NonMobEnergySystem.passesMultiActorJoinRoll(world, (class_1297)this, combinedActors.size())) {
                this.non$applyNonBypassRollCooldown(closest.method_5667(), nowTick);
                return true;
            }
            if (activeIsAttack) {
                NeedsOfNature.noteAttackJoin(world, combinedActors);
            }
            NonGatherSystem.startJoinGather(world, (class_1308)this, closest, activeInstance, combinedMatch.animationId());
            NeedsOfNature.LOGGER.info("Energy join gathering: mobType={} -> player={} instance={} anim={}", new Object[]{mobTypeId, playerName, activeInstance, combinedMatch.animationId()});
            NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.INFO, (class_2561)class_2561.method_43469((String)"debug.needsofnature.join_request_accepted", (Object[])new Object[]{mobName, joinAnimationId}));
            return true;
        }
        if (!isAttack || !canJoin) {
            return false;
        }
        ArrayList<class_1297> actors = new ArrayList<class_1297>(2);
        actors.add((class_1297)this);
        actors.add((class_1297)closest);
        actors.sort(Comparator.comparingInt(class_1297::method_5628));
        NonGatherSystem.GatherCandidate gatherCandidate = NonGatherSystem.findGatherCandidate(world, actors, true, (class_1297)this, (class_1297)closest);
        if (gatherCandidate == null) {
            return false;
        }
        String playerName = closest.method_5477().getString();
        NeedsOfNature.LOGGER.info("Energy start attempt (200): mobType={} -> player={} anim={} distanceSq={}", new Object[]{mobTypeId, playerName, gatherCandidate.animationId(), closestDistSq});
        NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.INFO, (class_2561)class_2561.method_43469((String)"debug.needsofnature.starting_animation_with_mob_200", (Object[])new Object[]{mobName}));
        boolean gatherStarted = NonGatherSystem.startMobPlayerGather(world, (class_1308)this, closest, gatherCandidate.animationId(), gatherCandidate.actorKeys(), gatherCandidate.stages());
        if (!gatherStarted) {
            this.non$playerSearchCooldown = 100;
            return true;
        }
        NeedsOfNature.LOGGER.info("Energy start gathering: mobType={} player={} anim={}", new Object[]{mobTypeId, playerName, gatherCandidate.animationId()});
        NeedsOfNature.sendDebugChat(closest, NonDebugChatCategory.INFO, (class_2561)class_2561.method_43469((String)"debug.needsofnature.gathering_with_mob", (Object[])new Object[]{mobName}));
        this.non$playerSearchCooldown = 100;
        return true;
    }

    @Unique
    private void non$spawnMaxEnergyHeartParticle(class_3218 world) {
        if (world == null) {
            return;
        }
        ++this.non$heartParticleCooldown;
        if (this.non$heartParticleCooldown < 10) {
            return;
        }
        this.non$heartParticleCooldown = 0;
        world.method_65096((class_2394)NonParticles.SMALLHEART, this.method_23317(), this.method_23323(0.6), this.method_23321(), 1, 0.3, 0.3, 0.3, 0.0);
    }

    @Unique
    private boolean non$shouldBypassPlayerCooldown(class_3222 player, double distSq) {
        if (player == null) {
            return false;
        }
        if (!this.non$bypassablePartnerCooldowns.containsKey(player.method_5667())) {
            return false;
        }
        if (distSq > 64.0) {
            return false;
        }
        class_1308 self = (class_1308)this;
        return self.method_6057((class_1297)player);
    }

    @Unique
    @Nullable
    private class_3222 non$findNearestActiveAnimationPlayerForLinger(class_3218 world) {
        if (world == null || world.method_18456().isEmpty()) {
            return null;
        }
        class_3222 nearest = null;
        double nearestDistSq = Double.MAX_VALUE;
        class_1308 self = (class_1308)this;
        double maxDistSq = 400.0;
        for (class_3222 player : world.method_18456()) {
            UUID activeInstance;
            double distSq;
            if (player == null || !player.method_5805() || player.method_31481() || player.method_7325() || player.method_68878() || (distSq = player.method_5858((class_1297)self)) > maxDistSq || distSq >= nearestDistSq || (activeInstance = NeedsOfNature.getActivePlayerInstance(player)) == null || !this.non$hasPairAnimationWithPlayer(player)) continue;
            nearest = player;
            nearestDistSq = distSq;
        }
        return nearest;
    }

    @Unique
    private boolean non$hasPairAnimationWithPlayer(class_3222 player) {
        if (player == null) {
            return false;
        }
        class_3218 class_32182 = player.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            return false;
        }
        class_3218 world = class_32182;
        ArrayList<MobEntityEnergyMixin> pair = new ArrayList<MobEntityEnergyMixin>(2);
        pair.add(this);
        pair.add((MobEntityEnergyMixin)player);
        pair.sort(Comparator.comparingInt(class_1297::method_5628));
        return NeedsOfNature.findAnimationForActors(world, pair, Set.of(), null, false, null, Set.of(), NonInjectorMatchPolicy::allowsAutomaticNonMatch) != null;
    }

    @Unique
    private void non$applyPlayerAnimationLinger(class_3222 player) {
        if (player == null) {
            return;
        }
        class_1308 self = (class_1308)this;
        double lookY = Math.max(player.method_23318() + 0.6, player.method_23320() - 2.0);
        self.method_5988().method_6230(player.method_23317(), lookY, player.method_23321(), 30.0f, 30.0f);
        double distSq = self.method_5858((class_1297)player);
        if (distSq > 9.0 || distSq < 2.25) {
            double dz;
            double targetX = player.method_23317();
            double targetZ = player.method_23321();
            double dx = self.method_23317() - player.method_23317();
            double length = Math.sqrt(dx * dx + (dz = self.method_23321() - player.method_23321()) * dz);
            if (length < 1.0E-4) {
                double angle = Math.toRadians(Math.floorMod(self.method_5628() * 47, 360));
                dx = Math.cos(angle);
                dz = Math.sin(angle);
                length = 1.0;
            }
            double ringDistance = 2.25;
            self.method_5942().method_6337(targetX += dx / length * ringDistance, player.method_23318(), targetZ += dz / length * ringDistance, 1.1);
        }
    }

    @Unique
    private List<class_1297> non$resolveJoinActors(class_3218 world, UUID instanceId) {
        ArrayList<class_1297> combined = new ArrayList<class_1297>();
        if (world != null && instanceId != null) {
            List<UUID> existing = NeedsOfNature.getInstanceActors(instanceId);
            for (UUID uuid : existing) {
                class_1297 e2 = world.method_66347(uuid);
                if (e2 == null) continue;
                combined.add(e2);
            }
        }
        if (combined.stream().noneMatch(e -> e.method_5667().equals(this.method_5667()))) {
            combined.add((class_1297)this);
        }
        combined.sort(Comparator.comparingInt(class_1297::method_5628));
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
    private void non$ensureEnergyInitialized(class_3218 world) {
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
            int initial = world.method_8409().method_43048(maxInitial + 1);
            this.setEnergy(initial);
        }
    }
}

