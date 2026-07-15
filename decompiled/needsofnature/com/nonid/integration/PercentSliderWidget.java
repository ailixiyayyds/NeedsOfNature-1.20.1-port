/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_357
 */
package com.nonid.integration;

import java.util.function.IntConsumer;
import net.minecraft.class_2561;
import net.minecraft.class_357;

final class PercentSliderWidget
extends class_357 {
    private final IntConsumer listener;
    private int percent;

    PercentSliderWidget(int x, int y, int width, int height, int initialPercent, IntConsumer listener) {
        super(x, y, width, height, (class_2561)class_2561.method_43473(), (double)PercentSliderWidget.clampPercent(initialPercent) / 200.0);
        this.listener = listener;
        this.percent = PercentSliderWidget.clampPercent(initialPercent);
        this.method_25346();
    }

    void setPercent(int value) {
        this.percent = PercentSliderWidget.clampPercent(value);
        this.field_22753 = (double)this.percent / 200.0;
        this.method_25346();
        if (this.listener != null) {
            this.listener.accept(this.percent);
        }
    }

    protected void method_25346() {
        this.percent = PercentSliderWidget.clampPercent((int)Math.round(this.field_22753 * 200.0));
        this.method_25355((class_2561)class_2561.method_43469((String)"config.needsofnature.action_sound_volume.value", (Object[])new Object[]{this.percent}));
    }

    protected void method_25344() {
        this.percent = PercentSliderWidget.clampPercent((int)Math.round(this.field_22753 * 200.0));
        if (this.listener != null) {
            this.listener.accept(this.percent);
        }
    }

    private static int clampPercent(int value) {
        return Math.max(0, Math.min(200, value));
    }
}

