/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.input.KeyInput
 *  net.minecraft.client.gui.Click
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.widget.ClickableWidget
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.text.MutableText
 */
package com.nonid.integration;

import com.nonid.NonConfig;
import com.nonid.client.NonHudOverlay;
import com.nonid.integration.NonModMenuScreens;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.Click;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;

final class NonModMenuHudConfigScreen {
    private NonModMenuHudConfigScreen() {
    }

    static class UiConfigScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private static final int UI_MIN = -999;
        private static final int UI_MAX = 999;
        private int energyX;
        private int energyY;
        private int energyHeartX;
        private int energyHeartY;
        private int attackX;
        private int attackY;
        private int promptX;
        private int promptY;
        private int liquidX;
        private int liquidY;
        private boolean energyVisible;
        private boolean energyHeartVisible;
        private boolean attackVisible;
        private boolean promptVisible;
        private boolean liquidVisible;
        private int appliedEnergyX;
        private int appliedEnergyY;
        private int appliedEnergyHeartX;
        private int appliedEnergyHeartY;
        private int appliedAttackX;
        private int appliedAttackY;
        private int appliedPromptX;
        private int appliedPromptY;
        private int appliedLiquidX;
        private int appliedLiquidY;
        private boolean appliedEnergyVisible;
        private boolean appliedEnergyHeartVisible;
        private boolean appliedAttackVisible;
        private boolean appliedPromptVisible;
        private boolean appliedLiquidVisible;
        private HudElement selectedElement = HudElement.LIQUID;
        private HudElement draggedElement = null;
        private double dragStartMouseX;
        private double dragStartMouseY;
        private int dragStartOffsetX;
        private int dragStartOffsetY;
        private ButtonWidget saveButton;
        private ButtonWidget cancelButton;
        private ButtonWidget toggleSelectedVisibilityButton;
        private ButtonWidget resetSelectedButton;
        private ButtonWidget resetAllButton;
        private ButtonWidget toolbarVisibilityButton;
        private boolean toolbarVisible = true;
        private NonHudOverlay.UiPreviewBounds lastBounds = NonHudOverlay.UiPreviewBounds.empty();

