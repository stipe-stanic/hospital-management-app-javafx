package eng.java.project.main.controller.entity.edit;

import eng.java.project.entity.hospital.core.Department;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.exception.ParameterNotFound;
import eng.java.project.main.HealthcareApplication;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentEditController {
    public ListView<String> departmentListView;
    public TextField name;

    private static final String SERIALIZATION_FILE_NAME = "files\\edited_object.dat";
    private static final Logger logger = LoggerFactory.getLogger(DepartmentEditController.class);
    private List<Department> allDepartments;
    private Department selectedDepartment = null;

    public void initialize() {
        try {
            allDepartments = HealthcareApplication.getDatabaseSource().queryAllDepartments();
        } catch (DatabaseQueryException ex) {
            String msg = "Error ocurred while fetching departments to display on edit screen";
            logger.error(msg, ex);

            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.setTitle("Error");
            alert.show();
        }

        departmentListView.setItems(FXCollections.observableList(allDepartments.stream()
                .map(Department::getName).toList()));

        departmentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                newValue = oldValue;
            }
            String[] selectParts = newValue.split(" ");
            String nameSelected = selectParts[0];

            selectedDepartment = allDepartments.stream()
                    .filter(d -> d.getName().equals(nameSelected))
                    .findFirst()
                    .orElse(null);

            if (selectedDepartment != null) {
                name.setText(selectedDepartment.getName());
            }
        });
    }

    public void editDepartment() {
        String alertMessage = "Are you sure you want to edit selected department: " + selectedDepartment.getName();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertMessage);
        alert.setTitle("Confirmation dialog");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            StringBuilder sqlQuery = new StringBuilder("UPDATE DEPARTMENT SET");
            List<String> parameters = new ArrayList<>();
            if(!(name.getText().equals(selectedDepartment.getName()))) {
                selectedDepartment.setName(name.getText());
                sqlQuery.append(" name = ?,");
                parameters.add("name");
            }

            sqlQuery.deleteCharAt(sqlQuery.length() - 1);
            sqlQuery.append(" WHERE id = ?");
            parameters.add("id");

            try {
                HealthcareApplication.getDatabaseSource().updateDepartment(selectedDepartment, sqlQuery.toString(), parameters);
                allDepartments = HealthcareApplication.getDatabaseSource().queryAllDepartments();
                departmentListView.setItems(FXCollections.observableList(allDepartments.stream()
                        .map(Department::getName).toList()));
                departmentListView.refresh();

                name.clear();

                Alert updatedAlert = new Alert(Alert.AlertType.CONFIRMATION, "Edit successful");
                updatedAlert.setTitle("Success");
                updatedAlert.show();
            } catch (DatabaseQueryException | ParameterNotFound ex) {
                String message = "Error occurred while updating department to database on edit screen";
                logger.error(message, ex);

                Alert updatedAlert = new Alert(Alert.AlertType.ERROR, message);
                updatedAlert.setTitle("Error");
                updatedAlert.show();
            }
        }
    }

    public void deleteDepartment() {
        String alertMessage = "Are you sure you want to edit selected department: " + selectedDepartment.getName();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertMessage);
        alert.setTitle("Confirmation dialog");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                HealthcareApplication.getDatabaseSource().deleteDepartment(selectedDepartment);
            } catch (DatabaseQueryException ex) {
                String message = "Error occured on edit screen, while deleting selected department from database";
                logger.error(message, ex);

                Alert deleteAlert = new Alert(Alert.AlertType.ERROR, message);
                deleteAlert.setTitle("Error");
                deleteAlert.show();
            }
        }
    }
}
