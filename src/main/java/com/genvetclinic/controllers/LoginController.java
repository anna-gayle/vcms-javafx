package com.genvetclinic.controllers;

import com.genvetclinic.models.Admin;
import com.genvetclinic.services.*;
import com.genvetclinic.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * The {@code LoginController} class controls the login functionality of the veterinary clinic application.
 * It handles user authentication, input validation, and navigation to the main dashboard.
 *
 * <p>This controller uses JavaFX components, such as TextField and PasswordField, to collect user credentials.
 * It interacts with the {@link com.genvetclinic.services.AdminDao} class for user authentication.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class LoginController {

    private AdminDao adminDao;

    // FXML annotated fields for UI elements
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Hyperlink recoverPasswordLink;
    @FXML private ImageView loginLogoImage;

    /**
     * Constructor for LoginController.
     */
    public LoginController() {
        try {
            initializeAdminDao();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    /**
     * Initializes the AdminDao.
     *
     * @throws SQLException If an SQL exception occurs.
     */
    private void initializeAdminDao() throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        adminDao = new AdminDao(databaseConnection);
    }

    /**
     * Initializes the UI components and sets up event handlers.
     */
    @FXML
    private void initialize() {
        // Set login logo image
        setImageForLoginLogo();

        // Set up event handlers
        setupEventHandlers();

        // Add hover effects
        EffectsUtils.addHoverEffect(loginButton);
        EffectsUtils.addHoverEffect(signupButton);
    }

    /**
     * Set the image for the login logo.
     */
    private void setImageForLoginLogo() {
        Image image = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/GVC Logo Green.png"));
        loginLogoImage.setImage(image);
    }

    /**
     * Set up event handlers for buttons and hyperlinks.
     */
    private void setupEventHandlers() {
        loginButton.setOnAction(event -> handleLoginButton());
        signupButton.setOnAction(event -> handleSignupButton());
        recoverPasswordLink.setOnAction(event -> handleRecoverPasswordLink());
    }

    /**
     * Handles the login button click event.
     */
    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isInputValid(username, password)) {
            try {
                Admin admin = authenticateUser(username, password);
                if (admin != null) {
                    handleSuccessfulLogin(admin);
                } else {
                    handleFailedLogin("Invalid username or password. Please try again.");
                }
            } catch (SQLException e) {
                handleSQLException(e);
                handleFailedLogin("Failed to connect to the database.");
            }
        } else {
            AlertUtils.showErrorAlert("Login Failed", "Please enter username and password.");
        }
    }

    /**
     * Authenticates the user against the database.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return The authenticated Admin object, or null if authentication fails.
     * @throws SQLException If an SQL exception occurs.
     */
    private Admin authenticateUser(String username, String password) throws SQLException {
        Admin admin = adminDao.getAdminByUsername(username);

        if (admin != null && ValidationUtils.isUsernameMatch(username, admin.getUsername())
                && admin.getPassword().equals(password)) {
            return admin;
        } else {
            return null;
        }
    }

    /**
     * Handles a successful login.
     *
     * @param admin The authenticated Admin.
     */
    private void handleSuccessfulLogin(Admin admin) {
        try {
            adminDao.setAdminActiveStatus(admin.getUsername(), true);

            AlertUtils.showInformationAlert("Login Successful", "You have successfully logged in!");
            closeCurrentStage();
            WindowUtils.openWindow("Dashboard", "vcms-dashboard.fxml");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    /**
     * Checks if the input is valid (non-empty username and password).
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return True if the input is valid, false otherwise.
     */
    private boolean isInputValid(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    /**
     * Handles a failed login attempt.
     *
     * @param message The error message to display.
     */
    private void handleFailedLogin(String message) {
        AlertUtils.showErrorAlert("Login Failed", message);
    }

    /**
     * Handles the signup button click event.
     */
    @FXML
    private void handleSignupButton() {
        closeCurrentStage();
        WindowUtils.openWindow("Signup", "vcms-signup.fxml");
    }

    /**
     * Handles the recover password link click event.
     */
    @FXML
    private void handleRecoverPasswordLink() {
        closeCurrentStage();
        WindowUtils.openWindow("Recover Password", "vcms-recoverpassword.fxml");
    }

    /**
     * Closes the current stage.
     */
    private void closeCurrentStage() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        WindowUtils.closeWindow(stage);
    }

    /**
     * Handles an SQL exception by printing the stack trace.
     *
     * @param e The SQLException.
     */
    private void handleSQLException(SQLException e) {
        e.printStackTrace();
    }

}
