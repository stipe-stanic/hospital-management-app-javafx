package eng.java.project.entity.history;

import eng.java.project.entity.hospital.util.Symptom;

import java.io.Serializable;

public record SymptomHistory(Symptom symptom) implements Serializable {
}
