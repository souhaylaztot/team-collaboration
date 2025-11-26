package com.smartpropertymanager.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StatCard extends HBox {
    public StatCard(String title, String value, String trend, String icon, String backgroundColor) {
        setPrefWidth(280);
        setPrefHeight(120);
        setPadding(new Insets(20));
        setSpacing(15);
        setStyle("-fx-background-color: white; -fx-border-radius: 12; -fx-border-color: #EEEEEE;");
        setAlignment(Pos.CENTER_LEFT);
        setCursor(javafx.scene.Cursor.HAND);

        // Left section: Title and metrics
        VBox leftSection = new VBox(8);
        leftSection.setSpacing(8);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666666;");

        HBox metricsBox = new HBox(8);
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label arrowLabel = new Label("📈");
        Label trendLabel = new Label(trend);
        trendLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #10B981; -fx-font-weight: bold;");

        metricsBox.getChildren().addAll(valueLabel, arrowLabel, trendLabel);
        leftSection.getChildren().addAll(titleLabel, metricsBox);

        // Right section: Icon
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 48; -fx-padding: 10;");
        VBox iconBox = new VBox();
        iconBox.setPrefWidth(80);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-radius: 12;");
        iconBox.getChildren().add(iconLabel);

        HBox.setHgrow(leftSection, javafx.scene.layout.Priority.ALWAYS);
        getChildren().addAll(leftSection, iconBox);

        // Hover effect
        setOnMouseEntered(e -> setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 12; -fx-border-color: #E0E0E0;"));
        setOnMouseExited(e -> setStyle("-fx-background-color: white; -fx-border-radius: 12; -fx-border-color: #EEEEEE;"));
    }
}
