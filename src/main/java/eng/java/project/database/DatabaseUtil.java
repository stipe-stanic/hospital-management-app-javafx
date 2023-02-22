package eng.java.project.database;

import eng.java.project.entity.history.AppointmentHistory;
import eng.java.project.entity.history.History;
import eng.java.project.entity.history.SymptomHistory;
import eng.java.project.entity.history.TreatmentHistory;
import eng.java.project.entity.hospital.core.Department;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.entity.hospital.util.*;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.exception.ParameterNotFound;
import eng.java.project.exception.UnknownEnumerationValue;
import org.h2.store.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public final class DatabaseUtil implements DatabaseSource {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    private static Connection connectToDatabase() throws SQLException, IOException {
        Properties configuration = new Properties();
        configuration.load(new FileReader("files/database.properties"));

        String databaseURL = configuration.getProperty("url");
        String databaseUser = configuration.getProperty("user");
        String databasePassword = configuration.getProperty("password");

        return DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
    }

    private static MedicalDevice queryMedicalDeviceById(Long idAppliance, Connection connection) throws DatabaseQueryException {
        MedicalDevice device = null;

        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM MEDICAL_DEVICE WHERE 1=1");
            sqlQuery.append(" AND ID = ").append(idAppliance);

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = preparedStatement.executeQuery();

            if(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String manufacturerName = result.getString("manufacturer_name");
                Integer modelNumber = result.getInt("model_number");
                Long serialNumber = result.getLong("serial_number");
                String type = result.getString("type");
                LocalDate dateOfPurchase = result.getTimestamp("date_of_purchase").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                LocalDate expirationDate = result.getTimestamp("expiration_date").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();

                device = new MedicalDevice(id, name, manufacturerName, modelNumber, serialNumber, type, dateOfPurchase,
                        expirationDate);
            }
        } catch (SQLException ex) {
            String message = "Error occurred while selecting device used for treatment";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return device;
    }


    private static Patient queryPatientById(Long idPatient, Connection connection) throws DatabaseQueryException {
        Patient patient = null;

        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM PATIENT WHERE 1=1");
            sqlQuery.append(" AND ID = ").append(idPatient);

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = statement.executeQuery();

            if(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                LocalDate dateOfBirth = result.getTimestamp("date_of_birth").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                String phoneNumber = result.getString("phone_number");
                Long doctor_id = result.getLong("doctor_id");

                List<Appointment> appointmentList = queryAppointmentsOfPatient(id, connection);
                History<AppointmentHistory> appointmentHistory = filterHistoryAppointments(appointmentList);

                List<Treatment> treatmentList = queryTreatmentsOfPatient(id, connection);
                History<TreatmentHistory> treatmentHistory = filterHistoryTreatments(treatmentList);

                List<Symptom> symptomList = querySymptomsOfPatient(id, connection);
                History<SymptomHistory> symptomHistory = filterHistorySymptoms(symptomList);

                patient = new Patient.PatientBuilder(id).withName(name).withSurname(surname).
                        withDateOfBirth(dateOfBirth).withPhoneNumber(phoneNumber).
                        withAppointmentHistory(appointmentHistory).withTreatmentList(treatmentList).
                        withTreatmentHistory(treatmentHistory).withSymptomList(symptomList).
                        withSymptomHistory(symptomHistory).build();
            }

        } catch(SQLException ex) {
            String message = "Error occurred while selecting patient by id";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return patient;
    }
    private static Doctor queryDoctorById(Long idDoctor, Connection connection) throws DatabaseQueryException {
        Doctor doctor = null;

        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM DOCTOR WHERE 1=1");
            sqlQuery.append(" AND ID = ").append(idDoctor);

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = preparedStatement.executeQuery();

            if(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String title = result.getString("title");
                Long licenseNumber = result.getLong("license_number");
                LocalDate dateOfBirth = result.getTimestamp("date_of_birth").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                String phoneNumber = result.getString("phone_number");
                Integer yearsOfExperience = result.getInt("years_of_experience");
                LocalTime workStartTime = result.getTimestamp("work_start_time").toLocalDateTime().toLocalTime();
                LocalTime workEndTime = result.getTimestamp("work_end_time").toLocalDateTime().toLocalTime();

                List<Patient> patientList = queryPatientsOfDoctor(id, connection);
                List<Appointment> appointmentList = queryAppointmentsOfDoctor(id, connection);

                doctor = new Doctor.DoctorBuilder(id).withName(name).withSurname(surname).withTitle(title).
                        withLicenseNumber(licenseNumber).withDateOfBirth(dateOfBirth).withPhoneNumber(phoneNumber).
                        withYearsOfExperience(yearsOfExperience).withWorkStartTime(workStartTime).
                        withWorkEndTime(workEndTime).withPatientList(patientList).withAppointmentList(appointmentList).
                        build();
            }
        } catch (SQLException ex) {
            String message = "Error occurred while selecting doctor by id";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return doctor;
    }


    private static List<Patient> queryPatientsOfDoctor(Long idDoctor, Connection connection) throws DatabaseQueryException {
        List<Patient> patientList = new ArrayList<>();
        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM PATIENT WHERE 1=1");
            sqlQuery.append(" AND DOCTOR_ID = ").append(idDoctor);

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                LocalDate dateOfBirth = result.getTimestamp("date_of_birth").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                String phoneNumber = result.getString("phone_number");
                Long doctor_id = result.getLong("doctor_id");
                Long department_id = result.getLong("department_id");

                List<Appointment> appointmentList = queryAppointmentsOfPatient(id, connection);
                History<AppointmentHistory> appointmentHistory = filterHistoryAppointments(appointmentList);

                List<Treatment> treatmentList = queryTreatmentsOfPatient(id, connection);
                History<TreatmentHistory> treatmentHistory = filterHistoryTreatments(treatmentList);

                List<Symptom> symptomList = querySymptomsOfPatient(id, connection);
                History<SymptomHistory> symptomHistory = filterHistorySymptoms(symptomList);

                InsuranceProvider insuranceProvider = queryInsuranceProviderOfPatient(id, connection);

                Patient patient = new Patient.PatientBuilder(id).withName(name).withSurname(surname).
                        withDateOfBirth(dateOfBirth).withPhoneNumber(phoneNumber).withAppointmentList(appointmentList).
                        withAppointmentHistory(appointmentHistory).withTreatmentList(treatmentList).
                        withTreatmentHistory(treatmentHistory).withSymptomList(symptomList).
                        withSymptomHistory(symptomHistory).withInsurance(insuranceProvider).build();

                patientList.add(patient);
            }
        } catch (SQLException ex) {
            String message = "Error occurred while selecting patients of the doctor";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
        return patientList;
    }
    private static List<Appointment> queryAppointmentsOfDoctor(Long idDoctor, Connection connection) throws DatabaseQueryException {
        List<Appointment> appointmentList = new ArrayList<>();

        try  {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM APPOINTMENT WHERE 1=1");
            sqlQuery.append(" AND DOCTOR_ID = ").append(idDoctor);

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                Long doctor_id = result.getLong("doctor_id");
                Long patient_id = result.getLong("patient_id");
                LocalDate date = result.getTimestamp("date").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                LocalTime time = result.getTimestamp("time").toLocalDateTime().toLocalTime();
                String doctorNote = result.getString("doctor_note");

                Patient patient = queryPatientById(patient_id, connection);

                Appointment appointment = new Appointment.AppointmentBuilder(id).withPatient(patient)
                        .withDate(date).withTime(time).build();

                appointmentList.add(appointment);
            }
        } catch (SQLException ex) {
            String message = "Error occurred while selecting appointments of the doctor";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return appointmentList;
    }


    private static InsuranceProvider queryInsuranceProviderOfPatient(Long idPatient, Connection connection) throws DatabaseQueryException {
        InsuranceProvider insurance = null;

        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM INSURANCE_PROVIDER WHERE 1=1");
            sqlQuery.append(" AND PATIENT_ID = ").append(idPatient);

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Long id = result.getLong("id");
                Long patient_id = result.getLong("patient_id");
                String name = result.getString("name");
                String adress = result.getString("address");
                String phoneNumber = result.getString("phone_number");
                LocalDate expirationDate = result.getTimestamp("expiration_date").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();

                insurance = new InsuranceProvider(id, name, adress, phoneNumber, expirationDate);
            }
        } catch(SQLException ex) {
            String message = "Error occurred while selecting all insurance provider of patient from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return insurance;
    }
    private static List<Appointment> queryAppointmentsOfPatient(Long idPatient, Connection connection) throws DatabaseQueryException {
        List<Appointment> appointmentList = new ArrayList<>();

        try  {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM APPOINTMENT WHERE 1=1");
            sqlQuery.append(" AND PATIENT_ID = ").append(idPatient);

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                Long doctor_id = result.getLong("doctor_id");
                Long patient_id = result.getLong("patient_id");
                LocalDate date = result.getTimestamp("date").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                LocalTime time = result.getTimestamp("time").toLocalDateTime().toLocalTime();
                String doctorNote = result.getString("doctor_note");

                // Doctor doctor = queryDoctorById(doctor_id, connection);

                Appointment appointment = new Appointment.AppointmentBuilder(id)
                        .withDate(date).withTime(time).build();

                appointmentList.add(appointment);
            }
        } catch (SQLException ex) {
            String message = "Error occurred while selecting appointments of the patient";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return appointmentList;
    }
    private static List<Treatment> queryTreatmentsOfPatient(Long idPatient, Connection connection) throws DatabaseQueryException {
        List<Treatment> treatmentList = new ArrayList<>();

        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM TREATMENT WHERE 1=1");
            sqlQuery.append(" AND PATIENT_ID = ").append(idPatient);

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                Long doctor_id = result.getLong("doctor_id");
                Long patient_id = result.getLong("patient_id");
                LocalDate date = result.getTimestamp("date").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                LocalTime time = result.getTimestamp("time").toLocalDateTime().toLocalTime();
                Long appliance_id= result.getLong("medical_device_id");

                // Doctor doctor = queryDoctorById(doctor_id, connection);
                MedicalDevice medicalDevice = queryMedicalDeviceById(appliance_id, connection);

                Treatment treatment = new Treatment.TreatmentBuilder(id, date, time)
                        .withAppliance(medicalDevice).build();

                treatmentList.add(treatment);
            }
        } catch (SQLException ex) {
            String message = "Error occurred while selecting treatments of the patient";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return treatmentList;
    }
    private static List<Symptom> querySymptomsOfPatient(Long idPatient, Connection connection) throws DatabaseQueryException {
        List<Symptom> symptomList = new ArrayList<>();

        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM SYMPTOM WHERE 1=1");
            sqlQuery.append(" AND PATIENT_ID = ").append(idPatient);

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                Long patientId = result.getLong("patient_id");
                String description = result.getString("description");
                Integer severity = result.getInt("severity");
                LocalDate dateOfAppearance = result.getTimestamp("date_of_first_appearance").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                LocalDate dateOfDissapearance = result.getTimestamp("date_of_dissapearance").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                String relatedFactors = result.getString("related_factors");

                try {
                    SymptomRating symptomRating = parseSeverityOfSymptoms(severity);
                    List<String> relatedFactorsSplit = parseRelatedFactors(relatedFactors);

                    Symptom symptom = new Symptom.SymptomBuilder(id, description, symptomRating)
                            .withDateOfFirstAppearance(dateOfAppearance)
                            .withDateOfDisapearance(dateOfDissapearance)
                            .withRelatedFactors(relatedFactorsSplit).build();

                    symptomList.add(symptom);
                } catch (UnknownEnumerationValue ex) {
                    String message = "Error occured while building constructor with unknown enumeration value";
                    logger.error(message, ex);
                }
            }
        }catch (SQLException ex) {
            String message = "Error occurred while selecting symptoms of the patient";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return symptomList;
    }


    private static List<Doctor> queryDoctorByDepartment(Long idDepartment, Connection connection) throws DatabaseQueryException {
        List<Doctor> doctorList = new ArrayList<>();

        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM DOCTOR WHERE 1=1");
            sqlQuery.append(" AND DEPARTMENT_ID = ").append(idDepartment);

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String title = result.getString("title");
                Long licenseNumber = result.getLong("license_number");
                LocalDate dateOfBirth = result.getTimestamp("date_of_birth").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                String phoneNumber = result.getString("phone_number");
                Integer yearsOfExperience = result.getInt("years_of_experience");
                LocalTime workStartTime = result.getTimestamp("work_start_time").toLocalDateTime().toLocalTime();
                LocalTime workEndTime = result.getTimestamp("work_end_time").toLocalDateTime().toLocalTime();

                List<Patient> patientList = queryPatientsOfDoctor(id, connection);
                List<Appointment> appointmentList = queryAppointmentsOfDoctor(id, connection);

                Doctor doctor = new Doctor.DoctorBuilder(id).withName(name).withSurname(surname).withTitle(title).
                        withLicenseNumber(licenseNumber).withDateOfBirth(dateOfBirth).withPhoneNumber(phoneNumber).
                        withYearsOfExperience(yearsOfExperience).withWorkStartTime(workStartTime).
                        withWorkEndTime(workEndTime).withPatientList(patientList).withAppointmentList(appointmentList).
                        build();

                doctorList.add(doctor);
            }
        } catch (SQLException ex) {
            String message = "Error occurred while selecting doctors by department from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return doctorList;
    }
    private static List<Patient> queryPatientByDepartment(Long idDepartment, Connection connection) throws DatabaseQueryException {
        List<Patient> patientList = new ArrayList<>();

        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM PATIENT WHERE 1=1");
            sqlQuery.append(" AND DEPARTMENT_ID = ").append(idDepartment);

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                LocalDate dateOfBirth = result.getTimestamp("date_of_birth").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                String phoneNumber = result.getString("phone_number");
                Long doctor_id = result.getLong("doctor_id");

                // Doctor doctor = queryDoctorById(doctor_id, connection);

                List<Appointment> appointmentList = queryAppointmentsOfPatient(id, connection);
                History<AppointmentHistory> appointmentHistory = filterHistoryAppointments(appointmentList);

                List<Treatment> treatmentList = queryTreatmentsOfPatient(id, connection);
                History<TreatmentHistory> treatmentHistory = filterHistoryTreatments(treatmentList);

                List<Symptom> symptomList = querySymptomsOfPatient(id, connection);
                History<SymptomHistory> symptomHistory = filterHistorySymptoms(symptomList);

                InsuranceProvider insuranceProvider = queryInsuranceProviderOfPatient(id, connection);


                Patient patient = new Patient.PatientBuilder(id).withName(name).withSurname(surname).
                        withDateOfBirth(dateOfBirth).withPhoneNumber(phoneNumber)
                        .withAppointmentList(appointmentList).withAppointmentHistory(appointmentHistory).
                        withTreatmentList(treatmentList).withTreatmentHistory(treatmentHistory).
                        withSymptomList(symptomList).withSymptomHistory(symptomHistory).
                        withInsurance(insuranceProvider).build();

                patientList.add(patient);
            }
        } catch (SQLException ex) {
            String message = "Error occurred while selecting patients by department from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return patientList;
    }
    private static List<MedicalDevice> queryApplianceByDepartment(Long idDepartment, Connection connection) throws DatabaseQueryException {
        List<MedicalDevice> applianceList = new ArrayList<>();

        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM MEDICAL_DEVICE WHERE 1=1");
            sqlQuery.append(" AND DEPARTMENT_ID = ").append(idDepartment);

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String manufacturerName = result.getString("manufacturer_name");
                Integer modelNumber = result.getInt("model_number");
                Long serialNumber = result.getLong("serial_number");
                String typeOfDevice = result.getString("type");
                LocalDate dateOfPurchase = result.getTimestamp("date_of_purchase").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                LocalDate expirationDate = result.getTimestamp("expiration_date").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();

                MedicalDevice medicalDevice = new MedicalDevice(id, name, manufacturerName, modelNumber, serialNumber,
                        typeOfDevice, dateOfPurchase, expirationDate);

                applianceList.add(medicalDevice);
            }
        } catch (SQLException ex) {
            String message = "Error occurred while selecting medical devices by department from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return applianceList;
    }


    public List<Doctor> queryAllDoctors() throws DatabaseQueryException {
        List<Doctor> doctorList = new ArrayList<>();

        try(Connection connection = connectToDatabase()) {
            String sqlQuery = "SELECT * FROM DOCTOR";

            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String title = result.getString("title");
                Long licenseNumber = result.getLong("license_number");
                LocalDate dateOfBirth = result.getTimestamp("date_of_birth").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                String phoneNumber = result.getString("phone_number");
                Integer yearsOfExperience = result.getInt("years_of_experience");
                LocalTime workStartTime = result.getTimestamp("work_start_time").toLocalDateTime().toLocalTime();
                LocalTime workEndTime = result.getTimestamp("work_end_time").toLocalDateTime().toLocalTime();

                List<Patient> patientList = queryPatientsOfDoctor(id, connection);
                List<Appointment> appointmentList = queryAppointmentsOfDoctor(id, connection);

                Doctor doctor = new Doctor.DoctorBuilder(id).withName(name).withSurname(surname).withTitle(title).
                        withLicenseNumber(licenseNumber).withDateOfBirth(dateOfBirth).withPhoneNumber(phoneNumber).
                        withYearsOfExperience(yearsOfExperience).withWorkStartTime(workStartTime).
                        withWorkEndTime(workEndTime).withPatientList(patientList).withAppointmentList(appointmentList).
                        build();

                doctorList.add(doctor);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while selecting all doctors from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return doctorList;
    }
    public List<Patient> queryAllPatients() throws DatabaseQueryException {
        List<Patient> patientList = new ArrayList<>();

        try(Connection connection = connectToDatabase()) {
            String sqlQuery = "SELECT * FROM PATIENT";

            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                LocalDate dateOfBirth = result.getTimestamp("date_of_birth").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                String phoneNumber = result.getString("phone_number");
                Long doctor_id = result.getLong("doctor_id");

                // Doctor doctor = queryDoctorById(doctor_id, connection);

                List<Appointment> appointmentList = queryAppointmentsOfPatient(id, connection);
                History<AppointmentHistory> appointmentHistory = filterHistoryAppointments(appointmentList);

                List<Treatment> treatmentList = queryTreatmentsOfPatient(id, connection);
                History<TreatmentHistory> treatmentHistory = filterHistoryTreatments(treatmentList);

                List<Symptom> symptomList = querySymptomsOfPatient(id, connection);
                History<SymptomHistory> symptomHistory = filterHistorySymptoms(symptomList);

                InsuranceProvider insuranceProvider = queryInsuranceProviderOfPatient(id, connection);


                Patient patient = new Patient.PatientBuilder(id).withName(name).withSurname(surname).
                        withDateOfBirth(dateOfBirth).withPhoneNumber(phoneNumber)
                        .withAppointmentList(appointmentList).withAppointmentHistory(appointmentHistory).
                        withTreatmentList(treatmentList).withTreatmentHistory(treatmentHistory).
                        withSymptomList(symptomList).withSymptomHistory(symptomHistory).
                        withInsurance(insuranceProvider).build();

                patientList.add(patient);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while selecting all patients from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return patientList;
    }
    public  List<Department> queryAllDepartments() throws DatabaseQueryException {
        List<Department> departmentList = new ArrayList<>();

        try(Connection connection = connectToDatabase()) {
            String sqlQuery = "SELECT * FROM DEPARTMENT WHERE 1=1";

            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");

                List<Doctor> doctorList = queryDoctorByDepartment(id, connection);
                List<Patient> patientList = queryPatientByDepartment(id, connection);
                List<MedicalDevice> applianceList = queryApplianceByDepartment(id, connection);

                Department department = new Department.DepartmentBuilder(id).withName(name).withDoctors(doctorList)
                                .withPatients(patientList).withAppliances(applianceList).build();

                departmentList.add(department);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while selecting all departments from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return departmentList;
    }
    public  List<MedicalDevice> queryAllMedicalDevices() throws DatabaseQueryException {
        List<MedicalDevice> applianceList = new ArrayList<>();

        try(Connection connection = connectToDatabase()) {
            String sqlQuery = "SELECT * FROM MEDICAL_DEVICE";

            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String manufacturerName = result.getString("manufacturer_name");
                Integer modelNumber = result.getInt("model_number");
                Long serialNumber = result.getLong("serial_number");
                String typeOfDevice = result.getString("type");
                LocalDate dateOfPurchase = result.getTimestamp("date_of_purchase").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();
                LocalDate expirationDate = result.getTimestamp("expiration_date").toInstant().atZone(
                        ZoneId.systemDefault()).toLocalDate();

                MedicalDevice medicalDevice = new MedicalDevice(id, name, manufacturerName, modelNumber, serialNumber,
                        typeOfDevice, dateOfPurchase, expirationDate);

                applianceList.add(medicalDevice);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while selecting all medical devices from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }

        return applianceList;
    }


    public void deleteDoctor(Doctor doctor) throws DatabaseQueryException {
        try (Connection connection = connectToDatabase()) {
            StringBuilder sqlQuery = new StringBuilder("DELETE FROM DOCTOR WHERE 1=1");
            sqlQuery.append(" AND ID = ").append(doctor.getId());

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0) {
                String message = "Doctor with id " + doctor.getId() + " not found in database";
                logger.error(message);
                throw new DatabaseQueryException(message);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while deleting doctor from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }
    public void updateDoctor(Doctor doctor, String sqlQuery, List<String> parameters) throws DatabaseQueryException, ParameterNotFound {
        try (Connection connection =  connectToDatabase()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);

            for(int i = 0; i < parameters.size(); i++) {
                switch(parameters.get(i)) {
                    case "id" -> statement.setLong(i + 1, doctor.getId());
                    case "name" -> statement.setString(i + 1, doctor.getName());
                    case "surname" -> statement.setString(i + 1, doctor.getSurname());
                    case "title" -> statement.setString(i + 1, doctor.getTitle());
                    case "phone_number" -> statement.setString(i + 1, doctor.getPhoneNumber());
                    case "years_of_experience" -> statement.setInt(i + 1, doctor.getYearsOfExperience());
                    case "work_start_time" -> statement.setTime(i + 1, Time.valueOf(doctor.getWorkStartTime()));
                    case "work_end_time" -> statement.setTime(i + 1, Time.valueOf(doctor.getWorkEndTime()));
                    case "date_of_birth" -> statement.setDate(i + 1, Date.valueOf(doctor.getDateOfBirth()));
                    default -> throw new ParameterNotFound("Unkown parameter while filling prepared statement");
                }
            }

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0) {
                String message = "Doctor with id " + doctor.getId() + " not found in database";
                logger.error(message);
                throw new DatabaseQueryException(message);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while updating doctor from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }
    public void insertDoctor(Doctor doctor, Long departmendId) throws DatabaseQueryException {
        try(Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO DOCTOR(name, surname, " +
                    "title, license_number, date_of_birth, phone_number, years_of_experience, work_start_time, work_end_time, " +
                    "department_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, doctor.getName());
            preparedStatement.setString(2, doctor.getSurname());
            preparedStatement.setString(3, doctor.getTitle());
            preparedStatement.setLong(4, doctor.getLicenseNumber());
            preparedStatement.setDate(5, Date.valueOf(doctor.getDateOfBirth()));
            preparedStatement.setString(6, doctor.getPhoneNumber());
            preparedStatement.setInt(7, doctor.getYearsOfExperience());
            preparedStatement.setTime(8, Time.valueOf(doctor.getWorkStartTime()));
            preparedStatement.setTime(9, Time.valueOf(doctor.getWorkEndTime()));
            preparedStatement.setLong(10, departmendId);

            preparedStatement.executeUpdate();

        } catch(SQLException | IOException ex) {
            String message = "Error occurred while insert new doctor object to database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }

    public void deleteDepartment(Department department) throws DatabaseQueryException {
        try (Connection connection = connectToDatabase()) {
            StringBuilder sqlQuery = new StringBuilder("DELETE FROM DEPARTMENT WHERE 1=1");
            sqlQuery.append(" AND ID = ").append(department.getId());

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0) {
                String message = "Department with id " + department.getId() + " not found in database";
                logger.error(message);
                throw new DatabaseQueryException(message);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while deleting departmentr from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }
    public void updateDepartment(Department department, String sqlQuery, List<String> parameters) throws DatabaseQueryException, ParameterNotFound {
        try (Connection connection =  connectToDatabase()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);

            for(int i = 0; i < parameters.size(); i++) {
                switch(parameters.get(i)) {
                    case "id" -> statement.setLong(i + 1, department.getId());
                    case "name" -> statement.setString(i + 1, department.getName());
                    default -> throw new ParameterNotFound("Unkown parameter while filling prepared statement");
                }
            }

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0) {
                String message = "Department with id " + department.getId() + " not found in database";
                logger.error(message);
                throw new DatabaseQueryException(message);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while updating department from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }
    public void insertDepartment(Department department) throws DatabaseQueryException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO DEPARTMENT(name) VALUES(?)");
            preparedStatement.setString(1, department.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while insert new department object to database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }

    public void deleteMedicalDevice(MedicalDevice medicalDevice) throws DatabaseQueryException {
        try (Connection connection = connectToDatabase()) {
            StringBuilder sqlQuery = new StringBuilder("DELETE FROM MEDICAL_DEVICE WHERE 1=1");
            sqlQuery.append(" AND ID = ").append(medicalDevice.getId());

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0) {
                String message = "Medical device with id " + medicalDevice.getId() + " not found in database";
                logger.error(message);
                throw new DatabaseQueryException(message);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while deleting medical device from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }
    public void updateMedicalDevice(MedicalDevice medicalDevice, String sqlQuery, List<String> parameters) throws DatabaseQueryException, ParameterNotFound {
        try (Connection connection =  connectToDatabase()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);

            for(int i = 0; i < parameters.size(); i++) {
                switch(parameters.get(i)) {
                    case "id" -> statement.setLong(i + 1, medicalDevice.getId());
                    case "name" -> statement.setString(i + 1, medicalDevice.getName());
                    case "manufacturer_name" -> statement.setString(i + 1, medicalDevice.getManufacturerName());
                    case "type" -> statement.setString(i + 1, medicalDevice.getTypeOfDevice());
                    case "date_of_purchase" -> statement.setDate(i + 1, Date.valueOf(medicalDevice.getDateOfPurchase()));
                    case "expiration_date" -> statement.setDate(i + 1, Date.valueOf(medicalDevice.getExpirationDate()));
                    default -> throw new ParameterNotFound("Unkown parameter while filling prepared statement");
                }
            }

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0) {
                String message = "Medical device with id " + medicalDevice.getId() + " not found in database";
                logger.error(message);
                throw new DatabaseQueryException(message);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while updating medical device from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }
    public void insertMedicalDevice(MedicalDevice medicalDevice, Long departmendId) throws DatabaseQueryException {
        try(Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO MEDICAL_DEVICE(name, manufacturer_name, " +
                    "model_number, serial_number, type, date_of_purchase, expiration_date, department_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1,medicalDevice.getName());
            preparedStatement.setString(2,medicalDevice.getManufacturerName());
            preparedStatement.setInt(3, medicalDevice.getModelNumber());
            preparedStatement.setLong(4,medicalDevice.getSerialNumber());
            preparedStatement.setString(5, medicalDevice.getTypeOfDevice());
            preparedStatement.setDate(6, Date.valueOf(medicalDevice.getDateOfPurchase()));
            preparedStatement.setDate(7, Date.valueOf(medicalDevice.getExpirationDate()));
            preparedStatement.setLong(8, departmendId);

            preparedStatement.executeUpdate();

        } catch(SQLException | IOException ex) {
            String message = "Error occurred while inserting new medical device object to database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }

    public void deletePatient(Patient patient) throws DatabaseQueryException {
        try (Connection connection = connectToDatabase()) {
            StringBuilder sqlQuery = new StringBuilder("DELETE FROM PATIENT WHERE 1=1");
            sqlQuery.append(" AND ID = ").append(patient.getId());

            PreparedStatement statement = connection.prepareStatement(sqlQuery.toString());
            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0) {
                String message = "Patient with id " + patient.getId() + " not found in database";
                logger.error(message);
                throw new DatabaseQueryException(message);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while deleting patient from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }
    public void updatePatient(Patient patient, String sqlQuery, List<String> parameters) throws DatabaseQueryException, ParameterNotFound {
        try (Connection connection =  connectToDatabase()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);

            for(int i = 0; i < parameters.size(); i++) {
                switch(parameters.get(i)) {
                    case "id" -> statement.setLong(i + 1, patient.getId());
                    case "name" -> statement.setString(i + 1, patient.getName());
                    case "surname" -> statement.setString(i + 1, patient.getSurname());
                    case "date_of_birth" -> statement.setDate(i + 1, Date.valueOf(patient.getDateOfBirth()));
                    case "phone_number" -> statement.setString(i + 1, patient.getPhoneNumber());
                    default -> throw new ParameterNotFound("Unkown parameter while filling prepared statement");
                }
            }

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0) {
                String message = "Patient with id " + patient.getId() + " not found in database";
                logger.error(message);
                throw new DatabaseQueryException(message);
            }
        } catch (SQLException | IOException ex) {
            String message = "Error occurred while updating patient from database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }
    public void insertPatient(Patient patient, Long doctorId, Long departmendId) throws DatabaseQueryException {
        try(Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PATIENT(name, surname, " +
                    "date_of_birth, phone_number, doctor_id, department_id) VALUES(?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, patient.getName());
            preparedStatement.setString(2, patient.getSurname());
            preparedStatement.setDate(3, Date.valueOf(patient.getDateOfBirth()));
            preparedStatement.setString(4, patient.getPhoneNumber());
            preparedStatement.setLong(5, doctorId);
            preparedStatement.setLong(6, departmendId);

            preparedStatement.executeUpdate();

        } catch(SQLException | IOException ex) {
            String message = "Error occurred while inserting new patient object to database";
            logger.error(message, ex);
            throw new DatabaseQueryException(message, ex);
        }
    }

    private static History<AppointmentHistory> filterHistoryAppointments(List<Appointment> appointments) {
        History<AppointmentHistory> appointmentHistory = new History<>();

        List<Appointment> filteredAppointments = appointments.stream()
                .filter(a -> a.getDate().isBefore(LocalDate.now())).toList();

        for (Appointment appointment : filteredAppointments) {
            appointmentHistory.addHistoryObject(new AppointmentHistory(appointment));
        }

        return appointmentHistory;
    }
    private static History<TreatmentHistory> filterHistoryTreatments(List<Treatment> treatments) {
        History<TreatmentHistory> treatmentHistory = new History<>();

        List<Treatment> filteredTreatments = treatments.stream()
                .filter(t -> t.getDate().isBefore(LocalDate.now())).toList();

        for (Treatment treatment : filteredTreatments) {
            treatmentHistory.addHistoryObject(new TreatmentHistory(treatment));
        }

        return treatmentHistory;
    }
    private static History<SymptomHistory> filterHistorySymptoms(List<Symptom> symptoms) {
        History<SymptomHistory> symptomHistory = new History<>();

        List<Symptom> filteredSymptoms = symptoms.stream()
                .filter(s -> s.getDateOfDisapearance().isBefore(LocalDate.now())).toList();

        for (Symptom symptom : filteredSymptoms) {
            symptomHistory.addHistoryObject(new SymptomHistory(symptom));
        }

        return symptomHistory;
    }


    private static SymptomRating parseSeverityOfSymptoms(Integer severity) throws UnknownEnumerationValue {
        SymptomRating rating;

        switch(severity) {
            case 1 -> rating = SymptomRating.MILD;
            case 2 -> rating = SymptomRating.MODERATE;
            case 3 -> rating = SymptomRating.SEVERE;
            default -> throw new UnknownEnumerationValue("Error occured while parsing severity value for symptom rating enumeration");
        }

        return rating;
    }
    private static List<String> parseRelatedFactors(String relatedFactors) {
        return Arrays.asList(relatedFactors.split("\\. "));
    }
}
