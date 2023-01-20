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

public class TransferController {

    @FXML
    private AnchorPane transferPane;
    @FXML
    private TextField receiverField;
    @FXML
    private TextField amountField;

    private Account originAccount;
    private Account destinationAccount;

    public void initialize(){}

    /**
     * Psuedo contructor of the account object
     * @param account account object of the current logged-in user
     */
    public void initData(Account account){
        this.originAccount = account;
    }

    /**
     * Calls the singleton class and passes in the uid to retrieve data from the account table
     * and sets it to the destination account
     * @param uid uid of the destination account
     * @throws SQLException
     */
    private void setDestinationAccount(int uid) throws SQLException {
        this.destinationAccount = Datasource.getInstance().queryDestinationAccount(uid);
    }

    /**
     * onAction method of the transfer button
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    private void transferBalance() throws SQLException, IOException {

        if (!areFieldsValid()){
            int destinationAccountUid = getUid();
            setDestinationAccount(destinationAccountUid);

            if(destinationAccount.getAccountType() != null){
                double transferAmount = getAmount();

                double originBalance = getUpdatedBalance_Origin(transferAmount);
                double destinationBalance = getUpdatedBalance_Destination(transferAmount);

                boolean isOriginUpdateSuccess;

                if(originBalance > 0){
                    isOriginUpdateSuccess = Datasource.getInstance().updateBalanceAmount(originBalance,
                            originAccount.getuId());

                    if(isOriginUpdateSuccess){
                        boolean isDestinationUpdateSuccess = Datasource.getInstance().updateBalanceAmount
                                (destinationBalance,
                                destinationAccount.getuId());

                        if (isDestinationUpdateSuccess){
                            recordTransactionOrigin(transferAmount);
                            recordTransactionDestination(transferAmount);

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Successfully Transferred Funds");
                            alert.setHeaderText(null);
                            alert.setContentText("Please Click Ok to Proceed");
                            alert.showAndWait();

                            backToMenu();
                        }else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Transferring Funds");
                            alert.setHeaderText(null);
                            alert.setContentText("There was a problem with the server");
                            alert.showAndWait();
                        }
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Transferring Funds");
                        alert.setHeaderText(null);
                        alert.setContentText("There was a problem with the server");
                        alert.showAndWait();
                    }
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Not Enough Funds to Transfer");
                    alert.setHeaderText(null);
                    alert.setContentText("Please Check Input Amount");
                    alert.showAndWait();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Destination Account does not Exist");
                alert.setHeaderText(null);
                alert.setContentText("Please Check Destination Account Number");
                alert.showAndWait();

            }

        }

    }

    /**
     * Checks if the inputted fields have errors
     * @return true if there are errors, else false
     */
    private boolean areFieldsValid(){
        if(amountField.getText().trim().isEmpty() || receiverField.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Deposit Failed");
            alert.setHeaderText(null);
            alert.setContentText("There are empty fields!");
            alert.showAndWait();
            return true;
        }else {
            if(!amountField.getText().trim().matches("^(0|[1-9]\\d*)(\\.\\d+)?$")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Deposit Failed");
                alert.setHeaderText(null);
                alert.setContentText("Please input proper amount");
                alert.showAndWait();
                return true;
            }

            if(receiverField.getText().trim().length() != 8){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Deposit Failed");
                alert.setHeaderText(null);
                alert.setContentText("The destination account must be 8 digits long");
                alert.showAndWait();
                return true;
            }

            if(!receiverField.getText().trim().matches("[0-9]*")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Deposit Failed");
                alert.setHeaderText(null);
                alert.setContentText("Please only input numbers in destination field");
                alert.showAndWait();
                return true;
            }
        }

        return false;
    }

    /**
     * gets the amount inputted by the user and parses it as a double
     * @return amount inputted by the user parsed into a double
     */
    private double getAmount(){
        return Double.parseDouble(amountField.getText().trim());
    }
    /**
     * gets the uid inputted by the user and parses it as an int
     * @return uid inputted by the user parsed into an int
     */
    private int getUid(){
        return Integer.parseInt(receiverField.getText().trim());
    }

    /**
     * calculates the balance of the origin account after the transfer
     * @param transferAmount amount to be transferred
     * @return balance of the origin account
     */
    private double getUpdatedBalance_Origin(double transferAmount){
        // Uses bigDecimal to add doubles
        BigDecimal currentBalance = new BigDecimal(String.valueOf(originAccount.getBalance()));
        BigDecimal result = currentBalance.subtract(new BigDecimal(String.valueOf(transferAmount)));

        double newBalance = result.doubleValue();
        // sets the currentAccount objects balance to newBalance
        originAccount.setBalance(newBalance);

        return newBalance;
    }

    /**
     * calculates the balance of the destination account after the transfer
     * @param transferAmount amount to be transferred
     * @return balance of the destination account
     */
    private double getUpdatedBalance_Destination(double transferAmount){
        // Uses bigDecimal to add doubles
        BigDecimal currentBalance = new BigDecimal(String.valueOf(destinationAccount.getBalance()));
        BigDecimal result = currentBalance.add(new BigDecimal(String.valueOf(transferAmount)));

        double newBalance = result.doubleValue();
        // sets the currentAccount objects balance to newBalance
        destinationAccount.setBalance(newBalance);

        return newBalance;
    }

    /**
     * Creates a transaction object and calls the singleton class to record the transaction to the db
     * @param amount updated balance of the origin account
     */
    private void recordTransactionOrigin(double amount){
        Transaction transaction = new Transaction(amount, "transfer", originAccount.getuId(),
                LocalDate.now(), LocalTime.now());

        Datasource.getInstance().recordTransaction(transaction);
    }

    /**
     * Creates a transaction object and calls the singleton class to record the transaction to the db
     * @param amount updated balance of the destination account
     */
    private void recordTransactionDestination(double amount){
        Transaction transaction = new Transaction(amount, "receive", destinationAccount.getuId(),
                LocalDate.now(), LocalTime.now());

        Datasource.getInstance().recordTransaction(transaction);
    }

    /**
     * Goes back to the main window
     * @throws IOException
     */
    @FXML
    private void backToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(View.MENU.getFilename()));
        Parent root = loader.load();

        MenuController controller = loader.getController();
        controller.initData(originAccount);

        Stage stage = (Stage) transferPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
