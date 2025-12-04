package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DashboardPage extends VBox {
    
    public DashboardPage() {
        initDashboard();
    }
    
    private void initDashboard() {
        setPadding(new Insets(0));
        setSpacing(0);
        // خلفية متدرجة احترافية مع عناصر بصرية
        setStyle("-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 25%, #f093fb 50%, #f5576c 75%, #4facfe 100%); -fx-background-size: 400% 400%; -fx-background-position: 0% 50%;");
        
        // Wrap everything in ScrollPane for better visibility
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox content = new VBox(30);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        // Header
        VBox header = createHeader();
        
        // Stats cards
        HBox statsRow = createStatsRow();
        
        // Charts section
        HBox chartsRow = createChartsRow();
        
        // Activities and Tasks section
        HBox activitiesRow = createActivitiesRow();
        
        content.getChildren().addAll(header, statsRow, chartsRow, activitiesRow);
        scrollPane.setContent(content);
        
        // إضافة عناصر الخلفية التزيينية
        StackPane mainContainer = new StackPane();
        mainContainer.getChildren().addAll(createBackgroundElements(), scrollPane);
        
        getChildren().add(mainContainer);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(5);
        
        Label title = new Label("Dashboard Overview");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Welcome back! Here's what's happening with your properties.");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#6c757d"));
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    private HBox createStatsRow() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        statsRow.getChildren().addAll(
            createStatCard("Total Buildings", "24", "+2", "🏢", "#2C3E8C"),
            createStatCard("Total Properties", "156", "+8", "📄", "#4FD1C5"),
            createStatCard("Total Buyers", "142", "+5", "👥", "#F5C542"),
            createStatCard("Land Properties", "8", "+1", "🏞️", "#8B5CF6")
        );
        
        return statsRow;
    }
    
    private VBox createStatCard(String title, String value, String change, String icon, String color) {
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
        iconLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12; -fx-font-size: 20px;");
        
        content.getChildren().addAll(textBox, spacer, iconLabel);
        card.getChildren().add(content);
        
        return card;
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
    
    private VBox createRevenueChart() {
        VBox container = new VBox(15);
        container.setPrefWidth(650);
        container.setPrefHeight(400);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        
        Label title = new Label("Revenue & Expenses");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setPrefHeight(280);
        chart.setLegendVisible(true);
        
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");
        revenueSeries.getData().add(new XYChart.Data<>("Jan", 4500000));
        revenueSeries.getData().add(new XYChart.Data<>("Feb", 5800000));
        revenueSeries.getData().add(new XYChart.Data<>("Mar", 6200000));
        revenueSeries.getData().add(new XYChart.Data<>("Apr", 4900000));
        revenueSeries.getData().add(new XYChart.Data<>("May", 7500000));
        revenueSeries.getData().add(new XYChart.Data<>("Jun", 8800000));
        
        XYChart.Series<String, Number> expensesSeries = new XYChart.Series<>();
        expensesSeries.setName("Expenses");
        expensesSeries.getData().add(new XYChart.Data<>("Jan", 1200000));
        expensesSeries.getData().add(new XYChart.Data<>("Feb", 1500000));
        expensesSeries.getData().add(new XYChart.Data<>("Mar", 1300000));
        expensesSeries.getData().add(new XYChart.Data<>("Apr", 1400000));
        expensesSeries.getData().add(new XYChart.Data<>("May", 1600000));
        expensesSeries.getData().add(new XYChart.Data<>("Jun", 1500000));
        
        chart.getData().addAll(revenueSeries, expensesSeries);
        
        container.getChildren().addAll(title, chart);
        return container;
    }
    
    private VBox createOccupancyChart() {
        VBox container = new VBox(15);
        container.setPrefWidth(450);
        container.setPrefHeight(400);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        
        Label title = new Label("Occupancy Rate");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
        PieChart chart = new PieChart();
        chart.setPrefHeight(220);
        chart.setLegendVisible(false);
        
        PieChart.Data sold = new PieChart.Data("Sold", 142);
        PieChart.Data available = new PieChart.Data("Available", 14);
        
        chart.getData().addAll(sold, available);
        
        // Legend
        VBox legend = new VBox(10);
        HBox soldLegend = new HBox(10);
        Label soldColor = new Label("●");
        soldColor.setTextFill(Color.web("#4FD1C5"));
        soldColor.setFont(Font.font(16));
        Label soldLabel = new Label("Sold                142");
        soldLegend.getChildren().addAll(soldColor, soldLabel);
        
        HBox availableLegend = new HBox(10);
        Label availableColor = new Label("●");
        availableColor.setTextFill(Color.web("#F5C542"));
        availableColor.setFont(Font.font(16));
        Label availableLabel = new Label("Available        14");
        availableLegend.getChildren().addAll(availableColor, availableLabel);
        
        legend.getChildren().addAll(soldLegend, availableLegend);
        
        container.getChildren().addAll(title, chart, legend);
        return container;
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
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        
        Label title = new Label("Recent Activities");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
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
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        container.getChildren().addAll(title, scrollPane);
        return container;
    }
    
    private HBox createActivityItem(String icon, String text, String time, String color) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8; -fx-font-size: 14px;");
        
        VBox textBox = new VBox(3);
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        textLabel.setTextFill(Color.web("#2c3e50"));
        
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Arial", 11));
        timeLabel.setTextFill(Color.GRAY);
        
        textBox.getChildren().addAll(textLabel, timeLabel);
        item.getChildren().addAll(iconLabel, textBox);
        
        return item;
    }
    
    private VBox createUpcomingTasks() {
        VBox container = new VBox(15);
        container.setPrefWidth(550);
        container.setPrefHeight(350);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        
        Label title = new Label("Upcoming Tasks");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
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
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        container.getChildren().addAll(title, scrollPane);
        return container;
    }
    
    private HBox createTaskItem(String task, String date, String priority, boolean completed) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(completed);
        
        VBox textBox = new VBox(3);
        Label taskLabel = new Label(task);
        taskLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        taskLabel.setTextFill(completed ? Color.GRAY : Color.web("#2c3e50"));
        if (completed) {
            taskLabel.setStyle("-fx-strikethrough: true;");
        }
        
        HBox detailsBox = new HBox(10);
        Label dateLabel = new Label("📅 " + date);
        dateLabel.setFont(Font.font("Arial", 11));
        dateLabel.setTextFill(Color.GRAY);
        
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