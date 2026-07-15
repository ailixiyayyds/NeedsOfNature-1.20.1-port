/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.minecraft.util.Formatting
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.text.MutableText
 *  net.minecraft.network.packet.CustomPayload
 */
package com.nonid.client;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.client.NonWildfireGenderSync;
import com.nonid.network.SetPlayerGenderC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.network.packet.CustomPayload;

public final class NonGenderSelectionScreen
extends Screen {
    private final int allowedMask;
    private final int currentMask;
    private final boolean permanent;
    private int panelX;
    private int panelY;
    private int panelW;
    private int panelH;

    public NonGenderSelectionScreen(int allowedMask, int currentMask, boolean permanent) {
        super((Text)Text.translatable((String)"screen.needsofnature.gender_selection.title"));
        this.allowedMask = (allowedMask & 7) == 0 ? 7 : allowedMask & 7;
        this.currentMask = currentMask & 3;
        this.permanent = permanent;
    }

    protected void init() {
        this.panelW = Math.min(340, Math.max(260, this.width - 40));
        this.panelH = 176;
        this.panelX = (this.width - this.panelW) / 2;
        this.panelY = Math.max(34, (this.height - this.panelH) / 2);
        int buttonW = this.panelW - 56;
        int buttonX = this.panelX + 28;
        int y = this.panelY + 72;
        this.addGenderButton(buttonX, y, buttonW, NonConfig.PlayerGenderSelection.MALE);
        this.addGenderButton(buttonX, y + 26, buttonW, NonConfig.PlayerGenderSelection.FEMALE);
        this.addGenderButton(buttonX, y + 52, buttonW, NonConfig.PlayerGenderSelection.BOTH);
    }

    private void addGenderButton(int x, int y, int w, NonConfig.PlayerGenderSelection selection) {
        ButtonWidget button = ButtonWidget.builder((Text)this.genderText(selection), ignored -> this.choose(selection)).dimensions(x, y, w, 22).build();
        button.active = this.isAllowed(selection.mask());
        this.addDrawableChild((Element)button);
    }

    private void choose(NonConfig.PlayerGenderSelection selection) {
        NonConfig config;
        if (!this.isAllowed(selection.mask())) {
            return;
        }
        if (ClientPlayNetworking.canSend(SetPlayerGenderC2SPayload.ID)) {
            ClientPlayNetworking.send((CustomPayload)new SetPlayerGenderC2SPayload(selection.mask(), true));
        }
        if ((config = NeedsOfNature.getConfig()) != null) {
            config.setPlayerGenderSelection(selection);
            config.save();
            NonWildfireGenderSync.syncFromMask(config, selection.mask());
        }
        MinecraftClient.getInstance().setScreen(null);
    }

    private boolean isAllowed(int mask) {
        int bit = switch (mask & 3) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            default -> 0;
        };
        return bit != 0 && (this.allowedMask & bit) != 0;
    }

    private Text genderText(NonConfig.PlayerGenderSelection selection) {
        MutableText base;
        switch (selection) {
            default: {
                throw new MatchException(null, null);
            }
            case MALE: {
                MutableText class_52502 = Text.translatable((String)"screen.needsofnature.gender_selection.option.male").copy().formatted(Formatting.AQUA);
                break;
            }
            case FEMALE: {
                MutableText class_52502 = Text.translatable((String)"screen.needsofnature.gender_selection.option.female").copy().formatted(Formatting.LIGHT_PURPLE);
                break;
            }
            case BOTH: {
                MutableText class_52502 = base = Text.translatable((String)"screen.needsofnature.gender_selection.option.both").copy().formatted(Formatting.GOLD);
            }
        }
        if (selection.mask() == this.currentMask) {
            return Text.translatable((String)"screen.needsofnature.gender_selection.current", (Object[])new Object[]{base});
        }
        return base;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawPanel(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawTitle(context);
    }

    public void renderInGameBackground(DrawContext context) {
    }

    private void drawTitle(DrawContext context) {
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.panelY + 18, -1);
        context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)(this.permanent ? "screen.needsofnature.gender_selection.permanent" : "screen.needsofnature.gender_selection.changeable")), this.width / 2, this.panelY + 40, this.permanent ? -10630 : -5708033);
        context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"screen.needsofnature.gender_selection.subtitle"), this.width / 2, this.panelY + 54, -5197648);
    }

    private void drawPanel(DrawContext context) {
        context.fillGradient(0, 0, this.width, this.height, -871888629, -585427182);
        context.fill(this.panelX - 2, this.panelY - 2, this.panelX + this.panelW + 2, this.panelY + this.panelH + 2, -1442840576);
        context.fill(this.panelX, this.panelY, this.panelX + this.panelW, this.panelY + this.panelH, -300345324);
        context.fill(this.panelX, this.panelY, this.panelX + this.panelW, this.panelY + 2, -12166);
        context.fill(this.panelX, this.panelY + this.panelH - 2, this.panelX + this.panelW, this.panelY + this.panelH, -8760784);
        context.fill(this.panelX, this.panelY, this.panelX + 2, this.panelY + this.panelH, -8760784);
        context.fill(this.panelX + this.panelW - 2, this.panelY, this.panelX + this.panelW, this.panelY + this.panelH, -8760784);
        context.fill(this.panelX + 18, this.panelY + 64, this.panelX + this.panelW - 18, this.panelY + 65, 1716207668);
        context.fill(this.panelX + 18, this.panelY + this.panelH - 28, this.panelX + this.panelW - 18, this.panelY + this.panelH - 27, 1716207668);
    }

    public void close() {
    }
}

