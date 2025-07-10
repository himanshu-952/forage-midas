package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "midas.kafka.topic.transactions=test-transactions-topic"
})
public class TaskTwoTests {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void sendSampleTransactions() throws InterruptedException {
        kafkaTemplate.send("test-transactions-topic", "{\"id\":\"T1\", \"amount\":100.5, \"type\":\"CREDIT\"}");
        kafkaTemplate.send("test-transactions-topic", "{\"id\":\"T2\", \"amount\":250.0, \"type\":\"DEBIT\"}");
        kafkaTemplate.send("test-transactions-topic", "{\"id\":\"T3\", \"amount\":70.75, \"type\":\"CREDIT\"}");
        kafkaTemplate.send("test-transactions-topic", "{\"id\":\"T4\", \"amount\":125.25, \"type\":\"DEBIT\"}");

        Thread.sleep(2000); // Allow listener to process
    }
}
