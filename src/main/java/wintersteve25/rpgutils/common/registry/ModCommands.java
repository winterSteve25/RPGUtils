package wintersteve25.rpgutils.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.storage.ServerOnlyLoadedData;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketLoadData;
import wintersteve25.rpgutils.common.network.PacketOpenDialogueCreator;
import wintersteve25.rpgutils.common.systems.DialogueSystem;

public class ModCommands {
    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal(RPGUtils.MOD_ID).then(Commands.literal("create_dialogues").executes(ModCommands::openDialogueCreator)));
        dispatcher.register(Commands.literal(RPGUtils.MOD_ID).then(Commands.literal("reload").executes(ModCommands::reloadData)));
        dispatcher.register(Commands.literal(RPGUtils.MOD_ID).then(Commands.literal("play_dialogue").executes(ModCommands::playDialogue)));
    }

    private static int openDialogueCreator(CommandContext<CommandSource> source) throws CommandSyntaxException {
        ModNetworking.sendToClient(new PacketOpenDialogueCreator(), source.getSource().getPlayerOrException());
        return 1;
    }
    
    private static int reloadData(CommandContext<CommandSource> source) {
        for (ServerPlayerEntity player : source.getSource().getLevel().getPlayers(player -> true)) {
            ModNetworking.sendToClient(new PacketLoadData(), player);
        }

        ServerOnlyLoadedData.reloadAll();
        
        return 1;
    }

    private static int playDialogue(CommandContext<CommandSource> source) {
        DialogueSystem.play(new ResourceLocation(RPGUtils.MOD_ID, "boss_pool"));
        return 1;
    }
}