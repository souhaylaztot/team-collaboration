package com.smartpropertymanager.models;

public class Message {
    private int id;
    private String sender;
    private String text;
    private String time;
    private boolean isStaff;

    public Message(int id, String sender, String text, String time, boolean isStaff) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.time = time;
        this.isStaff = isStaff;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isStaff() { return isStaff; }
    public void setStaff(boolean staff) { isStaff = staff; }
}
