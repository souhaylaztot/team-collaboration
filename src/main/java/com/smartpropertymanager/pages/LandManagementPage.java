package com.smartpropertymanager.pages;

import com.smartpropertymanager.models.Land;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Arrays;
import java.util.List;

public class LandManagementPage implements Page {
    private VBox content;
    private String title = "Land Management";
    
    private ObservableList<Land> lands;

    public LandManagementPage() {
        content = new VBox();
        content.setStyle("-fx-background-color: #F8FAFC;");
        content.setPadding(new Insets(32));
        content.setSpacing(24);
        
        initializeData();
        createUI();
    }

    private void initializeData() {
        // Sample land data
        List<Land> landList = Arrays.asList(
            new Land(1, "Sunset Hills Plot", "4500 Hillside Avenue, North District", 
                    "12,500 sq ft", "Property Holdings LLC", 4500000, "Available",
                    "Prime location with city view, zoned for residential development", 3),
            new Land(2, "Downtown Commercial Lot", "1200 Business Street, Downtown", 
                    "8,000 sq ft", "Metro Investments", 8500000, "Under Development",
                    "Commercial zoning, ideal for mixed-use development", 5),
            new Land(3, "Riverside Estate", "7800 River Road, Westside", 
                    "25,000 sq ft", "Green Valley Partners", 12000000, "Available",
                    "Waterfront property with development permits approved", 8),
            new Land(4, "Industrial Park Site", "3400 Factory Lane, Industrial Zone", 
                    "50,000 sq ft", "Industrial Holdings Inc", 9800000, "Sold",
                    "Large industrial plot with warehouse facilities", 12)
        );
        
        lands = FXCollections.observableArrayList(landList);
    }

    private void createUI() {
        // Header
        HBox header = createHeader();
        content.getChildren().add(header);

        // Summary Cards
        HBox summaryCards = createSummaryCards();
        content.getChildren().add(summaryCards);

        // Lands Grid
        GridPane landsGrid = createLandsGrid();
        content.getChildren().add(landsGrid);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        VBox titleSection = new VBox(8);
        Label titleLabel = new Label("Land Management");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label subtitleLabel = new Label("Manage land properties and development sites");
        subtitleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addLandBtn = new Button("Add Land");
        addLandBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        addLandBtn.setOnAction(e -> showAddLandDialog());

        header.getChildren().addAll(titleSection, spacer, addLandBtn);
        return header;
    }

    private HBox createSummaryCards() {
        HBox summaryCards = new HBox(24);
        summaryCards.setAlignment(Pos.CENTER_LEFT);

        // Calculate summary statistics
        long totalLands = lands.size();
        long availableLands = lands.stream().filter(land -> "Available".equals(land.getStatus())).count();
        long developmentLands = lands.stream().filter(land -> "Under Development".equals(land.getStatus())).count();
        long totalValue = lands.stream().mapToLong(Land::getEstimatedValue).sum();

        // Total Lands Card
        VBox totalCard = createSummaryCard("Total Lands", String.valueOf(totalLands), "🏞️", "#2C3E8C");
        
        // Available Card
        VBox availableCard = createSummaryCard("Available", String.valueOf(availableLands), "●", "#10B981");
        
        // In Development Card
        VBox developmentCard = createSummaryCard("In Development", String.valueOf(developmentLands), "●", "#3B82F6");
        
        // Total Value Card
        VBox valueCard = createSummaryCard("Total Value", String.format("%,dM MAD", totalValue / 1000000), "💰", "#F5C542");

        summaryCards.getChildren().addAll(totalCard, availableCard, developmentCard, valueCard);
        return summaryCards;
    }

    private VBox createSummaryCard(String title, String value, String icon, String iconColor) {
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
        if (icon.equals("●")) {
            // Status dot
            iconLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 24;", iconColor));
        } else {
            // Regular icon
            iconLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 24;", iconColor));
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        content.getChildren().addAll(textSection, spacer, iconLabel);
        card.getChildren().add(content);

        return card;
    }

    private GridPane createLandsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(24);
        grid.setAlignment(Pos.TOP_LEFT);

        int col = 0;
        int row = 0;
        for (Land land : lands) {
            VBox landCard = createLandCard(land);
            grid.add(landCard, col, row);
            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }

        return grid;
    }

    private VBox createLandCard(Land land) {
        VBox card = new VBox();
        card.setPrefSize(450, 380);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        card.setSpacing(16);

        // Header with title and status
        HBox header = createLandHeader(land);
        
        // Location
        HBox location = createLandLocation(land);
        
        // Area and Value grid
        GridPane statsGrid = createLandStatsGrid(land);
        
        // Owner
        VBox ownerSection = createOwnerSection(land);
        
        // Description
        VBox descriptionSection = createDescriptionSection(land);
        
        // Footer with documents and actions
        HBox footer = createLandFooter(land);

        card.getChildren().addAll(header, location, statsGrid, ownerSection, descriptionSection, footer);
        return card;
    }

