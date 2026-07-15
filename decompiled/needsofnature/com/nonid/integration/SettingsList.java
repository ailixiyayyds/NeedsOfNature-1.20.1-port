/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_350$class_351
 *  net.minecraft.class_364
 *  net.minecraft.class_4265
 *  net.minecraft.class_4265$class_4266
 *  net.minecraft.class_5348
 *  net.minecraft.class_6379
 *  net.minecraft.class_7842
 *  net.minecraft.class_7919
 */
package com.nonid.integration;

import com.nonid.integration.NonModMenuScreens;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_350;
import net.minecraft.class_364;
import net.minecraft.class_4265;
import net.minecraft.class_5348;
import net.minecraft.class_6379;
import net.minecraft.class_7842;
import net.minecraft.class_7919;

final class SettingsList
extends class_4265<RowEntry> {
    SettingsList(class_310 client, int width, int height, int top) {
        super(client, width, height, top, 24);
        this.field_22744 = false;
    }

    void addEntryRow(RowEntry entry) {
        super.method_25321((class_350.class_351)entry);
    }

    public int method_25322() {
        return 440;
    }

    public int method_25342() {
        return (this.method_25368() - this.method_25322()) / 2;
    }

    static final class RowEntry
    extends class_4265.class_4266<RowEntry> {
        private final class_327 textRenderer;
        private final class_2561 label;
        private final class_7842 labelWidget;
        private final class_339 primary;
        private final class_339 reset;
        private final RowType type;
        private final List<class_339> widgets;

        private RowEntry(class_327 textRenderer, class_2561 label, class_339 primary, class_339 reset, RowType type, class_7919 tooltip) {
            this.textRenderer = textRenderer;
            this.label = label;
            this.labelWidget = label == null ? null : new class_7842(0, 0, 0, 20, label, textRenderer);
            this.primary = primary;
            this.reset = reset;
            this.type = type;
            this.widgets = new ArrayList<class_339>(2);
            if (primary != null) {
                this.widgets.add(primary);
            }
            if (reset != null) {
                this.widgets.add(reset);
            }
            if (tooltip != null) {
                if (this.labelWidget != null) {
                    NonModMenuScreens.setTooltip((class_339)this.labelWidget, tooltip);
                }
                if (this.primary != null) {
                    NonModMenuScreens.setTooltip(this.primary, tooltip);
                }
            }
        }

        static RowEntry labeledField(class_327 textRenderer, class_2561 label, class_339 field, class_339 reset, class_7919 tooltip) {
            return new RowEntry(textRenderer, label, field, reset, RowType.LABELED_FIELD, tooltip);
        }

        static RowEntry groupedField(class_327 textRenderer, class_2561 label, class_7919 tooltip, class_339 ... fields) {
            RowEntry entry = new RowEntry(textRenderer, label, null, null, RowType.GROUPED_FIELD, tooltip);
            if (fields != null) {
                for (class_339 field : fields) {
                    if (field == null) continue;
                    entry.widgets.add(field);
                }
            }
            return entry;
        }

        static RowEntry buttonWithReset(class_327 textRenderer, class_339 button, class_7919 tooltip) {
            return new RowEntry(textRenderer, null, button, null, RowType.BUTTON_RESET, tooltip);
        }

        static RowEntry sectionHeader(class_327 textRenderer, class_2561 label) {
            return new RowEntry(textRenderer, label, null, null, RowType.SECTION_HEADER, null);
        }

        public void method_25343(class_332 context, int mouseX, int mouseY, boolean hovered, float delta) {
            int rowX = this.method_73380();
            int rowY = this.method_73382();
            int rowWidth = this.method_73387();
            int widgetY = rowY + 2;
            if (this.type == RowType.SECTION_HEADER) {
                if (this.label != null) {
                    int bandY = rowY + 3;
                    int bandH = 18;
                    int centerX = rowX + rowWidth / 2;
                    int textY = rowY + 8;
                    int lineY = rowY + 12;
                    int textW = this.textRenderer.method_27525((class_5348)this.label);
                    int leftEnd = centerX - textW / 2 - 10;
                    int rightStart = centerX + textW / 2 + 10;
                    context.method_25294(rowX + 2, bandY, rowX + rowWidth - 2, bandY + bandH, 0x33000000);
                    context.method_25294(rowX + 8, lineY, Math.max(rowX + 8, leftEnd), lineY + 1, 0x66666666);
                    context.method_25294(Math.min(rowX + rowWidth - 8, rightStart), lineY, rowX + rowWidth - 8, lineY + 1, 0x66666666);
                    context.method_27534(this.textRenderer, this.label, centerX, textY, -3158065);
                }
                return;
            }
            if (this.type == RowType.GROUPED_FIELD) {
                int totalW = 0;
                for (class_339 widget : this.widgets) {
                    totalW += widget.method_25368();
                }
                int x = rowX + rowWidth - (totalW += Math.max(0, this.widgets.size() - 1) * 4);
                if (this.labelWidget != null) {
                    int labelWidth = Math.max(20, x - rowX - 8);
                    this.labelWidget.method_46421(rowX + 4);
                    this.labelWidget.method_46419(rowY + 6);
                    this.labelWidget.method_25358(labelWidth);
                }
                for (class_339 widget : this.widgets) {
                    widget.method_46421(x);
                    widget.method_46419(widgetY);
                    x += widget.method_25368() + 4;
                }
            } else if (this.primary != null) {
                if (this.type == RowType.LABELED_FIELD) {
                    resetW = this.reset == null ? 0 : this.reset.method_25368();
                    int fieldW = this.primary.method_25368();
                    int resetX = rowX + rowWidth - resetW;
                    int fieldX = resetX - 6 - fieldW;
                    this.primary.method_46421(fieldX);
                    this.primary.method_46419(widgetY);
                    if (this.reset != null) {
                        this.reset.method_46421(resetX);
                        this.reset.method_46419(widgetY);
                    }
                    if (this.labelWidget != null) {
                        int labelWidth = Math.max(20, fieldX - rowX - 8);
                        this.labelWidget.method_46421(rowX + 4);
                        this.labelWidget.method_46419(rowY + 6);
                        this.labelWidget.method_25358(labelWidth);
                    }
                } else {
                    resetW = this.reset == null ? 0 : this.reset.method_25368();
                    int buttonW = Math.max(20, rowWidth - resetW - 6);
                    this.primary.method_46421(rowX);
                    this.primary.method_46419(widgetY);
                    this.primary.method_25358(buttonW);
                    if (this.reset != null) {
                        this.reset.method_46421(rowX + rowWidth - resetW);
                        this.reset.method_46419(widgetY);
                    }
                }
            }
            if (this.labelWidget != null) {
                this.labelWidget.method_25394(context, mouseX, mouseY, delta);
            }
            for (class_339 widget : this.widgets) {
                widget.method_25394(context, mouseX, mouseY, delta);
            }
        }

        public List<? extends class_6379> method_37025() {
            return List.copyOf(this.widgets);
        }

        public void method_48206(Consumer<class_339> consumer) {
            this.widgets.forEach(consumer);
        }

        public List<? extends class_364> method_25396() {
            return List.copyOf(this.widgets);
        }

        private static enum RowType {
            LABELED_FIELD,
            GROUPED_FIELD,
            BUTTON_RESET,
            SECTION_HEADER;

        }
    }
}

