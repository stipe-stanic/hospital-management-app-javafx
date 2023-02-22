package eng.java.project.entity.hospital.util;

import eng.java.project.entity.hospital.core.Patient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public interface PatientStatistics {
    default BigDecimal getAverageOfPatients(List<Patient> patients) {
        int sumOfAges = patients.stream()
                .mapToInt(patient -> LocalDate.now().getYear() - patient.getDateOfBirth().getYear())
                .sum();
        return BigDecimal.valueOf(sumOfAges).divide(BigDecimal.valueOf(patients.size()), 2, RoundingMode.HALF_UP);
    }
}
