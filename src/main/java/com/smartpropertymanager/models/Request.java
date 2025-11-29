package com.smartpropertymanager.models;

import java.util.List;

public class Request {
    private String id;
    private String from;
    private String building;
    private String apartment;
    private String type;
    private String subject;
    private String status;
    private String date;
    private String priority;
    private List<Message> messages;

    public Request(String id, String from, String building, String apartment, String type, 
                  String subject, String status, String date, String priority, List<Message> messages) {
        this.id = id;
        this.from = from;
        this.building = building;
        this.apartment = apartment;
        this.type = type;
        this.subject = subject;
        this.status = status;
        this.date = date;
        this.priority = priority;
        this.messages = messages;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public String getApartment() { return apartment; }
    public void setApartment(String apartment) { this.apartment = apartment; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
}
