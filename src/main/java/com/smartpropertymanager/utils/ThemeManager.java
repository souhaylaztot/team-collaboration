package com.smartpropertymanager.utils;

import javafx.scene.Scene;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ThemeManager {
    private static ThemeManager instance;
    private static boolean isDarkMode = false;
    private static Scene currentScene;
    private static final List<ThemeChangeListener> listeners = new ArrayList<>();
    private static final String CONFIG_FILE = "smart_property_manager_config.properties";
    
    // CSS file paths
    private static final String BASE_CSS = "/css/base.css";
    private static final String LIGHT_THEME_CSS = "/css/light-theme.css";
    private static final String DARK_THEME_CSS = "/css/dark-theme.css";
    private static final String SIDEBAR_CSS = "/css/components/sidebar.css";
    private static final String DASHBOARD_CSS = "/css/components/dashboard.css";
    private static final String HEADER_CSS = "/css/components/header.css";
    private static final String SETTINGS_CSS = "/css/components/settings.css";
    
    public interface ThemeChangeListener {
        void onThemeChanged(boolean isDarkMode);
    }
    
    private ThemeManager() {
        // Private constructor for singleton
    }
    
    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }
    
    public static void initialize(Scene scene) {
        loadThemePreference();
        currentScene = scene;
        applyTheme(scene);
    }
    
    public static void toggleTheme() {
        isDarkMode = !isDarkMode;
        saveThemePreference();
        if (currentScene != null) {
            applyTheme(currentScene);
        }
        notifyListeners();
    }
    
    public static void applyTheme(Scene scene) {
        if (scene == null) return;
        
        // Clear existing stylesheets
        scene.getStylesheets().clear();
        
        // Add base styles
        addStylesheetIfExists(scene, BASE_CSS);
        
        // Add theme-specific styles
        if (isDarkMode) {
            addStylesheetIfExists(scene, DARK_THEME_CSS);
        } else {
            addStylesheetIfExists(scene, LIGHT_THEME_CSS);
        }
        
        // Add component styles
        addStylesheetIfExists(scene, SIDEBAR_CSS);
        addStylesheetIfExists(scene, DASHBOARD_CSS);
        addStylesheetIfExists(scene, HEADER_CSS);
        addStylesheetIfExists(scene, SETTINGS_CSS);
    }
    
    private static void addStylesheetIfExists(Scene scene, String path) {
        try {
            String stylesheet = ThemeManager.class.getResource(path).toExternalForm();
            scene.getStylesheets().add(stylesheet);
        } catch (Exception e) {
            System.err.println("Stylesheet not found: " + path);
        }
    }
    
    public static void addThemeChangeListener(ThemeChangeListener listener) {
        listeners.add(listener);
    }
    
    public static void removeThemeChangeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }
    
    private static void notifyListeners() {
        for (ThemeChangeListener listener : listeners) {
            listener.onThemeChanged(isDarkMode);
        }
    }
    
    private static void saveThemePreference() {
        Properties props = new Properties();
        props.setProperty("darkMode", String.valueOf(isDarkMode));
        
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, "Smart Property Manager Configuration");
        } catch (IOException e) {
            System.err.println("Error saving theme preference: " + e.getMessage());
        }
    }
    
    private static void loadThemePreference() {
        Properties props = new Properties();
        
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
            isDarkMode = Boolean.parseBoolean(props.getProperty("darkMode", "false"));
        } catch (IOException e) {
            // Use default theme if config file doesn't exist
            isDarkMode = false;
            System.out.println("No existing config found, using default theme.");
        }
    }
    
    public static boolean isDarkMode() {
        return isDarkMode;
    }
    
    public static void setDarkMode(boolean darkMode) {
        if (isDarkMode != darkMode) {
            isDarkMode = darkMode;
            saveThemePreference();
            if (currentScene != null) {
                applyTheme(currentScene);
            }
            notifyListeners();
        }
    }
    
    // Legacy color methods for backward compatibility
    public static String getBackgroundColor() {
        return isDarkMode ? "#1a1a1a" : "#F5F5F5";
    }
    
    public static String getCardBackgroundColor() {
        return isDarkMode ? "#2d2d2d" : "#FFFFFF";
    }
    
    public static String getTextColor() {
        return isDarkMode ? "#ffffff" : "#333333";
    }
    
    public static String getSecondaryTextColor() {
        return isDarkMode ? "#e0e0e0" : "#6B7280";
    }
    
    public static String getBorderColor() {
        return isDarkMode ? "#404040" : "#E5E7EB";
    }
    
    public static String getAccentColor() {
        return isDarkMode ? "#4FD1C5" : "#2C3E8C";
    }
    
    // Method to get config file path for debugging
    public static String getConfigFilePath() {
        return new File(CONFIG_FILE).getAbsolutePath();
    }
}