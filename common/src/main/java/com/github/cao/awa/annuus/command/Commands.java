package com.github.cao.awa.annuus.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public class Commands {

    /**
     * Register Commands with the CommandDispatcher
     *
     * @param dispatcher The CommandDispatcher to use to register commands
     *
     * @author strubium
     */
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher){
        AnnuusDebugCommand.register(dispatcher);
        AnnuusConfigCommand.register(dispatcher);
    }
}
