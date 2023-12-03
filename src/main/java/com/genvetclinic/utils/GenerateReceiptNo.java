package com.genvetclinic.utils;

import java.util.Random;

/**
 * The {@code GenerateReceiptNo} class provides a method to generate random receipt numbers.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class GenerateReceiptNo {

    /**
     * Generates a random receipt number.
     *
     * @return The randomly generated receipt number.
     */
    public static int generateRandomReceiptNo() {
        Random random = new Random();
        int receiptNo = random.nextInt((int) Math.pow(10, 10));

        return receiptNo;
    }
}
