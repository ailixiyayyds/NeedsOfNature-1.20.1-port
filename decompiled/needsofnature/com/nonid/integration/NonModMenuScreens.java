/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1132
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_5348
 *  net.minecraft.class_6382
 *  net.minecraft.class_7919
 *  net.minecraft.server.MinecraftServer
 */
package com.nonid.integration;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.integration.NonModMenuDebugScreens;
import com.nonid.integration.NonModMenuGameplayScreens;
import com.nonid.integration.NonModMenuPlayerScreens;
import com.nonid.integration.NonModMenuSystemSettingsScreens;
import java.time.Duration;
import net.minecraft.class_1132;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5348;
import net.minecraft.class_6382;
import net.minecraft.class_7919;
import net.minecraft.server.MinecraftServer;

final class NonModMenuScreens {
    private NonModMenuScreens() {
    }

    static class_7919 tooltip(String key) {
        return class_7919.method_47407((class_2561)class_2561.method_43471((String)key));
    }

    static class_7919 tooltip(class_2561 text) {
        return class_7919.method_47407((class_2561)text);
    }

    static void setTooltip(class_339 widget, String key) {
        if (widget == null) {
            return;
        }
        widget.method_47400(NonModMenuScreens.tooltip(key));
        widget.method_47402(Duration.ofMillis(500L));
    }

    static void setTooltip(class_339 widget, class_7919 tooltip) {
        if (widget == null) {
            return;
        }
        widget.method_47400(tooltip);
        widget.method_47402(Duration.ofMillis(500L));
    }

    static boolean canEditServerGameplaySettings() {
        class_310 client = class_310.method_1551();
        if (client == null) {
            return true;
        }
        if (client.field_1687 == null) {
            return true;
        }
        return client.method_1576() != null;
    }

    static void syncHostConfigIfIntegratedServer() {
        class_310 client = class_310.method_1551();
        if (client == null) {
            return;
        }
        class_1132 server = client.method_1576();
        if (server == null) {
            return;
        }
        server.execute(() -> NeedsOfNature.syncHostConfigStateToAll((MinecraftServer)server));
    }

    static class_437 createConfigScreen(class_437 parent) {
        return new ConfigCategoryScreen(parent, NeedsOfNature.getConfig());
    }

    private static class ConfigCategoryScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;

        protected ConfigCategoryScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.categories_title"));
            this.parent = parent;
            this.config = config;
        }

        protected void method_25426() {
            int centerX = this.field_22789 / 2;
            int y = this.field_22790 / 2 - 36;
            int gap = 24;
            class_4185 togglesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.toggles"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuSystemSettingsScreens.ToggleSettingsScreen(this, this.config))).method_46434(centerX - 100, y, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)togglesButton, "config.needsofnature.tooltip.category.toggles");
            this.method_37063((class_364)togglesButton);
            class_4185 playerButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.player_preferences"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuPlayerScreens.PlayerPreferencesScreen(this, this.config))).method_46434(centerX - 100, y += gap, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)playerButton, "config.needsofnature.tooltip.category.player_preferences");
            this.method_37063((class_364)playerButton);
            class_4185 settingsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.settings"), button -> class_310.method_1551().method_1507((class_437)new SettingsCategoryScreen(this, this.config))).method_46434(centerX - 100, y += gap, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)settingsButton, "config.needsofnature.tooltip.category.settings");
            this.method_37063((class_364)settingsButton);
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
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

    private static class SettingsCategoryScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;

        protected SettingsCategoryScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.settings_title"));
            this.parent = parent;
            this.config = config;
        }

        protected void method_25426() {
            int centerX = this.field_22789 / 2;
            int y = this.field_22790 / 2 - 58;
            int gap = 24;
            class_4185 gameplayButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.gameplay"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuGameplayScreens.GameplayCategoryScreen(this, this.config))).method_46434(centerX - 100, y, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)gameplayButton, "config.needsofnature.tooltip.category.gameplay");
            this.method_37063((class_364)gameplayButton);
            class_4185 vanillaOverridesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.vanilla_overrides"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuSystemSettingsScreens.VanillaOverrideScreen(this, this.config))).method_46434(centerX - 100, y += gap, 200, 20).method_46431();
            vanillaOverridesButton.field_22763 = this.config.isVanillaOverridesEnabled();
            NonModMenuScreens.setTooltip((class_339)vanillaOverridesButton, vanillaOverridesButton.field_22763 ? "config.needsofnature.tooltip.category.vanilla_overrides" : "config.needsofnature.tooltip.category.disabled_in_toggles");
            this.method_37063((class_364)vanillaOverridesButton);
            class_4185 liquidButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.liquid"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuSystemSettingsScreens.LiquidSettingsScreen(this, this.config))).method_46434(centerX - 100, y += gap, 200, 20).method_46431();
            liquidButton.field_22763 = this.config.isLiquidTankEnabled();
            NonModMenuScreens.setTooltip((class_339)liquidButton, liquidButton.field_22763 ? "config.needsofnature.tooltip.category.liquid" : "config.needsofnature.tooltip.category.disabled_in_toggles");
            this.method_37063((class_364)liquidButton);
            class_4185 pregnancyButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.pregnancy"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuSystemSettingsScreens.PregnancySettingsScreen(this, this.config))).method_46434(centerX - 100, y += gap, 200, 20).method_46431();
            pregnancyButton.field_22763 = this.config.isPregnancyEnabled();
            NonModMenuScreens.setTooltip((class_339)pregnancyButton, pregnancyButton.field_22763 ? "config.needsofnature.tooltip.category.pregnancy" : "config.needsofnature.tooltip.category.disabled_in_toggles");
            this.method_37063((class_364)pregnancyButton);
            class_4185 debugButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.debug_advanced"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuDebugScreens.DebugAdvancedScreen(this, this.config))).method_46434(centerX - 100, y += gap, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)debugButton, "config.needsofnature.tooltip.category.debug_advanced");
            this.method_37063((class_364)debugButton);
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
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

    static final class FlatTextButton
    extends class_339 {
        private final FlatPressAction onPress;

        FlatTextButton(int width, class_2561 text, FlatPressAction onPress) {
            super(0, 0, width, 12, text);
            this.onPress = onPress;
        }

        protected void method_48579(class_332 context, int mouseX, int mouseY, float delta) {
            class_327 renderer = class_310.method_1551().field_1772;
            if (renderer == null) {
                return;
            }
            int rgb = this.field_22763 ? (this.method_25367() ? 0xFFFFFF : 0xD0D0D0) : 0x808080;
            int alpha = Math.max(0, Math.min(255, (int)(this.method_75798() * 255.0f)));
            int color = alpha << 24 | rgb;
            int textX = this.method_46426() + (this.method_25368() - renderer.method_27525((class_5348)this.method_25369())) / 2;
            int textY = this.method_46427() + (this.method_25364() - 8) / 2;
            context.method_27535(renderer, this.method_25369(), textX, textY, color);
        }

        public void method_25348(class_11909 click, boolean doubled) {
            super.method_25348(click, doubled);
            if (this.field_22763 && this.onPress != null) {
                this.onPress.onPress(this);
            }
        }

        protected void method_47399(class_6382 builder) {
            this.method_37021(builder);
        }

        @FunctionalInterface
        static interface FlatPressAction {
            public void onPress(FlatTextButton var1);
        }
    }
}

