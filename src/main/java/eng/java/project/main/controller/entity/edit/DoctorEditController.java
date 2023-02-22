package eng.java.project.main.controller.entity.edit;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    private List<String> preEdit = new ArrayList<>();
    private List<String> afterEdit = new ArrayList<>();

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

        doctorListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                newValue = oldValue;
            }
            String[] selectParts = newValue.split(" ");
            String titleSelectedFirst = selectParts[0];
            String titleSelectedSecond = selectParts[1];
            String nameSelected = selectParts[2];
            String surnameSelected = selectParts[3];

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
            Doctor preEditDoctor = new Doctor(selectedDoctor);

            StringBuilder sqlQuery = new StringBuilder("UPDATE DOCTOR SET");
            List<String> parameters = new ArrayList<>();
            if(!(name.getText().equals(selectedDoctor.getName()))) {
                selectedDoctor.setName(name.getText());
                sqlQuery.append(" name = ?,");
                parameters.add("name");

                preEdit.add(preEditDoctor.getName());
                afterEdit.add(selectedDoctor.getName());
            }
            if(!surname.getText().equals(selectedDoctor.getSurname())) {
                selectedDoctor.setSurname(surname.getText());
                sqlQuery.append(" surname = ?,");
                parameters.add("surname");

                preEdit.add(preEditDoctor.getSurname());
                afterEdit.add(selectedDoctor.getSurname());
            }
            if(!title.getText().equals(selectedDoctor.getTitle())) {
                selectedDoctor.setTitle(title.getText());
                sqlQuery.append(" title = ?,");
                parameters.add("title");

                preEdit.add(preEditDoctor.getTitle());
                afterEdit.add(selectedDoctor.getTitle());
            }
            if(!phoneNumber.getText().equals(selectedDoctor.getPhoneNumber())) {
                selectedDoctor.setName(phoneNumber.getText());
                sqlQuery.append(" phone_number = ?,");
                parameters.add("phone_number");

                preEdit.add(preEditDoctor.getPhoneNumber());
                afterEdit.add(selectedDoctor.getPhoneNumber());
            }
            if(!yearsOfExperience.getText().equals(selectedDoctor.getYearsOfExperience().toString())) {
                selectedDoctor.setYearsOfExperience(Integer.valueOf(yearsOfExperience.getText()));
                sqlQuery.append(" years_of_experience = ?,");
                parameters.add("years_of_experience");

                preEdit.add(preEditDoctor.getYearsOfExperience().toString());
                afterEdit.add(selectedDoctor.getYearsOfExperience().toString());
            }
            if(!workStartTime.getText().equals(selectedDoctor.getWorkStartTime().toString())) {
                selectedDoctor.setWorkStartTime(LocalTime.parse(workStartTime.getText()));
                sqlQuery.append(" work_start_time = ?,");
                parameters.add("work_start_time");

                preEdit.add(preEditDoctor.getWorkStartTime().toString());
                afterEdit.add(selectedDoctor.getWorkStartTime().toString());
            }
            if(!workEndTime.getText().equals(selectedDoctor.getWorkEndTime().toString())) {
                selectedDoctor.setWorkEndTime(LocalTime.parse(workEndTime.getText()));
                sqlQuery.append(" work_end_time = ?,");
                parameters.add("work_end_time");

                preEdit.add(preEditDoctor.getWorkEndTime().toString());
                afterEdit.add(selectedDoctor.getWorkEndTime().toString());
            }
            if(!dateOfBirth.getValue().equals(selectedDoctor.getDateOfBirth())) {
                selectedDoctor.setDateOfBirth(dateOfBirth.getValue());
                sqlQuery.append(" date_of_birth = ?,");
                parameters.add("date_of_birth");

                preEdit.add(preEditDoctor.getDateOfBirth().toString());
                afterEdit.add(selectedDoctor.getDateOfBirth().toString());
            }

            sqlQuery.deleteCharAt(sqlQuery.length() - 1);
            sqlQuery.append(" WHERE id = ?");
            parameters.add("id");

            try {
                HealthcareApplication.getDatabaseSource().updateDoctor(selectedDoctor, sqlQuery.toString(), parameters);
                Doctor afterEditDoctor = new Doctor(selectedDoctor);
                allDoctors = HealthcareApplication.getDatabaseSource().queryAllDoctors();
                doctorListView.setItems(FXCollections.observableList(allDoctors.stream()
                        .map(d -> d.getTitle() + " " + d.getName() + " " + d.getSurname()).toList()));
                doctorListView.refresh();


                try (ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream(SERIALIZATION_FILE_NAME, true))) {
                    EditInfo<Doctor, GlobalUser> editInfo = new EditInfo<>(afterEditDoctor, user, preEdit, afterEdit, LocalDate.now(), LocalTime.now());
                    out.writeObject(editInfo);
                } catch (IOException ex) {
                    String msg = "Error occured while serialiazing 'EditInfo' object";
                    logger.error(msg, ex);
                }

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
}
