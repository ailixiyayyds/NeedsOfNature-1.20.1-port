/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.util.Identifier
 */
package com.nonid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nonid.NeedsOfNature;
import com.nonid.NonDebugChatCategory;
import com.nonid.NonDebugChatMode;
import com.nonid.client.NonLiquidColors;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public final class NonConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("needsofnature.json");
    private static final int PREGNANCY_DURATION_MINUTES_MIN = 1;
    private static final int PREGNANCY_DURATION_MINUTES_MAX = 5256000;
    public static final int DEFAULT_GENDER_MALE_CHANCE_PERCENT = 48;
    public static final int DEFAULT_GENDER_FEMALE_CHANCE_PERCENT = 47;
    public static final int DEFAULT_GENDER_BOTH_CHANCE_PERCENT = 5;
    public static final int DEFAULT_LIQUID_TANK_CAPACITY_ML = 200;
    public static final int LIQUID_TANK_CAPACITY_MIN_ML = 1;
    public static final int LIQUID_TANK_CAPACITY_MAX_ML = 10000;
    public static final int DEFAULT_ATTACK_OUTCOME_FAILSAFE_THRESHOLD = 3;
    public static final int DEFAULT_ACTION_SOUND_VOLUME_PERCENT = 100;
    public static final double DEFAULT_PEAK_XP_PER_ML = 0.06666666666666667;
    public static final int DEFAULT_NORMAL_DAMAGE_DESTROYED_SKIN_CHANCE_PERCENT = 10;
    public static final int DEFAULT_LAST_DEFEATED_ENERGY_THRESHOLD = 70;
    public static final int DEFAULT_LAST_DEFEATED_SEARCH_RADIUS = 6;
    public static final int DEFAULT_LAST_DEFEATED_COOLDOWN_SECONDS = 120;
    public static final int DEFAULT_ATTACK_ESCAPE_INVULNERABILITY_SECONDS = 3;
    public static final int DEFAULT_ATTACK_ESCAPE_ANIMATION_PROTECTION_SECONDS = 3;
    public static final int ATTACK_ESCAPE_PROTECTION_SECONDS_MAX = 10;
    public static final int DEFAULT_ATTACK_ESCAPE_DAMAGE_DIFFICULTY_PERCENT = 100;
    public static final int ATTACK_ESCAPE_DAMAGE_DIFFICULTY_PERCENT_MAX = 200;
    public static final int DEFAULT_POST_ESCAPE_GATHER_MAX_MOBS = 8;
    public static final boolean DEFAULT_BLOCK_ANIMATIONS_WHILE_RIDING_LIVING_ENTITIES = true;
    public static final boolean DEFAULT_BLOCK_ANIMATIONS_WHILE_RIDING_VEHICLES = false;
    public static final int PREGNANCY_OFFSPRING_COUNT_MIN = 1;
    public static final int PREGNANCY_OFFSPRING_COUNT_MAX = 16;
    public static final float DEFAULT_PREGNANCY_EGG_START_SIZE = 0.5f;
    public static final float DEFAULT_PREGNANCY_EGG_END_SIZE = 1.0f;
    public static final float DEFAULT_PREGNANCY_EGG_HEALTH = 2.0f;
    public static final float PREGNANCY_EGG_SIZE_MIN = 0.05f;
    public static final float PREGNANCY_EGG_SIZE_MAX = 4.0f;
    public static final float PREGNANCY_EGG_HEALTH_MIN = 0.5f;
    public static final float PREGNANCY_EGG_HEALTH_MAX = 100.0f;
    public static final String PREGNANCY_BIRTH_MODE_EGG = "egg";
    public static final String PREGNANCY_BIRTH_MODE_DIRECT = "direct";
    public static final String DEFAULT_DESTROYED_SKIN_TINT_HEX = "";
    public static final String DEFAULT_INTIFACE_SERVER_URL = "ws://127.0.0.1:12345";
    public static final int DEFAULT_INTIFACE_REACTIVE_IMPACT_STRENGTH_PERCENT = 60;
    public static final int DEFAULT_INTIFACE_REACTIVE_IMPACT_DURATION_MS = 170;
    public static final int DEFAULT_INTIFACE_PEAK_REACTIVE_IMPACT_STRENGTH_PERCENT = 20;
    public static final int DEFAULT_INTIFACE_PEAK_REACTIVE_IMPACT_DURATION_MS = 500;
    public static final int DEFAULT_INTIFACE_COOLDOWN_MS = 80;
    public static final int DEFAULT_INTIFACE_ANIMATION_BASELINE_STRENGTH_PERCENT = 1;
    public static final int DEFAULT_INTIFACE_OSCILLATOR_REGULAR_SPEED_PERCENT = 100;
    public static final int DEFAULT_INTIFACE_OSCILLATOR_PEAK_SPEED_PERCENT = 50;
    public static final int DEFAULT_INTIFACE_STROKER_MIN_DISTANCE_PERCENT = 0;
    public static final int DEFAULT_INTIFACE_STROKER_MAX_DISTANCE_PERCENT = 100;
    public static final int DEFAULT_INTIFACE_STROKER_REGULAR_MOVE_DURATION_MS = 500;
    public static final int DEFAULT_INTIFACE_STROKER_PEAK_MOVE_DURATION_MS = 300;
    public static final int DEFAULT_INTIFACE_ENERGIZED_ONE_BASE_PERCENT = 1;
    public static final int DEFAULT_INTIFACE_ENERGIZED_ONE_PULSE_PERCENT = 5;
    public static final int DEFAULT_INTIFACE_ENERGIZED_TWO_BASE_PERCENT = 5;
    public static final int DEFAULT_INTIFACE_ENERGIZED_TWO_PULSE_PERCENT = 10;
    public static final int DEFAULT_INTIFACE_ENERGIZED_THREE_BASE_PERCENT = 10;
    public static final int DEFAULT_INTIFACE_ENERGIZED_THREE_PULSE_PERCENT = 20;
    private int initialEnergyMax = 90;
    private double energyGainRate = 1.0;
    private double nearAnimationEnergyGainMult = 3.0;
    private double playerEnergyAuraMultLow = 2.0;
    private double playerEnergyAuraMultMid = 5.0;
    private double playerEnergyAuraMultHigh = 10.0;
    private double destroyedSkinAuraMultStage1 = 2.0;
    private double destroyedSkinAuraMultStage2 = 5.0;
    private double destroyedSkinAuraMultStage3 = 20.0;
    private double destroyedSkinAuraMultStage4 = 50.0;
    private int normalDamageDestroyedSkinChancePercent = 10;
    private boolean lastDefeatedEnabled = true;
    private int lastDefeatedEnergyThreshold = 70;
    private int lastDefeatedSearchRadius = 6;
    private int lastDefeatedCooldownSeconds = 120;
    private int playerEnergyAuraRadiusMin = 6;
    private int playerEnergyAuraRadiusMax = 20;
    private int playerEnergyAuraPulseTicks = 20;
    private String debugChatMode = null;
    private int scanBudgetPerTick = 32;
    private int loopProgressSeconds = 15;
    private int peakLoopProgressSeconds = 7;
    private int genderMaleChancePercent = 48;
    private int genderFemaleChancePercent = 47;
    private int genderBothChancePercent = 5;
    private int maleMaleChancePercent = 20;
    private int femaleFemaleChancePercent = 20;
    private int entityEntityChancePercent = 20;
    private int multiActorJoinChancePercent = 75;
    private String playerGender = PlayerGenderSelection.FEMALE.id();
    private boolean allowPlayerGenderChangeAnytime = true;
    private boolean requirePlayerGenderSelectionOnJoin = false;
    private int allowedStartingGenderMask = 7;
    private boolean pregnancyEnabled = true;
    private boolean liquidTankEnabled = true;
    private boolean messSystemEnabled = true;
    private boolean destroyedSkinSystemEnabled = true;
    private boolean allowCraftingTableSkinRepair = false;
    private boolean keepMessAfterDeath = false;
    private boolean keepRippedSkinAfterDeath = false;
    private boolean keepLiquidTankAfterDeath = false;
    private boolean keepPregnancyAfterDeath = false;
    private boolean armorShedEffectEnabled = true;
    private boolean syncFemaleGenderModWithNonGender = true;
    private boolean destroyedSkinFemaleGenderOverridesEnabled = true;
    private FemaleGenderDestroyedProfile femaleGenderDestroyedLow = FemaleGenderDestroyedProfile.lowDefault();
    private FemaleGenderDestroyedProfile femaleGenderDestroyedHigh = FemaleGenderDestroyedProfile.highDefault();
    private int actionSoundVolumePercent = 100;
    private boolean intifaceEnabled = false;
    private boolean intifaceAutoConnectOnWorldJoin = true;
    private boolean intifaceStopOnDisconnect = true;
    private String intifaceServerUrl = "ws://127.0.0.1:12345";
    private int intifaceReactiveImpactStrengthPercent = 60;
    private int intifaceReactiveImpactDurationMs = 170;
    private int intifacePeakReactiveImpactStrengthPercent = 20;
    private int intifacePeakReactiveImpactDurationMs = 500;
    private int intifaceCooldownMs = 80;
    private int intifaceAnimationBaselineStrengthPercent = 1;
    private int intifaceOscillatorRegularSpeedPercent = 100;
    private int intifaceOscillatorPeakSpeedPercent = 50;
    private int intifaceStrokerMinDistancePercent = 0;
    private int intifaceStrokerMaxDistancePercent = 100;
    private int intifaceStrokerRegularMoveDurationMs = 500;
    private int intifaceStrokerPeakMoveDurationMs = 300;
    private int intifaceEnergizedOneBasePercent = 1;
    private int intifaceEnergizedOnePulsePercent = 5;
    private int intifaceEnergizedTwoBasePercent = 5;
    private int intifaceEnergizedTwoPulsePercent = 10;
    private int intifaceEnergizedThreeBasePercent = 10;
    private int intifaceEnergizedThreePulsePercent = 20;
    private int liquidTankCapacityMl = 200;
    private boolean vanillaOverridesEnabled = true;
    private boolean useAnimationBreeding = true;
    private boolean requireMaleFemaleForBreeding = true;
    private boolean femaleCowOnlyMilking = true;
    private boolean attackCreativePlayers = false;
    private boolean convertMaleOnlyVInjectionsToA = true;
    private int attackEscapeHits = 12;
    private double attackDecayPerSecond = 0.2;
    private int attackEscapeDamageDifficultyPercent = 100;
    private Integer attackEscapeInvulnerabilityTicks = null;
    private int attackEscapeInvulnerabilitySeconds = 3;
    private int attackEscapeAnimationProtectionSeconds = 3;
    private int postEscapeGatherMaxMobs = 8;
    private boolean blockAnimationsWhileRidingLivingEntities = true;
    private boolean blockAnimationsWhileRidingVehicles = false;
    private int attackOutcomeFailsafeThreshold = 3;
    private int pregnancyChancePercent = 5;
    private int pregnancyDurationMinutes = 3;
    private Map<String, Integer> pregnancyChanceByEntity = new LinkedHashMap<String, Integer>();
    private Map<String, OffspringCountRange> pregnancyOffspringCountByEntity = new LinkedHashMap<String, OffspringCountRange>();
    private int pregnancyEggDefaultHatchSeconds = 60;
    private Map<String, Boolean> pregnancyEggEnabledByEntity = new LinkedHashMap<String, Boolean>();
    private Map<String, Integer> pregnancyEggHatchSecondsByEntity = new LinkedHashMap<String, Integer>();
    private boolean pregnancyAutoTameMobs = true;
    private boolean pregnancyFriendlyMobsIgnorePlayers = false;
    private Map<String, EntityProfile> entityProfilesByEntity = new LinkedHashMap<String, EntityProfile>();
    private double liquidDecayPerSecond = 1.0;
    private int liquidPuddleDespawnSeconds = 40;
    private double peakXpPerMl = 0.06666666666666667;
    private float filledStageOneSpeedMult = 0.8f;
    private float filledStageTwoSpeedMult = 0.7f;
    private float filledStageThreeSpeedMult = 0.6f;
    private float filledStageOneJumpMult = 0.9f;
    private float filledStageTwoJumpMult = 0.75f;
    private float filledStageThreeJumpMult = 0.6f;
    private int uiEnergyOffsetX = 0;
    private int uiEnergyOffsetY = 0;
    private int uiEnergyHeartOffsetX = 0;
    private int uiEnergyHeartOffsetY = 0;
    private int uiAttackOffsetX = 0;
    private int uiAttackOffsetY = 0;
    private int uiPromptOffsetX = 0;
    private int uiPromptOffsetY = 0;
    private int uiLiquidOffsetX = 0;
    private int uiLiquidOffsetY = 0;
    private boolean uiEnergyVisible = true;
    private boolean uiEnergyHeartVisible = true;
    private boolean uiAttackVisible = true;
    private boolean uiPromptVisible = true;
    private boolean uiLiquidVisible = true;
    private boolean debugSpinMode = false;
    private String destroyedSkinTintHex = "";
    private Map<String, GenderSpawnChances> genderSpawnByEntity = new LinkedHashMap<String, GenderSpawnChances>();
    private Map<String, Integer> liquidGainByEntity = new LinkedHashMap<String, Integer>();
    private Map<String, String> liquidColorByEntity = new LinkedHashMap<String, String>();
    private List<String> externalNoNPackLoadOrder = new ArrayList<String>();
    private List<String> disabledAnimationPacks = new ArrayList<String>();
    private List<String> disabledAnimations = new ArrayList<String>();
    private List<String> acknowledgedStartupWarnings = new ArrayList<String>();

    public int getInitialEnergyMax() {
        return Math.max(0, Math.min(200, this.initialEnergyMax));
    }

    public void setInitialEnergyMax(int value) {
        this.initialEnergyMax = Math.max(0, Math.min(200, value));
    }

    public double getEnergyGainRate() {
        return Math.max(0.1, Math.min(10.0, this.energyGainRate));
    }

    public void setEnergyGainRate(double value) {
        this.energyGainRate = Math.max(0.1, Math.min(10.0, value));
    }

    public int getActionSoundVolumePercent() {
        return Math.max(0, Math.min(200, this.actionSoundVolumePercent));
    }

    public float getActionSoundVolumeMultiplier() {
        return (float)this.getActionSoundVolumePercent() / 100.0f;
    }

    public void setActionSoundVolumePercent(int value) {
        this.actionSoundVolumePercent = Math.max(0, Math.min(200, value));
    }

    public boolean isIntifaceEnabled() {
        return this.intifaceEnabled;
    }

    public void setIntifaceEnabled(boolean value) {
        this.intifaceEnabled = value;
    }

    public boolean isIntifaceAutoConnectOnWorldJoin() {
        return this.intifaceAutoConnectOnWorldJoin;
    }

    public void setIntifaceAutoConnectOnWorldJoin(boolean value) {
        this.intifaceAutoConnectOnWorldJoin = value;
    }

    public boolean isIntifaceStopOnDisconnect() {
        return this.intifaceStopOnDisconnect;
    }

    public void setIntifaceStopOnDisconnect(boolean value) {
        this.intifaceStopOnDisconnect = value;
    }

    public String getIntifaceServerUrl() {
        return NonConfig.sanitizeIntifaceServerUrl(this.intifaceServerUrl);
    }

    public void setIntifaceServerUrl(String value) {
        this.intifaceServerUrl = NonConfig.sanitizeIntifaceServerUrl(value);
    }

    public int getIntifaceReactiveImpactStrengthPercent() {
        return NonConfig.clampPercent(this.intifaceReactiveImpactStrengthPercent);
    }

    public void setIntifaceReactiveImpactStrengthPercent(int value) {
        this.intifaceReactiveImpactStrengthPercent = NonConfig.clampPercent(value);
    }

    public int getIntifaceReactiveImpactDurationMs() {
        return NonConfig.clampInt(NonConfig.valueOrDefault(this.intifaceReactiveImpactDurationMs, 170), 20, 5000);
    }

    public void setIntifaceReactiveImpactDurationMs(int value) {
        this.intifaceReactiveImpactDurationMs = NonConfig.clampInt(value, 20, 5000);
    }

    public int getIntifacePeakReactiveImpactStrengthPercent() {
        return NonConfig.clampPercent(this.intifacePeakReactiveImpactStrengthPercent);
    }

    public void setIntifacePeakReactiveImpactStrengthPercent(int value) {
        this.intifacePeakReactiveImpactStrengthPercent = NonConfig.clampPercent(value);
    }

    public int getIntifacePeakReactiveImpactDurationMs() {
        return NonConfig.clampInt(NonConfig.valueOrDefault(this.intifacePeakReactiveImpactDurationMs, 500), 20, 5000);
    }

    public void setIntifacePeakReactiveImpactDurationMs(int value) {
        this.intifacePeakReactiveImpactDurationMs = NonConfig.clampInt(value, 20, 5000);
    }

    public int getIntifaceCooldownMs() {
        return NonConfig.clampInt(this.intifaceCooldownMs, 0, 5000);
    }

    public void setIntifaceCooldownMs(int value) {
        this.intifaceCooldownMs = NonConfig.clampInt(value, 0, 5000);
    }

    public int getIntifaceAnimationBaselineStrengthPercent() {
        return NonConfig.clampPercent(this.intifaceAnimationBaselineStrengthPercent);
    }

    public void setIntifaceAnimationBaselineStrengthPercent(int value) {
        this.intifaceAnimationBaselineStrengthPercent = NonConfig.clampPercent(value);
    }

    public int getIntifaceOscillatorRegularSpeedPercent() {
        return NonConfig.clampPercent(this.intifaceOscillatorRegularSpeedPercent);
    }

    public void setIntifaceOscillatorRegularSpeedPercent(int value) {
        this.intifaceOscillatorRegularSpeedPercent = NonConfig.clampPercent(value);
    }

    public int getIntifaceOscillatorPeakSpeedPercent() {
        return NonConfig.clampPercent(this.intifaceOscillatorPeakSpeedPercent);
    }

    public void setIntifaceOscillatorPeakSpeedPercent(int value) {
        this.intifaceOscillatorPeakSpeedPercent = NonConfig.clampPercent(value);
    }

    public int getIntifaceStrokerMinDistancePercent() {
        return NonConfig.clampPercent(this.intifaceStrokerMinDistancePercent);
    }

    public void setIntifaceStrokerMinDistancePercent(int value) {
        this.intifaceStrokerMinDistancePercent = Math.min(NonConfig.clampPercent(value), this.getIntifaceStrokerMaxDistancePercent());
    }

    public int getIntifaceStrokerMaxDistancePercent() {
        return NonConfig.clampPercent(this.intifaceStrokerMaxDistancePercent);
    }

    public void setIntifaceStrokerMaxDistancePercent(int value) {
        this.intifaceStrokerMaxDistancePercent = Math.max(NonConfig.clampPercent(value), this.getIntifaceStrokerMinDistancePercent());
    }

    public void setIntifaceStrokerDistancesPercent(int minValue, int maxValue) {
        int min = NonConfig.clampPercent(Math.min(minValue, maxValue));
        int max = NonConfig.clampPercent(Math.max(minValue, maxValue));
        this.intifaceStrokerMinDistancePercent = min;
        this.intifaceStrokerMaxDistancePercent = max;
    }

    public int getIntifaceStrokerRegularMoveDurationMs() {
        return NonConfig.clampInt(this.intifaceStrokerRegularMoveDurationMs, 0, 10000);
    }

    public void setIntifaceStrokerRegularMoveDurationMs(int value) {
        this.intifaceStrokerRegularMoveDurationMs = NonConfig.clampInt(value, 0, 10000);
    }

    public int getIntifaceStrokerPeakMoveDurationMs() {
        return NonConfig.clampInt(this.intifaceStrokerPeakMoveDurationMs, 0, 10000);
    }

    public void setIntifaceStrokerPeakMoveDurationMs(int value) {
        this.intifaceStrokerPeakMoveDurationMs = NonConfig.clampInt(value, 0, 10000);
    }

    public int getIntifaceEnergizedBasePercent(int level) {
        return switch (Math.max(1, Math.min(3, level))) {
            case 1 -> NonConfig.clampPercent(this.intifaceEnergizedOneBasePercent);
            case 2 -> NonConfig.clampPercent(this.intifaceEnergizedTwoBasePercent);
            default -> NonConfig.clampPercent(this.intifaceEnergizedThreeBasePercent);
        };
    }

    public void setIntifaceEnergizedBasePercent(int level, int value) {
        int clamped = NonConfig.clampPercent(value);
        switch (Math.max(1, Math.min(3, level))) {
            case 1: {
                this.intifaceEnergizedOneBasePercent = clamped;
                break;
            }
            case 2: {
                this.intifaceEnergizedTwoBasePercent = clamped;
                break;
            }
            default: {
                this.intifaceEnergizedThreeBasePercent = clamped;
            }
        }
    }

    public int getIntifaceEnergizedPulsePercent(int level) {
        return switch (Math.max(1, Math.min(3, level))) {
            case 1 -> NonConfig.clampPercent(this.intifaceEnergizedOnePulsePercent);
            case 2 -> NonConfig.clampPercent(this.intifaceEnergizedTwoPulsePercent);
            default -> NonConfig.clampPercent(this.intifaceEnergizedThreePulsePercent);
        };
    }

    public void setIntifaceEnergizedPulsePercent(int level, int value) {
        int clamped = NonConfig.clampPercent(value);
        switch (Math.max(1, Math.min(3, level))) {
            case 1: {
                this.intifaceEnergizedOnePulsePercent = clamped;
                break;
            }
            case 2: {
                this.intifaceEnergizedTwoPulsePercent = clamped;
                break;
            }
            default: {
                this.intifaceEnergizedThreePulsePercent = clamped;
            }
        }
    }

    public double getNearAnimationEnergyGainMult() {
        return Math.max(1.0, Math.min(10.0, this.nearAnimationEnergyGainMult));
    }

    public void setNearAnimationEnergyGainMult(double value) {
        this.nearAnimationEnergyGainMult = Math.max(1.0, Math.min(10.0, value));
    }

    public double getPlayerEnergyAuraMultLow() {
        return Math.max(1.0, Math.min(20.0, this.playerEnergyAuraMultLow));
    }

    public void setPlayerEnergyAuraMultLow(double value) {
        this.playerEnergyAuraMultLow = Math.max(1.0, Math.min(20.0, value));
    }

    public double getPlayerEnergyAuraMultMid() {
        return Math.max(1.0, Math.min(20.0, this.playerEnergyAuraMultMid));
    }

    public void setPlayerEnergyAuraMultMid(double value) {
        this.playerEnergyAuraMultMid = Math.max(1.0, Math.min(20.0, value));
    }

    public double getPlayerEnergyAuraMultHigh() {
        return Math.max(1.0, Math.min(20.0, this.playerEnergyAuraMultHigh));
    }

    public void setPlayerEnergyAuraMultHigh(double value) {
        this.playerEnergyAuraMultHigh = Math.max(1.0, Math.min(20.0, value));
    }

    public double getDestroyedSkinAuraMultStage1() {
        return this.clampDestroyedSkinAuraMult(this.destroyedSkinAuraMultStage1);
    }

    public void setDestroyedSkinAuraMultStage1(double value) {
        this.destroyedSkinAuraMultStage1 = this.clampDestroyedSkinAuraMult(value);
    }

    public double getDestroyedSkinAuraMultStage2() {
        return this.clampDestroyedSkinAuraMult(this.destroyedSkinAuraMultStage2);
    }

    public void setDestroyedSkinAuraMultStage2(double value) {
        this.destroyedSkinAuraMultStage2 = this.clampDestroyedSkinAuraMult(value);
    }

    public double getDestroyedSkinAuraMultStage3() {
        return this.clampDestroyedSkinAuraMult(this.destroyedSkinAuraMultStage3);
    }

    public void setDestroyedSkinAuraMultStage3(double value) {
        this.destroyedSkinAuraMultStage3 = this.clampDestroyedSkinAuraMult(value);
    }

    public double getDestroyedSkinAuraMultStage4() {
        return this.clampDestroyedSkinAuraMult(this.destroyedSkinAuraMultStage4);
    }

    public void setDestroyedSkinAuraMultStage4(double value) {
        this.destroyedSkinAuraMultStage4 = this.clampDestroyedSkinAuraMult(value);
    }

    private double clampDestroyedSkinAuraMult(double value) {
        return Math.max(1.0, Math.min(50.0, value));
    }

    public int getNormalDamageDestroyedSkinChancePercent() {
        return NonConfig.clampPercent(this.normalDamageDestroyedSkinChancePercent);
    }

    public void setNormalDamageDestroyedSkinChancePercent(int value) {
        this.normalDamageDestroyedSkinChancePercent = NonConfig.clampPercent(value);
    }

    public boolean isLastDefeatedEnabled() {
        return this.lastDefeatedEnabled;
    }

    public void setLastDefeatedEnabled(boolean value) {
        this.lastDefeatedEnabled = value;
    }

    public int getLastDefeatedEnergyThreshold() {
        return Math.max(0, Math.min(200, this.lastDefeatedEnergyThreshold));
    }

    public void setLastDefeatedEnergyThreshold(int value) {
        this.lastDefeatedEnergyThreshold = Math.max(0, Math.min(200, value));
    }

    public int getLastDefeatedSearchRadius() {
        return Math.max(1, Math.min(32, this.lastDefeatedSearchRadius));
    }

    public void setLastDefeatedSearchRadius(int value) {
        this.lastDefeatedSearchRadius = Math.max(1, Math.min(32, value));
    }

    public int getLastDefeatedCooldownSeconds() {
        return Math.max(0, Math.min(3600, this.lastDefeatedCooldownSeconds));
    }

    public void setLastDefeatedCooldownSeconds(int value) {
        this.lastDefeatedCooldownSeconds = Math.max(0, Math.min(3600, value));
    }

    public int getPlayerEnergyAuraRadiusMin() {
        return Math.max(1, Math.min(64, this.playerEnergyAuraRadiusMin));
    }

    public void setPlayerEnergyAuraRadiusMin(int value) {
        this.playerEnergyAuraRadiusMin = Math.max(1, Math.min(64, value));
    }

    public int getPlayerEnergyAuraRadiusMax() {
        return Math.max(this.getPlayerEnergyAuraRadiusMin(), Math.min(64, this.playerEnergyAuraRadiusMax));
    }

    public void setPlayerEnergyAuraRadiusMax(int value) {
        this.playerEnergyAuraRadiusMax = Math.max(1, Math.min(64, value));
    }

    public int getPlayerEnergyAuraPulseTicks() {
        return Math.max(1, Math.min(1200, this.playerEnergyAuraPulseTicks));
    }

    public void setPlayerEnergyAuraPulseTicks(int value) {
        this.playerEnergyAuraPulseTicks = Math.max(1, Math.min(1200, value));
    }

    public NonDebugChatMode debugChatMode() {
        return NonDebugChatMode.fromId(this.debugChatMode, NonDebugChatMode.SETUP_ERRORS);
    }

    public void setDebugChatMode(NonDebugChatMode mode) {
        NonDebugChatMode resolved = mode == null ? NonDebugChatMode.SETUP_ERRORS : mode;
        this.debugChatMode = resolved.id();
    }

    public boolean allowsDebugChat(NonDebugChatCategory category) {
        return this.debugChatMode().allows(category);
    }

    public int getScanBudgetPerTick() {
        return Math.max(1, Math.min(512, this.scanBudgetPerTick));
    }

    public void setScanBudgetPerTick(int value) {
        this.scanBudgetPerTick = Math.max(1, Math.min(512, value));
    }

    public int getLoopProgressSeconds() {
        return Math.max(5, Math.min(300, this.loopProgressSeconds));
    }

    public void setLoopProgressSeconds(int seconds) {
        this.loopProgressSeconds = Math.max(5, Math.min(300, seconds));
    }

    public int getPeakLoopProgressSeconds() {
        return Math.max(1, Math.min(300, this.peakLoopProgressSeconds));
    }

    public void setPeakLoopProgressSeconds(int seconds) {
        this.peakLoopProgressSeconds = Math.max(1, Math.min(300, seconds));
    }

    public int getGenderMaleChancePercent() {
        return NonConfig.clampPercent(this.genderMaleChancePercent);
    }

    public void setGenderMaleChancePercent(int percent) {
        this.genderMaleChancePercent = NonConfig.clampPercent(percent);
    }

    public int getGenderFemaleChancePercent() {
        return NonConfig.clampPercent(this.genderFemaleChancePercent);
    }

    public void setGenderFemaleChancePercent(int percent) {
        this.genderFemaleChancePercent = NonConfig.clampPercent(percent);
    }

    public int getGenderBothChancePercent() {
        return NonConfig.clampPercent(this.genderBothChancePercent);
    }

    public void setGenderBothChancePercent(int percent) {
        this.genderBothChancePercent = NonConfig.clampPercent(percent);
    }

    public GenderSpawnChances getGlobalGenderSpawnChances() {
        GenderSpawnChances chances = new GenderSpawnChances(this.getGenderMaleChancePercent(), this.getGenderFemaleChancePercent(), this.getGenderBothChancePercent());
        return chances.isValid() ? chances : NonConfig.defaultGenderSpawnChances();
    }

    public void setGlobalGenderSpawnChances(GenderSpawnChances chances) {
        GenderSpawnChances safe = NonConfig.validOrDefault(chances);
        this.setGenderMaleChancePercent(safe.male());
        this.setGenderFemaleChancePercent(safe.female());
        this.setGenderBothChancePercent(safe.both());
    }

    public int getMaleMaleChancePercent() {
        return Math.max(0, Math.min(100, this.maleMaleChancePercent));
    }

    public void setMaleMaleChancePercent(int percent) {
        this.maleMaleChancePercent = Math.max(0, Math.min(100, percent));
    }

    public int getFemaleFemaleChancePercent() {
        return Math.max(0, Math.min(100, this.femaleFemaleChancePercent));
    }

    public void setFemaleFemaleChancePercent(int percent) {
        this.femaleFemaleChancePercent = Math.max(0, Math.min(100, percent));
    }

    public int getEntityEntityChancePercent() {
        return Math.max(0, Math.min(100, this.entityEntityChancePercent));
    }

    public void setEntityEntityChancePercent(int percent) {
        this.entityEntityChancePercent = Math.max(0, Math.min(100, percent));
    }

    public int getMultiActorJoinChancePercent() {
        return Math.max(0, Math.min(100, this.multiActorJoinChancePercent));
    }

    public void setMultiActorJoinChancePercent(int percent) {
        this.multiActorJoinChancePercent = Math.max(0, Math.min(100, percent));
    }

    public PlayerGenderSelection getPlayerGenderSelection() {
        return PlayerGenderSelection.fromId(this.playerGender, PlayerGenderSelection.FEMALE);
    }

    public void setPlayerGenderSelection(PlayerGenderSelection selection) {
        PlayerGenderSelection resolved = selection == null ? PlayerGenderSelection.FEMALE : selection;
        this.playerGender = resolved.id();
    }

    public int getPlayerGenderMask() {
        return this.getPlayerGenderSelection().mask();
    }

    public boolean allowPlayerGenderChangeAnytime() {
        return this.allowPlayerGenderChangeAnytime;
    }

    public void setAllowPlayerGenderChangeAnytime(boolean value) {
        this.allowPlayerGenderChangeAnytime = value;
    }

    public boolean requirePlayerGenderSelectionOnJoin() {
        return this.requirePlayerGenderSelectionOnJoin;
    }

    public void setRequirePlayerGenderSelectionOnJoin(boolean value) {
        this.requirePlayerGenderSelectionOnJoin = value;
    }

    public int getAllowedStartingGenderMask() {
        int mask = this.allowedStartingGenderMask & 7;
        return mask == 0 ? 7 : mask;
    }

    public void setAllowedStartingGenderMask(int mask) {
        int normalized = mask & 7;
        this.allowedStartingGenderMask = normalized == 0 ? 7 : normalized;
    }

    public boolean isPregnancyEnabled() {
        return this.pregnancyEnabled;
    }

    public void setPregnancyEnabled(boolean value) {
        this.pregnancyEnabled = value;
    }

    public boolean isLiquidTankEnabled() {
        return this.liquidTankEnabled;
    }

    public void setLiquidTankEnabled(boolean value) {
        this.liquidTankEnabled = value;
    }

    public boolean isMessSystemEnabled() {
        return this.messSystemEnabled;
    }

    public void setMessSystemEnabled(boolean value) {
        this.messSystemEnabled = value;
    }

    public boolean isDestroyedSkinSystemEnabled() {
        return this.destroyedSkinSystemEnabled;
    }

    public void setDestroyedSkinSystemEnabled(boolean value) {
        this.destroyedSkinSystemEnabled = value;
    }

    public boolean isCraftingTableSkinRepairAllowed() {
        return this.allowCraftingTableSkinRepair;
    }

    public void setCraftingTableSkinRepairAllowed(boolean value) {
        this.allowCraftingTableSkinRepair = value;
    }

    public boolean keepMessAfterDeath() {
        return this.keepMessAfterDeath;
    }

    public void setKeepMessAfterDeath(boolean value) {
        this.keepMessAfterDeath = value;
    }

    public boolean keepRippedSkinAfterDeath() {
        return this.keepRippedSkinAfterDeath;
    }

    public void setKeepRippedSkinAfterDeath(boolean value) {
        this.keepRippedSkinAfterDeath = value;
    }

    public boolean keepLiquidTankAfterDeath() {
        return this.keepLiquidTankAfterDeath;
    }

    public void setKeepLiquidTankAfterDeath(boolean value) {
        this.keepLiquidTankAfterDeath = value;
    }

    public boolean keepPregnancyAfterDeath() {
        return this.keepPregnancyAfterDeath;
    }

    public void setKeepPregnancyAfterDeath(boolean value) {
        this.keepPregnancyAfterDeath = value;
    }

    public boolean isArmorShedEffectEnabled() {
        return this.armorShedEffectEnabled;
    }

    public void setArmorShedEffectEnabled(boolean value) {
        this.armorShedEffectEnabled = value;
    }

    public boolean isSyncFemaleGenderModWithNonGender() {
        return this.syncFemaleGenderModWithNonGender;
    }

    public void setSyncFemaleGenderModWithNonGender(boolean value) {
        this.syncFemaleGenderModWithNonGender = value;
    }

    public boolean isDestroyedSkinFemaleGenderOverridesEnabled() {
        return this.destroyedSkinFemaleGenderOverridesEnabled;
    }

    public void setDestroyedSkinFemaleGenderOverridesEnabled(boolean value) {
        this.destroyedSkinFemaleGenderOverridesEnabled = value;
    }

    public FemaleGenderDestroyedProfile getFemaleGenderDestroyedLow() {
        if (this.femaleGenderDestroyedLow == null) {
            this.femaleGenderDestroyedLow = FemaleGenderDestroyedProfile.lowDefault();
        }
        this.femaleGenderDestroyedLow = this.femaleGenderDestroyedLow.sanitized();
        return this.femaleGenderDestroyedLow;
    }

    public void setFemaleGenderDestroyedLow(FemaleGenderDestroyedProfile value) {
        this.femaleGenderDestroyedLow = value == null ? FemaleGenderDestroyedProfile.lowDefault() : value.sanitized();
    }

    public FemaleGenderDestroyedProfile getFemaleGenderDestroyedHigh() {
        if (this.femaleGenderDestroyedHigh == null) {
            this.femaleGenderDestroyedHigh = FemaleGenderDestroyedProfile.highDefault();
        }
        this.femaleGenderDestroyedHigh = this.femaleGenderDestroyedHigh.sanitized();
        return this.femaleGenderDestroyedHigh;
    }

    public void setFemaleGenderDestroyedHigh(FemaleGenderDestroyedProfile value) {
        this.femaleGenderDestroyedHigh = value == null ? FemaleGenderDestroyedProfile.highDefault() : value.sanitized();
    }

    public int getLiquidTankCapacityMl() {
        return Math.max(1, Math.min(10000, this.liquidTankCapacityMl));
    }

    public void setLiquidTankCapacityMl(int value) {
        this.liquidTankCapacityMl = Math.max(1, Math.min(10000, value));
    }

    public boolean isVanillaOverridesEnabled() {
        return this.vanillaOverridesEnabled;
    }

    public void setVanillaOverridesEnabled(boolean value) {
        this.vanillaOverridesEnabled = value;
    }

    public boolean useAnimationBreeding() {
        return this.vanillaOverridesEnabled && this.useAnimationBreeding;
    }

    public void setUseAnimationBreeding(boolean value) {
        this.useAnimationBreeding = value;
    }

    public boolean requireMaleFemaleForBreeding() {
        return this.vanillaOverridesEnabled && this.requireMaleFemaleForBreeding;
    }

    public void setRequireMaleFemaleForBreeding(boolean value) {
        this.requireMaleFemaleForBreeding = value;
    }

    public boolean femaleCowOnlyMilking() {
        return this.vanillaOverridesEnabled && this.femaleCowOnlyMilking;
    }

    public void setFemaleCowOnlyMilking(boolean value) {
        this.femaleCowOnlyMilking = value;
    }

    public boolean canAttackCreativePlayers() {
        return this.attackCreativePlayers;
    }

    public void setAttackCreativePlayers(boolean value) {
        this.attackCreativePlayers = value;
    }

    public boolean convertMaleOnlyVInjectionsToA() {
        return this.convertMaleOnlyVInjectionsToA;
    }

    public void setConvertMaleOnlyVInjectionsToA(boolean value) {
        this.convertMaleOnlyVInjectionsToA = value;
    }

    public int getAttackEscapeHits() {
        return Math.max(1, Math.min(50, this.attackEscapeHits));
    }

    public void setAttackEscapeHits(int hits) {
        this.attackEscapeHits = Math.max(1, Math.min(50, hits));
    }

    public double getAttackDecayPerSecond() {
        return Math.max(0.0, Math.min(5.0, this.attackDecayPerSecond));
    }

    public void setAttackDecayPerSecond(double decay) {
        this.attackDecayPerSecond = Math.max(0.0, Math.min(5.0, decay));
    }

    public int getAttackEscapeDamageDifficultyPercent() {
        return NonConfig.clampAttackEscapeDamageDifficultyPercent(this.attackEscapeDamageDifficultyPercent);
    }

    public void setAttackEscapeDamageDifficultyPercent(int percent) {
        this.attackEscapeDamageDifficultyPercent = NonConfig.clampAttackEscapeDamageDifficultyPercent(percent);
    }

    public int getAttackEscapeInvulnerabilitySeconds() {
        return NonConfig.clampAttackEscapeProtectionSeconds(this.attackEscapeInvulnerabilitySeconds);
    }

    public void setAttackEscapeInvulnerabilitySeconds(int seconds) {
        this.attackEscapeInvulnerabilityTicks = null;
        this.attackEscapeInvulnerabilitySeconds = NonConfig.clampAttackEscapeProtectionSeconds(seconds);
    }

    public int getAttackEscapeInvulnerabilityTicks() {
        return NonConfig.secondsToTicks(this.getAttackEscapeInvulnerabilitySeconds());
    }

    public int getAttackEscapeAnimationProtectionSeconds() {
        return NonConfig.clampAttackEscapeProtectionSeconds(this.attackEscapeAnimationProtectionSeconds);
    }

    public void setAttackEscapeAnimationProtectionSeconds(int seconds) {
        this.attackEscapeAnimationProtectionSeconds = NonConfig.clampAttackEscapeProtectionSeconds(seconds);
    }

    public int getAttackEscapeAnimationProtectionTicks() {
        return NonConfig.secondsToTicks(this.getAttackEscapeAnimationProtectionSeconds());
    }

    private static int clampAttackEscapeProtectionSeconds(int seconds) {
        return Math.max(0, Math.min(10, seconds));
    }

    private static int clampAttackEscapeDamageDifficultyPercent(int percent) {
        return Math.max(0, Math.min(200, percent));
    }

    private static int secondsToTicks(int seconds) {
        return NonConfig.clampAttackEscapeProtectionSeconds(seconds) * 20;
    }

    private static int ticksToSeconds(int ticks) {
        int clampedTicks = Math.max(0, ticks);
        if (clampedTicks == 0) {
            return 0;
        }
        return NonConfig.clampAttackEscapeProtectionSeconds((clampedTicks + 19) / 20);
    }

    public int getPostEscapeGatherMaxMobs() {
        return Math.max(0, Math.min(64, this.postEscapeGatherMaxMobs));
    }

    public void setPostEscapeGatherMaxMobs(int value) {
        this.postEscapeGatherMaxMobs = Math.max(0, Math.min(64, value));
    }

    public boolean blockAnimationsWhileRidingLivingEntities() {
        return this.blockAnimationsWhileRidingLivingEntities;
    }

    public void setBlockAnimationsWhileRidingLivingEntities(boolean value) {
        this.blockAnimationsWhileRidingLivingEntities = value;
    }

    public boolean blockAnimationsWhileRidingVehicles() {
        return this.blockAnimationsWhileRidingVehicles;
    }

    public void setBlockAnimationsWhileRidingVehicles(boolean value) {
        this.blockAnimationsWhileRidingVehicles = value;
    }

    public int getAttackOutcomeFailsafeThreshold() {
        return Math.max(1, Math.min(50, this.attackOutcomeFailsafeThreshold));
    }

    public void setAttackOutcomeFailsafeThreshold(int threshold) {
        this.attackOutcomeFailsafeThreshold = Math.max(1, Math.min(50, threshold));
    }

    public int getPregnancyChancePercent() {
        if (!this.pregnancyEnabled) {
            return 0;
        }
        return Math.max(0, Math.min(100, this.pregnancyChancePercent));
    }

    public void setPregnancyChancePercent(int value) {
        this.pregnancyChancePercent = Math.max(0, Math.min(100, value));
    }

    public int getPregnancyDurationMinutes() {
        return Math.max(1, Math.min(5256000, this.pregnancyDurationMinutes));
    }

    public void setPregnancyDurationMinutes(int minutes) {
        this.pregnancyDurationMinutes = Math.max(1, Math.min(5256000, minutes));
    }

    public long getPregnancyDurationTicks() {
        return (long)this.getPregnancyDurationMinutes() * 1200L;
    }

    public Map<String, Integer> getPregnancyChanceByEntity() {
        if (this.pregnancyChanceByEntity == null) {
            return Map.of();
        }
        return Map.copyOf(this.pregnancyChanceByEntity);
    }

    public void setPregnancyChanceByEntity(Map<String, Integer> entries) {
        LinkedHashMap<String, Integer> cleaned = new LinkedHashMap<String, Integer>();
        if (entries != null) {
            for (Map.Entry<String, Integer> entry : entries.entrySet()) {
                String key;
                if (entry == null || (key = entry.getKey()) == null || (key = key.trim()).isEmpty()) continue;
                int value = entry.getValue() == null ? 0 : entry.getValue();
                value = Math.max(0, Math.min(100, value));
                cleaned.put(key, value);
            }
        }
        this.pregnancyChanceByEntity = cleaned;
    }

    public int getPregnancyChanceFor(String entityId) {
        if (!this.pregnancyEnabled) {
            return 0;
        }
        if (entityId == null || entityId.isBlank() || this.pregnancyChanceByEntity == null) {
            return this.getPregnancyChancePercent();
        }
        Integer override = this.pregnancyChanceByEntity.get(entityId);
        if (override == null) {
            return this.getPregnancyChancePercent();
        }
        return Math.max(0, Math.min(100, override));
    }

    public Map<String, OffspringCountRange> getPregnancyOffspringCountByEntity() {
        if (this.pregnancyOffspringCountByEntity == null) {
            return Map.of();
        }
        LinkedHashMap<String, OffspringCountRange> copy = new LinkedHashMap<String, OffspringCountRange>();
        for (Map.Entry<String, OffspringCountRange> entry : this.pregnancyOffspringCountByEntity.entrySet()) {
            if (entry == null || entry.getKey() == null || entry.getValue() == null) continue;
            copy.put(entry.getKey(), entry.getValue().sanitized());
        }
        return copy;
    }

    public void setPregnancyOffspringCountByEntity(Map<String, OffspringCountRange> entries) {
        LinkedHashMap<String, OffspringCountRange> cleaned = new LinkedHashMap<String, OffspringCountRange>();
        if (entries != null) {
            for (Map.Entry<String, OffspringCountRange> entry : entries.entrySet()) {
                String key;
                if (entry == null || (key = entry.getKey()) == null || (key = key.trim()).isEmpty()) continue;
                OffspringCountRange range = entry.getValue() == null ? new OffspringCountRange() : entry.getValue().sanitized();
                cleaned.put(key, range);
            }
        }
        this.pregnancyOffspringCountByEntity = cleaned;
    }

    public OffspringCountRange getPregnancyOffspringCountRangeFor(String entityId) {
        if (entityId == null || entityId.isBlank() || this.pregnancyOffspringCountByEntity == null) {
            return new OffspringCountRange();
        }
        OffspringCountRange override = this.pregnancyOffspringCountByEntity.get(entityId);
        return override == null ? new OffspringCountRange() : override.sanitized();
    }

    public int getPregnancyEggDefaultHatchSeconds() {
        return Math.max(1, Math.min(72000, this.pregnancyEggDefaultHatchSeconds));
    }

    public void setPregnancyEggDefaultHatchSeconds(int seconds) {
        this.pregnancyEggDefaultHatchSeconds = Math.max(1, Math.min(72000, seconds));
    }

    public Map<String, Boolean> getPregnancyEggEnabledByEntity() {
        if (this.pregnancyEggEnabledByEntity == null) {
            return Map.of();
        }
        return Map.copyOf(this.pregnancyEggEnabledByEntity);
    }

    public void setPregnancyEggEnabledByEntity(Map<String, Boolean> entries) {
        LinkedHashMap<String, Boolean> cleaned = new LinkedHashMap<String, Boolean>();
        if (entries != null) {
            for (Map.Entry<String, Boolean> entry : entries.entrySet()) {
                String key;
                if (entry == null || (key = entry.getKey()) == null || (key = key.trim()).isEmpty()) continue;
                boolean enabled = Boolean.TRUE.equals(entry.getValue());
                cleaned.put(key, enabled);
            }
        }
        this.pregnancyEggEnabledByEntity = cleaned;
    }

    public boolean isPregnancyEggEnabledFor(String entityId) {
        if (entityId == null || entityId.isBlank() || this.pregnancyEggEnabledByEntity == null) {
            return false;
        }
        return Boolean.TRUE.equals(this.pregnancyEggEnabledByEntity.get(entityId));
    }

    public Map<String, Integer> getPregnancyEggHatchSecondsByEntity() {
        if (this.pregnancyEggHatchSecondsByEntity == null) {
            return Map.of();
        }
        return Map.copyOf(this.pregnancyEggHatchSecondsByEntity);
    }

    public void setPregnancyEggHatchSecondsByEntity(Map<String, Integer> entries) {
        LinkedHashMap<String, Integer> cleaned = new LinkedHashMap<String, Integer>();
        if (entries != null) {
            for (Map.Entry<String, Integer> entry : entries.entrySet()) {
                String key;
                if (entry == null || (key = entry.getKey()) == null || (key = key.trim()).isEmpty()) continue;
                int value = entry.getValue() == null ? this.getPregnancyEggDefaultHatchSeconds() : entry.getValue().intValue();
                value = Math.max(1, Math.min(72000, value));
                cleaned.put(key, value);
            }
        }
        this.pregnancyEggHatchSecondsByEntity = cleaned;
    }

    public int getPregnancyEggHatchSecondsFor(String entityId) {
        int fallback = this.getPregnancyEggDefaultHatchSeconds();
        if (entityId == null || entityId.isBlank() || this.pregnancyEggHatchSecondsByEntity == null) {
            return fallback;
        }
        Integer override = this.pregnancyEggHatchSecondsByEntity.get(entityId);
        if (override == null) {
            return fallback;
        }
        return Math.max(1, Math.min(72000, override));
    }

    public boolean isPregnancyAutoTameMobs() {
        return this.pregnancyAutoTameMobs;
    }

    public void setPregnancyAutoTameMobs(boolean value) {
        this.pregnancyAutoTameMobs = value;
    }

    public boolean pregnancyFriendlyMobsIgnorePlayers() {
        return this.pregnancyFriendlyMobsIgnorePlayers;
    }

    public void setPregnancyFriendlyMobsIgnorePlayers(boolean value) {
        this.pregnancyFriendlyMobsIgnorePlayers = value;
    }

    public Map<String, EntityProfile> getEntityProfilesByEntity() {
        if (this.entityProfilesByEntity == null) {
            return Map.of();
        }
        LinkedHashMap<String, EntityProfile> copy = new LinkedHashMap<String, EntityProfile>();
        for (Map.Entry<String, EntityProfile> entry : this.entityProfilesByEntity.entrySet()) {
            EntityProfile profile;
            String key;
            if (entry == null || entry.getKey() == null || entry.getValue() == null || (key = entry.getKey().trim()).isEmpty() || (profile = entry.getValue().sanitized()).isEmpty()) continue;
            copy.put(key, profile);
        }
        return copy;
    }

    public void setEntityProfilesByEntity(Map<String, EntityProfile> entries) {
        LinkedHashMap<String, EntityProfile> cleaned = new LinkedHashMap<String, EntityProfile>();
        if (entries != null) {
            for (Map.Entry<String, EntityProfile> entry : entries.entrySet()) {
                EntityProfile profile;
                String key;
                if (entry == null || entry.getKey() == null || entry.getValue() == null || (key = entry.getKey().trim()).isEmpty() || (profile = entry.getValue().sanitized()).isEmpty()) continue;
                cleaned.put(key, profile);
            }
        }
        this.entityProfilesByEntity = cleaned;
    }

    public double getLiquidDecayPerSecond() {
        return Math.max(0.0, Math.min(20.0, this.liquidDecayPerSecond));
    }

    public void setLiquidDecayPerSecond(double decay) {
        this.liquidDecayPerSecond = Math.max(0.0, Math.min(20.0, decay));
    }

    public int getLiquidPuddleDespawnSeconds() {
        return Math.max(1, Math.min(300, this.liquidPuddleDespawnSeconds));
    }

    public void setLiquidPuddleDespawnSeconds(int seconds) {
        this.liquidPuddleDespawnSeconds = Math.max(1, Math.min(300, seconds));
    }

    public double getPeakXpPerMl() {
        return Math.max(0.0, Math.min(10.0, this.peakXpPerMl));
    }

    public void setPeakXpPerMl(double value) {
        this.peakXpPerMl = Math.max(0.0, Math.min(10.0, value));
    }

    public float getFilledStageOneSpeedMult() {
        return this.clampMultiplier(this.filledStageOneSpeedMult);
    }

    public void setFilledStageOneSpeedMult(float value) {
        this.filledStageOneSpeedMult = this.clampMultiplier(value);
    }

    public float getFilledStageTwoSpeedMult() {
        return this.clampMultiplier(this.filledStageTwoSpeedMult);
    }

    public void setFilledStageTwoSpeedMult(float value) {
        this.filledStageTwoSpeedMult = this.clampMultiplier(value);
    }

    public float getFilledStageThreeSpeedMult() {
        return this.clampMultiplier(this.filledStageThreeSpeedMult);
    }

    public void setFilledStageThreeSpeedMult(float value) {
        this.filledStageThreeSpeedMult = this.clampMultiplier(value);
    }

    public float getFilledStageOneJumpMult() {
        return this.clampMultiplier(this.filledStageOneJumpMult);
    }

    public void setFilledStageOneJumpMult(float value) {
        this.filledStageOneJumpMult = this.clampMultiplier(value);
    }

    public float getFilledStageTwoJumpMult() {
        return this.clampMultiplier(this.filledStageTwoJumpMult);
    }

    public void setFilledStageTwoJumpMult(float value) {
        this.filledStageTwoJumpMult = this.clampMultiplier(value);
    }

    public float getFilledStageThreeJumpMult() {
        return this.clampMultiplier(this.filledStageThreeJumpMult);
    }

    public void setFilledStageThreeJumpMult(float value) {
        this.filledStageThreeJumpMult = this.clampMultiplier(value);
    }

    public int getUiEnergyOffsetX() {
        return this.clampUiOffset(this.uiEnergyOffsetX);
    }

    public void setUiEnergyOffsetX(int value) {
        this.uiEnergyOffsetX = this.clampUiOffset(value);
    }

    public int getUiEnergyOffsetY() {
        return this.clampUiOffset(this.uiEnergyOffsetY);
    }

    public void setUiEnergyOffsetY(int value) {
        this.uiEnergyOffsetY = this.clampUiOffset(value);
    }

    public int getUiEnergyHeartOffsetX() {
        return this.clampUiOffset(this.uiEnergyHeartOffsetX);
    }

    public void setUiEnergyHeartOffsetX(int value) {
        this.uiEnergyHeartOffsetX = this.clampUiOffset(value);
    }

    public int getUiEnergyHeartOffsetY() {
        return this.clampUiOffset(this.uiEnergyHeartOffsetY);
    }

    public void setUiEnergyHeartOffsetY(int value) {
        this.uiEnergyHeartOffsetY = this.clampUiOffset(value);
    }

    public int getUiAttackOffsetX() {
        return this.clampUiOffset(this.uiAttackOffsetX);
    }

    public void setUiAttackOffsetX(int value) {
        this.uiAttackOffsetX = this.clampUiOffset(value);
    }

    public int getUiAttackOffsetY() {
        return this.clampUiOffset(this.uiAttackOffsetY);
    }

    public void setUiAttackOffsetY(int value) {
        this.uiAttackOffsetY = this.clampUiOffset(value);
    }

    public int getUiPromptOffsetX() {
        return this.clampUiOffset(this.uiPromptOffsetX);
    }

    public void setUiPromptOffsetX(int value) {
        this.uiPromptOffsetX = this.clampUiOffset(value);
    }

    public int getUiPromptOffsetY() {
        return this.clampUiOffset(this.uiPromptOffsetY);
    }

    public void setUiPromptOffsetY(int value) {
        this.uiPromptOffsetY = this.clampUiOffset(value);
    }

    public int getUiLiquidOffsetX() {
        return this.clampUiOffset(this.uiLiquidOffsetX);
    }

    public void setUiLiquidOffsetX(int value) {
        this.uiLiquidOffsetX = this.clampUiOffset(value);
    }

    public int getUiLiquidOffsetY() {
        return this.clampUiOffset(this.uiLiquidOffsetY);
    }

    public void setUiLiquidOffsetY(int value) {
        this.uiLiquidOffsetY = this.clampUiOffset(value);
    }

    public boolean isUiEnergyVisible() {
        return this.uiEnergyVisible;
    }

    public void setUiEnergyVisible(boolean value) {
        this.uiEnergyVisible = value;
    }

    public boolean isUiEnergyHeartVisible() {
        return this.uiEnergyHeartVisible;
    }

    public void setUiEnergyHeartVisible(boolean value) {
        this.uiEnergyHeartVisible = value;
    }

    public boolean isUiAttackVisible() {
        return this.uiAttackVisible;
    }

    public void setUiAttackVisible(boolean value) {
        this.uiAttackVisible = value;
    }

    public boolean isUiPromptVisible() {
        return this.uiPromptVisible;
    }

    public void setUiPromptVisible(boolean value) {
        this.uiPromptVisible = value;
    }

    public boolean isUiLiquidVisible() {
        return this.uiLiquidVisible;
    }

    public void setUiLiquidVisible(boolean value) {
        this.uiLiquidVisible = value;
    }

    public boolean isDebugSpinMode() {
        return this.debugSpinMode;
    }

    public void setDebugSpinMode(boolean value) {
        this.debugSpinMode = value;
    }

    public String getDestroyedSkinTintHex() {
        return NonConfig.sanitizeHexColor(this.destroyedSkinTintHex);
    }

    public void setDestroyedSkinTintHex(String value) {
        this.destroyedSkinTintHex = NonConfig.sanitizeHexColor(value);
    }

    public boolean hasDestroyedSkinTintOverride() {
        return !this.getDestroyedSkinTintHex().isEmpty();
    }

    public int getDestroyedSkinTintRgb(int fallbackRgb) {
        String hex = this.getDestroyedSkinTintHex();
        if (hex.isEmpty()) {
            return fallbackRgb & 0xFFFFFF;
        }
        try {
            return Integer.parseUnsignedInt(hex, 16) & 0xFFFFFF;
        }
        catch (NumberFormatException e) {
            return fallbackRgb & 0xFFFFFF;
        }
    }

    public Map<String, GenderSpawnChances> getGenderSpawnByEntity() {
        if (this.genderSpawnByEntity == null) {
            return Map.of();
        }
        LinkedHashMap<String, GenderSpawnChances> out = new LinkedHashMap<String, GenderSpawnChances>();
        for (Map.Entry<String, GenderSpawnChances> entry : this.genderSpawnByEntity.entrySet()) {
            GenderSpawnChances chances;
            String key;
            if (entry == null || (key = NonConfig.sanitizeKey(entry.getKey())) == null || (chances = NonConfig.sanitizeGenderSpawnChances(entry.getValue())) == null) continue;
            out.put(key, chances);
        }
        return Map.copyOf(out);
    }

    public void setGenderSpawnByEntity(Map<String, GenderSpawnChances> entries) {
        LinkedHashMap<String, GenderSpawnChances> cleaned = new LinkedHashMap<String, GenderSpawnChances>();
        if (entries != null) {
            for (Map.Entry<String, GenderSpawnChances> entry : entries.entrySet()) {
                GenderSpawnChances chances;
                String key;
                if (entry == null || (key = NonConfig.sanitizeKey(entry.getKey())) == null || (chances = NonConfig.sanitizeGenderSpawnChances(entry.getValue())) == null) continue;
                cleaned.put(key, chances);
            }
        }
        this.genderSpawnByEntity = cleaned;
    }

    public Map<String, Integer> getLiquidGainByEntity() {
        if (this.liquidGainByEntity == null) {
            return Map.of();
        }
        return Map.copyOf(this.liquidGainByEntity);
    }

    public int getLiquidGainFor(String entityId) {
        if (entityId == null || this.liquidGainByEntity == null) {
            return 0;
        }
        Integer value = this.liquidGainByEntity.get(entityId);
        if (value == null) {
            return 0;
        }
        return Math.max(0, Math.min(1000, value));
    }

    public void setLiquidGainByEntity(Map<String, Integer> entries) {
        LinkedHashMap<String, Integer> cleaned = new LinkedHashMap<String, Integer>();
        if (entries != null) {
            for (Map.Entry<String, Integer> entry : entries.entrySet()) {
                String key;
                if (entry == null || (key = entry.getKey()) == null || (key = key.trim()).isEmpty()) continue;
                int value = entry.getValue() == null ? 0 : entry.getValue();
                value = Math.max(0, Math.min(1000, value));
                cleaned.put(key, value);
            }
        }
        this.liquidGainByEntity = cleaned;
    }

    public Map<String, String> getLiquidColorByEntity() {
        if (this.liquidColorByEntity == null) {
            return Map.of();
        }
        return Map.copyOf(this.liquidColorByEntity);
    }

    public void setLiquidColorByEntity(Map<String, String> entries) {
        LinkedHashMap<String, String> cleaned = new LinkedHashMap<String, String>();
        if (entries != null) {
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                String normalizedHex;
                String key;
                if (entry == null || (key = entry.getKey()) == null || (key = key.trim()).isEmpty() || (normalizedHex = NonLiquidColors.normalizeHexColor(entry.getValue())) == null) continue;
                cleaned.put(key, normalizedHex);
            }
        }
        this.liquidColorByEntity = cleaned;
    }

    public Integer getLiquidColorOverrideRgb(String entityId) {
        if (entityId == null || this.liquidColorByEntity == null) {
            return null;
        }
        return NonLiquidColors.resolveEntityOverrideFromMap(this.liquidColorByEntity, entityId);
    }

    public List<String> getExternalNoNPackLoadOrder() {
        if (this.externalNoNPackLoadOrder == null) {
            return List.of();
        }
        return List.copyOf(this.externalNoNPackLoadOrder);
    }

    public void setExternalNoNPackLoadOrder(List<String> order) {
        this.externalNoNPackLoadOrder = NonConfig.sanitizePackLoadOrder(order);
    }

    public List<String> getDisabledAnimationPacks() {
        if (this.disabledAnimationPacks == null) {
            return List.of();
        }
        return List.copyOf(this.disabledAnimationPacks);
    }

    public void setDisabledAnimationPacks(List<String> packIds) {
        this.disabledAnimationPacks = NonConfig.sanitizeStringList(packIds);
    }

    public List<String> getDisabledAnimations() {
        if (this.disabledAnimations == null) {
            return List.of();
        }
        return List.copyOf(this.disabledAnimations);
    }

    public void setDisabledAnimations(List<String> animationIds) {
        this.disabledAnimations = NonConfig.sanitizeStringList(animationIds);
    }

    public List<String> getAcknowledgedStartupWarnings() {
        if (this.acknowledgedStartupWarnings == null) {
            return List.of();
        }
        return List.copyOf(this.acknowledgedStartupWarnings);
    }

    public void setAcknowledgedStartupWarnings(List<String> warningIds) {
        this.acknowledgedStartupWarnings = NonConfig.sanitizeAcknowledgedStartupWarnings(warningIds);
    }

    public boolean isStartupWarningAcknowledged(String warningId) {
        String normalized = NonConfig.sanitizeWarningId(warningId);
        return normalized != null && this.getAcknowledgedStartupWarnings().contains(normalized);
    }

    public boolean acknowledgeStartupWarning(String warningId) {
        String normalized = NonConfig.sanitizeWarningId(warningId);
        if (normalized == null) {
            return false;
        }
        LinkedHashSet<String> merged = new LinkedHashSet<String>(this.getAcknowledgedStartupWarnings());
        if (!merged.add(normalized)) {
            return false;
        }
        this.setAcknowledgedStartupWarnings(new ArrayList<String>(merged));
        return true;
    }

    private static List<String> sanitizePackLoadOrder(List<String> order) {
        if (order == null || order.isEmpty()) {
            return new ArrayList<String>();
        }
        LinkedHashSet<String> cleaned = new LinkedHashSet<String>();
        for (String raw : order) {
            String trimmed;
            if (raw == null || (trimmed = raw.trim()).isEmpty()) continue;
            cleaned.add(trimmed);
        }
        return new ArrayList<String>(cleaned);
    }

    private static List<String> sanitizeStringList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return new ArrayList<String>();
        }
        LinkedHashSet<String> cleaned = new LinkedHashSet<String>();
        for (String raw : values) {
            String trimmed;
            if (raw == null || (trimmed = raw.trim()).isEmpty()) continue;
            cleaned.add(trimmed);
        }
        return new ArrayList<String>(cleaned);
    }

    private static List<String> sanitizeAcknowledgedStartupWarnings(List<String> warningIds) {
        if (warningIds == null || warningIds.isEmpty()) {
            return new ArrayList<String>();
        }
        LinkedHashSet<String> cleaned = new LinkedHashSet<String>();
        for (String raw : warningIds) {
            String normalized = NonConfig.sanitizeWarningId(raw);
            if (normalized == null) continue;
            cleaned.add(normalized);
        }
        return cleaned.stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toCollection(ArrayList::new));
    }

    private static String sanitizeWarningId(String raw) {
        if (raw == null) {
            return null;
        }
        String value = raw.trim();
        return value.isEmpty() ? null : value;
    }

    private int clampUiOffset(int value) {
        return Math.max(-999, Math.min(999, value));
    }

    private float clampMultiplier(float value) {
        if (!Float.isFinite(value)) {
            return 1.0f;
        }
        return Math.max(0.1f, Math.min(1.0f, value));
    }

    private static int clampPercent(int value) {
        return Math.max(0, Math.min(100, value));
    }

    private static int clampInt(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int valueOrDefault(int value, int fallback) {
        return value <= 0 ? fallback : value;
    }

    private static String sanitizeIntifaceServerUrl(String raw) {
        if (raw == null || raw.isBlank()) {
            return DEFAULT_INTIFACE_SERVER_URL;
        }
        String value = raw.trim();
        try {
            URI uri = new URI(value);
            String scheme = uri.getScheme();
            if (scheme == null) {
                return DEFAULT_INTIFACE_SERVER_URL;
            }
            if (!scheme.equalsIgnoreCase("ws") && !scheme.equalsIgnoreCase("wss")) {
                return DEFAULT_INTIFACE_SERVER_URL;
            }
            if (uri.getHost() == null || uri.getHost().isBlank()) {
                return DEFAULT_INTIFACE_SERVER_URL;
            }
            return value;
        }
        catch (URISyntaxException e) {
            return DEFAULT_INTIFACE_SERVER_URL;
        }
    }

    private static float clampFloat(float value, float min, float max) {
        if (!Float.isFinite(value)) {
            return min;
        }
        return Math.max(min, Math.min(max, value));
    }

    public static GenderSpawnChances defaultGenderSpawnChances() {
        return new GenderSpawnChances(48, 47, 5);
    }

    public static GenderSpawnChances validOrDefault(GenderSpawnChances chances) {
        GenderSpawnChances safe = NonConfig.sanitizeGenderSpawnChances(chances);
        return safe == null ? NonConfig.defaultGenderSpawnChances() : safe;
    }

    public static GenderSpawnChances sanitizeGenderSpawnChances(GenderSpawnChances chances) {
        if (chances == null) {
            return null;
        }
        GenderSpawnChances safe = chances.sanitized();
        return safe.isValid() ? safe : null;
    }

    public static String normalizeBirthMode(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (PREGNANCY_BIRTH_MODE_EGG.equals(normalized)) {
            return PREGNANCY_BIRTH_MODE_EGG;
        }
        if (PREGNANCY_BIRTH_MODE_DIRECT.equals(normalized)) {
            return PREGNANCY_BIRTH_MODE_DIRECT;
        }
        return null;
    }

    public static String normalizeEntityIdString(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    public static String normalizeTextureIdString(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            return null;
        }
        Identifier parsed = Identifier.tryParse((String)normalized);
        return parsed == null || parsed.getPath().isBlank() ? null : normalized;
    }

    private static String sanitizeKey(String raw) {
        if (raw == null) {
            return null;
        }
        String key = raw.trim();
        return key.isEmpty() ? null : key;
    }

    private static String sanitizeHexColor(String raw) {
        if (raw == null) {
            return DEFAULT_DESTROYED_SKIN_TINT_HEX;
        }
        String value = raw.trim();
        if (value.startsWith("#")) {
            value = value.substring(1);
        }
        if ((value = value.toUpperCase(Locale.ROOT)).isEmpty()) {
            return DEFAULT_DESTROYED_SKIN_TINT_HEX;
        }
        return value.matches("[0-9A-F]{6}") ? value : DEFAULT_DESTROYED_SKIN_TINT_HEX;
    }

    private static Map<String, Boolean> defaultPregnancyEggEnabledMap() {
        LinkedHashMap<String, Boolean> map = new LinkedHashMap<String, Boolean>();
        map.put("minecraft:spider", true);
        map.put("minecraft:cave_spider", true);
        return map;
    }

    private static Map<String, OffspringCountRange> defaultPregnancyOffspringCountMap() {
        LinkedHashMap<String, OffspringCountRange> map = new LinkedHashMap<String, OffspringCountRange>();
        map.put("minecraft:spider", new OffspringCountRange(1, 3));
        map.put("minecraft:cave_spider", new OffspringCountRange(2, 5));
        map.put("minecraft:wolf", new OffspringCountRange(1, 2));
        map.put("minecraft:horse", new OffspringCountRange(1, 1));
        map.put("minecraft:fox", new OffspringCountRange(1, 2));
        map.put("minecraft:dolphin", new OffspringCountRange(1, 1));
        map.put("minecraft:pig", new OffspringCountRange(1, 3));
        return map;
    }

    private static Map<String, GenderSpawnChances> defaultGenderSpawnByEntityMap() {
        LinkedHashMap<String, GenderSpawnChances> map = new LinkedHashMap<String, GenderSpawnChances>();
        GenderSpawnChances maleOnly = new GenderSpawnChances(100, 0, 0);
        map.put("minecraft:spider", maleOnly);
        map.put("minecraft:cave_spider", maleOnly);
        return map;
    }

    private void ensureDefaults() {
        this.debugChatMode = this.debugChatMode == null ? NonDebugChatMode.SETUP_ERRORS.id() : this.debugChatMode().id();
        this.pregnancyChancePercent = this.getPregnancyChancePercent();
        this.pregnancyDurationMinutes = this.getPregnancyDurationMinutes();
        this.pregnancyChanceByEntity = new LinkedHashMap<String, Integer>();
        this.pregnancyOffspringCountByEntity = new LinkedHashMap<String, OffspringCountRange>();
        this.pregnancyEggDefaultHatchSeconds = this.getPregnancyEggDefaultHatchSeconds();
        this.pregnancyEggEnabledByEntity = new LinkedHashMap<String, Boolean>();
        this.pregnancyEggHatchSecondsByEntity = new LinkedHashMap<String, Integer>();
        this.pregnancyAutoTameMobs = this.isPregnancyAutoTameMobs();
        this.pregnancyFriendlyMobsIgnorePlayers = this.pregnancyFriendlyMobsIgnorePlayers();
        this.setEntityProfilesByEntity(this.entityProfilesByEntity);
        LinkedHashMap<String, Integer> mergedGains = new LinkedHashMap<String, Integer>();
        if (this.liquidGainByEntity != null) {
            this.setLiquidGainByEntity(this.liquidGainByEntity);
            mergedGains.putAll(this.liquidGainByEntity);
        }
        this.liquidGainByEntity = mergedGains;
        LinkedHashMap<String, String> mergedColors = new LinkedHashMap<String, String>();
        if (this.liquidColorByEntity != null) {
            this.setLiquidColorByEntity(this.liquidColorByEntity);
            mergedColors.putAll(this.liquidColorByEntity);
        }
        this.liquidColorByEntity = mergedColors;
        this.setExternalNoNPackLoadOrder(this.externalNoNPackLoadOrder);
        this.setDisabledAnimationPacks(this.disabledAnimationPacks);
        this.setDisabledAnimations(this.disabledAnimations);
        this.destroyedSkinTintHex = this.getDestroyedSkinTintHex();
        this.liquidPuddleDespawnSeconds = this.getLiquidPuddleDespawnSeconds();
        this.peakXpPerMl = this.getPeakXpPerMl();
        this.initialEnergyMax = this.getInitialEnergyMax();
        this.energyGainRate = this.getEnergyGainRate();
        this.nearAnimationEnergyGainMult = this.getNearAnimationEnergyGainMult();
        this.playerEnergyAuraMultLow = this.getPlayerEnergyAuraMultLow();
        this.playerEnergyAuraMultMid = this.getPlayerEnergyAuraMultMid();
        this.playerEnergyAuraMultHigh = this.getPlayerEnergyAuraMultHigh();
        this.destroyedSkinAuraMultStage1 = this.getDestroyedSkinAuraMultStage1();
        this.destroyedSkinAuraMultStage2 = this.getDestroyedSkinAuraMultStage2();
        this.destroyedSkinAuraMultStage3 = this.getDestroyedSkinAuraMultStage3();
        this.destroyedSkinAuraMultStage4 = this.getDestroyedSkinAuraMultStage4();
        this.normalDamageDestroyedSkinChancePercent = this.getNormalDamageDestroyedSkinChancePercent();
        this.lastDefeatedEnabled = this.isLastDefeatedEnabled();
        this.lastDefeatedEnergyThreshold = this.getLastDefeatedEnergyThreshold();
        this.lastDefeatedSearchRadius = this.getLastDefeatedSearchRadius();
        this.lastDefeatedCooldownSeconds = this.getLastDefeatedCooldownSeconds();
        this.playerEnergyAuraRadiusMin = this.getPlayerEnergyAuraRadiusMin();
        this.playerEnergyAuraRadiusMax = this.getPlayerEnergyAuraRadiusMax();
        this.playerEnergyAuraPulseTicks = this.getPlayerEnergyAuraPulseTicks();
        this.genderSpawnByEntity = new LinkedHashMap<String, GenderSpawnChances>();
        this.setGlobalGenderSpawnChances(this.getGlobalGenderSpawnChances());
        this.maleMaleChancePercent = this.getMaleMaleChancePercent();
        this.femaleFemaleChancePercent = this.getFemaleFemaleChancePercent();
        this.entityEntityChancePercent = this.getEntityEntityChancePercent();
        this.multiActorJoinChancePercent = this.getMultiActorJoinChancePercent();
        this.setPlayerGenderSelection(this.getPlayerGenderSelection());
        this.allowPlayerGenderChangeAnytime = this.allowPlayerGenderChangeAnytime();
        this.requirePlayerGenderSelectionOnJoin = this.requirePlayerGenderSelectionOnJoin();
        this.setAllowedStartingGenderMask(this.allowedStartingGenderMask);
        this.setAcknowledgedStartupWarnings(this.acknowledgedStartupWarnings);
        this.messSystemEnabled = this.isMessSystemEnabled();
        this.destroyedSkinSystemEnabled = this.isDestroyedSkinSystemEnabled();
        this.allowCraftingTableSkinRepair = this.isCraftingTableSkinRepairAllowed();
        this.keepMessAfterDeath = this.keepMessAfterDeath();
        this.keepRippedSkinAfterDeath = this.keepRippedSkinAfterDeath();
        this.keepLiquidTankAfterDeath = this.keepLiquidTankAfterDeath();
        this.keepPregnancyAfterDeath = this.keepPregnancyAfterDeath();
        this.armorShedEffectEnabled = this.isArmorShedEffectEnabled();
        this.syncFemaleGenderModWithNonGender = this.isSyncFemaleGenderModWithNonGender();
        this.destroyedSkinFemaleGenderOverridesEnabled = this.isDestroyedSkinFemaleGenderOverridesEnabled();
        this.setFemaleGenderDestroyedLow(this.femaleGenderDestroyedLow);
        this.setFemaleGenderDestroyedHigh(this.femaleGenderDestroyedHigh);
        this.actionSoundVolumePercent = this.getActionSoundVolumePercent();
        this.intifaceServerUrl = this.getIntifaceServerUrl();
        this.intifaceReactiveImpactStrengthPercent = this.getIntifaceReactiveImpactStrengthPercent();
        this.intifaceReactiveImpactDurationMs = this.getIntifaceReactiveImpactDurationMs();
        this.intifacePeakReactiveImpactStrengthPercent = this.getIntifacePeakReactiveImpactStrengthPercent();
        this.intifacePeakReactiveImpactDurationMs = this.getIntifacePeakReactiveImpactDurationMs();
        this.intifaceCooldownMs = this.getIntifaceCooldownMs();
        this.intifaceAnimationBaselineStrengthPercent = this.getIntifaceAnimationBaselineStrengthPercent();
        this.intifaceOscillatorRegularSpeedPercent = this.getIntifaceOscillatorRegularSpeedPercent();
        this.intifaceOscillatorPeakSpeedPercent = this.getIntifaceOscillatorPeakSpeedPercent();
        this.intifaceStrokerMaxDistancePercent = this.getIntifaceStrokerMaxDistancePercent();
        this.intifaceStrokerMinDistancePercent = this.getIntifaceStrokerMinDistancePercent();
        this.intifaceStrokerRegularMoveDurationMs = this.getIntifaceStrokerRegularMoveDurationMs();
        this.intifaceStrokerPeakMoveDurationMs = this.getIntifaceStrokerPeakMoveDurationMs();
        this.intifaceEnergizedOneBasePercent = this.getIntifaceEnergizedBasePercent(1);
        this.intifaceEnergizedOnePulsePercent = this.getIntifaceEnergizedPulsePercent(1);
        this.intifaceEnergizedTwoBasePercent = this.getIntifaceEnergizedBasePercent(2);
        this.intifaceEnergizedTwoPulsePercent = this.getIntifaceEnergizedPulsePercent(2);
        this.intifaceEnergizedThreeBasePercent = this.getIntifaceEnergizedBasePercent(3);
        this.intifaceEnergizedThreePulsePercent = this.getIntifaceEnergizedPulsePercent(3);
        this.attackEscapeHits = this.getAttackEscapeHits();
        this.attackDecayPerSecond = this.getAttackDecayPerSecond();
        this.attackEscapeDamageDifficultyPercent = this.getAttackEscapeDamageDifficultyPercent();
        if (this.attackEscapeInvulnerabilityTicks != null) {
            this.attackEscapeInvulnerabilitySeconds = NonConfig.ticksToSeconds(this.attackEscapeInvulnerabilityTicks);
            this.attackEscapeInvulnerabilityTicks = null;
        }
        this.attackEscapeInvulnerabilitySeconds = this.getAttackEscapeInvulnerabilitySeconds();
        this.attackEscapeAnimationProtectionSeconds = this.getAttackEscapeAnimationProtectionSeconds();
        this.postEscapeGatherMaxMobs = this.getPostEscapeGatherMaxMobs();
        this.blockAnimationsWhileRidingLivingEntities = this.blockAnimationsWhileRidingLivingEntities();
        this.blockAnimationsWhileRidingVehicles = this.blockAnimationsWhileRidingVehicles();
        this.attackOutcomeFailsafeThreshold = this.getAttackOutcomeFailsafeThreshold();
        this.filledStageOneSpeedMult = this.clampMultiplier(this.filledStageOneSpeedMult);
        this.filledStageTwoSpeedMult = this.clampMultiplier(this.filledStageTwoSpeedMult);
        this.filledStageThreeSpeedMult = this.clampMultiplier(this.filledStageThreeSpeedMult);
        this.filledStageOneJumpMult = this.clampMultiplier(this.filledStageOneJumpMult);
        this.filledStageTwoJumpMult = this.clampMultiplier(this.filledStageTwoJumpMult);
        this.filledStageThreeJumpMult = this.clampMultiplier(this.filledStageThreeJumpMult);
    }

    public static NonConfig load() {
        NonConfig cfg = new NonConfig();
        if (Files.isRegularFile(PATH, new LinkOption[0])) {
            try (BufferedReader reader = Files.newBufferedReader(PATH);){
                NonConfig fromDisk = (NonConfig)GSON.fromJson((Reader)reader, NonConfig.class);
                if (fromDisk != null) {
                    cfg = fromDisk;
                }
            }
            catch (IOException e) {
                NeedsOfNature.LOGGER.warn("[NoN] Failed to read config at {}. Using defaults.", (Object)PATH, (Object)e);
            }
        }
        cfg.ensureDefaults();
        cfg.save();
        return cfg;
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent(), new FileAttribute[0]);
            try (BufferedWriter writer = Files.newBufferedWriter(PATH, new OpenOption[0]);){
                GSON.toJson((Object)this, (Appendable)writer);
            }
        }
        catch (IOException e) {
            NeedsOfNature.LOGGER.warn("[NoN] Failed to save config at {}.", (Object)PATH, (Object)e);
        }
    }

    public static enum PlayerGenderSelection {
        MALE("male", 1),
        FEMALE("female", 2),
        BOTH("both", 3);

        private final String id;
        private final int mask;

        private PlayerGenderSelection(String id, int mask) {
            this.id = id;
            this.mask = mask;
        }

        public String id() {
            return this.id;
        }

        public int mask() {
            return this.mask;
        }

        public PlayerGenderSelection next() {
            return switch (this) {
                case MALE -> FEMALE;
                case FEMALE -> BOTH;
                case BOTH -> MALE;
            };
        }

        public static PlayerGenderSelection fromMask(int mask, PlayerGenderSelection fallback) {
            int normalized = mask & 3;
            for (PlayerGenderSelection value : PlayerGenderSelection.values()) {
                if (value.mask != normalized) continue;
                return value;
            }
            return fallback == null ? FEMALE : fallback;
        }

        public static PlayerGenderSelection fromId(String id, PlayerGenderSelection fallback) {
            if (id != null) {
                String normalized = id.trim().toLowerCase(Locale.ROOT);
                for (PlayerGenderSelection value : PlayerGenderSelection.values()) {
                    if (!value.id.equals(normalized)) continue;
                    return value;
                }
            }
            return fallback == null ? FEMALE : fallback;
        }
    }

    public static final class FemaleGenderDestroyedProfile {
        private float breastSize;
        private float separation;
        private float height;
        private float depth;
        private float rotation;
        private boolean breastPhysics;
        private boolean dualPhysics;
        private float intensity;
        private float momentum;

        public FemaleGenderDestroyedProfile() {
            this(0.8f, 0.0f, 0.5f, -0.5f, 0.0f, true, false, 0.25f, 0.625f);
        }

        public FemaleGenderDestroyedProfile(float breastSize, float separation, float height, float depth, float rotation, boolean breastPhysics, boolean dualPhysics, float intensity, float momentum) {
            this.breastSize = NonConfig.clampFloat(breastSize, 0.0f, 0.8f);
            this.separation = NonConfig.clampFloat(separation, -1.0f, 1.0f);
            this.height = NonConfig.clampFloat(height, -1.0f, 1.0f);
            this.depth = NonConfig.clampFloat(depth, -1.0f, 0.0f);
            this.rotation = NonConfig.clampFloat(rotation, 0.0f, 0.1f);
            this.breastPhysics = breastPhysics;
            this.dualPhysics = dualPhysics;
            this.intensity = NonConfig.clampFloat(intensity, 0.0f, 0.5f);
            this.momentum = NonConfig.clampFloat(momentum, 0.25f, 1.0f);
        }

        public static FemaleGenderDestroyedProfile lowDefault() {
            return new FemaleGenderDestroyedProfile(0.8f, 0.0f, 0.5f, -0.5f, 0.0f, true, false, 0.25f, 0.625f);
        }

        public static FemaleGenderDestroyedProfile highDefault() {
            return new FemaleGenderDestroyedProfile(0.8f, -0.1f, 0.2f, 0.0f, 0.06f, true, true, 0.5f, 1.0f);
        }

        public FemaleGenderDestroyedProfile sanitized() {
            return new FemaleGenderDestroyedProfile(this.breastSize, this.separation, this.height, this.depth, this.rotation, this.breastPhysics, this.dualPhysics, this.intensity, this.momentum);
        }

        public float breastSize() {
            return NonConfig.clampFloat(this.breastSize, 0.0f, 0.8f);
        }

        public float separation() {
            return NonConfig.clampFloat(this.separation, -1.0f, 1.0f);
        }

        public float height() {
            return NonConfig.clampFloat(this.height, -1.0f, 1.0f);
        }

        public float depth() {
            return NonConfig.clampFloat(this.depth, -1.0f, 0.0f);
        }

        public float rotation() {
            return NonConfig.clampFloat(this.rotation, 0.0f, 0.1f);
        }

        public boolean breastPhysics() {
            return this.breastPhysics;
        }

        public boolean dualPhysics() {
            return this.dualPhysics;
        }

        public float intensity() {
            return NonConfig.clampFloat(this.intensity, 0.0f, 0.5f);
        }

        public float momentum() {
            return NonConfig.clampFloat(this.momentum, 0.25f, 1.0f);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FemaleGenderDestroyedProfile)) {
                return false;
            }
            FemaleGenderDestroyedProfile other = (FemaleGenderDestroyedProfile)obj;
            return Float.compare(this.breastSize(), other.breastSize()) == 0 && Float.compare(this.separation(), other.separation()) == 0 && Float.compare(this.height(), other.height()) == 0 && Float.compare(this.depth(), other.depth()) == 0 && Float.compare(this.rotation(), other.rotation()) == 0 && this.breastPhysics() == other.breastPhysics() && this.dualPhysics() == other.dualPhysics() && Float.compare(this.intensity(), other.intensity()) == 0 && Float.compare(this.momentum(), other.momentum()) == 0;
        }

        public int hashCode() {
            int result = Float.hashCode(this.breastSize());
            result = 31 * result + Float.hashCode(this.separation());
            result = 31 * result + Float.hashCode(this.height());
            result = 31 * result + Float.hashCode(this.depth());
            result = 31 * result + Float.hashCode(this.rotation());
            result = 31 * result + Boolean.hashCode(this.breastPhysics());
            result = 31 * result + Boolean.hashCode(this.dualPhysics());
            result = 31 * result + Float.hashCode(this.intensity());
            result = 31 * result + Float.hashCode(this.momentum());
            return result;
        }
    }

    public static final class GenderSpawnChances {
        private int male;
        private int female;
        private int both;

        public GenderSpawnChances() {
            this(48, 47, 5);
        }

        public GenderSpawnChances(int male, int female, int both) {
            this.male = NonConfig.clampPercent(male);
            this.female = NonConfig.clampPercent(female);
            this.both = NonConfig.clampPercent(both);
        }

        public int male() {
            return NonConfig.clampPercent(this.male);
        }

        public int female() {
            return NonConfig.clampPercent(this.female);
        }

        public int both() {
            return NonConfig.clampPercent(this.both);
        }

        public boolean isValid() {
            return this.male() + this.female() + this.both() == 100;
        }

        public GenderSpawnChances sanitized() {
            return new GenderSpawnChances(this.male(), this.female(), this.both());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof GenderSpawnChances)) {
                return false;
            }
            GenderSpawnChances other = (GenderSpawnChances)obj;
            return this.male() == other.male() && this.female() == other.female() && this.both() == other.both();
        }

        public int hashCode() {
            int result = this.male();
            result = 31 * result + this.female();
            result = 31 * result + this.both();
            return result;
        }
    }

    public static final class OffspringCountRange {
        private int min;
        private int max;

        public OffspringCountRange() {
            this(1, 1);
        }

        public OffspringCountRange(int min, int max) {
            int safeMin = NonConfig.clampInt(min, 1, 16);
            int safeMax = NonConfig.clampInt(max, 1, 16);
            if (safeMax < safeMin) {
                safeMax = safeMin;
            }
            this.min = safeMin;
            this.max = safeMax;
        }

        public int min() {
            return NonConfig.clampInt(this.min, 1, 16);
        }

        public int max() {
            return Math.max(this.min(), NonConfig.clampInt(this.max, 1, 16));
        }

        public OffspringCountRange sanitized() {
            return new OffspringCountRange(this.min(), this.max());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof OffspringCountRange)) {
                return false;
            }
            OffspringCountRange other = (OffspringCountRange)obj;
            return this.min() == other.min() && this.max() == other.max();
        }

        public int hashCode() {
            int result = this.min();
            result = 31 * result + this.max();
            return result;
        }
    }

    public static final class EntityProfile {
        private Integer pregnancyChancePercent;
        private Integer offspringMin;
        private Integer offspringMax;
        private String birthEntity;
        private String birthMode;
        private GenderSpawnChances genderSpawn;
        private EggProfile egg;

        public EntityProfile() {
        }

        public EntityProfile(Integer pregnancyChancePercent, Integer offspringMin, Integer offspringMax, String birthEntity, String birthMode, GenderSpawnChances genderSpawn) {
            this(pregnancyChancePercent, offspringMin, offspringMax, birthEntity, birthMode, genderSpawn, null);
        }

        public EntityProfile(Integer pregnancyChancePercent, Integer offspringMin, Integer offspringMax, String birthEntity, String birthMode, GenderSpawnChances genderSpawn, EggProfile egg) {
            this.pregnancyChancePercent = pregnancyChancePercent;
            this.offspringMin = offspringMin;
            this.offspringMax = offspringMax;
            this.birthEntity = birthEntity;
            this.birthMode = birthMode;
            this.genderSpawn = genderSpawn;
            this.egg = egg;
        }

        public static EntityProfile full(int pregnancyChancePercent, int offspringMin, int offspringMax, String birthEntity, String birthMode, GenderSpawnChances genderSpawn) {
            return EntityProfile.full(pregnancyChancePercent, offspringMin, offspringMax, birthEntity, birthMode, genderSpawn, null);
        }

        public static EntityProfile full(int pregnancyChancePercent, int offspringMin, int offspringMax, String birthEntity, String birthMode, GenderSpawnChances genderSpawn, EggProfile egg) {
            return new EntityProfile(NonConfig.clampPercent(pregnancyChancePercent), NonConfig.clampInt(offspringMin, 1, 16), NonConfig.clampInt(offspringMax, 1, 16), NonConfig.normalizeEntityIdString(birthEntity), NonConfig.normalizeBirthMode(birthMode), NonConfig.validOrDefault(genderSpawn), egg == null ? null : egg.sanitized()).sanitized();
        }

        public Integer pregnancyChancePercent() {
            return this.pregnancyChancePercent == null ? null : Integer.valueOf(NonConfig.clampPercent(this.pregnancyChancePercent));
        }

        public Integer offspringMin() {
            return this.offspringMin == null ? null : Integer.valueOf(NonConfig.clampInt(this.offspringMin, 1, 16));
        }

        public Integer offspringMax() {
            return this.offspringMax == null ? null : Integer.valueOf(NonConfig.clampInt(this.offspringMax, 1, 16));
        }

        public String birthEntity() {
            return NonConfig.normalizeEntityIdString(this.birthEntity);
        }

        public String birthMode() {
            return NonConfig.normalizeBirthMode(this.birthMode);
        }

        public GenderSpawnChances genderSpawn() {
            return NonConfig.sanitizeGenderSpawnChances(this.genderSpawn);
        }

        public EggProfile egg() {
            EggProfile safe = this.egg == null ? null : this.egg.sanitized();
            return safe == null || safe.isEmpty() ? null : safe;
        }

        public EntityProfile sanitized() {
            Integer safeMin = this.offspringMin();
            Integer safeMax = this.offspringMax();
            if (safeMin != null && safeMax != null && safeMax < safeMin) {
                safeMax = safeMin;
            }
            return new EntityProfile(this.pregnancyChancePercent(), safeMin, safeMax, this.birthEntity(), this.birthMode(), this.genderSpawn(), this.egg());
        }

        public boolean isEmpty() {
            return this.pregnancyChancePercent() == null && this.offspringMin() == null && this.offspringMax() == null && this.birthEntity() == null && this.birthMode() == null && this.genderSpawn() == null && this.egg() == null;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof EntityProfile)) {
                return false;
            }
            EntityProfile other = (EntityProfile)obj;
            EntityProfile a = this.sanitized();
            EntityProfile b = other.sanitized();
            return Objects.equals(a.pregnancyChancePercent, b.pregnancyChancePercent) && Objects.equals(a.offspringMin, b.offspringMin) && Objects.equals(a.offspringMax, b.offspringMax) && Objects.equals(a.birthEntity, b.birthEntity) && Objects.equals(a.birthMode, b.birthMode) && Objects.equals(a.genderSpawn, b.genderSpawn) && Objects.equals(a.egg, b.egg);
        }

        public int hashCode() {
            EntityProfile safe = this.sanitized();
            return Objects.hash(safe.pregnancyChancePercent, safe.offspringMin, safe.offspringMax, safe.birthEntity, safe.birthMode, safe.genderSpawn, safe.egg);
        }
    }

    public static final class EggProfile {
        private Float startSize;
        private Float endSize;
        private String texture;
        private Float health;

        public EggProfile() {
        }

        public EggProfile(Float startSize, Float endSize, String texture, Float health) {
            this.startSize = startSize;
            this.endSize = endSize;
            this.texture = texture;
            this.health = health;
        }

        public static EggProfile defaults() {
            return new EggProfile(Float.valueOf(0.5f), Float.valueOf(1.0f), null, Float.valueOf(2.0f));
        }

        public Float startSize() {
            return this.startSize == null ? null : Float.valueOf(NonConfig.clampFloat(this.startSize.floatValue(), 0.05f, 4.0f));
        }

        public Float endSize() {
            return this.endSize == null ? null : Float.valueOf(NonConfig.clampFloat(this.endSize.floatValue(), 0.05f, 4.0f));
        }

        public String texture() {
            return NonConfig.normalizeTextureIdString(this.texture);
        }

        public Float health() {
            return this.health == null ? null : Float.valueOf(NonConfig.clampFloat(this.health.floatValue(), 0.5f, 100.0f));
        }

        public EggProfile sanitized() {
            return new EggProfile(this.startSize(), this.endSize(), this.texture(), this.health());
        }

        public EggProfile resolvedOver(EggProfile fallback) {
            EggProfile base = fallback == null ? EggProfile.defaults() : fallback.sanitized();
            EggProfile safe = this.sanitized();
            return new EggProfile(safe.startSize() == null ? base.startSize() : safe.startSize(), safe.endSize() == null ? base.endSize() : safe.endSize(), safe.texture() == null ? base.texture() : safe.texture(), safe.health() == null ? base.health() : safe.health()).sanitized();
        }

        public boolean isEmpty() {
            return this.startSize() == null && this.endSize() == null && this.texture() == null && this.health() == null;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof EggProfile)) {
                return false;
            }
            EggProfile other = (EggProfile)obj;
            EggProfile a = this.sanitized();
            EggProfile b = other.sanitized();
            return Objects.equals(a.startSize, b.startSize) && Objects.equals(a.endSize, b.endSize) && Objects.equals(a.texture, b.texture) && Objects.equals(a.health, b.health);
        }

        public int hashCode() {
            EggProfile safe = this.sanitized();
            return Objects.hash(safe.startSize, safe.endSize, safe.texture, safe.health);
        }
    }
}

