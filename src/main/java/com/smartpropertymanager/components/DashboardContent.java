package com.smartpropertymanager.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DashboardContent {
    private VBox root;

    public DashboardContent() {
        root = new VBox();
        root.setPadding(new Insets(30));
        root.setSpacing(25);
        root.setStyle("-fx-background-color: #F5F5F5;");

        // Title and welcome message
        Label title = new Label("Dashboard Overview");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label welcomeMsg = new Label("Welcome back! Here's what's happening with your properties.");
        welcomeMsg.setStyle("-fx-font-size: 14; -fx-text-fill: #666666;");

        VBox headerSection = new VBox(5);
        headerSection.getChildren().addAll(title, welcomeMsg);
        root.getChildren().add(headerSection);

        // Stat cards
        HBox statCardsBox = new HBox(20);
        statCardsBox.setStyle("-fx-background-color: #F5F5F5;");
        statCardsBox.getChildren().addAll(
                new StatCard("Total Buildings", "24", "+2", "📊", "#1E40AF"),
                new StatCard("Total Properties", "156", "+8", "📋", "#10B981"),
                new StatCard("Total Buyers", "142", "+5", "👥", "#F59E0B"),
                new StatCard("Land Properties", "8", "+1", "🏞️", "#8B5CF6")
        );
        root.getChildren().add(statCardsBox);

        // Charts section
        HBox chartsBox = new HBox(20);
        chartsBox.setPrefHeight(400);

        // Revenue & Expenses chart
        VBox revenueChart = createRevenueChart();
        HBox.setHgrow(revenueChart, javafx.scene.layout.Priority.ALWAYS);
        chartsBox.getChildren().add(revenueChart);

        // Occupancy Rate chart
        VBox occupancyChart = createOccupancyChart();
        HBox.setHgrow(occupancyChart, javafx.scene.layout.Priority.ALWAYS);
        chartsBox.getChildren().add(occupancyChart);

        root.getChildren().add(chartsBox);
    }

    private VBox createRevenueChart() {
        VBox chartContainer = new VBox();
        chartContainer.setPadding(new Insets(20));
        chartContainer.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-border-color: #EEEEEE;");
        chartContainer.setSpacing(15);

        Label chartTitle = new Label("Revenue & Expenses");
        chartTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(javafx.collections.FXCollections.observableArrayList(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun"
        ));
        xAxis.setStyle("-fx-text-fill: #666666;");

        NumberAxis yAxis = new NumberAxis(0, 10000000, 2500000);
        yAxis.setStyle("-fx-text-fill: #666666;");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setStyle("-fx-background-color: white;");
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(300);

        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");
        revenueSeries.getData().addAll(
                new XYChart.Data<>("Jan", 4500000),
                new XYChart.Data<>("Feb", 6500000),
                new XYChart.Data<>("Mar", 6200000),
                new XYChart.Data<>("Apr", 5000000),
                new XYChart.Data<>("May", 7500000),
                new XYChart.Data<>("Jun", 8500000)
        );

        XYChart.Series<String, Number> expensesSeries = new XYChart.Series<>();
        expensesSeries.setName("Expenses");
        expensesSeries.getData().addAll(
                new XYChart.Data<>("Jan", 1500000),
                new XYChart.Data<>("Feb", 1700000),
                new XYChart.Data<>("Mar", 1500000),
                new XYChart.Data<>("Apr", 1300000),
                new XYChart.Data<>("May", 1600000),
                new XYChart.Data<>("Jun", 1400000)
        );

        barChart.getData().addAll(revenueSeries, expensesSeries);

        javafx.application.Platform.runLater(() -> {
            if (revenueSeries.getNode() != null) {
                revenueSeries.getNode().setStyle("-fx-bar-fill: #1E40AF;");
            }
            if (expensesSeries.getNode() != null) {
                expensesSeries.getNode().setStyle("-fx-bar-fill: #5FD3BC;");
            }
        });

        chartContainer.getChildren().addAll(chartTitle, barChart);
        VBox.setVgrow(barChart, javafx.scene.layout.Priority.ALWAYS);

        return chartContainer;
    }

    private VBox createOccupancyChart() {
        VBox chartContainer = new VBox();
        chartContainer.setPadding(new Insets(20));
        chartContainer.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-border-color: #EEEEEE;");
        chartContainer.setSpacing(15);
        chartContainer.setAlignment(Pos.TOP_CENTER);

        Label chartTitle = new Label("Occupancy Rate");
        chartTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Donut chart (simulated with circles)
        HBox donutContainer = new HBox();
        donutContainer.setAlignment(Pos.CENTER);
        donutContainer.setPrefHeight(250);

        VBox donut = createDonutChart();
        donutContainer.getChildren().add(donut);

        chartContainer.getChildren().addAll(chartTitle, donutContainer);
        VBox.setVgrow(chartContainer, javafx.scene.layout.Priority.ALWAYS);

        return chartContainer;
    }

    private VBox createDonutChart() {
        VBox donut = new VBox();
        donut.setAlignment(Pos.CENTER);
        donut.setPrefSize(200, 200);

        // Create nested circles for donut effect
        Circle outerCircle = new Circle(100);
        outerCircle.setFill(Color.web("#5FD3BC"));

        Circle innerCircle = new Circle(60);
        innerCircle.setFill(Color.web("#F5F5F5"));

        Circle segment = new Circle(100);
        segment.setFill(Color.web("#F59E0B"));

        // Stack the circles to create donut
        donut.getChildren().addAll(outerCircle, innerCircle);

        return donut;
    }

    public VBox getRoot() {
        return root;
    }
}
