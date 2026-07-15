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

public record DestroyedSkinMaskUploadC2SPayload(int stage, byte[] pngBytes) implements class_8710
{
    public static final class_2960 ID_RAW = class_2960.method_60655((String)"needsofnature", (String)"destroyed_skin_mask_upload");
    public static final class_8710.class_9154<DestroyedSkinMaskUploadC2SPayload> ID = new class_8710.class_9154(ID_RAW);
    public static final class_9139<class_9129, DestroyedSkinMaskUploadC2SPayload> CODEC = class_9139.method_56435((class_9139)class_9135.field_48550, DestroyedSkinMaskUploadC2SPayload::stage, (class_9139)class_9135.method_56895((int)65536), DestroyedSkinMaskUploadC2SPayload::pngBytes, DestroyedSkinMaskUploadC2SPayload::new);

    public class_8710.class_9154<? extends class_8710> method_56479() {
        return ID;
    }
}

