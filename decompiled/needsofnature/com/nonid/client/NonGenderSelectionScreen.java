/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.minecraft.class_124
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_5250
 *  net.minecraft.class_8710
 */
package com.nonid.client;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.client.NonWildfireGenderSync;
import com.nonid.network.SetPlayerGenderC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5250;
import net.minecraft.class_8710;

public final class NonGenderSelectionScreen
extends class_437 {
    private final int allowedMask;
    private final int currentMask;
    private final boolean permanent;
    private int panelX;
    private int panelY;
    private int panelW;
    private int panelH;

    public NonGenderSelectionScreen(int allowedMask, int currentMask, boolean permanent) {
        super((class_2561)class_2561.method_43471((String)"screen.needsofnature.gender_selection.title"));
        this.allowedMask = (allowedMask & 7) == 0 ? 7 : allowedMask & 7;
        this.currentMask = currentMask & 3;
        this.permanent = permanent;
    }

    protected void method_25426() {
        this.panelW = Math.min(340, Math.max(260, this.field_22789 - 40));
        this.panelH = 176;
        this.panelX = (this.field_22789 - this.panelW) / 2;
        this.panelY = Math.max(34, (this.field_22790 - this.panelH) / 2);
        int buttonW = this.panelW - 56;
        int buttonX = this.panelX + 28;
        int y = this.panelY + 72;
        this.addGenderButton(buttonX, y, buttonW, NonConfig.PlayerGenderSelection.MALE);
        this.addGenderButton(buttonX, y + 26, buttonW, NonConfig.PlayerGenderSelection.FEMALE);
        this.addGenderButton(buttonX, y + 52, buttonW, NonConfig.PlayerGenderSelection.BOTH);
    }

    private void addGenderButton(int x, int y, int w, NonConfig.PlayerGenderSelection selection) {
        class_4185 button = class_4185.method_46430((class_2561)this.genderText(selection), ignored -> this.choose(selection)).method_46434(x, y, w, 22).method_46431();
        button.field_22763 = this.isAllowed(selection.mask());
        this.method_37063((class_364)button);
    }

    private void choose(NonConfig.PlayerGenderSelection selection) {
        NonConfig config;
        if (!this.isAllowed(selection.mask())) {
            return;
        }
        if (ClientPlayNetworking.canSend(SetPlayerGenderC2SPayload.ID)) {
            ClientPlayNetworking.send((class_8710)new SetPlayerGenderC2SPayload(selection.mask(), true));
        }
        if ((config = NeedsOfNature.getConfig()) != null) {
            config.setPlayerGenderSelection(selection);
            config.save();
            NonWildfireGenderSync.syncFromMask(config, selection.mask());
        }
        class_310.method_1551().method_1507(null);
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

    private class_2561 genderText(NonConfig.PlayerGenderSelection selection) {
        class_5250 base;
        switch (selection) {
            default: {
                throw new MatchException(null, null);
            }
            case MALE: {
                class_5250 class_52502 = class_2561.method_43471((String)"screen.needsofnature.gender_selection.option.male").method_27661().method_27692(class_124.field_1075);
                break;
            }
            case FEMALE: {
                class_5250 class_52502 = class_2561.method_43471((String)"screen.needsofnature.gender_selection.option.female").method_27661().method_27692(class_124.field_1076);
                break;
            }
            case BOTH: {
                class_5250 class_52502 = base = class_2561.method_43471((String)"screen.needsofnature.gender_selection.option.both").method_27661().method_27692(class_124.field_1065);
            }
        }
        if (selection.mask() == this.currentMask) {
            return class_2561.method_43469((String)"screen.needsofnature.gender_selection.current", (Object[])new Object[]{base});
        }
        return base;
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        this.drawPanel(context);
        super.method_25394(context, mouseX, mouseY, delta);
        this.drawTitle(context);
    }

    public void method_52752(class_332 context) {
    }

    private void drawTitle(class_332 context) {
        context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, this.panelY + 18, -1);
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)(this.permanent ? "screen.needsofnature.gender_selection.permanent" : "screen.needsofnature.gender_selection.changeable")), this.field_22789 / 2, this.panelY + 40, this.permanent ? -10630 : -5708033);
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.needsofnature.gender_selection.subtitle"), this.field_22789 / 2, this.panelY + 54, -5197648);
    }

    private void drawPanel(class_332 context) {
        context.method_25296(0, 0, this.field_22789, this.field_22790, -871888629, -585427182);
        context.method_25294(this.panelX - 2, this.panelY - 2, this.panelX + this.panelW + 2, this.panelY + this.panelH + 2, -1442840576);
        context.method_25294(this.panelX, this.panelY, this.panelX + this.panelW, this.panelY + this.panelH, -300345324);
        context.method_25294(this.panelX, this.panelY, this.panelX + this.panelW, this.panelY + 2, -12166);
        context.method_25294(this.panelX, this.panelY + this.panelH - 2, this.panelX + this.panelW, this.panelY + this.panelH, -8760784);
        context.method_25294(this.panelX, this.panelY, this.panelX + 2, this.panelY + this.panelH, -8760784);
        context.method_25294(this.panelX + this.panelW - 2, this.panelY, this.panelX + this.panelW, this.panelY + this.panelH, -8760784);
        context.method_25294(this.panelX + 18, this.panelY + 64, this.panelX + this.panelW - 18, this.panelY + 65, 1716207668);
        context.method_25294(this.panelX + 18, this.panelY + this.panelH - 28, this.panelX + this.panelW - 18, this.panelY + this.panelH - 27, 1716207668);
    }

    public void method_25419() {
    }
}

