package com.propertymanager;

public class Apartment {
    public String number;
    public int floor;
    public String size;
    public int bedrooms;
    public double price;
    public String status;
    public String buyer;
    
    public Apartment(String number, int floor, String size, int bedrooms, double price, String status, String buyer) {
        this.number = number;
        this.floor = floor;
        this.size = size;
        this.bedrooms = bedrooms;
        this.price = price;
        this.status = status;
        this.buyer = buyer;
    }
}