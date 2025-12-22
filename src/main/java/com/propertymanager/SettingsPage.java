package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SettingsPage extends VBox implements LocalizationService.LocalizableComponent {
    
    private LocalizationService localizationService;
    private Label titleLabel;
    private Label subtitleLabel;
    private ComboBox<String> langCombo;
    private ComboBox<String> currencyCombo;
    
    public SettingsPage() {
        localizationService = LocalizationService.getInstance();
        localizationService.addLanguageChangeListener(locale -> updateLanguage());
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
        titleLabel = new Label();
        titleLabel.textProperty().bind(localizationService.getStringProperty("settings.title"));
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        subtitleLabel = new Label();
        subtitleLabel.setText("Configure your application preferences and account settings");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#6c757d"));
        
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
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
        editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;"));
        editBtn.setOnMouseExited(e -> editBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;"));
        editBtn.setOnAction(e -> showEditProfileDialog());
        
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
        themeToggle.setSelected(false);
        themeToggle.setStyle("-fx-cursor: hand;");
        themeToggle.setOnAction(e -> {
            boolean isDark = themeToggle.isSelected();
            String themeStatus = isDark ? "Dark" : "Light";
            showAlert("Theme Changed", "Switched to " + themeStatus + " theme.\n\nNote: Some changes may require application restart for full effect.");
            // Future: ThemeManager.setDarkMode(isDark);
        });
        
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
        fontCombo.setOnAction(e -> {
            String selectedSize = fontCombo.getValue();
            if (selectedSize != null) {
                showAlert("Font Size Updated", "Font size changed to " + selectedSize + ".\n\nThis setting will be applied to:\n• All text elements\n• Menu items\n• Dialog boxes\n\nRestart recommended for full effect.");
                // Future: FontManager.setFontSize(selectedSize);
            }
        });
        
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
        
        langCombo = new ComboBox<>();
        langCombo.getItems().addAll(localizationService.getSupportedLanguages());
        langCombo.setValue(localizationService.getCurrentLanguageName());
        langCombo.setPrefWidth(150);
        langCombo.setOnAction(e -> {
            String selectedLang = langCombo.getValue();
            if (selectedLang != null) {
                // Apply language change immediately
                localizationService.changeLanguage(selectedLang);
                
                // Show confirmation with immediate effect
                showAlert(localizationService.getMessageText("success"), 
                         "Language changed to " + selectedLang + ".\n\nThis affects:\n• All interface text\n• Date and time formats\n• Number formats\n• Currency display\n\nChanges applied immediately.");
            }
        });
        
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
        
        currencyCombo = new ComboBox<>();
        currencyCombo.getItems().addAll("MAD (Dirham)", "USD (Dollar)", "EUR (Euro)", "GBP (Pound)");
        currencyCombo.setValue(LanguageManager.getCurrentCurrencyDisplay());
        currencyCombo.setPrefWidth(150);
        currencyCombo.setOnAction(e -> {
            String selectedCurrency = currencyCombo.getValue();
            if (selectedCurrency != null) {
                // Apply currency change immediately
                LanguageManager.setCurrency(selectedCurrency);
                
                showAlert(localizationService.getMessageText("success"), 
                         "Default currency changed to " + selectedCurrency + ".\n\nThis affects:\n• All financial displays\n• Reports and invoices\n• Payment calculations\n• Export formats\n\nChange applied immediately.");
            }
        });
        
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
        toggle.setOnAction(e -> {
            String status = toggle.isSelected() ? "enabled" : "disabled";
            String details = getNotificationDetails(title, toggle.isSelected());
            showAlert("Setting Updated", title + " has been " + status + ".\n\n" + details);
            // Future: NotificationManager.updateSetting(title, toggle.isSelected());
        });
        
        row.getChildren().addAll(info, spacer, toggle);
        return row;
    }
    
    private HBox createSettingItem(String icon, String title, String description) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-cursor: hand; -fx-padding: 8;");
        
        row.setOnMouseEntered(e -> row.setStyle("-fx-cursor: hand; -fx-padding: 8; -fx-background-color: #f8f9fa; -fx-background-radius: 6;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-cursor: hand; -fx-padding: 8;"));
        row.setOnMouseClicked(e -> handleSettingClick(title));
        
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
        row.setOnMouseClicked(e -> handleLinkClick(title));
        
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
    
    private void showEditProfileDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Edit Profile");
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");
        
        // Header
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        
        Label icon = new Label("👤");
        icon.setStyle("-fx-font-size: 48px; -fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 30; -fx-padding: 15;");
        
        Label title = new Label("Edit Profile");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Update your personal information");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.GRAY);
        
        header.getChildren().addAll(icon, title, subtitle);
        
        // Form
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20, 0, 0, 0));
        
        // Full Name
        Label nameLabel = new Label("Full Name:");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField nameField = new TextField("Admin User");
        nameField.setPrefWidth(300);
        nameField.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        // Email
        Label emailLabel = new Label("Email Address:");
        emailLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField emailField = new TextField("admin@propertymanager.com");
        emailField.setPrefWidth(300);
        emailField.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        // Phone
        Label phoneLabel = new Label("Phone Number:");
        phoneLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField phoneField = new TextField("+212-XXX-XXXX");
        phoneField.setPrefWidth(300);
        phoneField.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        // Job Title
        Label jobLabel = new Label("Job Title:");
        jobLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField jobField = new TextField("System Administrator");
        jobField.setPrefWidth(300);
        jobField.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        // Department
        Label deptLabel = new Label("Department:");
        deptLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        ComboBox<String> deptCombo = new ComboBox<>();
        deptCombo.getItems().addAll("Administration", "Property Management", "Finance", "Maintenance", "Customer Service");
        deptCombo.setValue("Administration");
        deptCombo.setPrefWidth(300);
        deptCombo.setStyle("-fx-padding: 5;");
        
        // Bio
        Label bioLabel = new Label("Bio/Description:");
        bioLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextArea bioArea = new TextArea("Experienced system administrator managing property operations and user accounts.");
        bioArea.setPrefRowCount(3);
        bioArea.setPrefWidth(300);
        bioArea.setWrapText(true);
        bioArea.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(emailLabel, 0, 1);
        form.add(emailField, 1, 1);
        form.add(phoneLabel, 0, 2);
        form.add(phoneField, 1, 2);
        form.add(jobLabel, 0, 3);
        form.add(jobField, 1, 3);
        form.add(deptLabel, 0, 4);
        form.add(deptCombo, 1, 4);
        form.add(bioLabel, 0, 5);
        form.add(bioArea, 1, 5);
        
        // Buttons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPadding(new Insets(20, 0, 0, 0));
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(100);
        cancelBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 10 20; -fx-cursor: hand;");
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-cursor: hand;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-background-radius: 6; -fx-padding: 10 20; -fx-cursor: hand;"));
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button saveBtn = new Button("Save Changes");
        saveBtn.setPrefWidth(120);
        saveBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-background-color: #1a2a5e; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;"));
        
        saveBtn.setOnAction(e -> {
            // Validate fields
            if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Missing Required Information");
                alert.setContentText("Please fill in all required fields (Name and Email).");
                alert.showAndWait();
                return;
            }
            
            // Validate email format
            String email = emailField.getText().trim();
            if (!email.contains("@") || !email.contains(".")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Email");
                alert.setHeaderText("Email Format Error");
                alert.setContentText("Please enter a valid email address.");
                alert.showAndWait();
                return;
            }
            
            // Save changes (in real app, this would update database)
            String updatedInfo = "Profile Updated Successfully!\n\n" +
                               "Name: " + nameField.getText() + "\n" +
                               "Email: " + emailField.getText() + "\n" +
                               "Phone: " + phoneField.getText() + "\n" +
                               "Job Title: " + jobField.getText() + "\n" +
                               "Department: " + deptCombo.getValue() + "\n" +
                               "Bio: " + bioArea.getText().substring(0, Math.min(50, bioArea.getText().length())) + "...";
            
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Profile Updated");
            success.setHeaderText("Changes Saved Successfully");
            success.setContentText(updatedInfo);
            success.showAndWait();
            
            dialog.close();
        });
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(header, form, buttons);
        
        Scene scene = new Scene(content, 500, 650);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private void handleSettingClick(String settingName) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle(settingName);
        dialog.setHeaderText(settingName + " Settings");
        
        String content = switch (settingName) {
            case "Change Email" -> "Update your email address here.\n\nCurrent: admin@propertymanager.com\nNew email will require verification.";
            case "Change Password" -> "Update your password for security.\n\nRequirements:\n• At least 8 characters\n• Include numbers and symbols\n• Different from current password";
            case "Two-Factor Authentication" -> "Enable 2FA for extra security.\n\nOptions:\n• SMS verification\n• Authenticator app\n• Email verification";
            case "Session Timeout" -> "Configure automatic logout.\n\nOptions:\n• 15 minutes\n• 30 minutes (current)\n• 1 hour\n• Never";
            case "Activity Log" -> "View your account activity.\n\nRecent activity:\n• Login: Today 09:30 AM\n• Settings changed: Yesterday\n• Password updated: Last week";
            case "Privacy Settings" -> "Manage your privacy preferences.\n\nOptions:\n• Data sharing\n• Analytics\n• Marketing communications";
            case "Backup & Restore" -> "Manage your data backups.\n\nLast backup: Today 02:00 AM\nNext backup: Tomorrow 02:00 AM\nBackup location: Cloud storage";
            default -> "This setting will be configured here.";
        };
        
        dialog.setContentText(content);
        dialog.showAndWait();
    }
    
    private void handleLinkClick(String linkName) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle(linkName);
        dialog.setHeaderText(linkName);
        
        String content = switch (linkName) {
            case "Documentation" -> "User Manual and Guides\n\n• Getting Started Guide\n• Feature Documentation\n• Video Tutorials\n• FAQ Section\n\nVisit: docs.propertymanager.com";
            case "Report Bug" -> "Report Issues and Feedback\n\n• Bug reports\n• Feature requests\n• General feedback\n• Performance issues\n\nEmail: support@propertymanager.com";
            case "Support" -> "Technical Support\n\n• Live chat support\n• Email support\n• Phone support: +212-XXX-XXXX\n• Remote assistance\n\nAvailable 24/7";
            case "License" -> "Software License Information\n\nSmart Property Manager Pro\nVersion 2.1.0\n\n© 2025 Property Manager Inc.\nAll rights reserved.\n\nLicensed under Commercial License";
            default -> "Information about " + linkName + " will be displayed here.";
        };
        
        dialog.setContentText(content);
        dialog.showAndWait();
    }
    
    private String getNotificationDetails(String title, boolean enabled) {
        if (!enabled) return "You will no longer receive these notifications.";
        
        return switch (title) {
            case "Email Notifications" -> "You will receive notifications at admin@propertymanager.com\nFrequency: Immediate for urgent, daily digest for others";
            case "Sound Notifications" -> "System will play notification sounds\nVolume follows system settings";
            case "Push Notifications" -> "Desktop notifications will appear\nClick to view details or dismiss";
            case "Critical Alerts" -> "Important system alerts will be shown\nThese cannot be disabled for security";
            default -> "Notification setting has been updated.";
        };
    }
    
    @Override
    public void updateLanguage() {
        // Update combo box selection to reflect current language
        if (langCombo != null) {
            langCombo.setValue(localizationService.getCurrentLanguageName());
        }
        
        // Update title with emoji
        if (titleLabel != null) {
            titleLabel.setText("⚙️ " + localizationService.getString("settings.title"));
        }
        
        System.out.println("Settings page language updated to: " + localizationService.getCurrentLanguageName());
    }
    
    private void updateLanguageDisplay() {
        // This method would refresh UI elements with new language
        System.out.println("UI updated with language: " + localizationService.getCurrentLanguageName());
        // In a real application, this would trigger UI refresh
    }
    
    private void updateCurrencyDisplay() {
        // This method would refresh currency displays
        System.out.println("Currency displays updated: " + LanguageManager.getCurrentCurrencyDisplay());
        // In a real application, this would update all currency displays
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}