/*
 * Decompiled with CFR 0.152.
 */
package com.nonid.network;

import com.nonid.NonConfig;
import com.nonid.data.NonPeakStages;
import java.util.LinkedHashMap;
import java.util.Map;

public final class HostConfigSyncData {
    public int initialEnergyMax;
    public double energyGainRate;
    public double nearAnimationEnergyGainMult;
    public double playerEnergyAuraMultLow;
    public double playerEnergyAuraMultMid;
    public double playerEnergyAuraMultHigh;
    public double destroyedSkinAuraMultStage1;
    public double destroyedSkinAuraMultStage2;
    public double destroyedSkinAuraMultStage3;
    public double destroyedSkinAuraMultStage4;
    public int normalDamageDestroyedSkinChancePercent = 10;
    public boolean lastDefeatedEnabled = true;
    public int lastDefeatedEnergyThreshold = 70;
    public int lastDefeatedSearchRadius = 6;
    public int lastDefeatedCooldownSeconds = 120;
    public int playerEnergyAuraRadiusMin;
    public int playerEnergyAuraRadiusMax;
    public int playerEnergyAuraPulseTicks;
    public int scanBudgetPerTick;
    public int loopProgressSeconds;
    public int peakLoopProgressSeconds;
    public int genderMaleChancePercent;
    public int genderFemaleChancePercent;
    public int genderBothChancePercent;
    public int maleMaleChancePercent;
    public int femaleFemaleChancePercent;
    public int entityEntityChancePercent;
    public int multiActorJoinChancePercent;
    public boolean allowPlayerGenderChangeAnytime = true;
    public boolean requirePlayerGenderSelectionOnJoin = false;
    public int allowedStartingGenderMask = 7;
    public boolean pregnancyEnabled;
    public boolean liquidTankEnabled;
    public boolean messSystemEnabled = true;
    public boolean destroyedSkinSystemEnabled = true;
    public boolean allowCraftingTableSkinRepair = false;
    public boolean keepMessAfterDeath = false;
    public boolean keepRippedSkinAfterDeath = false;
    public boolean keepLiquidTankAfterDeath = false;
    public boolean keepPregnancyAfterDeath = false;
    public int liquidTankCapacityMl = 200;
    public boolean vanillaOverridesEnabled;
    public boolean useAnimationBreeding;
    public boolean requireMaleFemaleForBreeding;
    public boolean femaleCowOnlyMilking;
    public boolean attackCreativePlayers;
    public boolean convertMaleOnlyVInjectionsToA = true;
    public int attackEscapeHits;
    public double attackDecayPerSecond;
    public int attackEscapeDamageDifficultyPercent = 100;
    public int attackEscapeInvulnerabilitySeconds = 3;
    public int attackEscapeAnimationProtectionSeconds = 3;
    public int postEscapeGatherMaxMobs = 8;
    public boolean blockAnimationsWhileRidingLivingEntities = true;
    public boolean blockAnimationsWhileRidingVehicles = false;
    public int attackOutcomeFailsafeThreshold = 3;
    public int pregnancyChancePercent;
    public int pregnancyDurationMinutes;
    public Map<String, Integer> pregnancyChanceByEntity = new LinkedHashMap<String, Integer>();
    public Map<String, NonConfig.OffspringCountRange> pregnancyOffspringCountByEntity = new LinkedHashMap<String, NonConfig.OffspringCountRange>();
    public int pregnancyEggDefaultHatchSeconds;
    public Map<String, Boolean> pregnancyEggEnabledByEntity = new LinkedHashMap<String, Boolean>();
    public Map<String, Integer> pregnancyEggHatchSecondsByEntity = new LinkedHashMap<String, Integer>();
    public boolean pregnancyAutoTameMobs = true;
    public boolean pregnancyFriendlyMobsIgnorePlayers = false;
    public Map<String, NonConfig.EntityProfile> entityProfilesByEntity = new LinkedHashMap<String, NonConfig.EntityProfile>();
    public double liquidDecayPerSecond;
    public double peakXpPerMl = 0.06666666666666667;
    public float filledStageOneSpeedMult;
    public float filledStageTwoSpeedMult;
    public float filledStageThreeSpeedMult;
    public float filledStageOneJumpMult;
    public float filledStageTwoJumpMult;
    public float filledStageThreeJumpMult;
    public Map<String, NonConfig.GenderSpawnChances> genderSpawnByEntity = new LinkedHashMap<String, NonConfig.GenderSpawnChances>();
    public Map<String, Integer> liquidGainByEntity = new LinkedHashMap<String, Integer>();
    public Map<String, String> liquidColorByEntity = new LinkedHashMap<String, String>();
    public Map<String, Integer> peakStageByAnimation = new LinkedHashMap<String, Integer>();

