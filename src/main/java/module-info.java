module eng.java.project.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires com.h2database;


    opens eng.java.project.main to javafx.fxml;
    exports eng.java.project.main;
    exports eng.java.project.main.controller;
    opens eng.java.project.database to javafx.fxml;
    exports eng.java.project.database;
    opens eng.java.project.exception to javafx.fxml;
    opens eng.java.project.main.controller.entity.edit to javafx.fxml;
    exports eng.java.project.main.controller.entity.edit;
    exports eng.java.project.exception;
    opens eng.java.project.main.controller to javafx.fxml;
    opens eng.java.project.entity to javafx.fxml;
    exports eng.java.project.entity;
    exports eng.java.project.entity.history;
    opens eng.java.project.entity.history to javafx.fxml;
    exports eng.java.project.entity.hospital.core;
    opens eng.java.project.entity.hospital.core to javafx.fxml;
    exports eng.java.project.entity.hospital.util;
    opens eng.java.project.entity.hospital.util to javafx.fxml;
    exports eng.java.project.main.controller.entity.view;
    opens eng.java.project.main.controller.entity.view to javafx.fxml;
    exports eng.java.project.main.controller.entity.insert;
    opens eng.java.project.main.controller.entity.insert to javafx.fxml;
}