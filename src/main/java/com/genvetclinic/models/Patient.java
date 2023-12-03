package com.genvetclinic.models;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The {@code Patient} class represents an animal patient in the veterinary clinic system.
 * Patients have various attributes such as patient ID, name, species, breed, age, color,
 * admission date, medical history, vaccine history, special instructions, owner information,
 * insurance details, weight, microchip ID, and gender.
 *
 * <p>This class provides methods to access and modify these attributes, allowing for the
 * management of patient information within the system.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class Patient {

    /**
     * The unique identifier for the animal patient.
     */
    private String patientid;

    /**
     * The name of the animal patient.
     */
    private String patientName;

    /**
     * The species of the animal patient.
     */
    private String patientSpecies;

    /**
     * The breed of the animal patient.
     */
    private String patientBreed;

    /**
     * The age of the animal patient.
     */
    private BigDecimal patientAge;

    /**
     * The color of the animal patient.
     */
    private String patientColor;

    /**
     * The date when the animal patient was admitted.
     */
    private LocalDate admittedDate;

    /**
     * The medical history of the animal patient.
     */
    private String medicalHistory;

    /**
     * The vaccine history of the animal patient.
     */
    private String vaccineHistory;

    /**
     * Special instructions for the care of the animal patient.
     */
    private String specialInstructions;

    /**
     * The name of the owner of the animal patient.
     */
    private String ownerName;

    /**
     * The contact information of the owner of the animal patient.
     */
    private String ownerContact;

    /**
     * The email address of the owner of the animal patient.
     */
    private String ownerEmail;

    /**
     * The address of the owner of the animal patient.
     */
    private String ownerAddress;

    /**
     * The insurance details of the animal patient.
     */
    private String patientInsurance;

    /**
     * The weight of the animal patient.
     */
    private BigDecimal patientWeight;

    /**
     * The microchip ID of the animal patient.
     */
    private String microchipId;

    /**
     * The gender of the animal patient.
     */
    private String patientGender;

    /**
     * Constructs a new {@code Patient} instance with default values.
     */
    public Patient() {
    }

    /**
     * Constructs a new {@code Patient} instance with the specified parameters.
     *
     * @param patientid          The unique identifier for the animal patient.
     * @param patientName        The name of the animal patient.
     * @param patientSpecies     The species of the animal patient.
     * @param patientBreed       The breed of the animal patient.
     * @param patientAge         The age of the animal patient.
     * @param patientColor       The color of the animal patient.
     * @param admittedDate       The date when the animal patient was admitted.
     * @param medicalHistory     The medical history of the animal patient.
     * @param vaccineHistory     The vaccine history of the animal patient.
     * @param specialInstructions Special instructions for the care of the animal patient.
     * @param ownerName          The name of the owner of the animal patient.
     * @param ownerContact       The contact information of the owner of the animal patient.
     * @param ownerEmail         The email address of the owner of the animal patient.
     * @param ownerAddress       The address of the owner of the animal patient.
     * @param patientInsurance   The insurance details of the animal patient.
     * @param patientWeight      The weight of the animal patient.
     * @param microchipId        The microchip ID of the animal patient.
     * @param patientGender      The gender of the animal patient.
     */

    public Patient(String patientid, String patientName, String patientSpecies, String patientBreed, BigDecimal patientAge, String patientColor, 
            LocalDate admittedDate, String medicalHistory, String vaccineHistory, String specialInstructions, String ownerName, 
            String ownerContact, String ownerEmail, String ownerAddress, String patientInsurance, BigDecimal patientWeight, 
            String microchipId, String patientGender) {
                this.patientid = patientid;
                this.patientName = patientName;
                this.patientSpecies = patientSpecies;
                this.patientBreed = patientBreed;
                this.patientAge = patientAge;
                this.patientColor = patientColor;
                this.admittedDate = admittedDate;
                this.medicalHistory = medicalHistory;
                this.vaccineHistory = vaccineHistory;
                this.specialInstructions = specialInstructions;
                this.ownerName = ownerName;
                this.ownerContact = ownerContact;
                this.ownerEmail = ownerEmail;
                this.ownerAddress = ownerAddress;
                this.patientInsurance = patientInsurance;
                this.patientWeight = patientWeight;
                this.microchipId = microchipId;
                this.patientGender = patientGender;
    }

    // Getter and setter methods for each attribute...

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientId) {
        this.patientid = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSpecies() {
        return patientSpecies;
    }

    public void setPatientSpecies(String species) {
        this.patientSpecies = species;
    }

    public String getPatientBreed() {
        return patientBreed;
    }

    public void setPatientBreed(String breed) {
        this.patientBreed = breed;
    }

    public BigDecimal getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(BigDecimal patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientColor() {
        return patientColor;
    }

    public void setPatientColor(String patientColor) {
        this.patientColor = patientColor;
    }

    public LocalDate getAdmittedDate() {
        return admittedDate;
    }

    public void setAdmittedDate(LocalDate admittedDate) {
        this.admittedDate = admittedDate;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getVaccineHistory() {
        return vaccineHistory;
    }

    public void setVaccineHistory(String vaccineHistory) {
        this.vaccineHistory = vaccineHistory;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getPatientInsurance() {
        return patientInsurance;
    }

    public void setPatientInsurance(String patientInsurance) {
        this.patientInsurance = patientInsurance;
    }

    public BigDecimal getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(BigDecimal patientWeight) {
        this.patientWeight = patientWeight;
    }

    public String getMicrochipId() {
        return microchipId;
    }

    public void setMicrochipId(String microchipid) {
        this.microchipId = microchipid;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }
}
