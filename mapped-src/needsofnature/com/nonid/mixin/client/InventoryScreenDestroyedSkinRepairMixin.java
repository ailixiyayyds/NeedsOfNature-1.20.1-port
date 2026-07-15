/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.minecraft.client.gl.RenderPipelines
 *  net.minecraft.client.sound.SoundManager
 *  net.minecraft.client.gui.Click
 *  net.minecraft.entity.player.PlayerInventory
 *  net.minecraft.screen.ScreenHandler
 *  net.minecraft.screen.PlayerScreenHandler
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.screen.ingame.HandledScreen
 *  net.minecraft.client.gui.screen.ingame.InventoryScreen
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.client.gui.tooltip.Tooltip
 *  net.minecraft.network.packet.CustomPayload
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
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.gui.Click;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.network.packet.CustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InventoryScreen.class})
public abstract class InventoryScreenDestroyedSkinRepairMixin
extends HandledScreen<PlayerScreenHandler> {
    @Unique
    private static final int REPAIR_ICON_SIZE = 14;
    @Unique
    private static final int REPAIR_BUTTON_SIZE = 18;
    @Unique
    private ButtonWidget non$repairDestroyedSkinButton;

    private InventoryScreenDestroyedSkinRepairMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super((ScreenHandler)handler, inventory, title);
    }

    @Inject(method={"init"}, at={@At(value="TAIL")})
    private void non$addDestroyedSkinRepairButton(CallbackInfo ci) {
        this.non$repairDestroyedSkinButton = new SkinRepairButton(0, 0, 18, 18);
        this.addDrawableChild((Element)this.non$repairDestroyedSkinButton);
        this.non$updateDestroyedSkinRepairButton();
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void non$updateDestroyedSkinRepairButton(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.non$updateDestroyedSkinRepairButton();
    }

    public boolean mouseClicked(Click click, boolean doubled) {
        if (click.button() == 0 && this.non$tryManualDamageDestroyedSkin(click.comp_4798(), click.comp_4799())) {
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Unique
    private boolean non$tryManualDamageDestroyedSkin(double mouseX, double mouseY) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
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
            ClientPlayNetworking.send((CustomPayload)new ManualDamageDestroyedSkinC2SPayload());
            return true;
        }
        return false;
    }

    @Unique
    private boolean non$hasShearsForManualDamage(ClientPlayerEntity player) {
        ItemStack cursorStack = ((PlayerScreenHandler)this.handler).getCursorStack();
        return cursorStack.isOf(Items.SHEARS) || player.getMainHandStack().isOf(Items.SHEARS) || player.getOffHandStack().isOf(Items.SHEARS);
    }

    @Unique
    private void non$updateDestroyedSkinRepairButton() {
        int n;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
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
        boolean nearRepairBlock = visible && player != null && NonSkinRepairBlocks.isNearRepairBlock(player.getEntityWorld(), player.getBlockPos(), NeedsOfNature.getConfig());
        boolean allowCraftingTable = NeedsOfNature.getConfig().isCraftingTableSkinRepairAllowed();
        this.non$repairDestroyedSkinButton.visible = visible;
        this.non$repairDestroyedSkinButton.active = nearRepairBlock;
        this.non$repairDestroyedSkinButton.setPosition(this.x + 58, this.y + 61);
        this.non$repairDestroyedSkinButton.setTooltip(Tooltip.of((Text)Text.translatable((String)(nearRepairBlock ? "gui.needsofnature.repair_skin" : (allowCraftingTable ? "gui.needsofnature.repair_skin.needs_loom_or_crafting_table" : "gui.needsofnature.repair_skin.needs_loom")))));
    }

    @Unique
    private boolean non$isInsidePlayerPreview(double mouseX, double mouseY) {
        return mouseX >= (double)(this.x + 20) && mouseX <= (double)(this.x + 82) && mouseY >= (double)(this.y + 4) && mouseY <= (double)(this.y + 82);
    }

    @Unique
    private static final class SkinRepairButton
    extends ButtonWidget {
        private SkinRepairButton(int x, int y, int width, int height) {
            super(x, y, width, height, (Text)Text.empty(), button -> {
                if (ClientPlayNetworking.canSend(RepairDestroyedSkinC2SPayload.ID)) {
                    ClientPlayNetworking.send((CustomPayload)new RepairDestroyedSkinC2SPayload());
                }
            }, DEFAULT_NARRATION_SUPPLIER);
        }

        protected void drawIcon(DrawContext context, int x, int y, float deltaTicks) {
            int iconSize = 14;
            int iconX = this.getX() + (this.getWidth() - iconSize) / 2;
            int iconY = this.getY() + (this.getHeight() - iconSize) / 2;
            int color = this.active ? -1 : -2130706433;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, NonIconButtonTextures.SKIN_REPAIR, iconX, iconY, 0.0f, 0.0f, iconSize, iconSize, iconSize, iconSize, color);
        }

        public void playDownSound(SoundManager soundManager) {
        }
    }
}

