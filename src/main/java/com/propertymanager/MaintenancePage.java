package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintenancePage extends VBox {
    
    private String selectedTab = "all";
    private VBox requestsContainer;
    private List<MaintenanceRequest> requests;
    
    public MaintenancePage() {
        initRequests();
        initPage();
    }
    
    private void initRequests() {
        requests = new ArrayList<>();
        loadMaintenanceRequestsFromDatabase();
    }
    
    private void loadMaintenanceRequestsFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT mr.request_number, b.name as building_name, " +
                          "CASE WHEN mr.apartment_id IS NOT NULL THEN CONCAT('Apt ', a.apartment_number) ELSE 'Common Area' END as location, " +
                          "mr.requested_by, mr.issue_title, mr.description, mr.priority, mr.status, " +
                          "mr.request_date, mr.estimated_cost, mr.assigned_to, mr.completion_date " +
                          "FROM maintenance_requests mr " +
                          "JOIN buildings b ON mr.building_id = b.id " +
                          "LEFT JOIN apartments a ON mr.apartment_id = a.id " +
                          "ORDER BY mr.request_date DESC";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String completedDate = rs.getDate("completion_date") != null ? 
                                         rs.getDate("completion_date").toString() : null;
                    
                    MaintenanceRequest request = new MaintenanceRequest(
                        rs.getString("request_number"),
                        rs.getString("building_name"),
                        rs.getString("location"),
                        rs.getString("requested_by"),
                        rs.getString("issue_title"),
                        rs.getString("description"),
                        rs.getString("priority"),
                        rs.getString("status"),
                        rs.getDate("request_date").toString(),
                        rs.getInt("estimated_cost"),
                        rs.getString("assigned_to"),
                        completedDate
                    );
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void initPage() {
        setPadding(new Insets(30));
        setSpacing(25);
        setStyle("-fx-background-color: #f8f9fa;");
        
        HBox header = createHeader();
        HBox statsRow = createStatsRow();
        VBox requestsList = createRequestsList();
        
        getChildren().addAll(header, statsRow, requestsList);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Maintenance Tracker");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Track and manage maintenance requests");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#6c757d"));
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addButton = new Button("➕ New Request");
        addButton.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20; -fx-font-weight: bold;");
        addButton.setOnAction(e -> showAddRequestDialog());
        
        header.getChildren().addAll(titleBox, spacer, addButton);
        return header;
    }
    
    private HBox createStatsRow() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        int total = requests.size();
        int pending = (int) requests.stream().filter(r -> "pending".equals(r.status)).count();
        int inProgress = (int) requests.stream().filter(r -> "in-progress".equals(r.status)).count();
        int completed = (int) requests.stream().filter(r -> "completed".equals(r.status)).count();
        
        statsRow.getChildren().addAll(
            createStatCard("Total Requests", String.valueOf(total), "🔧", "#2C3E8C"),
            createStatCard("Pending", String.valueOf(pending), "⏰", "#F5C542"),
            createStatCard("In Progress", String.valueOf(inProgress), "⚠️", "#2196F3"),
            createStatCard("Completed", String.valueOf(completed), "✅", "#4CAF50")
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
    
    private VBox createRequestsList() {
        VBox container = new VBox(20);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        HBox tabsBox = createTabsBox();
        
        requestsContainer = new VBox(15);
        ScrollPane scrollPane = new ScrollPane(requestsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        updateRequestsList();
        
        container.getChildren().addAll(tabsBox, scrollPane);
        return container;
    }
    
    private HBox createTabsBox() {
        HBox tabsBox = new HBox(10);
        tabsBox.setAlignment(Pos.CENTER_LEFT);
        
        Button allTab = createTabButton("All Requests", "all");
        Button pendingTab = createTabButton("Pending", "pending");
        Button progressTab = createTabButton("In Progress", "in-progress");
        Button completedTab = createTabButton("Completed", "completed");
        
        tabsBox.getChildren().addAll(allTab, pendingTab, progressTab, completedTab);
        return tabsBox;
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
            selectedTab = tabValue;
            updateTabStyles();
            updateRequestsList();
        });
        
        return button;
    }
    
    private void updateTabStyles() {
        VBox parent = (VBox) getChildren().get(2);
        HBox newTabsBox = createTabsBox();
        parent.getChildren().set(0, newTabsBox);
    }
    
    private void updateRequestsList() {
        requestsContainer.getChildren().clear();
        
        List<MaintenanceRequest> filteredRequests = requests.stream()
            .filter(request -> selectedTab.equals("all") || request.status.equals(selectedTab))
            .toList();
        
        for (MaintenanceRequest request : filteredRequests) {
            VBox requestCard = createRequestCard(request);
            requestsContainer.getChildren().add(requestCard);
        }
    }
    
    private VBox createRequestCard(MaintenanceRequest request) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #e9ecef; -fx-border-radius: 8;");
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        HBox idRow = new HBox(10);
        idRow.setAlignment(Pos.CENTER_LEFT);
        
        Label idLabel = new Label(request.id);
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        idLabel.setTextFill(Color.web("#2c3e50"));
        
        Label statusBadge = createStatusBadge(request.status);
        Label priorityBadge = createPriorityBadge(request.priority);
        idRow.getChildren().addAll(idLabel, statusBadge, priorityBadge);
        
        Label issueLabel = new Label(request.issue);
        issueLabel.setFont(Font.font("Arial", 14));
        issueLabel.setTextFill(Color.GRAY);
        
        titleBox.getChildren().addAll(idRow, issueLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox buttonsBox = new HBox(10);
        
        if ("pending".equals(request.status)) {
            Button assignBtn = new Button("Assign");
            assignBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 15;");
            buttonsBox.getChildren().add(assignBtn);
        }
        
        if ("in-progress".equals(request.status)) {
            Button completeBtn = new Button("✅ Complete");
            completeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 15;");
            buttonsBox.getChildren().add(completeBtn);
        }
        
        Button detailsBtn = new Button("👁 Details");
        detailsBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8 15;");
        buttonsBox.getChildren().add(detailsBtn);
        
        header.getChildren().addAll(titleBox, spacer, buttonsBox);
        
        // Details grid
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(20);
        detailsGrid.setVgap(10);
        
        addDetailField(detailsGrid, 0, 0, "Building", request.building);
        addDetailField(detailsGrid, 1, 0, "📍 Location", request.apartment);
        addDetailField(detailsGrid, 2, 0, "Requested By", request.requestedBy);
        addDetailField(detailsGrid, 3, 0, "Date", request.date);
        addDetailField(detailsGrid, 4, 0, "💰 Est. Cost", request.estimatedCost + " MAD");
        
        // Description
        VBox descBox = new VBox(5);
        Label descTitle = new Label("Description");
        descTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        descTitle.setTextFill(Color.GRAY);
        
        Label descText = new Label(request.description);
        descText.setFont(Font.font("Arial", 13));
        descText.setTextFill(Color.web("#495057"));
        descText.setWrapText(true);
        
        descBox.getChildren().addAll(descTitle, descText);
        
        card.getChildren().addAll(header, detailsGrid, descBox);
        
        // Assignment info
        if (request.assignedTo != null) {
            Separator separator = new Separator();
            
            HBox assignmentInfo = new HBox(15);
            assignmentInfo.setAlignment(Pos.CENTER_LEFT);
            
            String infoText = "Assigned to: " + request.assignedTo;
            if (request.completedDate != null) {
                infoText += " • Completed: " + request.completedDate;
            }
            
            Label assignmentLabel = new Label(infoText);
            assignmentLabel.setFont(Font.font("Arial", 12));
            assignmentLabel.setTextFill(Color.GRAY);
            
            assignmentInfo.getChildren().add(assignmentLabel);
            card.getChildren().addAll(separator, assignmentInfo);
        }
        
        return card;
    }
    
    private void addDetailField(GridPane grid, int col, int row, String label, String value) {
        VBox fieldBox = new VBox(3);
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", 11));
        labelText.setTextFill(Color.GRAY);
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        valueText.setTextFill(Color.web("#2c3e50"));
        
        fieldBox.getChildren().addAll(labelText, valueText);
        grid.add(fieldBox, col, row);
    }
    
    private Label createStatusBadge(String status) {
        Label badge = new Label();
        badge.setPadding(new Insets(4, 8, 4, 8));
        badge.setStyle("-fx-background-radius: 12; -fx-font-size: 11px; -fx-font-weight: bold;");
        
        switch (status) {
            case "pending":
                badge.setText("Pending");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #fff3e0; -fx-text-fill: #f57c00;");
                break;
            case "in-progress":
                badge.setText("In Progress");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e3f2fd; -fx-text-fill: #1976d2;");
                break;
            case "completed":
                badge.setText("Completed");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32;");
                break;
        }
        
        return badge;
    }
    
    private Label createPriorityBadge(String priority) {
        Label badge = new Label();
        badge.setPadding(new Insets(4, 8, 4, 8));
        badge.setStyle("-fx-background-radius: 12; -fx-font-size: 11px; -fx-font-weight: bold;");
        
        switch (priority) {
            case "high":
                badge.setText("⚠️ High");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #ffebee; -fx-text-fill: #d32f2f;");
                break;
            case "medium":
                badge.setText("⏰ Medium");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #fff3e0; -fx-text-fill: #f57c00;");
                break;
            case "low":
                badge.setText("Low");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e8f5e8; -fx-text-fill: #388e3c;");
                break;
        }
        
        return badge;
    }
    
    private void showAddRequestDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Create Maintenance Request");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        // Building selection
        Label buildingLabel = new Label("Building");
        ComboBox<String> buildingCombo = new ComboBox<>();
        buildingCombo.getItems().addAll("Skyline Tower", "Riverside Apartments", "Garden View Complex");
        buildingCombo.setPrefWidth(300);
        
        // Location
        Label locationLabel = new Label("Location/Apartment");
        TextField locationField = new TextField();
        locationField.setPromptText("e.g., Apt 305, Lobby");
        locationField.setPrefWidth(300);
        
        // Requested by
        Label requestedLabel = new Label("Requested By");
        TextField requestedField = new TextField();
        requestedField.setPromptText("Buyer or staff name");
        requestedField.setPrefWidth(300);
        
        // Priority
        Label priorityLabel = new Label("Priority");
        ComboBox<String> priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll("High", "Medium", "Low");
        priorityCombo.setPrefWidth(300);
        
        // Issue title
        Label issueLabel = new Label("Issue Title");
        TextField issueField = new TextField();
        issueField.setPromptText("Brief description of the issue");
        issueField.setPrefWidth(600);
        
        // Description
        Label descLabel = new Label("Detailed Description");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Provide detailed information about the maintenance issue...");
        descArea.setPrefRowCount(4);
        descArea.setPrefWidth(600);
        
        // Estimated cost
        Label costLabel = new Label("Estimated Cost (MAD)");
        TextField costField = new TextField();
        costField.setPromptText("3500");
        costField.setPrefWidth(300);
        
        // Assign to
        Label assignLabel = new Label("Assign To (Optional)");
        TextField assignField = new TextField();
        assignField.setPromptText("Technician or company name");
        assignField.setPrefWidth(300);
        
        // File upload
        Label fileLabel = new Label("Attach Photos (Optional)");
        Button fileButton = new Button("Choose Files");
        fileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Photos");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            fileChooser.showOpenMultipleDialog(dialog);
        });
        
        form.add(buildingLabel, 0, 0);
        form.add(buildingCombo, 1, 0);
        form.add(locationLabel, 0, 1);
        form.add(locationField, 1, 1);
        form.add(requestedLabel, 0, 2);
        form.add(requestedField, 1, 2);
        form.add(priorityLabel, 0, 3);
        form.add(priorityCombo, 1, 3);
        form.add(issueLabel, 0, 4);
        form.add(issueField, 1, 4);
        form.add(descLabel, 0, 5);
        form.add(descArea, 1, 5);
        form.add(costLabel, 0, 6);
        form.add(costField, 1, 6);
        form.add(assignLabel, 0, 7);
        form.add(assignField, 1, 7);
        form.add(fileLabel, 0, 8);
        form.add(fileButton, 1, 8);
        
        // Buttons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 10 20;");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button submitBtn = new Button("Create Request");
        submitBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-font-weight: bold;");
        submitBtn.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(cancelBtn, submitBtn);
        
        content.getChildren().addAll(form, buttons);
        
        Scene scene = new Scene(content, 700, 600);
        dialog.setScene(scene);
        dialog.show();
    }
    
    // MaintenanceRequest data class
    private static class MaintenanceRequest {
        String id, building, apartment, requestedBy, issue, description, priority, status, date, assignedTo, completedDate;
        int estimatedCost;
        
        MaintenanceRequest(String id, String building, String apartment, String requestedBy, String issue, 
                          String description, String priority, String status, String date, int estimatedCost, 
                          String assignedTo, String completedDate) {
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
    }
}