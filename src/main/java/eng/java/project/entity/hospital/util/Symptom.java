package eng.java.project.entity.hospital.util;

import eng.java.project.entity.Entity;

import java.time.LocalDate;
import java.util.List;

public class Symptom extends Entity {
    private String description;
    private SymptomRating severity;
    private LocalDate dateOfFirstAppearance;
    private LocalDate dateOfDisapearance;
    private List<String> relatedFactors;

    public static class SymptomBuilder {
        private final Long id;
        private final String description;
        private final SymptomRating severity;
        private LocalDate dateOfFirstAppearance;
        private LocalDate dateOfDisapearance;
        private List<String> relatedFactors;

        public SymptomBuilder(Long id, String description, SymptomRating severity) {
            this.description = description;
            this.severity = severity;
            this.id = id;
        }

        public SymptomBuilder withDateOfFirstAppearance(LocalDate dateOfFirstAppearance) {
            this.dateOfFirstAppearance = dateOfFirstAppearance;
            return this;
        }

        public SymptomBuilder withDateOfDisapearance(LocalDate dateOfDisapearance) {
            this.dateOfDisapearance = dateOfDisapearance;
            return this;
        }

        public SymptomBuilder withRelatedFactors(List<String> relatedFactors) {
            this.relatedFactors = relatedFactors;
            return this;
        }

        public Symptom build() {
            return new Symptom(this);
        }
    }

    private Symptom(SymptomBuilder builder) {
        super(builder.id);
        this.description = builder.description;
        this.severity = builder.severity;
        this.dateOfFirstAppearance = builder.dateOfFirstAppearance;
        this.dateOfDisapearance = builder.dateOfDisapearance;
        this.relatedFactors = builder.relatedFactors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SymptomRating getSeverity() {
        return severity;
    }

    public void setSeverity(SymptomRating severity) {
        this.severity = severity;
    }

    public LocalDate getDateOfFirstAppearance() {
        return dateOfFirstAppearance;
    }

    public void setDateOfFirstAppearance(LocalDate dateOfFirstAppearance) {
        this.dateOfFirstAppearance = dateOfFirstAppearance;
    }

    public LocalDate getDateOfDisapearance() {
        return dateOfDisapearance;
    }

    public void setDateOfDisapearance(LocalDate dateOfDisapearance) {
        this.dateOfDisapearance = dateOfDisapearance;
    }

    public List<String> getRelatedFactors() {
        return relatedFactors;
    }

    public void setRelatedFactors(List<String> relatedFactors) {
        this.relatedFactors = relatedFactors;
    }
}
