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

public record SetPlayerGenderC2SPayload(int mask, boolean initialSelection) implements class_8710
{
    public static final class_2960 SET_PLAYER_GENDER_ID = class_2960.method_60655((String)"needsofnature", (String)"set_player_gender");
    public static final class_8710.class_9154<SetPlayerGenderC2SPayload> ID = new class_8710.class_9154(SET_PLAYER_GENDER_ID);
    public static final class_9139<class_9129, SetPlayerGenderC2SPayload> CODEC = class_9139.method_56435((class_9139)class_9135.field_49675, SetPlayerGenderC2SPayload::mask, (class_9139)class_9135.field_48547, SetPlayerGenderC2SPayload::initialSelection, SetPlayerGenderC2SPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

