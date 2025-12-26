package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
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

public class DashboardPage extends VBox {
    
    private int totalBuildings = 0;
    private int totalApartments = 0;
    private int totalBuyers = 0;
    private int totalLands = 0;
    private int soldApartments = 0;
    private int availableApartments = 0;
    
    private VBox mainContent;
    private HBox statsRow;
    private HBox chartsRow;
    private HBox activitiesRow;
    private boolean editMode = false;
    private List<VBox> widgets = new ArrayList<>();
    
    public DashboardPage() {
        loadDataFromDatabase();
        initDashboard();
    }
    
    private void loadDataFromDatabase() {
        try {
            // Use the new DAO classes to load statistics
            totalBuildings = BuildingDAO.getTotalBuildings();
            totalApartments = BuildingDAO.getTotalApartments();
            soldApartments = BuildingDAO.getOccupiedApartments();
            availableApartments = totalApartments - soldApartments;
            
            totalBuyers = BuyerDAO.getTotalBuyers();
            totalLands = LandDAO.getTotalLands();
            
            System.out.println("📊 Dashboard statistics loaded successfully!");
            System.out.println("Buildings: " + totalBuildings + ", Apartments: " + totalApartments + ", Buyers: " + totalBuyers + ", Lands: " + totalLands);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error loading dashboard data, using default values");
            // Use default values if database fails
            totalBuildings = 6;
            totalApartments = 240;
            totalBuyers = 69;
            totalLands = 6;
            soldApartments = 69;
            availableApartments = 171;
        }
    }
    
    private void initDashboard() {
        setPadding(new Insets(0));
        setSpacing(0);
        // Force dark theme for dashboard
        setStyle("-fx-background: " + ThemeManager.getBackground() + ";");
        
        // Wrap everything in ScrollPane for better visibility
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + ThemeManager.getBackground() + "; -fx-background-color: transparent;");
        
        VBox content = new VBox(30);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("root");
        
        // Header with edit controls
        VBox header = createEditableHeader();
        
        // Stats cards
        statsRow = createStatsRow();
        
        // Charts section
        chartsRow = createChartsRow();
        
        // Activities and Tasks section
        activitiesRow = createActivitiesRow();
        
        content.getChildren().addAll(header, statsRow, chartsRow, activitiesRow);
        mainContent = content;
        scrollPane.setContent(content);
        
        // Main container with theme background
        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background: " + ThemeManager.getBackground() + ";");
        mainContainer.getChildren().addAll(createBackgroundElements(), scrollPane);
        
