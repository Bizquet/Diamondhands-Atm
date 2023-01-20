package com.project.atm.controllers;

import com.project.atm.datamodel.AccountGenerator;
import com.project.atm.datamodel.ClientInfo;
import com.project.atm.datamodel.Datasource;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PinController {

    @FXML
    private PasswordField pinField;
    @FXML
    private TextField cardNoField;
    @FXML
    private DialogPane pinPane;

    private String cardNo;
    private ClientInfo info;

    public void initialize(){}

    public void initData(String cardNo, ClientInfo info){
        this.cardNo = cardNo;
        this.info = info;
        cardNoField.setText(cardNo);
    }

    /** Add option where you can only input a 4-6 length pin and only numbers
     *  Shows dialog pane where the user will be prompted to enter desired pin
     */
    @FXML
    private void registerData(){
        // get all data

        if(!checkPinHasErrors()){
            int client_uid = Integer.parseInt(AccountGenerator.clientUidGenerator());
            int account_uid = Integer.parseInt(AccountGenerator.accountUidGenerator());

            String pin = pinField.getText();

            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return Datasource.getInstance().addAllClientData(info.getAcc_type(),
                            pin, info, client_uid, account_uid, cardNo);
                }
            };

            task.setOnSucceeded(e ->{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Registration Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Press Ok to proceed to login");
                alert.showAndWait();
                return;
            });

            new Thread(task).start();

            Stage stage = (Stage) pinPane.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Checks if the inputted pin does not follow the specified format
     * @return true if pin has errors, else false
     */
    private boolean checkPinHasErrors(){
        if(pinField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please input a pin (4-6)");
            alert.showAndWait();
            return true;
        } else if (pinField.getText().length() > 7 || pinField.getText().length() < 4){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Pin should only contain 4 to 6 numbers");
            alert.showAndWait();
            return true;
        }else if(!pinField.getText().matches("[0-9]*")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Pin should only contain numbers");
            alert.showAndWait();
            return true;
        }else {
            return false;
        }
    }


}


