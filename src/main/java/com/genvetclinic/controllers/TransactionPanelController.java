package com.genvetclinic.controllers;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.genvetclinic.models.Transaction;
import com.genvetclinic.services.*;
import com.genvetclinic.utils.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.scene.image.*;

/**
 * The {@code TransactionPanelController} class controls the transaction panel of the veterinary clinic application.
 * It handles the display of transaction data in a TableView and provides functionality to interact with transactions.
 *
 * <p>The controller uses JavaFX components, such as TableView and TableColumn, to present a list of transactions.
 * It interacts with the {@link com.genvetclinic.services.TransactionDao} class to retrieve and update transaction data.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class TransactionPanelController {

    private TransactionDao transactionDao;
    private DatabaseConnection databaseConnection;
    private Transaction lastSelectedTransaction;

    // FXML annotated fields for UI elements
    @FXML private TextField remuneratorField;
    @FXML private TextField beneficiaryField;
    @FXML private MenuButton transactionTypeMenu;
    @FXML private MenuButton paymentMethodMenu;
    @FXML private TextField amountReceivedField;
    @FXML private TextField transactionTotalField;
    @FXML private TextField transactionDescField;
    @FXML private Text transactionDateTimeText;
    @FXML private Text receiptNoText;
    @FXML private Text transacChangeText;
    @FXML private Button createTransacButton;
    @FXML private Button clearTransacButton;
    @FXML private Button deleteTransacButton;
    @FXML private Button saveTransacButton;
    @FXML private MenuButton transactionStatusMenu;
    @FXML private ImageView addTransIconViewer;
    @FXML private ImageView manageTransIconViewer;
    @FXML private TableView<Transaction> transactionTableView;
    @FXML private TableColumn<Transaction, String> transactionIdColumn;
    @FXML private TableColumn<Transaction, String> remuneratorColumn;
    @FXML private TableColumn<Transaction, String> beneficiaryColumn;
    @FXML private TableColumn<Transaction, String> transacTypeColumn;
    @FXML private TableColumn<Transaction, String> transacDescColumn;
    @FXML private TableColumn<Transaction, String> transacTotalColumn;
    @FXML private TableColumn<Transaction, String> amountReceivedColumn;
    @FXML private TableColumn<Transaction, String> paymentMethodColumn;
    @FXML private TableColumn<Transaction, String> receiptNoColumn;
    @FXML private TableColumn<Transaction, String> transacDateTimeColumn;
    @FXML private TableColumn<Transaction, String> transactionStatusColumn;
    @FXML private TableColumn<Transaction,BigDecimal> transactionChangeColumn;

    /**
     * Initializes the Patients Panel Controller.
     */
    public TransactionPanelController() {
        try {
            this.databaseConnection = new DatabaseConnection();
            this.transactionDao = new TransactionDao(databaseConnection);
            this.transactionDao = new TransactionDao();
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to establish a database connection.");
        }
    }

    /**
     * Initializes the GUI components and sets event handlers.
     */
    @FXML
    private void initialize() {

        initializeTransactionTable();
        populateTransactionTable();
        enableTransactionTableSelection();
        initializeTransTypeMenu();
        initializePaymentMethodMenu();
        initializeTransStatusMenu();
        initializeButtons();
        setupEventHandlers();
        setImageForIcons();
        addHoverEffect();

        EffectsUtils.setTableSelectionColor(transactionTableView, "#358856");  
    }

    /**
     * Initializes the buttons in the Java function.
     */
    private void initializeButtons() {
        deleteTransacButton.setDisable(true);
        saveTransacButton.setDisable(true);
    }

    /**
     * Sets up the event handlers for the admitPatientButton, clearPatientButton,
     * deletePatientButton, and savePatientButton.
     *
     * @param event the event object that triggered the action
     */    
    private void setupEventHandlers() {
        createTransacButton.setOnAction(event -> handleCreateTransaction());
        clearTransacButton.setOnAction(event -> clearTransactionFields());
        deleteTransacButton.setOnAction(event -> handleDeleteTransaction());
        saveTransacButton.setOnAction(event -> handleSaveTransaction());
    }

    /**
     * Initializes the transaction table by setting up the cell value factories for each column.
     */
    private void initializeTransactionTable() {
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        remuneratorColumn.setCellValueFactory(new PropertyValueFactory<>("payer"));
        beneficiaryColumn.setCellValueFactory(new PropertyValueFactory<>("payee"));
        transacTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transacDescColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDesc"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        receiptNoColumn.setCellValueFactory(new PropertyValueFactory<>("receiptNo"));
        transacDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("transacDateTime"));
        transactionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("transactionStatus"));

        transacTotalColumn.setCellValueFactory(cellData -> new SimpleStringProperty("₱" + cellData.getValue().getTransactionAmt().toString()));
        amountReceivedColumn.setCellValueFactory(cellData -> new SimpleStringProperty("₱" + cellData.getValue().getAmtReceived().toString()));
        transactionChangeColumn.setCellValueFactory(cellData ->
        new ReadOnlyObjectWrapper<>(cellData.getValue().getTransactionChange()));

        transactionChangeColumn.setCellFactory(column -> {
            TableCell<Transaction, BigDecimal> cell = new TableCell<>() {
                @Override
                protected void updateItem(BigDecimal item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText("₱" + item.toString());
                    }
                }
            };

            return cell;
        });
    }

    /**
      * Enables the selection of a transaction in the transaction table view.
      * Adds a listener to the selected item property of the transaction table view's selection model.
      * When a new selection is made, it calls the handleTableRowSelection method with the new selection.
      * Sets an on-click event handler for the transaction table view.
      * If the primary mouse button is clicked once, it checks if the selected transaction is the same as the last selected transaction.
      * If they are the same, it clears the selection, sets the last selected transaction to null, and clears the transaction fields.
      * If they are different, it sets the last selected transaction to the selected transaction.
      * If the primary mouse button is clicked twice, it calls the handleTableRowSelection method with the selected item.
      *
      */
    private void enableTransactionTableSelection() {
        transactionTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> handleTableRowSelection(newSelection));

        transactionTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();

                if (selectedTransaction != null && selectedTransaction.equals(lastSelectedTransaction)) {
                    transactionTableView.getSelectionModel().clearSelection();
                    lastSelectedTransaction = null;
                    clearTransactionFields();
                } else {
                    lastSelectedTransaction = selectedTransaction;
                }
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleTableRowSelection(transactionTableView.getSelectionModel().getSelectedItem());
            }
        });
    }

    /**
     * Handles the selection of a table row by performing the necessary actions based on the selected transaction.
     *
     * @param  selectedTransaction  the selected transaction from the table row
     */    
    private void handleTableRowSelection(Transaction selectedTransaction) {
        if (selectedTransaction != null) {
            lastSelectedTransaction = selectedTransaction;
            displayTransactionFields(selectedTransaction);
            deleteTransacButton.setDisable(false);
            saveTransacButton.setDisable(false);
        } else {
            clearTransactionFields();
            deleteTransacButton.setDisable(true);
            saveTransacButton.setDisable(true);
            lastSelectedTransaction = null;
        }
    } 

    /**
     * Sets the image for icons.
     */    
    private void setImageForIcons() {
        Image admitTransIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Add Transac Icon 2.png"));
        Image manageTransIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Manage Transac Icon.png"));
        addTransIconViewer.setImage(admitTransIcon);
        manageTransIconViewer.setImage(manageTransIcon);
    }

    /**
     * Handles the action when creating a transaction.
     */
    @FXML
    private void handleCreateTransaction() {
        try {
            // Retrieve input values from the form fields
            String payer = remuneratorField.getText();
            String payee = beneficiaryField.getText();
            String transactionType = transactionTypeMenu.getText();
            String paymentMethod = paymentMethodMenu.getText();
            String transactionStatus = transactionStatusMenu.getText();
            BigDecimal amountReceived = ParseUtils.parseBigDecimal(amountReceivedField.getText());
            BigDecimal totalAmount = ParseUtils.parseBigDecimal(transactionTotalField.getText());
            String transactionDesc = transactionDescField.getText();
            LocalDateTime transacDateTime = LocalDateTime.now();
    
            // Validate if required fields are not empty and contain valid numbers
            if (ValidationUtils.isNullOrEmpty(payer, payee, transactionType, paymentMethod, transactionDesc)
                    || amountReceived == null || totalAmount == null) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields with valid numbers. Write 'n/a' for inapplicable fields.");
                return;
            }
    
            // Validate number format for amountReceived and totalAmount
            if (!ValidationUtils.isValidNumber(amountReceived) || !ValidationUtils.isValidNumber(totalAmount)
                    || (amountReceived.compareTo(BigDecimal.ZERO) < 0) || (totalAmount.compareTo(BigDecimal.ZERO) <= 0)) {
                AlertUtils.showErrorAlert("Error", "Invalid amount. Please enter a valid number.");
                return;
            }
    
            // Generate unique receipt number and transaction ID
            int receiptNo = GenerateReceiptNo.generateRandomReceiptNo();
            String transactionId = GenerateRandomIds.generateRandomId(8);
    
            // Calculate transaction change and take absolute value
            BigDecimal transactionChange = totalAmount.subtract(amountReceived).abs().setScale(2, RoundingMode.HALF_UP);
    
            // Create a new Transaction object
            Transaction newTransaction = new Transaction(
                    transactionId,
                    payer, payee, transactionType, transactionDesc,
                    totalAmount, amountReceived, paymentMethod, String.valueOf(receiptNo), transacDateTime,
                    transactionChange, transactionStatus
            );
    
            // Add the new transaction to the table view
            transactionTableView.getItems().add(newTransaction);
    
            // Save the transaction and update the transaction status
            transactionDao.saveTransaction(newTransaction, transactionStatus);
    
            // Clear form fields
            clearTransactionFields();
    
            // Show success message
            AlertUtils.showInformationAlert("Success", "Transaction added successfully.");
    
        } catch (Exception e) {
            // Handle unexpected errors
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }    

    /**
     * Handles the action when editing a transaction.
     */
    @FXML
    private void handleSaveTransaction() {
        try {
            // Get the selected transaction from the table view
            Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
    
            // Retrieve updated values from the form fields
            String updatedPayer = remuneratorField.getText();
            String updatedPayee = beneficiaryField.getText();
            String updatedTransactionType = transactionTypeMenu.getText();
            String updatedPaymentMethod = paymentMethodMenu.getText();
            String updatedTransactionStatus = transactionStatusMenu.getText();
            BigDecimal updatedAmountReceived = ParseUtils.parseBigDecimal(amountReceivedField.getText());
            BigDecimal updatedTotalAmount = ParseUtils.parseBigDecimal(transactionTotalField.getText());
            String updatedTransactionDesc = transactionDescField.getText();
            LocalDateTime updatedTransactionDateTime = LocalDateTime.now();
    
            // Validate if required fields are not empty and contain valid numbers
            if (ValidationUtils.isNullOrEmpty(
                    updatedPayer, updatedPayee, updatedTransactionType, updatedPaymentMethod, updatedTransactionDesc)
                    || updatedAmountReceived == null || updatedTotalAmount == null
                    || !ValidationUtils.isValidNumber(updatedAmountReceived) || !ValidationUtils.isValidNumber(updatedTotalAmount)
                    || updatedAmountReceived.compareTo(BigDecimal.ZERO) < 0 || updatedTotalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields with valid numbers. Write 'n/a' for inapplicable fields.");
                return;
            }
    
            // Validate number format for updated amountReceived and totalAmount
            if (!ValidationUtils.isValidNumber(updatedAmountReceived) || !ValidationUtils.isValidNumber(updatedTotalAmount)
                    || updatedAmountReceived.compareTo(BigDecimal.ZERO) < 0 || updatedTotalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                AlertUtils.showErrorAlert("Error", "Invalid amount. Please enter a valid number.");
                return;
            }
    
            // Check if any changes were made
            if (!Objects.equals(selectedTransaction.getPayer(), updatedPayer)
                    || !Objects.equals(selectedTransaction.getPayee(), updatedPayee)
                    || !Objects.equals(selectedTransaction.getTransactionType(), updatedTransactionType)
                    || !Objects.equals(selectedTransaction.getPaymentMethod(), updatedPaymentMethod)
                    || !Objects.equals(selectedTransaction.getAmtReceived(), updatedAmountReceived)
                    || !Objects.equals(selectedTransaction.getTransactionAmt(), updatedTotalAmount)
                    || !Objects.equals(selectedTransaction.getTransactionDesc(), updatedTransactionDesc)) {
    
                // Calculate updated transaction change
                BigDecimal updatedTransactionChange = updatedTotalAmount.subtract(updatedAmountReceived).abs().setScale(2, RoundingMode.HALF_UP);
    
                // Create an updated Transaction object
                Transaction updatedTransaction = new Transaction(
                        selectedTransaction.getTransactionId(),
                        updatedPayer, updatedPayee, updatedTransactionType, updatedTransactionDesc,
                        updatedTotalAmount, updatedAmountReceived, updatedPaymentMethod,
                        selectedTransaction.getReceiptNo(),
                        updatedTransactionDateTime, updatedTransactionChange, updatedTransactionStatus
                );
    
                // Update the transaction in the table view, refresh the table, and show success message
                transactionTableView.getItems().set(transactionTableView.getSelectionModel().getSelectedIndex(), updatedTransaction);
                transactionDao.updateTransaction(updatedTransaction, updatedTransactionStatus);
                refreshTransactionTable();
                AlertUtils.showInformationAlert("Success", "Transaction updated successfully.");
    
            } else {
                // No changes were made
                AlertUtils.showInformationAlert("Info", "No changes were made.");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }
    
    /**
     * Handles the action when deleting a transaction.
     */
    @FXML
    private void handleDeleteTransaction() {
        Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();

        boolean confirmed = AlertUtils.showConfirmationPrompt("Are you sure you want to delete this transaction?");

        if (confirmed) {
            try {
                transactionTableView.getItems().remove(selectedTransaction);
                transactionDao.deleteTransaction(selectedTransaction);
                clearTransactionFields();
                AlertUtils.showInformationAlert("Success", "Transaction deleted successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
            }
        }
    }   

    /**
     * Displays the transaction fields on the screen.
     *
     * @param  transaction  the transaction object containing the field values
     */    
    private void displayTransactionFields(Transaction transaction) {
        remuneratorField.setText(transaction.getPayer());
        beneficiaryField.setText(transaction.getPayee());
        transactionTypeMenu.setText(transaction.getTransactionType());
        paymentMethodMenu.setText(transaction.getPaymentMethod());
        amountReceivedField.setText(transaction.getAmtReceived().toString());
        transactionTotalField.setText(transaction.getTransactionAmt().toString());
        transactionDescField.setText(transaction.getTransactionDesc());
        receiptNoText.setText(transaction.getReceiptNo());
    
        for (MenuItem item : transactionStatusMenu.getItems()) {
            if (item.getText().equals(transaction.getTransactionStatus())) {
                transactionStatusMenu.setText(item.getText());
                break;
            }
        }
    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = transaction.getTransacDateTime().format(formatter);
        transactionDateTimeText.setText(formattedDateTime);
        transacChangeText.setText(transaction.getTransactionChange().toString());
    }

    /**
     * Populates the transaction table with data from the database.
     */
    private void populateTransactionTable() {
        try {
            ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM transaction");
                 ResultSet resultSet = statement.executeQuery()) {
    
                while (resultSet.next()) {
                    Transaction transaction = new Transaction(
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
    
                    transactions.add(transaction);
                }
            }
    
            if (transactions.isEmpty()) {
                transactionTableView.setPlaceholder(new Label("No transactions found."));
            } else {
                transactionTableView.setItems(transactions);
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Error", "Unable to fetch transactions. Please check your database connection.");
        } catch (Exception e) {
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }
    
    /**
     * Clears all transaction fields, resetting them to their default values.
     */
    private void clearTransactionFields() {
        remuneratorField.clear();
        beneficiaryField.clear();
        transactionTypeMenu.setText("Transaction Type");
        paymentMethodMenu.setText("Payment Method");
        amountReceivedField.clear();
        transactionTotalField.clear();
        transactionDescField.setText("");
        receiptNoText.setText("");
        transactionDateTimeText.setText("");
        transactionStatusMenu.setText("Transaction Status");
        transacChangeText.setText("");
    }

    /**
     * Refreshes the transaction table by clearing the items and populating it again.
     */
    private void refreshTransactionTable() {
        transactionTableView.getItems().clear();
        populateTransactionTable();
    }

    /**
     * Initializes the transaction type menu.
     */
    private void initializeTransTypeMenu() {
        transactionTypeMenu.getItems().clear();
        transactionTypeMenu.getItems().addAll(
                new MenuItem("Donations/Fundraising"),
                new MenuItem("Preventive Care Packages"),
                new MenuItem("Online/Phone Consultations"),
                new MenuItem("Payment Plans"),
                new MenuItem("Boarding and Grooming Fees"),
                new MenuItem("Emergency Care Charges"),
                new MenuItem("Service Fees"),
                new MenuItem("Product Sales"),
                new MenuItem("Consultation Fees"),
                new MenuItem("Diagnostic Services"),
                new MenuItem("Follow-up Appointments"),
                new MenuItem("Insurance Claims"),
                new MenuItem("Collaborations"),
                new MenuItem("In-House Pharmacy"),
                new MenuItem("Licensing/Regulatory Fees")
        );
        transactionTypeMenu.getItems().forEach(item -> item.setOnAction(event -> handleTransactionTypeMenuItem(item.getText())));
    }

    /**
     * Sets the text of the transaction type menu item.
     *
     * @param  transactionType  the transaction type to be set as the menu item text
     */    
    @FXML
    private void handleTransactionTypeMenuItem(String transactionType) {
        transactionTypeMenu.setText(transactionType);
    }

    /**
     * Initializes the payment method menu.
     */
    private void initializePaymentMethodMenu() {
        paymentMethodMenu.getItems().clear();
        paymentMethodMenu.getItems().addAll(
                new MenuItem("Bank Transfer"),
                new MenuItem("Online Payment"),
                new MenuItem("Check"),
                new MenuItem("Cash"),
                new MenuItem("Credit"),
                new MenuItem("Mobile Payments"),
                new MenuItem("Insurance"),
                new MenuItem("Third Party Service")
        );
        paymentMethodMenu.getItems().forEach(item -> item.setOnAction(event -> handlePaymentMethodMenuItem(item.getText())));
    }

    /**
     * Sets the text of the paymentMethodMenu to the specified payment method.
     *
     * @param  paymentMethod  the payment method to be set
     */
    @FXML
    private void handlePaymentMethodMenuItem(String paymentMethod) {
        paymentMethodMenu.setText(paymentMethod);
    }

    /**
     * Initializes the transaction status menu with a list of options.
     */
    private void initializeTransStatusMenu() {
        transactionStatusMenu.getItems().clear();
        transactionStatusMenu.getItems().addAll(
                new MenuItem("Canceled"),
                new MenuItem("Pending"),
                new MenuItem("Authorized"),
                new MenuItem("Captured"),
                new MenuItem("Failed"),
                new MenuItem("Refunded"),
                new MenuItem("Chargeback"),
                new MenuItem("Reversed"),
                new MenuItem("On Hold"),
                new MenuItem("Expired")
        );
        transactionStatusMenu.getItems().forEach(item -> item.setOnAction(event -> handleTransactionStatusMenuItem(item.getText())));
    }

    /**
     * Handles the selection of a transaction status menu item.
     *
     * @param  transactionStatus  the transaction status to be displayed
     */    
    @FXML
    private void handleTransactionStatusMenuItem(String transactionStatus) {
        transactionStatusMenu.setText(transactionStatus);
    }

    /**
     * Adds a hover effect to the buttons in the UI.
     *
     * @param  clearTransacButton  the button for clearing transactions
     * @param  saveTransacButton   the button for saving transactions
     * @param  deleteTransacButton the button for deleting transactions
     * @param  createTransacButton the button for creating transactions
     */
    private void addHoverEffect() {
        EffectsUtils.addHoverEffect(clearTransacButton);
        EffectsUtils.addHoverEffect(saveTransacButton);
        EffectsUtils.addHoverEffect(deleteTransacButton);
        EffectsUtils.addHoverEffect(createTransacButton);
    }

}