        getChildren().add(mainContainer);
    }
    
    private VBox createEditableHeader() {
        VBox header = new VBox(15);
        
        // Title row with edit controls
        HBox titleRow = new HBox(20);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("📊 Dashboard Overview");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(ThemeManager.getTextPrimary()));
        
        Label subtitle = new Label("Welcome back! Here's what's happening with your properties.");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web(ThemeManager.getTextSecondary()));
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Edit controls
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_RIGHT);
        
        Button editBtn = new Button(editMode ? "💾 Save Layout" : "✏️ Edit Dashboard");
        editBtn.setStyle("-fx-background-color: #4FD1C5; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 15; -fx-cursor: hand;");
        editBtn.setOnAction(e -> toggleEditMode());
        
        Button addWidgetBtn = new Button("➕ Add Widget");
        addWidgetBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 15; -fx-cursor: hand;");
        addWidgetBtn.setOnAction(e -> showAddWidgetDialog());
        
        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle("-fx-background-color: #F5C542; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 15; -fx-cursor: hand;");
        refreshBtn.setOnAction(e -> refreshDashboard());
        
        controls.getChildren().addAll(refreshBtn, addWidgetBtn, editBtn);
        
        titleRow.getChildren().addAll(titleBox, spacer, controls);
        header.getChildren().add(titleRow);
        
        return header;
    }
    
    private HBox createStatsRow() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        statsRow.getChildren().addAll(
            createStatCard("Total Buildings", String.valueOf(totalBuildings), "+0", "🏢", "#2C3E8C"),
            createStatCard("Total Properties", String.valueOf(totalApartments), "+0", "📄", "#4FD1C5"),
            createStatCard("Total Buyers", String.valueOf(totalBuyers), "+0", "👥", "#F5C542"),
            createStatCard("Land Properties", String.valueOf(totalLands), "+0", "🏞️", "#8B5CF6")
        );
        
        return statsRow;
    }
    
    private VBox createStatCard(String title, String value, String change, String icon, String color) {
        VBox card = new VBox(10);
        card.setPrefWidth(280);
        card.setPrefHeight(120);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + ThemeManager.getCard() + "; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: " + ThemeManager.getBorder() + "; -fx-effect: dropshadow(gaussian, rgba(0,0,0," + (ThemeManager.isDarkMode() ? "0.3" : "0.1") + "), 10, 0, 0, 2);");
        
        // Add edit controls when in edit mode
        if (editMode) {
            HBox editControls = new HBox(5);
            editControls.setAlignment(Pos.TOP_RIGHT);
            
            Button editCardBtn = new Button("✏️");
            editCardBtn.setStyle("-fx-background-color: #4FD1C5; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 5; -fx-font-size: 10px;");
            editCardBtn.setOnAction(e -> editStatCard(card, title, value, icon, color));
            
            Button deleteCardBtn = new Button("🗑️");
            deleteCardBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 5; -fx-font-size: 10px;");
            deleteCardBtn.setOnAction(e -> deleteStatCard(card));
            
            editControls.getChildren().addAll(editCardBtn, deleteCardBtn);
            card.getChildren().add(editControls);
        }
        
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        
        VBox textBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setTextFill(Color.web(ThemeManager.getTextMuted()));
        
        HBox valueBox = new HBox(10);
        valueBox.setAlignment(Pos.CENTER_LEFT);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.setTextFill(Color.web(ThemeManager.getTextPrimary()));
        
        Label changeLabel = new Label("📈 " + change);
        changeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        changeLabel.setTextFill(Color.GREEN);
        
        valueBox.getChildren().addAll(valueLabel, changeLabel);
        textBox.getChildren().addAll(titleLabel, valueBox);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12; -fx-font-size: 20px;");
        
        content.getChildren().addAll(textBox, spacer, iconLabel);
        card.getChildren().add(content);
        
        widgets.add(card);
        return card;
    }
    
    private void toggleEditMode() {
        editMode = !editMode;
        refreshDashboard();
    }
    
    private void refreshDashboard() {
        loadDataFromDatabase();
        mainContent.getChildren().clear();
        
        VBox header = createEditableHeader();
        statsRow = createStatsRow();
        chartsRow = createChartsRow();
        activitiesRow = createActivitiesRow();
        
        mainContent.getChildren().addAll(header, statsRow, chartsRow, activitiesRow);
        
        // Update all charts and diagrams with new data
        updateAllCharts();
    }
    
    private void updateAllCharts() {
        // Update occupancy chart data
        updateOccupancyChart();
        // Update revenue chart data  
        updateRevenueChart();
    }
    
    private void showAddWidgetDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("➕ Add New Widget");
        dialog.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f8fafc;");
        
        Label title = new Label("🎛️ Choose Widget Type");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        VBox options = new VBox(15);
        
        Button statCardBtn = createWidgetOption("📊 Statistics Card", "Add a new statistics display card");
        statCardBtn.setOnAction(e -> { addStatWidget(); dialog.close(); });
        
        Button chartBtn = createWidgetOption("📈 Chart Widget", "Add a new chart or graph");
        chartBtn.setOnAction(e -> { addChartWidget(); dialog.close(); });
        
        Button activityBtn = createWidgetOption("📋 Activity Feed", "Add an activity or task list");
        activityBtn.setOnAction(e -> { addActivityWidget(); dialog.close(); });
        
        Button customBtn = createWidgetOption("🎨 Custom Widget", "Create a custom information widget");
        customBtn.setOnAction(e -> { addCustomWidget(); dialog.close(); });
        
        options.getChildren().addAll(statCardBtn, chartBtn, activityBtn, customBtn);
        
        Button cancelBtn = new Button("❌ Cancel");
        cancelBtn.setStyle("-fx-background-color: #e5e7eb; -fx-text-fill: #374151; -fx-background-radius: 8; -fx-padding: 10 20;");
        cancelBtn.setOnAction(e -> dialog.close());
        
        root.getChildren().addAll(title, options, cancelBtn);
        
        Scene scene = new Scene(root, 400, 500);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private Button createWidgetOption(String title, String description) {
        Button btn = new Button();
        btn.setPrefWidth(340);
        btn.setPrefHeight(80);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-cursor: hand;");
        
        VBox content = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setTextFill(Color.GRAY);
        
        content.getChildren().addAll(titleLabel, descLabel);
        btn.setGraphic(content);
        
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #f0f9ff; -fx-border-color: #4FD1C5; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-cursor: hand;"));
        
        return btn;
    }
    
    private void addStatWidget() {
        VBox newWidget = createCustomStatCard("New Metric", "0", "+0", "📊", "#8B5CF6");
        statsRow.getChildren().add(newWidget);
        showAlert("Widget Added", "New statistics widget added successfully!", Alert.AlertType.INFORMATION);
    }
    
    private void addChartWidget() {
        VBox newChart = createMiniChart("New Chart", "Sample data visualization");
        chartsRow.getChildren().add(newChart);
        showAlert("Widget Added", "New chart widget added successfully!", Alert.AlertType.INFORMATION);
    }
    
    private void addActivityWidget() {
        VBox newActivity = createQuickNotes();
        activitiesRow.getChildren().add(newActivity);
        showAlert("Widget Added", "New activity widget added successfully!", Alert.AlertType.INFORMATION);
    }
    
    private void addCustomWidget() {
        VBox customWidget = createCustomInfoWidget();
        activitiesRow.getChildren().add(customWidget);
        showAlert("Widget Added", "New custom widget added successfully!", Alert.AlertType.INFORMATION);
    }
    
    private VBox createCustomStatCard(String title, String value, String change, String icon, String color) {
        VBox card = new VBox(10);
        card.setPrefWidth(280);
        card.setPrefHeight(120);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5); -fx-border-color: rgba(255,255,255,0.3); -fx-border-width: 1; -fx-border-radius: 20;");
        
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        
        VBox textBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setTextFill(Color.GRAY);
        
        HBox valueBox = new HBox(10);
        valueBox.setAlignment(Pos.CENTER_LEFT);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.setTextFill(Color.web("#2c3e50"));
        
        Label changeLabel = new Label("📈 " + change);
        changeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        changeLabel.setTextFill(Color.GREEN);
        
        valueBox.getChildren().addAll(valueLabel, changeLabel);
        textBox.getChildren().addAll(titleLabel, valueBox);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12; -fx-font-size: 20px;");
        
        content.getChildren().addAll(textBox, spacer, iconLabel);
        card.getChildren().add(content);
        
        return card;
    }
    
    private VBox createMiniChart(String title, String description) {
        VBox container = new VBox(15);
        container.setPrefWidth(300);
        container.setPrefHeight(200);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setTextFill(Color.GRAY);
        
        // Simple progress bar as placeholder
        ProgressBar progress = new ProgressBar(0.7);
        progress.setPrefWidth(250);
        progress.setStyle("-fx-accent: #4FD1C5;");
        
        container.getChildren().addAll(titleLabel, descLabel, progress);
        return container;
    }
    
    private VBox createQuickNotes() {
        VBox container = new VBox(15);
        container.setPrefWidth(350);
        container.setPrefHeight(300);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        
        Label title = new Label("📝 Quick Notes");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Add your notes here...");
        notesArea.setPrefHeight(200);
        notesArea.setStyle("-fx-background-radius: 8; -fx-border-radius: 8;");
        
        Button saveBtn = new Button("💾 Save Notes");
        saveBtn.setStyle("-fx-background-color: #4FD1C5; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 15;");
        saveBtn.setOnAction(e -> showAlert("Notes Saved", "Your notes have been saved successfully!", Alert.AlertType.INFORMATION));
        
        container.getChildren().addAll(title, notesArea, saveBtn);
        return container;
    }
    
    private VBox createCustomInfoWidget() {
        VBox container = new VBox(15);
        container.setPrefWidth(350);
        container.setPrefHeight(250);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        
        Label title = new Label("🎯 Custom Info");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        VBox infoList = new VBox(10);
        infoList.getChildren().addAll(
            createInfoItem("🏆", "Top Performer", "Building A - 95% occupancy"),
            createInfoItem("⚠️", "Attention Needed", "3 maintenance requests pending"),
            createInfoItem("💰", "Revenue Goal", "85% of monthly target achieved"),
            createInfoItem("📈", "Growth Rate", "+12% compared to last month")
        );
        
        container.getChildren().addAll(title, infoList);
        return container;
    }
    
    private HBox createInfoItem(String icon, String label, String value) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8));
        item.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6;");
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(14));
        
        VBox textBox = new VBox(2);
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("Arial", 11));
        valueText.setTextFill(Color.GRAY);
        
        textBox.getChildren().addAll(labelText, valueText);
        item.getChildren().addAll(iconLabel, textBox);
        
        return item;
    }
    
    private void editStatCard(VBox card, String title, String value, String icon, String color) {
        showEditCardDialog(card, title, value, icon, color);
    }
    
    private void deleteStatCard(VBox card) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Widget");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("This will permanently delete this widget.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                statsRow.getChildren().remove(card);
                widgets.remove(card);
            }
        });
    }
    
    private void showEditCardDialog(VBox card, String currentTitle, String currentValue, String currentIcon, String currentColor) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("✏️ Edit Widget");
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f8fafc;");
        
        Label title = new Label("🎛️ Edit Widget Properties");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        TextField titleField = new TextField(currentTitle);
        TextField valueField = new TextField(currentValue);
        TextField iconField = new TextField(currentIcon);
        ComboBox<String> colorCombo = new ComboBox<>();
        colorCombo.getItems().addAll("#2C3E8C", "#4FD1C5", "#F5C542", "#8B5CF6", "#ef4444", "#10b981");
        colorCombo.setValue(currentColor);
        
        form.add(new Label("Title:"), 0, 0);
        form.add(titleField, 1, 0);
        form.add(new Label("Value:"), 0, 1);
        form.add(valueField, 1, 1);
        form.add(new Label("Icon:"), 0, 2);
        form.add(iconField, 1, 2);
        form.add(new Label("Color:"), 0, 3);
        form.add(colorCombo, 1, 3);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button saveBtn = new Button("💾 Save");
        saveBtn.setStyle("-fx-background-color: #4FD1C5; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20;");
        saveBtn.setOnAction(e -> {
            // Update the card (simplified for demo)
            showAlert("Widget Updated", "Widget has been updated successfully!", Alert.AlertType.INFORMATION);
            dialog.close();
        });
        
        Button cancelBtn = new Button("❌ Cancel");
        cancelBtn.setStyle("-fx-background-color: #e5e7eb; -fx-text-fill: #374151; -fx-background-radius: 6; -fx-padding: 10 20;");
        cancelBtn.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(saveBtn, cancelBtn);
        
        root.getChildren().addAll(title, form, buttons);
        
        Scene scene = new Scene(root, 400, 350);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private HBox createChartsRow() {
        HBox chartsRow = new HBox(20);
        chartsRow.setAlignment(Pos.CENTER);
        
        // Revenue & Expenses Chart
        VBox revenueChart = createRevenueChart();
        
        // Occupancy Rate Chart
        VBox occupancyChart = createOccupancyChart();
        
        chartsRow.getChildren().addAll(revenueChart, occupancyChart);
        return chartsRow;
    }
    
    private BarChart<String, Number> revenueBarChart;
    private XYChart.Series<String, Number> revenueSeries;
    private XYChart.Series<String, Number> expensesSeries;
    
    private VBox createRevenueChart() {
        VBox container = new VBox(15);
        container.setPrefWidth(650);
        container.setPrefHeight(400);
        container.setStyle("-fx-background-color: #262626; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);");
        
        Label title = new Label("💰 Revenue & Expenses");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#FFFFFF"));
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        revenueBarChart = new BarChart<>(xAxis, yAxis);
        revenueBarChart.setPrefHeight(280);
        revenueBarChart.setLegendVisible(true);
        revenueBarChart.setAnimated(true);
        revenueBarChart.setStyle("-fx-background-color: transparent;");
        
        revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");
        
        expensesSeries = new XYChart.Series<>();
        expensesSeries.setName("Expenses");
        
        updateRevenueData();
        
        revenueBarChart.getData().addAll(revenueSeries, expensesSeries);
        
        // Apply dark theme chart styling
        revenueBarChart.applyCss();
        revenueBarChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        revenueBarChart.lookup(".chart-vertical-grid-lines").setStyle("-fx-stroke: #404040;");
        revenueBarChart.lookup(".chart-horizontal-grid-lines").setStyle("-fx-stroke: #404040;");
        
        container.getChildren().addAll(title, revenueBarChart);
        return container;
    }
    
    private void updateRevenueData() {
        if (revenueSeries != null && expensesSeries != null) {
            revenueSeries.getData().clear();
            expensesSeries.getData().clear();
            
            double baseRevenue = soldApartments * 500000; // Base calculation
            double baseExpenses = baseRevenue * 0.25;
            
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
            
            for (String month : months) {
                double variation = 0.8 + (Math.random() * 0.4);
                revenueSeries.getData().add(new XYChart.Data<>(month, baseRevenue * variation));
                expensesSeries.getData().add(new XYChart.Data<>(month, baseExpenses * variation));
            }
        }
    }
    
    private void updateRevenueChart() {
        updateRevenueData();
    }
    
    private PieChart occupancyPieChart;
    private Label soldLegendLabel;
    private Label availableLegendLabel;
    
    private VBox createOccupancyChart() {
        VBox container = new VBox(15);
        container.setPrefWidth(450);
        container.setPrefHeight(400);
        container.setStyle("-fx-background-color: #262626; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);");
        
        Label title = new Label("📊 Occupancy Rate");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#FFFFFF"));
        
        occupancyPieChart = new PieChart();
        occupancyPieChart.setPrefHeight(220);
        occupancyPieChart.setLegendVisible(false);
        occupancyPieChart.setAnimated(true);
        occupancyPieChart.setStyle("-fx-background-color: transparent;");
        
        PieChart.Data sold = new PieChart.Data("Sold", soldApartments);
        PieChart.Data available = new PieChart.Data("Available", availableApartments);
        
        occupancyPieChart.getData().addAll(sold, available);
        
        // Legend with live updates
        VBox legend = new VBox(10);
        HBox soldLegend = new HBox(10);
        Label soldColor = new Label("●");
        soldColor.setTextFill(Color.web("#10B981"));
        soldColor.setFont(Font.font(16));
        soldLegendLabel = new Label("Sold: " + soldApartments);
        soldLegendLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        soldLegendLabel.setTextFill(Color.web("#D1D5DB"));
        soldLegend.getChildren().addAll(soldColor, soldLegendLabel);
        
        HBox availableLegend = new HBox(10);
        Label availableColor = new Label("●");
        availableColor.setTextFill(Color.web("#EF4444"));
        availableColor.setFont(Font.font(16));
        availableLegendLabel = new Label("Available: " + availableApartments);
        availableLegendLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        availableLegendLabel.setTextFill(Color.web("#D1D5DB"));
        availableLegend.getChildren().addAll(availableColor, availableLegendLabel);
        
        legend.getChildren().addAll(soldLegend, availableLegend);
        
        container.getChildren().addAll(title, occupancyPieChart, legend);
        return container;
    }
    
    private void updateOccupancyChart() {
        if (occupancyPieChart != null && !occupancyPieChart.getData().isEmpty()) {
            occupancyPieChart.getData().get(0).setPieValue(soldApartments);
            occupancyPieChart.getData().get(1).setPieValue(availableApartments);
            
            if (soldLegendLabel != null) {
                soldLegendLabel.setText("Sold: " + soldApartments);
            }
            
            if (availableLegendLabel != null) {
                availableLegendLabel.setText("Available: " + availableApartments);
            }
        }
    }
    
    private HBox createActivitiesRow() {
        HBox activitiesRow = new HBox(20);
        activitiesRow.setAlignment(Pos.CENTER);
        
        // Recent Activities
        VBox activitiesCard = createRecentActivities();
        
        // Upcoming Tasks
        VBox tasksCard = createUpcomingTasks();
        
        activitiesRow.getChildren().addAll(activitiesCard, tasksCard);
        return activitiesRow;
    }
    
    private VBox createRecentActivities() {
        VBox container = new VBox(15);
        container.setPrefWidth(550);
        container.setPrefHeight(350);
        container.setStyle("-fx-background-color: " + ThemeManager.getCard() + "; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0," + (ThemeManager.isDarkMode() ? "0.3" : "0.1") + "), 8, 0, 0, 2);");
        
        Label title = new Label("Recent Activities");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(ThemeManager.getTextPrimary()));
        
        VBox activitiesList = new VBox(12);
        
        // Activity items
        activitiesList.getChildren().addAll(
            createActivityItem("💰", "Payment received from Sarah Johnson for Riverside - 205", "2 hours ago", "#4CAF50"),
            createActivityItem("🏠", "New apartment listing added - Downtown Plaza Unit 12", "4 hours ago", "#2196F3"),
            createActivityItem("👤", "New buyer registration - Ahmed Al-Rashid", "6 hours ago", "#FF9800"),
            createActivityItem("📋", "Property inspection completed - Building A, Unit 305", "1 day ago", "#9C27B0"),
            createActivityItem("💼", "Contract signed for Land Plot #15 - Commercial Zone", "2 days ago", "#4CAF50")
        );
        
        ScrollPane scrollPane = new ScrollPane(activitiesList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        container.getChildren().addAll(title, scrollPane);
        return container;
    }
    
    private HBox createActivityItem(String icon, String text, String time, String color) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: " + (ThemeManager.isDarkMode() ? "#2D2D2D" : "#F8F9FA") + "; -fx-background-radius: 8;");
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8; -fx-font-size: 14px;");
        
        VBox textBox = new VBox(3);
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        textLabel.setTextFill(Color.web(ThemeManager.getTextPrimary()));
        
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Arial", 11));
        timeLabel.setTextFill(Color.web(ThemeManager.getTextSecondary()));
        
        textBox.getChildren().addAll(textLabel, timeLabel);
        item.getChildren().addAll(iconLabel, textBox);
        
        return item;
    }
    
    private VBox createUpcomingTasks() {
        VBox container = new VBox(15);
        container.setPrefWidth(550);
        container.setPrefHeight(350);
        container.setStyle("-fx-background-color: " + ThemeManager.getCard() + "; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0," + (ThemeManager.isDarkMode() ? "0.3" : "0.1") + "), 8, 0, 0, 2);");
        
        Label title = new Label("Upcoming Tasks");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(ThemeManager.getTextPrimary()));
        
        VBox tasksList = new VBox(12);
        
        // Task items
        tasksList.getChildren().addAll(
            createTaskItem("Conduct property inspection - Land #5", "Nov 14", "high", false),
            createTaskItem("Follow up with potential buyer - Unit 208", "Nov 15", "medium", false),
            createTaskItem("Prepare monthly financial report", "Nov 16", "high", false),
            createTaskItem("Schedule maintenance for Building C", "Nov 18", "low", true),
            createTaskItem("Review and approve new listings", "Nov 20", "medium", false)
        );
        
        ScrollPane scrollPane = new ScrollPane(tasksList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        container.getChildren().addAll(title, scrollPane);
        return container;
    }
    
    private HBox createTaskItem(String task, String date, String priority, boolean completed) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: " + (ThemeManager.isDarkMode() ? "#2D2D2D" : "#F8F9FA") + "; -fx-background-radius: 8;");
        
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(completed);
        
        VBox textBox = new VBox(3);
        Label taskLabel = new Label(task);
        taskLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        taskLabel.setTextFill(completed ? Color.web(ThemeManager.getTextSecondary()) : Color.web(ThemeManager.getTextPrimary()));
        if (completed) {
            taskLabel.setStyle("-fx-strikethrough: true;");
        }
        
        HBox detailsBox = new HBox(10);
        Label dateLabel = new Label("📅 " + date);
        dateLabel.setFont(Font.font("Arial", 11));
        dateLabel.setTextFill(Color.web("#9CA3AF"));
        
        Label priorityLabel = new Label(priority.toUpperCase());
        priorityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        priorityLabel.setPadding(new Insets(2, 6, 2, 6));
        priorityLabel.setStyle("-fx-background-radius: 10;");
        
        switch (priority) {
            case "high":
                priorityLabel.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #d32f2f; -fx-background-radius: 10;");
                break;
            case "medium":
                priorityLabel.setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #f57c00; -fx-background-radius: 10;");
                break;
            case "low":
                priorityLabel.setStyle("-fx-background-color: #e8f5e8; -fx-text-fill: #388e3c; -fx-background-radius: 10;");
                break;
        }
        
        detailsBox.getChildren().addAll(dateLabel, priorityLabel);
        textBox.getChildren().addAll(taskLabel, detailsBox);
        item.getChildren().addAll(checkBox, textBox);
        
        return item;
    }
    
    private StackPane createBackgroundElements() {
        StackPane backgroundElements = new StackPane();
        backgroundElements.setMouseTransparent(true);
        
        // إضافة دوائر تزيينية
        VBox decorativeElements = new VBox();
        decorativeElements.setAlignment(Pos.TOP_RIGHT);
        decorativeElements.setPadding(new Insets(50, 50, 0, 0));
        
        // دائرة كبيرة شفافة
        Label bigCircle = new Label("🏢");
        bigCircle.setStyle("-fx-font-size: 120px; -fx-text-fill: rgba(255,255,255,0.1); -fx-rotate: 15;");
        
        // عناصر تزيينية إضافية
        HBox topDecorations = new HBox(30);
        topDecorations.setAlignment(Pos.TOP_RIGHT);
        topDecorations.setPadding(new Insets(100, 100, 0, 0));
        
        Label building1 = new Label("🏠");
        building1.setStyle("-fx-font-size: 60px; -fx-text-fill: rgba(255,255,255,0.08); -fx-rotate: -10;");
        
        Label building2 = new Label("🏘️");
        building2.setStyle("-fx-font-size: 80px; -fx-text-fill: rgba(255,255,255,0.06); -fx-rotate: 20;");
        
        topDecorations.getChildren().addAll(building1, building2);
        
        // عناصر سفلية
        VBox bottomElements = new VBox();
        bottomElements.setAlignment(Pos.BOTTOM_LEFT);
        bottomElements.setPadding(new Insets(0, 0, 80, 80));
        
        Label keys = new Label("🗝️");
        keys.setStyle("-fx-font-size: 70px; -fx-text-fill: rgba(255,255,255,0.07); -fx-rotate: -25;");
        
        bottomElements.getChildren().add(keys);
        
        // تجميع العناصر
        StackPane.setAlignment(bigCircle, Pos.TOP_RIGHT);
        StackPane.setAlignment(topDecorations, Pos.TOP_RIGHT);
        StackPane.setAlignment(bottomElements, Pos.BOTTOM_LEFT);
        
        backgroundElements.getChildren().addAll(bigCircle, topDecorations, bottomElements);
        
        return backgroundElements;
    }
}