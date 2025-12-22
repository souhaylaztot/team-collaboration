package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Sidebar extends VBox implements LocalizationService.LocalizableComponent {
    
    private MainApp mainApp;
    private LocalizationService localizationService;
    private VBox menuContainer;
    
    public Sidebar(MainApp mainApp) {
        this.mainApp = mainApp;
        this.localizationService = LocalizationService.getInstance();
        localizationService.addLanguageChangeListener(locale -> updateLanguage());
        initSidebar();
    }
    
    private void initSidebar() {
        setPrefWidth(250);
        updateTheme();
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
        title.setTextFill(Color.web(ThemeManager.getTextPrimary()));
        Label subtitle = new Label("Manager Pro");
        subtitle.setFont(Font.font("Arial", 12));
        subtitle.setTextFill(Color.web(ThemeManager.getTextSecondary()));
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
        textLabel.setTextFill(Color.web(ThemeManager.getTextPrimary()));
        
        content.getChildren().addAll(iconLabel, textLabel);
        
        if (!badge.isEmpty()) {
            Label badgeLabel = new Label(badge);
            badgeLabel.setStyle("-fx-background-color: #ffc107; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 2 6 2 6; -fx-font-size: 10px; -fx-font-weight: bold;");
            content.getChildren().add(badgeLabel);
        }
        
        btn.setGraphic(content);
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");
        
        // Theme-aware hover effects
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: " + ThemeManager.getHover() + "; -fx-background-radius: 8; -fx-cursor: hand;");
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");
            textLabel.setTextFill(Color.web(ThemeManager.getTextPrimary()));
        });
        
        btn.setOnMousePressed(e -> {
            btn.setStyle("-fx-background-color: " + ThemeManager.getPrimary() + "; -fx-background-radius: 8; -fx-cursor: hand;");
            textLabel.setTextFill(Color.web("#FFFFFF"));
        });
        
        btn.setOnMouseReleased(e -> {
            btn.setStyle("-fx-background-color: " + ThemeManager.getHover() + "; -fx-background-radius: 8; -fx-cursor: hand;");
            textLabel.setTextFill(Color.web(ThemeManager.getTextPrimary()));
        });
        
        btn.setOnAction(e -> {
            System.out.println("تم النقر على: " + text); // للتأكد من عمل الحدث
            if (!text.equals("Logout")) {
                mainApp.showPage(text);
            }
        });
        
        return btn;
    }

    
    private void updateMenuItems() {
        if (menuContainer == null) {
            menuContainer = new VBox(10);
            getChildren().add(menuContainer);
        }
        
        menuContainer.getChildren().clear();
        menuContainer.getChildren().addAll(
            createMenuItem("🏠", localizationService.getMenuText("dashboard"), ""),
            createMenuItem("🏢", localizationService.getMenuText("buildings"), ""),
            createMenuItem("👥", localizationService.getMenuText("buyers"), "3"),
            createMenuItem("🌞️", localizationService.getMenuText("lands"), ""),
            createMenuItem("📋", localizationService.getMenuText("permits"), "2"),
            createMenuItem("🔧", localizationService.getMenuText("maintenance"), "5"),
            createMenuItem("📊", localizationService.getMenuText("reports"), ""),
            createMenuItem("📝", localizationService.getMenuText("requests"), "4"),
            createMenuItem("🚛", localizationService.getMenuText("transportation"), ""),
            createSpacer(30),
            createMenuItem("⚙️", localizationService.getMenuText("settings"), ""),
            createMenuItem("🚺", localizationService.getMenuText("logout"), "")
        );
    }
    
    @Override
    public void updateLanguage() {
        updateMenuItems();
        System.out.println("Sidebar language updated to: " + localizationService.getCurrentLanguageName());
    }
    
    private VBox createSpacer(double height) {
        VBox spacer = new VBox();
        spacer.setPrefHeight(height);
        return spacer;
    }
    
    public void updateTheme() {
        setStyle("-fx-background-color: " + ThemeManager.getSidebar() + "; -fx-border-color: " + ThemeManager.getBorder() + "; -fx-border-width: 0 1 0 0;");
    }
}