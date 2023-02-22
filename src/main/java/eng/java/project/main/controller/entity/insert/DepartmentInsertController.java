package eng.java.project.main.controller.entity.insert;

import eng.java.project.entity.hospital.core.Department;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.main.HealthcareApplication;
import eng.java.project.main.controller.entity.edit.DepartmentEditController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DepartmentInsertController {
    @FXML
    TextField name;
    private static final Logger logger = LoggerFactory.getLogger(DepartmentInsertController.class);
    @FXML
    public void insertNewDepartment() {
        List<String> messages = new ArrayList<>();

        if (name.getText().isBlank()) {
            messages.add("Name field is empty");
        }

        if(messages.size() == 0) {
            Department department = new Department.DepartmentBuilder((long) - 1).withName(name.getText()).build();

            try {
                HealthcareApplication.getDatabaseSource().insertDepartment(department);
                name.clear();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Entry successful");
                alert.setTitle("Success");
                alert.show();
            } catch(DatabaseQueryException ex) {
                String msg = "Error occurred on 'insert' screen while inserting new department object to dabase";
                logger.error(msg, ex);

                Alert alert = new Alert(Alert.AlertType.ERROR, msg);
                alert.setTitle("Error");
                alert.show();
            }
        } else {
            String msg = String.join("\n", messages);

            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.setTitle("Warning");
            alert.show();
        }
    }
}
