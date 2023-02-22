package eng.java.project.main.controller.entity.edit;

import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.exception.ParameterNotFound;
import eng.java.project.main.HealthcareApplication;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicalDeviceEditController {
    public TextField name;
    public TextField manufacturerName;
    public TextField type;
    public DatePicker dateOfPurchase;
    public DatePicker expirationDate;
    public ListView<String> medicalDeviceListView;

    private static final String SERIALIZATION_FILE_NAME = "files\\edited_object.dat";
    private static final Logger logger = LoggerFactory.getLogger(MedicalDeviceEditController.class);
    private List<MedicalDevice> allMedicalDevices;
    private MedicalDevice selectedMedicalDevice = null;

    public void initialize() {
        try {
            allMedicalDevices = HealthcareApplication.getDatabaseSource().queryAllMedicalDevices();
        } catch (DatabaseQueryException ex) {
            String msg = "Error ocurred while fetching medical devices to display on edit screen";
            logger.error(msg, ex);

            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.setTitle("Error");
            alert.show();
        }

        medicalDeviceListView.setItems(FXCollections.observableList(allMedicalDevices.stream()
                .map(d -> d.getName() + " " + d.getModelNumber().toString()).toList()));

        medicalDeviceListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                newValue = oldValue;
            }
            String[] selectParts = newValue.split(" ");
            String nameSelected = selectParts[0];
            String modelNumberSelected = selectParts[1];

            selectedMedicalDevice = allMedicalDevices.stream()
                    .filter(d -> d.getName().equals(nameSelected) && d.getModelNumber().toString().equals(modelNumberSelected))
                    .findFirst()
                    .orElse(null);

            if (selectedMedicalDevice != null) {
                name.setText(selectedMedicalDevice.getName());
                manufacturerName.setText(selectedMedicalDevice.getManufacturerName());
                type.setText(selectedMedicalDevice.getTypeOfDevice());
                dateOfPurchase.setValue(selectedMedicalDevice.getDateOfPurchase());
                expirationDate.setValue(selectedMedicalDevice.getExpirationDate());
            }
        });
    }

    public void editMedicalDevice() {
        String alertMessage = "Are you sure you want to edit selected medical device: " + selectedMedicalDevice.getName() + " "
                + selectedMedicalDevice.getModelNumber();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertMessage);
        alert.setTitle("Confirmation dialog");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            StringBuilder sqlQuery = new StringBuilder("UPDATE MEDICAL_DEVICE SET");
            List<String> parameters = new ArrayList<>();
            if(!(name.getText().equals(selectedMedicalDevice.getName()))) {
                selectedMedicalDevice.setName(name.getText());
                sqlQuery.append(" name = ?,");
                parameters.add("name");
            }
            if(!manufacturerName.getText().equals(selectedMedicalDevice.getManufacturerName())) {
                selectedMedicalDevice.setManufacturerName(manufacturerName.getText());
                sqlQuery.append(" manufacturer_name = ?,");
                parameters.add("manufacturer_name");
            }
            if(!type.getText().equals(selectedMedicalDevice.getTypeOfDevice())) {
                selectedMedicalDevice.setTypeOfDevice(type.getText());
                sqlQuery.append(" type = ?,");
                parameters.add("type");
            }
            if(!dateOfPurchase.getValue().equals(selectedMedicalDevice.getDateOfPurchase())) {
                selectedMedicalDevice.setDateOfPurchase(dateOfPurchase.getValue());
                sqlQuery.append(" date_of_purchase = ?,");
                parameters.add("date_of_purchase");
            }
            if(!expirationDate.getValue().equals(selectedMedicalDevice.getExpirationDate())) {
                selectedMedicalDevice.setExpirationDate(expirationDate.getValue());
                sqlQuery.append(" expiration_date = ?,");
                parameters.add("expiration_date");
            }

            sqlQuery.deleteCharAt(sqlQuery.length() - 1);
            sqlQuery.append(" WHERE id = ?");
            parameters.add("id");

            try {
                HealthcareApplication.getDatabaseSource().updateMedicalDevice(selectedMedicalDevice, sqlQuery.toString(), parameters);
                allMedicalDevices = HealthcareApplication.getDatabaseSource().queryAllMedicalDevices();
                medicalDeviceListView.setItems(FXCollections.observableList(allMedicalDevices.stream()
                        .map(d -> d.getName() + " " + d.getModelNumber().toString()).toList()));
                medicalDeviceListView.refresh();

                name.clear();
                manufacturerName.clear();
                type.clear();
                dateOfPurchase.setValue(null);
                expirationDate.setValue(null);

                Alert updatedAlert = new Alert(Alert.AlertType.CONFIRMATION, "Edit successful");
                updatedAlert.setTitle("Success");
                updatedAlert.show();
            } catch (DatabaseQueryException | ParameterNotFound ex) {
                String message = "Error occurred while updating medical device to database on edit screen";
                logger.error(message, ex);

                Alert updatedAlert = new Alert(Alert.AlertType.ERROR, message);
                updatedAlert.setTitle("Error");
                updatedAlert.show();
            }
        }


    }

    public void deleteMedicalDevice() {
        String alertMessage = "Are you sure you want to edit selected medical device: " + selectedMedicalDevice.getName() + " "
                + selectedMedicalDevice.getModelNumber();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertMessage);
        alert.setTitle("Confirmation dialog");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                HealthcareApplication.getDatabaseSource().deleteMedicalDevice(selectedMedicalDevice);
            } catch (DatabaseQueryException ex) {
                String message = "Error occured on edit screen, while deleting selected medical device from database";
                logger.error(message, ex);

                Alert deleteAlert = new Alert(Alert.AlertType.ERROR, message);
                deleteAlert.setTitle("Error");
                deleteAlert.show();
            }
        }
    }
}
