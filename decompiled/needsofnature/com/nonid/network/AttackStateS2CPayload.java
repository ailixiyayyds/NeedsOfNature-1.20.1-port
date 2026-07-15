/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_4844
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9129
 *  net.minecraft.class_9135
 *  net.minecraft.class_9139
 */
package com.nonid.network;

import java.util.UUID;
import net.minecraft.class_2960;
import net.minecraft.class_4844;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record AttackStateS2CPayload(UUID instanceId, boolean attack, boolean escapable, EscapeProfile escapeProfile) implements class_8710
{
    public static final class_2960 ATTACK_STATE_ID = class_2960.method_60655((String)"needsofnature", (String)"attack_state");
    public static final class_8710.class_9154<AttackStateS2CPayload> ID = new class_8710.class_9154(ATTACK_STATE_ID);
    public static final class_9139<class_9129, AttackStateS2CPayload> CODEC = class_9139.method_56905((class_9139)class_4844.field_48453, AttackStateS2CPayload::instanceId, (class_9139)class_9135.field_48547, AttackStateS2CPayload::attack, (class_9139)class_9135.field_48547, AttackStateS2CPayload::escapable, (class_9139)class_9135.field_49675, payload -> payload.escapeProfile().ordinal(), AttackStateS2CPayload::fromPacket);

    public AttackStateS2CPayload {
        if (escapeProfile == null) {
            escapeProfile = EscapeProfile.NONE;
        }
    }

    private static AttackStateS2CPayload fromPacket(UUID instanceId, boolean attack, boolean escapable, int profileId) {
        return new AttackStateS2CPayload(instanceId, attack, escapable, EscapeProfile.fromId(profileId));
    }

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }

    public static enum EscapeProfile {
        NONE,
        NORMAL,
        DEFEATED;


        public static EscapeProfile fromId(int id) {
            EscapeProfile[] values = EscapeProfile.values();
            if (id < 0 || id >= values.length) {
                return NONE;
            }
            return values[id];
        }
    }
}

