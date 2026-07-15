/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_4286
 *  net.minecraft.class_437
 *  net.minecraft.class_5250
 *  net.minecraft.class_5348
 *  net.minecraft.class_5481
 */
package com.nonid.client;

import com.nonid.client.NonPackStartupWarnings;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_4286;
import net.minecraft.class_437;
import net.minecraft.class_5250;
import net.minecraft.class_5348;
import net.minecraft.class_5481;

public final class NonPackWarningScreen
extends class_437 {
    private static final long PROCEED_DELAY_MS = 10000L;
    private static final int CONTENT_WIDTH = 340;
    private final class_437 parent;
    private final NonPackStartupWarnings.Warning warning;
    private final long openedAtMs;
    private class_4185 proceedButton;
    private class_4286 dontShowAgainCheckbox;

    public NonPackWarningScreen(class_437 parent, NonPackStartupWarnings.Warning warning) {
        super(warning.title());
        this.parent = parent;
        this.warning = warning;
        this.openedAtMs = System.currentTimeMillis();
    }

    protected void method_25426() {
        int centerX = this.field_22789 / 2;
        int buttonY = Math.min(this.field_22790 - 38, 246);
        int checkboxY = buttonY - 28;
        this.dontShowAgainCheckbox = class_4286.method_54787((class_2561)class_2561.method_43471((String)"screen.needsofnature.warning.dont_show_again"), (class_327)this.field_22793).method_54789(centerX - 170, checkboxY).method_61131(340).method_54788();
        this.method_37063((class_364)this.dontShowAgainCheckbox);
        this.proceedButton = class_4185.method_46430((class_2561)this.proceedText(), button -> this.proceed()).method_46434(centerX - 165, buttonY, 160, 20).method_46431();
        this.method_37063((class_364)this.proceedButton);
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43471((String)"screen.needsofnature.warning.close_game"), button -> {
            class_310 client = this.field_22787;
            if (client != null) {
                client.method_1592();
            }
        }).method_46434(centerX + 5, buttonY, 160, 20).method_46431());
        this.updateProceedButton();
    }

    public void method_25393() {
        this.updateProceedButton();
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float deltaTicks) {
        context.method_25294(0, 0, this.field_22789, this.field_22790, -267382768);
        int centerX = this.field_22789 / 2;
        int top = 34;
        context.method_27534(this.field_22793, this.field_22785, centerX, top, -43691);
        int boxX = centerX - 170 - 12;
        int boxY = top + 22;
        int boxWidth = 364;
        int boxHeight = 118;
        context.method_25294(boxX, boxY, boxX + boxWidth, boxY + boxHeight, -1442840576);
        context.method_25294(boxX, boxY, boxX + boxWidth, boxY + 1, 0x66FFFFFF);
        context.method_25294(boxX, boxY + boxHeight - 1, boxX + boxWidth, boxY + boxHeight, 0x66000000);
        List lines = this.field_22793.method_1728((class_5348)this.warning.body(), 340);
        int y = boxY + 14;
        for (class_5481 line : lines) {
            if (y > boxY + boxHeight - 14) break;
            context.method_35720(this.field_22793, line, centerX - 170, y, -1644826);
            y += 11;
        }
        class_5250 packText = class_2561.method_43469((String)"screen.needsofnature.warning.pack", (Object[])new Object[]{this.warning.packName()});
        int packTextWidth = this.field_22793.method_27525((class_5348)packText);
        context.method_27535(this.field_22793, (class_2561)packText, Math.max(4, this.field_22789 - packTextWidth - 6), this.field_22790 - 12, -5592406);
        super.method_25394(context, mouseX, mouseY, deltaTicks);
    }

    public boolean method_25422() {
        return false;
    }

    public void method_25419() {
    }

    private void proceed() {
        class_310 client;
        if (!this.canProceed()) {
            return;
        }
        if (this.dontShowAgainCheckbox != null && this.dontShowAgainCheckbox.method_20372()) {
            NonPackStartupWarnings.acknowledge(this.warning.id());
        }
        if ((client = this.field_22787) != null) {
            client.method_1507(this.parent);
        }
    }

    private void updateProceedButton() {
        if (this.proceedButton == null) {
            return;
        }
        this.proceedButton.field_22763 = this.canProceed();
        this.proceedButton.method_25355(this.proceedText());
    }

    private class_2561 proceedText() {
        long remainingMs = Math.max(0L, 10000L - this.elapsedMs());
        if (remainingMs <= 0L) {
            return class_2561.method_43471((String)"screen.needsofnature.warning.proceed");
        }
        long remainingSeconds = Math.max(1L, (remainingMs + 999L) / 1000L);
        return class_2561.method_43469((String)"screen.needsofnature.warning.proceed_countdown", (Object[])new Object[]{remainingSeconds});
    }

    private boolean canProceed() {
        return this.elapsedMs() >= 10000L;
    }

    private long elapsedMs() {
        return System.currentTimeMillis() - this.openedAtMs;
    }
}

