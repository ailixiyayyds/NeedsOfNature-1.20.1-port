/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9129
 *  net.minecraft.class_9135
 *  net.minecraft.class_9139
 */
package com.nonid.network;

import net.minecraft.class_2960;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record GenderSelectionPromptS2CPayload(int allowedMask, int currentMask, boolean permanent) implements class_8710
{
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"needsofnature", (String)"gender_selection_prompt");
    public static final class_8710.class_9154<GenderSelectionPromptS2CPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, GenderSelectionPromptS2CPayload> CODEC = class_9139.method_56436((class_9139)class_9135.field_49675, GenderSelectionPromptS2CPayload::allowedMask, (class_9139)class_9135.field_49675, GenderSelectionPromptS2CPayload::currentMask, (class_9139)class_9135.field_48547, GenderSelectionPromptS2CPayload::permanent, GenderSelectionPromptS2CPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

