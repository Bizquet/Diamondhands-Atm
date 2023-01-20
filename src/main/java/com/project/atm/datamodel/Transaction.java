package com.project.atm.datamodel;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {

    private double amount;
    private String transactType;
    private int accountId;
    private LocalDate date;
    private LocalTime time;
    private int transaction_id;


    public Transaction(double amount, String transactType, int accountId, LocalDate date, LocalTime time) {
        this.amount = amount;
        this.transactType = transactType;
        this.accountId = accountId;
        this.date = date;
        this.time = time;
    }

    public Transaction() {
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactType() {
        return transactType;
    }

    public void setTransactType(String transactType) {
        this.transactType = transactType;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
