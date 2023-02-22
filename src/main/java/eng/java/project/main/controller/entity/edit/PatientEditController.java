package eng.java.project.main.controller.entity.edit;

import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.entity.hospital.util.EditInfo;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.exception.ParameterNotFound;
import eng.java.project.main.GlobalUser;
import eng.java.project.main.HealthcareApplication;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientEditController {

    public TextField name;
    public TextField surname;
    public TextField phoneNumber;
    public DatePicker dateOfBirth;
    public ListView<String> patientListView;

    private static final String SERIALIZATION_FILE_NAME = "files\\edited_patient_object.dat";
    private static final Logger logger = LoggerFactory.getLogger(PatientEditController.class);
    private List<Patient> allPatients;
    private Patient selectedPatient = null;

    private List<String> preEdit = new ArrayList<>();
    private List<String> afterEdit = new ArrayList<>();
    GlobalUser user = GlobalUser.getInstance();

    public void initialize() {
        try {
            allPatients = HealthcareApplication.getDatabaseSource().queryAllPatients();
        } catch (DatabaseQueryException ex) {
            String msg = "Error ocurred while fetching patients to display on edit screen";
            logger.error(msg, ex);

            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.setTitle("Error");
            alert.show();
        }

        patientListView.setItems(FXCollections.observableList(allPatients.stream()
                .map(d -> d.getName() + " " + d.getSurname()).toList()));

        patientListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                newValue = oldValue;
            }
            String[] selectParts = newValue.split(" ");
            String nameSelected = selectParts[0];
            String surnameSelected = selectParts[1];

            selectedPatient = allPatients.stream()
                    .filter(d -> d.getName().equals(nameSelected) && d.getSurname().equals(surnameSelected))
                    .findFirst()
                    .orElse(null);

            if (selectedPatient != null) {
                name.setText(selectedPatient.getName());
                surname.setText(selectedPatient.getSurname());
                dateOfBirth.setValue(selectedPatient.getDateOfBirth());
                phoneNumber.setText(selectedPatient.getPhoneNumber());
            }
        });
    }

    public void editPatient() {
        String alertMessage = "Are you sure you want to edit selected patient: " + selectedPatient.getName() + " "
                + selectedPatient.getSurname();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertMessage);
        alert.setTitle("Confirmation dialog");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Patient preEditPatient = new Patient(selectedPatient);

            StringBuilder sqlQuery = new StringBuilder("UPDATE PATIENT SET");
            List<String> parameters = new ArrayList<>();
            if(!(name.getText().equals(selectedPatient.getName()))) {
                selectedPatient.setName(name.getText());
                sqlQuery.append(" name = ?,");
                parameters.add("name");

                preEdit.add(preEditPatient.getName());
                afterEdit.add(selectedPatient.getName());
            }
            if(!surname.getText().equals(selectedPatient.getSurname())) {
                selectedPatient.setSurname(surname.getText());
                sqlQuery.append(" surname = ?,");
                parameters.add("surname");

                preEdit.add(preEditPatient.getSurname());
                afterEdit.add(selectedPatient.getSurname());
            }
            if(!dateOfBirth.getValue().equals(selectedPatient.getDateOfBirth())) {
                selectedPatient.setDateOfBirth(dateOfBirth.getValue());
                sqlQuery.append(" date_of_birth = ?,");
                parameters.add("date_of_birth");

                preEdit.add(preEditPatient.getDateOfBirth().toString());
                afterEdit.add(selectedPatient.getDateOfBirth().toString());
            }
            if(!phoneNumber.getText().equals(selectedPatient.getPhoneNumber())) {
                selectedPatient.setName(phoneNumber.getText());
                sqlQuery.append(" phone_number = ?,");
                parameters.add("phone_number");

                preEdit.add(preEditPatient.getPhoneNumber());
                afterEdit.add(selectedPatient.getPhoneNumber());
            }

            sqlQuery.deleteCharAt(sqlQuery.length() - 1);
            sqlQuery.append(" WHERE id = ?");
            parameters.add("id");




            try {
                HealthcareApplication.getDatabaseSource().updatePatient(selectedPatient, sqlQuery.toString(), parameters);
                Patient afterEditPatient = new Patient(selectedPatient);
                allPatients = HealthcareApplication.getDatabaseSource().queryAllPatients();
                patientListView.setItems(FXCollections.observableList(allPatients.stream()
                        .map(d -> d.getName() + " " + d.getSurname()).toList()));
                patientListView.refresh();

                try (ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream(SERIALIZATION_FILE_NAME, true))) {
                    EditInfo<Patient, GlobalUser> editInfo = new EditInfo<>(afterEditPatient, user, preEdit, afterEdit, LocalDate.now(), LocalTime.now());
                    out.writeObject(editInfo);
                } catch (IOException ex) {
                    String msg = "Error occured while serialiazing 'EditInfo' object";
                    logger.error(msg, ex);
                }

                name.clear();
                surname.clear();
                dateOfBirth.setValue(null);
                phoneNumber.clear();

                Alert updatedAlert = new Alert(Alert.AlertType.CONFIRMATION, "Edit successful");
                updatedAlert.setTitle("Success");
                updatedAlert.show();

            } catch (DatabaseQueryException | ParameterNotFound ex) {
                String message = "Error occurred while updating patient to database on edit screen";
                logger.error(message, ex);

                Alert updatedAlert = new Alert(Alert.AlertType.ERROR, message);
                updatedAlert.setTitle("Error");
                updatedAlert.show();
            }
        }
    }


    public void deletePatient() {
        String alertMessage = "Are you sure you want to remove selected patient: " + selectedPatient.getName() + " "
                + selectedPatient.getSurname();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertMessage);
        alert.setTitle("Confirmation dialog");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                HealthcareApplication.getDatabaseSource().deletePatient(selectedPatient);
            } catch (DatabaseQueryException ex) {
                String message = "Error occured on edit screen, while deleting selected patient from database";
                logger.error(message, ex);

                Alert deleteAlert = new Alert(Alert.AlertType.ERROR, message);
                deleteAlert.setTitle("Error");
                deleteAlert.show();
            }
        }
    }
}
