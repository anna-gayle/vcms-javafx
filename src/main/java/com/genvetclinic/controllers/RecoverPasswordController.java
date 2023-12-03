package com.genvetclinic.controllers;

import com.genvetclinic.models.Admin;
import com.genvetclinic.services.AdminDao;
import com.genvetclinic.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * The {@code RecoverPasswordController} class controls the password recovery functionality of the veterinary clinic application.
 * It handles the process of recovering a user's password through a provided email or username.
 *
 * <p>This controller uses JavaFX components, such as TextField, to collect user input for password recovery.
 * It interacts with the {@link com.genvetclinic.services.AdminDao} class for password recovery.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class RecoverPasswordController {

    private final AdminDao adminDao = createAdminDao();

    // FXML annotated fields for UI elements
    @FXML private TextField forgotUsernameField;
    @FXML private Text revealSQText;
    @FXML private TextField forgotAnswerField;
    @FXML private Text revealPasswordText;
    @FXML private Button revealSQButton;
    @FXML private Button revealPasswordButton;
    @FXML private Button returnToLoginButton;

    /**
     * Initializes the RecoverPasswordController.
     */
    @FXML
    private void initialize() {
        forgotAnswerField.setDisable(true);
        revealPasswordButton.setDisable(true);

        // Set event handlers for buttons
        setupButtonEventHandlers();

        // Add hover effects using EffectsUtils
        addHoverEffects();
    }

    /**
     * Creates an instance of AdminDao.
     *
     * @return An instance of AdminDao.
     */
    private AdminDao createAdminDao() {
        try {
            return new AdminDao();
        } catch (SQLException e) {
            handleSQLException("Error initializing RecoverPasswordController", e);
            return null; // Or throw a custom exception depending on your design
        }
    }

    /**
     * Handles the event when the user wants to retrieve the security question.
     */
    private void handleRetrieveSecurityQuestion() {
        String username = forgotUsernameField.getText();

        try {
            if (adminDao.doesUsernameExist(username)) {
                Admin admin = adminDao.getAdminByUsername(username);

                revealSQText.setText("Security Question: " + admin.getSecurityQuestion());
                revealSQText.setVisible(true);
                forgotAnswerField.setDisable(false);
                revealPasswordButton.setDisable(false);
            } else {
                AlertUtils.showErrorAlert("Error", "Username does not exist.");
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving security question", e);
        }
    }

    /**
     * Handles the event when the user wants to recover the password.
     */
    private void handleRecoverPassword() {
        String username = forgotUsernameField.getText();
        String answer = forgotAnswerField.getText();

        try {
            if (adminDao.doesUsernameExist(username)) {
                Admin admin = adminDao.getAdminByUsername(username);

                if (admin.getSecurityAnswer().equals(answer)) {
                    revealPasswordText.setText("Password: " + admin.getPassword());
                    revealPasswordText.setVisible(true);
                } else {
                    AlertUtils.showErrorAlert("Error", "Incorrect answer to security question.");
                }
            } else {
                AlertUtils.showErrorAlert("Error", "Username does not exist.");
            }
        } catch (SQLException e) {
            handleSQLException("Error recovering password", e);
        }
    }

    /**
     * Set up event handlers for buttons.
     */
    private void setupButtonEventHandlers() {
        revealSQButton.setOnAction(event -> handleRetrieveSecurityQuestion());
        revealPasswordButton.setOnAction(event -> handleRecoverPassword());
        returnToLoginButton.setOnAction(event -> handleReturnToLogin());
    }

    /**
     * Adds hover effects to buttons.
     */
    private void addHoverEffects() {
        EffectsUtils.addHoverEffect(returnToLoginButton);
        EffectsUtils.addHoverEffect(revealSQButton);
        EffectsUtils.addHoverEffect(revealPasswordButton);
    }

    /**
     * Handles the event when the user wants to return to the login screen.
     */
    private void handleReturnToLogin() {
        closeCurrentStage();
        WindowUtils.openWindow("Login", "vcms-login.fxml");
    }

    /**
     * Handles an SQL exception by showing an error alert.
     *
     * @param message The error message.
     * @param e       The SQLException.
     */
    private void handleSQLException(String message, SQLException e) {
        AlertUtils.showErrorAlert(message + ": " + e.getMessage(), "Error");
    }

    /**
     * Closes the current stage.
     */
    private void closeCurrentStage() {
        Stage stage = (Stage) returnToLoginButton.getScene().getWindow();
        stage.close();
    }
}
