package com.smartpropertymanager.models;

public class Permit {
    private final String id;
    private final String landName;
    private final String type;
    private final String requestedBy;
    private final String requestDate;
    private final String status;
    private final int estimatedCost;
    private final String description;
    private final String approvedBy;
    private final String approvalDate;

    public Permit(String id, String landName, String type, String requestedBy, String requestDate, 
                  String status, int estimatedCost, String description, String approvedBy, String approvalDate) {
        this.id = id;
        this.landName = landName;
        this.type = type;
        this.requestedBy = requestedBy;
        this.requestDate = requestDate;
        this.status = status;
        this.estimatedCost = estimatedCost;
        this.description = description;
        this.approvedBy = approvedBy;
        this.approvalDate = approvalDate;
    }

    // Getters
    public String getId() { return id; }
    public String getLandName() { return landName; }
    public String getType() { return type; }
    public String getRequestedBy() { return requestedBy; }
    public String getRequestDate() { return requestDate; }
    public String getStatus() { return status; }
    public int getEstimatedCost() { return estimatedCost; }
    public String getDescription() { return description; }
    public String getApprovedBy() { return approvedBy; }
    public String getApprovalDate() { return approvalDate; }
    
    public String getFormattedCost() {
        return String.format("%,d MAD", estimatedCost);
    }
}