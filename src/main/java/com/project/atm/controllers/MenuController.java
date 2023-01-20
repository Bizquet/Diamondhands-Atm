package com.project.atm.controllers;

import com.project.atm.Main;
import com.project.atm.datamodel.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private AnchorPane menuPanel;

    private Account currentAccount;

    public void initialize(){}

    public void initData(Account account){
        this.currentAccount = account;
    }

    /**
     * switches scene to the withdraw fxml
     * @throws IOException
     */
    @FXML
    public void withdraw() throws IOException {
        switchScene(View.WITHDRAW.getFilename());
    }

    /**
     * switches scene to the deposit fxml
     * @throws IOException
     */
    @FXML
    public void deposit() throws IOException {
        switchScene(View.DEPOSIT.getFilename());
    }

    /**
     * switches scene to the balance fxml
     * @throws IOException
     */
    @FXML
    public void checkBalance() throws IOException {
        switchScene(View.BALANCE.getFilename());
    }

    /**
     * switches scene to the history fxml
     * @throws IOException
     */
    @FXML
    public void checkTransactionHistory() throws IOException {
        switchScene(View.HISTORY.getFilename());
    }

    /**
     * switches scene to the transfer fxml
     * @throws IOException
     */
    @FXML
    public void transfer() throws IOException {
        switchScene(View.TRANSFER.getFilename());
    }

    /**
     * switches scene to the login fxml
     * @throws IOException
     */
    @FXML
    public void logout() throws IOException {
        switchScene(View.LOGIN.getFilename());
    }

    /**
     * Method that switches to the desired scene
     * @param filename name of the fxml file to switch to
     * @throws IOException
     */
    private void switchScene(String filename) throws IOException {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource(filename));
        Parent root = loader.load();

        switch (filename){
            case "clientWithdraw.fxml":
                WithdrawController controller1 = loader.getController();
                controller1.initData(currentAccount);

                Stage stage1 = (Stage) menuPanel.getScene().getWindow();
                stage1.setScene(new Scene(root));
                stage1.show();
                break;
            case "clientDeposit.fxml":
                DepositController controller2 = loader.getController();
                controller2.initData(currentAccount);

                Stage stage2 = (Stage) menuPanel.getScene().getWindow();
                stage2.setScene(new Scene(root));
                stage2.show();
                break;
            case "clientTransfer.fxml":
                TransferController controller3 = loader.getController();
                controller3.initData(currentAccount);

                Stage stage3 = (Stage) menuPanel.getScene().getWindow();
                stage3.setScene(new Scene(root));
                stage3.show();
                break;
            case "clientBalance.fxml":
                BalanceController controller4 = loader.getController();
                controller4.initData(currentAccount);

                Stage stage4 = (Stage) menuPanel.getScene().getWindow();
                stage4.setScene(new Scene(root));
                stage4.show();
                break;
            case "clientHistory.fxml":
                HistoryController controller5 = loader.getController();
                controller5.initData(currentAccount);

                Stage stage5 = (Stage) menuPanel.getScene().getWindow();
                stage5.setScene(new Scene(root));
                stage5.show();
                break;
            case "login.fxml":
                Stage stage6 = (Stage) menuPanel.getScene().getWindow();
                stage6.setScene(new Scene(root));
                stage6.show();
        }
    }


}
