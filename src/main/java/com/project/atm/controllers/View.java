package com.project.atm.controllers;

public enum View {

    LOGIN("login.fxml"),
    REGISTER("register.fxml"),
    MENU("clientMenu.fxml"),
    DEPOSIT("clientDeposit.fxml"),
    WITHDRAW("clientWithdraw.fxml"),
    TRANSFER("clientTransfer.fxml"),
    BALANCE("clientBalance.fxml"),
    PIN_DIALOG("pinDialog.fxml"),
    HISTORY("clientHistory.fxml");

    private String filename;

    View(String filename){
        this.filename = filename;
    }

    public String getFilename(){
        return filename;
    }
}
