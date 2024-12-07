package fr.hermann.simple_phone;

import de.maxhenkel.voicechat.api.ServerPlayer;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EntitySoundPacketEvent;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.SoundPacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import de.maxhenkel.voicechat.api.packets.EntitySoundPacket;
import de.maxhenkel.voicechat.api.packets.StaticSoundPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public class SimplePhoneManager {

    private static SimplePhoneManager INSTANCE;
    private VoicechatServerApi voicechatServerApi;
    private boolean started = false;

    private ArrayList<Call> calls = new ArrayList<>();

    // Constructeur privé pour empêcher l'instanciation externe
    private SimplePhoneManager() {
        // Initialisation si nécessaire
    }

    // Méthode pour obtenir l'instance unique
    public static SimplePhoneManager getInstance() {
        if (INSTANCE == null) {
            synchronized (SimplePhoneManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SimplePhoneManager();
                }
            }
        }
        return INSTANCE;
    }

    public ArrayList<Call> getCalls() {
        return calls;
    }

    public void onServerStarted(VoicechatServerStartedEvent event) {
        voicechatServerApi = event.getVoicechat();
        started = true;
    }
    public void onMicrophonePacketReceived(MicrophonePacketEvent event) {
        voicechatServerApi = event.getVoicechat();
        started = true;
        StaticSoundPacket packet = event.getPacket().staticSoundPacketBuilder().build();
        ServerPlayer fromPlayer = event.getSenderConnection().getPlayer();
        SimplePhoneManager simplePhoneManager = SimplePhoneManager.getInstance();
        System.out.println("Searching for current call");
        for (Call call : simplePhoneManager.getCalls()) {
            if ((call.from.getUuid().toString().equals(fromPlayer.getUuid().toString()) || call.to.getUuid().toString().equals(fromPlayer.getUuid().toString())) && call.state.equals("RUNNING")) {
                System.out.println("Can send entity packet");
                if (call.from.getUuid().toString().equals(fromPlayer.getUuid().toString())) {
                    ServerPlayerEntity toPlayer = call.to;
                    VoicechatConnection toPlayerConnection = voicechatServerApi.getConnectionOf(toPlayer.getUuid());
                    voicechatServerApi.sendStaticSoundPacketTo(toPlayerConnection, packet);
                }
                if (call.to.getUuid().toString().equals(fromPlayer.getUuid().toString())) {
                    ServerPlayerEntity toPlayer = call.from;
                    VoicechatConnection toPlayerConnection = voicechatServerApi.getConnectionOf(toPlayer.getUuid());
                    voicechatServerApi.sendStaticSoundPacketTo(toPlayerConnection, packet);
                }
            }
        }
    }
}
