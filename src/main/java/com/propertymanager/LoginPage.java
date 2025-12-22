package com.propertymanager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends Application {
    
    private TextField usernameField;
    private PasswordField passwordField;
    private CheckBox rememberMeBox;
    private ToggleGroup userTypeGroup;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart Property Manager - Login");
        
        // Main container with glass effect background
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 50%, #cbd5e1 100%);");
        
        // Login card
        VBox loginCard = createLoginCard();
        root.getChildren().add(loginCard);
        
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createLoginCard() {
        VBox card = new VBox(30);
        card.setMaxWidth(450);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(50));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 30, 0, 0, 10); -fx-border-color: rgba(255,255,255,0.3); -fx-border-width: 1; -fx-border-radius: 25;");
        
        // Logo and title
        VBox header = createHeader();
        
        // Form
        VBox form = createForm();
        
        // Footer
        VBox footer = createFooter();
        
        card.getChildren().addAll(header, form, footer);
        return card;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(20);
        header.setAlignment(Pos.CENTER);
        
        // Logo container with gradient background
        StackPane logoContainer = new StackPane();
        logoContainer.setPrefSize(80, 80);
        logoContainer.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(44,62,140,0.3), 15, 0, 0, 5);");
        
        Label logoIcon = new Label("🏢");
        logoIcon.setStyle("-fx-font-size: 40px;");
        logoContainer.getChildren().add(logoIcon);
        
        // Title
        Label title = new Label("Smart Property Manager");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#1a202c"));
        
        // Subtitle
        Label subtitle = new Label("Manage your properties with ease");
        subtitle.setFont(Font.font("Segoe UI", 16));
        subtitle.setTextFill(Color.web("#4a5568"));
        
        header.getChildren().addAll(logoContainer, title, subtitle);
        return header;
    }
    
    private VBox createForm() {
        VBox form = new VBox(25);
        
        // Username field
        VBox usernameBox = new VBox(10);
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        usernameLabel.setTextFill(Color.web("#1a202c"));
        
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefHeight(50);
        usernameField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(203, 213, 225, 0.8); -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
        
        // Focus effects
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                usernameField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-border-color: #2C3E8C; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
            } else {
                usernameField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(203, 213, 225, 0.8); -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
            }
        });
        
        usernameBox.getChildren().addAll(usernameLabel, usernameField);
        
        // Password field
        VBox passwordBox = new VBox(10);
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        passwordLabel.setTextFill(Color.web("#1a202c"));
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(50);
        passwordField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(203, 213, 225, 0.8); -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
        
        // Focus effects
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-border-color: #2C3E8C; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
            } else {
                passwordField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(203, 213, 225, 0.8); -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
            }
        });
        
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        
        // User type selection
        VBox userTypeBox = new VBox(10);
        Label userTypeLabel = new Label("Login as");
        userTypeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        userTypeLabel.setTextFill(Color.web("#1a202c"));
        
        HBox radioBox = new HBox(20);
        radioBox.setAlignment(Pos.CENTER_LEFT);
        
        userTypeGroup = new ToggleGroup();
        
        RadioButton adminRadio = new RadioButton("Admin User");
        adminRadio.setToggleGroup(userTypeGroup);
        adminRadio.setSelected(true);
        adminRadio.setFont(Font.font("Segoe UI", 14));
        adminRadio.setTextFill(Color.web("#1a202c"));
        
        RadioButton userRadio = new RadioButton("Regular User");
        userRadio.setToggleGroup(userTypeGroup);
        userRadio.setFont(Font.font("Segoe UI", 14));
        userRadio.setTextFill(Color.web("#1a202c"));
        
        radioBox.getChildren().addAll(adminRadio, userRadio);
        userTypeBox.getChildren().addAll(userTypeLabel, radioBox);
        
        // Remember me and forgot password
        HBox optionsBox = new HBox();
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        
        rememberMeBox = new CheckBox("Remember me");
        rememberMeBox.setFont(Font.font("Segoe UI", 14));
        rememberMeBox.setTextFill(Color.web("#1a202c"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Hyperlink forgotLink = new Hyperlink("Forgot password?");
        forgotLink.setFont(Font.font("Segoe UI", 14));
        forgotLink.setTextFill(Color.web("#2C3E8C"));
        forgotLink.setOnMouseEntered(e -> forgotLink.setTextFill(Color.web("#4FD1C5")));
        forgotLink.setOnMouseExited(e -> forgotLink.setTextFill(Color.web("#2C3E8C")));
        
        optionsBox.getChildren().addAll(rememberMeBox, spacer, forgotLink);
        
        // Sign in button
        Button signInBtn = new Button("Sign In");
        signInBtn.setPrefWidth(350);
        signInBtn.setPrefHeight(55);
        signInBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        signInBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(44,62,140,0.3), 10, 0, 0, 3);");
        
        signInBtn.setOnMouseEntered(e -> signInBtn.setStyle("-fx-background-color: linear-gradient(135deg, #1e2a5e, #3ba89a); -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(44,62,140,0.4), 15, 0, 0, 5); -fx-scale-x: 1.02; -fx-scale-y: 1.02;"));
        signInBtn.setOnMouseExited(e -> signInBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(44,62,140,0.3), 10, 0, 0, 3); -fx-scale-x: 1.0; -fx-scale-y: 1.0;"));
        
        signInBtn.setOnAction(e -> handleLogin());
        
        form.getChildren().addAll(usernameBox, passwordBox, userTypeBox, optionsBox, signInBtn);
        return form;
    }
    
    private VBox createFooter() {
        VBox footer = new VBox(15);
        footer.setAlignment(Pos.CENTER);
        
        // Contact admin
        HBox contactBox = new HBox(5);
        contactBox.setAlignment(Pos.CENTER);
        
        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setFont(Font.font("Segoe UI", 14));
        noAccountLabel.setTextFill(Color.web("#1a202c"));
        
        Hyperlink createAccountLink = new Hyperlink("Create Account");
        createAccountLink.setFont(Font.font("Segoe UI", 14));
        createAccountLink.setTextFill(Color.web("#2C3E8C"));
        createAccountLink.setOnMouseEntered(e -> createAccountLink.setTextFill(Color.web("#4FD1C5")));
        createAccountLink.setOnMouseExited(e -> createAccountLink.setTextFill(Color.web("#2C3E8C")));
        createAccountLink.setOnAction(e -> openCreateAccountPage());
        
        contactBox.getChildren().addAll(noAccountLabel, createAccountLink);
        
        // Copyright
        Label copyright = new Label("© 2025 Smart Property Manager. All rights reserved.");
        copyright.setFont(Font.font("Segoe UI", 12));
        copyright.setTextFill(Color.web("#4a5568"));
        
        footer.getChildren().addAll(contactBox, copyright);
        return footer;
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }
        
        // Authenticate against database
        authenticateUser(username, password);
    }
    
    private void authenticateUser(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection()) {
            // Check users table with password verification
            String userSQL = "SELECT email, user_type, phone, full_name FROM users WHERE email = ? AND password_hash = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(userSQL)) {
                stmt.setString(1, username);
                stmt.setString(2, hashPassword(password));
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    String userEmail = rs.getString("email");
                    String userName = rs.getString("full_name");
                    String userType = rs.getString("user_type");
                    String userPhone = rs.getString("phone");
                    
                    // Set user type based on role from database
                    RadioButton adminRadio = null;
                    RadioButton userRadio = null;
                    
                    for (Toggle toggle : userTypeGroup.getToggles()) {
                        RadioButton rb = (RadioButton) toggle;
                        if (rb.getText().equals("Admin User")) {
                            adminRadio = rb;
                        } else if (rb.getText().equals("Regular User")) {
                            userRadio = rb;
                        }
                    }
                    
                    if ("admin".equals(userType) && adminRadio != null) {
                        adminRadio.setSelected(true);
                    } else if (userRadio != null) {
                        userRadio.setSelected(true);
                    }
                    
                    // Create or update admin profile if administrator
                    if ("admin".equals(userType)) {
                        updateAdminProfile(conn, userName, userEmail, userPhone);
                    }
                    
                    openMainApplication(userEmail, userName);
                    return;
                }
            }
            
            // If authentication fails, show error
            showAlert("Login Failed", "Invalid username/email or password. Please try again.");
            
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
            showAlert("Database Error", "Unable to connect to database. Please try again later.");
        }
    }
    
    private void updateAdminProfile(Connection conn, String name, String email, String phone) {
        try {
            String adminSQL = "INSERT INTO admin_profile (name, email, phone, department, password_hash) " +
                             "VALUES (?, ?, ?, ?, ?) ON CONFLICT (email) DO UPDATE SET " +
                             "name = EXCLUDED.name, phone = EXCLUDED.phone, updated_at = CURRENT_TIMESTAMP";
            
            try (PreparedStatement stmt = conn.prepareStatement(adminSQL)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, phone);
                stmt.setString(4, "IT Administration");
                stmt.setString(5, hashPassword("admin123")); // Default admin password
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error updating admin profile: " + e.getMessage());
        }
    }
    
    private String hashPassword(String password) {
        // Simple hash for demo - in production use proper hashing like BCrypt
        return "hashed_" + password.hashCode();
    }
    
    private void openMainApplication(String userEmail, String userName) {
        try {
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
            
            // Get selected user type
            RadioButton selectedRadio = (RadioButton) userTypeGroup.getSelectedToggle();
            String selectedUserType = selectedRadio.getText();
            
            MainApp mainApp = new MainApp(selectedUserType, userEmail, userName);
            Stage mainStage = new Stage();
            mainApp.start(mainStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openCreateAccountPage() {
        try {
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
            
            CreateAccountPage createAccountPage = new CreateAccountPage();
            Stage createAccountStage = new Stage();
            createAccountPage.start(createAccountStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}