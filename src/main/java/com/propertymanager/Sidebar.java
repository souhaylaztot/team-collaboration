package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Sidebar extends VBox {
    
    private MainApp mainApp;
    
    public Sidebar(MainApp mainApp) {
        this.mainApp = mainApp;
        initSidebar();
    }
    
    private void initSidebar() {
        setPrefWidth(250);
        setStyle("-fx-background-color: #f8fafc; -fx-border-color: #e2e8f0; -fx-border-width: 0 1 0 0;");
        setPadding(new Insets(20));
        setSpacing(10);
        
        // Logo
        HBox logo = new HBox(10);
        logo.setAlignment(Pos.CENTER_LEFT);
        Label logoIcon = new Label("🏢");
        logoIcon.setStyle("-fx-font-size: 24px;");
        
        VBox logoText = new VBox();
        Label title = new Label("Smart Property");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label subtitle = new Label("Manager Pro");
        subtitle.setFont(Font.font("Arial", 12));
        subtitle.setTextFill(Color.GRAY);
        logoText.getChildren().addAll(title, subtitle);
        
        logo.getChildren().addAll(logoIcon, logoText);
        
        // Menu items
        getChildren().addAll(
            logo,
            createSpacer(20),
            createMenuItem("🏠", "Dashboard", ""),
            createMenuItem("🏢", "Buildings", ""),
            createMenuItem("👥", "Buyers", "3"),
            createMenuItem("🏞️", "Lands", ""),
            createMenuItem("📋", "Permits", "2"),
            createMenuItem("🔧", "Maintenance", "5"),
            createMenuItem("📊", "Reports", ""),
            createMenuItem("📝", "Requests", "4"),
            createMenuItem("🚛", "Transportation", ""),
            createSpacer(30),
            createMenuItem("⚙️", "Settings", ""),
            createMenuItem("🚪", "Logout", "")
        );
    }
    
    private Button createMenuItem(String icon, String text, String badge) {
        Button btn = new Button();
        btn.setPrefWidth(210);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 15, 12, 15));
        
        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Arial", 14));
        
        content.getChildren().addAll(iconLabel, textLabel);
        
        if (!badge.isEmpty()) {
            Label badgeLabel = new Label(badge);
            badgeLabel.setStyle("-fx-background-color: #ffc107; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 2 6 2 6; -fx-font-size: 10px; -fx-font-weight: bold;");
            content.getChildren().add(badgeLabel);
        }
        
        btn.setGraphic(content);
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");
        
        // تحسين التفاعل البصري
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8; -fx-cursor: hand;");
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");
        });
        
        btn.setOnMousePressed(e -> {
            btn.setStyle("-fx-background-color: #2C3E8C; -fx-background-radius: 8; -fx-cursor: hand;");
            // تغيير لون النص عند الضغط
            textLabel.setTextFill(Color.WHITE);
        });
        
        btn.setOnMouseReleased(e -> {
            btn.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8; -fx-cursor: hand;");
            textLabel.setTextFill(Color.BLACK);
        });
        
        btn.setOnAction(e -> {
            System.out.println("تم النقر على: " + text); // للتأكد من عمل الحدث
            if (!text.equals("Logout")) {
                mainApp.showPage(text);
            }
        });
        
        return btn;
    }
    
    private VBox createSpacer(double height) {
        VBox spacer = new VBox();
        spacer.setPrefHeight(height);
        return spacer;
    }
}