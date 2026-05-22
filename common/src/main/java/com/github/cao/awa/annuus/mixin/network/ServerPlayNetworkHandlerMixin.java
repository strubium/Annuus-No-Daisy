package com.github.cao.awa.annuus.mixin.network;

import com.github.cao.awa.annuus.version.AnnuusVersionStorage;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.cao.awa.annuus.Annuus.LOGGER;

@Mixin(ServerPlayNetworkHandler.class)
abstract public class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler {

    public ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void initAnnuusVersion(MinecraftServer server, ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        AnnuusVersionStorage versionStorage = ((AnnuusVersionStorage) this);

        // Setting annuus version.
        versionStorage.setAnnuusVersion(((AnnuusVersionStorage) connection).getAnnuusVersion());

        if (versionStorage.getAnnuusVersion() > -1) {
            LOGGER.info("Player {} updating Annuus protocol version {}", player.getName().getString(), versionStorage.getAnnuusVersion());

            ((AnnuusVersionStorage) this.connection).setAnnuusVersion(versionStorage.getAnnuusVersion());
        }
    }
}
