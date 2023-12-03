package com.genvetclinic.models;

/**
 * The {@code Lab} class represents a laboratory in the veterinary clinic system.
 * Labs have various attributes such as lab ID, name, number of equipment, and status.
 *
 * <p>This class provides methods to access and modify these attributes, allowing for the
 * management of laboratory information within the system.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class Lab {

    /**
     * The unique identifier for the laboratory.
     */
    private String labid;

    /**
     * The name of the laboratory.
     */
    private String labName;

    /**
     * The number of equipment in the laboratory.
     */
    private Integer noOfEquipment;

    /**
     * The status of the laboratory (e.g., operational, under maintenance).
     */
    private String labStatus;

    /**
     * Constructs a new {@code Lab} instance with default values.
     */
    public Lab() {
    }

    /**
     * Constructs a new {@code Lab} instance with the specified parameters.
     *
     * @param labid          The unique identifier for the laboratory.
     * @param labName        The name of the laboratory.
     * @param noOfEquipment  The number of equipment in the laboratory.
     * @param labStatus      The status of the laboratory.
     */

    public Lab(String labid, String labName, Integer noOfEquipment, String labStatus) {
        this.labid = labid;
        this.labName = labName;
        this.noOfEquipment = noOfEquipment;
        this.labStatus = labStatus;
    }

    // Getters and setters...

    public String getLabId() {
        return labid;
    }

    public void setLabId(String labId) {
        this.labid = labId;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public Integer getNoOfEquipment() {
        return noOfEquipment;
    }

    public void setNoOfEquipment(Integer noOfEquipment) {
        this.noOfEquipment = noOfEquipment;
    }

    public String getLabStatus() {
        return labStatus;
    }

    public void setLabStatus(String labStatus) {
        this.labStatus = labStatus;
    }
}
