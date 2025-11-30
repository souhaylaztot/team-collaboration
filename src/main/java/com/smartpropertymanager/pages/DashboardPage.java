package com.smartpropertymanager.pages;

import com.smartpropertymanager.components.DashboardContent;
import com.smartpropertymanager.utils.ThemeManager;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardPage implements Page {
    private VBox content;
    private DashboardContent dashboard;

    public DashboardPage() {
        content = new VBox();
        content.getStyleClass().add("dashboard-container");
        content.setPadding(new Insets(30));
        content.setSpacing(20);

        Label title = new Label("Dashboard");
        title.getStyleClass().add("label");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        dashboard = new DashboardContent();
        
        content.getChildren().addAll(title, dashboard.getRoot());
    }

    @Override
    public VBox getContent() {
        return content;
    }

    @Override
    public String getTitle() {
        return "Dashboard";
    }
    
    // Theme refresh no longer needed - CSS handles it automatically
}
