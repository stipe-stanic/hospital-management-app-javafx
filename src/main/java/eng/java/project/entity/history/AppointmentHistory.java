package eng.java.project.entity.history;

import eng.java.project.entity.hospital.util.Appointment;

import java.io.Serializable;

public record AppointmentHistory(Appointment appointment) implements Serializable {}
