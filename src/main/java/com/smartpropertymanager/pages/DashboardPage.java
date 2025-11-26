package com.smartpropertymanager.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import com.smartpropertymanager.components.DashboardContent;

public class DashboardPage implements Page {
    private VBox content;

    public DashboardPage() {
        content = new VBox();
        content.setStyle("-fx-background-color: #F5F5F5;");
        content.setPadding(new Insets(30));
        content.setSpacing(20);

        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");

        DashboardContent dashboard = new DashboardContent();
        
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
}
