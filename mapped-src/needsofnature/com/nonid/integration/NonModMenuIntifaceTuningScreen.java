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
import com.nonid.integration.NonModMenuIntifaceStrokerOscillatorScreen;
import com.nonid.integration.NonModMenuIntifaceVibratorScreen;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;

final class NonModMenuIntifaceTuningScreen
extends Screen {
    private final Screen parent;
    private final NonConfig config;

    NonModMenuIntifaceTuningScreen(Screen parent, NonConfig config) {
        super((Text)Text.translatable((String)"config.needsofnature.intiface_toy_title"));
        this.parent = parent;
        this.config = config;
    }

    protected void init() {
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.height - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
        this.addDrawableChild((Element)settingsList);
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_toy_settings")));
        ButtonWidget vibratorButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.intiface_vibrator_settings"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuIntifaceVibratorScreen(this, this.config))).dimensions(0, 0, 220, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)vibratorButton, "config.needsofnature.tooltip.intiface_vibrator_settings");
        settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.textRenderer, (ClickableWidget)vibratorButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_vibrator_settings")));
        ButtonWidget strokerOscillatorButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.intiface_stroker_oscillator_settings"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuIntifaceStrokerOscillatorScreen(this, this.config))).dimensions(0, 0, 220, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)strokerOscillatorButton, "config.needsofnature.tooltip.intiface_stroker_oscillator_settings");
        settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.textRenderer, (ClickableWidget)strokerOscillatorButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_oscillator_settings")));
        int centerX = this.width / 2;
        ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.close()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)doneButton, "config.needsofnature.tooltip.done_save");
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

