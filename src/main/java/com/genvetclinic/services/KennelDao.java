package com.genvetclinic.services;


import com.genvetclinic.models.Kennel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code KennelDao} class provides data access methods for managing kennels
 * in the veterinary clinic system. It includes operations for saving, updating, and deleting
 * kennel records, as well as querying kennel information.
 *
 * <p>This class interacts with the underlying database through the {@link DatabaseConnection}
 * class to perform database operations.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class KennelDao {


    /**
     * The {@link DatabaseConnection} instance used to establish a connection to the database.
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Constructs a new {@code KennelDao} instance with the default database connection.
     *
     * @throws SQLException if a database access error occurs.
     */
    public KennelDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

    /**
     * Constructs a new {@code KennelDao} instance with the specified database connection.
     *
     * @param databaseConnection the {@link DatabaseConnection} instance to use.
     */
    public KennelDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Methods for saving, updating, and deleting kennel records...
    public void saveKennel(Kennel kennel) throws SQLException {
        String sql = "INSERT INTO kennels (kennel_id, kennel_name, kennel_capacity, kennel_status) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setSaveKennelParameters(preparedStatement, kennel);
            preparedStatement.executeUpdate();
        }
    }

    public void updateKennel(Kennel kennel) throws SQLException {
        String sql = "UPDATE kennels SET kennel_name = ?, kennel_capacity = ?, kennel_status = ? WHERE kennel_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setUpdateKennelParameters(preparedStatement, kennel);
            preparedStatement.setString(4, kennel.getKennelId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteKennel(Kennel kennel) throws SQLException {
        String sql = "DELETE FROM kennels WHERE kennel_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, kennel.getKennelId());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Retrieves a notification message for kennels that need attention.
     *
     * @return a notification message indicating the number of kennels that need attention.
     * @throws SQLException if a database access error occurs.
     */
    public String getNotificationTextForKennels() throws SQLException {
        String sql = "SELECT COUNT(*) FROM kennels WHERE kennel_status <> 'Available for Boarding'";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            int kennelCount = resultSet.getInt(1);

            if (kennelCount > 0) {
                return kennelCount + " kennel(s) \n that need your attention.";
            } else {
                return "No new notifications.";
            }
        }
    }

    // Private methods for setting parameters during database operations...
    private void setSaveKennelParameters(PreparedStatement preparedStatement, Kennel kennel) throws SQLException {
        preparedStatement.setString(1, kennel.getKennelId());
        preparedStatement.setString(2, kennel.getKennelName());
        preparedStatement.setInt(3, kennel.getKennelCapacity());
        preparedStatement.setString(4, kennel.getKennelStatus());
    }

    private void setUpdateKennelParameters(PreparedStatement preparedStatement, Kennel kennel) throws SQLException {
        preparedStatement.setString(1, kennel.getKennelName());
        preparedStatement.setInt(2, kennel.getKennelCapacity());
        preparedStatement.setString(3, kennel.getKennelStatus());
    }

}
