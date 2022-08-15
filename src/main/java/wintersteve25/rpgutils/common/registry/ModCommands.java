package wintersteve25.rpgutils.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.storage.ServerOnlyLoadedData;
import wintersteve25.rpgutils.common.entities.NPCEntity;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketLoadData;
import wintersteve25.rpgutils.common.network.PacketOpenDialogueCreator;
import wintersteve25.rpgutils.common.network.PacketOpenQuestCreator;
import wintersteve25.rpgutils.common.systems.DialogueSystem;

public class ModCommands {
    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(RPGUtils.MOD_ID)
                        .then(Commands.literal("create_dialogues")
                                .executes(ModCommands::openDialogueCreator)));

        dispatcher.register(
                Commands.literal(RPGUtils.MOD_ID)
                        .then(Commands.literal("create_quests")
                                .executes(ModCommands::openQuestCreator)));

        dispatcher.register(Commands.literal(RPGUtils.MOD_ID)
                .then(Commands.literal("reload")
                        .executes(ModCommands::reloadData)));

        dispatcher.register(Commands.literal(RPGUtils.MOD_ID)
                .then(Commands.literal("play_dialogue")
                        .then(Commands.argument("dialogue", StringArgumentType.word())
                                .executes(ModCommands::playDialogue))));

        dispatcher.register(Commands.literal(RPGUtils.MOD_ID)
                .then(Commands.literal("npc_type")
                        .then(Commands.argument("target", EntityArgument.entity())
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .executes(ModCommands::modifyNpcType)))));
    }

    private static int openDialogueCreator(CommandContext<CommandSource> source) throws CommandSyntaxException {
        ModNetworking.sendToClient(new PacketOpenDialogueCreator(), source.getSource().getPlayerOrException());
        return 1;
    }

    private static int openQuestCreator(CommandContext<CommandSource> source) throws CommandSyntaxException {
        ModNetworking.sendToClient(new PacketOpenQuestCreator(), source.getSource().getPlayerOrException());
        return 1;
    }

    private static int reloadData(CommandContext<CommandSource> source) {
        for (ServerPlayerEntity player : source.getSource().getLevel().getPlayers(player -> true)) {
            ModNetworking.sendToClient(new PacketLoadData(), player);
        }

        ServerOnlyLoadedData.reloadAll();

        return 1;
    }

    private static int modifyNpcType(CommandContext<CommandSource> source) {
        try {
            Entity target = source.getArgument("target", EntitySelector.class).findSingleEntity(source.getSource());
            if (target instanceof NPCEntity) {
                String type = source.getArgument("type", String.class);
                ((NPCEntity) target).setNPCType(type);
            } else {
                RPGUtils.LOGGER.warn("Bad target argument for command: expected NPCEntity, received " + target.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static int playDialogue(CommandContext<CommandSource> source) {
        DialogueSystem.play(new ResourceLocation(RPGUtils.MOD_ID, source.getArgument("dialogue", String.class)));
        return 1;
    }
}