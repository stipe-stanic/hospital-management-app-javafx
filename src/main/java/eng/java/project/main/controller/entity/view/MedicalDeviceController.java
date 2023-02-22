package eng.java.project.main.controller.entity.view;

import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.main.HealthcareApplication;
import eng.java.project.main.LocalDateConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class MedicalDeviceController {
    public TextField name;
    public TextField manufacturerName;
    public TextField modelNumber;
    public TextField type;
    public DatePicker dateOfPurchase;
    public DatePicker expirationDate;
    public TableView<MedicalDevice> medicalDeviceTableView;
    public TableColumn<MedicalDevice, String> nameColumn;
    public TableColumn<MedicalDevice, String> manufacturerNameColumn;
    public TableColumn<MedicalDevice, String> modelNumberColumn;
    public TableColumn<MedicalDevice, String> serialNumberColumn;
    public TableColumn<MedicalDevice, String> typeColumn;
    public TableColumn<MedicalDevice, String> dateOfPurchaseColumn;
    public TableColumn<MedicalDevice, String> expirationDateColumn;

    private static final Logger logger = LoggerFactory.getLogger(MedicalDeviceController.class);
    private List<MedicalDevice> allMedicalDevices;

    public void initialize() {
        dateOfPurchase.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("d.M.yyyy")));
        expirationDate.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("d.M.yyyy")));

        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        manufacturerNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getManufacturerName()));
        modelNumberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModelNumber().toString()));
        serialNumberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSerialNumber().toString()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTypeOfDevice()));
        dateOfPurchaseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateOfPurchase()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        expirationDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExpirationDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

        try {
            allMedicalDevices = HealthcareApplication.getDatabaseSource().queryAllMedicalDevices();
        } catch (DatabaseQueryException ex) {
            String msg = "Error ocurred while fetching medical devices to display on search screen";
            logger.error(msg, ex);

            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.setTitle("Error");
            alert.show();
        }

        medicalDeviceTableView.setItems(FXCollections.observableList(allMedicalDevices));

    }
    public void searchMedicalDevices() {
        List<MedicalDevice> filtered = allMedicalDevices.stream()
                .filter(m -> m.getName().toLowerCase().contains(name.getText().toLowerCase()))
                .filter(m -> m.getManufacturerName().toLowerCase().contains(manufacturerName.getText().toLowerCase()))
                .filter(m -> m.getModelNumber().toString().toLowerCase().contains(modelNumber.getText().toLowerCase()))
                .filter(m -> m.getTypeOfDevice().toLowerCase().contains(type.getText().toLowerCase()))
                .filter(m -> dateOfPurchase.getValue() == null || m.getDateOfPurchase().equals(dateOfPurchase.getValue()))
                .filter(m -> expirationDate.getValue() == null || m.getExpirationDate().equals(expirationDate.getValue()))
                .toList();

        medicalDeviceTableView.setItems(FXCollections.observableList(filtered));
    }
}
