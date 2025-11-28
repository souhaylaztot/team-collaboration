package com.smartpropertymanager.models;

public class Building {
    private final int id;
    private final String name;
    private final String location;
    private final int floors;
    private final int totalApartments;
    private final int occupiedApartments;
    private final String status;
    
    public Building(int id, String name, String location, int floors, int totalApartments, int occupiedApartments, String status) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.floors = floors;
        this.totalApartments = totalApartments;
        this.occupiedApartments = occupiedApartments;
        this.status = status;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getFloors() { return floors; }
    public int getTotalApartments() { return totalApartments; }
    public int getOccupiedApartments() { return occupiedApartments; }
    public String getStatus() { return status; }
}
