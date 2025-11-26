package com.smartpropertymanager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        messageLabel.setText("Hello from HomeController!");
    }

    @FXML
    private void onButtonClick() {
        messageLabel.setText("Button clicked!");
    }
}
