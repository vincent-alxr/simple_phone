package fr.hermann.simple_phone;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.SoundPacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;

public class SimplePhonePlugin implements VoicechatPlugin {

    @Override
    public String getPluginId() {
        return "simple_phone";
    }

    @Override
    public void initialize(VoicechatApi api) {

        VoicechatPlugin.super.initialize(api);
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        SimplePhoneManager simplePhoneManagerInstance = SimplePhoneManager.getInstance();
        registration.registerEvent(VoicechatServerStartedEvent.class, simplePhoneManagerInstance::onServerStarted);
        registration.registerEvent(MicrophonePacketEvent.class, simplePhoneManagerInstance::onMicrophonePacketReceived);
    }
}
