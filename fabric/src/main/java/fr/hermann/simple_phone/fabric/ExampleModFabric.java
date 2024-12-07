package fr.hermann.simple_phone.fabric;

import fr.hermann.simple_phone.Call;
import fr.hermann.simple_phone.SimplePhoneManager;
import net.fabricmc.api.ModInitializer;

import fr.hermann.simple_phone.ExampleMod;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

// word()
// literal("foo")
import static net.minecraft.server.command.CommandManager.literal;
// argument("bar", word())
import static net.minecraft.server.command.CommandManager.argument;
// Import everything in the CommandManager


public final class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        // Run our common setup.
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(literal("call").then(argument("player", EntityArgumentType.player()).executes(commandContext -> {
                ServerPlayerEntity toPlayer = EntityArgumentType.getPlayer(commandContext, "player");
                SimplePhoneManager simplePhoneManager = SimplePhoneManager.getInstance();
                Call newCall = new Call(UUID.randomUUID());
                newCall.from = commandContext.getSource().getPlayer();
                newCall.to = toPlayer;
                newCall.state = "WAITING_ACCEPTATION";
                simplePhoneManager.getCalls().add(newCall);
                return 1;
            })));
            commandDispatcher.register(literal("accept").then(argument("player", EntityArgumentType.player()).executes(commandContext -> {
                ServerPlayerEntity fromPlayer = EntityArgumentType.getPlayer(commandContext, "player");
                SimplePhoneManager simplePhoneManager = SimplePhoneManager.getInstance();
                for (Call call : simplePhoneManager.getCalls()) {
                    if (call.from.getUuid().toString().equals(fromPlayer.getUuid().toString()) && call.state.equals("WAITING_ACCEPTATION")) {
                        call.state = "RUNNING";
                    }
                }
                return 1;
            })));
        }));
        ExampleMod.init();
    }
}
