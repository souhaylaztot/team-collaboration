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

public class CreateAccountPage extends Application {
    
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField phoneField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ToggleGroup userTypeGroup;
    private CheckBox termsCheckBox;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart Property Manager - Create Account");
        
        // Main container with glass effect background
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 50%, #cbd5e1 100%);");
        
        // Create account card
        VBox createAccountCard = createAccountCard();
        root.getChildren().add(createAccountCard);
        
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createAccountCard() {
        VBox card = new VBox(25);
        card.setMaxWidth(500);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 30, 0, 0, 10); -fx-border-color: rgba(255,255,255,0.3); -fx-border-width: 1; -fx-border-radius: 25;");
        
        // Header
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
        logoContainer.setStyle("-fx-background-color: linear-gradient(135deg, #10b981, #059669); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.3), 15, 0, 0, 5);");
        
        Label logoIcon = new Label("👤");
        logoIcon.setStyle("-fx-font-size: 40px;");
        logoContainer.getChildren().add(logoIcon);
        
        // Title
        Label title = new Label("Create Your Account");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#1a202c"));
        
        // Subtitle
        Label subtitle = new Label("Join Smart Property Manager today");
        subtitle.setFont(Font.font("Segoe UI", 16));
        subtitle.setTextFill(Color.web("#4a5568"));
        
