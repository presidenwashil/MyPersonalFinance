package com.example.mypersonalfinance;

public class Transaction {
    private int id;
    private double amount;
    private String description;
    private String type;
    private String date;

    public Transaction(int id, double amount, String description, String type, String date) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
