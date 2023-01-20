package com.project.atm;

import com.project.atm.controllers.View;
import com.project.atm.datamodel.Datasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(View.LOGIN.getFilename()));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("DiamondHands ATM");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(!Datasource.getInstance().open()){
            System.out.println("Fatal Error");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
    }

    public static void main(String[] args) {
        launch();
    }
}