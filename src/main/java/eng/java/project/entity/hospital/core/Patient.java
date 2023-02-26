package eng.java.project.entity.hospital.core;

import eng.java.project.entity.*;
import eng.java.project.entity.history.AppointmentHistory;
import eng.java.project.entity.history.History;
import eng.java.project.entity.history.SymptomHistory;
import eng.java.project.entity.history.TreatmentHistory;
import eng.java.project.entity.hospital.util.Appointment;
import eng.java.project.entity.hospital.util.InsuranceProvider;
import eng.java.project.entity.hospital.util.Symptom;
import eng.java.project.entity.hospital.util.Treatment;

import java.time.LocalDate;
import java.util.List;
import java.util.function.DoubleConsumer;

public class Patient extends Entity implements CoreObject {
   private String name;
   private String surname;
   private LocalDate dateOfBirth;
   private String phoneNumber;
   private List<Appointment> appointmentList;
   private History<AppointmentHistory> appointmentHistory;
   private List<Treatment> treatmentsList;
   private History<TreatmentHistory> treatmentsHistory;
   private List<Symptom> symptomsList;
   private History<SymptomHistory> symptomsHistory;
   private InsuranceProvider insurance;

   public static class PatientBuilder{
      private final Long id;
      private String name;
      private String surname;
      private LocalDate dateOfBirth;
      private String phoneNumber;
      private List<Appointment> appointmentList;
      private History<AppointmentHistory> appointmentHistory;
      private List<Treatment> treatmentsList;
      private History<TreatmentHistory> treatmentsHistory;
      private List<Symptom> symptomsList;
      private History<SymptomHistory> symptomsHistory;
      private InsuranceProvider insurance;

      public PatientBuilder(Long id) {
         this.id = id;
      }

      public PatientBuilder withName(String name){
         this.name = name;
         return this;
      }

      public PatientBuilder withSurname(String surname){
         this.surname = surname;
         return this;
      }

      public PatientBuilder withDateOfBirth(LocalDate dateOfBirth){
         this.dateOfBirth = dateOfBirth;
         return this;
      }

      public PatientBuilder withPhoneNumber(String phoneNumber){
         this.phoneNumber = phoneNumber;
         return this;
      }

//      // public PatientBuilder withDoctor(Doctor doctor) {
//         this.doctor = doctor;
//         return this;
//      }

      public PatientBuilder withAppointmentList(List<Appointment> appointmentList){
         this.appointmentList = appointmentList;
         return this;
      }

      public PatientBuilder withAppointmentHistory(History<AppointmentHistory> appointmentHistory){
         this.appointmentHistory = appointmentHistory;
         return this;
      }

      public PatientBuilder withTreatmentList(List<Treatment> treatmentsList){
         this.treatmentsList = treatmentsList;
         return this;
      }

      public PatientBuilder withTreatmentHistory(History<TreatmentHistory> treatmentsHistory){
         this.treatmentsHistory = treatmentsHistory;
         return this;
      }

      public PatientBuilder withSymptomList(List<Symptom> symptomsList){
         this.symptomsList = symptomsList;
         return this;
      }

      public PatientBuilder withSymptomHistory(History<SymptomHistory> symptomsHistory){
         this.symptomsHistory = symptomsHistory;
         return this;
      }

      public PatientBuilder withInsurance(InsuranceProvider insurance){
         this.insurance = insurance;
         return this;
      }

      public Patient build() {
         return new Patient(this);
      }
   }

   private Patient(PatientBuilder builder) {
      super(builder.id);
      this.name = builder.name;
      this.surname = builder.surname;
      this.dateOfBirth = builder.dateOfBirth;
      this.phoneNumber = builder.phoneNumber;
      this.appointmentList = builder.appointmentList;
      this.appointmentHistory = builder.appointmentHistory;
      this.treatmentsList = builder.treatmentsList;
      this.treatmentsHistory = builder.treatmentsHistory;
      this.symptomsList = builder.symptomsList;
      this.symptomsHistory = builder.symptomsHistory;
      this.insurance = builder.insurance;
   }

   public Patient(Patient other) {
      super(other.getId());
      this.name = other.name;
      this.surname = other.surname;
      this.dateOfBirth = other.dateOfBirth;
      this.phoneNumber = other.phoneNumber;
      // this.doctor = builder.doctor;
      this.appointmentList = other.appointmentList;
      this.appointmentHistory = other.appointmentHistory;
      this.treatmentsList = other.treatmentsList;
      this.treatmentsHistory = other.treatmentsHistory;
      this.symptomsList = other.symptomsList;
      this.symptomsHistory = other.symptomsHistory;
      this.insurance = other.insurance;
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

   public List<Appointment> getAppointmentList() {
      return appointmentList;
   }

   public void setAppointmentList(List<Appointment> appointmentList) {
      this.appointmentList = appointmentList;
   }

   public History<AppointmentHistory> getAppointmentHistory() {
      return appointmentHistory;
   }

   public void setAppointmentHistory(History<AppointmentHistory> appointmentHistory) {
      this.appointmentHistory = appointmentHistory;
   }

   public List<Treatment> getTreatmentsList() {
      return treatmentsList;
   }

   public void setTreatmentsList(List<Treatment> treatmentsList) {
      this.treatmentsList = treatmentsList;
   }

   public History<TreatmentHistory> getTreatmentsHistory() {
      return treatmentsHistory;
   }

   public void setTreatmentsHistory(History<TreatmentHistory> treatmentsHistory) {
      this.treatmentsHistory = treatmentsHistory;
   }

   public List<Symptom> getSymptomsList() {
      return symptomsList;
   }

   public void setSymptomsList(List<Symptom> symptomsList) {
      this.symptomsList = symptomsList;
   }

   public History<SymptomHistory> getSymptomsHistory() {
      return symptomsHistory;
   }

   public void setSymptomsHistory(History<SymptomHistory> symptomsHistory) {
      this.symptomsHistory = symptomsHistory;
   }

   public InsuranceProvider getInsurance() {
      return insurance;
   }

   public void setInsurance(InsuranceProvider insurance) {
      this.insurance = insurance;
   }
}
