package eng.java.project.main.controller.entity.insert;

import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.main.HealthcareApplication;
import eng.java.project.main.LocalDateConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MedicalDeviceInsertController {
    @FXML
    public TextField name;
    @FXML
    public TextField manufacturerName;
    @FXML
    public TextField modelNumber;
    @FXML
    public TextField serialNumber;
    @FXML
    public TextField type;
    @FXML
    public DatePicker dateOfPurchase;
    @FXML
    public TextField departmentId;
    @FXML
    public DatePicker expirationDate;

    private static final Logger logger = LoggerFactory.getLogger(MedicalDeviceInsertController.class);

    public void initialize() {
        dateOfPurchase.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        expirationDate.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @FXML
    public void insertNewMedicalDevice() {
        List<String> messages = new ArrayList<>();

        if (name.getText().isBlank()) {
            messages.add("Name field is empty");
        }
        if (manufacturerName.getText().isBlank()) {
            messages.add("Manufacturer field is empty");
        }
        if (modelNumber.getText() == null) {
            messages.add("Model number field is empty");
        }
        if (serialNumber.getText().isBlank()) {
            messages.add("Serial number field is empty");
        }
        if (type.getText().isBlank()) {
            messages.add("Type field is empty");
        }
        if(dateOfPurchase.getValue() == null) {
            messages.add("Date of purchase is not choosen");
        }
        if(expirationDate.getValue() == null) {
            messages.add("Expiration date is not choosen");
        }
        if (departmentId.getText().isBlank()) {
            messages.add("Department id field is empty");
        }

        if(messages.size() == 0) {
            MedicalDevice medicalDevice = new MedicalDevice((long) - 1, name.getText(), manufacturerName.getText(),
                    Integer.valueOf(modelNumber.getText()), Long.valueOf(serialNumber.getText()), type.getText(), dateOfPurchase.getValue(),
                    expirationDate.getValue());

            try {
                HealthcareApplication.getDatabaseSource().insertMedicalDevice(medicalDevice, Long.valueOf(departmentId.getText()));

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Entry successful");
                alert.setTitle("Success");
                alert.show();

                name.clear();
                manufacturerName.clear();
                modelNumber.clear();
                serialNumber.clear();
                type.clear();
                dateOfPurchase.setValue(null);
                expirationDate.setValue(null);
                departmentId.clear();
            } catch(DatabaseQueryException ex) {
                String msg = "Error occurred on 'insert' screen while inserting new medical device object to database";
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
