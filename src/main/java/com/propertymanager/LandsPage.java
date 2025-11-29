package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LandsPage extends VBox {
    
    private List<Land> lands;
    private GridPane landsGrid;
    
    public LandsPage() {
        initData();
        initPage();
    }
    
    private void initData() {
        lands = new ArrayList<>();
        
        lands.add(new Land(1, "Sunset Hills Plot", "4500 Hillside Avenue, North District", 
                          "12,500 sq ft", "Property Holdings LLC", 4500000, "Available", 
                          "Prime location with city view, zoned for residential development", 3));
        
        lands.add(new Land(2, "Downtown Commercial Lot", "1200 Business Street, Downtown", 
                          "8,000 sq ft", "Metro Investments", 8500000, "Under Development", 
                          "Commercial zoning, ideal for mixed-use development", 5));
        
        lands.add(new Land(3, "Riverside Estate", "7800 River Road, Westside", 
                          "25,000 sq ft", "Green Valley Partners", 12000000, "Available", 
                          "Waterfront property with development permits approved", 8));
        
        lands.add(new Land(4, "Industrial Park Site", "3400 Factory Lane, Industrial Zone", 
                          "50,000 sq ft", "Industrial Holdings Inc", 9800000, "Sold", 
                          "Large industrial plot with warehouse facilities", 12));
    }
    
    private void initPage() {
        setPadding(new Insets(30));
        setSpacing(20);
        setStyle("-fx-background-color: #f8f9fa;");
        
        // Header
        HBox header = createHeader();
        
        // Summary cards
        HBox summaryCards = createSummaryCards();
        
        // Lands grid
        ScrollPane scrollPane = new ScrollPane();
        landsGrid = new GridPane();
        landsGrid.setHgap(20);
        landsGrid.setVgap(20);
        scrollPane.setContent(landsGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        updateLandsGrid();
        
        getChildren().addAll(header, summaryCards, scrollPane);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Land Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Manage land properties and development sites");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.GRAY);
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addBtn = new Button("+ Add Land");
        addBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;");
        addBtn.setOnAction(e -> showAddLandDialog());
        
        header.getChildren().addAll(titleBox, spacer, addBtn);
        return header;
    }
    
    private HBox createSummaryCards() {
        HBox summaryBox = new HBox(20);
        summaryBox.setAlignment(Pos.CENTER);
        
        summaryBox.getChildren().addAll(
            createSummaryCard("Total Lands", "8", "🏞️", "#2C3E8C"),
            createSummaryCard("Available", "5", "🟢", "#10b981"),
            createSummaryCard("In Development", "2", "🔵", "#3b82f6"),
            createSummaryCard("Total Value", "35M MAD", "💰", "#F5C542")
        );
        
        return summaryBox;
    }
    
    private VBox createSummaryCard(String title, String value, String icon, String color) {
        VBox card = new VBox(10);
        card.setPrefWidth(250);
        card.setPrefHeight(100);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 12; -fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        
        VBox textBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setTextFill(Color.GRAY);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        valueLabel.setTextFill(Color.web(color));
        
        textBox.getChildren().addAll(titleLabel, valueLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");
        
        content.getChildren().addAll(textBox, spacer, iconLabel);
        card.getChildren().add(content);
        
        return card;
    }
    
    private void updateLandsGrid() {
        landsGrid.getChildren().clear();
        
        int col = 0;
        int row = 0;
        
        for (Land land : lands) {
            VBox card = createLandCard(land);
            landsGrid.add(card, col, row);
            
            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }
    }
    
    private VBox createLandCard(Land land) {
        VBox card = new VBox(15);
        card.setPrefWidth(500);
        card.setPrefHeight(350);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 12; -fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label name = new Label(land.name);
        name.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        name.setTextFill(Color.web("#2c3e50"));
        
        Label status = new Label(land.status);
        switch (land.status) {
            case "Available":
                status.setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 4; -fx-padding: 2 8;");
                break;
            case "Under Development":
                status.setStyle("-fx-background-color: #cce7ff; -fx-text-fill: #004085; -fx-background-radius: 4; -fx-padding: 2 8;");
                break;
            case "Sold":
                status.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #6c757d; -fx-background-radius: 4; -fx-padding: 2 8;");
                break;
        }
        
        titleBox.getChildren().addAll(name, status);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label icon = new Label("🏞️");
        icon.setStyle("-fx-background-color: linear-gradient(135deg, #8B5CF6, #6d3ec9); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8; -fx-font-size: 16px;");
        
        header.getChildren().addAll(titleBox, spacer, icon);
        
        // Location
        Label location = new Label("📍 " + land.location);
        location.setFont(Font.font("Arial", 12));
        location.setTextFill(Color.web("#6c757d"));
        location.setWrapText(true);
        
        // Details
        HBox details = new HBox(30);
        VBox areaBox = new VBox(2);
        Label areaLabel = new Label("Area");
        areaLabel.setFont(Font.font("Arial", 10));
        areaLabel.setTextFill(Color.GRAY);
        Label areaValue = new Label("📏 " + land.area);
        areaValue.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        areaBox.getChildren().addAll(areaLabel, areaValue);
        
        VBox valueBox = new VBox(2);
        Label valueLabel = new Label("Estimated Value");
        valueLabel.setFont(Font.font("Arial", 10));
        valueLabel.setTextFill(Color.GRAY);
        NumberFormat formatter = NumberFormat.getInstance(Locale.FRANCE);
        Label valueValue = new Label(formatter.format(land.estimatedValue) + " MAD");
        valueValue.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        valueBox.getChildren().addAll(valueLabel, valueValue);
        
        details.getChildren().addAll(areaBox, valueBox);
        
        // Owner
        VBox ownerBox = new VBox(2);
        Label ownerLabel = new Label("Owner");
        ownerLabel.setFont(Font.font("Arial", 10));
        ownerLabel.setTextFill(Color.GRAY);
        Label ownerValue = new Label(land.owner);
        ownerValue.setFont(Font.font("Arial", 12));
        ownerBox.getChildren().addAll(ownerLabel, ownerValue);
        
        // Description
        VBox descBox = new VBox(2);
        Label descLabel = new Label("Description");
        descLabel.setFont(Font.font("Arial", 10));
        descLabel.setTextFill(Color.GRAY);
        Label descValue = new Label(land.description);
        descValue.setFont(Font.font("Arial", 12));
        descValue.setWrapText(true);
        descBox.getChildren().addAll(descLabel, descValue);
        
        // Footer
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_LEFT);
        
        Label docsLabel = new Label("📄 " + land.documents + " documents attached");
        docsLabel.setFont(Font.font("Arial", 12));
        docsLabel.setTextFill(Color.web("#6c757d"));
        
        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);
        
        Button editBtn = new Button("✏️ Edit");
        editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #6c757d; -fx-border-radius: 4; -fx-padding: 6 12;");
        
        Button deleteBtn = new Button("🗑️");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #dc3545; -fx-text-fill: #dc3545; -fx-border-radius: 4; -fx-padding: 6 8;");
        
        footer.getChildren().addAll(docsLabel, footerSpacer, editBtn, deleteBtn);
        
        card.getChildren().addAll(header, location, details, ownerBox, descBox, footer);
        return card;
    }
    
    private void showAddLandDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add New Land Property");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label title = new Label("Add New Land Property");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Sunset Hills Plot");
        
        TextField locationField = new TextField();
        locationField.setPromptText("Full address");
        
        TextField areaField = new TextField();
        areaField.setPromptText("12,500");
        
        TextField valueField = new TextField();
        valueField.setPromptText("4500000");
        
        TextField ownerField = new TextField();
        ownerField.setPromptText("Owner name or company");
        
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Available", "Under Development", "Sold");
        statusBox.setPromptText("Select status");
        
        TextArea descArea = new TextArea();
        descArea.setPromptText("Property details, zoning information, etc.");
        descArea.setPrefRowCount(3);
        
        form.add(new Label("Property Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Location:"), 0, 1);
        form.add(locationField, 1, 1);
        form.add(new Label("Area (sq ft):"), 0, 2);
        form.add(areaField, 1, 2);
        form.add(new Label("Estimated Value (MAD):"), 0, 3);
        form.add(valueField, 1, 3);
        form.add(new Label("Owner:"), 0, 4);
        form.add(ownerField, 1, 4);
        form.add(new Label("Status:"), 0, 5);
        form.add(statusBox, 1, 5);
        form.add(new Label("Description:"), 0, 6);
        form.add(descArea, 1, 6);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button saveBtn = new Button("Save Land");
        saveBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 4;");
        saveBtn.setOnAction(e -> {
            // Add land logic here
            dialog.close();
        });
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(title, form, buttons);
        
        Scene scene = new Scene(content, 600, 600);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}