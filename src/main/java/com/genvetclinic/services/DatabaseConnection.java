package com.genvetclinic.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code DatabaseConnection} class manages the connection to the MySQL database
 * used by the veterinary clinic system. It provides methods for establishing, closing,
 * and executing queries on the database.
 *
 * <p>This class implements the {@link AutoCloseable} interface, allowing it to be used
 * in try-with-resources statements to ensure proper resource management.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class DatabaseConnection implements AutoCloseable {

    /**
     * The JDBC URL for connecting to the MySQL database.
     */
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/vcms_database";
    /**
     * The username for authenticating the database connection.
     */
    private static final String DB_USERNAME = "root";
    /**
     * The password for authenticating the database connection.
     */
    private static final String DB_PASSWORD = "";
    /**
     * The connection to the database.
     */
    private Connection connection;

    /**
     * Constructs a new {@code DatabaseConnection} instance and establishes a connection
     * to the MySQL database.
     *
     * @throws SQLException if a database access error occurs.
     */
    public DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException("Error establishing database connection", e);
        }
    }

    /**
     * Retrieves the current database connection.
     *
     * @return the current database connection.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error establishing database connection", e);
        }
        return connection;
    }

    // Methods for closing resources, executing queries, and updating the database...
    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

    public void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public void executeUpdate(String query, Object... params) throws SQLException {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeStatement(statement);
        }
    }
}

