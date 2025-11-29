package com.smartpropertymanager.pages;

import com.smartpropertymanager.models.Buyer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.Arrays;
import java.util.List;

public class BuyerManagementPage implements Page {
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

    public BuyerManagementPage() {
        content = new VBox();
        content.setStyle("-fx-background-color: #F8FAFC;");
        content.setPadding(new Insets(32));
        content.setSpacing(24);
        
        initializeData();
        createUI();
    }

    private void initializeData() {
        // Sample buyer data
        List<Buyer> buyerList = Arrays.asList(
            new Buyer(1, "John Smith", "Skyline Tower - 101", "+212 6 12 34 56 78", 
                     "john.smith@email.com", "2024-01-15", 2500000, 2500000, 0, "paid", "2024-01-15", "N/A"),
            new Buyer(2, "Sarah Johnson", "Riverside - 205", "+212 6 23 45 67 89", 
                     "sarah.j@email.com", "2024-03-20", 3200000, 2800000, 400000, "partial", "2025-10-15", "2025-11-15"),
            new Buyer(3, "Mike Brown", "Garden View - 312", "+212 6 34 56 78 90", 
                     "mike.brown@email.com", "2024-06-10", 4500000, 3000000, 1500000, "partial", "2025-09-20", "2025-11-20"),
            new Buyer(4, "Emma Davis", "Metro Heights - 405", "+212 6 45 67 89 01", 
                     "emma.davis@email.com", "2024-02-28", 2800000, 1800000, 1000000, "overdue", "2025-08-10", "2025-10-10"),
            new Buyer(5, "David Wilson", "Skyline Tower - 508", "+212 6 56 78 90 12", 
                     "david.w@email.com", "2024-04-25", 3500000, 3500000, 0, "paid", "2024-04-25", "N/A")
        );
        
        buyers = FXCollections.observableArrayList(buyerList);
        filteredBuyers = new FilteredList<>(buyers);
        filteredBuyers.setPredicate(buyer -> true); // Show all initially
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
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        Label subtitleLabel = new Label("Manage property buyers and payment tracking");
        subtitleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportBtn = new Button("Export");
        exportBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-text-fill: #000000; -fx-padding: 12 16; -fx-font-size: 14;");
        exportBtn.setOnAction(e -> showExportDialog());

        Button addBuyerBtn = new Button("+ Add Buyer");
        addBuyerBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-size: 14;");
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
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        HBox boxContent = new HBox();
        boxContent.setAlignment(Pos.CENTER_LEFT);
        boxContent.setSpacing(16);

        VBox textSection = new VBox(8);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 20; -fx-font-weight: bold;");

        textSection.getChildren().addAll(titleLabel, valueLabel);

        // Icon with gradient background
        StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(48, 48);
        iconContainer.setStyle(String.format("-fx-background-color: linear-gradient(to bottom right, %s, %s); -fx-background-radius: 12;", 
                                           startColor, endColor));
        iconContainer.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18;");

        iconContainer.getChildren().add(iconLabel);

        Region spacerRegion = new Region();
        HBox.setHgrow(spacerRegion, Priority.ALWAYS);

        boxContent.getChildren().addAll(textSection, spacerRegion, iconContainer);
        card.getChildren().add(boxContent);

        return card;
    }

    private VBox createBuyersSection() {
        VBox section = new VBox();
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        section.setPadding(new Insets(20));
        section.setSpacing(16);

        // Search bar for buyers
        HBox searchBar = new HBox(12);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(0, 0, 16, 0));
        
        TextField buyerSearchField = new TextField();
        buyerSearchField.setPromptText("Search buyers by name, property, or email...");
        buyerSearchField.setStyle("-fx-padding: 8; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #D1D5DB;");
        buyerSearchField.setPrefWidth(400);
        
        buyerSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterBuyersBySearch(newValue);
        });
        
        searchBar.getChildren().add(buyerSearchField);
        
        // Tabs
        tabPane = new TabPane();
        tabPane.setStyle("-fx-font-size: 13;");
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
        table.setStyle("-fx-font-size: 13;");
        
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
                    switch (status) {
                        case "paid":
                            badge.setText("Fully Paid");
                            badge.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;");
                            break;
                        case "partial":
                            badge.setText("Partial Payment");
                            badge.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;");
                            break;
                        case "overdue":
                            badge.setText("Overdue");
                            badge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;");
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
                receiptBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 4; -fx-min-width: 32; -fx-min-height: 32; -fx-font-size: 14;");
                editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 4; -fx-min-width: 32; -fx-min-height: 32; -fx-font-size: 14;");
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-border-radius: 4; -fx-text-fill: #DC2626; -fx-min-width: 32; -fx-min-height: 32; -fx-font-size: 14;");
                
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

    @Override
    public VBox getContent() {
        return content;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
