package wintersteve25.rpgutils.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.command.arguments.LocationInput;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.npc.NPCTypeLoader;
import wintersteve25.rpgutils.common.data.loaded.quest.PlayerQuestProgress;
import wintersteve25.rpgutils.common.data.loaded.storage.ServerOnlyLoadedData;
import wintersteve25.rpgutils.common.entities.NPCEntity;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketLoadData;
import wintersteve25.rpgutils.common.network.PacketOpenDialogueCreator;
import wintersteve25.rpgutils.common.network.PacketOpenQuestCreator;
import wintersteve25.rpgutils.common.systems.DialogueSystem;
import wintersteve25.rpgutils.common.systems.QuestSystem;

import javax.xml.transform.Source;

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
                .then(Commands.literal("start_quest")
                        .then(Commands.argument("quest", StringArgumentType.word())
                                .executes(ModCommands::startQuest))));
        
        dispatcher.register(Commands.literal(RPGUtils.MOD_ID)
                .then(Commands.literal("summon_npc")
                        .then(Commands.argument("position", BlockPosArgument.blockPos())
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .executes(ModCommands::summonNpc)))));
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
        ServerOnlyLoadedData.reloadAll();

        for (ServerPlayerEntity player : source.getSource().getLevel().getPlayers(player -> true)) {
            ModNetworking.sendToClient(new PacketLoadData(), player);
            
            PlayerQuestProgress.refreshAvailableQuests(player);
            PlayerQuestProgress.refreshClient(player);
        }
        
        return 1;
    }

    private static int summonNpc(CommandContext<CommandSource> source) {
        ServerWorld world = source.getSource().getLevel();
        NPCEntity entity = NPCEntity.create(ModEntities.NPC_ENTITY.get(), world, source.getArgument("type", String.class));
        BlockPos pos = source.getArgument("position", LocationInput.class).getBlockPos(source.getSource());
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        world.addFreshEntity(entity);
        entity.updateClients();
        return 1;
    }

    private static int playDialogue(CommandContext<CommandSource> source) {
        DialogueSystem.play(new ResourceLocation(RPGUtils.MOD_ID, source.getArgument("dialogue", String.class)));
        return 1;
    }
    
    private static int startQuest(CommandContext<CommandSource> source) {
        try {
            QuestSystem.attemptStartQuest(source.getSource().getPlayerOrException(), new ResourceLocation(RPGUtils.MOD_ID, source.getArgument("quest", String.class)));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return 0;
        }
        
        return 1;
    }
}