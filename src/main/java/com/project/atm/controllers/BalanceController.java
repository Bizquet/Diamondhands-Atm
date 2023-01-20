package com.project.atm.controllers;

import com.project.atm.Main;
import com.project.atm.datamodel.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class BalanceController {

    @FXML
    private Label accTypeLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private AnchorPane balancePane;

    private Account currentAccount;

    public void initialize(){}

    /**
     * Initializes the textfield before the window is loaded
     * @param account account object of the current logged-in user
     */
    public void initData(Account account){
        this.currentAccount = account;
        accTypeLabel.setText(currentAccount.getAccountType());
        balanceLabel.setText("₱ " + currentAccount.getBalance());
    }

    /**
     * switches back to the main window
     * @throws IOException
     */
    @FXML
    public void backToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(View.MENU.getFilename()));
        Parent root = loader.load();

        MenuController controller = loader.getController();
        controller.initData(currentAccount);

        Stage stage = (Stage) balancePane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
