package com.vivekkarn.kafkademo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import org.awaitility.Awaitility;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(KafkaTestResourceLifecycleManager.class)
public class PostMessageFlowTest {

   /* @Inject
    @Connector("smallrye-kafka")
    InMemoryConnector connector;*/

    /*@Inject
    @Connector("smallrye-in-memory")
    InMemoryConnector connector;*/

    @Inject
    @Any
    InMemoryConnector connector;

    @BeforeAll
    public static void switchMyChannels() {

        InMemoryConnector.switchOutgoingChannelsToInMemory("generated-messages");
        InMemoryConnector.switchIncomingChannelsToInMemory("received-messages");

    }

    @AfterAll
    public static void revertMyChannels() {
        InMemoryConnector.clear();
    }




    @Test
    void test_postMessage_flow_with_inMemoryKafka() {
        //for debug
        System.out.println("Available connector " + connector.toString());

        // Arrange
        String testMessage = "Message from test";

        /*@Incoming("uppercase")
        public void sink(String word) {
            System.out.println(">> " + word);
        }*/

        InMemorySink<String> receivedMessagesSink = connector.sink("received-messages");




        // Act
        given()
                .contentType(ContentType.JSON)
                .body("{\"content\": \"" + testMessage + "\"}")
                .when()
                .post("/post-message")
                .then()
                .statusCode(200);

        // Assert (await message from in-memory Kafka)
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var received = receivedMessagesSink.received()
                            .stream()
                            .map(m -> m.getPayload().toString())
                            .toList();

                    assertTrue(received.contains(testMessage), "Kafka message should be consumed");
                });
    }
}
