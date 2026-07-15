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
import com.nonid.integration.NonModMenuIntifaceStrokerOscillatorScreen;
import com.nonid.integration.NonModMenuIntifaceVibratorScreen;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;

final class NonModMenuIntifaceTuningScreen
extends class_437 {
    private final class_437 parent;
    private final NonConfig config;

    NonModMenuIntifaceTuningScreen(class_437 parent, NonConfig config) {
        super((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_toy_title"));
        this.parent = parent;
        this.config = config;
    }

    protected void method_25426() {
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
        this.method_37063((class_364)settingsList);
        settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.intiface_toy_settings")));
        class_4185 vibratorButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_vibrator_settings"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuIntifaceVibratorScreen(this, this.config))).method_46434(0, 0, 220, 20).method_46431();
        NonModMenuScreens.setTooltip((class_339)vibratorButton, "config.needsofnature.tooltip.intiface_vibrator_settings");
        settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)vibratorButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_vibrator_settings")));
        class_4185 strokerOscillatorButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface_stroker_oscillator_settings"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuIntifaceStrokerOscillatorScreen(this, this.config))).method_46434(0, 0, 220, 20).method_46431();
        NonModMenuScreens.setTooltip((class_339)strokerOscillatorButton, "config.needsofnature.tooltip.intiface_stroker_oscillator_settings");
        settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)strokerOscillatorButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.intiface_stroker_oscillator_settings")));
        int centerX = this.field_22789 / 2;
        class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> this.method_25419()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
        NonModMenuScreens.setTooltip((class_339)doneButton, "config.needsofnature.tooltip.done_save");
        this.method_37063((class_364)doneButton);
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        super.method_25394(context, mouseX, mouseY, delta);
        context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
    }

    public void method_25419() {
        class_310.method_1551().method_1507(this.parent);
    }
}

