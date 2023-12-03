package com.genvetclinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The {@code App} class serves as the entry point for the GenVetClinic application.
 * It extends the JavaFX {@link Application} class and initializes the main application window.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class App extends Application {

    private static Scene scene;

    /**
     * Initializes the main application window.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("vcms-login"), 695, 800);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * Sets the root of the scene to the specified FXML file.
     *
     * @param fxml The name of the FXML file (without the extension).
     * @throws IOException If an error occurs while loading the FXML file.
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Private helper method to load FXML files
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/genvetclinic/ui/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * The main entry point for the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}