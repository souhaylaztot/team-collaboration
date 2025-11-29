package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ReportsPage extends VBox {
    
    public ReportsPage() {
        initPage();
    }
    
    private void initPage() {
        setPadding(new Insets(20));
        setSpacing(20);
        setStyle("-fx-background-color: #f8f9fa;");
        
        // Create ScrollPane for better visibility
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        
        HBox header = createHeader();
        HBox summaryStats = createSummaryStats();
        VBox revenueChart = createRevenueChart();
        HBox chartsRow = createChartsRow();
        VBox rentChart = createRentChart();
        VBox quickReports = createQuickReports();
        
        content.getChildren().addAll(header, summaryStats, revenueChart, chartsRow, rentChart, quickReports);
        scrollPane.setContent(content);
        getChildren().add(scrollPane);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Reports & Analytics");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Comprehensive insights and performance metrics");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#6c757d"));
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_RIGHT);
        
        ComboBox<String> periodCombo = new ComboBox<>();
        periodCombo.getItems().addAll("This Week", "This Month", "This Quarter", "This Year");
        periodCombo.setValue("This Year");
        periodCombo.setPrefWidth(140);
        
        Button exportBtn = new Button("📥 Export Report");
        exportBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20; -fx-font-weight: bold;");
        
        controls.getChildren().addAll(periodCombo, exportBtn);
        header.getChildren().addAll(titleBox, spacer, controls);
        return header;
    }
    
    private HBox createSummaryStats() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        statsRow.getChildren().addAll(
            createStatCard("Total Revenue (YTD)", "158,500,000 MAD", "+12.5%", "💰", "#4CAF50"),
            createStatCard("Total Properties", "180", "+8", "🏢", "#2C3E8C"),
            createStatCard("Total Buyers", "142", "+5", "👥", "#4FD1C5"),
            createStatCard("Avg. Sales Rate", "91.5%", "+2.3%", "📈", "#F5C542")
        );
        
        return statsRow;
    }
    
    private VBox createStatCard(String title, String value, String change, String icon, String color) {
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
        
        HBox valueBox = new HBox(10);
        valueBox.setAlignment(Pos.CENTER_LEFT);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        valueLabel.setTextFill(Color.web("#2c3e50"));
        
        Label changeLabel = new Label("📈 " + change);
        changeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        changeLabel.setTextFill(Color.GREEN);
        
        valueBox.getChildren().addAll(valueLabel, changeLabel);
        textBox.getChildren().addAll(titleLabel, valueBox);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12; -fx-font-size: 20px;");
        
        content.getChildren().addAll(textBox, spacer, iconLabel);
        card.getChildren().add(content);
        
        return card;
    }
    
    private VBox createRevenueChart() {
        VBox container = new VBox(15);
        container.setPrefHeight(420);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("📊 Revenue, Expenses & Profit Trends");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        AreaChart<String, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setPrefHeight(320);
        chart.setLegendVisible(true);
        
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");
        revenueSeries.getData().add(new XYChart.Data<>("Jan", 12500000));
        revenueSeries.getData().add(new XYChart.Data<>("Feb", 13200000));
        revenueSeries.getData().add(new XYChart.Data<>("Mar", 12800000));
        revenueSeries.getData().add(new XYChart.Data<>("Apr", 14500000));
        revenueSeries.getData().add(new XYChart.Data<>("May", 15500000));
        revenueSeries.getData().add(new XYChart.Data<>("Jun", 16200000));
        revenueSeries.getData().add(new XYChart.Data<>("Jul", 15800000));
        revenueSeries.getData().add(new XYChart.Data<>("Aug", 16500000));
        revenueSeries.getData().add(new XYChart.Data<>("Sep", 17200000));
        revenueSeries.getData().add(new XYChart.Data<>("Oct", 16800000));
        revenueSeries.getData().add(new XYChart.Data<>("Nov", 17500000));
        
        XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
        profitSeries.setName("Profit");
        profitSeries.getData().add(new XYChart.Data<>("Jan", 8000000));
        profitSeries.getData().add(new XYChart.Data<>("Feb", 8400000));
        profitSeries.getData().add(new XYChart.Data<>("Mar", 7600000));
        profitSeries.getData().add(new XYChart.Data<>("Apr", 9600000));
        profitSeries.getData().add(new XYChart.Data<>("May", 10400000));
        profitSeries.getData().add(new XYChart.Data<>("Jun", 10900000));
        profitSeries.getData().add(new XYChart.Data<>("Jul", 10300000));
        profitSeries.getData().add(new XYChart.Data<>("Aug", 11300000));
        profitSeries.getData().add(new XYChart.Data<>("Sep", 11800000));
        profitSeries.getData().add(new XYChart.Data<>("Oct", 11200000));
        profitSeries.getData().add(new XYChart.Data<>("Nov", 11700000));
        
        chart.getData().addAll(revenueSeries, profitSeries);
        
        container.getChildren().addAll(title, chart);
        return container;
    }
    
    private HBox createChartsRow() {
        HBox chartsRow = new HBox(20);
        chartsRow.setAlignment(Pos.CENTER);
        
        VBox propertyChart = createPropertyDistributionChart();
        VBox occupancyChart = createOccupancyChart();
        
        chartsRow.getChildren().addAll(propertyChart, occupancyChart);
        return chartsRow;
    }
    
    private VBox createPropertyDistributionChart() {
        VBox container = new VBox(15);
        container.setPrefWidth(550);
        container.setPrefHeight(450);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Property Type Distribution");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
        PieChart chart = new PieChart();
        chart.setPrefHeight(280);
        
        PieChart.Data residential = new PieChart.Data("Residential", 156);
        PieChart.Data commercial = new PieChart.Data("Commercial", 12);
        PieChart.Data land = new PieChart.Data("Land", 8);
        PieChart.Data industrial = new PieChart.Data("Industrial", 4);
        
        chart.getData().addAll(residential, commercial, land, industrial);
        
        // Legend
        GridPane legend = new GridPane();
        legend.setHgap(20);
        legend.setVgap(8);
        
        addLegendItem(legend, 0, 0, "Residential", "156", "#2C3E8C");
        addLegendItem(legend, 1, 0, "Commercial", "12", "#4FD1C5");
        addLegendItem(legend, 0, 1, "Land", "8", "#F5C542");
        addLegendItem(legend, 1, 1, "Industrial", "4", "#8B5CF6");
        
        container.getChildren().addAll(title, chart, legend);
        return container;
    }
    
    private void addLegendItem(GridPane grid, int col, int row, String name, String value, String color) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label colorDot = new Label("●");
        colorDot.setTextFill(Color.web(color));
        colorDot.setFont(Font.font(14));
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", 12));
        nameLabel.setTextFill(Color.GRAY);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        valueLabel.setTextFill(Color.web("#2c3e50"));
        
        item.getChildren().addAll(colorDot, nameLabel, spacer, valueLabel);
        grid.add(item, col, row);
    }
    
    private VBox createOccupancyChart() {
        VBox container = new VBox(15);
        container.setPrefWidth(550);
        container.setPrefHeight(450);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Occupancy Rate Trend");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(80, 100, 5);
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setPrefHeight(320);
        chart.setLegendVisible(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Jan", 88));
        series.getData().add(new XYChart.Data<>("Feb", 90));
        series.getData().add(new XYChart.Data<>("Mar", 87));
        series.getData().add(new XYChart.Data<>("Apr", 91));
        series.getData().add(new XYChart.Data<>("May", 92));
        series.getData().add(new XYChart.Data<>("Jun", 94));
        series.getData().add(new XYChart.Data<>("Jul", 93));
        series.getData().add(new XYChart.Data<>("Aug", 95));
        series.getData().add(new XYChart.Data<>("Sep", 94));
        series.getData().add(new XYChart.Data<>("Oct", 91));
        series.getData().add(new XYChart.Data<>("Nov", 91));
        
        chart.getData().add(series);
        
        container.getChildren().addAll(title, chart);
        return container;
    }
    
    private VBox createRentChart() {
        VBox container = new VBox(15);
        container.setPrefHeight(380);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Rent Collection Performance");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setPrefHeight(280);
        chart.setLegendVisible(true);
        
        XYChart.Series<String, Number> onTimeSeries = new XYChart.Series<>();
        onTimeSeries.setName("On Time %");
        onTimeSeries.getData().add(new XYChart.Data<>("Jan", 92));
        onTimeSeries.getData().add(new XYChart.Data<>("Feb", 94));
        onTimeSeries.getData().add(new XYChart.Data<>("Mar", 90));
        onTimeSeries.getData().add(new XYChart.Data<>("Apr", 95));
        onTimeSeries.getData().add(new XYChart.Data<>("May", 93));
        onTimeSeries.getData().add(new XYChart.Data<>("Jun", 96));
        
        XYChart.Series<String, Number> lateSeries = new XYChart.Series<>();
        lateSeries.setName("Late %");
        lateSeries.getData().add(new XYChart.Data<>("Jan", 6));
        lateSeries.getData().add(new XYChart.Data<>("Feb", 4));
        lateSeries.getData().add(new XYChart.Data<>("Mar", 7));
        lateSeries.getData().add(new XYChart.Data<>("Apr", 3));
        lateSeries.getData().add(new XYChart.Data<>("May", 5));
        lateSeries.getData().add(new XYChart.Data<>("Jun", 3));
        
        XYChart.Series<String, Number> unpaidSeries = new XYChart.Series<>();
        unpaidSeries.setName("Unpaid %");
        unpaidSeries.getData().add(new XYChart.Data<>("Jan", 2));
        unpaidSeries.getData().add(new XYChart.Data<>("Feb", 2));
        unpaidSeries.getData().add(new XYChart.Data<>("Mar", 3));
        unpaidSeries.getData().add(new XYChart.Data<>("Apr", 2));
        unpaidSeries.getData().add(new XYChart.Data<>("May", 2));
        unpaidSeries.getData().add(new XYChart.Data<>("Jun", 1));
        
        chart.getData().addAll(onTimeSeries, lateSeries, unpaidSeries);
        
        container.getChildren().addAll(title, chart);
        return container;
    }
    
    private VBox createQuickReports() {
        VBox container = new VBox(15);
        container.setPrefHeight(150);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Quick Reports");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
        HBox reportsRow = new HBox(20);
        reportsRow.setAlignment(Pos.CENTER);
        
        Button monthlyBtn = createReportButton("📄", "Monthly Revenue Report", "#2C3E8C");
        Button propertyBtn = createReportButton("🏢", "Property Performance", "#4FD1C5");
        Button buyerBtn = createReportButton("👥", "Buyer Analysis", "#F5C542");
        
        reportsRow.getChildren().addAll(monthlyBtn, propertyBtn, buyerBtn);
        
        container.getChildren().addAll(title, reportsRow);
        return container;
    }
    
    private Button createReportButton(String icon, String text, String color) {
        Button button = new Button();
        button.setPrefSize(200, 80);
        button.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e9ecef; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(24));
        iconLabel.setTextFill(Color.web(color));
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        textLabel.setTextFill(Color.web("#2c3e50"));
        textLabel.setWrapText(true);
        
        content.getChildren().addAll(iconLabel, textLabel);
        button.setGraphic(content);
        
        return button;
    }
}