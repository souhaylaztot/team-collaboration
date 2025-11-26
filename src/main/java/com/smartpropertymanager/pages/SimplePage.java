package com.smartpropertymanager.pages;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class SimplePage implements Page {
    private VBox content;
    private String title;

    public SimplePage(String title) {
        this.title = title;
        content = new VBox();
        content.setStyle("-fx-background-color: #F5F5F5;");
        content.setPadding(new Insets(30));
        content.setSpacing(20);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label placeholder = new Label("Content for " + title + " page will be here");
        placeholder.setStyle("-fx-font-size: 14; -fx-text-fill: #999999;");

        content.getChildren().addAll(titleLabel, placeholder);
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
