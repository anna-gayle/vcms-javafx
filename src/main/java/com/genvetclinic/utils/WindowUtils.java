package com.genvetclinic.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The {@code WindowUtils} class provides utility methods for working with JavaFX windows and UI components.
 * It includes methods for opening new windows, loading FXML files into panes, and closing windows.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class WindowUtils {

    /**
     * Opens a new window with the specified title and loads the content from the given FXML file.
     *
     * @param title        The title of the new window.
     * @param fxmlFileName The name of the FXML file to load.
     */
    public static void openWindow(String title, String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtils.class.getResource("/com/genvetclinic/ui/" + fxmlFileName));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the content of the specified FXML file into the given {@code Pane}.
     *
     * @param contentLoaderPane The {@code Pane} where the FXML content will be loaded.
     * @param fxmlFileName      The name of the FXML file to load.
     */
    public static void loadPanel(Pane contentLoaderPane, String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtils.class.getResource("/com/genvetclinic/ui/" + fxmlFileName));
    
            Parent root = loader.load();
    
            contentLoaderPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Error loading FXML file: " + fxmlFileName);
        }
    }

    /**
     * Closes the specified JavaFX window.
     *
     * @param stage The JavaFX {@code Stage} to close.
     */
    public static void closeWindow(Stage stage) {
        stage.close();
    }
}