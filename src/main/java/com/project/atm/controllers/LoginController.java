package com.project.atm.controllers;

import com.project.atm.Main;
import com.project.atm.datamodel.Account;
import com.project.atm.datamodel.AccountGenerator;
import com.project.atm.datamodel.Datasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private AnchorPane loginPanel;
    @FXML
    private TextField cardField;
    @FXML
    private PasswordField passField;

    /**
     * Goes to the register page
     * @param event event by button
     * @throws IOException
     */
    @FXML
    public void register(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(View.REGISTER.getFilename()));
        Parent root = loader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * onAction event that starts the login process
     * @throws IOException
     */
    @FXML
    public void login() throws IOException {

        // Gather data
        String cardNo = cardField.getText().trim();
        String pin = passField.getText().trim();

        boolean isSuccessful = verifyCredentials(cardNo, pin);

        if(isSuccessful){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Login Successful!");
            alert.setHeaderText(null);
            alert.setContentText("Please Click Ok to Proceed");
            alert.showAndWait();

            switchToMain(cardNo);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please Check Card Number/Pin");
            alert.showAndWait();
        }

    }

    /**
     * calls the singleton class and checks if the inputted credentials matches any in the db
     * @param cardNo string that contains the card number of the user
     * @param pin string that contains the pin of the user
     * @return true if there is a match, else false
     */
    private boolean verifyCredentials(String cardNo, String pin){

        return Datasource.getInstance().checkCredentials(AccountGenerator.hash(pin),
                AccountGenerator.hash(cardNo));
    }


    /**
     * calls the db and creates an account object
     * @param cardNo string that contains the card number of the user
     * @return account object
     */
    private Account getAccount(String cardNo){

        return Datasource.getInstance().queryAccount(AccountGenerator.hash(cardNo));

    }

    /**
     * switches to the main window. Only executes if login is successful
     * @param cardNo string that contains the card number of the user
     * @throws IOException
     */
    private void switchToMain(String cardNo) throws IOException {

        Account account = getAccount(cardNo);

        FXMLLoader loader = new FXMLLoader(Main.class.getResource(View.MENU.getFilename()));
        Parent root = loader.load();

        MenuController controller = loader.getController();
        controller.initData(account);

        Stage stage = (Stage) loginPanel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}


