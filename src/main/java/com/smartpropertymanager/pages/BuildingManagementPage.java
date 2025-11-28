package com.smartpropertymanager.pages;

import com.smartpropertymanager.components.BuildingCard;
import com.smartpropertymanager.models.Building;
import com.smartpropertymanager.models.Apartment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Arrays;
import java.util.List;

public class BuildingManagementPage implements Page {
    private VBox content;
    private String title = "Building Management";
    
    // Sample data
    private List<Building> buildings = Arrays.asList(
        new Building(1, "Skyline Tower", "123 Main Street, Downtown", 12, 48, 45, "Active"),
        new Building(2, "Riverside Apartments", "456 River Road, Westside", 8, 32, 30, "Active"),
        new Building(3, "Garden View Complex", "789 Park Avenue, Eastside", 15, 60, 55, "Active"),
        new Building(4, "Metro Heights", "321 Business District", 10, 40, 38, "Active")
    );
    
    private Building selectedBuilding = null;

    public BuildingManagementPage() {
        content = new VBox();
        content.setStyle("-fx-background-color: #F8FAFC;");
        content.setPadding(new Insets(32));
        content.setSpacing(24);
        
        showBuildingListView();
    }

    private void showBuildingListView() {
        content.getChildren().clear();

        // Header
        HBox header = createHeader();
        content.getChildren().add(header);

        // Search bar
        HBox searchBar = createSearchBar();
        content.getChildren().add(searchBar);

        // Buildings Grid
        ScrollPane gridScroll = new ScrollPane();
        GridPane buildingsGrid = createBuildingsGrid();
        gridScroll.setContent(buildingsGrid);
        gridScroll.setFitToWidth(true);
        gridScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        content.getChildren().add(gridScroll);
        VBox.setVgrow(gridScroll, Priority.ALWAYS);
    }

    private void showApartmentListView(Building building) {
        content.getChildren().clear();
        this.selectedBuilding = building;

        // Back button and header
        HBox topBar = createApartmentTopBar(building);
        content.getChildren().add(topBar);

        // Apartments Table
        VBox apartmentsTable = createApartmentsTable(building);
        content.getChildren().add(apartmentsTable);
        VBox.setVgrow(apartmentsTable, Priority.ALWAYS);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        VBox titleSection = new VBox(8);
        Label titleLabel = new Label("Building Management");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label subtitleLabel = new Label("Manage all buildings and apartments");
        subtitleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBuildingBtn = new Button("+ Add Building");
        addBuildingBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-size: 14;");
        addBuildingBtn.setOnAction(e -> showAddBuildingDialog());

        header.getChildren().addAll(titleSection, spacer, addBuildingBtn);
        return header;
    }

    private HBox createSearchBar() {
        HBox searchBar = new HBox();
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setSpacing(12);

        TextField searchField = new TextField();
        searchField.setPromptText("Search buildings by name or location...");
        searchField.setStyle("-fx-padding: 12 12 12 40; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #D1D5DB; -fx-font-size: 14;");
        searchField.setPrefWidth(500);

        Label searchIcon = new Label("🔍");
        searchIcon.setStyle("-fx-text-fill: #6B7280; -fx-padding: 0 0 0 12;");
        StackPane searchContainer = new StackPane();
        searchContainer.getChildren().addAll(searchField, searchIcon);
        StackPane.setAlignment(searchIcon, Pos.CENTER_LEFT);

        HBox.setHgrow(searchContainer, Priority.ALWAYS);
        searchBar.getChildren().add(searchContainer);
        return searchBar;
    }

    private GridPane createBuildingsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(24);
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPadding(new Insets(16, 0, 0, 0));

        int col = 0;
        int row = 0;
        for (Building building : buildings) {
            BuildingCard card = new BuildingCard(building);
            card.setOnViewClicked(b -> showApartmentListView(b));
            
            grid.add(card, col, row);
            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }

