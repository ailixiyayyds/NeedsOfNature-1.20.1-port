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
 *  net.minecraft.server.PlayerConfigEntry
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.server.command.ServerCommandSource
 *  net.minecraft.server.command.CommandManager
 *  net.minecraft.command.argument.EntityArgumentType
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.registry.Registries
 *  net.minecraft.network.packet.CustomPayload
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
import com.nonid.network.NonServerNetworking;
import java.util.List;
import java.util.Locale;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.Registries;

final class NonCommands {
    private NonCommands() {
    }

    static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal((String)"needsofnature").requires(NonCommands::isOpCommandSource)).then(((LiteralArgumentBuilder)CommandManager.literal((String)"getplayerinfo").executes(ctx -> {
            ServerPlayerEntity self = ((ServerCommandSource)ctx.getSource()).getPlayer();
            if (self == null) {
                ((ServerCommandSource)ctx.getSource()).sendError((Text)Text.literal((String)"Usage from console: /needsofnature getplayerinfo <player>"));
                return 0;
            }
            return NonCommands.sendPlayerInfo((ServerCommandSource)ctx.getSource(), self);
        })).then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.sendPlayerInfo((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player")))))).then(CommandManager.literal((String)"resetmobcooldowns").executes(ctx -> NonCommands.resetMobCooldowns((ServerCommandSource)ctx.getSource())))).then(((LiteralArgumentBuilder)CommandManager.literal((String)"skin").then(NonCommands.messCommand())).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal((String)"ripped").then(CommandManager.literal((String)"info").executes(ctx -> NonCommands.requestRippedSkinInfo((ServerCommandSource)ctx.getSource())))).then(((LiteralArgumentBuilder)CommandManager.literal((String)"reset").executes(ctx -> {
            ServerPlayerEntity self = ((ServerCommandSource)ctx.getSource()).getPlayer();
            if (self == null) {
                ((ServerCommandSource)ctx.getSource()).sendError((Text)Text.literal((String)"Usage from console: /needsofnature skin ripped reset <player>"));
                return 0;
            }
            return NonCommands.resetDestroyedSkinCommand((ServerCommandSource)ctx.getSource(), self);
        })).then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.resetDestroyedSkinCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player")))))).then(CommandManager.literal((String)"set").then(((RequiredArgumentBuilder)CommandManager.argument((String)"stage", (ArgumentType)IntegerArgumentType.integer((int)0, (int)4)).executes(ctx -> {
            ServerPlayerEntity self = ((ServerCommandSource)ctx.getSource()).getPlayer();
            if (self == null) {
                ((ServerCommandSource)ctx.getSource()).sendError((Text)Text.literal((String)"Usage from console: /needsofnature skin ripped set <stage> <player>"));
                return 0;
            }
            return NonCommands.setDestroyedSkinStageCommand((ServerCommandSource)ctx.getSource(), self, IntegerArgumentType.getInteger((CommandContext)ctx, (String)"stage"));
        })).then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.setDestroyedSkinStageCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"stage"))))))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal((String)"gender").requires(NonCommands::isOpCommandSource)).then(CommandManager.literal((String)"get").then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.getGenderCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player")))))).then(CommandManager.literal((String)"set").then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).then(CommandManager.argument((String)"gender", (ArgumentType)StringArgumentType.word()).executes(ctx -> NonCommands.setGenderCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"gender"))))))).then(CommandManager.literal((String)"reset").then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.resetGenderCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player"))))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal((String)"pregnancy").then(CommandManager.literal((String)"set").then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).then(((RequiredArgumentBuilder)CommandManager.argument((String)"entity", (ArgumentType)StringArgumentType.string()).executes(ctx -> NonCommands.setPregnancyCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"entity"), NeedsOfNature.getConfig().getPregnancyDurationMinutes(), 0))).then(((RequiredArgumentBuilder)CommandManager.argument((String)"minutes", (ArgumentType)IntegerArgumentType.integer((int)1, (int)5256000)).executes(ctx -> NonCommands.setPregnancyCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"entity"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"minutes"), 0))).then(CommandManager.argument((String)"spawns", (ArgumentType)IntegerArgumentType.integer((int)1, (int)16)).executes(ctx -> NonCommands.setPregnancyCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"entity"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"minutes"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"spawns"))))))))).then(((LiteralArgumentBuilder)CommandManager.literal((String)"clear").executes(ctx -> {
            ServerPlayerEntity self = ((ServerCommandSource)ctx.getSource()).getPlayer();
            if (self == null) {
                ((ServerCommandSource)ctx.getSource()).sendError((Text)Text.literal((String)"Usage from console: /needsofnature pregnancy clear <player>"));
                return 0;
            }
            return NonCommands.clearPregnancyCommand((ServerCommandSource)ctx.getSource(), self);
        })).then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.clearPregnancyCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player")))))).then(((LiteralArgumentBuilder)CommandManager.literal((String)"complete").executes(ctx -> {
            ServerPlayerEntity self = ((ServerCommandSource)ctx.getSource()).getPlayer();
            if (self == null) {
                ((ServerCommandSource)ctx.getSource()).sendError((Text)Text.literal((String)"Usage from console: /needsofnature pregnancy complete <player>"));
                return 0;
            }
            return NonCommands.completePregnancyCommand((ServerCommandSource)ctx.getSource(), self);
        })).then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.completePregnancyCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player")))))).then(((LiteralArgumentBuilder)CommandManager.literal((String)"get").executes(ctx -> {
            ServerPlayerEntity self = ((ServerCommandSource)ctx.getSource()).getPlayer();
            if (self == null) {
                ((ServerCommandSource)ctx.getSource()).sendError((Text)Text.literal((String)"Usage from console: /needsofnature pregnancy get <player>"));
                return 0;
            }
            return NonCommands.sendPregnantEntity((ServerCommandSource)ctx.getSource(), self);
        })).then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.sendPregnantEntity((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player"))))))));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> messCommand() {
        return (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal((String)"mess").requires(NonCommands::isOpCommandSource)).then(((LiteralArgumentBuilder)CommandManager.literal((String)"clean").executes(ctx -> {
            ServerPlayerEntity self = ((ServerCommandSource)ctx.getSource()).getPlayer();
            if (self == null) {
                ((ServerCommandSource)ctx.getSource()).sendError((Text)Text.literal((String)"Usage from console: /needsofnature skin mess clean <player>"));
                return 0;
            }
            return NonCommands.cleanMessCommand((ServerCommandSource)ctx.getSource(), self);
        })).then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.cleanMessCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player")))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal((String)"set").then(CommandManager.argument((String)"v_value", (ArgumentType)IntegerArgumentType.integer((int)0, (int)10)).then(CommandManager.argument((String)"a_value", (ArgumentType)IntegerArgumentType.integer((int)0, (int)10)).then(((RequiredArgumentBuilder)CommandManager.argument((String)"m_value", (ArgumentType)IntegerArgumentType.integer((int)0, (int)10)).executes(ctx -> {
            ServerPlayerEntity self = ((ServerCommandSource)ctx.getSource()).getPlayer();
            if (self == null) {
                ((ServerCommandSource)ctx.getSource()).sendError((Text)Text.literal((String)"Usage from console: /needsofnature skin mess set <v> <a> <m> <player>"));
                return 0;
            }
            return NonCommands.setMessCommand((ServerCommandSource)ctx.getSource(), self, IntegerArgumentType.getInteger((CommandContext)ctx, (String)"v_value"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"a_value"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"m_value"));
        })).then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.setMessCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"v_value"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"a_value"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"m_value")))))))).then(NonCommands.messSlotSetCommand("v"))).then(NonCommands.messSlotSetCommand("a"))).then(NonCommands.messSlotSetCommand("m")));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> messSlotSetCommand(String slot) {
        return (LiteralArgumentBuilder)CommandManager.literal((String)slot).then(((RequiredArgumentBuilder)CommandManager.argument((String)"value", (ArgumentType)IntegerArgumentType.integer((int)0, (int)10)).executes(ctx -> {
            ServerPlayerEntity self = ((ServerCommandSource)ctx.getSource()).getPlayer();
            if (self == null) {
                ((ServerCommandSource)ctx.getSource()).sendError((Text)Text.literal((String)("Usage from console: /needsofnature skin mess set " + slot + " <value> <player>")));
                return 0;
            }
            return NonCommands.setMessSlotCommand((ServerCommandSource)ctx.getSource(), self, slot, IntegerArgumentType.getInteger((CommandContext)ctx, (String)"value"));
        })).then(CommandManager.argument((String)"player", (ArgumentType)EntityArgumentType.player()).executes(ctx -> NonCommands.setMessSlotCommand((ServerCommandSource)ctx.getSource(), EntityArgumentType.getPlayer((CommandContext)ctx, (String)"player"), slot, IntegerArgumentType.getInteger((CommandContext)ctx, (String)"value")))));
    }

    private static int sendPlayerInfo(ServerCommandSource source, ServerPlayerEntity target) {
        int result = NonCommands.sendPlayerEnergy(source, target);
        return result + NonCommands.sendPlayerCooldowns(source, target);
    }

    private static int sendPlayerEnergy(ServerCommandSource source, ServerPlayerEntity target) {
        if (!(target instanceof EnergyHolder)) {
            source.sendError((Text)Text.literal((String)"Target does not expose energy data."));
            return 0;
        }
        EnergyHolder holder = (EnergyHolder)target;
        int current = holder.getEnergy();
        int max = holder.getMaxEnergy();
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " energy: " + current + "/" + max)), false);
        return current;
    }

    private static int sendPlayerCooldowns(ServerCommandSource source, ServerPlayerEntity target) {
        if (!(target.getEntityWorld() instanceof ServerWorld world)) {
            source.sendError((Text)Text.literal((String)"Target is not in a server world."));
            return 0;
        }
        List<String> cooldowns = NeedsOfNature.getActivePlayerCooldownDebugLines(world, target);
        if (cooldowns.isEmpty()) {
            source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " cooldowns: none")), false);
            return 0;
        }
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " cooldowns:")), false);
        for (String line : cooldowns) {
            source.sendFeedback(() -> Text.literal((String)("- " + line)), false);
        }
        return cooldowns.size();
    }

    private static int requestRippedSkinInfo(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError((Text)Text.literal((String)"Usage from console: /needsofnature skin ripped info as a player"));
            return 0;
        }
        NonServerNetworking.send(player, new SkinRippedInfoRequestS2CPayload());
        source.sendFeedback(() -> Text.literal((String)"Requested local ripped skin info output."), false);
        return 1;
    }

    private static boolean isOpCommandSource(ServerCommandSource source) {
        if (source == null) {
            return false;
        }
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            return true;
        }
        return source.getServer().getPlayerManager().isOperator(player.getGameProfile());
    }

    private static int resetMobCooldowns(ServerCommandSource source) {
        int failsafeEntries = NeedsOfNature.clearAttackOutcomeFailsafeEntries();
        int mobsReset = 0;
        for (ServerWorld world : source.getServer().getWorlds()) {
            for (Entity entity : world.iterateEntities()) {
                MobEntity mob;
                if (!(entity instanceof MobEntity) || !((mob = (MobEntity)entity) instanceof EnergyHolder)) continue;
                EnergyHolder holder = (EnergyHolder)mob;
                holder.onNonAnimationStarted();
                ++mobsReset;
            }
        }
        int total = failsafeEntries + mobsReset;
        String feedback = "Reset mob cooldowns. Failsafe entries: " + failsafeEntries + ", mob partner cooldown sets: " + mobsReset + ".";
        source.sendFeedback(() -> Text.literal((String)feedback), true);
        return total;
    }

    private static int cleanMessCommand(ServerCommandSource source, ServerPlayerEntity target) {
        NonMessSystem.clearMess(target);
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " mess cleared.")), true);
        return 1;
    }

    private static int setMessCommand(ServerCommandSource source, ServerPlayerEntity target, int v, int a, int m) {
        if (!NeedsOfNature.isMessSystemEnabled()) {
            source.sendError((Text)Text.literal((String)"Mess system is disabled."));
            return 0;
        }
        if (!(target instanceof MessHolder)) {
            source.sendError((Text)Text.literal((String)"Target does not expose mess data."));
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

    private static int setMessSlotCommand(ServerCommandSource source, ServerPlayerEntity target, String slot, int value) {
        if (!NeedsOfNature.isMessSystemEnabled()) {
            source.sendError((Text)Text.literal((String)"Mess system is disabled."));
            return 0;
        }
        if (!(target instanceof MessHolder)) {
            source.sendError((Text)Text.literal((String)"Target does not expose mess data."));
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
                source.sendError((Text)Text.literal((String)("Unknown mess slot '" + slot + "'. Use v, a, or m.")));
                return 0;
            }
        }
        NonMessSystem.broadcastMessState(target);
        NonCommands.maybeGrantMessedUp(target, holder);
        NonCommands.sendMessSetFeedback(source, target, holder);
        return 1;
    }

    private static void sendMessSetFeedback(ServerCommandSource source, ServerPlayerEntity target, MessHolder holder) {
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " mess set to V=" + NonMessSystem.clampMess(holder.getVMess()) + ", A=" + NonMessSystem.clampMess(holder.getAMess()) + ", M=" + NonMessSystem.clampMess(holder.getMMess()) + ".")), true);
    }

    private static void maybeGrantMessedUp(ServerPlayerEntity target, MessHolder holder) {
        if (NonMessSystem.clampMess(holder.getVMess()) >= 10 && NonMessSystem.clampMess(holder.getAMess()) >= 10 && NonMessSystem.clampMess(holder.getMMess()) >= 10) {
            NonAdvancementHooks.grantMessedUp(target);
        }
    }

    private static int getGenderCommand(ServerCommandSource source, ServerPlayerEntity target) {
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " gender: " + NonCommands.genderName(NonCommands.currentGenderMask(target)))), false);
        return 1;
    }

    private static int setGenderCommand(ServerCommandSource source, ServerPlayerEntity target, String genderRaw) {
        int mask = NonCommands.parseGenderMask(genderRaw);
        if (mask == 0) {
            source.sendError((Text)Text.literal((String)("Unknown gender '" + genderRaw + "'. Use male, female, or both.")));
            return 0;
        }
        int applied = NonGenderSystem.setPlayerGenderFromCommand(target, mask);
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " gender set to " + NonCommands.genderName(applied) + ".")), true);
        return 1;
    }

    private static int resetGenderCommand(ServerCommandSource source, ServerPlayerEntity target) {
        NonGenderSystem.resetPlayerGenderSelection(target);
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " gender selection reset.")), true);
        return 1;
    }

    private static int currentGenderMask(ServerPlayerEntity target) {
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

    private static int setDestroyedSkinStageCommand(ServerCommandSource source, ServerPlayerEntity target, int stage) {
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            source.sendError((Text)Text.literal((String)"Ripped skin system is disabled."));
            return 0;
        }
        int clamped = Math.max(0, Math.min(4, stage));
        if (target instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)target;
            holder.setDestroyedSkinDamage(NonDestroyedSkinSystem.destroyedSkinDamageForStage(clamped));
        }
        NonDestroyedSkinSystem.setDestroyedSkinStage(target, clamped);
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " ripped skin stage set to " + clamped + " (damage " + NonDestroyedSkinSystem.destroyedSkinDamageForStage(clamped) + "/10).")), true);
        return clamped;
    }

    private static int resetDestroyedSkinCommand(ServerCommandSource source, ServerPlayerEntity target) {
        if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
            source.sendError((Text)Text.literal((String)"Ripped skin system is disabled."));
            return 0;
        }
        NonDestroyedSkinSystem.resetDestroyedSkinDamage(target);
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " ripped skin damage reset.")), true);
        return 1;
    }

    private static int sendPregnantEntity(ServerCommandSource source, ServerPlayerEntity target) {
        Identifier pregnantEntity = NeedsOfNature.getPregnantEntityTypeId(target);
        if (pregnantEntity == null) {
            source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " is not pregnant.")), false);
            return 0;
        }
        source.sendFeedback(() -> Text.literal((String)(target.getName().getString() + " pregnant entity: " + String.valueOf(pregnantEntity))), false);
        return 1;
    }

    private static int setPregnancyCommand(ServerCommandSource source, ServerPlayerEntity target, String entityIdRaw, int minutes, int offspringCountOverride) {
        if (NonGenderSystem.isOnlyMale((Entity)target)) {
            source.sendError((Text)Text.literal((String)"Cannot birth male-only players."));
            return 0;
        }
        Identifier entityId = Identifier.tryParse((String)entityIdRaw);
        if (entityId == null || !Registries.ENTITY_TYPE.containsId(entityId)) {
            source.sendError((Text)Text.literal((String)("Unknown entity id: " + entityIdRaw)));
            return 0;
        }
        if (!(target.getEntityWorld() instanceof ServerWorld world)) {
            source.sendError((Text)Text.literal((String)"Target is not in a server world."));
            return 0;
        }
        long now = NeedsOfNature.getGlobalTick(world);
        long durationTicks = Math.max(1L, Math.min(5256000L, (long)minutes)) * 1200L;
        NeedsOfNature.beginPregnancy(target, entityId, now + durationTicks, null, offspringCountOverride);
        int resolvedDisplaySpawnCount = ((PregnancyHolder)target).getPregnancyOffspringCount();
        if (resolvedDisplaySpawnCount <= 0) {
            resolvedDisplaySpawnCount = offspringCountOverride > 0 ? Math.max(1, Math.min(16, offspringCountOverride)) : 1;
        }
        int displaySpawnCount = resolvedDisplaySpawnCount;
        source.sendFeedback(() -> Text.literal((String)("Set pregnancy for " + target.getName().getString() + " to " + String.valueOf(entityId) + " (" + minutes + " minutes, " + displaySpawnCount + " spawn" + (displaySpawnCount == 1 ? "" : "s") + ").")), true);
        return 1;
    }

    private static int clearPregnancyCommand(ServerCommandSource source, ServerPlayerEntity target) {
        NeedsOfNature.clearPregnancyState(target, true);
        source.sendFeedback(() -> Text.literal((String)("Cleared pregnancy for " + target.getName().getString() + ".")), true);
        return 1;
    }

    private static int completePregnancyCommand(ServerCommandSource source, ServerPlayerEntity target) {
        Identifier pregnantEntity = NeedsOfNature.getPregnantEntityTypeId(target);
        if (pregnantEntity == null) {
            source.sendError((Text)Text.literal((String)(target.getName().getString() + " is not pregnant.")));
            return 0;
        }
        if (!(target.getEntityWorld() instanceof ServerWorld world)) {
            source.sendError((Text)Text.literal((String)"Target is not in a server world."));
            return 0;
        }
        NeedsOfNature.markPregnancyPending(target, NeedsOfNature.getGlobalTick(world));
        source.sendFeedback(() -> Text.literal((String)("Marked pregnancy complete for " + target.getName().getString() + ".")), true);
        return 1;
    }
}

