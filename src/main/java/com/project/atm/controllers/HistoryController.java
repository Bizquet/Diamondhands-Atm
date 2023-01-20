package com.project.atm.controllers;

import com.project.atm.Main;
import com.project.atm.datamodel.Account;
import com.project.atm.datamodel.Datasource;
import com.project.atm.datamodel.TransactionFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HistoryController {

    @FXML
    private BorderPane historyPane;
    @FXML
    private TableView transactionsTable;

    private Account currentAccount;


    public void initialize(){}

    /**
     * populates the table with transaction data of the current user
     * @param account account object of the current logged-in user
     */
    public void initData(Account account){
        this.currentAccount = account;


        Task<ObservableList<TransactionFX>> task = new GetAllTransactionsTask(currentAccount.getuId());
        transactionsTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
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

        Stage stage = (Stage) historyPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

/**
 * Task that returns an observable list of all transaction data of the current users
 */
class GetAllTransactionsTask extends Task{

    protected int uid;
    GetAllTransactionsTask(int uid){
        this.uid = uid;
    }

    @Override
    protected ObservableList<TransactionFX> call(){
        return FXCollections.observableArrayList(Datasource.getInstance().queryTransactions(uid));
    }
}
