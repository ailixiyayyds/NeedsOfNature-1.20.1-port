package com.nonid.mixin.client;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;

/**
 * 1.20.1 renders the inventory status-effect panel directly from
 * AbstractInventoryScreen; the 1.21 StatusEffectsDisplay helper does not exist.
 */
@Mixin(AbstractInventoryScreen.class)
public abstract class StatusEffectsDisplayMixin {
}
