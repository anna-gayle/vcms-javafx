package com.genvetclinic.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The {@code Appointment} class represents an appointment in the veterinary clinic system.
 * Appointments have various attributes such as appointment ID, client name, contact information,
 * service required, assigned personnel, date, time, and status.
 *
 * <p>This class provides methods to access and modify these attributes, as well as additional
 * methods for retrieving information related to the appointment, such as a formatted time slot
 * and a summary for a specific day of the week.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class Appointment {

    /**
     * The unique identifier for the appointment.
     */
    private String appointmentid;

    /**
     * The name of the client who scheduled the appointment.
     */
    private String clientName;

    /**
     * The contact information of the client.
     */
    private String clientContact;

    /**
     * The type of service required for the appointment.
     */
    private String serviceRequired;

    /**
     * The personnel assigned to the appointment.
     */
    private String assignedPersonnel;

    /**
     * The date of the appointment.
     */
    private LocalDate appointmentDate;

    /**
     * The time of the appointment.
     */
    private LocalTime appointmentTime;

    /**
     * The status of the appointment (e.g., scheduled, completed, canceled).
     */
    private String appointmentStatus;

    /**
     * A formatted representation of the time range of the appointment.
     */
    private String formattedTimeRange;

    /**
     * Constructs a new {@code Appointment} instance with default values.
     */
    public Appointment() {
    }

    /**
     * Constructs a new {@code Appointment} instance with the specified parameters.
     *
     * @param appointmentid      The unique identifier for the appointment.
     * @param clientName         The name of the client who scheduled the appointment.
     * @param clientContact      The contact information of the client.
     * @param serviceRequired    The type of service required for the appointment.
     * @param assignedPersonnel  The personnel assigned to the appointment.
     * @param appointmentDate    The date of the appointment.
     * @param appointmentTime    The time of the appointment.
     * @param appointmentStatus  The status of the appointment.
     */
    public Appointment(String appointmentid, String clientName, String clientContact,
                       String serviceRequired, String assignedPersonnel, LocalDate appointmentDate,
                       LocalTime appointmentTime, String appointmentStatus) {
        this.appointmentid = appointmentid;
        this.clientName = clientName;
        this.clientContact = clientContact;
        this.serviceRequired = serviceRequired;
        this.assignedPersonnel = assignedPersonnel;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = appointmentStatus;
    }

    // Getter and setter methods for each attribute...

    public String getAppointmentId() {
        return appointmentid;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentid = appointmentId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientContact() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    public String getServiceRequired() {
        return serviceRequired;
    }

    public void setServiceRequired(String serviceRequired) {
        this.serviceRequired = serviceRequired;
    }

    public String getAssignedPersonnel() {
        return assignedPersonnel;
    }

    public void setAssignedPersonnel(String assignedPersonnel) {
        this.assignedPersonnel = assignedPersonnel;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getFormattedTimeRange() {
        return formattedTimeRange;
    }

    public void setFormattedTimeRange(String formattedTimeRange) {
        this.formattedTimeRange = formattedTimeRange;
    }

    /**
     * Retrieves a formatted time slot based on the provided start time.
     *
     * @param startTime The start time of the appointment.
     * @return A formatted time slot string.
     */
    public String getFormattedTimeSlot(LocalTime startTime) {
        return String.format("%02d:%02d", startTime.getHour(), startTime.getMinute(),
                startTime.getHour(), startTime.getMinute());
    }

    /**
     * Retrieves a summary of the appointment for a specific day of the week.
     *
     * @param dayOfWeek The day of the week for which the summary is requested.
     * @return A formatted summary string or an empty string if the appointment is not on the specified day.
     */
    public String getDaySummaryForDay(DayOfWeek dayOfWeek) {
        if (appointmentDate.getDayOfWeek() == dayOfWeek) {
            return getFormattedTimeSlot(appointmentTime) + "\n" +
                   getClientName() + "\n" +
                   getAssignedPersonnel() + "\n" +
                   getServiceRequired();
        } else {
            return "";
        }
    }
}
