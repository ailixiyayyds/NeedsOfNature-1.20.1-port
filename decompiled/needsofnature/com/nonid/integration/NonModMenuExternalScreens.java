/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 */
package com.nonid.integration;

import com.nonid.NeedsOfNature;
import com.nonid.integration.NonModMenuPlayerScreens;
import net.minecraft.class_310;
import net.minecraft.class_437;

public final class NonModMenuExternalScreens {
    private NonModMenuExternalScreens() {
    }

    public static void openPlayerPreferences(class_437 parent) {
        class_310 client = class_310.method_1551();
        if (client != null) {
            client.method_1507((class_437)new NonModMenuPlayerScreens.PlayerPreferencesScreen(parent, NeedsOfNature.getConfig()));
        }
    }
}

