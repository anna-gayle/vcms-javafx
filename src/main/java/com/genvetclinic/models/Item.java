package com.genvetclinic.models;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The {@code Item} class represents an inventory item in the veterinary clinic system.
 * Items have various attributes such as item ID, name, type, quantity, unit cost, supplier,
 * expiration date, status, and total cost.
 *
 * <p>This class provides methods to access and modify these attributes, allowing for the
 * management of inventory items within the system.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class Item {
     /**
     * The unique identifier for the inventory item.
     */
    private String itemid;

    /**
     * The name of the inventory item.
     */
    private String itemName;

    /**
     * The type or category of the inventory item.
     */
    private String itemType;

    /**
     * The quantity of the inventory item available in stock.
     */
    private Integer itemQuantity;

    /**
     * The unit cost of the inventory item.
     */
    private BigDecimal unitCost;

    /**
     * The supplier providing the inventory item.
     */
    private String supplier;

    /**
     * The expiration date of the inventory item.
     */
    private LocalDate expDate;

    /**
     * The status of the inventory item (e.g., available, out of stock).
     */
    private String itemStatus;

    /**
     * The total cost of the inventory item based on quantity and unit cost.
     */
    private BigDecimal totalCost;

    /**
     * Constructs a new {@code Item} instance with default values.
     */
    public Item() {
    }

    /**
     * Constructs a new {@code Item} instance with the specified parameters.
     *
     * @param itemid        The unique identifier for the inventory item.
     * @param itemName      The name of the inventory item.
     * @param itemType      The type or category of the inventory item.
     * @param itemQuantity  The quantity of the inventory item available in stock.
     * @param unitCost      The unit cost of the inventory item.
     * @param supplier      The supplier providing the inventory item.
     * @param expDate       The expiration date of the inventory item.
     * @param itemStatus    The status of the inventory item.
     * @param totalCost     The total cost of the inventory item based on quantity and unit cost.
     */
    public Item(String itemid, String itemName, String itemType, Integer itemQuantity, BigDecimal unitCost, String supplier,
        LocalDate expDate, String itemStatus, BigDecimal totalCost) {
            this.itemid = itemid;
            this.itemName = itemName;
            this.itemType = itemType;
            this.itemQuantity = itemQuantity;
            this.unitCost = unitCost;
            this.supplier = supplier;
            this.expDate = expDate;
            this.itemStatus = itemStatus;
            this.totalCost = totalCost;
    }

    // Getter and setter methods for each attribute...

    public String getItemId() {
        return itemid;
    }

    public void setItemId(String itemId) {
        this.itemid = itemId;
    }

    public String getItemName() {
        return itemName;
    }
     
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
