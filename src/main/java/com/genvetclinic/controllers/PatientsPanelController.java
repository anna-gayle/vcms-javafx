package com.genvetclinic.controllers;

import java.math.*;
import java.sql.*;
import java.time.*;
import com.genvetclinic.models.Patient;
import com.genvetclinic.services.*;
import com.genvetclinic.utils.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.input.*;

/**
 * The {@code PatientsPanelController} class controls the patients panel of the veterinary clinic application.
 * It manages the display of patient data in a TableView and provides functionality to interact with patient records.
 *
 * <p>The controller uses JavaFX components, such as TableView and TableColumn, to present a list of patients.
 * It interacts with the {@link com.genvetclinic.services.PatientDao} class to retrieve and update patient data.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class PatientsPanelController {

    private PatientDao patientDao;
    private DatabaseConnection databaseConnection;
    private Patient lastSelectedPatient;

    // FXML annotated fields for UI elements
    @FXML private TextField patientNameField;
    @FXML private TextField patientSpeciesField;
    @FXML private TextField patientBreedField;
    @FXML private TextField patientAgeField;
    @FXML private TextField patientColorField;
    @FXML private TextField patientWeightField;
    @FXML private TextField patientMicroIDField;
    @FXML private DatePicker dateAdmittedDP;
    @FXML private TextArea medHistoryTA;
    @FXML private TextArea vaxHistoryTA;
    @FXML private TextArea specialInsTA;
    @FXML private TextField ownerNameField;
    @FXML private TextField ownerContactField;
    @FXML private TextField ownerEmailField;
    @FXML private TextField ownerAddressField;
    @FXML private TextField patientInsuranceField;
    @FXML private Button admitPatientButton;
    @FXML private Button deletePatientButton;
    @FXML private Button savePatientButton;
    @FXML private Button clearPatientButton;
    @FXML private MenuButton patientGenderMenu;
    @FXML private ImageView addPatientIconViewer;
    @FXML private ImageView managePatientIconViewer;
    @FXML private TableView<Patient> patientTableView;
    @FXML private TableColumn<Patient, String> patientIDColumn;
    @FXML private TableColumn<Patient, String> patientNameColumn;
    @FXML private TableColumn<Patient, String> patientSpeciesColumn;
    @FXML private TableColumn<Patient, String> patientBreedColumn;
    @FXML private TableColumn<Patient, String> patientAgeColumn;
    @FXML private TableColumn<Patient, String> patientGenderColumn;
    @FXML private TableColumn<Patient, String> patientColorColumn;
    @FXML private TableColumn<Patient, String> patientWeightColumn;
    @FXML private TableColumn<Patient, String> patientMChipIdColumn;
    @FXML private TableColumn<Patient, String> patientAdmittedDateColumn;
    @FXML private TableColumn<Patient, String> patientOwnerNameColumn;
    @FXML private TableColumn<Patient, String> ownerContactColumn;
    @FXML private TableColumn<Patient, String> ownerEmailColumn;
    @FXML private TableColumn<Patient, String> ownerAddColumn;
    @FXML private TableColumn<Patient, String> patientInsuranceColumn;
    @FXML private TableColumn<Patient, String> patientMedHistoryColumn;
    @FXML private TableColumn<Patient, String> patientVaxHistoryColumn;
    @FXML private TableColumn<Patient, String> patientSpecialColumn;

    /**
     * Initializes the Patients Panel Controller.
     */
    public PatientsPanelController() {
        try {
            this.databaseConnection = new DatabaseConnection();
            this.patientDao = new PatientDao(databaseConnection);
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
        
        initializePatientTable();
        populatePatientTable();
        enablePatientTableSelection();
        initializeGenderMenu();
        initializeButtons();
        setupEventHandlers();
        setImageForIcons();
        addHoverEffect();
    
        EffectsUtils.setTableSelectionColor(patientTableView, "#358856");
    }

    /**
     * Initializes the buttons in the Java function.
     */
    private void initializeButtons() {
        deletePatientButton.setDisable(true);
        savePatientButton.setDisable(true);
    }

    /**
     * Sets up the event handlers for the admitPatientButton, clearPatientButton,
     * deletePatientButton, and savePatientButton.
     *
     * @param event the event object that triggered the action
     */
    private void setupEventHandlers() {
        admitPatientButton.setOnAction(event -> handleAdmitPatient());
        clearPatientButton.setOnAction(event -> clearPatientFields());
        deletePatientButton.setOnAction(event -> handleDeletePatient());
        savePatientButton.setOnAction(event -> handleEditPatient());
    }

    /**
     * Initializes the patient table by setting up the cell value factories for each column.
     */
    private void initializePatientTable() {
        patientIDColumn.setCellValueFactory(new PropertyValueFactory<>("patientid"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patientSpeciesColumn.setCellValueFactory(new PropertyValueFactory<>("patientSpecies"));
        patientBreedColumn.setCellValueFactory(new PropertyValueFactory<>("patientBreed"));
        patientAgeColumn.setCellValueFactory(new PropertyValueFactory<>("patientAge"));
        patientGenderColumn.setCellValueFactory(new PropertyValueFactory<>("patientGender"));
        patientColorColumn.setCellValueFactory(new PropertyValueFactory<>("patientColor"));
        patientWeightColumn.setCellValueFactory(new PropertyValueFactory<>("patientWeight"));
        patientMChipIdColumn.setCellValueFactory(new PropertyValueFactory<>("microchipId"));
        patientAdmittedDateColumn.setCellValueFactory(new PropertyValueFactory<>("admittedDate"));
        patientOwnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        ownerContactColumn.setCellValueFactory(new PropertyValueFactory<>("ownerContact"));
        ownerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("ownerEmail"));
        ownerAddColumn.setCellValueFactory(new PropertyValueFactory<>("ownerAddress"));
        patientInsuranceColumn.setCellValueFactory(new PropertyValueFactory<>("patientInsurance"));
        patientMedHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));
        patientVaxHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("vaccineHistory"));
        patientSpecialColumn.setCellValueFactory(new PropertyValueFactory<>("specialInstructions"));
    }
   
    /**
      * Enables the selection of a patient in the patient table view.
      * Adds a listener to the selected item property of the patient table view's selection model.
      * When a new selection is made, it calls the handleTableRowSelection method with the new selection.
      * Sets an on-click event handler for the patient table view.
      * If the primary mouse button is clicked once, it checks if the selected patient is the same as the last selected patient.
      * If they are the same, it clears the selection, sets the last selected patient to null, and clears the patient fields.
      * If they are different, it sets the last selected patient to the selected patient.
      * If the primary mouse button is clicked twice, it calls the handleTableRowSelection method with the selected item.
      *
      */
     private void enablePatientTableSelection() {
        patientTableView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldSelection, newSelection) -> handleTableRowSelection(newSelection));

        patientTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();

                if (selectedPatient != null && selectedPatient.equals(lastSelectedPatient)) {
                    patientTableView.getSelectionModel().clearSelection();
                    lastSelectedPatient = null;
                    clearPatientFields();
                } else {
                    lastSelectedPatient = selectedPatient;
                }
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleTableRowSelection(patientTableView.getSelectionModel().getSelectedItem());
            }
        });  
     }

    /**
      * Handles the selection of a table row.
      *
      * @param  selectedPatient  the patient that was selected
      */
     private void handleTableRowSelection(Patient selectedPatient) {
        if (selectedPatient != null) {
            lastSelectedPatient = selectedPatient;
            displayPatientFields(selectedPatient); 
            deletePatientButton.setDisable(false);
            savePatientButton.setDisable(false);
        } else {
            clearPatientFields();
            deletePatientButton.setDisable(true);
            savePatientButton.setDisable(true);
            lastSelectedPatient = null;
        }
    }

    /**
     * Sets the image for icons.
     */
     private void setImageForIcons() {
        Image admitPatientIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Add Patient Icon.png"));
        Image managePatientIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Manage Patient Icon.png"));
        addPatientIconViewer.setImage(admitPatientIcon);
        managePatientIconViewer.setImage(managePatientIcon);
    }

    /**
     * Handles the action when admitting a new patient.
     */
    @FXML
    private void handleAdmitPatient() {
        try {
            // Get input values from the form fields
            String patientName = patientNameField.getText();
            String patientSpecies = patientSpeciesField.getText();
            String patientBreed = patientBreedField.getText();
            String patientColor = patientColorField.getText();
            String medicalHistory = medHistoryTA.getText();
            String vaccineHistory = vaxHistoryTA.getText();
            String specialInstructions = specialInsTA.getText();
            String ownerName = ownerNameField.getText();
            String ownerContact = ownerContactField.getText();
            String ownerEmail = ownerEmailField.getText();
            String ownerAddress = ownerAddressField.getText();
            String patientInsurance = patientInsuranceField.getText();
            String microchipid = patientMicroIDField.getText();
            String patientGender = patientGenderMenu.getText();
            BigDecimal patientAge = ParseUtils.parseBigDecimal(patientAgeField.getText());
            BigDecimal patientWeight = ParseUtils.parseBigDecimal(patientWeightField.getText());
            LocalDate admittedDate = ParseUtils.parseLocalDate(dateAdmittedDP.getValue().toString());

            // Validate if required fields are not empty
            if (ValidationUtils.isNullOrEmpty(
                    patientName, patientSpecies, patientBreed, patientColor, medicalHistory,
                    vaccineHistory, specialInstructions, ownerName, ownerContact, ownerEmail, ownerAddress,
                    patientInsurance, microchipid, patientGender)) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate patient and owner names
            if (!ValidationUtils.isValidName(patientName) || !ValidationUtils.isValidName(ownerName)) {
                AlertUtils.showErrorAlert("Error", "Invalid patient or owner name. Please use only letters and spaces.");
                return;
            }

            // Validate owner email
            if (!ValidationUtils.isValidEmail(ownerEmail)) {
                AlertUtils.showErrorAlert("Error", "Invalid owner email address.");
                return;
            }

            // Validate owner contact number
            if (!ValidationUtils.isValidInteger(ownerContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid owner contact number.");
                return;
            }

            // Validate numeric fields for correct format and greater than zero
            if (!ValidationUtils.isValidNumber(patientAge) || !ValidationUtils.isValidNumber(patientWeight, patientAge)) {
                AlertUtils.showErrorAlert("Error", "Age and Weight must be greater than or equal to zero.");
                return;
            }

            // Validate the date field
            if (!ValidationUtils.isValidDateFormat(admittedDate.toString())) {
                AlertUtils.showErrorAlert("Error", "Invalid date format. Please use the correct format.");
                return;
            }

            // Generate a random patient ID
            String patientId = GenerateRandomIds.generateRandomId(10);

            // Create a new Patient object and set its properties
            Patient patient = new Patient(
                    patientId, patientName, patientSpecies, patientBreed, patientAge, patientColor, admittedDate,
                    medicalHistory, vaccineHistory, specialInstructions, ownerName, ownerContact, ownerEmail,
                    ownerAddress, patientInsurance, patientWeight, microchipid, patientGender
            );

            // Add the patient to the table view
            patientTableView.getItems().add(patient);

            // Check if the patient already exists
            if (patientDao.isPatientExists(patientName, patientSpecies, patientBreed, ownerName)) {
                AlertUtils.showErrorAlert("Error", "Patient already exists.");
            } else {
                // Save the patient and show success message
                patientDao.savePatient(patient);
                clearPatientFields();
                AlertUtils.showInformationAlert("Success", "Patient admitted successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }

    /**
     * Handles the action when editing patient information.
     */
    @FXML
    private void handleEditPatient() {
        try {
            Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();

            String updatedName = patientNameField.getText();
            String updatedSpecies = patientSpeciesField.getText();
            String updatedBreed = patientBreedField.getText();
            String updatedColor = patientColorField.getText();
            String updatedMedicalHistory = medHistoryTA.getText();
            String updatedVaccineHistory = vaxHistoryTA.getText();
            String updatedSpecialInstructions = specialInsTA.getText();
            String updatedOwnerName = ownerNameField.getText();
            String updatedOwnerContact = ownerContactField.getText();
            String updatedOwnerEmail = ownerEmailField.getText();
            String updatedOwnerAddress = ownerAddressField.getText();
            String updatedPatientInsurance = patientInsuranceField.getText();
            String updatedMicrochipId = patientMicroIDField.getText();
            String updatedPatientGender = patientGenderMenu.getText();
            BigDecimal updatedPatientAge = ParseUtils.parseBigDecimal(patientAgeField.getText());
            BigDecimal updatedPatientWeight = ParseUtils.parseBigDecimal(patientWeightField.getText());
            LocalDate updatedAdmittedDate = ParseUtils.parseLocalDate(dateAdmittedDP.getValue().toString());

            // Validate if required fields are not empty
            if (ValidationUtils.isNullOrEmpty(
                    updatedName, updatedSpecies, updatedBreed, updatedColor, updatedMedicalHistory, updatedVaccineHistory,
                    updatedSpecialInstructions, updatedOwnerName, updatedOwnerContact, updatedOwnerEmail, updatedOwnerAddress,
                    updatedPatientInsurance, updatedMicrochipId, updatedPatientGender)) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate patient and owner names
            if (!ValidationUtils.isValidName(updatedName) || !ValidationUtils.isValidName(updatedOwnerName)) {
                AlertUtils.showErrorAlert("Error", "Invalid patient or owner name. Please use only letters and spaces.");
                return;
            }

            // Validate owner email
            if (!ValidationUtils.isValidEmail(updatedOwnerEmail)) {
                AlertUtils.showErrorAlert("Error", "Invalid owner email address.");
                return;
            }

            // Validate owner contact number
            if (!ValidationUtils.isValidInteger(updatedOwnerContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid owner contact number.");
                return;
            }

            // Validate numeric fields for correct format and greater than zero
            if (!ValidationUtils.isValidNumber(updatedPatientAge) || !ValidationUtils.isValidNumber(updatedPatientWeight, updatedPatientAge)) {
                AlertUtils.showErrorAlert("Error", "Age and Weight must be greater than or equal to zero.");
                return;
            }

            // Validate the date field
            if (!ValidationUtils.isValidDateFormat(updatedAdmittedDate.toString())) {
                AlertUtils.showErrorAlert("Error", "Invalid date format. Please use the correct format.");
                return;
            }

            // Check if any changes were made
            if (!selectedPatient.getPatientName().equals(updatedName) || !selectedPatient.getPatientSpecies().equals(updatedSpecies)
                    || !selectedPatient.getPatientBreed().equals(updatedBreed) || !selectedPatient.getPatientColor().equals(updatedColor)
                    || !selectedPatient.getMedicalHistory().equals(updatedMedicalHistory) || !selectedPatient.getVaccineHistory().equals(updatedVaccineHistory)
                    || !selectedPatient.getSpecialInstructions().equals(updatedSpecialInstructions) || !selectedPatient.getOwnerName().equals(updatedOwnerName)
                    || !selectedPatient.getOwnerContact().equals(updatedOwnerContact) || !selectedPatient.getOwnerEmail().equals(updatedOwnerEmail)
                    || !selectedPatient.getOwnerAddress().equals(updatedOwnerAddress) || !selectedPatient.getPatientInsurance().equals(updatedPatientInsurance)
                    || !selectedPatient.getMicrochipId().equals(updatedMicrochipId) || !selectedPatient.getPatientGender().equals(updatedPatientGender)
                    || !selectedPatient.getPatientAge().equals(updatedPatientAge) || !selectedPatient.getPatientWeight().equals(updatedPatientWeight)
                    || !selectedPatient.getAdmittedDate().equals(updatedAdmittedDate)) {

                // Update the patient information
                selectedPatient = new Patient(
                    selectedPatient.getPatientid(), updatedName, updatedSpecies, updatedBreed, updatedPatientAge,
                    updatedColor, updatedAdmittedDate, updatedMedicalHistory, updatedVaccineHistory,
                    updatedSpecialInstructions, updatedOwnerName, updatedOwnerContact, updatedOwnerEmail,
                    updatedOwnerAddress, updatedPatientInsurance, updatedPatientWeight, updatedMicrochipId,
                    updatedPatientGender
                );

                // Try to update the patient in the database
                try {
                    patientDao.updatePatient(selectedPatient);
                    refreshPatientTable();
                    AlertUtils.showInformationAlert("Success", "Patient information updated successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
                }
            } else {
                // No changes were made
                AlertUtils.showInformationAlert("Info", "No changes were made.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }

    /**
     * Handles the action when deleting a patient.
     */
    private void handleDeletePatient() {
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();

        boolean confirmed = AlertUtils.showConfirmationPrompt("Are you sure you want to delete this patient?");
    
        if (confirmed) {
            try {
                patientDao.deletePatient(selectedPatient);
                patientTableView.getItems().remove(selectedPatient);
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
            }
        }
    }

    /**
     * Displays the patient information in the input fields.
     *
     * @param patient The selected patient.
     */
    private void displayPatientFields(Patient patient) {
        patientNameField.setText(patient.getPatientName());
        patientSpeciesField.setText(patient.getPatientSpecies());
        patientBreedField.setText(patient.getPatientBreed());
        patientAgeField.setText(patient.getPatientAge().toString());
        patientGenderMenu.setText(patient.getPatientGender());
        patientColorField.setText(patient.getPatientColor());
        patientWeightField.setText(patient.getPatientWeight().toString());
        patientMicroIDField.setText(patient.getMicrochipId());
        dateAdmittedDP.setValue(patient.getAdmittedDate());
        medHistoryTA.setText(patient.getMedicalHistory());
        vaxHistoryTA.setText(patient.getVaccineHistory());
        specialInsTA.setText(patient.getSpecialInstructions());
        ownerNameField.setText(patient.getOwnerName());
        ownerContactField.setText(patient.getOwnerContact());
        ownerEmailField.setText(patient.getOwnerEmail());
        ownerAddressField.setText(patient.getOwnerAddress());
        patientInsuranceField.setText(patient.getPatientInsurance());
    }

    /**
     * Populates the patient table with data from the database.
     */
    private void populatePatientTable() {
        try {
            ObservableList<Patient> patientList = FXCollections.observableArrayList();

            try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM patients");
                ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Patient patient = new Patient(
                            resultSet.getString("patient_id"),
                            resultSet.getString("patient_name"),
                            resultSet.getString("patient_species"),
                            resultSet.getString("patient_breed"),
                            resultSet.getBigDecimal("age_in_years"),
                            resultSet.getString("patient_color"),
                            resultSet.getDate("admitted_date").toLocalDate(),
                            resultSet.getString("medical_history"),
                            resultSet.getString("vaccination_history"),
                            resultSet.getString("special_instruction"),
                            resultSet.getString("owner_name"),
                            resultSet.getString("owner_contact"),
                            resultSet.getString("owner_email"),
                            resultSet.getString("owner_address"),
                            resultSet.getString("patient_insurance"),
                            resultSet.getBigDecimal("patient_weight"),
                            resultSet.getString("mchip_id"),
                            resultSet.getString("patient_gender")
                    );

                    patientList.add(patient);
                }
            }

            if (patientList.isEmpty()) {
                patientTableView.setPlaceholder(new Label("No patients found in the database."));
            } else {
                patientTableView.setItems(patientList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the patient table by clearing and repopulating the data.
     */
    private void refreshPatientTable() {
        patientTableView.getItems().clear();
        populatePatientTable();
    }

    /**
     * Handles the selection of gender menu items.
     *
     * @param selectedGender The selected gender.
     */
    @FXML
    private void handleGenderMenuItem(String selectedGender) {
        patientGenderMenu.setText(selectedGender);
    }

    /**
     * Initializes the gender menu.
     */
    private void initializeGenderMenu() {
        patientGenderMenu.getItems().clear();
        patientGenderMenu.getItems().addAll(
            new MenuItem("Female"), 
            new MenuItem("Male")
        );
        patientGenderMenu.getItems().forEach(item -> item.setOnAction(event -> handleGenderMenuItem(item.getText())));
    }

    /**
     * Clears all input fields related to patient information.
     */
    private void clearPatientFields() {
        patientNameField.clear();
        patientSpeciesField.clear();
        patientBreedField.clear();
        patientAgeField.clear();
        patientGenderMenu.setText("Gender");
        patientColorField.clear();
        patientWeightField.clear();
        patientMicroIDField.clear();
        dateAdmittedDP.setValue(null);
        medHistoryTA.clear();
        vaxHistoryTA.clear();
        specialInsTA.clear();
        ownerNameField.clear();
        ownerContactField.clear();
        ownerEmailField.clear();
        ownerAddressField.clear();
        patientInsuranceField.clear();
    }

    /**
     * Adds a hover effect to the specified button.
     *
     * @param button The button to add the hover effect to.
     */
    private void addHoverEffect() {
        EffectsUtils.addHoverEffect(admitPatientButton);
        EffectsUtils.addHoverEffect(savePatientButton);
        EffectsUtils.addHoverEffect(deletePatientButton);
        EffectsUtils.addHoverEffect(clearPatientButton);
    }

}
