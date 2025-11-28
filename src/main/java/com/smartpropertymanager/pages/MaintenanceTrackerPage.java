package com.smartpropertymanager.pages;

import com.smartpropertymanager.models.MaintenanceRequest;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.Arrays;
import java.util.List;

public class MaintenanceTrackerPage implements Page {
    private VBox content;
    private String title = "Maintenance Tracker";
    
    private ObservableList<MaintenanceRequest> maintenanceRequests;
    private FilteredList<MaintenanceRequest> filteredRequests;
    private TabPane tabPane;
    private VBox requestsList;

    public MaintenanceTrackerPage() {
        content = new VBox();
        content.setStyle("-fx-background-color: #F8FAFC;");
        content.setPadding(new Insets(32));
        content.setSpacing(24);
        
        initializeData();
        createUI();
    }

    private void initializeData() {
        // Sample maintenance requests data
        List<MaintenanceRequest> requestList = Arrays.asList(
            new MaintenanceRequest("MNT-2025-045", "Skyline Tower", "Apt 305", "John Smith", 
                                 "Leaking pipe in bathroom", "Water dripping from ceiling pipe, needs immediate attention", 
                                 "high", "pending", "2025-11-04", 3500, null, null),
            new MaintenanceRequest("MNT-2025-046", "Riverside Apartments", "Apt 205", "Sarah Johnson", 
                                 "Air conditioning not working", "AC unit making loud noise and not cooling properly", 
                                 "medium", "in-progress", "2025-11-03", 5000, "Mike Torres", null),
            new MaintenanceRequest("MNT-2025-047", "Garden View Complex", "Lobby", "Property Manager", 
                                 "Elevator maintenance", "Routine elevator inspection and maintenance required", 
                                 "medium", "completed", "2025-10-28", 12000, "TechLift Services", "2025-11-01"),
            new MaintenanceRequest("MNT-2025-048", "Metro Heights", "Apt 410", "Emma Davis", 
                                 "Electrical outlet not working", "Kitchen outlet stopped working, need electrician", 
                                 "low", "pending", "2025-11-02", 1500, null, null),
            new MaintenanceRequest("MNT-2025-049", "Skyline Tower", "Parking Lot", "Security Staff", 
                                 "Broken gate motor", "Parking gate motor malfunctioning, manual operation required", 
                                 "high", "in-progress", "2025-11-05", 8000, "AutoGate Systems", null)
        );
        
        maintenanceRequests = FXCollections.observableArrayList(requestList);
        filteredRequests = new FilteredList<>(maintenanceRequests);
        filteredRequests.setPredicate(request -> true); // Show all initially
    }

    private void createUI() {
        // Header
        HBox header = createHeader();
        content.getChildren().add(header);

        // Stats Cards
        HBox statsCards = createStatsCards();
        content.getChildren().add(statsCards);

        // Maintenance Requests List with Tabs
        VBox requestsSection = createRequestsSection();
        VBox.setVgrow(requestsSection, Priority.ALWAYS);
        content.getChildren().add(requestsSection);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        VBox titleSection = new VBox(8);
        Label titleLabel = new Label("Maintenance Tracker");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label subtitleLabel = new Label("Track and manage maintenance requests");
        subtitleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button newRequestBtn = new Button("New Request");
        newRequestBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        newRequestBtn.setOnAction(e -> showNewRequestDialog());

        header.getChildren().addAll(titleSection, spacer, newRequestBtn);
        return header;
    }

    private HBox createStatsCards() {
        HBox statsCards = new HBox(24);
        statsCards.setAlignment(Pos.CENTER_LEFT);

        // Calculate statistics
        long total = maintenanceRequests.size();
        long pending = maintenanceRequests.stream().filter(req -> "pending".equals(req.getStatus())).count();
        long inProgress = maintenanceRequests.stream().filter(req -> "in-progress".equals(req.getStatus())).count();
        long completed = maintenanceRequests.stream().filter(req -> "completed".equals(req.getStatus())).count();

        // Total Requests Card
        VBox totalCard = createStatCard("Total Requests", String.valueOf(total), "🔧", "#2C3E8C");
        
        // Pending Card
        VBox pendingCard = createStatCard("Pending", String.valueOf(pending), "⏰", "#F59E0B");
        
        // In Progress Card
        VBox progressCard = createStatCard("In Progress", String.valueOf(inProgress), "⚠️", "#3B82F6");
        
        // Completed Card
        VBox completedCard = createStatCard("Completed", String.valueOf(completed), "✅", "#10B981");

        statsCards.getChildren().addAll(totalCard, pendingCard, progressCard, completedCard);
        return statsCards;
    }

