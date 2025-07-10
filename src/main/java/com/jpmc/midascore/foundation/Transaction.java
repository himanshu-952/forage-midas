package com.jpmc.midascore.foundation;

public class Transaction {
    private String id;
    private double amount;
    private String type;

    public Transaction() {}

    public Transaction(String id, double amount, String type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "Transaction{id='" + id + "', amount=" + amount + ", type='" + type + "'}";
    }
}