    public static HostConfigSyncData fromConfig(NonConfig config) {
        HostConfigSyncData data = new HostConfigSyncData();
        if (config == null) {
            return data;
        }
        data.initialEnergyMax = config.getInitialEnergyMax();
        data.energyGainRate = config.getEnergyGainRate();
        data.nearAnimationEnergyGainMult = config.getNearAnimationEnergyGainMult();
        data.playerEnergyAuraMultLow = config.getPlayerEnergyAuraMultLow();
        data.playerEnergyAuraMultMid = config.getPlayerEnergyAuraMultMid();
        data.playerEnergyAuraMultHigh = config.getPlayerEnergyAuraMultHigh();
        data.destroyedSkinAuraMultStage1 = config.getDestroyedSkinAuraMultStage1();
        data.destroyedSkinAuraMultStage2 = config.getDestroyedSkinAuraMultStage2();
        data.destroyedSkinAuraMultStage3 = config.getDestroyedSkinAuraMultStage3();
        data.destroyedSkinAuraMultStage4 = config.getDestroyedSkinAuraMultStage4();
        data.normalDamageDestroyedSkinChancePercent = config.getNormalDamageDestroyedSkinChancePercent();
        data.lastDefeatedEnabled = config.isLastDefeatedEnabled();
        data.lastDefeatedEnergyThreshold = config.getLastDefeatedEnergyThreshold();
        data.lastDefeatedSearchRadius = config.getLastDefeatedSearchRadius();
        data.lastDefeatedCooldownSeconds = config.getLastDefeatedCooldownSeconds();
        data.playerEnergyAuraRadiusMin = config.getPlayerEnergyAuraRadiusMin();
        data.playerEnergyAuraRadiusMax = config.getPlayerEnergyAuraRadiusMax();
        data.playerEnergyAuraPulseTicks = config.getPlayerEnergyAuraPulseTicks();
        data.scanBudgetPerTick = config.getScanBudgetPerTick();
        data.loopProgressSeconds = config.getLoopProgressSeconds();
        data.peakLoopProgressSeconds = config.getPeakLoopProgressSeconds();
        data.genderMaleChancePercent = config.getGenderMaleChancePercent();
        data.genderFemaleChancePercent = config.getGenderFemaleChancePercent();
        data.genderBothChancePercent = config.getGenderBothChancePercent();
        data.maleMaleChancePercent = config.getMaleMaleChancePercent();
        data.femaleFemaleChancePercent = config.getFemaleFemaleChancePercent();
        data.entityEntityChancePercent = config.getEntityEntityChancePercent();
        data.multiActorJoinChancePercent = config.getMultiActorJoinChancePercent();
        data.allowPlayerGenderChangeAnytime = config.allowPlayerGenderChangeAnytime();
        data.requirePlayerGenderSelectionOnJoin = config.requirePlayerGenderSelectionOnJoin();
        data.allowedStartingGenderMask = config.getAllowedStartingGenderMask();
        data.pregnancyEnabled = config.isPregnancyEnabled();
        data.liquidTankEnabled = config.isLiquidTankEnabled();
        data.messSystemEnabled = config.isMessSystemEnabled();
        data.destroyedSkinSystemEnabled = config.isDestroyedSkinSystemEnabled();
        data.allowCraftingTableSkinRepair = config.isCraftingTableSkinRepairAllowed();
        data.keepMessAfterDeath = config.keepMessAfterDeath();
        data.keepRippedSkinAfterDeath = config.keepRippedSkinAfterDeath();
        data.keepLiquidTankAfterDeath = config.keepLiquidTankAfterDeath();
        data.keepPregnancyAfterDeath = config.keepPregnancyAfterDeath();
        data.liquidTankCapacityMl = config.getLiquidTankCapacityMl();
        data.vanillaOverridesEnabled = config.isVanillaOverridesEnabled();
        data.useAnimationBreeding = config.useAnimationBreeding();
        data.requireMaleFemaleForBreeding = config.requireMaleFemaleForBreeding();
        data.femaleCowOnlyMilking = config.femaleCowOnlyMilking();
        data.attackCreativePlayers = config.canAttackCreativePlayers();
        data.convertMaleOnlyVInjectionsToA = config.convertMaleOnlyVInjectionsToA();
        data.attackEscapeHits = config.getAttackEscapeHits();
        data.attackDecayPerSecond = config.getAttackDecayPerSecond();
        data.attackEscapeDamageDifficultyPercent = config.getAttackEscapeDamageDifficultyPercent();
        data.attackEscapeInvulnerabilitySeconds = config.getAttackEscapeInvulnerabilitySeconds();
        data.attackEscapeAnimationProtectionSeconds = config.getAttackEscapeAnimationProtectionSeconds();
        data.postEscapeGatherMaxMobs = config.getPostEscapeGatherMaxMobs();
        data.blockAnimationsWhileRidingLivingEntities = config.blockAnimationsWhileRidingLivingEntities();
        data.blockAnimationsWhileRidingVehicles = config.blockAnimationsWhileRidingVehicles();
        data.attackOutcomeFailsafeThreshold = config.getAttackOutcomeFailsafeThreshold();
        data.pregnancyChancePercent = config.getPregnancyChancePercent();
        data.pregnancyDurationMinutes = config.getPregnancyDurationMinutes();
        data.pregnancyChanceByEntity = new LinkedHashMap<String, Integer>(config.getPregnancyChanceByEntity());
        data.pregnancyOffspringCountByEntity = new LinkedHashMap<String, NonConfig.OffspringCountRange>(config.getPregnancyOffspringCountByEntity());
        data.pregnancyEggDefaultHatchSeconds = config.getPregnancyEggDefaultHatchSeconds();
        data.pregnancyEggEnabledByEntity = new LinkedHashMap<String, Boolean>(config.getPregnancyEggEnabledByEntity());
        data.pregnancyEggHatchSecondsByEntity = new LinkedHashMap<String, Integer>(config.getPregnancyEggHatchSecondsByEntity());
        data.pregnancyAutoTameMobs = config.isPregnancyAutoTameMobs();
        data.pregnancyFriendlyMobsIgnorePlayers = config.pregnancyFriendlyMobsIgnorePlayers();
        data.entityProfilesByEntity = new LinkedHashMap<String, NonConfig.EntityProfile>(config.getEntityProfilesByEntity());
        data.liquidDecayPerSecond = config.getLiquidDecayPerSecond();
        data.peakXpPerMl = config.getPeakXpPerMl();
        data.filledStageOneSpeedMult = config.getFilledStageOneSpeedMult();
        data.filledStageTwoSpeedMult = config.getFilledStageTwoSpeedMult();
        data.filledStageThreeSpeedMult = config.getFilledStageThreeSpeedMult();
        data.filledStageOneJumpMult = config.getFilledStageOneJumpMult();
        data.filledStageTwoJumpMult = config.getFilledStageTwoJumpMult();
        data.filledStageThreeJumpMult = config.getFilledStageThreeJumpMult();
        data.genderSpawnByEntity = new LinkedHashMap<String, NonConfig.GenderSpawnChances>(config.getGenderSpawnByEntity());
        data.liquidGainByEntity = new LinkedHashMap<String, Integer>(config.getLiquidGainByEntity());
        data.liquidColorByEntity = new LinkedHashMap<String, String>(config.getLiquidColorByEntity());
        data.peakStageByAnimation = new LinkedHashMap<String, Integer>(NonPeakStages.snapshotPeakStagesForSync());
        return data;
    }

