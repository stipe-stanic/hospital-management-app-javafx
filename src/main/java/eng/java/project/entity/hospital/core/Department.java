package eng.java.project.entity.hospital.core;

import eng.java.project.entity.Entity;

import java.util.List;

public class Department extends Entity implements CoreObject {
    private String name;
    private List<Doctor> doctorList;
    private List<Patient> patientList;
    private List<MedicalDevice> appliancesList;

    public static class DepartmentBuilder {
        private final long id;
        private String name;
        private List<Doctor> doctorList;
        private List<Patient> patientList;
        private List<MedicalDevice> appliancesList;

        public DepartmentBuilder(Long id) {
            this.id = id;
        }

        public DepartmentBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DepartmentBuilder withDoctors(List<Doctor> doctorList) {
            this.doctorList = doctorList;
            return this;
        }

        public DepartmentBuilder withPatients(List<Patient> patientList) {
            this.patientList = patientList;
            return this;
        }

        public DepartmentBuilder withAppliances(List<MedicalDevice> appliancesList) {
            this.appliancesList = appliancesList;
            return this;
        }

        public Department build() {
            return new Department(this);
        }
    }

    private Department(DepartmentBuilder builder) {
        super(builder.id);
        this.name = builder.name;
        this.doctorList = builder.doctorList;
        this.patientList = builder.patientList;
        this.appliancesList = builder.appliancesList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public List<MedicalDevice> getAppliancesList() {
        return appliancesList;
    }

    public void setAppliancesList(List<MedicalDevice> appliancesList) {
        this.appliancesList = appliancesList;
    }

    @Override
    public String toString() {
        return name;
    }
}
