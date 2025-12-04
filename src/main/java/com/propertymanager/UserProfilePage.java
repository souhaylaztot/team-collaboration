package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserProfilePage extends VBox {
    
    public UserProfilePage() {
        initProfilePage();
    }
    
    private void initProfilePage() {
        setSpacing(30);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: #f8fafc;");
        
        // Header
        VBox header = createHeader();
        
        // Main content
        HBox mainContent = new HBox(30);
        mainContent.setAlignment(Pos.TOP_LEFT);
        
        // Left column - Profile info
        VBox leftColumn = createLeftColumn();
        leftColumn.setPrefWidth(400);
        
        // Right column - Activity & Settings
        VBox rightColumn = createRightColumn();
        rightColumn.setPrefWidth(500);
        
        mainContent.getChildren().addAll(leftColumn, rightColumn);
        
        getChildren().addAll(header, mainContent);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("👤 User Profile");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#1a202c"));
        
        Label subtitle = new Label("Manage your personal information and preferences");
        subtitle.setFont(Font.font("Segoe UI", 16));
        subtitle.setTextFill(Color.web("#4a5568"));
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    private VBox createLeftColumn() {
        VBox column = new VBox(25);
        
        // Profile card
        VBox profileCard = createProfileCard();
        
        // My properties card
        VBox propertiesCard = createMyPropertiesCard();
        
        column.getChildren().addAll(profileCard, propertiesCard);
        return column;
    }
    
    private VBox createProfileCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        // Profile picture and basic info
        VBox profileInfo = new VBox(20);
        profileInfo.setAlignment(Pos.CENTER);
        
        Button profilePic = new Button("👤");
        profilePic.setStyle("-fx-background-color: linear-gradient(135deg, #8b5cf6, #a855f7); -fx-text-fill: white; -fx-background-radius: 50; -fx-font-size: 40px; -fx-padding: 20;");
        profilePic.setPrefSize(100, 100);
        
        Label name = new Label("Sarah Johnson");
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        name.setTextFill(Color.web("#1a202c"));
        
        Label role = new Label("Property Owner");
        role.setFont(Font.font("Segoe UI", 16));
        role.setTextFill(Color.web("#4a5568"));
        
        Button editBtn = new Button("✏️ Edit Profile");
        editBtn.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #374151; -fx-background-radius: 10; -fx-padding: 12 20; -fx-cursor: hand;");
        editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: #e5e7eb; -fx-text-fill: #374151; -fx-background-radius: 10; -fx-padding: 12 20; -fx-cursor: hand;"));
        editBtn.setOnMouseExited(e -> editBtn.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #374151; -fx-background-radius: 10; -fx-padding: 12 20; -fx-cursor: hand;"));
        
        profileInfo.getChildren().addAll(profilePic, name, role, editBtn);
        
        // Contact info
        VBox contactInfo = new VBox(15);
        contactInfo.getChildren().addAll(
            createInfoRow("📧", "Email", "sarah.johnson@email.com"),
            createInfoRow("📱", "Phone", "+212 6 98 76 54 32"),
            createInfoRow("🏠", "Address", "123 Main Street, Casablanca"),
            createInfoRow("📅", "Member Since", "March 10, 2023")
        );
        
        card.getChildren().addAll(profileInfo, new Separator(), contactInfo);
        return card;
    }
    
    private VBox createMyPropertiesCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        Label title = new Label("🏠 My Properties");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1a202c"));
        
        VBox properties = new VBox(15);
        properties.getChildren().addAll(
            createPropertyItem("🏢", "Apartment 205", "Downtown Plaza", "Rented", "#10b981"),
            createPropertyItem("🏠", "Villa 12", "Green Valley", "Available", "#f59e0b"),
            createPropertyItem("🏬", "Office 304", "Business Center", "Rented", "#10b981")
        );
        
        Button viewAllBtn = new Button("👁️ View All Properties");
        viewAllBtn.setStyle("-fx-background-color: #8b5cf6; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 12 20; -fx-cursor: hand;");
        viewAllBtn.setPrefWidth(250);
        
        card.getChildren().addAll(title, properties, viewAllBtn);
        return card;
    }
    
    private VBox createRightColumn() {
        VBox column = new VBox(25);
        
        // Activity card
        VBox activityCard = createActivityCard();
        
        // Settings card
        VBox settingsCard = createSettingsCard();
        
        column.getChildren().addAll(activityCard, settingsCard);
        return column;
    }
    
    private VBox createActivityCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        Label title = new Label("📊 My Activity");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1a202c"));
        
        GridPane stats = new GridPane();
        stats.setHgap(20);
        stats.setVgap(20);
        
        stats.add(createStatBox("3", "Properties", "#8b5cf6"), 0, 0);
        stats.add(createStatBox("2", "Active Rentals", "#10b981"), 1, 0);
        stats.add(createStatBox("5", "Maintenance Requests", "#f59e0b"), 0, 1);
        stats.add(createStatBox("12", "Messages", "#3b82f6"), 1, 1);
        
        // Recent activity
        VBox recentActivity = new VBox(15);
        Label activityTitle = new Label("🕒 Recent Activity");
        activityTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        activityTitle.setTextFill(Color.web("#1a202c"));
        
        VBox activities = new VBox(10);
        activities.getChildren().addAll(
            createActivityItem("💰", "Rent payment received", "2 hours ago"),
            createActivityItem("🔧", "Maintenance request submitted", "1 day ago"),
            createActivityItem("📝", "Lease agreement signed", "3 days ago")
        );
        
        recentActivity.getChildren().addAll(activityTitle, activities);
        
        card.getChildren().addAll(title, stats, new Separator(), recentActivity);
        return card;
    }
    
    private VBox createSettingsCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        Label title = new Label("⚙️ Preferences");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1a202c"));
        
        VBox settings = new VBox(20);
        settings.getChildren().addAll(
            createSettingRow("🔔", "Email Notifications", true),
            createSettingRow("📱", "SMS Notifications", false),
            createSettingRow("🌙", "Dark Mode", false),
            createSettingRow("📧", "Marketing Emails", true)
        );
        
        Button saveBtn = new Button("💾 Save Preferences");
        saveBtn.setStyle("-fx-background-color: linear-gradient(135deg, #8b5cf6, #a855f7); -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 15 30; -fx-font-size: 16px; -fx-cursor: hand;");
        saveBtn.setPrefWidth(200);
        
        card.getChildren().addAll(title, settings, saveBtn);
        return card;
    }
    
    private HBox createInfoRow(String icon, String label, String value) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        
        Label labelText = new Label(label + ":");
        labelText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        labelText.setTextFill(Color.web("#4a5568"));
        labelText.setPrefWidth(80);
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("Segoe UI", 14));
        valueText.setTextFill(Color.web("#1a202c"));
        
        row.getChildren().addAll(iconLabel, labelText, valueText);
        return row;
    }
    
    private HBox createPropertyItem(String icon, String name, String location, String status, String statusColor) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 10;");
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(20));
        
        VBox info = new VBox(5);
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.web("#1a202c"));
        
        Label locationLabel = new Label(location);
        locationLabel.setFont(Font.font("Segoe UI", 12));
        locationLabel.setTextFill(Color.web("#6b7280"));
        
        info.getChildren().addAll(nameLabel, locationLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-background-color: " + statusColor + "; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 5 12; -fx-font-size: 12px;");
        
        item.getChildren().addAll(iconLabel, info, spacer, statusLabel);
        return item;
    }
    
    private VBox createStatBox(String number, String label, String color) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 15;");
        box.setPrefSize(120, 100);
        
        Label numLabel = new Label(number);
        numLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        numLabel.setTextFill(Color.WHITE);
        
        Label textLabel = new Label(label);
        textLabel.setFont(Font.font("Segoe UI", 12));
        textLabel.setTextFill(Color.WHITE);
        textLabel.setWrapText(true);
        textLabel.setAlignment(Pos.CENTER);
        
        box.getChildren().addAll(numLabel, textLabel);
        return box;
    }
    
    private HBox createActivityItem(String icon, String text, String time) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        
        VBox textBox = new VBox(2);
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Segoe UI", 14));
        textLabel.setTextFill(Color.web("#1a202c"));
        
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Segoe UI", 12));
        timeLabel.setTextFill(Color.web("#6b7280"));
        
        textBox.getChildren().addAll(textLabel, timeLabel);
        
        item.getChildren().addAll(iconLabel, textBox);
        return item;
    }
    
    private HBox createSettingRow(String icon, String text, boolean enabled) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Segoe UI", 14));
        textLabel.setTextFill(Color.web("#1a202c"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        CheckBox toggle = new CheckBox();
        toggle.setSelected(enabled);
        
        row.getChildren().addAll(iconLabel, textLabel, spacer, toggle);
        return row;
    }
}