    public void applyTo(NonConfig config) {
        if (config == null) {
            return;
        }
        config.setInitialEnergyMax(this.initialEnergyMax);
        config.setEnergyGainRate(this.energyGainRate);
        config.setNearAnimationEnergyGainMult(this.nearAnimationEnergyGainMult);
        config.setPlayerEnergyAuraMultLow(this.playerEnergyAuraMultLow);
        config.setPlayerEnergyAuraMultMid(this.playerEnergyAuraMultMid);
        config.setPlayerEnergyAuraMultHigh(this.playerEnergyAuraMultHigh);
        config.setDestroyedSkinAuraMultStage1(this.destroyedSkinAuraMultStage1);
        config.setDestroyedSkinAuraMultStage2(this.destroyedSkinAuraMultStage2);
        config.setDestroyedSkinAuraMultStage3(this.destroyedSkinAuraMultStage3);
        config.setDestroyedSkinAuraMultStage4(this.destroyedSkinAuraMultStage4);
        config.setNormalDamageDestroyedSkinChancePercent(this.normalDamageDestroyedSkinChancePercent);
        config.setLastDefeatedEnabled(this.lastDefeatedEnabled);
        config.setLastDefeatedEnergyThreshold(this.lastDefeatedEnergyThreshold);
        config.setLastDefeatedSearchRadius(this.lastDefeatedSearchRadius);
        config.setLastDefeatedCooldownSeconds(this.lastDefeatedCooldownSeconds);
        config.setPlayerEnergyAuraRadiusMin(this.playerEnergyAuraRadiusMin);
        config.setPlayerEnergyAuraRadiusMax(this.playerEnergyAuraRadiusMax);
        config.setPlayerEnergyAuraPulseTicks(this.playerEnergyAuraPulseTicks);
        config.setScanBudgetPerTick(this.scanBudgetPerTick);
        config.setLoopProgressSeconds(this.loopProgressSeconds);
        config.setPeakLoopProgressSeconds(this.peakLoopProgressSeconds);
        config.setGenderMaleChancePercent(this.genderMaleChancePercent);
        config.setGenderFemaleChancePercent(this.genderFemaleChancePercent);
        config.setGenderBothChancePercent(this.genderBothChancePercent);
        config.setMaleMaleChancePercent(this.maleMaleChancePercent);
        config.setFemaleFemaleChancePercent(this.femaleFemaleChancePercent);
        config.setEntityEntityChancePercent(this.entityEntityChancePercent);
        config.setMultiActorJoinChancePercent(this.multiActorJoinChancePercent);
        config.setAllowPlayerGenderChangeAnytime(this.allowPlayerGenderChangeAnytime);
        config.setRequirePlayerGenderSelectionOnJoin(this.requirePlayerGenderSelectionOnJoin);
        config.setAllowedStartingGenderMask(this.allowedStartingGenderMask);
        config.setPregnancyEnabled(this.pregnancyEnabled);
        config.setLiquidTankEnabled(this.liquidTankEnabled);
        config.setMessSystemEnabled(this.messSystemEnabled);
        config.setDestroyedSkinSystemEnabled(this.destroyedSkinSystemEnabled);
        config.setCraftingTableSkinRepairAllowed(this.allowCraftingTableSkinRepair);
        config.setKeepMessAfterDeath(this.keepMessAfterDeath);
        config.setKeepRippedSkinAfterDeath(this.keepRippedSkinAfterDeath);
        config.setKeepLiquidTankAfterDeath(this.keepLiquidTankAfterDeath);
        config.setKeepPregnancyAfterDeath(this.keepPregnancyAfterDeath);
        config.setLiquidTankCapacityMl(this.liquidTankCapacityMl);
        config.setVanillaOverridesEnabled(this.vanillaOverridesEnabled);
        config.setUseAnimationBreeding(this.useAnimationBreeding);
        config.setRequireMaleFemaleForBreeding(this.requireMaleFemaleForBreeding);
        config.setFemaleCowOnlyMilking(this.femaleCowOnlyMilking);
        config.setAttackCreativePlayers(this.attackCreativePlayers);
        config.setConvertMaleOnlyVInjectionsToA(this.convertMaleOnlyVInjectionsToA);
        config.setAttackEscapeHits(this.attackEscapeHits);
        config.setAttackDecayPerSecond(this.attackDecayPerSecond);
        config.setAttackEscapeDamageDifficultyPercent(this.attackEscapeDamageDifficultyPercent);
        config.setAttackEscapeInvulnerabilitySeconds(this.attackEscapeInvulnerabilitySeconds);
        config.setAttackEscapeAnimationProtectionSeconds(this.attackEscapeAnimationProtectionSeconds);
        config.setPostEscapeGatherMaxMobs(this.postEscapeGatherMaxMobs);
        config.setBlockAnimationsWhileRidingLivingEntities(this.blockAnimationsWhileRidingLivingEntities);
        config.setBlockAnimationsWhileRidingVehicles(this.blockAnimationsWhileRidingVehicles);
        config.setAttackOutcomeFailsafeThreshold(this.attackOutcomeFailsafeThreshold);
        config.setPregnancyChancePercent(this.pregnancyChancePercent);
        config.setPregnancyDurationMinutes(this.pregnancyDurationMinutes);
        config.setPregnancyChanceByEntity(HostConfigSyncData.copyIntMap(this.pregnancyChanceByEntity));
        config.setPregnancyOffspringCountByEntity(HostConfigSyncData.copyOffspringCountMap(this.pregnancyOffspringCountByEntity));
        config.setPregnancyEggDefaultHatchSeconds(this.pregnancyEggDefaultHatchSeconds);
        config.setPregnancyEggEnabledByEntity(HostConfigSyncData.copyBoolMap(this.pregnancyEggEnabledByEntity));
        config.setPregnancyEggHatchSecondsByEntity(HostConfigSyncData.copyIntMap(this.pregnancyEggHatchSecondsByEntity));
        config.setPregnancyAutoTameMobs(this.pregnancyAutoTameMobs);
        config.setPregnancyFriendlyMobsIgnorePlayers(this.pregnancyFriendlyMobsIgnorePlayers);
        config.setEntityProfilesByEntity(HostConfigSyncData.copyEntityProfileMap(this.entityProfilesByEntity));
        config.setLiquidDecayPerSecond(this.liquidDecayPerSecond);
        config.setPeakXpPerMl(this.peakXpPerMl);
        config.setFilledStageOneSpeedMult(this.filledStageOneSpeedMult);
        config.setFilledStageTwoSpeedMult(this.filledStageTwoSpeedMult);
        config.setFilledStageThreeSpeedMult(this.filledStageThreeSpeedMult);
        config.setFilledStageOneJumpMult(this.filledStageOneJumpMult);
        config.setFilledStageTwoJumpMult(this.filledStageTwoJumpMult);
        config.setFilledStageThreeJumpMult(this.filledStageThreeJumpMult);
        config.setGenderSpawnByEntity(HostConfigSyncData.copyGenderSpawnMap(this.genderSpawnByEntity));
        config.setLiquidGainByEntity(HostConfigSyncData.copyIntMap(this.liquidGainByEntity));
        config.setLiquidColorByEntity(HostConfigSyncData.copyStringMap(this.liquidColorByEntity));
        NonPeakStages.applySyncedPeakStages(HostConfigSyncData.copyIntMap(this.peakStageByAnimation));
    }

    private static Map<String, NonConfig.GenderSpawnChances> copyGenderSpawnMap(Map<String, NonConfig.GenderSpawnChances> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        return new LinkedHashMap<String, NonConfig.GenderSpawnChances>(source);
    }

    private static Map<String, NonConfig.EntityProfile> copyEntityProfileMap(Map<String, NonConfig.EntityProfile> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        return new LinkedHashMap<String, NonConfig.EntityProfile>(source);
    }

    private static Map<String, NonConfig.OffspringCountRange> copyOffspringCountMap(Map<String, NonConfig.OffspringCountRange> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        return new LinkedHashMap<String, NonConfig.OffspringCountRange>(source);
    }

    private static Map<String, Integer> copyIntMap(Map<String, Integer> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        return new LinkedHashMap<String, Integer>(source);
    }

    private static Map<String, Boolean> copyBoolMap(Map<String, Boolean> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        return new LinkedHashMap<String, Boolean>(source);
    }

    private static Map<String, String> copyStringMap(Map<String, String> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        return new LinkedHashMap<String, String>(source);
    }
}

