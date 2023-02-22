package eng.java.project.entity;

import eng.java.project.entity.hospital.core.Department;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.entity.hospital.core.Patient;

import java.util.List;

public abstract class HealthInstitution extends Entity {
    private String name;
    private List<Doctor> doctorList;
    private List<Patient> patientList;
    private List<Department> departmentList;
    private List<MedicalDevice> applianceList;

    public HealthInstitution(Long id, String name, List<Doctor> doctorList, List<Patient> patientList, List<Department> departmentList, List<MedicalDevice> applianceList) {
        super(id);
        this.name = name;
        this.doctorList = doctorList;
        this.patientList = patientList;
        this.departmentList = departmentList;
        this.applianceList = applianceList;
    }

    public String getName() {
        return name;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public List<MedicalDevice> getApplianceList() {
        return applianceList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public void setApplianceList(List<MedicalDevice> applianceList) {
        this.applianceList = applianceList;
    }
}
