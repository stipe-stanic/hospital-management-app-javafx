package eng.java.project.thread;

import eng.java.project.database.DatabaseSource;
import eng.java.project.database.DatabaseUtil;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.exception.DatabaseQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MostExpereincedDoctor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(YoungestPatientThread.class);
    private DatabaseSource databaseUtil = new DatabaseUtil();
    private static Optional<Doctor> mostExperiencedDoctor;
    private static volatile boolean running = true;

    public MostExpereincedDoctor() {}

    public void run() {
        while(running) {
            try {
                List<Doctor> patients = databaseUtil.queryAllDoctors();
                mostExperiencedDoctor = patients.stream().max(Comparator.comparing(Doctor::getYearsOfExperience));
                Thread.sleep(300);
            } catch (DatabaseQueryException ex) {
                String msg = "Error occurred while fetching all doctors in thread run method";
                logger.error(msg, ex);
                throw new RuntimeException(msg, ex);
            } catch (InterruptedException ex) {
                String msg = "Thread has been interrupted in 'MostExperiencedDoctor'";
                logger.info(msg, ex);
            }
        }
    }

    public static Optional<Doctor> getMostExperiencedDoctor() {
        return mostExperiencedDoctor;
    }

    public static void setRunning(Boolean value) {
        running = value;
    }
}
