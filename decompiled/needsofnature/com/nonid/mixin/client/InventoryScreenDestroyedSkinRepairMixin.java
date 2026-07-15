/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.minecraft.class_10799
 *  net.minecraft.class_1144
 *  net.minecraft.class_11909
 *  net.minecraft.class_1661
 *  net.minecraft.class_1703
 *  net.minecraft.class_1723
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_465
 *  net.minecraft.class_490
 *  net.minecraft.class_746
 *  net.minecraft.class_7919
 *  net.minecraft.class_8710
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin.client;

import com.nonid.DestroyedSkinHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonSkinRepairBlocks;
import com.nonid.client.NonIconButtonTextures;
import com.nonid.network.ManualDamageDestroyedSkinC2SPayload;
import com.nonid.network.RepairDestroyedSkinC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.class_10799;
import net.minecraft.class_1144;
import net.minecraft.class_11909;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1723;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_465;
import net.minecraft.class_490;
import net.minecraft.class_746;
import net.minecraft.class_7919;
import net.minecraft.class_8710;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_490.class})
public abstract class InventoryScreenDestroyedSkinRepairMixin
extends class_465<class_1723> {
    @Unique
    private static final int REPAIR_ICON_SIZE = 14;
    @Unique
    private static final int REPAIR_BUTTON_SIZE = 18;
    @Unique
    private class_4185 non$repairDestroyedSkinButton;

    private InventoryScreenDestroyedSkinRepairMixin(class_1723 handler, class_1661 inventory, class_2561 title) {
        super((class_1703)handler, inventory, title);
    }

    @Inject(method={"method_25426"}, at={@At(value="TAIL")})
    private void non$addDestroyedSkinRepairButton(CallbackInfo ci) {
        this.non$repairDestroyedSkinButton = new SkinRepairButton(0, 0, 18, 18);
        this.method_37063((class_364)this.non$repairDestroyedSkinButton);
        this.non$updateDestroyedSkinRepairButton();
    }

    @Inject(method={"method_25394"}, at={@At(value="HEAD")})
    private void non$updateDestroyedSkinRepairButton(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.non$updateDestroyedSkinRepairButton();
    }

    public boolean method_25402(class_11909 click, boolean doubled) {
        if (click.method_74245() == 0 && this.non$tryManualDamageDestroyedSkin(click.comp_4798(), click.comp_4799())) {
            return true;
        }
        return super.method_25402(click, doubled);
    }

    @Unique
    private boolean non$tryManualDamageDestroyedSkin(double mouseX, double mouseY) {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null) {
            return false;
        }
        if (!this.non$isInsidePlayerPreview(mouseX, mouseY)) {
            return false;
        }
        if (!this.non$hasShearsForManualDamage(player)) {
            return false;
        }
        if (ClientPlayNetworking.canSend(ManualDamageDestroyedSkinC2SPayload.ID)) {
            ClientPlayNetworking.send((class_8710)new ManualDamageDestroyedSkinC2SPayload());
            return true;
        }
        return false;
    }

    @Unique
    private boolean non$hasShearsForManualDamage(class_746 player) {
        class_1799 cursorStack = ((class_1723)this.field_2797).method_34255();
        return cursorStack.method_31574(class_1802.field_8868) || player.method_6047().method_31574(class_1802.field_8868) || player.method_6079().method_31574(class_1802.field_8868);
    }

    @Unique
    private void non$updateDestroyedSkinRepairButton() {
        int n;
        class_746 player = class_310.method_1551().field_1724;
        if (this.non$repairDestroyedSkinButton == null) {
            return;
        }
        if (player instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
            n = holder.getDestroyedSkinDamage();
        } else {
            n = 0;
        }
        int damage = n;
        boolean visible = damage > 0;
        boolean nearRepairBlock = visible && player != null && NonSkinRepairBlocks.isNearRepairBlock(player.method_73183(), player.method_24515(), NeedsOfNature.getConfig());
        boolean allowCraftingTable = NeedsOfNature.getConfig().isCraftingTableSkinRepairAllowed();
        this.non$repairDestroyedSkinButton.field_22764 = visible;
        this.non$repairDestroyedSkinButton.field_22763 = nearRepairBlock;
        this.non$repairDestroyedSkinButton.method_48229(this.field_2776 + 58, this.field_2800 + 61);
        this.non$repairDestroyedSkinButton.method_47400(class_7919.method_47407((class_2561)class_2561.method_43471((String)(nearRepairBlock ? "gui.needsofnature.repair_skin" : (allowCraftingTable ? "gui.needsofnature.repair_skin.needs_loom_or_crafting_table" : "gui.needsofnature.repair_skin.needs_loom")))));
    }

    @Unique
    private boolean non$isInsidePlayerPreview(double mouseX, double mouseY) {
        return mouseX >= (double)(this.field_2776 + 20) && mouseX <= (double)(this.field_2776 + 82) && mouseY >= (double)(this.field_2800 + 4) && mouseY <= (double)(this.field_2800 + 82);
    }

    @Unique
    private static final class SkinRepairButton
    extends class_4185 {
        private SkinRepairButton(int x, int y, int width, int height) {
            super(x, y, width, height, (class_2561)class_2561.method_43473(), button -> {
                if (ClientPlayNetworking.canSend(RepairDestroyedSkinC2SPayload.ID)) {
                    ClientPlayNetworking.send((class_8710)new RepairDestroyedSkinC2SPayload());
                }
            }, field_40754);
        }

        protected void method_75752(class_332 context, int x, int y, float deltaTicks) {
            int iconSize = 14;
            int iconX = this.method_46426() + (this.method_25368() - iconSize) / 2;
            int iconY = this.method_46427() + (this.method_25364() - iconSize) / 2;
            int color = this.field_22763 ? -1 : -2130706433;
            context.method_25291(class_10799.field_56883, NonIconButtonTextures.SKIN_REPAIR, iconX, iconY, 0.0f, 0.0f, iconSize, iconSize, iconSize, iconSize, color);
        }

        public void method_25354(class_1144 soundManager) {
        }
    }
}

