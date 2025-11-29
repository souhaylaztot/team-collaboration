package com.smartpropertymanager.pages;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.smartpropertymanager.models.Permit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PermitManagementPage implements Page {
    private VBox content;
    private String title = "Permit Management";
    private String currentFilter = "All";
    private VBox permitsContainer;
    
    // Sample data
    private List<Permit> permits = Arrays.asList(
        new Permit("PER-2025-001", "Sunset Hills Plot", "Residential Construction", 
                  "Property Holdings LLC", "2025-10-15", "approved", 25000000,
                  "Construction of 6-story residential building with 24 apartments",
                  "John Anderson", "2025-10-25"),
        new Permit("PER-2025-002", "Downtown Commercial Lot", "Commercial Development", 
                  "Metro Investments", "2025-10-20", "pending", 50000000,
                  "Mixed-use development with retail and office spaces", null, null),
        new Permit("PER-2025-003", "Green Valley Estate", "Infrastructure", 
                  "City Development Corp", "2025-10-18", "approved", 15000000,
                  "Road construction and utility installation", "Sarah Wilson", "2025-10-28"),
        new Permit("PER-2025-004", "Riverside Park", "Renovation", 
                  "Parks & Recreation", "2025-10-22", "rejected", 8000000,
                  "Park renovation and landscaping project", null, null),
        new Permit("PER-2025-005", "Tech Hub Complex", "Commercial Construction", 
                  "Innovation Partners", "2025-10-25", "pending", 75000000,
                  "Modern office complex with co-working spaces", null, null)
    );

    public PermitManagementPage() {
        content = new VBox();
        content.setStyle("-fx-background-color: #F8FAFC;");
        content.setPadding(new Insets(32));
        content.setSpacing(24);
        
        createContent();
    }

    private void createContent() {
        // Header
        HBox header = createHeader();
        content.getChildren().add(header);

        // Statistics Cards
        HBox statsCards = createStatsCards();
        content.getChildren().add(statsCards);

        // Filter Tabs
        HBox filterTabs = createFilterTabs();
        content.getChildren().add(filterTabs);

        // Permits Container
        ScrollPane scrollPane = new ScrollPane();
        permitsContainer = new VBox(16);
        scrollPane.setContent(permitsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        content.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Load permits
        filterPermits(currentFilter);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        VBox titleSection = new VBox(8);
        Label titleLabel = new Label("Permit Management");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label subtitleLabel = new Label("Manage construction permits and development requests");
        subtitleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button requestPermitBtn = new Button("+ Request Permit");
        requestPermitBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-size: 14;");
        requestPermitBtn.setOnAction(e -> showRequestPermitDialog());

        header.getChildren().addAll(titleSection, spacer, requestPermitBtn);
        return header;
    }

    private HBox createStatsCards() {
        HBox statsContainer = new HBox(24);
        statsContainer.setAlignment(Pos.CENTER_LEFT);

        long totalPermits = permits.size();
        long pendingPermits = permits.stream().filter(p -> "pending".equals(p.getStatus())).count();
        long approvedPermits = permits.stream().filter(p -> "approved".equals(p.getStatus())).count();
        long rejectedPermits = permits.stream().filter(p -> "rejected".equals(p.getStatus())).count();

        VBox totalCard = createStatCard("📋", "Total Permits", String.valueOf(totalPermits), "#4F46E5");
        VBox pendingCard = createStatCard("🕐", "Pending Review", String.valueOf(pendingPermits), "#F59E0B");
        VBox approvedCard = createStatCard("✅", "Approved", String.valueOf(approvedPermits), "#10B981");
        VBox rejectedCard = createStatCard("❌", "Rejected", String.valueOf(rejectedPermits), "#EF4444");

        statsContainer.getChildren().addAll(totalCard, pendingCard, approvedCard, rejectedCard);
        return statsContainer;
    }

    private VBox createStatCard(String icon, String title, String value, String color) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        card.setPrefWidth(200);

        HBox iconRow = new HBox(12);
        iconRow.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24; -fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 8; -fx-background-radius: 8;");
        
        iconRow.getChildren().add(iconLabel);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        valueLabel.setStyle("-fx-text-fill: #111827;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        card.getChildren().addAll(iconRow, valueLabel, titleLabel);
        return card;
    }

    private HBox createFilterTabs() {
        HBox tabsContainer = new HBox(0);
        tabsContainer.setAlignment(Pos.CENTER_LEFT);

        String[] tabs = {"All", "Pending", "Approved", "Rejected"};
        
        for (String tab : tabs) {
            Button tabBtn = new Button(tab);
            boolean isActive = tab.equals(currentFilter);
            
            String style = isActive 
                ? "-fx-background-color: #2563EB; -fx-text-fill: white; -fx-padding: 12 24; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-radius: 8 8 0 0;"
                : "-fx-background-color: #F3F4F6; -fx-text-fill: #6B7280; -fx-padding: 12 24; -fx-font-size: 14; -fx-background-radius: 8 8 0 0;";
            
            tabBtn.setStyle(style);
            tabBtn.setOnAction(e -> {
                currentFilter = tab;
                filterPermits(tab);
                createFilterTabs(); // Refresh tabs
                content.getChildren().set(2, createFilterTabs()); // Update tabs in content
            });
            
            tabsContainer.getChildren().add(tabBtn);
        }

        return tabsContainer;
    }

    private void filterPermits(String filter) {
        permitsContainer.getChildren().clear();
        
        List<Permit> filteredPermits = permits.stream()
            .filter(permit -> "All".equals(filter) || filter.toLowerCase().equals(permit.getStatus()))
            .collect(Collectors.toList());
        
        for (Permit permit : filteredPermits) {
            VBox permitCard = createPermitCard(permit);
            permitsContainer.getChildren().add(permitCard);
        }
    }

    private VBox createPermitCard(Permit permit) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        // Header row with ID and status
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);
        headerRow.setSpacing(12);

        Label idLabel = new Label(permit.getId());
        idLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        idLabel.setStyle("-fx-text-fill: #111827;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label statusBadge = createStatusBadge(permit.getStatus());

        headerRow.getChildren().addAll(idLabel, spacer, statusBadge);

        // Content rows
        VBox contentRows = new VBox(8);
        
        HBox row1 = new HBox(40);
        row1.getChildren().addAll(
            createInfoColumn("Land Name", permit.getLandName()),
            createInfoColumn("Type", permit.getType())
        );
        
        HBox row2 = new HBox(40);
        row2.getChildren().addAll(
            createInfoColumn("Requested By", permit.getRequestedBy()),
            createInfoColumn("Request Date", permit.getRequestDate())
        );
        
        HBox row3 = new HBox(40);
        row3.getChildren().addAll(
            createInfoColumn("Estimated Cost", permit.getFormattedCost()),
            createInfoColumn("Description", permit.getDescription())
        );

        contentRows.getChildren().addAll(row1, row2, row3);

        // Action buttons for pending permits
        HBox actionRow = new HBox(12);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        
        if ("pending".equals(permit.getStatus())) {
            Button approveBtn = new Button("Approve");
            approveBtn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6; -fx-font-size: 12;");
            approveBtn.setOnAction(e -> approvePermit(permit));
            
            Button rejectBtn = new Button("Reject");
            rejectBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6; -fx-font-size: 12;");
            rejectBtn.setOnAction(e -> rejectPermit(permit));
            
            actionRow.getChildren().addAll(approveBtn, rejectBtn);
        }

        card.getChildren().addAll(headerRow, contentRows, actionRow);
        return card;
    }

    private VBox createInfoColumn(String label, String value) {
        VBox column = new VBox(4);
        column.setPrefWidth(300);
        
        Label labelText = new Label(label);
        labelText.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12; -fx-font-weight: bold;");
        
        Label valueText = new Label(value != null ? value : "-");
        valueText.setStyle("-fx-text-fill: #111827; -fx-font-size: 14;");
        valueText.setWrapText(true);
        
        column.getChildren().addAll(labelText, valueText);
        return column;
    }

    private Label createStatusBadge(String status) {
        Label badge = new Label(status.toUpperCase());
        String style;
        
        switch (status.toLowerCase()) {
            case "approved":
                style = "-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 6 12; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
                break;
            case "pending":
                style = "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-padding: 6 12; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
                break;
            case "rejected":
                style = "-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-padding: 6 12; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
                break;
            default:
                style = "-fx-background-color: #F3F4F6; -fx-text-fill: #6B7280; -fx-padding: 6 12; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
        }
        
        badge.setStyle(style);
        return badge;
    }

    private void showRequestPermitDialog() {
        Dialog<Permit> dialog = new Dialog<>();
        dialog.setTitle("Request New Permit");
        dialog.setHeaderText("Submit a new permit request");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField landNameField = new TextField();
        landNameField.setPromptText("Land/Property Name");
        TextField typeField = new TextField();
        typeField.setPromptText("Permit Type");
        TextField requestedByField = new TextField();
        requestedByField.setPromptText("Requested By");
        TextField costField = new TextField();
        costField.setPromptText("Estimated Cost (MAD)");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Project Description");
        descriptionField.setPrefRowCount(3);

        grid.add(new Label("Land Name:"), 0, 0);
        grid.add(landNameField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Requested By:"), 0, 2);
        grid.add(requestedByField, 1, 2);
        grid.add(new Label("Estimated Cost:"), 0, 3);
        grid.add(costField, 1, 3);
        grid.add(new Label("Description:"), 0, 4);
        grid.add(descriptionField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        ButtonType submitButtonType = new ButtonType("Submit Request", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                try {
                    String newId = "PER-2025-" + String.format("%03d", permits.size() + 1);
                    return new Permit(newId, landNameField.getText(), typeField.getText(),
                                    requestedByField.getText(), "2025-10-30", "pending",
                                    Integer.parseInt(costField.getText()), descriptionField.getText(),
                                    null, null);
                } catch (NumberFormatException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Invalid Input");
                    errorAlert.setContentText("Please enter a valid number for estimated cost.");
                    errorAlert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(permit -> {
            if (permit != null) {
                permits.add(permit);
                filterPermits(currentFilter);
                content.getChildren().set(1, createStatsCards()); // Update stats
                
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Permit Request Submitted");
                successAlert.setContentText("Permit request " + permit.getId() + " has been successfully submitted for review.");
                successAlert.showAndWait();
            }
        });
    }

    private void approvePermit(Permit permit) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Approve Permit");
        confirmDialog.setHeaderText("Approve permit " + permit.getId() + "?");
        confirmDialog.setContentText("Are you sure you want to approve this permit request?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // In a real application, you would update the permit status in the database
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Permit Approved");
                successAlert.setContentText("Permit " + permit.getId() + " has been approved successfully.");
                successAlert.showAndWait();
                
                // Refresh the view
                filterPermits(currentFilter);
                content.getChildren().set(1, createStatsCards());
            }
        });
    }

    private void rejectPermit(Permit permit) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Reject Permit");
        confirmDialog.setHeaderText("Reject permit " + permit.getId() + "?");
        confirmDialog.setContentText("Are you sure you want to reject this permit request?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // In a real application, you would update the permit status in the database
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Permit Rejected");
                successAlert.setContentText("Permit " + permit.getId() + " has been rejected.");
                successAlert.showAndWait();
                
                // Refresh the view
                filterPermits(currentFilter);
                content.getChildren().set(1, createStatsCards());
            }
        });
    }

    @Override
    public VBox getContent() {
        return content;
    }

    @Override
    public String getTitle() {
        return title;
    }
}