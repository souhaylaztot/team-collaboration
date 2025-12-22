package com.propertymanager;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.text.NumberFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;

public class LanguageManager {
    private static final String LANGUAGE_KEY = "app_language";
    private static final String COUNTRY_KEY = "app_country";
    private static final String CURRENCY_KEY = "app_currency";
    
    private static Locale currentLocale = Locale.getDefault();
    private static Currency currentCurrency = Currency.getInstance("MAD");
    private static Preferences prefs = Preferences.userNodeForPackage(LanguageManager.class);
    
    static {
        loadSettings();
    }
    
    public static void setLanguage(String languageCode) {
        Locale newLocale;
        switch (languageCode) {
            case "العربية":
                newLocale = new Locale("ar", "MA");
                break;
            case "Français":
                newLocale = new Locale("fr", "FR");
                break;
            case "Español":
                newLocale = new Locale("es", "ES");
                break;
            case "English":
            default:
                newLocale = new Locale("en", "US");
                break;
        }
        
        currentLocale = newLocale;
        Locale.setDefault(newLocale);
        
        // Save to preferences
        prefs.put(LANGUAGE_KEY, newLocale.getLanguage());
        prefs.put(COUNTRY_KEY, newLocale.getCountry());
        
        System.out.println("Language changed to: " + languageCode + " (" + newLocale + ")");
    }
    
    public static void setCurrency(String currencyCode) {
        try {
            String code = extractCurrencyCode(currencyCode);
            currentCurrency = Currency.getInstance(code);
            prefs.put(CURRENCY_KEY, code);
            System.out.println("Currency changed to: " + currencyCode);
        } catch (Exception e) {
            System.err.println("Invalid currency: " + currencyCode);
        }
    }
    
    private static String extractCurrencyCode(String currencyDisplay) {
        if (currencyDisplay.contains("MAD")) return "MAD";
        if (currencyDisplay.contains("USD")) return "USD";
        if (currencyDisplay.contains("EUR")) return "EUR";
        if (currencyDisplay.contains("GBP")) return "GBP";
        return "MAD"; // default
    }
    
    public static Locale getCurrentLocale() {
        return currentLocale;
    }
    
    public static Currency getCurrentCurrency() {
        return currentCurrency;
    }
    
