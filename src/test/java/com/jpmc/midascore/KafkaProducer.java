package com.jpmc.midascore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ✅ Method for Task 2 - sends a sample transaction
    public void sendTransaction() {
        Transaction tx = new Transaction("123", 100.5, "credit");
        String json = convertToJson(tx);
        kafkaTemplate.send("test-transactions-topic", json);
    }

    // ✅ Additional method used in Task 4 - sends any string message
    public void send(String jsonMessage) {
        kafkaTemplate.send("test-transactions-topic", jsonMessage);
    }

    private String convertToJson(Transaction tx) {
        try {
            return objectMapper.writeValueAsString(tx);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert transaction to JSON", e);
        }
    }
}
