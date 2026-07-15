/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_5250
 */
package com.nonid.integration;

import com.nonid.NonConfig;
import com.nonid.client.NonHudOverlay;
import com.nonid.integration.NonModMenuScreens;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5250;

final class NonModMenuHudConfigScreen {
    private NonModMenuHudConfigScreen() {
    }

    static class UiConfigScreen
    extends class_437 {
        private final class_437 parent;
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
        private class_4185 saveButton;
        private class_4185 cancelButton;
        private class_4185 toggleSelectedVisibilityButton;
        private class_4185 resetSelectedButton;
        private class_4185 resetAllButton;
        private class_4185 toolbarVisibilityButton;
        private boolean toolbarVisible = true;
        private NonHudOverlay.UiPreviewBounds lastBounds = NonHudOverlay.UiPreviewBounds.empty();

        protected UiConfigScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.ui_title"));
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

        protected void method_25426() {
            NonHudOverlay.setUiPreviewEnabled(true);
            this.updatePreviewOffsets();
            int buttonY = 8;
            this.saveButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> {
                this.saveOffsets();
                NonHudOverlay.setUiPreviewEnabled(false);
                class_310.method_1551().method_1507(this.parent);
            }).method_46434(0, buttonY, 48, 18).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.saveButton, "config.needsofnature.tooltip.done_save");
            this.method_37063((class_364)this.saveButton);
            this.toggleSelectedVisibilityButton = class_4185.method_46430((class_2561)this.toggleSelectedVisibilityLabel(), button -> {
                this.setVisible(this.selectedElement, !this.isVisible(this.selectedElement));
                this.updatePreviewOffsets();
                this.updateButtons();
            }).method_46434(0, buttonY, 86, 18).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.toggleSelectedVisibilityButton, "config.needsofnature.tooltip.ui_toggle_selected_visibility");
            this.method_37063((class_364)this.toggleSelectedVisibilityButton);
            this.resetSelectedButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.ui_reset_selected"), button -> {
                this.resetElement(this.selectedElement);
                this.updatePreviewOffsets();
                this.updateButtons();
            }).method_46434(0, buttonY, 86, 18).method_46431();
            this.method_37063((class_364)this.resetSelectedButton);
            this.resetAllButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.ui_reset_all"), button -> {
                this.resetElement(HudElement.ENERGY);
                this.resetElement(HudElement.ENERGY_HEART);
                this.resetElement(HudElement.ATTACK);
                this.resetElement(HudElement.PROMPT);
                this.resetElement(HudElement.LIQUID);
                this.updatePreviewOffsets();
                this.updateButtons();
            }).method_46434(0, buttonY, 62, 18).method_46431();
            this.method_37063((class_364)this.resetAllButton);
            this.cancelButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.cancel"), button -> {
                this.resetToApplied();
                NonHudOverlay.setUiPreviewEnabled(false);
                class_310.method_1551().method_1507(this.parent);
            }).method_46434(0, buttonY, 54, 18).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.cancelButton, "config.needsofnature.tooltip.done_unsaved");
            this.method_37063((class_364)this.cancelButton);
            this.toolbarVisibilityButton = class_4185.method_46430((class_2561)this.toolbarVisibilityLabel(), button -> {
                this.toolbarVisible = !this.toolbarVisible;
                this.updateButtons();
            }).method_46434(0, buttonY, 18, 18).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.toolbarVisibilityButton, "config.needsofnature.tooltip.ui_toolbar_visibility");
            this.method_37063((class_364)this.toolbarVisibilityButton);
            this.updateButtons();
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updatePreviewOffsets();
            super.method_25394(context, mouseX, mouseY, delta);
            this.lastBounds = NonHudOverlay.getUiPreviewBounds(context);
            this.drawDragOverlays(context, mouseX, mouseY);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 12, 0xFFFFFF);
            class_5250 help = class_2561.method_43471((String)"config.needsofnature.ui_drag_help");
            context.method_27534(this.field_22793, (class_2561)help, this.field_22789 / 2, 24, -2039584);
            class_5250 selected = this.isVisible(this.selectedElement) ? class_2561.method_43469((String)"config.needsofnature.ui_selected", (Object[])new Object[]{class_2561.method_43471((String)this.selectedElement.labelKey)}) : class_2561.method_43469((String)"config.needsofnature.ui_selected_hidden", (Object[])new Object[]{class_2561.method_43471((String)this.selectedElement.labelKey)});
            context.method_27534(this.field_22793, (class_2561)selected, this.field_22789 / 2, 36, this.selectedElement.color);
            this.updateButtons();
        }

        public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
        }

        public boolean method_25402(class_11909 click, boolean doubled) {
            if (super.method_25402(click, doubled)) {
                return true;
            }
            if (click.method_74245() != 0) {
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

        public boolean method_25403(class_11909 click, double offsetX, double offsetY) {
            if (this.draggedElement == null || click.method_74245() != 0) {
                return super.method_25403(click, offsetX, offsetY);
            }
            int nextX = this.clampUiOffset(this.dragStartOffsetX + (int)Math.round(click.comp_4798() - this.dragStartMouseX));
            int nextY = this.clampUiOffset(this.dragStartOffsetY + (int)Math.round(click.comp_4799() - this.dragStartMouseY));
            this.setOffset(this.draggedElement, nextX, nextY);
            this.updatePreviewOffsets();
            this.updateButtons();
            return true;
        }

        public boolean method_25406(class_11909 click) {
            if (click.method_74245() == 0 && this.draggedElement != null) {
                this.draggedElement = null;
                this.updateButtons();
                return true;
            }
            return super.method_25406(click);
        }

        public boolean method_25404(class_11908 input) {
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
            return super.method_25404(input);
        }

        public void method_25419() {
            this.resetToApplied();
            NonHudOverlay.setUiPreviewEnabled(false);
            class_310.method_1551().method_1507(this.parent);
        }

        private void drawDragOverlays(class_332 context, int mouseX, int mouseY) {
            this.drawElementBox(context, this.lastBounds.energy(), HudElement.ENERGY, mouseX, mouseY);
            this.drawElementBox(context, this.lastBounds.energyHeart(), HudElement.ENERGY_HEART, mouseX, mouseY);
            this.drawElementBox(context, this.lastBounds.attack(), HudElement.ATTACK, mouseX, mouseY);
            this.drawElementBox(context, this.lastBounds.prompt(), HudElement.PROMPT, mouseX, mouseY);
            this.drawElementBox(context, this.lastBounds.liquid(), HudElement.LIQUID, mouseX, mouseY);
        }

        private void drawElementBox(class_332 context, NonHudOverlay.Rect rawRect, HudElement element, int mouseX, int mouseY) {
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
            context.method_25294(rect.x(), rect.y(), rect.x() + rect.width(), rect.y() + rect.height(), fill);
            this.drawOutline(context, rect, color);
            class_5250 label = visible ? class_2561.method_43471((String)element.labelKey) : class_2561.method_43469((String)"config.needsofnature.ui_hidden_label", (Object[])new Object[]{class_2561.method_43471((String)element.labelKey)});
            context.method_27535(this.field_22793, (class_2561)label, rect.x(), Math.max(0, rect.y() - 10), color);
        }

        private void drawOutline(class_332 context, NonHudOverlay.Rect rect, int color) {
            int x1 = rect.x();
            int y1 = rect.y();
            int x2 = rect.x() + rect.width();
            int y2 = rect.y() + rect.height();
            context.method_25294(x1, y1, x2, y1 + 1, color);
            context.method_25294(x1, y2 - 1, x2, y2, color);
            context.method_25294(x1, y1, x1 + 1, y2, color);
            context.method_25294(x2 - 1, y1, x2, y2, color);
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

        private class_2561 toolbarVisibilityLabel() {
            return class_2561.method_43470((String)(this.toolbarVisible ? "H" : "S"));
        }

        private class_2561 toggleSelectedVisibilityLabel() {
            return class_2561.method_43471((String)(this.isVisible(this.selectedElement) ? "config.needsofnature.ui_hide_selected" : "config.needsofnature.ui_show_selected"));
        }

        private void updateButtons() {
            this.updateToolbarLayout();
            boolean pending = this.hasPendingUiChanges();
            if (this.saveButton != null) {
                this.saveButton.field_22763 = pending;
            }
            if (this.toggleSelectedVisibilityButton != null) {
                this.toggleSelectedVisibilityButton.method_25355(this.toggleSelectedVisibilityLabel());
            }
            if (this.resetSelectedButton != null) {
                boolean bl = this.resetSelectedButton.field_22763 = this.offsetX(this.selectedElement) != this.defaultX(this.selectedElement) || this.offsetY(this.selectedElement) != this.defaultY(this.selectedElement) || this.isVisible(this.selectedElement) != this.defaultVisible(this.selectedElement);
            }
            if (this.resetAllButton != null) {
                boolean bl = this.resetAllButton.field_22763 = this.energyX != this.defaults.getUiEnergyOffsetX() || this.energyY != this.defaults.getUiEnergyOffsetY() || this.energyHeartX != this.defaults.getUiEnergyHeartOffsetX() || this.energyHeartY != this.defaults.getUiEnergyHeartOffsetY() || this.attackX != this.defaults.getUiAttackOffsetX() || this.attackY != this.defaults.getUiAttackOffsetY() || this.promptX != this.defaults.getUiPromptOffsetX() || this.promptY != this.defaults.getUiPromptOffsetY() || this.liquidX != this.defaults.getUiLiquidOffsetX() || this.liquidY != this.defaults.getUiLiquidOffsetY() || this.energyVisible != this.defaults.isUiEnergyVisible() || this.energyHeartVisible != this.defaults.isUiEnergyHeartVisible() || this.attackVisible != this.defaults.isUiAttackVisible() || this.promptVisible != this.defaults.isUiPromptVisible() || this.liquidVisible != this.defaults.isUiLiquidVisible();
            }
            if (this.saveButton != null) {
                this.saveButton.field_22764 = this.toolbarVisible;
            }
            if (this.cancelButton != null) {
                this.cancelButton.field_22764 = this.toolbarVisible;
            }
            if (this.toggleSelectedVisibilityButton != null) {
                this.toggleSelectedVisibilityButton.field_22764 = this.toolbarVisible;
            }
            if (this.resetSelectedButton != null) {
                this.resetSelectedButton.field_22764 = this.toolbarVisible;
            }
            if (this.resetAllButton != null) {
                this.resetAllButton.field_22764 = this.toolbarVisible;
            }
            if (this.toolbarVisibilityButton != null) {
                this.toolbarVisibilityButton.field_22764 = true;
                this.toolbarVisibilityButton.method_25355(this.toolbarVisibilityLabel());
            }
        }

        private void updateToolbarLayout() {
            if (this.toolbarVisibilityButton == null) {
                return;
            }
            int y = 8;
            int gap = 3;
            int eyeW = this.toolbarVisibilityButton.method_25368();
            int right = this.field_22789 - 8;
            this.toolbarVisibilityButton.method_48229(right - eyeW, y);
            if (!this.toolbarVisible) {
                return;
            }
            int nextY = y + this.toolbarVisibilityButton.method_25364() + gap;
            if (this.toggleSelectedVisibilityButton != null) {
                this.toggleSelectedVisibilityButton.method_48229(right - this.toggleSelectedVisibilityButton.method_25368(), nextY);
                nextY += this.toggleSelectedVisibilityButton.method_25364() + gap;
            }
            if (this.resetSelectedButton != null) {
                this.resetSelectedButton.method_48229(right - this.resetSelectedButton.method_25368(), nextY);
                nextY += this.resetSelectedButton.method_25364() + gap;
            }
            if (this.resetAllButton != null) {
                this.resetAllButton.method_48229(right - this.resetAllButton.method_25368(), nextY);
                nextY += this.resetAllButton.method_25364() + gap;
            }
            if (this.saveButton != null) {
                this.saveButton.method_48229(right - this.saveButton.method_25368(), nextY);
                nextY += this.saveButton.method_25364() + gap;
            }
            if (this.cancelButton != null) {
                this.cancelButton.method_48229(right - this.cancelButton.method_25368(), nextY);
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

