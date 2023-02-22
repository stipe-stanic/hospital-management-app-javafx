package eng.java.project.main.controller.entity.view;

import eng.java.project.entity.hospital.core.Department;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.main.HealthcareApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    @FXML
    public TextField departmentName;
    @FXML
    public TextField doctorName;
    @FXML
    public TextField patientName;
    @FXML
    public TextField applianceName;
    @FXML
    public TableView<Department> departmentTableView;
    @FXML
    public TableColumn<Department, String> departmentColumn;
    @FXML
    public TableColumn<Department, String> doctorColumn;
    @FXML
    public TableColumn<Department, String> patientColumn;
    @FXML
    public TableColumn<Department, String> applianceColumn;

    private List<Department> allDepartments;

    public void initialize() {
        departmentColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        doctorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDoctorList().stream()
                .map(d -> d.getTitle() + " " + d.getName() + " " + d.getSurname()).collect(Collectors.joining("\n"))));
        patientColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPatientList().stream()
                .map(p -> p.getName() + " " + p.getSurname()).collect(Collectors.joining("\n"))));
        applianceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppliancesList().stream()
                .map(MedicalDevice::getName).collect(Collectors.joining("\n"))));

        try {
            allDepartments = HealthcareApplication.getDatabaseSource().queryAllDepartments();
        } catch (DatabaseQueryException ex) {
            String msg = "Error occured while fetching departments to display to search screen";
            logger.error(msg, ex);

            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.setTitle("Error");
            alert.show();
        }

        departmentTableView.setItems(FXCollections.observableList(allDepartments));
    }
    public void filterDepartments() {

        List<Department> filtered = allDepartments.stream()
                .filter(d -> d.getName().toLowerCase().contains(departmentName.getText().toLowerCase()))
                .filter(d -> d.getDoctorList().stream()
                        .anyMatch(doc -> (doc.getName() + " " + doc.getSurname()).toLowerCase().contains(doctorName.getText().toLowerCase())))
                .filter(d -> d.getPatientList().stream()
                        .anyMatch(p -> (p.getName() + " " + p.getSurname()).toLowerCase().contains(patientName.getText().toLowerCase())))
                .filter(d -> d.getAppliancesList().stream()
                        .anyMatch(a -> (a.getName()).toLowerCase().contains(applianceName.getText().toLowerCase()))).toList();

        departmentTableView.setItems(FXCollections.observableList(filtered));
    }
}
