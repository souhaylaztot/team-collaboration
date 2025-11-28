package com.smartpropertymanager.models;

public class MaintenanceRequest {
    private String id;
    private String building;
    private String apartment;
    private String requestedBy;
    private String issue;
    private String description;
    private String priority;
    private String status;
    private String date;
    private int estimatedCost;
    private String assignedTo;
    private String completedDate;

    public MaintenanceRequest(String id, String building, String apartment, String requestedBy, 
                            String issue, String description, String priority, String status,
                            String date, int estimatedCost, String assignedTo, String completedDate) {
        this.id = id;
        this.building = building;
        this.apartment = apartment;
        this.requestedBy = requestedBy;
        this.issue = issue;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.date = date;
        this.estimatedCost = estimatedCost;
        this.assignedTo = assignedTo;
        this.completedDate = completedDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public String getApartment() { return apartment; }
    public void setApartment(String apartment) { this.apartment = apartment; }

    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(int estimatedCost) { this.estimatedCost = estimatedCost; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getCompletedDate() { return completedDate; }
    public void setCompletedDate(String completedDate) { this.completedDate = completedDate; }
}
