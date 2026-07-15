/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.screen.Screen
 */
package com.nonid.integration;

import com.nonid.NeedsOfNature;
import com.nonid.integration.NonModMenuPlayerScreens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public final class NonModMenuExternalScreens {
    private NonModMenuExternalScreens() {
    }

    public static void openPlayerPreferences(Screen parent) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.setScreen((Screen)new NonModMenuPlayerScreens.PlayerPreferencesScreen(parent, NeedsOfNature.getConfig()));
        }
    }
}

