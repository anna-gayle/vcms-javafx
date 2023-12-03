package com.genvetclinic.models;

/**
 * The {@code ActivationCode} class represents an activation code used in the veterinary clinic application.
 * It contains information about the activation code, its status, and its active status.
 *
 * <p>An activation code typically has three attributes: the activation code itself, its status (e.g., "Active" or "Expired"),
 * and its active status (e.g., "Active" or "Inactive").
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class ActivationCode {

    // Fields
    private String activationCode;
    private String codeStatus;
    private String activeStatus;

    /**
     * Default constructor for the ActivationCode class.
     */
    public ActivationCode() {
    }

    /**
     * Parameterized constructor for the ActivationCode class.
     *
     * @param activationCode The activation code.
     * @param codeStatus     The status of the activation code (e.g., "Active" or "Expired").
     * @param activeStatus   The active status of the activation code (e.g., "Active" or "Inactive").
     */
    public ActivationCode(String activationCode, String codeStatus, String activeStatus) {
        this.activationCode = activationCode;
        this.codeStatus = codeStatus;
        this.activeStatus = activeStatus;
    }

    /**
     * Get the activation code.
     *
     * @return The activation code.
     */
    public String getActivationCode() {
        return activationCode;
    }

    /**
     * Set the activation code.
     *
     * @param activationCode The activation code to set.
     */
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    /**
     * Get the status of the activation code.
     *
     * @return The status of the activation code.
     */
    public String getCodeStatus() {
        return codeStatus;
    }

    /**
     * Set the status of the activation code.
     *
     * @param codeStatus The status to set.
     */
    public void setCodeStatus(String codeStatus) {
        this.codeStatus = codeStatus;
    }

    /**
     * Get the active status of the activation code.
     *
     * @return The active status of the activation code.
     */
    public String getActiveStatus() {
        return activeStatus;
    }

    /**
     * Set the active status of the activation code.
     *
     * @param activeStatus The active status to set.
     */
    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }
}

