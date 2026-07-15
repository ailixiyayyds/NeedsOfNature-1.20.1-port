/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10295
 *  net.minecraft.class_10300
 *  net.minecraft.class_10302
 *  net.minecraft.class_10302$class_10306
 *  net.minecraft.class_10302$class_10307
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1844
 *  net.minecraft.class_1856
 *  net.minecraft.class_1865
 *  net.minecraft.class_1935
 *  net.minecraft.class_1937
 *  net.minecraft.class_2371
 *  net.minecraft.class_2960
 *  net.minecraft.class_3955
 *  net.minecraft.class_6880
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_7710
 *  net.minecraft.class_7923
 *  net.minecraft.class_9334
 *  net.minecraft.class_9694
 *  net.minecraft.class_9887
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.recipe;

import com.nonid.NeedsOfNature;
import com.nonid.item.EnergyStabilizerItem;
import com.nonid.potion.NonPotions;
import com.nonid.recipe.NonRecipeSerializers;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_10295;
import net.minecraft.class_10300;
import net.minecraft.class_10302;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_1856;
import net.minecraft.class_1865;
import net.minecraft.class_1935;
import net.minecraft.class_1937;
import net.minecraft.class_2371;
import net.minecraft.class_2960;
import net.minecraft.class_3955;
import net.minecraft.class_6880;
import net.minecraft.class_7225;
import net.minecraft.class_7710;
import net.minecraft.class_7923;
import net.minecraft.class_9334;
import net.minecraft.class_9694;
import net.minecraft.class_9887;
import org.jetbrains.annotations.Nullable;

public final class EnergyStabilizerRecipe
implements class_3955 {
    private final class_7710 category;
    private class_9887 ingredientPlacement;

    public EnergyStabilizerRecipe(class_7710 category) {
        this.category = category;
    }

    public boolean matches(class_9694 input, class_1937 world) {
        return EnergyStabilizerRecipe.findTargetEntity(input) != null;
    }

    public class_1799 craft(class_9694 input, class_7225.class_7874 registries) {
        class_2960 target = EnergyStabilizerRecipe.findTargetEntity(input);
        if (target == null) {
            return class_1799.field_8037;
        }
        return EnergyStabilizerItem.createStackForEntity(target);
    }

    public class_7710 method_45441() {
        return this.category;
    }

    public class_1865<? extends class_3955> method_8119() {
        return NonRecipeSerializers.ENERGY_STABILIZER_RECIPE;
    }

    public class_9887 method_61671() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = class_9887.method_61683(List.of(Optional.of(class_1856.method_8101((class_1935)class_1802.field_8574)), Optional.of(class_1856.method_8101((class_1935)class_1802.field_21988)), Optional.of(class_1856.method_8101((class_1935)class_1802.field_8574)), Optional.of(class_1856.method_8101((class_1935)NeedsOfNature.ENERGY_DIMINISHER)), Optional.of(class_1856.method_8101((class_1935)class_1802.field_28659)), Optional.of(class_1856.method_8101((class_1935)NeedsOfNature.ENERGY_DIMINISHER)), Optional.of(class_1856.method_8101((class_1935)class_1802.field_8574)), Optional.of(class_1856.method_8101((class_1935)class_1802.field_21988)), Optional.of(class_1856.method_8101((class_1935)class_1802.field_8574))));
        }
        return this.ingredientPlacement;
    }

    public List<class_10295> method_64664() {
        class_10302.class_10307 liquidDisplay = new class_10302.class_10307(EnergyStabilizerRecipe.createLiquidDisplayStack());
        class_10302 fungusDisplay = class_1856.method_8101((class_1935)class_1802.field_21988).method_64673();
        class_10302 diminisherDisplay = class_1856.method_8101((class_1935)NeedsOfNature.ENERGY_DIMINISHER).method_64673();
        class_10302 glowBerryDisplay = class_1856.method_8101((class_1935)class_1802.field_28659).method_64673();
        class_10302.class_10307 resultDisplay = new class_10302.class_10307(EnergyStabilizerItem.createStackForEntity(null));
        return List.of(new class_10300(3, 3, List.of(liquidDisplay, fungusDisplay, liquidDisplay, diminisherDisplay, glowBerryDisplay, diminisherDisplay, liquidDisplay, fungusDisplay, liquidDisplay), (class_10302)resultDisplay, (class_10302)new class_10302.class_10306(class_1802.field_8465)));
    }

    public class_2371<class_1799> method_17704(class_9694 input) {
        class_2371 remainders = class_2371.method_10213((int)input.method_59983(), (Object)class_1799.field_8037);
        for (int i = 0; i < input.method_59983(); ++i) {
            class_1799 stack = input.method_59984(i);
            if (stack.method_7960()) continue;
            if (EnergyStabilizerRecipe.isCorner(i, input.method_59991()) && NonPotions.isLiquidBottle(stack)) {
                remainders.set(i, (Object)new class_1799((class_1935)class_1802.field_8469));
                continue;
            }
            remainders.set(i, (Object)stack.method_7909().method_7858());
        }
        return remainders;
    }

    @Nullable
    private static class_2960 findTargetEntity(class_9694 input) {
        if (input.method_59991() != 3 || input.method_59992() != 3) {
            return null;
        }
        int[] corners = new int[]{0, 2, 6, 8};
        class_2960 entityTypeId = null;
        for (int corner : corners) {
            class_1799 liquidStack = input.method_59984(corner);
            class_2960 current = NonPotions.getLiquidBottleEntityTypeId(liquidStack);
            if (current == null) {
                return null;
            }
            if (entityTypeId == null) {
                entityTypeId = current;
                continue;
            }
            if (entityTypeId.equals((Object)current)) continue;
            return null;
        }
        if (!input.method_59984(1).method_31574(class_1802.field_21988)) {
            return null;
        }
        if (!input.method_59984(7).method_31574(class_1802.field_21988)) {
            return null;
        }
        if (!input.method_59984(3).method_31574(NeedsOfNature.ENERGY_DIMINISHER)) {
            return null;
        }
        if (!input.method_59984(5).method_31574(NeedsOfNature.ENERGY_DIMINISHER)) {
            return null;
        }
        if (!input.method_59984(4).method_31574(class_1802.field_28659)) {
            return null;
        }
        return entityTypeId;
    }

    private static boolean isCorner(int slot, int width) {
        if (width != 3) {
            return false;
        }
        return slot == 0 || slot == 2 || slot == 6 || slot == 8;
    }

    private static class_1799 createLiquidDisplayStack() {
        class_6880 liquidPotion = class_7923.field_41179.method_47983((Object)NonPotions.LIQUID);
        class_1799 stack = new class_1799((class_1935)class_1802.field_8574);
        stack.method_57379(class_9334.field_49651, (Object)new class_1844(liquidPotion));
        return stack;
    }
}

