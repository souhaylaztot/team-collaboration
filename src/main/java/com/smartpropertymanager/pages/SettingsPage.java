package com.smartpropertymanager.pages;

import com.smartpropertymanager.utils.LanguageManager;
import com.smartpropertymanager.utils.ThemeManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SettingsPage implements Page {
    private VBox content;
    private String title = "Settings";
    private ComboBox<String> languageCombo;
    private ComboBox<String> themeCombo;

    public SettingsPage() {
        content = new VBox();
        updateContentStyle();
        content.setPadding(new Insets(32));
        content.setSpacing(24);
        createUI();
    }
    
    private void updateContentStyle() {
        content.getStyleClass().add("settings-container");
    }

    private void createUI() {
        // Header
        VBox header = createHeader();
        content.getChildren().add(header);

        // Settings sections in a scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox settingsContainer = new VBox(24);
        settingsContainer.setPadding(new Insets(0, 16, 0, 0));

        // Create settings sections
        settingsContainer.getChildren().addAll(
            createGeneralSettings(),
            createNotificationSettings(),
            createDisplaySettings(),
            createSecuritySettings(),
            createDataSettings(),
            createSystemSettings()
        );

        scrollPane.setContent(settingsContainer);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        content.getChildren().add(scrollPane);
    }

    private VBox createHeader() {
        VBox header = new VBox(8);
        Label titleLabel = new Label(LanguageManager.getText("settings.title"));
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.getStyleClass().add("label");

        Label subtitleLabel = new Label(LanguageManager.getText("settings.subtitle"));
        subtitleLabel.getStyleClass().addAll("label-secondary");
        subtitleLabel.setStyle("-fx-font-size: 14;");

        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }

    private VBox createGeneralSettings() {
        VBox section = createSettingsSection(LanguageManager.getText("general.settings"), "⚙️");

        // Language setting
        HBox languageRow = createSettingRow(LanguageManager.getText("language"), LanguageManager.getText("language.desc"));
        languageCombo = new ComboBox<>(FXCollections.observableArrayList(LanguageManager.getAvailableLanguages()));
        languageCombo.setValue(LanguageManager.getCurrentLanguage());
        languageCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        languageCombo.setOnAction(e -> {
            String selectedLanguage = languageCombo.getValue();
            LanguageManager.setLanguage(selectedLanguage);
            showLanguageChangeDialog();
        });
        languageRow.getChildren().add(languageCombo);

        // Currency setting
        HBox currencyRow = createSettingRow(LanguageManager.getText("currency"), LanguageManager.getText("currency.desc"));
        ComboBox<String> currencyCombo = new ComboBox<>(FXCollections.observableArrayList(
            "MAD (Moroccan Dirham)", "USD (US Dollar)", "EUR (Euro)", "GBP (British Pound)"
        ));
        currencyCombo.setValue("MAD (Moroccan Dirham)");
        currencyCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        currencyRow.getChildren().add(currencyCombo);

        // Time zone setting
        HBox timezoneRow = createSettingRow(LanguageManager.getText("timezone"), LanguageManager.getText("timezone.desc"));
        ComboBox<String> timezoneCombo = new ComboBox<>(FXCollections.observableArrayList(
            "GMT+1 (Morocco)", "GMT+0 (London)", "GMT-5 (New York)", "GMT+1 (Paris)"
        ));
        timezoneCombo.setValue("GMT+1 (Morocco)");
        timezoneCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        timezoneRow.getChildren().add(timezoneCombo);

        section.getChildren().addAll(languageRow, currencyRow, timezoneRow);
        return section;
    }

    private VBox createNotificationSettings() {
        VBox section = createSettingsSection(LanguageManager.getText("notifications"), "🔔");

        // Email notifications
        HBox emailRow = createSettingRow(LanguageManager.getText("email.notifications"), LanguageManager.getText("email.notifications.desc"));
        CheckBox emailCheck = new CheckBox();
        emailCheck.setSelected(true);
        emailRow.getChildren().add(emailCheck);

        // Push notifications
        HBox pushRow = createSettingRow(LanguageManager.getText("push.notifications"), LanguageManager.getText("push.notifications.desc"));
        CheckBox pushCheck = new CheckBox();
        pushCheck.setSelected(true);
        pushRow.getChildren().add(pushCheck);

        // Payment reminders
        HBox paymentRow = createSettingRow("Payment Reminders", "Notify about upcoming payments");
        CheckBox paymentCheck = new CheckBox();
        paymentCheck.setSelected(true);
        paymentRow.getChildren().add(paymentCheck);

        // Maintenance alerts
        HBox maintenanceRow = createSettingRow("Maintenance Alerts", "Alerts for maintenance requests");
        CheckBox maintenanceCheck = new CheckBox();
        maintenanceCheck.setSelected(true);
        maintenanceRow.getChildren().add(maintenanceCheck);

        // Notification frequency
        HBox frequencyRow = createSettingRow("Notification Frequency", "How often to receive notifications");
        ComboBox<String> frequencyCombo = new ComboBox<>(FXCollections.observableArrayList(
            "Immediately", "Every 15 minutes", "Hourly", "Daily"
        ));
        frequencyCombo.setValue("Every 15 minutes");
        frequencyCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        frequencyRow.getChildren().add(frequencyCombo);

        section.getChildren().addAll(emailRow, pushRow, paymentRow, maintenanceRow, frequencyRow);
        return section;
    }

    private VBox createDisplaySettings() {
        VBox section = createSettingsSection(LanguageManager.getText("display.appearance"), "🎨");

        // Theme setting
        HBox themeRow = createSettingRow(LanguageManager.getText("theme"), LanguageManager.getText("theme.desc"));
        themeCombo = new ComboBox<>(FXCollections.observableArrayList(
            "Light", "Dark", "Auto (System)"
        ));
        themeCombo.setValue(ThemeManager.isDarkMode() ? "Dark" : "Light");
        themeCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        themeCombo.setOnAction(e -> {
            String selectedTheme = themeCombo.getValue();
            if ("Dark".equals(selectedTheme) && !ThemeManager.isDarkMode()) {
                ThemeManager.setDarkMode(true);
                showThemeChangeDialog("Dark");
            } else if ("Light".equals(selectedTheme) && ThemeManager.isDarkMode()) {
                ThemeManager.setDarkMode(false);
                showThemeChangeDialog("Light");
            }
        });
        themeRow.getChildren().add(themeCombo);

        // Font size
        HBox fontRow = createSettingRow(LanguageManager.getText("font.size"), LanguageManager.getText("font.size.desc"));
        ComboBox<String> fontCombo = new ComboBox<>(FXCollections.observableArrayList(
            "Small", "Medium", "Large", "Extra Large"
        ));
        fontCombo.setValue("Medium");
        fontCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        fontRow.getChildren().add(fontCombo);

        // Sidebar position
        HBox sidebarRow = createSettingRow("Sidebar Position", "Position of the navigation sidebar");
        ComboBox<String> sidebarCombo = new ComboBox<>(FXCollections.observableArrayList(
            "Left", "Right"
        ));
        sidebarCombo.setValue(LanguageManager.isRTL() ? "Right" : "Left");
        sidebarCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        sidebarRow.getChildren().add(sidebarCombo);

        // Compact mode
        HBox compactRow = createSettingRow("Compact Mode", "Use compact layout to show more content");
        CheckBox compactCheck = new CheckBox();
        compactCheck.setSelected(false);
        compactRow.getChildren().add(compactCheck);

        section.getChildren().addAll(themeRow, fontRow, sidebarRow, compactRow);
        return section;
    }

    private VBox createSecuritySettings() {
        VBox section = createSettingsSection(LanguageManager.getText("security.privacy"), "🔒");

        // Auto-lock
        HBox lockRow = createSettingRow(LanguageManager.getText("auto.lock"), LanguageManager.getText("auto.lock.desc"));
        CheckBox lockCheck = new CheckBox();
        lockCheck.setSelected(true);
        lockRow.getChildren().add(lockCheck);

        // Lock timeout
        HBox timeoutRow = createSettingRow("Lock Timeout", "Time before auto-lock activates");
        ComboBox<String> timeoutCombo = new ComboBox<>(FXCollections.observableArrayList(
            "5 minutes", "15 minutes", "30 minutes", "1 hour", "Never"
        ));
        timeoutCombo.setValue("15 minutes");
        timeoutCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        timeoutRow.getChildren().add(timeoutCombo);

        // Two-factor authentication
        HBox twoFactorRow = createSettingRow("Two-Factor Authentication", "Enable 2FA for enhanced security");
        Button twoFactorBtn = new Button("Configure");
        twoFactorBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        twoFactorBtn.setOnAction(e -> showTwoFactorDialog());
        twoFactorRow.getChildren().add(twoFactorBtn);

        // Change password
        HBox passwordRow = createSettingRow("Password", "Change your account password");
        Button passwordBtn = new Button("Change Password");
        passwordBtn.setStyle("-fx-background-color: #4FD1C5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        passwordBtn.setOnAction(e -> showPasswordDialog());
        passwordRow.getChildren().add(passwordBtn);

        section.getChildren().addAll(lockRow, timeoutRow, twoFactorRow, passwordRow);
        return section;
    }

    private VBox createDataSettings() {
        VBox section = createSettingsSection(LanguageManager.getText("data.management"), "💾");

        // Auto-save
        HBox autoSaveRow = createSettingRow(LanguageManager.getText("auto.save"), LanguageManager.getText("auto.save.desc"));
        CheckBox autoSaveCheck = new CheckBox();
        autoSaveCheck.setSelected(true);
        autoSaveRow.getChildren().add(autoSaveCheck);

        // Backup frequency
        HBox backupRow = createSettingRow("Backup Frequency", "How often to create backups");
        ComboBox<String> backupCombo = new ComboBox<>(FXCollections.observableArrayList(
            "Daily", "Weekly", "Monthly", "Manual only"
        ));
        backupCombo.setValue("Weekly");
        backupCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        backupRow.getChildren().add(backupCombo);

        // Export data
        HBox exportRow = createSettingRow("Export Data", "Export your data for backup or migration");
        Button exportBtn = new Button("Export");
        exportBtn.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        exportBtn.setOnAction(e -> showExportDialog());
        exportRow.getChildren().add(exportBtn);

        // Import data
        HBox importRow = createSettingRow("Import Data", "Import data from another system");
        Button importBtn = new Button("Import");
        importBtn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        importBtn.setOnAction(e -> showImportDialog());
        importRow.getChildren().add(importBtn);

        section.getChildren().addAll(autoSaveRow, backupRow, exportRow, importRow);
        return section;
    }

    private VBox createSystemSettings() {
        VBox section = createSettingsSection(LanguageManager.getText("system"), "🖥️");

        // Performance mode
        HBox performanceRow = createSettingRow(LanguageManager.getText("performance.mode"), LanguageManager.getText("performance.mode.desc"));
        ComboBox<String> performanceCombo = new ComboBox<>(FXCollections.observableArrayList(
            "Balanced", "Performance", "Power Saving"
        ));
        performanceCombo.setValue("Balanced");
        performanceCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        performanceRow.getChildren().add(performanceCombo);

        // Cache size
        HBox cacheRow = createSettingRow("Cache Size", "Amount of data to cache for faster access");
        ComboBox<String> cacheCombo = new ComboBox<>(FXCollections.observableArrayList(
            "50 MB", "100 MB", "200 MB", "500 MB"
        ));
        cacheCombo.setValue("100 MB");
        cacheCombo.setStyle("-fx-pref-width: 200; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        cacheRow.getChildren().add(cacheCombo);

        // Clear cache
        HBox clearCacheRow = createSettingRow("Clear Cache", "Clear cached data to free up space");
        Button clearCacheBtn = new Button("Clear Cache");
        clearCacheBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        clearCacheBtn.setOnAction(e -> showClearCacheDialog());
        clearCacheRow.getChildren().add(clearCacheBtn);

        // Reset settings
        HBox resetRow = createSettingRow("Reset Settings", "Reset all settings to default values");
        Button resetBtn = new Button("Reset to Defaults");
        resetBtn.setStyle("-fx-background-color: #6B7280; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        resetBtn.setOnAction(e -> showResetDialog());
        resetRow.getChildren().add(resetBtn);

        section.getChildren().addAll(performanceRow, cacheRow, clearCacheRow, resetRow);
        return section;
    }

    private VBox createSettingsSection(String title, String icon) {
        VBox section = new VBox(16);
        section.getStyleClass().addAll("card", "settings-section");
        section.setPadding(new Insets(24));

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 20;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.getStyleClass().add("label");

        header.getChildren().addAll(iconLabel, titleLabel);
        section.getChildren().add(header);

        return section;
    }

    private HBox createSettingRow(String title, String description) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(16);
        row.setPadding(new Insets(12, 0, 12, 0));
        row.getStyleClass().add("setting-row");

        VBox textSection = new VBox(4);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("label");
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("label-secondary");
        descLabel.setStyle("-fx-font-size: 12;");
        descLabel.setWrapText(true);

        textSection.getChildren().addAll(titleLabel, descLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(textSection, spacer);
        return row;
    }

    private void showTwoFactorDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Two-Factor Authentication");
        alert.setHeaderText("Configure 2FA");
        alert.setContentText("Two-factor authentication adds an extra layer of security to your account.\n\n" +
                "Steps to enable:\n" +
                "1. Download an authenticator app\n" +
                "2. Scan the QR code\n" +
                "3. Enter the verification code\n\n" +
                "This feature will be available in the next update.");
        alert.showAndWait();
    }

    private void showPasswordDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter your new password");

        ButtonType changeButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        PasswordField currentPassword = new PasswordField();
        currentPassword.setPromptText("Current Password");
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("New Password");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm New Password");

        grid.add(new Label("Current Password:"), 0, 0);
        grid.add(currentPassword, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPassword, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
    }

    private void showExportDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Data");
        alert.setHeaderText("Data Export");
        alert.setContentText("Your data will be exported to a CSV file.\n\n" +
                "Export includes:\n" +
                "• Buildings and properties\n" +
                "• Buyer information\n" +
                "• Financial records\n" +
                "• Maintenance requests\n\n" +
                "Choose a location to save the export file.");
        alert.showAndWait();
    }

    private void showImportDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Import Data");
        alert.setHeaderText("Data Import");
        alert.setContentText("Import data from a CSV file or another property management system.\n\n" +
                "Supported formats:\n" +
                "• CSV files\n" +
                "• Excel spreadsheets\n" +
                "• JSON format\n\n" +
                "Please select the file to import.");
        alert.showAndWait();
    }

    private void showClearCacheDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Cache");
        alert.setHeaderText("Clear Application Cache");
        alert.setContentText("This will clear all cached data and may slow down the application temporarily.\n\n" +
                "Are you sure you want to continue?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Cache Cleared");
                success.setHeaderText("Success");
                success.setContentText("Application cache has been cleared successfully.");
                success.showAndWait();
            }
        });
    }

    private void showResetDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Settings");
        alert.setHeaderText("Reset All Settings");
        alert.setContentText("This will reset all settings to their default values.\n\n" +
                "Are you sure you want to continue? This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Settings Reset");
                success.setHeaderText("Success");
                success.setContentText("All settings have been reset to default values.\n" +
                        "Please restart the application for changes to take effect.");
                success.showAndWait();
            }
        });
    }
    
    private void showLanguageChangeDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Language Changed");
        alert.setHeaderText("Language Successfully Changed");
        alert.setContentText("The application language has been changed to " + LanguageManager.getCurrentLanguage() + ".\n\n" +
                "Some interface elements will update immediately, while others may require restarting the application for full effect.\n\n" +
                "Current language: " + LanguageManager.getCurrentLanguage());
        alert.showAndWait();
        
        // Refresh the settings page to show new language
        refreshPage();
    }
    
    private void showThemeChangeDialog(String theme) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Theme Changed");
        alert.setHeaderText("Theme Successfully Changed");
        alert.setContentText("The application theme has been changed to " + theme + " mode.\n\n" +
                "The interface will update immediately to reflect the new theme.");
        alert.showAndWait();
    }
    
    private void refreshPage() {
        // Clear and recreate the content
        content.getChildren().clear();
        updateContentStyle();
        createUI();
    }
    
    // Theme refresh no longer needed - CSS handles it automatically

    @Override
    public VBox getContent() {
        return content;
    }

    @Override
    public String getTitle() {
        return title;
    }
}