package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminProfilePage extends VBox {
    private String currentUserEmail;
    private String currentUserName;
    private String currentUserPhone;
    private String currentUserDepartment;
    
    public AdminProfilePage() {
        loadUserData();
        initProfilePage();
    }
    
    public AdminProfilePage(String userEmail) {
        this.currentUserEmail = userEmail;
        loadUserData();
        initProfilePage();
    }
    
    private void initProfilePage() {
        setSpacing(30);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: #f8fafc;");
        
        // Header
        VBox header = createHeader();
        
        // Main content
        HBox mainContent = new HBox(30);
        mainContent.setAlignment(Pos.TOP_LEFT);
        
        // Left column - Profile info
        VBox leftColumn = createLeftColumn();
        leftColumn.setPrefWidth(400);
        
        // Right column - Settings & Stats
        VBox rightColumn = createRightColumn();
        rightColumn.setPrefWidth(500);
        
        mainContent.getChildren().addAll(leftColumn, rightColumn);
        
        getChildren().addAll(header, mainContent);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("👨‍💼 Administrator Profile");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#1a202c"));
        
        Label subtitle = new Label("Manage your account settings and system preferences");
        subtitle.setFont(Font.font("Segoe UI", 16));
        subtitle.setTextFill(Color.web("#4a5568"));
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    private VBox createLeftColumn() {
        VBox column = new VBox(25);
        
        // Profile card
        VBox profileCard = createProfileCard();
        
        // Quick actions
        VBox actionsCard = createQuickActionsCard();
        
        column.getChildren().addAll(profileCard, actionsCard);
        return column;
    }
    
    private VBox createProfileCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        // Profile picture and basic info
        VBox profileInfo = new VBox(20);
        profileInfo.setAlignment(Pos.CENTER);
        
        Button profilePic = new Button("👨‍💼");
        profilePic.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 50; -fx-font-size: 40px; -fx-padding: 20;");
        profilePic.setPrefSize(100, 100);
        
        Label name = new Label(currentUserName != null ? currentUserName : "Administrator");
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        name.setTextFill(Color.web("#1a202c"));
        
        Label role = new Label(currentUserDepartment != null ? currentUserDepartment : "System Administrator");
        role.setFont(Font.font("Segoe UI", 16));
        role.setTextFill(Color.web("#4a5568"));
        
        Button editBtn = new Button("✏️ Edit Profile");
        editBtn.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #374151; -fx-background-radius: 10; -fx-padding: 12 20; -fx-cursor: hand;");
        editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: #e5e7eb; -fx-text-fill: #374151; -fx-background-radius: 10; -fx-padding: 12 20; -fx-cursor: hand;"));
        editBtn.setOnMouseExited(e -> editBtn.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #374151; -fx-background-radius: 10; -fx-padding: 12 20; -fx-cursor: hand;"));
        editBtn.setOnAction(e -> showEditProfileDialog());
        
        profileInfo.getChildren().addAll(profilePic, name, role, editBtn);
        
        // Contact info
        VBox contactInfo = new VBox(15);
        contactInfo.getChildren().addAll(
            createInfoRow("📧", "Email", currentUserEmail != null ? currentUserEmail : "admin@propertymanager.com"),
            createInfoRow("📱", "Phone", currentUserPhone != null ? currentUserPhone : "+212 6 12 34 56 78"),
            createInfoRow("🏢", "Department", currentUserDepartment != null ? currentUserDepartment : "IT Administration"),
            createInfoRow("📅", "Joined", "January 15, 2023")
        );
        
        card.getChildren().addAll(profileInfo, new Separator(), contactInfo);
        return card;
    }
    
    private VBox createQuickActionsCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        Label title = new Label("⚡ Quick Actions");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1a202c"));
        
        VBox actions = new VBox(10);
        actions.getChildren().addAll(
            createActionButton("👥", "Manage Users", "#3b82f6"),
            createActionButton("🔐", "Security Settings", "#ef4444"),
            createActionButton("📊", "System Reports", "#10b981"),
            createActionButton("⚙️", "System Settings", "#6b7280")
        );
        
        card.getChildren().addAll(title, actions);
        return card;
    }
    
    private VBox createRightColumn() {
        VBox column = new VBox(25);
        
        // Statistics card
        VBox statsCard = createStatsCard();
        
        // Settings card
        VBox settingsCard = createSettingsCard();
        
        column.getChildren().addAll(statsCard, settingsCard);
        return column;
    }
    
    private VBox createStatsCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        Label title = new Label("📈 Admin Statistics");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1a202c"));
        
        GridPane stats = new GridPane();
        stats.setHgap(20);
        stats.setVgap(20);
        
        stats.add(createStatBox("156", "Total Properties", "#3b82f6"), 0, 0);
        stats.add(createStatBox("24", "Active Users", "#10b981"), 1, 0);
        stats.add(createStatBox("89", "Pending Requests", "#f59e0b"), 0, 1);
        stats.add(createStatBox("12", "System Alerts", "#ef4444"), 1, 1);
        
        card.getChildren().addAll(title, stats);
        return card;
    }
    
    private VBox createSettingsCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);");
        
        Label title = new Label("⚙️ Account Settings");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1a202c"));
        
        VBox settings = new VBox(20);
        settings.getChildren().addAll(
            createSettingRow("🔔", "Email Notifications", true),
            createSettingRow("🔐", "Two-Factor Authentication", true),
            createSettingRow("🌙", "Dark Mode", false),
            createSettingRow("📱", "SMS Alerts", true)
        );
        
        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 15 30; -fx-font-size: 16px; -fx-cursor: hand;");
        saveBtn.setPrefWidth(200);
        saveBtn.setOnAction(e -> saveSettings());
        
        card.getChildren().addAll(title, settings, saveBtn);
        return card;
    }
    
    private HBox createInfoRow(String icon, String label, String value) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        
        Label labelText = new Label(label + ":");
        labelText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        labelText.setTextFill(Color.web("#4a5568"));
        labelText.setPrefWidth(80);
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("Segoe UI", 14));
        valueText.setTextFill(Color.web("#1a202c"));
        
        row.getChildren().addAll(iconLabel, labelText, valueText);
        return row;
    }
    
    private Button createActionButton(String icon, String text, String color) {
        Button btn = new Button();
        btn.setPrefWidth(300);
        btn.setPrefHeight(50);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 15;");
        
        HBox content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(18));
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        content.getChildren().addAll(iconLabel, textLabel);
        btn.setGraphic(content);
        btn.setOnAction(e -> handleQuickAction(text));
        
        return btn;
    }
    
    private VBox createStatBox(String number, String label, String color) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 15;");
        box.setPrefSize(120, 100);
        
        Label numLabel = new Label(number);
        numLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        numLabel.setTextFill(Color.WHITE);
        
        Label textLabel = new Label(label);
        textLabel.setFont(Font.font("Segoe UI", 12));
        textLabel.setTextFill(Color.WHITE);
        textLabel.setWrapText(true);
        textLabel.setAlignment(Pos.CENTER);
        
        box.getChildren().addAll(numLabel, textLabel);
        return box;
    }
    
    private HBox createSettingRow(String icon, String text, boolean enabled) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Segoe UI", 14));
        textLabel.setTextFill(Color.web("#1a202c"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        CheckBox toggle = new CheckBox();
        toggle.setSelected(enabled);
        
        row.getChildren().addAll(iconLabel, textLabel, spacer, toggle);
        toggle.setOnAction(e -> updateSetting(text, toggle.isSelected()));
        return row;
    }
    
    private void showEditProfileDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("✏️ Edit Administrator Profile");
        dialog.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f8fafc;");
        
        // Header
        Label title = new Label("👨💼 Edit Profile Information");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a202c"));
        
        // Form
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
        
        // Form fields
        TextField nameField = new TextField(currentUserName != null ? currentUserName : "Administrator");
        TextField emailField = new TextField(currentUserEmail != null ? currentUserEmail : "admin@propertymanager.com");
        TextField phoneField = new TextField(currentUserPhone != null ? currentUserPhone : "+212 6 12 34 56 78");
        TextField departmentField = new TextField(currentUserDepartment != null ? currentUserDepartment : "IT Administration");
        PasswordField currentPasswordField = new PasswordField();
        PasswordField newPasswordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        
        // Style fields
        String fieldStyle = "-fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 14px; -fx-border-color: #e2e8f0; -fx-border-radius: 8;";
        nameField.setStyle(fieldStyle);
        emailField.setStyle(fieldStyle);
        phoneField.setStyle(fieldStyle);
        departmentField.setStyle(fieldStyle);
        currentPasswordField.setStyle(fieldStyle);
        newPasswordField.setStyle(fieldStyle);
        confirmPasswordField.setStyle(fieldStyle);
        
        // Add to form
        form.add(createLabel("👤 Full Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(createLabel("📧 Email:"), 0, 1);
        form.add(emailField, 1, 1);
        form.add(createLabel("📱 Phone:"), 0, 2);
        form.add(phoneField, 1, 2);
        form.add(createLabel("🏢 Department:"), 0, 3);
        form.add(departmentField, 1, 3);
        form.add(createLabel("🔐 Current Password:"), 0, 4);
        form.add(currentPasswordField, 1, 4);
        form.add(createLabel("🔑 New Password:"), 0, 5);
        form.add(newPasswordField, 1, 5);
        form.add(createLabel("✅ Confirm Password:"), 0, 6);
        form.add(confirmPasswordField, 1, 6);
        
        // Buttons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 12 25; -fx-font-size: 14px; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> {
            if (validateAndSaveProfile(nameField.getText(), emailField.getText(), phoneField.getText(), 
                                     departmentField.getText(), currentPasswordField.getText(), 
                                     newPasswordField.getText(), confirmPasswordField.getText())) {
                dialog.close();
            }
        });
        
        Button cancelBtn = new Button("❌ Cancel");
        cancelBtn.setStyle("-fx-background-color: #e5e7eb; -fx-text-fill: #374151; -fx-background-radius: 10; -fx-padding: 12 25; -fx-font-size: 14px; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(saveBtn, cancelBtn);
        
        root.getChildren().addAll(title, form, buttons);
        
        Scene scene = new Scene(root, 500, 600);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#374151"));
        return label;
    }
    
    private boolean validateAndSaveProfile(String name, String email, String phone, String department, 
                                         String currentPassword, String newPassword, String confirmPassword) {
        // Validation
        if (name.trim().isEmpty() || email.trim().isEmpty()) {
            showAlert("Validation Error", "Name and email are required fields.", Alert.AlertType.ERROR);
            return false;
        }
        
        if (!email.contains("@")) {
            showAlert("Validation Error", "Please enter a valid email address.", Alert.AlertType.ERROR);
            return false;
        }
        
        if (!newPassword.isEmpty()) {
            if (currentPassword.isEmpty()) {
                showAlert("Validation Error", "Current password is required to change password.", Alert.AlertType.ERROR);
                return false;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                showAlert("Validation Error", "New password and confirmation do not match.", Alert.AlertType.ERROR);
                return false;
            }
            
            if (newPassword.length() < 6) {
                showAlert("Validation Error", "New password must be at least 6 characters long.", Alert.AlertType.ERROR);
                return false;
            }
        }
        
        // Save to database
        try (Connection conn = DatabaseManager.getConnection()) {
            // Check if admin table exists, create if not
            String createTableSQL = "CREATE TABLE IF NOT EXISTS admin_profile (" +
                                  "id SERIAL PRIMARY KEY, " +
                                  "name VARCHAR(100) NOT NULL, " +
                                  "email VARCHAR(100) UNIQUE NOT NULL, " +
                                  "phone VARCHAR(20), " +
                                  "department VARCHAR(50), " +
                                  "password_hash VARCHAR(255), " +
                                  "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                  "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                                  ")";
            
            try (PreparedStatement createStmt = conn.prepareStatement(createTableSQL)) {
                createStmt.executeUpdate();
            }
            
            // Check if profile exists
            String checkSQL = "SELECT id FROM admin_profile WHERE email = ?";
            boolean profileExists = false;
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                profileExists = rs.next();
            }
            
            String sql;
            if (profileExists) {
                if (!newPassword.isEmpty()) {
                    sql = "UPDATE admin_profile SET name = ?, phone = ?, department = ?, password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?";
                } else {
                    sql = "UPDATE admin_profile SET name = ?, phone = ?, department = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?";
                }
            } else {
                sql = "INSERT INTO admin_profile (name, email, phone, department, password_hash) VALUES (?, ?, ?, ?, ?)";
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                
                if (profileExists) {
                    stmt.setString(2, phone);
                    stmt.setString(3, department);
                    
                    if (!newPassword.isEmpty()) {
                        stmt.setString(4, hashPassword(newPassword));
                        stmt.setString(5, email);
                    } else {
                        stmt.setString(4, email);
                    }
                } else {
                    stmt.setString(2, email);
                    stmt.setString(3, phone);
                    stmt.setString(4, department);
                    stmt.setString(5, !newPassword.isEmpty() ? hashPassword(newPassword) : hashPassword("admin123"));
                }
                
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Update current user data
                    currentUserName = name;
                    currentUserEmail = email;
                    currentUserPhone = phone;
                    currentUserDepartment = department;
                    
                    // Refresh the profile display
                    getChildren().clear();
                    initProfilePage();
                    
                    showAlert("Success", "Profile updated successfully!", Alert.AlertType.INFORMATION);
                    return true;
                } else {
                    showAlert("Error", "Failed to update profile. Please try again.", Alert.AlertType.ERROR);
                    return false;
                }
            }
            
        } catch (SQLException e) {
            showAlert("Database Error", "Error updating profile: " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }
    
    private String hashPassword(String password) {
        // Simple hash for demo - in production use proper hashing like BCrypt
        return "hashed_" + password.hashCode();
    }
    
    private void loadUserData() {
        try (Connection conn = DatabaseManager.getConnection()) {
            // First try to get from admin_profile table
            String sql = "SELECT name, email, phone, department FROM admin_profile ORDER BY id DESC LIMIT 1";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    currentUserName = rs.getString("name");
                    currentUserEmail = rs.getString("email");
                    currentUserPhone = rs.getString("phone");
                    currentUserDepartment = rs.getString("department");
                } else {
                    // If no admin profile exists, try to get from users table
                    String userSQL = "SELECT name, email, phone FROM users WHERE role = 'admin' OR role = 'Administrator' LIMIT 1";
                    
                    try (PreparedStatement userStmt = conn.prepareStatement(userSQL)) {
                        ResultSet userRs = userStmt.executeQuery();
                        
                        if (userRs.next()) {
                            currentUserName = userRs.getString("name");
                            currentUserEmail = userRs.getString("email");
                            currentUserPhone = userRs.getString("phone");
                            currentUserDepartment = "System Administrator";
                        }
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading user data: " + e.getMessage());
            // Set default values if database error
            currentUserName = "Administrator";
            currentUserEmail = "admin@propertymanager.com";
            currentUserPhone = "+212 6 12 34 56 78";
            currentUserDepartment = "IT Administration";
        }
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void handleQuickAction(String action) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(action);
        alert.setHeaderText("Quick Action: " + action);
        alert.setContentText("Opening " + action.toLowerCase() + " panel...\n\nThis would navigate to the " + action + " section.");
        alert.showAndWait();
    }
    
    private void saveSettings() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText("Success");
        alert.setContentText("Your account settings have been saved successfully!");
        alert.showAndWait();
    }
    
    private void updateSetting(String setting, boolean enabled) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Setting Updated");
        alert.setHeaderText(setting);
        alert.setContentText(setting + " has been " + (enabled ? "enabled" : "disabled") + ".");
        alert.showAndWait();
    }
}