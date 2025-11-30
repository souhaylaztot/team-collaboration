package com.smartpropertymanager.components;

import com.smartpropertymanager.utils.ThemeManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatCard extends HBox {

    // New full constructor used by DashboardContent
    public StatCard(String title, String value, String change, String trend, String iconEmoji, String startColor, String endColor) {
        init(title, value, change, trend, iconEmoji, startColor, endColor);
    }

    // Backwards-compatible constructor used elsewhere (no gradient end color)
    public StatCard(String title, String value, String change, String iconEmoji, String startColor) {
        init(title, value, change, "up", iconEmoji, startColor, startColor);
    }

    private void init(String title, String value, String change, String trend, String iconEmoji, String startColor, String endColor) {
        setPrefWidth(200);
        setPrefHeight(100);
        setPadding(new Insets(16));
        setSpacing(12);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("stat-card");

        VBox left = new VBox(6);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(13));
        titleLabel.getStyleClass().add("label-secondary");

        HBox valueRow = new HBox(8);
        valueRow.setAlignment(Pos.CENTER_LEFT);
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        valueLabel.getStyleClass().add("label");

        Label changeLabel = new Label(change);
        changeLabel.setFont(Font.font(12));
        changeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + ("up".equals(trend) ? "#10B981" : "#EF4444") + ";");

        valueRow.getChildren().addAll(valueLabel, changeLabel);
        left.getChildren().addAll(titleLabel, valueRow);

        // Icon with gradient background
        Rectangle rect = new Rectangle(44, 44);
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(startColor)), new Stop(1, Color.web(endColor)));
        rect.setFill(lg);

        Label icon = new Label(iconEmoji);
        icon.setFont(Font.font(18));
        icon.setStyle("-fx-text-fill: white;");

        StackPane iconContainer = new StackPane(rect, icon);
        iconContainer.setAlignment(Pos.CENTER);

        getChildren().addAll(left, iconContainer);
        HBox.setHgrow(left, Priority.ALWAYS);

        // Hover
        setOnMouseEntered(e -> setStyle("-fx-background-color: " + (ThemeManager.isDarkMode() ? "#4B5563" : "#F9FAFB") + "; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: " + ThemeManager.getBorderColor() + "; -fx-effect: dropshadow(gaussian, rgba(2,6,23,0.12), 18, 0, 0, 6);"));
        setOnMouseExited(e -> updateCardStyle());
    }

    private void updateCardStyle() {
        setStyle("");
    }
}
