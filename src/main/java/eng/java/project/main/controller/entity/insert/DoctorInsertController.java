package eng.java.project.main.controller.entity.insert;

import eng.java.project.entity.hospital.core.Department;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.main.HealthcareApplication;
import eng.java.project.main.LocalDateConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DoctorInsertController {
    @FXML
    public TextField name;
    @FXML
    public TextField surname;
    @FXML
    public TextField title;
    @FXML
    public TextField licenseNumber;
    @FXML
    public DatePicker dateOfBirth;
    @FXML
    public TextField phoneNumber;
    @FXML
    public TextField yearsOfWorkExperience;
    @FXML
    public TextField workStartTime;
    @FXML
    public TextField workEndTime;
    @FXML
    public TextField departmentId;

    private static final Logger logger = LoggerFactory.getLogger(DoctorInsertController.class);


    public void initialize() {
        dateOfBirth.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
    @FXML
    public void insertNewDoctor() {
        List<String> messages = new ArrayList<>();

        if (name.getText().isBlank()) {
            messages.add("Name field is empty");
        }
        if (surname.getText().isBlank()) {
            messages.add("Surname field is empty");
        }
        if (title.getText().isBlank()) {
            messages.add("Title field is empty");
        }
        if (licenseNumber.getText().isBlank()) {
            messages.add("License number field is empty");
        }
        if (dateOfBirth.getValue() == null) {
            messages.add("Birth date is not choosen");
        }
        if (phoneNumber.getText().isBlank()) {
            messages.add("Phone number field is empty");
        }
        if (yearsOfWorkExperience.getText().isBlank()) {
            messages.add("Years of work experience field is empty");
        }
        if (workStartTime.getText().isBlank()) {
            messages.add("Work start time field is empty");
        }
        if (workEndTime.getText().isBlank()) {
            messages.add("Work end time field is empty");
        }
        if (departmentId.getText().isBlank()) {
            messages.add("Department id field is empty");
        }

        if(messages.size() == 0) {
            Doctor doctor = new Doctor.DoctorBuilder((long) - 1).withName(name.getText()).withSurname(surname.getText())
                    .withTitle(title.getText()).withLicenseNumber(Long.valueOf(licenseNumber.getText())).
                    withDateOfBirth(LocalDate.parse(dateOfBirth.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .withPhoneNumber(phoneNumber.getText()).withYearsOfExperience(Integer.valueOf(yearsOfWorkExperience.getText()))
                    .withWorkStartTime(LocalTime.parse(workStartTime.getText(), DateTimeFormatter.ofPattern("HH:mm")))
                    .withWorkEndTime(LocalTime.parse(workEndTime.getText(), DateTimeFormatter.ofPattern("HH:mm")))
                    .build();


            try {
                HealthcareApplication.getDatabaseSource().insertDoctor(doctor, Long.valueOf(departmentId.getText()));

                name.clear();
                surname.clear();
                title.clear();
                licenseNumber.clear();
                dateOfBirth.setValue(null);
                phoneNumber.clear();
                yearsOfWorkExperience.clear();
                workStartTime.clear();
                workEndTime.clear();
                departmentId.clear();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Entry successful");
                alert.setTitle("Success");
                alert.show();
            } catch(DatabaseQueryException ex) {
                String msg = "Error occurred on 'insert' screen while inserting new doctor object to dabase";
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
