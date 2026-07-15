/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_342
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_7919
 */
package com.nonid.integration;

import com.nonid.NonConfig;
import com.nonid.client.NonHudOverlay;
import com.nonid.integration.NonModMenuDebugScreens;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import java.util.Locale;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_7919;

final class NonModMenuGameplayScreens {
    private NonModMenuGameplayScreens() {
    }

    static class GameplayDifficultyScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private int attackHits;
        private double attackDecay;
        private int attackEscapeDamageDifficultyPercent;
        private int attackEscapeInvulnerabilitySeconds;
        private int attackEscapeAnimationProtectionSeconds;
        private int postEscapeGatherMaxMobs;
        private boolean blockAnimationsWhileRidingLivingEntities;
        private boolean blockAnimationsWhileRidingVehicles;
        private int attackOutcomeFailsafeThreshold;
        private boolean lastDefeatedEnabled;
        private int lastDefeatedEnergyThreshold;
        private int lastDefeatedSearchRadius;
        private int lastDefeatedCooldownSeconds;
        private boolean allowCraftingTableSkinRepair;
        private boolean keepMessAfterDeath;
        private boolean keepRippedSkinAfterDeath;
        private boolean keepLiquidTankAfterDeath;
        private boolean keepPregnancyAfterDeath;
        private class_342 attackHitsField;
        private class_342 attackDecayField;
        private class_342 attackEscapeDamageDifficultyPercentField;
        private class_342 attackEscapeInvulnerabilitySecondsField;
        private class_342 attackEscapeAnimationProtectionSecondsField;
        private class_342 postEscapeGatherMaxMobsField;
        private class_342 attackOutcomeFailsafeThresholdField;
        private class_342 lastDefeatedEnergyThresholdField;
        private class_342 lastDefeatedSearchRadiusField;
        private class_342 lastDefeatedCooldownSecondsField;
        private class_4185 resetAttackHitsButton;
        private class_4185 resetAttackDecayButton;
        private class_4185 resetAttackEscapeDamageDifficultyPercentButton;
        private class_4185 resetAttackEscapeInvulnerabilitySecondsButton;
        private class_4185 resetAttackEscapeAnimationProtectionSecondsButton;
        private class_4185 resetPostEscapeGatherMaxMobsButton;
        private class_4185 blockAnimationsWhileRidingLivingEntitiesButton;
        private class_4185 resetBlockAnimationsWhileRidingLivingEntitiesButton;
        private class_4185 blockAnimationsWhileRidingVehiclesButton;
        private class_4185 resetBlockAnimationsWhileRidingVehiclesButton;
        private class_4185 resetAttackOutcomeFailsafeThresholdButton;
        private class_4185 lastDefeatedEnabledButton;
        private class_4185 resetLastDefeatedEnabledButton;
        private class_4185 resetLastDefeatedEnergyThresholdButton;
        private class_4185 resetLastDefeatedSearchRadiusButton;
        private class_4185 resetLastDefeatedCooldownSecondsButton;
        private class_4185 allowCraftingTableSkinRepairButton;
        private class_4185 resetAllowCraftingTableSkinRepairButton;
        private class_4185 keepMessAfterDeathButton;
        private class_4185 resetKeepMessAfterDeathButton;
        private class_4185 keepRippedSkinAfterDeathButton;
        private class_4185 resetKeepRippedSkinAfterDeathButton;
        private class_4185 keepLiquidTankAfterDeathButton;
        private class_4185 resetKeepLiquidTankAfterDeathButton;
        private class_4185 keepPregnancyAfterDeathButton;
        private class_4185 resetKeepPregnancyAfterDeathButton;
        private boolean serverConfigEditable;

