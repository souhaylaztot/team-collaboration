package com.propertymanager;

import java.util.ArrayList;
import java.util.List;

public class Building {
    public int id;
    public String name;
    public String location;
    public int floors;
    public int totalApartments;
    public int occupiedApartments;
    public String status;
    public List<Apartment> apartments;
    
    public Building(int id, String name, String location, int floors, int totalApartments, int occupiedApartments, String status) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.floors = floors;
        this.totalApartments = totalApartments;
        this.occupiedApartments = occupiedApartments;
        this.status = status;
        this.apartments = new ArrayList<>();
    }
    
    public double getOccupancyPercentage() {
        return Math.round((double) occupiedApartments / totalApartments * 100);
    }
}