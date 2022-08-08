package wintersteve25.rpgutils.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.network.ModNetworking;
import wintersteve25.rpgutils.common.network.PacketOpenDialogueCreator;

public class ModCommands {
    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal(RPGUtils.MOD_ID).then(Commands.literal("dialogues").then(Commands.literal("creator").executes(ModCommands::openDialogueCreator))));
    }

    private static int openDialogueCreator(CommandContext<CommandSource> source) throws CommandSyntaxException {
        ModNetworking.sendToClient(new PacketOpenDialogueCreator(), source.getSource().getPlayerOrException());
        return 1;
    }
}
