package com.genvetclinic.controllers;

import com.genvetclinic.services.*;
import com.genvetclinic.models.Personnel;
import com.genvetclinic.utils.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import java.util.Arrays;
import java.util.Objects;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class PersonnelPanelController {

    private PersonnelDao personnelDao;
    private DatabaseConnection databaseConnection;
    private Personnel lastSelectedPersonnel;

    // FXML annotated fields for UI elements
    @FXML private TextField employeeNameField;
    @FXML private TextField employeeEmailField;
    @FXML private TextField employeeAddressField;
    @FXML private TextField employeeContactField;
    @FXML private TextField emergencyContactField;
    @FXML private TextField employeeCertificationField;
    @FXML private TextField workScheduleField;
    @FXML private DatePicker employeeHireDateField;
    @FXML private TextField perfRatingField;
    @FXML private TextField attRatingField;
    @FXML private CheckBox exemptRatingCB;
    @FXML private Button addPersonnelButton;
    @FXML private Button clearPersonnelButton;
    @FXML private Button savePersonnelButton;
    @FXML private Button deletePersonnelButton;
    @FXML private MenuButton jobTitleMenu;
    @FXML private MenuButton vetSpecMenu;
    @FXML private ImageView addEmpIconViewer;
    @FXML private ImageView manageEmpIconViewer;
    @FXML private TableView<Personnel> personnelTableView;
    @FXML private TableColumn<Personnel, String> employeeIdColumn;
    @FXML private TableColumn<Personnel, String> employeeNameColumn;
    @FXML private TableColumn<Personnel, String> employeeEmailColumn;
    @FXML private TableColumn<Personnel, String> employeeAddressColumn;
    @FXML private TableColumn<Personnel, String> employeeContactColumn;
    @FXML private TableColumn<Personnel, String> emergencyContactColumn;
    @FXML private TableColumn<Personnel, String> jobTitleColumn;
    @FXML private TableColumn<Personnel, String> specializationColumn;
    @FXML private TableColumn<Personnel, String> certificationColumn;
    @FXML private TableColumn<Personnel, String> workScheduleColumn;
    @FXML private TableColumn<Personnel, String> hireDateColumn;
    @FXML private TableColumn<Personnel, String> perfRatingColumn;
    @FXML private TableColumn<Personnel, String> attRatingColumn;

    /**
     * The {@code PersonnelPanelController} class controls the personnel panel of the veterinary clinic application.
     * It manages the display of personnel data in a TableView and provides functionality to interact with personnel records.
     *
     * <p>The controller uses JavaFX components, such as TableView and TableColumn, to present a list of personnel.
     * It interacts with the {@link com.genvetclinic.services.PersonnelDao} class to retrieve and update personnel data.
     *
     * @author vcms-group
     * @version 1.0
     * @since 2023-11-19
     */
    public PersonnelPanelController() {
        try {
            this.databaseConnection = new DatabaseConnection();
            this.personnelDao = new PersonnelDao(databaseConnection);
            personnelDao = new PersonnelDao();
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
        
        initializePersonnelTable();
        populatePersonnelTable();
        enablePersonnelTableSelection();
        initializeJobTitleMenu();
        initializeSpecializationMenu();
        initializeButtons();
        setupEventHandlers();
        setImageForIcons();
        addHoverEffect();
        handleExemptRating();

        EffectsUtils.setTableSelectionColor(personnelTableView, "#358856");
    }

    /**
     * Initializes the buttons in the Java function.
     */
    private void initializeButtons() {
        deletePersonnelButton.setDisable(true);
        savePersonnelButton.setDisable(true);
    }

    /**
     * Sets up the event handlers for the addPersonnelButton, clearPersonnelButton,
     * deletePersonnelButton, and savePersonnelButton.
     *
     * @param event the event object that triggered the action
     */
    private void setupEventHandlers() {
        addPersonnelButton.setOnAction(event -> handleAdmitPersonnel());
        clearPersonnelButton.setOnAction(event -> clearPersonnelFields());
        deletePersonnelButton.setOnAction(event -> handleDeletePersonnel());
        savePersonnelButton.setOnAction(event -> handleSavePersonnel());
    }

    /**
     * Initializes the personnel table by setting up the cell value factories for each column.
     */
    private void initializePersonnelTable() {
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("personnelId"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("personnelName"));
        employeeEmailColumn.setCellValueFactory(new PropertyValueFactory<>("personnelEmail"));
        employeeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("personnelAddress"));
        employeeContactColumn.setCellValueFactory(new PropertyValueFactory<>("personnelContact"));
        emergencyContactColumn.setCellValueFactory(new PropertyValueFactory<>("emergencyContact"));
        jobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("vetSpec"));
        certificationColumn.setCellValueFactory(new PropertyValueFactory<>("certification"));
        workScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("workSched"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        perfRatingColumn.setCellValueFactory(new PropertyValueFactory<>("perfRating"));
        attRatingColumn.setCellValueFactory(new PropertyValueFactory<>("attendRating"));
    }

    /**
      * Enables the selection of a personnel in the personnel table view.
      * Adds a listener to the selected item property of the personnel table view's selection model.
      * When a new selection is made, it calls the handleTableRowSelection method with the new selection.
      * Sets an on-click event handler for the personnel table view.
      * If the primary mouse button is clicked once, it checks if the selected personnel is the same as the last selected personnel.
      * If they are the same, it clears the selection, sets the last selected personnel to null, and clears the personnel fields.
      * If they are different, it sets the last selected personnel to the selected personnel.
      * If the primary mouse button is clicked twice, it calls the handleTableRowSelection method with the selected item.
      *
      */
    private void enablePersonnelTableSelection() {
        personnelTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> handleTableRowSelection(newSelection));

        personnelTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Personnel selectedPersonnel = personnelTableView.getSelectionModel().getSelectedItem();

                if (selectedPersonnel != null && selectedPersonnel.equals(lastSelectedPersonnel)) {
                    personnelTableView.getSelectionModel().clearSelection();
                    lastSelectedPersonnel = null;
                    clearPersonnelFields();
                } else {
                    lastSelectedPersonnel = selectedPersonnel;
                }
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleTableRowSelection(personnelTableView.getSelectionModel().getSelectedItem());
            }
        });
    }

    /**
     * Handles the selection of a table row.
     *
     * @param  selectedPersonnel  the selected personnel object
     */    
    @FXML
    private void handleTableRowSelection(Personnel selectedPersonnel) {
        if (selectedPersonnel != null) {
            lastSelectedPersonnel = selectedPersonnel;
            displayPersonnelFields(selectedPersonnel);
            deletePersonnelButton.setDisable(false);
            savePersonnelButton.setDisable(false);
        } else {
            clearPersonnelFields();
            deletePersonnelButton.setDisable(true);
            savePersonnelButton.setDisable(true);
            lastSelectedPersonnel = null;
        }
    }

    /**
     * Sets the image for icons.
     */
    private void setImageForIcons() {
        Image addPersonnelIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Add Employee Icon.png"));
        Image managePersonnelIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Manage Employee Icon.png"));
        addEmpIconViewer.setImage(addPersonnelIcon);
        manageEmpIconViewer.setImage(managePersonnelIcon);
    }

    /**
     * Handles the action when admitting a new personnel.
     */
    @FXML
    private void handleAdmitPersonnel() {
        try {
            // Retrieve input values from the form fields
            String employeeName = employeeNameField.getText();
            String employeeEmail = employeeEmailField.getText();
            String employeeAddress = employeeAddressField.getText();
            String employeeContact = employeeContactField.getText();
            String emergencyContact = emergencyContactField.getText();
            String jobTitle = jobTitleMenu.getText();
            String specialization = vetSpecMenu.getText();
            String certification = employeeCertificationField.getText();
            LocalDate hireDate = employeeHireDateField.getValue();
            String workSchedule = workScheduleField.getText();
            BigDecimal performanceRating = ParseUtils.parseBigDecimal(perfRatingField.getText());
            BigDecimal attendanceRating = ParseUtils.parseBigDecimal(attRatingField.getText());

            // Validate if required fields are not empty
            if (ValidationUtils.isNullOrEmpty(
                    employeeName, employeeEmail, employeeAddress, employeeContact, emergencyContact, jobTitle,
                    certification, workSchedule) || hireDate == null) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate employee names
            if (!ValidationUtils.isValidName(employeeName)) {
                AlertUtils.showErrorAlert("Error", "Invalid employee name. Please use only letters and spaces.");
                return;
            }

            // Validate employee email
            if (!ValidationUtils.isValidEmail(employeeEmail)) {
                AlertUtils.showErrorAlert("Error", "Invalid employee email address.");
                return;
            }

            // Validate employee contact number
            if (!ValidationUtils.isValidInteger(employeeContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid employee contact number.");
                return;
            }

            // Validate employee emergency contact number
            if (!ValidationUtils.isValidInteger(emergencyContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid employee emergency contact number.");
                return;
            }

            // Validate employee certifications
            if ("Veterinarian".equals(jobTitle) && specialization == null) {
                AlertUtils.showErrorAlert("Error", "Specialization is required for Veterinarians.");
                return;
            }

            if (Arrays.asList("Veterinarian", "Veterinary Technician", "Laboratory Technician", "Radiology Technician", "Pharmacy Technician")
                    .contains(jobTitle) && certification != null && certification.equalsIgnoreCase("n/a")) {
                AlertUtils.showErrorAlert("Error", "Certification cannot be 'n/a' for the selected job title.");
                return;
            }

            // Validate numeric fields for correct format and greater than zero
            if (performanceRating != null && !ValidationUtils.isValidNumber(performanceRating)) {
                AlertUtils.showErrorAlert("Error", "Invalid format for performance rating. Please enter a valid number.");
                return;
            }

            if (attendanceRating != null && !ValidationUtils.isValidNumber(attendanceRating)) {
                AlertUtils.showErrorAlert("Error", "Invalid format for attendance rating. Please enter a valid number.");
                return;
            }

            // Validate the hire date format
            if (!ValidationUtils.isValidDateFormat(hireDate.toString())) {
                AlertUtils.showErrorAlert("Error", "Invalid hire date format. Please use the correct format.");
                return;
            }

            // Create a new Personnel object with specified details
            Personnel newPersonnel = new Personnel(
                GenerateRandomIds.generateRandomId(10), employeeName, employeeEmail, employeeAddress, employeeContact,
                emergencyContact, jobTitle, specialization, certification, hireDate, workSchedule,
                performanceRating, attendanceRating
            );


            if (personnelDao.isPersonnelExists(newPersonnel)) {
                AlertUtils.showErrorAlert("Error", "Personnel with the same name and job title already exists.");
                return;
            }

            personnelTableView.getItems().add(newPersonnel);
            personnelDao.savePersonnel(newPersonnel);
            clearPersonnelFields();

            AlertUtils.showInformationAlert("Success", "Personnel admitted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }

    /**
     * Handles the action when editing personnel information.
     */
    @FXML
    private void handleSavePersonnel() {
        try {
            // Get the selected personnel from the table view
            Personnel selectedPersonnel = personnelTableView.getSelectionModel().getSelectedItem();

            // Get updated values from the form fields
            String updatedEmployeeName = employeeNameField.getText();
            String updatedEmployeeEmail = employeeEmailField.getText();
            String updatedEmployeeAddress = employeeAddressField.getText();
            String updatedEmployeeContact = employeeContactField.getText();
            String updatedEmergencyContact = emergencyContactField.getText();
            String updatedJobTitle = jobTitleMenu.getText();
            String updatedSpecialization = vetSpecMenu.getText();
            String updatedCertification = employeeCertificationField.getText();
            LocalDate updatedHireDate = employeeHireDateField.getValue();
            String updatedWorkSchedule = workScheduleField.getText();
            BigDecimal updatedPerformanceRating = ParseUtils.parseBigDecimal(perfRatingField.getText());
            BigDecimal updatedAttendanceRating = ParseUtils.parseBigDecimal(attRatingField.getText());

            // Validate if required fields are not empty
            if (ValidationUtils.isNullOrEmpty(
                    updatedEmployeeName, updatedEmployeeEmail, updatedEmployeeAddress, updatedEmployeeContact,
                    updatedEmergencyContact, updatedJobTitle, updatedCertification, updatedWorkSchedule)
                    || updatedHireDate == null) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate employee names
            if (!ValidationUtils.isValidName(updatedEmployeeName)) {
                AlertUtils.showErrorAlert("Error", "Invalid employee name. Please use only letters and spaces.");
                return;
            }

            // Validate employee email
            if (!ValidationUtils.isValidEmail(updatedEmployeeEmail)) {
                AlertUtils.showErrorAlert("Error", "Invalid employee email address.");
                return;
            }

            // Validate employee contact number
            if (!ValidationUtils.isValidInteger(updatedEmployeeContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid employee contact number.");
                return;
            }

            // Validate employee emergency contact number
            if (!ValidationUtils.isValidInteger(updatedEmergencyContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid employee emergency contact number.");
                return;
            }

            // Validate specialization for Veterinarians
            if ("Veterinarian".equals(updatedJobTitle) && updatedSpecialization == null) {
                AlertUtils.showErrorAlert("Error", "Specialization is required for Veterinarians.");
                return;
            }

            // Validate performance and attendance ratings
            if (updatedPerformanceRating != null && !ValidationUtils.isValidNumber(updatedPerformanceRating)
                    || updatedAttendanceRating != null && !ValidationUtils.isValidNumber(updatedAttendanceRating)) {
                AlertUtils.showErrorAlert("Error", "Invalid format for rating. Please enter a valid number.");
                return;
            }

            // Validate the hire date format
            if (!ValidationUtils.isValidDateFormat(updatedHireDate.toString())) {
                AlertUtils.showErrorAlert("Error", "Invalid hire date format. Please use the correct format.");
                return;
            }

            // Check if any changes were made
            if (!selectedPersonnel.getPersonnelName().equals(updatedEmployeeName)
                    || !selectedPersonnel.getPersonnelEmail().equals(updatedEmployeeEmail)
                    || !selectedPersonnel.getPersonnelAddress().equals(updatedEmployeeAddress)
                    || !selectedPersonnel.getPersonnelContact().equals(updatedEmployeeContact)
                    || !selectedPersonnel.getEmergencyContact().equals(updatedEmergencyContact)
                    || !selectedPersonnel.getJobTitle().equals(updatedJobTitle)
                    || !selectedPersonnel.getVetSpec().equals(updatedSpecialization)
                    || !selectedPersonnel.getCertification().equals(updatedCertification)
                    || !selectedPersonnel.getHireDate().equals(updatedHireDate)
                    || !selectedPersonnel.getWorkSched().equals(updatedWorkSchedule)
                    || !Objects.equals(selectedPersonnel.getPerfRating(), updatedPerformanceRating)
                    || !Objects.equals(selectedPersonnel.getAttendRating(), updatedAttendanceRating)) {

                // Create a new Personnel object with updated values
                Personnel updatedPersonnel = new Personnel(
                        selectedPersonnel.getPersonnelId(),
                        updatedEmployeeName, updatedEmployeeEmail, updatedEmployeeAddress, updatedEmployeeContact,
                        updatedEmergencyContact, updatedJobTitle, updatedSpecialization, updatedCertification,
                        updatedHireDate, updatedWorkSchedule, updatedPerformanceRating, updatedAttendanceRating
                );

                // Update the personnel in the table view, refresh the table, and show success message
                personnelTableView.getItems().set(personnelTableView.getSelectionModel().getSelectedIndex(), updatedPersonnel);
                personnelDao.updatePersonnel(updatedPersonnel);
                clearPersonnelFields();
                refreshPersonnelTable();
                AlertUtils.showInformationAlert("Success", "Personnel updated successfully.");
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
     * Handles the action when deleting an employee.
     */
    @FXML
    private void handleDeletePersonnel() {
        Personnel selectedPersonnel = personnelTableView.getSelectionModel().getSelectedItem();

        boolean confirmed = AlertUtils.showConfirmationPrompt("Are you sure you want to delete this personnel?");

        if (confirmed) {
            try {
                personnelTableView.getItems().remove(selectedPersonnel);
                personnelDao.deletePersonnel(selectedPersonnel);
                clearPersonnelFields();
                AlertUtils.showInformationAlert("Success", "Personnel deleted successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
            }
        }
    }

    /**
     * Displays the personnel fields on the UI based on the given Personnel object.
     *
     * @param personnel the Personnel object containing the personnel information
     */
    private void displayPersonnelFields(Personnel personnel) {
        employeeNameField.setText(personnel.getPersonnelName());
        employeeEmailField.setText(personnel.getPersonnelEmail());
        employeeAddressField.setText(personnel.getPersonnelAddress());
        employeeContactField.setText(personnel.getPersonnelContact());
        emergencyContactField.setText(personnel.getEmergencyContact());
        jobTitleMenu.setText(personnel.getJobTitle());
        vetSpecMenu.setText(personnel.getVetSpec());
        employeeCertificationField.setText(personnel.getCertification());
        workScheduleField.setText(personnel.getWorkSched());
        employeeHireDateField.setValue(personnel.getHireDate());
    
        if (personnel.getPerfRating() == null || personnel.getAttendRating() == null) {
            exemptRatingCB.setSelected(true);
            perfRatingField.clear();
            attRatingField.clear();
        } else {
            exemptRatingCB.setSelected(false);
            perfRatingField.setText(String.valueOf(personnel.getPerfRating()));
            attRatingField.setText(String.valueOf(personnel.getAttendRating()));
        }
    }

    /**
     * Populates the personnel table with data from the database.
     */
    private void populatePersonnelTable() {
        try {
            ObservableList<Personnel> personnelList = FXCollections.observableArrayList();
    
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM personnel");
                 ResultSet resultSet = statement.executeQuery()) {
    
                while (resultSet.next()) {
                    Personnel personnel = new Personnel();
                    personnel.setPersonnelId(resultSet.getString("personnel_id"));
                    personnel.setPersonnelName(resultSet.getString("personnel_name"));
                    personnel.setPersonnelEmail(resultSet.getString("personnel_email"));
                    personnel.setPersonnelAddress(resultSet.getString("personnel_address"));
                    personnel.setPersonnelContact(resultSet.getString("personnel_contact"));
                    personnel.setEmergencyContact(resultSet.getString("emergency_contact"));
                    personnel.setJobTitle(resultSet.getString("job_title"));
                    personnel.setVetSpec(resultSet.getString("vet_specialization"));
                    personnel.setCertification(resultSet.getString("personnel_certification"));
                    personnel.setWorkSched(resultSet.getString("work_schedule"));
                    personnel.setHireDate(resultSet.getDate("hire_date").toLocalDate());
                    String perfRatingString = resultSet.getString("performance_rating");
                    personnel.setPerfRating(perfRatingString != null ? new BigDecimal(perfRatingString) : null);
                    String attRatingString = resultSet.getString("attendance_rating");
                    personnel.setAttendRating(attRatingString != null ? new BigDecimal(attRatingString) : null);
    
                    personnelList.add(personnel);
                }
            }
    
            if (personnelList.isEmpty()) {
                personnelTableView.setPlaceholder(new Label("No personnel found in the database."));
            } else {
                personnelTableView.setItems(personnelList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Refreshes the personnel table by clearing the items and populating it again.
     */
    private void refreshPersonnelTable() {
        personnelTableView.getItems().clear();
        populatePersonnelTable();
    }

    /**
     * Initializes the job title menu with a list of available job titles.
     */    
    private void initializeJobTitleMenu() {
        jobTitleMenu.getItems().clear();
        jobTitleMenu.getItems().addAll(
            new MenuItem("Veterinarian"),
            new MenuItem("Veterinary Technician"),
            new MenuItem("Veterinary Assistant"),
            new MenuItem("Receptionist"),
            new MenuItem("Practice Owner"),
            new MenuItem("Practice Manager"),
            new MenuItem("Accountant"),
            new MenuItem("Bookkeeper"),
            new MenuItem("Kennel Attendant"),
            new MenuItem("Groomer"),
            new MenuItem("Laboratory Technician"),
            new MenuItem("Radiology Technician"),
            new MenuItem("Client Service Representative"),
            new MenuItem("Pharmacy Technician"),
            new MenuItem("IT Manager"),
            new MenuItem("System Administrator"),
            new MenuItem("IT Support Technician"),
            new MenuItem("Custodian")
        );
        jobTitleMenu.getItems().forEach(item -> item.setOnAction(event -> handleJobTitleMenuItem(item.getText())));
    }

    /**
     * Handles the job title menu item by setting the text to the given job title.
     *
     * @param  jobTitle  the job title to be set
     */
    private void handleJobTitleMenuItem(String jobTitle) {
        jobTitleMenu.setText(jobTitle);
    }

    /**
     * Initializes the specialization menu.
     */    
    private void initializeSpecializationMenu() {
        vetSpecMenu.getItems().clear();
        vetSpecMenu.getItems().addAll(
            new MenuItem("n/a"),
            new MenuItem("General Practitioner"),
            new MenuItem("Veterinary Surgery"),
            new MenuItem("Veterinary Internal Medicine"),
            new MenuItem("Veterinary Dermatology"),
            new MenuItem("Veterinary Oncology"),
            new MenuItem("Veterinary Radiology"),
            new MenuItem("Veterinary Neurology"),
            new MenuItem("Veterinary Cardiology"),
            new MenuItem("Veterinary Ophthalmology"),
            new MenuItem("Veterinary Dentistry"),
            new MenuItem("Veterinary Anesthesiology"),
            new MenuItem("Emergency and Critical Care"),
            new MenuItem("Veterinary Behavior"),
            new MenuItem("Equine Medicine/Surgery"),
            new MenuItem("Zoological Medicine"),
            new MenuItem("Theriogenology"),
            new MenuItem("Veterinary Nutrition"),
            new MenuItem("Veterinary Pathology"),
            new MenuItem("Veterinary Preventive Medicine")
        );
        vetSpecMenu.getItems().forEach(item -> item.setOnAction(event -> handleSpecializationMenuItem(item.getText())));
    }
    
    /**
     * A description of the entire Java function.
     *
     * @param  specialization   description of parameter
     */
    private void handleSpecializationMenuItem(String specialization) {
        vetSpecMenu.setText(specialization);
    }

    /**
     * Handles the exempt rating checkbox.
     *
     * @param  observable   
     * @param  oldValue    
     * @param  newValue             
     */
    private void handleExemptRating() {
        exemptRatingCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            perfRatingField.setDisable(newValue);
            attRatingField.setDisable(newValue);
        });
    }
    
    /**
     * Clears all the personnel fields.
     */
    private void clearPersonnelFields() {
        employeeNameField.clear();
        employeeEmailField.clear();
        employeeAddressField.clear();
        employeeContactField.clear();
        emergencyContactField.clear();
        employeeCertificationField.clear();
        workScheduleField.clear();
        employeeHireDateField.setValue(null);
        perfRatingField.clear();
        attRatingField.clear();
        exemptRatingCB.setSelected(false);
        jobTitleMenu.setText("Job Title");
        vetSpecMenu.setText("Specialization");
    }

    /**
     * Adds hover effect to the specified buttons.
     */    
    private void addHoverEffect() {
        EffectsUtils.addHoverEffect(addPersonnelButton);
        EffectsUtils.addHoverEffect(savePersonnelButton);
        EffectsUtils.addHoverEffect(deletePersonnelButton);
        EffectsUtils.addHoverEffect(clearPersonnelButton);
    }

}
