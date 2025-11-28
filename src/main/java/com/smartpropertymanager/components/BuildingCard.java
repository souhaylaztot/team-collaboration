package com.smartpropertymanager.components;

import com.smartpropertymanager.models.Building;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.function.Consumer;

public class BuildingCard extends VBox {
    private Consumer<Building> onViewClicked;
    
    public BuildingCard(Building building) {
        setPrefSize(350, 280);
        setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        setPadding(new Insets(20));
        setSpacing(16);
        
        // Header with icon and title
        HBox header = createHeader(building);
        
        // Location
        HBox location = createLocation(building);
        
        // Stats grid
        GridPane statsGrid = createStatsGrid(building);
        
        // Occupancy progress
        VBox occupancySection = createOccupancySection(building);
        
        // Action buttons
        HBox actionButtons = createActionButtons(building);
        
        getChildren().addAll(header, location, statsGrid, occupancySection, actionButtons);
    }
    
    private HBox createHeader(Building building) {
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        
        // Icon background with gradient
        Rectangle iconBg = new Rectangle(40, 40);
        iconBg.setArcWidth(8);
        iconBg.setArcHeight(8);
        iconBg.setFill(Color.web("#2C3E8C"));
        
        Label icon = new Label("🏢");
        icon.setStyle("-fx-font-size: 16; -fx-text-fill: white;");
        
        StackPane iconContainer = new StackPane(iconBg, icon);
        iconContainer.setAlignment(Pos.CENTER);
        
        VBox titleSection = new VBox(4);
        Label title = new Label(building.getName());
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #000000;");
        
        Label status = new Label(building.getStatus());
        status.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;");
        
        titleSection.getChildren().addAll(title, status);
        header.getChildren().addAll(iconContainer, titleSection);
        
        return header;
    }
    
    private HBox createLocation(Building building) {
        HBox location = new HBox(8);
        location.setAlignment(Pos.CENTER_LEFT);
        
        Label pinIcon = new Label("📍");
        pinIcon.setStyle("-fx-font-size: 14;");
        
        Label address = new Label(building.getLocation());
        address.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14; -fx-wrap-text: true;");
        
        location.getChildren().addAll(pinIcon, address);
        return location;
    }
    
    private GridPane createStatsGrid(Building building) {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(8);
        
        // Floors
        Label floorsLabel = new Label("Floors");
        floorsLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");
        
        HBox floorsValue = new HBox(4);
        Label floorsIcon = new Label("🏗️");
        Label floorsText = new Label(String.valueOf(building.getFloors()));
        floorsText.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold;");
        floorsValue.getChildren().addAll(floorsIcon, floorsText);
        
        // Apartments
        Label aptsLabel = new Label("Apartments");
        aptsLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");
        
        Label aptsText = new Label(String.valueOf(building.getTotalApartments()));
        aptsText.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold;");
        
        grid.add(floorsLabel, 0, 0);
        grid.add(floorsValue, 0, 1);
        grid.add(aptsLabel, 1, 0);
        grid.add(aptsText, 1, 1);
        
        return grid;
    }
    
    private VBox createOccupancySection(Building building) {
        VBox occupancy = new VBox(8);
        
        HBox occupancyHeader = new HBox();
        occupancyHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label occupancyLabel = new Label("Occupancy");
        occupancyLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        int occupied = building.getOccupiedApartments();
        int total = building.getTotalApartments();
        int percentage = (int) ((occupied / (double) total) * 100);
        
        Label percentageLabel = new Label(occupied + "/" + total + " (" + percentage + "%)");
        percentageLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-font-weight: bold;");
        
        occupancyHeader.getChildren().addAll(occupancyLabel, spacer, percentageLabel);
        
        ProgressBar progressBar = new ProgressBar(occupied / (double) total);
        progressBar.setStyle("-fx-accent: #2C3E8C; -fx-pref-height: 8;");
        progressBar.setPrefWidth(300);
        
        occupancy.getChildren().addAll(occupancyHeader, progressBar);
        return occupancy;
    }
    
    private HBox createActionButtons(Building building) {
        HBox actions = new HBox(8);
        
        Button viewBtn = new Button("View");
        viewBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-text-fill: #374151; -fx-pref-width: 100;");
        viewBtn.setOnAction(e -> {
            if (onViewClicked != null) {
                onViewClicked.accept(building);
            }
        });
        
        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-text-fill: #374151;");
        editBtn.setOnAction(e -> showEditBuildingDialog(building));
        
        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-text-fill: #DC2626;");
        deleteBtn.setOnAction(e -> showDeleteBuildingConfirmation(building));
        
        actions.getChildren().addAll(viewBtn, editBtn, deleteBtn);
        return actions;
    }
    
    private void showEditBuildingDialog(Building building) {
        javafx.scene.control.Alert dialog = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        dialog.setTitle("Edit Building");
        dialog.setHeaderText("Edit Building: " + building.getName());
        dialog.setContentText("Building Name: " + building.getName() + "\n" +
                "Location: " + building.getLocation() + "\n" +
                "Floors: " + building.getFloors() + "\n" +
                "Total Apartments: " + building.getTotalApartments() + "\n" +
                "Occupied: " + building.getOccupiedApartments() + "\n" +
                "Status: " + building.getStatus() + "\n\n" +
                "Edit dialog would open here");
        dialog.showAndWait();
    }
    
    private void showDeleteBuildingConfirmation(Building building) {
        javafx.scene.control.Alert confirmDialog = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Building");
        confirmDialog.setHeaderText("Delete Building: " + building.getName() + "?");
        confirmDialog.setContentText("Are you sure you want to delete " + building.getName() + "?\n" +
                "This action cannot be undone.");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Delete action would be implemented here
                javafx.scene.control.Alert successDialog = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                successDialog.setTitle("Success");
                successDialog.setHeaderText("Building Deleted");
                successDialog.setContentText(building.getName() + " has been successfully deleted.");
                successDialog.showAndWait();
            }
        });
    }
    
    public void setOnViewClicked(Consumer<Building> callback) {
        this.onViewClicked = callback;
    }
}
