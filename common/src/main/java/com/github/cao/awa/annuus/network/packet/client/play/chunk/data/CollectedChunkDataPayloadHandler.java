package com.github.cao.awa.annuus.network.packet.client.play.chunk.data;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.LightData;

import java.util.Iterator;

public class CollectedChunkDataPayloadHandler {
    public static void loadChunksFromPayload(CollectedChunkDataPayload payload, MinecraftClient client, ClientPlayerEntity player) {
        ClientPlayNetworkHandler networkHandler = player.networkHandler;
        ClientWorld world = networkHandler.getWorld();

        RegistryByteBuf delegateBuf = new RegistryByteBuf(
                new PacketByteBuf(Unpooled.buffer()),
                world.getRegistryManager()
        );

        Iterator<ChunkData> chunkDataIterator = payload.chunkData().iterator();
        Iterator<LightData> lightDataIterator = payload.lightData().iterator();
        IntList xPositions = payload.xPositions();
        IntList zPositions = payload.zPositions();

        int i = 0;
        while (chunkDataIterator.hasNext()) {
            delegateBuf.writeInt(xPositions.getInt(i));
            delegateBuf.writeInt(zPositions.getInt(i));
            chunkDataIterator.next().write(delegateBuf);
            lightDataIterator.next().write(delegateBuf);

            i++;
        }

        while (delegateBuf.readableBytes() > 0) {
            networkHandler.onChunkData(ChunkDataS2CPacket.CODEC.decode(delegateBuf));
        }
    }
}
