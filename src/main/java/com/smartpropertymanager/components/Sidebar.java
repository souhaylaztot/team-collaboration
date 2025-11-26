package com.smartpropertymanager.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.util.function.Consumer;

public class Sidebar {
    private VBox root;
    private Consumer<String> onMenuItemClick;
    private HBox activeMenuItem;

    public Sidebar() {
        root = new VBox();
        root.setPrefWidth(250);
        root.setStyle("-fx-background-color: white; -fx-border-color: #EEEEEE; -fx-border-width: 0 1 0 0;");
        root.setSpacing(0);

        // Logo
        HBox logo = createLogoSection();
        root.getChildren().add(logo);

        // Menu items - Store references to enable active state switching
        HBox dashboardItem = createMenuItem("🏠", "Dashboard", true);
        HBox buildingsItem = createMenuItem("🏢", "Buildings", false);
        HBox buyersItem = createMenuItemWithBadge("👥", "Buyers", false, "3");
        HBox landsItem = createMenuItem("🏞️", "Lands", false);
        HBox permitsItem = createMenuItemWithBadge("📋", "Permits", false, "2");
        HBox maintenanceItem = createMenuItemWithBadge("🔧", "Maintenance", false, "5");
        HBox reportsItem = createMenuItem("📊", "Reports", false);
        HBox requestsItem = createMenuItemWithBadge("💬", "Requests", false, "4");
        HBox settingsItem = createMenuItem("⚙️", "Settings", false);
        HBox logoutItem = createMenuItem("🚪", "Logout", false);

        root.getChildren().addAll(
                dashboardItem, buildingsItem, buyersItem, landsItem, permitsItem,
                maintenanceItem, reportsItem, requestsItem, settingsItem, logoutItem
        );

        // Set Dashboard as active by default
        activeMenuItem = dashboardItem;
    }

    private HBox createLogoSection() {
        HBox logoBox = new HBox(10);
        logoBox.setPadding(new Insets(20));
        logoBox.setStyle("-fx-border-color: #EEEEEE; -fx-border-width: 0 0 1 0;");
        logoBox.setAlignment(Pos.CENTER_LEFT);

        Label logoIcon = new Label("🏢");
        logoIcon.setStyle("-fx-font-size: 28; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8; -fx-border-radius: 8;");

        VBox logoText = new VBox(2);
        Label title = new Label("Smart Property");
        title.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #333333;");
        Label subtitle = new Label("Manager Pro");
        subtitle.setStyle("-fx-font-size: 12; -fx-text-fill: #999999;");
        logoText.getChildren().addAll(title, subtitle);

        logoBox.getChildren().addAll(logoIcon, logoText);
        return logoBox;
    }

    private HBox createMenuItem(String icon, String text, boolean active) {
        HBox item = new HBox(15);
        item.setPadding(new Insets(15, 20, 15, 20));
        item.setAlignment(Pos.CENTER_LEFT);
        item.setCursor(javafx.scene.Cursor.HAND);
        item.setStyle(active ? "-fx-background-color: #F0F0F0;" : "-fx-background-color: white;");
        item.setUserData(text);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #333333;");

        item.getChildren().addAll(iconLabel, textLabel);

        item.setOnMouseClicked(e -> handleMenuClick(item));
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #FAFAFA;"));
        item.setOnMouseExited(e -> updateItemStyle(item));

        return item;
    }

    private HBox createMenuItemWithBadge(String icon, String text, boolean active, String badge) {
        HBox item = new HBox(15);
        item.setPadding(new Insets(15, 20, 15, 20));
        item.setAlignment(Pos.CENTER_LEFT);
        item.setCursor(javafx.scene.Cursor.HAND);
        item.setStyle(active ? "-fx-background-color: #F0F0F0;" : "-fx-background-color: white;");
        item.setUserData(text);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #333333;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Label badgeLabel = new Label(badge);
        badgeLabel.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-padding: 3 8; -fx-border-radius: 10; -fx-font-size: 11; -fx-font-weight: bold;");

        item.getChildren().addAll(iconLabel, textLabel, spacer, badgeLabel);

        item.setOnMouseClicked(e -> handleMenuClick(item));
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #FAFAFA;"));
        item.setOnMouseExited(e -> updateItemStyle(item));

        return item;
    }

    private void handleMenuClick(HBox item) {
        if (onMenuItemClick != null) {
            String pageName = (String) item.getUserData();
            onMenuItemClick.accept(pageName);

            // Update active state
            if (activeMenuItem != null) {
                updateItemStyle(activeMenuItem);
            }
            activeMenuItem = item;
            item.setStyle("-fx-background-color: #F0F0F0;");
        }
    }

    private void updateItemStyle(HBox item) {
        if (item == activeMenuItem) {
            item.setStyle("-fx-background-color: #F0F0F0;");
        } else {
            item.setStyle("-fx-background-color: white;");
        }
    }

    public void setOnMenuItemClick(Consumer<String> callback) {
        this.onMenuItemClick = callback;
    }

    public VBox getRoot() {
        return root;
    }
}
