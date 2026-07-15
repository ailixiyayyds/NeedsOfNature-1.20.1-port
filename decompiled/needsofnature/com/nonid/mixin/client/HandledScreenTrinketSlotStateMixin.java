/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.emi.trinkets.TrinketSlot
 *  net.minecraft.class_1309
 *  net.minecraft.class_1703
 *  net.minecraft.class_1735
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_465
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.nonid.mixin.client;

import com.nonid.NonTrinketsIntegration;
import dev.emi.trinkets.TrinketSlot;
import net.minecraft.class_1309;
import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_465;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_465.class})
public abstract class HandledScreenTrinketSlotStateMixin<T extends class_1703> {
    @Shadow
    protected class_1735 field_2787;

    @Inject(method={"method_2385"}, at={@At(value="TAIL")})
    private void non$drawUnavailableTrinketSlotOverlay(class_332 context, class_1735 slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (HandledScreenTrinketSlotStateMixin.non$unavailableSlotTooltip(slot) == null) {
            return;
        }
        context.method_25294(slot.field_7873, slot.field_7872, slot.field_7873 + 16, slot.field_7872 + 16, -1728053248);
        context.method_73198(slot.field_7873, slot.field_7872, 16, 16, -1435011209);
    }

    @Inject(method={"method_2380"}, at={@At(value="RETURN")})
    private void non$drawUnavailableTrinketSlotTooltip(class_332 context, int x, int y, CallbackInfo ci) {
        class_2561 tooltip = HandledScreenTrinketSlotStateMixin.non$unavailableSlotTooltip(this.field_2787);
        if (tooltip != null) {
            context.method_71276(tooltip, x, y);
        }
    }

    private static class_2561 non$unavailableSlotTooltip(class_1735 slot) {
        if (!(slot instanceof TrinketSlot)) {
            return null;
        }
        TrinketSlot trinketSlot = (TrinketSlot)slot;
        class_746 player = class_310.method_1551().field_1724;
        if (player == null || trinketSlot.getType() == null) {
            return null;
        }
        String slotId = trinketSlot.getType().getId();
        if (NonTrinketsIntegration.isSlotAvailableFor((class_1309)player, slotId)) {
            return null;
        }
        if ("legs/v".equals(slotId)) {
            return class_2561.method_43471((String)"gui.needsofnature.trinket_slot.requires_female");
        }
        if ("legs/d".equals(slotId)) {
            return class_2561.method_43471((String)"gui.needsofnature.trinket_slot.requires_male");
        }
        return class_2561.method_43471((String)"gui.needsofnature.trinket_slot.unavailable");
    }
}

