package com.jpmc.midascore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @KafkaListener(topics = "${midas.kafka.topic.transactions}", groupId = "midas-core")
    public void listen(String message) {
        try {
            Transaction transaction = objectMapper.readValue(message, Transaction.class);

            // Extract senderId and recipientId from ID field (format: "senderId:recipientId")
            String[] ids = transaction.getId().split(":");
            Long senderId = Long.parseLong(ids[0].trim());
            Long recipientId = Long.parseLong(ids[1].trim());

            double amount = transaction.getAmount();

            UserRecord sender = userRepository.findById(senderId).orElse(null);
            UserRecord recipient = userRepository.findById(recipientId).orElse(null);

            if (sender == null || recipient == null) {
                System.out.println("❌ Invalid sender or recipient.");
                return;
            }

            if (sender.getBalance() < amount) {
                System.out.println("❌ Insufficient balance.");
                return;
            }

            // 💸 Post to Incentive API
            Incentive incentive = restTemplate.postForObject("http://localhost:8080/incentive", transaction, Incentive.class);
            float incentiveAmount = incentive != null ? incentive.getAmount() : 0.0f;

            // 💾 Save transaction
            TransactionRecord record = new TransactionRecord(senderId, recipientId, amount, incentiveAmount);
            transactionRecordRepository.save(record);

            // 💰 Update balances
            sender.setBalance(sender.getBalance() - (float) amount);
            recipient.setBalance(recipient.getBalance() + (float) amount + incentiveAmount);
            userRepository.save(sender);
            userRepository.save(recipient);

            System.out.println("✅ Processed Transaction: " + transaction + " | Incentive: " + incentiveAmount);
        } catch (Exception e) {
            System.err.println("❌ Failed to process transaction: " + e.getMessage());
        }
    }
}