        header.getChildren().addAll(logoContainer, title, subtitle);
        return header;
    }
    
    private VBox createForm() {
        VBox form = new VBox(20);
        
        // Name fields row
        HBox nameRow = new HBox(15);
        nameRow.setAlignment(Pos.CENTER);
        
        VBox firstNameBox = createFieldBox("First Name", "Enter your first name");
        firstNameField = (TextField) firstNameBox.getChildren().get(1);
        firstNameBox.setPrefWidth(200);
        
        VBox lastNameBox = createFieldBox("Last Name", "Enter your last name");
        lastNameField = (TextField) lastNameBox.getChildren().get(1);
        lastNameBox.setPrefWidth(200);
        
        nameRow.getChildren().addAll(firstNameBox, lastNameBox);
        
        // Email field
        VBox emailBox = createFieldBox("Email Address", "Enter your email address");
        emailField = (TextField) emailBox.getChildren().get(1);
        
        // Phone field
        VBox phoneBox = createFieldBox("Phone Number", "Enter your phone number");
        phoneField = (TextField) phoneBox.getChildren().get(1);
        
        // Password fields
        VBox passwordBox = createPasswordBox("Password", "Create a strong password");
        passwordField = (PasswordField) passwordBox.getChildren().get(1);
        
        VBox confirmPasswordBox = createPasswordBox("Confirm Password", "Confirm your password");
        confirmPasswordField = (PasswordField) confirmPasswordBox.getChildren().get(1);
        
        // Account type selection
        VBox accountTypeBox = new VBox(10);
        Label accountTypeLabel = new Label("Account Type");
        accountTypeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        accountTypeLabel.setTextFill(Color.web("#1a202c"));
        
        HBox radioBox = new HBox(20);
        radioBox.setAlignment(Pos.CENTER_LEFT);
        
        userTypeGroup = new ToggleGroup();
        
        RadioButton userRadio = new RadioButton("Property Owner");
        userRadio.setToggleGroup(userTypeGroup);
        userRadio.setSelected(true);
        userRadio.setFont(Font.font("Segoe UI", 14));
        userRadio.setTextFill(Color.web("#1a202c"));
        
        RadioButton adminRadio = new RadioButton("Administrator");
        adminRadio.setToggleGroup(userTypeGroup);
        adminRadio.setFont(Font.font("Segoe UI", 14));
        adminRadio.setTextFill(Color.web("#1a202c"));
        
        radioBox.getChildren().addAll(userRadio, adminRadio);
        accountTypeBox.getChildren().addAll(accountTypeLabel, radioBox);
        
        // Terms and conditions
        HBox termsBox = new HBox(10);
        termsBox.setAlignment(Pos.CENTER_LEFT);
        
        termsCheckBox = new CheckBox();
        
        Label termsLabel = new Label("I agree to the ");
        termsLabel.setFont(Font.font("Segoe UI", 14));
        termsLabel.setTextFill(Color.web("#1a202c"));
        
        Hyperlink termsLink = new Hyperlink("Terms & Conditions");
        termsLink.setFont(Font.font("Segoe UI", 14));
        termsLink.setTextFill(Color.web("#10b981"));
        
        Label andLabel = new Label(" and ");
        andLabel.setFont(Font.font("Segoe UI", 14));
        andLabel.setTextFill(Color.web("#1a202c"));
        
        Hyperlink privacyLink = new Hyperlink("Privacy Policy");
        privacyLink.setFont(Font.font("Segoe UI", 14));
        privacyLink.setTextFill(Color.web("#10b981"));
        
        termsBox.getChildren().addAll(termsCheckBox, termsLabel, termsLink, andLabel, privacyLink);
        
        // Create account button
        Button createAccountBtn = new Button("Create Account");
        createAccountBtn.setPrefWidth(420);
        createAccountBtn.setPrefHeight(55);
        createAccountBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        createAccountBtn.setStyle("-fx-background-color: linear-gradient(135deg, #10b981, #059669); -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.3), 10, 0, 0, 3);");
        
        createAccountBtn.setOnMouseEntered(e -> createAccountBtn.setStyle("-fx-background-color: linear-gradient(135deg, #059669, #047857); -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.4), 15, 0, 0, 5); -fx-scale-x: 1.02; -fx-scale-y: 1.02;"));
        createAccountBtn.setOnMouseExited(e -> createAccountBtn.setStyle("-fx-background-color: linear-gradient(135deg, #10b981, #059669); -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.3), 10, 0, 0, 3); -fx-scale-x: 1.0; -fx-scale-y: 1.0;"));
        
        createAccountBtn.setOnAction(e -> handleCreateAccount());
        
        form.getChildren().addAll(nameRow, emailBox, phoneBox, passwordBox, confirmPasswordBox, accountTypeBox, termsBox, createAccountBtn);
        return form;
    }
    
    private VBox createFieldBox(String labelText, String promptText) {
        VBox box = new VBox(10);
        
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        label.setTextFill(Color.web("#1a202c"));
        
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setPrefHeight(50);
        field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(203, 213, 225, 0.8); -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
        
        // Focus effects
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-border-color: #10b981; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
            } else {
                field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(203, 213, 225, 0.8); -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
            }
        });
        
        box.getChildren().addAll(label, field);
        return box;
    }
    
    private VBox createPasswordBox(String labelText, String promptText) {
        VBox box = new VBox(10);
        
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        label.setTextFill(Color.web("#1a202c"));
        
        PasswordField field = new PasswordField();
        field.setPromptText(promptText);
        field.setPrefHeight(50);
        field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(203, 213, 225, 0.8); -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
        
        // Focus effects
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-border-color: #10b981; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
            } else {
                field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(203, 213, 225, 0.8); -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 15; -fx-font-size: 15px; -fx-text-fill: #1a202c; -fx-prompt-text-fill: rgba(26, 32, 44, 0.6);");
            }
        });
        
        box.getChildren().addAll(label, field);
        return box;
    }
    
    private VBox createFooter() {
        VBox footer = new VBox(15);
        footer.setAlignment(Pos.CENTER);
        
        // Back to login
        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.CENTER);
        
        Label alreadyHaveLabel = new Label("Already have an account?");
        alreadyHaveLabel.setFont(Font.font("Segoe UI", 14));
        alreadyHaveLabel.setTextFill(Color.web("#1a202c"));
        
        Hyperlink loginLink = new Hyperlink("Sign In");
        loginLink.setFont(Font.font("Segoe UI", 14));
        loginLink.setTextFill(Color.web("#10b981"));
        loginLink.setOnMouseEntered(e -> loginLink.setTextFill(Color.web("#059669")));
        loginLink.setOnMouseExited(e -> loginLink.setTextFill(Color.web("#10b981")));
        loginLink.setOnAction(e -> backToLogin());
        
        loginBox.getChildren().addAll(alreadyHaveLabel, loginLink);
        
        // Copyright
        Label copyright = new Label("© 2025 Smart Property Manager. All rights reserved.");
        copyright.setFont(Font.font("Segoe UI", 12));
        copyright.setTextFill(Color.web("#4a5568"));
        
        footer.getChildren().addAll(loginBox, copyright);
        return footer;
    }
    
    private void handleCreateAccount() {
        // Validate fields
        if (!validateFields()) {
            return;
        }
        
        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Created");
        alert.setHeaderText(null);
        alert.setContentText("Your account has been created successfully! You can now sign in.");
        alert.showAndWait();
        
        // Go back to login
        backToLogin();
    }
    
    private boolean validateFields() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || 
            emailField.getText().isEmpty() || phoneField.getText().isEmpty() ||
            passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return false;
        }
        
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Error", "Passwords do not match.");
            return false;
        }
        
        if (!termsCheckBox.isSelected()) {
            showAlert("Error", "Please accept the Terms & Conditions and Privacy Policy.");
            return false;
        }
        
        return true;
    }
    
    private void backToLogin() {
        try {
            Stage currentStage = (Stage) firstNameField.getScene().getWindow();
            currentStage.close();
            
            LoginPage loginPage = new LoginPage();
            Stage loginStage = new Stage();
            loginPage.start(loginStage);
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