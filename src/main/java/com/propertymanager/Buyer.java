package com.propertymanager;

public class Buyer {
    public int id;
    public String name;
    public String property;
    public String phone;
    public String email;
    public String purchaseDate;
    public double purchaseAmount;
    public double paidAmount;
    public double remainingAmount;
    public String paymentStatus;
    public String lastPayment;
    public String nextDue;
    
    public Buyer(int id, String name, String property, String phone, String email, 
                String purchaseDate, double purchaseAmount, double paidAmount, 
                String paymentStatus, String lastPayment, String nextDue) {
        this.id = id;
        this.name = name;
        this.property = property;
        this.phone = phone;
        this.email = email;
        this.purchaseDate = purchaseDate;
        this.purchaseAmount = purchaseAmount;
        this.paidAmount = paidAmount;
        this.remainingAmount = purchaseAmount - paidAmount;
        this.paymentStatus = paymentStatus;
        this.lastPayment = lastPayment;
        this.nextDue = nextDue;
    }
    
    // Getters for PropertyValueFactory
    public String getName() { return name; }
    public String getProperty() { return property; }
    public String getPurchaseDate() { return purchaseDate; }
    public double getPurchaseAmount() { return purchaseAmount; }
    public double getRemainingAmount() { return remainingAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getNextDue() { return nextDue; }
}