package eng.java.project.main.controller;

import eng.java.project.main.HealthcareApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Objects;

public class MenuController {
    public void showDoctors() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("view/doctors_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insertNewDoctor() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("insert/doctors_insert_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void editDoctor() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("edit/doctors_edit_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPatients() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("view/patients_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insertNewPatient() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("insert/patients_insert_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void editPatient() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("edit/patients_edit_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDepartments() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("view/departments_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insertNewDepartment() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("insert/departments_insert_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void editDepartment() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("edit/departments_edit_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMedicalDevices() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("view/medical_devices_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insertNewMedicalDevice() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("insert/medical_devices_insert_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void editMedicalDevice() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("edit/medical_devices_edit_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEditHistory() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("edit_history_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHome() {
        GridPane root;
        try {
            root = (GridPane) FXMLLoader.load(Objects.requireNonNull(HealthcareApplication.class.getResource("start_view.fxml")));
            HealthcareApplication.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
