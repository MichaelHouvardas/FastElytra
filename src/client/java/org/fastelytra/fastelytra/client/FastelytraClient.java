package org.fastelytra.fastelytra.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FastelytraClient implements ClientModInitializer {

    private boolean jumpKeyPreviouslyPressed = false; // Track the state of the jump key

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                PlayerEntity player = client.player;
                MinecraftClient minecraftClient = MinecraftClient.getInstance();

                // Check if the player is flying with an elytra and holding the "W" key (forward)
                if (player.isFallFlying() && minecraftClient.options.forwardKey.isPressed()) {
                    // Increase the player's flight speed when holding W with elytra
                    player.addVelocity(
                            player.getRotationVector().x * 0.5,
                            player.getRotationVector().y * 0.5,
                            player.getRotationVector().z * 0.5
                    );
                }

                // Check if the player is flying with an elytra and presses the jump key
                boolean jumpKeyPressed = minecraftClient.options.jumpKey.isPressed();

                if (player.isFallFlying() && jumpKeyPressed && !jumpKeyPreviouslyPressed) {
                    // Make the player stop using the elytra if the jump key was just pressed
                    player.stopFallFlying();
                }

                // Allow the player to use the elytra again if they are in the air and press the jump key
                if (!player.isFallFlying() && !player.isOnGround() && jumpKeyPressed && !jumpKeyPreviouslyPressed) {
                    // Start using the elytra
                    player.startFallFlying();
                }

                // Update the previous state of the jump key
                jumpKeyPreviouslyPressed = jumpKeyPressed;
            }
        });
    }
}
