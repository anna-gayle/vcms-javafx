package com.genvetclinic.services;

import com.genvetclinic.models.Patient;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code PatientDao} class provides data access methods for managing patient information
 * in the veterinary clinic system. It includes operations for saving, updating, and deleting
 * patient records, as well as querying patient information.
 *
 * <p>This class interacts with the underlying database through the {@link DatabaseConnection}
 * class to perform database operations.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class PatientDao {

    /**
     * The {@link DatabaseConnection} instance used to establish a connection to the database.
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Constructs a new {@code PatientDao} instance with the specified database connection.
     *
     * @param databaseConnection the {@link DatabaseConnection} instance to use.
     */
    public PatientDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

    public PatientDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

     // Methods for saving, updating, and deleting patient records...
    public void savePatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (patient_id, patient_name, patient_species, patient_breed, age_in_years, patient_color, admitted_date, "
                + "medical_history, vaccination_history, special_instruction, owner_name, owner_contact, owner_email, owner_address, patient_insurance, "
                + "patient_weight, mchip_id, patient_gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setSavePatientParameters(preparedStatement, patient);
            preparedStatement.executeUpdate();
        }
    }
    
    public void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET patient_name = ?, patient_species = ?, patient_breed = ?, age_in_years = ?, patient_color = ?, "
                + "admitted_date = ?, medical_history = ?, vaccination_history = ?, special_instruction = ?, owner_name = ?, owner_contact = ?, "
                + "owner_email = ?, owner_address = ?, patient_insurance = ?, patient_weight = ?, mchip_id = ?, patient_gender = ? "
                + "WHERE patient_id = ?";  
        
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setUpdatePatientParameters(preparedStatement, patient);
            preparedStatement.setString(18, patient.getPatientid()); 
            preparedStatement.executeUpdate();
        }
    }
    
    public void deletePatient(Patient patient) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, patient.getPatientid());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Checks if a patient with the specified details already exists in the database.
     *
     * @param patientName    the name of the patient.
     * @param patientSpecies the species of the patient.
     * @param patientBreed   the breed of the patient.
     * @param ownerName      the name of the owner.
     * @return {@code true} if the patient exists, {@code false} otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean isPatientExists(String patientName, String patientSpecies, String patientBreed, String ownerName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM patients WHERE patient_name = ? AND patient_species = ? AND patient_breed = ? AND owner_name = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, patientName);
            preparedStatement.setString(2, patientSpecies);
            preparedStatement.setString(3, patientBreed);
            preparedStatement.setString(4, ownerName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }

    /**
     * Retrieves the total number of patients in the database.
     *
     * @return the total number of patients.
     * @throws SQLException if a database access error occurs.
     */
    public int getTotalPatients() throws SQLException {
        String sql = "SELECT COUNT(*) FROM patients";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    /**
     * Counts the number of patients admitted within the current week.
     *
     * @return the number of patients admitted this week.
     * @throws SQLException if a database access error occurs.
     */
    
    public int countPatientsAdmittedThisWeek() throws SQLException {
        String sql = "SELECT COUNT(*) FROM patients WHERE admitted_date >= CURDATE() - INTERVAL DAYOFWEEK(CURDATE()) - 1 DAY AND admitted_date < CURDATE() + INTERVAL 7 - DAYOFWEEK(CURDATE()) DAY";
        
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }    
    
    // Private methods for setting parameters during database operations...
    private void setSavePatientParameters(PreparedStatement preparedStatement, Patient patient) throws SQLException {
        preparedStatement.setString(1, patient.getPatientid());
        preparedStatement.setString(2, patient.getPatientName());
        preparedStatement.setString(3, patient.getPatientSpecies());
        preparedStatement.setString(4, patient.getPatientBreed());
        preparedStatement.setBigDecimal(5, patient.getPatientAge());
        preparedStatement.setString(6, patient.getPatientColor());
        preparedStatement.setDate(7, Date.valueOf(patient.getAdmittedDate()));
        preparedStatement.setString(8, patient.getMedicalHistory());
        preparedStatement.setString(9, patient.getVaccineHistory());
        preparedStatement.setString(10, patient.getSpecialInstructions());
        preparedStatement.setString(11, patient.getOwnerName());
        preparedStatement.setString(12, patient.getOwnerContact());
        preparedStatement.setString(13, patient.getOwnerEmail());
        preparedStatement.setString(14, patient.getOwnerAddress());
        preparedStatement.setString(15, patient.getPatientInsurance());
        preparedStatement.setBigDecimal(16, patient.getPatientWeight());
        preparedStatement.setString(17, patient.getMicrochipId());
        preparedStatement.setString(18, patient.getPatientGender());
    }

    private void setUpdatePatientParameters(PreparedStatement preparedStatement, Patient patient) throws SQLException {
        preparedStatement.setString(1, patient.getPatientName());
        preparedStatement.setString(2, patient.getPatientSpecies());
        preparedStatement.setString(3, patient.getPatientBreed());
        preparedStatement.setBigDecimal(4, patient.getPatientAge());
        preparedStatement.setString(5, patient.getPatientColor());
        preparedStatement.setDate(6, Date.valueOf(patient.getAdmittedDate()));
        preparedStatement.setString(7, patient.getMedicalHistory());
        preparedStatement.setString(8, patient.getVaccineHistory());
        preparedStatement.setString(9, patient.getSpecialInstructions());
        preparedStatement.setString(10, patient.getOwnerName());
        preparedStatement.setString(11, patient.getOwnerContact());
        preparedStatement.setString(12, patient.getOwnerEmail());
        preparedStatement.setString(13, patient.getOwnerAddress());
        preparedStatement.setString(14, patient.getPatientInsurance());
        preparedStatement.setBigDecimal(15, patient.getPatientWeight());
        preparedStatement.setString(16, patient.getMicrochipId());
        preparedStatement.setString(17, patient.getPatientGender());
    }
}
