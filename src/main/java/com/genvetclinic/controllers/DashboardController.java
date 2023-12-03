package com.genvetclinic.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;

/**
 * The {@code DashboardController} class is responsible for managing the veterinary clinic's dashboard
 * and navigation functionality. It controls the loading of different panels, sets up event handlers,
 * and adds hover effects to sidebar buttons.
 *
 * <p>The class uses JavaFX components for UI elements, including buttons, images, and progress indicators.
 * It asynchronously loads different panels using JavaFX Task and ProgressIndicator.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class DashboardController {
    
    // FXML injection for various UI elements...
    @FXML private Button sidebarDashboardButton;
    @FXML private Button sidebarPatientButton;
    @FXML private Button sidebarAppointmentButton;
    @FXML private Button sidebarInventoryButton;
    @FXML private Button sidebarKennelButton;
    @FXML private Button sidebarLabButton;
    @FXML private Button sidebarTransacButton;
    @FXML private Button sidebarPersonnelButton;
    @FXML private Button sidebarProfileButton;
    @FXML private Button sidebarBoarderButton;
    @FXML private ImageView logoImageViewer;
    @FXML private ImageView dashboardIconViewer;
    @FXML private ImageView patientsIconViewer;
    @FXML private ImageView boarderIconViewer;
    @FXML private ImageView appointmentIconViewer;
    @FXML private ImageView inventoryIconViewer;
    @FXML private ImageView kennelIconViewer;
    @FXML private ImageView labIconViewer;
    @FXML private ImageView employeeIconViewer;
    @FXML private ImageView transactionIconViewer;
    @FXML private ImageView profileIconViewer;
    @FXML private Pane contentLoaderPane;
    @FXML private ProgressIndicator loadingProgressBar;

    /**
     * Initializes the controller. It loads the default panel, sets up event handlers,
     * adds hover effects to sidebar buttons, and sets images for icons.
     */
    @FXML
    private void initialize() {
        loadPanel("/com/genvetclinic/ui/vcms-dashboardpanel.fxml");
        setImageforIcons();
        setupEventhandler();
        addHoverEffectSidebar();
    }

    /**
     * Loads a specified FXML panel into the content loader pane with a loading progress indicator.
     *
     * @param fxmlFileName the file path of the FXML panel to load.
     */
    private void loadPanel(String fxmlFileName) {
        loadingProgressBar.setStyle("-fx-progress-color: #358856;");
        loadingProgressBar.setVisible(true);

        Task<Pane> task = new Task<>() {
            @Override
            protected Pane call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
                return loader.load();
            }
        };

        loadingProgressBar.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded(event -> {
            Pane newPane = task.getValue();
            contentLoaderPane.getChildren().setAll(newPane);
            loadingProgressBar.setVisible(false);
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            loadingProgressBar.setVisible(false);
        });

        new Thread(task).start();
    }

    /**
     * Sets images for various icons used in the dashboard.
     */
    private void setImageforIcons() {
        Image logoImage = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/GVC Logo White 2.png"));
        Image dashboardIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Dashboard Icon.png"));
        Image patientsIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Patients Icon.png"));
        Image boarderIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Boarder Icon 2.png"));
        Image appointmentIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Appointments Icon.png"));
        Image inventoryIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Inventory Icon.png"));
        Image kennelIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Kennel Icon.png"));
        Image labIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Laboratory Icon.png"));
        Image employeeIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Personnel Icon.png"));
        Image transactionIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Transaction Icon.png"));
        Image profileIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Account Icon.png"));

        logoImageViewer.setImage(logoImage);
        dashboardIconViewer.setImage(dashboardIcon);
        patientsIconViewer.setImage(patientsIcon);
        boarderIconViewer.setImage(boarderIcon);
        appointmentIconViewer.setImage(appointmentIcon);
        inventoryIconViewer.setImage(inventoryIcon);
        kennelIconViewer.setImage(kennelIcon);
        labIconViewer.setImage(labIcon);
        employeeIconViewer.setImage(employeeIcon);
        transactionIconViewer.setImage(transactionIcon);
        profileIconViewer.setImage(profileIcon);
    }

    /**
     * Sets up event handlers for sidebar buttons, specifying the corresponding FXML panel to load.
     */
    private void setupEventhandler() {
        sidebarDashboardButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-dashboardpanel.fxml"));
        sidebarPatientButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-patientspanel.fxml"));
        sidebarAppointmentButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-appointmentspanel.fxml"));
        sidebarInventoryButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-inventorypanel.fxml"));
        sidebarKennelButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-kennelspanel.fxml"));
        sidebarLabButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-labpanel.fxml"));
        sidebarTransacButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-transactionspanel.fxml"));
        sidebarPersonnelButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-personnelpanel.fxml"));
        sidebarProfileButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-profilepanel.fxml"));
        sidebarBoarderButton.setOnAction(event -> loadPanel("/com/genvetclinic/ui/vcms-boarderspanel.fxml"));
    }

    /**
     * Adds hover effects to sidebar buttons to change their appearance when the mouse enters or exits.
     */
    private void addHoverEffectSidebar() {
        addMenuHoverEffect(sidebarDashboardButton);
        addMenuHoverEffect(sidebarPatientButton);
        addMenuHoverEffect(sidebarAppointmentButton);
        addMenuHoverEffect(sidebarInventoryButton);
        addMenuHoverEffect(sidebarKennelButton);
        addMenuHoverEffect(sidebarLabButton);
        addMenuHoverEffect(sidebarTransacButton);
        addMenuHoverEffect(sidebarPersonnelButton);
        addMenuHoverEffect(sidebarProfileButton);
        addMenuHoverEffect(sidebarBoarderButton);
    }

    /**
     * Adds hover effect to a specified button, changing its background color and text fill on mouse events.
     *
     * @param button the button to apply hover effect.
     */
    private void addMenuHoverEffect(Button button) {
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setStyle("-fx-background-color: #30694B; -fx-text-fill: #ffffff; -fx-alignment: center-left;");
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button.setStyle("-fx-background-color: #358856; -fx-text-fill: #ffffff; -fx-alignment: center-left;");
        });
    }
}