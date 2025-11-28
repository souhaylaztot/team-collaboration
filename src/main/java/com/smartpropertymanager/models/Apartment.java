package com.smartpropertymanager.models;

import javafx.beans.property.*;

public class Apartment {
    private final StringProperty number;
    private final IntegerProperty floor;
    private final StringProperty size;
    private final IntegerProperty bedrooms;
    private final IntegerProperty price;
    private final StringProperty status;
    private final StringProperty buyer;
    
    public Apartment(String number, int floor, String size, int bedrooms, int price, String status, String buyer) {
        this.number = new SimpleStringProperty(number);
        this.floor = new SimpleIntegerProperty(floor);
        this.size = new SimpleStringProperty(size);
        this.bedrooms = new SimpleIntegerProperty(bedrooms);
        this.price = new SimpleIntegerProperty(price);
        this.status = new SimpleStringProperty(status);
        this.buyer = new SimpleStringProperty(buyer);
    }
    
    // Property getters for TableView
    public StringProperty numberProperty() { return number; }
    public IntegerProperty floorProperty() { return floor; }
    public StringProperty sizeProperty() { return size; }
    public IntegerProperty bedroomsProperty() { return bedrooms; }
    public StringProperty priceFormattedProperty() { 
        return new SimpleStringProperty(String.format("%,d MAD", price.get())); 
    }
    public StringProperty statusProperty() { return status; }
    public StringProperty buyerProperty() { return buyer; }
    
    // Regular getters
    public String getNumber() { return number.get(); }
    public int getFloor() { return floor.get(); }
    public String getSize() { return size.get(); }
    public int getBedrooms() { return bedrooms.get(); }
    public int getPrice() { return price.get(); }
    public String getStatus() { return status.get(); }
    public String getBuyer() { return buyer.get(); }
}
