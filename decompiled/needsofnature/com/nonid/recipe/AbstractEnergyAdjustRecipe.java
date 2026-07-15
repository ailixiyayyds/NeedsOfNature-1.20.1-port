/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10295
 *  net.minecraft.class_10301
 *  net.minecraft.class_10302
 *  net.minecraft.class_10302$class_10306
 *  net.minecraft.class_10302$class_10307
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1844
 *  net.minecraft.class_1856
 *  net.minecraft.class_1865
 *  net.minecraft.class_1935
 *  net.minecraft.class_1937
 *  net.minecraft.class_2371
 *  net.minecraft.class_3955
 *  net.minecraft.class_6880
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_7710
 *  net.minecraft.class_7923
 *  net.minecraft.class_9334
 *  net.minecraft.class_9694
 *  net.minecraft.class_9887
 */
package com.nonid.recipe;

import com.nonid.NeedsOfNature;
import com.nonid.potion.NonPotions;
import java.util.List;
import net.minecraft.class_10295;
import net.minecraft.class_10301;
import net.minecraft.class_10302;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_1856;
import net.minecraft.class_1865;
import net.minecraft.class_1935;
import net.minecraft.class_1937;
import net.minecraft.class_2371;
import net.minecraft.class_3955;
import net.minecraft.class_6880;
import net.minecraft.class_7225;
import net.minecraft.class_7710;
import net.minecraft.class_7923;
import net.minecraft.class_9334;
import net.minecraft.class_9694;
import net.minecraft.class_9887;

abstract class AbstractEnergyAdjustRecipe
implements class_3955 {
    private final class_7710 category;
    private class_9887 ingredientPlacement;

    protected AbstractEnergyAdjustRecipe(class_7710 category) {
        this.category = category;
    }

    public boolean matches(class_9694 input, class_1937 world) {
        int liquid = 0;
        int honeycomb = 0;
        int sugar = 0;
        int flowerMix = 0;
        int fungus = 0;
        int nonEmpty = 0;
        for (int i = 0; i < input.method_59983(); ++i) {
            class_1799 stack = input.method_59984(i);
            if (stack.method_7960()) continue;
            ++nonEmpty;
            if (NonPotions.isLiquidBottle(stack)) {
                ++liquid;
                continue;
            }
            if (stack.method_31574(class_1802.field_20414)) {
                ++honeycomb;
                continue;
            }
            if (stack.method_31574(class_1802.field_8479)) {
                ++sugar;
                continue;
            }
            if (stack.method_31574(NeedsOfNature.FLOWER_MIX)) {
                ++flowerMix;
                continue;
            }
            if (this.isRequiredFungus(stack)) {
                ++fungus;
                continue;
            }
            return false;
        }
        return nonEmpty == 5 && liquid == 1 && honeycomb == 1 && sugar == 1 && flowerMix == 1 && fungus == 1;
    }

    public class_1799 craft(class_9694 input, class_7225.class_7874 registries) {
        return this.getResultStack().method_7972();
    }

    public class_7710 method_45441() {
        return this.category;
    }

    public class_9887 method_61671() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = class_9887.method_61686(List.of(class_1856.method_8101((class_1935)class_1802.field_8574), class_1856.method_8101((class_1935)class_1802.field_20414), class_1856.method_8101((class_1935)class_1802.field_8479), class_1856.method_8101((class_1935)NeedsOfNature.FLOWER_MIX), class_1856.method_8101((class_1935)this.getRequiredFungusItem())));
        }
        return this.ingredientPlacement;
    }

    public List<class_10295> method_64664() {
        class_10302.class_10307 liquidDisplay = new class_10302.class_10307(AbstractEnergyAdjustRecipe.createLiquidDisplayStack());
        class_10302 honeycombDisplay = class_1856.method_8101((class_1935)class_1802.field_20414).method_64673();
        class_10302 sugarDisplay = class_1856.method_8101((class_1935)class_1802.field_8479).method_64673();
        class_10302 flowerMixDisplay = class_1856.method_8101((class_1935)NeedsOfNature.FLOWER_MIX).method_64673();
        class_10302 fungusDisplay = class_1856.method_8101((class_1935)this.getRequiredFungusItem()).method_64673();
        return List.of(new class_10301(List.of(liquidDisplay, honeycombDisplay, sugarDisplay, flowerMixDisplay, fungusDisplay), (class_10302)new class_10302.class_10307(this.getResultStack()), (class_10302)new class_10302.class_10306(class_1802.field_8465)));
    }

    public class_2371<class_1799> method_17704(class_9694 input) {
        class_2371 remainders = class_2371.method_10213((int)input.method_59983(), (Object)class_1799.field_8037);
        for (int i = 0; i < input.method_59983(); ++i) {
            class_1799 stack = input.method_59984(i);
            if (stack.method_7960()) continue;
            if (NonPotions.isLiquidBottle(stack)) {
                remainders.set(i, (Object)new class_1799((class_1935)class_1802.field_8469));
                continue;
            }
            remainders.set(i, (Object)stack.method_7909().method_7858());
        }
        return remainders;
    }

    private static class_1799 createLiquidDisplayStack() {
        class_6880 liquidPotion = class_7923.field_41179.method_47983((Object)NonPotions.LIQUID);
        class_1799 stack = new class_1799((class_1935)class_1802.field_8574);
        stack.method_57379(class_9334.field_49651, (Object)new class_1844(liquidPotion));
        return stack;
    }

    protected abstract class_1792 getRequiredFungusItem();

    protected abstract boolean isRequiredFungus(class_1799 var1);

    protected abstract class_1799 getResultStack();

    public abstract class_1865<? extends AbstractEnergyAdjustRecipe> method_8119();
}

