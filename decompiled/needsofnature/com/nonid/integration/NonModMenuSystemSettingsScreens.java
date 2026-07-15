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
 */
package com.nonid.integration;

import com.nonid.NonConfig;
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

final class NonModMenuSystemSettingsScreens {
    private NonModMenuSystemSettingsScreens() {
    }

    static class LiquidSettingsScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean serverConfigEditable;
        private int liquidTankCapacityMl;
        private double liquidDecay;
        private double peakXpPerMl;
        private double filledStageOneSpeed;
        private double filledStageTwoSpeed;
        private double filledStageThreeSpeed;
        private double filledStageOneJump;
        private double filledStageTwoJump;
        private double filledStageThreeJump;
        private class_342 liquidTankCapacityField;
        private class_342 liquidDecayField;
        private class_342 peakXpPerMlField;
        private class_342 filledStageOneSpeedField;
        private class_342 filledStageTwoSpeedField;
        private class_342 filledStageThreeSpeedField;
        private class_342 filledStageOneJumpField;
        private class_342 filledStageTwoJumpField;
        private class_342 filledStageThreeJumpField;
        private class_4185 resetLiquidTankCapacityButton;
        private class_4185 resetLiquidDecayButton;
        private class_4185 resetPeakXpPerMlButton;
        private class_4185 resetFilledStageOneSpeedButton;
        private class_4185 resetFilledStageTwoSpeedButton;
        private class_4185 resetFilledStageThreeSpeedButton;
        private class_4185 resetFilledStageOneJumpButton;
        private class_4185 resetFilledStageTwoJumpButton;
        private class_4185 resetFilledStageThreeJumpButton;

