package com.propertymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuyersPage extends VBox {
    
    private List<Buyer> buyers;
    private ObservableList<Buyer> filteredBuyers;
    private TableView<Buyer> buyersTable;
    private String selectedTab = "all";
    
    // Stats
    private Label totalCollectedLabel;
    private Label pendingLabel;
    private Label overdueLabel;
    
    public BuyersPage() {
        initData();
        initPage();
        updateStats();
    }
    
    private void initData() {
        buyers = new ArrayList<>();
        
        buyers.add(new Buyer(1, "John Smith", "Skyline Tower - 101", "+212 6 12 34 56 78", 
                           "john.smith@email.com", "2024-01-15", 2500000, 2500000, "paid", "2024-01-15", "N/A"));
        
        buyers.add(new Buyer(2, "Sarah Johnson", "Riverside - 205", "+212 6 23 45 67 89", 
                           "sarah.j@email.com", "2024-03-20", 3200000, 2800000, "partial", "2025-10-15", "2025-11-15"));
        
        buyers.add(new Buyer(3, "Mike Brown", "Garden View - 312", "+212 6 34 56 78 90", 
                           "mike.brown@email.com", "2024-06-10", 4500000, 3000000, "partial", "2025-09-20", "2025-11-20"));
        
        buyers.add(new Buyer(4, "Emma Davis", "Metro Heights - 405", "+212 6 45 67 89 01", 
                           "emma.davis@email.com", "2024-02-28", 2800000, 1800000, "overdue", "2025-08-10", "2025-10-10"));
        
        buyers.add(new Buyer(5, "David Wilson", "Skyline Tower - 508", "+212 6 56 78 90 12", 
                           "david.w@email.com", "2024-04-25", 3500000, 3500000, "paid", "2024-04-25", "N/A"));
        
        filteredBuyers = FXCollections.observableArrayList(buyers);
    }
    
    private void initPage() {
        setPadding(new Insets(30));
        setSpacing(20);
        setStyle("-fx-background-color: #f8f9fa;");
        
        // Header
        HBox header = createHeader();
        
        // Stats cards
        HBox statsCards = createStatsCards();
        
        // Buyers table
        VBox tableContainer = createTableContainer();
        
        getChildren().addAll(header, statsCards, tableContainer);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Buyer & Payment Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Manage property buyers and payment tracking");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.GRAY);
        
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Export menu
        MenuButton exportBtn = new MenuButton("📥 Export");
        exportBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #6c757d; -fx-border-radius: 8; -fx-padding: 8 16;");
        
        MenuItem pdfItem = new MenuItem("📄 Export as PDF");
        MenuItem excelItem = new MenuItem("📊 Export as Excel");
        MenuItem csvItem = new MenuItem("📋 Export as CSV");
        
        exportBtn.getItems().addAll(pdfItem, excelItem, csvItem);
        
        Button addBtn = new Button("+ Add Buyer");
        addBtn.setStyle("-fx-background-color: linear-gradient(135deg, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 20;");
        addBtn.setOnAction(e -> showAddBuyerDialog());
        
        header.getChildren().addAll(titleBox, spacer, exportBtn, addBtn);
        return header;
    }
    
    private HBox createStatsCards() {
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        
        // Total Collected
        VBox totalCard = createStatCard("Total Collected", "13,600,000 MAD", "💰", "#10b981");
        
        // Pending Payment
        VBox pendingCard = createStatCard("Pending Payment", "2,900,000 MAD", "📅", "#f59e0b");
        
        // Overdue
        VBox overdueCard = createStatCard("Overdue", "1,000,000 MAD", "⚠️", "#dc3545");
        
        statsBox.getChildren().addAll(totalCard, pendingCard, overdueCard);
        return statsBox;
    }
    
    private VBox createStatCard(String title, String value, String icon, String color) {
        VBox card = new VBox(15);
        card.setPrefWidth(350);
        card.setPrefHeight(120);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 12; -fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox textBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setTextFill(Color.GRAY);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        valueLabel.setTextFill(Color.web(color));
        
        textBox.getChildren().addAll(titleLabel, valueLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12; -fx-font-size: 20px;");
        
        header.getChildren().addAll(textBox, spacer, iconLabel);
        card.getChildren().add(header);
        
        return card;
    }
    
    private VBox createTableContainer() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 12; -fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        container.setPadding(new Insets(20));
        
        // Tabs
        HBox tabs = createTabs();
        
        // Table
        buyersTable = createBuyersTable();
        
        container.getChildren().addAll(tabs, buyersTable);
        return container;
    }
    
    private HBox createTabs() {
        HBox tabs = new HBox(10);
        
        Button allTab = createTabButton("All Buyers", "all");
        Button paidTab = createTabButton("Fully Paid", "paid");
        Button partialTab = createTabButton("Partial Payment", "partial");
        Button overdueTab = createTabButton("Overdue", "overdue");
        
        tabs.getChildren().addAll(allTab, paidTab, partialTab, overdueTab);
        return tabs;
    }
    
    private Button createTabButton(String text, String tabId) {
        Button tab = new Button(text);
        tab.setPrefHeight(35);
        tab.setPadding(new Insets(8, 16, 8, 16));
        
        if (tabId.equals(selectedTab)) {
            tab.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 6;");
        } else {
            tab.setStyle("-fx-background-color: transparent; -fx-text-fill: #6c757d; -fx-border-color: #e9ecef; -fx-border-radius: 6; -fx-background-radius: 6;");
        }
        
        tab.setOnAction(e -> {
            selectedTab = tabId;
            filterBuyers();
            updateTabStyles();
        });
        
        return tab;
    }
    
    private void updateTabStyles() {
        // This would update tab styles - simplified for this implementation
        filterBuyers();
    }
    
    private TableView<Buyer> createBuyersTable() {
        TableView<Buyer> table = new TableView<>();
        table.setPrefHeight(400);
        
        // Name column
        TableColumn<Buyer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);
        
        // Contact column
        TableColumn<Buyer, String> contactCol = new TableColumn<>("Contact");
        contactCol.setPrefWidth(200);
        contactCol.setCellFactory(col -> new TableCell<Buyer, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Buyer buyer = getTableRow().getItem();
                    VBox contact = new VBox(2);
                    Label phone = new Label("📞 " + buyer.phone);
                    phone.setFont(Font.font(10));
                    Label email = new Label("✉️ " + buyer.email);
                    email.setFont(Font.font(10));
                    contact.getChildren().addAll(phone, email);
                    setGraphic(contact);
                    setText(null);
                }
            }
        });
        
        // Property column
        TableColumn<Buyer, String> propertyCol = new TableColumn<>("Property");
        propertyCol.setCellValueFactory(new PropertyValueFactory<>("property"));
        propertyCol.setPrefWidth(150);
        
        // Purchase Date column
        TableColumn<Buyer, String> dateCol = new TableColumn<>("Purchase Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        dateCol.setPrefWidth(120);
        
        // Amount column
        TableColumn<Buyer, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("purchaseAmount"));
        amountCol.setPrefWidth(120);
        amountCol.setCellFactory(col -> new TableCell<Buyer, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    NumberFormat formatter = NumberFormat.getInstance(Locale.FRANCE);
                    setText(formatter.format(amount) + " MAD");
                }
            }
        });
        
        // Remaining column
        TableColumn<Buyer, Double> remainingCol = new TableColumn<>("Remaining");
        remainingCol.setCellValueFactory(new PropertyValueFactory<>("remainingAmount"));
        remainingCol.setPrefWidth(120);
        remainingCol.setCellFactory(col -> new TableCell<Buyer, Double>() {
            @Override
            protected void updateItem(Double remaining, boolean empty) {
                super.updateItem(remaining, empty);
                if (empty || remaining == null) {
                    setText(null);
                    setStyle("");
                } else {
                    NumberFormat formatter = NumberFormat.getInstance(Locale.FRANCE);
                    setText(formatter.format(remaining) + " MAD");
                    if (remaining > 0) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.GREEN);
                    }
                }
            }
        });
        
        // Status column
        TableColumn<Buyer, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        statusCol.setPrefWidth(120);
        statusCol.setCellFactory(col -> new TableCell<Buyer, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Label badge = new Label();
                    switch (status) {
                        case "paid":
                            badge.setText("Fully Paid");
                            badge.setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 4; -fx-padding: 2 8;");
                            break;
                        case "partial":
                            badge.setText("Partial Payment");
                            badge.setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404; -fx-background-radius: 4; -fx-padding: 2 8;");
                            break;
                        case "overdue":
                            badge.setText("Overdue");
                            badge.setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-background-radius: 4; -fx-padding: 2 8;");
                            break;
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });
        
        // Next Due column
        TableColumn<Buyer, String> nextDueCol = new TableColumn<>("Next Due");
        nextDueCol.setCellValueFactory(new PropertyValueFactory<>("nextDue"));
        nextDueCol.setPrefWidth(100);
        
        // Actions column
        TableColumn<Buyer, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<Buyer, Void>() {
            private final Button receiptBtn = new Button("📄");
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox buttons = new HBox(5, receiptBtn, editBtn, deleteBtn);
            
            {
                receiptBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #6c757d; -fx-border-radius: 4; -fx-padding: 4 8;");
                editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #6c757d; -fx-border-radius: 4; -fx-padding: 4 8;");
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #dc3545; -fx-text-fill: #dc3545; -fx-border-radius: 4; -fx-padding: 4 8;");
                
                receiptBtn.setTooltip(new Tooltip("Generate Receipt"));
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
        
        table.getColumns().addAll(nameCol, contactCol, propertyCol, dateCol, amountCol, remainingCol, statusCol, nextDueCol, actionsCol);
        table.setItems(filteredBuyers);
        
        return table;
    }
    
    private void filterBuyers() {
        filteredBuyers.clear();
        if ("all".equals(selectedTab)) {
            filteredBuyers.addAll(buyers);
        } else {
            for (Buyer buyer : buyers) {
                if (buyer.paymentStatus.equals(selectedTab)) {
                    filteredBuyers.add(buyer);
                }
            }
        }
    }
    
    private void updateStats() {
        double totalCollected = buyers.stream().mapToDouble(b -> b.paidAmount).sum();
        double pending = buyers.stream().filter(b -> "partial".equals(b.paymentStatus)).mapToDouble(b -> b.remainingAmount).sum();
        double overdue = buyers.stream().filter(b -> "overdue".equals(b.paymentStatus)).mapToDouble(b -> b.remainingAmount).sum();
        
        NumberFormat formatter = NumberFormat.getInstance(Locale.FRANCE);
    }
    
    private void showAddBuyerDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Register New Buyer");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label title = new Label("Register New Buyer");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        
        TextField nameField = new TextField();
        nameField.setPromptText("John Doe");
        
        TextField phoneField = new TextField();
        phoneField.setPromptText("+212 6 12 34 56 78");
        
        TextField emailField = new TextField();
        emailField.setPromptText("john.doe@email.com");
        
        ComboBox<String> propertyBox = new ComboBox<>();
        propertyBox.getItems().addAll("Skyline Tower - 101", "Riverside - 205", "Garden View - 312");
        propertyBox.setPromptText("Select property");
        
        DatePicker datePicker = new DatePicker();
        
        TextField amountField = new TextField();
        amountField.setPromptText("2500000");
        
        TextField paidField = new TextField();
        paidField.setPromptText("2500000");
        
        form.add(new Label("Full Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Phone Number:"), 0, 1);
        form.add(phoneField, 1, 1);
        form.add(new Label("Email Address:"), 0, 2);
        form.add(emailField, 1, 2);
        form.add(new Label("Property:"), 0, 3);
        form.add(propertyBox, 1, 3);
        form.add(new Label("Purchase Date:"), 0, 4);
        form.add(datePicker, 1, 4);
        form.add(new Label("Purchase Amount (MAD):"), 0, 5);
        form.add(amountField, 1, 5);
        form.add(new Label("Amount Paid (MAD):"), 0, 6);
        form.add(paidField, 1, 6);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button saveBtn = new Button("Register Buyer");
        saveBtn.setStyle("-fx-background-color: #2C3E8C; -fx-text-fill: white; -fx-background-radius: 4;");
        saveBtn.setOnAction(e -> {
            // Add buyer logic here
            dialog.close();
        });
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(title, form, buttons);
        
        Scene scene = new Scene(content, 600, 500);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}