package eng.java.project.main.controller.entity.view;

import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.main.HealthcareApplication;
import eng.java.project.main.LocalDateConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorController {
    public TextField doctorName;
    public TextField doctorSurname;
    public TextField patientName;
    public DatePicker dateOfBirth;
    public DatePicker appointmentDate;
    public TableView<Doctor> doctorTableView;
    public TableColumn<Doctor, String> nameColumn;
    public TableColumn<Doctor, String> surnameColumn;
    public TableColumn<Doctor, String> titleColumn;
    public TableColumn<Doctor, String> licenseNumberColumn;
    public TableColumn<Doctor, String> dateOfBirthColumn;
    public TableColumn<Doctor, String> phoneNumberColumn;
    public TableColumn<Doctor, String> experienceColumn;
    public TableColumn<Doctor, String> workStartTimeColumn;
    public TableColumn<Doctor, String> workEndTimeColumn;
    public TableColumn<Doctor, String> appointmentsColumn;
    public TableColumn<Doctor, String> patientsColum;

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);
    private List<Doctor> allDoctors;

    public void initialize() {
        dateOfBirth.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("d.M.yyyy")));
        appointmentDate.setConverter(new LocalDateConverter(DateTimeFormatter.ofPattern("d.M.yyyy")));

        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        surnameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurname()));
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        licenseNumberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLicenseNumber().toString()));
        dateOfBirthColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateOfBirth().
                format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        phoneNumberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));
        experienceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getYearsOfExperience().toString()));
        workStartTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWorkStartTime().
                format(DateTimeFormatter.ofPattern("HH:mm"))));
        workEndTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWorkEndTime().
                format(DateTimeFormatter.ofPattern("HH:mm"))));
        patientsColum.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPatientList().stream()
                .map(p -> p.getName() + " " + p.getSurname()).collect(Collectors.joining("\n"))));
        appointmentsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentsList().stream()
                .filter(a -> a.getDate().isAfter(LocalDate.now()))
                .map(a -> a.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " - " + a.getTime() + "h")
                .collect(Collectors.joining("\n"))));

        try {
            allDoctors = HealthcareApplication.getDatabaseSource().queryAllDoctors();
        } catch (DatabaseQueryException ex) {
            String msg = "Error ocurred while fetching doctors to display on search screen";
            logger.error(msg, ex);

            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.setTitle("Error");
            alert.show();
        }

        doctorTableView.setItems(FXCollections.observableList(allDoctors));

    }
    public void filterDoctors() {
        List<Doctor> filtered = allDoctors.stream()
                .filter(d -> d.getName().toLowerCase().contains(doctorName.getText().toLowerCase()))
                .filter(d -> d.getSurname().toLowerCase().contains(doctorSurname.getText().toLowerCase()))
                .filter(d -> dateOfBirth.getValue() == null || d.getDateOfBirth().equals(dateOfBirth.getValue()))
                .filter(d -> d.getPatientList().stream()
                        .anyMatch(p -> (p.getName() + " " + p.getSurname()).toLowerCase().contains(patientName.getText().toLowerCase())))
                .filter(d -> appointmentDate.getValue() == null || d.getAppointmentsList().stream()
                        .anyMatch(a -> a.getDate().equals(appointmentDate.getValue()))).toList();

        doctorTableView.setItems(FXCollections.observableList(filtered));
    }
}
