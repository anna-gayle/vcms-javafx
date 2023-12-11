package com.genvetclinic.services;

import com.genvetclinic.models.Personnel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code PersonnelDao} class provides data access methods for managing Personnel entities
 * in the veterinary clinic system. It includes methods for saving, updating, deleting,
 * and retrieving personnel information from the database.
 *
 * <p>This class utilizes a {@link DatabaseConnection} to establish a connection to the database
 * and execute SQL queries for interacting with the personnel data.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class PersonnelDao {

    /**
     * The database connection used by this DAO.
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Constructs a new {@code PersonnelDao} instance with a default database connection.
     *
     * @throws SQLException if a database access error occurs.
     */
    public PersonnelDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

    /**
     * Constructs a new {@code PersonnelDao} instance with the specified database connection.
     *
     * @param databaseConnection the database connection to be used by this DAO.
     */
    public PersonnelDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void savePersonnel(Personnel personnel) throws SQLException {
        String sql = "INSERT INTO personnel (personnel_id, personnel_name, personnel_email, personnel_address, personnel_contact, "
                + "emergency_contact, job_title, vet_specialization, hire_date, work_schedule, performance_rating, attendance_rating, "
                + "personnel_certification) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setSavePersonnelParameters(preparedStatement, personnel);
            preparedStatement.executeUpdate();
        }
    }

    // Methods for saving, updating, deleting, and retrieving personnel information...
    public void updatePersonnel(Personnel personnel) throws SQLException {
        String sql = "UPDATE personnel SET personnel_name = ?, personnel_email = ?, personnel_address = ?, personnel_contact = ?, "
            + "emergency_contact = ?, job_title = ?, vet_specialization = ?, hire_date = ?, work_schedule = ?, performance_rating = ?, "
            + "attendance_rating = ?, personnel_certification = ? "
            + "WHERE personnel_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setUpdatePersonnelParameters(preparedStatement, personnel);
            preparedStatement.setString(13, personnel.getPersonnelId());
            preparedStatement.executeUpdate();
        }
    }

    public void deletePersonnel(Personnel personnel) {
        String sql = "DELETE FROM personnel WHERE personnel_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, personnel.getPersonnelId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if a personnel exists in the database based on their name, email, and contact.
     *
     * @param  personnel  the personnel object to check for existence
     * @return            true if the personnel exists, false otherwise
     * @throws SQLException if there is an error executing the SQL query
     */
    public boolean isPersonnelExists(Personnel personnel) throws SQLException {
        String sql = "SELECT COUNT(*) FROM personnel WHERE personnel_name = ? AND personnel_email = ? AND personnel_contact = ?";
        try (Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, personnel.getPersonnelName());
            preparedStatement.setString(2, personnel.getPersonnelEmail());
            preparedStatement.setString(3, personnel.getPersonnelContact());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }

    public int getTotalPersonnel() throws SQLException {
        String sql = "SELECT COUNT(*) FROM personnel";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public int countRecentlyHiredPersonnelWithinWeek() throws SQLException {
        String sql = "SELECT COUNT(*) FROM personnel WHERE hire_date >= CURDATE() - INTERVAL DAYOFWEEK(CURDATE())-1 DAY AND hire_date < CURDATE() + INTERVAL 7-DAYOFWEEK(CURDATE()) DAY";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public String getNotificationTextForPersonnel() throws SQLException {
        int recentlyHiredPersonnelCount = countRecentlyHiredPersonnelWithinWeek();

        if (recentlyHiredPersonnelCount > 0) {
            return recentlyHiredPersonnelCount + " recently \n hired personnel.";
        } else {
            return "No new notifications.";
        }
    }

    // Private methods for setting parameters during database operations...
    private void setSavePersonnelParameters(PreparedStatement preparedStatement, Personnel personnel) throws SQLException {
        preparedStatement.setString(1, personnel.getPersonnelId());
        preparedStatement.setString(2, personnel.getPersonnelName());
        preparedStatement.setString(3, personnel.getPersonnelEmail());
        preparedStatement.setString(4, personnel.getPersonnelAddress());
        preparedStatement.setString(5, personnel.getPersonnelContact());
        preparedStatement.setString(6, personnel.getEmergencyContact());
        preparedStatement.setString(7, personnel.getJobTitle());
        preparedStatement.setString(8, personnel.getVetSpec());
        preparedStatement.setDate(9, Date.valueOf(personnel.getHireDate()));
        preparedStatement.setString(10, personnel.getWorkSched());      
        
        if (personnel.getPerfRating() != null) {
            preparedStatement.setBigDecimal(11, personnel.getPerfRating());
        } else {
            preparedStatement.setNull(11, java.sql.Types.INTEGER);
        }
    
        if (personnel.getAttendRating() != null) {
            preparedStatement.setBigDecimal(12, personnel.getAttendRating());
        } else {
            preparedStatement.setNull(12, java.sql.Types.INTEGER);
        }
    
        preparedStatement.setString(13, personnel.getCertification());
    }
    
    private void setUpdatePersonnelParameters(PreparedStatement preparedStatement, Personnel personnel) throws SQLException {
        preparedStatement.setString(1, personnel.getPersonnelName());
        preparedStatement.setString(2, personnel.getPersonnelEmail());
        preparedStatement.setString(3, personnel.getPersonnelAddress());
        preparedStatement.setString(4, personnel.getPersonnelContact());
        preparedStatement.setString(5, personnel.getEmergencyContact());
        preparedStatement.setString(6, personnel.getJobTitle());
        preparedStatement.setString(7, personnel.getVetSpec());
        preparedStatement.setDate(8, Date.valueOf(personnel.getHireDate()));
        preparedStatement.setString(9, personnel.getWorkSched());
        
        if (personnel.getPerfRating() != null) {
            preparedStatement.setBigDecimal(10, personnel.getPerfRating());
        } else {
            preparedStatement.setNull(10, java.sql.Types.INTEGER);
        }
    
        if (personnel.getAttendRating() != null) {
            preparedStatement.setBigDecimal(11, personnel.getAttendRating());
        } else {
            preparedStatement.setNull(11, java.sql.Types.INTEGER);
        }
    
        preparedStatement.setString(12, personnel.getCertification());
    }    
    
}
