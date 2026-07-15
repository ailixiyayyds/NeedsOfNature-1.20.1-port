/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
 *  net.fabricmc.fabric.api.client.screen.v1.Screens
 *  net.minecraft.text.Text
 *  net.minecraft.client.gui.widget.ClickableWidget
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.tooltip.Tooltip
 */
package com.nonid.client;

import com.nonid.NeedsOfNature;
import com.nonid.client.NonWildfireGenderSync;
import com.nonid.integration.NonModMenuExternalScreens;
import java.time.Duration;
import java.util.List;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;

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
            NonWildfireGenderScreenHooks.applyLocks(screen, scaledWidth, scaledHeight, Screens.getButtons((Screen)screen));
        });
    }

    private static void applyLocks(Screen screen, int width, int height, List<ClickableWidget> widgets) {
        ClickableWidget personalizationButton;
        ClickableWidget genderButton;
        boolean lockGender = NonWildfireGenderSync.shouldLockWildfireGenderControls(NeedsOfNature.getConfig());
        boolean lockPersonalization = NonWildfireGenderSync.shouldLockWildfirePersonalizationControls(NeedsOfNature.getConfig());
        if (!lockGender && !lockPersonalization) {
            return;
        }
        ClickableWidget BridgeEnd = genderButton = lockGender ? NonWildfireGenderScreenHooks.findGenderButton(widgets, width, height) : null;
        if (genderButton != null) {
            NonWildfireGenderScreenHooks.disableWithTooltip(genderButton, "config.needsofnature.tooltip.wildfire_gender_controlled_by_non");
        }
        if (lockGender) {
            int x = genderButton != null ? genderButton.getX() : width / 2 - 130;
            int y = (genderButton != null ? genderButton.getY() : height / 2 + 33) + 15 + 3;
            ButtonWidget redirectButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.open_non_player_preferences"), button -> NonModMenuExternalScreens.openPlayerPreferences(screen)).dimensions(x, y, 176, 15).build();
            redirectButton.setTooltip(Tooltip.of((Text)Text.translatable((String)"config.needsofnature.tooltip.open_non_player_preferences")));
            redirectButton.setTooltipDelay(Duration.ofMillis(250L));
            widgets.add((ClickableWidget)redirectButton);
        }
        ClickableWidget Bridge = personalizationButton = lockPersonalization ? NonWildfireGenderScreenHooks.findPersonalizationButton(widgets, width, height) : null;
        if (personalizationButton != null) {
            NonWildfireGenderScreenHooks.disableWithTooltip(personalizationButton, "config.needsofnature.tooltip.wildfire_personalization_controlled_by_non");
        }
    }

    private static ClickableWidget findGenderButton(List<ClickableWidget> widgets, int width, int height) {
        return NonWildfireGenderScreenHooks.findButton(widgets, width / 2 - 130, height / 2 + 33, 80, 15);
    }

    private static ClickableWidget findPersonalizationButton(List<ClickableWidget> widgets, int width, int height) {
        return NonWildfireGenderScreenHooks.findButton(widgets, width / 2 - 36, height / 2 - 63, 157, 20);
    }

    private static ClickableWidget findButton(List<ClickableWidget> widgets, int x, int y, int width, int height) {
        for (ClickableWidget widget : widgets) {
            if (widget.getX() != x || widget.getY() != y || widget.getWidth() != width || widget.getHeight() != height) continue;
            return widget;
        }
        return null;
    }

    private static void disableWithTooltip(ClickableWidget widget, String tooltipKey) {
        widget.active = false;
        widget.setTooltip(Tooltip.of((Text)Text.translatable((String)tooltipKey)));
        widget.setTooltipDelay(Duration.ofMillis(250L));
    }

    private static boolean isWardrobeScreen(Screen screen) {
        for (Class<?> type = screen.getClass(); type != null; type = type.getSuperclass()) {
            if (!WARDROBE_SCREEN_CLASS.equals(type.getName())) continue;
            return true;
        }
        return false;
    }
}

