package com.vivekkarn.kafkademo;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import jakarta.enterprise.context.ApplicationScoped;


import java.util.HashMap;
import java.util.Map;


@ApplicationScoped
public class KafkaTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    /*@Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();
        env.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("received-messages"));
        env.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("generated-messages"));
        return env;
    }*/


    /*
    Reactive Messaging provides two main annotations:
    https://smallrye.io/smallrye-reactive-messaging/smallrye-reactive-messaging/3.6/testing/testing.html

    org.eclipse.microprofile.reactive.messaging.Incoming - indicates the consumed channel

    org.eclipse.microprofile.reactive.messaging.Outgoing - indicates the populated channel

    additional resource -
    https://quarkus.io/guides/kafka#testing-without-a-broker
     */
    @Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();
        // Only your actual test flow
        env.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("generated-messages"));
        env.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("received-messages"));


       /* // Disable unrelated messaging flows or add @Broadcast to using uppercase where there are more consumers even if in test
        env.put("mp.messaging.incoming.words-in.connector", "disabled");
        env.put("mp.messaging.outgoing.words-out.connector", "disabled");
        env.put("mp.messaging.outgoing.uppercase.connector", "disabled");
        //env.put("mp.messaging.incoming.uppercase.connector", "disabled");*/

        // Disable or redirect other unrelated channels
        env.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("words-out"));
        env.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("words-in"));
        env.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("uppercase"));


        return env;
    }


    @Override
    public void stop() {
        InMemoryConnector.clear();
    }
}
