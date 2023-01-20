package com.project.atm.datamodel;

public class Account {

    private int uId;
    private String accountType;
    private double balance;
    private int accountHolder;
    private String cardNumber;
    private String pinHash;

    public Account() {
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
