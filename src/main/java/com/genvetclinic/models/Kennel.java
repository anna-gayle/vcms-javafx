package com.genvetclinic.models;

/**
 * The {@code Kennel} class represents an animal kennel in the veterinary clinic system.
 * Kennels have various attributes such as kennel ID, name, capacity, and status.
 *
 * <p>This class provides methods to access and modify these attributes, allowing for the
 * management of kennel information within the system.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class Kennel {
    /**
     * The unique identifier for the animal kennel.
     */
    private String kennelid;

    /**
     * The name of the animal kennel.
     */
    private String kennelName;

    /**
     * The capacity of the animal kennel.
     */
    private Integer kennelCapacity;

    /**
     * The status of the animal kennel (e.g., occupied, available).
     */
    private String kennelStatus;

    /**
     * Constructs a new {@code Kennel} instance with default values.
     */
    public Kennel() {
    }

    /**
     * Constructs a new {@code Kennel} instance with the specified parameters.
     *
     * @param kennelid        The unique identifier for the animal kennel.
     * @param kennelName      The name of the animal kennel.
     * @param kennelCapacity  The capacity of the animal kennel.
     * @param kennelStatus    The status of the animal kennel.
     */
    public Kennel(String kennelid, String kennelName, Integer kennelCapacity, String kennelStatus) {
        this.kennelid = kennelid;
        this.kennelName = kennelName;
        this.kennelCapacity = kennelCapacity;
        this.kennelStatus = kennelStatus;
    }

    // Getters and setters...

    public String getKennelId() {
        return kennelid;
    }

    public void setKennelId(String kennelId) {
        this.kennelid = kennelId;
    }

    public String getKennelName() {
        return kennelName;
    }

    public void setKennelName(String kennelName) {
        this.kennelName = kennelName;
    }

    public Integer getKennelCapacity() {
        return kennelCapacity;
    }

    public void setKennelCapacity(Integer kennelCapacity) {
        this.kennelCapacity = kennelCapacity;
    }

    public String getKennelStatus() {
        return kennelStatus;
    }

    public void setKennelStatus(String kennelStatus) {
        this.kennelStatus = kennelStatus;
    }

}
