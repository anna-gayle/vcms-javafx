package com.genvetclinic.controllers;

import java.sql.*;
import com.genvetclinic.models.Kennel;
import com.genvetclinic.services.*;
import com.genvetclinic.utils.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.image.*;

/**
 * The {@code KennelsPanelController} class controls the Kennels Panel of the veterinary clinic application.
 * It manages the display and interaction with the kennel data.
 *
 * <p>This controller utilizes JavaFX components, such as TableView and TableColumn, to present a list of kennels.
 * It interacts with the {@link com.genvetclinic.services.KennelDao} class to retrieve and update kennel data from the database.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class KennelsPanelController {

    // DAO for Kennel entity
    private KennelDao kennelDao;

    // Connection to the database
    private DatabaseConnection databaseConnection;

    // The last selected kennel in the table
    private Kennel lastSelectedKennel;

    // FXML annotated fields for UI elements
    @FXML private MenuButton kennelNameMenu;
    @FXML private TextField kennelCapacityField;
    @FXML private MenuButton kennelStatusMenu;
    @FXML private Button addKennelButton;
    @FXML private Button clearKennelButton;
    @FXML private Button saveKennelButton;
    @FXML private Button deleteKennelButton;
    @FXML private ImageView addKennelIconViewer;
    @FXML private ImageView manageKennelIconViewer;
    @FXML private TableView<Kennel> kennelTableView;
    @FXML private TableColumn<Kennel, String> kennelIDColumn;
    @FXML private TableColumn<Kennel, String> kennelNameColumn;
    @FXML private TableColumn<Kennel, String> kennelCapacityColumn;
    @FXML private TableColumn<Kennel, String> kennelStatusColumn;

    /**
     * Constructor for KennelsPanelController.
     */
    public KennelsPanelController() {
        try {
            // Initialize database connection and KennelDao
            this.databaseConnection = new DatabaseConnection();
            this.kennelDao = new KennelDao(databaseConnection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error alert if unable to establish a database connection
            AlertUtils.showErrorAlert("Error", "Unable to establish a database connection.");
        }
    }

    /**
     * Initializes the controller after all FXML elements have been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize UI components and set up event handlers
        initializeKennelTable();
        populateKennelTable();
        enableKennelTableSelection();
        initializeKennelNameMenu();
        initializeKennelStatusMenu();
        initializeButtons();
        setupEventHandlers();
        setImageForIcons();
        addHoverEffect();

        // Set table selection color using EffectsUtils
        EffectsUtils.setTableSelectionColor(kennelTableView, "#358856");
    }

    /**
     * Initializes buttons' initial states.
     */
    private void initializeButtons() {
        deleteKennelButton.setDisable(true);
        saveKennelButton.setDisable(true);
    }

    /**
     * Sets up event handlers for buttons.
     */
    private void setupEventHandlers() {      
        addKennelButton.setOnAction(event -> handleAddKennel());
        clearKennelButton.setOnAction(event -> clearKennelFields());
        deleteKennelButton.setOnAction(event -> handleDeleteKennel());
        saveKennelButton.setOnAction(event -> handleSaveKennel());  
    }

    /**
     * Initializes the kennel table columns.
     */
    private void initializeKennelTable() {
        kennelIDColumn.setCellValueFactory(new PropertyValueFactory<>("kennelId"));
        kennelNameColumn.setCellValueFactory(new PropertyValueFactory<>("kennelName"));
        kennelCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("kennelCapacity"));
        kennelStatusColumn.setCellValueFactory(new PropertyValueFactory<>("kennelStatus"));
    }

    /**
     * Enables selection and adds event handlers to the kennel table.
     */
    private void enableKennelTableSelection() {
        kennelTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> handleKennelTableRowSelection(newSelection));

        kennelTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Kennel selectedKennel = kennelTableView.getSelectionModel().getSelectedItem();

                if (selectedKennel != null && selectedKennel.equals(lastSelectedKennel)) {
                    kennelTableView.getSelectionModel().clearSelection();
                    lastSelectedKennel = null;
                    clearKennelFields();
                } else {
                    lastSelectedKennel = selectedKennel;
                }
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleKennelTableRowSelection(kennelTableView.getSelectionModel().getSelectedItem());
            }
        });
    }

    /**
     * Handles selection of a kennel table row.
     *
     * @param selectedKennel The selected kennel.
     */
    private void handleKennelTableRowSelection(Kennel selectedKennel) {
        if (selectedKennel != null) {
            lastSelectedKennel = selectedKennel;
            displayKennelFields(selectedKennel);
            deleteKennelButton.setDisable(false);
            saveKennelButton.setDisable(false);
        } else {
            clearKennelFields();
            deleteKennelButton.setDisable(true);
            saveKennelButton.setDisable(true);
            lastSelectedKennel = null;
        }
    }

    /**
     * Sets images for icons displayed in the UI.
     */
    private void setImageForIcons() {
        Image addkennelIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Add Kennel Icon.png"));
        Image manageKennelIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Manage Kennel Icon.png"));
        addKennelIconViewer.setImage(addkennelIcon);
        manageKennelIconViewer.setImage(manageKennelIcon);        
    }

    /**
     * Handles adding a new kennel.
     */
    @FXML
    private void handleAddKennel() {
        try {
            String kennelName = kennelNameMenu.getText();
            Integer kennelCapacity = ParseUtils.parseInteger(kennelCapacityField.getText());
            String kennelStatus = kennelStatusMenu.getText();

            // Validate input fields
            if (ValidationUtils.isNullOrEmpty(kennelName, kennelStatus) || kennelCapacity == null) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate integer format for kennelCapacity
            if (!ValidationUtils.isValidInteger(String.valueOf(kennelCapacity))) {
                AlertUtils.showErrorAlert("Error", "Invalid format for number of equipment. Please enter a valid integer.");
                return;
            }

            // Create a new Kennel object
            Kennel newKennel = new Kennel(
                    GenerateRandomIds.generateRandomId(10),
                    kennelName, kennelCapacity, kennelStatus
            );

            // Add the new kennel to the table and save it to the database
            kennelTableView.getItems().add(newKennel);
            kennelDao.saveKennel(newKennel);
            clearKennelFields();

            AlertUtils.showInformationAlert("Success", "Kennel added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }

    /**
     * Handles saving changes to an existing kennel.
     */
    @FXML
    private void handleSaveKennel() {
        Kennel selectedKennel = kennelTableView.getSelectionModel().getSelectedItem();

        try {
            String updatedKennelName = kennelNameMenu.getText();
            Integer updatedKennelCapacity = ParseUtils.parseInteger(kennelCapacityField.getText());
            String updatedKennelStatus = kennelStatusMenu.getText();

            // Validate input fields
            if (ValidationUtils.isNullOrEmpty(updatedKennelName, updatedKennelStatus) || updatedKennelCapacity == null) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate integer format for updatedKennelCapacity
            if (!ValidationUtils.isValidInteger(String.valueOf(updatedKennelCapacity))) {
                AlertUtils.showErrorAlert("Error", "Invalid format for number of equipment. Please enter a valid integer.");
                return;
            }

            // Check if any changes were made
            if (!selectedKennel.getKennelName().equals(updatedKennelName) ||
                !selectedKennel.getKennelCapacity().equals(updatedKennelCapacity) ||
                !selectedKennel.getKennelStatus().equals(updatedKennelStatus)) {

                // Create an updated Kennel object
                Kennel updatedKennel = new Kennel(
                        selectedKennel.getKennelId(),
                        updatedKennelName, updatedKennelCapacity, updatedKennelStatus
                );

                // Update the kennel in the table view, refresh the table, and show success message
                kennelTableView.getItems().set(kennelTableView.getSelectionModel().getSelectedIndex(), updatedKennel);
                kennelDao.updateKennel(updatedKennel);
                refreshKennelsTable();
                AlertUtils.showInformationAlert("Success", "Kennel updated successfully.");
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
     * Handles deleting a kennel.
     */
    @FXML
    private void handleDeleteKennel() {
        Kennel selectedKennel = kennelTableView.getSelectionModel().getSelectedItem();

        // Ask for confirmation before deleting
        boolean confirmed = AlertUtils.showConfirmationPrompt("Are you sure you want to delete this kennel?");

        if (confirmed) {
            try {
                // Remove the kennel from the table and delete it from the database
                kennelTableView.getItems().remove(selectedKennel);
                kennelDao.deleteKennel(selectedKennel);
                clearKennelFields();
                AlertUtils.showInformationAlert("Success", "Kennel deleted successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
            }
        }
    }

    /**
     * Displays the details of the selected kennel in the input fields.
     *
     * @param kennel The selected kennel.
     */
    private void displayKennelFields(Kennel kennel) {
        kennelNameMenu.setText(kennel.getKennelName());
        kennelCapacityField.setText(String.valueOf(kennel.getKennelCapacity()));
        kennelStatusMenu.setText(kennel.getKennelStatus());
    }

    /**
     * Initializes the kennel name menu with options and event handlers.
     */
    private void initializeKennelNameMenu() {
        kennelNameMenu.getItems().clear();
        kennelNameMenu.getItems().addAll(
            new MenuItem("Boarding Kennel"),
            new MenuItem("Daycare Kennel"),
            new MenuItem("Breed-specific Kennel"),
            new MenuItem("Rescue/Shelter Kennel"),
            new MenuItem("Veterinary Kennel"),
            new MenuItem("Training Kennel"),
            new MenuItem("Private Kennel")
        );
        kennelNameMenu.getItems().forEach(item -> item.setOnAction(event -> handleKennelNameMenuItem(item.getText())));
    }

    /**
     * Handles selection of an item in the kennel name menu.
     *
     * @param selectedName The selected kennel name.
     */
    @FXML
    private void handleKennelNameMenuItem(String selectedName) {
        kennelNameMenu.setText(selectedName);
    }

    /**
     * Initializes the kennel status menu with options and event handlers.
     */
    private void initializeKennelStatusMenu() {
        kennelStatusMenu.getItems().clear();
        kennelStatusMenu.getItems().addAll(
                new MenuItem("Available for Boarding"),
                new MenuItem("Occupied"),
                new MenuItem("Reserved"),
                new MenuItem("Under Cleaning"),
                new MenuItem("Medical Observation"),
                new MenuItem("Adoption Pending"),
                new MenuItem("In Training"),
                new MenuItem("Vacant"),
                new MenuItem("Quarantine"),
                new MenuItem("Scheduled Vet Visit")
        );
        kennelStatusMenu.getItems().forEach(item -> item.setOnAction(event -> handleKennelStatusMenuItem(item.getText())));
    }

    /**
     * Handles selection of an item in the kennel status menu.
     *
     * @param selectedStatus The selected kennel status.
     */
    @FXML
    private void handleKennelStatusMenuItem(String selectedStatus) {
        kennelStatusMenu.setText(selectedStatus);
    }

    /**
     * Populates the kennel table with data from the database.
     */
    private void populateKennelTable() {
        try {
            ObservableList<Kennel> kennels = FXCollections.observableArrayList();

            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM kennels");
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    // Create Kennel objects from database results
                    Kennel kennel = new Kennel(
                            resultSet.getString("kennel_id"),
                            resultSet.getString("kennel_name"),
                            resultSet.getInt("kennel_capacity"),
                            resultSet.getString("kennel_status")
                    );

                    kennels.add(kennel);
                }
            }

            // Set the table items or show a placeholder if no kennels are found
            if (kennels.isEmpty()) {
                kennelTableView.setPlaceholder(new Label("No kennels found."));
            } else {
                kennelTableView.setItems(kennels);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to fetch kennels from the database.");
        }
    }

    /**
     * Clears input fields and resets button states.
     */
    private void clearKennelFields() {
        kennelNameMenu.setText("Kennel Type");
        kennelCapacityField.clear();
        kennelStatusMenu.setText("Status");
        kennelTableView.getSelectionModel().clearSelection();
        deleteKennelButton.setDisable(true);
        saveKennelButton.setDisable(true);
        lastSelectedKennel = null;
    }

    /**
     * Refreshes the kennel table by clearing and repopulating it.
     */
    private void refreshKennelsTable() {
        kennelTableView.getItems().clear();
        populateKennelTable();
    }

    /**
     * Adds a hover effect to buttons using EffectsUtils.
     */
    private void addHoverEffect() {
        EffectsUtils.addHoverEffect(addKennelButton);
        EffectsUtils.addHoverEffect(deleteKennelButton);
        EffectsUtils.addHoverEffect(saveKennelButton);
        EffectsUtils.addHoverEffect(clearKennelButton);
    }

}
