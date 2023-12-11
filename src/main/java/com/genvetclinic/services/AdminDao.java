package com.genvetclinic.services;

import com.genvetclinic.models.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AdminDao} class provides data access methods for managing Admin entities
 * in the veterinary clinic system. It includes methods for saving, updating, deleting,
 * and retrieving admin information from the database.
 *
 * <p>This class utilizes a {@link DatabaseConnection} to establish a connection to the database
 * and execute SQL queries for interacting with the admin data.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class AdminDao {

    /**
     * The database connection used by this DAO.
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Constructs a new {@code AdminDao} instance with a default database connection.
     *
     * @throws SQLException if a database access error occurs.
     */
    public AdminDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

   /**
     * Constructs a new {@code AdminDao} instance with the specified database connection.
     *
     * @param databaseConnection the database connection to be used by this DAO.
     */
    public AdminDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Methods for saving, updating, deleting, validating, and retrieving admin information...
    public void save(Admin admin) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO admin (admin_id, username, password, admin_email, security_question, security_answer, admin_code, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, admin.getAdminId());
            preparedStatement.setString(2, admin.getUsername());
            preparedStatement.setString(3, admin.getPassword());
            preparedStatement.setString(4, admin.getAdminEmail());
            preparedStatement.setString(5, admin.getSecurityQuestion());
            preparedStatement.setString(6, admin.getSecurityAnswer());
            preparedStatement.setString(7, admin.getAdminCode());
            preparedStatement.setBoolean(8, admin.isActive());

            preparedStatement.executeUpdate();
        }
    }

    public void updateAdmin(Admin admin) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE admin SET username = ?, password = ?, admin_email = ?, security_question = ?, security_answer = ?, admin_code = ?, is_active = ? WHERE admin_id = ?")) {

            preparedStatement.setString(1, admin.getUsername());
            preparedStatement.setString(2, admin.getPassword());
            preparedStatement.setString(3, admin.getAdminEmail());
            preparedStatement.setString(4, admin.getSecurityQuestion());
            preparedStatement.setString(5, admin.getSecurityAnswer());
            preparedStatement.setString(6, admin.getAdminCode());
            preparedStatement.setBoolean(7, admin.isActive());
            preparedStatement.setString(8, admin.getAdminId());

            preparedStatement.executeUpdate();
        }
    }

    public void deleteAdmin(String adminId) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM admin WHERE admin_id = ?")) {

            preparedStatement.setString(1, adminId);

            preparedStatement.executeUpdate();
        }
    }

    public void setAdminActiveStatus(String username, boolean isActive) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE admin SET is_active = ? WHERE username = ?")) {
    
            preparedStatement.setBoolean(1, isActive);
            preparedStatement.setString(2, username);
    
            preparedStatement.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }   
               
    public Admin getAdminByUsername(String username) throws SQLException {
        Admin admin = null;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM admin WHERE username = ?")) {
    
            preparedStatement.setString(1, username);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    admin = new Admin(
                            resultSet.getString("admin_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("admin_email"),
                            resultSet.getString("security_question"),
                            resultSet.getString("security_answer"),
                            resultSet.getString("admin_code"),
                            resultSet.getBoolean("is_active")
                    );
                }
            }
        }
        return admin;
    }        

    public String getSecurityQuestionByUsername(String username) throws SQLException {
        String securityQuestion = null;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT security_question FROM admin WHERE username = ?")) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    securityQuestion = resultSet.getString("security_question");
                }
            }
        }
        return securityQuestion;
    }

    public boolean doesUsernameExist(String username) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM admin WHERE username = ? ")) {
    
            preparedStatement.setString(1, username);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public boolean doesUserExist(String username, String adminEmail) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM admin WHERE username = ? AND (admin_email = ? OR admin_email IS NULL)")) {
    
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, adminEmail);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public List<Admin> getActiveAdmins() throws SQLException {
        List<Admin> activeAdmins = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM admin WHERE is_active = 1")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Admin admin = new Admin(
                            resultSet.getString("admin_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("admin_email"),
                            resultSet.getString("security_question"),
                            resultSet.getString("security_answer"),
                            resultSet.getString("admin_code"),
                            resultSet.getBoolean("is_active")
                    );
                    activeAdmins.add(admin);
                }
            }
        }
        return activeAdmins;
    }
}
