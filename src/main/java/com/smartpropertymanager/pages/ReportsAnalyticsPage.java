package com.smartpropertymanager.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Arrays;
import java.util.List;

public class ReportsAnalyticsPage implements Page {
    private VBox content;
    private String title = "Reports & Analytics";

    public ReportsAnalyticsPage() {
        content = new VBox();
        content.setStyle("-fx-background-color: #F8FAFC;");
        content.setPadding(new Insets(32));
        content.setSpacing(24);
        
        createUI();
    }

    private void createUI() {
        // Header
        HBox header = createHeader();
        content.getChildren().add(header);

        // Summary Stats
        HBox summaryStats = createSummaryStats();
        content.getChildren().add(summaryStats);

        // Revenue & Profit Chart
        VBox revenueChart = createRevenueChart();
        content.getChildren().add(revenueChart);

        // Property Distribution & Occupancy
        HBox chartsRow1 = createChartsRow1();
        content.getChildren().add(chartsRow1);

        // Rent Collection Performance
        VBox rentCollectionChart = createRentCollectionChart();
        content.getChildren().add(rentCollectionChart);

        // Quick Reports
        VBox quickReports = createQuickReports();
        content.getChildren().add(quickReports);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        VBox titleSection = new VBox(8);
        Label titleLabel = new Label("Reports & Analytics");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label subtitleLabel = new Label("Comprehensive insights and performance metrics");
        subtitleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Time period selector
        ComboBox<String> timePeriodCombo = new ComboBox<>();
        timePeriodCombo.getItems().addAll("This Week", "This Month", "This Quarter", "This Year");
        timePeriodCombo.setValue("This Year");
        timePeriodCombo.setStyle("-fx-background-color: white; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-padding: 8 12;");

        Button exportBtn = new Button("Export Report");
        exportBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        exportBtn.setOnAction(e -> showExportDialog());

        HBox controls = new HBox(12, timePeriodCombo, exportBtn);
        controls.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(titleSection, spacer, controls);
        return header;
    }

    private HBox createSummaryStats() {
        HBox statsGrid = new HBox(24);
        statsGrid.setAlignment(Pos.CENTER_LEFT);

        // Summary stats data
        VBox revenueCard = createStatCard("Total Revenue (YTD)", "158,500,000 MAD", "+12.5%", "💰", "#10B981", "#059669");
        VBox propertiesCard = createStatCard("Total Properties", "180", "+8", "🏢", "#2C3E8C", "#4a5fb8");
        VBox buyersCard = createStatCard("Total Buyers", "142", "+5", "👥", "#4FD1C5", "#3ab5a8");
        VBox salesRateCard = createStatCard("Avg. Sales Rate", "91.5%", "+2.3%", "📈", "#F5C542", "#d9a82e");

        statsGrid.getChildren().addAll(revenueCard, propertiesCard, buyersCard, salesRateCard);
        return statsGrid;
    }

    private VBox createStatCard(String title, String value, String change, String icon, String startColor, String endColor) {
        VBox card = new VBox();
        card.setPrefSize(280, 120);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(16);

        VBox textSection = new VBox(8);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        HBox valueRow = new HBox(8);
        valueRow.setAlignment(Pos.CENTER_LEFT);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 20; -fx-font-weight: bold;");

        Label changeLabel = new Label(change);
        changeLabel.setStyle("-fx-text-fill: #10B981; -fx-font-size: 14; -fx-font-weight: bold;");

        valueRow.getChildren().addAll(valueLabel, changeLabel);
        textSection.getChildren().addAll(titleLabel, valueRow);

        // Icon with gradient background
        StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(48, 48);
        iconContainer.setStyle(String.format("-fx-background-color: linear-gradient(to bottom right, %s, %s); -fx-background-radius: 12;", startColor, endColor));
        iconContainer.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18;");

        iconContainer.getChildren().add(iconLabel);

        Region spacerRegion = new Region();
        HBox.setHgrow(spacerRegion, Priority.ALWAYS);

        content.getChildren().addAll(textSection, spacerRegion, iconContainer);
        card.getChildren().add(content);

        return card;
    }

