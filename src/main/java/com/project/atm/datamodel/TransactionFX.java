package com.project.atm.datamodel;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionFX {
    private SimpleDoubleProperty amount = new SimpleDoubleProperty();
    private SimpleStringProperty transactType = new SimpleStringProperty("");
    private SimpleIntegerProperty accountId = new SimpleIntegerProperty();
    private SimpleStringProperty date = new SimpleStringProperty("");
    private SimpleStringProperty time = new SimpleStringProperty("");
    private SimpleIntegerProperty transaction_id = new SimpleIntegerProperty();


//    public Transaction(double amount, String transactType, int accountId, LocalDate date, LocalTime time) {
//        this.amount = amount;
//        this.transactType = transactType;
//        this.accountId = accountId;
//        this.date = date;
//        this.time = time;
//    }


    public TransactionFX(double amount, String transactType, int accountId, LocalDate date, LocalTime time) {
        this.amount.setValue(amount);
        this.transactType.set(transactType);
        this.accountId.set(accountId);
        this.date.set(date.toString());
        this.time.set(time.toString());
    }

    public TransactionFX() {
    }

    public double getAmount() {
        return amount.get();
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public String getTransactType() {
        return transactType.get();
    }

    public SimpleStringProperty transactTypeProperty() {
        return transactType;
    }

    public void setTransactType(String transactType) {
        this.transactType.set(transactType);
    }

    public int getAccountId() {
        return accountId.get();
    }

    public SimpleIntegerProperty accountIdProperty() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId.set(accountId);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public int getTransaction_id() {
        return transaction_id.get();
    }

    public SimpleIntegerProperty transaction_idProperty() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id.set(transaction_id);
    }
}
