package eng.java.project.database;

import eng.java.project.entity.hospital.core.Department;
import eng.java.project.entity.hospital.core.Doctor;
import eng.java.project.entity.hospital.core.MedicalDevice;
import eng.java.project.entity.hospital.core.Patient;
import eng.java.project.exception.DatabaseQueryException;
import eng.java.project.exception.ParameterNotFound;
import javafx.scene.control.MenuItem;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public sealed interface DatabaseSource permits DatabaseUtil {
    List<Doctor> queryAllDoctors() throws DatabaseQueryException;
    void deleteDoctor(Doctor doctor) throws DatabaseQueryException;
    void updateDoctor(Doctor doctor, String sqlQuery, List<String> parameters) throws DatabaseQueryException, ParameterNotFound;
    void insertDoctor(Doctor doctor, Long departmentId) throws DatabaseQueryException;

    List<Patient> queryAllPatients() throws DatabaseQueryException;
    void deletePatient(Patient patient) throws DatabaseQueryException;
    void updatePatient(Patient patient, String sqlQuery, List<String> parameters) throws DatabaseQueryException, ParameterNotFound;
    void insertPatient(Patient patient, Long doctorId, Long departmentId) throws DatabaseQueryException;

    List<Department> queryAllDepartments() throws DatabaseQueryException;
    void deleteDepartment(Department department) throws DatabaseQueryException;
    void updateDepartment(Department department, String sqlQuery, List<String> parameters) throws DatabaseQueryException, ParameterNotFound;
    void insertDepartment(Department department) throws DatabaseQueryException;

    List<MedicalDevice> queryAllMedicalDevices() throws DatabaseQueryException;
    void deleteMedicalDevice(MedicalDevice department) throws DatabaseQueryException;
    void updateMedicalDevice(MedicalDevice department, String sqlQuery, List<String> parameters) throws DatabaseQueryException, ParameterNotFound;
    void insertMedicalDevice(MedicalDevice device, Long departmentId) throws DatabaseQueryException;
}
