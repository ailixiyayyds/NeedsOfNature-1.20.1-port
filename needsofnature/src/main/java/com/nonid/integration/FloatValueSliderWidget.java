/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.client.gui.widget.SliderWidget
 */
package com.nonid.integration;

import java.util.Locale;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.SliderWidget;

final class FloatValueSliderWidget
extends SliderWidget {
    private final double min;
    private final double max;
    private final double step;
    private final DoubleConsumer listener;
    private final DoubleFunction<String> formatter;
    private float floatValue;

    FloatValueSliderWidget(int x, int y, int width, int height, float initialValue, float min, float max, float step, DoubleConsumer listener) {
        this(x, y, width, height, initialValue, min, max, step, listener, FloatValueSliderWidget::format);
    }

    FloatValueSliderWidget(int x, int y, int width, int height, float initialValue, float min, float max, float step, DoubleConsumer listener, DoubleFunction<String> formatter) {
        super(x, y, width, height, (Text)Text.empty(), FloatValueSliderWidget.toSliderValue(initialValue, min, max));
        this.min = min;
        this.max = max;
        this.step = Math.max(1.0E-4, (double)step);
        this.listener = listener;
        this.formatter = formatter == null ? FloatValueSliderWidget::format : formatter;
        this.floatValue = this.clampAndRound(initialValue);
        this.value = FloatValueSliderWidget.toSliderValue(this.floatValue, min, max);
        this.updateMessage();
    }

    float getFloatValue() {
        return this.floatValue;
    }

    void setFloatValue(float value) {
        this.floatValue = this.clampAndRound(value);
        this.value = FloatValueSliderWidget.toSliderValue(this.floatValue, this.min, this.max);
        this.updateMessage();
        if (this.listener != null) {
            this.listener.accept(this.floatValue);
        }
    }

    protected void updateMessage() {
        this.floatValue = this.clampAndRound((float)(this.min + this.value * (this.max - this.min)));
        this.setMessage((Text)Text.literal((String)this.formatter.apply(this.floatValue)));
    }

    protected void applyValue() {
        this.floatValue = this.clampAndRound((float)(this.min + this.value * (this.max - this.min)));
        this.value = FloatValueSliderWidget.toSliderValue(this.floatValue, this.min, this.max);
        if (this.listener != null) {
            this.listener.accept(this.floatValue);
        }
    }

    private float clampAndRound(float input) {
        double clamped = Math.max(this.min, Math.min(this.max, (double)input));
        double rounded = (double)Math.round(clamped / this.step) * this.step;
        return (float)Math.max(this.min, Math.min(this.max, rounded));
    }

    private static double toSliderValue(double value, double min, double max) {
        if (max <= min) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, (value - min) / (max - min)));
    }

    private static String format(double value) {
        return String.format(Locale.ROOT, "%.3f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}

