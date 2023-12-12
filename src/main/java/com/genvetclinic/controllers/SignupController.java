package com.genvetclinic.controllers;

import com.genvetclinic.models.Admin;
import com.genvetclinic.services.ActivationCodeDao;
import com.genvetclinic.services.AdminDao;
import com.genvetclinic.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * The {@code SignupController} class controls the user signup functionality of the veterinary clinic application.
 * It handles the process of creating a new user account with the provided information.
 *
 * <p>This controller uses JavaFX components, such as TextField and PasswordField, to collect user input for signup.
 * It interacts with the {@link com.genvetclinic.services.AdminDao} class to create a new user account.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class SignupController {

    private final AdminDao adminDao;

    // FXML annotated fields for UI elements
    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private MenuButton securityQuestionMenu;
    @FXML private TextField newEmailField;
    @FXML private TextField securityAnswerField;
    @FXML private TextField activationField;
    @FXML private Button newSignupButton;
    @FXML private Button cancelSignUpButton;
    @FXML private Button clearSignupButton;

    /**
     * Constructor for SignupController.
     */
    public SignupController() {
        this.adminDao = initializeAdminDao();
    }

    /**
     * Initializes the AdminDao.
     *
     * @return An instance of AdminDao.
     */
    private AdminDao initializeAdminDao() {
        try {
            return new AdminDao();
        } catch (SQLException e) {
            handleSQLException(e);
            return null; // Or throw a custom exception depending on your design
        }
    }

    /**
     * Initializes the SignupController.
     */
    @FXML
    private void initialize() {
        initializeSecurityQuestionsMenu();
        setupButtonEventHandlers();
        addHoverEffects();
    }

    /**
     * Initializes the security questions menu with predefined questions.
     */
    private void initializeSecurityQuestionsMenu() {
        securityQuestionMenu.getItems().addAll(
                createSecurityQuestionItem("What is your dream car/motorcycle?"),
                createSecurityQuestionItem("What was the name of your childhood pet?"),
                createSecurityQuestionItem("What year was your grandmother born?"),
                createSecurityQuestionItem("What was the first concert you attended?"),
                createSecurityQuestionItem("In what city or town did your parents meet?"),
                createSecurityQuestionItem("What was your favorite toy as a child?"),
                createSecurityQuestionItem("What was the last name of your favorite professor?"),
                createSecurityQuestionItem("As a child, what did you want to be when you grew up?"),
                createSecurityQuestionItem("Who was your childhood hero?"),
                createSecurityQuestionItem("What is your favorite song?")
        );

        // Set event handlers for each menu item
        securityQuestionMenu.getItems().forEach(item -> item.setOnAction(event -> handleSecurityQuestionMenuItem(item.getText())));
    }

    /**
     * Creates a MenuItem for a security question.
     *
     * @param question The security question.
     * @return The created MenuItem.
     */
    private MenuItem createSecurityQuestionItem(String question) {
        MenuItem menuItem = new MenuItem(question);
        menuItem.setOnAction(event -> handleSecurityQuestionMenuItem(question));
        return menuItem;
    }

    /**
     * Set up event handlers for buttons.
     */
    private void setupButtonEventHandlers() {
        newSignupButton.setOnAction(event -> handleSignup());
        cancelSignUpButton.setOnAction(event -> handleCancel());
        clearSignupButton.setOnAction(event -> clearInputFields());
    }

    /**
     * Adds hover effects to buttons.
     */
    private void addHoverEffects() {
        EffectsUtils.addHoverEffect(newSignupButton);
        EffectsUtils.addHoverEffect(cancelSignUpButton);
        EffectsUtils.addHoverEffect(clearSignupButton);
    }

    /**
     * Handles the signup button click event.
     */
    private void handleSignup() {
        try {
            Admin admin = createAdminFromInput();
    
            // Validate user input
            if (!checkIfNull(admin)) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields.");
                return;
            }

            // Validate password minimum characters
            if (ValidationUtils.isTooShort(admin.getPassword(), 6)) {
                AlertUtils.showErrorAlert("Error", "Password needs a minimum of 6 characters.");
                return;
            }

            // Validate email
            if (!ValidationUtils.isValidEmail(admin.getAdminEmail())) {
                AlertUtils.showErrorAlert("Error", "Please enter a valid email address.");
                return;
            }

            // Check if the user already exists
            if (adminDao.doesUserExist(admin.getUsername(), admin.getAdminEmail())) {
                AlertUtils.showErrorAlert("Error", "Admin already exists.");
                return;
            }
    
            ActivationCodeDao activationCodeDao = new ActivationCodeDao();
    
            // Check if the activation code exists in the database
            if (!activationCodeDao.isActivationCodeExists(admin.getAdminCode())) {
                AlertUtils.showErrorAlert("Error", "Invalid activation code.");
                return;
            }
    
            // Check if the activation code is active
            if (activationCodeDao.isCodeUsed(admin.getAdminCode())) {
                AlertUtils.showErrorAlert("Error", "Activation code has already expired.");
                return;
            }
    
            // Update the activation code status to 'Expired'
            activationCodeDao.updateActivationCodeStatus(admin.getAdminCode(), "Expired");
    
            // Save the admin
            adminDao.save(admin);
            clearInputFields();
            AlertUtils.showSuccessAlert("Signup Successful", "Your account has been created successfully. Please return to login to access your account.");
            closeCurrentStage();
            WindowUtils.openWindow("Login", "vcms-login.fxml");
    
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }    

    /**
     * Creates an Admin object from input fields.
     *
     * @return The created Admin object.
     */
    private Admin createAdminFromInput() {
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();
        String securityQuestion = securityQuestionMenu.getText();
        String email = newEmailField.getText();
        String securityAnswer = securityAnswerField.getText();
        String activationCode = activationField.getText();

        String adminId = GenerateRandomIds.generateRandomId(10);

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setSecurityQuestion(securityQuestion);
        admin.setAdminEmail(email);
        admin.setSecurityAnswer(securityAnswer);
        admin.setAdminCode(activationCode);
        admin.setAdminId(adminId);

        return admin;
    }

    /**
     * Validates the input fields.
     *
     * @param admin The Admin object to validate.
     * @return True if the input is valid, false otherwise.
     */
    private boolean checkIfNull(Admin admin) {
        return !ValidationUtils.isNullOrEmpty(admin.getUsername()) &&
                !ValidationUtils.isNullOrEmpty(admin.getPassword()) &&
                !ValidationUtils.isNullOrEmpty(admin.getSecurityQuestion()) &&
                !ValidationUtils.isNullOrEmpty(admin.getAdminEmail()) &&
                !ValidationUtils.isNullOrEmpty(admin.getSecurityAnswer()) &&
                !ValidationUtils.isNullOrEmpty(admin.getAdminCode());
    }

    /**
     * Handles the cancel button click event.
     */
    private void handleCancel() {
        clearInputFields();
        closeCurrentStage();
        WindowUtils.openWindow("Login", "vcms-login.fxml");
    }

    /**
     * Clears all input fields.
     */
    private void clearInputFields() {
        newUsernameField.clear();
        newPasswordField.clear();
        securityQuestionMenu.setText("Pick security question...");
        newEmailField.clear();
        securityAnswerField.clear();
        activationField.clear();
    }

    /**
     * Handles the selection of a security question from the menu.
     *
     * @param securityQuestion The selected security question.
     */
    private void handleSecurityQuestionMenuItem(String securityQuestion) {
        securityQuestionMenu.setText(securityQuestion);
    }

    /**
     * Handles an SQL exception by printing the stack trace and showing an error alert.
     *
     * @param e The SQLException.
     */
    private void handleSQLException(SQLException e) {
        e.printStackTrace();
        AlertUtils.showErrorAlert("Error", "An error occurred during signup. Please try again.");
    }

    /**
     * Closes the current stage.
     */
    private void closeCurrentStage() {
        Stage stage = (Stage) newSignupButton.getScene().getWindow();
        stage.close();
    }
}
