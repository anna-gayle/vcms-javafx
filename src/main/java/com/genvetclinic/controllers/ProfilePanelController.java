package com.genvetclinic.controllers;

import com.genvetclinic.models.ActivationCode;
import com.genvetclinic.models.Admin;
import com.genvetclinic.services.AdminDao;
import com.genvetclinic.services.ActivationCodeDao;
import com.genvetclinic.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

/**
 * The {@code ProfilePanelController} class controls the profile panel of the veterinary clinic application.
 * It manages the display and update of the logged-in admin's profile information.
 *
 * <p>The controller uses JavaFX components, such as Label and TextField, to display and edit the admin's profile.
 * It interacts with the {@link com.genvetclinic.services.AdminDao} class to retrieve and update admin data.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class ProfilePanelController {

    private AdminDao adminDao;
    private ActivationCodeDao activationCodeDao;
    private String loggedInUsername;

    // FXML annotated fields for UI elements
    @FXML private Text helloUserText;
    @FXML private Button adminLogoutButton;
    @FXML private Text adminIdText;
    @FXML private TextField editUsernameField;
    @FXML private TextField editPasswordField;
    @FXML private TextField editEmailField;
    @FXML private MenuButton editSQMenu;
    @FXML private TextField editSecurityAnswerField;
    @FXML private Button saveAdminButton;
    @FXML private Button deleteAdminButton;
    @FXML private Button generateCodeButton;
    @FXML private TableView<ActivationCode> activationCodeTableView;
    @FXML private TableColumn<ActivationCode, String> codeColumn;
    @FXML private TableColumn<ActivationCode, String> codeStatusColumn;

    /**
     * Default constructor for the Profile Panel Controller.
     */
    public ProfilePanelController() {
    }

    /**
     * Constructor with a logged-in username parameter.
     *
     * @param loggedInUsername The username of the logged-in admin.
     */
    public ProfilePanelController(String loggedInUsername) {
        try {
            this.adminDao = new AdminDao();
            this.loggedInUsername = loggedInUsername;
            this.activationCodeDao = new ActivationCodeDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize method called when the FXML file is loaded.
     */
    @FXML
    private void initialize() {
        try {
            this.adminDao = new AdminDao();
            loadAdminDetails();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        
        setupEventHandlers();
        initializeActivationCodeTable();
        populateActivationTable();
        initializeSecurityQuestionMenu();
        addHoverEffect();
    }

    /**
     * Set up event handlers for buttons.
     */
    private void setupEventHandlers() {
        adminLogoutButton.setOnAction(event -> handleAdminLogoutButton());
        deleteAdminButton.setOnAction(event -> handleDeleteAdminButton());
        saveAdminButton.setOnAction(event -> handleSaveAdminButton());
        generateCodeButton.setOnAction(event -> handleGenerateCodeButton());
    }

    /**
     * Adds hover effects to buttons.
     */
    private void addHoverEffect() {
        EffectsUtils.addHoverEffect(adminLogoutButton);
        EffectsUtils.addHoverEffect(deleteAdminButton);
        EffectsUtils.addHoverEffect(saveAdminButton);
        EffectsUtils.addHoverEffect(generateCodeButton);
    }

    /**
     * Initializes the code table by setting up the cell value factories for each column.
     */
    private void initializeActivationCodeTable() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("activationCode"));
        codeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("codeStatus"));
    
        // Initialize the activationCodeDao if not already done
        if (activationCodeDao == null) {
            try {
                this.activationCodeDao = new ActivationCodeDao();
            } catch (SQLException e) {
                // Handle the exception more gracefully
                e.printStackTrace(); // Log the exception or display a more user-friendly message
            }
        }
    }

    /**
     * Initialize the Security Question menu with predefined questions.
     */
    private void initializeSecurityQuestionMenu() {
        editSQMenu.getItems().clear();
        editSQMenu.getItems().addAll(
                new MenuItem("What is your dream car/motorcycle?"),
                new MenuItem("What was the name of your childhood pet?"),
                new MenuItem("What year was your grandmother born?"),
                new MenuItem("What was the first concert you attended?"),
                new MenuItem("In what city or town did your parents meet?"),
                new MenuItem("What was your favorite toy as a child?"),
                new MenuItem("What was the last name of your favorite professor?"),
                new MenuItem("As a child, what did you want to be when you grew up?"),
                new MenuItem("Who was your childhood hero?"),
                new MenuItem("What is your favorite song?")
        );
        editSQMenu.getItems().forEach(item -> item.setOnAction(event -> handleEditSQMenuItem(item.getText())));
    }

    /**
     * Load details of the logged-in admin and populate the UI components.
     */
    private void loadAdminDetails() {
        try {
            List<Admin> activeAdmins = adminDao.getActiveAdmins();
            if (!activeAdmins.isEmpty()) {
                Admin admin = activeAdmins.get(0);
                helloUserText.setText("Hello " + admin.getUsername() + "!");
                adminIdText.setText(String.valueOf(admin.getAdminId()));
                editUsernameField.setText(admin.getUsername());
                editPasswordField.setText(admin.getPassword());
                editEmailField.setText(admin.getAdminEmail());
                editSQMenu.setText(admin.getSecurityQuestion());
                editSecurityAnswerField.setText(admin.getSecurityAnswer());
                loggedInUsername = admin.getUsername();
            } else {
                System.out.println("No active admins found.");
            }
        } catch (SQLException e) {
            System.out.println("Error loading active admin details: " + e.getMessage());
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to load active admin details. Please try again.");
        }
    }

    /**
     * Handle the event when the "Save" button is clicked to update admin details.
     */
    @FXML
    private void handleSaveAdminButton() {
        try {
            String newUsername = editUsernameField.getText().trim();
            String newPassword = editPasswordField.getText().trim();
            String newEmail = editEmailField.getText().trim();
            String newSecurityQuestion = editSQMenu.getText().trim();
            String newSecurityAnswer = editSecurityAnswerField.getText().trim();

            if (newUsername.isEmpty() || newPassword.isEmpty() || newEmail.isEmpty() || newSecurityQuestion.isEmpty()) {
                AlertUtils.showErrorAlert("Validation Error", "All fields must be filled out.");
                return;
            }

            if (!newUsername.equals(loggedInUsername) && adminDao.doesUsernameExist(newUsername)) {
                AlertUtils.showErrorAlert("Validation Error", "Username already exists. Please choose a different username.");
                return;
            }

            // Get the admin after the validation checks
            Admin admin = adminDao.getAdminByUsername(loggedInUsername);

            // Check if the email is valid
            if (!ValidationUtils.isValidEmail(newEmail)) {
                AlertUtils.showErrorAlert("Error", "Please enter a valid email address.");
                return;
            }

            // Check if the password meets the minimum length requirement
            int minPasswordLength = 6;
            if (ValidationUtils.isTooShort(newPassword, minPasswordLength)) {
                AlertUtils.showErrorAlert("Error", "Password is too short. It must be at least " + minPasswordLength + " characters.");
                return;
            }

            // Update admin details
            if (admin != null) {
                admin.setUsername(newUsername);
                admin.setPassword(newPassword);
                admin.setAdminEmail(newEmail);
                admin.setSecurityQuestion(newSecurityQuestion);
                admin.setSecurityAnswer(newSecurityAnswer);

                adminDao.updateAdmin(admin);

                AlertUtils.showInformationAlert("Update Successful", "Admin details updated successfully!");
                refreshProfilePanel();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to update admin details. Please try again.");
        }
    }

    /**
     * Handle the event when the "Delete" button is clicked to delete the admin account.
     */
    @FXML
    private void handleDeleteAdminButton() {
        boolean confirmed = AlertUtils.showConfirmationPrompt(
                "Are you sure you want to delete your admin account? This action cannot be undone."
        );

        if (confirmed) {
            try {
                Admin admin = adminDao.getAdminByUsername(loggedInUsername);
                if (admin != null) {
                    adminDao.setAdminActiveStatus(loggedInUsername, false);
                    adminDao.deleteAdmin(admin.getAdminId());

                    AlertUtils.showInformationAlert("Deletion Successful", "Admin account deleted successfully!");
                    closeCurrentStage();
                    WindowUtils.openWindow("Login", "vcms-login.fxml");
                } else {
                    AlertUtils.showErrorAlert("Error", "Unable to find admin account for deletion.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "Unable to delete admin account. Please try again. Error: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleGenerateCodeButton() {
        try {
            // Generate a new activation code
            String newCode = ActivationCodeGenerator.generateActivationCode();

            // Insert the new activation code into the database using an instance of ActivationCodeDao
            ActivationCodeDao activationCodeDao = new ActivationCodeDao();
            ActivationCode activationCode = new ActivationCode(newCode, "Active", "Inactive");
            activationCodeDao.insertActivationCode(activationCode);

            // Refresh the activation codes table
            populateActivationTable();
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to generate activation code. Please try again.");
        }
    }

    /**
     * Handle the event when a Security Question menu item is selected.
     *
     * @param securityQuestion The selected security question.
     */
    @FXML
    private void handleEditSQMenuItem(String securityQuestion) {
        editSQMenu.setText(securityQuestion);
    }

    /**
     * Handle the event when the "Logout" button is clicked to log out the admin.
     */
    @FXML
    private void handleAdminLogoutButton() {
        try {
            adminDao.setAdminActiveStatus(loggedInUsername, false);
            AlertUtils.showInformationAlert("Logout Successful", "You have successfully logged out.");
            closeCurrentStage();
            WindowUtils.openWindow("Login", "vcms-login.fxml");
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to set admin to inactive. Please try again.");
        }
    }

    /**
     * Populate the activation codes table with data from the database.
     */
    private void populateActivationTable() {
        try {
            ActivationCodeDao activationCodeDao = new ActivationCodeDao();
            List<ActivationCode> activationCodes = activationCodeDao.getAllActivationCodes();

            // Clear existing items
            activationCodeTableView.getItems().clear();

            // Add new items
            activationCodeTableView.getItems().addAll(activationCodes);
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to populate activation codes table. Please try again.");
        }
    }

    /**
     * Close the current stage/window.
     */
    private void closeCurrentStage() {
        Stage stage = (Stage) adminLogoutButton.getScene().getWindow();
        WindowUtils.closeWindow(stage);
    }

    /**
     * Set the logged-in username.
     *
     * @param username The username of the logged-in admin.
     */
    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }

    /**
     * Refresh the profile panel by re-initializing its contents.
     */
    public void refreshProfilePanel() {
        initialize();
    }
}
