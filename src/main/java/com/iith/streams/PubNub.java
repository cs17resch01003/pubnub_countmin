package com.iith.streams;

import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class PubNub {
    
    private final String subscriberKey;
    private final String channel;
    
    private boolean paused;

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public PubNub(String subscriberKey, String channel) {
        this.subscriberKey = subscriberKey;
        this.channel = channel;
    }
    
    public void start() {
        PNConfiguration configuration = new PNConfiguration();
        configuration.setSubscribeKey(subscriberKey);
        configuration.setSecure(false);

        com.pubnub.api.PubNub pubnub = new com.pubnub.api.PubNub(configuration);
        pubnub.subscribe()
                .channels(Arrays.asList(channel)) // subscribe to channels
                .execute();
        
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(com.pubnub.api.PubNub pubnub, PNStatus pns) {
            }

            @Override
            public void message(com.pubnub.api.PubNub pubnub, PNMessageResult pnmr) {
                if (!isPaused()) {
                    JsonObject object = pnmr.getMessage().getAsJsonObject();
                    callback(object);
                }
            }

            @Override
            public void presence(com.pubnub.api.PubNub pubnub, PNPresenceEventResult pnper) {
            }
        });
    }
    
    public abstract void callback(JsonObject object);
}