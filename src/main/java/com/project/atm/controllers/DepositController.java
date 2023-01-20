package com.project.atm.controllers;

import com.project.atm.Main;
import com.project.atm.datamodel.Account;
import com.project.atm.datamodel.Datasource;
import com.project.atm.datamodel.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class DepositController {

    @FXML
    private AnchorPane depositPane;
    @FXML
    private TextField amountField;

    private Account currentAccount;


    public void initialize(){}

    /**
     * Pseudo constructor that sets the currentAccount
     * @param account account object of the current logged-in user
     */
    public void initData(Account account){

        this.currentAccount = account;

    }

    @FXML
    public void depositAmount() throws SQLException, IOException {

        if(!isInputNotValid()){
            double depositAmount = getAmount();
            double updatedBalance = getUpdatedBalance(depositAmount);

            boolean isSuccessful = Datasource.getInstance().updateBalanceAmount(updatedBalance,
                    currentAccount.getuId());

            if(isSuccessful){
                recordTransaction(depositAmount);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Deposit Successful");
                alert.setHeaderText(null);
                alert.setContentText("Please Click Ok to Proceed");
                alert.showAndWait();

                backToMenu();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Deposit Failed");
                alert.setHeaderText(null);
                alert.setContentText("There is a problem with the server");
                alert.showAndWait();
            }
        }

    }

    /**
     * Gets amount input from textfield
     * @return value casted to double
     */
    private double getAmount(){
        // add arguments in textField to only input numbers and not input negative
        return Double.parseDouble(amountField.getText().trim());
    }

    /**
     *
     * @param depositAmount amount inputted by the user
     * @return update balance after adding deposit amount
     */
    private double getUpdatedBalance(double depositAmount){
        // Uses bigDecimal to add doubles
        BigDecimal currentBalance = new BigDecimal(String.valueOf(currentAccount.getBalance()));
        BigDecimal result = currentBalance.add(new BigDecimal(String.valueOf(depositAmount)));

        double newBalance = result.doubleValue();
        // sets the currentAccount objects balance to newBalance
        currentAccount.setBalance(newBalance);

        return newBalance;
    }

    /**
     * creates a transaction intstance and calls record transaction to add a transaction row to transaction table
     * @param depositAmount amount deposited
     */
    private void recordTransaction(double depositAmount){
        Transaction transaction = new Transaction(depositAmount, "deposit", currentAccount.getuId(),
                LocalDate.now(), LocalTime.now());

        Datasource.getInstance().recordTransaction(transaction);
    }

    /**
     * Checks if the inputted amount follows the correct format
     * @return true if input is invalid, else false
     */
    private boolean isInputNotValid(){
        String input = amountField.getText().trim();

        if(input.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Deposit Failed");
            alert.setHeaderText(null);
            alert.setContentText("There is no amount inputted!");
            alert.showAndWait();
            return true;
        }else if(!input.matches("^(0|[1-9]\\d*)(\\.\\d+)?$")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Deposit Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please input proper amount");
            alert.showAndWait();
            return true;
        }else {
            return false;
        }
    }

    /**
     * goes back to the main window
     * @throws IOException
     */
    @FXML
    private void backToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(View.MENU.getFilename()));
        Parent root = loader.load();

        MenuController controller = loader.getController();
        controller.initData(currentAccount);

        Stage stage = (Stage) depositPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
