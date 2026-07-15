/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10295
 *  net.minecraft.class_10301
 *  net.minecraft.class_10302
 *  net.minecraft.class_10302$class_10306
 *  net.minecraft.class_10302$class_10307
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1842
 *  net.minecraft.class_1844
 *  net.minecraft.class_1856
 *  net.minecraft.class_1865
 *  net.minecraft.class_1935
 *  net.minecraft.class_1937
 *  net.minecraft.class_2371
 *  net.minecraft.class_3955
 *  net.minecraft.class_4174
 *  net.minecraft.class_4174$class_4175
 *  net.minecraft.class_6880
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_7710
 *  net.minecraft.class_7923
 *  net.minecraft.class_9334
 *  net.minecraft.class_9694
 *  net.minecraft.class_9887
 */
package com.nonid.recipe;

import com.nonid.potion.NonPotions;
import com.nonid.recipe.NonRecipeSerializers;
import java.util.List;
import net.minecraft.class_10295;
import net.minecraft.class_10301;
import net.minecraft.class_10302;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_1856;
import net.minecraft.class_1865;
import net.minecraft.class_1935;
import net.minecraft.class_1937;
import net.minecraft.class_2371;
import net.minecraft.class_3955;
import net.minecraft.class_4174;
import net.minecraft.class_6880;
import net.minecraft.class_7225;
import net.minecraft.class_7710;
import net.minecraft.class_7923;
import net.minecraft.class_9334;
import net.minecraft.class_9694;
import net.minecraft.class_9887;

public final class FertileNectarRecipe
implements class_3955 {
    private static final class_4174 FERTILE_NECTAR_FOOD = new class_4174.class_4175().method_19238(7).method_19237(0.0f).method_19240().method_19242();
    private final class_7710 category;
    private class_9887 ingredientPlacement;

    public FertileNectarRecipe(class_7710 category) {
        this.category = category;
    }

    public boolean matches(class_9694 input, class_1937 world) {
        int liquid = 0;
        int honeyBottle = 0;
        int nonEmpty = 0;
        for (int i = 0; i < input.method_59983(); ++i) {
            class_1799 stack = input.method_59984(i);
            if (stack.method_7960()) continue;
            ++nonEmpty;
            if (NonPotions.isLiquidBottle(stack)) {
                ++liquid;
                continue;
            }
            if (stack.method_31574(class_1802.field_20417)) {
                ++honeyBottle;
                continue;
            }
            return false;
        }
        return nonEmpty == 2 && liquid == 1 && honeyBottle == 1;
    }

    public class_1799 craft(class_9694 input, class_7225.class_7874 registries) {
        return FertileNectarRecipe.createPotionStack(NonPotions.FERTILE_NECTAR);
    }

    public class_7710 method_45441() {
        return this.category;
    }

    public class_9887 method_61671() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = class_9887.method_61686(List.of(class_1856.method_8101((class_1935)class_1802.field_8574), class_1856.method_8101((class_1935)class_1802.field_20417)));
        }
        return this.ingredientPlacement;
    }

    public List<class_10295> method_64664() {
        class_10302.class_10307 liquidDisplay = new class_10302.class_10307(FertileNectarRecipe.createPotionStack(NonPotions.LIQUID));
        class_10302 honeyDisplay = class_1856.method_8101((class_1935)class_1802.field_20417).method_64673();
        class_10302.class_10307 resultDisplay = new class_10302.class_10307(FertileNectarRecipe.createPotionStack(NonPotions.FERTILE_NECTAR));
        return List.of(new class_10301(List.of(liquidDisplay, honeyDisplay), (class_10302)resultDisplay, (class_10302)new class_10302.class_10306(class_1802.field_8465)));
    }

    public class_2371<class_1799> method_17704(class_9694 input) {
        class_2371 remainders = class_2371.method_10213((int)input.method_59983(), (Object)class_1799.field_8037);
        for (int i = 0; i < input.method_59983(); ++i) {
            class_1799 stack = input.method_59984(i);
            if (stack.method_7960() || NonPotions.isLiquidBottle(stack)) continue;
            remainders.set(i, (Object)stack.method_7909().method_7858());
        }
        return remainders;
    }

    public class_1865<? extends class_3955> method_8119() {
        return NonRecipeSerializers.FERTILE_NECTAR_RECIPE;
    }

    private static class_1799 createPotionStack(class_1842 potion) {
        class_6880 potionEntry = class_7923.field_41179.method_47983((Object)potion);
        class_1799 stack = new class_1799((class_1935)class_1802.field_8574);
        stack.method_57379(class_9334.field_49651, (Object)new class_1844(potionEntry));
        if (potion == NonPotions.FERTILE_NECTAR) {
            stack.method_57379(class_9334.field_50075, (Object)FERTILE_NECTAR_FOOD);
            stack.method_57379(class_9334.field_50071, (Object)16);
            NonPotions.applyHoneyBottleDrinkSound(stack);
        }
        return stack;
    }
}

