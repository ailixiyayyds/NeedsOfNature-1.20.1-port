/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
 *  net.fabricmc.fabric.api.client.screen.v1.Screens
 *  net.minecraft.class_2561
 *  net.minecraft.class_339
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_7919
 */
package com.nonid.client;

import com.nonid.NeedsOfNature;
import com.nonid.client.NonWildfireGenderSync;
import com.nonid.integration.NonModMenuExternalScreens;
import java.time.Duration;
import java.util.List;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.class_2561;
import net.minecraft.class_339;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_7919;

public final class NonWildfireGenderScreenHooks {
    private static final String WARDROBE_SCREEN_CLASS = "com.wildfire.gui.screen.WardrobeBrowserScreen";
    private static final int GENDER_BUTTON_WIDTH = 80;
    private static final int GENDER_BUTTON_HEIGHT = 15;
    private static final int PERSONALIZATION_BUTTON_WIDTH = 157;
    private static final int PERSONALIZATION_BUTTON_HEIGHT = 20;
    private static final int REDIRECT_BUTTON_WIDTH = 176;
    private static final int REDIRECT_BUTTON_VERTICAL_GAP = 3;

    private NonWildfireGenderScreenHooks() {
    }

    public static void register() {
        if (!NonWildfireGenderSync.isAvailable()) {
            return;
        }
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!NonWildfireGenderScreenHooks.isWardrobeScreen(screen)) {
                return;
            }
            NonWildfireGenderScreenHooks.applyLocks(screen, scaledWidth, scaledHeight, Screens.getButtons((class_437)screen));
        });
    }

    private static void applyLocks(class_437 screen, int width, int height, List<class_339> widgets) {
        class_339 personalizationButton;
        class_339 genderButton;
        boolean lockGender = NonWildfireGenderSync.shouldLockWildfireGenderControls(NeedsOfNature.getConfig());
        boolean lockPersonalization = NonWildfireGenderSync.shouldLockWildfirePersonalizationControls(NeedsOfNature.getConfig());
        if (!lockGender && !lockPersonalization) {
            return;
        }
        class_339 class_3392 = genderButton = lockGender ? NonWildfireGenderScreenHooks.findGenderButton(widgets, width, height) : null;
        if (genderButton != null) {
            NonWildfireGenderScreenHooks.disableWithTooltip(genderButton, "config.needsofnature.tooltip.wildfire_gender_controlled_by_non");
        }
        if (lockGender) {
            int x = genderButton != null ? genderButton.method_46426() : width / 2 - 130;
            int y = (genderButton != null ? genderButton.method_46427() : height / 2 + 33) + 15 + 3;
            class_4185 redirectButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.open_non_player_preferences"), button -> NonModMenuExternalScreens.openPlayerPreferences(screen)).method_46434(x, y, 176, 15).method_46431();
            redirectButton.method_47400(class_7919.method_47407((class_2561)class_2561.method_43471((String)"config.needsofnature.tooltip.open_non_player_preferences")));
            redirectButton.method_47402(Duration.ofMillis(250L));
            widgets.add((class_339)redirectButton);
        }
        class_339 class_3393 = personalizationButton = lockPersonalization ? NonWildfireGenderScreenHooks.findPersonalizationButton(widgets, width, height) : null;
        if (personalizationButton != null) {
            NonWildfireGenderScreenHooks.disableWithTooltip(personalizationButton, "config.needsofnature.tooltip.wildfire_personalization_controlled_by_non");
        }
    }

    private static class_339 findGenderButton(List<class_339> widgets, int width, int height) {
        return NonWildfireGenderScreenHooks.findButton(widgets, width / 2 - 130, height / 2 + 33, 80, 15);
    }

    private static class_339 findPersonalizationButton(List<class_339> widgets, int width, int height) {
        return NonWildfireGenderScreenHooks.findButton(widgets, width / 2 - 36, height / 2 - 63, 157, 20);
    }

    private static class_339 findButton(List<class_339> widgets, int x, int y, int width, int height) {
        for (class_339 widget : widgets) {
            if (widget.method_46426() != x || widget.method_46427() != y || widget.method_25368() != width || widget.method_25364() != height) continue;
            return widget;
        }
        return null;
    }

    private static void disableWithTooltip(class_339 widget, String tooltipKey) {
        widget.field_22763 = false;
        widget.method_47400(class_7919.method_47407((class_2561)class_2561.method_43471((String)tooltipKey)));
        widget.method_47402(Duration.ofMillis(250L));
    }

    private static boolean isWardrobeScreen(class_437 screen) {
        for (Class<?> type = screen.getClass(); type != null; type = type.getSuperclass()) {
            if (!WARDROBE_SCREEN_CLASS.equals(type.getName())) continue;
            return true;
        }
        return false;
    }
}

