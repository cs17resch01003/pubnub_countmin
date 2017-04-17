package com.iith.pubnub;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import java.util.Arrays;

public class PubNubImpl {

    public static void start(String subscriberKey, String channel, String property) {
        PNConfiguration configuration = new PNConfiguration();
        configuration.setSubscribeKey(subscriberKey);
        configuration.setSecure(false);

        PubNub pubnub = new PubNub(configuration);
        pubnub.subscribe()
                .channels(Arrays.asList(channel)) // subscribe to channels
                .execute();
        
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus pns) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult pnmr) {
                String item = pnmr.getMessage().getAsJsonObject().get(property).getAsString();
                Counter.add(item);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult pnper) {
            }
        });
    }
}