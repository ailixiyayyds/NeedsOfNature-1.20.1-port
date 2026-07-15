/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.integrated.IntegratedServer
 *  net.minecraft.client.gui.Click
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.font.TextRenderer
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.widget.ClickableWidget
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.text.StringVisitable
 *  net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
 *  net.minecraft.client.gui.tooltip.Tooltip
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
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.gui.Click;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.StringVisitable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.server.MinecraftServer;

final class NonModMenuScreens {
    private NonModMenuScreens() {
    }

    static Tooltip tooltip(String key) {
        return Tooltip.of((Text)Text.translatable((String)key));
    }

    static Tooltip tooltip(Text text) {
        return Tooltip.of((Text)text);
    }

    static void setTooltip(ClickableWidget widget, String key) {
        if (widget == null) {
            return;
        }
        widget.setTooltip(NonModMenuScreens.tooltip(key));
        widget.setTooltipDelay(Duration.ofMillis(500L));
    }

    static void setTooltip(ClickableWidget widget, Tooltip tooltip) {
        if (widget == null) {
            return;
        }
        widget.setTooltip(tooltip);
        widget.setTooltipDelay(Duration.ofMillis(500L));
    }

    static boolean canEditServerGameplaySettings() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return true;
        }
        if (client.world == null) {
            return true;
        }
        return client.getServer() != null;
    }

    static void syncHostConfigIfIntegratedServer() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }
        IntegratedServer server = client.getServer();
        if (server == null) {
            return;
        }
        server.execute(() -> NeedsOfNature.syncHostConfigStateToAll((MinecraftServer)server));
    }

    static Screen createConfigScreen(Screen parent) {
        return new ConfigCategoryScreen(parent, NeedsOfNature.getConfig());
    }

    private static class ConfigCategoryScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;

        protected ConfigCategoryScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.categories_title"));
            this.parent = parent;
            this.config = config;
        }

        protected void init() {
            int centerX = this.width / 2;
            int y = this.height / 2 - 36;
            int gap = 24;
            ButtonWidget togglesButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.category.toggles"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuSystemSettingsScreens.ToggleSettingsScreen(this, this.config))).dimensions(centerX - 100, y, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)togglesButton, "config.needsofnature.tooltip.category.toggles");
            this.addDrawableChild((Element)togglesButton);
            ButtonWidget playerButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.category.player_preferences"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuPlayerScreens.PlayerPreferencesScreen(this, this.config))).dimensions(centerX - 100, y += gap, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)playerButton, "config.needsofnature.tooltip.category.player_preferences");
            this.addDrawableChild((Element)playerButton);
            ButtonWidget settingsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.category.settings"), button -> MinecraftClient.getInstance().setScreen((Screen)new SettingsCategoryScreen(this, this.config))).dimensions(centerX - 100, y += gap, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)settingsButton, "config.needsofnature.tooltip.category.settings");
            this.addDrawableChild((Element)settingsButton);
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX - 100, this.height - 28, 200, 20).build();
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

    private static class SettingsCategoryScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;

        protected SettingsCategoryScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.settings_title"));
            this.parent = parent;
            this.config = config;
        }

        protected void init() {
            int centerX = this.width / 2;
            int y = this.height / 2 - 58;
            int gap = 24;
            ButtonWidget gameplayButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.category.gameplay"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuGameplayScreens.GameplayCategoryScreen(this, this.config))).dimensions(centerX - 100, y, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)gameplayButton, "config.needsofnature.tooltip.category.gameplay");
            this.addDrawableChild((Element)gameplayButton);
            ButtonWidget vanillaOverridesButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.category.vanilla_overrides"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuSystemSettingsScreens.VanillaOverrideScreen(this, this.config))).dimensions(centerX - 100, y += gap, 200, 20).build();
            vanillaOverridesButton.active = this.config.isVanillaOverridesEnabled();
            NonModMenuScreens.setTooltip((ClickableWidget)vanillaOverridesButton, vanillaOverridesButton.active ? "config.needsofnature.tooltip.category.vanilla_overrides" : "config.needsofnature.tooltip.category.disabled_in_toggles");
            this.addDrawableChild((Element)vanillaOverridesButton);
            ButtonWidget liquidButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.category.liquid"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuSystemSettingsScreens.LiquidSettingsScreen(this, this.config))).dimensions(centerX - 100, y += gap, 200, 20).build();
            liquidButton.active = this.config.isLiquidTankEnabled();
            NonModMenuScreens.setTooltip((ClickableWidget)liquidButton, liquidButton.active ? "config.needsofnature.tooltip.category.liquid" : "config.needsofnature.tooltip.category.disabled_in_toggles");
            this.addDrawableChild((Element)liquidButton);
            ButtonWidget pregnancyButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.category.pregnancy"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuSystemSettingsScreens.PregnancySettingsScreen(this, this.config))).dimensions(centerX - 100, y += gap, 200, 20).build();
            pregnancyButton.active = this.config.isPregnancyEnabled();
            NonModMenuScreens.setTooltip((ClickableWidget)pregnancyButton, pregnancyButton.active ? "config.needsofnature.tooltip.category.pregnancy" : "config.needsofnature.tooltip.category.disabled_in_toggles");
            this.addDrawableChild((Element)pregnancyButton);
            ButtonWidget debugButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.category.debug_advanced"), button -> MinecraftClient.getInstance().setScreen((Screen)new NonModMenuDebugScreens.DebugAdvancedScreen(this, this.config))).dimensions(centerX - 100, y += gap, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)debugButton, "config.needsofnature.tooltip.category.debug_advanced");
            this.addDrawableChild((Element)debugButton);
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX - 100, this.height - 28, 200, 20).build();
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

    static final class FlatTextButton
    extends ClickableWidget {
        private final FlatPressAction onPress;

        FlatTextButton(int width, Text text, FlatPressAction onPress) {
            super(0, 0, width, 12, text);
            this.onPress = onPress;
        }

        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            if (renderer == null) {
                return;
            }
            int rgb = this.active ? (this.isSelected() ? 0xFFFFFF : 0xD0D0D0) : 0x808080;
            int alpha = Math.max(0, Math.min(255, (int)(this.getAlpha() * 255.0f)));
            int color = alpha << 24 | rgb;
            int textX = this.getX() + (this.getWidth() - renderer.getWidth((StringVisitable)this.getMessage())) / 2;
            int textY = this.getY() + (this.getHeight() - 8) / 2;
            context.drawTextWithShadow(renderer, this.getMessage(), textX, textY, color);
        }

        public void onClick(Click click, boolean doubled) {
            super.onClick(click, doubled);
            if (this.active && this.onPress != null) {
                this.onPress.onPress(this);
            }
        }

        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }

        @FunctionalInterface
        static interface FlatPressAction {
            public void onPress(FlatTextButton var1);
        }
    }
}

