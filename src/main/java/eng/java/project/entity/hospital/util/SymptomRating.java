package eng.java.project.entity.hospital.util;

public enum SymptomRating {
    MILD(1, "Patient has expressed mild symptoms"),
    MODERATE(2, "Patient has expressed moderate symptoms"),
    SEVERE(3, "Patient has expressed severe symptoms");

    private final Integer status;
    private final String description;

    SymptomRating(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
