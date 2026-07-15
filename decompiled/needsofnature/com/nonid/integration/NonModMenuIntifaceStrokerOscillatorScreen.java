/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_339
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
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;

final class NonModMenuIntifaceStrokerOscillatorScreen
extends class_437 {
    private final class_437 parent;
    private final NonConfig config;
    private final NonConfig defaults = new NonConfig();
    private int oscillatorRegularSpeedPercent;
    private int oscillatorPeakSpeedPercent;
    private int strokerMinDistancePercent;
    private int strokerMaxDistancePercent;
    private int strokerRegularMoveDurationMs;
    private int strokerPeakMoveDurationMs;
    private FloatValueSliderWidget oscillatorRegularSlider;
    private class_4185 resetOscillatorRegularButton;
    private FloatValueSliderWidget oscillatorPeakSlider;
    private class_4185 resetOscillatorPeakButton;
    private FloatValueSliderWidget strokerMinSlider;
    private class_4185 resetStrokerMinButton;
    private FloatValueSliderWidget strokerMaxSlider;
    private class_4185 resetStrokerMaxButton;
    private FloatValueSliderWidget strokerRegularSlider;
    private class_4185 resetStrokerRegularButton;
    private FloatValueSliderWidget strokerPeakSlider;
    private class_4185 resetStrokerPeakButton;

    NonModMenuIntifaceStrokerOscillatorScreen(class_437 parent, NonConfig config) {
        super((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_stroker_oscillator_title"));
        this.parent = parent;
        this.config = config;
        this.loadFromConfig();
    }

    protected void method_25426() {
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
        this.method_37063((class_364)settingsList);
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_oscillator")));
        this.oscillatorRegularSlider = this.percentSlider(this.oscillatorRegularSpeedPercent, value -> {
            this.oscillatorRegularSpeedPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetOscillatorRegularButton = this.resetButton(() -> {
            this.oscillatorRegularSpeedPercent = this.defaults.getIntifaceOscillatorRegularSpeedPercent();
            this.oscillatorRegularSlider.setFloatValue(this.oscillatorRegularSpeedPercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_oscillator_regular_speed"), (class_339)this.oscillatorRegularSlider, (class_339)this.resetOscillatorRegularButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_oscillator_regular_speed")));
        this.oscillatorPeakSlider = this.percentSlider(this.oscillatorPeakSpeedPercent, value -> {
            this.oscillatorPeakSpeedPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetOscillatorPeakButton = this.resetButton(() -> {
            this.oscillatorPeakSpeedPercent = this.defaults.getIntifaceOscillatorPeakSpeedPercent();
            this.oscillatorPeakSlider.setFloatValue(this.oscillatorPeakSpeedPercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_oscillator_peak_speed"), (class_339)this.oscillatorPeakSlider, (class_339)this.resetOscillatorPeakButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_oscillator_peak_speed")));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_stroker")));
        this.strokerMaxSlider = this.percentSlider(this.strokerMaxDistancePercent, value -> {
            this.strokerMaxDistancePercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetStrokerMaxButton = this.resetButton(() -> {
            this.strokerMaxDistancePercent = this.defaults.getIntifaceStrokerMaxDistancePercent();
            this.strokerMaxSlider.setFloatValue(this.strokerMaxDistancePercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_stroker_max_distance"), (class_339)this.strokerMaxSlider, (class_339)this.resetStrokerMaxButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_max_distance")));
        this.strokerMinSlider = this.percentSlider(this.strokerMinDistancePercent, value -> {
            this.strokerMinDistancePercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetStrokerMinButton = this.resetButton(() -> {
            this.strokerMinDistancePercent = this.defaults.getIntifaceStrokerMinDistancePercent();
            this.strokerMinSlider.setFloatValue(this.strokerMinDistancePercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_stroker_min_distance"), (class_339)this.strokerMinSlider, (class_339)this.resetStrokerMinButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_min_distance")));
        this.strokerRegularSlider = this.durationSlider(this.strokerRegularMoveDurationMs, value -> {
            this.strokerRegularMoveDurationMs = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetStrokerRegularButton = this.resetButton(() -> {
            this.strokerRegularMoveDurationMs = this.defaults.getIntifaceStrokerRegularMoveDurationMs();
            this.strokerRegularSlider.setFloatValue(this.strokerRegularMoveDurationMs);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_stroker_regular_duration"), (class_339)this.strokerRegularSlider, (class_339)this.resetStrokerRegularButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_regular_duration")));
        this.strokerPeakSlider = this.durationSlider(this.strokerPeakMoveDurationMs, value -> {
            this.strokerPeakMoveDurationMs = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetStrokerPeakButton = this.resetButton(() -> {
            this.strokerPeakMoveDurationMs = this.defaults.getIntifaceStrokerPeakMoveDurationMs();
            this.strokerPeakSlider.setFloatValue(this.strokerPeakMoveDurationMs);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_stroker_peak_duration"), (class_339)this.strokerPeakSlider, (class_339)this.resetStrokerPeakButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_peak_duration")));
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
        this.config.setIntifaceOscillatorRegularSpeedPercent(this.oscillatorRegularSpeedPercent);
        this.config.setIntifaceOscillatorPeakSpeedPercent(this.oscillatorPeakSpeedPercent);
        this.config.setIntifaceStrokerDistancesPercent(this.strokerMinDistancePercent, this.strokerMaxDistancePercent);
        this.config.setIntifaceStrokerRegularMoveDurationMs(this.strokerRegularMoveDurationMs);
        this.config.setIntifaceStrokerPeakMoveDurationMs(this.strokerPeakMoveDurationMs);
        this.config.save();
        this.loadFromConfig();
    }

    private void loadFromConfig() {
        this.oscillatorRegularSpeedPercent = this.config.getIntifaceOscillatorRegularSpeedPercent();
        this.oscillatorPeakSpeedPercent = this.config.getIntifaceOscillatorPeakSpeedPercent();
        this.strokerMinDistancePercent = this.config.getIntifaceStrokerMinDistancePercent();
        this.strokerMaxDistancePercent = this.config.getIntifaceStrokerMaxDistancePercent();
        this.strokerRegularMoveDurationMs = this.config.getIntifaceStrokerRegularMoveDurationMs();
        this.strokerPeakMoveDurationMs = this.config.getIntifaceStrokerPeakMoveDurationMs();
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

    private FloatValueSliderWidget durationSlider(int initial, DoubleConsumer listener) {
        return new FloatValueSliderWidget(0, 0, 130, 20, initial, 0.0f, 5000.0f, 10.0f, listener, value -> String.format(Locale.ROOT, "%dms", Math.round(value)));
    }

    private void updateResetButtons() {
        if (this.resetOscillatorRegularButton != null) {
            boolean bl = this.resetOscillatorRegularButton.field_22763 = this.oscillatorRegularSpeedPercent != this.defaults.getIntifaceOscillatorRegularSpeedPercent();
        }
        if (this.resetOscillatorPeakButton != null) {
            boolean bl = this.resetOscillatorPeakButton.field_22763 = this.oscillatorPeakSpeedPercent != this.defaults.getIntifaceOscillatorPeakSpeedPercent();
        }
        if (this.resetStrokerMinButton != null) {
            boolean bl = this.resetStrokerMinButton.field_22763 = this.strokerMinDistancePercent != this.defaults.getIntifaceStrokerMinDistancePercent();
        }
        if (this.resetStrokerMaxButton != null) {
            boolean bl = this.resetStrokerMaxButton.field_22763 = this.strokerMaxDistancePercent != this.defaults.getIntifaceStrokerMaxDistancePercent();
        }
        if (this.resetStrokerRegularButton != null) {
            boolean bl = this.resetStrokerRegularButton.field_22763 = this.strokerRegularMoveDurationMs != this.defaults.getIntifaceStrokerRegularMoveDurationMs();
        }
        if (this.resetStrokerPeakButton != null) {
            this.resetStrokerPeakButton.field_22763 = this.strokerPeakMoveDurationMs != this.defaults.getIntifaceStrokerPeakMoveDurationMs();
        }
    }
}

