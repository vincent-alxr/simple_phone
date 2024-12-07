package fr.hermann.simple_phone;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class Call {
    public UUID callId;
    public ServerPlayerEntity from;
    public ServerPlayerEntity to;
    public String state = "NOT_ACCEPTED";

    public Call(UUID callId) {
        this.callId = callId;
    }
}
