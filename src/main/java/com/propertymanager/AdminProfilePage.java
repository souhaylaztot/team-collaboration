package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminProfilePage extends VBox {
    
    public AdminProfilePage() {
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
        
        // Right column - Settings & Stats
        VBox rightColumn = createRightColumn();
        rightColumn.setPrefWidth(500);
        
        mainContent.getChildren().addAll(leftColumn, rightColumn);
        
        getChildren().addAll(header, mainContent);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("👨‍💼 Administrator Profile");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#1a202c"));
        
        Label subtitle = new Label("Manage your account settings and system preferences");
        subtitle.setFont(Font.font("Segoe UI", 16));
        subtitle.setTextFill(Color.web("#4a5568"));
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    private VBox createLeftColumn() {
        VBox column = new VBox(25);
        
        // Profile card
        VBox profileCard = createProfileCard();
        
        // Quick actions
        VBox actionsCard = createQuickActionsCard();
        
        column.getChildren().addAll(profileCard, actionsCard);
        return column;
    }
    
    private VBox createProfileCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        // Profile picture and basic info
        VBox profileInfo = new VBox(20);
        profileInfo.setAlignment(Pos.CENTER);
        
        Button profilePic = new Button("👨‍💼");
        profilePic.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 50; -fx-font-size: 40px; -fx-padding: 20;");
        profilePic.setPrefSize(100, 100);
        
        Label name = new Label("Ahmed Hassan");
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        name.setTextFill(Color.web("#1a202c"));
        
        Label role = new Label("System Administrator");
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
            createInfoRow("📧", "Email", "ahmed.hassan@propertymanager.com"),
            createInfoRow("📱", "Phone", "+212 6 12 34 56 78"),
            createInfoRow("🏢", "Department", "IT Administration"),
            createInfoRow("📅", "Joined", "January 15, 2023")
        );
        
        card.getChildren().addAll(profileInfo, new Separator(), contactInfo);
        return card;
    }
    
    private VBox createQuickActionsCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        Label title = new Label("⚡ Quick Actions");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1a202c"));
        
        VBox actions = new VBox(10);
        actions.getChildren().addAll(
            createActionButton("👥", "Manage Users", "#3b82f6"),
            createActionButton("🔐", "Security Settings", "#ef4444"),
            createActionButton("📊", "System Reports", "#10b981"),
            createActionButton("⚙️", "System Settings", "#6b7280")
        );
        
        card.getChildren().addAll(title, actions);
        return card;
    }
    
    private VBox createRightColumn() {
        VBox column = new VBox(25);
        
        // Statistics card
        VBox statsCard = createStatsCard();
        
        // Settings card
        VBox settingsCard = createSettingsCard();
        
        column.getChildren().addAll(statsCard, settingsCard);
        return column;
    }
    
    private VBox createStatsCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        Label title = new Label("📈 Admin Statistics");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1a202c"));
        
        GridPane stats = new GridPane();
        stats.setHgap(20);
        stats.setVgap(20);
        
        stats.add(createStatBox("156", "Total Properties", "#3b82f6"), 0, 0);
        stats.add(createStatBox("24", "Active Users", "#10b981"), 1, 0);
        stats.add(createStatBox("89", "Pending Requests", "#f59e0b"), 0, 1);
        stats.add(createStatBox("12", "System Alerts", "#ef4444"), 1, 1);
        
        card.getChildren().addAll(title, stats);
        return card;
    }
    
    private VBox createSettingsCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        Label title = new Label("⚙️ Account Settings");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1a202c"));
        
        VBox settings = new VBox(20);
        settings.getChildren().addAll(
            createSettingRow("🔔", "Email Notifications", true),
            createSettingRow("🔐", "Two-Factor Authentication", true),
            createSettingRow("🌙", "Dark Mode", false),
            createSettingRow("📱", "SMS Alerts", true)
        );
        
        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 15 30; -fx-font-size: 16px; -fx-cursor: hand;");
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
    
    private Button createActionButton(String icon, String text, String color) {
        Button btn = new Button();
        btn.setPrefWidth(300);
        btn.setPrefHeight(50);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 15;");
        
        HBox content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(18));
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        content.getChildren().addAll(iconLabel, textLabel);
        btn.setGraphic(content);
        
        return btn;
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