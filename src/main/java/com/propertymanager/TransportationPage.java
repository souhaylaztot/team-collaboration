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

public class TransportationPage extends VBox {
    
    private String selectedTab = "workers";
    private String filterStatus = "all";
    private VBox workersContainer;
    private VBox materialsContainer;
    private VBox vehiclesContainer;
    private List<WorkerTransport> workerTransports;
    private List<MaterialTransport> materialTransports;
    private List<Vehicle> vehicles;
    
    public TransportationPage() {
        initData();
        initPage();
    }
    
    private void initData() {
        workerTransports = new ArrayList<>();
        materialTransports = new ArrayList<>();
        vehicles = new ArrayList<>();
        loadTransportationDataFromDatabase();
    }
    
    private void loadTransportationDataFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection()) {
            // Load vehicles
            String vehicleQuery = "SELECT id, vehicle_number, vehicle_type, capacity, status, last_maintenance_date, next_maintenance_date FROM vehicles";
            try (PreparedStatement stmt = conn.prepareStatement(vehicleQuery)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    vehicles.add(new Vehicle(
                        rs.getString("id"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_type"),
                        rs.getString("capacity"),
                        rs.getString("status"),
                        rs.getDate("last_maintenance_date").toString(),
                        rs.getDate("next_maintenance_date").toString()
                    ));
                }
            }
            
            // Load worker transports
            String workerQuery = "SELECT wt.id, v.vehicle_number, wt.driver_name, wt.worker_count, wt.pickup_point, wt.destination, wt.departure_time, wt.arrival_time, wt.status, wt.transport_date FROM worker_transports wt JOIN vehicles v ON wt.vehicle_id = v.id";
            try (PreparedStatement stmt = conn.prepareStatement(workerQuery)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    workerTransports.add(new WorkerTransport(
                        rs.getString("id"),
                        rs.getString("vehicle_number"),
                        rs.getString("driver_name"),
                        rs.getInt("worker_count"),
                        rs.getString("pickup_point"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        rs.getString("status"),
                        rs.getDate("transport_date").toString()
                    ));
                }
            }
            
            // Load material transports
            String materialQuery = "SELECT mt.id, v.vehicle_number, mt.driver_name, mt.material_type, mt.quantity, mt.unit, mt.origin, mt.destination, mt.departure_time, mt.estimated_arrival, mt.status, mt.transport_date, mt.urgency FROM material_transports mt JOIN vehicles v ON mt.vehicle_id = v.id";
            try (PreparedStatement stmt = conn.prepareStatement(materialQuery)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    materialTransports.add(new MaterialTransport(
                        rs.getString("id"),
                        rs.getString("vehicle_number"),
                        rs.getString("driver_name"),
                        rs.getString("material_type"),
                        rs.getString("quantity"),
                        rs.getString("unit"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getString("estimated_arrival"),
                        rs.getString("status"),
                        rs.getDate("transport_date").toString(),
                        rs.getString("urgency")
                    ));
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
        VBox tabsSection = createTabsSection();
        
        getChildren().addAll(header, statsRow, tabsSection);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Transportation Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Manage worker and material transportation logistics");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#6c757d"));
        
        titleBox.getChildren().addAll(title, subtitle);
        header.getChildren().add(titleBox);
        return header;
    }
    
    private HBox createStatsRow() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        int activeWorkerTransports = (int) workerTransports.stream().filter(t -> "in-transit".equals(t.status) || "scheduled".equals(t.status)).count();
        int activeMaterialTransports = (int) materialTransports.stream().filter(t -> !"delivered".equals(t.status)).count();
        int availableVehicles = (int) vehicles.stream().filter(v -> "available".equals(v.status)).count();
        int criticalDeliveries = (int) materialTransports.stream().filter(t -> "critical".equals(t.urgency) && !"delivered".equals(t.status)).count();
        
        statsRow.getChildren().addAll(
            createStatCard("Worker Transports", String.valueOf(activeWorkerTransports) + " Active", "👥", "#2C3E8C"),
            createStatCard("Material Deliveries", String.valueOf(activeMaterialTransports) + " Active", "📦", "#4FD1C5"),
            createStatCard("Available Vehicles", availableVehicles + " of " + vehicles.size(), "🚛", "#F5C542"),
            createStatCard("Critical Deliveries", String.valueOf(criticalDeliveries) + " Pending", "⚠️", "#EF4444")
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
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
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
    
    private VBox createTabsSection() {
        VBox container = new VBox(20);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Tabs
        HBox tabsBox = createTabsBox();
        
        // Content area
        VBox contentArea = new VBox(20);
        
        // Search and filter bar
        HBox searchBar = createSearchBar();
        
        // Initialize containers
        workersContainer = new VBox(15);
        materialsContainer = new VBox(15);
        vehiclesContainer = new VBox(15);
        
        // Create scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        // Set initial content
        Label initialLabel = new Label("Transportation Management");
        initialLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        VBox initialContent = new VBox(initialLabel);
        initialContent.setPadding(new Insets(50));
        initialContent.setAlignment(Pos.CENTER);
        scrollPane.setContent(initialContent);
        
        contentArea.getChildren().addAll(searchBar, scrollPane);
        container.getChildren().addAll(tabsBox, contentArea);
        
        // Update content after everything is set up
        updateContent();
        
        return container;
    }
    
    private HBox createTabsBox() {
        HBox tabsBox = new HBox(10);
        tabsBox.setAlignment(Pos.CENTER_LEFT);
        
        Button workersTab = createTabButton("👥 Worker Transport", "workers");
        Button materialsTab = createTabButton("📦 Material Transport", "materials");
        Button vehiclesTab = createTabButton("🚛 Vehicle Fleet", "vehicles");
        
        tabsBox.getChildren().addAll(workersTab, materialsTab, vehiclesTab);
        return tabsBox;
    }
    
    private Button createTabButton(String text, String tabValue) {
        Button button = new Button(text);
        button.setPadding(new Insets(12, 20, 12, 20));
        
        if (selectedTab.equals(tabValue)) {
            button.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand;");
        } else {
            button.setStyle("-fx-background-color: #f1f3f4; -fx-text-fill: #666; -fx-background-radius: 8; -fx-cursor: hand;");
        }
        
        button.setOnAction(e -> {
            System.out.println("تم النقر على تبويب: " + text);
            selectedTab = tabValue;
            updateTabStyles();
            updateContent();
        });
        
        button.setOnMouseEntered(e -> {
            if (!selectedTab.equals(tabValue)) {
                button.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 8; -fx-cursor: hand;");
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!selectedTab.equals(tabValue)) {
                button.setStyle("-fx-background-color: #f1f3f4; -fx-text-fill: #666; -fx-background-radius: 8;");
            }
        });
        
        return button;
    }
    
    private void updateTabStyles() {
        VBox container = (VBox) getChildren().get(2);
        HBox newTabsBox = createTabsBox();
        container.getChildren().set(0, newTabsBox);
    }
    
    private HBox createSearchBar() {
        HBox searchBar = new HBox(15);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search by vehicle, driver, or location...");
        searchField.setPrefWidth(400);
        
        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "Scheduled", "In Transit", "Completed", "Delayed");
        statusFilter.setValue("All Status");
        statusFilter.setPrefWidth(150);
        
        Button addButton = new Button("➕ Add New");
        addButton.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        
        addButton.setOnMouseEntered(e -> {
            addButton.setStyle("-fx-background-color: linear-gradient(to right, #1a2a5e, #3bb3a8); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        });
        
        addButton.setOnMouseExited(e -> {
            addButton.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        });
        
        addButton.setOnAction(e -> {
            System.out.println("تم النقر على زر إضافة جديد");
            showAddDialog();
        });
        
        searchBar.getChildren().addAll(searchField, statusFilter, addButton);
        return searchBar;
    }
    
    private void updateContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Transportation Management - " + selectedTab.toUpperCase());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        switch (selectedTab) {
            case "workers":
                updateWorkersContent();
                mainContent.getChildren().addAll(titleLabel, workersContainer);
                break;
            case "materials":
                updateMaterialsContent();
                mainContent.getChildren().addAll(titleLabel, materialsContainer);
                break;
            case "vehicles":
                updateVehiclesContent();
                mainContent.getChildren().addAll(titleLabel, vehiclesContainer);
                break;
        }
        
        try {
            if (getChildren().size() > 2) {
                VBox container = (VBox) getChildren().get(2);
                if (container.getChildren().size() > 1) {
                    VBox contentArea = (VBox) container.getChildren().get(1);
                    if (contentArea.getChildren().size() > 1) {
                        ScrollPane scrollPane = (ScrollPane) contentArea.getChildren().get(1);
                        scrollPane.setContent(mainContent);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating content: " + e.getMessage());
        }
    }
    
    private void updateWorkersContent() {
        workersContainer.getChildren().clear();
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        
        int col = 0, row = 0;
        for (WorkerTransport transport : workerTransports) {
            VBox card = createWorkerTransportCard(transport);
            grid.add(card, col, row);
            
            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }
        
        workersContainer.getChildren().add(grid);
    }
    
    private VBox createWorkerTransportCard(WorkerTransport transport) {
        VBox card = new VBox(15);
        card.setPrefWidth(400);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 12; -fx-border-color: #e9ecef; -fx-border-radius: 12;");
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label vehicleLabel = new Label(transport.vehicleNumber);
        vehicleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vehicleLabel.setTextFill(Color.web("#2c3e50"));
        
        Label driverLabel = new Label(transport.driverName);
        driverLabel.setFont(Font.font("Arial", 12));
        driverLabel.setTextFill(Color.GRAY);
        
        titleBox.getChildren().addAll(vehicleLabel, driverLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label statusBadge = createStatusBadge(transport.status);
        
        header.getChildren().addAll(titleBox, spacer, statusBadge);
        
        // Details
        VBox details = new VBox(8);
        details.getChildren().addAll(
            createDetailRow("👥", transport.workers + " Workers"),
            createDetailRow("📍", transport.pickupPoint + " → " + transport.destination),
            createDetailRow("⏰", transport.departureTime + " - " + transport.arrivalTime),
            createDetailRow("📅", transport.date)
        );
        
        // Actions
        HBox actions = new HBox(10);
        Button editBtn = new Button("✏️ Edit");
        editBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8 15;");
        
        Button deleteBtn = new Button("🗑️");
        deleteBtn.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #d32f2f; -fx-background-radius: 6; -fx-padding: 8 10;");
        
        actions.getChildren().addAll(editBtn, deleteBtn);
        
        card.getChildren().addAll(header, details, actions);
        return card;
    }
    
    private void updateMaterialsContent() {
        materialsContainer.getChildren().clear();
        
        for (MaterialTransport transport : materialTransports) {
            VBox card = createMaterialTransportCard(transport);
            materialsContainer.getChildren().add(card);
        }
    }
    
    private VBox createMaterialTransportCard(MaterialTransport transport) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 12; -fx-border-color: #e9ecef; -fx-border-radius: 12;");
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label materialLabel = new Label(transport.materialType);
        materialLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        materialLabel.setTextFill(Color.web("#2c3e50"));
        
        HBox badgesBox = new HBox(10);
        Label urgencyBadge = createUrgencyBadge(transport.urgency);
        Label statusBadge = createStatusBadge(transport.status);
        badgesBox.getChildren().addAll(urgencyBadge, statusBadge);
        
        titleBox.getChildren().addAll(materialLabel, badgesBox);
        
        header.getChildren().add(titleBox);
        
        // Details grid
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(30);
        detailsGrid.setVgap(8);
        
        detailsGrid.add(createDetailRow("📦", transport.quantity + " " + transport.unit), 0, 0);
        detailsGrid.add(createDetailRow("🚛", transport.vehicleNumber + " - " + transport.driverName), 1, 0);
        detailsGrid.add(createDetailRow("📍", transport.origin + " → " + transport.destination), 0, 1);
        detailsGrid.add(createDetailRow("⏰", transport.departureTime + " - " + transport.estimatedArrival), 1, 1);
        
        // Actions
        HBox actions = new HBox(10);
        Button editBtn = new Button("✏️ Edit");
        editBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8 15;");
        
        Button deleteBtn = new Button("🗑️");
        deleteBtn.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #d32f2f; -fx-background-radius: 6; -fx-padding: 8 10;");
        
        actions.getChildren().addAll(editBtn, deleteBtn);
        
        card.getChildren().addAll(header, detailsGrid, actions);
        return card;
    }
    
    private void updateVehiclesContent() {
        vehiclesContainer.getChildren().clear();
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        
        int col = 0, row = 0;
        for (Vehicle vehicle : vehicles) {
            VBox card = createVehicleCard(vehicle);
            grid.add(card, col, row);
            
            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }
        
        vehiclesContainer.getChildren().add(grid);
    }
    
    private VBox createVehicleCard(Vehicle vehicle) {
        VBox card = new VBox(15);
        card.setPrefWidth(400);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 12; -fx-border-color: #e9ecef; -fx-border-radius: 12;");
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label vehicleLabel = new Label(vehicle.vehicleNumber);
        vehicleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vehicleLabel.setTextFill(Color.web("#2c3e50"));
        
        Label typeLabel = new Label(vehicle.type.replace("-", " ").toUpperCase());
        typeLabel.setFont(Font.font("Arial", 12));
        typeLabel.setTextFill(Color.GRAY);
        
        titleBox.getChildren().addAll(vehicleLabel, typeLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label statusBadge = createStatusBadge(vehicle.status);
        
        header.getChildren().addAll(titleBox, spacer, statusBadge);
        
        // Details
        VBox details = new VBox(8);
        details.getChildren().addAll(
            createDetailRow("📦", "Capacity: " + vehicle.capacity),
            createDetailRow("🔧", "Last Maintenance: " + vehicle.lastMaintenance),
            createDetailRow("📅", "Next Maintenance: " + vehicle.nextMaintenance)
        );
        
        // Actions
        HBox actions = new HBox(10);
        Button editBtn = new Button("✏️ Edit");
        editBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8 15;");
        
        if ("available".equals(vehicle.status)) {
            Button assignBtn = new Button("📋 Assign");
            assignBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 15;");
            actions.getChildren().add(assignBtn);
        }
        
        Button deleteBtn = new Button("🗑️");
        deleteBtn.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #d32f2f; -fx-background-radius: 6; -fx-padding: 8 10;");
        
        actions.getChildren().addAll(editBtn, deleteBtn);
        
        card.getChildren().addAll(header, details, actions);
        return card;
    }
    
    private HBox createDetailRow(String icon, String text) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(14));
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Arial", 12));
        textLabel.setTextFill(Color.web("#495057"));
        
        row.getChildren().addAll(iconLabel, textLabel);
        return row;
    }
    
    private Label createStatusBadge(String status) {
        Label badge = new Label();
        badge.setPadding(new Insets(4, 8, 4, 8));
        badge.setStyle("-fx-background-radius: 12; -fx-font-size: 11px; -fx-font-weight: bold;");
        
        switch (status) {
            case "completed":
            case "delivered":
                badge.setText("✅ " + status.toUpperCase());
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32;");
                break;
            case "in-transit":
            case "loading":
                badge.setText("🚛 " + status.toUpperCase().replace("-", " "));
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e3f2fd; -fx-text-fill: #1976d2;");
                break;
            case "scheduled":
                badge.setText("⏰ SCHEDULED");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #f3e5f5; -fx-text-fill: #7b1fa2;");
                break;
            case "delayed":
                badge.setText("⚠️ DELAYED");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #ffebee; -fx-text-fill: #d32f2f;");
                break;
            case "available":
                badge.setText("✅ AVAILABLE");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32;");
                break;
            case "in-use":
                badge.setText("🚛 IN USE");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e3f2fd; -fx-text-fill: #1976d2;");
                break;
            case "maintenance":
                badge.setText("🔧 MAINTENANCE");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #fff3e0; -fx-text-fill: #f57c00;");
                break;
        }
        
        return badge;
    }
    
    private Label createUrgencyBadge(String urgency) {
        Label badge = new Label();
        badge.setPadding(new Insets(4, 8, 4, 8));
        badge.setStyle("-fx-background-radius: 12; -fx-font-size: 11px; -fx-font-weight: bold;");
        
        switch (urgency) {
            case "critical":
                badge.setText("🔴 CRITICAL");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #ffebee; -fx-text-fill: #d32f2f;");
                break;
            case "urgent":
                badge.setText("🟡 URGENT");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #fff3e0; -fx-text-fill: #f57c00;");
                break;
            case "normal":
                badge.setText("🟢 NORMAL");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32;");
                break;
        }
        
        return badge;
    }
    
    private void showAddDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New " + selectedTab.substring(0, 1).toUpperCase() + selectedTab.substring(1));
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Add New " + selectedTab.replace("s", "").toUpperCase());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Simple form based on selected tab
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        switch (selectedTab) {
            case "workers":
                form.add(new Label("Vehicle Number:"), 0, 0);
                form.add(new TextField(), 1, 0);
                form.add(new Label("Driver Name:"), 0, 1);
                form.add(new TextField(), 1, 1);
                form.add(new Label("Workers Count:"), 0, 2);
                form.add(new TextField(), 1, 2);
                break;
            case "materials":
                form.add(new Label("Material Type:"), 0, 0);
                form.add(new TextField(), 1, 0);
                form.add(new Label("Quantity:"), 0, 1);
                form.add(new TextField(), 1, 1);
                form.add(new Label("Vehicle Number:"), 0, 2);
                form.add(new TextField(), 1, 2);
                break;
            case "vehicles":
                form.add(new Label("Vehicle Number:"), 0, 0);
                form.add(new TextField(), 1, 0);
                form.add(new Label("Type:"), 0, 1);
                ComboBox<String> typeCombo = new ComboBox<>();
                typeCombo.getItems().addAll("Passenger", "Cargo Truck", "Flatbed", "Mixer");
                form.add(typeCombo, 1, 1);
                form.add(new Label("Capacity:"), 0, 2);
                form.add(new TextField(), 1, 2);
                break;
        }
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20;");
        saveBtn.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(titleLabel, form, buttons);
        
        Scene scene = new Scene(content, 400, 300);
        dialog.setScene(scene);
        dialog.show();
    }
    
    // Data classes
    private static class WorkerTransport {
        String id, vehicleNumber, driverName, pickupPoint, destination, departureTime, arrivalTime, status, date;
        int workers;
        
        WorkerTransport(String id, String vehicleNumber, String driverName, int workers, String pickupPoint, 
                       String destination, String departureTime, String arrivalTime, String status, String date) {
            this.id = id;
            this.vehicleNumber = vehicleNumber;
            this.driverName = driverName;
            this.workers = workers;
            this.pickupPoint = pickupPoint;
            this.destination = destination;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.status = status;
            this.date = date;
        }
    }
    
    private static class MaterialTransport {
        String id, vehicleNumber, driverName, materialType, quantity, unit, origin, destination;
        String departureTime, estimatedArrival, status, date, urgency;
        
        MaterialTransport(String id, String vehicleNumber, String driverName, String materialType, String quantity, 
                         String unit, String origin, String destination, String departureTime, String estimatedArrival, 
                         String status, String date, String urgency) {
            this.id = id;
            this.vehicleNumber = vehicleNumber;
            this.driverName = driverName;
            this.materialType = materialType;
            this.quantity = quantity;
            this.unit = unit;
            this.origin = origin;
            this.destination = destination;
            this.departureTime = departureTime;
            this.estimatedArrival = estimatedArrival;
            this.status = status;
            this.date = date;
            this.urgency = urgency;
        }
    }
    
    private static class Vehicle {
        String id, vehicleNumber, type, capacity, status, lastMaintenance, nextMaintenance;
        
        Vehicle(String id, String vehicleNumber, String type, String capacity, String status, 
                String lastMaintenance, String nextMaintenance) {
            this.id = id;
            this.vehicleNumber = vehicleNumber;
            this.type = type;
            this.capacity = capacity;
            this.status = status;
            this.lastMaintenance = lastMaintenance;
            this.nextMaintenance = nextMaintenance;
        }
    }
}