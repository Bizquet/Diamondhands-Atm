module com.project.atm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.project.atm to javafx.fxml;
    exports com.project.atm;
    exports com.project.atm.controllers;
    opens com.project.atm.controllers to javafx.fxml;
    opens com.project.atm.datamodel;
}