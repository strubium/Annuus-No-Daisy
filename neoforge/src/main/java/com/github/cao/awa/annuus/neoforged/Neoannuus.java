package com.github.cao.awa.annuus.neoforged;

import com.github.cao.awa.annuus.Annuus;
import com.github.cao.awa.annuus.command.Commands;
import com.github.cao.awa.annuus.network.packet.client.play.block.update.*;
import com.github.cao.awa.annuus.network.packet.client.play.chunk.data.CollectedChunkDataPayload;
import com.github.cao.awa.annuus.network.packet.client.play.chunk.data.CollectedChunkDataPayloadHandler;
import com.github.cao.awa.annuus.network.packet.client.play.chunk.update.CollectedChunkBlockUpdatePayload;
import com.github.cao.awa.annuus.network.packet.client.play.chunk.update.CollectedChunkBlockUpdatePayloadHandler;
import com.github.cao.awa.annuus.network.packet.client.play.recipe.ShortRecipeSyncPayload;
import com.github.cao.awa.annuus.network.packet.client.play.recipe.ShortRecipeSyncPayloadHandler;
import com.github.cao.awa.annuus.network.packet.server.notice.NoticeServerAnnuusPayload;
import com.github.cao.awa.annuus.network.packet.server.notice.NoticeServerAnnuusPayloadHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.github.cao.awa.annuus.Annuus.LOGGER;

@Mod("annuus")
public class Neoannuus {

    public Neoannuus(IEventBus eventBus) {
        eventBus.addListener(FMLCommonSetupEvent.class, this::onCommonSetup);

        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, Neoannuus::registerCommand);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            LOGGER.debug("Loading annuus neoforge client");

            Annuus.isServer = false;

            NeoForge.EVENT_BUS.addListener(
                    ClientPlayerNetworkEvent.LoggingIn.class,
                    Neoannuus::sendAnnuusServerNotice
            );
        }
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            LOGGER.debug("Loading annuus neoforge server");

            Annuus.isServer = true;
        }
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        Annuus.onInitialize();

        // Sets the current network version
        PayloadRegistrar registrar = new PayloadRegistrar("1");

//        PayloadTypeRegistryImpl.PLAY_S2C.register(CollectedChunkDataPayload.IDENTIFIER, CollectedChunkDataPayload.CODEC);
//        PayloadTypeRegistryImpl.CONFIGURATION_C2S.register(NoticeServerAnnuusPayload.IDENTIFIER, NoticeServerAnnuusPayload.CODEC);

        registrar.playToClient(CollectedBlockUpdatePayload.IDENTIFIER, CollectedBlockUpdatePayload.CODEC, (payload, context) -> {
            CollectedBlockUpdatePayloadHandler.updateBlocksFromPayload(payload, MinecraftClient.getInstance(), (ClientPlayerEntity) context.player());
        });

        registrar.playToClient(CollectedChunkBlockUpdatePayload.IDENTIFIER, CollectedChunkBlockUpdatePayload.CODEC, (payload, context) -> {
            CollectedChunkBlockUpdatePayloadHandler.updateBlocksFromPayload(payload, MinecraftClient.getInstance(), (ClientPlayerEntity) context.player());
        });

        registrar.playToClient(CollectedChunkDataPayload.IDENTIFIER, CollectedChunkDataPayload.CODEC, (payload, context) -> {
            CollectedChunkDataPayloadHandler.loadChunksFromPayload(payload, MinecraftClient.getInstance(), (ClientPlayerEntity) context.player());
        });

        registrar.playToServer(NoticeServerAnnuusPayload.IDENTIFIER, NoticeServerAnnuusPayload.CODEC, (payload, context) -> {
            NoticeServerAnnuusPayloadHandler.updateAnnuusVersionDuringPlay(payload, context.player());
        });

        registrar.playToClient(ShortRecipeSyncPayload.IDENTIFIER, ShortRecipeSyncPayload.CODEC, (payload, context) -> {
            ShortRecipeSyncPayloadHandler.syncRecipesFromPayload(payload, MinecraftClient.getInstance(), (ClientPlayerEntity) context.player());
        });
    }

    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<ServerCommandSource> dispatcher = event.getDispatcher();

        LOGGER.info("Registering annuus commands");
        Commands.registerCommands(dispatcher);
    }

    public static void sendAnnuusServerNotice(ClientPlayerNetworkEvent.LoggingIn event) {
        event.getConnection().send(NoticeServerAnnuusPayload.createPacket());
    }
}
