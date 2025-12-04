package com.propertymanager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    private BorderPane root;
    private Sidebar sidebar;
    private TopBar topBar;
    private String userType = "Admin User";
    
    public MainApp() {}
    
    public MainApp(String userType) {
        this.userType = userType;
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart Property Manager Pro");
        
        // Create main container with window controls
        VBox mainContainer = new VBox();
        
        // Window controls bar (smaller)
        HBox windowControls = createWindowControls(primaryStage);
        
        root = new BorderPane();
        
        // Create components
        sidebar = new Sidebar(this);
        topBar = new TopBar(userType, this);
        
        // Layout
        root.setLeft(sidebar);
        root.setTop(topBar);
        
        // Add scroll to initial page
        ScrollPane initialScroll = new ScrollPane();
        initialScroll.setFitToWidth(true);
        initialScroll.setFitToHeight(true);
        initialScroll.setStyle("-fx-background-color: transparent;");
        
        VBox initialWrapper = new VBox();
        initialWrapper.setPadding(new Insets(0, 0, 50, 0));
        initialWrapper.getChildren().add(new DashboardPage());
        initialScroll.setContent(initialWrapper);
        
        root.setCenter(initialScroll);
        
        mainContainer.getChildren().addAll(windowControls, root);
        VBox.setVgrow(root, Priority.ALWAYS);
        
        Scene scene = new Scene(mainContainer, 1400, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private HBox createWindowControls(Stage stage) {
        HBox controls = new HBox();
        controls.setPrefHeight(25);
        controls.setStyle("-fx-background-color: #2C3E8C;");
        controls.setAlignment(Pos.CENTER_RIGHT);
        controls.setPadding(new Insets(3, 8, 3, 8));
        
        // Minimize button
        Button minimizeBtn = new Button("−");
        minimizeBtn.setPrefSize(30, 20);
        minimizeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        minimizeBtn.setOnMouseEntered(e -> minimizeBtn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;"));
        minimizeBtn.setOnMouseExited(e -> minimizeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;"));
        minimizeBtn.setOnAction(e -> stage.setIconified(true));
        
        // Maximize/Restore button
        Button maximizeBtn = new Button("□");
        maximizeBtn.setPrefSize(30, 20);
        maximizeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        maximizeBtn.setOnMouseEntered(e -> maximizeBtn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;"));
        maximizeBtn.setOnMouseExited(e -> maximizeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;"));
        maximizeBtn.setOnAction(e -> {
            if (stage.isMaximized()) {
                stage.setMaximized(false);
                maximizeBtn.setText("□");
            } else {
                stage.setMaximized(true);
                maximizeBtn.setText("❐");
            }
        });
        
        // Close button
        Button closeBtn = new Button("✕");
        closeBtn.setPrefSize(30, 20);
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;"));
        closeBtn.setOnAction(e -> stage.close());
        
        controls.getChildren().addAll(minimizeBtn, maximizeBtn, closeBtn);
        return controls;
    }
    
    public void showPage(String pageName) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        // Create wrapper with bottom padding
        VBox wrapper = new VBox();
        wrapper.setPadding(new Insets(0, 0, 50, 0)); // Add 50px bottom padding
        
        switch (pageName) {
            case "Dashboard":
                wrapper.getChildren().add(new DashboardPage());
                break;
            case "Buildings":
                wrapper.getChildren().add(new BuildingsPage());
                break;
            case "Buyers":
                wrapper.getChildren().add(new BuyersPage());
                break;
            case "Lands":
                wrapper.getChildren().add(new LandsPage());
                break;
            case "Permits":
                wrapper.getChildren().add(new PermitsPage());
                break;
            case "Maintenance":
                wrapper.getChildren().add(new MaintenancePage());
                break;
            case "Reports":
                wrapper.getChildren().add(new ReportsPage());
                break;
            case "Requests":
                wrapper.getChildren().add(new RequestsPage());
                break;
            case "Transportation":
                wrapper.getChildren().add(new TransportationPage());
                break;
            case "Settings":
                wrapper.getChildren().add(new SettingsPage());
                break;
            case "AdminProfile":
                wrapper.getChildren().add(new AdminProfilePage());
                break;
            case "UserProfile":
                wrapper.getChildren().add(new UserProfilePage());
                break;
        }
        
        scrollPane.setContent(wrapper);
        root.setCenter(scrollPane);
    }
    
    public void startWithUserType(Stage stage, String userType) throws Exception {
        this.userType = userType;
        start(stage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}