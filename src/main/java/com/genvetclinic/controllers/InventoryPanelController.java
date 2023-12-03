package com.genvetclinic.controllers;

import java.sql.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.genvetclinic.models.Item;
import com.genvetclinic.services.*;
import com.genvetclinic.utils.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

/**
 * The {@code InventoryPanelController} class controls the Inventory Panel of the veterinary clinic application.
 * It manages the display and interaction with the inventory items data.
 *
 * <p>This controller utilizes JavaFX components, such as TableView and TableColumn, to present a list of inventory items.
 * It interacts with the {@link com.genvetclinic.services.ItemDao} class to retrieve and update inventory data from the database.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class InventoryPanelController {

    private ItemDao itemDao;
    private DatabaseConnection databaseConnection;
    private Item lastSelectedItem;

    // FXML injection for various UI elements...
    @FXML private TextField itemNameField;
    @FXML private MenuButton itemTypeMenu;
    @FXML private TextField itemQuantityField;
    @FXML private TextField unitCostField;
    @FXML private TextField itemSupplierField;
    @FXML private DatePicker expDateField;
    @FXML private MenuButton itemStatusMenu;
    @FXML private TextField totalCostField;
    @FXML private Button addItemButton;
    @FXML private Button clearItemButton;
    @FXML private ImageView addItemIconViewer;
    @FXML private ImageView manageItemIconViewer;
    @FXML private TableView<Item> itemTableView;
    @FXML private TableColumn<Item, String> itemIdColumn;
    @FXML private TableColumn<Item, String> itemNameColumn;
    @FXML private TableColumn<Item, String> itemTypeColumn;
    @FXML private TableColumn<Item, String> itemQuantityColumn;
    @FXML private TableColumn<Item, String> unitCostColumn;
    @FXML private TableColumn<Item, String> itemSupplierColumn;
    @FXML private TableColumn<Item, String> expDateColumn;
    @FXML private TableColumn<Item, String> itemStatusColumn;
    @FXML private TableColumn<Item, String> totalCostColumn;
    @FXML private Button deleteItemButton;
    @FXML private Button saveItemButton;
    @FXML private CheckBox hasAnExpDateCB;

    /**
     * Initializes the Inventory Panel Controller.
     */
    public InventoryPanelController() {
        try {
            this.databaseConnection = new DatabaseConnection();
            this.itemDao = new ItemDao(databaseConnection);
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
        
        initializeItemTable();
        populateItemTable();
        enableItemTableSelection();
        initializeItemTypeMenu();
        initializeItemStatusMenu();
        initializeButtons();
        setupEventHandlers();
        setImageForIcons();
        addHoverEffect(); 
        checkboxFunctionality();

        EffectsUtils.setTableSelectionColor(itemTableView, "#358856");         
    }

    /**
     * Initializes the buttons in the Java function.
     */
    private void initializeButtons() {
        deleteItemButton.setDisable(true);
        saveItemButton.setDisable(true);
    }

    /**
     * Sets up the event handlers for the admitItemButton, clearItemButton,
     * deleteItemButton, and saveItemButton.
     *
     * @param event the event object that triggered the action
     */
    private void setupEventHandlers() {
        addItemButton.setOnAction(event -> handleCreateItem());
        clearItemButton.setOnAction(event -> clearItemFields());
        deleteItemButton.setOnAction(event -> handleDeleteItem());
        saveItemButton.setOnAction(event -> handleSaveItem());

    }

    /**
     * Initializes the inventory table by setting up the cell value factories for each column.
     */
    private void initializeItemTable() {
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        itemSupplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        expDateColumn.setCellValueFactory(new PropertyValueFactory<>("expDate"));
        itemStatusColumn.setCellValueFactory(new PropertyValueFactory<>("itemStatus"));   
        unitCostColumn.setCellValueFactory(cellData -> new SimpleStringProperty("₱" + cellData.getValue().getUnitCost().toString()));
        totalCostColumn.setCellValueFactory(cellData -> new SimpleStringProperty("₱" + cellData.getValue().getTotalCost().toString()));     
    }

    /**
     * Enables the selection of an item in the item table view.
     * Adds a listener to the selected item property of the item table view's selection model.
     * When a new selection is made, it calls the handleTableRowSelection method with the new selection.
     * Sets an on-click event handler for the item table view.
     * If the primary mouse button is clicked once, it checks if the selected item is the same as the last selected item.
     * If they are the same, it clears the selection, sets the last selected item to null, and clears the item fields.
     * If they are different, it sets the last selected item to the selected item.
     * If the primary mouse button is clicked twice, it calls the handleTableRowSelection method with the selected item.
     *
     */
    private void enableItemTableSelection() {
        itemTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> handleTableRowSelection(newSelection));

        itemTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();

                if (selectedItem != null && selectedItem.equals(lastSelectedItem)) {
                    itemTableView.getSelectionModel().clearSelection();
                    lastSelectedItem = null;
                    clearItemFields();
                } else {
                    lastSelectedItem = selectedItem;
                }
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleTableRowSelection(itemTableView.getSelectionModel().getSelectedItem());
            }
        });
     }

    /**
     * Configures functionality for a checkbox that controls the state of an expiration date field.
     * When the checkbox is selected, the associated expiration date field is disabled, and vice versa.
     * 
     * @implNote This method is designed to be used as an event listener for a JavaFX checkbox.
     * 
     * @see javafx.scene.control.CheckBox
     * @see javafx.scene.control.TextField
     */
    private void checkboxFunctionality() {
        hasAnExpDateCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            expDateField.setDisable(newValue);
        });
    }

    /**
     * Handles the selection of a table row.
     *
     * @param  selectedItem the item that was selected
     */    
    private void handleTableRowSelection(Item selectedItem) {
        if (selectedItem != null) {
            lastSelectedItem = selectedItem;
            displayItemFields(selectedItem);
            deleteItemButton.setDisable(false);
            saveItemButton.setDisable(false);
        } else {
            clearItemFields();
            deleteItemButton.setDisable(true);
            saveItemButton.setDisable(true);
            lastSelectedItem = null;
        }
    }

    /**
     * Sets the image for icons.
     */
    private void setImageForIcons() {
        Image addItemIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Add Item Icon.png"));
        Image manageItemIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Manage Item Icon.png"));
        addItemIconViewer.setImage(addItemIcon);
        manageItemIconViewer.setImage(manageItemIcon);
    }

    @FXML
    private void handleCreateItem() {
        try {
            // Retrieve input values from the form fields
            String itemName = itemNameField.getText();
            String itemType = itemTypeMenu.getText();
            Integer itemQuantity = ParseUtils.parseInteger(itemQuantityField.getText());
            BigDecimal unitCost = ParseUtils.parseBigDecimal(unitCostField.getText());
            String itemSupplier = itemSupplierField.getText();
            // If 'Has an Expiration Date' checkbox is selected, set expiration date to null; otherwise, parse the date
            LocalDate expDate = hasAnExpDateCB.isSelected() ? null : ParseUtils.parseLocalDate(expDateField.getValue().toString());
            String itemStatus = itemStatusMenu.getText();

            // Check if any required fields are null or empty
            if (ValidationUtils.isNullOrEmpty(itemName, itemType, itemSupplier, itemStatus) || (expDate == null && !hasAnExpDateCB.isSelected())) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Check if quantity or unit cost are invalid
            if (itemQuantity == null || unitCost == null) {
                AlertUtils.showErrorAlert("Error", "Invalid quantity or unit cost. Please enter valid numbers.");
                return;
            }

            // Calculate the total cost of the item
            BigDecimal totalCost = unitCost.multiply(BigDecimal.valueOf(itemQuantity));

            // Check if the expiration date is in the past
            if (!hasAnExpDateCB.isSelected() && (expDate.isEqual(LocalDate.now()) || expDate.isBefore(LocalDate.now()))) {
                boolean confirm = AlertUtils.showConfirmationPrompt(
                        "The expiration date is today or in the past. Do you still want to add this item?"
                );

                if (!confirm) {
                    return;
                }
            }

            // Validate the date fields
            if (!ValidationUtils.isValidDateFormat(expDate != null ? expDate.toString() : null) && !hasAnExpDateCB.isSelected()) {
                AlertUtils.showErrorAlert("Error", "Invalid date format for expiration date. Please use the correct format.");
                return;
            }

            // Check if the item already exists in the database
            if (itemDao.isItemExists(itemName, itemType, itemSupplier, expDate)) {
                AlertUtils.showErrorAlert("Error", "An item with the same name, type, supplier, and expiration date already exists.");
                return;
            }

            // Create a new Item object with generated ID and set its properties
            Item newItem = new Item(
                    GenerateRandomIds.generateRandomId(10),
                    itemName, itemType, itemQuantity, unitCost, itemSupplier, expDate, itemStatus, totalCost
            );

            // Add the new item to the table view
            itemTableView.getItems().add(newItem);

            // Save the new item to the database, clear form fields, and show success message
            itemDao.saveItem(newItem);
            clearItemFields();
            AlertUtils.showInformationAlert("Success", "Item added to inventory successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }

    @FXML
    private void handleSaveItem() {
        // Retrieve the currently selected item from the table view
        Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();

        try {
            // Get updated values from the form fields
            String updatedItemName = itemNameField.getText();
            String updatedItemType = itemTypeMenu.getText();
            Integer updatedItemQuantity = ParseUtils.parseInteger(itemQuantityField.getText());
            BigDecimal updatedUnitCost = ParseUtils.parseBigDecimal(unitCostField.getText());
            String updatedItemSupplier = itemSupplierField.getText();
            // If 'Has an Expiration Date' checkbox is selected, set expiration date to null; otherwise, parse the date
            LocalDate updatedExpDate = hasAnExpDateCB.isSelected() ? null : expDateField.getValue();
            String updatedItemStatus = itemStatusMenu.getText();

            // Check if any required fields are null or empty
            if (ValidationUtils.isNullOrEmpty(updatedItemName, updatedItemType, updatedItemSupplier, updatedItemStatus) || (updatedExpDate == null && !hasAnExpDateCB.isSelected())) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }
            // Check if quantity or unit cost are invalid
            if (updatedItemQuantity == null || updatedUnitCost == null) {
                AlertUtils.showErrorAlert("Error", "Invalid quantity or unit cost. Please enter valid numbers.");
                return;
            }

            // Calculate the total cost of the updated item
            BigDecimal updatedTotalCost = updatedUnitCost.multiply(BigDecimal.valueOf(updatedItemQuantity));

            // Check if the expiration date is in the past
            if (!hasAnExpDateCB.isSelected() && (updatedExpDate.isEqual(LocalDate.now()) || updatedExpDate.isBefore(LocalDate.now()))) {
                boolean confirm = AlertUtils.showConfirmationPrompt(
                        "The expiration date is today or in the past. Do you still want to update this item?"
                );

                if (!confirm) {
                    return;
                }
            }

            // Validate the date fields
            if (!ValidationUtils.isValidDateFormat(updatedExpDate != null ? updatedExpDate.toString() : null) && !hasAnExpDateCB.isSelected()) {
                AlertUtils.showErrorAlert("Error", "Invalid date format for expiration date. Please use the correct format.");
                return;
            }

            // Create a new Item object with updated values
            Item updatedItem = new Item(
                    selectedItem.getItemId(),
                    updatedItemName, updatedItemType, updatedItemQuantity, updatedUnitCost,
                    updatedItemSupplier, updatedExpDate, updatedItemStatus, updatedTotalCost
            );

            // Update the item in the table view and refresh the item table
            itemTableView.getItems().set(itemTableView.getSelectionModel().getSelectedIndex(), updatedItem);
            itemDao.updateItem(updatedItem);
            refreshItemTable();
            AlertUtils.showInformationAlert("Success", "Item updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }

    @FXML
    private void handleDeleteItem() {
        Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();

        boolean confirmed = AlertUtils.showConfirmationPrompt("Are you sure you want to delete this item?");

        if (confirmed) {
            try {
                itemTableView.getItems().remove(selectedItem);
                itemDao.deleteItem(selectedItem);
                clearItemFields();
                AlertUtils.showInformationAlert("Success", "Item deleted successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
            }
        }
    }

    private void initializeItemTypeMenu() {
        itemTypeMenu.getItems().clear();
        itemTypeMenu.getItems().addAll(
            new MenuItem("Medical Supplies"),
            new MenuItem("Medication"),
            new MenuItem("Food and Water"),
            new MenuItem("Cleaning Supplies"),
            new MenuItem("Laboratory Supplies"),
            new MenuItem("Office Supplies"),
            new MenuItem("Equipment"),
            new MenuItem("Animal Care Items")
        );
        itemTypeMenu.getItems().forEach(item -> item.setOnAction(event -> handleItemTypeMenuItem(item.getText())));

    }

    @FXML
    private void handleItemTypeMenuItem(String selectedType) {
        itemTypeMenu.setText(selectedType);
    }

    private void initializeItemStatusMenu() {
        itemStatusMenu.getItems().clear();
        itemStatusMenu.getItems().addAll(
                new MenuItem("Out of Stock"),
                new MenuItem("In Stock"),
                new MenuItem("On Order"),
                new MenuItem("Reserved"),
                new MenuItem("Damaged/Faulty"),
                new MenuItem("In transit"),
                new MenuItem("Low Stock"),
                new MenuItem("Under Inspection"),
                new MenuItem("Discontinued"),
                new MenuItem("Returned"),
                new MenuItem("Sold/Issued"),
                new MenuItem("Scrapped/Obsolete")
        );
        itemStatusMenu.getItems().forEach(item -> item.setOnAction(event -> handleItemStatusMenuItem(item.getText())));
    }

    @FXML
    private void handleItemStatusMenuItem(String selectedStatus) {
        itemStatusMenu.setText(selectedStatus);
    }

    private void populateItemTable() {
        try {
            ObservableList<Item> items = FXCollections.observableArrayList();
    
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM inventory");
                 ResultSet resultSet = statement.executeQuery()) {
    
                while (resultSet.next()) {
                    Item item = new Item(
                        resultSet.getString("item_id"),
                        resultSet.getString("item_name"),
                        resultSet.getString("item_type"),
                        resultSet.getInt("item_quantity"),
                        resultSet.getBigDecimal("unit_cost"),
                        resultSet.getString("item_supplier"),
                        resultSet.getDate("exp_date") != null ? resultSet.getDate("exp_date").toLocalDate() : null,
                        resultSet.getString("item_status"),
                        resultSet.getBigDecimal("total_cost")
                    );
                    
                    items.add(item);
                }
                    
            }
    
            if (items.isEmpty()) {
                itemTableView.setPlaceholder(new Label("No items found in the inventory."));
            } else {
                itemTableView.setItems(items);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to fetch items from the inventory.");
        }
    }    

    private void displayItemFields(Item item) {
        itemNameField.setText(item.getItemName());
        itemTypeMenu.setText(item.getItemType());
        itemQuantityField.setText(String.valueOf(item.getItemQuantity()));
        unitCostField.setText(String.valueOf(item.getUnitCost()));
        itemSupplierField.setText(item.getSupplier());
        expDateField.setValue(item.getExpDate());
        itemStatusMenu.setText(item.getItemStatus());

        if (item.getExpDate() != null) {
            hasAnExpDateCB.setSelected(true);
        } else {
            hasAnExpDateCB.setSelected(false);
        }
    }

    private void clearItemFields() {
        itemNameField.clear();
        itemTypeMenu.setText("Category");
        itemQuantityField.clear();
        unitCostField.clear();
        itemSupplierField.clear();
        expDateField.setValue(null);
        itemStatusMenu.setText("Select Status");
        itemTableView.getSelectionModel().clearSelection();
        hasAnExpDateCB.setSelected(false);
        lastSelectedItem = null;
    }

    private void refreshItemTable() {
        itemTableView.getItems().clear();
        populateItemTable();
    }

    @FXML
    private void handleExpDateCheckBox() {
        expDateField.setDisable(hasAnExpDateCB.isSelected());
    }

    /**
     * Adds a hover effect to the specified button.
     *
     * @param button The button to add the hover effect to.
     */
    private void addHoverEffect() {
        EffectsUtils.addHoverEffect(addItemButton);
        EffectsUtils.addHoverEffect(saveItemButton);
        EffectsUtils.addHoverEffect(deleteItemButton);
        EffectsUtils.addHoverEffect(clearItemButton);
    }
}
