package com.propertymanager;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BackupManager {
    
    private static final String BACKUP_EXTENSION = ".sql";
    private static final String DEFAULT_BACKUP_DIR = "backups";
    
    public static void showBackupDialog(Stage parentStage) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle("Database Backup & Restore");
        dialog.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("dialog");
        
        // Header
        VBox header = createHeader();
        
        // Backup Section
        VBox backupSection = createBackupSection(dialog);
        
        // Restore Section
        VBox restoreSection = createRestoreSection(dialog);
        
        // Recent Backups
        VBox recentSection = createRecentBackupsSection();
        
        root.getChildren().addAll(header, backupSection, new Separator(), restoreSection, new Separator(), recentSection);
        
        Scene scene = new Scene(root, 600, 700);
        scene.getStylesheets().add(BackupManager.class.getResource("/styles/global.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private static VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        
        Label icon = new Label("💾");
        icon.setStyle("-fx-font-size: 48px;");
        
        Label title = new Label("Database Backup & Restore");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a202c"));
        
        Label subtitle = new Label("Backup your data or restore from previous backups");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.web("#6b7280"));
        
        header.getChildren().addAll(icon, title, subtitle);
        return header;
    }
    
    private static VBox createBackupSection(Stage dialog) {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("📤 Create Backup");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1a202c"));
        
        Label description = new Label("Create a complete backup of your property management database");
        description.setFont(Font.font("Segoe UI", 12));
        description.setTextFill(Color.web("#6b7280"));
        description.setWrapText(true);
        
        HBox backupControls = new HBox(15);
        backupControls.setAlignment(Pos.CENTER_LEFT);
        
        TextField locationField = new TextField();
        locationField.setPromptText("Select backup location...");
        locationField.setPrefWidth(300);
        locationField.setEditable(false);
        locationField.setStyle("-fx-background-color: #f3f4f6; -fx-border-color: #d1d5db; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        
        Button browseBtn = new Button("📁 Browse");
        browseBtn.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 16;");
        browseBtn.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Backup Location");
            File selectedDir = chooser.showDialog(dialog);
            if (selectedDir != null) {
                locationField.setText(selectedDir.getAbsolutePath());
            }
        });
        
        Button backupBtn = new Button("💾 Create Backup");
        backupBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 16; -fx-font-weight: bold;");
        backupBtn.setOnAction(e -> {
            String location = locationField.getText();
            if (location.isEmpty()) {
                // Use default location
                File defaultDir = new File(DEFAULT_BACKUP_DIR);
                if (!defaultDir.exists()) {
                    defaultDir.mkdirs();
                }
                location = defaultDir.getAbsolutePath();
            }
            createBackup(location, dialog);
        });
        
        backupControls.getChildren().addAll(locationField, browseBtn, backupBtn);
        
        section.getChildren().addAll(title, description, backupControls);
        return section;
    }
    
    private static VBox createRestoreSection(Stage dialog) {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("📥 Restore Backup");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1a202c"));
        
        Label description = new Label("Restore your database from a previous backup file");
        description.setFont(Font.font("Segoe UI", 12));
        description.setTextFill(Color.web("#6b7280"));
        description.setWrapText(true);
        
        Label warning = new Label("⚠️ Warning: This will replace all current data!");
        warning.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        warning.setTextFill(Color.web("#dc2626"));
        
        HBox restoreControls = new HBox(15);
        restoreControls.setAlignment(Pos.CENTER_LEFT);
        
        TextField fileField = new TextField();
        fileField.setPromptText("Select backup file...");
        fileField.setPrefWidth(300);
        fileField.setEditable(false);
        fileField.setStyle("-fx-background-color: #f3f4f6; -fx-border-color: #d1d5db; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
        
        Button browseFileBtn = new Button("📁 Browse");
        browseFileBtn.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 16;");
        browseFileBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Backup File");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));
            File selectedFile = chooser.showOpenDialog(dialog);
            if (selectedFile != null) {
                fileField.setText(selectedFile.getAbsolutePath());
            }
        });
        
        Button restoreBtn = new Button("📥 Restore");
        restoreBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 16; -fx-font-weight: bold;");
        restoreBtn.setOnAction(e -> {
            String filePath = fileField.getText();
            if (filePath.isEmpty()) {
                showAlert("Error", "Please select a backup file to restore.");
                return;
            }
            
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Restore");
            confirmAlert.setHeaderText("Are you sure you want to restore from backup?");
            confirmAlert.setContentText("This will replace ALL current data with the backup data. This action cannot be undone.");
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    restoreBackup(filePath, dialog);
                }
            });
        });
        
        restoreControls.getChildren().addAll(fileField, browseFileBtn, restoreBtn);
        
        section.getChildren().addAll(title, description, warning, restoreControls);
        return section;
    }
    
    private static VBox createRecentBackupsSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("📋 Recent Backups");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1a202c"));
        
        ListView<String> backupList = new ListView<>();
        backupList.setPrefHeight(150);
        backupList.setStyle("-fx-background-color: #f9fafb; -fx-border-color: #e5e7eb; -fx-border-radius: 6;");
        
        // Load recent backups
        loadRecentBackups(backupList);
        
        section.getChildren().addAll(title, backupList);
        return section;
    }
    
    private static void loadRecentBackups(ListView<String> backupList) {
        File backupDir = new File(DEFAULT_BACKUP_DIR);
        if (backupDir.exists() && backupDir.isDirectory()) {
            File[] backupFiles = backupDir.listFiles((dir, name) -> name.endsWith(BACKUP_EXTENSION));
            if (backupFiles != null) {
                for (File file : backupFiles) {
                    String fileName = file.getName();
                    long fileSize = file.length() / 1024; // KB
                    String lastModified = LocalDateTime.ofEpochSecond(file.lastModified() / 1000, 0, java.time.ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    
                    backupList.getItems().add(String.format("%s (%d KB) - %s", fileName, fileSize, lastModified));
                }
            }
        }
        
        if (backupList.getItems().isEmpty()) {
            backupList.getItems().add("No backups found");
        }
    }
    
    private static void createBackup(String location, Stage parentStage) {
        // Create progress dialog
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.initOwner(parentStage);
        progressStage.setTitle("Creating Backup...");
        
        VBox progressRoot = new VBox(20);
        progressRoot.setPadding(new Insets(30));
        progressRoot.setAlignment(Pos.CENTER);
        
        Label statusLabel = new Label("Creating database backup...");
        statusLabel.setFont(Font.font("Segoe UI", 14));
        
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);
        
        progressRoot.getChildren().addAll(statusLabel, progressBar);
        
        Scene progressScene = new Scene(progressRoot, 400, 150);
        progressStage.setScene(progressScene);
        progressStage.show();
        
        // Create backup task
        Task<Boolean> backupTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                updateMessage("Connecting to database...");
                updateProgress(0.1, 1.0);
                
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String fileName = "property_manager_backup_" + timestamp + BACKUP_EXTENSION;
                File backupFile = new File(location, fileName);
                
                updateMessage("Exporting database...");
                updateProgress(0.3, 1.0);
                
                return exportDatabase(backupFile.getAbsolutePath());
            }
            
            @Override
            protected void succeeded() {
                progressStage.close();
                if (getValue()) {
                    showAlert("Success", "Database backup created successfully!");
                } else {
                    showAlert("Error", "Failed to create database backup.");
                }
            }
            
            @Override
            protected void failed() {
                progressStage.close();
                showAlert("Error", "Backup failed: " + getException().getMessage());
            }
        };
        
        statusLabel.textProperty().bind(backupTask.messageProperty());
        progressBar.progressProperty().bind(backupTask.progressProperty());
        
        Thread backupThread = new Thread(backupTask);
        backupThread.setDaemon(true);
        backupThread.start();
    }
    
    private static boolean exportDatabase(String filePath) {
        try {
            // Use pg_dump command
            ProcessBuilder pb = new ProcessBuilder(
                "pg_dump",
                "-h", "localhost",
                "-p", "5432",
                "-U", "postgres",
                "-d", "property_manager",
                "-f", filePath,
                "--no-password"
            );
            
            // Set PGPASSWORD environment variable
            pb.environment().put("PGPASSWORD", "4426");
            
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: Manual SQL export
            return exportDatabaseManually(filePath);
        }
    }
    
    private static boolean exportDatabaseManually(String filePath) {
        try (Connection conn = DatabaseManager.getConnection()) {
            StringBuilder sql = new StringBuilder();
            
            // Get all table names
            List<String> tables = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            
            // Export each table
            for (String table : tables) {
                sql.append("-- Table: ").append(table).append("\n");
                
                // Get table data
                PreparedStatement dataStmt = conn.prepareStatement("SELECT * FROM " + table);
                ResultSet dataRs = dataStmt.executeQuery();
                
                while (dataRs.next()) {
                    sql.append("INSERT INTO ").append(table).append(" VALUES (");
                    int columnCount = dataRs.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i > 1) sql.append(", ");
                        Object value = dataRs.getObject(i);
                        if (value == null) {
                            sql.append("NULL");
                        } else if (value instanceof String) {
                            sql.append("'").append(value.toString().replace("'", "''")).append("'");
                        } else {
                            sql.append(value.toString());
                        }
                    }
                    sql.append(");\n");
                }
                sql.append("\n");
            }
            
            // Write to file
            Files.write(Paths.get(filePath), sql.toString().getBytes());
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static void restoreBackup(String filePath, Stage parentStage) {
        // Create progress dialog
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.initOwner(parentStage);
        progressStage.setTitle("Restoring Backup...");
        
        VBox progressRoot = new VBox(20);
        progressRoot.setPadding(new Insets(30));
        progressRoot.setAlignment(Pos.CENTER);
        
        Label statusLabel = new Label("Restoring database from backup...");
        statusLabel.setFont(Font.font("Segoe UI", 14));
        
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);
        
        progressRoot.getChildren().addAll(statusLabel, progressBar);
        
        Scene progressScene = new Scene(progressRoot, 400, 150);
        progressStage.setScene(progressScene);
        progressStage.show();
        
        // Create restore task
        Task<Boolean> restoreTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                updateMessage("Preparing restore...");
                updateProgress(0.1, 1.0);
                
                updateMessage("Clearing existing data...");
                updateProgress(0.3, 1.0);
                
                updateMessage("Restoring from backup...");
                updateProgress(0.7, 1.0);
                
                return importDatabase(filePath);
            }
            
            @Override
            protected void succeeded() {
                progressStage.close();
                if (getValue()) {
                    showAlert("Success", "Database restored successfully!");
                } else {
                    showAlert("Error", "Failed to restore database.");
                }
            }
            
            @Override
            protected void failed() {
                progressStage.close();
                showAlert("Error", "Restore failed: " + getException().getMessage());
            }
        };
        
        statusLabel.textProperty().bind(restoreTask.messageProperty());
        progressBar.progressProperty().bind(restoreTask.progressProperty());
        
        Thread restoreThread = new Thread(restoreTask);
        restoreThread.setDaemon(true);
        restoreThread.start();
    }
    
    private static boolean importDatabase(String filePath) {
        try {
            // Use psql command
            ProcessBuilder pb = new ProcessBuilder(
                "psql",
                "-h", "localhost",
                "-p", "5432",
                "-U", "postgres",
                "-d", "property_manager",
                "-f", filePath
            );
            
            // Set PGPASSWORD environment variable
            pb.environment().put("PGPASSWORD", "4426");
            
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: Manual SQL import
            return importDatabaseManually(filePath);
        }
    }
    
    private static boolean importDatabaseManually(String filePath) {
        try {
            String sql = new String(Files.readAllBytes(Paths.get(filePath)));
            
            try (Connection conn = DatabaseManager.getConnection()) {
                // Split SQL into individual statements
                String[] statements = sql.split(";");
                
                for (String statement : statements) {
                    statement = statement.trim();
                    if (!statement.isEmpty() && !statement.startsWith("--")) {
                        try (PreparedStatement stmt = conn.prepareStatement(statement)) {
                            stmt.execute();
                        }
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}