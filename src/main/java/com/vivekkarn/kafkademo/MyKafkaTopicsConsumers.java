package com.vivekkarn.kafkademo;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class MyKafkaTopicsConsumers {

    @Incoming("received-messages")
    public void consume(String message) {
        System.out.println("✅ Received from Kafka: " + message);
    }
}
