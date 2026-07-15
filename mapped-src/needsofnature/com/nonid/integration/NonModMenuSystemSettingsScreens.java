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
 */
package com.nonid.integration;

import com.nonid.NonConfig;
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

final class NonModMenuSystemSettingsScreens {
    private NonModMenuSystemSettingsScreens() {
    }

    static class LiquidSettingsScreen
    extends Screen {
        private final Screen parent;
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
        private TextFieldWidget liquidTankCapacityField;
        private TextFieldWidget liquidDecayField;
        private TextFieldWidget peakXpPerMlField;
        private TextFieldWidget filledStageOneSpeedField;
        private TextFieldWidget filledStageTwoSpeedField;
        private TextFieldWidget filledStageThreeSpeedField;
        private TextFieldWidget filledStageOneJumpField;
        private TextFieldWidget filledStageTwoJumpField;
        private TextFieldWidget filledStageThreeJumpField;
        private ButtonWidget resetLiquidTankCapacityButton;
        private ButtonWidget resetLiquidDecayButton;
        private ButtonWidget resetPeakXpPerMlButton;
        private ButtonWidget resetFilledStageOneSpeedButton;
        private ButtonWidget resetFilledStageTwoSpeedButton;
        private ButtonWidget resetFilledStageThreeSpeedButton;
        private ButtonWidget resetFilledStageOneJumpButton;
        private ButtonWidget resetFilledStageTwoJumpButton;
        private ButtonWidget resetFilledStageThreeJumpButton;

        protected LiquidSettingsScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.liquid_settings_title"));
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

        protected void init() {
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
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild((Element)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.liquid_tank")));
            this.liquidTankCapacityField = this.newNumberField(fieldWidth, this.liquidTankCapacityMl);
            this.resetLiquidTankCapacityButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.liquidTankCapacityField.setText(String.valueOf(this.defaults.getLiquidTankCapacityMl()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetLiquidTankCapacityButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.liquid_tank_capacity"), (ClickableWidget)this.liquidTankCapacityField, (ClickableWidget)this.resetLiquidTankCapacityButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.liquid_tank_capacity")));
            this.liquidDecayField = this.newDecimalField(fieldWidth, this.liquidDecay);
            this.resetLiquidDecayButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.liquidDecayField.setText(String.valueOf(this.defaults.getLiquidDecayPerSecond()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetLiquidDecayButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.liquid_decay"), (ClickableWidget)this.liquidDecayField, (ClickableWidget)this.resetLiquidDecayButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.liquid_decay")));
            this.peakXpPerMlField = this.newDecimalField(fieldWidth, this.peakXpPerMl);
            this.resetPeakXpPerMlButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.peakXpPerMlField.setText(String.valueOf(this.defaults.getPeakXpPerMl()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPeakXpPerMlButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.peak_xp_per_ml"), (ClickableWidget)this.peakXpPerMlField, (ClickableWidget)this.resetPeakXpPerMlButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.peak_xp_per_ml")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.filled_penalties")));
            this.filledStageOneSpeedField = this.newDecimalField(fieldWidth, this.filledStageOneSpeed);
            this.resetFilledStageOneSpeedButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.filledStageOneSpeedField.setText(String.valueOf(this.defaults.getFilledStageOneSpeedMult()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetFilledStageOneSpeedButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.filled_stage_one_speed"), (ClickableWidget)this.filledStageOneSpeedField, (ClickableWidget)this.resetFilledStageOneSpeedButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_one_speed")));
            this.filledStageTwoSpeedField = this.newDecimalField(fieldWidth, this.filledStageTwoSpeed);
            this.resetFilledStageTwoSpeedButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.filledStageTwoSpeedField.setText(String.valueOf(this.defaults.getFilledStageTwoSpeedMult()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetFilledStageTwoSpeedButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.filled_stage_two_speed"), (ClickableWidget)this.filledStageTwoSpeedField, (ClickableWidget)this.resetFilledStageTwoSpeedButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_two_speed")));
            this.filledStageThreeSpeedField = this.newDecimalField(fieldWidth, this.filledStageThreeSpeed);
            this.resetFilledStageThreeSpeedButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.filledStageThreeSpeedField.setText(String.valueOf(this.defaults.getFilledStageThreeSpeedMult()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetFilledStageThreeSpeedButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.filled_stage_three_speed"), (ClickableWidget)this.filledStageThreeSpeedField, (ClickableWidget)this.resetFilledStageThreeSpeedButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_three_speed")));
            this.filledStageOneJumpField = this.newDecimalField(fieldWidth, this.filledStageOneJump);
            this.resetFilledStageOneJumpButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.filledStageOneJumpField.setText(String.valueOf(this.defaults.getFilledStageOneJumpMult()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetFilledStageOneJumpButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.filled_stage_one_jump"), (ClickableWidget)this.filledStageOneJumpField, (ClickableWidget)this.resetFilledStageOneJumpButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_one_jump")));
            this.filledStageTwoJumpField = this.newDecimalField(fieldWidth, this.filledStageTwoJump);
            this.resetFilledStageTwoJumpButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.filledStageTwoJumpField.setText(String.valueOf(this.defaults.getFilledStageTwoJumpMult()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetFilledStageTwoJumpButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.filled_stage_two_jump"), (ClickableWidget)this.filledStageTwoJumpField, (ClickableWidget)this.resetFilledStageTwoJumpButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_two_jump")));
            this.filledStageThreeJumpField = this.newDecimalField(fieldWidth, this.filledStageThreeJump);
            this.resetFilledStageThreeJumpButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.filledStageThreeJumpField.setText(String.valueOf(this.defaults.getFilledStageThreeJumpMult()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetFilledStageThreeJumpButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.filled_stage_three_jump"), (ClickableWidget)this.filledStageThreeJumpField, (ClickableWidget)this.resetFilledStageThreeJumpButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.filled_stage_three_jump")));
            this.liquidTankCapacityField.setEditable(this.serverConfigEditable);
            this.liquidDecayField.setEditable(this.serverConfigEditable);
            this.peakXpPerMlField.setEditable(this.serverConfigEditable);
            this.filledStageOneSpeedField.setEditable(this.serverConfigEditable);
            this.filledStageTwoSpeedField.setEditable(this.serverConfigEditable);
            this.filledStageThreeSpeedField.setEditable(this.serverConfigEditable);
            this.filledStageOneJumpField.setEditable(this.serverConfigEditable);
            this.filledStageTwoJumpField.setEditable(this.serverConfigEditable);
            this.filledStageThreeJumpField.setEditable(this.serverConfigEditable);
            this.liquidTankCapacityField.setChangedListener(ignored -> this.updateResetButtons());
            this.liquidDecayField.setChangedListener(ignored -> this.updateResetButtons());
            this.peakXpPerMlField.setChangedListener(ignored -> this.updateResetButtons());
            this.filledStageOneSpeedField.setChangedListener(ignored -> this.updateResetButtons());
            this.filledStageTwoSpeedField.setChangedListener(ignored -> this.updateResetButtons());
            this.filledStageThreeSpeedField.setChangedListener(ignored -> this.updateResetButtons());
            this.filledStageOneJumpField.setChangedListener(ignored -> this.updateResetButtons());
            this.filledStageTwoJumpField.setChangedListener(ignored -> this.updateResetButtons());
            this.filledStageThreeJumpField.setChangedListener(ignored -> this.updateResetButtons());
            this.updateResetButtons();
            int centerX = this.width / 2;
            ButtonWidget liquidGainButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.liquid_gain_button"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuDebugScreens.LiquidGainConfigScreen(this, this.config))).dimensions(centerX - 100, this.height - 52, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)liquidGainButton, "config.needsofnature.tooltip.liquid_gain_button");
            this.addDrawableChild((Element)liquidGainButton);
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
            if (this.resetLiquidTankCapacityButton != null) {
                boolean bl = this.resetLiquidTankCapacityButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.liquidTankCapacityField, this.defaults.getLiquidTankCapacityMl());
            }
            if (this.resetLiquidDecayButton != null) {
                boolean bl = this.resetLiquidDecayButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.liquidDecayField, this.defaults.getLiquidDecayPerSecond());
            }
            if (this.resetPeakXpPerMlButton != null) {
                boolean bl = this.resetPeakXpPerMlButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.peakXpPerMlField, this.defaults.getPeakXpPerMl());
            }
            if (this.resetFilledStageOneSpeedButton != null) {
                boolean bl = this.resetFilledStageOneSpeedButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageOneSpeedField, this.defaults.getFilledStageOneSpeedMult());
            }
            if (this.resetFilledStageTwoSpeedButton != null) {
                boolean bl = this.resetFilledStageTwoSpeedButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageTwoSpeedField, this.defaults.getFilledStageTwoSpeedMult());
            }
            if (this.resetFilledStageThreeSpeedButton != null) {
                boolean bl = this.resetFilledStageThreeSpeedButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageThreeSpeedField, this.defaults.getFilledStageThreeSpeedMult());
            }
            if (this.resetFilledStageOneJumpButton != null) {
                boolean bl = this.resetFilledStageOneJumpButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageOneJumpField, this.defaults.getFilledStageOneJumpMult());
            }
            if (this.resetFilledStageTwoJumpButton != null) {
                boolean bl = this.resetFilledStageTwoJumpButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageTwoJumpField, this.defaults.getFilledStageTwoJumpMult());
            }
            if (this.resetFilledStageThreeJumpButton != null) {
                this.resetFilledStageThreeJumpButton.active = this.serverConfigEditable && this.isDoubleFieldChanged(this.filledStageThreeJumpField, this.defaults.getFilledStageThreeJumpMult());
            }
        }
    }

    static class PregnancySettingsScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean serverConfigEditable;
        private int pregnancyChancePercent;
        private int pregnancyDurationMinutes;
        private int pregnancyEggHatchMinutes;
        private boolean pregnancyAutoTameMobs;
        private boolean pregnancyFriendlyMobsIgnorePlayers;
        private TextFieldWidget pregnancyChanceField;
        private TextFieldWidget pregnancyDurationField;
        private TextFieldWidget pregnancyEggHatchMinutesField;
        private ButtonWidget pregnancyAutoTameMobsButton;
        private ButtonWidget pregnancyFriendlyMobsIgnorePlayersButton;
        private ButtonWidget resetPregnancyChanceButton;
        private ButtonWidget resetPregnancyDurationButton;
        private ButtonWidget resetPregnancyEggHatchMinutesButton;
        private ButtonWidget resetPregnancyAutoTameMobsButton;
        private ButtonWidget resetPregnancyFriendlyMobsIgnorePlayersButton;

        protected PregnancySettingsScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.pregnancy_title"));
            this.parent = parent;
            this.config = config;
            this.pregnancyChancePercent = config.getPregnancyChancePercent();
            this.pregnancyDurationMinutes = config.getPregnancyDurationMinutes();
            this.pregnancyEggHatchMinutes = this.eggHatchSecondsToMinutes(config.getPregnancyEggDefaultHatchSeconds());
            this.pregnancyAutoTameMobs = config.isPregnancyAutoTameMobs();
            this.pregnancyFriendlyMobsIgnorePlayers = config.pregnancyFriendlyMobsIgnorePlayers();
        }

        protected void init() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.pregnancyChanceField != null) {
                this.pregnancyChancePercent = this.parseField(this.pregnancyChanceField, this.pregnancyChancePercent);
                this.pregnancyDurationMinutes = this.parseField(this.pregnancyDurationField, this.pregnancyDurationMinutes);
                this.pregnancyEggHatchMinutes = this.parseField(this.pregnancyEggHatchMinutesField, this.pregnancyEggHatchMinutes);
            }
            int listTop = 32;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild((Element)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.pregnancy_timing")));
            this.pregnancyChanceField = this.newNumberField(fieldWidth, this.pregnancyChancePercent);
            this.resetPregnancyChanceButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.pregnancyChanceField.setText(String.valueOf(this.defaults.getPregnancyChancePercent()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPregnancyChanceButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.pregnancy_chance_percent"), (ClickableWidget)this.pregnancyChanceField, (ClickableWidget)this.resetPregnancyChanceButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_chance_percent")));
            this.pregnancyDurationField = this.newNumberField(fieldWidth, this.pregnancyDurationMinutes);
            this.resetPregnancyDurationButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.pregnancyDurationField.setText(String.valueOf(this.defaults.getPregnancyDurationMinutes()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPregnancyDurationButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.pregnancy_duration_minutes"), (ClickableWidget)this.pregnancyDurationField, (ClickableWidget)this.resetPregnancyDurationButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_duration_minutes")));
            this.pregnancyEggHatchMinutesField = this.newNumberField(fieldWidth, this.pregnancyEggHatchMinutes);
            this.resetPregnancyEggHatchMinutesButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.pregnancyEggHatchMinutesField.setText(String.valueOf(this.eggHatchSecondsToMinutes(this.defaults.getPregnancyEggDefaultHatchSeconds())))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPregnancyEggHatchMinutesButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.pregnancy_egg_hatch_minutes"), (ClickableWidget)this.pregnancyEggHatchMinutesField, (ClickableWidget)this.resetPregnancyEggHatchMinutesButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_egg_hatch_minutes")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.pregnancy_behavior")));
            this.pregnancyAutoTameMobsButton = ButtonWidget.builder((Text)this.toggleText(this.pregnancyAutoTameMobs), button -> {
                this.pregnancyAutoTameMobs = !this.pregnancyAutoTameMobs;
                button.setMessage(this.toggleText(this.pregnancyAutoTameMobs));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetPregnancyAutoTameMobsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.pregnancyAutoTameMobs = this.defaults.isPregnancyAutoTameMobs();
                this.pregnancyAutoTameMobsButton.setMessage(this.toggleText(this.pregnancyAutoTameMobs));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPregnancyAutoTameMobsButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.pregnancy_auto_tame_mobs"), (ClickableWidget)this.pregnancyAutoTameMobsButton, (ClickableWidget)this.resetPregnancyAutoTameMobsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_auto_tame_mobs")));
            this.pregnancyFriendlyMobsIgnorePlayersButton = ButtonWidget.builder((Text)this.toggleText(this.pregnancyFriendlyMobsIgnorePlayers), button -> {
                this.pregnancyFriendlyMobsIgnorePlayers = !this.pregnancyFriendlyMobsIgnorePlayers;
                button.setMessage(this.toggleText(this.pregnancyFriendlyMobsIgnorePlayers));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetPregnancyFriendlyMobsIgnorePlayersButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.pregnancyFriendlyMobsIgnorePlayers = this.defaults.pregnancyFriendlyMobsIgnorePlayers();
                this.pregnancyFriendlyMobsIgnorePlayersButton.setMessage(this.toggleText(this.pregnancyFriendlyMobsIgnorePlayers));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPregnancyFriendlyMobsIgnorePlayersButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.pregnancy_friendly_mobs_ignore_players"), (ClickableWidget)this.pregnancyFriendlyMobsIgnorePlayersButton, (ClickableWidget)this.resetPregnancyFriendlyMobsIgnorePlayersButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.pregnancy_friendly_mobs_ignore_players")));
            this.pregnancyChanceField.setEditable(this.serverConfigEditable);
            this.pregnancyDurationField.setEditable(this.serverConfigEditable);
            this.pregnancyEggHatchMinutesField.setEditable(this.serverConfigEditable);
            this.pregnancyAutoTameMobsButton.active = this.serverConfigEditable;
            this.pregnancyFriendlyMobsIgnorePlayersButton.active = this.serverConfigEditable;
            this.pregnancyChanceField.setChangedListener(ignored -> this.updateResetButtons());
            this.pregnancyDurationField.setChangedListener(ignored -> this.updateResetButtons());
            this.pregnancyEggHatchMinutesField.setChangedListener(ignored -> this.updateResetButtons());
            this.updateResetButtons();
            int centerX = this.width / 2;
            ButtonWidget offspringCountButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.offspring_count_button"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuDebugScreens.OffspringCountConfigScreen(this, this.config))).dimensions(centerX - 100, this.height - 52, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)offspringCountButton, "config.needsofnature.tooltip.offspring_count_button");
            this.addDrawableChild((Element)offspringCountButton);
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

        private boolean isIntFieldChanged(TextFieldWidget field, int defaultValue) {
            if (field == null) {
                return false;
            }
            return this.parseField(field, defaultValue) != defaultValue;
        }

        private Text toggleText(boolean enabled) {
            return Text.translatable((String)(enabled ? "options.on" : "options.off"));
        }

        private void updateResetButtons() {
            if (this.resetPregnancyChanceButton != null) {
                boolean bl = this.resetPregnancyChanceButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.pregnancyChanceField, this.defaults.getPregnancyChancePercent());
            }
            if (this.resetPregnancyDurationButton != null) {
                boolean bl = this.resetPregnancyDurationButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.pregnancyDurationField, this.defaults.getPregnancyDurationMinutes());
            }
            if (this.resetPregnancyEggHatchMinutesButton != null) {
                boolean bl = this.resetPregnancyEggHatchMinutesButton.active = this.serverConfigEditable && this.isIntFieldChanged(this.pregnancyEggHatchMinutesField, this.eggHatchSecondsToMinutes(this.defaults.getPregnancyEggDefaultHatchSeconds()));
            }
            if (this.resetPregnancyAutoTameMobsButton != null) {
                boolean bl = this.resetPregnancyAutoTameMobsButton.active = this.serverConfigEditable && this.pregnancyAutoTameMobs != this.defaults.isPregnancyAutoTameMobs();
            }
            if (this.resetPregnancyFriendlyMobsIgnorePlayersButton != null) {
                this.resetPregnancyFriendlyMobsIgnorePlayersButton.active = this.serverConfigEditable && this.pregnancyFriendlyMobsIgnorePlayers != this.defaults.pregnancyFriendlyMobsIgnorePlayers();
            }
        }
    }

    static class VanillaOverrideScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean useAnimationBreeding;
        private boolean requireMaleFemaleForBreeding;
        private boolean femaleCowOnlyMilking;
        private ButtonWidget useAnimationBreedingButton;
        private ButtonWidget resetUseAnimationBreedingButton;
        private ButtonWidget requireMaleFemaleForBreedingButton;
        private ButtonWidget resetRequireMaleFemaleForBreedingButton;
        private ButtonWidget femaleCowOnlyMilkingButton;
        private ButtonWidget resetFemaleCowOnlyMilkingButton;
        private boolean serverConfigEditable;

        protected VanillaOverrideScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.vanilla_override_title"));
            this.parent = parent;
            this.config = config;
            this.useAnimationBreeding = config.useAnimationBreeding();
            this.requireMaleFemaleForBreeding = config.requireMaleFemaleForBreeding();
            this.femaleCowOnlyMilking = config.femaleCowOnlyMilking();
        }

        protected void init() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            int centerX = this.width / 2;
            int listTop = 32;
            int bottomArea = 88;
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild((Element)settingsList);
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.breeding")));
            this.useAnimationBreedingButton = ButtonWidget.builder((Text)this.toggleWithValue(this.useAnimationBreeding), button -> {
                this.useAnimationBreeding = !this.useAnimationBreeding;
                button.setMessage(this.toggleWithValue(this.useAnimationBreeding));
                this.updateDependentTooltips();
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.useAnimationBreedingButton, "config.needsofnature.tooltip.use_animation_breeding");
            this.resetUseAnimationBreedingButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.useAnimationBreeding = this.defaults.useAnimationBreeding();
                this.useAnimationBreedingButton.setMessage(this.toggleWithValue(this.useAnimationBreeding));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetUseAnimationBreedingButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.use_animation_breeding"), (ClickableWidget)this.useAnimationBreedingButton, (ClickableWidget)this.resetUseAnimationBreedingButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.use_animation_breeding")));
            this.requireMaleFemaleForBreedingButton = ButtonWidget.builder((Text)this.toggleWithValue(this.requireMaleFemaleForBreeding), button -> {
                this.requireMaleFemaleForBreeding = !this.requireMaleFemaleForBreeding;
                button.setMessage(this.toggleWithValue(this.requireMaleFemaleForBreeding));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetRequireMaleFemaleForBreedingButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.requireMaleFemaleForBreeding = this.defaults.requireMaleFemaleForBreeding();
                this.requireMaleFemaleForBreedingButton.setMessage(this.toggleWithValue(this.requireMaleFemaleForBreeding));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetRequireMaleFemaleForBreedingButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.require_male_female_breeding"), (ClickableWidget)this.requireMaleFemaleForBreedingButton, (ClickableWidget)this.resetRequireMaleFemaleForBreedingButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.require_male_female_breeding")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.milking")));
            this.femaleCowOnlyMilkingButton = ButtonWidget.builder((Text)this.toggleWithValue(this.femaleCowOnlyMilking), button -> {
                this.femaleCowOnlyMilking = !this.femaleCowOnlyMilking;
                button.setMessage(this.toggleWithValue(this.femaleCowOnlyMilking));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.femaleCowOnlyMilkingButton, "config.needsofnature.tooltip.female_cow_only_milking");
            this.resetFemaleCowOnlyMilkingButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.femaleCowOnlyMilking = this.defaults.femaleCowOnlyMilking();
                this.femaleCowOnlyMilkingButton.setMessage(this.toggleWithValue(this.femaleCowOnlyMilking));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetFemaleCowOnlyMilkingButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.female_cow_only_milking"), (ClickableWidget)this.femaleCowOnlyMilkingButton, (ClickableWidget)this.resetFemaleCowOnlyMilkingButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.female_cow_only_milking")));
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.saveAndClose()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.addDrawableChild((Element)doneButton);
            if (this.useAnimationBreedingButton != null) {
                this.useAnimationBreedingButton.active = this.serverConfigEditable;
            }
            if (this.requireMaleFemaleForBreedingButton != null) {
                boolean bl = this.requireMaleFemaleForBreedingButton.active = this.serverConfigEditable && this.useAnimationBreeding;
            }
            if (this.femaleCowOnlyMilkingButton != null) {
                this.femaleCowOnlyMilkingButton.active = this.serverConfigEditable;
            }
            this.updateDependentTooltips();
            this.updateResetButtons();
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
                this.config.setUseAnimationBreeding(this.useAnimationBreeding);
                this.config.setRequireMaleFemaleForBreeding(this.requireMaleFemaleForBreeding);
                this.config.setFemaleCowOnlyMilking(this.femaleCowOnlyMilking);
                this.config.save();
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
            }
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private void updateResetButtons() {
            if (this.resetUseAnimationBreedingButton != null) {
                boolean bl = this.resetUseAnimationBreedingButton.active = this.serverConfigEditable && this.useAnimationBreeding != this.defaults.useAnimationBreeding();
            }
            if (this.resetRequireMaleFemaleForBreedingButton != null) {
                boolean bl = this.resetRequireMaleFemaleForBreedingButton.active = this.serverConfigEditable && this.useAnimationBreeding && this.requireMaleFemaleForBreeding != this.defaults.requireMaleFemaleForBreeding();
            }
            if (this.resetFemaleCowOnlyMilkingButton != null) {
                boolean bl = this.resetFemaleCowOnlyMilkingButton.active = this.serverConfigEditable && this.femaleCowOnlyMilking != this.defaults.femaleCowOnlyMilking();
            }
            if (this.useAnimationBreedingButton != null) {
                this.useAnimationBreedingButton.active = this.serverConfigEditable;
            }
            if (this.requireMaleFemaleForBreedingButton != null) {
                boolean bl = this.requireMaleFemaleForBreedingButton.active = this.serverConfigEditable && this.useAnimationBreeding;
            }
            if (this.femaleCowOnlyMilkingButton != null) {
                this.femaleCowOnlyMilkingButton.active = this.serverConfigEditable;
            }
            this.updateDependentTooltips();
        }

        private void updateDependentTooltips() {
            if (this.requireMaleFemaleForBreedingButton != null) {
                NonModMenuScreens.setTooltip((ClickableWidget)this.requireMaleFemaleForBreedingButton, this.useAnimationBreeding ? "config.needsofnature.tooltip.require_male_female_breeding" : "config.needsofnature.tooltip.require_male_female_breeding.disabled_animation_breeding");
            }
        }

        private Text toggleWithValue(boolean enabled) {
            return Text.translatable((String)(enabled ? "options.on" : "options.off"));
        }
    }

    static class ToggleSettingsScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean pregnancyEnabled;
        private boolean liquidTankEnabled;
        private boolean messSystemEnabled;
        private boolean destroyedSkinSystemEnabled;
        private boolean vanillaOverridesEnabled;
        private ButtonWidget pregnancyEnabledButton;
        private ButtonWidget resetPregnancyEnabledButton;
        private ButtonWidget liquidTankEnabledButton;
        private ButtonWidget resetLiquidTankEnabledButton;
        private ButtonWidget messSystemEnabledButton;
        private ButtonWidget resetMessSystemEnabledButton;
        private ButtonWidget destroyedSkinSystemEnabledButton;
        private ButtonWidget resetDestroyedSkinSystemEnabledButton;
        private ButtonWidget vanillaOverridesEnabledButton;
        private ButtonWidget resetVanillaOverridesEnabledButton;
        private boolean serverConfigEditable;

        protected ToggleSettingsScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.toggles_title"));
            this.parent = parent;
            this.config = config;
            this.pregnancyEnabled = config.isPregnancyEnabled();
            this.liquidTankEnabled = config.isLiquidTankEnabled();
            this.messSystemEnabled = config.isMessSystemEnabled();
            this.destroyedSkinSystemEnabled = config.isDestroyedSkinSystemEnabled();
            this.vanillaOverridesEnabled = config.isVanillaOverridesEnabled();
        }

        protected void init() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            int centerX = this.width / 2;
            int listTop = 32;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild((Element)settingsList);
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.core_systems")));
            this.pregnancyEnabledButton = ButtonWidget.builder((Text)this.toggleWithValue(this.pregnancyEnabled), button -> {
                this.pregnancyEnabled = !this.pregnancyEnabled;
                button.setMessage(this.toggleWithValue(this.pregnancyEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.pregnancyEnabledButton, "config.needsofnature.tooltip.toggle.pregnancy");
            this.resetPregnancyEnabledButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.pregnancyEnabled = this.defaults.isPregnancyEnabled();
                this.pregnancyEnabledButton.setMessage(this.toggleWithValue(this.pregnancyEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetPregnancyEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.toggle.pregnancy"), (ClickableWidget)this.pregnancyEnabledButton, (ClickableWidget)this.resetPregnancyEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.pregnancy")));
            this.liquidTankEnabledButton = ButtonWidget.builder((Text)this.toggleWithValue(this.liquidTankEnabled), button -> {
                this.liquidTankEnabled = !this.liquidTankEnabled;
                button.setMessage(this.toggleWithValue(this.liquidTankEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.liquidTankEnabledButton, "config.needsofnature.tooltip.toggle.liquid_tank");
            this.resetLiquidTankEnabledButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.liquidTankEnabled = this.defaults.isLiquidTankEnabled();
                this.liquidTankEnabledButton.setMessage(this.toggleWithValue(this.liquidTankEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetLiquidTankEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.toggle.liquid_tank"), (ClickableWidget)this.liquidTankEnabledButton, (ClickableWidget)this.resetLiquidTankEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.liquid_tank")));
            this.messSystemEnabledButton = ButtonWidget.builder((Text)this.toggleWithValue(this.messSystemEnabled), button -> {
                this.messSystemEnabled = !this.messSystemEnabled;
                button.setMessage(this.toggleWithValue(this.messSystemEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.messSystemEnabledButton, "config.needsofnature.tooltip.toggle.mess_system");
            this.resetMessSystemEnabledButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.messSystemEnabled = this.defaults.isMessSystemEnabled();
                this.messSystemEnabledButton.setMessage(this.toggleWithValue(this.messSystemEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetMessSystemEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.toggle.mess_system"), (ClickableWidget)this.messSystemEnabledButton, (ClickableWidget)this.resetMessSystemEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.mess_system")));
            this.destroyedSkinSystemEnabledButton = ButtonWidget.builder((Text)this.toggleWithValue(this.destroyedSkinSystemEnabled), button -> {
                this.destroyedSkinSystemEnabled = !this.destroyedSkinSystemEnabled;
                button.setMessage(this.toggleWithValue(this.destroyedSkinSystemEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.destroyedSkinSystemEnabledButton, "config.needsofnature.tooltip.toggle.destroyed_skin_system");
            this.resetDestroyedSkinSystemEnabledButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.destroyedSkinSystemEnabled = this.defaults.isDestroyedSkinSystemEnabled();
                this.destroyedSkinSystemEnabledButton.setMessage(this.toggleWithValue(this.destroyedSkinSystemEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetDestroyedSkinSystemEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.toggle.destroyed_skin_system"), (ClickableWidget)this.destroyedSkinSystemEnabledButton, (ClickableWidget)this.resetDestroyedSkinSystemEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.destroyed_skin_system")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.vanilla_integration")));
            this.vanillaOverridesEnabledButton = ButtonWidget.builder((Text)this.toggleWithValue(this.vanillaOverridesEnabled), button -> {
                this.vanillaOverridesEnabled = !this.vanillaOverridesEnabled;
                button.setMessage(this.toggleWithValue(this.vanillaOverridesEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.vanillaOverridesEnabledButton, "config.needsofnature.tooltip.toggle.vanilla_overrides");
            this.resetVanillaOverridesEnabledButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.vanillaOverridesEnabled = this.defaults.isVanillaOverridesEnabled();
                this.vanillaOverridesEnabledButton.setMessage(this.toggleWithValue(this.vanillaOverridesEnabled));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetVanillaOverridesEnabledButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.toggle.vanilla_overrides"), (ClickableWidget)this.vanillaOverridesEnabledButton, (ClickableWidget)this.resetVanillaOverridesEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.toggle.vanilla_overrides")));
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.saveAndClose()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)doneButton, this.serverConfigEditable ? "config.needsofnature.tooltip.done_save" : "config.needsofnature.tooltip.done_unsaved");
            this.addDrawableChild((Element)doneButton);
            if (this.pregnancyEnabledButton != null) {
                this.pregnancyEnabledButton.active = this.serverConfigEditable;
            }
            if (this.liquidTankEnabledButton != null) {
                this.liquidTankEnabledButton.active = this.serverConfigEditable;
            }
            if (this.messSystemEnabledButton != null) {
                this.messSystemEnabledButton.active = this.serverConfigEditable;
            }
            if (this.destroyedSkinSystemEnabledButton != null) {
                this.destroyedSkinSystemEnabledButton.active = this.serverConfigEditable;
            }
            if (this.vanillaOverridesEnabledButton != null) {
                this.vanillaOverridesEnabledButton.active = this.serverConfigEditable;
            }
            this.updateResetButtons();
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
                this.config.setPregnancyEnabled(this.pregnancyEnabled);
                this.config.setLiquidTankEnabled(this.liquidTankEnabled);
                this.config.setMessSystemEnabled(this.messSystemEnabled);
                this.config.setDestroyedSkinSystemEnabled(this.destroyedSkinSystemEnabled);
                this.config.setVanillaOverridesEnabled(this.vanillaOverridesEnabled);
                this.config.save();
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
            }
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private void updateResetButtons() {
            if (this.resetPregnancyEnabledButton != null) {
                boolean bl = this.resetPregnancyEnabledButton.active = this.serverConfigEditable && this.pregnancyEnabled != this.defaults.isPregnancyEnabled();
            }
            if (this.resetLiquidTankEnabledButton != null) {
                boolean bl = this.resetLiquidTankEnabledButton.active = this.serverConfigEditable && this.liquidTankEnabled != this.defaults.isLiquidTankEnabled();
            }
            if (this.resetMessSystemEnabledButton != null) {
                boolean bl = this.resetMessSystemEnabledButton.active = this.serverConfigEditable && this.messSystemEnabled != this.defaults.isMessSystemEnabled();
            }
            if (this.resetDestroyedSkinSystemEnabledButton != null) {
                boolean bl = this.resetDestroyedSkinSystemEnabledButton.active = this.serverConfigEditable && this.destroyedSkinSystemEnabled != this.defaults.isDestroyedSkinSystemEnabled();
            }
            if (this.resetVanillaOverridesEnabledButton != null) {
                boolean bl = this.resetVanillaOverridesEnabledButton.active = this.serverConfigEditable && this.vanillaOverridesEnabled != this.defaults.isVanillaOverridesEnabled();
            }
            if (this.pregnancyEnabledButton != null) {
                this.pregnancyEnabledButton.active = this.serverConfigEditable;
            }
            if (this.liquidTankEnabledButton != null) {
                this.liquidTankEnabledButton.active = this.serverConfigEditable;
            }
            if (this.messSystemEnabledButton != null) {
                this.messSystemEnabledButton.active = this.serverConfigEditable;
            }
            if (this.destroyedSkinSystemEnabledButton != null) {
                this.destroyedSkinSystemEnabledButton.active = this.serverConfigEditable;
            }
            if (this.vanillaOverridesEnabledButton != null) {
                this.vanillaOverridesEnabledButton.active = this.serverConfigEditable;
            }
        }

        private Text toggleWithValue(boolean enabled) {
            return Text.translatable((String)(enabled ? "options.on" : "options.off"));
        }
    }
}

