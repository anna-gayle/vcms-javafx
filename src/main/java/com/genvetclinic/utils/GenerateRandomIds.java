package com.genvetclinic.utils;

import java.util.Random;

/**
 * The {@code GenerateRandomIds} class provides a method to generate random alphanumeric IDs.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class GenerateRandomIds {
    
    /**
     * Generates a random alphanumeric ID of the specified length.
     *
     * @param length The length of the generated ID.
     * @return The randomly generated alphanumeric ID.
     */
    public static String generateRandomId(int length) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(letters.length());
            sb.append(letters.charAt(index));
        }

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(numbers.length());
            sb.append(numbers.charAt(index));
        }

        return sb.toString();
    }
}
