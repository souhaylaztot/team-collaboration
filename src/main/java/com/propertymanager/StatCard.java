package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatCard extends VBox {
    
    public StatCard(String title, String value, String change, String icon, String color) {
        initCard(title, value, change, icon, color);
    }
    
    private void initCard(String title, String value, String change, String icon, String color) {
        setPrefWidth(280);
        setPrefHeight(120);
        setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #e9ecef; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        setPadding(new Insets(20));
        setSpacing(10);
        
        // Header with title and icon
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 14));
        titleLabel.setTextFill(Color.GRAY);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8; -fx-font-size: 16px;");
        
        header.getChildren().addAll(titleLabel, iconLabel);
        
        // Value and change
        HBox valueBox = new HBox(10);
        valueBox.setAlignment(Pos.CENTER_LEFT);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.web("#2c3e50"));
        
        Label changeLabel = new Label(change);
        changeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        changeLabel.setTextFill(change.startsWith("+") ? Color.GREEN : Color.RED);
        
        valueBox.getChildren().addAll(valueLabel, changeLabel);
        
        getChildren().addAll(header, valueBox);
    }
}