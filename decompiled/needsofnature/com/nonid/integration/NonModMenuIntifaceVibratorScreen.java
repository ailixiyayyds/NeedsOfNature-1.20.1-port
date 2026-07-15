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
import com.nonid.integration.FloatValueSliderWidget;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import java.util.Locale;
import java.util.function.DoubleConsumer;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;

final class NonModMenuIntifaceVibratorScreen
extends class_437 {
    private final class_437 parent;
    private final NonConfig config;
    private final NonConfig defaults = new NonConfig();
    private int reactiveStrengthPercent;
    private int reactiveDurationMs;
    private int peakStrengthPercent;
    private int peakDurationMs;
    private int cooldownMs;
    private int animationBaselineStrengthPercent;
    private final int[] energizedBasePercent = new int[3];
    private final int[] energizedPulsePercent = new int[3];
    private FloatValueSliderWidget reactiveStrengthSlider;
    private class_342 reactiveDurationField;
    private class_4185 resetReactiveButton;
    private FloatValueSliderWidget peakStrengthSlider;
    private class_342 peakDurationField;
    private class_4185 resetPeakButton;
    private class_342 cooldownField;
    private class_4185 resetCooldownButton;
    private FloatValueSliderWidget animationBaselineSlider;
    private class_4185 resetAnimationBaselineButton;
    private final FloatValueSliderWidget[] energizedBaseSliders = new FloatValueSliderWidget[3];
    private final FloatValueSliderWidget[] energizedPulseSliders = new FloatValueSliderWidget[3];
    private final class_4185[] resetEnergizedButtons = new class_4185[3];

    NonModMenuIntifaceVibratorScreen(class_437 parent, NonConfig config) {
        super((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_vibrator_title"));
        this.parent = parent;
        this.config = config;
        this.loadFromConfig();
    }

    protected void method_25426() {
        if (this.reactiveDurationField != null) {
            this.reactiveDurationMs = this.parseField(this.reactiveDurationField, this.reactiveDurationMs);
            this.peakDurationMs = this.parseField(this.peakDurationField, this.peakDurationMs);
            this.cooldownMs = this.parseField(this.cooldownField, this.cooldownMs);
        }
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
        this.method_37063((class_364)settingsList);
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_reactiveimpact")));
        this.reactiveStrengthSlider = this.percentSlider(this.reactiveStrengthPercent, value -> {
            this.reactiveStrengthPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.reactiveDurationField = this.newNumberField(70, this.reactiveDurationMs);
        this.reactiveDurationField.method_1863(ignored -> this.updateResetButtons());
        this.resetReactiveButton = this.resetButton(() -> {
            this.reactiveStrengthPercent = this.defaults.getIntifaceReactiveImpactStrengthPercent();
            this.reactiveDurationMs = this.defaults.getIntifaceReactiveImpactDurationMs();
            this.reactiveStrengthSlider.setFloatValue(this.reactiveStrengthPercent);
            this.reactiveDurationField.method_1852(String.valueOf(this.reactiveDurationMs));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_reactiveimpact"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_reactiveimpact"), new class_339[]{this.reactiveStrengthSlider, this.reactiveDurationField, this.resetReactiveButton}));
        this.peakStrengthSlider = this.percentSlider(this.peakStrengthPercent, value -> {
            this.peakStrengthPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.peakDurationField = this.newNumberField(70, this.peakDurationMs);
        this.peakDurationField.method_1863(ignored -> this.updateResetButtons());
        this.resetPeakButton = this.resetButton(() -> {
            this.peakStrengthPercent = this.defaults.getIntifacePeakReactiveImpactStrengthPercent();
            this.peakDurationMs = this.defaults.getIntifacePeakReactiveImpactDurationMs();
            this.peakStrengthSlider.setFloatValue(this.peakStrengthPercent);
            this.peakDurationField.method_1852(String.valueOf(this.peakDurationMs));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_peak_reactiveimpact"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_peak_reactiveimpact"), new class_339[]{this.peakStrengthSlider, this.peakDurationField, this.resetPeakButton}));
        this.cooldownField = this.newNumberField(70, this.cooldownMs);
        this.cooldownField.method_1863(ignored -> this.updateResetButtons());
        this.resetCooldownButton = this.resetButton(() -> {
            this.cooldownMs = this.defaults.getIntifaceCooldownMs();
            this.cooldownField.method_1852(String.valueOf(this.cooldownMs));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_cooldown"), (class_339)this.cooldownField, (class_339)this.resetCooldownButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_cooldown")));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_baseline")));
        this.animationBaselineSlider = this.percentSlider(this.animationBaselineStrengthPercent, value -> {
            this.animationBaselineStrengthPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetAnimationBaselineButton = this.resetButton(() -> {
            this.animationBaselineStrengthPercent = this.defaults.getIntifaceAnimationBaselineStrengthPercent();
            this.animationBaselineSlider.setFloatValue(this.animationBaselineStrengthPercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_animation_baseline"), (class_339)this.animationBaselineSlider, (class_339)this.resetAnimationBaselineButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_animation_baseline")));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_energized")));
        for (int level = 1; level <= 3; ++level) {
            int index = level - 1;
            int rowLevel = level;
            this.energizedBaseSliders[index] = this.percentSlider(this.energizedBasePercent[index], value -> {
                this.energizedBasePercent[rowLevel - 1] = (int)Math.round(value);
                this.updateResetButtons();
            });
            this.energizedPulseSliders[index] = this.percentSlider(this.energizedPulsePercent[index], value -> {
                this.energizedPulsePercent[rowLevel - 1] = (int)Math.round(value);
                this.updateResetButtons();
            });
            this.resetEnergizedButtons[index] = this.resetButton(() -> {
                this.energizedBasePercent[rowLevel - 1] = this.defaults.getIntifaceEnergizedBasePercent(rowLevel);
                this.energizedPulsePercent[rowLevel - 1] = this.defaults.getIntifaceEnergizedPulsePercent(rowLevel);
                this.energizedBaseSliders[rowLevel - 1].setFloatValue(this.energizedBasePercent[rowLevel - 1]);
                this.energizedPulseSliders[rowLevel - 1].setFloatValue(this.energizedPulsePercent[rowLevel - 1]);
            });
            settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.field_22793, (class_2561)class_2561.method_43469((String)"config.needsofnature.intiface_energized_level", (Object[])new Object[]{level}), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_energized"), new class_339[]{this.energizedBaseSliders[index], this.energizedPulseSliders[index], this.resetEnergizedButtons[index]}));
        }
        this.updateResetButtons();
        int centerX = this.field_22789 / 2;
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
        this.saveValues();
        class_310.method_1551().method_1507(this.parent);
    }

    private void saveValues() {
        this.reactiveDurationMs = this.parseField(this.reactiveDurationField, this.reactiveDurationMs);
        this.peakDurationMs = this.parseField(this.peakDurationField, this.peakDurationMs);
        this.cooldownMs = this.parseField(this.cooldownField, this.cooldownMs);
        this.config.setIntifaceReactiveImpactStrengthPercent(this.reactiveStrengthPercent);
        this.config.setIntifaceReactiveImpactDurationMs(this.reactiveDurationMs);
        this.config.setIntifacePeakReactiveImpactStrengthPercent(this.peakStrengthPercent);
        this.config.setIntifacePeakReactiveImpactDurationMs(this.peakDurationMs);
        this.config.setIntifaceCooldownMs(this.cooldownMs);
        this.config.setIntifaceAnimationBaselineStrengthPercent(this.animationBaselineStrengthPercent);
        for (int level = 1; level <= 3; ++level) {
            this.config.setIntifaceEnergizedBasePercent(level, this.energizedBasePercent[level - 1]);
            this.config.setIntifaceEnergizedPulsePercent(level, this.energizedPulsePercent[level - 1]);
        }
        this.config.save();
        this.loadFromConfig();
    }

    private void loadFromConfig() {
        this.reactiveStrengthPercent = this.config.getIntifaceReactiveImpactStrengthPercent();
        this.reactiveDurationMs = this.config.getIntifaceReactiveImpactDurationMs();
        this.peakStrengthPercent = this.config.getIntifacePeakReactiveImpactStrengthPercent();
        this.peakDurationMs = this.config.getIntifacePeakReactiveImpactDurationMs();
        this.cooldownMs = this.config.getIntifaceCooldownMs();
        this.animationBaselineStrengthPercent = this.config.getIntifaceAnimationBaselineStrengthPercent();
        for (int level = 1; level <= 3; ++level) {
            this.energizedBasePercent[level - 1] = this.config.getIntifaceEnergizedBasePercent(level);
            this.energizedPulsePercent[level - 1] = this.config.getIntifaceEnergizedPulsePercent(level);
        }
    }

    private class_4185 resetButton(Runnable action) {
        class_4185 button = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), ignored -> {
            if (action != null) {
                action.run();
            }
            this.updateResetButtons();
        }).method_46434(0, 0, 20, 20).method_46431();
        NonModMenuScreens.setTooltip((class_339)button, "config.needsofnature.tooltip.reset");
        return button;
    }

    private FloatValueSliderWidget percentSlider(int initial, DoubleConsumer listener) {
        return new FloatValueSliderWidget(0, 0, 130, 20, initial, 0.0f, 100.0f, 1.0f, listener, value -> String.format(Locale.ROOT, "%d%%", Math.round(value)));
    }

    private class_342 newNumberField(int width, int initial) {
        class_342 field = new class_342(this.field_22793, 0, 0, width, 20, (class_2561)class_2561.method_43473());
        field.method_1852(String.valueOf(initial));
        field.method_1880(8);
        field.method_1888(true);
        return field;
    }

    private int parseField(class_342 field, int fallback) {
        if (field == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(field.method_1882().trim());
        }
        catch (NumberFormatException e) {
            return fallback;
        }
    }

    private void updateResetButtons() {
        if (this.resetReactiveButton != null) {
            boolean bl = this.resetReactiveButton.field_22763 = this.reactiveStrengthPercent != this.defaults.getIntifaceReactiveImpactStrengthPercent() || this.parseField(this.reactiveDurationField, this.reactiveDurationMs) != this.defaults.getIntifaceReactiveImpactDurationMs();
        }
        if (this.resetPeakButton != null) {
            boolean bl = this.resetPeakButton.field_22763 = this.peakStrengthPercent != this.defaults.getIntifacePeakReactiveImpactStrengthPercent() || this.parseField(this.peakDurationField, this.peakDurationMs) != this.defaults.getIntifacePeakReactiveImpactDurationMs();
        }
        if (this.resetCooldownButton != null) {
            boolean bl = this.resetCooldownButton.field_22763 = this.parseField(this.cooldownField, this.cooldownMs) != this.defaults.getIntifaceCooldownMs();
        }
        if (this.resetAnimationBaselineButton != null) {
            this.resetAnimationBaselineButton.field_22763 = this.animationBaselineStrengthPercent != this.defaults.getIntifaceAnimationBaselineStrengthPercent();
        }
        for (int level = 1; level <= 3; ++level) {
            class_4185 reset = this.resetEnergizedButtons[level - 1];
            if (reset == null) continue;
            reset.field_22763 = this.energizedBasePercent[level - 1] != this.defaults.getIntifaceEnergizedBasePercent(level) || this.energizedPulsePercent[level - 1] != this.defaults.getIntifaceEnergizedPulsePercent(level);
        }
    }
}

