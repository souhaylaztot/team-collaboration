package com.smartpropertymanager.components;

import com.smartpropertymanager.utils.ThemeManager;

import javafx.application.Platform;
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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DashboardContent {
    private VBox root;

    public DashboardContent() {
        root = new VBox();
        root.setPadding(new Insets(24));
        root.setSpacing(24);
        root.getStyleClass().add("dashboard-container");

        // Header
        Label title = new Label("Dashboard Overview");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.getStyleClass().add("label");

        Label subtitle = new Label("Welcome back! Here's what's happening with your properties.");
        subtitle.setFont(Font.font(14));
        subtitle.getStyleClass().add("label-secondary");

        VBox header = new VBox(6, title, subtitle);
        header.getStyleClass().add("dashboard-header");
        root.getChildren().add(header);

        // Stats grid (4 columns)
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        StatCard s1 = new StatCard("Total Buildings", "24", "+2", "up", "🏢", "#2C3E8C", "#4FD1C5");
        StatCard s2 = new StatCard("Total Properties", "156", "+8", "up", "📋", "#4FD1C5", "#8B5CF6");
        StatCard s3 = new StatCard("Total Buyers", "142", "+5", "up", "👥", "#F5C542", "#F59E0B");
        StatCard s4 = new StatCard("Land Properties", "8", "+1", "up", "🏞️", "#8B5CF6", "#2C3E8C");

        s1.setPrefSize(200, 100);
        s2.setPrefSize(200, 100);
        s3.setPrefSize(200, 100);
        s4.setPrefSize(200, 100);

        statsRow.getChildren().addAll(s1, s2, s3, s4);
        root.getChildren().add(statsRow);

        // Charts section - 2/3 and 1/3 split
        HBox charts = new HBox(20);

        VBox revenueBox = createRevenueBox();
        VBox occupancyBox = createOccupancyBox();

        HBox.setHgrow(revenueBox, Priority.ALWAYS);
        HBox.setHgrow(occupancyBox, Priority.NEVER);

        // Bind widths for responsive split
        revenueBox.prefWidthProperty().bind(root.widthProperty().subtract(48).multiply(0.66));
        occupancyBox.prefWidthProperty().bind(root.widthProperty().subtract(48).multiply(0.34));

        charts.getChildren().addAll(revenueBox, occupancyBox);
        root.getChildren().add(charts);

        // Bottom row: Activities and Tasks (equal split)
        HBox bottom = new HBox(20);
        VBox activities = createActivitiesPanel();
        VBox tasks = createTasksPanel();

        HBox.setHgrow(activities, Priority.ALWAYS);
        HBox.setHgrow(tasks, Priority.ALWAYS);

        bottom.getChildren().addAll(activities, tasks);
        root.getChildren().add(bottom);
    }

    private VBox createRevenueBox() {
        VBox container = new VBox(12);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-border-radius: 8; -fx-border-color: " + ThemeManager.getBorderColor() + ";");

        Label title = new Label("Revenue & Expenses");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + ThemeManager.getTextColor() + ";");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(javafx.collections.FXCollections.observableArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun"));
        xAxis.setTickLabelFill(Color.web(ThemeManager.getSecondaryTextColor()));

        NumberAxis yAxis = new NumberAxis(0, 10000000, 2500000);
        yAxis.setTickLabelFill(Color.web(ThemeManager.getSecondaryTextColor()));

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(true);
        chart.setAnimated(false);
        chart.setCategoryGap(16);
        chart.setBarGap(6);
        chart.setPrefHeight(300);

        XYChart.Series<String, Number> rev = new XYChart.Series<>();
        rev.setName("Revenue");
        rev.getData().addAll(
                new XYChart.Data<>("Jan", 4500000),
                new XYChart.Data<>("Feb", 6500000),
                new XYChart.Data<>("Mar", 6200000),
                new XYChart.Data<>("Apr", 5000000),
                new XYChart.Data<>("May", 7500000),
                new XYChart.Data<>("Jun", 8500000)
        );

        XYChart.Series<String, Number> exp = new XYChart.Series<>();
        exp.setName("Expenses");
        exp.getData().addAll(
                new XYChart.Data<>("Jan", 1500000),
                new XYChart.Data<>("Feb", 1700000),
                new XYChart.Data<>("Mar", 1500000),
                new XYChart.Data<>("Apr", 1300000),
                new XYChart.Data<>("May", 1600000),
                new XYChart.Data<>("Jun", 1400000)
        );

        chart.getData().addAll(rev, exp);

        Platform.runLater(() -> {
            // color series: revenue #2C3E8C, expenses #4FD1C5
            for (int i = 0; i < chart.getData().size(); i++) {
                XYChart.Series<String, Number> s = chart.getData().get(i);
                String color = (i == 0) ? "#2C3E8C" : "#4FD1C5";
                for (XYChart.Data<String, Number> d : s.getData()) {
                    if (d.getNode() != null) {
                        d.getNode().setStyle("-fx-bar-fill: " + color + ";");
                        Tooltip.install(d.getNode(), new Tooltip(s.getName() + ": " + String.format("%,d", d.getYValue().intValue())));
                    }
                }
            }

            // combined Feb tooltip example
            try {
                XYChart.Data<String, Number> rFeb = rev.getData().get(1);
                XYChart.Data<String, Number> eFeb = exp.getData().get(1);
                if (rFeb.getNode() != null) Tooltip.install(rFeb.getNode(), new Tooltip("Revenue: " + String.format("%,d", rFeb.getYValue().intValue()) + "\nExpenses: " + String.format("%,d", eFeb.getYValue().intValue())));
            } catch (Exception ignored) {}
        });

        chart.prefWidthProperty().bind(container.widthProperty());

        container.getChildren().addAll(title, chart);
        VBox.setVgrow(chart, Priority.ALWAYS);
        return container;
    }

    private VBox createOccupancyBox() {
        VBox container = new VBox(12);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-border-radius: 8; -fx-border-color: " + ThemeManager.getBorderColor() + ";");
        container.setMinWidth(240);

        Label title = new Label("Occupancy Rate");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + ThemeManager.getTextColor() + ";");

        int occupied = 142;
        int available = 14;
        double pct = Math.round((occupied * 100.0 / (occupied + available)) * 10.0) / 10.0;

        PieChart pie = new PieChart();
        pie.setLegendVisible(false);
        pie.setLabelsVisible(false);
        PieChart.Data d1 = new PieChart.Data("Occupied", occupied);
        PieChart.Data d2 = new PieChart.Data("Available", available);
        pie.getData().addAll(d1, d2);
        pie.setPrefSize(200, 200);

        StackPane donut = new StackPane();
        donut.setPrefSize(200, 200);

        Circle inner = new Circle(64, Color.web(ThemeManager.getBackgroundColor()));
        inner.setStroke(Color.TRANSPARENT);

        Label centerNum = new Label(String.valueOf(occupied));
        centerNum.setFont(Font.font("System", FontWeight.BOLD, 20));
        centerNum.setStyle("-fx-text-fill: " + ThemeManager.getTextColor() + ";");

        Label centerSub = new Label("Occupied\n(" + String.format("%.0f", pct) + "% )");
        centerSub.setFont(Font.font(12));
        centerSub.setStyle("-fx-text-fill: " + ThemeManager.getSecondaryTextColor() + ";");

        VBox centerBox = new VBox(2, centerNum, centerSub);
        centerBox.setAlignment(Pos.CENTER);

        donut.getChildren().addAll(pie, inner, centerBox);

        Platform.runLater(() -> {
            if (pie.getData().size() >= 2) {
                if (d1.getNode() != null) d1.getNode().setStyle("-fx-pie-color: #4FD1C5;");
                if (d2.getNode() != null) d2.getNode().setStyle("-fx-pie-color: #F5C542;");
            }
        });

        container.getChildren().addAll(title, donut);
        return container;
    }

    private VBox createActivitiesPanel() {
        VBox container = new VBox(12);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-border-radius: 8; -fx-border-color: " + ThemeManager.getBorderColor() + ";");

        Label title = new Label("Recent Activities");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + ThemeManager.getTextColor() + ";");

        VBox list = new VBox(12);
        list.getChildren().addAll(
                activityRow(Color.web("#10B981"), "Payment received from Sarah Johnson for Riverside - 205", "2 hours ago"),
                activityRow(Color.web("#2C3E8C"), "New maintenance request – Building A", "4 hours ago"),
                activityRow(Color.web("#10B981"), "Construction permit approved for Land #3", "1 day ago"),
                activityRow(Color.web("#2C3E8C"), "New buyer registered – Garden View 412", "2 days ago"),
                activityRow(Color.web("#F5C542"), "Overdue payment alert – 1 buyer", "2 days ago")
        );

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(220);

        container.getChildren().addAll(title, scroll);
        return container;
    }

    private HBox activityRow(Color dotColor, String text, String time) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        Circle dot = new Circle(6, dotColor);
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + ThemeManager.getTextColor() + "; -fx-font-size: 13;");
        lbl.setWrapText(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label timeLbl = new Label(time);
        timeLbl.setStyle("-fx-text-fill: " + ThemeManager.getSecondaryTextColor() + "; -fx-font-size: 12;");

        row.getChildren().addAll(dot, lbl, spacer, timeLbl);
        return row;
    }

    private VBox createTasksPanel() {
        VBox container = new VBox(12);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-border-radius: 8; -fx-border-color: " + ThemeManager.getBorderColor() + ";");

        Label title = new Label("Upcoming Tasks");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + ThemeManager.getTextColor() + ";");

        VBox list = new VBox(12);
        list.getChildren().addAll(
                taskRow("Review permit application for Building C extension", "Today", "high"),
                taskRow("Schedule maintenance for Building B elevator", "Tomorrow", "medium"),
                taskRow("Follow up on pending payments from buyers", "Nov 12", "high"),
                taskRow("Conduct property inspection – Land #5", "Nov 14", "low")
        );

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(220);

        container.getChildren().addAll(title, scroll);
        return container;
    }

    private HBox taskRow(String text, String date, String priority) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        CheckBox cb = new CheckBox();
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + ThemeManager.getTextColor() + "; -fx-font-size: 13;");
        lbl.setWrapText(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label dateLbl = new Label(date);
        dateLbl.setStyle("-fx-text-fill: " + ThemeManager.getSecondaryTextColor() + "; -fx-font-size: 12;");

        Label badge = new Label();
        badge.setStyle("-fx-text-fill: white; -fx-padding: 4 10; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-size: 12; -fx-font-weight: bold;");
        switch (priority) {
            case "high":
                badge.setText("HIGH");
                badge.setStyle(badge.getStyle() + " -fx-background-color: #EF4444;");
                break;
            case "medium":
                badge.setText("MEDIUM");
                badge.setStyle(badge.getStyle() + " -fx-background-color: #F5C542;");
                break;
            default:
                badge.setText("LOW");
                badge.setStyle(badge.getStyle() + " -fx-background-color: #10B981;");
                break;
        }

        row.getChildren().addAll(cb, lbl, spacer, dateLbl, badge);
        return row;
    }

    public void refreshTheme() {
        root.setStyle("-fx-background-color: " + ThemeManager.getBackgroundColor() + ";");
        
        // Refresh all StatCards and VBox containers
        root.getChildren().forEach(node -> {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                hbox.getChildren().forEach(child -> {
                    if (child instanceof VBox) {
                        refreshVBoxStyle((VBox) child);
                    }
                });
            }
        });
    }

    private void refreshVBoxStyle(VBox vbox) {
        vbox.setStyle("-fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-border-radius: 8; -fx-border-color: " + ThemeManager.getBorderColor() + ";");
        vbox.getChildren().forEach(child -> {
            if (child instanceof Label) {
                Label label = (Label) child;
                if (label.getStyle().contains("label-secondary")) {
                    label.setStyle("-fx-text-fill: " + ThemeManager.getSecondaryTextColor() + ";");
                } else {
                    label.setStyle("-fx-text-fill: " + ThemeManager.getTextColor() + ";");
                }
            }
        });
    }

    public VBox getRoot() {
        return root;
    }
}
