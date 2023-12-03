package com.genvetclinic.models;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The {@code Boarder} class represents an animal boarder in the veterinary clinic system.
 * Boarders have various attributes such as boarder ID, name, species, breed, color, special instructions,
 * owner information, boarding and departure dates, age, gender, and weight.
 *
 * <p>This class provides methods to access and modify these attributes, allowing for the
 * management of boarder information within the system.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class Boarder {

    /**
     * The unique identifier for the animal boarder.
     */
    private String boarderId;

    /**
     * The name of the animal boarder.
     */
    private String boarderName;

    /**
     * The species of the animal boarder.
     */
    private String boarderSpecies;

    /**
     * The breed of the animal boarder.
     */
    private String boarderBreed;

    /**
     * The color of the animal boarder.
     */
    private String boarderColor;

    /**
     * Special instructions for the care of the animal boarder.
     */
    private String bSpecialInstructions;

    /**
     * The name of the owner of the animal boarder.
     */
    private String bOwnerName;

    /**
     * The contact information of the owner of the animal boarder.
     */
    private String bOwnerContact;

    /**
     * The address of the owner of the animal boarder.
     */
    private String bOwnerAddress;

    /**
     * The date when the animal boarder was boarded.
     */
    private LocalDate dateBoarded;

    /**
     * The age of the animal boarder.
     */
    private BigDecimal boarderAge;

    /**
     * The gender of the animal boarder.
     */
    private String boarderGender;

    /**
     * The weight of the animal boarder.
     */
    private BigDecimal boarderWeight;

    /**
     * The date when the animal boarder departed.
     */
    private LocalDate dateDeparted;

    /**
     * The email address of the owner of the animal boarder.
     */
    private String bOwnerEmail;

    /**
     * Constructs a new {@code Boarder} instance with default values.
     */
    public Boarder() {
    }

    /**
     * Constructs a new {@code Boarder} instance with the specified parameters.
     *
     * @param boarderId          The unique identifier for the animal boarder.
     * @param boarderName        The name of the animal boarder.
     * @param boarderSpecies     The species of the animal boarder.
     * @param boarderBreed       The breed of the animal boarder.
     * @param boarderColor       The color of the animal boarder.
     * @param bSpecialInstructions Special instructions for the care of the animal boarder.
     * @param bOwnerName         The name of the owner of the animal boarder.
     * @param bOwnerContact      The contact information of the owner of the animal boarder.
     * @param bOwnerAddress      The address of the owner of the animal boarder.
     * @param dateBoarded        The date when the animal boarder was boarded.
     * @param boarderAge         The age of the animal boarder.
     * @param boarderGender      The gender of the animal boarder.
     * @param boarderWeight      The weight of the animal boarder.
     * @param dateDeparted       The date when the animal boarder departed.
     * @param bOwnerEmail        The email address of the owner of the animal boarder.
     */
    public Boarder(String boarderId, String boarderName, String boarderSpecies, String boarderBreed,
                   String boarderColor, String bSpecialInstructions, String bOwnerName, String bOwnerContact,
                   String bOwnerAddress, LocalDate dateBoarded, BigDecimal boarderAge, String boarderGender,
                   BigDecimal boarderWeight, LocalDate dateDeparted, String bOwnerEmail) {
        this.boarderId = boarderId;
        this.boarderName = boarderName;
        this.boarderSpecies = boarderSpecies;
        this.boarderBreed = boarderBreed;
        this.boarderColor = boarderColor;
        this.bSpecialInstructions = bSpecialInstructions;
        this.bOwnerName = bOwnerName;
        this.bOwnerContact = bOwnerContact;
        this.bOwnerAddress = bOwnerAddress;
        this.dateBoarded = dateBoarded;
        this.boarderAge = boarderAge;
        this.boarderGender = boarderGender;
        this.boarderWeight = boarderWeight;
        this.dateDeparted = dateDeparted;
        this.bOwnerEmail = bOwnerEmail;
    }

    // Getter and setter methods for each attribute...

public String getBoarderId() {
        return boarderId;
    }

    public void setBoarderId(String boarderId) {
        this.boarderId = boarderId;
    }

    public String getBoarderName() {
        return boarderName;
    }

    public void setBoarderName(String boarderName) {
        this.boarderName = boarderName;
    }

    public String getBoarderSpecies() {
        return boarderSpecies;
    }

    public void setBoarderSpecies(String boarderSpecies) {
        this.boarderSpecies = boarderSpecies;
    }

    public String getBoarderBreed() {
        return boarderBreed;
    }

    public void setBoarderBreed(String boarderBreed) {
        this.boarderBreed = boarderBreed;
    }

    public BigDecimal getBoarderAge() {
        return boarderAge;
    }

    public void setBoarderAge(BigDecimal boarderAge) {
        this.boarderAge = boarderAge;
    }

    public String getBoarderColor() {
        return boarderColor;
    }

    public void setBoarderColor(String boarderColor) {
        this.boarderColor = boarderColor;
    }

    public LocalDate getBoardedDate() {
        return dateBoarded;
    }

    public void setBoardedDate(LocalDate dateBoarded) {
        this.dateBoarded = dateBoarded;
    }

    public String getbSpecialInstructions() {
        return bSpecialInstructions;
    }

    public void setbSpecialInstructions(String bSpecialInstructions) {
        this.bSpecialInstructions = bSpecialInstructions;
    }

    public String getBrdrOwnerName() {
        return bOwnerName;
    }

    public void setBrdrOwnerName(String bOwnerName) {
        this.bOwnerName = bOwnerName;
    }

    public String getBrdrOwnerContact() {
        return bOwnerContact;
    }

    public void setBrdrOwnerContact(String bOwnerContact) {
        this.bOwnerContact = bOwnerContact;
    }

    public String getBrdrOwnerAddress() {
        return bOwnerAddress;
    }

    public void setBrdrOwnerAddress(String bOwnerAddress) {
        this.bOwnerAddress = bOwnerAddress;
    }

    public String getBoarderGender() {
        return boarderGender;
    }

    public void setBoarderGender(String boarderGender) {
        this.boarderGender = boarderGender;
    }

    public BigDecimal getBoarderWeight() {
        return boarderWeight;
    }

    public void setBoarderWeight(BigDecimal boarderWeight) {
        this.boarderWeight = boarderWeight;
    }

    public LocalDate getDateDeparted() {
        return dateDeparted;
    }

    public void setDateDeparted(LocalDate dateDeparted) {
        this.dateDeparted = dateDeparted;
    }

    public String getBrdrOwnerEmail() {
        return bOwnerEmail;
    }

    public void setBrdrOwnerEmail(String bOwnerEmail) {
        this.bOwnerEmail = bOwnerEmail;
    }

}
