/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.font.TextRenderer
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.widget.CheckboxWidget
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.text.MutableText
 *  net.minecraft.text.StringVisitable
 *  net.minecraft.text.OrderedText
 */
package com.nonid.client;

import com.nonid.client.NonPackStartupWarnings;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.OrderedText;

public final class NonPackWarningScreen
extends Screen {
    private static final long PROCEED_DELAY_MS = 10000L;
    private static final int CONTENT_WIDTH = 340;
    private final Screen parent;
    private final NonPackStartupWarnings.Warning warning;
    private final long openedAtMs;
    private ButtonWidget proceedButton;
    private CheckboxWidget dontShowAgainCheckbox;

    public NonPackWarningScreen(Screen parent, NonPackStartupWarnings.Warning warning) {
        super(warning.title());
        this.parent = parent;
        this.warning = warning;
        this.openedAtMs = System.currentTimeMillis();
    }

    protected void init() {
        int centerX = this.width / 2;
        int buttonY = Math.min(this.height - 38, 246);
        int checkboxY = buttonY - 28;
        this.dontShowAgainCheckbox = CheckboxWidget.builder((Text)Text.translatable((String)"screen.needsofnature.warning.dont_show_again"), (TextRenderer)this.textRenderer).pos(centerX - 170, checkboxY).maxWidth(340).build();
        this.addDrawableChild((Element)this.dontShowAgainCheckbox);
        this.proceedButton = ButtonWidget.builder((Text)this.proceedText(), button -> this.proceed()).dimensions(centerX - 165, buttonY, 160, 20).build();
        this.addDrawableChild((Element)this.proceedButton);
        this.addDrawableChild((Element)ButtonWidget.builder((Text)Text.translatable((String)"screen.needsofnature.warning.close_game"), button -> {
            MinecraftClient client = this.client;
            if (client != null) {
                client.scheduleStop();
            }
        }).dimensions(centerX + 5, buttonY, 160, 20).build());
        this.updateProceedButton();
    }

    public void tick() {
        this.updateProceedButton();
    }

    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.fill(0, 0, this.width, this.height, -267382768);
        int centerX = this.width / 2;
        int top = 34;
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, centerX, top, -43691);
        int boxX = centerX - 170 - 12;
        int boxY = top + 22;
        int boxWidth = 364;
        int boxHeight = 118;
        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, -1442840576);
        context.fill(boxX, boxY, boxX + boxWidth, boxY + 1, 0x66FFFFFF);
        context.fill(boxX, boxY + boxHeight - 1, boxX + boxWidth, boxY + boxHeight, 0x66000000);
        List lines = this.textRenderer.wrapLines((StringVisitable)this.warning.body(), 340);
        int y = boxY + 14;
        for (OrderedText line : lines) {
            if (y > boxY + boxHeight - 14) break;
            context.drawTextWithShadow(this.textRenderer, line, centerX - 170, y, -1644826);
            y += 11;
        }
        MutableText packText = Text.translatable((String)"screen.needsofnature.warning.pack", (Object[])new Object[]{this.warning.packName()});
        int packTextWidth = this.textRenderer.getWidth((StringVisitable)packText);
        context.drawTextWithShadow(this.textRenderer, (Text)packText, Math.max(4, this.width - packTextWidth - 6), this.height - 12, -5592406);
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public void close() {
    }

    private void proceed() {
        MinecraftClient client;
        if (!this.canProceed()) {
            return;
        }
        if (this.dontShowAgainCheckbox != null && this.dontShowAgainCheckbox.isChecked()) {
            NonPackStartupWarnings.acknowledge(this.warning.id());
        }
        if ((client = this.client) != null) {
            client.setScreen(this.parent);
        }
    }

    private void updateProceedButton() {
        if (this.proceedButton == null) {
            return;
        }
        this.proceedButton.active = this.canProceed();
        this.proceedButton.setMessage(this.proceedText());
    }

    private Text proceedText() {
        long remainingMs = Math.max(0L, 10000L - this.elapsedMs());
        if (remainingMs <= 0L) {
            return Text.translatable((String)"screen.needsofnature.warning.proceed");
        }
        long remainingSeconds = Math.max(1L, (remainingMs + 999L) / 1000L);
        return Text.translatable((String)"screen.needsofnature.warning.proceed_countdown", (Object[])new Object[]{remainingSeconds});
    }

    private boolean canProceed() {
        return this.elapsedMs() >= 10000L;
    }

    private long elapsedMs() {
        return System.currentTimeMillis() - this.openedAtMs;
    }
}

