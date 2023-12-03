package com.genvetclinic.controllers;

import java.sql.*;
import java.time.*;
import java.time.format.*;
import com.genvetclinic.models.Appointment;
import com.genvetclinic.services.*;
import com.genvetclinic.utils.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.image.*;

/**
 * The {@code AppointmentsPanelController} class controls the Appointments Panel of the veterinary clinic application.
 * It manages the display and interaction with the appointments data.
 *
 * <p>This controller utilizes JavaFX components, such as TableView and TableColumn, to present a list of appointments.
 * It interacts with the {@link com.genvetclinic.services.AppointmentDao} class to retrieve and update appointment data from the database.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class AppointmentsPanelController {

    private AppointmentDao appointmentDao;
    private DatabaseConnection databaseConnection;
    private Appointment lastSelectedAppointment;

    @FXML private TextField clientNameField;
    @FXML private TextField clientContactField;
    @FXML private MenuButton serviceRequiredMenu;
    @FXML private TextField assignedPersonnelField;
    @FXML private DatePicker appointmentDateField;
    @FXML private TextField appointmentTimeField;
    @FXML private MenuButton appointmentStatusMenu;
    @FXML private Button createAppointmentButton;
    @FXML private Button clearAppointmentButton;
    @FXML private ImageView createApptconViewer;
    @FXML private ImageView manageApptIconViewer;
    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, String> appointmentIdColumn;
    @FXML private TableColumn<Appointment, String> clientNameColumn;
    @FXML private TableColumn<Appointment, String> clientContactColumn;
    @FXML private TableColumn<Appointment, String> serviceRequiredColumn;
    @FXML private TableColumn<Appointment, String> assignedPersonnelColumn;
    @FXML private TableColumn<Appointment, String> appointmentDateColumn;
    @FXML private TableColumn<Appointment, String> appointmentTimeColumn;
    @FXML private TableColumn<Appointment, String> appointmentStatusColumn;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button saveAppointmentButton;

    /**
     * Initializes the Appointments Panel Controller.
     */
    public AppointmentsPanelController() {
        try {
            this.databaseConnection = new DatabaseConnection();
            this.appointmentDao = new AppointmentDao(databaseConnection);
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
        
        initializeAppointmentTable();
        populateAppointmentTable();
        enableAppointmentTableSelection();
        initializeAppStatusMenu();
        initializeServiceMenu();
        initializeButtons();
        setupEventHandlers();
        setImageForIcons();
        addHoverEffect();

        EffectsUtils.setTableSelectionColor(appointmentTableView, "#358856");
    }

    /**
     * Initializes the buttons in the Java function.
     */    
    private void initializeButtons() {
        deleteAppointmentButton.setDisable(true);
        saveAppointmentButton.setDisable(true);
    }

    /**
     * Sets up the event handlers for the createAppointmentButton, clearAppointmentButton,
     * deleteAppointmentButton, and saveAppointmentButton.
     *
     * @param event the event object that triggered the action
     */
    private void setupEventHandlers() {
        createAppointmentButton.setOnAction(event -> handleCreateAppointment());
        clearAppointmentButton.setOnAction(event -> clearAppointmentFields());
        deleteAppointmentButton.setOnAction(event -> handleDeleteAppointment());
        saveAppointmentButton.setOnAction(event -> handleSaveAppointment());
    }

    /**
     * Initializes the appointments table by setting up the cell value factories for each column.
     */
    private void initializeAppointmentTable() {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        clientContactColumn.setCellValueFactory(new PropertyValueFactory<>("clientContact"));
        serviceRequiredColumn.setCellValueFactory(new PropertyValueFactory<>("serviceRequired"));
        assignedPersonnelColumn.setCellValueFactory(new PropertyValueFactory<>("assignedPersonnel"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        appointmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStatus"));
    }

    /**
      * Enables the selection of an appointment in the appointment table view.
      * Adds a listener to the selected item property of the appointment table view's selection model.
      * When a new selection is made, it calls the handleTableRowSelection method with the new selection.
      * Sets an on-click event handler for the appointment table view.
      * If the primary mouse button is clicked once, it checks if the selected appointment is the same as the last selected appointment.
      * If they are the same, it clears the selection, sets the last selected patient to null, and clears the appointment fields.
      * If they are different, it sets the last selected appointment to the selected appointment.
      * If the primary mouse button is clicked twice, it calls the handleTableRowSelection method with the selected item.
      *
      */
    private void enableAppointmentTableSelection() {
        appointmentTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> handleTableRowSelection(newSelection));

        appointmentTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
            
                if (selectedAppointment != null && selectedAppointment.equals(lastSelectedAppointment)) {
                    appointmentTableView.getSelectionModel().clearSelection();
                    lastSelectedAppointment = null;
                    clearAppointmentFields();
                } else {
                    lastSelectedAppointment = selectedAppointment;
                }
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleTableRowSelection(appointmentTableView.getSelectionModel().getSelectedItem());
            }
        });   
    }

    /**
     * Handles the selection of a table row.
     *
     * @param  selectedAppointment  the selected appointment object
     */    
    private void handleTableRowSelection(Appointment selectedAppointment) {
        if (selectedAppointment != null) {
            lastSelectedAppointment = selectedAppointment;
            displayAppointmentFields(selectedAppointment);
            deleteAppointmentButton.setDisable(false);
            saveAppointmentButton.setDisable(false);
        } else {
            clearAppointmentFields();
            deleteAppointmentButton.setDisable(true);
            saveAppointmentButton.setDisable(true);
            lastSelectedAppointment = null;
        }
    }

    /**
     * Sets the image for icons.
     */
    private void setImageForIcons() {
        Image createApptIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Add Appointment Icon.png"));
        Image manageApptIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Manage Appointment Icon.png"));
        createApptconViewer.setImage(createApptIcon);
        manageApptIconViewer.setImage(manageApptIcon);
    }

    /**
     * Handles the action when creating a new appointment.
     */
    @FXML
    private void handleCreateAppointment() {
        try {
            // Get input values from the form fields
            String clientName = clientNameField.getText();
            String clientContact = clientContactField.getText();
            String serviceRequired = serviceRequiredMenu.getText();
            String assignedPersonnel = assignedPersonnelField.getText();
            String appointmentStatus = appointmentStatusMenu.getText();
            LocalDate appointmentDate = ParseUtils.parseLocalDate(appointmentDateField.getValue().toString());
            LocalTime appointmentTime = ParseUtils.parseLocalTime(appointmentTimeField.getText());

            // Validate if required fields are not empty
            if (ValidationUtils.isNullOrEmpty(clientName, clientContact, serviceRequired, assignedPersonnel)) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate client name
            if (!ValidationUtils.isValidName(clientName)) {
                AlertUtils.showErrorAlert("Error", "Invalid client name. Please use only letters and spaces.");
                return;
            }

            // Validate assigned personnel name
            if (!ValidationUtils.isValidName(assignedPersonnel)) {
                AlertUtils.showErrorAlert("Error", "Invalid assigned personnel name. Please use only letters and spaces.");
                return;
            }

            // Validate client contact number
            if (!ValidationUtils.isValidInteger(clientContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid client contact number.");
                return;
            }

            // Validate time and date format and check if it's in the past for today's appointments
            if (appointmentTime == null || appointmentDate == null) {
                AlertUtils.showErrorAlert("Error", "Invalid date or time format. Please use the correct format.");
                return;
            }

            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            // Check if the appointment is in the past
            if (appointmentDate.isBefore(currentDate) || (appointmentDate.isEqual(currentDate) && appointmentTime.isBefore(currentTime))) {
                AlertUtils.showErrorAlert("Error", "Invalid date or time. Please select a date and time in the future.");
                return;
            }

            // Check if the appointment already exists
            if (appointmentDao.isAppointmentExists(clientName, assignedPersonnel, serviceRequired, appointmentDate, appointmentTime)) {
                AlertUtils.showErrorAlert("Error", "An appointment with the same client, personnel, date, and time already exists.");
                return;
            }

            // Generate a random appointment ID
            String appointmentId = GenerateRandomIds.generateRandomId(10);

            // Create a new Appointment object and set its properties
            Appointment appointment = new Appointment(appointmentId, clientName, clientContact, serviceRequired,
                    assignedPersonnel, appointmentDate, appointmentTime, appointmentStatus);

            // Add the appointment to the table view
            appointmentTableView.getItems().add(appointment);

            // Save the appointment and show success message
            appointmentDao.saveAppointment(appointment);
            clearAppointmentFields();
            AlertUtils.showInformationAlert("Success", "Appointment created successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }

    /**
     * Handles the action when editing an appointment information.
     */
    @FXML
    private void handleSaveAppointment() {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        try {
            String updatedClientName = clientNameField.getText();
            String updatedClientContact = clientContactField.getText();
            String updatedServiceRequired = serviceRequiredMenu.getText();
            String updatedAssignedPersonnel = assignedPersonnelField.getText();
            String updatedAppointmentStatus = appointmentStatusMenu.getText();
            LocalDate updatedAppointmentDate = ParseUtils.parseLocalDate(appointmentDateField.getValue().toString());
            LocalTime updatedAppointmentTime = ParseUtils.parseLocalTime(appointmentTimeField.getText());

            // Validate if required fields are not empty
            if (ValidationUtils.isNullOrEmpty(updatedClientName, updatedClientContact, updatedServiceRequired, updatedAssignedPersonnel)) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate client name
            if (!ValidationUtils.isValidName(updatedClientName)) {
                AlertUtils.showErrorAlert("Error", "Invalid client name. Please use only letters and spaces.");
                return;
            }

            // Validate assigned personnel name
            if (!ValidationUtils.isValidName(updatedAssignedPersonnel)) {
                AlertUtils.showErrorAlert("Error", "Invalid assigned personnel name. Please use only letters and spaces.");
                return;
            }

            // Validate client contact number
            if (!ValidationUtils.isValidInteger(updatedClientContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid client contact number.");
                return;
            }

            // Validate time and date format
            if (updatedAppointmentTime == null) {
                AlertUtils.showErrorAlert("Error", "Invalid time format. Please use the correct format.");
                return;
            }

            if (updatedAppointmentDate == null) {
                AlertUtils.showErrorAlert("Error", "Invalid date format. Please use the correct format.");
                return;
            }

            // Check if any changes were made
            if (!selectedAppointment.getClientName().equals(updatedClientName) || !selectedAppointment.getClientContact().equals(updatedClientContact)
                    || !selectedAppointment.getServiceRequired().equals(updatedServiceRequired) || !selectedAppointment.getAssignedPersonnel().equals(updatedAssignedPersonnel)
                    || !selectedAppointment.getAppointmentStatus().equals(updatedAppointmentStatus) || !selectedAppointment.getAppointmentDate().equals(updatedAppointmentDate)
                    || !selectedAppointment.getAppointmentTime().equals(updatedAppointmentTime)) {

                // Update appointment information
                selectedAppointment = new Appointment(selectedAppointment.getAppointmentId(), updatedClientName,
                        updatedClientContact, updatedServiceRequired, updatedAssignedPersonnel, updatedAppointmentDate,
                        updatedAppointmentTime, updatedAppointmentStatus);

                appointmentDao.updateAppointment(selectedAppointment);
                refreshAppointmentTable();
                AlertUtils.showInformationAlert("Success", "Appointment updated successfully.");
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
     * Handles the action when deleting an appointment.
     */
    @FXML
    private void handleDeleteAppointment() {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        boolean confirmed = AlertUtils.showConfirmationPrompt("Are you sure you want to delete this appointment?");

        if (confirmed) {
            try {
                appointmentTableView.getItems().remove(selectedAppointment);
                appointmentDao.deleteAppointment(selectedAppointment);
                clearAppointmentFields();
                AlertUtils.showInformationAlert("Success", "Appointment deleted successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
            }
        }
    }

    /**
     * Sets the appointment fields with the corresponding values from the given Appointment object.
     *
     * @param  appointment    the Appointment object containing the values to be displayed
     */    
    private void displayAppointmentFields(Appointment appointment) {
        clientNameField.setText(appointment.getClientName());
        clientContactField.setText(appointment.getClientContact());
        serviceRequiredMenu.setText(appointment.getServiceRequired());
        assignedPersonnelField.setText(appointment.getAssignedPersonnel());
        appointmentDateField.setValue(appointment.getAppointmentDate());
        appointmentTimeField.setText(appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        appointmentStatusMenu.setText(appointment.getAppointmentStatus());
    }

    /**
     * Populates the appointment table with data from the database.
     */
    private void populateAppointmentTable() {
        try {
            ObservableList<Appointment> appointments = FXCollections.observableArrayList();

            try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointment");
                ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Appointment appointment = new Appointment(
                            resultSet.getString("appointment_id"),
                            resultSet.getString("client_name"),
                            resultSet.getString("client_contact"),
                            resultSet.getString("service_required"),
                            resultSet.getString("assigned_personnel"),
                            resultSet.getDate("appointment_date").toLocalDate(),
                            resultSet.getTime("appointment_time").toLocalTime(),
                            resultSet.getString("appointment_status")
                    );

                    appointments.add(appointment);
                }
            }

            if (appointments.isEmpty()) {
                appointmentTableView.setPlaceholder(new Label("No appointments found in the database."));
            } else {
                appointmentTableView.setItems(appointments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to fetch appointments from the database.");
        }
    }

    /**
     * Refreshes the appointment table by clearing and repopulating the data.
     */
    private void refreshAppointmentTable() {
        appointmentTableView.getItems().clear();
        populateAppointmentTable();
    }

    /**
     * A description of the entire Java function.
     *
     * @param  selectedService    description of parameter
     */
    @FXML
    private void handleServiceMenuItem(String selectedService) {
        serviceRequiredMenu.setText(selectedService);
    }

    /**
     * Initializes the service menu by clearing the items and adding new items.
     * The new items include diagnosis and treatment, vaccination, parasite control,
     * laboratory services, counseling, boarding, grooming, and euthanasia.
     * Also sets the action event for each item to handle the service menu item.
     */    
    private void initializeServiceMenu() {
        serviceRequiredMenu.getItems().clear();
        serviceRequiredMenu.getItems().addAll(
                new MenuItem("Diagnosis and Treatment"),
                new MenuItem("Vaccination"),
                new MenuItem("Parasite Control"),
                new MenuItem("Laboratory Services"),
                new MenuItem("Counseling"),
                new MenuItem("Boarding"),
                new MenuItem("Grooming"),
                new MenuItem("Euthanasia")
        );
        serviceRequiredMenu.getItems().forEach(item -> item.setOnAction(event -> handleServiceMenuItem(item.getText())));
    }

    /**
     * Handles the app status menu item.
     *
     * @param  selectedStatus  the selected status
     */    
    @FXML
    private void handleAppStatusMenuItem(String selectedStatus) {
        appointmentStatusMenu.setText(selectedStatus);
    }

    /**
     * Initializes the application status menu.
     */    
    private void initializeAppStatusMenu() {
        appointmentStatusMenu.getItems().clear();
        appointmentStatusMenu.getItems().addAll(
                new MenuItem("Ongoing"),
                new MenuItem("Completed"),
                new MenuItem("Cancelled"),
                new MenuItem("Set for Reschedule")
        );
        appointmentStatusMenu.getItems().forEach(item -> item.setOnAction(event -> handleAppStatusMenuItem(item.getText())));
    }

    /**
     * Clears all the appointment fields.
     */    
    private void clearAppointmentFields() {
        clientNameField.clear();
        clientContactField.clear();
        serviceRequiredMenu.setText("Select Service");
        assignedPersonnelField.clear();
        appointmentDateField.setValue(null);
        appointmentTimeField.clear();
        appointmentStatusMenu.setText("Select Status");
        appointmentTableView.getSelectionModel().clearSelection();
        lastSelectedAppointment = null;
    }

    /**
     * Adds a hover effect to the specified button.
     *
     * @param button The button to add the hover effect to.
     */
    private void addHoverEffect() {
        EffectsUtils.addHoverEffect(createAppointmentButton);
        EffectsUtils.addHoverEffect(saveAppointmentButton);
        EffectsUtils.addHoverEffect(deleteAppointmentButton);
        EffectsUtils.addHoverEffect(clearAppointmentButton);
    }

}
