package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class TopBar extends HBox {
    private String userType = "Admin User";
    private MainApp mainApp;
    
    public TopBar() {
        initTopBar();
    }
    
    public TopBar(String userType) {
        this.userType = userType;
        initTopBar();
    }
    
    public TopBar(String userType, MainApp mainApp) {
        this.userType = userType;
        this.mainApp = mainApp;
        initTopBar();
    }
    
    private void initTopBar() {
        setPrefHeight(50);
        setStyle("-fx-background-color: white; -fx-border-color: #e9ecef; -fx-border-width: 0 0 1 0;");
        setPadding(new Insets(10, 30, 10, 30));
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(20);
        
        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search buildings, buyers, permits...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e9ecef; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Notifications
        Button notificationBtn = new Button("🔔");
        notificationBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-border-color: transparent; -fx-cursor: hand;");
        
        HBox notificationBox = new HBox();
        notificationBox.setAlignment(Pos.CENTER);
        notificationBox.getChildren().add(notificationBtn);
        
        // Add click handler for notifications
        notificationBtn.setOnAction(e -> showNotificationsPopup(notificationBtn));
        

        
        // Theme toggle
        Button themeBtn = new Button("🌙");
        themeBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-border-color: transparent;");
        
        // User profile
        HBox userProfile = new HBox(10);
        userProfile.setAlignment(Pos.CENTER);
        
        Button userBtn = new Button("👤");
        userBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 8;");
        
        Label userName = new Label(userType);
        userName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        String roleText = userType.equals("Admin User") ? "Administrator" : "User";
        Label userRole = new Label(roleText);
        userRole.setFont(Font.font("Arial", 12));
        userRole.setTextFill(Color.GRAY);
        
        userProfile.getChildren().addAll(userBtn, userName);
        
        // Add click handler for user profile
        userProfile.setOnMouseClicked(e -> showUserProfilePopup(userProfile));
        userProfile.setStyle("-fx-cursor: hand;");
        
        getChildren().addAll(searchField, spacer, notificationBox, themeBtn, userProfile);
    }
    

    
    private void showNotificationsPopup(Button notificationBtn) {
        Stage popup = new Stage();
        popup.initStyle(StageStyle.TRANSPARENT); // Remove title bar
        popup.setWidth(600);
        popup.setHeight(700);
        popup.setResizable(false);
        
        VBox content = createNotificationContent();
        
        Scene scene = new Scene(content, 600, 700);
        scene.setFill(null); // Make scene background transparent
        popup.setScene(scene);
        
        // Position near the notification button
        popup.setX(notificationBtn.getScene().getWindow().getX() + notificationBtn.getScene().getWindow().getWidth() - 620);
        popup.setY(notificationBtn.getScene().getWindow().getY() + 100);
        
        // Close when clicking outside
        popup.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                popup.close();
            }
        });
        
        popup.show();
    }
    
    private void showUserProfilePopup(HBox userProfile) {
        Stage popup = new Stage();
        popup.initStyle(StageStyle.TRANSPARENT);
        popup.setWidth(300);
        popup.setHeight(250);
        popup.setResizable(false);
        
        VBox content = createUserProfileContent();
        
        Scene scene = new Scene(content, 300, 200);
        scene.setFill(null);
        popup.setScene(scene);
        
        // Position near the user profile
        popup.setX(userProfile.getScene().getWindow().getX() + userProfile.getScene().getWindow().getWidth() - 320);
        popup.setY(userProfile.getScene().getWindow().getY() + 100);
        
        // Close when clicking outside
        popup.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                popup.close();
            }
        });
        
        popup.show();
    }
    
    private VBox createUserProfileContent() {
        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        
        // Header with user info
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0;");
        
        Button userIcon = new Button("👤");
        userIcon.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 25; -fx-font-size: 20px; -fx-padding: 10;");
        userIcon.setPrefSize(50, 50);
        
        Label userName = new Label(userType);
        userName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        userName.setTextFill(Color.web("#111827"));
        
        String roleText = userType.equals("Admin User") ? "Administrator" : "User";
        Label userRole = new Label(roleText);
        userRole.setFont(Font.font("Segoe UI", 12));
        userRole.setTextFill(Color.web("#6b7280"));
        
        header.getChildren().addAll(userIcon, userName, userRole);
        
        // Menu options
        VBox menu = new VBox(0);
        
        Button profileBtn = createMenuButton("👤", "Profile Settings");
        Button logoutBtn = createMenuButton("🚪", "Log Out");
        
        profileBtn.setOnAction(e -> {
            openProfilePage();
            ((Stage) profileBtn.getScene().getWindow()).close();
        });
        
        logoutBtn.setOnAction(e -> {
            handleLogout();
            ((Stage) logoutBtn.getScene().getWindow()).close();
        });
        
        menu.getChildren().addAll(profileBtn, logoutBtn);
        
        content.getChildren().addAll(header, menu);
        return content;
    }
    
    private Button createMenuButton(String icon, String text) {
        Button btn = new Button();
        btn.setPrefWidth(300);
        btn.setPrefHeight(50);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand; -fx-padding: 15 20;");
        
        HBox content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(18));
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Segoe UI", 14));
        textLabel.setTextFill(Color.web("#374151"));
        
        content.getChildren().addAll(iconLabel, textLabel);
        btn.setGraphic(content);
        
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #f3f4f6; -fx-border-color: transparent; -fx-cursor: hand; -fx-padding: 15 20;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand; -fx-padding: 15 20;"));
        
        return btn;
    }
    
    private void handleLogout() {
        try {
            // Close current window
            Stage currentStage = (Stage) this.getScene().getWindow();
            currentStage.close();
            
            // Open login page
            LoginPage loginPage = new LoginPage();
            Stage loginStage = new Stage();
            loginPage.start(loginStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openProfilePage() {
        try {
            if (mainApp != null) {
                if (userType.equals("Admin User")) {
                    mainApp.showPage("AdminProfile");
                } else {
                    mainApp.showPage("UserProfile");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private VBox createNotificationContent() {
        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        
        // Header
        HBox header = new HBox(20);
        header.setPadding(new Insets(30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-border-color: #e5e7eb; -fx-border-width: 0 0 2 0;");
        
        Label title = new Label("🔔 Notifications");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#111827"));
        
        Label badge = new Label("3 new");
        badge.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 16; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button markAllBtn = new Button("✓ Mark all as read");
        markAllBtn.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #374151; -fx-font-size: 16px; -fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 10 20;");
        markAllBtn.setOnAction(e -> System.out.println("Mark all as read clicked"));
        
        header.getChildren().addAll(title, badge, spacer, markAllBtn);
        
        // Notifications list
        VBox notificationsList = new VBox(0);
        
        notificationsList.getChildren().addAll(
            createLargeNotification("$", "Payment Overdue", "Emma Davis has an overdue payment of 1,000,000 MAD for property Metro Heights - 405.", "2h ago", false, "#10b981"),
            createLargeNotification("🔧", "New Maintenance Request", "Water leak reported in Skyline Tower - Unit 305. Priority: High", "3h ago", false, "#f59e0b"),
            createLargeNotification("📋", "Construction Permit Approved", "Permit #CP-2024-089 for Riverside Plaza renovation has been approved.", "5h ago", false, "#3b82f6"),
            createLargeNotification("👥", "New Buyer Registered", "Jennifer Wilson has completed purchase for Garden View - 412.", "1d ago", true, "#8b5cf6")
        );
        
        ScrollPane scrollPane = new ScrollPane(notificationsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(580);
        scrollPane.setStyle("-fx-background-color: white;");
        
        content.getChildren().addAll(header, scrollPane);
        return content;
    }
    
    private VBox createLargeNotification(String icon, String title, String message, String time, boolean isRead, String iconColor) {
        VBox notification = new VBox();
        notification.setPadding(new Insets(25, 30, 25, 30));
        
        if (!isRead) {
            notification.setStyle("-fx-background-color: #f0fdf4; -fx-cursor: hand;");
        } else {
            notification.setStyle("-fx-background-color: white; -fx-cursor: hand;");
        }
        
        notification.setOnMouseEntered(e -> notification.setStyle("-fx-background-color: #f9fafb; -fx-cursor: hand;"));
        notification.setOnMouseExited(e -> {
            if (!isRead) {
                notification.setStyle("-fx-background-color: #f0fdf4; -fx-cursor: hand;");
            } else {
                notification.setStyle("-fx-background-color: white; -fx-cursor: hand;");
            }
        });
        
        HBox content = new HBox(20);
        content.setAlignment(Pos.TOP_LEFT);
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: " + iconColor + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 20px; -fx-font-weight: bold;");
        iconLabel.setPrefSize(60, 60);
        iconLabel.setAlignment(Pos.CENTER);
        
        // Content
        VBox textBox = new VBox(8);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        
        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#111827"));
        
        if (!isRead) {
            Label dot = new Label("•");
            dot.setTextFill(Color.web("#f59e0b"));
            dot.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            titleRow.getChildren().addAll(titleLabel, dot);
        } else {
            titleRow.getChildren().add(titleLabel);
        }
        
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Arial", 15));
        messageLabel.setTextFill(Color.web("#6b7280"));
        messageLabel.setWrapText(true);
        messageLabel.setPrefWidth(480);
        
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Arial", 14));
        timeLabel.setTextFill(Color.web("#9ca3af"));
        
        textBox.getChildren().addAll(titleRow, messageLabel, timeLabel);
        content.getChildren().addAll(iconLabel, textBox);
        notification.getChildren().add(content);
        
        // Add separator
        Region separator = new Region();
        separator.setPrefHeight(2);
        separator.setStyle("-fx-background-color: #f3f4f6;");
        notification.getChildren().add(separator);
        
        notification.setOnMouseClicked(e -> {
            System.out.println("Clicked: " + title);
        });
        
        return notification;
    }
    

    
    private List<NotificationItem> createSampleNotifications() {
        List<NotificationItem> notifications = new ArrayList<>();
        notifications.add(new NotificationItem("💰", "Payment Overdue", "Emma Davis - Metro Heights 405", "2h ago", false, "rent"));
        notifications.add(new NotificationItem("🔧", "Maintenance Request", "Water leak - Skyline Tower 305", "3h ago", false, "maintenance"));
        notifications.add(new NotificationItem("📋", "Permit Approved", "Construction permit #CP-2024-089", "5h ago", false, "permit"));
        notifications.add(new NotificationItem("👥", "New Buyer", "Jennifer Wilson - Garden View 412", "1d ago", true, "tenant"));
        notifications.add(new NotificationItem("⚙️", "System Backup", "Daily backup completed successfully", "1d ago", true, "system"));
        return notifications;
    }
    
    private VBox createNotificationCard(NotificationItem notification) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        
        if (!notification.isRead) {
            card.setStyle("-fx-background-color: #f0f8ff; -fx-background-radius: 8; -fx-border-color: #2C3E8C; -fx-border-width: 1; -fx-border-radius: 8;");
        } else {
            card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 8;");
        }
        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label icon = new Label(notification.icon);
        icon.setFont(Font.font(16));
        
        VBox textBox = new VBox(2);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        
        HBox titleRow = new HBox(8);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label(notification.title);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        title.setTextFill(Color.web("#2c3e50"));
        
        if (!notification.isRead) {
            Label unreadDot = new Label("●");
            unreadDot.setTextFill(Color.web("#dc3545"));
            unreadDot.setFont(Font.font(8));
            titleRow.getChildren().addAll(title, unreadDot);
        } else {
            titleRow.getChildren().add(title);
        }
        
        Label message = new Label(notification.message);
        message.setFont(Font.font("Arial", 12));
        message.setTextFill(Color.web("#6c757d"));
        
        Label time = new Label(notification.time);
        time.setFont(Font.font("Arial", 10));
        time.setTextFill(Color.GRAY);
        
        textBox.getChildren().addAll(titleRow, message, time);
        
        Button actionBtn = new Button("⋯");
        actionBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #6c757d; -fx-cursor: hand;");
        
        header.getChildren().addAll(icon, textBox, actionBtn);
        card.getChildren().add(header);
        
        return card;
    }
    
    // Simple notification data class
    private static class NotificationItem {
        String icon, title, message, time, type;
        boolean isRead;
        
        NotificationItem(String icon, String title, String message, String time, boolean isRead, String type) {
            this.icon = icon;
            this.title = title;
            this.message = message;
            this.time = time;
            this.isRead = isRead;
            this.type = type;
        }
    }
}