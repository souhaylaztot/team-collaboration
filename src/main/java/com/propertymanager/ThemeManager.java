package com.propertymanager;

public class ThemeManager {
    private static boolean isDarkMode = false;
    
    public static boolean isDarkMode() {
        return isDarkMode;
    }
    
    public static void setDarkMode(boolean darkMode) {
        isDarkMode = darkMode;
    }
    
    public static void toggleTheme() {
        isDarkMode = !isDarkMode;
    }
    
    public static void applyLightTheme() {
        isDarkMode = false;
    }
    
    // WhatsApp-like color scheme
    public static String getBackground() {
        return isDarkMode ? "linear-gradient(to bottom right, #0A0A0A, #1A1A1A)" : "#F8FAFC";
    }
    
    public static String getSurface() {
        return isDarkMode ? "#1F2C34" : "#FFFFFF";
    }
    
    public static String getCard() {
        return isDarkMode ? "#262626" : "#FFFFFF";
    }
    
    public static String getSidebar() {
        return isDarkMode ? "#111827" : "#FFFFFF";
    }
    
    public static String getTopbar() {
        return isDarkMode ? "#1F2937" : "#FFFFFF";
    }
    
    public static String getPrimary() {
        return isDarkMode ? "#1E40AF" : "#00A884";
    }
    
    public static String getTextPrimary() {
        return isDarkMode ? "#FFFFFF" : "#111B21";
    }
    
    public static String getTextSecondary() {
        return isDarkMode ? "#E5E5E5" : "#667781";
    }
    
    public static String getTextMuted() {
        return isDarkMode ? "#A3A3A3" : "#8696A0";
    }
    
    public static String getBorder() {
        return isDarkMode ? "#404040" : "#E9EDEF";
    }
    
    public static String getHover() {
        return isDarkMode ? "#374151" : "#F5F6F6";
    }
    
    public static String getAccent() {
        return isDarkMode ? "#1E40AF" : "#00A884";
    }
    
    public static String getError() {
        return isDarkMode ? "#F15C6D" : "#E53E3E";
    }
    
    public static String getWarning() {
        return isDarkMode ? "#FFAB00" : "#FF9500";
    }
    
    public static String getSuccess() {
        return isDarkMode ? "#06CF9C" : "#06CF9C";
    }
}