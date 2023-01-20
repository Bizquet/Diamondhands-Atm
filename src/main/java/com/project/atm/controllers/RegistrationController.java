package com.project.atm.controllers;

import com.project.atm.Main;
import com.project.atm.datamodel.AccountGenerator;
import com.project.atm.datamodel.ClientInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationController {

    @FXML
    private AnchorPane registerPanel;
    @FXML
    private TextField fNameField;
    @FXML
    private TextField lNameField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField occupationField;
    @FXML
    private ComboBox cardTypeBox;
    @FXML
    private TextField purposeField;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public void initialize() {}

    /**
     * Goes back to login
     *
     * @param event buttonEvent
     * @throws IOException
     */
    @FXML
    private void backToLogin(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource(View.LOGIN.getFilename()));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

    /**
     * overloaded method that is used by the showPinWindow method
     *
     * @throws IOException
     */
    private void backToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(View.LOGIN.getFilename()));
        Parent root = loader.load();

        Stage stage = (Stage) registerPanel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Gets input from textfields and passes them into a client info object
     *
     * @return client info object
     */
    private ClientInfo gatherInfo() {
        ClientInfo info = new ClientInfo();
        // get all info

        try {
            String fName = fNameField.getText().trim();
            String lName = lNameField.getText().trim();
            LocalDate birthDate = birthDatePicker.getValue();
            String address = addressField.getText().trim();
            String occupation = occupationField.getText().trim();
            String email = emailField.getText().trim();
            String accType = cardTypeBox.getValue().toString().trim();
            String purpose = purposeField.getText().trim();

            info.setAcc_type(accType);
            info.setFirstName(fName);
            info.setLastName(lName);
            info.setBirthDay(birthDate);
            info.setAddress(address);
            info.setOccupation(occupation);
            info.setPurpose(purpose);
            info.setEmail(email);


        }catch (Exception e){
            System.out.println("Some fields are empty");
        }

        return info;
    }

    /**
     *  Checks each textfield if there are incorrect inputs
     * @return true if incorrect, else false
     */
    private boolean checkFields(){

        if (fNameField.getText().isEmpty() || lNameField.getText().isEmpty() || addressField.getText().isEmpty() ||
                occupationField.getText().isEmpty() || emailField.getText().isEmpty() ||
                purposeField.getText().isEmpty()) {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("A field is empty!");
            alert.showAndWait();
            return true;

        }
        else if (fNameField.getText().matches(".*\\d.*") || lNameField.getText().matches(".*\\d.*") ||
                occupationField.getText().matches(".*\\d.*")
                || purposeField.getText().matches(".*\\d.*")) {

            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error Message");
            al.setHeaderText(null);
            al.setContentText("You input a wrong variable!");
            al.showAndWait();
            return true;
        }


        if(!isEmailValid(emailField.getText())){
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error Message");
            al.setHeaderText(null);
            al.setContentText("Please input a valid email");
            al.showAndWait();
            return true;
        }

        if(!isBirthDateValid(birthDatePicker.getValue())){
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error Message");
            al.setHeaderText(null);
            al.setContentText("You must at least 18 years of age to open an account");
            al.showAndWait();
            return true;
        }

        return false;
    }

    /**
     * shows the pin window where the user will input their pin
     * @throws IOException
     */
    @FXML
    private void showPinWindow() throws IOException {

        if (!checkFields()){
            ClientInfo info = gatherInfo();
            String cardNo = AccountGenerator.cardGenerator();

            // pass in info and card no (show card number there)

            FXMLLoader loader = new FXMLLoader(Main.class.getResource(View.PIN_DIALOG.getFilename()));
            Parent root = loader.load();

            PinController controller = loader.getController();
            controller.initData(cardNo, info);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(registerPanel.getScene().getWindow());

            stage.showAndWait();

            backToLogin();
        }

    }

    /**
     * checks if the given email is valid
     * @param email string that the user inputted
     * @return true if the email is valid, else false
     */
    private boolean isEmailValid(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    /**
     * checks if the birthdate equates of the user is 18 or above years old
     * @param birthDate date that the user chose
     * @return true if the user is 18 years and above
     */
    private boolean isBirthDateValid(LocalDate birthDate){

        long datesBetween = ChronoUnit.DAYS.between(birthDate, LocalDate.now());

        return datesBetween >= 6570;

    }

}