    private HBox createLandHeader(Land land) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);

        VBox titleSection = new VBox(4);
        Label titleLabel = new Label(land.getName());
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label statusBadge = new Label(land.getStatus());
        statusBadge.setStyle(getStatusBadgeStyle(land.getStatus()));

        titleSection.getChildren().addAll(titleLabel, statusBadge);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Icon with gradient background
        StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(48, 48);
        iconContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B5CF6, #6d3ec9); -fx-background-radius: 8;");
        iconContainer.setAlignment(Pos.CENTER);

        Label iconLabel = new Label("🏞️");
        iconLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18;");

        iconContainer.getChildren().add(iconLabel);

        header.getChildren().addAll(titleSection, spacer, iconContainer);
        return header;
    }

    private HBox createLandLocation(Land land) {
        HBox location = new HBox(8);
        location.setAlignment(Pos.CENTER_LEFT);

        Label pinIcon = new Label("📍");
        pinIcon.setStyle("-fx-font-size: 14;");

        Label address = new Label(land.getLocation());
        address.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14; -fx-wrap-text: true;");

        location.getChildren().addAll(pinIcon, address);
        return location;
    }

    private GridPane createLandStatsGrid(Land land) {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(8);

        // Area
        Label areaLabel = new Label("Area");
        areaLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");

        HBox areaValue = new HBox(4);
        Label areaIcon = new Label("📐");
        Label areaText = new Label(land.getArea());
        areaText.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-font-weight: bold;");
        areaValue.getChildren().addAll(areaIcon, areaText);

        // Estimated Value
        Label valueLabel = new Label("Estimated Value");
        valueLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");

        Label valueText = new Label(String.format("%,d MAD", land.getEstimatedValue()));
        valueText.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-font-weight: bold;");

        grid.add(areaLabel, 0, 0);
        grid.add(areaValue, 0, 1);
        grid.add(valueLabel, 1, 0);
        grid.add(valueText, 1, 1);

        return grid;
    }

    private VBox createOwnerSection(Land land) {
        VBox ownerSection = new VBox(4);
        
        Label ownerLabel = new Label("Owner");
        ownerLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");

        Label ownerText = new Label(land.getOwner());
        ownerText.setStyle("-fx-text-fill: #000000; -fx-font-size: 14;");

        ownerSection.getChildren().addAll(ownerLabel, ownerText);
        return ownerSection;
    }

    private VBox createDescriptionSection(Land land) {
        VBox descriptionSection = new VBox(4);
        
        Label descLabel = new Label("Description");
        descLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");

        Label descText = new Label(land.getDescription());
        descText.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-wrap-text: true;");

        descriptionSection.getChildren().addAll(descLabel, descText);
        return descriptionSection;
    }

    private HBox createLandFooter(Land land) {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setStyle("-fx-border-color: #E5E7EB; -fx-border-width: 1 0 0 0; -fx-padding: 12 0 0 0;");

        // Documents info
        HBox documentsInfo = new HBox(8);
        documentsInfo.setAlignment(Pos.CENTER_LEFT);
        
        Label docIcon = new Label("📄");
        docIcon.setStyle("-fx-font-size: 14;");
        
        Label docText = new Label(land.getDocuments() + " documents attached");
        docText.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");
        
        documentsInfo.getChildren().addAll(docIcon, docText);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Action buttons
        HBox actionButtons = new HBox(8);
        
        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-text-fill: #374151; -fx-padding: 8 12;");
        editBtn.setOnAction(e -> editLand(land));
        
        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-text-fill: #DC2626; -fx-padding: 8 12;");
        deleteBtn.setOnAction(e -> deleteLand(land));
        
        actionButtons.getChildren().addAll(editBtn, deleteBtn);

        footer.getChildren().addAll(documentsInfo, spacer, actionButtons);
        return footer;
    }

    private String getStatusBadgeStyle(String status) {
        switch (status) {
            case "Available":
                return "-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            case "Under Development":
                return "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            case "Sold":
                return "-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            default:
                return "-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
        }
    }

    private void showAddLandDialog() {
        Dialog<Land> dialog = new Dialog<>();
        dialog.setTitle("Add New Land Property");
        dialog.setHeaderText("Enter land property details");

        // Create form components
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Sunset Hills Plot");
        TextField locationField = new TextField();
        locationField.setPromptText("4500 Hillside Avenue, North District");
        TextField areaField = new TextField();
        areaField.setPromptText("12,500 sq ft");
        TextField valueField = new TextField();
        valueField.setPromptText("4500000");
        TextField ownerField = new TextField();
        ownerField.setPromptText("Property Holdings LLC");
        
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Available", "Under Development", "Sold");
        statusCombo.setPromptText("Select status");
        
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Property details, zoning information, etc.");
        descriptionArea.setPrefRowCount(3);

        grid.add(new Label("Property Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Location:"), 0, 1);
        grid.add(locationField, 1, 1);
        grid.add(new Label("Area:"), 0, 2);
        grid.add(areaField, 1, 2);
        grid.add(new Label("Estimated Value:"), 0, 3);
        grid.add(valueField, 1, 3);
        grid.add(new Label("Owner:"), 0, 4);
        grid.add(ownerField, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusCombo, 1, 5);
        grid.add(new Label("Description:"), 0, 6);
        grid.add(descriptionArea, 1, 6);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Save Land", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    return new Land(lands.size() + 1, nameField.getText(), locationField.getText(),
                                  areaField.getText(), ownerField.getText(), 
                                  Integer.parseInt(valueField.getText()), 
                                  statusCombo.getValue(), descriptionArea.getText(), 0);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(land -> {
            lands.add(land);
            // Refresh the UI to show updated summary cards
            refreshUI();
        });
    }

    private void editLand(Land land) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Land");
        alert.setHeaderText("Edit Land: " + land.getName());
        alert.setContentText("Edit functionality would open here with pre-filled land data.");
        alert.showAndWait();
    }

    private void deleteLand(Land land) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Land");
        alert.setHeaderText("Delete " + land.getName() + "?");
        alert.setContentText("Are you sure you want to delete this land property? This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                lands.remove(land);
                refreshUI();
            }
        });
    }

    private void refreshUI() {
        content.getChildren().clear();
        createUI();
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
