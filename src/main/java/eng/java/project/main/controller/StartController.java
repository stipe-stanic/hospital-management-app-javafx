package eng.java.project.main.controller;

import eng.java.project.database.DatabaseSource;
import eng.java.project.database.DatabaseUtil;
import eng.java.project.entity.HealthInstitution;
import eng.java.project.entity.hospital.PublicHospital;
import eng.java.project.entity.hospital.core.Department;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.main.HealthcareApplication;
import eng.java.project.thread.MostExpereincedDoctor;
import eng.java.project.thread.YoungestPatientThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StartController {
    private static final String SERIALIZATION_FILE_NAME = "files\\hospital_object.dat";
    private static final Logger logger = LoggerFactory.getLogger(StartController.class);
    private static final DatabaseSource databaseUtil = new DatabaseUtil();
    public Label mostExperiencedDoctor;
    private Stage stage;
    public Label youngestPatient;


    public void initialize() {
        Thread thread = new Thread(new YoungestPatientThread());
        thread.start();
        Timeline showYoungestPatient = new Timeline(
                new KeyFrame(Duration.seconds(1.25), event -> Platform.runLater(() -> {
                    Optional<Patient> patient = YoungestPatientThread.getYoungestPatient();
                    patient.ifPresent(value -> youngestPatient.setText("Youngest patient is: " + patient.get().getName() +
                            " " + patient.get().getSurname() + " (" + (LocalDate.now().getYear() - value.getDateOfBirth().getYear()) +
                            ")"));
                })));
        showYoungestPatient.setCycleCount(Timeline.INDEFINITE);
        showYoungestPatient.play();



        Thread threadDoctor = new Thread(new MostExpereincedDoctor());
        threadDoctor.start();
        Timeline showMostExperiencedDoctor = new Timeline(
                new KeyFrame(Duration.seconds(1.25), event -> Platform.runLater(() -> {
                    Optional<Doctor> doctor = MostExpereincedDoctor.getMostExperiencedDoctor();
                    doctor.ifPresent(value -> mostExperiencedDoctor.setText("Doctor with the most exprience is: " + doctor.get().getTitle() + " " + doctor.get().getName() +
                            " " + doctor.get().getSurname() + " (" + doctor.get().getYearsOfExperience() +
                            ")"));
                })));
        showMostExperiencedDoctor.setCycleCount(Timeline.INDEFINITE);
        showMostExperiencedDoctor.play();

        Stage stage = HealthcareApplication.getAppStage();
        stage.setOnCloseRequest(e -> {
            YoungestPatientThread.setRunning(false);
            MostExpereincedDoctor.setRunning(false);
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
