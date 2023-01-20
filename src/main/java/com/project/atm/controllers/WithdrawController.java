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
import java.time.LocalDate;
import java.time.LocalTime;

public class WithdrawController {

    @FXML
    private AnchorPane withdrawPane;
    @FXML
    private TextField amountField;
    private Account currentAccount;


    public void initialize(){}

    /**
     * Psuedo contructor of the account object
     * @param account account object of the current logged-in user
     */
    public void initData(Account account){
        this.currentAccount = account;
    }

    /**
     * onAction of the withdraw button
     */
    @FXML
    private void withdrawAmount(){

        if(!isInputValid()) {
            try{
                double depositAmount = getAmount(); // already checked in get amount if value is valid
                double updatedBalance = getUpdatedBalance(depositAmount);

                if (!(Double.compare(updatedBalance, 0.0) < 0)){
                    boolean isSuccessful = Datasource.getInstance().updateBalanceAmount(updatedBalance,
                            currentAccount.getuId());
                    if(isSuccessful){
                        recordTransaction(depositAmount);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Withdraw Successful");
                        alert.setHeaderText(null);
                        alert.setContentText("Please Click Ok to Proceed");
                        alert.showAndWait();

                        backToMenu();
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Withdraw Failed");
                        alert.setHeaderText(null);
                        alert.setContentText("There is a problem with the server");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Withdraw Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Not Enough Funds to Withdraw");
                    alert.showAndWait();
                }

            } catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Withdraw Failed");
                alert.setHeaderText(null);
                alert.setContentText("Please Check Input Amount");
                alert.showAndWait();
            }
        }

    }

    /**
     * gets the input from the textfield and parses it to a double
     * @return input amount casted to a double
     */
    private double getAmount(){
        // add arguments in textField to only input numbers and not input negative
        return Double.parseDouble(amountField.getText().trim());
    }

    /**
     * subtracts the depositAmount from the current balance of the account
     * @param depositAmount amount to be deposited
     * @return new balance of the current account after subtraction
     */
    private double getUpdatedBalance(double depositAmount){
        // Uses bigDecimal to add doubles
        BigDecimal currentBalance = new BigDecimal(String.valueOf(currentAccount.getBalance()));
        BigDecimal result = currentBalance.subtract(new BigDecimal(String.valueOf(depositAmount)));

        double newBalance = result.doubleValue();
        // sets the currentAccount objects balance to newBalance

        if(!(Double.compare(newBalance, 0.0) < 0)){
            currentAccount.setBalance(newBalance);
        }

        return newBalance;
    }

    /**
     * Creates a transaction object and calls the singleton class to add a transaction to the transactions table
     * @param depositAmount amount to be deposited
     */
    private void recordTransaction(double depositAmount){
        Transaction transaction = new Transaction(depositAmount, "withdraw", currentAccount.getuId(),
                LocalDate.now(), LocalTime.now());

        Datasource.getInstance().recordTransaction(transaction);
    }

    /**
     * checks if the inputted amount is valied
     * @return returns true if there are errors, else false
     */
    private boolean isInputValid(){
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
            alert.setTitle("Withdraw Failed");
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
    public void backToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(View.MENU.getFilename()));
        Parent root = loader.load();

        MenuController controller = loader.getController();
        controller.initData(currentAccount);

        Stage stage = (Stage) withdrawPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
