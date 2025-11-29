
package com.smartpropertymanager.pages;

import java.util.List;

import com.smartpropertymanager.models.Message;
import com.smartpropertymanager.models.Request;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RequestCenterPage implements Page {
    private VBox content;
    private String title = "Request Center";
    private ObservableList<Request> requests;
    private FilteredList<Request> filteredRequests;
    private TabPane tabPane;
    private Request selectedRequest;

    public RequestCenterPage() {
        content = new VBox();
        content.setStyle("-fx-background-color: #F8FAFC;");
        content.setPadding(new Insets(32));
        content.setSpacing(24);
        initializeData();
        createUI();
    }

    private void initializeData() {
        requests = FXCollections.observableArrayList(
            new Request("REQ-2025-120", "John Smith", "Skyline Tower", "Apt 305", "Information", "Lease Renewal Process", "open", "2025-11-05 09:30", "medium",
                FXCollections.observableArrayList(new Message(1, "John Smith", "Hi, I would like to know the process for renewing my lease which expires in January.", "2025-11-05 09:30", false))),
            new Request("REQ-2025-121", "Sarah Johnson", "Riverside Apartments", "Apt 205", "Document", "Request for Proof of Residence", "responded", "2025-11-04 14:20", "low",
                FXCollections.observableArrayList(
                    new Message(1, "Sarah Johnson", "I need a proof of residence document for my bank application. Can you provide this?", "2025-11-04 14:20", false),
                    new Message(2, "Admin", "Hello Sarah, we can provide that for you. The document will be ready by tomorrow. You can pick it up from the office.", "2025-11-04 16:45", true)
                )),
            new Request("REQ-2025-122", "Mike Brown", "Garden View Complex", "Apt 312", "Complaint", "Noise Complaint - Neighboring Apartment", "in-progress", "2025-11-03 22:15", "high",
                FXCollections.observableArrayList(
                    new Message(1, "Mike Brown", "There has been excessive noise from the apartment above mine (Apt 412) late at night for the past week.", "2025-11-03 22:15", false),
                    new Message(2, "Admin", "Thank you for reporting this. We will contact the tenant in Apt 412 and address this issue.", "2025-11-04 09:00", true),
                    new Message(3, "Mike Brown", "Thank you. Please keep me updated.", "2025-11-04 09:30", false)
                )),
            new Request("REQ-2025-123", "Emma Davis", "Metro Heights", "Apt 405", "Maintenance", "Request for Additional Parking Space", "closed", "2025-10-28 11:00", "low",
                FXCollections.observableArrayList(
                    new Message(1, "Emma Davis", "I recently purchased a second vehicle and need an additional parking space. Is one available?", "2025-10-28 11:00", false),
                    new Message(2, "Admin", "We have checked availability and space P-42 is available for 500 MAD/month. Would you like to proceed?", "2025-10-28 15:30", true),
                    new Message(3, "Emma Davis", "Yes, please. How do I complete the registration?", "2025-10-29 08:20", false),
                    new Message(4, "Admin", "Perfect! Space P-42 has been assigned to you. The updated agreement has been sent to your email.", "2025-10-29 10:00", true)
                ))
        );
        filteredRequests = new FilteredList<>(requests);
        filteredRequests.setPredicate(request -> true);
    }

    private void createUI() {
        HBox header = createHeader();
        content.getChildren().add(header);
        HBox statsCards = createStatsCards();
        content.getChildren().add(statsCards);
        HBox requestsView = createRequestsView();
        content.getChildren().add(requestsView);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);
        VBox titleSection = new VBox(8);
        Label titleLabel = new Label("Request Center");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #000000;");
        Label subtitleLabel = new Label("Manage buyer and staff requests");
        subtitleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");
        titleSection.getChildren().addAll(titleLabel, subtitleLabel);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button newRequestBtn = new Button("New Request");
        newRequestBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        newRequestBtn.setOnAction(e -> showNewRequestDialog());
        header.getChildren().addAll(titleSection, spacer, newRequestBtn);
        return header;
    }

    private HBox createStatsCards() {
        HBox statsCards = new HBox(24);
        statsCards.setAlignment(Pos.CENTER_LEFT);
        long total = requests.size();
        long open = requests.stream().filter(req -> "open".equals(req.getStatus())).count();
        long inProgress = requests.stream().filter(req -> "in-progress".equals(req.getStatus())).count();
        long responded = requests.stream().filter(req -> "responded".equals(req.getStatus())).count();
        VBox totalCard = createStatCard("Total Requests", String.valueOf(total), "💬", "#2C3E8C");
        VBox openCard = createStatCard("Open", String.valueOf(open), "⏰", "#3B82F6");
        VBox progressCard = createStatCard("In Progress", String.valueOf(inProgress), "💬", "#F59E0B");
        VBox respondedCard = createStatCard("Responded", String.valueOf(responded), "✅", "#10B981");
        statsCards.getChildren().addAll(totalCard, openCard, progressCard, respondedCard);
        return statsCards;
    }

    private VBox createStatCard(String title, String value, String icon, String iconColor) {
        VBox card = new VBox();
        card.setPrefSize(220, 100);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(16);
        VBox textSection = new VBox(8);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 20; -fx-font-weight: bold;");
        textSection.getChildren().addAll(titleLabel, valueLabel);
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 24;", iconColor));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        content.getChildren().addAll(textSection, spacer, iconLabel);
        card.getChildren().add(content);
        return card;
    }

    private HBox createRequestsView() {
        HBox requestsView = new HBox(24);
        requestsView.setAlignment(Pos.TOP_LEFT);
        VBox requestsListPanel = createRequestsListPanel();
        VBox messageThreadPanel = createMessageThreadPanel();
        HBox.setHgrow(requestsListPanel, Priority.NEVER);
        HBox.setHgrow(messageThreadPanel, Priority.ALWAYS);
        requestsListPanel.setPrefWidth(400);
        messageThreadPanel.setPrefWidth(600);
        requestsView.getChildren().addAll(requestsListPanel, messageThreadPanel);
        return requestsView;
    }

    private VBox createRequestsListPanel() {
        VBox panel = new VBox();
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        tabPane = new TabPane();
        tabPane.getStyleClass().add("floating");
        Tab allTab = new Tab("All");
        Tab openTab = new Tab("Open");
        tabPane.getTabs().addAll(allTab, openTab);
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            filterRequestsByTab(newTab.getText());
        });
        ScrollPane requestsScroll = createRequestsList();
        VBox.setVgrow(requestsScroll, Priority.ALWAYS);
        panel.getChildren().addAll(tabPane, requestsScroll);
        return panel;
    }

    private ScrollPane createRequestsList() {
        VBox requestsList = new VBox(8);
        requestsList.setPadding(new Insets(16));
        requestsList.setStyle("-fx-background-color: transparent;");
        for (Request request : filteredRequests) {
            VBox requestCard = createRequestCard(request);
            requestsList.getChildren().add(requestCard);
        }
        ScrollPane scrollPane = new ScrollPane(requestsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setPrefHeight(600);
        return scrollPane;
    }

    private VBox createRequestCard(Request request) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E5E7EB; -fx-padding: 16; -fx-cursor: hand;");
        card.setSpacing(8);
        if (selectedRequest != null && selectedRequest.getId().equals(request.getId())) {
            card.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #2C3E8C; -fx-border-width: 2; -fx-padding: 16; -fx-cursor: hand;");
        }
        card.setOnMouseClicked(e -> {
            selectedRequest = request;
            refreshMessageThread();
            refreshRequestsList();
        });
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(8);
        VBox subjectSection = new VBox(4);
        Label subjectLabel = new Label(request.getSubject());
        subjectLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14; -fx-font-weight: bold; -fx-wrap-text: true;");
        HBox badges = new HBox(8);
        Label statusBadge = new Label(getStatusDisplayText(request.getStatus()));
        statusBadge.setStyle(getStatusBadgeStyle(request.getStatus()));
        Label typeBadge = new Label(request.getType());
        typeBadge.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-text-fill: #6B7280; -fx-padding: 2 6; -fx-border-radius: 8; -fx-font-size: 11;");
        badges.getChildren().addAll(statusBadge, typeBadge);
        subjectSection.getChildren().addAll(subjectLabel, badges);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(subjectSection, spacer);
        VBox details = new VBox(4);
        HBox fromRow = new HBox(4);
        fromRow.setAlignment(Pos.CENTER_LEFT);
        Label fromIcon = new Label("👤");
        fromIcon.setStyle("-fx-font-size: 12;");
        Label fromLabel = new Label(request.getFrom());
        fromLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");
        fromRow.getChildren().addAll(fromIcon, fromLabel);
        HBox buildingRow = new HBox(4);
        buildingRow.setAlignment(Pos.CENTER_LEFT);
        Label buildingIcon = new Label("🏢");
        buildingIcon.setStyle("-fx-font-size: 12;");
        Label buildingLabel = new Label(request.getBuilding() + " - " + request.getApartment());
        buildingLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");
        buildingRow.getChildren().addAll(buildingIcon, buildingLabel);
        Label dateLabel = new Label(request.getDate());
        dateLabel.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 11;");
        details.getChildren().addAll(fromRow, buildingRow, dateLabel);
        card.getChildren().addAll(header, details);
        return card;
    }

    private VBox createMessageThreadPanel() {
        VBox panel = new VBox();
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #E5E7EB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");
        panel.setPadding(new Insets(24));
        VBox header = createMessageThreadHeader();
        ScrollPane messagesScroll = createMessagesArea();
        VBox.setVgrow(messagesScroll, Priority.ALWAYS);
        VBox replySection = createReplySection();
        panel.getChildren().addAll(header, messagesScroll, replySection);
        return panel;
    }

    private VBox createMessageThreadHeader() {
        VBox header = new VBox(8);
        if (selectedRequest != null) {
            HBox titleRow = new HBox();
            titleRow.setAlignment(Pos.CENTER_LEFT);
            titleRow.setSpacing(8);
            VBox titleSection = new VBox(2);
            Label subjectLabel = new Label(selectedRequest.getSubject());
            subjectLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 18; -fx-font-weight: bold;");
            Label locationLabel = new Label(selectedRequest.getBuilding() + " - " + selectedRequest.getApartment());
            locationLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");
            titleSection.getChildren().addAll(subjectLabel, locationLabel);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            HBox badges = new HBox(8);
            Label statusBadge = new Label(getStatusDisplayText(selectedRequest.getStatus()));
            statusBadge.setStyle(getStatusBadgeStyle(selectedRequest.getStatus()));
            Label priorityBadge = new Label(selectedRequest.getPriority() + " priority");
            priorityBadge.setStyle(getPriorityBadgeStyle(selectedRequest.getPriority()));
            badges.getChildren().addAll(statusBadge, priorityBadge);
            titleRow.getChildren().addAll(titleSection, spacer, badges);
            header.getChildren().add(titleRow);
        } else {
            Label placeholder = new Label("Select a request to view details");
            placeholder.setStyle("-fx-text-fill: #000000; -fx-font-size: 18; -fx-font-weight: bold;");
            header.getChildren().add(placeholder);
        }
        return header;
    }

    private ScrollPane createMessagesArea() {
        VBox messagesContainer = new VBox(16);
        messagesContainer.setPadding(new Insets(16, 0, 16, 0));
        messagesContainer.setStyle("-fx-background-color: transparent;");
        if (selectedRequest != null) {
            for (Message message : selectedRequest.getMessages()) {
                HBox messageBubble = createMessageBubble(message);
                messagesContainer.getChildren().add(messageBubble);
            }
        } else {
            VBox placeholder = createMessagesPlaceholder();
            messagesContainer.getChildren().add(placeholder);
        }
        ScrollPane scrollPane = new ScrollPane(messagesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setPrefHeight(450);
        scrollPane.setVvalue(1.0);
        return scrollPane;
    }

    private HBox createMessageBubble(Message message) {
        HBox messageRow = new HBox();
        messageRow.setAlignment(message.isStaff() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        VBox messageBubble = new VBox(8);
        messageBubble.setMaxWidth(400);
        messageBubble.setPadding(new Insets(16));
        messageBubble.setStyle(message.isStaff() ?
            "-fx-background-color: linear-gradient(to bottom right, #2C3E8C, #4a5fb8); -fx-background-radius: 12; -fx-border-radius: 12;" :
            "-fx-background-color: #F3F4F6; -fx-background-radius: 12; -fx-border-radius: 12;");
        HBox senderRow = new HBox(8);
        senderRow.setAlignment(Pos.CENTER_LEFT);
        Label senderIcon = new Label("👤");
        senderIcon.setStyle("-fx-font-size: 14;");
        Label senderLabel = new Label(message.getSender());
        senderLabel.setStyle(message.isStaff() ?
            "-fx-text-fill: white; -fx-font-size: 14;" :
            "-fx-text-fill: #000000; -fx-font-size: 14;");
        senderRow.getChildren().addAll(senderIcon, senderLabel);
        Label messageText = new Label(message.getText());
        messageText.setStyle(message.isStaff() ?
            "-fx-text-fill: white; -fx-font-size: 14; -fx-wrap-text: true;" :
            "-fx-text-fill: #000000; -fx-font-size: 14; -fx-wrap-text: true;");
        Label timestamp = new Label(message.getTime());
        timestamp.setStyle(message.isStaff() ?
            "-fx-text-fill: rgba(255,255,255,0.7); -fx-font-size: 12;" :
            "-fx-text-fill: #6B7280; -fx-font-size: 12;");
        messageBubble.getChildren().addAll(senderRow, messageText, timestamp);
        messageRow.getChildren().add(messageBubble);
        return messageRow;
    }

    private VBox createMessagesPlaceholder() {
        VBox placeholder = new VBox(16);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setPrefHeight(400);
        Label icon = new Label("💬");
        icon.setStyle("-fx-font-size: 64; -fx-text-fill: #9CA3AF;");
        Label text = new Label("Select a request from the list to view the conversation");
        text.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 16;");
        placeholder.getChildren().addAll(icon, text);
        return placeholder;
    }

    private VBox createReplySection() {
        VBox replySection = new VBox(16);
        replySection.setStyle("-fx-border-color: #E5E7EB; -fx-border-width: 1 0 0 0; -fx-padding: 16 0 0 0;");
        if (selectedRequest != null) {
            HBox replyInput = new HBox(8);
            replyInput.setAlignment(Pos.CENTER_LEFT);
            TextField messageField = new TextField();
            messageField.setPromptText("Type your reply...");
            messageField.setStyle("-fx-background-color: white; -fx-border-color: #D1D5DB; -fx-border-radius: 8; -fx-padding: 12 16; -fx-font-size: 14;");
            HBox.setHgrow(messageField, Priority.ALWAYS);
            Button sendBtn = new Button("Send");
            sendBtn.setStyle("-fx-background-color: linear-gradient(to right, #2C3E8C, #4FD1C5); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
            sendBtn.setOnAction(e -> {
                String text = messageField.getText();
                if (text != null && !text.trim().isEmpty()) {
                    int nextId = selectedRequest.getMessages().size() + 1;
                    selectedRequest.getMessages().add(new Message(nextId, "Admin", text, java.time.LocalDateTime.now().toString(), true));
                    messageField.clear();
                    refreshMessageThread();
                }
            });
            replyInput.getChildren().addAll(messageField, sendBtn);
            if (!"closed".equals(selectedRequest.getStatus())) {
                HBox actionButtons = new HBox(8);
                Button markProgressBtn = new Button("Mark In Progress");
                markProgressBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-text-fill: #374151; -fx-padding: 12 16; -fx-background-radius: 8; -fx-pref-width: 200;");
                markProgressBtn.setOnAction(e -> markRequestInProgress());
                Button closeBtn = new Button("Close Request");
                closeBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-text-fill: #10B981; -fx-padding: 12 16; -fx-background-radius: 8; -fx-pref-width: 200;");
                closeBtn.setOnAction(e -> closeRequest());
                actionButtons.getChildren().addAll(markProgressBtn, closeBtn);
                replySection.getChildren().addAll(replyInput, actionButtons);
            } else {
                replySection.getChildren().add(replyInput);
            }
        }
        return replySection;
    }

    private String getStatusDisplayText(String status) {
        switch (status) {
            case "open": return "Open";
            case "responded": return "Responded";
            case "in-progress": return "In Progress";
            case "closed": return "Closed";
            default: return status;
        }
    }

    private String getStatusBadgeStyle(String status) {
        switch (status) {
            case "open":
                return "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            case "responded":
                return "-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            case "in-progress":
                return "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            case "closed":
                return "-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
            default:
                return "-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-padding: 4 8; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;";
        }
    }

    private String getPriorityBadgeStyle(String priority) {
        switch (priority) {
            case "high":
                return "-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-text-fill: #EF4444; -fx-padding: 4 8; -fx-border-radius: 12; -fx-font-size: 12;";
            case "medium":
                return "-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-text-fill: #F59E0B; -fx-padding: 4 8; -fx-border-radius: 12; -fx-font-size: 12;";
            case "low":
                return "-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-text-fill: #10B981; -fx-padding: 4 8; -fx-border-radius: 12; -fx-font-size: 12;";
            default:
                return "-fx-background-color: transparent; -fx-border-color: #D1D5DB; -fx-text-fill: #6B7280; -fx-padding: 4 8; -fx-border-radius: 12; -fx-font-size: 12;";
        }
    }

    private void filterRequestsByTab(String tabText) {
        switch (tabText) {
            case "All":
                filteredRequests.setPredicate(request -> true);
                break;
            case "Open":
                filteredRequests.setPredicate(request -> "open".equals(request.getStatus()));
                break;
        }
        refreshRequestsList();
    }

    private void refreshRequestsList() {
        content.getChildren().clear();
        createUI();
    }

    private void refreshMessageThread() {
        content.getChildren().clear();
        createUI();
    }

    private void showNewRequestDialog() {
        Dialog<Request> dialog = new Dialog<>();
        dialog.setTitle("New Request");
        dialog.setHeaderText("Create New Request");
        ButtonType createBtnType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createBtnType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField fromField = new TextField();
        fromField.setPromptText("From (Name)");
        TextField buildingField = new TextField();
        buildingField.setPromptText("Building");
        TextField apartmentField = new TextField();
        apartmentField.setPromptText("Apartment");
        ComboBox<String> typeCombo = new ComboBox<>(FXCollections.observableArrayList("Information", "Document", "Maintenance", "Complaint", "Other"));
        typeCombo.setValue("Information");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject");
        ComboBox<String> priorityCombo = new ComboBox<>(FXCollections.observableArrayList("high", "medium", "low"));
        priorityCombo.setValue("medium");
        grid.add(new Label("From:"), 0, 0); grid.add(fromField, 1, 0);
        grid.add(new Label("Building:"), 0, 1); grid.add(buildingField, 1, 1);
        grid.add(new Label("Apartment:"), 0, 2); grid.add(apartmentField, 1, 2);
        grid.add(new Label("Type:"), 0, 3); grid.add(typeCombo, 1, 3);
        grid.add(new Label("Subject:"), 0, 4); grid.add(subjectField, 1, 4);
        grid.add(new Label("Priority:"), 0, 5); grid.add(priorityCombo, 1, 5);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createBtnType) {
                return new Request(
                    "REQ-" + java.time.LocalDate.now() + "-" + (requests.size() + 1),
                    fromField.getText(),
                    buildingField.getText(),
                    apartmentField.getText(),
                    typeCombo.getValue(),
                    subjectField.getText(),
                    "open",
                    java.time.LocalDateTime.now().toString(),
                    priorityCombo.getValue(),
                    FXCollections.observableArrayList(new Message(1, fromField.getText(), subjectField.getText(), java.time.LocalDateTime.now().toString(), false))
                );
            }
            return null;
        });
        dialog.showAndWait().ifPresent(request -> {
            requests.add(request);
            filteredRequests.setPredicate(r -> true);
            selectedRequest = request;
            refreshRequestsList();
        });
    }

    private void markRequestInProgress() {
        if (selectedRequest != null) {
            selectedRequest.setStatus("in-progress");
            refreshMessageThread();
        }
    }

    private void closeRequest() {
        if (selectedRequest != null) {
            selectedRequest.setStatus("closed");
            refreshMessageThread();
        }
    }

    @Override
    public VBox getContent() { return content; }
    @Override
    public String getTitle() { return title; }
}