        return grid;
    }

    private HBox createApartmentTopBar(Building building) {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(16);
        topBar.setPadding(new Insets(0, 0, 16, 0));

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-text-fill: #374151; -fx-padding: 8 16; -fx-font-size: 14;");
        backBtn.setOnAction(e -> showBuildingListView());

        VBox titleSection = new VBox(4);
        Label titleLabel = new Label(building.getName() + " - Apartments");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label locationLabel = new Label(building.getLocation());
        locationLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        titleSection.getChildren().addAll(titleLabel, locationLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addApartmentBtn = new Button("+ Add Apartment");
        addApartmentBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-size: 14;");
        addApartmentBtn.setOnAction(e -> showAddApartmentDialog());

        topBar.getChildren().addAll(backBtn, titleSection, spacer, addApartmentBtn);
        return topBar;
    }

    private VBox createApartmentsTable(Building building) {
        VBox tableContainer = new VBox();
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        tableContainer.setPadding(new Insets(20));

        // Table view for apartments
        TableView<Apartment> tableView = new TableView<>();
        
        // Define columns
        TableColumn<Apartment, String> aptNumberCol = new TableColumn<>("Apt #");
        aptNumberCol.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        aptNumberCol.setPrefWidth(80);
        
        TableColumn<Apartment, Integer> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(cellData -> cellData.getValue().floorProperty().asObject());
        floorCol.setPrefWidth(80);
        
        TableColumn<Apartment, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        sizeCol.setPrefWidth(120);
        
        TableColumn<Apartment, Integer> bedroomsCol = new TableColumn<>("Bedrooms");
        bedroomsCol.setCellValueFactory(cellData -> cellData.getValue().bedroomsProperty().asObject());
        bedroomsCol.setPrefWidth(100);
        
        TableColumn<Apartment, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> cellData.getValue().priceFormattedProperty());
        priceCol.setPrefWidth(130);
        
        TableColumn<Apartment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        statusCol.setPrefWidth(100);
        statusCol.setCellFactory(col -> new TableCell<Apartment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(item);
                    String color = "Sold".equals(item) ? "#D1FAE5" : "#FEF3C7";
                    String textColor = "Sold".equals(item) ? "#065F46" : "#92400E";
                    badge.setStyle("-fx-background-color: " + color + "; -fx-text-fill: " + textColor + "; -fx-padding: 4 8; -fx-background-radius: 6; -fx-font-size: 12; -fx-font-weight: bold;");
                    setGraphic(badge);
                }
            }
        });
        
        TableColumn<Apartment, String> buyerCol = new TableColumn<>("Buyer");
        buyerCol.setCellValueFactory(cellData -> cellData.getValue().buyerProperty());
        buyerCol.setPrefWidth(140);
        
        TableColumn<Apartment, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<Apartment, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons = new HBox(8);
            
            {
                editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #374151; -fx-border-color: #D1D5DB; -fx-border-radius: 4; -fx-padding: 6 12;");
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #DC2626; -fx-border-color: #D1D5DB; -fx-border-radius: 4; -fx-padding: 6 12;");
                
                editBtn.setOnAction(e -> {
                    Apartment apt = getTableView().getItems().get(getIndex());
                    showEditApartmentDialog(apt);
                });
                
                deleteBtn.setOnAction(e -> {
                    Apartment apt = getTableView().getItems().get(getIndex());
                    showDeleteApartmentConfirmation(apt, getTableView());
                });
                
                buttons.getChildren().addAll(editBtn, deleteBtn);
                buttons.setAlignment(Pos.CENTER);
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

        tableView.getColumns().addAll(aptNumberCol, floorCol, sizeCol, bedroomsCol, priceCol, statusCol, buyerCol, actionsCol);
        
        // Sample apartment data
        tableView.getItems().addAll(
            new Apartment("101", 1, "850 sq ft", 2, 2400000, "Sold", "John Smith"),
            new Apartment("102", 1, "920 sq ft", 2, 2600000, "Sold", "Sarah Johnson"),
            new Apartment("103", 1, "750 sq ft", 1, 2000000, "Available", "-"),
            new Apartment("201", 2, "1100 sq ft", 3, 3200000, "Sold", "Mike Brown"),
            new Apartment("202", 2, "850 sq ft", 2, 2400000, "Sold", "Emma Davis"),
            new Apartment("203", 2, "920 sq ft", 2, 2700000, "Sold", "David Wilson")
        );

        tableView.setStyle("-fx-background-color: transparent; -fx-font-size: 13;");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        tableContainer.getChildren().add(tableView);
        return tableContainer;
    }

    private void showAddBuildingDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Add Building");
        dialog.setHeaderText("Add New Building");
        dialog.setContentText("Add Building dialog would open here");
        dialog.showAndWait();
    }

    private void showAddApartmentDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Add Apartment");
        dialog.setHeaderText("Add New Apartment");
        dialog.setContentText("Add Apartment dialog would open here");
        dialog.showAndWait();
    }

    private void showEditApartmentDialog(Apartment apartment) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Edit Apartment");
        dialog.setHeaderText("Edit Apartment " + apartment.getNumber());
        dialog.setContentText("Apartment: " + apartment.getNumber() + "\n" +
                "Floor: " + apartment.getFloor() + "\n" +
                "Size: " + apartment.getSize() + "\n" +
                "Bedrooms: " + apartment.getBedrooms() + "\n" +
                "Price: " + String.format("%,d MAD", apartment.getPrice()) + "\n" +
                "Status: " + apartment.getStatus() + "\n" +
                "Buyer: " + apartment.getBuyer() + "\n\n" +
                "Edit dialog would open here");
        dialog.showAndWait();
    }

    private void showDeleteApartmentConfirmation(Apartment apartment, TableView<Apartment> tableView) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Apartment");
        confirmDialog.setHeaderText("Delete Apartment " + apartment.getNumber() + "?");
        confirmDialog.setContentText("Are you sure you want to delete apartment " + apartment.getNumber() + "?\n" +
                "This action cannot be undone.");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                tableView.getItems().remove(apartment);
                
                // Show success notification
                Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                successDialog.setTitle("Success");
                successDialog.setHeaderText("Apartment Deleted");
                successDialog.setContentText("Apartment " + apartment.getNumber() + " has been successfully deleted.");
                successDialog.showAndWait();
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
