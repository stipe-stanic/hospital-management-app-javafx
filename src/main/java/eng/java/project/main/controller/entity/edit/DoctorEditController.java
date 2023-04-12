package eng.java.project.main.controller.entity.edit;

import eng.java.project.entity.hospital.core.CoreObject;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.util.EditInfo;
import eng.java.project.entity.hospital.util.Symptom;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.exception.ParameterNotFound;
import eng.java.project.main.GlobalUser;
import eng.java.project.main.HealthcareApplication;
import eng.java.project.main.controller.entity.view.DoctorController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.h2.engine.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.CredentialException;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DoctorEditController {
    public TextField name;
    public TextField surname;
    public TextField title;
    public TextField phoneNumber;
    public TextField yearsOfExperience;
    public TextField workStartTime;
    public TextField workEndTime;
    public DatePicker dateOfBirth;
    public ListView<String> doctorListView;

    private static final String SERIALIZATION_FILE_NAME = "files\\edited_doctor_object.dat";
    private static final Logger logger = LoggerFactory.getLogger(DoctorEditController.class);
    
    private List<Doctor> allDoctors;
    private Doctor selectedDoctor = null;
    private final List<String> originalAttributes = new ArrayList<>();
    private final List<String> editedAttributes = new ArrayList<>();
    GlobalUser user = GlobalUser.getInstance();

    public void initialize() {
        try {
            allDoctors = HealthcareApplication.getDatabaseSource().queryAllDoctors();
        } catch (DatabaseQueryException ex) {
            String msg = "Error ocurred while fetching doctors to display on edit screen";
            logger.error(msg, ex);

            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.setTitle("Error");
            alert.show();
        }

        doctorListView.setItems(FXCollections.observableList(allDoctors.stream()
                .map(d -> d.getTitle() + " " + d.getName() + " " + d.getSurname()).toList()));

        // Get selected doctor and fill text fields with its attributes
        doctorListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                newValue = oldValue;
            }
            String[] selectedParts = newValue.split(" ");
            String titleSelectedFirst = selectedParts[0];
            String titleSelectedSecond = selectedParts[1];
            String nameSelected = selectedParts[2];
            String surnameSelected = selectedParts[3];

            selectedDoctor = allDoctors.stream()
                    .filter(d -> d.getTitle().equals(titleSelectedFirst + " " + titleSelectedSecond) && d.getName().equals(nameSelected) && d.getSurname().equals(surnameSelected))
                    .findFirst()
                    .orElse(null);

            if (selectedDoctor != null) {
                name.setText(selectedDoctor.getName());
                surname.setText(selectedDoctor.getSurname());
                title.setText(selectedDoctor.getTitle());
                phoneNumber.setText(selectedDoctor.getPhoneNumber());
                yearsOfExperience.setText(selectedDoctor.getYearsOfExperience().toString());
                workStartTime.setText(selectedDoctor.getWorkStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                workEndTime.setText(selectedDoctor.getWorkEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                dateOfBirth.setValue(selectedDoctor.getDateOfBirth());
            }
        });
    }

    public void editDoctor() {
        String alertMessage = "Are you sure you want to edit selected doctor: " + selectedDoctor.getTitle() + " " + selectedDoctor.getName() + " "
                + selectedDoctor.getSurname();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertMessage);
        alert.setTitle("Confirmation dialog");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Doctor originalDoctor = new Doctor(selectedDoctor);
            StringBuilder sqlQuery = new StringBuilder("UPDATE DOCTOR SET");

            // Maps sqlQuery and parameters values
            Map<String, String> queryParameterMap = buildDynamicQuery(originalDoctor);
            for (String key : queryParameterMap.keySet()) {
                sqlQuery.append(" ").append(key).append(",");
            }

            // Deletes the last coma
            sqlQuery.deleteCharAt(sqlQuery.length() - 1);
            sqlQuery.append(" WHERE id = ?");

            List<String> parameters = new ArrayList<>(queryParameterMap.values());
            parameters.add("id");

            try {
                HealthcareApplication.getDatabaseSource().updateDoctor(selectedDoctor, sqlQuery.toString(), parameters);
                Doctor editedAttributesDoctor = new Doctor(selectedDoctor);
                allDoctors = HealthcareApplication.getDatabaseSource().queryAllDoctors();
                doctorListView.setItems(FXCollections.observableList(allDoctors.stream()
                        .map(d -> d.getTitle() + " " + d.getName() + " " + d.getSurname()).toList()));
                doctorListView.refresh();

                // Check if serialized file already exists, updates list with the deserialized list
                File file = new File(SERIALIZATION_FILE_NAME);
                List<EditInfo<CoreObject, GlobalUser>> editedDoctors = new ArrayList<>();
                if(file.exists()) {
                    editedDoctors = deserializeEditedDoctors();
                }

                // Adds new edited doctor to deserialized list
                serializeEditedDoctors(editedDoctors, editedAttributesDoctor);

                // Clearing lists of attributes since they are globally declared
                originalAttributes.clear();
                editedAttributes.clear();

                // Clearing text fields after the update
                name.clear();
                surname.clear();
                title.clear();
                phoneNumber.clear();
                yearsOfExperience.clear();
                workStartTime.clear();
                workEndTime.clear();
                dateOfBirth.setValue(null);

                Alert updatedAlert = new Alert(Alert.AlertType.CONFIRMATION, "Edit successful");
                updatedAlert.setTitle("Success");
                updatedAlert.show();
            } catch (DatabaseQueryException | ParameterNotFound ex) {
                String message = "Error occurred while updating doctor to database on edit screen";
                logger.error(message, ex);

                Alert updatedAlert = new Alert(Alert.AlertType.ERROR, message);
                updatedAlert.setTitle("Error");
                updatedAlert.show();
            }
        }
    }

    public void deleteDoctor() {
        String alertMessage = "Are you sure you want to remove selected doctor: " + selectedDoctor.getTitle() + " " + selectedDoctor.getName() + " "
                + selectedDoctor.getSurname();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertMessage);
        alert.setTitle("Confirmation dialog");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                HealthcareApplication.getDatabaseSource().deleteDoctor(selectedDoctor);
            } catch (DatabaseQueryException ex) {
                String message = "Error occured on edit screen, while deleting selected doctor from database";
                logger.error(message, ex);

                Alert deleteAlert = new Alert(Alert.AlertType.ERROR, message);
                deleteAlert.setTitle("Error");
                deleteAlert.show();
            }
        }
    }

    private Map<String, String> buildDynamicQuery(Doctor originalDoctor) {
        Map<String, String> queryParameterMap = new HashMap<>();

        // Checks if text field value is edited
        if(!(name.getText().equals(selectedDoctor.getName()))) {
            selectedDoctor.setName(name.getText());
            queryParameterMap.put("name = ?", "name");

            originalAttributes.add(originalDoctor.getName());
            editedAttributes.add(selectedDoctor.getName());
        }
        if(!surname.getText().equals(selectedDoctor.getSurname())) {
            selectedDoctor.setSurname(surname.getText());
            queryParameterMap.put("surname = ?", "surname");

            originalAttributes.add(originalDoctor.getSurname());
            editedAttributes.add(selectedDoctor.getSurname());
        }
        if(!title.getText().equals(selectedDoctor.getTitle())) {
            selectedDoctor.setTitle(title.getText());
            queryParameterMap.put("title = ?", "title");

            originalAttributes.add(originalDoctor.getTitle());
            editedAttributes.add(selectedDoctor.getTitle());
        }
        if(!phoneNumber.getText().equals(selectedDoctor.getPhoneNumber())) {
            selectedDoctor.setName(phoneNumber.getText());
            queryParameterMap.put("phone_number = ?", "phone_number");

            originalAttributes.add(originalDoctor.getPhoneNumber());
            editedAttributes.add(selectedDoctor.getPhoneNumber());
        }
        if(!yearsOfExperience.getText().equals(selectedDoctor.getYearsOfExperience().toString())) {
            selectedDoctor.setYearsOfExperience(Integer.valueOf(yearsOfExperience.getText()));
            queryParameterMap.put("years_of_experience = ?", "years_of_experience");

            originalAttributes.add(originalDoctor.getYearsOfExperience().toString());
            editedAttributes.add(selectedDoctor.getYearsOfExperience().toString());
        }
        if(!workStartTime.getText().equals(selectedDoctor.getWorkStartTime().toString())) {
            selectedDoctor.setWorkStartTime(LocalTime.parse(workStartTime.getText()));
            queryParameterMap.put("work_start_time = ?", "work_start_time");

            originalAttributes.add(originalDoctor.getWorkStartTime().toString());
            editedAttributes.add(selectedDoctor.getWorkStartTime().toString());
        }
        if(!workEndTime.getText().equals(selectedDoctor.getWorkEndTime().toString())) {
            selectedDoctor.setWorkEndTime(LocalTime.parse(workEndTime.getText()));
            queryParameterMap.put("work_end_time = ?", "work_end_time");

            originalAttributes.add(originalDoctor.getWorkEndTime().toString());
            editedAttributes.add(selectedDoctor.getWorkEndTime().toString());
        }
        if(!dateOfBirth.getValue().equals(selectedDoctor.getDateOfBirth())) {
            selectedDoctor.setDateOfBirth(dateOfBirth.getValue());
            queryParameterMap.put("date_of_birth = ?", "date_of_birth");

            originalAttributes.add(originalDoctor.getDateOfBirth().toString());
            editedAttributes.add(selectedDoctor.getDateOfBirth().toString());
        }

        return queryParameterMap;
    }
    private List<EditInfo<CoreObject, GlobalUser>> deserializeEditedDoctors() {
        List<EditInfo<CoreObject, GlobalUser>> editedDoctors = new ArrayList<>();

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALIZATION_FILE_NAME))) {
            try {
                editedDoctors = (List<EditInfo<CoreObject, GlobalUser>>) in.readObject();
            } catch (EOFException ex) {
                String msg = "Reached end of file when deserializing 'EditInfo' objects";
                logger.info(msg, ex);
            }
        } catch (IOException | ClassNotFoundException ex) {
            String msg = "Error occurred while attempting deserialization of 'EditInfo' object";
            logger.error(msg, ex);
        }

        return editedDoctors;
    }
    private void serializeEditedDoctors(List<EditInfo<CoreObject, GlobalUser>> editedDoctors, Doctor editedAttributesDoctor) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SERIALIZATION_FILE_NAME))) {
            EditInfo<CoreObject, GlobalUser> editInfo = new EditInfo<>(editedAttributesDoctor, user, originalAttributes, editedAttributes, LocalDate.now(), LocalTime.now());
            editedDoctors.add(editInfo);
            out.writeObject(editedDoctors);
        } catch (IOException ex) {
            String msg = "Error occured while serialiazing 'EditInfo' object";
            logger.error(msg, ex);
        }
    }
}
