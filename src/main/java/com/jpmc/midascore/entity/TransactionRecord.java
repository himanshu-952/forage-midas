package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long recipientId;
    private double amount;
    private float incentive;

    public TransactionRecord() {}

    public TransactionRecord(Long senderId, Long recipientId, double amount, float incentive) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = incentive;
    }

    // Getters and setters
    public Long getId() { return id; }

    public Long getSenderId() { return senderId; }

    public Long getRecipientId() { return recipientId; }

    public double getAmount() { return amount; }

    public float getIncentive() { return incentive; }

    public void setId(Long id) { this.id = id; }

    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public void setAmount(double amount) { this.amount = amount; }

    public void setIncentive(float incentive) { this.incentive = incentive; }
}
