/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9129
 *  net.minecraft.class_9139
 */
package com.afwid.network;

import net.minecraft.class_2960;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9139;

public record DebugChatPreferenceC2SPayload(String modeId) implements class_8710
{
    public static final class_2960 DEBUG_CHAT_PREF_ID = class_2960.method_60655((String)"animationframework", (String)"debug_chat_pref");
    public static final class_8710.class_9154<DebugChatPreferenceC2SPayload> ID = new class_8710.class_9154(DEBUG_CHAT_PREF_ID);
    public static final class_9139<class_9129, DebugChatPreferenceC2SPayload> CODEC = class_9139.method_56438(DebugChatPreferenceC2SPayload::encode, DebugChatPreferenceC2SPayload::decode);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }

    private static void encode(DebugChatPreferenceC2SPayload payload, class_9129 buf) {
        buf.method_10814(payload.modeId());
    }

    private static DebugChatPreferenceC2SPayload decode(class_9129 buf) {
        return new DebugChatPreferenceC2SPayload(buf.method_10800(64));
    }
}

