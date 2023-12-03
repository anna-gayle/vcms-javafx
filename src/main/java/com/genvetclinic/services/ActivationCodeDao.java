package com.genvetclinic.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.genvetclinic.models.ActivationCode;

/**
 * The {@code ActivationCodeDao} class provides methods to interact with the activation codes stored in the database.
 * It includes operations such as retrieving all activation codes, inserting new activation codes, checking if an activation
 * code is used or exists, updating the status of an activation code, and more.
 *
 * <p>This class uses the {@link DatabaseConnection} class for establishing a connection to the database.
 *
 * @author Your Name
 * @version 1.0
 * @since 2023-01-01
 */
public class ActivationCodeDao {
    // Constants
    private static final String TABLE_NAME = "activation";

    // Fields
    private DatabaseConnection databaseConnection;

    /**
     * Constructs an {@code ActivationCodeDao} object.
     * Initializes the {@link DatabaseConnection} used for database operations.
     *
     * @throws SQLException if a database access error occurs.
     */
    public ActivationCodeDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

    /**
     * Retrieves a list of all activation codes from the database.
     *
     * @return A list of {@link ActivationCode} objects representing the activation codes.
     */
    public List<ActivationCode> getAllActivationCodes() {
        List<ActivationCode> activationCodes = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection()) {
            String query = "SELECT * FROM " + TABLE_NAME;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    ActivationCode activationCode = new ActivationCode();
                    activationCode.setActivationCode(resultSet.getString("activation_code"));
                    activationCode.setCodeStatus(resultSet.getString("code_status"));
                    activationCodes.add(activationCode);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activationCodes;
    }

    /**
     * Inserts a new activation code into the database.
     *
     * @param activationCode The {@link ActivationCode} object to be inserted.
     * @throws SQLException if a database access error occurs.
     */
    public void insertActivationCode(ActivationCode activationCode) throws SQLException {
        try (DatabaseConnection databaseConnection = new DatabaseConnection()) {
            Connection connection = databaseConnection.getConnection();
            String query = "INSERT INTO " + TABLE_NAME + " (activation_code, code_status) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, activationCode.getActivationCode());
                preparedStatement.setString(2, activationCode.getCodeStatus());

                preparedStatement.executeUpdate();
            }
        }
    }

    /**
     * Checks if an activation code is marked as 'Expired' in the database.
     *
     * @param activationCode The activation code to check.
     * @return {@code true} if the activation code is used and marked as 'Expired', {@code false} otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean isCodeUsed(String activationCode) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE activation_code = ? AND code_status = 'Expired'";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, activationCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    /**
     * Updates the status of an activation code in the database.
     *
     * @param activationCode The activation code to be updated.
     * @param status         The new status of the activation code.
     * @throws SQLException if a database access error occurs.
     */
    public void updateActivationCodeStatus(String activationCode, String status) throws SQLException {
        String query = "UPDATE " + TABLE_NAME + " SET code_status = ? WHERE activation_code = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, status);
            preparedStatement.setString(2, activationCode);

            preparedStatement.executeUpdate();
        }
    }

    /**
     * Checks if an activation code exists in the database.
     *
     * @param activationCode The activation code to check.
     * @return {@code true} if the activation code exists, {@code false} otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean isActivationCodeExists(String activationCode) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE activation_code = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, activationCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
}
