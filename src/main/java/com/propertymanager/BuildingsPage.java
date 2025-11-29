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

import java.util.ArrayList;
import java.util.List;

public class BuildingsPage extends VBox {
    
    private TextField searchField;
    private GridPane buildingsGrid;
    private VBox apartmentView;
    private Building selectedBuilding;
    private List<Building> buildings;
    private ObservableList<Building> filteredBuildings;
    
    public BuildingsPage() {
        initData();
        initPage();
    }
    
    private void initData() {
        buildings = new ArrayList<>();
        
        Building b1 = new Building(1, "Skyline Tower", "123 Main Street, Downtown", 12, 48, 45, "Active");
        b1.apartments.add(new Apartment("101", 1, "850 sq ft", 2, 2400000, "Sold", "John Smith"));
        b1.apartments.add(new Apartment("102", 1, "920 sq ft", 2, 2600000, "Sold", "Sarah Johnson"));
        b1.apartments.add(new Apartment("103", 1, "750 sq ft", 1, 2000000, "Available", "-"));
        b1.apartments.add(new Apartment("201", 2, "1100 sq ft", 3, 3200000, "Sold", "Mike Brown"));
        b1.apartments.add(new Apartment("202", 2, "850 sq ft", 2, 2400000, "Sold", "Emma Davis"));
        b1.apartments.add(new Apartment("203", 2, "920 sq ft", 2, 2700000, "Sold", "David Wilson"));
        
        buildings.add(b1);
        buildings.add(new Building(2, "Riverside Apartments", "456 River Road, Westside", 8, 32, 30, "Active"));
        buildings.add(new Building(3, "Garden View Complex", "789 Park Avenue, Eastside", 15, 60, 55, "Active"));
        buildings.add(new Building(4, "Metro Heights", "321 Business District", 10, 40, 38, "Active"));
        
        filteredBuildings = FXCollections.observableArrayList(buildings);
    }
    
    private void initPage() {
        setPadding(new Insets(30));
        setSpacing(20);
        setStyle("-fx-background-color: #f8f9fa;");
        
        // Header
        HBox header = createHeader();
        
        // Search
        HBox searchBox = createSearchBox();
        
        // Buildings grid
        ScrollPane scrollPane = new ScrollPane();
        buildingsGrid = new GridPane();
        buildingsGrid.setHgap(20);
        buildingsGrid.setVgap(20);
        scrollPane.setContent(buildingsGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        // Apartment view (initially hidden)
        apartmentView = new VBox();
        apartmentView.setVisible(false);
        
        updateBuildingsGrid();
        
        getChildren().addAll(header, searchBox, scrollPane, apartmentView);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Building Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Manage all buildings and apartments");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.GRAY);
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addBtn = new Button("+ Add Building");
        addBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;");
        addBtn.setOnAction(e -> showAddBuildingDialog());
        
        header.getChildren().addAll(titleBox, spacer, addBtn);
        return header;
    }
    
    private HBox createSearchBox() {
        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        searchField = new TextField();
        searchField.setPromptText("🔍 Search buildings by name or location...");
        searchField.setPrefWidth(400);
        searchField.setPrefHeight(40);
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
        
        int col = 0;
        int row = 0;
        
        for (Building building : filteredBuildings) {
            VBox card = createBuildingCard(building);
            buildingsGrid.add(card, col, row);
            
            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }
    }
    
    private VBox createBuildingCard(Building building) {
        VBox card = new VBox(15);
        card.setPrefWidth(350);
        card.setPrefHeight(280);
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
        HBox occupancyHeader = new HBox();
        occupancyHeader.setAlignment(Pos.CENTER_LEFT);
        Label occupancyLabel = new Label("Occupancy");
        occupancyLabel.setFont(Font.font("Arial", 12));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label occupancyValue = new Label(building.occupiedApartments + "/" + building.totalApartments + " (" + (int)building.getOccupancyPercentage() + "%)");
        occupancyValue.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        occupancyHeader.getChildren().addAll(occupancyLabel, spacer, occupancyValue);
        
        ProgressBar progressBar = new ProgressBar(building.getOccupancyPercentage() / 100.0);
        progressBar.setPrefWidth(310);
        progressBar.setStyle("-fx-accent: linear-gradient(135deg, #2C3E8C, #4FD1C5);");
        
        occupancyBox.getChildren().addAll(occupancyHeader, progressBar);
        
        // Buttons
        HBox buttons = new HBox(10);
        Button viewBtn = new Button("👁️ View");
        viewBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #6c757d; -fx-border-radius: 4; -fx-padding: 6 12;");
        viewBtn.setOnAction(e -> showApartments(building));
        
        Button editBtn = new Button("✏️");
        editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #6c757d; -fx-border-radius: 4; -fx-padding: 6 8;");
        
        Button deleteBtn = new Button("🗑️");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #dc3545; -fx-text-fill: #dc3545; -fx-border-radius: 4; -fx-padding: 6 8;");
        
        buttons.getChildren().addAll(viewBtn, editBtn, deleteBtn);
        
        card.getChildren().addAll(header, location, stats, occupancyBox, buttons);
        return card;
    }
    
    private void showApartments(Building building) {
        selectedBuilding = building;
        
        // Hide buildings grid
        buildingsGrid.setVisible(false);
        
        // Show apartment view
        apartmentView.getChildren().clear();
        apartmentView.setVisible(true);
        
        // Create apartment view
        VBox apartmentCard = new VBox(20);
        apartmentCard.setPadding(new Insets(20));
        apartmentCard.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 12; -fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Header
        HBox apartmentHeader = new HBox();
        apartmentHeader.setAlignment(Pos.CENTER_LEFT);
        
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #6c757d; -fx-border-radius: 4; -fx-padding: 6 12;");
        backBtn.setOnAction(e -> {
            apartmentView.setVisible(false);
            buildingsGrid.setVisible(true);
        });
        
        Label apartmentTitle = new Label(building.name + " - Apartments");
        apartmentTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        apartmentTitle.setTextFill(Color.web("#2c3e50"));
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        Button addAptBtn = new Button("+ Add Apartment");
        addAptBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        
        apartmentHeader.getChildren().addAll(backBtn, apartmentTitle, spacer2, addAptBtn);
        
        Label locationLabel = new Label(building.location);
        locationLabel.setFont(Font.font("Arial", 12));
        locationLabel.setTextFill(Color.GRAY);
        
        // Table
        TableView<Apartment> table = createApartmentTable();
        
        apartmentCard.getChildren().addAll(apartmentHeader, locationLabel, table);
        apartmentView.getChildren().add(apartmentCard);
    }
    
    private TableView<Apartment> createApartmentTable() {
        TableView<Apartment> table = new TableView<>();
        table.setPrefHeight(400);
        
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
            // Add building logic here
            dialog.close();
        });
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(title, form, buttons);
        
        Scene scene = new Scene(content, 500, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}