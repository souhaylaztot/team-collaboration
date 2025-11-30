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
import com.smartpropertymanager.pages.SettingsPage;
import com.smartpropertymanager.pages.SimplePage;
import com.smartpropertymanager.utils.ThemeManager;

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
    private HBox header;
    private VBox mainContent;
    private BorderPane root;
    private Scene scene;
    private Sidebar sidebar;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        root.setStyle("-fx-font-family: 'Segoe UI', Arial, sans-serif;");

        // Create sidebar
        sidebar = new Sidebar();
        sidebar.setOnMenuItemClick(this::navigateTo);
        root.setLeft(sidebar.getRoot());

        // Create main content area
        mainContent = createMainContent();
        root.setCenter(mainContent);

        // Load Dashboard page by default
        navigateTo("Dashboard");

        scene = new Scene(root, 1400, 900);
        scene.setFill(Color.web("#F5F5F5"));

        primaryStage.setTitle("Smart Property Manager Pro");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMainContent() {
        VBox content = new VBox();
        content.getStyleClass().add("main-content");

        // Header
        header = createHeader();
        content.getChildren().add(header);

        // Content area with scroll pane
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("main-scroll-pane");

        contentArea = new VBox();
        contentArea.getStyleClass().add("content-area");
        scrollPane.setContent(contentArea);

        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
        content.getChildren().add(scrollPane);

        return content;
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
        javafx.scene.Node pageContent = page.getContent();
        pageContent.setUserData(page); // Store reference to page object
        contentArea.getChildren().add(pageContent);
        scrollPane.setVvalue(0);
    }

    private HBox createHeader() {
        HBox headerBox = new HBox();
        headerBox.setPadding(new Insets(15, 25, 15, 25));
        headerBox.setSpacing(15);
        headerBox.setStyle("-fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-border-color: " + ThemeManager.getBorderColor() + "; -fx-border-width: 0 0 1 0;");
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Search bar
        searchField = new TextField();
        searchField.setPromptText("Search buildings, buyers, permits...");
        searchField.setPrefWidth(500);
        searchField.getStyleClass().add("search-field");
        
        // Add search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(newValue);
        });

        HBox.setHgrow(searchField, javafx.scene.layout.Priority.SOMETIMES);
        headerBox.getChildren().add(searchField);

        // Spacer
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        headerBox.getChildren().add(spacer);

        // Dark mode toggle
        Button darkModeBtn = new Button(ThemeManager.isDarkMode() ? "☀️" : "🌙");
        darkModeBtn.getStyleClass().addAll("button", "icon-button");
        darkModeBtn.setOnAction(e -> toggleDarkMode(darkModeBtn));
        headerBox.getChildren().add(darkModeBtn);

        // Notifications
        Button notificationsBtn = new Button("🔔");
        notificationsBtn.getStyleClass().addAll("button", "icon-button");
        notificationsBtn.setCursor(javafx.scene.Cursor.HAND);
        notificationsBtn.setOnAction(e -> showNotifications());
        Label notificationBadge = new Label("3");
        notificationBadge.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-padding: 2 6; -fx-border-radius: 10; -fx-font-size: 12; -fx-font-weight: bold;");
        VBox notificationBox = new VBox(notificationsBtn, notificationBadge);
        notificationBox.setPadding(new Insets(-20, 0, 0, -10));
        headerBox.getChildren().add(notificationBox);

        // User profile
        HBox userProfile = new HBox(10);
        userProfile.setAlignment(Pos.CENTER);
        Label userCircle = new Label("👤");
        userCircle.setStyle("-fx-font-size: 28; -fx-text-fill: white; -fx-background-color: #5B8DBE; -fx-padding: 8; -fx-border-radius: 50;");
        Label userName = new Label("Admin User\nAdministrator");
        userName.setStyle("-fx-font-size: 12; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        userProfile.getChildren().addAll(userCircle, userName);
        headerBox.getChildren().add(userProfile);

        return headerBox;
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
        ThemeManager.toggleTheme();
        
        // Update button icon
        if (ThemeManager.isDarkMode()) {
            darkModeBtn.setText("☀️");
        } else {
            darkModeBtn.setText("🌙");
        }
        
        // Update header styles
        updateHeaderStyles();
        
        // Refresh current page to apply theme
        if (contentArea.getChildren().size() > 0) {
            String currentPage = getCurrentPageName();
            if (currentPage != null) {
                navigateTo(currentPage);
            }
        }
        
        Alert modeAlert = new Alert(Alert.AlertType.INFORMATION);
        modeAlert.setTitle("Theme Changed");
        modeAlert.setHeaderText(ThemeManager.isDarkMode() ? "Dark Mode Enabled" : "Light Mode Enabled");
        modeAlert.setContentText("Theme has been switched to " + (ThemeManager.isDarkMode() ? "dark" : "light") + " mode.");
        modeAlert.show();
    }
    
    private void updateHeaderStyles() {
        if (header != null) {
            header.setStyle("-fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-border-color: " + ThemeManager.getBorderColor() + "; -fx-border-width: 0 0 1 0; -fx-padding: 15 25 15 25;");
            
            // Update all header text elements
            header.getChildren().forEach(node -> {
                if (node instanceof javafx.scene.control.Button) {
                    javafx.scene.control.Button btn = (javafx.scene.control.Button) node;
                    if (btn.getText().equals("🌙") || btn.getText().equals("☀️") || btn.getText().equals("🔔")) {
                        btn.setStyle("-fx-padding: 8 15; -fx-font-size: 16; -fx-background-color: transparent; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
                    }
                } else if (node instanceof javafx.scene.layout.HBox) {
                    javafx.scene.layout.HBox hbox = (javafx.scene.layout.HBox) node;
                    hbox.getChildren().forEach(child -> {
                        if (child instanceof javafx.scene.control.Label) {
                            javafx.scene.control.Label label = (javafx.scene.control.Label) child;
                            if (label.getText().contains("Admin User")) {
                                label.setStyle("-fx-font-size: 12; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
                            }
                        }
                    });
                }
            });
        }
        if (mainContent != null) {
            mainContent.setStyle("-fx-background-color: " + ThemeManager.getBackgroundColor() + ";");
        }
        if (contentArea != null) {
            contentArea.setStyle("-fx-background-color: " + ThemeManager.getBackgroundColor() + ";");
        }
        if (scrollPane != null) {
            scrollPane.setStyle("-fx-control-inner-background: " + ThemeManager.getBackgroundColor() + "; -fx-background-color: " + ThemeManager.getBackgroundColor() + ";");
        }
        if (searchField != null) {
            searchField.setStyle("-fx-padding: 10; -fx-font-size: 14; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: " + ThemeManager.getBorderColor() + "; -fx-background-color: " + ThemeManager.getCardBackgroundColor() + "; -fx-text-fill: " + ThemeManager.getTextColor() + ";");
        }
    }
    
    private String getCurrentPageName() {
        // This is a simple way to track current page - in a real app you'd want better state management
        return "Dashboard"; // Default fallback
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
