package com.genvetclinic.controllers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.beans.property.*;
import com.genvetclinic.models.Appointment;
import com.genvetclinic.services.*;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.*;

/**
 * The {@code DashboardPanelController} class controls the dashboard panel of the veterinary clinic application.
 * It manages various UI elements, updates data, and displays notifications.
 *
 * <p>The class uses JavaFX components such as Text, TableView, TableColumn, and ImageView to create a dynamic
 * and informative dashboard. It interacts with different DAO classes to retrieve data from the database.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class DashboardPanelController {

    private final PatientDao patientDao;
    private final PersonnelDao personnelDao;
    private final BoarderDao boarderDao;
    private final TransactionDao transactionDao;
    private final AppointmentDao appointmentDao;
    private final ItemDao itemDao;
    private final KennelDao kennelDao;
    private final LabDao labDao;

    // FXML injection for various UI elements...
    @FXML private Text digitalClockText;
    @FXML private Text dateYearDayText;
    @FXML private Text noPatientsText;
    @FXML private Text noPersonnelText;
    @FXML private Text noKennelsOccupantsText;
    @FXML private Text earningstext;
    @FXML private Text weekDatesText;
    @FXML private Accordion notificationBoard;
    @FXML private Text patientsNotifText;
    @FXML private Text boardersNotifText;
    @FXML private Text appointmentNotifText;
    @FXML private Text inventoryNotifText;
    @FXML private Text kennelNotifText;
    @FXML private Text labNotifText;
    @FXML private Text personnelNotifText;
    @FXML private Text transactionNotifText;
    @FXML private ImageView timeCounterIcon;
    @FXML private ImageView patientCounterIcon;
    @FXML private ImageView employeeCounterIcon;
    @FXML private ImageView boarderCounterIcon;
    @FXML private ImageView earningsCounterIcon;
    @FXML private TableView<Appointment> weeklyScheduleTable;
    @FXML private TableColumn<Appointment, String> sundayColumn;
    @FXML private TableColumn<Appointment, String> mondayColumn;
    @FXML private TableColumn<Appointment, String> tuesdayColumn;
    @FXML private TableColumn<Appointment, String> wednesdayColumn;
    @FXML private TableColumn<Appointment, String> thursdayColumn;
    @FXML private TableColumn<Appointment, String> fridayColumn;
    @FXML private TableColumn<Appointment, String> saturdayColumn;

    /**
     * Initializes the controller. It sets up icons, starts the clock, and updates various components on the dashboard.
     *
     * @throws SQLException if a SQL exception occurs.
     */
    public DashboardPanelController() throws SQLException {
        this.patientDao = new PatientDao();
        this.personnelDao = new PersonnelDao();
        this.boarderDao = new BoarderDao();
        this.transactionDao = new TransactionDao();
        this.appointmentDao = new AppointmentDao();
        this.itemDao = new ItemDao();
        this.kennelDao = new KennelDao();
        this.labDao = new LabDao();
    }

    /**
     * Initializes the dashboard panel. It sets up icons, starts the clock, and updates various components on the dashboard.
     */
    @FXML
    private void initialize() {
        setImageforIcons();
        startClock();
        updateDateTime();
        updateTotalPatients();
        updateTotalPersonnel();
        updateTotalBoarders();
        updateTotalEarnings();
        updatePatientsNotification();
        updateBoardersNotificationsText();
        updateAppointmentNotification();
        updateItemNotification();
        updateKennelNotification();
        updateLabNotification();
        updatePersonnelNotifications();
        updateTransactionNotifications();
        initTableView();
        updateTableView();
        updateWeekDatesText();
        customizeTable();
    }

    /**
     * Sets images for various icons used in the dashboard.
     */
    private void setImageforIcons() {
        Image timeIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Time Icon.png"));
        Image patientIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Pets Icon.png"));
        Image employeeIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Employees Icon.png"));
        Image earningsIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Earnings Count Icon.png"));
        Image boardeeIcon = new Image(getClass().getResourceAsStream("/com/genvetclinic/images/Boardees Icon.png"));

        timeCounterIcon.setImage(timeIcon);
        patientCounterIcon.setImage(patientIcon);
        employeeCounterIcon.setImage(employeeIcon);
        earningsCounterIcon.setImage(earningsIcon);
        boarderCounterIcon.setImage(boardeeIcon);
    }

    /**
     * Starts the clock to update the digital clock and date on the dashboard.
     */
    private void startClock() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateDateTime())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Updates the digital clock and date on the dashboard.
     */
    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter clockFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, EEEE");

        String formattedClock = now.format(clockFormatter);
        String formattedDate = now.format(dateFormatter);

        digitalClockText.setText(formattedClock);
        dateYearDayText.setText(formattedDate);
    }

   /**
     * Updates the total number of patients on the dashboard.
     */
    private void updateTotalPatients() {
        try {
            int totalPatients = patientDao.getTotalPatients();
            noPatientsText.setText(String.format("%02d", Math.min(totalPatients, 99)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the total number of personnel on the dashboard.
     */
    private void updateTotalPersonnel() {
        try {
            int totalPersonnel = personnelDao.getTotalPersonnel();
            noPersonnelText.setText(String.format("%02d", Math.min(totalPersonnel, 99)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the total number of boarders on the dashboard.
     */
    private void updateTotalBoarders() {
        try {
            int totalBoarders = boarderDao.countBoarders();
            noKennelsOccupantsText.setText(String.format("%02d", Math.min(totalBoarders, 99)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the total earnings on the dashboard.
     */
    private void updateTotalEarnings() {
        try {
            BigDecimal totalEarnings = transactionDao.getTotalEarnings();

            if (totalEarnings.abs().compareTo(BigDecimal.valueOf(1_000_000)) >= 0) {
                totalEarnings = totalEarnings.divide(BigDecimal.valueOf(1_000_000));
                earningstext.setText(String.format("₱%.2fM", totalEarnings));
            } else if (totalEarnings.abs().compareTo(BigDecimal.valueOf(1_000)) >= 0) {
                totalEarnings = totalEarnings.divide(BigDecimal.valueOf(1_000));
                earningstext.setText(String.format("₱%.2fK", totalEarnings));
            } else {
                earningstext.setText(String.format("₱%.2f", totalEarnings));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the patients' notification on the dashboard.
     */
    private void updatePatientsNotification() {
        try {
            int count = patientDao.countPatientsAdmittedThisWeek();
            if (count > 0) {
                patientsNotifText.setText(count + " patient(s) \n admitted this week.");
            } else {
                patientsNotifText.setText("No New Notifications");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the boarders' notification on the dashboard.
     */
    private void updateBoardersNotificationsText() {
        try {
            String notificationText = boarderDao.getNotificationText();
            boardersNotifText.setText(notificationText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the appointment notification on the dashboard.
     */
    private void updateAppointmentNotification() {
        try {
            String appointmentsWithinWeekNotificationText = appointmentDao.getNotificationTextForAppointmentsWithinWeekAndNeedingAttention();

            if (!appointmentsWithinWeekNotificationText.equals("No new notifications.")) {
                appointmentNotifText.setText("You have " + appointmentsWithinWeekNotificationText);
            } else {
                appointmentNotifText.setText("No new notifications.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the item notification on the dashboard.
     */
    private void updateItemNotification() {
        try {
            String itemNotificationText = itemDao.getNotificationTextForItems();
            inventoryNotifText.setText(itemNotificationText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the kennel notification on the dashboard.
     */
    private void updateKennelNotification() {
        try {
            String kennelNotificationText = kennelDao.getNotificationTextForKennels();
            kennelNotifText.setText(kennelNotificationText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the lab notification on the dashboard.
     */
    private void updateLabNotification() {
        try {
            String labsNotificationText = labDao.getNotificationTextForLabs();

            if (!labsNotificationText.equals("No new notifications.")) {
                labNotifText.setText("You have " + labsNotificationText);
            } else {
                labNotifText.setText("No new notifications.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the personnel notification on the dashboard.
     */
    private void updatePersonnelNotifications() {
        try {
            String personnelNotificationText = personnelDao.getNotificationTextForPersonnel();
            personnelNotifText.setText(personnelNotificationText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the transaction notification on the dashboard.
     */
    private void updateTransactionNotifications() {
        try {
            String transactionNotificationText = transactionDao.getNotificationTextForTransactions();
            transactionNotifText.setText(transactionNotificationText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the text displaying the dates of the current week on the dashboard.
     */
    private void updateWeekDatesText() {
        LocalDate today = LocalDate.now();
        DayOfWeek firstDayOfWeek = DayOfWeek.SUNDAY; 
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d");

        String formattedStartDate = startOfWeek.format(formatter);
        String formattedEndDate = endOfWeek.format(formatter);

        String weekDates = formattedStartDate + " - " + formattedEndDate;
        weekDatesText.setText(weekDates);
    }

    /**
     * Initializes the TableView for the weekly schedule.
     */
    private void initTableView() {
        sundayColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        mondayColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        saturdayColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        weeklyScheduleTable.setRowFactory(tv -> {
            TableRow<Appointment> row = new TableRow<>();
            row.setMouseTransparent(true); 
            return row;
        });
                     
        List<TableColumn<Appointment, String>> dayColumns = Arrays.asList(sundayColumn, mondayColumn, tuesdayColumn, wednesdayColumn, thursdayColumn, fridayColumn, saturdayColumn);

        for (TableColumn<Appointment, String> dayColumn : dayColumns) {
            dayColumn.setCellValueFactory(cellData -> {
                StringProperty property = new SimpleStringProperty();
                Appointment appointment = cellData.getValue();

                switch (dayColumn.getText()) {
                    case "Sun":
                        property.setValue(appointment.getDaySummaryForDay(DayOfWeek.SUNDAY));
                        break;
                    case "Mon":
                        property.setValue(appointment.getDaySummaryForDay(DayOfWeek.MONDAY));
                        break;
                    case "Tue":
                        property.setValue(appointment.getDaySummaryForDay(DayOfWeek.TUESDAY));
                        break;
                    case "Wed":
                        property.setValue(appointment.getDaySummaryForDay(DayOfWeek.WEDNESDAY));
                        break;
                    case "Thu":
                        property.setValue(appointment.getDaySummaryForDay(DayOfWeek.THURSDAY));
                        break;
                    case "Fri":
                        property.setValue(appointment.getDaySummaryForDay(DayOfWeek.FRIDAY));
                        break;
                    case "Sat":
                        property.setValue(appointment.getDaySummaryForDay(DayOfWeek.SATURDAY));
                        break;
                    default:
                        property.setValue("");
                }

                return property;
            });

            dayColumn.setCellFactory(column -> new TableCell<Appointment, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            });
        }

    }

    /**
     * Updates the data in the TableView for the weekly schedule.
     */
    private void updateTableView() {
        try {
            List<Appointment> appointments = appointmentDao.getAppointmentsForCurrentWeek();
            appointments.forEach(appointment -> {
                String timeRange = appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        + " - " + appointment.getAppointmentTime().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm"));
                appointment.setFormattedTimeRange(timeRange);
            });
            weeklyScheduleTable.setItems(FXCollections.observableArrayList(appointments));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Customizes the appearance of the weekly schedule TableView.
     */
    private void customizeTable() {
        weeklyScheduleTable.setFixedCellSize(80);
        sundayColumn.setMinWidth(150);
        mondayColumn.setMinWidth(150);
        tuesdayColumn.setMinWidth(150);
        wednesdayColumn.setMinWidth(150);
        thursdayColumn.setMinWidth(150);
        fridayColumn.setMinWidth(150);
        saturdayColumn.setMinWidth(150);
    }

}