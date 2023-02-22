package eng.java.project.entity.hospital.util;


import eng.java.project.entity.Entity;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment extends Entity {
    private Doctor doctor;
    private Patient patient;
    private LocalDate date;
    private LocalTime time;
    private String doctorNote;

    public static class AppointmentBuilder {
        private final Long id;
        private Doctor doctor;
        private Patient patient;
        private LocalDate date;
        private LocalTime time;
        private String doctorNote;

        public AppointmentBuilder(Long id) {
            this.id = id;
        }

        public AppointmentBuilder withDoctor(Doctor doctor) {
            this.doctor = doctor;
            return this;
        }

        public AppointmentBuilder withPatient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public AppointmentBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public AppointmentBuilder withTime(LocalTime time) {
            this.time = time;
            return this;
        }

        public AppointmentBuilder withDoctorNote(String doctorNote) {
            this.doctorNote = doctorNote;
            return this;
        }

        public Appointment build() {
            return new Appointment(this);
        }
    }

    private Appointment(AppointmentBuilder builder) {
        super(builder.id);
        this.doctor = builder.doctor;
        this.patient = builder.patient;
        this.date = builder.date;
        this.time = builder.time;
        this.doctorNote = builder.doctorNote;
    }

    public String getDoctorNote() {
        return doctorNote;
    }

    public void setDoctorNote(String doctorNote) {
        this.doctorNote = doctorNote;
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
}
