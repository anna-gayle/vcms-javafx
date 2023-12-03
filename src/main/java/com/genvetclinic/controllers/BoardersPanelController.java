package com.genvetclinic.controllers;

import com.genvetclinic.models.Boarder;
import com.genvetclinic.services.*;
import com.genvetclinic.utils.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.image.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.*;

/**
 * The {@code BoardersPanelController} class controls the Boarders Panel of the veterinary clinic application.
 * It manages the display and interaction with the boarders data.
 *
 * <p>This controller utilizes JavaFX components, such as TableView and TableColumn, to present a list of boarders.
 * It interacts with the {@link com.genvetclinic.services.BoarderDao} class to retrieve and update boarder data from the database.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class BoardersPanelController {

    private BoarderDao boarderDao;
    private DatabaseConnection databaseConnection;
    private Boarder lastSelectedBoarder;

    // FXML injection for various UI elements...
    @FXML private TextField boarderNameField;
    @FXML private TextField boarderSpeciesField;
    @FXML private TextField boarderBreedField;
    @FXML private TextField boarderColorField;
    @FXML private TextArea boarderSITextArea;
    @FXML private TextField bOwnerNameField;
    @FXML private TextField bOwnerContactField;
    @FXML private TextField bOwnerAddressField;
    @FXML private TextField bOwnerEmailField;
    @FXML private DatePicker dateBoardedDP;
    @FXML private DatePicker departureDateDP;
    @FXML private TextField boarderAgeField;
    @FXML private MenuButton boarderGenderMenu;
    @FXML private TextField boarderWeightField;
    @FXML private Button addBoarderButton;
    @FXML private Button clearBoarderButton;
    @FXML private Button saveBoarderButton;
    @FXML private Button deleteBoarderButton;
    @FXML private ImageView admitBoarderIconViewer;
    @FXML private ImageView manageBoarderIconViewer;
    @FXML private TableView<Boarder> boarderTableView;
    @FXML private TableColumn<Boarder, String> boarderIdColumn;
    @FXML private TableColumn<Boarder, String> boarderNameColumn;
    @FXML private TableColumn<Boarder, String> boarderSpeciesColumn;
    @FXML private TableColumn<Boarder, String> boarderBreedColumn;
    @FXML private TableColumn<Boarder, String> boarderColorColumn;
    @FXML private TableColumn<Boarder, Number> boarderAgeColumn;
    @FXML private TableColumn<Boarder, String> boarderGenderColumn;
    @FXML private TableColumn<Boarder, Number> boarderWeightColumn;
    @FXML private TableColumn<Boarder, String> brdrOwnerNameColumn;
    @FXML private TableColumn<Boarder, String> brdrOwnerContactColumn;
    @FXML private TableColumn<Boarder, String> brdrOwnerAddColumn;
    @FXML private TableColumn<Boarder, String> brdrOwnerEmailColumn;
    @FXML private TableColumn<Boarder, String> departureDateColumn;
    @FXML private TableColumn<Boarder, String> boardAdmitColumn;

    /**
     * Initializes the Boarders Panel Controller.
     */
    public BoardersPanelController() {
        try {
            this.databaseConnection = new DatabaseConnection();
            this.boarderDao = new BoarderDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes the GUI components and sets event handlers.
     */
    @FXML
    private void initialize() {
        
        initializeBoarderTable();
        populateBoarderTable();
        enableBoarderTableSelection();
        initializeGenderMenu();
        initializeButtons();
        setupEventHandlers();
        setImageForIcons();
        addHoverEffect();

        EffectsUtils.setTableSelectionColor(boarderTableView, "#358856");
    }

    /**
     * Initializes the buttons.
     */
    private void initializeButtons() {
        deleteBoarderButton.setDisable(true);
        saveBoarderButton.setDisable(true);
    }

    /**
     * Sets up event handlers for the buttons in the UI.
     */
    private void setupEventHandlers() {
        addBoarderButton.setOnAction(event -> handleAddBoarder());
        clearBoarderButton.setOnAction(event -> clearBoarderFields());
        deleteBoarderButton.setOnAction(event -> handleDeleteBoarder());
        saveBoarderButton.setOnAction(event -> handleSaveBoarder());
    }

    /**
     * Initializes the boarder table by setting the cell value factories for each column.
     */    
    private void initializeBoarderTable() {
        boarderIdColumn.setCellValueFactory(new PropertyValueFactory<>("boarderId"));
        boarderNameColumn.setCellValueFactory(new PropertyValueFactory<>("boarderName"));
        boarderSpeciesColumn.setCellValueFactory(new PropertyValueFactory<>("boarderSpecies"));
        boarderBreedColumn.setCellValueFactory(new PropertyValueFactory<>("boarderBreed"));
        boarderColorColumn.setCellValueFactory(new PropertyValueFactory<>("boarderColor"));
        boarderAgeColumn.setCellValueFactory(new PropertyValueFactory<>("boarderAge"));
        boarderGenderColumn.setCellValueFactory(new PropertyValueFactory<>("boarderGender"));
        boarderWeightColumn.setCellValueFactory(new PropertyValueFactory<>("boarderWeight"));
        brdrOwnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("brdrOwnerName"));
        brdrOwnerContactColumn.setCellValueFactory(new PropertyValueFactory<>("brdrOwnerContact"));
        brdrOwnerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("brdrOwnerEmail"));
        brdrOwnerAddColumn.setCellValueFactory(new PropertyValueFactory<>("brdrOwnerAddress"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateDeparted"));
        boardAdmitColumn.setCellValueFactory(new PropertyValueFactory<>("boardedDate"));
    }

    /**
     * Enables the selection of a boarder in the boarder table.
     * Sets up a listener for the selected item property of the boarder table view.
     * Calls the handleBoarderTableRowSelection method when the selected item changes.
     * Sets up a mouse click event handler for the boarder table view.
     * If a single left click is performed, it checks if the selected boarder is the same as the last selected boarder.
     * If they are the same, clears the selection, sets lastSelectedBoarder to null, and clears the boarder fields.
     * If they are different, updates the lastSelectedBoarder variable.
     * If a double left click is performed, calls the handleBoarderTableRowSelection method for the selected item.
     */
    private void enableBoarderTableSelection() {
        boarderTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> handleBoarderTableRowSelection(newSelection));

        boarderTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Boarder selectedBoarder = boarderTableView.getSelectionModel().getSelectedItem();

                if (selectedBoarder != null && selectedBoarder.equals(lastSelectedBoarder)) {
                    boarderTableView.getSelectionModel().clearSelection();
                    lastSelectedBoarder = null;
                    clearBoarderFields();
                } else {
                    lastSelectedBoarder = selectedBoarder;
                }
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleBoarderTableRowSelection(boarderTableView.getSelectionModel().getSelectedItem());
            }
        });
    }

    /**
     * Handles the selection of a boarder table row.
     *
     * @param  selectedBoarder  the selected boarder object
     */
    private void handleBoarderTableRowSelection(Boarder selectedBoarder) {
        if (selectedBoarder != null) {
            lastSelectedBoarder = selectedBoarder;
            displayBoarderFields(selectedBoarder); 
            deleteBoarderButton.setDisable(false); 
            saveBoarderButton.setDisable(false); 
        } else {
            clearBoarderFields(); 
            deleteBoarderButton.setDisable(true);
            saveBoarderButton.setDisable(true);
            lastSelectedBoarder = null;
        }
    }

    /**
     * Sets the image for icons.
     */
    private void setImageForIcons() {
        Image admitBoarderIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Add Boarder 2.png"));
        Image manageBoarderIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Manage Boarder Icon.png"));
        admitBoarderIconViewer.setImage(admitBoarderIcon);
        manageBoarderIconViewer.setImage(manageBoarderIcon);
    }

    /**
     * Handles the action when adding a new boarder.
     */ 
    @FXML
    private void handleAddBoarder() {
        try {
            // Get input values from the form fields
            String boarderName = boarderNameField.getText();
            String boarderSpecies = boarderSpeciesField.getText();
            String boarderBreed = boarderBreedField.getText();
            String boarderColor = boarderColorField.getText();
            String bSpecialInstructions = boarderSITextArea.getText();
            String bOwnerName = bOwnerNameField.getText();
            String bOwnerContact = bOwnerContactField.getText();
            String bOwnerAddress = bOwnerAddressField.getText();
            String boarderGender = boarderGenderMenu.getText();
            String bOwnerEmail = bOwnerEmailField.getText();
            BigDecimal boarderAge = ParseUtils.parseBigDecimal(boarderAgeField.getText());
            BigDecimal boarderWeight = ParseUtils.parseBigDecimal(boarderWeightField.getText());
            LocalDate dateBoarded = ParseUtils.parseLocalDate(dateBoardedDP.getValue().toString());
            LocalDate dateDeparted = ParseUtils.parseLocalDate(departureDateDP.getValue().toString());

            if (ValidationUtils.isNullOrEmpty(
                    boarderName, boarderSpecies, boarderBreed, boarderColor, bSpecialInstructions, bOwnerName,
                    bOwnerContact, bOwnerAddress, boarderGender)) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate boarder and owner names
            if (!ValidationUtils.isValidName(boarderName) || !ValidationUtils.isValidName(bOwnerName)) {
                AlertUtils.showErrorAlert("Error", "Invalid boarder or owner name. Please use only letters and spaces.");
                return;
            }

            // Validate owner email
            if (!ValidationUtils.isValidEmail(bOwnerEmail)) {
                AlertUtils.showErrorAlert("Error", "Invalid owner email address.");
                return;
            }

            // Validate owner contact number
            if (!ValidationUtils.isValidInteger(bOwnerContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid owner contact number.");
                return;
            }

            // Validate numeric fields for correct format and greater than zero
            if (!ValidationUtils.isValidNumber(boarderAge) || !ValidationUtils.isValidNumber(boarderWeight, boarderAge)) {
                AlertUtils.showErrorAlert("Error", "Age and Weight must be greater than or equal to zero.");
                return;
            }

            // Validate the date fields
            if (!ValidationUtils.isValidDateFormat(dateBoarded.toString())) {
                AlertUtils.showErrorAlert("Error", "Invalid date format for Date Boarded. Please use the correct format.");
                return;
            }

            if (!ValidationUtils.isValidDateFormat(dateDeparted.toString())) {
                AlertUtils.showErrorAlert("Error", "Invalid date format for Departure Date. Please use the correct format.");
                return;
            }

            // Check if the departure date is in the past
            if (dateDeparted.isBefore(LocalDate.now())) {
                AlertUtils.showErrorAlert("Error", "Departure date must be in the future.");
                return;
            }

            // Check if the boarder already exists
            if (boarderDao.isBoarderExists(boarderName, boarderSpecies, boarderBreed, bOwnerName, dateBoarded, dateDeparted)) {
                AlertUtils.showErrorAlert("Error", "Boarder already exists.");
            } else {
                // Generate a random boarder ID
                String boarderId = GenerateRandomIds.generateRandomId(10);

                // Create a new Boarder object and set its properties
                Boarder newBoarder = new Boarder(
                        boarderId, boarderName, boarderSpecies, boarderBreed, boarderColor, bSpecialInstructions, bOwnerName,
                        bOwnerContact, bOwnerAddress, dateBoarded, boarderAge, boarderGender, boarderWeight, dateDeparted, bOwnerEmail
                );

                // Save the boarder and show success message
                boarderDao.saveBoarder(newBoarder);
                populateBoarderTable();
                clearBoarderFields();
                AlertUtils.showInformationAlert("Success", "Boarder added successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }


    /**
     * Handles the action when editing boarder information.
     */
    @FXML
    private void handleSaveBoarder() {
        try {
            Boarder selectedBoarder = boarderTableView.getSelectionModel().getSelectedItem();

            String updatedBoarderName = boarderNameField.getText();
            String updatedBoarderSpecies = boarderSpeciesField.getText();
            String updatedBoarderBreed = boarderBreedField.getText();
            String updatedBoarderColor = boarderColorField.getText();
            String updatedBSpecialInstructions = boarderSITextArea.getText();
            String updatedBOwnerName = bOwnerNameField.getText();
            String updatedBOwnerContact = bOwnerContactField.getText();
            String updatedBOwnerAddress = bOwnerAddressField.getText();
            String updatedBoarderGender = boarderGenderMenu.getText();
            String updatedBOwnerEmail = bOwnerEmailField.getText();
            BigDecimal updatedBoarderAge = ParseUtils.parseBigDecimal(boarderAgeField.getText());
            BigDecimal updatedBoarderWeight = ParseUtils.parseBigDecimal(boarderWeightField.getText());
            LocalDate updatedDateBoarded = ParseUtils.parseLocalDate(dateBoardedDP.getValue().toString());
            LocalDate updatedDateDeparted = ParseUtils.parseLocalDate(departureDateDP.getValue().toString());

            if (ValidationUtils.isNullOrEmpty(
                    updatedBoarderName, updatedBoarderSpecies, updatedBoarderBreed, updatedBoarderColor, updatedBSpecialInstructions, updatedBOwnerName,
                    updatedBOwnerContact, updatedBOwnerAddress, updatedBoarderGender)) {
                AlertUtils.showErrorAlert("Error", "Please fill in all fields. Write 'n/a' for inapplicable fields.");
                return;
            }

            // Validate boarder and owner names
            if (!ValidationUtils.isValidName(updatedBoarderName) || !ValidationUtils.isValidName(updatedBOwnerName)) {
                AlertUtils.showErrorAlert("Error", "Invalid boarder or owner name. Please use only letters and spaces.");
                return;
            }

            // Validate owner email
            if (!ValidationUtils.isValidEmail(updatedBOwnerEmail)) {
                AlertUtils.showErrorAlert("Error", "Invalid owner email address.");
                return;
            }

            // Validate owner contact number
            if (!ValidationUtils.isValidInteger(updatedBOwnerContact)) {
                AlertUtils.showErrorAlert("Error", "Invalid owner contact number.");
                return;
            }

            // Validate numeric fields for correct format and greater than zero
            if (updatedBoarderAge == null || updatedBoarderWeight == null) {
                AlertUtils.showErrorAlert("Error", "Invalid number format. Please check age and weight fields.");
                return;
            }

            if (updatedBoarderAge.compareTo(BigDecimal.ZERO) <= 0 || updatedBoarderWeight.compareTo(BigDecimal.ZERO) <= 0) {
                AlertUtils.showErrorAlert("Error", "Age and Weight must be greater than zero.");
                return;
            }

            // Validate the date fields
            if (!ValidationUtils.isValidDateFormat(updatedDateBoarded.toString()) || 
            !ValidationUtils.isValidDateFormat(updatedDateDeparted.toString())) {
            AlertUtils.showErrorAlert("Error", "Invalid date format. Please use the correct format.");
            return;
            }

            // Check if any changes were made
            if (!selectedBoarder.getBoarderName().equals(updatedBoarderName) || !selectedBoarder.getBoarderSpecies().equals(updatedBoarderSpecies)
                    || !selectedBoarder.getBoarderBreed().equals(updatedBoarderBreed) || !selectedBoarder.getBoarderColor().equals(updatedBoarderColor)
                    || !selectedBoarder.getbSpecialInstructions().equals(updatedBSpecialInstructions) || !selectedBoarder.getBrdrOwnerName().equals(updatedBOwnerName)
                    || !selectedBoarder.getBrdrOwnerContact().equals(updatedBOwnerContact) || !selectedBoarder.getBrdrOwnerAddress().equals(updatedBOwnerAddress)
                    || !selectedBoarder.getBoarderGender().equals(updatedBoarderGender) || !selectedBoarder.getBrdrOwnerEmail().equals(updatedBOwnerEmail)
                    || !selectedBoarder.getBoarderAge().equals(updatedBoarderAge) || !selectedBoarder.getBoarderWeight().equals(updatedBoarderWeight)
                    || !selectedBoarder.getBoardedDate().equals(updatedDateBoarded) || !selectedBoarder.getDateDeparted().equals(updatedDateDeparted)) {

                // Update the boarder information
                Boarder updatedBoarder = new Boarder(
                        selectedBoarder.getBoarderId(), updatedBoarderName, updatedBoarderSpecies, updatedBoarderBreed,
                        updatedBoarderColor, updatedBSpecialInstructions, updatedBOwnerName, updatedBOwnerContact,
                        updatedBOwnerAddress, updatedDateBoarded, updatedBoarderAge, updatedBoarderGender,
                        updatedBoarderWeight, updatedDateDeparted, updatedBOwnerEmail
                );

                // Try to update the boarder in the database
                try {
                    boarderDao.updateBoarder(updatedBoarder);
                    refreshBoarderTable();
                    AlertUtils.showInformationAlert("Success", "Boarder information updated successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
                }
            } else {
                // No changes were made
                AlertUtils.showInformationAlert("Info", "No changes were made.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "An unexpected error occurred.");
        }
    }

    /**
     * Handles the action when deleting a patient.
     */
    @FXML
    private void handleDeleteBoarder() {
        Boarder selectedBoarder = boarderTableView.getSelectionModel().getSelectedItem();

        boolean confirmed = AlertUtils.showConfirmationPrompt("Are you sure you want to delete this boarder?");

        if (confirmed) {
            try {
                boarderTableView.getItems().remove(selectedBoarder);
                boarderDao.deleteBoarder(selectedBoarder);
                clearBoarderFields();
                AlertUtils.showInformationAlert("Success", "Boarder deleted successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "An unexpected database error occurred.");
            }
        }
    }

    /**
     * Sets the values of the boarder fields in the UI based on the provided Boarder object.
     *
     * @param  boarder  the Boarder object containing the data to be displayed
     */
    private void displayBoarderFields(Boarder boarder) {
        boarderNameField.setText(boarder.getBoarderName());
        boarderSpeciesField.setText(boarder.getBoarderSpecies());
        boarderBreedField.setText(boarder.getBoarderBreed());
        boarderAgeField.setText(String.valueOf(boarder.getBoarderAge()));
        boarderColorField.setText(boarder.getBoarderColor());
        dateBoardedDP.setValue(boarder.getBoardedDate());
        boarderSITextArea.setText(boarder.getbSpecialInstructions());
        departureDateDP.setValue(boarder.getDateDeparted());
        bOwnerNameField.setText(boarder.getBrdrOwnerName());
        bOwnerContactField.setText(boarder.getBrdrOwnerContact());
        bOwnerAddressField.setText(boarder.getBrdrOwnerAddress());
        boarderGenderMenu.setText(boarder.getBoarderGender());
        boarderWeightField.setText(String.valueOf(boarder.getBoarderWeight()));
        bOwnerEmailField.setText(boarder.getBrdrOwnerEmail());
    }

    /**
     * Populates the boarder table with data from the database.
     */
    private void populateBoarderTable() {
        try {
            ObservableList<Boarder> boarders = FXCollections.observableArrayList();
    
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM boarders");
                 ResultSet resultSet = statement.executeQuery()) {
    
                while (resultSet.next()) {
                    Boarder boarder = new Boarder(
                            resultSet.getString("boarder_id"),
                            resultSet.getString("boarder_name"),
                            resultSet.getString("boarder_species"),
                            resultSet.getString("boarder_breed"),
                            resultSet.getString("boarder_color"),
                            resultSet.getString("b_special_instructions"),
                            resultSet.getString("b_owner_name"),
                            resultSet.getString("b_owner_contact"),
                            resultSet.getString("b_owner_address"),
                            resultSet.getDate("date_boarded").toLocalDate(),
                            resultSet.getBigDecimal("boarder_age"),
                            resultSet.getString("boarder_gender"),
                            resultSet.getBigDecimal("boarder_weight"),
                            resultSet.getDate("date_departed").toLocalDate(),
                            resultSet.getString("b_owner_email")
                    );
    
                    boarders.add(boarder);
                }
            }
    
            if (boarders.isEmpty()) {
                boarderTableView.setPlaceholder(new Label("No boarders found."));
            } else {
                boarderTableView.setItems(boarders);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Error", "Unable to fetch boarders from the database.");
        }
    }    

    /**
     * Refreshes the boarder table.
     */
    private void refreshBoarderTable() {
        boarderTableView.getItems().clear();
        populateBoarderTable();
    }

    /**
     * Handles the menu item for selecting the gender of a boarder.
     *
     * @param  gender  The gender to be set for the boarder.
     */
    private void handleBoarderGenderMenuItem(String gender) {
        boarderGenderMenu.setText(gender);
    }

    /**
     * Initializes the gender menu by clearing its items and adding new items for "Female" and "Male".
     * Also sets the action event for each item to call the handleBoarderGenderMenuItem method with the item's text.
     */    
    private void initializeGenderMenu() {
        boarderGenderMenu.getItems().clear();
        boarderGenderMenu.getItems().addAll(
                new MenuItem("Female"),
                new MenuItem("Male")
        );
        boarderGenderMenu.getItems().forEach(item -> item.setOnAction(event -> handleBoarderGenderMenuItem(item.getText())));
    }

    /**
     * Clears all the fields related to the boarder in the UI.
     */    
    private void clearBoarderFields() {
        boarderNameField.clear();
        boarderSpeciesField.clear();;
        boarderBreedField.clear();
        boarderColorField.clear();
        boarderSITextArea.clear();
        bOwnerNameField.clear();
        bOwnerContactField.clear();
        bOwnerAddressField.clear();
        dateBoardedDP.setValue(null);
        boarderAgeField.clear();
        boarderGenderMenu.setText("Gender");
        boarderWeightField.clear();
        departureDateDP.setValue(null);
        bOwnerEmailField.clear();

        deleteBoarderButton.setDisable(true);
        saveBoarderButton.setDisable(true);

        boarderTableView.getSelectionModel().clearSelection();
        lastSelectedBoarder = null;
    }

    /**
     * Adds a hover effect to the specified buttons.
     *
     * @param  addBoarderButton    the button to add a hover effect to
     * @param  saveBoarderButton   the button to add a hover effect to
     * @param  deleteBoarderButton the button to add a hover effect to
     * @param  clearBoarderButton  the button to add a hover effect to
     */    
    private void addHoverEffect() {
        EffectsUtils.addHoverEffect(addBoarderButton);
        EffectsUtils.addHoverEffect(saveBoarderButton);
        EffectsUtils.addHoverEffect(deleteBoarderButton);
        EffectsUtils.addHoverEffect(clearBoarderButton);
    } 
    
}
