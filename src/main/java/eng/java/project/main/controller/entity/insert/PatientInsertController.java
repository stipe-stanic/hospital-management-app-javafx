package eng.java.project.main.controller.entity.insert;

import eng.java.project.entity.hospital.core.Doctor;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PatientInsertController {
    @FXML
    public TextField name;
    @FXML
    public TextField surname;
    @FXML
    public TextField phoneNumber;
    @FXML
    public TextField doctorId;
    @FXML
    public TextField departmentId;
    @FXML
    public DatePicker dateOfBirth;

    private static final Logger logger = LoggerFactory.getLogger(PatientInsertController.class);

    public void initialize() {
        dateOfBirth.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @FXML
    public void insertNewPatient() {
        List<String> messages = new ArrayList<>();

        if (name.getText().isBlank()) {
            messages.add("Name field is empty");
        }
        if (surname.getText().isBlank()) {
            messages.add("Surname field is empty");
        }
        if (dateOfBirth.getValue() == null) {
            messages.add("Birth date is not choosen");
        }
        if (phoneNumber.getText().isBlank()) {
            messages.add("Phone number field is empty");
        }
        if (doctorId.getText().isBlank()) {
            messages.add("Doctor id field is empty");
        }
        if (departmentId.getText().isBlank()) {
            messages.add("Department id field is empty");
        }

        if(messages.size() == 0) {
            Patient patient = new Patient.PatientBuilder((long) -1).withName(name.getText()).withSurname(surname.getText())
                    .withDateOfBirth(LocalDate.parse(dateOfBirth.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .withPhoneNumber(phoneNumber.getText())
                    .build();

            try {
                HealthcareApplication.getDatabaseSource().insertPatient(patient, Long.valueOf(doctorId.getText()),
                        Long.valueOf(departmentId.getText()));

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Entry successful");
                alert.setTitle("Success");
                alert.show();

                name.clear();
                surname.clear();
                dateOfBirth.setValue(null);
                phoneNumber.clear();
                doctorId.clear();
                departmentId.clear();
            } catch(DatabaseQueryException ex) {
                String msg = "Error occurred on 'insert' screen while inserting new patient object to database";
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
