package com.genvetclinic.utils;

import java.math.BigDecimal;
// import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
import java.util.Arrays;
// import java.util.Date;

/**
 * The {@code ValidationUtils} class provides utility methods for performing common validation tasks.
 * It includes methods for checking username matches, string null or empty, email format, number positivity,
 * name validity, integer format, time format, string length, and date format.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class ValidationUtils {

    /**
     * Checks if the input username matches the stored username.
     *
     * @param inputUsername The input username to check.
     * @param storedUsername The stored username for comparison.
     * @return {@code true} if the usernames match, {@code false} otherwise.
     */
    public static boolean isUsernameMatch(String inputUsername, String storedUsername) {
        return inputUsername.equals(storedUsername);
    }

    /**
     * Checks if any of the provided strings is null or empty.
     *
     * @param values The strings to check for null or emptiness.
     * @return {@code true} if any string is null or empty, {@code false} otherwise.
     */
    public static boolean isNullOrEmpty(String... values) {
        return Arrays.stream(values).anyMatch(value -> value == null || value.trim().isEmpty());
    }

    /**
     * Validates an email address format.
     *
     * @param email The email address to validate.
     * @return {@code true} if the email format is valid, {@code false} otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Validates the positivity of BigDecimal numbers.
     *
     * @param numbers The BigDecimal numbers to validate.
     * @return {@code true} if all numbers are non-null and non-negative, {@code false} otherwise.
     */
    public static boolean isValidNumber(BigDecimal... numbers) {
        for (BigDecimal number : numbers) {
            if (number == null || number.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        }
        return true;
    }       
    
    /**
     * Validates a name format.
     *
     * @param name The name to validate.
     * @return {@code true} if the name format is valid, {@code false} otherwise.
     */
    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z\\s]+$");
    }

    /**
     * Validates if a string represents a valid integer format.
     *
     * @param number The string to validate as an integer.
     * @return {@code true} if the string is a valid integer, {@code false} otherwise.
     */
    public static boolean isValidInteger(String number) {
        return number.matches("\\d+"); 
    }

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Validates the format of a time string.
     *
     * @param time The time string to validate.
     * @return {@code true} if the time format is valid, {@code false} otherwise.
     */
    public static boolean isValidTimeFormat(String time) {
        try {
            // Attempt to parse the input time with the specified format
            TIME_FORMATTER.parse(time);
            return true;
        } catch (DateTimeParseException e) {
            // Parsing failed, indicating an invalid time format
            return false;
        }
    }
    
    /**
     * Checks if a string is too short based on a specified minimum length.
     *
     * @param input    The input string to check.
     * @param minLength The minimum length required.
     * @return {@code true} if the string is too short, {@code false} otherwise.
     */
    public static boolean isTooShort(String input, int minLength) {
        return input.length() < minLength;
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Validates the format of a date string.
     *
     * @param dateString The date string to validate.
     * @return {@code true} if the date format is valid, {@code false} otherwise.
     */
    public static boolean isValidDateFormat(String dateString) {
        try {
            LocalDate.parse(dateString, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
}
