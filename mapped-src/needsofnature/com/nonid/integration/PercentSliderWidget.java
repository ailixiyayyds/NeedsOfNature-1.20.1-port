/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.client.gui.widget.SliderWidget
 */
package com.nonid.integration;

import java.util.function.IntConsumer;
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.SliderWidget;

final class PercentSliderWidget
extends SliderWidget {
    private final IntConsumer listener;
    private int percent;

    PercentSliderWidget(int x, int y, int width, int height, int initialPercent, IntConsumer listener) {
        super(x, y, width, height, (Text)Text.empty(), (double)PercentSliderWidget.clampPercent(initialPercent) / 200.0);
        this.listener = listener;
        this.percent = PercentSliderWidget.clampPercent(initialPercent);
        this.updateMessage();
    }

    void setPercent(int value) {
        this.percent = PercentSliderWidget.clampPercent(value);
        this.value = (double)this.percent / 200.0;
        this.updateMessage();
        if (this.listener != null) {
            this.listener.accept(this.percent);
        }
    }

    protected void updateMessage() {
        this.percent = PercentSliderWidget.clampPercent((int)Math.round(this.value * 200.0));
        this.setMessage((Text)Text.translatable((String)"config.needsofnature.action_sound_volume.value", (Object[])new Object[]{this.percent}));
    }

    protected void applyValue() {
        this.percent = PercentSliderWidget.clampPercent((int)Math.round(this.value * 200.0));
        if (this.listener != null) {
            this.listener.accept(this.percent);
        }
    }

    private static int clampPercent(int value) {
        return Math.max(0, Math.min(200, value));
    }
}

