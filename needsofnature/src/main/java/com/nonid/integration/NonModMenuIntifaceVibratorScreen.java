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
import com.nonid.integration.FloatValueSliderWidget;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import java.util.Locale;
import java.util.function.DoubleConsumer;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;

final class NonModMenuIntifaceVibratorScreen
extends Screen {
    private final Screen parent;
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
    private TextFieldWidget reactiveDurationField;
    private ButtonWidget resetReactiveButton;
    private FloatValueSliderWidget peakStrengthSlider;
    private TextFieldWidget peakDurationField;
    private ButtonWidget resetPeakButton;
    private TextFieldWidget cooldownField;
    private ButtonWidget resetCooldownButton;
    private FloatValueSliderWidget animationBaselineSlider;
    private ButtonWidget resetAnimationBaselineButton;
    private final FloatValueSliderWidget[] energizedBaseSliders = new FloatValueSliderWidget[3];
    private final FloatValueSliderWidget[] energizedPulseSliders = new FloatValueSliderWidget[3];
    private final ButtonWidget[] resetEnergizedButtons = new ButtonWidget[3];

    NonModMenuIntifaceVibratorScreen(Screen parent, NonConfig config) {
        super((Text)Text.translatable((String)"config.needsofnature.intiface_vibrator_title"));
        this.parent = parent;
        this.config = config;
        this.loadFromConfig();
    }

    protected void init() {
        if (this.reactiveDurationField != null) {
            this.reactiveDurationMs = this.parseField(this.reactiveDurationField, this.reactiveDurationMs);
            this.peakDurationMs = this.parseField(this.peakDurationField, this.peakDurationMs);
            this.cooldownMs = this.parseField(this.cooldownField, this.cooldownMs);
        }
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.height - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
        this.addDrawableChild(settingsList);
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_reactiveimpact")));
        this.reactiveStrengthSlider = this.percentSlider(this.reactiveStrengthPercent, value -> {
            this.reactiveStrengthPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.reactiveDurationField = this.newNumberField(70, this.reactiveDurationMs);
        this.reactiveDurationField.setChangedListener(ignored -> this.updateResetButtons());
        this.resetReactiveButton = this.resetButton(() -> {
            this.reactiveStrengthPercent = this.defaults.getIntifaceReactiveImpactStrengthPercent();
            this.reactiveDurationMs = this.defaults.getIntifaceReactiveImpactDurationMs();
            this.reactiveStrengthSlider.setFloatValue(this.reactiveStrengthPercent);
            this.reactiveDurationField.setText(String.valueOf(this.reactiveDurationMs));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_reactiveimpact"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_reactiveimpact"), new ClickableWidget[]{this.reactiveStrengthSlider, this.reactiveDurationField, this.resetReactiveButton}));
        this.peakStrengthSlider = this.percentSlider(this.peakStrengthPercent, value -> {
            this.peakStrengthPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.peakDurationField = this.newNumberField(70, this.peakDurationMs);
        this.peakDurationField.setChangedListener(ignored -> this.updateResetButtons());
        this.resetPeakButton = this.resetButton(() -> {
            this.peakStrengthPercent = this.defaults.getIntifacePeakReactiveImpactStrengthPercent();
            this.peakDurationMs = this.defaults.getIntifacePeakReactiveImpactDurationMs();
            this.peakStrengthSlider.setFloatValue(this.peakStrengthPercent);
            this.peakDurationField.setText(String.valueOf(this.peakDurationMs));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_peak_reactiveimpact"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_peak_reactiveimpact"), new ClickableWidget[]{this.peakStrengthSlider, this.peakDurationField, this.resetPeakButton}));
        this.cooldownField = this.newNumberField(70, this.cooldownMs);
        this.cooldownField.setChangedListener(ignored -> this.updateResetButtons());
        this.resetCooldownButton = this.resetButton(() -> {
            this.cooldownMs = this.defaults.getIntifaceCooldownMs();
            this.cooldownField.setText(String.valueOf(this.cooldownMs));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_cooldown"), (ClickableWidget)this.cooldownField, (ClickableWidget)this.resetCooldownButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_cooldown")));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_baseline")));
        this.animationBaselineSlider = this.percentSlider(this.animationBaselineStrengthPercent, value -> {
            this.animationBaselineStrengthPercent = (int)Math.round(value);
            this.updateResetButtons();
        });
        this.resetAnimationBaselineButton = this.resetButton(() -> {
            this.animationBaselineStrengthPercent = this.defaults.getIntifaceAnimationBaselineStrengthPercent();
            this.animationBaselineSlider.setFloatValue(this.animationBaselineStrengthPercent);
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_animation_baseline"), (ClickableWidget)this.animationBaselineSlider, (ClickableWidget)this.resetAnimationBaselineButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_animation_baseline")));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_energized")));
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
            settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_energized_level", (Object[])new Object[]{level}), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_energized"), new ClickableWidget[]{this.energizedBaseSliders[index], this.energizedPulseSliders[index], this.resetEnergizedButtons[index]}));
        }
        this.updateResetButtons();
        int centerX = this.width / 2;
        ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.saveAndClose()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)doneButton, "config.needsofnature.tooltip.done_save");
        this.addDrawableChild(doneButton);
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

    private TextFieldWidget newNumberField(int width, int initial) {
        TextFieldWidget field = new TextFieldWidget(this.textRenderer, 0, 0, width, 20, (Text)Text.empty());
        field.setText(String.valueOf(initial));
        field.setMaxLength(8);
        field.setEditable(true);
        return field;
    }

    private int parseField(TextFieldWidget field, int fallback) {
        if (field == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(field.getText().trim());
        }
        catch (NumberFormatException e) {
            return fallback;
        }
    }

    private void updateResetButtons() {
        if (this.resetReactiveButton != null) {
            boolean bl = this.resetReactiveButton.active = this.reactiveStrengthPercent != this.defaults.getIntifaceReactiveImpactStrengthPercent() || this.parseField(this.reactiveDurationField, this.reactiveDurationMs) != this.defaults.getIntifaceReactiveImpactDurationMs();
        }
        if (this.resetPeakButton != null) {
            boolean bl = this.resetPeakButton.active = this.peakStrengthPercent != this.defaults.getIntifacePeakReactiveImpactStrengthPercent() || this.parseField(this.peakDurationField, this.peakDurationMs) != this.defaults.getIntifacePeakReactiveImpactDurationMs();
        }
        if (this.resetCooldownButton != null) {
            boolean bl = this.resetCooldownButton.active = this.parseField(this.cooldownField, this.cooldownMs) != this.defaults.getIntifaceCooldownMs();
        }
        if (this.resetAnimationBaselineButton != null) {
            this.resetAnimationBaselineButton.active = this.animationBaselineStrengthPercent != this.defaults.getIntifaceAnimationBaselineStrengthPercent();
        }
        for (int level = 1; level <= 3; ++level) {
            ButtonWidget reset = this.resetEnergizedButtons[level - 1];
            if (reset == null) continue;
            reset.active = this.energizedBasePercent[level - 1] != this.defaults.getIntifaceEnergizedBasePercent(level) || this.energizedPulsePercent[level - 1] != this.defaults.getIntifaceEnergizedPulsePercent(level);
        }
    }
}

