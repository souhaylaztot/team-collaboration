package com.propertymanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BuyersPage extends VBox {
    private VBox content;
    private String title = "Buyer & Payment Management";
    
    private ObservableList<Buyer> buyers;
    private FilteredList<Buyer> filteredBuyers;
    private TabPane tabPane;
    private TableView<Buyer> tableView;
    
    // Payment statistics
    private long totalCollected = 13600000;
    private long pending = 2900000;
    private long overdue = 1000000;

    public BuyersPage() {
        content = new VBox();
        content.setStyle("-fx-background-color: #F8FAFC;");
        content.setPadding(new Insets(32));
        content.setSpacing(24);
        
        initializeData();
        createUI();
        
        // Add content to this VBox
        this.setStyle("-fx-background-color: #F8FAFC;");
        getChildren().add(content);
    }

    private void initializeData() {
        buyers = FXCollections.observableArrayList();
        loadBuyersFromDatabase();
        filteredBuyers = new FilteredList<>(buyers);
        filteredBuyers.setPredicate(buyer -> true); // Show all initially
    }
    
    private void loadBuyersFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT b.id, b.name, b.phone, b.email, b.purchase_date, " +
                          "b.purchase_amount, b.paid_amount, b.remaining_amount, b.payment_status, " +
                          "b.last_payment_date, b.next_due_date, " +
                          "CONCAT(bd.name, ' - ', a.apartment_number) as property " +
                          "FROM buyers b " +
                          "LEFT JOIN property_purchases pp ON b.id = pp.buyer_id " +
                          "LEFT JOIN apartments a ON pp.apartment_id = a.id " +
                          "LEFT JOIN buildings bd ON a.building_id = bd.id " +
                          "ORDER BY b.name";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String lastPayment = rs.getDate("last_payment_date") != null ? 
                                       rs.getDate("last_payment_date").toString() : "N/A";
                    String nextDue = rs.getDate("next_due_date") != null ? 
                                   rs.getDate("next_due_date").toString() : "N/A";
                    
                    Buyer buyer = new Buyer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("property") != null ? rs.getString("property") : "No Property",
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getDate("purchase_date").toString(),
                        (int) rs.getLong("purchase_amount"),
                        (int) rs.getLong("paid_amount"),
                        (int) rs.getLong("remaining_amount"),
                        rs.getString("payment_status"),
                        lastPayment,
                        nextDue
                    );
                    buyers.add(buyer);
                }
            }
            
            // Update payment statistics
            updatePaymentStatistics(conn);
            
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Failed to load buyers from database: " + e.getMessage());
        }
    }
    
    private void updatePaymentStatistics(Connection conn) throws SQLException {
        String statsQuery = "SELECT " +
                           "SUM(paid_amount) as total_collected, " +
                           "SUM(CASE WHEN payment_status = 'partial' OR payment_status = 'pending' THEN remaining_amount ELSE 0 END) as pending, " +
                           "SUM(CASE WHEN payment_status = 'overdue' THEN remaining_amount ELSE 0 END) as overdue " +
                           "FROM buyers";
        
        try (PreparedStatement stmt = conn.prepareStatement(statsQuery)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalCollected = rs.getLong("total_collected");
                pending = rs.getLong("pending");
                overdue = rs.getLong("overdue");
            }
        }
    }

    private void createUI() {
        // Header
        HBox header = createHeader();
        content.getChildren().add(header);

        // Payment Stats
        HBox statsGrid = createPaymentStats();
        content.getChildren().add(statsGrid);

        // Buyers Table with Tabs
        VBox buyersSection = createBuyersSection();
        content.getChildren().add(buyersSection);
        VBox.setVgrow(buyersSection, Priority.ALWAYS);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        VBox titleSection = new VBox(8);
        Label titleLabel = new Label("Buyer & Payment Management");
        titleLabel.getStyleClass().add("page-title");

        Label subtitleLabel = new Label("Manage property buyers and payment tracking");
        subtitleLabel.getStyleClass().add("page-subtitle");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportBtn = new Button("Export");
        exportBtn.getStyleClass().add("secondary-button");
        exportBtn.setOnAction(e -> showExportDialog());

        Button addBuyerBtn = new Button("+ Add Buyer");
        addBuyerBtn.getStyleClass().add("primary-button");
        addBuyerBtn.setOnAction(e -> showAddBuyerDialog());

        HBox buttonContainer = new HBox(12, exportBtn, addBuyerBtn);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(titleSection, spacer, buttonContainer);
        return header;
    }

    private HBox createPaymentStats() {
        HBox statsGrid = new HBox(24);
        statsGrid.setAlignment(Pos.CENTER_LEFT);

        // Total Collected Card
        VBox totalCard = createStatCard("Total Collected", 
                                       String.format("%,d MAD", totalCollected), 
                                       "💰", "#10B981", "#059669");
        
        // Pending Payment Card
        VBox pendingCard = createStatCard("Pending Payment", 
                                         String.format("%,d MAD", pending), 
                                         "📅", "#F59E0B", "#D97706");
        
        // Overdue Card
        VBox overdueCard = createStatCard("Overdue", 
                                         String.format("%,d MAD", overdue), 
                                         "⚠️", "#EF4444", "#DC2626");

        statsGrid.getChildren().addAll(totalCard, pendingCard, overdueCard);
        return statsGrid;
    }

    private VBox createStatCard(String title, String value, String icon, String startColor, String endColor) {
        VBox card = new VBox();
        card.setPrefSize(280, 120);
        card.getStyleClass().add("stat-card");

        HBox boxContent = new HBox();
        boxContent.setAlignment(Pos.CENTER_LEFT);
        boxContent.setSpacing(16);

        VBox textSection = new VBox(8);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-label");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-number");

        textSection.getChildren().addAll(titleLabel, valueLabel);

        // Icon with gradient background
        StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(48, 48);
        iconContainer.setStyle(String.format("-fx-background-color: linear-gradient(to bottom right, %s, %s); -fx-background-radius: 12;", 
                                           startColor, endColor));
        iconContainer.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("icon-text");

        iconContainer.getChildren().add(iconLabel);

        Region spacerRegion = new Region();
        HBox.setHgrow(spacerRegion, Priority.ALWAYS);

        boxContent.getChildren().addAll(textSection, spacerRegion, iconContainer);
        card.getChildren().add(boxContent);

        return card;
    }

    private VBox createBuyersSection() {
        VBox section = new VBox();
        section.getStyleClass().add("content-card");
        section.setPadding(new Insets(20));
        section.setSpacing(16);

        // Search bar for buyers
        HBox searchBar = new HBox(12);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(0, 0, 16, 0));
        
        TextField buyerSearchField = new TextField();
        buyerSearchField.setPromptText("Search buyers by name, property, or email...");
        buyerSearchField.getStyleClass().add("search-field");
        buyerSearchField.setPrefWidth(400);
        
        buyerSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterBuyersBySearch(newValue);
        });
        
        searchBar.getChildren().add(buyerSearchField);
        
        // Tabs
        tabPane = new TabPane();
        tabPane.getStyleClass().add("tab-pane");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab allTab = new Tab("All Buyers");
        allTab.setClosable(false);
        Tab paidTab = new Tab("Fully Paid");
        paidTab.setClosable(false);
        Tab partialTab = new Tab("Partial Payment");
        partialTab.setClosable(false);
        Tab overdueTab = new Tab("Overdue");
        overdueTab.setClosable(false);
        
        tabPane.getTabs().addAll(allTab, paidTab, partialTab, overdueTab);
        
        // Add listener for tab changes
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            filterBuyersByTab(newTab.getText());
        });

        // Table
        tableView = createBuyersTable();
        
        section.getChildren().addAll(searchBar, tabPane, tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        
        return section;
    }

    private TableView<Buyer> createBuyersTable() {
        TableView<Buyer> table = new TableView<>();
        table.setItems(filteredBuyers);
        table.getStyleClass().add("table-view");
        
        // Name Column
        TableColumn<Buyer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameCol.setPrefWidth(130);
        
        // Phone Column
        TableColumn<Buyer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        phoneCol.setPrefWidth(130);
        
        // Email Column
        TableColumn<Buyer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        emailCol.setPrefWidth(150);
        
        // Property Column
        TableColumn<Buyer, String> propertyCol = new TableColumn<>("Property");
        propertyCol.setCellValueFactory(cellData -> cellData.getValue().propertyProperty());
        propertyCol.setPrefWidth(140);
        
        // Purchase Date Column
        TableColumn<Buyer, String> purchaseDateCol = new TableColumn<>("Purchase Date");
        purchaseDateCol.setCellValueFactory(cellData -> cellData.getValue().purchaseDateProperty());
        purchaseDateCol.setPrefWidth(110);
        
        // Amount Column
        TableColumn<Buyer, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> cellData.getValue().purchaseAmountFormattedProperty());
        amountCol.setPrefWidth(120);
        
        // Remaining Column
        TableColumn<Buyer, String> remainingCol = new TableColumn<>("Remaining");
        remainingCol.setCellValueFactory(cellData -> cellData.getValue().remainingAmountFormattedProperty());
        remainingCol.setPrefWidth(120);
        
        // Status Column
        TableColumn<Buyer, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().paymentStatusProperty());
        statusCol.setCellFactory(column -> new TableCell<Buyer, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label();
                    badge.getStyleClass().clear();
                    switch (status) {
                        case "paid":
                            badge.setText("Fully Paid");
                            badge.getStyleClass().add("status-badge-paid");
                            break;
                        case "partial":
                            badge.setText("Partial Payment");
                            badge.getStyleClass().add("status-badge-partial");
                            break;
                        case "overdue":
                            badge.setText("Overdue");
                            badge.getStyleClass().add("status-badge-overdue");
                            break;
                    }
                    setGraphic(badge);
                }
            }
        });
        statusCol.setPrefWidth(110);
        
        // Next Due Column
        TableColumn<Buyer, String> nextDueCol = new TableColumn<>("Next Due");
        nextDueCol.setCellValueFactory(cellData -> cellData.getValue().nextDueProperty());
        nextDueCol.setPrefWidth(100);
        
        // Actions Column
        TableColumn<Buyer, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(param -> new TableCell<Buyer, Void>() {
            private final Button receiptBtn = new Button("📄");
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox buttons = new HBox(8);
            
            {
                receiptBtn.getStyleClass().add("action-button");
                editBtn.getStyleClass().add("action-button");
                deleteBtn.getStyleClass().add("action-button-danger");
                
                receiptBtn.setOnAction(e -> {
                    Buyer buyer = getTableView().getItems().get(getIndex());
                    generateReceipt(buyer);
                });
                editBtn.setOnAction(e -> {
                    Buyer buyer = getTableView().getItems().get(getIndex());
                    editBuyer(buyer);
                });
                deleteBtn.setOnAction(e -> {
                    Buyer buyer = getTableView().getItems().get(getIndex());
                    deleteBuyer(buyer);
                });
                
                buttons.getChildren().addAll(receiptBtn, editBtn, deleteBtn);
                buttons.setAlignment(Pos.CENTER);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
        actionsCol.setPrefWidth(110);
        
        table.getColumns().addAll(nameCol, phoneCol, emailCol, propertyCol, purchaseDateCol, 
                                 amountCol, remainingCol, statusCol, nextDueCol, actionsCol);
        
        return table;
    }

    private void filterBuyersByTab(String tabText) {
        switch (tabText) {
            case "All Buyers":
                filteredBuyers.setPredicate(buyer -> true);
                break;
            case "Fully Paid":
                filteredBuyers.setPredicate(buyer -> "paid".equals(buyer.getPaymentStatus()));
                break;
            case "Partial Payment":
                filteredBuyers.setPredicate(buyer -> "partial".equals(buyer.getPaymentStatus()));
                break;
            case "Overdue":
                filteredBuyers.setPredicate(buyer -> "overdue".equals(buyer.getPaymentStatus()));
                break;
        }
    }
    
    private void filterBuyersBySearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            // Reset to current tab filter
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                filterBuyersByTab(selectedTab.getText());
            }
            return;
        }
        
        String lowerSearchText = searchText.toLowerCase();
        filteredBuyers.setPredicate(buyer -> 
            buyer.getName().toLowerCase().contains(lowerSearchText) ||
            buyer.getProperty().toLowerCase().contains(lowerSearchText) ||
            buyer.getEmail().toLowerCase().contains(lowerSearchText) ||
            buyer.getPhone().toLowerCase().contains(lowerSearchText)
        );
    }

    private void showExportDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Buyers");
        alert.setHeaderText("Export Buyer Data");
        alert.setContentText("Export formats available:\n\n" +
                "• PDF - Professional formatted document\n" +
                "• Excel (XLSX) - Spreadsheet with formulas\n" +
                "• CSV - Comma-separated values\n\n" +
                "Export functionality would be implemented here.");
        alert.showAndWait();
    }

    private void showAddBuyerDialog() {
        Dialog<Buyer> dialog = new Dialog<>();
        dialog.setTitle("Register New Buyer");
        dialog.setHeaderText("Enter buyer details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("John Doe");
        TextField phoneField = new TextField();
        phoneField.setPromptText("+212 6 12 34 56 78");
        TextField emailField = new TextField();
        emailField.setPromptText("john.doe@email.com");
        TextField propertyField = new TextField();
        propertyField.setPromptText("Skyline Tower - 101");
        TextField purchaseDateField = new TextField();
        purchaseDateField.setPromptText("2024-01-15");
        TextField purchaseAmountField = new TextField();
        purchaseAmountField.setPromptText("2500000");

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Property:"), 0, 3);
        grid.add(propertyField, 1, 3);
        grid.add(new Label("Purchase Date:"), 0, 4);
        grid.add(purchaseDateField, 1, 4);
        grid.add(new Label("Purchase Amount:"), 0, 5);
        grid.add(purchaseAmountField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                try {
                    int amount = Integer.parseInt(purchaseAmountField.getText());
                    return new Buyer(buyers.size() + 1, nameField.getText(), propertyField.getText(), 
                                   phoneField.getText(), emailField.getText(), purchaseDateField.getText(),
                                   amount, 0, amount, "partial", 
                                   purchaseDateField.getText(), "N/A");
                } catch (NumberFormatException e) {
                    showError("Invalid amount", "Purchase amount must be a number");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(buyer -> {
            if (buyer != null) {
                buyers.add(buyer);
                tableView.refresh();
            }
        });
    }

    private void generateReceipt(Buyer buyer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Receipt Generated");
        alert.setHeaderText("Payment Receipt");
        alert.setContentText("Receipt Details:\n\n" +
                "Buyer: " + buyer.getName() + "\n" +
                "Property: " + buyer.getProperty() + "\n" +
                "Amount: " + String.format("%,d MAD", buyer.getPurchaseAmount()) + "\n" +
                "Paid: " + String.format("%,d MAD", buyer.getPaidAmount()) + "\n" +
                "Remaining: " + String.format("%,d MAD", buyer.getRemainingAmount()) + "\n\n" +
                "Receipt has been generated successfully.");
        alert.showAndWait();
    }

    private void editBuyer(Buyer buyer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Buyer");
        alert.setHeaderText("Edit Buyer: " + buyer.getName());
        alert.setContentText("Current Details:\n\n" +
                "Name: " + buyer.getName() + "\n" +
                "Phone: " + buyer.getPhone() + "\n" +
                "Email: " + buyer.getEmail() + "\n" +
                "Property: " + buyer.getProperty() + "\n" +
                "Purchase Amount: " + String.format("%,d MAD", buyer.getPurchaseAmount()) + "\n" +
                "Payment Status: " + buyer.getPaymentStatus() + "\n\n" +
                "Edit dialog would open here with form fields.");
        alert.showAndWait();
    }

    private void deleteBuyer(Buyer buyer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Buyer");
        alert.setHeaderText("Delete " + buyer.getName() + "?");
        alert.setContentText("Are you sure you want to delete this buyer record?\n" +
                "Property: " + buyer.getProperty() + "\n\n" +
                "This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                buyers.remove(buyer);
                tableView.refresh();
            }
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }
}