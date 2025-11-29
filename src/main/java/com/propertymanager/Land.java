package com.propertymanager;

public class Land {
    public int id;
    public String name;
    public String location;
    public String area;
    public String owner;
    public double estimatedValue;
    public String status;
    public String description;
    public int documents;
    
    public Land(int id, String name, String location, String area, String owner, 
               double estimatedValue, String status, String description, int documents) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.area = area;
        this.owner = owner;
        this.estimatedValue = estimatedValue;
        this.status = status;
        this.description = description;
        this.documents = documents;
    }
    
    // Getters for PropertyValueFactory
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getArea() { return area; }
    public String getOwner() { return owner; }
    public double getEstimatedValue() { return estimatedValue; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public int getDocuments() { return documents; }
}