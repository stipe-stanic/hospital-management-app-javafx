package eng.java.project.thread;

import eng.java.project.database.DatabaseSource;
import eng.java.project.database.DatabaseUtil;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.main.controller.entity.view.DepartmentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class YoungestPatientThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(YoungestPatientThread.class);
    private DatabaseSource databaseUtil = new DatabaseUtil();
    private static Optional<Patient> youngestPatient;
    private static volatile boolean running = true;

    public YoungestPatientThread() {}

    public void run() {
        while(running) {
            try {
                List<Patient> patients = databaseUtil.queryAllPatients();
                 youngestPatient = patients.stream().min(Comparator.comparing(p -> {
                    LocalDate now = LocalDate.now();
                    LocalDate birthDate = p.getDateOfBirth();
                    return Period.between(birthDate, now).getYears();
                }));

                Thread.sleep(300);
            } catch (DatabaseQueryException ex) {
                String msg = "Error occurred while fetching all patients in thread run method";
                logger.error(msg, ex);
                throw new RuntimeException(msg, ex);
            } catch (InterruptedException ex) {
                String msg = "Thread has been interrupted in 'YoungestPatientClass'";
                logger.info(msg, ex);
            }
        }
    }

    public static Optional<Patient> getYoungestPatient() {
        return youngestPatient;
    }

    public static void setRunning(Boolean value) {
        running = value;
    }
}
