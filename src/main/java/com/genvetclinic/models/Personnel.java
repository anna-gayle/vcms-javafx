package com.genvetclinic.models;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * The {@code Personnel} class represents an employee or staff member in the veterinary clinic system.
 * Personnel have various attributes such as personnel ID, name, email, address, contact information,
 * emergency contact, job title, veterinary specialization, hire date, work schedule, certification,
 * performance rating, and attendance rating.
 *
 * <p>This class provides methods to access and modify these attributes, allowing for the management
 * of personnel information within the system.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class Personnel {

    /**
     * The unique identifier for the personnel.
     */
    private String personnelId;

    /**
     * The name of the personnel.
     */
    private String personnelName;

    /**
     * The email address of the personnel.
     */
    private String personnelEmail;

    /**
     * The address of the personnel.
     */
    private String personnelAddress;

    /**
     * The contact number of the personnel.
     */
    private String personnelContact;

    /**
     * The emergency contact number of the personnel.
     */
    private String emergencyContact;

    /**
     * The job title of the personnel.
     */
    private String jobTitle;

    /**
     * The veterinary specialization of the personnel.
     */
    private String vetSpec;

    /**
     * The date when the personnel was hired.
     */
    private LocalDate hireDate;

    /**
     * The work schedule of the personnel.
     */
    private String workSched;

    /**
     * The certification of the personnel.
     */
    private String certification;

    /**
     * The performance rating of the personnel.
     */
    private BigDecimal perfRating;

    /**
     * The attendance rating of the personnel.
     */
    private BigDecimal attendRating;

    /**
     * Constructs a new {@code Personnel} instance with default values.
     */
    public Personnel() {
    }

    /**
     * Constructs a new {@code Personnel} instance with the specified parameters.
     *
     * @param personnelId       The unique identifier for the personnel.
     * @param personnelName     The name of the personnel.
     * @param personnelEmail    The email address of the personnel.
     * @param personnelAddress  The address of the personnel.
     * @param personnelContact  The contact number of the personnel.
     * @param emergencyContact  The emergency contact number of the personnel.
     * @param jobTitle          The job title of the personnel.
     * @param vetSpec           The veterinary specialization of the personnel.
     * @param certification     The certification of the personnel.
     * @param hireDate          The date when the personnel was hired.
     * @param workSched         The work schedule of the personnel.
     * @param perfRating        The performance rating of the personnel.
     * @param attendRating      The attendance rating of the personnel.
     */
    public Personnel(String personnelId, String personnelName, String personnelEmail, String personnelAddress, String personnelContact,
        String emergencyContact, String jobTitle, String vetSpec, String certification, LocalDate hireDate, String workSched, BigDecimal perfRating,
        BigDecimal attendRating) {
            this.personnelId = personnelId;
            this.personnelName = personnelName;
            this.personnelEmail = personnelEmail;
            this.personnelAddress = personnelAddress;
            this.personnelContact = personnelContact;
            this.emergencyContact = emergencyContact;
            this.jobTitle = jobTitle;
            this.vetSpec = vetSpec;
            this.hireDate = hireDate;
            this.workSched = workSched;
            this.perfRating = perfRating;
            this.attendRating = attendRating;
            this.certification = certification;
        }

    // Getter and setter methods for each attribute...

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getPersonnelName() {
        return personnelName;
    }

    public void setPersonnelName(String personnelName) {
        this.personnelName = personnelName;
    }

    public String getPersonnelEmail() {
        return personnelEmail;
    }

    public void setPersonnelEmail(String personnelEmail) {
        this.personnelEmail = personnelEmail;
    }

    public String getPersonnelAddress() {
        return personnelAddress;
    }

    public void setPersonnelAddress(String personnelAddress) {
        this.personnelAddress = personnelAddress;
    }

    public String getPersonnelContact() {
        return personnelContact;
    }

    public void setPersonnelContact(String personnelContact) {
        this.personnelContact = personnelContact;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getVetSpec() {
        return vetSpec;
    }

    public void setVetSpec(String vetSpec) {
        this.vetSpec = vetSpec;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getWorkSched() {
        return workSched;
    }

    public void setWorkSched(String workSched) {
        this.workSched = workSched;
    }

    public BigDecimal getPerfRating() {
        return perfRating;
    }

    public void setPerfRating(BigDecimal perfRating) {
        this.perfRating = perfRating;
    }

    public BigDecimal getAttendRating() {
        return attendRating;
    }

    public void setAttendRating(BigDecimal attendRating) {
        this.attendRating = attendRating;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }
}