    private VBox createRevenueChart() {
        VBox chartContainer = new VBox();
        chartContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        chartContainer.setPadding(new Insets(24));

        // Header
        HBox chartHeader = new HBox(8);
        chartHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label chartIcon = new Label("📊");
        chartIcon.setStyle("-fx-font-size: 20;");
        
        Label chartTitle = new Label("Revenue, Expenses & Profit Trends");
        chartTitle.setStyle("-fx-text-fill: #000000; -fx-font-size: 18; -fx-font-weight: bold;");
        
        chartHeader.getChildren().addAll(chartIcon, chartTitle);

        // Revenue data
        ObservableList<XYChart.Series<String, Number>> revenueData = createRevenueData();

        // Create area chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableArrayList(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
            "Jul", "Aug", "Sep", "Oct", "Nov"
        ));
        xAxis.setTickLabelFill(javafx.scene.paint.Color.web("#6B7280"));

        NumberAxis yAxis = new NumberAxis(0, 20000000, 5000000);
        yAxis.setTickLabelFill(javafx.scene.paint.Color.web("#6B7280"));
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "", "M"));

        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setData(revenueData);
        areaChart.setLegendVisible(false);
        areaChart.setAnimated(false);
        areaChart.setPrefHeight(350);
        areaChart.setStyle("-fx-background-color: transparent;");

        // Style the series
        areaChart.lookup(".chart-series-area-line.series0").setStyle("-fx-stroke: #2C3E8C; -fx-stroke-width: 2px;");
        areaChart.lookup(".chart-series-area-fill.series0").setStyle("-fx-fill: linear-gradient(from 0% 0% to 0% 100%, #2C3E8C80, #2C3E8C00);");

        areaChart.lookup(".chart-series-area-line.series1").setStyle("-fx-stroke: #4FD1C5; -fx-stroke-width: 2px;");
        areaChart.lookup(".chart-series-area-fill.series1").setStyle("-fx-fill: linear-gradient(from 0% 0% to 0% 100%, #4FD1C580, #4FD1C500);");

        chartContainer.getChildren().addAll(chartHeader, areaChart);
        return chartContainer;
    }

    private ObservableList<XYChart.Series<String, Number>> createRevenueData() {
        // Revenue series
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.getData().addAll(
            new XYChart.Data<>("Jan", 12500000),
            new XYChart.Data<>("Feb", 13200000),
            new XYChart.Data<>("Mar", 12800000),
            new XYChart.Data<>("Apr", 14500000),
            new XYChart.Data<>("May", 15500000),
            new XYChart.Data<>("Jun", 16200000),
            new XYChart.Data<>("Jul", 15800000),
            new XYChart.Data<>("Aug", 16500000),
            new XYChart.Data<>("Sep", 17200000),
            new XYChart.Data<>("Oct", 16800000),
            new XYChart.Data<>("Nov", 17500000)
        );

        // Profit series
        XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
        profitSeries.getData().addAll(
            new XYChart.Data<>("Jan", 8000000),
            new XYChart.Data<>("Feb", 8400000),
            new XYChart.Data<>("Mar", 7600000),
            new XYChart.Data<>("Apr", 9600000),
            new XYChart.Data<>("May", 10400000),
            new XYChart.Data<>("Jun", 10900000),
            new XYChart.Data<>("Jul", 10300000),
            new XYChart.Data<>("Aug", 11300000),
            new XYChart.Data<>("Sep", 11800000),
            new XYChart.Data<>("Oct", 11200000),
            new XYChart.Data<>("Nov", 11700000)
        );

        return FXCollections.observableArrayList(revenueSeries, profitSeries);
    }

    private HBox createChartsRow1() {
        HBox chartsRow = new HBox(24);
        chartsRow.setAlignment(Pos.TOP_LEFT);

        VBox propertyDistribution = createPropertyDistributionChart();
        VBox occupancyTrend = createOccupancyTrendChart();

        HBox.setHgrow(propertyDistribution, Priority.ALWAYS);
        HBox.setHgrow(occupancyTrend, Priority.ALWAYS);

        chartsRow.getChildren().addAll(propertyDistribution, occupancyTrend);
        return chartsRow;
    }

    private VBox createPropertyDistributionChart() {
        VBox chartContainer = new VBox();
        chartContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        chartContainer.setPadding(new Insets(24));

        Label chartTitle = new Label("Property Type Distribution");
        chartTitle.setStyle("-fx-text-fill: #000000; -fx-font-size: 18; -fx-font-weight: bold;");

        // Pie chart data
        PieChart pieChart = new PieChart();
        pieChart.setData(FXCollections.observableArrayList(
            new PieChart.Data("Residential", 156),
            new PieChart.Data("Commercial", 12),
            new PieChart.Data("Land", 8),
            new PieChart.Data("Industrial", 4)
        ));
        pieChart.setLegendVisible(false);
        pieChart.setLabelsVisible(false);
        pieChart.setPrefSize(400, 300);

        // Color the slices
        pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #2C3E8C;");
        pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #4FD1C5;");
        pieChart.getData().get(2).getNode().setStyle("-fx-pie-color: #F5C542;");
        pieChart.getData().get(3).getNode().setStyle("-fx-pie-color: #8B5CF6;");

        // Legend
        GridPane legend = new GridPane();
        legend.setHgap(16);
        legend.setVgap(8);
        legend.setPadding(new Insets(16, 0, 0, 0));

        String[] categories = {"Residential", "Commercial", "Land", "Industrial"};
        String[] colors = {"#2C3E8C", "#4FD1C5", "#F5C542", "#8B5CF6"};
        int[] values = {156, 12, 8, 4};

        for (int i = 0; i < categories.length; i++) {
            HBox legendItem = new HBox(8);
            legendItem.setAlignment(Pos.CENTER_LEFT);

            StackPane colorBox = new StackPane();
            colorBox.setPrefSize(12, 12);
            colorBox.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 2;", colors[i]));

            Label nameLabel = new Label(categories[i]);
            nameLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

            Region spacerRegion = new Region();
            HBox.setHgrow(spacerRegion, Priority.ALWAYS);

            Label valueLabel = new Label(String.valueOf(values[i]));
            valueLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-font-weight: bold;");

            legendItem.getChildren().addAll(colorBox, nameLabel, spacerRegion, valueLabel);
            legend.add(legendItem, i % 2, i / 2);
        }

        chartContainer.getChildren().addAll(chartTitle, pieChart, legend);
        return chartContainer;
    }

    private VBox createOccupancyTrendChart() {
        VBox chartContainer = new VBox();
        chartContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        chartContainer.setPadding(new Insets(24));

        Label chartTitle = new Label("Occupancy Rate Trend");
        chartTitle.setStyle("-fx-text-fill: #000000; -fx-font-size: 18; -fx-font-weight: bold;");

        // Line chart data
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableArrayList(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
            "Jul", "Aug", "Sep", "Oct", "Nov"
        ));
        xAxis.setTickLabelFill(javafx.scene.paint.Color.web("#6B7280"));

        NumberAxis yAxis = new NumberAxis(80, 100, 5);
        yAxis.setTickLabelFill(javafx.scene.paint.Color.web("#6B7280"));

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(
            new XYChart.Data<>("Jan", 88),
            new XYChart.Data<>("Feb", 90),
            new XYChart.Data<>("Mar", 87),
            new XYChart.Data<>("Apr", 91),
            new XYChart.Data<>("May", 92),
            new XYChart.Data<>("Jun", 94),
            new XYChart.Data<>("Jul", 93),
            new XYChart.Data<>("Aug", 95),
            new XYChart.Data<>("Sep", 94),
            new XYChart.Data<>("Oct", 91),
            new XYChart.Data<>("Nov", 91)
        );

        lineChart.setData(FXCollections.observableArrayList(series));
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setPrefSize(400, 300);
        lineChart.setStyle("-fx-background-color: transparent;");

        // Style the line
        lineChart.lookup(".chart-series-line").setStyle("-fx-stroke: #4FD1C5; -fx-stroke-width: 3px;");
        lineChart.lookup(".chart-line-symbol").setStyle("-fx-background-color: #4FD1C5, white; -fx-background-radius: 5px; -fx-padding: 5px;");

        chartContainer.getChildren().addAll(chartTitle, lineChart);
        return chartContainer;
    }

    private VBox createRentCollectionChart() {
        VBox chartContainer = new VBox();
        chartContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        chartContainer.setPadding(new Insets(24));

        Label chartTitle = new Label("Payment Collection Performance");
        chartTitle.setStyle("-fx-text-fill: #000000; -fx-font-size: 18; -fx-font-weight: bold;");

        // Bar chart data
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun"));
        xAxis.setTickLabelFill(javafx.scene.paint.Color.web("#6B7280"));

        NumberAxis yAxis = new NumberAxis(0, 100, 20);
        yAxis.setTickLabelFill(javafx.scene.paint.Color.web("#6B7280"));

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        
        // On Time series
        XYChart.Series<String, Number> onTimeSeries = new XYChart.Series<>();
        onTimeSeries.getData().addAll(
            new XYChart.Data<>("Jan", 92),
            new XYChart.Data<>("Feb", 94),
            new XYChart.Data<>("Mar", 90),
            new XYChart.Data<>("Apr", 95),
            new XYChart.Data<>("May", 93),
            new XYChart.Data<>("Jun", 96)
        );

        // Late series
        XYChart.Series<String, Number> lateSeries = new XYChart.Series<>();
        lateSeries.getData().addAll(
            new XYChart.Data<>("Jan", 6),
            new XYChart.Data<>("Feb", 4),
            new XYChart.Data<>("Mar", 7),
            new XYChart.Data<>("Apr", 3),
            new XYChart.Data<>("May", 5),
            new XYChart.Data<>("Jun", 3)
        );

        // Unpaid series
        XYChart.Series<String, Number> unpaidSeries = new XYChart.Series<>();
        unpaidSeries.getData().addAll(
            new XYChart.Data<>("Jan", 2),
            new XYChart.Data<>("Feb", 2),
            new XYChart.Data<>("Mar", 3),
            new XYChart.Data<>("Apr", 2),
            new XYChart.Data<>("May", 2),
            new XYChart.Data<>("Jun", 1)
        );

        barChart.setData(FXCollections.observableArrayList(onTimeSeries, lateSeries, unpaidSeries));
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.setCategoryGap(20);
        barChart.setBarGap(5);
        barChart.setPrefHeight(300);
        barChart.setStyle("-fx-background-color: transparent;");

        chartContainer.getChildren().addAll(chartTitle, barChart);
        return chartContainer;
    }

    private VBox createQuickReports() {
        VBox reportsContainer = new VBox();
        reportsContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        reportsContainer.setPadding(new Insets(24));

        Label sectionTitle = new Label("Quick Reports");
        sectionTitle.setStyle("-fx-text-fill: #000000; -fx-font-size: 18; -fx-font-weight: bold;");

        HBox reportsGrid = new HBox(16);
        reportsGrid.setAlignment(Pos.CENTER);

        // Monthly Revenue Report Button
        VBox revenueReportBtn = createReportButton("📄", "Monthly Revenue Report", "#2C3E8C");
        
        // Property Performance Button
        VBox propertyReportBtn = createReportButton("🏢", "Property Performance", "#4FD1C5");
        
        // Buyer Analysis Button
        VBox buyerReportBtn = createReportButton("👥", "Buyer Analysis", "#F5C542");

        reportsGrid.getChildren().addAll(revenueReportBtn, propertyReportBtn, buyerReportBtn);
        reportsContainer.getChildren().addAll(sectionTitle, reportsGrid);

        return reportsContainer;
    }

    private VBox createReportButton(String icon, String text, String color) {
        VBox buttonContainer = new VBox(8);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 8; -fx-padding: 16; -fx-cursor: hand;");
        buttonContainer.setOnMouseEntered(e -> buttonContainer.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #D1D5DB; -fx-border-radius: 8; -fx-padding: 16; -fx-cursor: hand;"));
        buttonContainer.setOnMouseExited(e -> buttonContainer.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 8; -fx-padding: 16; -fx-cursor: hand;"));
        buttonContainer.setOnMouseClicked(e -> showReportDetails(text));

        Label iconLabel = new Label(icon);
        iconLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 24;", color));

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-text-alignment: center; -fx-wrap-text: true;");
        textLabel.setAlignment(Pos.CENTER);

        buttonContainer.getChildren().addAll(iconLabel, textLabel);
        return buttonContainer;
    }

    private void showExportDialog() {
        Alert exportAlert = new Alert(Alert.AlertType.INFORMATION);
        exportAlert.setTitle("Export Report");
        exportAlert.setHeaderText("Export Options");
        exportAlert.setContentText("Select format to export:\n\n" +
                "• PDF - Full formatted report\n" +
                "• Excel - Spreadsheet with data tables\n" +
                "• CSV - Raw data in comma-separated format\n\n" +
                "Export functionality ready for integration.");
        exportAlert.show();
    }

    private void showReportDetails(String reportType) {
        Alert reportAlert = new Alert(Alert.AlertType.INFORMATION);
        reportAlert.setTitle("Report: " + reportType);
        reportAlert.setHeaderText(reportType);
        reportAlert.setContentText("Loading " + reportType + "...\n\n" +
                "This report contains comprehensive data and insights.\n\n" +
                "• Total data points: 1,200+\n" +
                "• Time period: Current Year (Jan - Nov)\n" +
                "• Generated: " + new java.text.SimpleDateFormat("MMM dd, yyyy").format(new java.util.Date()) + "\n\n" +
                "Full report view available after clicking.");
        reportAlert.show();
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
