package eng.java.project.entity.history;

import eng.java.project.entity.hospital.util.Treatment;

import java.io.Serializable;

public record TreatmentHistory(Treatment treatment) implements Serializable {
}
