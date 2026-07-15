/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.screen.Screen
 */
package com.nonid.integration;

import com.nonid.NonConfig;
import com.nonid.client.intiface.NonIntifaceDependencyManager;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;

final class NonModMenuIntifaceInstallScreen
extends Screen {
    private final Screen parent;
    private final NonConfig config;
    private final boolean enableAfterInstall;
    private ButtonWidget downloadButton;

    NonModMenuIntifaceInstallScreen(Screen parent, NonConfig config, boolean enableAfterInstall) {
        super((Text)Text.translatable((String)"config.needsofnature.intiface.libs.install_title"));
        this.parent = parent;
        this.config = config;
        this.enableAfterInstall = enableAfterInstall;
    }

    protected void init() {
        int centerX = this.width / 2;
        this.downloadButton = ButtonWidget.builder((Text)Text.translatable((String)(NonIntifaceDependencyManager.generatedBundleOutdated() ? "config.needsofnature.intiface.libs.redownload" : "config.needsofnature.intiface.libs.download")), button -> this.startInstall()).dimensions(centerX - 155, this.height - 52, 150, 20).build();
        this.addDrawableChild(this.downloadButton);
        ButtonWidget cancelButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.cancel"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX + 5, this.height - 52, 150, 20).build();
        this.addDrawableChild(cancelButton);
        ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX - 75, this.height - 28, 150, 20).build();
        this.addDrawableChild(doneButton);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 28, 0xFFFFFF);
        int y = 58;
        this.drawCentered(context, (Text)Text.translatable((String)"config.needsofnature.intiface.libs.install_line_1"), y);
        this.drawCentered(context, (Text)Text.translatable((String)"config.needsofnature.intiface.libs.install_line_2", (Object[])new Object[]{NonIntifaceDependencyManager.totalSizeMbText()}), y += 14);
        this.drawCentered(context, (Text)Text.translatable((String)"config.needsofnature.intiface.libs.install_line_3"), y += 14);
        this.drawCentered(context, NonIntifaceDependencyManager.statusText(), y += 22);
        if (this.downloadButton != null) {
            this.downloadButton.setMessage((Text)Text.translatable((String)(NonIntifaceDependencyManager.generatedBundleOutdated() ? "config.needsofnature.intiface.libs.redownload" : "config.needsofnature.intiface.libs.download")));
            this.downloadButton.active = !NonIntifaceDependencyManager.installing() && !NonIntifaceDependencyManager.restartRequired() && NonIntifaceDependencyManager.installRequired();
        }
    }

    public void close() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    private void startInstall() {
        if (this.downloadButton != null) {
            this.downloadButton.active = false;
        }
        NonIntifaceDependencyManager.installAsync(() -> {
            if (this.enableAfterInstall && NonIntifaceDependencyManager.restartRequired()) {
                this.config.setIntifaceEnabled(true);
                this.config.save();
            }
        });
    }

    private void drawCentered(DrawContext context, Text text, int y) {
        context.drawCenteredTextWithShadow(this.textRenderer, text, this.width / 2, y, -2039584);
    }
}

