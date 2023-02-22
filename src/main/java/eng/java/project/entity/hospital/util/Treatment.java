package eng.java.project.entity.hospital.util;

import eng.java.project.entity.Entity;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.entity.hospital.core.Patient;

import java.time.LocalDate;
import java.time.LocalTime;

public class Treatment extends Entity {
    private Doctor doctor;
    private Patient patient;
    private LocalDate date;
    private LocalTime time;
    private MedicalDevice appliance;

    public static class TreatmentBuilder{
        private final Long id;
        private Doctor doctor;
        private Patient patient;
        private final LocalDate date;
        private final LocalTime time;
        private MedicalDevice appliance;

        public TreatmentBuilder(Long id, LocalDate date, LocalTime time) {
            this.id = id;
            this.date = date;
            this.time = time;
        }

        public TreatmentBuilder withDoctor(Doctor doctor) {
            this.doctor = doctor;
            return this;
        }

        public TreatmentBuilder withPatient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public TreatmentBuilder withAppliance(MedicalDevice appliance) {
            this.appliance = appliance;
            return this;
        }

        public Treatment build() {
            return new Treatment(this);
        }
    }

    private Treatment(TreatmentBuilder builder) {
        super(builder.id);
        this.doctor = builder.doctor;
        this.patient = builder.patient;
        this.date = builder.date;
        this.time = builder.time;
        this.appliance = builder.appliance;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public MedicalDevice getAppliance() {
        return appliance;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setAppliance(MedicalDevice appliance) {
        this.appliance = appliance;
    }
}