        protected UiConfigScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.ui_title"));
            this.parent = parent;
            this.config = config;
            this.energyX = config.getUiEnergyOffsetX();
            this.energyY = config.getUiEnergyOffsetY();
            this.energyHeartX = config.getUiEnergyHeartOffsetX();
            this.energyHeartY = config.getUiEnergyHeartOffsetY();
            this.attackX = config.getUiAttackOffsetX();
            this.attackY = config.getUiAttackOffsetY();
            this.promptX = config.getUiPromptOffsetX();
            this.promptY = config.getUiPromptOffsetY();
            this.liquidX = config.getUiLiquidOffsetX();
            this.liquidY = config.getUiLiquidOffsetY();
            this.energyVisible = config.isUiEnergyVisible();
            this.energyHeartVisible = config.isUiEnergyHeartVisible();
            this.attackVisible = config.isUiAttackVisible();
            this.promptVisible = config.isUiPromptVisible();
            this.liquidVisible = config.isUiLiquidVisible();
            this.appliedEnergyX = this.energyX;
            this.appliedEnergyY = this.energyY;
            this.appliedEnergyHeartX = this.energyHeartX;
            this.appliedEnergyHeartY = this.energyHeartY;
            this.appliedAttackX = this.attackX;
            this.appliedAttackY = this.attackY;
            this.appliedPromptX = this.promptX;
            this.appliedPromptY = this.promptY;
            this.appliedLiquidX = this.liquidX;
            this.appliedLiquidY = this.liquidY;
            this.appliedEnergyVisible = this.energyVisible;
            this.appliedEnergyHeartVisible = this.energyHeartVisible;
            this.appliedAttackVisible = this.attackVisible;
            this.appliedPromptVisible = this.promptVisible;
            this.appliedLiquidVisible = this.liquidVisible;
        }

        protected void init() {
            NonHudOverlay.setUiPreviewEnabled(true);
            this.updatePreviewOffsets();
            int buttonY = 8;
            this.saveButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> {
                this.saveOffsets();
                NonHudOverlay.setUiPreviewEnabled(false);
                MinecraftClient.getInstance().setScreen(this.parent);
            }).dimensions(0, buttonY, 48, 18).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.saveButton, "config.needsofnature.tooltip.done_save");
            this.addDrawableChild((Element)this.saveButton);
            this.toggleSelectedVisibilityButton = ButtonWidget.builder((Text)this.toggleSelectedVisibilityLabel(), button -> {
                this.setVisible(this.selectedElement, !this.isVisible(this.selectedElement));
                this.updatePreviewOffsets();
                this.updateButtons();
            }).dimensions(0, buttonY, 86, 18).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.toggleSelectedVisibilityButton, "config.needsofnature.tooltip.ui_toggle_selected_visibility");
            this.addDrawableChild((Element)this.toggleSelectedVisibilityButton);
            this.resetSelectedButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.ui_reset_selected"), button -> {
                this.resetElement(this.selectedElement);
                this.updatePreviewOffsets();
                this.updateButtons();
            }).dimensions(0, buttonY, 86, 18).build();
            this.addDrawableChild((Element)this.resetSelectedButton);
            this.resetAllButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.ui_reset_all"), button -> {
                this.resetElement(HudElement.ENERGY);
                this.resetElement(HudElement.ENERGY_HEART);
                this.resetElement(HudElement.ATTACK);
                this.resetElement(HudElement.PROMPT);
                this.resetElement(HudElement.LIQUID);
                this.updatePreviewOffsets();
                this.updateButtons();
            }).dimensions(0, buttonY, 62, 18).build();
            this.addDrawableChild((Element)this.resetAllButton);
            this.cancelButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.cancel"), button -> {
                this.resetToApplied();
                NonHudOverlay.setUiPreviewEnabled(false);
                MinecraftClient.getInstance().setScreen(this.parent);
            }).dimensions(0, buttonY, 54, 18).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.cancelButton, "config.needsofnature.tooltip.done_unsaved");
            this.addDrawableChild((Element)this.cancelButton);
            this.toolbarVisibilityButton = ButtonWidget.builder((Text)this.toolbarVisibilityLabel(), button -> {
                this.toolbarVisible = !this.toolbarVisible;
                this.updateButtons();
            }).dimensions(0, buttonY, 18, 18).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.toolbarVisibilityButton, "config.needsofnature.tooltip.ui_toolbar_visibility");
            this.addDrawableChild((Element)this.toolbarVisibilityButton);
            this.updateButtons();
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updatePreviewOffsets();
            super.render(context, mouseX, mouseY, delta);
            this.lastBounds = NonHudOverlay.getUiPreviewBounds(context);
            this.drawDragOverlays(context, mouseX, mouseY);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);
            MutableText help = Text.translatable((String)"config.needsofnature.ui_drag_help");
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)help, this.width / 2, 24, -2039584);
            MutableText selected = this.isVisible(this.selectedElement) ? Text.translatable((String)"config.needsofnature.ui_selected", (Object[])new Object[]{Text.translatable((String)this.selectedElement.labelKey)}) : Text.translatable((String)"config.needsofnature.ui_selected_hidden", (Object[])new Object[]{Text.translatable((String)this.selectedElement.labelKey)});
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)selected, this.width / 2, 36, this.selectedElement.color);
            this.updateButtons();
        }

        public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        }

        public boolean mouseClicked(Click click, boolean doubled) {
            if (super.mouseClicked(click, doubled)) {
                return true;
            }
            if (click.button() != 0) {
                return false;
            }
            HudElement hit = this.hitTest(click.comp_4798(), click.comp_4799());
            if (hit == null) {
                return false;
            }
            this.selectedElement = hit;
            this.draggedElement = hit;
            this.dragStartMouseX = click.comp_4798();
            this.dragStartMouseY = click.comp_4799();
            this.dragStartOffsetX = this.offsetX(hit);
            this.dragStartOffsetY = this.offsetY(hit);
            this.updateButtons();
            return true;
        }

        public boolean mouseDragged(Click click, double offsetX, double offsetY) {
            if (this.draggedElement == null || click.button() != 0) {
                return super.mouseDragged(click, offsetX, offsetY);
            }
            int nextX = this.clampUiOffset(this.dragStartOffsetX + (int)Math.round(click.comp_4798() - this.dragStartMouseX));
            int nextY = this.clampUiOffset(this.dragStartOffsetY + (int)Math.round(click.comp_4799() - this.dragStartMouseY));
            this.setOffset(this.draggedElement, nextX, nextY);
            this.updatePreviewOffsets();
            this.updateButtons();
            return true;
        }

        public boolean mouseReleased(Click click) {
            if (click.button() == 0 && this.draggedElement != null) {
                this.draggedElement = null;
                this.updateButtons();
                return true;
            }
            return super.mouseReleased(click);
        }

        public boolean keyPressed(KeyInput input) {
            int step = (input.comp_4797() & 1) != 0 ? 5 : 1;
            int dx = 0;
            int dy = 0;
            int keyCode = input.comp_4795();
            if (keyCode == 263) {
                dx = -step;
            }
            if (keyCode == 262) {
                dx = step;
            }
            if (keyCode == 265) {
                dy = -step;
            }
            if (keyCode == 264) {
                dy = step;
            }
            if (dx != 0 || dy != 0) {
                this.setOffset(this.selectedElement, this.clampUiOffset(this.offsetX(this.selectedElement) + dx), this.clampUiOffset(this.offsetY(this.selectedElement) + dy));
                this.updatePreviewOffsets();
                this.updateButtons();
                return true;
            }
            return super.keyPressed(input);
        }

        public void close() {
            this.resetToApplied();
            NonHudOverlay.setUiPreviewEnabled(false);
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private void drawDragOverlays(DrawContext context, int mouseX, int mouseY) {
            this.drawElementBox(context, this.lastBounds.energy(), HudElement.ENERGY, mouseX, mouseY);
            this.drawElementBox(context, this.lastBounds.energyHeart(), HudElement.ENERGY_HEART, mouseX, mouseY);
            this.drawElementBox(context, this.lastBounds.attack(), HudElement.ATTACK, mouseX, mouseY);
            this.drawElementBox(context, this.lastBounds.prompt(), HudElement.PROMPT, mouseX, mouseY);
            this.drawElementBox(context, this.lastBounds.liquid(), HudElement.LIQUID, mouseX, mouseY);
        }

        private void drawElementBox(DrawContext context, NonHudOverlay.Rect rawRect, HudElement element, int mouseX, int mouseY) {
            int color;
            NonHudOverlay.Rect rect = rawRect.padded(4);
            boolean hovered = rect.contains(mouseX, mouseY);
            boolean active = element == this.selectedElement || element == this.draggedElement || hovered;
            boolean visible = this.isVisible(element);
            if (!active && visible) {
                return;
            }
            int n = color = visible ? element.color : -8355712;
            int fill = color & 0xFFFFFF | (hovered || element == this.draggedElement ? 0x33000000 : (visible ? 0x18000000 : 0x22000000));
            context.fill(rect.x(), rect.y(), rect.x() + rect.width(), rect.y() + rect.height(), fill);
            this.drawOutline(context, rect, color);
            MutableText label = visible ? Text.translatable((String)element.labelKey) : Text.translatable((String)"config.needsofnature.ui_hidden_label", (Object[])new Object[]{Text.translatable((String)element.labelKey)});
            context.drawTextWithShadow(this.textRenderer, (Text)label, rect.x(), Math.max(0, rect.y() - 10), color);
        }

        private void drawOutline(DrawContext context, NonHudOverlay.Rect rect, int color) {
            int x1 = rect.x();
            int y1 = rect.y();
            int x2 = rect.x() + rect.width();
            int y2 = rect.y() + rect.height();
            context.fill(x1, y1, x2, y1 + 1, color);
            context.fill(x1, y2 - 1, x2, y2, color);
            context.fill(x1, y1, x1 + 1, y2, color);
            context.fill(x2 - 1, y1, x2, y2, color);
        }

        private HudElement hitTest(double mouseX, double mouseY) {
            if (this.lastBounds.liquid().padded(6).contains(mouseX, mouseY)) {
                return HudElement.LIQUID;
            }
            if (this.lastBounds.prompt().padded(6).contains(mouseX, mouseY)) {
                return HudElement.PROMPT;
            }
            if (this.lastBounds.attack().padded(6).contains(mouseX, mouseY)) {
                return HudElement.ATTACK;
            }
            if (this.lastBounds.energyHeart().padded(6).contains(mouseX, mouseY)) {
                return HudElement.ENERGY_HEART;
            }
            if (this.lastBounds.energy().padded(6).contains(mouseX, mouseY)) {
                return HudElement.ENERGY;
            }
            return null;
        }

        private int offsetX(HudElement element) {
            return switch (element.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> this.energyX;
                case 1 -> this.energyHeartX;
                case 2 -> this.attackX;
                case 3 -> this.promptX;
                case 4 -> this.liquidX;
            };
        }

        private int offsetY(HudElement element) {
            return switch (element.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> this.energyY;
                case 1 -> this.energyHeartY;
                case 2 -> this.attackY;
                case 3 -> this.promptY;
                case 4 -> this.liquidY;
            };
        }

        private void setOffset(HudElement element, int x, int y) {
            switch (element.ordinal()) {
                case 0: {
                    this.energyX = x;
                    this.energyY = y;
                    break;
                }
                case 1: {
                    this.energyHeartX = x;
                    this.energyHeartY = y;
                    break;
                }
                case 2: {
                    this.attackX = x;
                    this.attackY = y;
                    break;
                }
                case 3: {
                    this.promptX = x;
                    this.promptY = y;
                    break;
                }
                case 4: {
                    this.liquidX = x;
                    this.liquidY = y;
                }
            }
        }

        private boolean isVisible(HudElement element) {
            return switch (element.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> this.energyVisible;
                case 1 -> this.energyHeartVisible;
                case 2 -> this.attackVisible;
                case 3 -> this.promptVisible;
                case 4 -> this.liquidVisible;
            };
        }

        private void setVisible(HudElement element, boolean visible) {
            switch (element.ordinal()) {
                case 0: {
                    this.energyVisible = visible;
                    break;
                }
                case 1: {
                    this.energyHeartVisible = visible;
                    break;
                }
                case 2: {
                    this.attackVisible = visible;
                    break;
                }
                case 3: {
                    this.promptVisible = visible;
                    break;
                }
                case 4: {
                    this.liquidVisible = visible;
                }
            }
        }

        private void resetElement(HudElement element) {
            switch (element.ordinal()) {
                case 0: {
                    this.setOffset(element, this.defaults.getUiEnergyOffsetX(), this.defaults.getUiEnergyOffsetY());
                    this.setVisible(element, this.defaults.isUiEnergyVisible());
                    break;
                }
                case 1: {
                    this.setOffset(element, this.defaults.getUiEnergyHeartOffsetX(), this.defaults.getUiEnergyHeartOffsetY());
                    this.setVisible(element, this.defaults.isUiEnergyHeartVisible());
                    break;
                }
                case 2: {
                    this.setOffset(element, this.defaults.getUiAttackOffsetX(), this.defaults.getUiAttackOffsetY());
                    this.setVisible(element, this.defaults.isUiAttackVisible());
                    break;
                }
                case 3: {
                    this.setOffset(element, this.defaults.getUiPromptOffsetX(), this.defaults.getUiPromptOffsetY());
                    this.setVisible(element, this.defaults.isUiPromptVisible());
                    break;
                }
                case 4: {
                    this.setOffset(element, this.defaults.getUiLiquidOffsetX(), this.defaults.getUiLiquidOffsetY());
                    this.setVisible(element, this.defaults.isUiLiquidVisible());
                }
            }
        }

        private void saveOffsets() {
            this.config.setUiEnergyOffsetX(this.energyX);
            this.config.setUiEnergyOffsetY(this.energyY);
            this.config.setUiEnergyHeartOffsetX(this.energyHeartX);
            this.config.setUiEnergyHeartOffsetY(this.energyHeartY);
            this.config.setUiAttackOffsetX(this.attackX);
            this.config.setUiAttackOffsetY(this.attackY);
            this.config.setUiPromptOffsetX(this.promptX);
            this.config.setUiPromptOffsetY(this.promptY);
            this.config.setUiLiquidOffsetX(this.liquidX);
            this.config.setUiLiquidOffsetY(this.liquidY);
            this.config.setUiEnergyVisible(this.energyVisible);
            this.config.setUiEnergyHeartVisible(this.energyHeartVisible);
            this.config.setUiAttackVisible(this.attackVisible);
            this.config.setUiPromptVisible(this.promptVisible);
            this.config.setUiLiquidVisible(this.liquidVisible);
            this.config.save();
            this.appliedEnergyX = this.energyX;
            this.appliedEnergyY = this.energyY;
            this.appliedEnergyHeartX = this.energyHeartX;
            this.appliedEnergyHeartY = this.energyHeartY;
            this.appliedAttackX = this.attackX;
            this.appliedAttackY = this.attackY;
            this.appliedPromptX = this.promptX;
            this.appliedPromptY = this.promptY;
            this.appliedLiquidX = this.liquidX;
            this.appliedLiquidY = this.liquidY;
            this.appliedEnergyVisible = this.energyVisible;
            this.appliedEnergyHeartVisible = this.energyHeartVisible;
            this.appliedAttackVisible = this.attackVisible;
            this.appliedPromptVisible = this.promptVisible;
            this.appliedLiquidVisible = this.liquidVisible;
        }

        private void resetToApplied() {
            this.energyX = this.appliedEnergyX;
            this.energyY = this.appliedEnergyY;
            this.energyHeartX = this.appliedEnergyHeartX;
            this.energyHeartY = this.appliedEnergyHeartY;
            this.attackX = this.appliedAttackX;
            this.attackY = this.appliedAttackY;
            this.promptX = this.appliedPromptX;
            this.promptY = this.appliedPromptY;
            this.liquidX = this.appliedLiquidX;
            this.liquidY = this.appliedLiquidY;
            this.energyVisible = this.appliedEnergyVisible;
            this.energyHeartVisible = this.appliedEnergyHeartVisible;
            this.attackVisible = this.appliedAttackVisible;
            this.promptVisible = this.appliedPromptVisible;
            this.liquidVisible = this.appliedLiquidVisible;
            this.updatePreviewOffsets();
        }

        private void updatePreviewOffsets() {
            NonHudOverlay.setUiPreviewOffsets(this.energyX, this.energyY, this.attackX, this.attackY, this.promptX, this.promptY, this.liquidX, this.liquidY, this.energyHeartX, this.energyHeartY, this.energyVisible, this.energyHeartVisible, this.attackVisible, this.promptVisible, this.liquidVisible);
        }

        private boolean hasPendingUiChanges() {
            return this.energyX != this.appliedEnergyX || this.energyY != this.appliedEnergyY || this.energyHeartX != this.appliedEnergyHeartX || this.energyHeartY != this.appliedEnergyHeartY || this.attackX != this.appliedAttackX || this.attackY != this.appliedAttackY || this.promptX != this.appliedPromptX || this.promptY != this.appliedPromptY || this.liquidX != this.appliedLiquidX || this.liquidY != this.appliedLiquidY || this.energyVisible != this.appliedEnergyVisible || this.energyHeartVisible != this.appliedEnergyHeartVisible || this.attackVisible != this.appliedAttackVisible || this.promptVisible != this.appliedPromptVisible || this.liquidVisible != this.appliedLiquidVisible;
        }

        private Text toolbarVisibilityLabel() {
            return Text.literal((String)(this.toolbarVisible ? "H" : "S"));
        }

        private Text toggleSelectedVisibilityLabel() {
            return Text.translatable((String)(this.isVisible(this.selectedElement) ? "config.needsofnature.ui_hide_selected" : "config.needsofnature.ui_show_selected"));
        }

        private void updateButtons() {
            this.updateToolbarLayout();
            boolean pending = this.hasPendingUiChanges();
            if (this.saveButton != null) {
                this.saveButton.active = pending;
            }
            if (this.toggleSelectedVisibilityButton != null) {
                this.toggleSelectedVisibilityButton.setMessage(this.toggleSelectedVisibilityLabel());
            }
            if (this.resetSelectedButton != null) {
                boolean bl = this.resetSelectedButton.active = this.offsetX(this.selectedElement) != this.defaultX(this.selectedElement) || this.offsetY(this.selectedElement) != this.defaultY(this.selectedElement) || this.isVisible(this.selectedElement) != this.defaultVisible(this.selectedElement);
            }
            if (this.resetAllButton != null) {
                boolean bl = this.resetAllButton.active = this.energyX != this.defaults.getUiEnergyOffsetX() || this.energyY != this.defaults.getUiEnergyOffsetY() || this.energyHeartX != this.defaults.getUiEnergyHeartOffsetX() || this.energyHeartY != this.defaults.getUiEnergyHeartOffsetY() || this.attackX != this.defaults.getUiAttackOffsetX() || this.attackY != this.defaults.getUiAttackOffsetY() || this.promptX != this.defaults.getUiPromptOffsetX() || this.promptY != this.defaults.getUiPromptOffsetY() || this.liquidX != this.defaults.getUiLiquidOffsetX() || this.liquidY != this.defaults.getUiLiquidOffsetY() || this.energyVisible != this.defaults.isUiEnergyVisible() || this.energyHeartVisible != this.defaults.isUiEnergyHeartVisible() || this.attackVisible != this.defaults.isUiAttackVisible() || this.promptVisible != this.defaults.isUiPromptVisible() || this.liquidVisible != this.defaults.isUiLiquidVisible();
            }
            if (this.saveButton != null) {
                this.saveButton.visible = this.toolbarVisible;
            }
            if (this.cancelButton != null) {
                this.cancelButton.visible = this.toolbarVisible;
            }
            if (this.toggleSelectedVisibilityButton != null) {
                this.toggleSelectedVisibilityButton.visible = this.toolbarVisible;
            }
            if (this.resetSelectedButton != null) {
                this.resetSelectedButton.visible = this.toolbarVisible;
            }
            if (this.resetAllButton != null) {
                this.resetAllButton.visible = this.toolbarVisible;
            }
            if (this.toolbarVisibilityButton != null) {
                this.toolbarVisibilityButton.visible = true;
                this.toolbarVisibilityButton.setMessage(this.toolbarVisibilityLabel());
            }
        }

        private void updateToolbarLayout() {
            if (this.toolbarVisibilityButton == null) {
                return;
            }
            int y = 8;
            int gap = 3;
            int eyeW = this.toolbarVisibilityButton.getWidth();
            int right = this.width - 8;
            this.toolbarVisibilityButton.setPosition(right - eyeW, y);
            if (!this.toolbarVisible) {
                return;
            }
            int nextY = y + this.toolbarVisibilityButton.getHeight() + gap;
            if (this.toggleSelectedVisibilityButton != null) {
                this.toggleSelectedVisibilityButton.setPosition(right - this.toggleSelectedVisibilityButton.getWidth(), nextY);
                nextY += this.toggleSelectedVisibilityButton.getHeight() + gap;
            }
            if (this.resetSelectedButton != null) {
                this.resetSelectedButton.setPosition(right - this.resetSelectedButton.getWidth(), nextY);
                nextY += this.resetSelectedButton.getHeight() + gap;
            }
            if (this.resetAllButton != null) {
                this.resetAllButton.setPosition(right - this.resetAllButton.getWidth(), nextY);
                nextY += this.resetAllButton.getHeight() + gap;
            }
            if (this.saveButton != null) {
                this.saveButton.setPosition(right - this.saveButton.getWidth(), nextY);
                nextY += this.saveButton.getHeight() + gap;
            }
            if (this.cancelButton != null) {
                this.cancelButton.setPosition(right - this.cancelButton.getWidth(), nextY);
            }
        }

        private int defaultX(HudElement element) {
            return switch (element.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> this.defaults.getUiEnergyOffsetX();
                case 1 -> this.defaults.getUiEnergyHeartOffsetX();
                case 2 -> this.defaults.getUiAttackOffsetX();
                case 3 -> this.defaults.getUiPromptOffsetX();
                case 4 -> this.defaults.getUiLiquidOffsetX();
            };
        }

        private int defaultY(HudElement element) {
            return switch (element.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> this.defaults.getUiEnergyOffsetY();
                case 1 -> this.defaults.getUiEnergyHeartOffsetY();
                case 2 -> this.defaults.getUiAttackOffsetY();
                case 3 -> this.defaults.getUiPromptOffsetY();
                case 4 -> this.defaults.getUiLiquidOffsetY();
            };
        }

        private boolean defaultVisible(HudElement element) {
            return switch (element.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> this.defaults.isUiEnergyVisible();
                case 1 -> this.defaults.isUiEnergyHeartVisible();
                case 2 -> this.defaults.isUiAttackVisible();
                case 3 -> this.defaults.isUiPromptVisible();
                case 4 -> this.defaults.isUiLiquidVisible();
            };
        }

        private int clampUiOffset(int value) {
            return Math.max(-999, Math.min(999, value));
        }

        private static enum HudElement {
            ENERGY("config.needsofnature.ui_energy", -8532993),
            ENERGY_HEART("config.needsofnature.ui_energy_heart", -33333),
            ATTACK("config.needsofnature.ui_attack", -38037),
            PROMPT("config.needsofnature.ui_prompt", -11930),
            LIQUID("config.needsofnature.ui_liquid", -10616907);

            private final String labelKey;
            private final int color;

            private HudElement(String labelKey, int color) {
                this.labelKey = labelKey;
                this.color = color;
            }
        }
    }
}

