package com.genvetclinic.services;

import com.genvetclinic.models.Appointment;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AppointmentDao} class provides data access methods for managing Appointment entities
 * in the veterinary clinic system. It includes methods for saving, updating, deleting,
 * and retrieving appointment information from the database.
 *
 * <p>This class utilizes a {@link DatabaseConnection} to establish a connection to the database
 * and execute SQL queries for interacting with the appointment data.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class AppointmentDao {

    /**
     * The database connection used by this DAO.
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Constructs a new {@code AppointmentDao} instance with a default database connection.
     *
     * @throws SQLException if a database access error occurs.
     */
    public AppointmentDao() throws SQLException {
        this.databaseConnection = new DatabaseConnection();
    }

    /**
     * Constructs a new {@code AppointmentDao} instance with the specified database connection.
     *
     * @param databaseConnection the database connection to be used by this DAO.
     */
    public AppointmentDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Methods for saving, updating, deleting, and retrieving appointment information...

    public void saveAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointment (appointment_id, client_name, client_contact, service_required, assigned_personnel, appointment_date, appointment_time, appointment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setSaveAppointmentParameters(preparedStatement, appointment);
            preparedStatement.executeUpdate();
        }
    }

    public void updateAppointment(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointment SET client_name = ?, client_contact = ?, service_required = ?, " +
                "assigned_personnel = ?, appointment_date = ?, appointment_time = ?, appointment_status = ? WHERE appointment_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setUpdateAppointmentParameters(preparedStatement, appointment);
            preparedStatement.setString(8, appointment.getAppointmentId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteAppointment(Appointment appointment) {
        String sql = "DELETE FROM appointment WHERE appointment_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, appointment.getAppointmentId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Appointment> getAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                appointments.add(new Appointment(
                        resultSet.getString("appointment_id"),
                        resultSet.getString("client_name"),
                        resultSet.getString("client_contact"),
                        resultSet.getString("service_required"),
                        resultSet.getString("assigned_personnel"),
                        resultSet.getDate("appointment_date").toLocalDate(),
                        resultSet.getTime("appointment_time").toLocalTime(),
                        resultSet.getString("appointment_status")
                ));
            }
        }
        return appointments;
    }

    public boolean isAppointmentExists(String clientName, String assignedPersonnel, String serviceRequired, LocalDate appointmentDate, LocalTime appointmentTime) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointment WHERE client_name = ? AND assigned_personnel = ? AND service_required = ? AND appointment_date = ? AND appointment_time = ?";
        try (Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clientName);
            preparedStatement.setString(2, assignedPersonnel);
            preparedStatement.setString(3, serviceRequired);
            preparedStatement.setDate(4, Date.valueOf(appointmentDate));
            preparedStatement.setTime(5, Time.valueOf(appointmentTime));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }


    public int countAppointmentsNeedingAttentionAndWithinWeek() throws SQLException {
        LocalDate currentDate = LocalDate.now();
        LocalDate oneWeekLater = currentDate.plusDays(7);

        String sql = "SELECT COUNT(*) FROM appointment WHERE " +
                     "appointment_status <> 'Completed' AND " +
                     "appointment_date BETWEEN ? AND ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(currentDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(oneWeekLater));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        }
    }

    public String getNotificationTextForAppointmentsWithinWeekAndNeedingAttention() throws SQLException {
        int appointmentsWithinWeekCount = countAppointmentsNeedingAttentionAndWithinWeek();

        if (appointmentsWithinWeekCount > 0) {
            return appointmentsWithinWeekCount + " \n appointment(s) within \n this week that need \n your attention.";
        } else {
            return "No new notifications.";
        }
    }

    public List<Appointment> getAppointmentsForCurrentWeek() throws SQLException {
        LocalDate today = LocalDate.now();
        DayOfWeek firstDayOfWeek = DayOfWeek.SUNDAY; 
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        String sql = "SELECT * FROM appointment WHERE appointment_date BETWEEN ? AND ?";
        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(startOfWeek));
            preparedStatement.setDate(2, Date.valueOf(endOfWeek));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(createAppointmentFromResultSet(resultSet));
                }
            }
        }
        return appointments;
    }

    private Appointment createAppointmentFromResultSet(ResultSet resultSet) throws SQLException {
        return new Appointment(
                resultSet.getString("appointment_id"),
                resultSet.getString("client_name"),
                resultSet.getString("client_contact"),
                resultSet.getString("service_required"),
                resultSet.getString("assigned_personnel"),
                resultSet.getDate("appointment_date").toLocalDate(),
                resultSet.getTime("appointment_time").toLocalTime(),
                resultSet.getString("appointment_status")
        );
    }

    private void setSaveAppointmentParameters(PreparedStatement preparedStatement, Appointment appointment) throws SQLException {
        preparedStatement.setString(1, appointment.getAppointmentId());
        preparedStatement.setString(2, appointment.getClientName());
        preparedStatement.setString(3, appointment.getClientContact());
        preparedStatement.setString(4, appointment.getServiceRequired());
        preparedStatement.setString(5, appointment.getAssignedPersonnel());
        preparedStatement.setDate(6, Date.valueOf(appointment.getAppointmentDate()));
        preparedStatement.setTime(7, Time.valueOf(appointment.getAppointmentTime()));
        preparedStatement.setString(8, appointment.getAppointmentStatus());
    } 

    private void setUpdateAppointmentParameters(PreparedStatement preparedStatement, Appointment appointment) throws SQLException {
        preparedStatement.setString(1, appointment.getClientName());
        preparedStatement.setString(2, appointment.getClientContact());
        preparedStatement.setString(3, appointment.getServiceRequired());
        preparedStatement.setString(4, appointment.getAssignedPersonnel());
        preparedStatement.setDate(5, Date.valueOf(appointment.getAppointmentDate()));
        preparedStatement.setTime(6, Time.valueOf(appointment.getAppointmentTime()));
        preparedStatement.setString(7, appointment.getAppointmentStatus());
    } 
}

