package com.genvetclinic.controllers;

import com.genvetclinic.models.Lab;
import com.genvetclinic.services.*;
import com.genvetclinic.utils.*;
import java.sql.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.image.*;

/**
 * The {@code LaboratoriesPanelController} class controls the Laboratories Panel of the veterinary clinic application.
 * It manages the display and interaction with the laboratory data.
 *
 * <p>This controller utilizes JavaFX components, such as TableView and TableColumn, to present a list of laboratories.
 * It interacts with the {@link com.genvetclinic.services.LabDao} class to retrieve and update laboratory data from the database.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class LaboratoriesPanelController {
    private LabDao labDao;
    private DatabaseConnection databaseConnection;
    private Lab lastSelectedLab;

    // FXML annotated fields for UI elements
    @FXML private MenuButton labNameMenu;
    @FXML private TextField noLabEquipField;
    @FXML private MenuButton labStatusMenu;
    @FXML private Button addLabButton;
    @FXML private Button clearLabButton;
    @FXML private Button saveLabButton;
    @FXML private Button deleteLabButton;
    @FXML private ImageView addLabIconViewer;
    @FXML private ImageView manageLabIconViewer;
    @FXML private TableView<Lab> labTableView;
    @FXML private TableColumn<Lab, String> labIDColumn;
    @FXML private TableColumn<Lab, String> labNameColumn;
    @FXML private TableColumn<Lab, Integer> noLabEquipColumn;
    @FXML private TableColumn<Lab, String> labStatusColumn;

    /**
     * Initializes the Laboratories Panel Controller.
     */
    public LaboratoriesPanelController() {
        try {
            this.databaseConnection = new DatabaseConnection();
            this.labDao = new LabDao(databaseConnection);
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
        
        initializeLabTable();
        populateLabTable();
        enableLabTableSelection();
        initializeLabNameMenu();
        initializeLabStatusMenu();
        initializeButtons();
        setupEventHandlers();
        setImageForIcons();
        addHoverEffect();

        EffectsUtils.setTableSelectionColor(labTableView, "#358856");
    }

    /**
     * Initializes the buttons in the Java function.
     */    
    private void initializeButtons() {
        deleteLabButton.setDisable(true);
        saveLabButton.setDisable(true);
    }

    /**
     * Sets up the event handlers for the buttons in the UI.
     *
     * This function assigns event handlers to the addLabButton, clearLabButton,
     * deleteLabButton, and saveLabButton buttons. When these buttons are clicked,
     * their respective handler functions are called.
     */    
    private void setupEventHandlers() {
        addLabButton.setOnAction(event -> handleAddLab());
        clearLabButton.setOnAction(event -> clearLabFields());
        deleteLabButton.setOnAction(event -> handleDeleteLab());
        saveLabButton.setOnAction(event -> handleSaveLab());   
    }

    /**
     * Initializes the lab table by setting the cell value factories for each column.
     */
    private void initializeLabTable() {
        labIDColumn.setCellValueFactory(new PropertyValueFactory<>("labId"));
        labNameColumn.setCellValueFactory(new PropertyValueFactory<>("labName"));
        noLabEquipColumn.setCellValueFactory(new PropertyValueFactory<>("noOfEquipment"));
        labStatusColumn.setCellValueFactory(new PropertyValueFactory<>("labStatus"));
    }

    /**
     * Enables the selection of a laboratory in the laboratory table view.
     * Adds a listener to the selected item property of the laboratory table view's selection model.
     * When a new selection is made, it calls the handleTableRowSelection method with the new selection.
     * Sets an on-click event handler for the laboratory table view.
     * If the primary mouse button is clicked once, it checks if the selected laboratory is the same as the last selected laboratory.
     * If they are the same, it clears the selection, sets the last selected laboratory to null, and clears the laboratory fields.
     * If they are different, it sets the last selected laboratory to the selected laboratory.
     * If the primary mouse button is clicked twice, it calls the handleTableRowSelection method with the selected item.
     */
    private void enableLabTableSelection() {
        labTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> handleLabTableRowSelection(newSelection));

        labTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Lab selectedLab = labTableView.getSelectionModel().getSelectedItem();

                if (selectedLab != null && selectedLab.equals(lastSelectedLab)) {
                    labTableView.getSelectionModel().clearSelection();
                    lastSelectedLab = null;
                    clearLabFields();
                } else {
                    lastSelectedLab = selectedLab;
                }
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleLabTableRowSelection(labTableView.getSelectionModel().getSelectedItem());
            }
        });
    }

    /**
     * Handles the selection of a lab table row.
     *
     * @param  selectedLab  the selected lab object
     */
    private void handleLabTableRowSelection(Lab selectedLab) {
        if (selectedLab != null) {
            lastSelectedLab = selectedLab;
            displayLabFields(selectedLab);
            deleteLabButton.setDisable(false);
            saveLabButton.setDisable(false);
        } else {
            clearLabFields();
            deleteLabButton.setDisable(true);
            saveLabButton.setDisable(true);
            lastSelectedLab = null;
        }
    }

    /**
     * Sets the image for icons.
     *
     * This function loads and sets the images for the addLabIconViewer and manageLabIconViewer.
     * It retrieves the images from the specified file paths.
     */
    private void setImageForIcons() {
        Image addLabIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Add Lab Icon 2.png"));
        Image manageLabIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Manage Lab Icon.png"));
        addLabIconViewer.setImage(addLabIcon);
        manageLabIconViewer.setImage(manageLabIcon);
    }

    /**
     * Handles the action of adding a lab.
     */
    @FXML
    private void handleAddLab() {
        try {
            // Retrieve input values from the form fields
            String labName = labNameMenu.getText();
            Integer noOfEquipment = ParseUtils.parseInteger(noLabEquipField.getText());
            String labStatus = labStatusMenu.getText();

            // Validate if required fields are not empty
            if (ValidationUtils.isNullOrEmpty(labName, labStatus) || noOfEquipment == null) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate number format for noOfEquipment (assuming noOfEquipment is an Integer)
            if (!ValidationUtils.isValidInteger(String.valueOf(noOfEquipment))) {
                AlertUtils.showErrorAlert("Error", "Invalid format for number of equipment. Please enter a valid integer.");
                return;
            }

            // Create a new Lab object
            Lab newLab = new Lab(
                    GenerateRandomIds.generateRandomId(10),
                    labName, noOfEquipment, labStatus
            );

            // Add the new lab to the table view
            labTableView.getItems().add(newLab);


            // Save the lab and clear form fields
            labDao.saveLab(newLab);
            clearLabFields();

            // Show success message
            AlertUtils.showInformationAlert("Success", "Lab added successfully.");
        } catch (Exception e) {
            // Handle unexpected errors
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }

    /**
     * Handles the save action for the Lab.
     */    
    @FXML
    private void handleSaveLab() {
        Lab selectedLab = labTableView.getSelectionModel().getSelectedItem();

        try {
            // Retrieve updated values from the form fields
            String updatedLabName = labNameMenu.getText();
            Integer updatedNoOfEquipment = ParseUtils.parseInteger(noLabEquipField.getText());
            String updatedLabStatus = labStatusMenu.getText();

            // Validate if required fields are not empty
            if (ValidationUtils.isNullOrEmpty(updatedLabName, updatedLabStatus) || updatedNoOfEquipment == null) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate number format for updatedNoOfEquipment (assuming updatedNoOfEquipment is an Integer)
            if (!ValidationUtils.isValidInteger(String.valueOf(updatedNoOfEquipment))) {
                AlertUtils.showErrorAlert("Error", "Invalid format for number of equipment. Please enter a valid integer.");
                return;
            }

            // Check if any changes were made
            if (!selectedLab.getLabName().equals(updatedLabName) ||
                !selectedLab.getNoOfEquipment().equals(updatedNoOfEquipment) ||
                !selectedLab.getLabStatus().equals(updatedLabStatus)) {

                // Create an updated Lab object
                Lab updatedLab = new Lab(
                        selectedLab.getLabId(),
                        updatedLabName, updatedNoOfEquipment, updatedLabStatus
                );

                // Update the lab in the table view, refresh the table, and show success message
                labTableView.getItems().set(labTableView.getSelectionModel().getSelectedIndex(), updatedLab);
                labDao.updateLab(updatedLab);
                refreshAppointmentTable();
                AlertUtils.showInformationAlert("Success", "Lab updated successfully.");
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
     * Handles the delete lab action.
     */
    @FXML
    private void handleDeleteLab() {
        Lab selectedLab = labTableView.getSelectionModel().getSelectedItem();

        boolean confirmed = AlertUtils.showConfirmationPrompt("Are you sure you want to delete this lab?");

        if (confirmed) {
            try {
                labTableView.getItems().remove(selectedLab);
                labDao.deleteLab(selectedLab);
                clearLabFields();
                AlertUtils.showInformationAlert("Success", "Lab deleted successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
            }
        }
    }

    /**
     * Initializes the lab status menu by clearing its items and adding a set of predefined items.
     * Also sets the action for each item to handle the lab status menu item selection.
     */
    private void initializeLabStatusMenu() {
        labStatusMenu.getItems().clear();
        labStatusMenu.getItems().addAll(
            new MenuItem("Available for Testing"),
            new MenuItem("In Use"),
            new MenuItem("Under Maintenance"),
            new MenuItem("Out of Service"),
            new MenuItem("Pending Approval"),
            new MenuItem("Completed Experiment"),
            new MenuItem("Awaiting Supplies"),
            new MenuItem("Calibration Due"),
            new MenuItem("Data Analysis")
        );
        labStatusMenu.getItems().forEach(item -> item.setOnAction(event -> handleLabStatusMenuItem(item.getText())));
    }

    /**
     * A description of the entire Java function.
     *
     * @param  selectedStatus	description of parameter
     */    
    @FXML
    private void handleLabStatusMenuItem(String selectedStatus) {
        labStatusMenu.setText(selectedStatus);
    }

    /**
     * Initializes the lab name menu by clearing existing items and adding a list of lab names.
     * Also sets the action for each menu item to call the handleLabNameMenuItem method.
     */    
    private void initializeLabNameMenu() {
        labNameMenu.getItems().clear();
        labNameMenu.getItems().addAll(
            new MenuItem("Clinical Pathology Lab"),
            new MenuItem("Microbiology Lab"),
            new MenuItem("Parasitology Lab"),
            new MenuItem("Hematology Lab"),
            new MenuItem("Serology Lab"),
            new MenuItem("Cytology Lab"),
            new MenuItem("Histopathology Lab"),
            new MenuItem("Diagnostic Imaging Lab"),
            new MenuItem("Genetics Lab"),
            new MenuItem("Chemistry Lab")
        );
        labNameMenu.getItems().forEach(item -> item.setOnAction(event -> handleLabNameMenuItem(item.getText())));
    }

    /**
     * A description of the entire Java function.
     *
     * @param  selectedLabName  description of parameter
     */
    @FXML
    private void handleLabNameMenuItem(String selectedLabName) {
        labNameMenu.setText(selectedLabName);
    }

    /**
     * Displays the fields of a given Lab object.
     *
     * @param  lab  the Lab object to display the fields for
     */
    private void displayLabFields(Lab lab) {
        labNameMenu.setText(lab.getLabName());
        noLabEquipField.setText(String.valueOf(lab.getNoOfEquipment()));
        labStatusMenu.setText(lab.getLabStatus());
    }

    /**
     * Populates the lab table with data retrieved from the database.
     */
    private void populateLabTable() {
        try {
            ObservableList<Lab> labs = FXCollections.observableArrayList();
    
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM laboratories");
                 ResultSet resultSet = statement.executeQuery()) {
    
                while (resultSet.next()) {
                    Lab lab = new Lab(
                            resultSet.getString("lab_id"),
                            resultSet.getString("lab_name"),
                            resultSet.getInt("no_of_lab_equipment"),
                            resultSet.getString("lab_status")
                    );
    
                    labs.add(lab);
                }
    
            }
    
            if (labs.isEmpty()) {
                labTableView.setPlaceholder(new Label("No labs found."));
            } else {
                labTableView.setItems(labs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to fetch labs from the database.");
        }
    }    

    /**
     * Refreshes the appointment table by clearing the items and repopulating it.
     */    
    private void refreshAppointmentTable() {
        labTableView.getItems().clear();
        populateLabTable();
    }

    /**
     * Clears the fields in the lab.
     */
    private void clearLabFields() {
        labStatusMenu.setText("Lab Type");;
        noLabEquipField.clear();
        labStatusMenu.setText("Status");
        labTableView.getSelectionModel().clearSelection();
        deleteLabButton.setDisable(true);
        saveLabButton.setDisable(true);
        lastSelectedLab = null;
    }

    /**
     * Adds a hover effect to the specified buttons.
     *
     * @param  addLabButton    the button to add the hover effect to when the mouse hovers over it
     * @param  saveLabButton   the button to add the hover effect to when the mouse hovers over it
     * @param  deleteLabButton the button to add the hover effect to when the mouse hovers over it
     * @param  clearLabButton  the button to add the hover effect to when the mouse hovers over it
     */
    private void addHoverEffect() {
        EffectsUtils.addHoverEffect(addLabButton);
        EffectsUtils.addHoverEffect(saveLabButton);
        EffectsUtils.addHoverEffect(deleteLabButton);
        EffectsUtils.addHoverEffect(clearLabButton);
    }
    
}
