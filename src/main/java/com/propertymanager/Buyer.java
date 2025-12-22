package com.propertymanager;

import javafx.beans.property.*;

public class Buyer {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty property;
    private final StringProperty phone;
    private final StringProperty email;
    private final StringProperty purchaseDate;
    private final IntegerProperty purchaseAmount;
    private final IntegerProperty paidAmount;
    private final IntegerProperty remainingAmount;
    private final StringProperty paymentStatus;
    private final StringProperty lastPayment;
    private final StringProperty nextDue;

    public Buyer(int id, String name, String property, String phone, String email, 
                 String purchaseDate, int purchaseAmount, int paidAmount, int remainingAmount,
                 String paymentStatus, String lastPayment, String nextDue) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.property = new SimpleStringProperty(property);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.purchaseDate = new SimpleStringProperty(purchaseDate);
        this.purchaseAmount = new SimpleIntegerProperty(purchaseAmount);
        this.paidAmount = new SimpleIntegerProperty(paidAmount);
        this.remainingAmount = new SimpleIntegerProperty(remainingAmount);
        this.paymentStatus = new SimpleStringProperty(paymentStatus);
        this.lastPayment = new SimpleStringProperty(lastPayment);
        this.nextDue = new SimpleStringProperty(nextDue);
    }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty propertyProperty() { return property; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty emailProperty() { return email; }
    public StringProperty purchaseDateProperty() { return purchaseDate; }
    public IntegerProperty purchaseAmountProperty() { return purchaseAmount; }
    public IntegerProperty paidAmountProperty() { return paidAmount; }
    public IntegerProperty remainingAmountProperty() { return remainingAmount; }
    public StringProperty paymentStatusProperty() { return paymentStatus; }
    public StringProperty lastPaymentProperty() { return lastPayment; }
    public StringProperty nextDueProperty() { return nextDue; }

    // Value getters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getProperty() { return property.get(); }
    public String getPhone() { return phone.get(); }
    public String getEmail() { return email.get(); }
    public String getPurchaseDate() { return purchaseDate.get(); }
    public int getPurchaseAmount() { return purchaseAmount.get(); }
    public int getPaidAmount() { return paidAmount.get(); }
    public int getRemainingAmount() { return remainingAmount.get(); }
    public String getPaymentStatus() { return paymentStatus.get(); }
    public String getLastPayment() { return lastPayment.get(); }
    public String getNextDue() { return nextDue.get(); }

    // Formatted properties for display
    public StringProperty purchaseAmountFormattedProperty() {
        return new SimpleStringProperty(String.format("%,d MAD", getPurchaseAmount()));
    }

    public StringProperty remainingAmountFormattedProperty() {
        return new SimpleStringProperty(String.format("%,d MAD", getRemainingAmount()));
    }
}