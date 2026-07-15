/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.emi.trinkets.TrinketSlot
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.screen.ScreenHandler
 *  net.minecraft.screen.slot.Slot
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.screen.ingame.HandledScreen
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin.client;

import com.nonid.NonTrinketsIntegration;
import dev.emi.trinkets.TrinketSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={HandledScreen.class})
public abstract class HandledScreenTrinketSlotStateMixin<T extends ScreenHandler> {
    @Shadow
    protected Slot focusedSlot;

    @Inject(method={"drawSlot"}, at={@At(value="TAIL")})
    private void non$drawUnavailableTrinketSlotOverlay(DrawContext context, Slot slot, CallbackInfo ci) {
        if (HandledScreenTrinketSlotStateMixin.non$unavailableSlotTooltip(slot) == null) {
            return;
        }
        context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, -1728053248);
        int color = -1435011209;
        context.fill(slot.x, slot.y, slot.x + 16, slot.y + 1, color);
        context.fill(slot.x, slot.y + 15, slot.x + 16, slot.y + 16, color);
        context.fill(slot.x, slot.y + 1, slot.x + 1, slot.y + 15, color);
        context.fill(slot.x + 15, slot.y + 1, slot.x + 16, slot.y + 15, color);
    }

    @Inject(method={"drawMouseoverTooltip"}, at={@At(value="RETURN")})
    private void non$drawUnavailableTrinketSlotTooltip(DrawContext context, int x, int y, CallbackInfo ci) {
        Text tooltip = HandledScreenTrinketSlotStateMixin.non$unavailableSlotTooltip(this.focusedSlot);
        if (tooltip != null) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip, x, y);
        }
    }

    private static Text non$unavailableSlotTooltip(Slot slot) {
        if (!(slot instanceof TrinketSlot)) {
            return null;
        }
        TrinketSlot trinketSlot = (TrinketSlot)slot;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || trinketSlot.getType() == null) {
            return null;
        }
        String slotId = trinketSlot.getType().getGroup() + "/" + trinketSlot.getType().getName();
        if (NonTrinketsIntegration.isSlotAvailableFor((LivingEntity)player, slotId)) {
            return null;
        }
        if ("legs/v".equals(slotId)) {
            return Text.translatable((String)"gui.needsofnature.trinket_slot.requires_female");
        }
        if ("legs/d".equals(slotId)) {
            return Text.translatable((String)"gui.needsofnature.trinket_slot.requires_male");
        }
        return Text.translatable((String)"gui.needsofnature.trinket_slot.unavailable");
    }
}

