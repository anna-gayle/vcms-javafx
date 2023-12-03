package com.genvetclinic.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import java.util.Optional;

/**
 * The {@code AlertUtils} class provides utility methods for displaying different types of alerts in a JavaFX application.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class AlertUtils {

    /**
     * Displays an error alert with the specified title and message.
     *
     * @param title   The title of the error alert.
     * @param message The message to be displayed in the error alert.
     */
    public static void showErrorAlert(String title, String message) {
        showAlert(AlertType.ERROR, title, message);
    }

    /**
     * Displays an information alert with the specified title and message.
     *
     * @param title   The title of the information alert.
     * @param message The message to be displayed in the information alert.
     */
    public static void showInformationAlert(String title, String message) {
        showAlert(AlertType.INFORMATION, title, message);
    }

    /**
     * Displays a success alert with the specified title and message.
     *
     * @param title   The title of the success alert.
     * @param message The message to be displayed in the success alert.
     */
    public static void showSuccessAlert(String title, String message) {
        showAlert(AlertType.INFORMATION, title, message);
    }

    /**
     * Displays a failure alert with the specified title and message.
     *
     * @param title   The title of the failure alert.
     * @param message The message to be displayed in the failure alert.
     */
    public static void showFailureAlert(String title, String message) {
        showAlert(AlertType.ERROR, title, message);
    }

/**
     * Displays a confirmation prompt with the specified message and returns the user's response.
     *
     * @param message The message to be displayed in the confirmation prompt.
     * @return {@code true} if the user clicks OK, {@code false} otherwise.
     */
    public static boolean showConfirmationPrompt(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initModality(Modality.APPLICATION_MODAL);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Private helper method to display generic alerts
    private static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}