package com.genvetclinic.services;

import com.genvetclinic.models.Lab;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code LabDao} class provides data access methods for managing laboratories
 * in the veterinary clinic system. It includes operations for saving, updating, and deleting
 * lab records, as well as querying lab information.
 *
 * <p>This class interacts with the underlying database through the {@link DatabaseConnection}
 * class to perform database operations.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class LabDao {

    /**
     * The {@link DatabaseConnection} instance used to establish a connection to the database.
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Constructs a new {@code LabDao} instance with the default database connection.
     *
     * @throws SQLException if a database access error occurs.
     */
    public LabDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

    /**
     * Constructs a new {@code LabDao} instance with the specified database connection.
     *
     * @param databaseConnection the {@link DatabaseConnection} instance to use.
     */
    public LabDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Methods for saving, updating, retrieving, and deleting lab records...
    public void saveLab(Lab lab) throws SQLException {
        String sql = "INSERT INTO laboratories (lab_id, lab_name, no_of_lab_equipment, lab_status) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setSaveLabParameters(preparedStatement, lab);
            preparedStatement.executeUpdate();
        }
    }

    public void updateLab(Lab lab) throws SQLException {
        String sql = "UPDATE laboratories SET lab_name = ?, no_of_lab_equipment = ?, lab_status = ? WHERE lab_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setUpdateLabParameters(preparedStatement, lab);
            preparedStatement.setString(4, lab.getLabId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteLab(Lab lab) throws SQLException {
        String sql = "DELETE FROM laboratories WHERE lab_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, lab.getLabId());
            preparedStatement.executeUpdate();
        }
    }

    public int countLabsWithAttentionNeeded() throws SQLException {
        String sql = "SELECT COUNT(*) FROM laboratories WHERE lab_status <> 'Available for Testing'";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    /**
     * Retrieves a notification message for laboratories that need attention.
     *
     * @return a notification message indicating the number of laboratories that need attention.
     * @throws SQLException if a database access error occurs.
     */
    public String getNotificationTextForLabs() throws SQLException {
        int labsNeedingAttentionCount = countLabsWithAttentionNeeded();

        if (labsNeedingAttentionCount > 0) {
            return labsNeedingAttentionCount + " laboratory(ies) \n that need your attention.";
        } else {
            return "No new notifications.";
        }
    }

    // Private methods for setting parameters during database operations...
    private void setSaveLabParameters(PreparedStatement preparedStatement, Lab lab) throws SQLException {
        preparedStatement.setString(1, lab.getLabId());
        preparedStatement.setString(2, lab.getLabName());
        preparedStatement.setInt(3, lab.getNoOfEquipment());
        preparedStatement.setString(4, lab.getLabStatus());
    }

    private void setUpdateLabParameters(PreparedStatement preparedStatement, Lab lab) throws SQLException {
        preparedStatement.setString(1, lab.getLabName());
        preparedStatement.setInt(2, lab.getNoOfEquipment());
        preparedStatement.setString(3, lab.getLabStatus());
    }

}
