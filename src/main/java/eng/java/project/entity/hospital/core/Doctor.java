package eng.java.project.entity.hospital.core;

import eng.java.project.entity.hospital.util.Appointment;
import eng.java.project.entity.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Doctor extends Entity implements CoreObject {
    private String name;
    private String surname;
    private String title;
    private final Long licenseNumber;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private Integer yearsOfExperience;
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private List<Patient> patientList;
    private List<Appointment> appointmentsList;

    public static class DoctorBuilder{
        private final Long id;
        private String name;
        private String surname;
        private String title;
        private Long licenseNumber;
        private LocalDate dateOfBirth;
        private String phoneNumber;
        private Integer yearsOfExperience;
        private LocalTime workStartTime;
        private LocalTime workEndTime;
        private List<Patient> patientList;
        private List<Appointment> appointmentsList;

        public DoctorBuilder(Long id) {
            this.id = id;
        }

        public DoctorBuilder withName(String name){
            this.name = name;
            return this;
        }

        public DoctorBuilder withSurname(String surname){
            this.surname = surname;
            return this;
        }

        public DoctorBuilder withTitle(String title){
            this.title = title;
            return this;
        }

        public DoctorBuilder withLicenseNumber(Long licenseNumber){
            this.licenseNumber = licenseNumber;
            return this;
        }

        public DoctorBuilder withDateOfBirth(LocalDate dateOfBirth){
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public DoctorBuilder withPhoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }

        public DoctorBuilder withYearsOfExperience(Integer yearsOfExperience){
            this.yearsOfExperience = yearsOfExperience;
            return this;
        }

        public DoctorBuilder withWorkStartTime(LocalTime workStartTime){
            this.workStartTime = workStartTime;
            return this;
        }

        public DoctorBuilder withWorkEndTime(LocalTime workEndTime){
            this.workEndTime = workEndTime;
            return this;
        }

        public DoctorBuilder withPatientList(List<Patient> patientList){
            this.patientList = patientList;
            return this;
        }

        public DoctorBuilder withAppointmentList(List<Appointment> appointmentsList){
            this.appointmentsList = appointmentsList;
            return this;
        }

        public Doctor build() {
            return new Doctor(this);
        }
    }

    private Doctor(DoctorBuilder builder) {
        super(builder.id);
        this.name = builder.name;
        this.surname = builder.surname;
        this.title = builder.title;
        this.licenseNumber = builder.licenseNumber;
        this.dateOfBirth = builder.dateOfBirth;
        this.phoneNumber = builder.phoneNumber;
        this.yearsOfExperience = builder.yearsOfExperience;
        this.workStartTime = builder.workStartTime;
        this.workEndTime = builder.workEndTime;
        this.patientList = builder.patientList;
        this.appointmentsList = builder.appointmentsList;
    }

    public Doctor(Doctor other) {
        super(other.getId());
        this.name = other.name;
        this.surname = other.surname;
        this.title = other.title;
        this.licenseNumber = other.licenseNumber;
        this.dateOfBirth = other.dateOfBirth;
        this.phoneNumber = other.phoneNumber;
        this.yearsOfExperience = other.yearsOfExperience;
        this.workStartTime = other.workStartTime;
        this.workEndTime = other.workEndTime;
        this.patientList = other.patientList;
        this.appointmentsList = other.appointmentsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getLicenseNumber() {
        return licenseNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public LocalTime getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(LocalTime workStartTime) {
        this.workStartTime = workStartTime;
    }

    public LocalTime getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(LocalTime workEndTime) {
        this.workEndTime = workEndTime;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public List<Appointment> getAppointmentsList() {
        return appointmentsList;
    }

    public void setAppointmentsList(List<Appointment> appointmentsList) {
        this.appointmentsList = appointmentsList;
    }
}
