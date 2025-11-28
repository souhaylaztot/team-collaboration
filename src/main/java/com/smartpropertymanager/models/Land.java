package com.smartpropertymanager.models;

public class Land {
    private final int id;
    private final String name;
    private final String location;
    private final String area;
    private final String owner;
    private final int estimatedValue;
    private final String status;
    private final String description;
    private final int documents;

    public Land(int id, String name, String location, String area, String owner, 
                int estimatedValue, String status, String description, int documents) {
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

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getArea() { return area; }
    public String getOwner() { return owner; }
    public int getEstimatedValue() { return estimatedValue; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public int getDocuments() { return documents; }
}
