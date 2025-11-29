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

public class LoginPage extends Application {
    
    private TextField usernameField;
    private PasswordField passwordField;
    private CheckBox rememberMeBox;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart Property Manager - Login");
        
        // Main container
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        
        // Login card
        VBox loginCard = createLoginCard();
        root.getChildren().add(loginCard);
        
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createLoginCard() {
        VBox card = new VBox(25);
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);");
        
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
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        
        // Logo
        Label logoIcon = new Label("🏢");
        logoIcon.setStyle("-fx-font-size: 48px; -fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-background-radius: 15; -fx-padding: 15; -fx-text-fill: white;");
        
        // Title
        Label title = new Label("Smart Property Manager");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));
        
        // Subtitle
        Label subtitle = new Label("Manage your properties with ease");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));
        
        header.getChildren().addAll(logoIcon, title, subtitle);
        return header;
    }
    
    private VBox createForm() {
        VBox form = new VBox(20);
        
        // Username field
        VBox usernameBox = new VBox(8);
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        usernameLabel.setTextFill(Color.web("#2c3e50"));
        
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefHeight(45);
        usernameField.setStyle("-fx-background-color: rgba(248, 249, 250, 0.8); -fx-border-color: #e9ecef; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 14px;");
        
        usernameBox.getChildren().addAll(usernameLabel, usernameField);
        
        // Password field
        VBox passwordBox = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        passwordLabel.setTextFill(Color.web("#2c3e50"));
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(45);
        passwordField.setStyle("-fx-background-color: rgba(248, 249, 250, 0.8); -fx-border-color: #e9ecef; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 14px;");
        
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        
        // Remember me and forgot password
        HBox optionsBox = new HBox();
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        
        rememberMeBox = new CheckBox("Remember me");
        rememberMeBox.setFont(Font.font("Arial", 12));
        rememberMeBox.setTextFill(Color.web("#2c3e50"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Hyperlink forgotLink = new Hyperlink("Forgot password?");
        forgotLink.setFont(Font.font("Arial", 12));
        forgotLink.setTextFill(Color.web("#2C3E8C"));
        
        optionsBox.getChildren().addAll(rememberMeBox, spacer, forgotLink);
        
        // Sign in button
        Button signInBtn = new Button("Sign In");
        signInBtn.setPrefWidth(320);
        signInBtn.setPrefHeight(45);
        signInBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        signInBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        
        signInBtn.setOnMouseEntered(e -> signInBtn.setStyle("-fx-background-color: linear-gradient(135deg, #1e2a5e, #3ba89a); -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;"));
        signInBtn.setOnMouseExited(e -> signInBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;"));
        
        signInBtn.setOnAction(e -> handleLogin());
        
        form.getChildren().addAll(usernameBox, passwordBox, optionsBox, signInBtn);
        return form;
    }
    
    private VBox createFooter() {
        VBox footer = new VBox(15);
        footer.setAlignment(Pos.CENTER);
        
        // Contact admin
        HBox contactBox = new HBox(5);
        contactBox.setAlignment(Pos.CENTER);
        
        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setFont(Font.font("Arial", 12));
        noAccountLabel.setTextFill(Color.web("#2c3e50"));
        
        Hyperlink contactLink = new Hyperlink("Contact Administrator");
        contactLink.setFont(Font.font("Arial", 12));
        contactLink.setTextFill(Color.web("#2C3E8C"));
        
        contactBox.getChildren().addAll(noAccountLabel, contactLink);
        
        // Copyright
        Label copyright = new Label("© 2025 Smart Property Manager. All rights reserved.");
        copyright.setFont(Font.font("Arial", 10));
        copyright.setTextFill(Color.web("#7f8c8d"));
        
        footer.getChildren().addAll(contactBox, copyright);
        return footer;
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        // Simple validation (you can enhance this)
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }
        
        // For demo purposes, accept any non-empty credentials
        // In real app, validate against database
        openMainApplication();
    }
    
    private void openMainApplication() {
        try {
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
            
            MainApp mainApp = new MainApp();
            Stage mainStage = new Stage();
            mainApp.start(mainStage);
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