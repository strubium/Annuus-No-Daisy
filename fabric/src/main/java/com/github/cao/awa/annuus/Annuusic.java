package com.github.cao.awa.annuus;

import com.github.cao.awa.annuus.command.Commands;
import com.github.cao.awa.annuus.network.packet.client.play.block.update.CollectedBlockUpdatePayload;
import com.github.cao.awa.annuus.network.packet.client.play.chunk.update.CollectedChunkBlockUpdatePayload;
import com.github.cao.awa.annuus.network.packet.client.play.chunk.data.CollectedChunkDataPayload;
import com.github.cao.awa.annuus.network.packet.client.play.recipe.ShortRecipeSyncPayload;
import com.github.cao.awa.annuus.network.packet.server.notice.NoticeServerAnnuusPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import static com.github.cao.awa.annuus.Annuus.LOGGER;

public class Annuusic implements ModInitializer {

    @Override
    public void onInitialize() {
        LOGGER.info("Loading annuus using fabric bootstrap");

        Annuus.onInitialize();

        PayloadTypeRegistry.playS2C().register(CollectedChunkDataPayload.IDENTIFIER, CollectedChunkDataPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CollectedBlockUpdatePayload.IDENTIFIER, CollectedBlockUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CollectedChunkBlockUpdatePayload.IDENTIFIER, CollectedChunkBlockUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ShortRecipeSyncPayload.IDENTIFIER, ShortRecipeSyncPayload.CODEC);
        PayloadTypeRegistry.configurationC2S().register(NoticeServerAnnuusPayload.IDENTIFIER, NoticeServerAnnuusPayload.CODEC);

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            LOGGER.info("Registering commands");
            Commands.registerCommands(server.getCommandManager().getDispatcher());
        });
    }
}
