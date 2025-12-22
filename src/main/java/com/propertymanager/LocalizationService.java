package com.propertymanager;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;

public class LocalizationService {
    private static LocalizationService instance;
    private ResourceBundle resourceBundle;
    private Locale currentLocale;
    private final Preferences prefs = Preferences.userNodeForPackage(LocalizationService.class);
    
    // Observable properties for UI binding
    private final Map<String, StringProperty> localizedStrings = new ConcurrentHashMap<>();
    
    // Observers for language change notifications
    private final ObservableList<LanguageChangeListener> listeners = FXCollections.observableArrayList();
    
    // Supported locales
    private static final Map<String, Locale> SUPPORTED_LOCALES = Map.of(
        "English", Locale.ENGLISH,
        "Français", Locale.FRENCH,
        "Español", new Locale("es", "ES")
    );
    
    private LocalizationService() {
        loadSavedLanguage();
        initializeResourceBundle();
    }
    
    public static LocalizationService getInstance() {
        if (instance == null) {
            instance = new LocalizationService();
        }
        return instance;
    }
    
    private void loadSavedLanguage() {
        String savedLanguage = prefs.get("language", "English");
        currentLocale = SUPPORTED_LOCALES.getOrDefault(savedLanguage, Locale.ENGLISH);
    }
    
    private void initializeResourceBundle() {
        try {
            resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
        } catch (MissingResourceException e) {
            System.err.println("Resource bundle not found for locale: " + currentLocale);
            resourceBundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
        }
    }
    
    public void changeLanguage(String languageName) {
        Locale newLocale = SUPPORTED_LOCALES.get(languageName);
        if (newLocale != null && !newLocale.equals(currentLocale)) {
            currentLocale = newLocale;
            
            // Save preference
            prefs.put("language", languageName);
            
            // Reload resource bundle
            initializeResourceBundle();
            
            // Update all observable strings
            updateAllLocalizedStrings();
            
            // Notify all listeners
            notifyLanguageChanged();
            
            System.out.println("Language changed to: " + languageName);
        }
    }
    
    public String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            System.err.println("Missing translation key: " + key);
            return "!" + key + "!";
        }
    }
    
    public StringProperty getStringProperty(String key) {
        return localizedStrings.computeIfAbsent(key, k -> {
            StringProperty property = new SimpleStringProperty(getString(k));
            return property;
        });
    }
    
    private void updateAllLocalizedStrings() {
        localizedStrings.forEach((key, property) -> {
            property.set(getString(key));
        });
    }
    
    public void addLanguageChangeListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removeLanguageChangeListener(LanguageChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyLanguageChanged() {
        for (LanguageChangeListener listener : listeners) {
            try {
                listener.onLanguageChanged(currentLocale);
            } catch (Exception e) {
                System.err.println("Error notifying language change listener: " + e.getMessage());
            }
        }
    }
    
    public Locale getCurrentLocale() {
        return currentLocale;
    }
    
    public String getCurrentLanguageName() {
        return SUPPORTED_LOCALES.entrySet().stream()
            .filter(entry -> entry.getValue().equals(currentLocale))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse("English");
    }
    
    public Set<String> getSupportedLanguages() {
        return SUPPORTED_LOCALES.keySet();
    }
    
    // Functional interface for language change listeners
    @FunctionalInterface
    public interface LanguageChangeListener {
        void onLanguageChanged(Locale newLocale);
    }
    
    // Utility methods for common UI elements
    public String getMenuText(String menuItem) {
        return getString("menu." + menuItem.toLowerCase());
    }
    
    public String getActionText(String action) {
        return getString("action." + action.toLowerCase());
    }
    
    public String getFormText(String field) {
        return getString("form." + field.toLowerCase());
    }
    
    public String getMessageText(String message) {
        return getString("msg." + message.toLowerCase());
    }
    
    public String getValidationText(String validation) {
        return getString("validation." + validation.toLowerCase());
    }
    
    // Method to refresh a specific UI component
    public void refreshComponent(Object component) {
        if (component instanceof LocalizableComponent) {
            ((LocalizableComponent) component).updateLanguage();
        }
    }
    
    // Interface for components that need language updates
    public interface LocalizableComponent {
        void updateLanguage();
    }
}