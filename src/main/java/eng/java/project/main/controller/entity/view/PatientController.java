package eng.java.project.main.controller.entity.view;

import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.entity.hospital.util.Symptom;
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
import java.util.List;
import java.util.stream.Collectors;

public class PatientController {
    public TextField name;
    public TextField surname;
    public TextField symptom;
    public TextField insuranceName;
    public DatePicker dateOfBirth;
    public DatePicker appointmentDate;
    public DatePicker treatmentDate;
    public TableView<Patient> patientTableView;
    public TableColumn<Patient, String> nameColumn;
    public TableColumn<Patient, String> surnameColumn;
    public TableColumn<Patient, String> dateOfBirthColumn;
    public TableColumn<Patient, String> phoneNumberColumn;
    public TableColumn<Patient, String> appointmentDateColumn;
    public TableColumn<Patient, String> treatmentDateColumn;
    public TableColumn<Patient, String> symptomColumn;
    public TableColumn<Patient, String> insuranceColumn;

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);
    private List<Patient> allPatients;

    public void initialize() {
        dateOfBirth.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("d.M.yyyy")));
        appointmentDate.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("d.M.yyyy")));
        treatmentDate.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("d.M.yyyy")));

        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        surnameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurname()));
        dateOfBirthColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateOfBirth()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        phoneNumberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));
        appointmentDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentList().stream()
                .map(a -> a.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + a.getTime()).collect(Collectors.joining("\n"))));
        treatmentDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTreatmentsList().stream()
                .map(t -> t.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + t.getTime()).collect(Collectors.joining("\n"))));
        symptomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSymptomsList().stream()
                .map(Symptom::getDescription).collect(Collectors.joining("\n"))));
        insuranceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInsurance().getName()));

        try {
            allPatients = HealthcareApplication.getDatabaseSource().queryAllPatients();
        } catch (DatabaseQueryException ex) {
            String msg = "Error ocurred while fetching patients to display on search screen";
            logger.error(msg, ex);

            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.setTitle("Error");
            alert.show();
        }

        patientTableView.setItems(FXCollections.observableList(allPatients));
    }

    public void searchPatients() {
        List<Patient> filtered = allPatients.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.getText().toLowerCase()))
                .filter(p -> p.getSurname().toLowerCase().contains(surname.getText().toLowerCase()))
                .filter(p -> dateOfBirth.getValue() == null || p.getDateOfBirth().equals(dateOfBirth.getValue()))
                .filter(p -> appointmentDate.getValue() == null || p.getAppointmentList().stream()
                        .anyMatch(d -> d.getDate().equals(appointmentDate.getValue())))
                .filter(p -> treatmentDate.getValue() == null || p.getAppointmentList().stream()
                        .anyMatch(d -> d.getDate().equals(treatmentDate.getValue())))
                .filter(p -> p.getSymptomsList().stream()
                        .anyMatch(s -> s.getDescription().toLowerCase().contains(symptom.getText().toLowerCase())))
                .filter(p -> p.getInsurance().getName().contains(insuranceName.getText().toLowerCase())).toList();

        patientTableView.setItems(FXCollections.observableList(filtered));
    }
}