    public static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(currentLocale);
        formatter.setCurrency(currentCurrency);
        return formatter.format(amount);
    }
    
    public static String formatNumber(double number) {
        NumberFormat formatter = NumberFormat.getNumberInstance(currentLocale);
        return formatter.format(number);
    }
    
    public static String formatDate(java.util.Date date) {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, currentLocale);
        return formatter.format(date);
    }
    
    public static String formatDateTime(java.util.Date date) {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, currentLocale);
        return formatter.format(date);
    }
    
    // Localized text methods
    public static String getText(String key) {
        return getLocalizedText(key, currentLocale);
    }
    
    private static String getLocalizedText(String key, Locale locale) {
        // In a real application, this would load from resource bundles
        // For now, we'll return localized strings based on the key and locale
        
        if (locale.getLanguage().equals("ar")) {
            return getArabicText(key);
        } else if (locale.getLanguage().equals("fr")) {
            return getFrenchText(key);
        } else if (locale.getLanguage().equals("es")) {
            return getSpanishText(key);
        } else {
            return getEnglishText(key);
        }
    }
    
    private static String getEnglishText(String key) {
        return switch (key) {
            case "dashboard" -> "Dashboard";
            case "buildings" -> "Buildings";
            case "buyers" -> "Buyers";
            case "lands" -> "Lands";
            case "permits" -> "Permits";
            case "maintenance" -> "Maintenance";
            case "reports" -> "Reports";
            case "requests" -> "Requests";
            case "transportation" -> "Transportation";
            case "settings" -> "Settings";
            case "logout" -> "Logout";
            case "welcome" -> "Welcome";
            case "total_properties" -> "Total Properties";
            case "active_tenants" -> "Active Tenants";
            case "monthly_revenue" -> "Monthly Revenue";
            case "pending_requests" -> "Pending Requests";
            default -> key;
        };
    }
    
    private static String getArabicText(String key) {
        return switch (key) {
            case "dashboard" -> "لوحة التحكم";
            case "buildings" -> "المباني";
            case "buyers" -> "المشترين";
            case "lands" -> "الأراضي";
            case "permits" -> "التصاريح";
            case "maintenance" -> "الصيانة";
            case "reports" -> "التقارير";
            case "requests" -> "الطلبات";
            case "transportation" -> "النقل";
            case "settings" -> "الإعدادات";
            case "logout" -> "تسجيل الخروج";
            case "welcome" -> "مرحباً";
            case "total_properties" -> "إجمالي العقارات";
            case "active_tenants" -> "المستأجرين النشطين";
            case "monthly_revenue" -> "الإيرادات الشهرية";
            case "pending_requests" -> "الطلبات المعلقة";
            default -> key;
        };
    }
    
    private static String getFrenchText(String key) {
        return switch (key) {
            case "dashboard" -> "Tableau de bord";
            case "buildings" -> "Bâtiments";
            case "buyers" -> "Acheteurs";
            case "lands" -> "Terrains";
            case "permits" -> "Permis";
            case "maintenance" -> "Maintenance";
            case "reports" -> "Rapports";
            case "requests" -> "Demandes";
            case "transportation" -> "Transport";
            case "settings" -> "Paramètres";
            case "logout" -> "Déconnexion";
            case "welcome" -> "Bienvenue";
            case "total_properties" -> "Total des propriétés";
            case "active_tenants" -> "Locataires actifs";
            case "monthly_revenue" -> "Revenus mensuels";
            case "pending_requests" -> "Demandes en attente";
            default -> key;
        };
    }
    
    private static String getSpanishText(String key) {
        return switch (key) {
            case "dashboard" -> "Panel de control";
            case "buildings" -> "Edificios";
            case "buyers" -> "Compradores";
            case "lands" -> "Terrenos";
            case "permits" -> "Permisos";
            case "maintenance" -> "Mantenimiento";
            case "reports" -> "Informes";
            case "requests" -> "Solicitudes";
            case "transportation" -> "Transporte";
            case "settings" -> "Configuración";
            case "logout" -> "Cerrar sesión";
            case "welcome" -> "Bienvenido";
            case "total_properties" -> "Total de propiedades";
            case "active_tenants" -> "Inquilinos activos";
            case "monthly_revenue" -> "Ingresos mensuales";
            case "pending_requests" -> "Solicitudes pendientes";
            default -> key;
        };
    }
    
    public static String getCurrentLanguageDisplay() {
        return switch (currentLocale.getLanguage()) {
            case "ar" -> "العربية";
            case "fr" -> "Français";
            case "es" -> "Español";
            default -> "English";
        };
    }
    
    public static String getCurrentCurrencyDisplay() {
        String code = currentCurrency.getCurrencyCode();
        return switch (code) {
            case "MAD" -> "MAD (Dirham)";
            case "USD" -> "USD (Dollar)";
            case "EUR" -> "EUR (Euro)";
            case "GBP" -> "GBP (Pound)";
            default -> code;
        };
    }
    
    private static void loadSettings() {
        String language = prefs.get(LANGUAGE_KEY, "en");
        String country = prefs.get(COUNTRY_KEY, "US");
        String currencyCode = prefs.get(CURRENCY_KEY, "MAD");
        
        currentLocale = new Locale(language, country);
        Locale.setDefault(currentLocale);
        
        try {
            currentCurrency = Currency.getInstance(currencyCode);
        } catch (Exception e) {
            currentCurrency = Currency.getInstance("MAD");
        }
    }
    
    public static boolean isRTL() {
        return currentLocale.getLanguage().equals("ar");
    }
}