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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermitsPage extends VBox {
    
    private String selectedTab = "all";
    private VBox permitsContainer;
    private List<Permit> permits;
    
    public PermitsPage() {
        initPermits();
        initPage();
    }
    
    private void initPermits() {
        permits = new ArrayList<>();
        loadPermitsFromDatabase();
    }
    
    private void loadPermitsFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT p.permit_number, COALESCE(l.name, 'General Property') as land_name, " +
                          "p.permit_type, p.requested_by, p.request_date, p.status, p.estimated_cost, " +
                          "p.description, p.approved_by, p.approval_date " +
                          "FROM permits p " +
                          "LEFT JOIN lands l ON p.land_id = l.id " +
                          "ORDER BY p.request_date DESC";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String approvalDate = rs.getDate("approval_date") != null ? 
                                        rs.getDate("approval_date").toString() : null;
                    
                    Permit permit = new Permit(
                        rs.getString("permit_number"),
                        rs.getString("land_name"),
                        rs.getString("permit_type"),
                        rs.getString("requested_by"),
                        rs.getDate("request_date").toString(),
                        rs.getString("status"),
                        rs.getInt("estimated_cost"),
                        rs.getString("description"),
                        rs.getString("approved_by"),
                        approvalDate
                    );
                    permits.add(permit);
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
        
        // Header
        HBox header = createHeader();
        
        // Stats cards
        HBox statsRow = createStatsRow();
        
        // Permits list
        VBox permitsList = createPermitsList();
        
        getChildren().addAll(header, statsRow, permitsList);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Construction Permits");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Manage building permits and development requests");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#6c757d"));
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addButton = new Button("➕ Request Permit");
        addButton.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20; -fx-font-weight: bold;");
        addButton.setOnAction(e -> showAddPermitDialog());
        
        header.getChildren().addAll(titleBox, spacer, addButton);
        return header;
    }
    
    private HBox createStatsRow() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        int total = permits.size();
        int pending = (int) permits.stream().filter(p -> "pending".equals(p.status)).count();
        int approved = (int) permits.stream().filter(p -> "approved".equals(p.status)).count();
        int rejected = (int) permits.stream().filter(p -> "rejected".equals(p.status)).count();
        
        statsRow.getChildren().addAll(
            createStatCard("Total Permits", String.valueOf(total), "📋", "#2C3E8C"),
            createStatCard("Pending Review", String.valueOf(pending), "⏰", "#F5C542"),
            createStatCard("Approved", String.valueOf(approved), "✅", "#4CAF50"),
            createStatCard("Rejected", String.valueOf(rejected), "❌", "#F44336")
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
    
    private VBox createPermitsList() {
        VBox container = new VBox(20);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Tabs
        HBox tabsBox = createTabsBox();
        
        // Permits container
        permitsContainer = new VBox(15);
        ScrollPane scrollPane = new ScrollPane(permitsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        updatePermitsList();
        
        container.getChildren().addAll(tabsBox, scrollPane);
        return container;
    }
    
    private HBox createTabsBox() {
        HBox tabsBox = new HBox(10);
        tabsBox.setAlignment(Pos.CENTER_LEFT);
        
        Button allTab = createTabButton("All Permits", "all");
        Button pendingTab = createTabButton("Pending", "pending");
        Button approvedTab = createTabButton("Approved", "approved");
        Button rejectedTab = createTabButton("Rejected", "rejected");
        
        tabsBox.getChildren().addAll(allTab, pendingTab, approvedTab, rejectedTab);
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
            updatePermitsList();
        });
        
        return button;
    }
    
    private void updateTabStyles() {
        // Recreate tabs with updated styles
        VBox parent = (VBox) getChildren().get(2);
        HBox newTabsBox = createTabsBox();
        parent.getChildren().set(0, newTabsBox);
    }
    
    private void updatePermitsList() {
        permitsContainer.getChildren().clear();
        
        List<Permit> filteredPermits = permits.stream()
            .filter(permit -> selectedTab.equals("all") || permit.status.equals(selectedTab))
            .toList();
        
        for (Permit permit : filteredPermits) {
            VBox permitCard = createPermitCard(permit);
            permitsContainer.getChildren().add(permitCard);
        }
    }
    
    private VBox createPermitCard(Permit permit) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #e9ecef; -fx-border-radius: 8;");
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        HBox idRow = new HBox(10);
        idRow.setAlignment(Pos.CENTER_LEFT);
        
        Label idLabel = new Label(permit.id);
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        idLabel.setTextFill(Color.web("#2c3e50"));
        
        Label statusBadge = createStatusBadge(permit.status);
        idRow.getChildren().addAll(idLabel, statusBadge);
        
        Label landLabel = new Label(permit.landName);
        landLabel.setFont(Font.font("Arial", 14));
        landLabel.setTextFill(Color.GRAY);
        
        titleBox.getChildren().addAll(idRow, landLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox buttonsBox = new HBox(10);
        
        if ("pending".equals(permit.status)) {
            Button approveBtn = new Button("✅ Approve");
            approveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 15;");
            approveBtn.setOnAction(e -> approvePermit(permit));
            
            Button rejectBtn = new Button("❌ Reject");
            rejectBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 15;");
            rejectBtn.setOnAction(e -> rejectPermit(permit));
            
            buttonsBox.getChildren().addAll(approveBtn, rejectBtn);
        }
        
        Button detailsBtn = new Button("👁 Details");
        detailsBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8 15;");
        detailsBtn.setOnAction(e -> showPermitDetails(permit));
        buttonsBox.getChildren().add(detailsBtn);
        
        header.getChildren().addAll(titleBox, spacer, buttonsBox);
        
        // Details grid
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(20);
        detailsGrid.setVgap(10);
        
        addDetailField(detailsGrid, 0, 0, "Type", permit.type);
        addDetailField(detailsGrid, 1, 0, "Requested By", permit.requestedBy);
        addDetailField(detailsGrid, 2, 0, "📅 Request Date", permit.requestDate);
        addDetailField(detailsGrid, 3, 0, "💰 Estimated Cost", String.format("%,d MAD", permit.estimatedCost));
        
        // Description
        VBox descBox = new VBox(5);
        Label descTitle = new Label("Description");
        descTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        descTitle.setTextFill(Color.GRAY);
        
        Label descText = new Label(permit.description);
        descText.setFont(Font.font("Arial", 13));
        descText.setTextFill(Color.web("#495057"));
        descText.setWrapText(true);
        
        descBox.getChildren().addAll(descTitle, descText);
        
        card.getChildren().addAll(header, detailsGrid, descBox);
        
        // Approval info
        if (!"pending".equals(permit.status) && permit.approvedBy != null) {
            Separator separator = new Separator();
            
            HBox approvalInfo = new HBox(15);
            approvalInfo.setAlignment(Pos.CENTER_LEFT);
            
            String statusText = "approved".equals(permit.status) ? "Approved" : "Rejected";
            Label approvalLabel = new Label(statusText + " by: " + permit.approvedBy + " • " + permit.approvalDate);
            approvalLabel.setFont(Font.font("Arial", 12));
            approvalLabel.setTextFill(Color.GRAY);
            
            approvalInfo.getChildren().add(approvalLabel);
            card.getChildren().addAll(separator, approvalInfo);
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
            case "approved":
                badge.setText("✅ Approved");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32;");
                break;
            case "pending":
                badge.setText("⏰ Pending");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #fff3e0; -fx-text-fill: #f57c00;");
                break;
            case "rejected":
                badge.setText("❌ Rejected");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #ffebee; -fx-text-fill: #d32f2f;");
                break;
        }
        
        return badge;
    }
    
    private void showAddPermitDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Request Construction Permit");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        // Land selection
        Label landLabel = new Label("Select Land/Property");
        ComboBox<String> landCombo = new ComboBox<>();
        landCombo.getItems().addAll("Sunset Hills Plot", "Downtown Commercial Lot", "Riverside Estate");
        landCombo.setPrefWidth(300);
        
        // Type selection
        Label typeLabel = new Label("Type of Construction");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Residential Construction", "Commercial Development", "Renovation", "Building Extension", "Warehouse/Industrial");
        typeCombo.setPrefWidth(300);
        
        // Requested by
        Label requestedLabel = new Label("Requested By");
        TextField requestedField = new TextField();
        requestedField.setPromptText("Company or individual name");
        requestedField.setPrefWidth(300);
        
        // Estimated cost
        Label costLabel = new Label("Estimated Cost (MAD)");
        TextField costField = new TextField();
        costField.setPromptText("25000000");
        costField.setPrefWidth(300);
        
        // Description
        Label descLabel = new Label("Project Description");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Detailed description of the construction project...");
        descArea.setPrefRowCount(4);
        descArea.setPrefWidth(600);
        
        // File upload
        Label fileLabel = new Label("Attach Plans/Documents");
        Button fileButton = new Button("Choose Files");
        fileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Documents");
            fileChooser.showOpenMultipleDialog(dialog);
        });
        
        form.add(landLabel, 0, 0);
        form.add(landCombo, 1, 0);
        form.add(typeLabel, 0, 1);
        form.add(typeCombo, 1, 1);
        form.add(requestedLabel, 0, 2);
        form.add(requestedField, 1, 2);
        form.add(costLabel, 0, 3);
        form.add(costField, 1, 3);
        form.add(descLabel, 0, 4);
        form.add(descArea, 1, 4);
        form.add(fileLabel, 0, 5);
        form.add(fileButton, 1, 5);
        
        // Buttons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 10 20;");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button submitBtn = new Button("Submit Request");
        submitBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-font-weight: bold;");
        submitBtn.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(cancelBtn, submitBtn);
        
        content.getChildren().addAll(form, buttons);
        
        Scene scene = new Scene(content, 650, 500);
        dialog.setScene(scene);
        dialog.show();
    }
    
    private void approvePermit(Permit permit) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Approve Permit");
        confirmAlert.setHeaderText("Approve " + permit.id + "?");
        confirmAlert.setContentText("Are you sure you want to approve this construction permit?\n\n" +
                "Land: " + permit.landName + "\n" +
                "Type: " + permit.type + "\n" +
                "Requested by: " + permit.requestedBy);
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                updatePermitStatus(permit, "approved", "Admin User");
            }
        });
    }
    
    private void rejectPermit(Permit permit) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Reject Permit");
        confirmAlert.setHeaderText("Reject " + permit.id + "?");
        confirmAlert.setContentText("Are you sure you want to reject this construction permit?\n\n" +
                "Land: " + permit.landName + "\n" +
                "Type: " + permit.type + "\n" +
                "Requested by: " + permit.requestedBy);
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                updatePermitStatus(permit, "rejected", "Admin User");
            }
        });
    }
    
    private void updatePermitStatus(Permit permit, String newStatus, String approvedBy) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "UPDATE permits SET status = ?, approved_by = ?, approval_date = CURRENT_DATE WHERE permit_number = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, newStatus);
                stmt.setString(2, approvedBy);
                stmt.setString(3, permit.id);
                
                int rowsUpdated = stmt.executeUpdate();
                
                if (rowsUpdated > 0) {
                    // Update local permit object
                    permit.status = newStatus;
                    permit.approvedBy = approvedBy;
                    permit.approvalDate = java.time.LocalDate.now().toString();
                    
                    // Refresh the permits list
                    permits.clear();
                    loadPermitsFromDatabase();
                    updatePermitsList();
                    
                    // Show success message
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Permit " + ("approved".equals(newStatus) ? "Approved" : "Rejected"));
                    successAlert.setContentText("Permit " + permit.id + " has been " + newStatus + " successfully!");
                    successAlert.showAndWait();
                } else {
                    showErrorAlert("Update Failed", "Failed to update permit status. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "Error updating permit: " + e.getMessage());
        }
    }
    
    private void showPermitDetails(Permit permit) {
        Stage detailsDialog = new Stage();
        detailsDialog.setTitle("Permit Details - " + permit.id);
        detailsDialog.initModality(Modality.APPLICATION_MODAL);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header
        VBox headerBox = new VBox(10);
        Label titleLabel = new Label(permit.id);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        HBox statusBox = new HBox(10);
        Label statusBadge = createStatusBadge(permit.status);
        Label typeLabel = new Label(permit.type);
        typeLabel.setPadding(new Insets(4, 8, 4, 8));
        typeLabel.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 12; -fx-font-size: 12px;");
        statusBox.getChildren().addAll(statusBadge, typeLabel);
        
        headerBox.getChildren().addAll(titleLabel, statusBox);
        
        // Details Grid
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(30);
        detailsGrid.setVgap(15);
        detailsGrid.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20;");
        
        addDetailRow(detailsGrid, 0, 0, "🏢 Land/Property", permit.landName);
        addDetailRow(detailsGrid, 1, 0, "🏗️ Type", permit.type);
        addDetailRow(detailsGrid, 0, 1, "👤 Requested By", permit.requestedBy);
        addDetailRow(detailsGrid, 1, 1, "📅 Request Date", permit.requestDate);
        addDetailRow(detailsGrid, 0, 2, "💰 Estimated Cost", String.format("%,d MAD", permit.estimatedCost));
        
        if (permit.approvedBy != null) {
            addDetailRow(detailsGrid, 1, 2, "✅ Approved By", permit.approvedBy);
            addDetailRow(detailsGrid, 0, 3, "📅 Approval Date", permit.approvalDate);
        }
        
        // Description
        VBox descriptionBox = new VBox(10);
        descriptionBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20;");
        
        Label descTitle = new Label("📝 Project Description");
        descTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        descTitle.setTextFill(Color.web("#2c3e50"));
        
        Label descText = new Label(permit.description);
        descText.setFont(Font.font("Arial", 14));
        descText.setTextFill(Color.web("#495057"));
        descText.setWrapText(true);
        descText.setPrefWidth(500);
        
        descriptionBox.getChildren().addAll(descTitle, descText);
        
        // Action Buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        
        if ("pending".equals(permit.status)) {
            Button approveBtn = new Button("✅ Approve Permit");
            approveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20; -fx-font-weight: bold;");
            approveBtn.setOnAction(e -> {
                approvePermit(permit);
                detailsDialog.close();
            });
            
            Button rejectBtn = new Button("❌ Reject Permit");
            rejectBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20; -fx-font-weight: bold;");
            rejectBtn.setOnAction(e -> {
                rejectPermit(permit);
                detailsDialog.close();
            });
            
            actionButtons.getChildren().addAll(approveBtn, rejectBtn);
        }
        
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20;");
        closeBtn.setOnAction(e -> detailsDialog.close());
        actionButtons.getChildren().add(closeBtn);
        
        content.getChildren().addAll(headerBox, detailsGrid, descriptionBox, actionButtons);
        
        Scene scene = new Scene(content, 600, 500);
        detailsDialog.setScene(scene);
        detailsDialog.show();
    }
    
    private void addDetailRow(GridPane grid, int col, int row, String label, String value) {
        VBox fieldBox = new VBox(5);
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        labelText.setTextFill(Color.web("#6c757d"));
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("Arial", 14));
        valueText.setTextFill(Color.web("#2c3e50"));
        valueText.setWrapText(true);
        
        fieldBox.getChildren().addAll(labelText, valueText);
        grid.add(fieldBox, col, row);
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Permit data class
    private static class Permit {
        String id, landName, type, requestedBy, requestDate, status, approvedBy, approvalDate, description;
        int estimatedCost;
        
        Permit(String id, String landName, String type, String requestedBy, String requestDate, 
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
    }
}