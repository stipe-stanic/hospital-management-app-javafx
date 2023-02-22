package eng.java.project.entity.hospital;

import eng.java.project.entity.HealthInstitution;
import eng.java.project.entity.hospital.core.Department;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.entity.hospital.util.PatientStatistics;

import java.util.List;

public class PrivateHospital extends HealthInstitution implements PatientStatistics {
    public PrivateHospital(Long id, String name, List<Doctor> doctorList, List<Patient> patientList,
                          List<Department> departmentList, List<MedicalDevice> applianceList) {
        super(id, name, doctorList, patientList, departmentList, applianceList);
    }
}
