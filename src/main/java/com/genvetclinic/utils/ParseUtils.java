package com.genvetclinic.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code ParseUtils} class provides utility methods for parsing strings into specific data types
 * such as LocalTime, LocalDate, BigDecimal, and Integer.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class ParseUtils {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Parses a string into a LocalTime object.
     *
     * @param timeString The string to parse as LocalTime.
     * @return The parsed LocalTime, or {@code null} if parsing fails.
     */
    public static LocalTime parseLocalTime(String timeString) {
        try {
            return LocalTime.parse(timeString, TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parses a string into a LocalDate object.
     *
     * @param dateString The string to parse as LocalDate.
     * @return The parsed LocalDate, or {@code null} if parsing fails.
     */
    public static LocalDate parseLocalDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parses a string into a BigDecimal object.
     *
     * @param numberString The string to parse as BigDecimal.
     * @return The parsed BigDecimal, or {@code null} if parsing fails.
     */
    public static BigDecimal parseBigDecimal(String numberString) {
        try {
            return new BigDecimal(numberString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses a string into an Integer object.
     *
     * @param numberString The string to parse as Integer.
     * @return The parsed Integer, or {@code null} if parsing fails.
     */
    public static Integer parseInteger(String numberString) {
        try {
            return Integer.parseInt(numberString);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
