/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_2960
 *  net.minecraft.class_5819
 */
package com.nonid.client;

import com.nonid.client.NonHudOverlay;
import com.nonid.data.NonPeakStages;
import java.util.Locale;
import net.minecraft.class_1309;
import net.minecraft.class_2960;
import net.minecraft.class_5819;

public final class NonSoundCues {
    private static final int DRY_IMPACT_COUNT = 11;
    private static final int WET_IMPACT_COUNT = 16;
    private static final String[] REACTIVE_DRY = new String[]{"impactdry01", "impactdry02", "impactdry03"};
    private static final String[] REACTIVE_WET_IMPACT = new String[]{"impactwet01", "impactwet02", "impactwet03"};
    private static final String[] REACTIVE_WET = new String[]{"wet04", "wet06", "wet07"};
    private static final String PEAK_REACTIVE_SOUND = "shot_in01";
    private static final String BIRTH_SOUND = "needsofnature:pop";

    private NonSoundCues() {
    }

    public static class_2960 resolveSoundId(String effect, class_1309 anchor, class_2960 stageAnimationId, int stageIndex) {
        class_2960 directResolved;
        class_2960 direct;
        if (effect == null || effect.isBlank()) {
            return null;
        }
        if (effect.indexOf(58) >= 0 && (direct = class_2960.method_12829((String)effect)) != null) {
            if ("minecraft".equals(direct.method_12836()) && NonSoundCues.isLocalEffectPath(direct.method_12832())) {
                return class_2960.method_60655((String)"needsofnature", (String)direct.method_12832());
            }
            return direct;
        }
        String resolved = NonSoundCues.mapEffect(effect, anchor, stageAnimationId, stageIndex);
        if (resolved == null) {
            return null;
        }
        if (resolved.indexOf(58) >= 0 && (directResolved = class_2960.method_12829((String)resolved)) != null) {
            if ("minecraft".equals(directResolved.method_12836()) && NonSoundCues.isLocalEffectPath(directResolved.method_12832())) {
                return class_2960.method_60655((String)"needsofnature", (String)directResolved.method_12832());
            }
            return directResolved;
        }
        return class_2960.method_60655((String)"needsofnature", (String)resolved);
    }

    public static boolean isPeakReactiveImpactCue(String effect, class_2960 stageAnimationId, int stageIndex) {
        if (effect == null || effect.isBlank()) {
            return false;
        }
        if (!"reactiveimpact".equalsIgnoreCase(effect)) {
            return false;
        }
        return NonSoundCues.isPeakStage(stageAnimationId, stageIndex);
    }

    public static boolean isPeakAnimationStage(class_2960 stageAnimationId, int stageIndex) {
        return NonSoundCues.isPeakStage(stageAnimationId, stageIndex);
    }

    public static boolean isActionSoundId(class_2960 soundId) {
        return soundId != null && "needsofnature".equals(soundId.method_12836()) && NonSoundCues.isLocalEffectPath(soundId.method_12832());
    }

    private static String mapEffect(String effect, class_1309 anchor, class_2960 stageAnimationId, int stageIndex) {
        if (effect.equals("impactdry")) {
            return NonSoundCues.pickRandom("impactdry", 11, anchor);
        }
        if (effect.equals("impactwet")) {
            return NonSoundCues.pickRandom("impactwet", 16, anchor);
        }
        if (effect.equals("reactiveimpact")) {
            return NonSoundCues.pickReactiveImpact(anchor, stageAnimationId, stageIndex);
        }
        if (effect.equals("birth")) {
            return BIRTH_SOUND;
        }
        if (effect.startsWith("impactdry") || effect.startsWith("impactwet") || effect.startsWith("wet") || effect.startsWith("shot_in") || effect.startsWith("shot_out") || effect.startsWith("motion") || effect.startsWith("retract")) {
            return effect;
        }
        return null;
    }

    private static String pickRandom(String prefix, int count, class_1309 anchor) {
        class_5819 random = anchor != null ? anchor.method_73183().method_8409() : class_5819.method_43047();
        int index = random.method_43048(count) + 1;
        return prefix + String.format(Locale.ROOT, "%02d", index);
    }

    private static String pickReactiveImpact(class_1309 anchor, class_2960 stageAnimationId, int stageIndex) {
        if (NonSoundCues.isPeakStage(stageAnimationId, stageIndex)) {
            return PEAK_REACTIVE_SOUND;
        }
        float ratio = NonHudOverlay.getLiquidFillRatio();
        if (ratio < 0.1f) {
            return NonSoundCues.pickFromList(REACTIVE_DRY, anchor);
        }
        if (ratio > 0.8f) {
            return NonSoundCues.pickFromList(REACTIVE_WET, anchor);
        }
        return NonSoundCues.pickFromList(REACTIVE_WET_IMPACT, anchor);
    }

    private static boolean isPeakStage(class_2960 stageAnimationId, int stageIndex) {
        if (stageAnimationId == null) {
            return false;
        }
        if (stageIndex < 0) {
            return false;
        }
        if (NonPeakStages.stageNumberFromId(stageAnimationId) == null) {
            return false;
        }
        class_2960 baseAnimationId = NonPeakStages.baseAnimationId(stageAnimationId);
        Integer peakStage = NonPeakStages.getPeakStage(baseAnimationId);
        if (peakStage == null) {
            return false;
        }
        int logicalStage = stageIndex + 1;
        return logicalStage == peakStage;
    }

    private static String pickFromList(String[] options, class_1309 anchor) {
        if (options == null || options.length == 0) {
            return null;
        }
        class_5819 random = anchor != null ? anchor.method_73183().method_8409() : class_5819.method_43047();
        return options[random.method_43048(options.length)];
    }

    private static boolean isLocalEffectPath(String path) {
        return path.startsWith("impactdry") || path.startsWith("impactwet") || path.startsWith("wet") || path.startsWith("shot_in") || path.startsWith("shot_out") || path.startsWith("motion") || path.startsWith("retract");
    }
}

