/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.widget.ClickableWidget
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.screen.Screen
 */
package com.nonid.integration;

import com.nonid.NonConfig;
import com.nonid.integration.FloatValueSliderWidget;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import java.util.Locale;
import java.util.function.DoubleConsumer;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;

final class NonModMenuIntifaceStrokerOscillatorScreen
extends Screen {
    private final Screen parent;
    private final NonConfig config;
    private final NonConfig defaults = new NonConfig();
    private int oscillatorRegularSpeedPercent;
    private int oscillatorPeakSpeedPercent;
    private int strokerMinDistancePercent;
    private int strokerMaxDistancePercent;
    private int strokerRegularMoveDurationMs;
    private int strokerPeakMoveDurationMs;
    private FloatValueSliderWidget oscillatorRegularSlider;
    private ButtonWidget resetOscillatorRegularButton;
    private FloatValueSliderWidget oscillatorPeakSlider;
    private ButtonWidget resetOscillatorPeakButton;
    private FloatValueSliderWidget strokerMinSlider;
    private ButtonWidget resetStrokerMinButton;
    private FloatValueSliderWidget strokerMaxSlider;
    private ButtonWidget resetStrokerMaxButton;
    private FloatValueSliderWidget strokerRegularSlider;
    private ButtonWidget resetStrokerRegularButton;
    private FloatValueSliderWidget strokerPeakSlider;
    private ButtonWidget resetStrokerPeakButton;

    NonModMenuIntifaceStrokerOscillatorScreen(Screen parent, NonConfig config) {
        super((Text)Text.translatable((String)"config.needsofnature.intiface_stroker_oscillator_title"));
        this.parent = parent;
        this.config = config;
        this.loadFromConfig();
    }

    protected void init() {
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.height - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
        this.addDrawableChild((Element)settingsList);
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_oscillator")));
        this.oscillatorRegularSlider = this.percentSlider(this.oscillatorRegularSpeedPercent, value -> {
            this.oscillatorRegularSpeedPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetOscillatorRegularButton = this.resetButton(() -> {
            this.oscillatorRegularSpeedPercent = this.defaults.getIntifaceOscillatorRegularSpeedPercent();
            this.oscillatorRegularSlider.setFloatValue(this.oscillatorRegularSpeedPercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_oscillator_regular_speed"), (ClickableWidget)this.oscillatorRegularSlider, (ClickableWidget)this.resetOscillatorRegularButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_oscillator_regular_speed")));
        this.oscillatorPeakSlider = this.percentSlider(this.oscillatorPeakSpeedPercent, value -> {
            this.oscillatorPeakSpeedPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetOscillatorPeakButton = this.resetButton(() -> {
            this.oscillatorPeakSpeedPercent = this.defaults.getIntifaceOscillatorPeakSpeedPercent();
            this.oscillatorPeakSlider.setFloatValue(this.oscillatorPeakSpeedPercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_oscillator_peak_speed"), (ClickableWidget)this.oscillatorPeakSlider, (ClickableWidget)this.resetOscillatorPeakButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_oscillator_peak_speed")));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_stroker")));
        this.strokerMaxSlider = this.percentSlider(this.strokerMaxDistancePercent, value -> {
            this.strokerMaxDistancePercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetStrokerMaxButton = this.resetButton(() -> {
            this.strokerMaxDistancePercent = this.defaults.getIntifaceStrokerMaxDistancePercent();
            this.strokerMaxSlider.setFloatValue(this.strokerMaxDistancePercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_stroker_max_distance"), (ClickableWidget)this.strokerMaxSlider, (ClickableWidget)this.resetStrokerMaxButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_max_distance")));
        this.strokerMinSlider = this.percentSlider(this.strokerMinDistancePercent, value -> {
            this.strokerMinDistancePercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetStrokerMinButton = this.resetButton(() -> {
            this.strokerMinDistancePercent = this.defaults.getIntifaceStrokerMinDistancePercent();
            this.strokerMinSlider.setFloatValue(this.strokerMinDistancePercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_stroker_min_distance"), (ClickableWidget)this.strokerMinSlider, (ClickableWidget)this.resetStrokerMinButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_min_distance")));
        this.strokerRegularSlider = this.durationSlider(this.strokerRegularMoveDurationMs, value -> {
            this.strokerRegularMoveDurationMs = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetStrokerRegularButton = this.resetButton(() -> {
            this.strokerRegularMoveDurationMs = this.defaults.getIntifaceStrokerRegularMoveDurationMs();
            this.strokerRegularSlider.setFloatValue(this.strokerRegularMoveDurationMs);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_stroker_regular_duration"), (ClickableWidget)this.strokerRegularSlider, (ClickableWidget)this.resetStrokerRegularButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_regular_duration")));
        this.strokerPeakSlider = this.durationSlider(this.strokerPeakMoveDurationMs, value -> {
            this.strokerPeakMoveDurationMs = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetStrokerPeakButton = this.resetButton(() -> {
            this.strokerPeakMoveDurationMs = this.defaults.getIntifaceStrokerPeakMoveDurationMs();
            this.strokerPeakSlider.setFloatValue(this.strokerPeakMoveDurationMs);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_stroker_peak_duration"), (ClickableWidget)this.strokerPeakSlider, (ClickableWidget)this.resetStrokerPeakButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_peak_duration")));
        this.updateResetButtons();
        int centerX = this.width / 2;
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
        this.saveValues();
        MinecraftClient.getInstance().setScreen(this.parent);
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

    private ButtonWidget resetButton(Runnable action) {
        ButtonWidget button = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), ignored -> {
            if (action != null) {
                action.run();
            }
            this.updateResetButtons();
        }).dimensions(0, 0, 20, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)button, "config.needsofnature.tooltip.reset");
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
            boolean bl = this.resetOscillatorRegularButton.active = this.oscillatorRegularSpeedPercent != this.defaults.getIntifaceOscillatorRegularSpeedPercent();
        }
        if (this.resetOscillatorPeakButton != null) {
            boolean bl = this.resetOscillatorPeakButton.active = this.oscillatorPeakSpeedPercent != this.defaults.getIntifaceOscillatorPeakSpeedPercent();
        }
        if (this.resetStrokerMinButton != null) {
            boolean bl = this.resetStrokerMinButton.active = this.strokerMinDistancePercent != this.defaults.getIntifaceStrokerMinDistancePercent();
        }
        if (this.resetStrokerMaxButton != null) {
            boolean bl = this.resetStrokerMaxButton.active = this.strokerMaxDistancePercent != this.defaults.getIntifaceStrokerMaxDistancePercent();
        }
        if (this.resetStrokerRegularButton != null) {
            boolean bl = this.resetStrokerRegularButton.active = this.strokerRegularMoveDurationMs != this.defaults.getIntifaceStrokerRegularMoveDurationMs();
        }
        if (this.resetStrokerPeakButton != null) {
            this.resetStrokerPeakButton.active = this.strokerPeakMoveDurationMs != this.defaults.getIntifaceStrokerPeakMoveDurationMs();
        }
    }
}

