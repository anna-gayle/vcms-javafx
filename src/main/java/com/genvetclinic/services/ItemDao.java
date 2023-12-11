package com.genvetclinic.services;

import com.genvetclinic.models.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ItemDao} class provides data access methods for managing inventory items
 * in the veterinary clinic system. It includes operations for saving, updating, and deleting
 * item records, as well as querying item information.
 *
 * <p>This class interacts with the underlying database through the {@link DatabaseConnection}
 * class to perform database operations.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class ItemDao {

    /**
     * The {@link DatabaseConnection} instance used to establish a connection to the database.
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Constructs a new {@code ItemDao} instance with the default database connection.
     *
     * @throws SQLException if a database access error occurs.
     */
    public ItemDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

    /**
     * Constructs a new {@code ItemDao} instance with the specified database connection.
     *
     * @param databaseConnection the {@link DatabaseConnection} instance to use.
     */
    public ItemDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

     // Methods for saving, updating, retrieving, and deleting item records...
    public void saveItem(Item item) throws SQLException {
        String sql = "INSERT INTO inventory (item_id, item_name, item_type, item_quantity, unit_cost, " +
                     "item_supplier, exp_date, item_status, total_cost) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, item.getItemId());
            statement.setString(2, item.getItemName());
            statement.setString(3, item.getItemType());
            statement.setInt(4, item.getItemQuantity());
            statement.setBigDecimal(5, item.getUnitCost());
            statement.setString(6, item.getSupplier());

            if (item.getExpDate() != null) {
                statement.setDate(7, java.sql.Date.valueOf(item.getExpDate()));
            } else {
                statement.setNull(7, java.sql.Types.DATE);
            }

            statement.setString(8, item.getItemStatus());
            statement.setBigDecimal(9, item.getTotalCost());

            statement.executeUpdate();
        }
    }

    public void updateItem(Item item) throws SQLException {
        String sql = "UPDATE inventory SET item_name = ?, item_type = ?, item_quantity = ?, " +
                     "unit_cost = ?, item_supplier = ?, exp_date = ?, item_status = ?, total_cost = ? " +
                     "WHERE item_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, item.getItemName());
            statement.setString(2, item.getItemType());
            statement.setInt(3, item.getItemQuantity());
            statement.setBigDecimal(4, item.getUnitCost());
            statement.setString(5, item.getSupplier());

            if (item.getExpDate() != null) {
                statement.setDate(6, java.sql.Date.valueOf(item.getExpDate()));
            } else {
                statement.setNull(6, java.sql.Types.DATE);
            }

            statement.setString(7, item.getItemStatus());
            statement.setBigDecimal(8, item.getTotalCost());
            statement.setString(9, item.getItemId());

            statement.executeUpdate();
        }
    }

    public void deleteItem(Item item) {
        String sql = "DELETE FROM inventory WHERE item_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, item.getItemId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if an item with the specified details already exists in the database.
     *
     * @param itemName   the name of the item.
     * @param itemType   the type of the item.
     * @param supplier   the supplier of the item.
     * @param expDate    the expiration date of the item.
     * @return {@code true} if the item exists, {@code false} otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean isItemExists(String itemName, String itemType, String supplier, LocalDate expDate) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inventory WHERE item_name = ? AND item_type = ? AND item_supplier = ? AND exp_date = ?";
        try (Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, itemName);
            preparedStatement.setString(2, itemType);
            preparedStatement.setString(3, supplier);
            
            if (expDate != null) {
                preparedStatement.setDate(4, java.sql.Date.valueOf(expDate));
            } else {
                preparedStatement.setNull(4, java.sql.Types.DATE);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }

   /**
     * Retrieves a list of all items in the inventory.
     *
     * @return a list of {@link Item} objects representing inventory items.
     * @throws SQLException if a database access error occurs.
     */
    public List<Item> getItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                items.add(mapResultSetToItem(resultSet));
            }
        }
        return items;
    }
    
    /**
     * Retrieves a notification message for items that need attention.
     *
     * @return a notification message indicating the number of items that need attention.
     * @throws SQLException if a database access error occurs.
     */
    public String getNotificationTextForItems() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inventory WHERE item_status <> 'In Stock'";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            int itemCount = resultSet.getInt(1);

            if (itemCount > 0) {
                return itemCount + " item(s)/unit(s) \n that need your attention.";
            } else {
                return "No new notifications.";
            }
        }
    }

    // Private methods for setting parameters during database operations...
    private Item mapResultSetToItem(ResultSet resultSet) throws SQLException {
        return new Item(
                resultSet.getString("item_id"),
                resultSet.getString("item_name"),
                resultSet.getString("item_type"),
                resultSet.getInt("item_quantity"),
                resultSet.getBigDecimal("unit_cost"),
                resultSet.getString("item_supplier"),
                resultSet.getDate("exp_date").toLocalDate(),
                resultSet.getString("item_status"),
                resultSet.getBigDecimal("total_cost")
        );
    }
}
