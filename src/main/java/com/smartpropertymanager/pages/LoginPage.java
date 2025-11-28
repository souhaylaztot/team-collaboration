package com.smartpropertymanager.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginPage implements Page {
    private VBox content;
    private String title = "Login";
    private Runnable onLoginSuccess;

    public LoginPage(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        content = new VBox();
        content.setStyle("-fx-background-color: linear-gradient(to bottom right, #F8FAFC, #E2E8F0);");
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        createLoginUI();
    }

    private void createLoginUI() {
        // Main container
        VBox mainContainer = new VBox();
        mainContainer.setMaxWidth(400);
        mainContainer.setAlignment(Pos.CENTER);

        // Login Card
        VBox loginCard = createLoginCard();
        mainContainer.getChildren().add(loginCard);

        // Footer
        VBox footer = createFooter();
        VBox.setMargin(footer, new Insets(24, 0, 0, 0));
        mainContainer.getChildren().add(footer);

        content.getChildren().add(mainContainer);
    }

    private VBox createLoginCard() {
        VBox loginCard = new VBox();
        loginCard.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 16; -fx-border-radius: 16; -fx-border-color: rgba(255, 255, 255, 0.3); -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 20, 0, 0, 4);");
        loginCard.setPadding(new Insets(32));
        loginCard.setSpacing(24);
        loginCard.setMaxWidth(400);

        // Logo and Title Section
        VBox headerSection = createHeaderSection();
        loginCard.getChildren().add(headerSection);

        // Login Form
        VBox loginForm = createLoginForm();
        loginCard.getChildren().add(loginForm);

        return loginCard;
    }

    private VBox createHeaderSection() {
        VBox headerSection = new VBox();
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setSpacing(16);

        // Logo Icon
        StackPane logoContainer = new StackPane();
        logoContainer.setPrefSize(64, 64);
        logoContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #2C3E8C, #4FD1C5); -fx-background-radius: 12;");
        logoContainer.setAlignment(Pos.CENTER);

        Label logoIcon = new Label("🏢");
        logoIcon.setStyle("-fx-text-fill: white; -fx-font-size: 24;");

        logoContainer.getChildren().add(logoIcon);

        // Title
        VBox titleSection = new VBox(4);
        titleSection.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Smart Property Manager");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label subtitleLabel = new Label("Manage your properties with ease");
        subtitleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);

        headerSection.getChildren().addAll(logoContainer, titleSection);
        return headerSection;
    }

    private VBox createLoginForm() {
        VBox loginForm = new VBox();
        loginForm.setSpacing(24);

        // Username Field
        VBox usernameField = createFormField("Username", "Enter your username", false);
        
        // Password Field
        VBox passwordField = createFormField("Password", "Enter your password", true);
        
        // Remember me and Forgot password
        HBox optionsRow = createOptionsRow();
        
        // Login Button
        Button loginButton = createLoginButton();

        loginForm.getChildren().addAll(usernameField, passwordField, optionsRow, loginButton);
        return loginForm;
    }

    private VBox createFormField(String labelText, String promptText, boolean isPassword) {
        VBox fieldContainer = new VBox(8);

        Label fieldLabel = new Label(labelText);
        fieldLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-font-weight: bold;");

        TextField inputField;
        if (isPassword) {
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText(promptText);
            inputField = passwordField;
        } else {
            TextField textField = new TextField();
            textField.setPromptText(promptText);
            inputField = textField;
        }

        inputField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E5E7EB; -fx-padding: 12 16; -fx-font-size: 14; -fx-text-fill: #000000;");
        inputField.setPrefHeight(44);

        fieldContainer.getChildren().addAll(fieldLabel, inputField);
        return fieldContainer;
    }

    private HBox createOptionsRow() {
        HBox optionsRow = new HBox();
        optionsRow.setAlignment(Pos.CENTER_LEFT);
        optionsRow.setSpacing(8);

        // Remember me checkbox
        CheckBox rememberMe = new CheckBox();
        rememberMe.setText("Remember me");
        rememberMe.setStyle("-fx-text-fill: #000000; -fx-font-size: 14;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Forgot password link
        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        forgotPassword.setStyle("-fx-text-fill: #2C3E8C; -fx-font-size: 14; -fx-border-color: transparent;");
        forgotPassword.setOnAction(e -> showForgotPasswordDialog());

        optionsRow.getChildren().addAll(rememberMe, spacer, forgotPassword);
        return optionsRow;
    }

    private Button createLoginButton() {
        Button loginButton = new Button("Sign In");
        loginButton.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-background-radius: 8; -fx-padding: 12 16; -fx-cursor: hand;");
        loginButton.setPrefHeight(44);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        
        loginButton.setOnAction(e -> handleLogin());

        return loginButton;
    }

    private VBox createFooter() {
        VBox footer = new VBox(8);
        footer.setAlignment(Pos.CENTER);

        // Contact admin link
        HBox contactRow = new HBox(4);
        contactRow.setAlignment(Pos.CENTER);

        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        Hyperlink contactAdmin = new Hyperlink("Contact Administrator");
        contactAdmin.setStyle("-fx-text-fill: #2C3E8C; -fx-font-size: 14; -fx-border-color: transparent;");
        contactAdmin.setOnAction(e -> showContactAdminDialog());

        contactRow.getChildren().addAll(noAccountLabel, contactAdmin);

        // Copyright
        Label copyrightLabel = new Label("© 2025 Smart Property Manager. All rights reserved.");
        copyrightLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");

        footer.getChildren().addAll(contactRow, copyrightLabel);
        return footer;
    }

    private void handleLogin() {
        // Simple login validation (in real app, this would validate against a database)
        // For demo purposes, we'll accept any non-empty credentials
        if (isValidLogin()) {
            if (onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        } else {
            showLoginError();
        }
    }

    private boolean isValidLogin() {
        // Simple validation - in real application, this would check against database
        // For demo, accept any non-empty username and password
        return true; // Always return true for demo purposes
    }

    private void showLoginError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText("Invalid Credentials");
        alert.setContentText("Please check your username and password and try again.");
        alert.showAndWait();
    }

    private void showForgotPasswordDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText("Password Recovery");
        alert.setContentText("Please contact your system administrator to reset your password.");
        alert.showAndWait();
    }

    private void showContactAdminDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contact Administrator");
        alert.setHeaderText("Account Request");
        alert.setContentText("Please contact the system administrator at admin@smartpropertymanager.com to request a new account.");
        alert.showAndWait();
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
