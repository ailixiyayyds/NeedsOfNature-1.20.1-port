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
import com.nonid.client.intiface.NonIntifaceBridge;
import com.nonid.client.intiface.NonIntifaceDependencyManager;
import com.nonid.integration.NonModMenuIntifaceInstallScreen;
import com.nonid.integration.NonModMenuIntifaceTuningScreen;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;

final class NonModMenuIntifaceScreen
extends Screen {
    private final Screen parent;
    private final NonConfig config;
    private final NonConfig defaults = new NonConfig();
    private boolean enabled;
    private boolean autoConnectOnWorldJoin;
    private boolean stopOnDisconnect;
    private String serverUrl;
    private ButtonWidget enabledButton;
    private ButtonWidget resetEnabledButton;
    private ButtonWidget autoConnectButton;
    private ButtonWidget resetAutoConnectButton;
    private ButtonWidget stopOnDisconnectButton;
    private ButtonWidget resetStopOnDisconnectButton;
    private TextFieldWidget serverUrlField;
    private ButtonWidget resetServerUrlButton;
    private ButtonWidget statusButton;
    private ButtonWidget connectButton;
    private ButtonWidget scanButton;
    private ButtonWidget testButton;

    NonModMenuIntifaceScreen(Screen parent, NonConfig config) {
        super((Text)Text.translatable((String)"config.needsofnature.intiface_title"));
        this.parent = parent;
        this.config = config;
        this.loadFromConfig();
    }

    protected void init() {
        if (this.serverUrlField != null) {
            this.serverUrl = this.serverUrlField.getText();
        }
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.height - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
        this.addDrawableChild((Element)settingsList);
        int resetW = 20;
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_connection")));
        boolean librariesUsable = NonIntifaceDependencyManager.librariesUsable();
        this.enabledButton = ButtonWidget.builder((Text)this.toggleText(this.enabled), button -> {
            if (!this.enabled && !librariesUsable) {
                MinecraftClient.getInstance().setScreen((Screen)new NonModMenuIntifaceInstallScreen(this, this.config, true));
                return;
            }
            this.enabled = !this.enabled;
            button.setMessage(this.toggleText(this.enabled));
            this.updateResetButtons();
        }).dimensions(0, 0, 100, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)this.enabledButton, "config.needsofnature.tooltip.intiface_enabled");
        this.resetEnabledButton = this.resetButton(() -> {
            this.enabled = this.defaults.isIntifaceEnabled();
            this.enabledButton.setMessage(this.toggleText(this.enabled));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_enabled"), (ClickableWidget)this.enabledButton, (ClickableWidget)this.resetEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_enabled")));
        if (!librariesUsable) {
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_libraries")));
            this.statusButton = ButtonWidget.builder((Text)NonIntifaceDependencyManager.statusText(), button -> {}).dimensions(0, 0, 220, 20).build();
            this.statusButton.active = false;
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.textRenderer, (ClickableWidget)this.statusButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_libraries_status")));
            ButtonWidget installButton = ButtonWidget.builder((Text)Text.translatable((String)(NonIntifaceDependencyManager.restartRequired() ? "config.needsofnature.intiface.libs.restart_required" : (NonIntifaceDependencyManager.generatedBundleOutdated() ? "config.needsofnature.intiface.libs.redownload" : "config.needsofnature.intiface.libs.install"))), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuIntifaceInstallScreen(this, this.config, false))).dimensions(0, 0, 220, 20).build();
            installButton.active = !NonIntifaceDependencyManager.installing() && !NonIntifaceDependencyManager.restartRequired() && NonIntifaceDependencyManager.installRequired();
            NonModMenuScreens.setTooltip((ClickableWidget)installButton, "config.needsofnature.tooltip.intiface_libraries_install");
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.textRenderer, (ClickableWidget)installButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_libraries_install")));
            this.updateResetButtons();
            this.addDoneButton();
            return;
        }
        this.serverUrlField = new TextFieldWidget(this.textRenderer, 0, 0, 180, 20, (Text)Text.empty());
        this.serverUrlField.setMaxLength(128);
        this.serverUrlField.setText(this.serverUrl);
        this.serverUrlField.setChangedListener(value -> this.updateResetButtons());
        this.resetServerUrlButton = this.resetButton(() -> this.serverUrlField.setText(this.defaults.getIntifaceServerUrl()));
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_server_url"), (ClickableWidget)this.serverUrlField, (ClickableWidget)this.resetServerUrlButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_server_url")));
        this.autoConnectButton = ButtonWidget.builder((Text)this.toggleText(this.autoConnectOnWorldJoin), button -> {
            this.autoConnectOnWorldJoin = !this.autoConnectOnWorldJoin;
            button.setMessage(this.toggleText(this.autoConnectOnWorldJoin));
            this.updateResetButtons();
        }).dimensions(0, 0, 100, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)this.autoConnectButton, "config.needsofnature.tooltip.intiface_auto_connect");
        this.resetAutoConnectButton = this.resetButton(() -> {
            this.autoConnectOnWorldJoin = this.defaults.isIntifaceAutoConnectOnWorldJoin();
            this.autoConnectButton.setMessage(this.toggleText(this.autoConnectOnWorldJoin));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_auto_connect"), (ClickableWidget)this.autoConnectButton, (ClickableWidget)this.resetAutoConnectButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_auto_connect")));
        this.stopOnDisconnectButton = ButtonWidget.builder((Text)this.toggleText(this.stopOnDisconnect), button -> {
            this.stopOnDisconnect = !this.stopOnDisconnect;
            button.setMessage(this.toggleText(this.stopOnDisconnect));
            this.updateResetButtons();
        }).dimensions(0, 0, 100, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)this.stopOnDisconnectButton, "config.needsofnature.tooltip.intiface_stop_on_disconnect");
        this.resetStopOnDisconnectButton = this.resetButton(() -> {
            this.stopOnDisconnect = this.defaults.isIntifaceStopOnDisconnect();
            this.stopOnDisconnectButton.setMessage(this.toggleText(this.stopOnDisconnect));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_stop_on_disconnect"), (ClickableWidget)this.stopOnDisconnectButton, (ClickableWidget)this.resetStopOnDisconnectButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stop_on_disconnect")));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_controls")));
        this.statusButton = ButtonWidget.builder((Text)NonIntifaceBridge.statusText(this.config), button -> {}).dimensions(0, 0, 220, 20).build();
        this.statusButton.active = false;
        settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.textRenderer, (ClickableWidget)this.statusButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_status")));
        this.connectButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.intiface_connect"), button -> {
            if (NonIntifaceBridge.isConnected()) {
                NonIntifaceBridge.disconnect();
            } else {
                this.saveValues();
                NonIntifaceBridge.connect(this.config);
            }
        }).dimensions(0, 0, 106, 20).build();
        this.scanButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.intiface_scan"), button -> {
            this.saveValues();
            NonIntifaceBridge.startScanning(this.config);
        }).dimensions(0, 0, 106, 20).build();
        settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_connect_scan"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_connect_scan"), new ClickableWidget[]{this.connectButton, this.scanButton}));
        this.testButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.intiface_test"), button -> {
            this.saveValues();
            NonIntifaceBridge.testPulse(this.config, false);
        }).dimensions(0, 0, 216, 20).build();
        settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.intiface_test_pulse"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_test_pulse"), new ClickableWidget[]{this.testButton}));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.intiface_toy_settings")));
        ButtonWidget tuningButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.intiface_toy_settings"), button -> {
            this.saveValues();
            MinecraftClient.getInstance().setScreen((Screen)new NonModMenuIntifaceTuningScreen(this, this.config));
        }).dimensions(0, 0, 220, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)tuningButton, "config.needsofnature.tooltip.intiface_toy_settings");
        settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.textRenderer, (ClickableWidget)tuningButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_toy_settings")));
        this.updateResetButtons();
        this.updateConnectionButtons();
        this.addDoneButton();
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.updateResetButtons();
        if (this.statusButton != null) {
            this.statusButton.setMessage(NonIntifaceDependencyManager.librariesUsable() ? NonIntifaceBridge.statusText(this.config) : NonIntifaceDependencyManager.statusText());
        }
        this.updateConnectionButtons();
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
        this.serverUrl = this.serverUrlField == null ? this.serverUrl : this.serverUrlField.getText();
        this.config.setIntifaceEnabled(this.enabled);
        this.config.setIntifaceAutoConnectOnWorldJoin(this.autoConnectOnWorldJoin);
        this.config.setIntifaceStopOnDisconnect(this.stopOnDisconnect);
        this.config.setIntifaceServerUrl(this.serverUrl);
        this.config.save();
        this.loadFromConfig();
        if (!this.enabled) {
            NonIntifaceBridge.stopAll();
            NonIntifaceBridge.disconnect();
        }
    }

    private void loadFromConfig() {
        this.enabled = this.config.isIntifaceEnabled();
        this.autoConnectOnWorldJoin = this.config.isIntifaceAutoConnectOnWorldJoin();
        this.stopOnDisconnect = this.config.isIntifaceStopOnDisconnect();
        this.serverUrl = this.config.getIntifaceServerUrl();
    }

    private void addDoneButton() {
        int centerX = this.width / 2;
        ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.saveAndClose()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
        NonModMenuScreens.setTooltip((ClickableWidget)doneButton, "config.needsofnature.tooltip.done_save");
        this.addDrawableChild((Element)doneButton);
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

    private void updateResetButtons() {
        if (this.resetEnabledButton != null) {
            boolean bl = this.resetEnabledButton.active = this.enabled != this.defaults.isIntifaceEnabled();
        }
        if (this.resetAutoConnectButton != null) {
            boolean bl = this.resetAutoConnectButton.active = this.autoConnectOnWorldJoin != this.defaults.isIntifaceAutoConnectOnWorldJoin();
        }
        if (this.resetStopOnDisconnectButton != null) {
            boolean bl = this.resetStopOnDisconnectButton.active = this.stopOnDisconnect != this.defaults.isIntifaceStopOnDisconnect();
        }
        if (this.resetServerUrlButton != null && this.serverUrlField != null) {
            this.resetServerUrlButton.active = !this.serverUrlField.getText().equals(this.defaults.getIntifaceServerUrl());
        }
    }

    private void updateConnectionButtons() {
        boolean connected = NonIntifaceBridge.isConnected();
        if (this.connectButton != null) {
            this.connectButton.setMessage((Text)Text.translatable((String)(connected ? "config.needsofnature.intiface_disconnect" : "config.needsofnature.intiface_connect")));
        }
        if (this.scanButton != null) {
            this.scanButton.active = connected;
        }
        if (this.testButton != null) {
            this.testButton.active = connected;
        }
    }

    private Text toggleText(boolean value) {
        return Text.translatable((String)(value ? "options.on" : "options.off"));
    }
}

