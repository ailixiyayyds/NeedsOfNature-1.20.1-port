/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.class_11560
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_2168
 *  net.minecraft.class_2170
 *  net.minecraft.class_2186
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_7923
 *  net.minecraft.class_8710
 */
package com.nonid;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nonid.DestroyedSkinHolder;
import com.nonid.EnergyHolder;
import com.nonid.GenderHolder;
import com.nonid.MessHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonAdvancementHooks;
import com.nonid.NonDestroyedSkinSystem;
import com.nonid.NonGenderSystem;
import com.nonid.NonMessSystem;
import com.nonid.PregnancyHolder;
import com.nonid.network.SkinRippedInfoRequestS2CPayload;
import java.util.List;
import java.util.Locale;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_11560;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_2186;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_7923;
import net.minecraft.class_8710;

final class NonCommands {
    private NonCommands() {
    }

    static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247((String)"needsofnature").requires(NonCommands::isOpCommandSource)).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"getplayerinfo").executes(ctx -> {
            class_3222 self = ((class_2168)ctx.getSource()).method_44023();
            if (self == null) {
                ((class_2168)ctx.getSource()).method_9213((class_2561)class_2561.method_43470((String)"Usage from console: /needsofnature getplayerinfo <player>"));
                return 0;
            }
            return NonCommands.sendPlayerInfo((class_2168)ctx.getSource(), self);
        })).then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.sendPlayerInfo((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player")))))).then(class_2170.method_9247((String)"resetmobcooldowns").executes(ctx -> NonCommands.resetMobCooldowns((class_2168)ctx.getSource())))).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"skin").then(NonCommands.messCommand())).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247((String)"ripped").then(class_2170.method_9247((String)"info").executes(ctx -> NonCommands.requestRippedSkinInfo((class_2168)ctx.getSource())))).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"reset").executes(ctx -> {
            class_3222 self = ((class_2168)ctx.getSource()).method_44023();
            if (self == null) {
                ((class_2168)ctx.getSource()).method_9213((class_2561)class_2561.method_43470((String)"Usage from console: /needsofnature skin ripped reset <player>"));
                return 0;
            }
            return NonCommands.resetDestroyedSkinCommand((class_2168)ctx.getSource(), self);
        })).then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.resetDestroyedSkinCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player")))))).then(class_2170.method_9247((String)"set").then(((RequiredArgumentBuilder)class_2170.method_9244((String)"stage", (ArgumentType)IntegerArgumentType.integer((int)0, (int)4)).executes(ctx -> {
            class_3222 self = ((class_2168)ctx.getSource()).method_44023();
            if (self == null) {
                ((class_2168)ctx.getSource()).method_9213((class_2561)class_2561.method_43470((String)"Usage from console: /needsofnature skin ripped set <stage> <player>"));
                return 0;
            }
            return NonCommands.setDestroyedSkinStageCommand((class_2168)ctx.getSource(), self, IntegerArgumentType.getInteger((CommandContext)ctx, (String)"stage"));
        })).then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.setDestroyedSkinStageCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"stage"))))))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247((String)"gender").requires(NonCommands::isOpCommandSource)).then(class_2170.method_9247((String)"get").then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.getGenderCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player")))))).then(class_2170.method_9247((String)"set").then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).then(class_2170.method_9244((String)"gender", (ArgumentType)StringArgumentType.word()).executes(ctx -> NonCommands.setGenderCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"gender"))))))).then(class_2170.method_9247((String)"reset").then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.resetGenderCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"))))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247((String)"pregnancy").then(class_2170.method_9247((String)"set").then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).then(((RequiredArgumentBuilder)class_2170.method_9244((String)"entity", (ArgumentType)StringArgumentType.string()).executes(ctx -> NonCommands.setPregnancyCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"entity"), NeedsOfNature.getConfig().getPregnancyDurationMinutes(), 0))).then(((RequiredArgumentBuilder)class_2170.method_9244((String)"minutes", (ArgumentType)IntegerArgumentType.integer((int)1, (int)5256000)).executes(ctx -> NonCommands.setPregnancyCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"entity"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"minutes"), 0))).then(class_2170.method_9244((String)"spawns", (ArgumentType)IntegerArgumentType.integer((int)1, (int)16)).executes(ctx -> NonCommands.setPregnancyCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"entity"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"minutes"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"spawns"))))))))).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"clear").executes(ctx -> {
            class_3222 self = ((class_2168)ctx.getSource()).method_44023();
            if (self == null) {
                ((class_2168)ctx.getSource()).method_9213((class_2561)class_2561.method_43470((String)"Usage from console: /needsofnature pregnancy clear <player>"));
                return 0;
            }
            return NonCommands.clearPregnancyCommand((class_2168)ctx.getSource(), self);
        })).then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.clearPregnancyCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player")))))).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"complete").executes(ctx -> {
            class_3222 self = ((class_2168)ctx.getSource()).method_44023();
            if (self == null) {
                ((class_2168)ctx.getSource()).method_9213((class_2561)class_2561.method_43470((String)"Usage from console: /needsofnature pregnancy complete <player>"));
                return 0;
            }
            return NonCommands.completePregnancyCommand((class_2168)ctx.getSource(), self);
        })).then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.completePregnancyCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player")))))).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"get").executes(ctx -> {
            class_3222 self = ((class_2168)ctx.getSource()).method_44023();
            if (self == null) {
                ((class_2168)ctx.getSource()).method_9213((class_2561)class_2561.method_43470((String)"Usage from console: /needsofnature pregnancy get <player>"));
                return 0;
            }
            return NonCommands.sendPregnantEntity((class_2168)ctx.getSource(), self);
        })).then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.sendPregnantEntity((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"))))))));
    }

    private static LiteralArgumentBuilder<class_2168> messCommand() {
        return (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247((String)"mess").requires(NonCommands::isOpCommandSource)).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"clean").executes(ctx -> {
            class_3222 self = ((class_2168)ctx.getSource()).method_44023();
            if (self == null) {
                ((class_2168)ctx.getSource()).method_9213((class_2561)class_2561.method_43470((String)"Usage from console: /needsofnature skin mess clean <player>"));
                return 0;
            }
            return NonCommands.cleanMessCommand((class_2168)ctx.getSource(), self);
        })).then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.cleanMessCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player")))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247((String)"set").then(class_2170.method_9244((String)"v_value", (ArgumentType)IntegerArgumentType.integer((int)0, (int)10)).then(class_2170.method_9244((String)"a_value", (ArgumentType)IntegerArgumentType.integer((int)0, (int)10)).then(((RequiredArgumentBuilder)class_2170.method_9244((String)"m_value", (ArgumentType)IntegerArgumentType.integer((int)0, (int)10)).executes(ctx -> {
            class_3222 self = ((class_2168)ctx.getSource()).method_44023();
            if (self == null) {
                ((class_2168)ctx.getSource()).method_9213((class_2561)class_2561.method_43470((String)"Usage from console: /needsofnature skin mess set <v> <a> <m> <player>"));
                return 0;
            }
            return NonCommands.setMessCommand((class_2168)ctx.getSource(), self, IntegerArgumentType.getInteger((CommandContext)ctx, (String)"v_value"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"a_value"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"m_value"));
        })).then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.setMessCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"v_value"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"a_value"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"m_value")))))))).then(NonCommands.messSlotSetCommand("v"))).then(NonCommands.messSlotSetCommand("a"))).then(NonCommands.messSlotSetCommand("m")));
    }

    private static LiteralArgumentBuilder<class_2168> messSlotSetCommand(String slot) {
        return (LiteralArgumentBuilder)class_2170.method_9247((String)slot).then(((RequiredArgumentBuilder)class_2170.method_9244((String)"value", (ArgumentType)IntegerArgumentType.integer((int)0, (int)10)).executes(ctx -> {
            class_3222 self = ((class_2168)ctx.getSource()).method_44023();
            if (self == null) {
                ((class_2168)ctx.getSource()).method_9213((class_2561)class_2561.method_43470((String)("Usage from console: /needsofnature skin mess set " + slot + " <value> <player>")));
                return 0;
            }
            return NonCommands.setMessSlotCommand((class_2168)ctx.getSource(), self, slot, IntegerArgumentType.getInteger((CommandContext)ctx, (String)"value"));
        })).then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).executes(ctx -> NonCommands.setMessSlotCommand((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), slot, IntegerArgumentType.getInteger((CommandContext)ctx, (String)"value")))));
    }

    private static int sendPlayerInfo(class_2168 source, class_3222 target) {
        int result = NonCommands.sendPlayerEnergy(source, target);
        return result + NonCommands.sendPlayerCooldowns(source, target);
    }

    private static int sendPlayerEnergy(class_2168 source, class_3222 target) {
        if (!(target instanceof EnergyHolder)) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Target does not expose energy data."));
            return 0;
        }
        EnergyHolder holder = (EnergyHolder)target;
        int current = holder.getEnergy();
        int max = holder.getMaxEnergy();
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " energy: " + current + "/" + max)), false);
        return current;
    }

    private static int sendPlayerCooldowns(class_2168 source, class_3222 target) {
        class_3218 class_32182 = target.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Target is not in a server world."));
            return 0;
        }
        class_3218 world = class_32182;
        List<String> cooldowns = NeedsOfNature.getActivePlayerCooldownDebugLines(world, target);
        if (cooldowns.isEmpty()) {
            source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " cooldowns: none")), false);
            return 0;
        }
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " cooldowns:")), false);
        for (String line : cooldowns) {
            source.method_9226(() -> class_2561.method_43470((String)("- " + line)), false);
        }
        return cooldowns.size();
    }

    private static int requestRippedSkinInfo(class_2168 source) {
        class_3222 player = source.method_44023();
        if (player == null) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Usage from console: /needsofnature skin ripped info as a player"));
            return 0;
        }
        ServerPlayNetworking.send((class_3222)player, (class_8710)new SkinRippedInfoRequestS2CPayload());
        source.method_9226(() -> class_2561.method_43470((String)"Requested local ripped skin info output."), false);
        return 1;
    }

    private static boolean isOpCommandSource(class_2168 source) {
        if (source == null) {
            return false;
        }
        class_3222 player = source.method_44023();
        if (player == null) {
            return true;
        }
        return source.method_9211().method_3760().method_14569(new class_11560(player.method_7334()));
    }

    private static int resetMobCooldowns(class_2168 source) {
        int failsafeEntries = NeedsOfNature.clearAttackOutcomeFailsafeEntries();
        int mobsReset = 0;
        for (class_3218 world : source.method_9211().method_3738()) {
            for (class_1297 entity : world.method_27909()) {
                class_1308 mob;
                if (!(entity instanceof class_1308) || !((mob = (class_1308)entity) instanceof EnergyHolder)) continue;
                EnergyHolder holder = (EnergyHolder)mob;
                holder.onNonAnimationStarted();
                ++mobsReset;
            }
        }
        int total = failsafeEntries + mobsReset;
        String feedback = "Reset mob cooldowns. Failsafe entries: " + failsafeEntries + ", mob partner cooldown sets: " + mobsReset + ".";
        source.method_9226(() -> class_2561.method_43470((String)feedback), true);
        return total;
    }

    private static int cleanMessCommand(class_2168 source, class_3222 target) {
        NonMessSystem.clearMess(target);
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " mess cleared.")), true);
        return 1;
    }

    private static int setMessCommand(class_2168 source, class_3222 target, int v, int a, int m) {
        if (!NeedsOfNature.isMessSystemEnabled()) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Mess system is disabled."));
            return 0;
        }
        if (!(target instanceof MessHolder)) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Target does not expose mess data."));
            return 0;
        }
        MessHolder holder = (MessHolder)target;
        holder.setVMess(NonMessSystem.clampMess(v));
        holder.setAMess(NonMessSystem.clampMess(a));
        holder.setMMess(NonMessSystem.clampMess(m));
        NonMessSystem.broadcastMessState(target);
        NonCommands.maybeGrantMessedUp(target, holder);
        NonCommands.sendMessSetFeedback(source, target, holder);
        return 1;
    }

    private static int setMessSlotCommand(class_2168 source, class_3222 target, String slot, int value) {
        if (!NeedsOfNature.isMessSystemEnabled()) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Mess system is disabled."));
            return 0;
        }
        if (!(target instanceof MessHolder)) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Target does not expose mess data."));
            return 0;
        }
        MessHolder holder = (MessHolder)target;
        int clamped = NonMessSystem.clampMess(value);
        switch (slot) {
            case "v": {
                holder.setVMess(clamped);
                break;
            }
            case "a": {
                holder.setAMess(clamped);
                break;
            }
            case "m": {
                holder.setMMess(clamped);
                break;
            }
            default: {
                source.method_9213((class_2561)class_2561.method_43470((String)("Unknown mess slot '" + slot + "'. Use v, a, or m.")));
                return 0;
            }
        }
        NonMessSystem.broadcastMessState(target);
        NonCommands.maybeGrantMessedUp(target, holder);
        NonCommands.sendMessSetFeedback(source, target, holder);
        return 1;
    }

    private static void sendMessSetFeedback(class_2168 source, class_3222 target, MessHolder holder) {
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " mess set to V=" + NonMessSystem.clampMess(holder.getVMess()) + ", A=" + NonMessSystem.clampMess(holder.getAMess()) + ", M=" + NonMessSystem.clampMess(holder.getMMess()) + ".")), true);
    }

    private static void maybeGrantMessedUp(class_3222 target, MessHolder holder) {
        if (NonMessSystem.clampMess(holder.getVMess()) >= 10 && NonMessSystem.clampMess(holder.getAMess()) >= 10 && NonMessSystem.clampMess(holder.getMMess()) >= 10) {
            NonAdvancementHooks.grantMessedUp(target);
        }
    }

    private static int getGenderCommand(class_2168 source, class_3222 target) {
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " gender: " + NonCommands.genderName(NonCommands.currentGenderMask(target)))), false);
        return 1;
    }

    private static int setGenderCommand(class_2168 source, class_3222 target, String genderRaw) {
        int mask = NonCommands.parseGenderMask(genderRaw);
        if (mask == 0) {
            source.method_9213((class_2561)class_2561.method_43470((String)("Unknown gender '" + genderRaw + "'. Use male, female, or both.")));
            return 0;
        }
        int applied = NonGenderSystem.setPlayerGenderFromCommand(target, mask);
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " gender set to " + NonCommands.genderName(applied) + ".")), true);
        return 1;
    }

    private static int resetGenderCommand(class_2168 source, class_3222 target) {
        NonGenderSystem.resetPlayerGenderSelection(target);
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " gender selection reset.")), true);
        return 1;
    }

    private static int currentGenderMask(class_3222 target) {
        if (target instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)target;
            return holder.getGenderMask() & 3;
        }
        return 0;
    }

    private static int parseGenderMask(String raw) {
        if (raw == null) {
            return 0;
        }
        return switch (raw.trim().toLowerCase(Locale.ROOT)) {
            case "m", "male" -> 1;
            case "f", "female" -> 2;
            case "b", "both", "male+female", "female+male" -> 3;
            default -> 0;
        };
    }

    private static String genderName(int mask) {
        return switch (mask & 3) {
            case 1 -> "male";
            case 2 -> "female";
            case 3 -> "male+female";
            default -> "none";
        };
    }

    private static int setDestroyedSkinStageCommand(class_2168 source, class_3222 target, int stage) {
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Ripped skin system is disabled."));
            return 0;
        }
        int clamped = Math.max(0, Math.min(4, stage));
        if (target instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)target;
            holder.setDestroyedSkinDamage(NonDestroyedSkinSystem.destroyedSkinDamageForStage(clamped));
        }
        NonDestroyedSkinSystem.setDestroyedSkinStage(target, clamped);
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " ripped skin stage set to " + clamped + " (damage " + NonDestroyedSkinSystem.destroyedSkinDamageForStage(clamped) + "/10).")), true);
        return clamped;
    }

    private static int resetDestroyedSkinCommand(class_2168 source, class_3222 target) {
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Ripped skin system is disabled."));
            return 0;
        }
        NonDestroyedSkinSystem.resetDestroyedSkinDamage(target);
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " ripped skin damage reset.")), true);
        return 1;
    }

    private static int sendPregnantEntity(class_2168 source, class_3222 target) {
        class_2960 pregnantEntity = NeedsOfNature.getPregnantEntityTypeId(target);
        if (pregnantEntity == null) {
            source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " is not pregnant.")), false);
            return 0;
        }
        source.method_9226(() -> class_2561.method_43470((String)(target.method_5477().getString() + " pregnant entity: " + String.valueOf(pregnantEntity))), false);
        return 1;
    }

    private static int setPregnancyCommand(class_2168 source, class_3222 target, String entityIdRaw, int minutes, int offspringCountOverride) {
        if (NonGenderSystem.isOnlyMale((class_1297)target)) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Cannot birth male-only players."));
            return 0;
        }
        class_2960 entityId = class_2960.method_12829((String)entityIdRaw);
        if (entityId == null || !class_7923.field_41177.method_10250(entityId)) {
            source.method_9213((class_2561)class_2561.method_43470((String)("Unknown entity id: " + entityIdRaw)));
            return 0;
        }
        class_3218 class_32182 = target.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Target is not in a server world."));
            return 0;
        }
        class_3218 world = class_32182;
        long now = NeedsOfNature.getGlobalTick(world);
        long durationTicks = Math.max(1L, Math.min(5256000L, (long)minutes)) * 1200L;
        NeedsOfNature.beginPregnancy(target, entityId, now + durationTicks, null, offspringCountOverride);
        int resolvedDisplaySpawnCount = ((PregnancyHolder)target).getPregnancyOffspringCount();
        if (resolvedDisplaySpawnCount <= 0) {
            resolvedDisplaySpawnCount = offspringCountOverride > 0 ? Math.max(1, Math.min(16, offspringCountOverride)) : 1;
        }
        int displaySpawnCount = resolvedDisplaySpawnCount;
        source.method_9226(() -> class_2561.method_43470((String)("Set pregnancy for " + target.method_5477().getString() + " to " + String.valueOf(entityId) + " (" + minutes + " minutes, " + displaySpawnCount + " spawn" + (displaySpawnCount == 1 ? "" : "s") + ").")), true);
        return 1;
    }

    private static int clearPregnancyCommand(class_2168 source, class_3222 target) {
        NeedsOfNature.clearPregnancyState(target, true);
        source.method_9226(() -> class_2561.method_43470((String)("Cleared pregnancy for " + target.method_5477().getString() + ".")), true);
        return 1;
    }

    private static int completePregnancyCommand(class_2168 source, class_3222 target) {
        class_2960 pregnantEntity = NeedsOfNature.getPregnantEntityTypeId(target);
        if (pregnantEntity == null) {
            source.method_9213((class_2561)class_2561.method_43470((String)(target.method_5477().getString() + " is not pregnant.")));
            return 0;
        }
        class_3218 class_32182 = target.method_51469();
        if (!(class_32182 instanceof class_3218)) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Target is not in a server world."));
            return 0;
        }
        class_3218 world = class_32182;
        NeedsOfNature.markPregnancyPending(target, NeedsOfNature.getGlobalTick(world));
        source.method_9226(() -> class_2561.method_43470((String)("Marked pregnancy complete for " + target.method_5477().getString() + ".")), true);
        return 1;
    }
}

