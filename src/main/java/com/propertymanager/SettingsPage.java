package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SettingsPage extends VBox {
    
    public SettingsPage() {
        initPage();
    }
    
    private void initPage() {
        setPadding(new Insets(30));
        setSpacing(25);
        setStyle("-fx-background-color: #f8f9fa;");
        
        HBox header = createHeader();
        VBox mainContent = createMainContent();
        
        getChildren().addAll(header, mainContent);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("⚙️ Settings");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Configure your application preferences and account settings");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#6c757d"));
        
        titleBox.getChildren().addAll(title, subtitle);
        header.getChildren().add(titleBox);
        return header;
    }
    
    private VBox createMainContent() {
        VBox content = new VBox(25);
        
        // First row - Account and Security
        HBox firstRow = new HBox(25);
        firstRow.setAlignment(Pos.TOP_LEFT);
        
        VBox accountCol = createAccountSection();
        accountCol.setPrefWidth(550);
        
        VBox securityCol = createSecuritySection();
        securityCol.setPrefWidth(550);
        
        firstRow.getChildren().addAll(accountCol, securityCol);
        
        // Second row - Appearance and Language
        HBox secondRow = new HBox(25);
        secondRow.setAlignment(Pos.TOP_LEFT);
        
        VBox appearanceCol = createAppearanceSection();
        appearanceCol.setPrefWidth(550);
        
        VBox languageCol = createLanguageSection();
        languageCol.setPrefWidth(550);
        
        secondRow.getChildren().addAll(appearanceCol, languageCol);
        
        // Third row - Notifications and About
        HBox thirdRow = new HBox(25);
        thirdRow.setAlignment(Pos.TOP_LEFT);
        
        VBox notificationCol = createNotificationSection();
        notificationCol.setPrefWidth(550);
        
        VBox aboutCol = createAboutSection();
        aboutCol.setPrefWidth(550);
        
        thirdRow.getChildren().addAll(notificationCol, aboutCol);
        
        content.getChildren().addAll(firstRow, secondRow, thirdRow);
        
        return content;
    }
    
    private VBox createAccountSection() {
        VBox section = new VBox(18);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0, 0, 3);");
        
        Label sectionTitle = new Label("👤 Account Settings");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        VBox profileBox = new VBox(10);
        
        HBox profileRow = new HBox(15);
        profileRow.setAlignment(Pos.CENTER_LEFT);
        
        Label avatar = new Label("👤");
        avatar.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 25; -fx-font-size: 20px; -fx-padding: 15;");
        
        VBox userInfo = new VBox(5);
        Label userName = new Label("Admin User");
        userName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Label userEmail = new Label("admin@propertymanager.com");
        userEmail.setFont(Font.font("Arial", 12));
        userEmail.setTextFill(Color.GRAY);
        
        userInfo.getChildren().addAll(userName, userEmail);
        
        Button editBtn = new Button("✏️ Edit Profile");
        editBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        profileRow.getChildren().addAll(avatar, userInfo, spacer, editBtn);
        
        Separator separator = new Separator();
        
        VBox settingsItems = new VBox(10);
        settingsItems.getChildren().addAll(
            createSettingItem("📧", "Change Email", "Update your email address"),
            createSettingItem("🔑", "Change Password", "Update your password"),
            createSettingItem("📱", "Two-Factor Authentication", "Enable 2FA for extra security")
        );
        
        profileBox.getChildren().addAll(profileRow, separator, settingsItems);
        section.getChildren().addAll(sectionTitle, profileBox);
        
        return section;
    }
    
    private VBox createAppearanceSection() {
        VBox section = new VBox(18);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0, 0, 3);");
        
        Label sectionTitle = new Label("🎨 Appearance");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        VBox settingsBox = new VBox(15);
        
        // Theme setting
        HBox themeRow = new HBox(15);
        themeRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox themeInfo = new VBox(3);
        Label themeLabel = new Label("🌙 Dark Mode");
        themeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label themeDesc = new Label("Switch between light and dark themes");
        themeDesc.setFont(Font.font("Arial", 12));
        themeDesc.setTextFill(Color.GRAY);
        
        themeInfo.getChildren().addAll(themeLabel, themeDesc);
        
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        
        CheckBox themeToggle = new CheckBox();
        themeToggle.setStyle("-fx-cursor: hand;");
        
        themeRow.getChildren().addAll(themeInfo, spacer1, themeToggle);
        
        // Font size setting
        HBox fontRow = new HBox(15);
        fontRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox fontInfo = new VBox(3);
        Label fontLabel = new Label("🔤 Font Size");
        fontLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label fontDesc = new Label("Adjust text size for better readability");
        fontDesc.setFont(Font.font("Arial", 12));
        fontDesc.setTextFill(Color.GRAY);
        
        fontInfo.getChildren().addAll(fontLabel, fontDesc);
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        ComboBox<String> fontCombo = new ComboBox<>();
        fontCombo.getItems().addAll("Small", "Medium", "Large");
        fontCombo.setValue("Medium");
        fontCombo.setPrefWidth(120);
        
        fontRow.getChildren().addAll(fontInfo, spacer2, fontCombo);
        
        settingsBox.getChildren().addAll(themeRow, fontRow);
        section.getChildren().addAll(sectionTitle, settingsBox);
        
        return section;
    }
    
    private VBox createNotificationSection() {
        VBox section = new VBox(18);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0, 0, 3);");
        
        Label sectionTitle = new Label("🔔 Notifications");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        VBox settingsBox = new VBox(15);
        
        settingsBox.getChildren().addAll(
            createToggleSetting("📧", "Email Notifications", "Receive notifications via email", true),
            createToggleSetting("🔊", "Sound Notifications", "Play sound for new notifications", false),
            createToggleSetting("📱", "Push Notifications", "Show desktop notifications", true),
            createToggleSetting("⚠️", "Critical Alerts", "Receive urgent system alerts", true)
        );
        
        section.getChildren().addAll(sectionTitle, settingsBox);
        return section;
    }
    
    private VBox createSecuritySection() {
        VBox section = new VBox(18);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0, 0, 3);");
        
        Label sectionTitle = new Label("🔒 Security & Privacy");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        VBox settingsBox = new VBox(10);
        
        settingsBox.getChildren().addAll(
            createSettingItem("🔐", "Session Timeout", "Auto-logout after 30 minutes"),
            createSettingItem("📊", "Activity Log", "View your account activity"),
            createSettingItem("🛡️", "Privacy Settings", "Manage data sharing preferences"),
            createSettingItem("🔄", "Backup & Restore", "Manage your data backups")
        );
        
        section.getChildren().addAll(sectionTitle, settingsBox);
        return section;
    }
    
    private VBox createLanguageSection() {
        VBox section = new VBox(18);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0, 0, 3);");
        
        Label sectionTitle = new Label("🌍 Language & Region");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        VBox settingsBox = new VBox(15);
        
        // Language setting
        HBox langRow = new HBox(15);
        langRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox langInfo = new VBox(3);
        Label langLabel = new Label("🗣️ Language");
        langLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label langDesc = new Label("Choose your preferred language");
        langDesc.setFont(Font.font("Arial", 12));
        langDesc.setTextFill(Color.GRAY);
        
        langInfo.getChildren().addAll(langLabel, langDesc);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        ComboBox<String> langCombo = new ComboBox<>();
        langCombo.getItems().addAll("English", "العربية", "Français", "Español");
        langCombo.setValue("English");
        langCombo.setPrefWidth(150);
        
        langRow.getChildren().addAll(langInfo, spacer, langCombo);
        
        // Currency setting
        HBox currencyRow = new HBox(15);
        currencyRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox currencyInfo = new VBox(3);
        Label currencyLabel = new Label("💰 Currency");
        currencyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label currencyDesc = new Label("Default currency for transactions");
        currencyDesc.setFont(Font.font("Arial", 12));
        currencyDesc.setTextFill(Color.GRAY);
        
        currencyInfo.getChildren().addAll(currencyLabel, currencyDesc);
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        ComboBox<String> currencyCombo = new ComboBox<>();
        currencyCombo.getItems().addAll("MAD (Dirham)", "USD (Dollar)", "EUR (Euro)", "GBP (Pound)");
        currencyCombo.setValue("MAD (Dirham)");
        currencyCombo.setPrefWidth(150);
        
        currencyRow.getChildren().addAll(currencyInfo, spacer2, currencyCombo);
        
        settingsBox.getChildren().addAll(langRow, currencyRow);
        section.getChildren().addAll(sectionTitle, settingsBox);
        
        return section;
    }
    
    private VBox createAboutSection() {
        VBox section = new VBox(18);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0, 0, 3);");
        
        Label sectionTitle = new Label("ℹ️ About");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        VBox aboutBox = new VBox(15);
        
        // App info
        VBox appInfo = new VBox(8);
        Label appName = new Label("Smart Property Manager Pro");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Label version = new Label("Version 2.1.0");
        version.setFont(Font.font("Arial", 12));
        version.setTextFill(Color.GRAY);
        
        Label buildDate = new Label("Build: November 2025");
        buildDate.setFont(Font.font("Arial", 12));
        buildDate.setTextFill(Color.GRAY);
        
        appInfo.getChildren().addAll(appName, version, buildDate);
        
        Separator separator = new Separator();
        
        VBox linksBox = new VBox(10);
        linksBox.getChildren().addAll(
            createLinkItem("📖", "Documentation", "View user manual and guides"),
            createLinkItem("🐛", "Report Bug", "Report issues and feedback"),
            createLinkItem("📞", "Support", "Contact technical support"),
            createLinkItem("⚖️", "License", "View software license")
        );
        
        aboutBox.getChildren().addAll(appInfo, separator, linksBox);
        section.getChildren().addAll(sectionTitle, aboutBox);
        
        return section;
    }
    
    private HBox createToggleSetting(String icon, String title, String description, boolean defaultValue) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        
        VBox info = new VBox(3);
        Label titleLabel = new Label(icon + " " + title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setTextFill(Color.GRAY);
        
        info.getChildren().addAll(titleLabel, descLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        CheckBox toggle = new CheckBox();
        toggle.setSelected(defaultValue);
        toggle.setStyle("-fx-cursor: hand;");
        
        row.getChildren().addAll(info, spacer, toggle);
        return row;
    }
    
    private HBox createSettingItem(String icon, String title, String description) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-cursor: hand; -fx-padding: 8;");
        
        row.setOnMouseEntered(e -> row.setStyle("-fx-cursor: hand; -fx-padding: 8; -fx-background-color: #f8f9fa; -fx-background-radius: 6;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-cursor: hand; -fx-padding: 8;"));
        
        VBox info = new VBox(3);
        Label titleLabel = new Label(icon + " " + title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setTextFill(Color.GRAY);
        
        info.getChildren().addAll(titleLabel, descLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label arrow = new Label("›");
        arrow.setFont(Font.font("Arial", 16));
        arrow.setTextFill(Color.GRAY);
        
        row.getChildren().addAll(info, spacer, arrow);
        return row;
    }
    
    private HBox createLinkItem(String icon, String title, String description) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-cursor: hand; -fx-padding: 8;");
        
        row.setOnMouseEntered(e -> row.setStyle("-fx-cursor: hand; -fx-padding: 8; -fx-background-color: #f0f8ff; -fx-background-radius: 6;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-cursor: hand; -fx-padding: 8;"));
        
        VBox info = new VBox(3);
        Label titleLabel = new Label(icon + " " + title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#2C3E8C"));
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setTextFill(Color.GRAY);
        
        info.getChildren().addAll(titleLabel, descLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label arrow = new Label("↗");
        arrow.setFont(Font.font("Arial", 14));
        arrow.setTextFill(Color.web("#2C3E8C"));
        
        row.getChildren().addAll(info, spacer, arrow);
        return row;
    }
}