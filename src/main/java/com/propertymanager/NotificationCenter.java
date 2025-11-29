package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationCenter extends VBox {
    
    private String selectedTab = "all";
    private String filterType = "all";
    private TextField searchField;
    private VBox notificationsContainer;
    private List<Notification> notifications;
    private List<String> selectedNotifications;
    
    public NotificationCenter() {
        selectedNotifications = new ArrayList<>();
        initData();
        initPage();
    }
    
    private void initData() {
        notifications = new ArrayList<>();
        notifications.add(new Notification("1", "rent", "Payment Overdue", "Emma Davis has an overdue payment of 1,000,000 MAD for property Metro Heights - 405.", "2h ago", "2025-11-08", false));
        notifications.add(new Notification("2", "maintenance", "New Maintenance Request", "Water leak reported in Skyline Tower - Unit 305. Priority: High.", "3h ago", "2025-11-08", false));
        notifications.add(new Notification("3", "permit", "Construction Permit Approved", "Permit #CP-2024-089 for Riverside Plaza renovation has been approved.", "5h ago", "2025-11-08", false));
        notifications.add(new Notification("4", "tenant", "New Buyer Registered", "Jennifer Wilson has completed purchase for Garden View - 412.", "1d ago", "2025-11-07", true));
        notifications.add(new Notification("5", "system", "System Backup Completed", "Daily backup completed successfully at 2:00 AM.", "1d ago", "2025-11-07", true));
        notifications.add(new Notification("6", "alert", "Maintenance Due Soon", "Vehicle MT-2006 requires maintenance within the next 7 days.", "2d ago", "2025-11-06", true));
    }
    
    private void initPage() {
        setPadding(new Insets(30));
        setSpacing(25);
        setStyle("-fx-background-color: #f8f9fa;");
        
        HBox header = createHeader();
        HBox statsRow = createStatsRow();
        VBox mainContent = createMainContent();
        
        getChildren().addAll(header, statsRow, mainContent);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Notification Center");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Manage all your system notifications and alerts");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#6c757d"));
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        int unreadCount = (int) notifications.stream().filter(n -> !n.isRead).count();
        
        HBox rightBox = new HBox(15);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        
        if (unreadCount > 0) {
            Label unreadBadge = new Label(unreadCount + " unread");
            unreadBadge.setStyle("-fx-background-color: #F5C542; -fx-text-fill: black; -fx-background-radius: 12; -fx-padding: 4 12;");
            rightBox.getChildren().add(unreadBadge);
        }
        
        Button markAllBtn = new Button("✓ Mark All Read");
        markAllBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");
        markAllBtn.setOnAction(e -> markAllAsRead());
        rightBox.getChildren().add(markAllBtn);
        
        header.getChildren().addAll(titleBox, spacer, rightBox);
        return header;
    }
    
    private HBox createStatsRow() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        int total = notifications.size();
        int unread = (int) notifications.stream().filter(n -> !n.isRead).count();
        int read = total - unread;
        int filtered = getFilteredNotifications().size();
        
        statsRow.getChildren().addAll(
            createStatCard("Total Notifications", String.valueOf(total), "🔔", "#2C3E8C"),
            createStatCard("Unread", String.valueOf(unread), "⚠️", "#F5C542"),
            createStatCard("Read", String.valueOf(read), "✅", "#4CAF50"),
            createStatCard("Filtered", String.valueOf(filtered), "🔍", "#8B5CF6")
        );
        
        return statsRow;
    }
    
    private VBox createStatCard(String title, String value, String icon, String color) {
        VBox card = new VBox(10);
        card.setPrefWidth(280);
        card.setPrefHeight(120);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        
        VBox textBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setTextFill(Color.GRAY);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.setTextFill(Color.web("#2c3e50"));
        
        textBox.getChildren().addAll(titleLabel, valueLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12; -fx-font-size: 20px;");
        
        content.getChildren().addAll(textBox, spacer, iconLabel);
        card.getChildren().add(content);
        
        return card;
    }
    
    private VBox createMainContent() {
        VBox container = new VBox(20);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Tabs and filters
        VBox filtersSection = createFiltersSection();
        
        // Notifications list
        notificationsContainer = new VBox(15);
        ScrollPane scrollPane = new ScrollPane(notificationsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        updateNotificationsList();
        
        container.getChildren().addAll(filtersSection, scrollPane);
        return container;
    }
    
    private VBox createFiltersSection() {
        VBox section = new VBox(15);
        
        // Tabs
        HBox tabsBox = new HBox(10);
        Button allTab = createTabButton("All", "all");
        Button unreadTab = createTabButton("Unread", "unread");
        Button readTab = createTabButton("Read", "read");
        tabsBox.getChildren().addAll(allTab, unreadTab, readTab);
        
        // Search and filter
        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        searchField = new TextField();
        searchField.setPromptText("🔍 Search notifications...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, old, text) -> updateNotificationsList());
        
        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All Types", "Alert", "Rent", "Permit", "Maintenance", "Tenant", "System");
        typeFilter.setValue("All Types");
        typeFilter.setOnAction(e -> {
            filterType = typeFilter.getValue().equals("All Types") ? "all" : typeFilter.getValue().toLowerCase();
            updateNotificationsList();
        });
        
        searchBox.getChildren().addAll(searchField, typeFilter);
        
        section.getChildren().addAll(tabsBox, searchBox);
        return section;
    }
    
    private Button createTabButton(String text, String tabValue) {
        Button button = new Button(text);
        button.setPadding(new Insets(10, 20, 10, 20));
        
        if (selectedTab.equals(tabValue)) {
            button.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand;");
        } else {
            button.setStyle("-fx-background-color: #f1f3f4; -fx-text-fill: #666; -fx-background-radius: 8; -fx-cursor: hand;");
        }
        
        button.setOnAction(e -> {
            selectedTab = tabValue;
            updateTabStyles();
            updateNotificationsList();
        });
        
        return button;
    }
    
    private void updateTabStyles() {
        VBox container = (VBox) getChildren().get(2);
        VBox filtersSection = (VBox) container.getChildren().get(0);
        HBox newTabsBox = new HBox(10);
        Button allTab = createTabButton("All", "all");
        Button unreadTab = createTabButton("Unread", "unread");
        Button readTab = createTabButton("Read", "read");
        newTabsBox.getChildren().addAll(allTab, unreadTab, readTab);
        filtersSection.getChildren().set(0, newTabsBox);
    }
    
    private List<Notification> getFilteredNotifications() {
        return notifications.stream()
            .filter(n -> {
                boolean matchesTab = selectedTab.equals("all") || 
                    (selectedTab.equals("unread") && !n.isRead) ||
                    (selectedTab.equals("read") && n.isRead);
                
                boolean matchesSearch = searchField == null || searchField.getText().isEmpty() ||
                    n.title.toLowerCase().contains(searchField.getText().toLowerCase()) ||
                    n.message.toLowerCase().contains(searchField.getText().toLowerCase());
                
                boolean matchesType = filterType.equals("all") || n.type.equals(filterType);
                
                return matchesTab && matchesSearch && matchesType;
            })
            .collect(Collectors.toList());
    }
    
    private void updateNotificationsList() {
        notificationsContainer.getChildren().clear();
        
        List<Notification> filtered = getFilteredNotifications();
        
        if (filtered.isEmpty()) {
            VBox emptyState = new VBox(15);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPrefHeight(300);
            
            Label emptyIcon = new Label("🔔");
            emptyIcon.setFont(Font.font(48));
            
            Label emptyText = new Label("No notifications found");
            emptyText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            
            Label emptySubtext = new Label("Try adjusting your filters or search terms");
            emptySubtext.setFont(Font.font("Arial", 14));
            emptySubtext.setTextFill(Color.GRAY);
            
            emptyState.getChildren().addAll(emptyIcon, emptyText, emptySubtext);
            notificationsContainer.getChildren().add(emptyState);
        } else {
            for (Notification notification : filtered) {
                VBox notificationCard = createNotificationCard(notification);
                notificationsContainer.getChildren().add(notificationCard);
            }
        }
    }
    
    private VBox createNotificationCard(Notification notification) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        
        if (!notification.isRead) {
            card.setStyle("-fx-background-color: #f0f8ff; -fx-background-radius: 12; -fx-border-color: #2C3E8C; -fx-border-width: 1; -fx-border-radius: 12;");
        } else {
            card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 12; -fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 12;");
        }
        
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label typeIcon = new Label(getTypeIcon(notification.type));
        typeIcon.setStyle("-fx-background-color: " + getTypeColor(notification.type) + "; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8; -fx-font-size: 16px;");
        
        VBox contentBox = new VBox(8);
        HBox.setHgrow(contentBox, Priority.ALWAYS);
        
        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(notification.title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        Label typeBadge = new Label(notification.type.toUpperCase());
        typeBadge.setStyle("-fx-background-color: " + getTypeColor(notification.type) + "; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 2 8; -fx-font-size: 10px;");
        
        if (!notification.isRead) {
            Label unreadDot = new Label("●");
            unreadDot.setTextFill(Color.web("#F5C542"));
            unreadDot.setFont(Font.font(12));
            titleRow.getChildren().addAll(titleLabel, typeBadge, unreadDot);
        } else {
            titleRow.getChildren().addAll(titleLabel, typeBadge);
        }
        
        Label messageLabel = new Label(notification.message);
        messageLabel.setFont(Font.font("Arial", 13));
        messageLabel.setTextFill(Color.web("#495057"));
        messageLabel.setWrapText(true);
        
        HBox timeBox = new HBox(10);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        Label timeLabel = new Label(notification.timestamp + " • " + notification.date);
        timeLabel.setFont(Font.font("Arial", 11));
        timeLabel.setTextFill(Color.GRAY);
        timeBox.getChildren().add(timeLabel);
        
        contentBox.getChildren().addAll(titleRow, messageLabel, timeBox);
        
        VBox actionsBox = new VBox(5);
        
        Button readBtn = new Button(notification.isRead ? "📧" : "✓");
        readBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8; -fx-cursor: hand;");
        readBtn.setOnAction(e -> toggleReadStatus(notification));
        
        Button deleteBtn = new Button("🗑️");
        deleteBtn.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #d32f2f; -fx-background-radius: 6; -fx-padding: 8; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> deleteNotification(notification));
        
        actionsBox.getChildren().addAll(readBtn, deleteBtn);
        
        header.getChildren().addAll(typeIcon, contentBox, actionsBox);
        card.getChildren().add(header);
        
        return card;
    }
    
    private String getTypeIcon(String type) {
        switch (type) {
            case "alert": return "⚠️";
            case "rent": return "💰";
            case "permit": return "📋";
            case "maintenance": return "🔧";
            case "tenant": return "👥";
            case "system": return "⚙️";
            default: return "🔔";
        }
    }
    
    private String getTypeColor(String type) {
        switch (type) {
            case "alert": return "#dc3545";
            case "rent": return "#28a745";
            case "permit": return "#007bff";
            case "maintenance": return "#fd7e14";
            case "tenant": return "#6f42c1";
            case "system": return "#20c997";
            default: return "#6c757d";
        }
    }
    
    private void toggleReadStatus(Notification notification) {
        notification.isRead = !notification.isRead;
        updateNotificationsList();
        updateStatsRow();
    }
    
    private void deleteNotification(Notification notification) {
        notifications.remove(notification);
        updateNotificationsList();
        updateStatsRow();
    }
    
    private void markAllAsRead() {
        notifications.forEach(n -> n.isRead = true);
        updateNotificationsList();
        updateStatsRow();
    }
    
    private void updateStatsRow() {
        HBox newStatsRow = createStatsRow();
        getChildren().set(1, newStatsRow);
    }
    
    // Data class
    private static class Notification {
        String id, type, title, message, timestamp, date;
        boolean isRead;
        
        Notification(String id, String type, String title, String message, String timestamp, String date, boolean isRead) {
            this.id = id;
            this.type = type;
            this.title = title;
            this.message = message;
            this.timestamp = timestamp;
            this.date = date;
            this.isRead = isRead;
        }
    }
}