        protected LiquidSettingsScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_settings_title"));
            this.parent = parent;
            this.config = config;
            this.liquidTankCapacityMl = config.getLiquidTankCapacityMl();
            this.liquidDecay = config.getLiquidDecayPerSecond();
            this.peakXpPerMl = config.getPeakXpPerMl();
            this.filledStageOneSpeed = config.getFilledStageOneSpeedMult();
            this.filledStageTwoSpeed = config.getFilledStageTwoSpeedMult();
            this.filledStageThreeSpeed = config.getFilledStageThreeSpeedMult();
            this.filledStageOneJump = config.getFilledStageOneJumpMult();
            this.filledStageTwoJump = config.getFilledStageTwoJumpMult();
            this.filledStageThreeJump = config.getFilledStageThreeJumpMult();
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.liquidTankCapacityField != null) {
                this.liquidTankCapacityMl = this.parseField(this.liquidTankCapacityField, this.liquidTankCapacityMl);
                this.liquidDecay = this.parseDecimalField(this.liquidDecayField, this.liquidDecay);
                this.peakXpPerMl = this.parseDecimalField(this.peakXpPerMlField, this.peakXpPerMl);
                this.filledStageOneSpeed = this.parseDecimalField(this.filledStageOneSpeedField, this.filledStageOneSpeed);
                this.filledStageTwoSpeed = this.parseDecimalField(this.filledStageTwoSpeedField, this.filledStageTwoSpeed);
                this.filledStageThreeSpeed = this.parseDecimalField(this.filledStageThreeSpeedField, this.filledStageThreeSpeed);
                this.filledStageOneJump = this.parseDecimalField(this.filledStageOneJumpField, this.filledStageOneJump);
                this.filledStageTwoJump = this.parseDecimalField(this.filledStageTwoJumpField, this.filledStageTwoJump);
                this.filledStageThreeJump = this.parseDecimalField(this.filledStageThreeJumpField, this.filledStageThreeJump);
            }
            int listTop = 32;
            int bottomArea = 88;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.liquid_tank")));
            this.liquidTankCapacityField = this.newNumberField(fieldWidth, this.liquidTankCapacityMl);
            this.resetLiquidTankCapacityButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.liquidTankCapacityField.method_1852(String.valueOf(this.defaults.getLiquidTankCapacityMl()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetLiquidTankCapacityButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_tank_capacity"), (class_339)this.liquidTankCapacityField, (class_339)this.resetLiquidTankCapacityButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.liquid_tank_capacity")));
            this.liquidDecayField = this.newDecimalField(fieldWidth, this.liquidDecay);
            this.resetLiquidDecayButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.liquidDecayField.method_1852(String.valueOf(this.defaults.getLiquidDecayPerSecond()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetLiquidDecayButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_decay"), (class_339)this.liquidDecayField, (class_339)this.resetLiquidDecayButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.liquid_decay")));
            this.peakXpPerMlField = this.newDecimalField(fieldWidth, this.peakXpPerMl);
            this.resetPeakXpPerMlButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.peakXpPerMlField.method_1852(String.valueOf(this.defaults.getPeakXpPerMl()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPeakXpPerMlButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.peak_xp_per_ml"), (class_339)this.peakXpPerMlField, (class_339)this.resetPeakXpPerMlButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.peak_xp_per_ml")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.filled_penalties")));
            this.filledStageOneSpeedField = this.newDecimalField(fieldWidth, this.filledStageOneSpeed);
            this.resetFilledStageOneSpeedButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.filledStageOneSpeedField.method_1852(String.valueOf(this.defaults.getFilledStageOneSpeedMult()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetFilledStageOneSpeedButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.filled_stage_one_speed"), (class_339)this.filledStageOneSpeedField, (class_339)this.resetFilledStageOneSpeedButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_one_speed")));
            this.filledStageTwoSpeedField = this.newDecimalField(fieldWidth, this.filledStageTwoSpeed);
            this.resetFilledStageTwoSpeedButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.filledStageTwoSpeedField.method_1852(String.valueOf(this.defaults.getFilledStageTwoSpeedMult()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetFilledStageTwoSpeedButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.filled_stage_two_speed"), (class_339)this.filledStageTwoSpeedField, (class_339)this.resetFilledStageTwoSpeedButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_two_speed")));
            this.filledStageThreeSpeedField = this.newDecimalField(fieldWidth, this.filledStageThreeSpeed);
            this.resetFilledStageThreeSpeedButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.filledStageThreeSpeedField.method_1852(String.valueOf(this.defaults.getFilledStageThreeSpeedMult()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetFilledStageThreeSpeedButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.filled_stage_three_speed"), (class_339)this.filledStageThreeSpeedField, (class_339)this.resetFilledStageThreeSpeedButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_three_speed")));
            this.filledStageOneJumpField = this.newDecimalField(fieldWidth, this.filledStageOneJump);
            this.resetFilledStageOneJumpButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.filledStageOneJumpField.method_1852(String.valueOf(this.defaults.getFilledStageOneJumpMult()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetFilledStageOneJumpButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.filled_stage_one_jump"), (class_339)this.filledStageOneJumpField, (class_339)this.resetFilledStageOneJumpButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_one_jump")));
            this.filledStageTwoJumpField = this.newDecimalField(fieldWidth, this.filledStageTwoJump);
            this.resetFilledStageTwoJumpButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.filledStageTwoJumpField.method_1852(String.valueOf(this.defaults.getFilledStageTwoJumpMult()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetFilledStageTwoJumpButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.filled_stage_two_jump"), (class_339)this.filledStageTwoJumpField, (class_339)this.resetFilledStageTwoJumpButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_two_jump")));
            this.filledStageThreeJumpField = this.newDecimalField(fieldWidth, this.filledStageThreeJump);
            this.resetFilledStageThreeJumpButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.filledStageThreeJumpField.method_1852(String.valueOf(this.defaults.getFilledStageThreeJumpMult()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetFilledStageThreeJumpButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.filled_stage_three_jump"), (class_339)this.filledStageThreeJumpField, (class_339)this.resetFilledStageThreeJumpButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_three_jump")));
            this.liquidTankCapacityField.method_1888(this.serverConfigEditable);
            this.liquidDecayField.method_1888(this.serverConfigEditable);
            this.peakXpPerMlField.method_1888(this.serverConfigEditable);
            this.filledStageOneSpeedField.method_1888(this.serverConfigEditable);
            this.filledStageTwoSpeedField.method_1888(this.serverConfigEditable);
            this.filledStageThreeSpeedField.method_1888(this.serverConfigEditable);
            this.filledStageOneJumpField.method_1888(this.serverConfigEditable);
            this.filledStageTwoJumpField.method_1888(this.serverConfigEditable);
            this.filledStageThreeJumpField.method_1888(this.serverConfigEditable);
            this.liquidTankCapacityField.method_1863(ignored -> this.updateResetButtons());
            this.liquidDecayField.method_1863(ignored -> this.updateResetButtons());
            this.peakXpPerMlField.method_1863(ignored -> this.updateResetButtons());
            this.filledStageOneSpeedField.method_1863(ignored -> this.updateResetButtons());
            this.filledStageTwoSpeedField.method_1863(ignored -> this.updateResetButtons());
            this.filledStageThreeSpeedField.method_1863(ignored -> this.updateResetButtons());
            this.filledStageOneJumpField.method_1863(ignored -> this.updateResetButtons());
            this.filledStageTwoJumpField.method_1863(ignored -> this.updateResetButtons());
            this.filledStageThreeJumpField.method_1863(ignored -> this.updateResetButtons());
            this.updateResetButtons();
            int centerX = this.field_22789 / 2;
            class_4185 liquidGainButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_button"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuDebugScreens.LiquidGainConfigScreen(this, this.config))).method_46434(centerX - 100, this.field_22790 - 52, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)liquidGainButton, "config.needsofnature.tooltip.liquid_gain_button");
            this.method_37063((class_364)liquidGainButton);
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
            this.liquidTankCapacityMl = this.clampLiquidTankCapacityMl(this.parseField(this.liquidTankCapacityField, this.liquidTankCapacityMl));
            this.liquidDecay = this.clampLiquidDecay(this.parseDecimalField(this.liquidDecayField, this.liquidDecay));
            this.peakXpPerMl = this.clampPeakXpPerMl(this.parseDecimalField(this.peakXpPerMlField, this.peakXpPerMl));
            this.filledStageOneSpeed = this.clampMultiplier(this.parseDecimalField(this.filledStageOneSpeedField, this.filledStageOneSpeed));
            this.filledStageTwoSpeed = this.clampMultiplier(this.parseDecimalField(this.filledStageTwoSpeedField, this.filledStageTwoSpeed));
            this.filledStageThreeSpeed = this.clampMultiplier(this.parseDecimalField(this.filledStageThreeSpeedField, this.filledStageThreeSpeed));
            this.filledStageOneJump = this.clampMultiplier(this.parseDecimalField(this.filledStageOneJumpField, this.filledStageOneJump));
            this.filledStageTwoJump = this.clampMultiplier(this.parseDecimalField(this.filledStageTwoJumpField, this.filledStageTwoJump));
            this.filledStageThreeJump = this.clampMultiplier(this.parseDecimalField(this.filledStageThreeJumpField, this.filledStageThreeJump));
            if (this.serverConfigEditable) {
                this.config.setLiquidTankCapacityMl(this.liquidTankCapacityMl);
                this.config.setLiquidDecayPerSecond(this.liquidDecay);
                this.config.setPeakXpPerMl(this.peakXpPerMl);
                this.config.setFilledStageOneSpeedMult((float)this.filledStageOneSpeed);
                this.config.setFilledStageTwoSpeedMult((float)this.filledStageTwoSpeed);
                this.config.setFilledStageThreeSpeedMult((float)this.filledStageThreeSpeed);
                this.config.setFilledStageOneJumpMult((float)this.filledStageOneJump);
                this.config.setFilledStageTwoJumpMult((float)this.filledStageTwoJump);
                this.config.setFilledStageThreeJumpMult((float)this.filledStageThreeJump);
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

        private double clampLiquidDecay(double v) {
            return Math.max(0.0, Math.min(20.0, v));
        }

        private double clampPeakXpPerMl(double v) {
            return Math.max(0.0, Math.min(10.0, v));
        }

        private int clampLiquidTankCapacityMl(int v) {
            return Math.max(1, Math.min(10000, v));
        }

        private double clampMultiplier(double v) {
            return Math.max(0.1, Math.min(1.0, v));
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
            if (this.resetLiquidTankCapacityButton != null) {
                boolean bl = this.resetLiquidTankCapacityButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.liquidTankCapacityField, this.defaults.getLiquidTankCapacityMl());
            }
            if (this.resetLiquidDecayButton != null) {
                boolean bl = this.resetLiquidDecayButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.liquidDecayField, this.defaults.getLiquidDecayPerSecond());
            }
            if (this.resetPeakXpPerMlButton != null) {
                boolean bl = this.resetPeakXpPerMlButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.peakXpPerMlField, this.defaults.getPeakXpPerMl());
            }
            if (this.resetFilledStageOneSpeedButton != null) {
                boolean bl = this.resetFilledStageOneSpeedButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageOneSpeedField, this.defaults.getFilledStageOneSpeedMult());
            }
            if (this.resetFilledStageTwoSpeedButton != null) {
                boolean bl = this.resetFilledStageTwoSpeedButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageTwoSpeedField, this.defaults.getFilledStageTwoSpeedMult());
            }
            if (this.resetFilledStageThreeSpeedButton != null) {
                boolean bl = this.resetFilledStageThreeSpeedButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageThreeSpeedField, this.defaults.getFilledStageThreeSpeedMult());
            }
            if (this.resetFilledStageOneJumpButton != null) {
                boolean bl = this.resetFilledStageOneJumpButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageOneJumpField, this.defaults.getFilledStageOneJumpMult());
            }
            if (this.resetFilledStageTwoJumpButton != null) {
                boolean bl = this.resetFilledStageTwoJumpButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageTwoJumpField, this.defaults.getFilledStageTwoJumpMult());
            }
            if (this.resetFilledStageThreeJumpButton != null) {
                this.resetFilledStageThreeJumpButton.field_22763 = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageThreeJumpField, this.defaults.getFilledStageThreeJumpMult());
            }
        }
    }

    static class PregnancySettingsScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean serverConfigEditable;
        private int pregnancyChancePercent;
        private int pregnancyDurationMinutes;
        private int pregnancyEggHatchMinutes;
        private boolean pregnancyAutoTameMobs;
        private boolean pregnancyFriendlyMobsIgnorePlayers;
        private class_342 pregnancyChanceField;
        private class_342 pregnancyDurationField;
        private class_342 pregnancyEggHatchMinutesField;
        private class_4185 pregnancyAutoTameMobsButton;
        private class_4185 pregnancyFriendlyMobsIgnorePlayersButton;
        private class_4185 resetPregnancyChanceButton;
        private class_4185 resetPregnancyDurationButton;
        private class_4185 resetPregnancyEggHatchMinutesButton;
        private class_4185 resetPregnancyAutoTameMobsButton;
        private class_4185 resetPregnancyFriendlyMobsIgnorePlayersButton;

        protected PregnancySettingsScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.pregnancy_title"));
            this.parent = parent;
            this.config = config;
            this.pregnancyChancePercent = config.getPregnancyChancePercent();
            this.pregnancyDurationMinutes = config.getPregnancyDurationMinutes();
            this.pregnancyEggHatchMinutes = this.eggHatchSecondsToMinutes(config.getPregnancyEggDefaultHatchSeconds());
            this.pregnancyAutoTameMobs = config.isPregnancyAutoTameMobs();
            this.pregnancyFriendlyMobsIgnorePlayers = config.pregnancyFriendlyMobsIgnorePlayers();
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.pregnancyChanceField != null) {
                this.pregnancyChancePercent = this.parseField(this.pregnancyChanceField, this.pregnancyChancePercent);
                this.pregnancyDurationMinutes = this.parseField(this.pregnancyDurationField, this.pregnancyDurationMinutes);
                this.pregnancyEggHatchMinutes = this.parseField(this.pregnancyEggHatchMinutesField, this.pregnancyEggHatchMinutes);
            }
            int listTop = 32;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.pregnancy_timing")));
            this.pregnancyChanceField = this.newNumberField(fieldWidth, this.pregnancyChancePercent);
            this.resetPregnancyChanceButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.pregnancyChanceField.method_1852(String.valueOf(this.defaults.getPregnancyChancePercent()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPregnancyChanceButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.pregnancy_chance_percent"), (class_339)this.pregnancyChanceField, (class_339)this.resetPregnancyChanceButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_chance_percent")));
            this.pregnancyDurationField = this.newNumberField(fieldWidth, this.pregnancyDurationMinutes);
            this.resetPregnancyDurationButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.pregnancyDurationField.method_1852(String.valueOf(this.defaults.getPregnancyDurationMinutes()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPregnancyDurationButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.pregnancy_duration_minutes"), (class_339)this.pregnancyDurationField, (class_339)this.resetPregnancyDurationButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_duration_minutes")));
            this.pregnancyEggHatchMinutesField = this.newNumberField(fieldWidth, this.pregnancyEggHatchMinutes);
            this.resetPregnancyEggHatchMinutesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.pregnancyEggHatchMinutesField.method_1852(String.valueOf(this.eggHatchSecondsToMinutes(this.defaults.getPregnancyEggDefaultHatchSeconds())))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPregnancyEggHatchMinutesButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.pregnancy_egg_hatch_minutes"), (class_339)this.pregnancyEggHatchMinutesField, (class_339)this.resetPregnancyEggHatchMinutesButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_egg_hatch_minutes")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.pregnancy_behavior")));
            this.pregnancyAutoTameMobsButton = class_4185.method_46430((class_2561)this.toggleText(this.pregnancyAutoTameMobs), button -> {
                this.pregnancyAutoTameMobs = !this.pregnancyAutoTameMobs;
                button.method_25355(this.toggleText(this.pregnancyAutoTameMobs));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetPregnancyAutoTameMobsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.pregnancyAutoTameMobs = this.defaults.isPregnancyAutoTameMobs();
                this.pregnancyAutoTameMobsButton.method_25355(this.toggleText(this.pregnancyAutoTameMobs));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPregnancyAutoTameMobsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.pregnancy_auto_tame_mobs"), (class_339)this.pregnancyAutoTameMobsButton, (class_339)this.resetPregnancyAutoTameMobsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_auto_tame_mobs")));
            this.pregnancyFriendlyMobsIgnorePlayersButton = class_4185.method_46430((class_2561)this.toggleText(this.pregnancyFriendlyMobsIgnorePlayers), button -> {
                this.pregnancyFriendlyMobsIgnorePlayers = !this.pregnancyFriendlyMobsIgnorePlayers;
                button.method_25355(this.toggleText(this.pregnancyFriendlyMobsIgnorePlayers));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetPregnancyFriendlyMobsIgnorePlayersButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.pregnancyFriendlyMobsIgnorePlayers = this.defaults.pregnancyFriendlyMobsIgnorePlayers();
                this.pregnancyFriendlyMobsIgnorePlayersButton.method_25355(this.toggleText(this.pregnancyFriendlyMobsIgnorePlayers));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPregnancyFriendlyMobsIgnorePlayersButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.pregnancy_friendly_mobs_ignore_players"), (class_339)this.pregnancyFriendlyMobsIgnorePlayersButton, (class_339)this.resetPregnancyFriendlyMobsIgnorePlayersButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_friendly_mobs_ignore_players")));
            this.pregnancyChanceField.method_1888(this.serverConfigEditable);
            this.pregnancyDurationField.method_1888(this.serverConfigEditable);
            this.pregnancyEggHatchMinutesField.method_1888(this.serverConfigEditable);
            this.pregnancyAutoTameMobsButton.field_22763 = this.serverConfigEditable;
            this.pregnancyFriendlyMobsIgnorePlayersButton.field_22763 = this.serverConfigEditable;
            this.pregnancyChanceField.method_1863(ignored -> this.updateResetButtons());
            this.pregnancyDurationField.method_1863(ignored -> this.updateResetButtons());
            this.pregnancyEggHatchMinutesField.method_1863(ignored -> this.updateResetButtons());
            this.updateResetButtons();
            int centerX = this.field_22789 / 2;
            class_4185 offspringCountButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.offspring_count_button"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuDebugScreens.OffspringCountConfigScreen(this, this.config))).method_46434(centerX - 100, this.field_22790 - 52, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)offspringCountButton, "config.needsofnature.tooltip.offspring_count_button");
            this.method_37063((class_364)offspringCountButton);
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
                this.pregnancyChancePercent = this.clampPercent(this.parseField(this.pregnancyChanceField, this.pregnancyChancePercent));
                this.pregnancyDurationMinutes = this.clampPregnancyDurationMinutes(this.parseField(this.pregnancyDurationField, this.pregnancyDurationMinutes));
                this.pregnancyEggHatchMinutes = this.clampPregnancyEggHatchMinutes(this.parseField(this.pregnancyEggHatchMinutesField, this.pregnancyEggHatchMinutes));
                this.config.setPregnancyChancePercent(this.pregnancyChancePercent);
                this.config.setPregnancyDurationMinutes(this.pregnancyDurationMinutes);
                this.config.setPregnancyEggDefaultHatchSeconds(this.eggHatchMinutesToSeconds(this.pregnancyEggHatchMinutes));
                this.config.setPregnancyAutoTameMobs(this.pregnancyAutoTameMobs);
                this.config.setPregnancyFriendlyMobsIgnorePlayers(this.pregnancyFriendlyMobsIgnorePlayers);
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

        private int clampPercent(int v) {
            return Math.max(0, Math.min(100, v));
        }

        private int clampPregnancyDurationMinutes(int v) {
            return Math.max(1, Math.min(5256000, v));
        }

        private int clampPregnancyEggHatchMinutes(int v) {
            return Math.max(1, Math.min(1200, v));
        }

        private int eggHatchSecondsToMinutes(int seconds) {
            return Math.max(1, (int)Math.ceil((double)seconds / 60.0));
        }

        private int eggHatchMinutesToSeconds(int minutes) {
            int clamped = this.clampPregnancyEggHatchMinutes(minutes);
            return Math.max(1, Math.min(72000, clamped * 60));
        }

        private boolean isIntFieldChanged(class_342 field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseField(field, defaultValue) != defaultValue;
        }

        private class_2561 toggleText(boolean enabled) {
            return class_2561.method_43471((String)(enabled ? "options.on" : "options.off"));
        }

        private void updateResetButtons() {
            if (this.resetPregnancyChanceButton != null) {
                boolean bl = this.resetPregnancyChanceButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.pregnancyChanceField, this.defaults.getPregnancyChancePercent());
            }
            if (this.resetPregnancyDurationButton != null) {
                boolean bl = this.resetPregnancyDurationButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.pregnancyDurationField, this.defaults.getPregnancyDurationMinutes());
            }
            if (this.resetPregnancyEggHatchMinutesButton != null) {
                boolean bl = this.resetPregnancyEggHatchMinutesButton.field_22763 = this.serverConfigEditable && this.isIntFieldChanged(this.pregnancyEggHatchMinutesField, this.eggHatchSecondsToMinutes(this.defaults.getPregnancyEggDefaultHatchSeconds()));
            }
            if (this.resetPregnancyAutoTameMobsButton != null) {
                boolean bl = this.resetPregnancyAutoTameMobsButton.field_22763 = this.serverConfigEditable && this.pregnancyAutoTameMobs != this.defaults.isPregnancyAutoTameMobs();
            }
            if (this.resetPregnancyFriendlyMobsIgnorePlayersButton != null) {
                this.resetPregnancyFriendlyMobsIgnorePlayersButton.field_22763 = this.serverConfigEditable && this.pregnancyFriendlyMobsIgnorePlayers != this.defaults.pregnancyFriendlyMobsIgnorePlayers();
            }
        }
    }

    static class VanillaOverrideScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean useAnimationBreeding;
        private boolean requireMaleFemaleForBreeding;
        private boolean femaleCowOnlyMilking;
        private class_4185 useAnimationBreedingButton;
        private class_4185 resetUseAnimationBreedingButton;
        private class_4185 requireMaleFemaleForBreedingButton;
        private class_4185 resetRequireMaleFemaleForBreedingButton;
        private class_4185 femaleCowOnlyMilkingButton;
        private class_4185 resetFemaleCowOnlyMilkingButton;
        private boolean serverConfigEditable;

        protected VanillaOverrideScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.vanilla_override_title"));
            this.parent = parent;
            this.config = config;
            this.useAnimationBreeding = config.useAnimationBreeding();
            this.requireMaleFemaleForBreeding = config.requireMaleFemaleForBreeding();
            this.femaleCowOnlyMilking = config.femaleCowOnlyMilking();
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            int centerX = this.field_22789 / 2;
            int listTop = 32;
            int bottomArea = 88;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.breeding")));
            this.useAnimationBreedingButton = class_4185.method_46430((class_2561)this.toggleWithValue(this.useAnimationBreeding), button -> {
                this.useAnimationBreeding = !this.useAnimationBreeding;
                button.method_25355(this.toggleWithValue(this.useAnimationBreeding));
                this.updateDependentTooltips();
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.useAnimationBreedingButton, "config.needsofnature.tooltip.use_animation_breeding");
            this.resetUseAnimationBreedingButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.useAnimationBreeding = this.defaults.useAnimationBreeding();
                this.useAnimationBreedingButton.method_25355(this.toggleWithValue(this.useAnimationBreeding));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetUseAnimationBreedingButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.use_animation_breeding"), (class_339)this.useAnimationBreedingButton, (class_339)this.resetUseAnimationBreedingButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.use_animation_breeding")));
            this.requireMaleFemaleForBreedingButton = class_4185.method_46430((class_2561)this.toggleWithValue(this.requireMaleFemaleForBreeding), button -> {
                this.requireMaleFemaleForBreeding = !this.requireMaleFemaleForBreeding;
                button.method_25355(this.toggleWithValue(this.requireMaleFemaleForBreeding));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetRequireMaleFemaleForBreedingButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.requireMaleFemaleForBreeding = this.defaults.requireMaleFemaleForBreeding();
                this.requireMaleFemaleForBreedingButton.method_25355(this.toggleWithValue(this.requireMaleFemaleForBreeding));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetRequireMaleFemaleForBreedingButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.require_male_female_breeding"), (class_339)this.requireMaleFemaleForBreedingButton, (class_339)this.resetRequireMaleFemaleForBreedingButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.require_male_female_breeding")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.milking")));
            this.femaleCowOnlyMilkingButton = class_4185.method_46430((class_2561)this.toggleWithValue(this.femaleCowOnlyMilking), button -> {
                this.femaleCowOnlyMilking = !this.femaleCowOnlyMilking;
                button.method_25355(this.toggleWithValue(this.femaleCowOnlyMilking));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.femaleCowOnlyMilkingButton, "config.needsofnature.tooltip.female_cow_only_milking");
            this.resetFemaleCowOnlyMilkingButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.femaleCowOnlyMilking = this.defaults.femaleCowOnlyMilking();
                this.femaleCowOnlyMilkingButton.method_25355(this.toggleWithValue(this.femaleCowOnlyMilking));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetFemaleCowOnlyMilkingButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.female_cow_only_milking"), (class_339)this.femaleCowOnlyMilkingButton, (class_339)this.resetFemaleCowOnlyMilkingButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.female_cow_only_milking")));
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> this.saveAndClose()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.method_37063((class_364)doneButton);
            if (this.useAnimationBreedingButton != null) {
                this.useAnimationBreedingButton.field_22763 = this.serverConfigEditable;
            }
            if (this.requireMaleFemaleForBreedingButton != null) {
                boolean bl = this.requireMaleFemaleForBreedingButton.field_22763 = this.serverConfigEditable && this.useAnimationBreeding;
            }
            if (this.femaleCowOnlyMilkingButton != null) {
                this.femaleCowOnlyMilkingButton.field_22763 = this.serverConfigEditable;
            }
            this.updateDependentTooltips();
            this.updateResetButtons();
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
                this.config.setUseAnimationBreeding(this.useAnimationBreeding);
                this.config.setRequireMaleFemaleForBreeding(this.requireMaleFemaleForBreeding);
                this.config.setFemaleCowOnlyMilking(this.femaleCowOnlyMilking);
                this.config.save();
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
            }
            class_310.method_1551().method_1507(this.parent);
        }

        private void updateResetButtons() {
            if (this.resetUseAnimationBreedingButton != null) {
                boolean bl = this.resetUseAnimationBreedingButton.field_22763 = this.serverConfigEditable && this.useAnimationBreeding != this.defaults.useAnimationBreeding();
            }
            if (this.resetRequireMaleFemaleForBreedingButton != null) {
                boolean bl = this.resetRequireMaleFemaleForBreedingButton.field_22763 = this.serverConfigEditable && this.useAnimationBreeding && this.requireMaleFemaleForBreeding != this.defaults.requireMaleFemaleForBreeding();
            }
            if (this.resetFemaleCowOnlyMilkingButton != null) {
                boolean bl = this.resetFemaleCowOnlyMilkingButton.field_22763 = this.serverConfigEditable && this.femaleCowOnlyMilking != this.defaults.femaleCowOnlyMilking();
            }
            if (this.useAnimationBreedingButton != null) {
                this.useAnimationBreedingButton.field_22763 = this.serverConfigEditable;
            }
            if (this.requireMaleFemaleForBreedingButton != null) {
                boolean bl = this.requireMaleFemaleForBreedingButton.field_22763 = this.serverConfigEditable && this.useAnimationBreeding;
            }
            if (this.femaleCowOnlyMilkingButton != null) {
                this.femaleCowOnlyMilkingButton.field_22763 = this.serverConfigEditable;
            }
            this.updateDependentTooltips();
        }

        private void updateDependentTooltips() {
            if (this.requireMaleFemaleForBreedingButton != null) {
                NonModMenuScreens.setTooltip((class_339)this.requireMaleFemaleForBreedingButton, this.useAnimationBreeding ? "config.needsofnature.tooltip.require_male_female_breeding" : "config.needsofnature.tooltip.require_male_female_breeding.disabled_animation_breeding");
            }
        }

        private class_2561 toggleWithValue(boolean enabled) {
            return class_2561.method_43471((String)(enabled ? "options.on" : "options.off"));
        }
    }

    static class ToggleSettingsScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean pregnancyEnabled;
        private boolean liquidTankEnabled;
        private boolean messSystemEnabled;
        private boolean destroyedSkinSystemEnabled;
        private boolean vanillaOverridesEnabled;
        private class_4185 pregnancyEnabledButton;
        private class_4185 resetPregnancyEnabledButton;
        private class_4185 liquidTankEnabledButton;
        private class_4185 resetLiquidTankEnabledButton;
        private class_4185 messSystemEnabledButton;
        private class_4185 resetMessSystemEnabledButton;
        private class_4185 destroyedSkinSystemEnabledButton;
        private class_4185 resetDestroyedSkinSystemEnabledButton;
        private class_4185 vanillaOverridesEnabledButton;
        private class_4185 resetVanillaOverridesEnabledButton;
        private boolean serverConfigEditable;

        protected ToggleSettingsScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.toggles_title"));
            this.parent = parent;
            this.config = config;
            this.pregnancyEnabled = config.isPregnancyEnabled();
            this.liquidTankEnabled = config.isLiquidTankEnabled();
            this.messSystemEnabled = config.isMessSystemEnabled();
            this.destroyedSkinSystemEnabled = config.isDestroyedSkinSystemEnabled();
            this.vanillaOverridesEnabled = config.isVanillaOverridesEnabled();
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            int centerX = this.field_22789 / 2;
            int listTop = 32;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.core_systems")));
            this.pregnancyEnabledButton = class_4185.method_46430((class_2561)this.toggleWithValue(this.pregnancyEnabled), button -> {
                this.pregnancyEnabled = !this.pregnancyEnabled;
                button.method_25355(this.toggleWithValue(this.pregnancyEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.pregnancyEnabledButton, "config.needsofnature.tooltip.toggle.pregnancy");
            this.resetPregnancyEnabledButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.pregnancyEnabled = this.defaults.isPregnancyEnabled();
                this.pregnancyEnabledButton.method_25355(this.toggleWithValue(this.pregnancyEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPregnancyEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.toggle.pregnancy"), (class_339)this.pregnancyEnabledButton, (class_339)this.resetPregnancyEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.pregnancy")));
            this.liquidTankEnabledButton = class_4185.method_46430((class_2561)this.toggleWithValue(this.liquidTankEnabled), button -> {
                this.liquidTankEnabled = !this.liquidTankEnabled;
                button.method_25355(this.toggleWithValue(this.liquidTankEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.liquidTankEnabledButton, "config.needsofnature.tooltip.toggle.liquid_tank");
            this.resetLiquidTankEnabledButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.liquidTankEnabled = this.defaults.isLiquidTankEnabled();
                this.liquidTankEnabledButton.method_25355(this.toggleWithValue(this.liquidTankEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetLiquidTankEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.toggle.liquid_tank"), (class_339)this.liquidTankEnabledButton, (class_339)this.resetLiquidTankEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.liquid_tank")));
            this.messSystemEnabledButton = class_4185.method_46430((class_2561)this.toggleWithValue(this.messSystemEnabled), button -> {
                this.messSystemEnabled = !this.messSystemEnabled;
                button.method_25355(this.toggleWithValue(this.messSystemEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.messSystemEnabledButton, "config.needsofnature.tooltip.toggle.mess_system");
            this.resetMessSystemEnabledButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.messSystemEnabled = this.defaults.isMessSystemEnabled();
                this.messSystemEnabledButton.method_25355(this.toggleWithValue(this.messSystemEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetMessSystemEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.toggle.mess_system"), (class_339)this.messSystemEnabledButton, (class_339)this.resetMessSystemEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.mess_system")));
            this.destroyedSkinSystemEnabledButton = class_4185.method_46430((class_2561)this.toggleWithValue(this.destroyedSkinSystemEnabled), button -> {
                this.destroyedSkinSystemEnabled = !this.destroyedSkinSystemEnabled;
                button.method_25355(this.toggleWithValue(this.destroyedSkinSystemEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.destroyedSkinSystemEnabledButton, "config.needsofnature.tooltip.toggle.destroyed_skin_system");
            this.resetDestroyedSkinSystemEnabledButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.destroyedSkinSystemEnabled = this.defaults.isDestroyedSkinSystemEnabled();
                this.destroyedSkinSystemEnabledButton.method_25355(this.toggleWithValue(this.destroyedSkinSystemEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetDestroyedSkinSystemEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.toggle.destroyed_skin_system"), (class_339)this.destroyedSkinSystemEnabledButton, (class_339)this.resetDestroyedSkinSystemEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.destroyed_skin_system")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.vanilla_integration")));
            this.vanillaOverridesEnabledButton = class_4185.method_46430((class_2561)this.toggleWithValue(this.vanillaOverridesEnabled), button -> {
                this.vanillaOverridesEnabled = !this.vanillaOverridesEnabled;
                button.method_25355(this.toggleWithValue(this.vanillaOverridesEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.vanillaOverridesEnabledButton, "config.needsofnature.tooltip.toggle.vanilla_overrides");
            this.resetVanillaOverridesEnabledButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.vanillaOverridesEnabled = this.defaults.isVanillaOverridesEnabled();
                this.vanillaOverridesEnabledButton.method_25355(this.toggleWithValue(this.vanillaOverridesEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetVanillaOverridesEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.toggle.vanilla_overrides"), (class_339)this.vanillaOverridesEnabledButton, (class_339)this.resetVanillaOverridesEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.vanilla_overrides")));
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> this.saveAndClose()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.method_37063((class_364)doneButton);
            if (this.pregnancyEnabledButton != null) {
                this.pregnancyEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.liquidTankEnabledButton != null) {
                this.liquidTankEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.messSystemEnabledButton != null) {
                this.messSystemEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.destroyedSkinSystemEnabledButton != null) {
                this.destroyedSkinSystemEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.vanillaOverridesEnabledButton != null) {
                this.vanillaOverridesEnabledButton.field_22763 = this.serverConfigEditable;
            }
            this.updateResetButtons();
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
                this.config.setPregnancyEnabled(this.pregnancyEnabled);
                this.config.setLiquidTankEnabled(this.liquidTankEnabled);
                this.config.setMessSystemEnabled(this.messSystemEnabled);
                this.config.setDestroyedSkinSystemEnabled(this.destroyedSkinSystemEnabled);
                this.config.setVanillaOverridesEnabled(this.vanillaOverridesEnabled);
                this.config.save();
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
            }
            class_310.method_1551().method_1507(this.parent);
        }

        private void updateResetButtons() {
            if (this.resetPregnancyEnabledButton != null) {
                boolean bl = this.resetPregnancyEnabledButton.field_22763 = this.serverConfigEditable && this.pregnancyEnabled != this.defaults.isPregnancyEnabled();
            }
            if (this.resetLiquidTankEnabledButton != null) {
                boolean bl = this.resetLiquidTankEnabledButton.field_22763 = this.serverConfigEditable && this.liquidTankEnabled != this.defaults.isLiquidTankEnabled();
            }
            if (this.resetMessSystemEnabledButton != null) {
                boolean bl = this.resetMessSystemEnabledButton.field_22763 = this.serverConfigEditable && this.messSystemEnabled != this.defaults.isMessSystemEnabled();
            }
            if (this.resetDestroyedSkinSystemEnabledButton != null) {
                boolean bl = this.resetDestroyedSkinSystemEnabledButton.field_22763 = this.serverConfigEditable && this.destroyedSkinSystemEnabled != this.defaults.isDestroyedSkinSystemEnabled();
            }
            if (this.resetVanillaOverridesEnabledButton != null) {
                boolean bl = this.resetVanillaOverridesEnabledButton.field_22763 = this.serverConfigEditable && this.vanillaOverridesEnabled != this.defaults.isVanillaOverridesEnabled();
            }
            if (this.pregnancyEnabledButton != null) {
                this.pregnancyEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.liquidTankEnabledButton != null) {
                this.liquidTankEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.messSystemEnabledButton != null) {
                this.messSystemEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.destroyedSkinSystemEnabledButton != null) {
                this.destroyedSkinSystemEnabledButton.field_22763 = this.serverConfigEditable;
            }
            if (this.vanillaOverridesEnabledButton != null) {
                this.vanillaOverridesEnabledButton.field_22763 = this.serverConfigEditable;
            }
        }

        private class_2561 toggleWithValue(boolean enabled) {
            return class_2561.method_43471((String)(enabled ? "options.on" : "options.off"));
        }
    }
}

