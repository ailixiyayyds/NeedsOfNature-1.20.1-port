/*
 * Decompiled with CFR 0.152.
 */
package com.nonid.api;

import com.nonid.api.NonAccessoryEffectsSnapshot;
import com.nonid.api.NonDestroyedSkinSnapshot;
import com.nonid.api.NonGender;
import com.nonid.api.NonLiquidTankSnapshot;
import com.nonid.api.NonMessSnapshot;
import com.nonid.api.NonPregnancySnapshot;

public record NonPlayerStateSnapshot(NonGender gender, NonLiquidTankSnapshot liquidTank, NonMessSnapshot mess, NonDestroyedSkinSnapshot destroyedSkin, NonPregnancySnapshot pregnancy, NonAccessoryEffectsSnapshot accessoryEffects) {
}

