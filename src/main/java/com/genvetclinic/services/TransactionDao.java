package com.genvetclinic.services;

import com.genvetclinic.models.Transaction;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code TransactionDao} class provides data access methods for managing Transaction entities
 * in the veterinary clinic system. It includes methods for saving, updating, deleting,
 * and retrieving transaction information from the database.
 *
 * <p>This class utilizes a {@link DatabaseConnection} to establish a connection to the database
 * and execute SQL queries for interacting with the transaction data.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class TransactionDao {

    /**
     * The database connection used by this DAO.
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Constructs a new {@code TransactionDao} instance with a default database connection.
     *
     * @throws SQLException if a database access error occurs.
     */
    public TransactionDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

    /**
     * Constructs a new {@code TransactionDao} instance with the specified database connection.
     *
     * @param databaseConnection the database connection to be used by this DAO.
     */

    public TransactionDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Methods for saving, updating, deleting, and retrieving transaction information...

    public void saveTransaction(Transaction transaction, String transactionStatus) throws SQLException {
        String sql = "INSERT INTO transaction (transaction_id, payer, payee, transaction_type, transaction_desc, " +
                     "transaction_amt, amt_received, payment_method, receipt_no, transac_datetime, transaction_change, transaction_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setSaveTransactionParameters(preparedStatement, transaction, transactionStatus);
            preparedStatement.executeUpdate();
        }
    }

    public void updateTransaction(Transaction transaction, String transactionStatus) throws SQLException {
        String sql = "UPDATE transaction SET payer = ?, payee = ?, transaction_type = ?, " +
             "transaction_desc = ?, transaction_amt = ?, amt_received = ?, " +
             "payment_method = ?, receipt_no = ?, transac_datetime = ?, transaction_change = ?, transaction_status = ? " +
             "WHERE transaction_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setUpdateTransactionParameters(preparedStatement, transaction, transactionStatus);
            preparedStatement.setString(12, transaction.getTransactionId()); 
            preparedStatement.executeUpdate();
        }
    }    

    public void deleteTransaction(Transaction transaction) throws SQLException {
        String sql = "DELETE FROM transaction WHERE transaction_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, transaction.getTransactionId());
            preparedStatement.executeUpdate();
        }
    }

    public List<Transaction> getTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                transactions.add(mapResultSetToTransaction(resultSet));
            }
        }
        return transactions;
    }

    public boolean isTransactionExists(String transactionId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM transaction WHERE transaction_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, transactionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }

    public BigDecimal getTotalEarnings() throws SQLException {
        String sql = "SELECT SUM(transaction_amt) FROM transaction WHERE transaction_status = 'Captured'";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getBigDecimal(1);
        }
    }

    public int countTransactionsNeedingAttention() throws SQLException {
        String sql = "SELECT COUNT(*) FROM transaction WHERE transaction_status NOT IN ('Authorized', 'Captured')";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
    
    public String getNotificationTextForTransactions() throws SQLException {
        int transactionsNeedingAttentionCount = countTransactionsNeedingAttention();
    
        if (transactionsNeedingAttentionCount > 0) {
            return transactionsNeedingAttentionCount + " transaction(s) \n that need your attention.";
        } else {
            return "No new notifications.";
        }
    }
    

    private void setSaveTransactionParameters(PreparedStatement preparedStatement, Transaction transaction, String transactionStatus) throws SQLException {
        preparedStatement.setString(1, transaction.getTransactionId());
        preparedStatement.setString(2, transaction.getPayer());
        preparedStatement.setString(3, transaction.getPayee());
        preparedStatement.setString(4, transaction.getTransactionType());
        preparedStatement.setString(5, transaction.getTransactionDesc());
        preparedStatement.setBigDecimal(6, transaction.getTransactionAmt());
        preparedStatement.setBigDecimal(7, transaction.getAmtReceived());
        preparedStatement.setString(8, transaction.getPaymentMethod());
        preparedStatement.setString(9, transaction.getReceiptNo());
        preparedStatement.setTimestamp(10, java.sql.Timestamp.valueOf(transaction.getTransacDateTime()));
        preparedStatement.setBigDecimal(11, BigDecimal.ZERO);
        preparedStatement.setString(12, transactionStatus);
    }
    
    private void setUpdateTransactionParameters(PreparedStatement preparedStatement, Transaction transaction, String transactionStatus) throws SQLException {
        preparedStatement.setString(1, transaction.getPayer());
        preparedStatement.setString(2, transaction.getPayee());
        preparedStatement.setString(3, transaction.getTransactionType());
        preparedStatement.setString(4, transaction.getTransactionDesc());
        preparedStatement.setBigDecimal(5, transaction.getTransactionAmt());
        preparedStatement.setBigDecimal(6, transaction.getAmtReceived());
        preparedStatement.setString(7, transaction.getPaymentMethod());
        preparedStatement.setString(8, transaction.getReceiptNo());
        preparedStatement.setTimestamp(9, java.sql.Timestamp.valueOf(transaction.getTransacDateTime()));
        preparedStatement.setBigDecimal(10, transaction.getTransactionChange());
        preparedStatement.setString(11, transactionStatus); 
    }    
    
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        return new Transaction(
                resultSet.getString("transaction_id"),
                resultSet.getString("payer"),
                resultSet.getString("payee"),
                resultSet.getString("transaction_type"),
                resultSet.getString("transaction_desc"),
                resultSet.getBigDecimal("transaction_amt"),
                resultSet.getBigDecimal("amt_received"),
                resultSet.getString("payment_method"),
                resultSet.getString("receipt_no"),
                resultSet.getTimestamp("transac_datetime").toLocalDateTime(),
                resultSet.getBigDecimal("transaction_change"),
                resultSet.getString("transaction_status")
        );
    }
}
