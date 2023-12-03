package com.genvetclinic.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The {@code Transaction} class represents a financial transaction in the veterinary clinic system.
 * Transactions have various attributes such as transaction ID, payer, payee, transaction type,
 * transaction description, transaction amount, amount received, payment method, receipt number,
 * transaction date and time, transaction change, and transaction status.
 *
 * <p>This class provides methods to access and modify these attributes, allowing for the
 * management of financial transactions within the system.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class Transaction {

    /**
     * The unique identifier for the financial transaction.
     */
    private String transactionId;

    /**
     * The entity making the payment in the transaction.
     */
    private String payer;

    /**
     * The entity receiving the payment in the transaction.
     */
    private String payee;

    /**
     * The type of the financial transaction (e.g., sale, purchase).
     */
    private String transactionType;

    /**
     * A description of the financial transaction.
     */
    private String transactionDesc;

    /**
     * The total amount of the financial transaction.
     */
    private BigDecimal transactionAmt;

    /**
     * The amount received in the financial transaction.
     */
    private BigDecimal amtReceived;

    /**
     * The payment method used in the financial transaction.
     */
    private String paymentMethod;

    /**
     * The receipt number associated with the financial transaction.
     */
    private String receiptNo;

    /**
     * The date and time when the financial transaction occurred.
     */
    private LocalDateTime transacDateTime;

    /**
     * The change amount in the financial transaction.
     */
    private BigDecimal transactionChange;

    /**
     * The status of the financial transaction (e.g., completed, pending).
     */
    private String transactionStatus;

    /**
     * Constructs a new {@code Transaction} instance with default values.
     */
    public Transaction() {
    }

    /**
     * Constructs a new {@code Transaction} instance with the specified parameters.
     *
     * @param transactionId       The unique identifier for the financial transaction.
     * @param payer               The entity making the payment in the transaction.
     * @param payee               The entity receiving the payment in the transaction.
     * @param transactionType     The type of the financial transaction.
     * @param transactionDesc     A description of the financial transaction.
     * @param transactionAmt      The total amount of the financial transaction.
     * @param amtReceived         The amount received in the financial transaction.
     * @param paymentMethod       The payment method used in the financial transaction.
     * @param receiptNo           The receipt number associated with the financial transaction.
     * @param transacDateTime     The date and time when the financial transaction occurred.
     * @param transactionChange   The change amount in the financial transaction.
     * @param transactionStatus   The status of the financial transaction.
     */
    public Transaction(
            String transactionId, String payer, String payee, String transactionType, String transactionDesc,
            BigDecimal transactionAmt, BigDecimal amtReceived, String paymentMethod, String receiptNo,
            LocalDateTime transacDateTime, BigDecimal transactionChange, String transactionStatus) {
        this.transactionId = transactionId;
        this.payer = payer;
        this.payee = payee;
        this.transactionType = transactionType;
        this.transactionDesc = transactionDesc;
        this.transactionAmt = transactionAmt;
        this.amtReceived = amtReceived;
        this.paymentMethod = paymentMethod;
        this.receiptNo = receiptNo;
        this.transacDateTime = transacDateTime;
        this.transactionChange = transactionChange;
        this.transactionStatus = transactionStatus;
    }

    // Getter and setter methods for each attribute...

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }

    public BigDecimal getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(BigDecimal transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public BigDecimal getAmtReceived() {
        return amtReceived;
    }

    public void setAmtReceived(BigDecimal amtReceived) {
        this.amtReceived = amtReceived;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public LocalDateTime getTransacDateTime() {
        return transacDateTime;
    }

    public void setTransacDateTime(LocalDateTime transacDateTime) {
        this.transacDateTime = transacDateTime;
    }

    public BigDecimal getTransactionChange() {
        return transactionChange;
    }

    public void setTransactionChange(BigDecimal transactionChange) {
        this.transactionChange = transactionChange;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
