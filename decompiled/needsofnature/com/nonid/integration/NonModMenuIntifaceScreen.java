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
import com.nonid.client.intiface.NonIntifaceBridge;
import com.nonid.client.intiface.NonIntifaceDependencyManager;
import com.nonid.integration.NonModMenuIntifaceInstallScreen;
import com.nonid.integration.NonModMenuIntifaceTuningScreen;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;

final class NonModMenuIntifaceScreen
extends class_437 {
    private final class_437 parent;
    private final NonConfig config;
    private final NonConfig defaults = new NonConfig();
    private boolean enabled;
    private boolean autoConnectOnWorldJoin;
    private boolean stopOnDisconnect;
    private String serverUrl;
    private class_4185 enabledButton;
    private class_4185 resetEnabledButton;
    private class_4185 autoConnectButton;
    private class_4185 resetAutoConnectButton;
    private class_4185 stopOnDisconnectButton;
    private class_4185 resetStopOnDisconnectButton;
    private class_342 serverUrlField;
    private class_4185 resetServerUrlButton;
    private class_4185 statusButton;
    private class_4185 connectButton;
    private class_4185 scanButton;
    private class_4185 testButton;

    NonModMenuIntifaceScreen(class_437 parent, NonConfig config) {
        super((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_title"));
        this.parent = parent;
        this.config = config;
        this.loadFromConfig();
    }

    protected void method_25426() {
        if (this.serverUrlField != null) {
            this.serverUrl = this.serverUrlField.method_1882();
        }
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
        this.method_37063((class_364)settingsList);
        int resetW = 20;
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_connection")));
        boolean librariesUsable = NonIntifaceDependencyManager.librariesUsable();
        this.enabledButton = class_4185.method_46430((class_2561)this.toggleText(this.enabled), button -> {
            if (!this.enabled && !librariesUsable) {
                class_310.method_1551().method_1507((class_437)new NonModMenuIntifaceInstallScreen(this, this.config, true));
                return;
            }
            this.enabled = !this.enabled;
            button.method_25355(this.toggleText(this.enabled));
            this.updateResetButtons();
        }).method_46434(0, 0, 100, 20).method_46431();
        NonModMenuScreens.setTooltip((class_339)this.enabledButton, "config.needsofnature.tooltip.intiface_enabled");
        this.resetEnabledButton = this.resetButton(() -> {
            this.enabled = this.defaults.isIntifaceEnabled();
            this.enabledButton.method_25355(this.toggleText(this.enabled));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_enabled"), (class_339)this.enabledButton, (class_339)this.resetEnabledButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_enabled")));
        if (!librariesUsable) {
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_libraries")));
            this.statusButton = class_4185.method_46430((class_2561)NonIntifaceDependencyManager.statusText(), button -> {}).method_46434(0, 0, 220, 20).method_46431();
            this.statusButton.field_22763 = false;
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)this.statusButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_libraries_status")));
            class_4185 installButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)(NonIntifaceDependencyManager.restartRequired() ? "config.needsofnature.intiface.libs.restart_required" : (NonIntifaceDependencyManager.generatedBundleOutdated() ? "config.needsofnature.intiface.libs.redownload" : "config.needsofnature.intiface.libs.install"))), button -> class_310.method_1551().method_1507((class_437)new NonModMenuIntifaceInstallScreen(this, this.config, false))).method_46434(0, 0, 220, 20).method_46431();
            installButton.field_22763 = !NonIntifaceDependencyManager.installing() && !NonIntifaceDependencyManager.restartRequired() && NonIntifaceDependencyManager.installRequired();
            NonModMenuScreens.setTooltip((class_339)installButton, "config.needsofnature.tooltip.intiface_libraries_install");
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)installButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_libraries_install")));
            this.updateResetButtons();
            this.addDoneButton();
            return;
        }
        this.serverUrlField = new class_342(this.field_22793, 0, 0, 180, 20, (class_2561)class_2561.method_43473());
        this.serverUrlField.method_1880(128);
        this.serverUrlField.method_1852(this.serverUrl);
        this.serverUrlField.method_1863(value -> this.updateResetButtons());
        this.resetServerUrlButton = this.resetButton(() -> this.serverUrlField.method_1852(this.defaults.getIntifaceServerUrl()));
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_server_url"), (class_339)this.serverUrlField, (class_339)this.resetServerUrlButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_server_url")));
        this.autoConnectButton = class_4185.method_46430((class_2561)this.toggleText(this.autoConnectOnWorldJoin), button -> {
            this.autoConnectOnWorldJoin = !this.autoConnectOnWorldJoin;
            button.method_25355(this.toggleText(this.autoConnectOnWorldJoin));
            this.updateResetButtons();
        }).method_46434(0, 0, 100, 20).method_46431();
        NonModMenuScreens.setTooltip((class_339)this.autoConnectButton, "config.needsofnature.tooltip.intiface_auto_connect");
        this.resetAutoConnectButton = this.resetButton(() -> {
            this.autoConnectOnWorldJoin = this.defaults.isIntifaceAutoConnectOnWorldJoin();
            this.autoConnectButton.method_25355(this.toggleText(this.autoConnectOnWorldJoin));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_auto_connect"), (class_339)this.autoConnectButton, (class_339)this.resetAutoConnectButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_auto_connect")));
        this.stopOnDisconnectButton = class_4185.method_46430((class_2561)this.toggleText(this.stopOnDisconnect), button -> {
            this.stopOnDisconnect = !this.stopOnDisconnect;
            button.method_25355(this.toggleText(this.stopOnDisconnect));
            this.updateResetButtons();
        }).method_46434(0, 0, 100, 20).method_46431();
        NonModMenuScreens.setTooltip((class_339)this.stopOnDisconnectButton, "config.needsofnature.tooltip.intiface_stop_on_disconnect");
        this.resetStopOnDisconnectButton = this.resetButton(() -> {
            this.stopOnDisconnect = this.defaults.isIntifaceStopOnDisconnect();
            this.stopOnDisconnectButton.method_25355(this.toggleText(this.stopOnDisconnect));
        });
        settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_stop_on_disconnect"), (class_339)this.stopOnDisconnectButton, (class_339)this.resetStopOnDisconnectButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stop_on_disconnect")));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_controls")));
        this.statusButton = class_4185.method_46430((class_2561)NonIntifaceBridge.statusText(this.config), button -> {}).method_46434(0, 0, 220, 20).method_46431();
        this.statusButton.field_22763 = false;
        settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)this.statusButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_status")));
        this.connectButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_connect"), button -> {
            if (NonIntifaceBridge.isConnected()) {
                NonIntifaceBridge.disconnect();
            } else {
                this.saveValues();
                NonIntifaceBridge.connect(this.config);
            }
        }).method_46434(0, 0, 106, 20).method_46431();
        this.scanButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_scan"), button -> {
            this.saveValues();
            NonIntifaceBridge.startScanning(this.config);
        }).method_46434(0, 0, 106, 20).method_46431();
        settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_connect_scan"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_connect_scan"), new class_339[]{this.connectButton, this.scanButton}));
        this.testButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_test"), button -> {
            this.saveValues();
            NonIntifaceBridge.testPulse(this.config, false);
        }).method_46434(0, 0, 216, 20).method_46431();
        settingsList.addEntryRow(SettingsList.RowEntry.groupedField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_test_pulse"), NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_test_pulse"), new class_339[]{this.testButton}));
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_toy_settings")));
        class_4185 tuningButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_toy_settings"), button -> {
            this.saveValues();
            class_310.method_1551().method_1507((class_437)new NonModMenuIntifaceTuningScreen(this, this.config));
        }).method_46434(0, 0, 220, 20).method_46431();
        NonModMenuScreens.setTooltip((class_339)tuningButton, "config.needsofnature.tooltip.intiface_toy_settings");
        settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)tuningButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_toy_settings")));
        this.updateResetButtons();
        this.updateConnectionButtons();
        this.addDoneButton();
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        this.updateResetButtons();
        if (this.statusButton != null) {
            this.statusButton.method_25355(NonIntifaceDependencyManager.librariesUsable() ? NonIntifaceBridge.statusText(this.config) : NonIntifaceDependencyManager.statusText());
        }
        this.updateConnectionButtons();
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
        this.serverUrl = this.serverUrlField == null ? this.serverUrl : this.serverUrlField.method_1882();
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
        int centerX = this.field_22789 / 2;
        class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> this.saveAndClose()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
        NonModMenuScreens.setTooltip((class_339)doneButton, "config.needsofnature.tooltip.done_save");
        this.method_37063((class_364)doneButton);
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

    private void updateResetButtons() {
        if (this.resetEnabledButton != null) {
            boolean bl = this.resetEnabledButton.field_22763 = this.enabled != this.defaults.isIntifaceEnabled();
        }
        if (this.resetAutoConnectButton != null) {
            boolean bl = this.resetAutoConnectButton.field_22763 = this.autoConnectOnWorldJoin != this.defaults.isIntifaceAutoConnectOnWorldJoin();
        }
        if (this.resetStopOnDisconnectButton != null) {
            boolean bl = this.resetStopOnDisconnectButton.field_22763 = this.stopOnDisconnect != this.defaults.isIntifaceStopOnDisconnect();
        }
        if (this.resetServerUrlButton != null && this.serverUrlField != null) {
            this.resetServerUrlButton.field_22763 = !this.serverUrlField.method_1882().equals(this.defaults.getIntifaceServerUrl());
        }
    }

    private void updateConnectionButtons() {
        boolean connected = NonIntifaceBridge.isConnected();
        if (this.connectButton != null) {
            this.connectButton.method_25355((class_2561)class_2561.method_43471((String)(connected ? "config.needsofnature.intiface_disconnect" : "config.needsofnature.intiface_connect")));
        }
        if (this.scanButton != null) {
            this.scanButton.field_22763 = connected;
        }
        if (this.testButton != null) {
            this.testButton.field_22763 = connected;
        }
    }

    private class_2561 toggleText(boolean value) {
        return class_2561.method_43471((String)(value ? "options.on" : "options.off"));
    }
}

