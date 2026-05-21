package com.github.cao.awa.annuus.network.packet.client.play.recipe;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.recipe.RecipeEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShortRecipeSyncPayloadHandler {
    public static void syncRecipesFromPayload(ShortRecipeSyncPayload payload, MinecraftClient client, ClientPlayerEntity player) {
        client.executeSync(() -> {
            // Convert the short recipe to the vanilla recipe.
            Collection<RecipeEntry<?>> recipes = new ArrayList<>(List.of(payload.recipes().toVanilla()));

            // Sync recipes by vanilla.
            player.networkHandler.onSynchronizeRecipes(
                    new SynchronizeRecipesS2CPacket(
                            recipes
                    )
            );
        });
    }
}
