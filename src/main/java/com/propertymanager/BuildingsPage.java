package com.propertymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuildingsPage extends ScrollPane {
    
    private TextField searchField;
    private TilePane buildingsGrid;
    private VBox apartmentView;
    private Building selectedBuilding;
    private List<Building> buildings;
    private ObservableList<Building> filteredBuildings;
    private VBox mainContent;
    
    public BuildingsPage() {
        initData();
        initPage();
    }
    
    private void initData() {
        buildings = new ArrayList<>();
        loadBuildingsFromDatabase();
        filteredBuildings = FXCollections.observableArrayList(buildings);
    }
    
    private void loadBuildingsFromDatabase() {
        try {
            // Use the new DAO to load buildings
            buildings.clear();
            buildings.addAll(BuildingDAO.getAllBuildings());
            
            // Load apartments for each building
            for (Building building : buildings) {
                loadApartmentsForBuilding(building);
            }
            
            System.out.println("✅ Successfully loaded " + buildings.size() + " buildings with apartments");
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Database Error", "Failed to load buildings from database: " + e.getMessage());
        }
    }
    
    private void loadApartmentsForBuilding(Building building) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String apartmentQuery = "SELECT a.apartment_number, a.floor, a.size, a.bedrooms, a.price, a.status, " +
                                   "COALESCE(b.name, '-') as buyer_name " +
                                   "FROM apartments a " +
                                   "LEFT JOIN property_purchases pp ON a.id = pp.apartment_id " +
                                   "LEFT JOIN buyers b ON pp.buyer_id = b.id " +
                                   "WHERE a.building_id = ? ORDER BY a.floor, a.apartment_number";
            
            try (PreparedStatement stmt = conn.prepareStatement(apartmentQuery)) {
                stmt.setInt(1, building.id);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Apartment apartment = new Apartment(
                        rs.getString("apartment_number"),
                        rs.getInt("floor"),
                        rs.getString("size"),
                        rs.getInt("bedrooms"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getString("buyer_name")
                    );
                    building.apartments.add(apartment);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error loading apartments for building " + building.name + ": " + e.getMessage());
        }
    }
    
    private void initPage() {
        setFitToWidth(true);
        setFitToHeight(false);
        setStyle("-fx-background-color: #f8f9fa;");
        
        mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));
        VBox.setVgrow(mainContent, Priority.NEVER);
        
        // Header
        HBox header = createHeader();
        
        // Search
        HBox searchBox = createSearchBox();
        
        // Buildings grid
        buildingsGrid = new TilePane();
        buildingsGrid.setHgap(20);
        buildingsGrid.setVgap(20);
        buildingsGrid.setPrefColumns(3);
        
        // Apartment view (initially hidden)
        apartmentView = new VBox(20);
        apartmentView.setVisible(false);
        
        updateBuildingsGrid();
        
        mainContent.getChildren().addAll(header, searchBox, buildingsGrid, apartmentView);
        setContent(mainContent);
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Building Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Manage all buildings and apartments");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.GRAY);
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Button addBtn = new Button("+ Add Building");
        addBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;");
        addBtn.setOnAction(e -> showAddBuildingDialog());
        
        header.getChildren().addAll(titleBox, addBtn);
        HBox.setHgrow(titleBox, Priority.ALWAYS);
        
        return header;
    }
    
    private HBox createSearchBox() {
        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        searchField = new TextField();
        searchField.setPromptText("🔍 Search buildings by name or location...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #e9ecef; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");
        
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterBuildings(newVal));
        
        searchBox.getChildren().add(searchField);
        return searchBox;
    }
    
    private void filterBuildings(String searchTerm) {
        filteredBuildings.clear();
        if (searchTerm == null || searchTerm.isEmpty()) {
            filteredBuildings.addAll(buildings);
        } else {
            for (Building building : buildings) {
                if (building.name.toLowerCase().contains(searchTerm.toLowerCase()) ||
                    building.location.toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredBuildings.add(building);
                }
            }
        }
        updateBuildingsGrid();
    }
    
    private void updateBuildingsGrid() {
        buildingsGrid.getChildren().clear();
        
        for (Building building : filteredBuildings) {
            VBox card = createBuildingCard(building);
            buildingsGrid.getChildren().add(card);
        }
    }
    
    private VBox createBuildingCard(Building building) {
        VBox card = new VBox(15);
        card.setPrefWidth(350);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 12; -fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label icon = new Label("🏢");
        icon.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8; -fx-font-size: 16px;");
        
        VBox titleBox = new VBox(2);
        Label name = new Label(building.name);
        name.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        name.setTextFill(Color.web("#2c3e50"));
        
        Label status = new Label(building.status);
        status.setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 4; -fx-padding: 2 8;");
        
        titleBox.getChildren().addAll(name, status);
        header.getChildren().addAll(icon, titleBox);
        
        // Location
        Label location = new Label("📍 " + building.location);
        location.setFont(Font.font("Arial", 12));
        location.setTextFill(Color.web("#6c757d"));
        location.setWrapText(true);
        
        // Stats
        HBox stats = new HBox(20);
        VBox floorsBox = new VBox(2);
        Label floorsLabel = new Label("Floors");
        floorsLabel.setFont(Font.font("Arial", 10));
        floorsLabel.setTextFill(Color.GRAY);
        Label floorsValue = new Label("🏗️ " + building.floors);
        floorsValue.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        floorsBox.getChildren().addAll(floorsLabel, floorsValue);
        
        VBox aptsBox = new VBox(2);
        Label aptsLabel = new Label("Apartments");
        aptsLabel.setFont(Font.font("Arial", 10));
        aptsLabel.setTextFill(Color.GRAY);
        Label aptsValue = new Label(String.valueOf(building.totalApartments));
        aptsValue.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        aptsBox.getChildren().addAll(aptsLabel, aptsValue);
        
        stats.getChildren().addAll(floorsBox, aptsBox);
        
        // Occupancy
        VBox occupancyBox = new VBox(5);
        HBox occupancyHeader = new HBox(10);
        occupancyHeader.setAlignment(Pos.CENTER_LEFT);
        Label occupancyLabel = new Label("Occupancy");
        occupancyLabel.setFont(Font.font("Arial", 12));
        
        Label occupancyValue = new Label(building.occupiedApartments + "/" + building.totalApartments + " (" + (int)building.getOccupancyPercentage() + "%)");
        occupancyValue.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        occupancyHeader.getChildren().addAll(occupancyLabel, occupancyValue);
        HBox.setHgrow(occupancyLabel, Priority.ALWAYS);
        
        ProgressBar progressBar = new ProgressBar(building.getOccupancyPercentage() / 100.0);
        progressBar.setPrefWidth(310);
        progressBar.setStyle("-fx-accent: linear-gradient(135deg, #2C3E8C, #4FD1C5);");
        
        occupancyBox.getChildren().addAll(occupancyHeader, progressBar);
        
        // Buttons
        HBox buttons = new HBox(10);
        Button viewBtn = new Button("👁️ View Details");
        viewBtn.setStyle("-fx-background-color: #4FD1C5; -fx-text-fill: white; -fx-border-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        viewBtn.setOnMouseEntered(e -> viewBtn.setStyle("-fx-background-color: #38B2AC; -fx-text-fill: white; -fx-border-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;"));
        viewBtn.setOnMouseExited(e -> viewBtn.setStyle("-fx-background-color: #4FD1C5; -fx-text-fill: white; -fx-border-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;"));
        viewBtn.setOnAction(e -> showBuildingDetails(building));
        
        Button editBtn = new Button("✏️");
        editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #6c757d; -fx-border-radius: 4; -fx-padding: 6 8;");
        editBtn.setOnAction(e -> showEditBuildingDialog(building));
        
        Button deleteBtn = new Button("🗑️");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #dc3545; -fx-text-fill: #dc3545; -fx-border-radius: 4; -fx-padding: 6 8;");
        deleteBtn.setOnAction(e -> showDeleteBuildingConfirmation(building));
        
        buttons.getChildren().addAll(viewBtn, editBtn, deleteBtn);
        
        card.getChildren().addAll(header, location, stats, occupancyBox, buttons);
        return card;
    }
    
    private void showBuildingDetails(Building building) {
        Stage detailsStage = new Stage();
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.setTitle("🏢 " + building.name + " - Building Details");
        detailsStage.setMaximized(true);
        
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #f8fafc;");
        
        // Header with building info
        VBox header = createBuildingDetailsHeader(building);
        
        // Tabs for different views
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");
        
        // Overview Tab
        Tab overviewTab = new Tab("📊 Overview");
        overviewTab.setClosable(false);
        overviewTab.setContent(createOverviewTab(building));
        
        // Apartments Tab
        Tab apartmentsTab = new Tab("🏠 Apartments (" + building.apartments.size() + ")");
        apartmentsTab.setClosable(false);
        apartmentsTab.setContent(createApartmentsTab(building));
        
        // Financial Tab
        Tab financialTab = new Tab("💰 Financial");
        financialTab.setClosable(false);
        financialTab.setContent(createFinancialTab(building));
        
        tabPane.getTabs().addAll(overviewTab, apartmentsTab, financialTab);
        
        root.getChildren().addAll(header, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        
        Scene scene = new Scene(root, 1200, 800);
        detailsStage.setScene(scene);
        detailsStage.showAndWait();
    }
    
    private VBox createBuildingDetailsHeader(Building building) {
        VBox header = new VBox(20);
        header.setPadding(new Insets(30));
        header.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white;");
        
        HBox titleRow = new HBox(20);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        
        Label icon = new Label("🏢");
        icon.setStyle("-fx-font-size: 48px;");
        
        VBox titleBox = new VBox(5);
        Label name = new Label(building.name);
        name.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        name.setTextFill(Color.WHITE);
        
        Label location = new Label("📍 " + building.location);
        location.setFont(Font.font("Arial", 16));
        location.setTextFill(Color.web("#E2E8F0"));
        
        titleBox.getChildren().addAll(name, location);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        VBox statusBox = new VBox(5);
        statusBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label status = new Label(building.status);
        status.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 8 16; -fx-font-weight: bold;");
        
        statusBox.getChildren().add(status);
        
        titleRow.getChildren().addAll(icon, titleBox, spacer, statusBox);
        
        // Quick stats
        HBox statsRow = new HBox(40);
        statsRow.setAlignment(Pos.CENTER_LEFT);
        
        statsRow.getChildren().addAll(
            createQuickStat("🏗️", "Floors", String.valueOf(building.floors)),
            createQuickStat("🏠", "Total Units", String.valueOf(building.totalApartments)),
            createQuickStat("👥", "Occupied", String.valueOf(building.occupiedApartments)),
            createQuickStat("📊", "Occupancy", String.format("%.1f%%", building.getOccupancyPercentage()))
        );
        
        header.getChildren().addAll(titleRow, statsRow);
        return header;
    }
    
    private VBox createQuickStat(String icon, String label, String value) {
        VBox stat = new VBox(5);
        stat.setAlignment(Pos.CENTER);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        valueLabel.setTextFill(Color.WHITE);
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", 12));
        labelText.setTextFill(Color.web("#E2E8F0"));
        
        stat.getChildren().addAll(iconLabel, valueLabel, labelText);
        return stat;
    }
    
    private ScrollPane createOverviewTab(Building building) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        
        VBox content = new VBox(30);
        content.setPadding(new Insets(30));
        
        // Building Information Card
        VBox infoCard = createBuildingInfoCard(building);
        
        // Occupancy Analytics
        HBox analyticsRow = new HBox(30);
        VBox occupancyChart = createOccupancyAnalytics(building);
        VBox floorBreakdown = createFloorBreakdown(building);
        
        analyticsRow.getChildren().addAll(occupancyChart, floorBreakdown);
        
        content.getChildren().addAll(infoCard, analyticsRow);
        scroll.setContent(content);
        
        return scroll;
    }
    
    private VBox createBuildingInfoCard(Building building) {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");
        
        Label title = new Label("🏢 Building Information");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        GridPane info = new GridPane();
        info.setHgap(30);
        info.setVgap(15);
        
        info.add(createInfoItem("🏗️", "Total Floors", String.valueOf(building.floors)), 0, 0);
        info.add(createInfoItem("🏠", "Total Apartments", String.valueOf(building.totalApartments)), 1, 0);
        info.add(createInfoItem("👥", "Occupied Units", String.valueOf(building.occupiedApartments)), 0, 1);
        info.add(createInfoItem("🏃", "Available Units", String.valueOf(building.totalApartments - building.occupiedApartments)), 1, 1);
        info.add(createInfoItem("📊", "Occupancy Rate", String.format("%.1f%%", building.getOccupancyPercentage())), 0, 2);
        
        card.getChildren().addAll(title, info);
        return card;
    }
    
    private HBox createInfoItem(String icon, String label, String value) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");
        
        VBox textBox = new VBox(2);
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", 12));
        labelText.setTextFill(Color.GRAY);
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        textBox.getChildren().addAll(labelText, valueText);
        item.getChildren().addAll(iconLabel, textBox);
        
        return item;
    }
    
    private ScrollPane createApartmentsTab(Building building) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        
        // Header with controls
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("🏠 Apartment Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addAptBtn = new Button("➕ Add Apartment");
        addAptBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20;");
        addAptBtn.setOnAction(e -> showAddApartmentDialog(building));
        
        header.getChildren().addAll(title, spacer, addAptBtn);
        
        // Enhanced apartment table
        TableView<Apartment> table = createEnhancedApartmentTable(building);
        
        content.getChildren().addAll(header, table);
        scroll.setContent(content);
        
        return scroll;
    }
    
    private TableView<Apartment> createEnhancedApartmentTable(Building building) {
        TableView<Apartment> table = new TableView<>();
        table.setPrefHeight(500);
        
        TableColumn<Apartment, String> numberCol = new TableColumn<>("Apt #");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberCol.setPrefWidth(80);
        
        TableColumn<Apartment, Integer> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));
        floorCol.setPrefWidth(80);
        
        TableColumn<Apartment, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setPrefWidth(120);
        
        TableColumn<Apartment, Integer> bedroomsCol = new TableColumn<>("Bedrooms");
        bedroomsCol.setCellValueFactory(new PropertyValueFactory<>("bedrooms"));
        bedroomsCol.setPrefWidth(100);
        
        TableColumn<Apartment, Double> priceCol = new TableColumn<>("Price (MAD)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(150);
        priceCol.setCellFactory(col -> new TableCell<Apartment, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("💰 %.0f MAD", price));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #10b981;");
                }
            }
        });
        
        TableColumn<Apartment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);
        statusCol.setCellFactory(col -> new TableCell<Apartment, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "Sold":
                            setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 8; -fx-font-weight: bold;");
                            break;
                        case "Available":
                            setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404; -fx-background-radius: 8; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #6c757d; -fx-background-radius: 8;");
                    }
                }
            }
        });
        
        TableColumn<Apartment, String> buyerCol = new TableColumn<>("Buyer/Tenant");
        buyerCol.setCellValueFactory(new PropertyValueFactory<>("buyer"));
        buyerCol.setPrefWidth(180);
        
        table.getColumns().addAll(numberCol, floorCol, sizeCol, bedroomsCol, priceCol, statusCol, buyerCol);
        table.getItems().addAll(building.apartments);
        
        return table;
    }
    
    private VBox createOccupancyAnalytics(Building building) {
        VBox card = new VBox(15);
        card.setPrefWidth(400);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");
        
        Label title = new Label("📊 Occupancy Analytics");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        VBox progress = new VBox(15);
        
        progress.getChildren().addAll(
            createProgressItem("Sold", building.occupiedApartments, building.totalApartments, "#4FD1C5"),
            createProgressItem("Available", building.totalApartments - building.occupiedApartments, building.totalApartments, "#F5C542"),
            createProgressItem("Occupancy Rate", (int)building.getOccupancyPercentage(), 100, "#2C3E8C")
        );
        
        card.getChildren().addAll(title, progress);
        return card;
    }
    
    private HBox createProgressItem(String label, int current, int total, String color) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        
        VBox textBox = new VBox(2);
        textBox.setPrefWidth(120);
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        Label valueText = new Label(current + "/" + total);
        valueText.setFont(Font.font("Arial", 10));
        valueText.setTextFill(Color.GRAY);
        
        textBox.getChildren().addAll(labelText, valueText);
        
        ProgressBar progress = new ProgressBar((double)current / total);
        progress.setPrefWidth(200);
        progress.setStyle("-fx-accent: " + color + ";");
        
        Label percentage = new Label(String.format("%.1f%%", (double)current / total * 100));
        percentage.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        item.getChildren().addAll(textBox, progress, percentage);
        return item;
    }
    
    private VBox createFloorBreakdown(Building building) {
        VBox card = new VBox(15);
        card.setPrefWidth(400);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");
        
        Label title = new Label("🏗️ Floor Breakdown");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(200);
        scroll.setStyle("-fx-background-color: transparent;");
        
        VBox floorList = new VBox(10);
        
        for (int floor = 1; floor <= building.floors; floor++) {
            final int currentFloor = floor;
            long floorApartments = building.apartments.stream()
                .filter(apt -> apt.floor == currentFloor)
                .count();
            
            long soldOnFloor = building.apartments.stream()
                .filter(apt -> apt.floor == currentFloor && "Sold".equals(apt.status))
                .count();
            
            HBox floorItem = new HBox(15);
            floorItem.setAlignment(Pos.CENTER_LEFT);
            floorItem.setPadding(new Insets(10));
            floorItem.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
            
            Label floorLabel = new Label("Floor " + floor);
            floorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            floorLabel.setPrefWidth(80);
            
            ProgressBar progress = new ProgressBar(floorApartments > 0 ? (double)soldOnFloor / floorApartments : 0);
            progress.setPrefWidth(150);
            progress.setStyle("-fx-accent: #4FD1C5;");
            
            Label stats = new Label(soldOnFloor + "/" + floorApartments);
            stats.setFont(Font.font("Arial", 10));
            stats.setTextFill(Color.GRAY);
            
            floorItem.getChildren().addAll(floorLabel, progress, stats);
            floorList.getChildren().add(floorItem);
        }
        
        scroll.setContent(floorList);
        card.getChildren().addAll(title, scroll);
        return card;
    }
    
    private ScrollPane createFinancialTab(Building building) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        
        VBox content = new VBox(30);
        content.setPadding(new Insets(30));
        
        Label title = new Label("💰 Financial Overview");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        // Revenue summary
        VBox revenueCard = new VBox(15);
        revenueCard.setPadding(new Insets(25));
        revenueCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");
        
        Label revenueTitle = new Label("💰 Revenue Summary");
        revenueTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        double totalRevenue = building.apartments.stream()
            .filter(apt -> "Sold".equals(apt.status))
            .mapToDouble(apt -> apt.price)
            .sum();
        
        Label totalRevenueLabel = new Label(String.format("Total Revenue: %.0f MAD", totalRevenue));
        totalRevenueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalRevenueLabel.setTextFill(Color.web("#10b981"));
        
        revenueCard.getChildren().addAll(revenueTitle, totalRevenueLabel);
        
        content.getChildren().addAll(title, revenueCard);
        scroll.setContent(content);
        
        return scroll;
    }
    
    private void showApartments(Building building) {
        showBuildingDetails(building);
    }
    
    private TableView<Apartment> createApartmentTable() {
        TableView<Apartment> table = new TableView<>();
        
        TableColumn<Apartment, String> numberCol = new TableColumn<>("Apt #");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberCol.setPrefWidth(80);
        
        TableColumn<Apartment, Integer> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));
        floorCol.setPrefWidth(80);
        
        TableColumn<Apartment, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setPrefWidth(100);
        
        TableColumn<Apartment, Integer> bedroomsCol = new TableColumn<>("Bedrooms");
        bedroomsCol.setCellValueFactory(new PropertyValueFactory<>("bedrooms"));
        bedroomsCol.setPrefWidth(100);
        
        TableColumn<Apartment, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(120);
        priceCol.setCellFactory(col -> new TableCell<Apartment, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f MAD", price));
                }
            }
        });
        
        TableColumn<Apartment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);
        statusCol.setCellFactory(col -> new TableCell<Apartment, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("Sold".equals(status)) {
                        setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 4;");
                    } else {
                        setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404; -fx-background-radius: 4;");
                    }
                }
            }
        });
        
        TableColumn<Apartment, String> buyerCol = new TableColumn<>("Buyer");
        buyerCol.setCellValueFactory(new PropertyValueFactory<>("buyer"));
        buyerCol.setPrefWidth(150);
        
        TableColumn<Apartment, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(120);
        actionsCol.setCellFactory(col -> new TableCell<Apartment, Void>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);
            
            {
                editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #6c757d; -fx-border-radius: 4; -fx-padding: 4 8;");
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #dc3545; -fx-text-fill: #dc3545; -fx-border-radius: 4; -fx-padding: 4 8;");
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
        
        table.getColumns().addAll(numberCol, floorCol, sizeCol, bedroomsCol, priceCol, statusCol, buyerCol, actionsCol);
        
        if (selectedBuilding != null) {
            table.getItems().addAll(selectedBuilding.apartments);
        }
        
        return table;
    }
    
    private void showAddBuildingDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add New Building");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label title = new Label("Add New Building");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Enter building name");
        
        TextField locationField = new TextField();
        locationField.setPromptText("Enter full address");
        
        TextField floorsField = new TextField();
        floorsField.setPromptText("12");
        
        TextField apartmentsField = new TextField();
        apartmentsField.setPromptText("48");
        
        form.add(new Label("Building Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Location:"), 0, 1);
        form.add(locationField, 1, 1);
        form.add(new Label("Number of Floors:"), 0, 2);
        form.add(floorsField, 1, 2);
        form.add(new Label("Total Apartments:"), 0, 3);
        form.add(apartmentsField, 1, 3);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button saveBtn = new Button("Save Building");
        saveBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 4;");
        saveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String location = locationField.getText().trim();
                String floorsText = floorsField.getText().trim();
                String apartmentsText = apartmentsField.getText().trim();
                
                if (name.isEmpty() || location.isEmpty() || floorsText.isEmpty() || apartmentsText.isEmpty()) {
                    showError("Validation Error", "Please fill in all fields.");
                    return;
                }
                
                int floors = Integer.parseInt(floorsText);
                int totalApartments = Integer.parseInt(apartmentsText);
                
                if (floors <= 0 || totalApartments <= 0) {
                    showError("Validation Error", "Floors and apartments must be positive numbers.");
                    return;
                }
                
                // Save to database
                if (saveBuildingToDatabase(name, location, floors, totalApartments)) {
                    // Reload buildings from database to get the new ID
                    loadBuildingsFromDatabase();
                    filteredBuildings.clear();
                    filteredBuildings.addAll(buildings);
                    
                    updateBuildingsGrid();
                    
                    showSuccess("Success", "Building '" + name + "' has been added successfully!");
                    dialog.close();
                } else {
                    showError("Database Error", "Failed to save building to database.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Input", "Please enter valid numbers for floors and apartments.");
            } catch (Exception ex) {
                showError("Error", "An error occurred while adding the building: " + ex.getMessage());
            }
        });
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(title, form, buttons);
        
        Scene scene = new Scene(content, 500, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean saveBuildingToDatabase(String name, String location, int floors, int totalApartments) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String insertSQL = "INSERT INTO buildings (name, location, floors, total_apartments, occupied_apartments, status, construction_date, description) " +
                             "VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, ?)";
            
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setString(1, name);
                stmt.setString(2, location);
                stmt.setInt(3, floors);
                stmt.setInt(4, totalApartments);
                stmt.setInt(5, 0); // occupied_apartments starts at 0
                stmt.setString(6, "active"); // status
                stmt.setString(7, "New building added via application"); // description
                
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean updateBuildingInDatabase(int buildingId, String name, String location, int floors, int totalApartments) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String updateSQL = "UPDATE buildings SET name = ?, location = ?, floors = ?, total_apartments = ? WHERE id = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
                stmt.setString(1, name);
                stmt.setString(2, location);
                stmt.setInt(3, floors);
                stmt.setInt(4, totalApartments);
                stmt.setInt(5, buildingId);
                
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean deleteBuildingFromDatabase(int buildingId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            // First delete all apartments in this building
            String deleteApartmentsSQL = "DELETE FROM apartments WHERE building_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteApartmentsSQL)) {
                stmt.setInt(1, buildingId);
                stmt.executeUpdate();
            }
            
            // Then delete the building
            String deleteBuildingSQL = "DELETE FROM buildings WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteBuildingSQL)) {
                stmt.setInt(1, buildingId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean saveApartmentToDatabase(int buildingId, String number, int floor, String size, int bedrooms, double price, String status) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String insertSQL = "INSERT INTO apartments (building_id, apartment_number, floor, size, bedrooms, bathrooms, price, status) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setInt(1, buildingId);
                stmt.setString(2, number);
                stmt.setInt(3, floor);
                stmt.setString(4, size);
                stmt.setInt(5, bedrooms);
                stmt.setInt(6, bedrooms > 1 ? 2 : 1); // Default bathrooms based on bedrooms
                stmt.setDouble(7, price);
                stmt.setString(8, status.toLowerCase());
                
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void updateBuildingOccupancy(int buildingId, int occupiedCount) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String updateSQL = "UPDATE buildings SET occupied_apartments = ? WHERE id = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
                stmt.setInt(1, occupiedCount);
                stmt.setInt(2, buildingId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void showEditBuildingDialog(Building building) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Building");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label title = new Label("Edit Building");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        TextField nameField = new TextField(building.name);
        TextField locationField = new TextField(building.location);
        TextField floorsField = new TextField(String.valueOf(building.floors));
        TextField apartmentsField = new TextField(String.valueOf(building.totalApartments));
        
        form.add(new Label("Building Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Location:"), 0, 1);
        form.add(locationField, 1, 1);
        form.add(new Label("Number of Floors:"), 0, 2);
        form.add(floorsField, 1, 2);
        form.add(new Label("Total Apartments:"), 0, 3);
        form.add(apartmentsField, 1, 3);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button saveBtn = new Button("Update Building");
        saveBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 4;");
        saveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String location = locationField.getText().trim();
                String floorsText = floorsField.getText().trim();
                String apartmentsText = apartmentsField.getText().trim();
                
                if (name.isEmpty() || location.isEmpty() || floorsText.isEmpty() || apartmentsText.isEmpty()) {
                    showError("Validation Error", "Please fill in all fields.");
                    return;
                }
                
                int floors = Integer.parseInt(floorsText);
                int totalApartments = Integer.parseInt(apartmentsText);
                
                if (floors <= 0 || totalApartments <= 0) {
                    showError("Validation Error", "Floors and apartments must be positive numbers.");
                    return;
                }
                
                // Update in database
                if (updateBuildingInDatabase(building.id, name, location, floors, totalApartments)) {
                    building.name = name;
                    building.location = location;
                    building.floors = floors;
                    building.totalApartments = totalApartments;
                    
                    updateBuildingsGrid();
                    
                    showSuccess("Success", "Building '" + name + "' has been updated successfully!");
                    dialog.close();
                } else {
                    showError("Database Error", "Failed to update building in database.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Input", "Please enter valid numbers for floors and apartments.");
            } catch (Exception ex) {
                showError("Error", "An error occurred while updating the building: " + ex.getMessage());
            }
        });
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        content.getChildren().addAll(title, form, buttons);
        
        Scene scene = new Scene(content, 500, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private void showDeleteBuildingConfirmation(Building building) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Building");
        alert.setHeaderText("Delete " + building.name + "?");
        alert.setContentText("Are you sure you want to delete this building?\n\n" +
                "Building: " + building.name + "\n" +
                "Location: " + building.location + "\n" +
                "Total Apartments: " + building.totalApartments + "\n\n" +
                "This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete from database
                if (deleteBuildingFromDatabase(building.id)) {
                    buildings.remove(building);
                    filteredBuildings.remove(building);
                    updateBuildingsGrid();
                    showSuccess("Success", "Building '" + building.name + "' has been deleted successfully!");
                } else {
                    showError("Database Error", "Failed to delete building from database.");
                }
            }
        });
    }
    
    private void showAddApartmentDialog(Building building) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add New Apartment");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label title = new Label("Add New Apartment to " + building.name);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        TextField numberField = new TextField();
        numberField.setPromptText("e.g., 301");
        
        TextField floorField = new TextField();
        floorField.setPromptText("e.g., 3");
        
        TextField sizeField = new TextField();
        sizeField.setPromptText("e.g., 850 sq ft");
        
        TextField bedroomsField = new TextField();
        bedroomsField.setPromptText("e.g., 2");
        
        TextField priceField = new TextField();
        priceField.setPromptText("e.g., 2500000");
        
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Available", "Sold", "Reserved");
        statusCombo.setValue("Available");
        
        TextField buyerField = new TextField();
        buyerField.setPromptText("Leave empty if available");
        buyerField.setText("-");
        
        form.add(new Label("Apartment Number:"), 0, 0);
        form.add(numberField, 1, 0);
        form.add(new Label("Floor:"), 0, 1);
        form.add(floorField, 1, 1);
        form.add(new Label("Size:"), 0, 2);
        form.add(sizeField, 1, 2);
        form.add(new Label("Bedrooms:"), 0, 3);
        form.add(bedroomsField, 1, 3);
        form.add(new Label("Price (MAD):"), 0, 4);
        form.add(priceField, 1, 4);
        form.add(new Label("Status:"), 0, 5);
        form.add(statusCombo, 1, 5);
        form.add(new Label("Buyer:"), 0, 6);
        form.add(buyerField, 1, 6);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button saveBtn = new Button("Add Apartment");
        saveBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 4;");
        saveBtn.setOnAction(e -> {
            try {
                String number = numberField.getText().trim();
                String floorText = floorField.getText().trim();
                String size = sizeField.getText().trim();
                String bedroomsText = bedroomsField.getText().trim();
                String priceText = priceField.getText().trim();
                String status = statusCombo.getValue();
                String buyer = buyerField.getText().trim();
                
                if (number.isEmpty() || floorText.isEmpty() || size.isEmpty() || 
                    bedroomsText.isEmpty() || priceText.isEmpty()) {
                    showError("Validation Error", "Please fill in all required fields.");
                    return;
                }
                
                boolean exists = building.apartments.stream()
                    .anyMatch(apt -> apt.number.equals(number));
                if (exists) {
                    showError("Validation Error", "Apartment number " + number + " already exists in this building.");
                    return;
                }
                
                int floor = Integer.parseInt(floorText);
                int bedrooms = Integer.parseInt(bedroomsText);
                double price = Double.parseDouble(priceText);
                
                if (floor <= 0 || bedrooms <= 0 || price <= 0) {
                    showError("Validation Error", "Floor, bedrooms, and price must be positive numbers.");
                    return;
                }
                
                if (floor > building.floors) {
                    showError("Validation Error", "Floor cannot be higher than the building's total floors (" + building.floors + ").");
                    return;
                }
                
                // Save apartment to database
                if (saveApartmentToDatabase(building.id, number, floor, size, bedrooms, price, status)) {
                    Apartment newApartment = new Apartment(number, floor, size, bedrooms, price, status, buyer);
                    building.apartments.add(newApartment);
                    
                    if ("sold".equals(status.toLowerCase())) {
                        building.occupiedApartments++;
                        // Update building's occupied count in database
                        updateBuildingOccupancy(building.id, building.occupiedApartments);
                    }
                    
                    if (apartmentView.isVisible() && selectedBuilding == building) {
                        showApartments(building);
                    }
                    
                    updateBuildingsGrid();
                    
                    showSuccess("Success", "Apartment " + number + " has been added successfully!");
                    dialog.close();
                } else {
                    showError("Database Error", "Failed to save apartment to database.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Input", "Please enter valid numbers for floor, bedrooms, and price.");
            } catch (Exception ex) {
                showError("Error", "An error occurred while adding the apartment: " + ex.getMessage());
            }
        });
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        content.getChildren().addAll(title, form, buttons);
        
        Scene scene = new Scene(content, 500, 500);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}