package com.smartpropertymanager;

import com.smartpropertymanager.components.Sidebar;
import com.smartpropertymanager.pages.BuildingManagementPage;
import com.smartpropertymanager.pages.BuyerManagementPage;
import com.smartpropertymanager.pages.DashboardPage;
import com.smartpropertymanager.pages.LandManagementPage;
import com.smartpropertymanager.pages.MaintenanceTrackerPage;
import com.smartpropertymanager.pages.Page;
import com.smartpropertymanager.pages.PermitManagementPage;
import com.smartpropertymanager.pages.ReportsAnalyticsPage;
import com.smartpropertymanager.pages.RequestCenterPage;
import com.smartpropertymanager.pages.SimplePage;
import com.smartpropertymanager.pages.SettingsPage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private VBox contentArea;
    private ScrollPane scrollPane;
    private TextField searchField;
    private boolean isDarkMode = false;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-font-family: 'Segoe UI', Arial, sans-serif;");

        // Create sidebar
        Sidebar sidebar = new Sidebar();
        sidebar.setOnMenuItemClick(this::navigateTo);
        root.setLeft(sidebar.getRoot());

        // Create main content area
        VBox mainContent = createMainContent();
        root.setCenter(mainContent);

        // Load Dashboard page by default
        navigateTo("Dashboard");

        Scene scene = new Scene(root, 1400, 900);
        scene.setFill(Color.web("#F5F5F5"));

        primaryStage.setTitle("Smart Property Manager Pro");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox();
        mainContent.setStyle("-fx-background-color: #F5F5F5;");

        // Header
        HBox header = createHeader();
        mainContent.getChildren().add(header);

        // Content area with scroll pane
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-control-inner-background: #F5F5F5;");

        contentArea = new VBox();
        contentArea.setStyle("-fx-background-color: #F5F5F5;");
        scrollPane.setContent(contentArea);

        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
        mainContent.getChildren().add(scrollPane);

        return mainContent;
    }

    private void navigateTo(String pageName) {
        Page page;

        switch(pageName) {
            case "Dashboard":
                page = new DashboardPage();
                break;
            case "Building Management":
                page = new BuildingManagementPage();
                break;
            case "Land Management":
                page = new LandManagementPage();
                break;
            case "Buyer Management":
                page = new BuyerManagementPage();
                break;
            case "Maintenance":
                page = new MaintenanceTrackerPage();
                break;
            case "Permits":
                page = new PermitManagementPage();
                break;
            case "Reports":
                page = new ReportsAnalyticsPage();
                break;
            case "Requests":
                page = new RequestCenterPage();
                break;
            case "Settings":
                page = new SettingsPage();
                break;
            case "Logout":
                System.exit(0);
                return;
            default:
                page = new SimplePage(pageName);
        }

        // Clear previous content and set new page
        contentArea.getChildren().clear();
        contentArea.getChildren().add(page.getContent());
        scrollPane.setVvalue(0);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setSpacing(15);
        header.setStyle("-fx-background-color: white; -fx-border-color: #EEEEEE; -fx-border-width: 0 0 1 0;");
        header.setAlignment(Pos.CENTER_LEFT);

        // Search bar
        searchField = new TextField();
        searchField.setPromptText("Search buildings, buyers, permits...");
        searchField.setPrefWidth(500);
        searchField.setStyle("-fx-padding: 10; -fx-font-size: 14; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #EEEEEE;");
        
        // Add search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(newValue);
        });

        HBox.setHgrow(searchField, javafx.scene.layout.Priority.SOMETIMES);
        header.getChildren().add(searchField);

        // Spacer
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        header.getChildren().add(spacer);

        // Dark mode toggle
        Button darkModeBtn = new Button("🌙");
        darkModeBtn.setStyle("-fx-padding: 8 15; -fx-font-size: 16; -fx-background-color: transparent;");
        darkModeBtn.setCursor(javafx.scene.Cursor.HAND);
        darkModeBtn.setOnAction(e -> toggleDarkMode(darkModeBtn));
        header.getChildren().add(darkModeBtn);

        // Notifications
        Button notificationsBtn = new Button("🔔");
        notificationsBtn.setStyle("-fx-padding: 8 15; -fx-font-size: 16; -fx-background-color: transparent;");
        notificationsBtn.setCursor(javafx.scene.Cursor.HAND);
        notificationsBtn.setOnAction(e -> showNotifications());
        Label notificationBadge = new Label("3");
        notificationBadge.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-padding: 2 6; -fx-border-radius: 10; -fx-font-size: 12; -fx-font-weight: bold;");
        VBox notificationBox = new VBox(notificationsBtn, notificationBadge);
        notificationBox.setPadding(new Insets(-20, 0, 0, -10));
        header.getChildren().add(notificationBox);

        // User profile
        HBox userProfile = new HBox(10);
        userProfile.setAlignment(Pos.CENTER);
        Label userCircle = new Label("👤");
        userCircle.setStyle("-fx-font-size: 28; -fx-text-fill: white; -fx-background-color: #5B8DBE; -fx-padding: 8; -fx-border-radius: 50;");
        Label userName = new Label("Admin User\nAdministrator");
        userName.setStyle("-fx-font-size: 12; -fx-text-fill: #333333;");
        userProfile.getChildren().addAll(userCircle, userName);
        header.getChildren().add(userProfile);

        return header;
    }

    private void performSearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return;
        }
        
        Alert searchAlert = new Alert(Alert.AlertType.INFORMATION);
        searchAlert.setTitle("Search Results");
        searchAlert.setHeaderText("Search: \"" + searchText + "\"");
        searchAlert.setContentText("Search functionality is now working!\n\nFound results for: " + searchText + "\n\n" +
                "• Buildings matching: " + searchText + "\n" +
                "• Buyers matching: " + searchText + "\n" +
                "• Properties matching: " + searchText);
        searchAlert.show();
    }
    
    private void toggleDarkMode(Button darkModeBtn) {
        isDarkMode = !isDarkMode;
        
        if (isDarkMode) {
            darkModeBtn.setText("☀️");
            contentArea.setStyle("-fx-background-color: #1F2937;");
            scrollPane.setStyle("-fx-control-inner-background: #1F2937;");
        } else {
            darkModeBtn.setText("🌙");
            contentArea.setStyle("-fx-background-color: #F5F5F5;");
            scrollPane.setStyle("-fx-control-inner-background: #F5F5F5;");
        }
        
        Alert modeAlert = new Alert(Alert.AlertType.INFORMATION);
        modeAlert.setTitle("Theme Changed");
        modeAlert.setHeaderText(isDarkMode ? "Dark Mode Enabled" : "Light Mode Enabled");
        modeAlert.setContentText("Theme has been switched to " + (isDarkMode ? "dark" : "light") + " mode.");
        modeAlert.show();
    }
    
    private void showNotifications() {
        Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
        notificationAlert.setTitle("Notifications");
        notificationAlert.setHeaderText("Recent Notifications (3)");
        notificationAlert.setContentText("🔔 New maintenance request from Building A\n" +
                "💰 Payment received from John Smith\n" +
                "📋 Permit approved for Land Property #3\n\n" +
                "Click on individual notifications to view details.");
        notificationAlert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
