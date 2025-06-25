package com.vivekkarn.kafkademo;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/post-message")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class SendToKafkaResource {

    @Inject
    @Channel("generated-messages")
    Emitter<String> emitter;

    @POST
    @Transactional
    public String postMessage(MessageRequest request) {
        // Save to DB
        Message message = new Message(request.content);
        message.persist();

        // Send to Kafka
        emitter.send(request.content);

        return "Message stored and sent to Kafka.";
    }
}
