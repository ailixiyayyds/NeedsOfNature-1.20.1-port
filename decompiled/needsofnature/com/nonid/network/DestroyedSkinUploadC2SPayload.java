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

public record DestroyedSkinUploadC2SPayload(byte[] pngBytes) implements class_8710
{
    public static final int MAX_BYTES = 65536;
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"needsofnature", (String)"destroyed_skin_upload");
    public static final class_8710.class_9154<DestroyedSkinUploadC2SPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, DestroyedSkinUploadC2SPayload> CODEC = class_9139.method_56434((class_9139)class_9135.method_56895((int)65536), DestroyedSkinUploadC2SPayload::pngBytes, DestroyedSkinUploadC2SPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

