package com.genvetclinic.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import com.genvetclinic.models.Boarder;

/**
 * The {@code BoarderDao} class provides data access methods for managing Boarder entities
 * in the veterinary clinic system. It includes methods for saving, updating, deleting,
 * and retrieving boarder information from the database.
 *
 * <p>This class utilizes a {@link DatabaseConnection} to establish a connection to the database
 * and execute SQL queries for interacting with the boarder data.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class BoarderDao {

    /**
     * The database connection used by this DAO.
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Constructs a new {@code BoarderDao} instance with a default database connection.
     *
     * @throws SQLException if a database access error occurs.
     */
    public BoarderDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

    /**
     * Constructs a new {@code BoarderDao} instance with the specified database connection.
     *
     * @param databaseConnection the database connection to be used by this DAO.
     */
    public BoarderDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Methods for saving, updating, deleting, validating, and retrieving boarder information...
    public void saveBoarder(Boarder boarder) throws SQLException {
        String sql = "INSERT INTO boarders (boarder_id, boarder_name, boarder_species, boarder_breed, boarder_color, " +
                "b_special_instructions, b_owner_name, b_owner_contact, b_owner_address, date_boarded, boarder_age, " +
                "boarder_gender, boarder_weight, date_departed, b_owner_email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setSaveBoarderParameters(preparedStatement, boarder);
            preparedStatement.executeUpdate();
        }
    }

    public void updateBoarder(Boarder boarder) throws SQLException {
        String sql = "UPDATE boarders SET boarder_name = ?, boarder_species = ?, boarder_breed = ?, boarder_age = ?, " +
                "boarder_color = ?, date_boarded = ?, b_special_instructions = ?, b_owner_name = ?, " +
                "b_owner_contact = ?, b_owner_address = ?, boarder_gender = ?, boarder_weight = ?, date_departed = ?, " +
                "b_owner_email = ? WHERE boarder_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setUpdateBoarderParameters(preparedStatement, boarder);
            preparedStatement.setString(15, boarder.getBoarderId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteBoarder(Boarder boarder) throws SQLException {
        String sql = "DELETE FROM boarders WHERE boarder_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, boarder.getBoarderId());
            preparedStatement.executeUpdate();
        }
    }

    public boolean isBoarderExists(String boarderName, String boarderSpecies, String boarderBreed, String bOwnerName,
                                    LocalDate dateBoarded, LocalDate dateDeparted) throws SQLException {
        String sql = "SELECT COUNT(*) FROM boarders WHERE boarder_name = ? AND boarder_species = ? AND boarder_breed = ? " +
                "AND b_owner_name = ? AND date_boarded = ? AND date_departed = ?";
        try (Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, boarderName);
            preparedStatement.setString(2, boarderSpecies);
            preparedStatement.setString(3, boarderBreed);
            preparedStatement.setString(4, bOwnerName);
            preparedStatement.setDate(5, java.sql.Date.valueOf(dateBoarded));
            preparedStatement.setDate(6, java.sql.Date.valueOf(dateDeparted));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }

    public int countBoarders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM boarders";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
    
    public int countRecentlyAdmittedBoarders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM boarders WHERE date_boarded >= CURDATE() - INTERVAL DAYOFWEEK(CURDATE()) - 1 DAY AND date_boarded < CURDATE() + INTERVAL 7 - DAYOFWEEK(CURDATE()) DAY";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
          

    public int countBoardersWithDepartureDateWithinWeek() throws SQLException {
        LocalDate oneWeekLater = LocalDate.now().plusWeeks(1);
        String sql = "SELECT COUNT(*) FROM boarders WHERE date_departed <= ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(oneWeekLater));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        }
    }

    public String getNotificationText() throws SQLException {
        int recentlyAdmittedCount = countRecentlyAdmittedBoarders();
        int departureWithinWeekCount = countBoardersWithDepartureDateWithinWeek();
    
        if (recentlyAdmittedCount > 0 && departureWithinWeekCount > 0) {
            return "You have " + recentlyAdmittedCount + " recently \n admitted boarder(s) \n and "
                    + departureWithinWeekCount + " boarder(s) \n with departure dates \n within the week.";
        } else if (recentlyAdmittedCount > 0) {
            return "You have " + recentlyAdmittedCount + " recently \n admitted boarder(s).";
        } else if (departureWithinWeekCount > 0) {
            return "You have " + departureWithinWeekCount + " boarder(s) \n with departure dates \n within the week.";
        } else {
            return "No new notifications.";
        }
    }    

    // Private methods for setting parameters during database operations...
    private void setSaveBoarderParameters(PreparedStatement preparedStatement, Boarder boarder) throws SQLException {
        preparedStatement.setString(1, boarder.getBoarderId());
        preparedStatement.setString(2, boarder.getBoarderName());
        preparedStatement.setString(3, boarder.getBoarderSpecies());
        preparedStatement.setString(4, boarder.getBoarderBreed());
        preparedStatement.setString(5, boarder.getBoarderColor());
        preparedStatement.setString(6, boarder.getbSpecialInstructions());
        preparedStatement.setString(7, boarder.getBrdrOwnerName());
        preparedStatement.setString(8, boarder.getBrdrOwnerContact());
        preparedStatement.setString(9, boarder.getBrdrOwnerAddress());
        preparedStatement.setDate(10, java.sql.Date.valueOf(boarder.getBoardedDate()));
        preparedStatement.setBigDecimal(11, boarder.getBoarderAge());
        preparedStatement.setString(12, boarder.getBoarderGender());
        preparedStatement.setBigDecimal(13, boarder.getBoarderWeight());
        preparedStatement.setDate(14, java.sql.Date.valueOf(boarder.getDateDeparted()));
        preparedStatement.setString(15, boarder.getBrdrOwnerEmail());
    }
    
    
    private void setUpdateBoarderParameters(PreparedStatement preparedStatement, Boarder boarder) throws SQLException {
        preparedStatement.setString(1, boarder.getBoarderName());
        preparedStatement.setString(2, boarder.getBoarderSpecies());
        preparedStatement.setString(3, boarder.getBoarderBreed());
        preparedStatement.setBigDecimal(4, boarder.getBoarderAge());
        preparedStatement.setString(5, boarder.getBoarderColor());
        preparedStatement.setDate(6, java.sql.Date.valueOf(boarder.getBoardedDate()));
        preparedStatement.setString(7, boarder.getbSpecialInstructions());
        preparedStatement.setString(8, boarder.getBrdrOwnerName());
        preparedStatement.setString(9, boarder.getBrdrOwnerContact());
        preparedStatement.setString(10, boarder.getBrdrOwnerAddress());
        preparedStatement.setString(11, boarder.getBoarderGender());
        preparedStatement.setBigDecimal(12, boarder.getBoarderWeight());
        preparedStatement.setDate(13, java.sql.Date.valueOf(boarder.getDateDeparted()));
        preparedStatement.setString(14, boarder.getBrdrOwnerEmail());
    }
      
    
}
