/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1299
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1792
 *  net.minecraft.class_1792$class_1793
 *  net.minecraft.class_1799
 *  net.minecraft.class_1935
 *  net.minecraft.class_1937
 *  net.minecraft.class_2394
 *  net.minecraft.class_2398
 *  net.minecraft.class_2487
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 *  net.minecraft.class_5354
 *  net.minecraft.class_7923
 *  net.minecraft.class_9279
 *  net.minecraft.class_9331
 *  net.minecraft.class_9334
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.item;

import com.nonid.EnergyHolder;
import com.nonid.NeedsOfNature;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1299;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1935;
import net.minecraft.class_1937;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_2487;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_5354;
import net.minecraft.class_7923;
import net.minecraft.class_9279;
import net.minecraft.class_9331;
import net.minecraft.class_9334;
import org.jetbrains.annotations.Nullable;

public class EnergyStabilizerItem
extends class_1792 {
    private static final String STABILIZER_ENTITY_DATA_KEY = "stabilizer_entity";

    public EnergyStabilizerItem(class_1792.class_1793 settings) {
        super(settings);
    }

    public class_2561 method_7864(class_1799 stack) {
        class_2960 target = EnergyStabilizerItem.getTargetEntityTypeId(stack);
        if (target == null) {
            return super.method_7864(stack);
        }
        class_2561 entityName = class_7923.field_41177.method_17966(target).map(class_1299::method_5897).orElseGet(() -> class_2561.method_43470((String)target.toString()));
        return class_2561.method_43469((String)"item.needsofnature.entity_energy_stabilizer", (Object[])new Object[]{entityName});
    }

    public class_1269 method_7847(class_1799 stack, class_1657 user, class_1309 entity, class_1268 hand) {
        class_1937 class_19372;
        class_1308 mob;
        if (!(entity instanceof class_1308) || !((mob = (class_1308)entity) instanceof EnergyHolder)) {
            return class_1269.field_5811;
        }
        class_2960 target = EnergyStabilizerItem.getTargetEntityTypeId(stack);
        if (target == null) {
            return class_1269.field_5814;
        }
        class_2960 mobType = class_7923.field_41177.method_10221((Object)mob.method_5864());
        if (!target.equals((Object)mobType)) {
            return class_1269.field_5814;
        }
        if (mob.method_5752().contains("non.energy_stabilized")) {
            return class_1269.field_5814;
        }
        if (user.method_73183().method_8608()) {
            return class_1269.field_5812;
        }
        EnergyHolder holder = (EnergyHolder)mob;
        mob.method_5780("non.energy_stabilized");
        holder.setEnergy(Math.min(holder.getEnergy(), 199));
        mob.method_5980(null);
        if (mob instanceof class_5354) {
            class_5354 angerable = (class_5354)mob;
            angerable.method_29922();
        }
        if ((class_19372 = user.method_73183()) instanceof class_3218) {
            class_3218 serverWorld = (class_3218)class_19372;
            EnergyStabilizerItem.playFeedback(serverWorld, mob);
        }
        if (!user.method_56992()) {
            stack.method_7934(1);
        }
        return class_1269.field_5812;
    }

    public static class_1799 createStackForEntity(@Nullable class_2960 entityTypeId) {
        class_1799 stack = new class_1799((class_1935)NeedsOfNature.ENERGY_STABILIZER);
        if (entityTypeId != null) {
            EnergyStabilizerItem.setTargetEntityTypeId(stack, entityTypeId);
        }
        return stack;
    }

    public static void setTargetEntityTypeId(class_1799 stack, @Nullable class_2960 entityTypeId) {
        if (stack == null || stack.method_7960()) {
            return;
        }
        class_9279.method_57452((class_9331)class_9334.field_49628, (class_1799)stack, nbt -> {
            String value = entityTypeId == null ? "" : entityTypeId.toString();
            nbt.method_10582(STABILIZER_ENTITY_DATA_KEY, value);
        });
    }

    @Nullable
    public static class_2960 getTargetEntityTypeId(class_1799 stack) {
        if (stack == null || stack.method_7960() || !stack.method_31574(NeedsOfNature.ENERGY_STABILIZER)) {
            return null;
        }
        class_9279 custom = (class_9279)stack.method_58694(class_9334.field_49628);
        if (custom == null || custom.method_57458()) {
            return null;
        }
        class_2487 nbt = custom.method_57461();
        if (!nbt.method_10545(STABILIZER_ENTITY_DATA_KEY)) {
            return null;
        }
        String raw = nbt.method_68564(STABILIZER_ENTITY_DATA_KEY, "");
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return class_2960.method_12829((String)raw);
    }

    private static void playFeedback(class_3218 world, class_1308 mob) {
        world.method_65096((class_2394)class_2398.field_11207, mob.method_23317(), mob.method_23323(0.65), mob.method_23321(), 12, 0.4, 0.4, 0.4, 0.02);
        world.method_43128(null, mob.method_23317(), mob.method_23318(), mob.method_23321(), class_3417.field_14891, class_3419.field_15254, 0.9f, 1.0f);
    }
}