        protected GameplayDifficultyScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.section.difficulty"));
            this.parent = parent;
            this.config = config;
            this.attackHits = NonHudOverlay.getRuntimeAttackEscapeHits();
            this.attackDecay = NonHudOverlay.getRuntimeAttackDecayPerSecond();
            this.attackEscapeDamageDifficultyPercent = NonHudOverlay.getRuntimeAttackEscapeDamageDifficultyPercent();
            this.attackEscapeInvulnerabilitySeconds = config.getAttackEscapeInvulnerabilitySeconds();
            this.attackEscapeAnimationProtectionSeconds = config.getAttackEscapeAnimationProtectionSeconds();
            this.postEscapeGatherMaxMobs = config.getPostEscapeGatherMaxMobs();
            this.blockAnimationsWhileRidingLivingEntities = config.blockAnimationsWhileRidingLivingEntities();
            this.blockAnimationsWhileRidingVehicles = config.blockAnimationsWhileRidingVehicles();
            this.attackOutcomeFailsafeThreshold = config.getAttackOutcomeFailsafeThreshold();
            this.lastDefeatedEnabled = config.isLastDefeatedEnabled();
            this.lastDefeatedEnergyThreshold = config.getLastDefeatedEnergyThreshold();
            this.lastDefeatedSearchRadius = config.getLastDefeatedSearchRadius();
            this.lastDefeatedCooldownSeconds = config.getLastDefeatedCooldownSeconds();
            this.allowCraftingTableSkinRepair = config.isCraftingTableSkinRepairAllowed();
            this.keepMessAfterDeath = config.keepMessAfterDeath();
            this.keepRippedSkinAfterDeath = config.keepRippedSkinAfterDeath();
            this.keepLiquidTankAfterDeath = config.keepLiquidTankAfterDeath();
            this.keepPregnancyAfterDeath = config.keepPregnancyAfterDeath();
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.attackHitsField != null) {
                this.attackHits = this.parseField(this.attackHitsField, this.attackHits);
                this.attackDecay = this.parseDecimalField(this.attackDecayField, this.attackDecay);
                this.attackEscapeDamageDifficultyPercent = this.parseField(this.attackEscapeDamageDifficultyPercentField, this.attackEscapeDamageDifficultyPercent);
                this.attackEscapeInvulnerabilitySeconds = this.parseField(this.attackEscapeInvulnerabilitySecondsField, this.attackEscapeInvulnerabilitySeconds);
                this.attackEscapeAnimationProtectionSeconds = this.parseField(this.attackEscapeAnimationProtectionSecondsField, this.attackEscapeAnimationProtectionSeconds);
                this.postEscapeGatherMaxMobs = this.parseField(this.postEscapeGatherMaxMobsField, this.postEscapeGatherMaxMobs);
                this.attackOutcomeFailsafeThreshold = this.parseField(this.attackOutcomeFailsafeThresholdField, this.attackOutcomeFailsafeThreshold);
                this.lastDefeatedEnergyThreshold = this.parseField(this.lastDefeatedEnergyThresholdField, this.lastDefeatedEnergyThreshold);
                this.lastDefeatedSearchRadius = this.parseField(this.lastDefeatedSearchRadiusField, this.lastDefeatedSearchRadius);
                this.lastDefeatedCooldownSeconds = this.parseField(this.lastDefeatedCooldownSecondsField, this.lastDefeatedCooldownSeconds);
            }
            int listTop = 32;
            int bottomArea = 40;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.attack_escape")));
            this.attackHitsField = this.newNumberField(fieldWidth, this.attackHits);
            this.resetAttackHitsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.attackHitsField.method_1852(String.valueOf(this.defaults.getAttackEscapeHits()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetAttackHitsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.attack_hits"), (class_339)this.attackHitsField, (class_339)this.resetAttackHitsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_hits")));
            this.attackDecayField = this.newDecimalField(fieldWidth, this.attackDecay);
            this.resetAttackDecayButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.attackDecayField.method_1852(String.valueOf(this.defaults.getAttackDecayPerSecond()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetAttackDecayButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.attack_decay"), (class_339)this.attackDecayField, (class_339)this.resetAttackDecayButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_decay")));
            this.attackEscapeDamageDifficultyPercentField = this.newNumberField(fieldWidth, this.attackEscapeDamageDifficultyPercent);
            this.resetAttackEscapeDamageDifficultyPercentButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.attackEscapeDamageDifficultyPercentField.method_1852(String.valueOf(this.defaults.getAttackEscapeDamageDifficultyPercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetAttackEscapeDamageDifficultyPercentButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.attack_escape_damage_difficulty_percent"), (class_339)this.attackEscapeDamageDifficultyPercentField, (class_339)this.resetAttackEscapeDamageDifficultyPercentButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_escape_damage_difficulty_percent")));
            this.attackEscapeInvulnerabilitySecondsField = this.newNumberField(fieldWidth, this.attackEscapeInvulnerabilitySeconds);
            this.resetAttackEscapeInvulnerabilitySecondsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.attackEscapeInvulnerabilitySecondsField.method_1852(String.valueOf(this.defaults.getAttackEscapeInvulnerabilitySeconds()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetAttackEscapeInvulnerabilitySecondsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.attack_escape_invulnerability_seconds"), (class_339)this.attackEscapeInvulnerabilitySecondsField, (class_339)this.resetAttackEscapeInvulnerabilitySecondsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_escape_invulnerability_seconds")));
            this.attackEscapeAnimationProtectionSecondsField = this.newNumberField(fieldWidth, this.attackEscapeAnimationProtectionSeconds);
            this.resetAttackEscapeAnimationProtectionSecondsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.attackEscapeAnimationProtectionSecondsField.method_1852(String.valueOf(this.defaults.getAttackEscapeAnimationProtectionSeconds()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetAttackEscapeAnimationProtectionSecondsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.attack_escape_animation_protection_seconds"), (class_339)this.attackEscapeAnimationProtectionSecondsField, (class_339)this.resetAttackEscapeAnimationProtectionSecondsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_escape_animation_protection_seconds")));
            this.postEscapeGatherMaxMobsField = this.newNumberField(fieldWidth, this.postEscapeGatherMaxMobs);
            this.resetPostEscapeGatherMaxMobsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.postEscapeGatherMaxMobsField.method_1852(String.valueOf(this.defaults.getPostEscapeGatherMaxMobs()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPostEscapeGatherMaxMobsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.post_escape_gather_max_mobs"), (class_339)this.postEscapeGatherMaxMobsField, (class_339)this.resetPostEscapeGatherMaxMobsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.post_escape_gather_max_mobs")));
            this.attackOutcomeFailsafeThresholdField = this.newNumberField(fieldWidth, this.attackOutcomeFailsafeThreshold);
            this.resetAttackOutcomeFailsafeThresholdButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.attackOutcomeFailsafeThresholdField.method_1852(String.valueOf(this.defaults.getAttackOutcomeFailsafeThreshold()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetAttackOutcomeFailsafeThresholdButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.attack_outcome_failsafe_threshold"), (class_339)this.attackOutcomeFailsafeThresholdField, (class_339)this.resetAttackOutcomeFailsafeThresholdButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_outcome_failsafe_threshold")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.mount_protection")));
            this.blockAnimationsWhileRidingLivingEntitiesButton = class_4185.method_46430((class_2561)this.onOffLabel(this.blockAnimationsWhileRidingLivingEntities), button -> {
                this.blockAnimationsWhileRidingLivingEntities = !this.blockAnimationsWhileRidingLivingEntities;
                button.method_25355(this.onOffLabel(this.blockAnimationsWhileRidingLivingEntities));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.blockAnimationsWhileRidingLivingEntitiesButton, "config.needsofnature.tooltip.block_animations_while_riding_living_entities");
            this.resetBlockAnimationsWhileRidingLivingEntitiesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.blockAnimationsWhileRidingLivingEntities = this.defaults.blockAnimationsWhileRidingLivingEntities();
                this.blockAnimationsWhileRidingLivingEntitiesButton.method_25355(this.onOffLabel(this.blockAnimationsWhileRidingLivingEntities));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetBlockAnimationsWhileRidingLivingEntitiesButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.block_animations_while_riding_living_entities"), (class_339)this.blockAnimationsWhileRidingLivingEntitiesButton, (class_339)this.resetBlockAnimationsWhileRidingLivingEntitiesButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.block_animations_while_riding_living_entities")));
            this.blockAnimationsWhileRidingVehiclesButton = class_4185.method_46430((class_2561)this.onOffLabel(this.blockAnimationsWhileRidingVehicles), button -> {
                this.blockAnimationsWhileRidingVehicles = !this.blockAnimationsWhileRidingVehicles;
                button.method_25355(this.onOffLabel(this.blockAnimationsWhileRidingVehicles));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.blockAnimationsWhileRidingVehiclesButton, "config.needsofnature.tooltip.block_animations_while_riding_vehicles");
            this.resetBlockAnimationsWhileRidingVehiclesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.blockAnimationsWhileRidingVehicles = this.defaults.blockAnimationsWhileRidingVehicles();
                this.blockAnimationsWhileRidingVehiclesButton.method_25355(this.onOffLabel(this.blockAnimationsWhileRidingVehicles));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetBlockAnimationsWhileRidingVehiclesButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.block_animations_while_riding_vehicles"), (class_339)this.blockAnimationsWhileRidingVehiclesButton, (class_339)this.resetBlockAnimationsWhileRidingVehiclesButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.block_animations_while_riding_vehicles")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.last_defeated")));
            this.lastDefeatedEnabledButton = class_4185.method_46430((class_2561)this.onOffLabel(this.lastDefeatedEnabled), button -> {
                this.lastDefeatedEnabled = !this.lastDefeatedEnabled;
                button.method_25355(this.onOffLabel(this.lastDefeatedEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetLastDefeatedEnabledButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.lastDefeatedEnabled = this.defaults.isLastDefeatedEnabled();
                this.lastDefeatedEnabledButton.method_25355(this.onOffLabel(this.lastDefeatedEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetLastDefeatedEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.last_defeated_enabled"), (class_339)this.lastDefeatedEnabledButton, (class_339)this.resetLastDefeatedEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.last_defeated_enabled")));
            this.lastDefeatedEnergyThresholdField = this.newNumberField(fieldWidth, this.lastDefeatedEnergyThreshold);
            this.resetLastDefeatedEnergyThresholdButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.lastDefeatedEnergyThresholdField.method_1852(String.valueOf(this.defaults.getLastDefeatedEnergyThreshold()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetLastDefeatedEnergyThresholdButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.last_defeated_energy_threshold"), (class_339)this.lastDefeatedEnergyThresholdField, (class_339)this.resetLastDefeatedEnergyThresholdButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.last_defeated_energy_threshold")));
            this.lastDefeatedSearchRadiusField = this.newNumberField(fieldWidth, this.lastDefeatedSearchRadius);
            this.resetLastDefeatedSearchRadiusButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.lastDefeatedSearchRadiusField.method_1852(String.valueOf(this.defaults.getLastDefeatedSearchRadius()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetLastDefeatedSearchRadiusButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.last_defeated_search_radius"), (class_339)this.lastDefeatedSearchRadiusField, (class_339)this.resetLastDefeatedSearchRadiusButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.last_defeated_search_radius")));
            this.lastDefeatedCooldownSecondsField = this.newNumberField(fieldWidth, this.lastDefeatedCooldownSeconds);
            this.resetLastDefeatedCooldownSecondsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.lastDefeatedCooldownSecondsField.method_1852(String.valueOf(this.defaults.getLastDefeatedCooldownSeconds()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetLastDefeatedCooldownSecondsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.last_defeated_cooldown_seconds"), (class_339)this.lastDefeatedCooldownSecondsField, (class_339)this.resetLastDefeatedCooldownSecondsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.last_defeated_cooldown_seconds")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.ripped_skin_repair")));
            this.allowCraftingTableSkinRepairButton = class_4185.method_46430((class_2561)this.onOffLabel(this.allowCraftingTableSkinRepair), button -> {
                this.allowCraftingTableSkinRepair = !this.allowCraftingTableSkinRepair;
                button.method_25355(this.onOffLabel(this.allowCraftingTableSkinRepair));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.allowCraftingTableSkinRepairButton, "config.needsofnature.tooltip.allow_crafting_table_skin_repair");
            this.resetAllowCraftingTableSkinRepairButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.allowCraftingTableSkinRepair = this.defaults.isCraftingTableSkinRepairAllowed();
                this.allowCraftingTableSkinRepairButton.method_25355(this.onOffLabel(this.allowCraftingTableSkinRepair));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetAllowCraftingTableSkinRepairButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.allow_crafting_table_skin_repair"), (class_339)this.allowCraftingTableSkinRepairButton, (class_339)this.resetAllowCraftingTableSkinRepairButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.allow_crafting_table_skin_repair")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.death_persistence")));
            this.keepMessAfterDeathButton = class_4185.method_46430((class_2561)this.onOffLabel(this.keepMessAfterDeath), button -> {
                this.keepMessAfterDeath = !this.keepMessAfterDeath;
                button.method_25355(this.onOffLabel(this.keepMessAfterDeath));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.keepMessAfterDeathButton, "config.needsofnature.tooltip.keep_mess_after_death");
            this.resetKeepMessAfterDeathButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.keepMessAfterDeath = this.defaults.keepMessAfterDeath();
                this.keepMessAfterDeathButton.method_25355(this.onOffLabel(this.keepMessAfterDeath));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetKeepMessAfterDeathButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.keep_mess_after_death"), (class_339)this.keepMessAfterDeathButton, (class_339)this.resetKeepMessAfterDeathButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.keep_mess_after_death")));
            this.keepRippedSkinAfterDeathButton = class_4185.method_46430((class_2561)this.onOffLabel(this.keepRippedSkinAfterDeath), button -> {
                this.keepRippedSkinAfterDeath = !this.keepRippedSkinAfterDeath;
                button.method_25355(this.onOffLabel(this.keepRippedSkinAfterDeath));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.keepRippedSkinAfterDeathButton, "config.needsofnature.tooltip.keep_ripped_skin_after_death");
            this.resetKeepRippedSkinAfterDeathButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.keepRippedSkinAfterDeath = this.defaults.keepRippedSkinAfterDeath();
                this.keepRippedSkinAfterDeathButton.method_25355(this.onOffLabel(this.keepRippedSkinAfterDeath));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetKeepRippedSkinAfterDeathButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.keep_ripped_skin_after_death"), (class_339)this.keepRippedSkinAfterDeathButton, (class_339)this.resetKeepRippedSkinAfterDeathButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.keep_ripped_skin_after_death")));
            this.keepLiquidTankAfterDeathButton = class_4185.method_46430((class_2561)this.onOffLabel(this.keepLiquidTankAfterDeath), button -> {
                this.keepLiquidTankAfterDeath = !this.keepLiquidTankAfterDeath;
                button.method_25355(this.onOffLabel(this.keepLiquidTankAfterDeath));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.keepLiquidTankAfterDeathButton, "config.needsofnature.tooltip.keep_liquid_tank_after_death");
            this.resetKeepLiquidTankAfterDeathButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.keepLiquidTankAfterDeath = this.defaults.keepLiquidTankAfterDeath();
                this.keepLiquidTankAfterDeathButton.method_25355(this.onOffLabel(this.keepLiquidTankAfterDeath));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetKeepLiquidTankAfterDeathButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.keep_liquid_tank_after_death"), (class_339)this.keepLiquidTankAfterDeathButton, (class_339)this.resetKeepLiquidTankAfterDeathButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.keep_liquid_tank_after_death")));
            this.keepPregnancyAfterDeathButton = class_4185.method_46430((class_2561)this.onOffLabel(this.keepPregnancyAfterDeath), button -> {
                this.keepPregnancyAfterDeath = !this.keepPregnancyAfterDeath;
                button.method_25355(this.onOffLabel(this.keepPregnancyAfterDeath));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.keepPregnancyAfterDeathButton, "config.needsofnature.tooltip.keep_pregnancy_after_death");
            this.resetKeepPregnancyAfterDeathButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.keepPregnancyAfterDeath = this.defaults.keepPregnancyAfterDeath();
                this.keepPregnancyAfterDeathButton.method_25355(this.onOffLabel(this.keepPregnancyAfterDeath));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetKeepPregnancyAfterDeathButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.keep_pregnancy_after_death"), (class_339)this.keepPregnancyAfterDeathButton, (class_339)this.resetKeepPregnancyAfterDeathButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.keep_pregnancy_after_death")));
            this.attackHitsField.method_1863(ignored -> this.updateResetButtons());
            this.attackDecayField.method_1863(ignored -> this.updateResetButtons());
            this.attackEscapeDamageDifficultyPercentField.method_1863(ignored -> this.updateResetButtons());
            this.attackEscapeInvulnerabilitySecondsField.method_1863(ignored -> this.updateResetButtons());
            this.attackEscapeAnimationProtectionSecondsField.method_1863(ignored -> this.updateResetButtons());
            this.postEscapeGatherMaxMobsField.method_1863(ignored -> this.updateResetButtons());
            this.attackOutcomeFailsafeThresholdField.method_1863(ignored -> this.updateResetButtons());
            this.lastDefeatedEnergyThresholdField.method_1863(ignored -> this.updateResetButtons());
            this.lastDefeatedSearchRadiusField.method_1863(ignored -> this.updateResetButtons());
            this.lastDefeatedCooldownSecondsField.method_1863(ignored -> this.updateResetButtons());
            this.attackHitsField.method_1888(this.serverConfigEditable);
            this.attackDecayField.method_1888(this.serverConfigEditable);
            this.attackEscapeDamageDifficultyPercentField.method_1888(this.serverConfigEditable);
            this.attackEscapeInvulnerabilitySecondsField.method_1888(this.serverConfigEditable);
            this.attackEscapeAnimationProtectionSecondsField.method_1888(this.serverConfigEditable);
            this.postEscapeGatherMaxMobsField.method_1888(this.serverConfigEditable);
            this.attackOutcomeFailsafeThresholdField.method_1888(this.serverConfigEditable);
            this.lastDefeatedEnergyThresholdField.method_1888(this.serverConfigEditable);
            this.lastDefeatedSearchRadiusField.method_1888(this.serverConfigEditable);
            this.lastDefeatedCooldownSecondsField.method_1888(this.serverConfigEditable);
            if (this.lastDefeatedEnabledButton != null) {
                this.lastDefeatedEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.blockAnimationsWhileRidingLivingEntitiesButton != null) {
                this.blockAnimationsWhileRidingLivingEntitiesButton.field_22763 = this.serverConfigEditable;
            }
            if (this.blockAnimationsWhileRidingVehiclesButton != null) {
                this.blockAnimationsWhileRidingVehiclesButton.field_22763 = this.serverConfigEditable;
            }
            if (this.allowCraftingTableSkinRepairButton != null) {
                this.allowCraftingTableSkinRepairButton.field_22763 = this.serverConfigEditable;
            }
            if (this.keepMessAfterDeathButton != null) {
                this.keepMessAfterDeathButton.field_22763 = this.serverConfigEditable;
            }
            if (this.keepRippedSkinAfterDeathButton != null) {
                this.keepRippedSkinAfterDeathButton.field_22763 = this.serverConfigEditable;
            }
            if (this.keepLiquidTankAfterDeathButton != null) {
                this.keepLiquidTankAfterDeathButton.field_22763 = this.serverConfigEditable;
            }
            if (this.keepPregnancyAfterDeathButton != null) {
                this.keepPregnancyAfterDeathButton.field_22763 = this.serverConfigEditable;
            }
            this.updateResetButtons();
            int centerX = this.field_22789 / 2;
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> this.saveAndClose()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.method_37063((class_364)doneButton);
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
        }

        public void method_25419() {
            this.saveAndClose();
        }

        private void saveAndClose() {
            if (this.serverConfigEditable) {
                this.attackHits = this.clampAttackHits(this.parseField(this.attackHitsField, this.attackHits));
                this.attackDecay = this.clampAttackDecay(this.parseDecimalField(this.attackDecayField, this.attackDecay));
                this.attackEscapeDamageDifficultyPercent = this.clampAttackEscapeDamageDifficultyPercent(this.parseField(this.attackEscapeDamageDifficultyPercentField, this.attackEscapeDamageDifficultyPercent));
                this.attackEscapeInvulnerabilitySeconds = this.clampAttackEscapeProtectionSeconds(this.parseField(this.attackEscapeInvulnerabilitySecondsField, this.attackEscapeInvulnerabilitySeconds));
                this.attackEscapeAnimationProtectionSeconds = this.clampAttackEscapeProtectionSeconds(this.parseField(this.attackEscapeAnimationProtectionSecondsField, this.attackEscapeAnimationProtectionSeconds));
                this.postEscapeGatherMaxMobs = this.clampPostEscapeGatherMaxMobs(this.parseField(this.postEscapeGatherMaxMobsField, this.postEscapeGatherMaxMobs));
                this.attackOutcomeFailsafeThreshold = this.clampAttackOutcomeFailsafeThreshold(this.parseField(this.attackOutcomeFailsafeThresholdField, this.attackOutcomeFailsafeThreshold));
                this.lastDefeatedEnergyThreshold = this.clampLastDefeatedEnergyThreshold(this.parseField(this.lastDefeatedEnergyThresholdField, this.lastDefeatedEnergyThreshold));
                this.lastDefeatedSearchRadius = this.clampLastDefeatedSearchRadius(this.parseField(this.lastDefeatedSearchRadiusField, this.lastDefeatedSearchRadius));
                this.lastDefeatedCooldownSeconds = this.clampLastDefeatedCooldownSeconds(this.parseField(this.lastDefeatedCooldownSecondsField, this.lastDefeatedCooldownSeconds));
                this.config.setAttackEscapeHits(this.attackHits);
                this.config.setAttackDecayPerSecond(this.attackDecay);
                this.config.setAttackEscapeDamageDifficultyPercent(this.attackEscapeDamageDifficultyPercent);
                this.config.setAttackEscapeInvulnerabilitySeconds(this.attackEscapeInvulnerabilitySeconds);
                this.config.setAttackEscapeAnimationProtectionSeconds(this.attackEscapeAnimationProtectionSeconds);
                this.config.setPostEscapeGatherMaxMobs(this.postEscapeGatherMaxMobs);
                this.config.setBlockAnimationsWhileRidingLivingEntities(this.blockAnimationsWhileRidingLivingEntities);
                this.config.setBlockAnimationsWhileRidingVehicles(this.blockAnimationsWhileRidingVehicles);
                this.config.setAttackOutcomeFailsafeThreshold(this.attackOutcomeFailsafeThreshold);
                this.config.setLastDefeatedEnabled(this.lastDefeatedEnabled);
                this.config.setLastDefeatedEnergyThreshold(this.lastDefeatedEnergyThreshold);
                this.config.setLastDefeatedSearchRadius(this.lastDefeatedSearchRadius);
                this.config.setLastDefeatedCooldownSeconds(this.lastDefeatedCooldownSeconds);
                this.config.setCraftingTableSkinRepairAllowed(this.allowCraftingTableSkinRepair);
                this.config.setKeepMessAfterDeath(this.keepMessAfterDeath);
                this.config.setKeepRippedSkinAfterDeath(this.keepRippedSkinAfterDeath);
                this.config.setKeepLiquidTankAfterDeath(this.keepLiquidTankAfterDeath);
                this.config.setKeepPregnancyAfterDeath(this.keepPregnancyAfterDeath);
                this.config.save();
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
            }
            class_310.method_1551().method_1507(this.parent);
        }

        private class_342 newNumberField(int w, int initial) {
            class_342 field = new class_342(this.field_22793, 0, 0, w, 20, (class_2561)class_2561.method_43473());
            field.method_1852(String.valueOf(initial));
            field.method_1880(8);
            field.method_1888(true);
            return field;
        }

        private class_342 newDecimalField(int w, double initial) {
            class_342 field = new class_342(this.field_22793, 0, 0, w, 20, (class_2561)class_2561.method_43473());
            int maxLen = 16;
            field.method_1880(maxLen);
            field.method_1852(this.trimToMax(this.formatDecimal(initial), maxLen));
            field.method_1888(true);
            return field;
        }

        private int parseField(class_342 field, int fallback) {
            try {
                return Integer.parseInt(field.method_1882().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private double parseDecimalField(class_342 field, double fallback) {
            try {
                return Double.parseDouble(field.method_1882().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private String formatDecimal(double value) {
            String text = String.format(Locale.ROOT, "%.3f", value);
            int dot = text.indexOf(46);
            if (dot >= 0) {
                int end;
                for (end = text.length(); end > dot + 1 && text.charAt(end - 1) == '0'; --end) {
                }
                if (end == dot + 1) {
                    end = dot;
                }
                text = text.substring(0, end);
            }
            return text;
        }

        private String trimToMax(String value, int max) {
            if (value == null) {
                return "";
            }
            return value.length() > max ? value.substring(0, max) : value;
        }

        private int clampAttackHits(int v) {
            return Math.max(1, Math.min(50, v));
        }

        private double clampAttackDecay(double v) {
            return Math.max(0.0, Math.min(5.0, v));
        }

        private int clampAttackEscapeDamageDifficultyPercent(int v) {
            return Math.max(0, Math.min(200, v));
        }

        private int clampAttackOutcomeFailsafeThreshold(int v) {
            return Math.max(1, Math.min(50, v));
        }

        private int clampAttackEscapeProtectionSeconds(int v) {
            return Math.max(0, Math.min(10, v));
        }

        private int clampPostEscapeGatherMaxMobs(int v) {
            return Math.max(0, Math.min(64, v));
        }

        private int clampLastDefeatedEnergyThreshold(int v) {
            return Math.max(0, Math.min(200, v));
        }

        private int clampLastDefeatedSearchRadius(int v) {
            return Math.max(1, Math.min(32, v));
        }

        private int clampLastDefeatedCooldownSeconds(int v) {
            return Math.max(0, Math.min(3600, v));
        }

        private class_2561 onOffLabel(boolean enabled) {
            return class_2561.method_43471((String)(enabled ? "options.on" : "options.off"));
        }

        private boolean isIntFieldChanged(class_342 field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseField(field, defaultValue) != defaultValue;
        }

        private boolean isDoubleFieldChanged(class_342 field, double defaultValue) {
            if (field == null) {
                return false;
            }
            double value = this.parseDecimalField(field, defaultValue);
            return Math.abs(value - defaultValue) >= 1.0E-4;
        }

        private void updateResetButtons() {
            if (this.resetAttackHitsButton != null) {
                boolean bl = this.resetAttackHitsButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.attackHitsField, this.defaults.getAttackEscapeHits());
            }
            if (this.resetAttackDecayButton != null) {
                boolean bl = this.resetAttackDecayButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.attackDecayField, this.defaults.getAttackDecayPerSecond());
            }
            if (this.resetAttackEscapeDamageDifficultyPercentButton != null) {
                boolean bl = this.resetAttackEscapeDamageDifficultyPercentButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.attackEscapeDamageDifficultyPercentField, this.defaults.getAttackEscapeDamageDifficultyPercent());
            }
            if (this.resetAttackEscapeInvulnerabilitySecondsButton != null) {
                boolean bl = this.resetAttackEscapeInvulnerabilitySecondsButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.attackEscapeInvulnerabilitySecondsField, this.defaults.getAttackEscapeInvulnerabilitySeconds());
            }
            if (this.resetAttackEscapeAnimationProtectionSecondsButton != null) {
                boolean bl = this.resetAttackEscapeAnimationProtectionSecondsButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.attackEscapeAnimationProtectionSecondsField, this.defaults.getAttackEscapeAnimationProtectionSeconds());
            }
            if (this.resetPostEscapeGatherMaxMobsButton != null) {
                boolean bl = this.resetPostEscapeGatherMaxMobsButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.postEscapeGatherMaxMobsField, this.defaults.getPostEscapeGatherMaxMobs());
            }
            if (this.resetAttackOutcomeFailsafeThresholdButton != null) {
                boolean bl = this.resetAttackOutcomeFailsafeThresholdButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.attackOutcomeFailsafeThresholdField, this.defaults.getAttackOutcomeFailsafeThreshold());
            }
            if (this.resetBlockAnimationsWhileRidingLivingEntitiesButton != null) {
                boolean bl = this.resetBlockAnimationsWhileRidingLivingEntitiesButton.field_22763 = this.serverConfigEditable && this.blockAnimationsWhileRidingLivingEntities != this.defaults.blockAnimationsWhileRidingLivingEntities();
            }
            if (this.resetBlockAnimationsWhileRidingVehiclesButton != null) {
                boolean bl = this.resetBlockAnimationsWhileRidingVehiclesButton.field_22763 = this.serverConfigEditable && this.blockAnimationsWhileRidingVehicles != this.defaults.blockAnimationsWhileRidingVehicles();
            }
            if (this.resetLastDefeatedEnabledButton != null) {
                boolean bl = this.resetLastDefeatedEnabledButton.field_22763 = this.serverConfigEditable && this.lastDefeatedEnabled != this.defaults.isLastDefeatedEnabled();
            }
            if (this.resetLastDefeatedEnergyThresholdButton != null) {
                boolean bl = this.resetLastDefeatedEnergyThresholdButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.lastDefeatedEnergyThresholdField, this.defaults.getLastDefeatedEnergyThreshold());
            }
            if (this.resetLastDefeatedSearchRadiusButton != null) {
                boolean bl = this.resetLastDefeatedSearchRadiusButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.lastDefeatedSearchRadiusField, this.defaults.getLastDefeatedSearchRadius());
            }
            if (this.resetLastDefeatedCooldownSecondsButton != null) {
                boolean bl = this.resetLastDefeatedCooldownSecondsButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.lastDefeatedCooldownSecondsField, this.defaults.getLastDefeatedCooldownSeconds());
            }
            if (this.resetAllowCraftingTableSkinRepairButton != null) {
                boolean bl = this.resetAllowCraftingTableSkinRepairButton.field_22763 = this.serverConfigEditable && this.allowCraftingTableSkinRepair != this.defaults.isCraftingTableSkinRepairAllowed();
            }
            if (this.resetKeepMessAfterDeathButton != null) {
                boolean bl = this.resetKeepMessAfterDeathButton.field_22763 = this.serverConfigEditable && this.keepMessAfterDeath != this.defaults.keepMessAfterDeath();
            }
            if (this.resetKeepRippedSkinAfterDeathButton != null) {
                boolean bl = this.resetKeepRippedSkinAfterDeathButton.field_22763 = this.serverConfigEditable && this.keepRippedSkinAfterDeath != this.defaults.keepRippedSkinAfterDeath();
            }
            if (this.resetKeepLiquidTankAfterDeathButton != null) {
                boolean bl = this.resetKeepLiquidTankAfterDeathButton.field_22763 = this.serverConfigEditable && this.keepLiquidTankAfterDeath != this.defaults.keepLiquidTankAfterDeath();
            }
            if (this.resetKeepPregnancyAfterDeathButton != null) {
                boolean bl = this.resetKeepPregnancyAfterDeathButton.field_22763 = this.serverConfigEditable && this.keepPregnancyAfterDeath != this.defaults.keepPregnancyAfterDeath();
            }
            if (this.lastDefeatedEnabledButton != null) {
                this.lastDefeatedEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.blockAnimationsWhileRidingLivingEntitiesButton != null) {
                this.blockAnimationsWhileRidingLivingEntitiesButton.field_22763 = this.serverConfigEditable;
            }
            if (this.blockAnimationsWhileRidingVehiclesButton != null) {
                this.blockAnimationsWhileRidingVehiclesButton.field_22763 = this.serverConfigEditable;
            }
            if (this.allowCraftingTableSkinRepairButton != null) {
                this.allowCraftingTableSkinRepairButton.field_22763 = this.serverConfigEditable;
            }
            if (this.keepMessAfterDeathButton != null) {
                this.keepMessAfterDeathButton.field_22763 = this.serverConfigEditable;
            }
            if (this.keepRippedSkinAfterDeathButton != null) {
                this.keepRippedSkinAfterDeathButton.field_22763 = this.serverConfigEditable;
            }
            if (this.keepLiquidTankAfterDeathButton != null) {
                this.keepLiquidTankAfterDeathButton.field_22763 = this.serverConfigEditable;
            }
            if (this.keepPregnancyAfterDeathButton != null) {
                this.keepPregnancyAfterDeathButton.field_22763 = this.serverConfigEditable;
            }
        }
    }

    static class GameplayAnimationsScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private int loopSeconds;
        private int peakLoopSeconds;
        private int maleMalePercent;
        private int femaleFemalePercent;
        private int entityEntityPercent;
        private int multiActorJoinPercent;
        private class_342 loopField;
        private class_342 peakLoopField;
        private class_342 maleMaleField;
        private class_342 femaleFemaleField;
        private class_342 entityEntityField;
        private class_342 multiActorJoinField;
        private class_4185 resetLoopButton;
        private class_4185 resetPeakLoopButton;
        private class_4185 resetMaleMaleButton;
        private class_4185 resetFemaleFemaleButton;
        private class_4185 resetEntityEntityButton;
        private class_4185 resetMultiActorJoinButton;
        private boolean serverConfigEditable;

        protected GameplayAnimationsScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.section.animations"));
            this.parent = parent;
            this.config = config;
            this.loopSeconds = NonHudOverlay.getRuntimeLoopSeconds();
            this.peakLoopSeconds = NonHudOverlay.getRuntimePeakLoopSeconds();
            this.maleMalePercent = config.getMaleMaleChancePercent();
            this.femaleFemalePercent = config.getFemaleFemaleChancePercent();
            this.entityEntityPercent = config.getEntityEntityChancePercent();
            this.multiActorJoinPercent = config.getMultiActorJoinChancePercent();
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.loopField != null) {
                this.loopSeconds = this.parseField(this.loopField, this.loopSeconds);
                this.peakLoopSeconds = this.parseField(this.peakLoopField, this.peakLoopSeconds);
                this.maleMalePercent = this.parseField(this.maleMaleField, this.maleMalePercent);
                this.femaleFemalePercent = this.parseField(this.femaleFemaleField, this.femaleFemalePercent);
                this.entityEntityPercent = this.parseField(this.entityEntityField, this.entityEntityPercent);
                this.multiActorJoinPercent = this.parseField(this.multiActorJoinField, this.multiActorJoinPercent);
            }
            int listTop = 32;
            int bottomArea = 40;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.animation_pacing")));
            this.loopField = this.newNumberField(fieldWidth, this.loopSeconds);
            this.resetLoopButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.loopField.method_1852(String.valueOf(this.defaults.getLoopProgressSeconds()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetLoopButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.loop_seconds"), (class_339)this.loopField, (class_339)this.resetLoopButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.loop_seconds")));
            this.peakLoopField = this.newNumberField(fieldWidth, this.peakLoopSeconds);
            this.resetPeakLoopButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.peakLoopField.method_1852(String.valueOf(this.defaults.getPeakLoopProgressSeconds()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPeakLoopButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.peak_loop_seconds"), (class_339)this.peakLoopField, (class_339)this.resetPeakLoopButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.peak_loop_seconds")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.match_chances")));
            this.maleMaleField = this.newNumberField(fieldWidth, this.maleMalePercent);
            this.resetMaleMaleButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.maleMaleField.method_1852(String.valueOf(this.defaults.getMaleMaleChancePercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetMaleMaleButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.male_male_chance"), (class_339)this.maleMaleField, (class_339)this.resetMaleMaleButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.male_male_chance")));
            this.femaleFemaleField = this.newNumberField(fieldWidth, this.femaleFemalePercent);
            this.resetFemaleFemaleButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.femaleFemaleField.method_1852(String.valueOf(this.defaults.getFemaleFemaleChancePercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetFemaleFemaleButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.female_female_chance"), (class_339)this.femaleFemaleField, (class_339)this.resetFemaleFemaleButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.female_female_chance")));
            this.entityEntityField = this.newNumberField(fieldWidth, this.entityEntityPercent);
            this.resetEntityEntityButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.entityEntityField.method_1852(String.valueOf(this.defaults.getEntityEntityChancePercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetEntityEntityButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.entity_entity_chance"), (class_339)this.entityEntityField, (class_339)this.resetEntityEntityButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.entity_entity_chance")));
            this.multiActorJoinField = this.newNumberField(fieldWidth, this.multiActorJoinPercent);
            this.resetMultiActorJoinButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.multiActorJoinField.method_1852(String.valueOf(this.defaults.getMultiActorJoinChancePercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetMultiActorJoinButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.multi_actor_join_chance"), (class_339)this.multiActorJoinField, (class_339)this.resetMultiActorJoinButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.multi_actor_join_chance")));
            this.loopField.method_1863(ignored -> this.updateResetButtons());
            this.peakLoopField.method_1863(ignored -> this.updateResetButtons());
            this.maleMaleField.method_1863(ignored -> this.updateResetButtons());
            this.femaleFemaleField.method_1863(ignored -> this.updateResetButtons());
            this.entityEntityField.method_1863(ignored -> this.updateResetButtons());
            this.multiActorJoinField.method_1863(ignored -> this.updateResetButtons());
            this.loopField.method_1888(this.serverConfigEditable);
            this.peakLoopField.method_1888(this.serverConfigEditable);
            this.maleMaleField.method_1888(this.serverConfigEditable);
            this.femaleFemaleField.method_1888(this.serverConfigEditable);
            this.entityEntityField.method_1888(this.serverConfigEditable);
            this.multiActorJoinField.method_1888(this.serverConfigEditable);
            this.updateResetButtons();
            int centerX = this.field_22789 / 2;
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> this.saveAndClose()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.method_37063((class_364)doneButton);
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
        }

        public void method_25419() {
            this.saveAndClose();
        }

        private void saveAndClose() {
            if (this.serverConfigEditable) {
                this.loopSeconds = this.clampLoopSeconds(this.parseField(this.loopField, this.loopSeconds));
                this.peakLoopSeconds = this.clampPeakLoopSeconds(this.parseField(this.peakLoopField, this.peakLoopSeconds));
                this.maleMalePercent = this.clampPercent(this.parseField(this.maleMaleField, this.maleMalePercent));
                this.femaleFemalePercent = this.clampPercent(this.parseField(this.femaleFemaleField, this.femaleFemalePercent));
                this.entityEntityPercent = this.clampPercent(this.parseField(this.entityEntityField, this.entityEntityPercent));
                this.multiActorJoinPercent = this.clampPercent(this.parseField(this.multiActorJoinField, this.multiActorJoinPercent));
                this.config.setLoopProgressSeconds(this.loopSeconds);
                this.config.setPeakLoopProgressSeconds(this.peakLoopSeconds);
                this.config.setMaleMaleChancePercent(this.maleMalePercent);
                this.config.setFemaleFemaleChancePercent(this.femaleFemalePercent);
                this.config.setEntityEntityChancePercent(this.entityEntityPercent);
                this.config.setMultiActorJoinChancePercent(this.multiActorJoinPercent);
                this.config.save();
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
            }
            class_310.method_1551().method_1507(this.parent);
        }

        private class_342 newNumberField(int w, int initial) {
            class_342 field = new class_342(this.field_22793, 0, 0, w, 20, (class_2561)class_2561.method_43473());
            field.method_1852(String.valueOf(initial));
            field.method_1880(8);
            field.method_1888(true);
            return field;
        }

        private int parseField(class_342 field, int fallback) {
            try {
                return Integer.parseInt(field.method_1882().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private int clampLoopSeconds(int v) {
            return Math.max(5, Math.min(300, v));
        }

        private int clampPeakLoopSeconds(int v) {
            return Math.max(1, Math.min(300, v));
        }

        private int clampPercent(int v) {
            return Math.max(0, Math.min(100, v));
        }

        private boolean isIntFieldChanged(class_342 field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseField(field, defaultValue) != defaultValue;
        }

        private void updateResetButtons() {
            if (this.resetLoopButton != null) {
                boolean bl = this.resetLoopButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.loopField, this.defaults.getLoopProgressSeconds());
            }
            if (this.resetPeakLoopButton != null) {
                boolean bl = this.resetPeakLoopButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.peakLoopField, this.defaults.getPeakLoopProgressSeconds());
            }
            if (this.resetMaleMaleButton != null) {
                boolean bl = this.resetMaleMaleButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.maleMaleField, this.defaults.getMaleMaleChancePercent());
            }
            if (this.resetFemaleFemaleButton != null) {
                boolean bl = this.resetFemaleFemaleButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.femaleFemaleField, this.defaults.getFemaleFemaleChancePercent());
            }
            if (this.resetEntityEntityButton != null) {
                boolean bl = this.resetEntityEntityButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.entityEntityField, this.defaults.getEntityEntityChancePercent());
            }
            if (this.resetMultiActorJoinButton != null) {
                this.resetMultiActorJoinButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.multiActorJoinField, this.defaults.getMultiActorJoinChancePercent());
            }
        }
    }

    static class GameplayEnergyScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private double energyGainRate;
        private double nearAnimationEnergyGainMult;
        private double playerEnergyAuraMultLow;
        private double playerEnergyAuraMultMid;
        private double playerEnergyAuraMultHigh;
        private double destroyedSkinAuraMultStage1;
        private double destroyedSkinAuraMultStage2;
        private double destroyedSkinAuraMultStage3;
        private double destroyedSkinAuraMultStage4;
        private int normalDamageDestroyedSkinChancePercent;
        private int playerEnergyAuraRadiusMin;
        private int playerEnergyAuraRadiusMax;
        private int playerEnergyAuraPulseTicks;
        private class_342 energyGainRateField;
        private class_342 nearAnimationEnergyGainMultField;
        private class_342 playerEnergyAuraMultLowField;
        private class_342 playerEnergyAuraMultMidField;
        private class_342 playerEnergyAuraMultHighField;
        private class_342 destroyedSkinAuraMultStage1Field;
        private class_342 destroyedSkinAuraMultStage2Field;
        private class_342 destroyedSkinAuraMultStage3Field;
        private class_342 destroyedSkinAuraMultStage4Field;
        private class_342 normalDamageDestroyedSkinChanceField;
        private class_342 playerEnergyAuraRadiusMinField;
        private class_342 playerEnergyAuraRadiusMaxField;
        private class_342 playerEnergyAuraPulseTicksField;
        private class_4185 resetEnergyGainRateButton;
        private class_4185 resetNearAnimationEnergyGainMultButton;
        private class_4185 resetPlayerEnergyAuraMultLowButton;
        private class_4185 resetPlayerEnergyAuraMultMidButton;
        private class_4185 resetPlayerEnergyAuraMultHighButton;
        private class_4185 resetDestroyedSkinAuraMultStage1Button;
        private class_4185 resetDestroyedSkinAuraMultStage2Button;
        private class_4185 resetDestroyedSkinAuraMultStage3Button;
        private class_4185 resetDestroyedSkinAuraMultStage4Button;
        private class_4185 resetNormalDamageDestroyedSkinChanceButton;
        private class_4185 resetPlayerEnergyAuraRadiusMinButton;
        private class_4185 resetPlayerEnergyAuraRadiusMaxButton;
        private class_4185 resetPlayerEnergyAuraPulseTicksButton;
        private boolean serverConfigEditable;

        protected GameplayEnergyScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.section.energy"));
            this.parent = parent;
            this.config = config;
            this.energyGainRate = config.getEnergyGainRate();
            this.nearAnimationEnergyGainMult = config.getNearAnimationEnergyGainMult();
            this.playerEnergyAuraMultLow = config.getPlayerEnergyAuraMultLow();
            this.playerEnergyAuraMultMid = config.getPlayerEnergyAuraMultMid();
            this.playerEnergyAuraMultHigh = config.getPlayerEnergyAuraMultHigh();
            this.destroyedSkinAuraMultStage1 = config.getDestroyedSkinAuraMultStage1();
            this.destroyedSkinAuraMultStage2 = config.getDestroyedSkinAuraMultStage2();
            this.destroyedSkinAuraMultStage3 = config.getDestroyedSkinAuraMultStage3();
            this.destroyedSkinAuraMultStage4 = config.getDestroyedSkinAuraMultStage4();
            this.normalDamageDestroyedSkinChancePercent = config.getNormalDamageDestroyedSkinChancePercent();
            this.playerEnergyAuraRadiusMin = config.getPlayerEnergyAuraRadiusMin();
            this.playerEnergyAuraRadiusMax = config.getPlayerEnergyAuraRadiusMax();
            this.playerEnergyAuraPulseTicks = config.getPlayerEnergyAuraPulseTicks();
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.energyGainRateField != null) {
                this.energyGainRate = this.parseDecimalField(this.energyGainRateField, this.energyGainRate);
                this.nearAnimationEnergyGainMult = this.parseDecimalField(this.nearAnimationEnergyGainMultField, this.nearAnimationEnergyGainMult);
                this.playerEnergyAuraMultLow = this.parseDecimalField(this.playerEnergyAuraMultLowField, this.playerEnergyAuraMultLow);
                this.playerEnergyAuraMultMid = this.parseDecimalField(this.playerEnergyAuraMultMidField, this.playerEnergyAuraMultMid);
                this.playerEnergyAuraMultHigh = this.parseDecimalField(this.playerEnergyAuraMultHighField, this.playerEnergyAuraMultHigh);
                this.destroyedSkinAuraMultStage1 = this.parseDecimalField(this.destroyedSkinAuraMultStage1Field, this.destroyedSkinAuraMultStage1);
                this.destroyedSkinAuraMultStage2 = this.parseDecimalField(this.destroyedSkinAuraMultStage2Field, this.destroyedSkinAuraMultStage2);
                this.destroyedSkinAuraMultStage3 = this.parseDecimalField(this.destroyedSkinAuraMultStage3Field, this.destroyedSkinAuraMultStage3);
                this.destroyedSkinAuraMultStage4 = this.parseDecimalField(this.destroyedSkinAuraMultStage4Field, this.destroyedSkinAuraMultStage4);
                this.normalDamageDestroyedSkinChancePercent = this.parseNumberField(this.normalDamageDestroyedSkinChanceField, this.normalDamageDestroyedSkinChancePercent);
                this.playerEnergyAuraRadiusMin = this.parseNumberField(this.playerEnergyAuraRadiusMinField, this.playerEnergyAuraRadiusMin);
                this.playerEnergyAuraRadiusMax = this.parseNumberField(this.playerEnergyAuraRadiusMaxField, this.playerEnergyAuraRadiusMax);
                this.playerEnergyAuraPulseTicks = this.parseNumberField(this.playerEnergyAuraPulseTicksField, this.playerEnergyAuraPulseTicks);
            }
            int listTop = 32;
            int bottomArea = 40;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.energy_gain")));
            this.energyGainRateField = this.newDecimalField(fieldWidth, this.energyGainRate);
            this.resetEnergyGainRateButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.energyGainRateField.method_1852(String.valueOf(this.defaults.getEnergyGainRate()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetEnergyGainRateButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.energy_gain_rate"), (class_339)this.energyGainRateField, (class_339)this.resetEnergyGainRateButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.energy_gain_rate")));
            this.nearAnimationEnergyGainMultField = this.newDecimalField(fieldWidth, this.nearAnimationEnergyGainMult);
            this.resetNearAnimationEnergyGainMultButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.nearAnimationEnergyGainMultField.method_1852(String.valueOf(this.defaults.getNearAnimationEnergyGainMult()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetNearAnimationEnergyGainMultButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.near_animation_energy_gain_mult"), (class_339)this.nearAnimationEnergyGainMultField, (class_339)this.resetNearAnimationEnergyGainMultButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.near_animation_energy_gain_mult")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.player_energy_aura")));
            this.playerEnergyAuraMultLowField = this.newDecimalField(fieldWidth, this.playerEnergyAuraMultLow);
            this.resetPlayerEnergyAuraMultLowButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraMultLowField.method_1852(String.valueOf(this.defaults.getPlayerEnergyAuraMultLow()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPlayerEnergyAuraMultLowButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.player_energy_aura_mult_low"), (class_339)this.playerEnergyAuraMultLowField, (class_339)this.resetPlayerEnergyAuraMultLowButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_mult_low")));
            this.playerEnergyAuraMultMidField = this.newDecimalField(fieldWidth, this.playerEnergyAuraMultMid);
            this.resetPlayerEnergyAuraMultMidButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraMultMidField.method_1852(String.valueOf(this.defaults.getPlayerEnergyAuraMultMid()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPlayerEnergyAuraMultMidButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.player_energy_aura_mult_mid"), (class_339)this.playerEnergyAuraMultMidField, (class_339)this.resetPlayerEnergyAuraMultMidButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_mult_mid")));
            this.playerEnergyAuraMultHighField = this.newDecimalField(fieldWidth, this.playerEnergyAuraMultHigh);
            this.resetPlayerEnergyAuraMultHighButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraMultHighField.method_1852(String.valueOf(this.defaults.getPlayerEnergyAuraMultHigh()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPlayerEnergyAuraMultHighButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.player_energy_aura_mult_high"), (class_339)this.playerEnergyAuraMultHighField, (class_339)this.resetPlayerEnergyAuraMultHighButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_mult_high")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.destroyed_skin")));
            this.destroyedSkinAuraMultStage1Field = this.newDecimalField(fieldWidth, this.destroyedSkinAuraMultStage1);
            this.resetDestroyedSkinAuraMultStage1Button = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.destroyedSkinAuraMultStage1Field.method_1852(String.valueOf(this.defaults.getDestroyedSkinAuraMultStage1()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetDestroyedSkinAuraMultStage1Button, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_aura_mult_stage1"), (class_339)this.destroyedSkinAuraMultStage1Field, (class_339)this.resetDestroyedSkinAuraMultStage1Button, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.destroyed_skin_aura_mult_stage1")));
            this.destroyedSkinAuraMultStage2Field = this.newDecimalField(fieldWidth, this.destroyedSkinAuraMultStage2);
            this.resetDestroyedSkinAuraMultStage2Button = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.destroyedSkinAuraMultStage2Field.method_1852(String.valueOf(this.defaults.getDestroyedSkinAuraMultStage2()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetDestroyedSkinAuraMultStage2Button, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_aura_mult_stage2"), (class_339)this.destroyedSkinAuraMultStage2Field, (class_339)this.resetDestroyedSkinAuraMultStage2Button, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.destroyed_skin_aura_mult_stage2")));
            this.destroyedSkinAuraMultStage3Field = this.newDecimalField(fieldWidth, this.destroyedSkinAuraMultStage3);
            this.resetDestroyedSkinAuraMultStage3Button = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.destroyedSkinAuraMultStage3Field.method_1852(String.valueOf(this.defaults.getDestroyedSkinAuraMultStage3()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetDestroyedSkinAuraMultStage3Button, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_aura_mult_stage3"), (class_339)this.destroyedSkinAuraMultStage3Field, (class_339)this.resetDestroyedSkinAuraMultStage3Button, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.destroyed_skin_aura_mult_stage3")));
            this.destroyedSkinAuraMultStage4Field = this.newDecimalField(fieldWidth, this.destroyedSkinAuraMultStage4);
            this.resetDestroyedSkinAuraMultStage4Button = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.destroyedSkinAuraMultStage4Field.method_1852(String.valueOf(this.defaults.getDestroyedSkinAuraMultStage4()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetDestroyedSkinAuraMultStage4Button, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_aura_mult_stage4"), (class_339)this.destroyedSkinAuraMultStage4Field, (class_339)this.resetDestroyedSkinAuraMultStage4Button, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.destroyed_skin_aura_mult_stage4")));
            this.normalDamageDestroyedSkinChanceField = this.newNumberField(fieldWidth, this.normalDamageDestroyedSkinChancePercent);
            this.resetNormalDamageDestroyedSkinChanceButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.normalDamageDestroyedSkinChanceField.method_1852(String.valueOf(this.defaults.getNormalDamageDestroyedSkinChancePercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetNormalDamageDestroyedSkinChanceButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.normal_damage_destroyed_skin_chance_percent"), (class_339)this.normalDamageDestroyedSkinChanceField, (class_339)this.resetNormalDamageDestroyedSkinChanceButton, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.normal_damage_destroyed_skin_chance_percent")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.aura_range_timing")));
            this.playerEnergyAuraRadiusMinField = this.newNumberField(fieldWidth, this.playerEnergyAuraRadiusMin);
            this.resetPlayerEnergyAuraRadiusMinButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraRadiusMinField.method_1852(String.valueOf(this.defaults.getPlayerEnergyAuraRadiusMin()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPlayerEnergyAuraRadiusMinButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.player_energy_aura_radius_min"), (class_339)this.playerEnergyAuraRadiusMinField, (class_339)this.resetPlayerEnergyAuraRadiusMinButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_radius_min")));
            this.playerEnergyAuraRadiusMaxField = this.newNumberField(fieldWidth, this.playerEnergyAuraRadiusMax);
            this.resetPlayerEnergyAuraRadiusMaxButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraRadiusMaxField.method_1852(String.valueOf(this.defaults.getPlayerEnergyAuraRadiusMax()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPlayerEnergyAuraRadiusMaxButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.player_energy_aura_radius_max"), (class_339)this.playerEnergyAuraRadiusMaxField, (class_339)this.resetPlayerEnergyAuraRadiusMaxButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_radius_max")));
            this.playerEnergyAuraPulseTicksField = this.newNumberField(fieldWidth, this.playerEnergyAuraPulseTicks);
            this.resetPlayerEnergyAuraPulseTicksButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraPulseTicksField.method_1852(String.valueOf(this.defaults.getPlayerEnergyAuraPulseTicks()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPlayerEnergyAuraPulseTicksButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.player_energy_aura_pulse_ticks"), (class_339)this.playerEnergyAuraPulseTicksField, (class_339)this.resetPlayerEnergyAuraPulseTicksButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_pulse_ticks")));
            this.energyGainRateField.method_1863(ignored -> this.updateResetButtons());
            this.nearAnimationEnergyGainMultField.method_1863(ignored -> this.updateResetButtons());
            this.playerEnergyAuraMultLowField.method_1863(ignored -> this.updateResetButtons());
            this.playerEnergyAuraMultMidField.method_1863(ignored -> this.updateResetButtons());
            this.playerEnergyAuraMultHighField.method_1863(ignored -> this.updateResetButtons());
            this.destroyedSkinAuraMultStage1Field.method_1863(ignored -> this.updateResetButtons());
            this.destroyedSkinAuraMultStage2Field.method_1863(ignored -> this.updateResetButtons());
            this.destroyedSkinAuraMultStage3Field.method_1863(ignored -> this.updateResetButtons());
            this.destroyedSkinAuraMultStage4Field.method_1863(ignored -> this.updateResetButtons());
            this.normalDamageDestroyedSkinChanceField.method_1863(ignored -> this.updateResetButtons());
            this.playerEnergyAuraRadiusMinField.method_1863(ignored -> this.updateResetButtons());
            this.playerEnergyAuraRadiusMaxField.method_1863(ignored -> this.updateResetButtons());
            this.playerEnergyAuraPulseTicksField.method_1863(ignored -> this.updateResetButtons());
            this.energyGainRateField.method_1888(this.serverConfigEditable);
            this.nearAnimationEnergyGainMultField.method_1888(this.serverConfigEditable);
            this.playerEnergyAuraMultLowField.method_1888(this.serverConfigEditable);
            this.playerEnergyAuraMultMidField.method_1888(this.serverConfigEditable);
            this.playerEnergyAuraMultHighField.method_1888(this.serverConfigEditable);
            this.destroyedSkinAuraMultStage1Field.method_1888(this.destroyedSkinSettingsEditable());
            this.destroyedSkinAuraMultStage2Field.method_1888(this.destroyedSkinSettingsEditable());
            this.destroyedSkinAuraMultStage3Field.method_1888(this.destroyedSkinSettingsEditable());
            this.destroyedSkinAuraMultStage4Field.method_1888(this.destroyedSkinSettingsEditable());
            this.normalDamageDestroyedSkinChanceField.method_1888(this.destroyedSkinSettingsEditable());
            this.playerEnergyAuraRadiusMinField.method_1888(this.serverConfigEditable);
            this.playerEnergyAuraRadiusMaxField.method_1888(this.serverConfigEditable);
            this.playerEnergyAuraPulseTicksField.method_1888(this.serverConfigEditable);
            this.updateResetButtons();
            int centerX = this.field_22789 / 2;
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> this.saveAndClose()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.method_37063((class_364)doneButton);
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
        }

        public void method_25419() {
            this.saveAndClose();
        }

        private void saveAndClose() {
            if (this.serverConfigEditable) {
                this.energyGainRate = this.clampEnergyGainRate(this.parseDecimalField(this.energyGainRateField, this.energyGainRate));
                this.nearAnimationEnergyGainMult = this.clampNearAnimationEnergyGainMult(this.parseDecimalField(this.nearAnimationEnergyGainMultField, this.nearAnimationEnergyGainMult));
                this.playerEnergyAuraMultLow = this.clampPlayerEnergyAuraMult(this.parseDecimalField(this.playerEnergyAuraMultLowField, this.playerEnergyAuraMultLow));
                this.playerEnergyAuraMultMid = this.clampPlayerEnergyAuraMult(this.parseDecimalField(this.playerEnergyAuraMultMidField, this.playerEnergyAuraMultMid));
                this.playerEnergyAuraMultHigh = this.clampPlayerEnergyAuraMult(this.parseDecimalField(this.playerEnergyAuraMultHighField, this.playerEnergyAuraMultHigh));
                this.destroyedSkinAuraMultStage1 = this.clampDestroyedSkinAuraMult(this.parseDecimalField(this.destroyedSkinAuraMultStage1Field, this.destroyedSkinAuraMultStage1));
                this.destroyedSkinAuraMultStage2 = this.clampDestroyedSkinAuraMult(this.parseDecimalField(this.destroyedSkinAuraMultStage2Field, this.destroyedSkinAuraMultStage2));
                this.destroyedSkinAuraMultStage3 = this.clampDestroyedSkinAuraMult(this.parseDecimalField(this.destroyedSkinAuraMultStage3Field, this.destroyedSkinAuraMultStage3));
                this.destroyedSkinAuraMultStage4 = this.clampDestroyedSkinAuraMult(this.parseDecimalField(this.destroyedSkinAuraMultStage4Field, this.destroyedSkinAuraMultStage4));
                this.normalDamageDestroyedSkinChancePercent = this.clampPercent(this.parseNumberField(this.normalDamageDestroyedSkinChanceField, this.normalDamageDestroyedSkinChancePercent));
                this.playerEnergyAuraRadiusMin = this.clampPlayerEnergyAuraRadius(this.parseNumberField(this.playerEnergyAuraRadiusMinField, this.playerEnergyAuraRadiusMin));
                this.playerEnergyAuraRadiusMax = Math.max(this.playerEnergyAuraRadiusMin, this.clampPlayerEnergyAuraRadius(this.parseNumberField(this.playerEnergyAuraRadiusMaxField, this.playerEnergyAuraRadiusMax)));
                this.playerEnergyAuraPulseTicks = this.clampPlayerEnergyAuraPulseTicks(this.parseNumberField(this.playerEnergyAuraPulseTicksField, this.playerEnergyAuraPulseTicks));
                this.config.setEnergyGainRate(this.energyGainRate);
                this.config.setNearAnimationEnergyGainMult(this.nearAnimationEnergyGainMult);
                this.config.setPlayerEnergyAuraMultLow(this.playerEnergyAuraMultLow);
                this.config.setPlayerEnergyAuraMultMid(this.playerEnergyAuraMultMid);
                this.config.setPlayerEnergyAuraMultHigh(this.playerEnergyAuraMultHigh);
                this.config.setDestroyedSkinAuraMultStage1(this.destroyedSkinAuraMultStage1);
                this.config.setDestroyedSkinAuraMultStage2(this.destroyedSkinAuraMultStage2);
                this.config.setDestroyedSkinAuraMultStage3(this.destroyedSkinAuraMultStage3);
                this.config.setDestroyedSkinAuraMultStage4(this.destroyedSkinAuraMultStage4);
                this.config.setNormalDamageDestroyedSkinChancePercent(this.normalDamageDestroyedSkinChancePercent);
                this.config.setPlayerEnergyAuraRadiusMin(this.playerEnergyAuraRadiusMin);
                this.config.setPlayerEnergyAuraRadiusMax(this.playerEnergyAuraRadiusMax);
                this.config.setPlayerEnergyAuraPulseTicks(this.playerEnergyAuraPulseTicks);
                this.config.save();
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
            }
            class_310.method_1551().method_1507(this.parent);
        }

        private class_342 newDecimalField(int w, double initial) {
            class_342 field = new class_342(this.field_22793, 0, 0, w, 20, (class_2561)class_2561.method_43473());
            int maxLen = 16;
            field.method_1880(maxLen);
            field.method_1852(this.trimToMax(this.formatDecimal(initial), maxLen));
            field.method_1888(true);
            return field;
        }

        private class_342 newNumberField(int w, int initial) {
            class_342 field = new class_342(this.field_22793, 0, 0, w, 20, (class_2561)class_2561.method_43473());
            int maxLen = 16;
            field.method_1880(maxLen);
            field.method_1852(this.trimToMax(String.valueOf(initial), maxLen));
            field.method_1888(true);
            return field;
        }

        private double parseDecimalField(class_342 field, double fallback) {
            try {
                return Double.parseDouble(field.method_1882().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private int parseNumberField(class_342 field, int fallback) {
            try {
                return Integer.parseInt(field.method_1882().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private String formatDecimal(double value) {
            String text = String.format(Locale.ROOT, "%.3f", value);
            int dot = text.indexOf(46);
            if (dot >= 0) {
                int end;
                for (end = text.length(); end > dot + 1 && text.charAt(end - 1) == '0'; --end) {
                }
                if (end == dot + 1) {
                    end = dot;
                }
                text = text.substring(0, end);
            }
            return text;
        }

        private String trimToMax(String value, int max) {
            if (value == null) {
                return "";
            }
            return value.length() > max ? value.substring(0, max) : value;
        }

        private double clampEnergyGainRate(double v) {
            return Math.max(0.1, Math.min(10.0, v));
        }

        private double clampNearAnimationEnergyGainMult(double v) {
            return Math.max(1.0, Math.min(10.0, v));
        }

        private double clampPlayerEnergyAuraMult(double v) {
            return Math.max(1.0, Math.min(20.0, v));
        }

        private double clampDestroyedSkinAuraMult(double v) {
            return Math.max(1.0, Math.min(50.0, v));
        }

        private int clampPercent(int v) {
            return Math.max(0, Math.min(100, v));
        }

        private int clampPlayerEnergyAuraRadius(int v) {
            return Math.max(1, Math.min(64, v));
        }

        private int clampPlayerEnergyAuraPulseTicks(int v) {
            return Math.max(1, Math.min(1200, v));
        }

        private boolean destroyedSkinSettingsEditable() {
            return this.serverConfigEditable && this.config.isDestroyedSkinSystemEnabled();
        }

        private class_7919 destroyedSkinSettingTooltip(String enabledKey) {
            return NonModMenuScreens.tooltip(this.config.isDestroyedSkinSystemEnabled() ? enabledKey : "config.needsofnature.tooltip.destroyed_skin_system.disabled_setting");
        }

        private boolean isDoubleFieldChanged(class_342 field, double defaultValue) {
            if (field == null) {
                return false;
            }
            double value = this.parseDecimalField(field, defaultValue);
            return Math.abs(value - defaultValue) >= 1.0E-4;
        }

        private boolean isIntFieldChanged(class_342 field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseNumberField(field, defaultValue) != defaultValue;
        }

        private void updateResetButtons() {
            if (this.resetEnergyGainRateButton != null) {
                boolean bl = this.resetEnergyGainRateButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.energyGainRateField, this.defaults.getEnergyGainRate());
            }
            if (this.resetNearAnimationEnergyGainMultButton != null) {
                boolean bl = this.resetNearAnimationEnergyGainMultButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.nearAnimationEnergyGainMultField, this.defaults.getNearAnimationEnergyGainMult());
            }
            if (this.resetPlayerEnergyAuraMultLowButton != null) {
                boolean bl = this.resetPlayerEnergyAuraMultLowButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.playerEnergyAuraMultLowField, this.defaults.getPlayerEnergyAuraMultLow());
            }
            if (this.resetPlayerEnergyAuraMultMidButton != null) {
                boolean bl = this.resetPlayerEnergyAuraMultMidButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.playerEnergyAuraMultMidField, this.defaults.getPlayerEnergyAuraMultMid());
            }
            if (this.resetPlayerEnergyAuraMultHighButton != null) {
                boolean bl = this.resetPlayerEnergyAuraMultHighButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.playerEnergyAuraMultHighField, this.defaults.getPlayerEnergyAuraMultHigh());
            }
            if (this.resetDestroyedSkinAuraMultStage1Button != null) {
                boolean bl = this.resetDestroyedSkinAuraMultStage1Button.field_22763 = this.destroyedSkinSettingsEditable() && this.isDoubleFieldChanged(this.destroyedSkinAuraMultStage1Field, this.defaults.getDestroyedSkinAuraMultStage1());
            }
            if (this.resetDestroyedSkinAuraMultStage2Button != null) {
                boolean bl = this.resetDestroyedSkinAuraMultStage2Button.field_22763 = this.destroyedSkinSettingsEditable() && this.isDoubleFieldChanged(this.destroyedSkinAuraMultStage2Field, this.defaults.getDestroyedSkinAuraMultStage2());
            }
            if (this.resetDestroyedSkinAuraMultStage3Button != null) {
                boolean bl = this.resetDestroyedSkinAuraMultStage3Button.field_22763 = this.destroyedSkinSettingsEditable() && this.isDoubleFieldChanged(this.destroyedSkinAuraMultStage3Field, this.defaults.getDestroyedSkinAuraMultStage3());
            }
            if (this.resetDestroyedSkinAuraMultStage4Button != null) {
                boolean bl = this.resetDestroyedSkinAuraMultStage4Button.field_22763 = this.destroyedSkinSettingsEditable() && this.isDoubleFieldChanged(this.destroyedSkinAuraMultStage4Field, this.defaults.getDestroyedSkinAuraMultStage4());
            }
            if (this.resetNormalDamageDestroyedSkinChanceButton != null) {
                boolean bl = this.resetNormalDamageDestroyedSkinChanceButton.field_22763 = this.destroyedSkinSettingsEditable() && this.isIntFieldChanged(this.normalDamageDestroyedSkinChanceField, this.defaults.getNormalDamageDestroyedSkinChancePercent());
            }
            if (this.resetPlayerEnergyAuraRadiusMinButton != null) {
                boolean bl = this.resetPlayerEnergyAuraRadiusMinButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.playerEnergyAuraRadiusMinField, this.defaults.getPlayerEnergyAuraRadiusMin());
            }
            if (this.resetPlayerEnergyAuraRadiusMaxButton != null) {
                boolean bl = this.resetPlayerEnergyAuraRadiusMaxButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.playerEnergyAuraRadiusMaxField, this.defaults.getPlayerEnergyAuraRadiusMax());
            }
            if (this.resetPlayerEnergyAuraPulseTicksButton != null) {
                this.resetPlayerEnergyAuraPulseTicksButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.playerEnergyAuraPulseTicksField, this.defaults.getPlayerEnergyAuraPulseTicks());
            }
        }
    }

    static class GameplayWorldScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private int initialEnergyMax;
        private int genderMalePercent;
        private int genderFemalePercent;
        private int genderBothPercent;
        private boolean allowPlayerGenderChangeAnytime;
        private boolean requirePlayerGenderSelectionOnJoin;
        private int allowedStartingGenderMask;
        private class_342 initialEnergyField;
        private class_342 genderMaleField;
        private class_342 genderFemaleField;
        private class_342 genderBothField;
        private class_4185 allowPlayerGenderChangeButton;
        private class_4185 requirePlayerGenderSelectionButton;
        private class_4185 allowStartingMaleButton;
        private class_4185 allowStartingFemaleButton;
        private class_4185 allowStartingBothButton;
        private class_4185 resetInitialEnergyButton;
        private class_4185 resetGenderMaleButton;
        private class_4185 resetGenderFemaleButton;
        private class_4185 resetGenderBothButton;
        private class_4185 resetAllowPlayerGenderChangeButton;
        private class_4185 resetRequirePlayerGenderSelectionButton;
        private class_4185 resetAllowedStartingGendersButton;
        private boolean serverConfigEditable;

        protected GameplayWorldScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.section.world"));
            this.parent = parent;
            this.config = config;
            this.initialEnergyMax = config.getInitialEnergyMax();
            this.genderMalePercent = config.getGenderMaleChancePercent();
            this.genderFemalePercent = config.getGenderFemaleChancePercent();
            this.genderBothPercent = config.getGenderBothChancePercent();
            this.allowPlayerGenderChangeAnytime = config.allowPlayerGenderChangeAnytime();
            this.requirePlayerGenderSelectionOnJoin = config.requirePlayerGenderSelectionOnJoin();
            this.allowedStartingGenderMask = config.getAllowedStartingGenderMask();
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.initialEnergyField != null) {
                this.initialEnergyMax = this.parseField(this.initialEnergyField, this.initialEnergyMax);
                this.genderMalePercent = this.parseField(this.genderMaleField, this.genderMalePercent);
                this.genderFemalePercent = this.parseField(this.genderFemaleField, this.genderFemalePercent);
                this.genderBothPercent = this.parseField(this.genderBothField, this.genderBothPercent);
            }
            int listTop = 32;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.mob_energy")));
            this.initialEnergyField = this.newNumberField(fieldWidth, this.initialEnergyMax);
            this.resetInitialEnergyButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.initialEnergyField.method_1852(String.valueOf(this.defaults.getInitialEnergyMax()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetInitialEnergyButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.initial_energy_max"), (class_339)this.initialEnergyField, (class_339)this.resetInitialEnergyButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.initial_energy_max")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.gender_spawn_chances")));
            this.genderMaleField = this.newNumberField(fieldWidth, this.genderMalePercent);
            this.resetGenderMaleButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.genderMaleField.method_1852(String.valueOf(this.defaults.getGenderMaleChancePercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetGenderMaleButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.gender_male"), (class_339)this.genderMaleField, (class_339)this.resetGenderMaleButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.gender_male")));
            this.genderFemaleField = this.newNumberField(fieldWidth, this.genderFemalePercent);
            this.resetGenderFemaleButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.genderFemaleField.method_1852(String.valueOf(this.defaults.getGenderFemaleChancePercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetGenderFemaleButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.gender_female"), (class_339)this.genderFemaleField, (class_339)this.resetGenderFemaleButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.gender_female")));
            this.genderBothField = this.newNumberField(fieldWidth, this.genderBothPercent);
            this.resetGenderBothButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.genderBothField.method_1852(String.valueOf(this.defaults.getGenderBothChancePercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetGenderBothButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.gender_both"), (class_339)this.genderBothField, (class_339)this.resetGenderBothButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.gender_both")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.player_gender_rules")));
            this.allowPlayerGenderChangeButton = class_4185.method_46430((class_2561)this.onOffLabel(this.allowPlayerGenderChangeAnytime), button -> {
                this.allowPlayerGenderChangeAnytime = !this.allowPlayerGenderChangeAnytime;
                button.method_25355(this.onOffLabel(this.allowPlayerGenderChangeAnytime));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetAllowPlayerGenderChangeButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.allowPlayerGenderChangeAnytime = this.defaults.allowPlayerGenderChangeAnytime();
                this.allowPlayerGenderChangeButton.method_25355(this.onOffLabel(this.allowPlayerGenderChangeAnytime));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetAllowPlayerGenderChangeButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.allow_player_gender_change_anytime"), (class_339)this.allowPlayerGenderChangeButton, (class_339)this.resetAllowPlayerGenderChangeButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.allow_player_gender_change_anytime")));
            this.requirePlayerGenderSelectionButton = class_4185.method_46430((class_2561)this.onOffLabel(this.requirePlayerGenderSelectionOnJoin), button -> {
                this.requirePlayerGenderSelectionOnJoin = !this.requirePlayerGenderSelectionOnJoin;
                button.method_25355(this.onOffLabel(this.requirePlayerGenderSelectionOnJoin));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetRequirePlayerGenderSelectionButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.requirePlayerGenderSelectionOnJoin = this.defaults.requirePlayerGenderSelectionOnJoin();
                this.requirePlayerGenderSelectionButton.method_25355(this.onOffLabel(this.requirePlayerGenderSelectionOnJoin));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetRequirePlayerGenderSelectionButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.require_player_gender_selection_on_join"), (class_339)this.requirePlayerGenderSelectionButton, (class_339)this.resetRequirePlayerGenderSelectionButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.require_player_gender_selection_on_join")));
            this.allowStartingMaleButton = class_4185.method_46430((class_2561)this.startingGenderToggleLabel("config.needsofnature.player_gender.male", this.isStartingGenderChoiceAllowed(1)), button -> {
                this.allowedStartingGenderMask = this.toggleAllowedStartingGenderChoice(this.allowedStartingGenderMask, 1);
                this.updateAllowedStartingGenderButtons();
                this.updateResetButtons();
            }).method_46434(0, 0, 64, 20).method_46431();
            this.allowStartingFemaleButton = class_4185.method_46430((class_2561)this.startingGenderToggleLabel("config.needsofnature.player_gender.female", this.isStartingGenderChoiceAllowed(2)), button -> {
                this.allowedStartingGenderMask = this.toggleAllowedStartingGenderChoice(this.allowedStartingGenderMask, 2);
                this.updateAllowedStartingGenderButtons();
                this.updateResetButtons();
            }).method_46434(0, 0, 70, 20).method_46431();
            this.allowStartingBothButton = class_4185.method_46430((class_2561)this.startingGenderToggleLabel("config.needsofnature.player_gender.both", this.isStartingGenderChoiceAllowed(4)), button -> {
                this.allowedStartingGenderMask = this.toggleAllowedStartingGenderChoice(this.allowedStartingGenderMask, 4);
                this.updateAllowedStartingGenderButtons();
                this.updateResetButtons();
            }).method_46434(0, 0, 98, 20).method_46431();
            this.resetAllowedStartingGendersButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.allowedStartingGenderMask = this.defaults.getAllowedStartingGenderMask();
                this.updateAllowedStartingGenderButtons();
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.allowStartingMaleButton, "config.needsofnature.tooltip.allowed_starting_genders");
            NonModMenuScreens.setTooltip((class_339)this.allowStartingFemaleButton, "config.needsofnature.tooltip.allowed_starting_genders");
            NonModMenuScreens.setTooltip((class_339)this.allowStartingBothButton, "config.needsofnature.tooltip.allowed_starting_genders");
            NonModMenuScreens.setTooltip((class_339)this.resetAllowedStartingGendersButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.allowed_starting_genders"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.allowed_starting_genders"), new class_339[]{this.allowStartingMaleButton, this.allowStartingFemaleButton, this.allowStartingBothButton, this.resetAllowedStartingGendersButton}));
            this.initialEnergyField.method_1863(ignored -> this.updateResetButtons());
            this.genderMaleField.method_1863(ignored -> this.updateResetButtons());
            this.genderFemaleField.method_1863(ignored -> this.updateResetButtons());
            this.genderBothField.method_1863(ignored -> this.updateResetButtons());
            this.initialEnergyField.method_1888(this.serverConfigEditable);
            this.genderMaleField.method_1888(this.serverConfigEditable);
            this.genderFemaleField.method_1888(this.serverConfigEditable);
            this.genderBothField.method_1888(this.serverConfigEditable);
            this.allowPlayerGenderChangeButton.field_22763 = this.serverConfigEditable;
            this.requirePlayerGenderSelectionButton.field_22763 = this.serverConfigEditable;
            this.allowStartingMaleButton.field_22763 = this.serverConfigEditable;
            this.allowStartingFemaleButton.field_22763 = this.serverConfigEditable;
            this.allowStartingBothButton.field_22763 = this.serverConfigEditable;
            this.updateResetButtons();
            int centerX = this.field_22789 / 2;
            class_4185 genderSpawnButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_button"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuDebugScreens.GenderSpawnConfigScreen(this, this.config))).method_46434(centerX - 100, this.field_22790 - 52, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)genderSpawnButton, "config.needsofnature.tooltip.gender_spawn_button");
            this.method_37063((class_364)genderSpawnButton);
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> this.saveAndClose()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, "config.needsofnature.tooltip.done_save");
            this.method_37063((class_364)doneButton);
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
        }

        public void method_25419() {
            this.saveAndClose();
        }

        private void saveAndClose() {
            if (this.serverConfigEditable) {
                this.initialEnergyMax = this.clampInitialEnergy(this.parseField(this.initialEnergyField, this.initialEnergyMax));
                this.genderMalePercent = this.clampPercent(this.parseField(this.genderMaleField, this.genderMalePercent));
                this.genderFemalePercent = this.clampPercent(this.parseField(this.genderFemaleField, this.genderFemalePercent));
                this.genderBothPercent = this.clampPercent(this.parseField(this.genderBothField, this.genderBothPercent));
                this.config.setInitialEnergyMax(this.initialEnergyMax);
                this.config.setGlobalGenderSpawnChances(new NonConfig.GenderSpawnChances(this.genderMalePercent, this.genderFemalePercent, this.genderBothPercent));
                this.config.setAllowPlayerGenderChangeAnytime(this.allowPlayerGenderChangeAnytime);
                this.config.setRequirePlayerGenderSelectionOnJoin(this.requirePlayerGenderSelectionOnJoin);
                this.config.setAllowedStartingGenderMask(this.allowedStartingGenderMask);
            }
            this.config.save();
            if (this.serverConfigEditable) {
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
            }
            class_310.method_1551().method_1507(this.parent);
        }

        private class_342 newNumberField(int w, int initial) {
            class_342 field = new class_342(this.field_22793, 0, 0, w, 20, (class_2561)class_2561.method_43473());
            field.method_1852(String.valueOf(initial));
            field.method_1880(8);
            field.method_1888(true);
            return field;
        }

        private int parseField(class_342 field, int fallback) {
            try {
                return Integer.parseInt(field.method_1882().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private int clampInitialEnergy(int v) {
            return Math.max(0, Math.min(200, v));
        }

        private int clampPercent(int v) {
            return Math.max(0, Math.min(100, v));
        }

        private boolean isIntFieldChanged(class_342 field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseField(field, defaultValue) != defaultValue;
        }

        private class_2561 onOffLabel(boolean enabled) {
            return class_2561.method_43471((String)(enabled ? "options.on" : "options.off"));
        }

        private class_2561 startingGenderToggleLabel(String labelKey, boolean enabled) {
            return class_2561.method_43469((String)(enabled ? "config.needsofnature.toggle_label.enabled" : "config.needsofnature.toggle_label.disabled"), (Object[])new Object[]{class_2561.method_43471((String)labelKey)});
        }

        private boolean isStartingGenderChoiceAllowed(int choiceBit) {
            return (this.normalizeAllowedStartingGenderMask(this.allowedStartingGenderMask) & choiceBit) != 0;
        }

        private int normalizeAllowedStartingGenderMask(int mask) {
            int normalized = mask & 7;
            return normalized == 0 ? 7 : normalized;
        }

        private int toggleAllowedStartingGenderChoice(int mask, int choiceBit) {
            int normalized = this.normalizeAllowedStartingGenderMask(mask);
            int next = normalized ^ choiceBit;
            return next == 0 ? normalized : next;
        }

        private void updateAllowedStartingGenderButtons() {
            if (this.allowStartingMaleButton != null) {
                this.allowStartingMaleButton.method_25355(this.startingGenderToggleLabel("config.needsofnature.player_gender.male", this.isStartingGenderChoiceAllowed(1)));
            }
            if (this.allowStartingFemaleButton != null) {
                this.allowStartingFemaleButton.method_25355(this.startingGenderToggleLabel("config.needsofnature.player_gender.female", this.isStartingGenderChoiceAllowed(2)));
            }
            if (this.allowStartingBothButton != null) {
                this.allowStartingBothButton.method_25355(this.startingGenderToggleLabel("config.needsofnature.player_gender.both", this.isStartingGenderChoiceAllowed(4)));
            }
        }

        private void updateResetButtons() {
            if (this.resetInitialEnergyButton != null) {
                boolean bl = this.resetInitialEnergyButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.initialEnergyField, this.defaults.getInitialEnergyMax());
            }
            if (this.resetGenderMaleButton != null) {
                boolean bl = this.resetGenderMaleButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.genderMaleField, this.defaults.getGenderMaleChancePercent());
            }
            if (this.resetGenderFemaleButton != null) {
                boolean bl = this.resetGenderFemaleButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.genderFemaleField, this.defaults.getGenderFemaleChancePercent());
            }
            if (this.resetGenderBothButton != null) {
                boolean bl = this.resetGenderBothButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.genderBothField, this.defaults.getGenderBothChancePercent());
            }
            if (this.resetAllowPlayerGenderChangeButton != null) {
                boolean bl = this.resetAllowPlayerGenderChangeButton.field_22763 = this.serverConfigEditable && this.allowPlayerGenderChangeAnytime != this.defaults.allowPlayerGenderChangeAnytime();
            }
            if (this.resetRequirePlayerGenderSelectionButton != null) {
                boolean bl = this.resetRequirePlayerGenderSelectionButton.field_22763 = this.serverConfigEditable && this.requirePlayerGenderSelectionOnJoin != this.defaults.requirePlayerGenderSelectionOnJoin();
            }
            if (this.resetAllowedStartingGendersButton != null) {
                boolean bl = this.resetAllowedStartingGendersButton.field_22763 = this.serverConfigEditable && this.allowedStartingGenderMask != this.defaults.getAllowedStartingGenderMask();
            }
            if (this.allowPlayerGenderChangeButton != null) {
                this.allowPlayerGenderChangeButton.field_22763 = this.serverConfigEditable;
            }
            if (this.requirePlayerGenderSelectionButton != null) {
                this.requirePlayerGenderSelectionButton.field_22763 = this.serverConfigEditable;
            }
            if (this.allowStartingMaleButton != null) {
                this.allowStartingMaleButton.field_22763 = this.serverConfigEditable;
            }
            if (this.allowStartingFemaleButton != null) {
                this.allowStartingFemaleButton.field_22763 = this.serverConfigEditable;
            }
            if (this.allowStartingBothButton != null) {
                this.allowStartingBothButton.field_22763 = this.serverConfigEditable;
            }
        }
    }

    static class GameplayCategoryScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;

        protected GameplayCategoryScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.gameplay_title"));
            this.parent = parent;
            this.config = config;
        }

        protected void method_25426() {
            int centerX = this.field_22789 / 2;
            int y = this.field_22790 / 2 - 48;
            int gap = 24;
            class_4185 worldButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.section.world"), button -> class_310.method_1551().method_1507((class_437)new GameplayWorldScreen(this, this.config))).method_46434(centerX - 100, y, 200, 20).method_46431();
            this.method_37063((class_364)worldButton);
            class_4185 energyButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.section.energy"), button -> class_310.method_1551().method_1507((class_437)new GameplayEnergyScreen(this, this.config))).method_46434(centerX - 100, y += gap, 200, 20).method_46431();
            this.method_37063((class_364)energyButton);
            class_4185 animationsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.section.animations"), button -> class_310.method_1551().method_1507((class_437)new GameplayAnimationsScreen(this, this.config))).method_46434(centerX - 100, y += gap, 200, 20).method_46431();
            this.method_37063((class_364)animationsButton);
            class_4185 difficultyButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.section.difficulty"), button -> class_310.method_1551().method_1507((class_437)new GameplayDifficultyScreen(this, this.config))).method_46434(centerX - 100, y += gap, 200, 20).method_46431();
            this.method_37063((class_364)difficultyButton);
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            this.method_37063((class_364)doneButton);
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
        }

        public void method_25419() {
            class_310.method_1551().method_1507(this.parent);
        }
    }
}

