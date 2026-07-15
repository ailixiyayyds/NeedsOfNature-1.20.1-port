/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.widget.ClickableWidget
 *  net.minecraft.client.gui.widget.TextFieldWidget
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.tooltip.Tooltip
 */
package com.nonid.integration;

import com.nonid.NonConfig;
import com.nonid.client.NonHudOverlay;
import com.nonid.integration.NonModMenuDebugScreens;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import java.util.Locale;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;

final class NonModMenuGameplayScreens {
    private NonModMenuGameplayScreens() {
    }

    static class GameplayDifficultyScreen
    extends Screen {
        private final Screen parent;
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
        private TextFieldWidget attackHitsField;
        private TextFieldWidget attackDecayField;
        private TextFieldWidget attackEscapeDamageDifficultyPercentField;
        private TextFieldWidget attackEscapeInvulnerabilitySecondsField;
        private TextFieldWidget attackEscapeAnimationProtectionSecondsField;
        private TextFieldWidget postEscapeGatherMaxMobsField;
        private TextFieldWidget attackOutcomeFailsafeThresholdField;
        private TextFieldWidget lastDefeatedEnergyThresholdField;
        private TextFieldWidget lastDefeatedSearchRadiusField;
        private TextFieldWidget lastDefeatedCooldownSecondsField;
        private ButtonWidget resetAttackHitsButton;
        private ButtonWidget resetAttackDecayButton;
        private ButtonWidget resetAttackEscapeDamageDifficultyPercentButton;
        private ButtonWidget resetAttackEscapeInvulnerabilitySecondsButton;
        private ButtonWidget resetAttackEscapeAnimationProtectionSecondsButton;
        private ButtonWidget resetPostEscapeGatherMaxMobsButton;
        private ButtonWidget blockAnimationsWhileRidingLivingEntitiesButton;
        private ButtonWidget resetBlockAnimationsWhileRidingLivingEntitiesButton;
        private ButtonWidget blockAnimationsWhileRidingVehiclesButton;
        private ButtonWidget resetBlockAnimationsWhileRidingVehiclesButton;
        private ButtonWidget resetAttackOutcomeFailsafeThresholdButton;
        private ButtonWidget lastDefeatedEnabledButton;
        private ButtonWidget resetLastDefeatedEnabledButton;
        private ButtonWidget resetLastDefeatedEnergyThresholdButton;
        private ButtonWidget resetLastDefeatedSearchRadiusButton;
        private ButtonWidget resetLastDefeatedCooldownSecondsButton;
        private ButtonWidget allowCraftingTableSkinRepairButton;
        private ButtonWidget resetAllowCraftingTableSkinRepairButton;
        private ButtonWidget keepMessAfterDeathButton;
        private ButtonWidget resetKeepMessAfterDeathButton;
        private ButtonWidget keepRippedSkinAfterDeathButton;
        private ButtonWidget resetKeepRippedSkinAfterDeathButton;
        private ButtonWidget keepLiquidTankAfterDeathButton;
        private ButtonWidget resetKeepLiquidTankAfterDeathButton;
        private ButtonWidget keepPregnancyAfterDeathButton;
        private ButtonWidget resetKeepPregnancyAfterDeathButton;
        private boolean serverConfigEditable;

        protected GameplayDifficultyScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.section.difficulty"));
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

        protected void init() {
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
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild((Element)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.attack_escape")));
            this.attackHitsField = this.newNumberField(fieldWidth, this.attackHits);
            this.resetAttackHitsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.attackHitsField.setText(String.valueOf(this.defaults.getAttackEscapeHits()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAttackHitsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.attack_hits"), (ClickableWidget)this.attackHitsField, (ClickableWidget)this.resetAttackHitsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_hits")));
            this.attackDecayField = this.newDecimalField(fieldWidth, this.attackDecay);
            this.resetAttackDecayButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.attackDecayField.setText(String.valueOf(this.defaults.getAttackDecayPerSecond()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAttackDecayButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.attack_decay"), (ClickableWidget)this.attackDecayField, (ClickableWidget)this.resetAttackDecayButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_decay")));
            this.attackEscapeDamageDifficultyPercentField = this.newNumberField(fieldWidth, this.attackEscapeDamageDifficultyPercent);
            this.resetAttackEscapeDamageDifficultyPercentButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.attackEscapeDamageDifficultyPercentField.setText(String.valueOf(this.defaults.getAttackEscapeDamageDifficultyPercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAttackEscapeDamageDifficultyPercentButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.attack_escape_damage_difficulty_percent"), (ClickableWidget)this.attackEscapeDamageDifficultyPercentField, (ClickableWidget)this.resetAttackEscapeDamageDifficultyPercentButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_escape_damage_difficulty_percent")));
            this.attackEscapeInvulnerabilitySecondsField = this.newNumberField(fieldWidth, this.attackEscapeInvulnerabilitySeconds);
            this.resetAttackEscapeInvulnerabilitySecondsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.attackEscapeInvulnerabilitySecondsField.setText(String.valueOf(this.defaults.getAttackEscapeInvulnerabilitySeconds()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAttackEscapeInvulnerabilitySecondsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.attack_escape_invulnerability_seconds"), (ClickableWidget)this.attackEscapeInvulnerabilitySecondsField, (ClickableWidget)this.resetAttackEscapeInvulnerabilitySecondsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_escape_invulnerability_seconds")));
            this.attackEscapeAnimationProtectionSecondsField = this.newNumberField(fieldWidth, this.attackEscapeAnimationProtectionSeconds);
            this.resetAttackEscapeAnimationProtectionSecondsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.attackEscapeAnimationProtectionSecondsField.setText(String.valueOf(this.defaults.getAttackEscapeAnimationProtectionSeconds()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAttackEscapeAnimationProtectionSecondsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.attack_escape_animation_protection_seconds"), (ClickableWidget)this.attackEscapeAnimationProtectionSecondsField, (ClickableWidget)this.resetAttackEscapeAnimationProtectionSecondsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_escape_animation_protection_seconds")));
            this.postEscapeGatherMaxMobsField = this.newNumberField(fieldWidth, this.postEscapeGatherMaxMobs);
            this.resetPostEscapeGatherMaxMobsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.postEscapeGatherMaxMobsField.setText(String.valueOf(this.defaults.getPostEscapeGatherMaxMobs()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPostEscapeGatherMaxMobsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.post_escape_gather_max_mobs"), (ClickableWidget)this.postEscapeGatherMaxMobsField, (ClickableWidget)this.resetPostEscapeGatherMaxMobsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.post_escape_gather_max_mobs")));
            this.attackOutcomeFailsafeThresholdField = this.newNumberField(fieldWidth, this.attackOutcomeFailsafeThreshold);
            this.resetAttackOutcomeFailsafeThresholdButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.attackOutcomeFailsafeThresholdField.setText(String.valueOf(this.defaults.getAttackOutcomeFailsafeThreshold()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAttackOutcomeFailsafeThresholdButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.attack_outcome_failsafe_threshold"), (ClickableWidget)this.attackOutcomeFailsafeThresholdField, (ClickableWidget)this.resetAttackOutcomeFailsafeThresholdButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_outcome_failsafe_threshold")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.mount_protection")));
            this.blockAnimationsWhileRidingLivingEntitiesButton = ButtonWidget.builder((Text)this.onOffLabel(this.blockAnimationsWhileRidingLivingEntities), button -> {
                this.blockAnimationsWhileRidingLivingEntities = !this.blockAnimationsWhileRidingLivingEntities;
                button.setMessage(this.onOffLabel(this.blockAnimationsWhileRidingLivingEntities));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.blockAnimationsWhileRidingLivingEntitiesButton, "config.needsofnature.tooltip.block_animations_while_riding_living_entities");
            this.resetBlockAnimationsWhileRidingLivingEntitiesButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.blockAnimationsWhileRidingLivingEntities = this.defaults.blockAnimationsWhileRidingLivingEntities();
                this.blockAnimationsWhileRidingLivingEntitiesButton.setMessage(this.onOffLabel(this.blockAnimationsWhileRidingLivingEntities));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetBlockAnimationsWhileRidingLivingEntitiesButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.block_animations_while_riding_living_entities"), (ClickableWidget)this.blockAnimationsWhileRidingLivingEntitiesButton, (ClickableWidget)this.resetBlockAnimationsWhileRidingLivingEntitiesButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.block_animations_while_riding_living_entities")));
            this.blockAnimationsWhileRidingVehiclesButton = ButtonWidget.builder((Text)this.onOffLabel(this.blockAnimationsWhileRidingVehicles), button -> {
                this.blockAnimationsWhileRidingVehicles = !this.blockAnimationsWhileRidingVehicles;
                button.setMessage(this.onOffLabel(this.blockAnimationsWhileRidingVehicles));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.blockAnimationsWhileRidingVehiclesButton, "config.needsofnature.tooltip.block_animations_while_riding_vehicles");
            this.resetBlockAnimationsWhileRidingVehiclesButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.blockAnimationsWhileRidingVehicles = this.defaults.blockAnimationsWhileRidingVehicles();
                this.blockAnimationsWhileRidingVehiclesButton.setMessage(this.onOffLabel(this.blockAnimationsWhileRidingVehicles));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetBlockAnimationsWhileRidingVehiclesButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.block_animations_while_riding_vehicles"), (ClickableWidget)this.blockAnimationsWhileRidingVehiclesButton, (ClickableWidget)this.resetBlockAnimationsWhileRidingVehiclesButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.block_animations_while_riding_vehicles")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.last_defeated")));
            this.lastDefeatedEnabledButton = ButtonWidget.builder((Text)this.onOffLabel(this.lastDefeatedEnabled), button -> {
                this.lastDefeatedEnabled = !this.lastDefeatedEnabled;
                button.setMessage(this.onOffLabel(this.lastDefeatedEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetLastDefeatedEnabledButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.lastDefeatedEnabled = this.defaults.isLastDefeatedEnabled();
                this.lastDefeatedEnabledButton.setMessage(this.onOffLabel(this.lastDefeatedEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetLastDefeatedEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.last_defeated_enabled"), (ClickableWidget)this.lastDefeatedEnabledButton, (ClickableWidget)this.resetLastDefeatedEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.last_defeated_enabled")));
            this.lastDefeatedEnergyThresholdField = this.newNumberField(fieldWidth, this.lastDefeatedEnergyThreshold);
            this.resetLastDefeatedEnergyThresholdButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.lastDefeatedEnergyThresholdField.setText(String.valueOf(this.defaults.getLastDefeatedEnergyThreshold()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetLastDefeatedEnergyThresholdButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.last_defeated_energy_threshold"), (ClickableWidget)this.lastDefeatedEnergyThresholdField, (ClickableWidget)this.resetLastDefeatedEnergyThresholdButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.last_defeated_energy_threshold")));
            this.lastDefeatedSearchRadiusField = this.newNumberField(fieldWidth, this.lastDefeatedSearchRadius);
            this.resetLastDefeatedSearchRadiusButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.lastDefeatedSearchRadiusField.setText(String.valueOf(this.defaults.getLastDefeatedSearchRadius()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetLastDefeatedSearchRadiusButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.last_defeated_search_radius"), (ClickableWidget)this.lastDefeatedSearchRadiusField, (ClickableWidget)this.resetLastDefeatedSearchRadiusButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.last_defeated_search_radius")));
            this.lastDefeatedCooldownSecondsField = this.newNumberField(fieldWidth, this.lastDefeatedCooldownSeconds);
            this.resetLastDefeatedCooldownSecondsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.lastDefeatedCooldownSecondsField.setText(String.valueOf(this.defaults.getLastDefeatedCooldownSeconds()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetLastDefeatedCooldownSecondsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.last_defeated_cooldown_seconds"), (ClickableWidget)this.lastDefeatedCooldownSecondsField, (ClickableWidget)this.resetLastDefeatedCooldownSecondsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.last_defeated_cooldown_seconds")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.ripped_skin_repair")));
            this.allowCraftingTableSkinRepairButton = ButtonWidget.builder((Text)this.onOffLabel(this.allowCraftingTableSkinRepair), button -> {
                this.allowCraftingTableSkinRepair = !this.allowCraftingTableSkinRepair;
                button.setMessage(this.onOffLabel(this.allowCraftingTableSkinRepair));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.allowCraftingTableSkinRepairButton, "config.needsofnature.tooltip.allow_crafting_table_skin_repair");
            this.resetAllowCraftingTableSkinRepairButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.allowCraftingTableSkinRepair = this.defaults.isCraftingTableSkinRepairAllowed();
                this.allowCraftingTableSkinRepairButton.setMessage(this.onOffLabel(this.allowCraftingTableSkinRepair));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAllowCraftingTableSkinRepairButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.allow_crafting_table_skin_repair"), (ClickableWidget)this.allowCraftingTableSkinRepairButton, (ClickableWidget)this.resetAllowCraftingTableSkinRepairButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.allow_crafting_table_skin_repair")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.death_persistence")));
            this.keepMessAfterDeathButton = ButtonWidget.builder((Text)this.onOffLabel(this.keepMessAfterDeath), button -> {
                this.keepMessAfterDeath = !this.keepMessAfterDeath;
                button.setMessage(this.onOffLabel(this.keepMessAfterDeath));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.keepMessAfterDeathButton, "config.needsofnature.tooltip.keep_mess_after_death");
            this.resetKeepMessAfterDeathButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.keepMessAfterDeath = this.defaults.keepMessAfterDeath();
                this.keepMessAfterDeathButton.setMessage(this.onOffLabel(this.keepMessAfterDeath));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetKeepMessAfterDeathButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.keep_mess_after_death"), (ClickableWidget)this.keepMessAfterDeathButton, (ClickableWidget)this.resetKeepMessAfterDeathButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.keep_mess_after_death")));
            this.keepRippedSkinAfterDeathButton = ButtonWidget.builder((Text)this.onOffLabel(this.keepRippedSkinAfterDeath), button -> {
                this.keepRippedSkinAfterDeath = !this.keepRippedSkinAfterDeath;
                button.setMessage(this.onOffLabel(this.keepRippedSkinAfterDeath));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.keepRippedSkinAfterDeathButton, "config.needsofnature.tooltip.keep_ripped_skin_after_death");
            this.resetKeepRippedSkinAfterDeathButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.keepRippedSkinAfterDeath = this.defaults.keepRippedSkinAfterDeath();
                this.keepRippedSkinAfterDeathButton.setMessage(this.onOffLabel(this.keepRippedSkinAfterDeath));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetKeepRippedSkinAfterDeathButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.keep_ripped_skin_after_death"), (ClickableWidget)this.keepRippedSkinAfterDeathButton, (ClickableWidget)this.resetKeepRippedSkinAfterDeathButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.keep_ripped_skin_after_death")));
            this.keepLiquidTankAfterDeathButton = ButtonWidget.builder((Text)this.onOffLabel(this.keepLiquidTankAfterDeath), button -> {
                this.keepLiquidTankAfterDeath = !this.keepLiquidTankAfterDeath;
                button.setMessage(this.onOffLabel(this.keepLiquidTankAfterDeath));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.keepLiquidTankAfterDeathButton, "config.needsofnature.tooltip.keep_liquid_tank_after_death");
            this.resetKeepLiquidTankAfterDeathButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.keepLiquidTankAfterDeath = this.defaults.keepLiquidTankAfterDeath();
                this.keepLiquidTankAfterDeathButton.setMessage(this.onOffLabel(this.keepLiquidTankAfterDeath));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetKeepLiquidTankAfterDeathButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.keep_liquid_tank_after_death"), (ClickableWidget)this.keepLiquidTankAfterDeathButton, (ClickableWidget)this.resetKeepLiquidTankAfterDeathButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.keep_liquid_tank_after_death")));
            this.keepPregnancyAfterDeathButton = ButtonWidget.builder((Text)this.onOffLabel(this.keepPregnancyAfterDeath), button -> {
                this.keepPregnancyAfterDeath = !this.keepPregnancyAfterDeath;
                button.setMessage(this.onOffLabel(this.keepPregnancyAfterDeath));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.keepPregnancyAfterDeathButton, "config.needsofnature.tooltip.keep_pregnancy_after_death");
            this.resetKeepPregnancyAfterDeathButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.keepPregnancyAfterDeath = this.defaults.keepPregnancyAfterDeath();
                this.keepPregnancyAfterDeathButton.setMessage(this.onOffLabel(this.keepPregnancyAfterDeath));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetKeepPregnancyAfterDeathButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.keep_pregnancy_after_death"), (ClickableWidget)this.keepPregnancyAfterDeathButton, (ClickableWidget)this.resetKeepPregnancyAfterDeathButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.keep_pregnancy_after_death")));
            this.attackHitsField.setChangedListener(ignored -> this.updateResetButtons());
            this.attackDecayField.setChangedListener(ignored -> this.updateResetButtons());
            this.attackEscapeDamageDifficultyPercentField.setChangedListener(ignored -> this.updateResetButtons());
            this.attackEscapeInvulnerabilitySecondsField.setChangedListener(ignored -> this.updateResetButtons());
            this.attackEscapeAnimationProtectionSecondsField.setChangedListener(ignored -> this.updateResetButtons());
            this.postEscapeGatherMaxMobsField.setChangedListener(ignored -> this.updateResetButtons());
            this.attackOutcomeFailsafeThresholdField.setChangedListener(ignored -> this.updateResetButtons());
            this.lastDefeatedEnergyThresholdField.setChangedListener(ignored -> this.updateResetButtons());
            this.lastDefeatedSearchRadiusField.setChangedListener(ignored -> this.updateResetButtons());
            this.lastDefeatedCooldownSecondsField.setChangedListener(ignored -> this.updateResetButtons());
            this.attackHitsField.setEditable(this.serverConfigEditable);
            this.attackDecayField.setEditable(this.serverConfigEditable);
            this.attackEscapeDamageDifficultyPercentField.setEditable(this.serverConfigEditable);
            this.attackEscapeInvulnerabilitySecondsField.setEditable(this.serverConfigEditable);
            this.attackEscapeAnimationProtectionSecondsField.setEditable(this.serverConfigEditable);
            this.postEscapeGatherMaxMobsField.setEditable(this.serverConfigEditable);
            this.attackOutcomeFailsafeThresholdField.setEditable(this.serverConfigEditable);
            this.lastDefeatedEnergyThresholdField.setEditable(this.serverConfigEditable);
            this.lastDefeatedSearchRadiusField.setEditable(this.serverConfigEditable);
            this.lastDefeatedCooldownSecondsField.setEditable(this.serverConfigEditable);
            if (this.lastDefeatedEnabledButton != null) {
                this.lastDefeatedEnabledButton.active = this.serverConfigEditable;
            }
            if (this.blockAnimationsWhileRidingLivingEntitiesButton != null) {
                this.blockAnimationsWhileRidingLivingEntitiesButton.active = this.serverConfigEditable;
            }
            if (this.blockAnimationsWhileRidingVehiclesButton != null) {
                this.blockAnimationsWhileRidingVehiclesButton.active = this.serverConfigEditable;
            }
            if (this.allowCraftingTableSkinRepairButton != null) {
                this.allowCraftingTableSkinRepairButton.active = this.serverConfigEditable;
            }
            if (this.keepMessAfterDeathButton != null) {
                this.keepMessAfterDeathButton.active = this.serverConfigEditable;
            }
            if (this.keepRippedSkinAfterDeathButton != null) {
                this.keepRippedSkinAfterDeathButton.active = this.serverConfigEditable;
            }
            if (this.keepLiquidTankAfterDeathButton != null) {
                this.keepLiquidTankAfterDeathButton.active = this.serverConfigEditable;
            }
            if (this.keepPregnancyAfterDeathButton != null) {
                this.keepPregnancyAfterDeathButton.active = this.serverConfigEditable;
            }
            this.updateResetButtons();
            int centerX = this.width / 2;
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.saveAndClose()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.addDrawableChild((Element)doneButton);
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }

        public void close() {
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
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private TextFieldWidget newNumberField(int w, int initial) {
            TextFieldWidget field = new TextFieldWidget(this.textRenderer, 0, 0, w, 20, (Text)Text.empty());
            field.setText(String.valueOf(initial));
            field.setMaxLength(8);
            field.setEditable(true);
            return field;
        }

        private TextFieldWidget newDecimalField(int w, double initial) {
            TextFieldWidget field = new TextFieldWidget(this.textRenderer, 0, 0, w, 20, (Text)Text.empty());
            int maxLen = 16;
            field.setMaxLength(maxLen);
            field.setText(this.trimToMax(this.formatDecimal(initial), maxLen));
            field.setEditable(true);
            return field;
        }

        private int parseField(TextFieldWidget field, int fallback) {
            try {
                return Integer.parseInt(field.getText().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private double parseDecimalField(TextFieldWidget field, double fallback) {
            try {
                return Double.parseDouble(field.getText().trim());
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

        private Text onOffLabel(boolean enabled) {
            return Text.translatable((String)(enabled ? "options.on" : "options.off"));
        }

        private boolean isIntFieldChanged(TextFieldWidget field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseField(field, defaultValue) != defaultValue;
        }

        private boolean isDoubleFieldChanged(TextFieldWidget field, double defaultValue) {
            if (field == null) {
                return false;
            }
            double value = this.parseDecimalField(field, defaultValue);
            return Math.abs(value - defaultValue) >= 1.0E-4;
        }

        private void updateResetButtons() {
            if (this.resetAttackHitsButton != null) {
                boolean bl = this.resetAttackHitsButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.attackHitsField, this.defaults.getAttackEscapeHits());
            }
            if (this.resetAttackDecayButton != null) {
                boolean bl = this.resetAttackDecayButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.attackDecayField, this.defaults.getAttackDecayPerSecond());
            }
            if (this.resetAttackEscapeDamageDifficultyPercentButton != null) {
                boolean bl = this.resetAttackEscapeDamageDifficultyPercentButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.attackEscapeDamageDifficultyPercentField, this.defaults.getAttackEscapeDamageDifficultyPercent());
            }
            if (this.resetAttackEscapeInvulnerabilitySecondsButton != null) {
                boolean bl = this.resetAttackEscapeInvulnerabilitySecondsButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.attackEscapeInvulnerabilitySecondsField, this.defaults.getAttackEscapeInvulnerabilitySeconds());
            }
            if (this.resetAttackEscapeAnimationProtectionSecondsButton != null) {
                boolean bl = this.resetAttackEscapeAnimationProtectionSecondsButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.attackEscapeAnimationProtectionSecondsField, this.defaults.getAttackEscapeAnimationProtectionSeconds());
            }
            if (this.resetPostEscapeGatherMaxMobsButton != null) {
                boolean bl = this.resetPostEscapeGatherMaxMobsButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.postEscapeGatherMaxMobsField, this.defaults.getPostEscapeGatherMaxMobs());
            }
            if (this.resetAttackOutcomeFailsafeThresholdButton != null) {
                boolean bl = this.resetAttackOutcomeFailsafeThresholdButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.attackOutcomeFailsafeThresholdField, this.defaults.getAttackOutcomeFailsafeThreshold());
            }
            if (this.resetBlockAnimationsWhileRidingLivingEntitiesButton != null) {
                boolean bl = this.resetBlockAnimationsWhileRidingLivingEntitiesButton.active = this.serverConfigEditable && this.blockAnimationsWhileRidingLivingEntities != this.defaults.blockAnimationsWhileRidingLivingEntities();
            }
            if (this.resetBlockAnimationsWhileRidingVehiclesButton != null) {
                boolean bl = this.resetBlockAnimationsWhileRidingVehiclesButton.active = this.serverConfigEditable && this.blockAnimationsWhileRidingVehicles != this.defaults.blockAnimationsWhileRidingVehicles();
            }
            if (this.resetLastDefeatedEnabledButton != null) {
                boolean bl = this.resetLastDefeatedEnabledButton.active = this.serverConfigEditable && this.lastDefeatedEnabled != this.defaults.isLastDefeatedEnabled();
            }
            if (this.resetLastDefeatedEnergyThresholdButton != null) {
                boolean bl = this.resetLastDefeatedEnergyThresholdButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.lastDefeatedEnergyThresholdField, this.defaults.getLastDefeatedEnergyThreshold());
            }
            if (this.resetLastDefeatedSearchRadiusButton != null) {
                boolean bl = this.resetLastDefeatedSearchRadiusButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.lastDefeatedSearchRadiusField, this.defaults.getLastDefeatedSearchRadius());
            }
            if (this.resetLastDefeatedCooldownSecondsButton != null) {
                boolean bl = this.resetLastDefeatedCooldownSecondsButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.lastDefeatedCooldownSecondsField, this.defaults.getLastDefeatedCooldownSeconds());
            }
            if (this.resetAllowCraftingTableSkinRepairButton != null) {
                boolean bl = this.resetAllowCraftingTableSkinRepairButton.active = this.serverConfigEditable && this.allowCraftingTableSkinRepair != this.defaults.isCraftingTableSkinRepairAllowed();
            }
            if (this.resetKeepMessAfterDeathButton != null) {
                boolean bl = this.resetKeepMessAfterDeathButton.active = this.serverConfigEditable && this.keepMessAfterDeath != this.defaults.keepMessAfterDeath();
            }
            if (this.resetKeepRippedSkinAfterDeathButton != null) {
                boolean bl = this.resetKeepRippedSkinAfterDeathButton.active = this.serverConfigEditable && this.keepRippedSkinAfterDeath != this.defaults.keepRippedSkinAfterDeath();
            }
            if (this.resetKeepLiquidTankAfterDeathButton != null) {
                boolean bl = this.resetKeepLiquidTankAfterDeathButton.active = this.serverConfigEditable && this.keepLiquidTankAfterDeath != this.defaults.keepLiquidTankAfterDeath();
            }
            if (this.resetKeepPregnancyAfterDeathButton != null) {
                boolean bl = this.resetKeepPregnancyAfterDeathButton.active = this.serverConfigEditable && this.keepPregnancyAfterDeath != this.defaults.keepPregnancyAfterDeath();
            }
            if (this.lastDefeatedEnabledButton != null) {
                this.lastDefeatedEnabledButton.active = this.serverConfigEditable;
            }
            if (this.blockAnimationsWhileRidingLivingEntitiesButton != null) {
                this.blockAnimationsWhileRidingLivingEntitiesButton.active = this.serverConfigEditable;
            }
            if (this.blockAnimationsWhileRidingVehiclesButton != null) {
                this.blockAnimationsWhileRidingVehiclesButton.active = this.serverConfigEditable;
            }
            if (this.allowCraftingTableSkinRepairButton != null) {
                this.allowCraftingTableSkinRepairButton.active = this.serverConfigEditable;
            }
            if (this.keepMessAfterDeathButton != null) {
                this.keepMessAfterDeathButton.active = this.serverConfigEditable;
            }
            if (this.keepRippedSkinAfterDeathButton != null) {
                this.keepRippedSkinAfterDeathButton.active = this.serverConfigEditable;
            }
            if (this.keepLiquidTankAfterDeathButton != null) {
                this.keepLiquidTankAfterDeathButton.active = this.serverConfigEditable;
            }
            if (this.keepPregnancyAfterDeathButton != null) {
                this.keepPregnancyAfterDeathButton.active = this.serverConfigEditable;
            }
        }
    }

    static class GameplayAnimationsScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private int loopSeconds;
        private int peakLoopSeconds;
        private int maleMalePercent;
        private int femaleFemalePercent;
        private int entityEntityPercent;
        private int multiActorJoinPercent;
        private TextFieldWidget loopField;
        private TextFieldWidget peakLoopField;
        private TextFieldWidget maleMaleField;
        private TextFieldWidget femaleFemaleField;
        private TextFieldWidget entityEntityField;
        private TextFieldWidget multiActorJoinField;
        private ButtonWidget resetLoopButton;
        private ButtonWidget resetPeakLoopButton;
        private ButtonWidget resetMaleMaleButton;
        private ButtonWidget resetFemaleFemaleButton;
        private ButtonWidget resetEntityEntityButton;
        private ButtonWidget resetMultiActorJoinButton;
        private boolean serverConfigEditable;

        protected GameplayAnimationsScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.section.animations"));
            this.parent = parent;
            this.config = config;
            this.loopSeconds = NonHudOverlay.getRuntimeLoopSeconds();
            this.peakLoopSeconds = NonHudOverlay.getRuntimePeakLoopSeconds();
            this.maleMalePercent = config.getMaleMaleChancePercent();
            this.femaleFemalePercent = config.getFemaleFemaleChancePercent();
            this.entityEntityPercent = config.getEntityEntityChancePercent();
            this.multiActorJoinPercent = config.getMultiActorJoinChancePercent();
        }

        protected void init() {
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
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild((Element)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.animation_pacing")));
            this.loopField = this.newNumberField(fieldWidth, this.loopSeconds);
            this.resetLoopButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.loopField.setText(String.valueOf(this.defaults.getLoopProgressSeconds()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetLoopButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.loop_seconds"), (ClickableWidget)this.loopField, (ClickableWidget)this.resetLoopButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.loop_seconds")));
            this.peakLoopField = this.newNumberField(fieldWidth, this.peakLoopSeconds);
            this.resetPeakLoopButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.peakLoopField.setText(String.valueOf(this.defaults.getPeakLoopProgressSeconds()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPeakLoopButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.peak_loop_seconds"), (ClickableWidget)this.peakLoopField, (ClickableWidget)this.resetPeakLoopButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.peak_loop_seconds")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.match_chances")));
            this.maleMaleField = this.newNumberField(fieldWidth, this.maleMalePercent);
            this.resetMaleMaleButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.maleMaleField.setText(String.valueOf(this.defaults.getMaleMaleChancePercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetMaleMaleButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.male_male_chance"), (ClickableWidget)this.maleMaleField, (ClickableWidget)this.resetMaleMaleButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.male_male_chance")));
            this.femaleFemaleField = this.newNumberField(fieldWidth, this.femaleFemalePercent);
            this.resetFemaleFemaleButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.femaleFemaleField.setText(String.valueOf(this.defaults.getFemaleFemaleChancePercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetFemaleFemaleButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.female_female_chance"), (ClickableWidget)this.femaleFemaleField, (ClickableWidget)this.resetFemaleFemaleButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.female_female_chance")));
            this.entityEntityField = this.newNumberField(fieldWidth, this.entityEntityPercent);
            this.resetEntityEntityButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.entityEntityField.setText(String.valueOf(this.defaults.getEntityEntityChancePercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetEntityEntityButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.entity_entity_chance"), (ClickableWidget)this.entityEntityField, (ClickableWidget)this.resetEntityEntityButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.entity_entity_chance")));
            this.multiActorJoinField = this.newNumberField(fieldWidth, this.multiActorJoinPercent);
            this.resetMultiActorJoinButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.multiActorJoinField.setText(String.valueOf(this.defaults.getMultiActorJoinChancePercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetMultiActorJoinButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.multi_actor_join_chance"), (ClickableWidget)this.multiActorJoinField, (ClickableWidget)this.resetMultiActorJoinButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.multi_actor_join_chance")));
            this.loopField.setChangedListener(ignored -> this.updateResetButtons());
            this.peakLoopField.setChangedListener(ignored -> this.updateResetButtons());
            this.maleMaleField.setChangedListener(ignored -> this.updateResetButtons());
            this.femaleFemaleField.setChangedListener(ignored -> this.updateResetButtons());
            this.entityEntityField.setChangedListener(ignored -> this.updateResetButtons());
            this.multiActorJoinField.setChangedListener(ignored -> this.updateResetButtons());
            this.loopField.setEditable(this.serverConfigEditable);
            this.peakLoopField.setEditable(this.serverConfigEditable);
            this.maleMaleField.setEditable(this.serverConfigEditable);
            this.femaleFemaleField.setEditable(this.serverConfigEditable);
            this.entityEntityField.setEditable(this.serverConfigEditable);
            this.multiActorJoinField.setEditable(this.serverConfigEditable);
            this.updateResetButtons();
            int centerX = this.width / 2;
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.saveAndClose()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.addDrawableChild((Element)doneButton);
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }

        public void close() {
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
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private TextFieldWidget newNumberField(int w, int initial) {
            TextFieldWidget field = new TextFieldWidget(this.textRenderer, 0, 0, w, 20, (Text)Text.empty());
            field.setText(String.valueOf(initial));
            field.setMaxLength(8);
            field.setEditable(true);
            return field;
        }

        private int parseField(TextFieldWidget field, int fallback) {
            try {
                return Integer.parseInt(field.getText().trim());
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

        private boolean isIntFieldChanged(TextFieldWidget field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseField(field, defaultValue) != defaultValue;
        }

        private void updateResetButtons() {
            if (this.resetLoopButton != null) {
                boolean bl = this.resetLoopButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.loopField, this.defaults.getLoopProgressSeconds());
            }
            if (this.resetPeakLoopButton != null) {
                boolean bl = this.resetPeakLoopButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.peakLoopField, this.defaults.getPeakLoopProgressSeconds());
            }
            if (this.resetMaleMaleButton != null) {
                boolean bl = this.resetMaleMaleButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.maleMaleField, this.defaults.getMaleMaleChancePercent());
            }
            if (this.resetFemaleFemaleButton != null) {
                boolean bl = this.resetFemaleFemaleButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.femaleFemaleField, this.defaults.getFemaleFemaleChancePercent());
            }
            if (this.resetEntityEntityButton != null) {
                boolean bl = this.resetEntityEntityButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.entityEntityField, this.defaults.getEntityEntityChancePercent());
            }
            if (this.resetMultiActorJoinButton != null) {
                this.resetMultiActorJoinButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.multiActorJoinField, this.defaults.getMultiActorJoinChancePercent());
            }
        }
    }

    static class GameplayEnergyScreen
    extends Screen {
        private final Screen parent;
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
        private TextFieldWidget energyGainRateField;
        private TextFieldWidget nearAnimationEnergyGainMultField;
        private TextFieldWidget playerEnergyAuraMultLowField;
        private TextFieldWidget playerEnergyAuraMultMidField;
        private TextFieldWidget playerEnergyAuraMultHighField;
        private TextFieldWidget destroyedSkinAuraMultStage1Field;
        private TextFieldWidget destroyedSkinAuraMultStage2Field;
        private TextFieldWidget destroyedSkinAuraMultStage3Field;
        private TextFieldWidget destroyedSkinAuraMultStage4Field;
        private TextFieldWidget normalDamageDestroyedSkinChanceField;
        private TextFieldWidget playerEnergyAuraRadiusMinField;
        private TextFieldWidget playerEnergyAuraRadiusMaxField;
        private TextFieldWidget playerEnergyAuraPulseTicksField;
        private ButtonWidget resetEnergyGainRateButton;
        private ButtonWidget resetNearAnimationEnergyGainMultButton;
        private ButtonWidget resetPlayerEnergyAuraMultLowButton;
        private ButtonWidget resetPlayerEnergyAuraMultMidButton;
        private ButtonWidget resetPlayerEnergyAuraMultHighButton;
        private ButtonWidget resetDestroyedSkinAuraMultStage1Button;
        private ButtonWidget resetDestroyedSkinAuraMultStage2Button;
        private ButtonWidget resetDestroyedSkinAuraMultStage3Button;
        private ButtonWidget resetDestroyedSkinAuraMultStage4Button;
        private ButtonWidget resetNormalDamageDestroyedSkinChanceButton;
        private ButtonWidget resetPlayerEnergyAuraRadiusMinButton;
        private ButtonWidget resetPlayerEnergyAuraRadiusMaxButton;
        private ButtonWidget resetPlayerEnergyAuraPulseTicksButton;
        private boolean serverConfigEditable;

        protected GameplayEnergyScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.section.energy"));
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

        protected void init() {
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
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild((Element)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.energy_gain")));
            this.energyGainRateField = this.newDecimalField(fieldWidth, this.energyGainRate);
            this.resetEnergyGainRateButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.energyGainRateField.setText(String.valueOf(this.defaults.getEnergyGainRate()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetEnergyGainRateButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.energy_gain_rate"), (ClickableWidget)this.energyGainRateField, (ClickableWidget)this.resetEnergyGainRateButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.energy_gain_rate")));
            this.nearAnimationEnergyGainMultField = this.newDecimalField(fieldWidth, this.nearAnimationEnergyGainMult);
            this.resetNearAnimationEnergyGainMultButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.nearAnimationEnergyGainMultField.setText(String.valueOf(this.defaults.getNearAnimationEnergyGainMult()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetNearAnimationEnergyGainMultButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.near_animation_energy_gain_mult"), (ClickableWidget)this.nearAnimationEnergyGainMultField, (ClickableWidget)this.resetNearAnimationEnergyGainMultButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.near_animation_energy_gain_mult")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.player_energy_aura")));
            this.playerEnergyAuraMultLowField = this.newDecimalField(fieldWidth, this.playerEnergyAuraMultLow);
            this.resetPlayerEnergyAuraMultLowButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraMultLowField.setText(String.valueOf(this.defaults.getPlayerEnergyAuraMultLow()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPlayerEnergyAuraMultLowButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.player_energy_aura_mult_low"), (ClickableWidget)this.playerEnergyAuraMultLowField, (ClickableWidget)this.resetPlayerEnergyAuraMultLowButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_mult_low")));
            this.playerEnergyAuraMultMidField = this.newDecimalField(fieldWidth, this.playerEnergyAuraMultMid);
            this.resetPlayerEnergyAuraMultMidButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraMultMidField.setText(String.valueOf(this.defaults.getPlayerEnergyAuraMultMid()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPlayerEnergyAuraMultMidButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.player_energy_aura_mult_mid"), (ClickableWidget)this.playerEnergyAuraMultMidField, (ClickableWidget)this.resetPlayerEnergyAuraMultMidButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_mult_mid")));
            this.playerEnergyAuraMultHighField = this.newDecimalField(fieldWidth, this.playerEnergyAuraMultHigh);
            this.resetPlayerEnergyAuraMultHighButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraMultHighField.setText(String.valueOf(this.defaults.getPlayerEnergyAuraMultHigh()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPlayerEnergyAuraMultHighButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.player_energy_aura_mult_high"), (ClickableWidget)this.playerEnergyAuraMultHighField, (ClickableWidget)this.resetPlayerEnergyAuraMultHighButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_mult_high")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.destroyed_skin")));
            this.destroyedSkinAuraMultStage1Field = this.newDecimalField(fieldWidth, this.destroyedSkinAuraMultStage1);
            this.resetDestroyedSkinAuraMultStage1Button = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.destroyedSkinAuraMultStage1Field.setText(String.valueOf(this.defaults.getDestroyedSkinAuraMultStage1()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetDestroyedSkinAuraMultStage1Button, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.destroyed_skin_aura_mult_stage1"), (ClickableWidget)this.destroyedSkinAuraMultStage1Field, (ClickableWidget)this.resetDestroyedSkinAuraMultStage1Button, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.destroyed_skin_aura_mult_stage1")));
            this.destroyedSkinAuraMultStage2Field = this.newDecimalField(fieldWidth, this.destroyedSkinAuraMultStage2);
            this.resetDestroyedSkinAuraMultStage2Button = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.destroyedSkinAuraMultStage2Field.setText(String.valueOf(this.defaults.getDestroyedSkinAuraMultStage2()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetDestroyedSkinAuraMultStage2Button, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.destroyed_skin_aura_mult_stage2"), (ClickableWidget)this.destroyedSkinAuraMultStage2Field, (ClickableWidget)this.resetDestroyedSkinAuraMultStage2Button, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.destroyed_skin_aura_mult_stage2")));
            this.destroyedSkinAuraMultStage3Field = this.newDecimalField(fieldWidth, this.destroyedSkinAuraMultStage3);
            this.resetDestroyedSkinAuraMultStage3Button = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.destroyedSkinAuraMultStage3Field.setText(String.valueOf(this.defaults.getDestroyedSkinAuraMultStage3()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetDestroyedSkinAuraMultStage3Button, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.destroyed_skin_aura_mult_stage3"), (ClickableWidget)this.destroyedSkinAuraMultStage3Field, (ClickableWidget)this.resetDestroyedSkinAuraMultStage3Button, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.destroyed_skin_aura_mult_stage3")));
            this.destroyedSkinAuraMultStage4Field = this.newDecimalField(fieldWidth, this.destroyedSkinAuraMultStage4);
            this.resetDestroyedSkinAuraMultStage4Button = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.destroyedSkinAuraMultStage4Field.setText(String.valueOf(this.defaults.getDestroyedSkinAuraMultStage4()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetDestroyedSkinAuraMultStage4Button, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.destroyed_skin_aura_mult_stage4"), (ClickableWidget)this.destroyedSkinAuraMultStage4Field, (ClickableWidget)this.resetDestroyedSkinAuraMultStage4Button, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.destroyed_skin_aura_mult_stage4")));
            this.normalDamageDestroyedSkinChanceField = this.newNumberField(fieldWidth, this.normalDamageDestroyedSkinChancePercent);
            this.resetNormalDamageDestroyedSkinChanceButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.normalDamageDestroyedSkinChanceField.setText(String.valueOf(this.defaults.getNormalDamageDestroyedSkinChancePercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetNormalDamageDestroyedSkinChanceButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.normal_damage_destroyed_skin_chance_percent"), (ClickableWidget)this.normalDamageDestroyedSkinChanceField, (ClickableWidget)this.resetNormalDamageDestroyedSkinChanceButton, this.destroyedSkinSettingTooltip("config.needsofnature.tooltip.normal_damage_destroyed_skin_chance_percent")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.aura_range_timing")));
            this.playerEnergyAuraRadiusMinField = this.newNumberField(fieldWidth, this.playerEnergyAuraRadiusMin);
            this.resetPlayerEnergyAuraRadiusMinButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraRadiusMinField.setText(String.valueOf(this.defaults.getPlayerEnergyAuraRadiusMin()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPlayerEnergyAuraRadiusMinButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.player_energy_aura_radius_min"), (ClickableWidget)this.playerEnergyAuraRadiusMinField, (ClickableWidget)this.resetPlayerEnergyAuraRadiusMinButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_radius_min")));
            this.playerEnergyAuraRadiusMaxField = this.newNumberField(fieldWidth, this.playerEnergyAuraRadiusMax);
            this.resetPlayerEnergyAuraRadiusMaxButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraRadiusMaxField.setText(String.valueOf(this.defaults.getPlayerEnergyAuraRadiusMax()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPlayerEnergyAuraRadiusMaxButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.player_energy_aura_radius_max"), (ClickableWidget)this.playerEnergyAuraRadiusMaxField, (ClickableWidget)this.resetPlayerEnergyAuraRadiusMaxButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_radius_max")));
            this.playerEnergyAuraPulseTicksField = this.newNumberField(fieldWidth, this.playerEnergyAuraPulseTicks);
            this.resetPlayerEnergyAuraPulseTicksButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.playerEnergyAuraPulseTicksField.setText(String.valueOf(this.defaults.getPlayerEnergyAuraPulseTicks()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPlayerEnergyAuraPulseTicksButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.player_energy_aura_pulse_ticks"), (ClickableWidget)this.playerEnergyAuraPulseTicksField, (ClickableWidget)this.resetPlayerEnergyAuraPulseTicksButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.player_energy_aura_pulse_ticks")));
            this.energyGainRateField.setChangedListener(ignored -> this.updateResetButtons());
            this.nearAnimationEnergyGainMultField.setChangedListener(ignored -> this.updateResetButtons());
            this.playerEnergyAuraMultLowField.setChangedListener(ignored -> this.updateResetButtons());
            this.playerEnergyAuraMultMidField.setChangedListener(ignored -> this.updateResetButtons());
            this.playerEnergyAuraMultHighField.setChangedListener(ignored -> this.updateResetButtons());
            this.destroyedSkinAuraMultStage1Field.setChangedListener(ignored -> this.updateResetButtons());
            this.destroyedSkinAuraMultStage2Field.setChangedListener(ignored -> this.updateResetButtons());
            this.destroyedSkinAuraMultStage3Field.setChangedListener(ignored -> this.updateResetButtons());
            this.destroyedSkinAuraMultStage4Field.setChangedListener(ignored -> this.updateResetButtons());
            this.normalDamageDestroyedSkinChanceField.setChangedListener(ignored -> this.updateResetButtons());
            this.playerEnergyAuraRadiusMinField.setChangedListener(ignored -> this.updateResetButtons());
            this.playerEnergyAuraRadiusMaxField.setChangedListener(ignored -> this.updateResetButtons());
            this.playerEnergyAuraPulseTicksField.setChangedListener(ignored -> this.updateResetButtons());
            this.energyGainRateField.setEditable(this.serverConfigEditable);
            this.nearAnimationEnergyGainMultField.setEditable(this.serverConfigEditable);
            this.playerEnergyAuraMultLowField.setEditable(this.serverConfigEditable);
            this.playerEnergyAuraMultMidField.setEditable(this.serverConfigEditable);
            this.playerEnergyAuraMultHighField.setEditable(this.serverConfigEditable);
            this.destroyedSkinAuraMultStage1Field.setEditable(this.destroyedSkinSettingsEditable());
            this.destroyedSkinAuraMultStage2Field.setEditable(this.destroyedSkinSettingsEditable());
            this.destroyedSkinAuraMultStage3Field.setEditable(this.destroyedSkinSettingsEditable());
            this.destroyedSkinAuraMultStage4Field.setEditable(this.destroyedSkinSettingsEditable());
            this.normalDamageDestroyedSkinChanceField.setEditable(this.destroyedSkinSettingsEditable());
            this.playerEnergyAuraRadiusMinField.setEditable(this.serverConfigEditable);
            this.playerEnergyAuraRadiusMaxField.setEditable(this.serverConfigEditable);
            this.playerEnergyAuraPulseTicksField.setEditable(this.serverConfigEditable);
            this.updateResetButtons();
            int centerX = this.width / 2;
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.saveAndClose()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.addDrawableChild((Element)doneButton);
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }

        public void close() {
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
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private TextFieldWidget newDecimalField(int w, double initial) {
            TextFieldWidget field = new TextFieldWidget(this.textRenderer, 0, 0, w, 20, (Text)Text.empty());
            int maxLen = 16;
            field.setMaxLength(maxLen);
            field.setText(this.trimToMax(this.formatDecimal(initial), maxLen));
            field.setEditable(true);
            return field;
        }

        private TextFieldWidget newNumberField(int w, int initial) {
            TextFieldWidget field = new TextFieldWidget(this.textRenderer, 0, 0, w, 20, (Text)Text.empty());
            int maxLen = 16;
            field.setMaxLength(maxLen);
            field.setText(this.trimToMax(String.valueOf(initial), maxLen));
            field.setEditable(true);
            return field;
        }

        private double parseDecimalField(TextFieldWidget field, double fallback) {
            try {
                return Double.parseDouble(field.getText().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private int parseNumberField(TextFieldWidget field, int fallback) {
            try {
                return Integer.parseInt(field.getText().trim());
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

        private Tooltip destroyedSkinSettingTooltip(String enabledKey) {
            return NonModMenuScreens.tooltip(this.config.isDestroyedSkinSystemEnabled() ? enabledKey : "config.needsofnature.tooltip.destroyed_skin_system.disabled_setting");
        }

        private boolean isDoubleFieldChanged(TextFieldWidget field, double defaultValue) {
            if (field == null) {
                return false;
            }
            double value = this.parseDecimalField(field, defaultValue);
            return Math.abs(value - defaultValue) >= 1.0E-4;
        }

        private boolean isIntFieldChanged(TextFieldWidget field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseNumberField(field, defaultValue) != defaultValue;
        }

        private void updateResetButtons() {
            if (this.resetEnergyGainRateButton != null) {
                boolean bl = this.resetEnergyGainRateButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.energyGainRateField, this.defaults.getEnergyGainRate());
            }
            if (this.resetNearAnimationEnergyGainMultButton != null) {
                boolean bl = this.resetNearAnimationEnergyGainMultButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.nearAnimationEnergyGainMultField, this.defaults.getNearAnimationEnergyGainMult());
            }
            if (this.resetPlayerEnergyAuraMultLowButton != null) {
                boolean bl = this.resetPlayerEnergyAuraMultLowButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.playerEnergyAuraMultLowField, this.defaults.getPlayerEnergyAuraMultLow());
            }
            if (this.resetPlayerEnergyAuraMultMidButton != null) {
                boolean bl = this.resetPlayerEnergyAuraMultMidButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.playerEnergyAuraMultMidField, this.defaults.getPlayerEnergyAuraMultMid());
            }
            if (this.resetPlayerEnergyAuraMultHighButton != null) {
                boolean bl = this.resetPlayerEnergyAuraMultHighButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.playerEnergyAuraMultHighField, this.defaults.getPlayerEnergyAuraMultHigh());
            }
            if (this.resetDestroyedSkinAuraMultStage1Button != null) {
                boolean bl = this.resetDestroyedSkinAuraMultStage1Button.active = this.destroyedSkinSettingsEditable() && this.isDoubleFieldChanged(this.destroyedSkinAuraMultStage1Field, this.defaults.getDestroyedSkinAuraMultStage1());
            }
            if (this.resetDestroyedSkinAuraMultStage2Button != null) {
                boolean bl = this.resetDestroyedSkinAuraMultStage2Button.active = this.destroyedSkinSettingsEditable() && this.isDoubleFieldChanged(this.destroyedSkinAuraMultStage2Field, this.defaults.getDestroyedSkinAuraMultStage2());
            }
            if (this.resetDestroyedSkinAuraMultStage3Button != null) {
                boolean bl = this.resetDestroyedSkinAuraMultStage3Button.active = this.destroyedSkinSettingsEditable() && this.isDoubleFieldChanged(this.destroyedSkinAuraMultStage3Field, this.defaults.getDestroyedSkinAuraMultStage3());
            }
            if (this.resetDestroyedSkinAuraMultStage4Button != null) {
                boolean bl = this.resetDestroyedSkinAuraMultStage4Button.active = this.destroyedSkinSettingsEditable() && this.isDoubleFieldChanged(this.destroyedSkinAuraMultStage4Field, this.defaults.getDestroyedSkinAuraMultStage4());
            }
            if (this.resetNormalDamageDestroyedSkinChanceButton != null) {
                boolean bl = this.resetNormalDamageDestroyedSkinChanceButton.active = this.destroyedSkinSettingsEditable() && this.isIntFieldChanged(this.normalDamageDestroyedSkinChanceField, this.defaults.getNormalDamageDestroyedSkinChancePercent());
            }
            if (this.resetPlayerEnergyAuraRadiusMinButton != null) {
                boolean bl = this.resetPlayerEnergyAuraRadiusMinButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.playerEnergyAuraRadiusMinField, this.defaults.getPlayerEnergyAuraRadiusMin());
            }
            if (this.resetPlayerEnergyAuraRadiusMaxButton != null) {
                boolean bl = this.resetPlayerEnergyAuraRadiusMaxButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.playerEnergyAuraRadiusMaxField, this.defaults.getPlayerEnergyAuraRadiusMax());
            }
            if (this.resetPlayerEnergyAuraPulseTicksButton != null) {
                this.resetPlayerEnergyAuraPulseTicksButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.playerEnergyAuraPulseTicksField, this.defaults.getPlayerEnergyAuraPulseTicks());
            }
        }
    }

    static class GameplayWorldScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private int initialEnergyMax;
        private int genderMalePercent;
        private int genderFemalePercent;
        private int genderBothPercent;
        private boolean allowPlayerGenderChangeAnytime;
        private boolean requirePlayerGenderSelectionOnJoin;
        private int allowedStartingGenderMask;
        private TextFieldWidget initialEnergyField;
        private TextFieldWidget genderMaleField;
        private TextFieldWidget genderFemaleField;
        private TextFieldWidget genderBothField;
        private ButtonWidget allowPlayerGenderChangeButton;
        private ButtonWidget requirePlayerGenderSelectionButton;
        private ButtonWidget allowStartingMaleButton;
        private ButtonWidget allowStartingFemaleButton;
        private ButtonWidget allowStartingBothButton;
        private ButtonWidget resetInitialEnergyButton;
        private ButtonWidget resetGenderMaleButton;
        private ButtonWidget resetGenderFemaleButton;
        private ButtonWidget resetGenderBothButton;
        private ButtonWidget resetAllowPlayerGenderChangeButton;
        private ButtonWidget resetRequirePlayerGenderSelectionButton;
        private ButtonWidget resetAllowedStartingGendersButton;
        private boolean serverConfigEditable;

        protected GameplayWorldScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.section.world"));
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

        protected void init() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.initialEnergyField != null) {
                this.initialEnergyMax = this.parseField(this.initialEnergyField, this.initialEnergyMax);
                this.genderMalePercent = this.parseField(this.genderMaleField, this.genderMalePercent);
                this.genderFemalePercent = this.parseField(this.genderFemaleField, this.genderFemalePercent);
                this.genderBothPercent = this.parseField(this.genderBothField, this.genderBothPercent);
            }
            int listTop = 32;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild((Element)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.mob_energy")));
            this.initialEnergyField = this.newNumberField(fieldWidth, this.initialEnergyMax);
            this.resetInitialEnergyButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.initialEnergyField.setText(String.valueOf(this.defaults.getInitialEnergyMax()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetInitialEnergyButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.initial_energy_max"), (ClickableWidget)this.initialEnergyField, (ClickableWidget)this.resetInitialEnergyButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.initial_energy_max")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.gender_spawn_chances")));
            this.genderMaleField = this.newNumberField(fieldWidth, this.genderMalePercent);
            this.resetGenderMaleButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.genderMaleField.setText(String.valueOf(this.defaults.getGenderMaleChancePercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetGenderMaleButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.gender_male"), (ClickableWidget)this.genderMaleField, (ClickableWidget)this.resetGenderMaleButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.gender_male")));
            this.genderFemaleField = this.newNumberField(fieldWidth, this.genderFemalePercent);
            this.resetGenderFemaleButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.genderFemaleField.setText(String.valueOf(this.defaults.getGenderFemaleChancePercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetGenderFemaleButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.gender_female"), (ClickableWidget)this.genderFemaleField, (ClickableWidget)this.resetGenderFemaleButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.gender_female")));
            this.genderBothField = this.newNumberField(fieldWidth, this.genderBothPercent);
            this.resetGenderBothButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.genderBothField.setText(String.valueOf(this.defaults.getGenderBothChancePercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetGenderBothButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.gender_both"), (ClickableWidget)this.genderBothField, (ClickableWidget)this.resetGenderBothButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.gender_both")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.player_gender_rules")));
            this.allowPlayerGenderChangeButton = ButtonWidget.builder((Text)this.onOffLabel(this.allowPlayerGenderChangeAnytime), button -> {
                this.allowPlayerGenderChangeAnytime = !this.allowPlayerGenderChangeAnytime;
                button.setMessage(this.onOffLabel(this.allowPlayerGenderChangeAnytime));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetAllowPlayerGenderChangeButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.allowPlayerGenderChangeAnytime = this.defaults.allowPlayerGenderChangeAnytime();
                this.allowPlayerGenderChangeButton.setMessage(this.onOffLabel(this.allowPlayerGenderChangeAnytime));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAllowPlayerGenderChangeButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.allow_player_gender_change_anytime"), (ClickableWidget)this.allowPlayerGenderChangeButton, (ClickableWidget)this.resetAllowPlayerGenderChangeButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.allow_player_gender_change_anytime")));
            this.requirePlayerGenderSelectionButton = ButtonWidget.builder((Text)this.onOffLabel(this.requirePlayerGenderSelectionOnJoin), button -> {
                this.requirePlayerGenderSelectionOnJoin = !this.requirePlayerGenderSelectionOnJoin;
                button.setMessage(this.onOffLabel(this.requirePlayerGenderSelectionOnJoin));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetRequirePlayerGenderSelectionButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.requirePlayerGenderSelectionOnJoin = this.defaults.requirePlayerGenderSelectionOnJoin();
                this.requirePlayerGenderSelectionButton.setMessage(this.onOffLabel(this.requirePlayerGenderSelectionOnJoin));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetRequirePlayerGenderSelectionButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.require_player_gender_selection_on_join"), (ClickableWidget)this.requirePlayerGenderSelectionButton, (ClickableWidget)this.resetRequirePlayerGenderSelectionButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.require_player_gender_selection_on_join")));
            this.allowStartingMaleButton = ButtonWidget.builder((Text)this.startingGenderToggleLabel("config.needsofnature.player_gender.male", this.isStartingGenderChoiceAllowed(1)), button -> {
                this.allowedStartingGenderMask = this.toggleAllowedStartingGenderChoice(this.allowedStartingGenderMask, 1);
                this.updateAllowedStartingGenderButtons();
                this.updateResetButtons();
            }).dimensions(0, 0, 64, 20).build();
            this.allowStartingFemaleButton = ButtonWidget.builder((Text)this.startingGenderToggleLabel("config.needsofnature.player_gender.female", this.isStartingGenderChoiceAllowed(2)), button -> {
                this.allowedStartingGenderMask = this.toggleAllowedStartingGenderChoice(this.allowedStartingGenderMask, 2);
                this.updateAllowedStartingGenderButtons();
                this.updateResetButtons();
            }).dimensions(0, 0, 70, 20).build();
            this.allowStartingBothButton = ButtonWidget.builder((Text)this.startingGenderToggleLabel("config.needsofnature.player_gender.both", this.isStartingGenderChoiceAllowed(4)), button -> {
                this.allowedStartingGenderMask = this.toggleAllowedStartingGenderChoice(this.allowedStartingGenderMask, 4);
                this.updateAllowedStartingGenderButtons();
                this.updateResetButtons();
            }).dimensions(0, 0, 98, 20).build();
            this.resetAllowedStartingGendersButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.allowedStartingGenderMask = this.defaults.getAllowedStartingGenderMask();
                this.updateAllowedStartingGenderButtons();
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.allowStartingMaleButton, "config.needsofnature.tooltip.allowed_starting_genders");
            NonModMenuScreens.setTooltip((ClickableWidget)this.allowStartingFemaleButton, "config.needsofnature.tooltip.allowed_starting_genders");
            NonModMenuScreens.setTooltip((ClickableWidget)this.allowStartingBothButton, "config.needsofnature.tooltip.allowed_starting_genders");
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAllowedStartingGendersButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.allowed_starting_genders"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.allowed_starting_genders"), new ClickableWidget[]{this.allowStartingMaleButton, this.allowStartingFemaleButton, this.allowStartingBothButton, this.resetAllowedStartingGendersButton}));
            this.initialEnergyField.setChangedListener(ignored -> this.updateResetButtons());
            this.genderMaleField.setChangedListener(ignored -> this.updateResetButtons());
            this.genderFemaleField.setChangedListener(ignored -> this.updateResetButtons());
            this.genderBothField.setChangedListener(ignored -> this.updateResetButtons());
            this.initialEnergyField.setEditable(this.serverConfigEditable);
            this.genderMaleField.setEditable(this.serverConfigEditable);
            this.genderFemaleField.setEditable(this.serverConfigEditable);
            this.genderBothField.setEditable(this.serverConfigEditable);
            this.allowPlayerGenderChangeButton.active = this.serverConfigEditable;
            this.requirePlayerGenderSelectionButton.active = this.serverConfigEditable;
            this.allowStartingMaleButton.active = this.serverConfigEditable;
            this.allowStartingFemaleButton.active = this.serverConfigEditable;
            this.allowStartingBothButton.active = this.serverConfigEditable;
            this.updateResetButtons();
            int centerX = this.width / 2;
            ButtonWidget genderSpawnButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.gender_spawn_button"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuDebugScreens.GenderSpawnConfigScreen(this, this.config))).dimensions(centerX - 100, this.height - 52, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)genderSpawnButton, "config.needsofnature.tooltip.gender_spawn_button");
            this.addDrawableChild((Element)genderSpawnButton);
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.saveAndClose()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)doneButton, "config.needsofnature.tooltip.done_save");
            this.addDrawableChild((Element)doneButton);
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }

        public void close() {
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
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private TextFieldWidget newNumberField(int w, int initial) {
            TextFieldWidget field = new TextFieldWidget(this.textRenderer, 0, 0, w, 20, (Text)Text.empty());
            field.setText(String.valueOf(initial));
            field.setMaxLength(8);
            field.setEditable(true);
            return field;
        }

        private int parseField(TextFieldWidget field, int fallback) {
            try {
                return Integer.parseInt(field.getText().trim());
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

        private boolean isIntFieldChanged(TextFieldWidget field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseField(field, defaultValue) != defaultValue;
        }

        private Text onOffLabel(boolean enabled) {
            return Text.translatable((String)(enabled ? "options.on" : "options.off"));
        }

        private Text startingGenderToggleLabel(String labelKey, boolean enabled) {
            return Text.translatable((String)(enabled ? "config.needsofnature.toggle_label.enabled" : "config.needsofnature.toggle_label.disabled"), (Object[])new Object[]{Text.translatable((String)labelKey)});
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
                this.allowStartingMaleButton.setMessage(this.startingGenderToggleLabel("config.needsofnature.player_gender.male", this.isStartingGenderChoiceAllowed(1)));
            }
            if (this.allowStartingFemaleButton != null) {
                this.allowStartingFemaleButton.setMessage(this.startingGenderToggleLabel("config.needsofnature.player_gender.female", this.isStartingGenderChoiceAllowed(2)));
            }
            if (this.allowStartingBothButton != null) {
                this.allowStartingBothButton.setMessage(this.startingGenderToggleLabel("config.needsofnature.player_gender.both", this.isStartingGenderChoiceAllowed(4)));
            }
        }

        private void updateResetButtons() {
            if (this.resetInitialEnergyButton != null) {
                boolean bl = this.resetInitialEnergyButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.initialEnergyField, this.defaults.getInitialEnergyMax());
            }
            if (this.resetGenderMaleButton != null) {
                boolean bl = this.resetGenderMaleButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.genderMaleField, this.defaults.getGenderMaleChancePercent());
            }
            if (this.resetGenderFemaleButton != null) {
                boolean bl = this.resetGenderFemaleButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.genderFemaleField, this.defaults.getGenderFemaleChancePercent());
            }
            if (this.resetGenderBothButton != null) {
                boolean bl = this.resetGenderBothButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.genderBothField, this.defaults.getGenderBothChancePercent());
            }
            if (this.resetAllowPlayerGenderChangeButton != null) {
                boolean bl = this.resetAllowPlayerGenderChangeButton.active = this.serverConfigEditable && this.allowPlayerGenderChangeAnytime != this.defaults.allowPlayerGenderChangeAnytime();
            }
            if (this.resetRequirePlayerGenderSelectionButton != null) {
                boolean bl = this.resetRequirePlayerGenderSelectionButton.active = this.serverConfigEditable && this.requirePlayerGenderSelectionOnJoin != this.defaults.requirePlayerGenderSelectionOnJoin();
            }
            if (this.resetAllowedStartingGendersButton != null) {
                boolean bl = this.resetAllowedStartingGendersButton.active = this.serverConfigEditable && this.allowedStartingGenderMask != this.defaults.getAllowedStartingGenderMask();
            }
            if (this.allowPlayerGenderChangeButton != null) {
                this.allowPlayerGenderChangeButton.active = this.serverConfigEditable;
            }
            if (this.requirePlayerGenderSelectionButton != null) {
                this.requirePlayerGenderSelectionButton.active = this.serverConfigEditable;
            }
            if (this.allowStartingMaleButton != null) {
                this.allowStartingMaleButton.active = this.serverConfigEditable;
            }
            if (this.allowStartingFemaleButton != null) {
                this.allowStartingFemaleButton.active = this.serverConfigEditable;
            }
            if (this.allowStartingBothButton != null) {
                this.allowStartingBothButton.active = this.serverConfigEditable;
            }
        }
    }

    static class GameplayCategoryScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;

        protected GameplayCategoryScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.gameplay_title"));
            this.parent = parent;
            this.config = config;
        }

        protected void init() {
            int centerX = this.width / 2;
            int y = this.height / 2 - 48;
            int gap = 24;
            ButtonWidget worldButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.section.world"), button -> MinecraftClient.getInstance().setScreen((Screen)new GameplayWorldScreen(this, this.config))).dimensions(centerX - 100, y, 200, 20).build();
            this.addDrawableChild((Element)worldButton);
            ButtonWidget energyButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.section.energy"), button -> MinecraftClient.getInstance().setScreen((Screen)new GameplayEnergyScreen(this, this.config))).dimensions(centerX - 100, y += gap, 200, 20).build();
            this.addDrawableChild((Element)energyButton);
            ButtonWidget animationsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.section.animations"), button -> MinecraftClient.getInstance().setScreen((Screen)new GameplayAnimationsScreen(this, this.config))).dimensions(centerX - 100, y += gap, 200, 20).build();
            this.addDrawableChild((Element)animationsButton);
            ButtonWidget difficultyButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.section.difficulty"), button -> MinecraftClient.getInstance().setScreen((Screen)new GameplayDifficultyScreen(this, this.config))).dimensions(centerX - 100, y += gap, 200, 20).build();
            this.addDrawableChild((Element)difficultyButton);
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            this.addDrawableChild((Element)doneButton);
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }

        public void close() {
            MinecraftClient.getInstance().setScreen(this.parent);
        }
    }
}