    private VBox createStatCard(String title, String value, String icon, String iconColor) {
        VBox card = new VBox();
        card.setPrefSize(220, 100);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(16);

        VBox textSection = new VBox(8);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 20; -fx-font-weight: bold;");

        textSection.getChildren().addAll(titleLabel, valueLabel);

        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 24;", iconColor));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        content.getChildren().addAll(textSection, spacer, iconLabel);
        card.getChildren().add(content);

        return card;
    }

    private VBox createRequestsSection() {
        VBox section = new VBox();
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        section.setPadding(new Insets(24));
        section.setSpacing(24);

        // Tabs
        tabPane = new TabPane();
        tabPane.setPrefHeight(50);
        tabPane.getStyleClass().add("floating");
        
        Tab allTab = new Tab("All Requests", new Label(""));
        Tab pendingTab = new Tab("Pending", new Label(""));
        Tab inProgressTab = new Tab("In Progress", new Label(""));
        Tab completedTab = new Tab("Completed", new Label(""));
        
        allTab.setClosable(false);
        pendingTab.setClosable(false);
        inProgressTab.setClosable(false);
        completedTab.setClosable(false);
        
        tabPane.getTabs().addAll(allTab, pendingTab, inProgressTab, completedTab);
        
        // Add listener for tab changes
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                filterRequestsByTabIndex(tabPane.getTabs().indexOf(selectedTab));
            }
        });

        // Requests List with ScrollPane
        requestsList = createRequestsList();
        ScrollPane scrollPane = new ScrollPane(requestsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-control-inner-background: white;");
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        section.getChildren().addAll(tabPane, scrollPane);
        
        return section;
    }

    private VBox createRequestsList() {
        VBox list = new VBox(16);
        list.setStyle("-fx-background-color: transparent;");

        for (MaintenanceRequest request : filteredRequests) {
            VBox requestCard = createRequestCard(request);
            list.getChildren().add(requestCard);
        }

        return list;
    }

    private VBox createRequestCard(MaintenanceRequest request) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E5E7EB; -fx-padding: 24;");
        card.setSpacing(16);

        // Header with ID, status, and priority
        HBox header = createRequestHeader(request);
        
        // Issue description
        Label issueLabel = new Label(request.getIssue());
        issueLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        // Details grid
        GridPane detailsGrid = createRequestDetailsGrid(request);
        
        // Description
        VBox descriptionSection = createDescriptionSection(request);
        
        // Assignment info (if assigned)
        if (request.getAssignedTo() != null && !request.getAssignedTo().isEmpty()) {
            HBox assignmentSection = createAssignmentSection(request);
            card.getChildren().add(assignmentSection);
        }

        card.getChildren().addAll(header, issueLabel, detailsGrid, descriptionSection);
        return card;
    }

    private HBox createRequestHeader(MaintenanceRequest request) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);

        // Request ID
        Label idLabel = new Label(request.getId());
        idLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 16; -fx-font-weight: bold;");

        // Status badge
        Label statusBadge = new Label(getStatusDisplayText(request.getStatus()));
        statusBadge.setStyle(getStatusBadgeStyle(request.getStatus()));

        // Priority badge
        Label priorityBadge = new Label(getPriorityDisplayText(request.getPriority()));
        priorityBadge.setStyle(getPriorityBadgeStyle(request.getPriority()));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Action buttons
        HBox actionButtons = createRequestActionButtons(request);

        header.getChildren().addAll(idLabel, statusBadge, priorityBadge, spacer, actionButtons);
        return header;
    }

    private HBox createRequestActionButtons(MaintenanceRequest request) {
        HBox buttons = new HBox(8);
        
        if ("pending".equals(request.getStatus())) {
            Button assignBtn = new Button("Assign");
            assignBtn.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 6 12; -fx-background-radius: 6;");
            assignBtn.setOnAction(e -> assignRequest(request));
            buttons.getChildren().add(assignBtn);
        } else if ("in-progress".equals(request.getStatus())) {
            Button completeBtn = new Button("Complete");
            completeBtn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 6 12; -fx-background-radius: 6;");
            completeBtn.setOnAction(e -> completeRequest(request));
            buttons.getChildren().add(completeBtn);
        }

        Button detailsBtn = new Button("Details");
        detailsBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-text-fill: #374151; -fx-font-size: 12; -fx-padding: 6 12; -fx-background-radius: 6;");
        detailsBtn.setOnAction(e -> showRequestDetails(request));
        buttons.getChildren().add(detailsBtn);

        return buttons;
    }

    private GridPane createRequestDetailsGrid(MaintenanceRequest request) {
        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(8);

        // Building
        addDetailToGrid(grid, "Building", request.getBuilding(), 0, 0);
        
        // Location
        addDetailToGrid(grid, "Location", request.getApartment(), 1, 0);
        
        // Requested By
        addDetailToGrid(grid, "Requested By", request.getRequestedBy(), 2, 0);
        
        // Date
        addDetailToGrid(grid, "Date", request.getDate(), 3, 0);
        
        // Estimated Cost
        addDetailToGrid(grid, "Est. Cost", String.format("%,d MAD", request.getEstimatedCost()), 4, 0);

        return grid;
    }

    private void addDetailToGrid(GridPane grid, String label, String value, int col, int row) {
        VBox detail = new VBox(4);
        
        Label detailLabel = new Label(label);
        detailLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");
        
        Label detailValue = new Label(value);
        detailValue.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-font-weight: bold;");
        
        detail.getChildren().addAll(detailLabel, detailValue);
        grid.add(detail, col, row);
    }

    private VBox createDescriptionSection(MaintenanceRequest request) {
        VBox section = new VBox(4);
        
        Label descLabel = new Label("Description");
        descLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");

        Label descText = new Label(request.getDescription());
        descText.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14; -fx-wrap-text: true;");

        section.getChildren().addAll(descLabel, descText);
        return section;
    }

    private HBox createAssignmentSection(MaintenanceRequest request) {
        HBox section = new HBox(16);
        section.setStyle("-fx-border-color: #E5E7EB; -fx-border-width: 1 0 0 0; -fx-padding: 16 0 0 0;");
        section.setAlignment(Pos.CENTER_LEFT);

        Label assignedLabel = new Label("Assigned to:");
        assignedLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        Label assignedTo = new Label(request.getAssignedTo());
        assignedTo.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-font-weight: bold;");

        section.getChildren().addAll(assignedLabel, assignedTo);

        if (request.getCompletedDate() != null && !request.getCompletedDate().isEmpty()) {
            Label separator = new Label("•");
            separator.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 14;");

            Label completedLabel = new Label("Completed: " + request.getCompletedDate());
            completedLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

            section.getChildren().addAll(separator, completedLabel);
        }

        return section;
    }

    private String getStatusDisplayText(String status) {
        switch (status) {
            case "pending": return "Pending";
            case "in-progress": return "In Progress";
            case "completed": return "Completed";
            default: return status;
        }
    }

    private String getStatusBadgeStyle(String status) {
        switch (status) {
            case "pending":
                return "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            case "in-progress":
                return "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            case "completed":
                return "-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            default:
                return "-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
        }
    }

    private String getPriorityDisplayText(String priority) {
        switch (priority) {
            case "high": return "High";
            case "medium": return "Medium";
            case "low": return "Low";
            default: return priority;
        }
    }

    private String getPriorityBadgeStyle(String priority) {
        switch (priority) {
            case "high":
                return "-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            case "medium":
                return "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            case "low":
                return "-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            default:
                return "-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
        }
    }

    private void filterRequestsByTabIndex(int tabIndex) {
        switch (tabIndex) {
            case 0: // All Requests
                filteredRequests.setPredicate(request -> true);
                break;
            case 1: // Pending
                filteredRequests.setPredicate(request -> "pending".equals(request.getStatus()));
                break;
            case 2: // In Progress
                filteredRequests.setPredicate(request -> "in-progress".equals(request.getStatus()));
                break;
            case 3: // Completed
                filteredRequests.setPredicate(request -> "completed".equals(request.getStatus()));
                break;
        }
        refreshRequestsList();
    }

    private void refreshRequestsList() {
        VBox newList = createRequestsList();
        int childIndex = content.getChildren().indexOf(requestsList);
        if (childIndex >= 0 && content.getChildren().size() > childIndex) {
            VBox section = (VBox) content.getChildren().get(childIndex);
            ScrollPane scrollPane = (ScrollPane) section.getChildren().get(1);
            scrollPane.setContent(newList);
        }
        requestsList = newList;
    }

    private void showNewRequestDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Maintenance Request");
        alert.setHeaderText("Create Maintenance Request");
        alert.setContentText("New request dialog would open here with form fields for building, location, issue, priority, etc.");
        alert.showAndWait();
    }

    private void assignRequest(MaintenanceRequest request) {
        request.setStatus("in-progress");
        request.setAssignedTo("Assigned Technician");
        refreshRequestsList();
    }

    private void completeRequest(MaintenanceRequest request) {
        request.setStatus("completed");
        request.setCompletedDate(java.time.LocalDate.now().toString());
        refreshRequestsList();
    }

    private void showRequestDetails(MaintenanceRequest request) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Maintenance Request Details");
        alert.setHeaderText("Request: " + request.getId());
        alert.setContentText("Detailed view of maintenance request would open here.");
        alert.showAndWait();
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
