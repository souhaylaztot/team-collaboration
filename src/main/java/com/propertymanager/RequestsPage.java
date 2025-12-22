package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestsPage extends VBox {
    
    private String selectedTab = "all";
    private VBox requestsContainer;
    private VBox messagesContainer;
    private List<Request> requests;
    private Request selectedRequest;
    private TextField replyField;
    
    public RequestsPage() {
        initRequests();
        initPage();
    }
    
    private void initRequests() {
        requests = new ArrayList<>();
        loadRequestsFromDatabase();
    }
    
    private void loadRequestsFromDatabase() {
        // Add sample requests for demonstration
        List<Message> messages1 = new ArrayList<>();
        messages1.add(new Message(1, "Emma Davis", "I have been experiencing a water leak in my bathroom for the past 3 days. The water is coming from under the sink and has started to damage the floor. Please send someone to fix this as soon as possible.", "2025-12-22 09:30", false));
        messages1.add(new Message(2, "Admin", "Thank you for reporting this issue. We have scheduled a maintenance technician to visit your apartment tomorrow at 2 PM. Please ensure someone is available to provide access.", "2025-12-22 10:15", true));
        messages1.add(new Message(3, "Emma Davis", "Perfect, I will be available at that time. Thank you for the quick response!", "2025-12-22 10:20", false));
        
        List<Message> messages2 = new ArrayList<>();
        messages2.add(new Message(1, "John Smith", "I need a copy of my lease agreement for insurance purposes. Could you please provide me with a digital copy?", "2025-12-22 08:45", false));
        
        List<Message> messages3 = new ArrayList<>();
        messages3.add(new Message(1, "Sarah Wilson", "The elevator in our building has been making strange noises and sometimes stops between floors. This is concerning for safety reasons.", "2025-12-21 16:20", false));
        messages3.add(new Message(2, "Admin", "We have contacted the elevator maintenance company and they will inspect it first thing Monday morning. We will post notices about temporary service interruption.", "2025-12-21 17:00", true));
        
        List<Message> messages4 = new ArrayList<>();
        messages4.add(new Message(1, "Mike Johnson", "My neighbors in apartment 306 have been playing loud music late at night. This has been going on for over a week and is affecting my sleep.", "2025-12-20 22:30", false));
        
        requests.add(new Request("REQ-001", "Emma Davis", "Skyline Tower", "Apt 305", "Maintenance", "Water leak in bathroom", "responded", "2025-12-22 09:30", "High", messages1));
        requests.add(new Request("REQ-002", "John Smith", "Garden View", "Apt 412", "Document Request", "Need lease agreement copy", "open", "2025-12-22 08:45", "Medium", messages2));
        requests.add(new Request("REQ-003", "Sarah Wilson", "Metro Heights", "Apt 205", "Maintenance", "Elevator safety concern", "in-progress", "2025-12-21 16:20", "High", messages3));
        requests.add(new Request("REQ-004", "Mike Johnson", "Riverside Plaza", "Apt 108", "Complaint", "Noise complaint - loud music", "open", "2025-12-20 22:30", "Medium", messages4));
        
        // Try to load from database as well
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT cr.request_number, COALESCE(b.name, 'Unknown Buyer') as buyer_name, " +
                          "COALESCE(bd.name, 'General') as building_name, " +
                          "CASE WHEN cr.apartment_id IS NOT NULL THEN CONCAT('Apt ', a.apartment_number) ELSE 'General' END as apartment, " +
                          "cr.request_type, cr.subject, cr.priority, cr.status, cr.created_at " +
                          "FROM customer_requests cr " +
                          "LEFT JOIN buyers b ON cr.buyer_id = b.id " +
                          "LEFT JOIN buildings bd ON cr.building_id = bd.id " +
                          "LEFT JOIN apartments a ON cr.apartment_id = a.id " +
                          "ORDER BY cr.created_at DESC";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    List<Message> messages = loadMessagesForRequest(conn, rs.getInt("request_number"));
                    
                    Request request = new Request(
                        rs.getString("request_number"),
                        rs.getString("buyer_name"),
                        rs.getString("building_name"),
                        rs.getString("apartment"),
                        rs.getString("request_type"),
                        rs.getString("subject"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at").toString(),
                        rs.getString("priority"),
                        messages
                    );
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database not available, using sample data only");
        }
    }
    
    private List<Message> loadMessagesForRequest(Connection conn, int requestId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT sender_name, message_text, is_staff_message, created_at " +
                      "FROM request_messages WHERE request_id = ? ORDER BY created_at";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            
            int messageId = 1;
            while (rs.next()) {
                Message message = new Message(
                    messageId++,
                    rs.getString("sender_name"),
                    rs.getString("message_text"),
                    rs.getTimestamp("created_at").toString(),
                    rs.getBoolean("is_staff_message")
                );
                messages.add(message);
            }
        }
        
        return messages;
    }
    
    private void initPage() {
        setPadding(new Insets(30));
        setSpacing(25);
        setStyle("-fx-background-color: #f8f9fa;");
        
        HBox header = createHeader();
        HBox statsRow = createStatsRow();
        HBox mainContent = createMainContent();
        
        getChildren().addAll(header, statsRow, mainContent);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Request Center");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Manage buyer and staff requests");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#6c757d"));
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addButton = new Button("➕ New Request");
        addButton.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20; -fx-font-weight: bold; -fx-cursor: hand;");
        
        // تحسين التفاعل البصري
        addButton.setOnMouseEntered(e -> {
            addButton.setStyle("-fx-background-color: linear-gradient(to right, #1a2a5e, #3bb3a8); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20; -fx-font-weight: bold; -fx-cursor: hand;");
        });
        
        addButton.setOnMouseExited(e -> {
            addButton.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20; -fx-font-weight: bold; -fx-cursor: hand;");
        });
        
        addButton.setOnAction(e -> {
            System.out.println("Opening new request dialog");
            showAddRequestDialog();
        });
        
        header.getChildren().addAll(titleBox, spacer, addButton);
        return header;
    }
    
    private HBox createStatsRow() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        int total = requests.size();
        int open = (int) requests.stream().filter(r -> "open".equals(r.status)).count();
        int inProgress = (int) requests.stream().filter(r -> "in-progress".equals(r.status)).count();
        int responded = (int) requests.stream().filter(r -> "responded".equals(r.status)).count();
        
        statsRow.getChildren().addAll(
            createStatCard("Total Requests", String.valueOf(total), "💬", "#2C3E8C"),
            createStatCard("Open", String.valueOf(open), "⏰", "#2196F3"),
            createStatCard("In Progress", String.valueOf(inProgress), "💬", "#F5C542"),
            createStatCard("Responded", String.valueOf(responded), "✅", "#4CAF50")
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
    
    private HBox createMainContent() {
        HBox mainContent = new HBox(20);
        
        // Requests list
        VBox requestsList = createRequestsList();
        requestsList.setPrefWidth(400);
        
        // Messages view
        VBox messagesView = createMessagesView();
        HBox.setHgrow(messagesView, Priority.ALWAYS);
        
        mainContent.getChildren().addAll(requestsList, messagesView);
        return mainContent;
    }
    
    private VBox createRequestsList() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Tabs
        HBox tabsBox = new HBox(10);
        Button allTab = createTabButton("All", "all");
        Button openTab = createTabButton("Open", "open");
        tabsBox.getChildren().addAll(allTab, openTab);
        
        // Requests container
        requestsContainer = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(requestsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        updateRequestsList();
        
        container.getChildren().addAll(tabsBox, scrollPane);
        return container;
    }
    
    private Button createTabButton(String text, String tabValue) {
        Button button = new Button(text);
        button.setPadding(new Insets(10, 20, 10, 20));
        
        if (selectedTab.equals(tabValue)) {
            button.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else {
            button.setStyle("-fx-background-color: #f1f3f4; -fx-text-fill: #666; -fx-background-radius: 6;");
        }
        
        button.setOnAction(e -> {
            System.out.println("Switching to tab: " + text);
            selectedTab = tabValue;
            updateTabStyles();
            updateRequestsList();
        });
        
        return button;
    }
    
    private void updateTabStyles() {
        HBox mainContent = (HBox) getChildren().get(2);
        VBox requestsList = (VBox) mainContent.getChildren().get(0);
        HBox newTabsBox = new HBox(10);
        Button allTab = createTabButton("All", "all");
        Button openTab = createTabButton("Open", "open");
        newTabsBox.getChildren().addAll(allTab, openTab);
        requestsList.getChildren().set(0, newTabsBox);
    }
    
    private void updateRequestsList() {
        requestsContainer.getChildren().clear();
        
        List<Request> filteredRequests = requests.stream()
            .filter(request -> selectedTab.equals("all") || request.status.equals(selectedTab))
            .toList();
        
        for (Request request : filteredRequests) {
            VBox requestCard = createRequestCard(request);
            requestsContainer.getChildren().add(requestCard);
        }
    }
    
    private VBox createRequestCard(Request request) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        
        boolean isSelected = selectedRequest != null && selectedRequest.id.equals(request.id);
        if (isSelected) {
            card.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 8; -fx-border-color: #2C3E8C; -fx-border-width: 2; -fx-border-radius: 8; -fx-cursor: hand;");
        } else {
            card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 8; -fx-cursor: hand;");
        }
        
        card.setOnMouseClicked(e -> {
            System.out.println("Selected request: " + request.subject);
            selectedRequest = request;
            updateRequestsList();
            updateMessagesView();
        });
        
        card.setOnMouseEntered(e -> {
            if (selectedRequest == null || !selectedRequest.id.equals(request.id)) {
                card.setStyle("-fx-background-color: #f0f8ff; -fx-background-radius: 8; -fx-border-color: #2C3E8C; -fx-border-width: 1; -fx-border-radius: 8; -fx-cursor: hand;");
            }
        });
        
        card.setOnMouseExited(e -> {
            if (selectedRequest == null || !selectedRequest.id.equals(request.id)) {
                card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #e9ecef; -fx-border-radius: 8; -fx-cursor: hand;");
            }
        });
        
        // Subject
        Label subjectLabel = new Label(request.subject);
        subjectLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        subjectLabel.setTextFill(Color.web("#2c3e50"));
        subjectLabel.setWrapText(true);
        
        // Status and type badges
        HBox badgesBox = new HBox(8);
        Label statusBadge = createStatusBadge(request.status);
        Label typeBadge = new Label(request.type);
        typeBadge.setPadding(new Insets(2, 6, 2, 6));
        typeBadge.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 10; -fx-font-size: 10px;");
        badgesBox.getChildren().addAll(statusBadge, typeBadge);
        
        // Details
        VBox detailsBox = new VBox(3);
        Label fromLabel = new Label("👤 " + request.from);
        fromLabel.setFont(Font.font("Arial", 11));
        fromLabel.setTextFill(Color.GRAY);
        
        Label locationLabel = new Label("🏢 " + request.building + " - " + request.apartment);
        locationLabel.setFont(Font.font("Arial", 11));
        locationLabel.setTextFill(Color.GRAY);
        
        Label dateLabel = new Label(request.date);
        dateLabel.setFont(Font.font("Arial", 10));
        dateLabel.setTextFill(Color.web("#999"));
        
        detailsBox.getChildren().addAll(fromLabel, locationLabel, dateLabel);
        
        card.getChildren().addAll(subjectLabel, badgesBox, detailsBox);
        return card;
    }
    
    private Label createStatusBadge(String status) {
        Label badge = new Label();
        badge.setPadding(new Insets(2, 6, 2, 6));
        badge.setStyle("-fx-background-radius: 10; -fx-font-size: 10px; -fx-font-weight: bold;");
        
        switch (status) {
            case "open":
                badge.setText("Open");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e3f2fd; -fx-text-fill: #1976d2;");
                break;
            case "responded":
                badge.setText("Responded");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32;");
                break;
            case "in-progress":
                badge.setText("In Progress");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #fff3e0; -fx-text-fill: #f57c00;");
                break;
            case "closed":
                badge.setText("Closed");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #f5f5f5; -fx-text-fill: #616161;");
                break;
        }
        
        return badge;
    }
    
    private VBox createMessagesView() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Header
        VBox headerBox = new VBox(10);
        Label titleLabel = new Label("Select a request to view details");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        headerBox.getChildren().add(titleLabel);
        
        // Messages container
        messagesContainer = new VBox(15);
        ScrollPane messagesScroll = new ScrollPane(messagesContainer);
        messagesScroll.setFitToWidth(true);
        messagesScroll.setPrefHeight(400);
        messagesScroll.setStyle("-fx-background-color: transparent;");
        
        // Reply section
        VBox replySection = new VBox(10);
        HBox replyBox = new HBox(10);
        replyBox.setAlignment(Pos.CENTER_LEFT);
        
        replyField = new TextField();
        replyField.setPromptText("Type your reply...");
        replyField.setPrefHeight(40);
        HBox.setHgrow(replyField, Priority.ALWAYS);
        
        Button sendBtn = new Button("📤 Send");
        sendBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 15; -fx-font-weight: bold; -fx-cursor: hand;");
        
        sendBtn.setOnMouseEntered(e -> {
            sendBtn.setStyle("-fx-background-color: linear-gradient(to right, #1a2a5e, #3bb3a8); -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 15; -fx-font-weight: bold; -fx-cursor: hand;");
        });
        
        sendBtn.setOnMouseExited(e -> {
            sendBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 15; -fx-font-weight: bold; -fx-cursor: hand;");
        });
        
        sendBtn.setOnAction(e -> {
            System.out.println("Sending reply");
            sendReply();
        });
        
        replyBox.getChildren().addAll(replyField, sendBtn);
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        Button progressBtn = new Button("⏰ Mark In Progress");
        progressBtn.setStyle("-fx-background-color: #f1f3f4; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        
        progressBtn.setOnMouseEntered(e -> {
            progressBtn.setStyle("-fx-background-color: #F5C542; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        });
        
        progressBtn.setOnMouseExited(e -> {
            progressBtn.setStyle("-fx-background-color: #f1f3f4; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        });
        
        progressBtn.setOnAction(e -> {
            System.out.println("Marking request as in progress");
            if (selectedRequest != null) {
                selectedRequest.status = "in-progress";
                updateRequestsList();
                updateMessagesView();
                // Add system message
                Message statusMessage = new Message(
                    selectedRequest.messages.size() + 1,
                    "System",
                    "Request status updated to: In Progress",
                    java.time.LocalDateTime.now().toString().substring(0, 16),
                    true
                );
                selectedRequest.messages.add(statusMessage);
                updateMessagesView();
            }
        });
        
        Button closeBtn = new Button("✅ Close Request");
        closeBtn.setStyle("-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        
        closeBtn.setOnMouseEntered(e -> {
            closeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        });
        
        closeBtn.setOnMouseExited(e -> {
            closeBtn.setStyle("-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        });
        
        closeBtn.setOnAction(e -> {
            System.out.println("Closing request");
            if (selectedRequest != null) {
                selectedRequest.status = "closed";
                updateRequestsList();
                updateMessagesView();
                // Add system message
                Message statusMessage = new Message(
                    selectedRequest.messages.size() + 1,
                    "System",
                    "Request has been closed and marked as resolved.",
                    java.time.LocalDateTime.now().toString().substring(0, 16),
                    true
                );
                selectedRequest.messages.add(statusMessage);
                updateMessagesView();
            }
        });
        
        actionButtons.getChildren().addAll(progressBtn, closeBtn);
        
        replySection.getChildren().addAll(replyBox, actionButtons);
        
        // Empty state
        VBox emptyState = new VBox(15);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPrefHeight(400);
        
        Label emptyIcon = new Label("💬");
        emptyIcon.setFont(Font.font(48));
        emptyIcon.setTextFill(Color.web("#ccc"));
        
        Label emptyText = new Label("Select a request from the list to view the conversation");
        emptyText.setFont(Font.font("Arial", 14));
        emptyText.setTextFill(Color.GRAY);
        
        emptyState.getChildren().addAll(emptyIcon, emptyText);
        messagesContainer.getChildren().add(emptyState);
        
        container.getChildren().addAll(headerBox, messagesScroll, replySection);
        return container;
    }
    
    private void updateMessagesView() {
        if (selectedRequest == null) return;
        
        // Update header
        VBox container = (VBox) ((HBox) getChildren().get(2)).getChildren().get(1);
        VBox headerBox = (VBox) container.getChildren().get(0);
        headerBox.getChildren().clear();
        
        Label titleLabel = new Label(selectedRequest.subject);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        Label locationLabel = new Label(selectedRequest.building + " - " + selectedRequest.apartment);
        locationLabel.setFont(Font.font("Arial", 12));
        locationLabel.setTextFill(Color.GRAY);
        
        HBox badgesBox = new HBox(10);
        Label statusBadge = createStatusBadge(selectedRequest.status);
        Label priorityBadge = new Label(selectedRequest.priority + " priority");
        priorityBadge.setPadding(new Insets(2, 6, 2, 6));
        priorityBadge.setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #f57c00; -fx-background-radius: 10; -fx-font-size: 10px;");
        badgesBox.getChildren().addAll(statusBadge, priorityBadge);
        
        headerBox.getChildren().addAll(titleLabel, locationLabel, badgesBox);
        
        // Update messages
        messagesContainer.getChildren().clear();
        
        for (Message message : selectedRequest.messages) {
            VBox messageBox = createMessageBox(message);
            messagesContainer.getChildren().add(messageBox);
        }
    }
    
    private VBox createMessageBox(Message message) {
        VBox messageBox = new VBox(8);
        messageBox.setPadding(new Insets(15));
        messageBox.setMaxWidth(600);
        
        if (message.isStaff) {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageBox.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4a5fb8); -fx-background-radius: 12; -fx-text-fill: white;");
        } else {
            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageBox.setStyle("-fx-background-color: #f1f3f4; -fx-background-radius: 12;");
        }
        
        HBox senderBox = new HBox(8);
        senderBox.setAlignment(Pos.CENTER_LEFT);
        
        Label senderIcon = new Label("👤");
        Label senderLabel = new Label(message.sender);
        senderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        senderLabel.setTextFill(message.isStaff ? Color.WHITE : Color.web("#2c3e50"));
        
        senderBox.getChildren().addAll(senderIcon, senderLabel);
        
        Label textLabel = new Label(message.text);
        textLabel.setFont(Font.font("Arial", 13));
        textLabel.setTextFill(message.isStaff ? Color.WHITE : Color.web("#2c3e50"));
        textLabel.setWrapText(true);
        
        Label timeLabel = new Label(message.time);
        timeLabel.setFont(Font.font("Arial", 10));
        timeLabel.setTextFill(message.isStaff ? Color.web("#ffffff80") : Color.GRAY);
        
        messageBox.getChildren().addAll(senderBox, textLabel, timeLabel);
        
        HBox alignmentBox = new HBox();
        if (message.isStaff) {
            alignmentBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            alignmentBox.setAlignment(Pos.CENTER_LEFT);
        }
        alignmentBox.getChildren().add(messageBox);
        
        VBox wrapper = new VBox();
        wrapper.getChildren().add(alignmentBox);
        return wrapper;
    }
    
    private void sendReply() {
        if (selectedRequest == null || replyField.getText().trim().isEmpty()) return;
        
        String replyText = replyField.getText().trim();
        Message newMessage = new Message(
            selectedRequest.messages.size() + 1,
            "Admin",
            replyText,
            "2025-11-05 " + java.time.LocalTime.now().toString().substring(0, 5),
            true
        );
        
        selectedRequest.messages.add(newMessage);
        replyField.clear();
        updateMessagesView();
    }
    
    private void showAddRequestDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Create New Request");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        // Form fields
        Label fromLabel = new Label("From (Name)");
        TextField fromField = new TextField();
        fromField.setPromptText("Tenant or staff name");
        
        Label typeLabel = new Label("Request Type");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Information", "Document Request", "Maintenance", "Complaint", "Other");
        
        Label buildingLabel = new Label("Building");
        ComboBox<String> buildingCombo = new ComboBox<>();
        buildingCombo.getItems().addAll("Skyline Tower", "Riverside Apartments", "Garden View Complex");
        
        Label apartmentLabel = new Label("Apartment");
        TextField apartmentField = new TextField();
        apartmentField.setPromptText("e.g., Apt 305");
        
        Label subjectLabel = new Label("Subject");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Brief description");
        
        Label messageLabel = new Label("Message");
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Detailed description of the request...");
        messageArea.setPrefRowCount(4);
        
        Label priorityLabel = new Label("Priority");
        ComboBox<String> priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll("High", "Medium", "Low");
        priorityCombo.setValue("Medium");
        
        form.add(fromLabel, 0, 0);
        form.add(fromField, 1, 0);
        form.add(typeLabel, 0, 1);
        form.add(typeCombo, 1, 1);
        form.add(buildingLabel, 0, 2);
        form.add(buildingCombo, 1, 2);
        form.add(apartmentLabel, 0, 3);
        form.add(apartmentField, 1, 3);
        form.add(subjectLabel, 0, 4);
        form.add(subjectField, 1, 4);
        form.add(messageLabel, 0, 5);
        form.add(messageArea, 1, 5);
        form.add(priorityLabel, 0, 6);
        form.add(priorityCombo, 1, 6);
        
        // Buttons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 10 20; -fx-cursor: hand;");
        
        cancelBtn.setOnMouseEntered(e -> {
            cancelBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-cursor: hand;");
        });
        
        cancelBtn.setOnMouseExited(e -> {
            cancelBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 10 20; -fx-cursor: hand;");
        });
        
        cancelBtn.setOnAction(e -> {
            System.out.println("Canceling new request dialog");
            dialog.close();
        });
        
        Button submitBtn = new Button("Submit Request");
        submitBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        
        submitBtn.setOnMouseEntered(e -> {
            submitBtn.setStyle("-fx-background-color: #1a2a5e; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        });
        
        submitBtn.setOnMouseExited(e -> {
            submitBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        });
        
        submitBtn.setOnAction(e -> {
            System.out.println("Submitting new request");
            // Validate required fields
            if (fromField.getText().trim().isEmpty() || 
                typeCombo.getValue() == null || 
                subjectField.getText().trim().isEmpty() || 
                messageArea.getText().trim().isEmpty()) {
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText("Please fill in all required fields");
                alert.setContentText("From, Type, Subject, and Message are required fields.");
                alert.showAndWait();
                return;
            }
            
            // Create new request
            List<Message> newMessages = new ArrayList<>();
            newMessages.add(new Message(
                1,
                fromField.getText().trim(),
                messageArea.getText().trim(),
                java.time.LocalDateTime.now().toString().substring(0, 16),
                false
            ));
            
            String requestId = "REQ-" + String.format("%03d", requests.size() + 1);
            String building = buildingCombo.getValue() != null ? buildingCombo.getValue() : "General";
            String apartment = apartmentField.getText().trim().isEmpty() ? "General" : apartmentField.getText().trim();
            String priority = priorityCombo.getValue() != null ? priorityCombo.getValue() : "Medium";
            
            Request newRequest = new Request(
                requestId,
                fromField.getText().trim(),
                building,
                apartment,
                typeCombo.getValue(),
                subjectField.getText().trim(),
                "open",
                java.time.LocalDateTime.now().toString().substring(0, 16),
                priority,
                newMessages
            );
            
            requests.add(0, newRequest); // Add to beginning of list
            updateRequestsList();
            
            // Show success message
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Request Created");
            success.setHeaderText("Request submitted successfully!");
            success.setContentText("Request ID: " + requestId + "\nYou can track its progress in the requests list.");
            success.showAndWait();
            
            dialog.close();
        });
        
        buttons.getChildren().addAll(cancelBtn, submitBtn);
        
        content.getChildren().addAll(form, buttons);
        
        Scene scene = new Scene(content, 600, 500);
        dialog.setScene(scene);
        dialog.show();
    }
    
    // Data classes
    private static class Request {
        String id, from, building, apartment, type, subject, status, date, priority;
        List<Message> messages;
        
        Request(String id, String from, String building, String apartment, String type, String subject, 
                String status, String date, String priority, List<Message> messages) {
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
    }
    
    private static class Message {
        int id;
        String sender, text, time;
        boolean isStaff;
        
        Message(int id, String sender, String text, String time, boolean isStaff) {
            this.id = id;
            this.sender = sender;
            this.text = text;
            this.time = time;
            this.isStaff = isStaff;
        }
    }
}