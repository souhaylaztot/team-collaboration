package com.smartpropertymanager.utils;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    private static String currentLanguage = "English";
    private static Map<String, Map<String, String>> translations = new HashMap<>();
    
    static {
        initializeTranslations();
    }
    
    private static void initializeTranslations() {
        // English translations (default)
        Map<String, String> english = new HashMap<>();
        english.put("app.title", "Smart Property Manager Pro");
        english.put("dashboard", "Dashboard");
        english.put("building.management", "Building Management");
        english.put("buyer.management", "Buyer Management");
        english.put("land.management", "Land Management");
        english.put("permits", "Permits");
        english.put("maintenance", "Maintenance");
        english.put("reports", "Reports");
        english.put("requests", "Requests");
        english.put("settings", "Settings");
        english.put("logout", "Logout");
        
        // Settings page
        english.put("settings.title", "Settings");
        english.put("settings.subtitle", "Manage your application preferences and configurations");
        english.put("general.settings", "General Settings");
        english.put("language", "Language");
        english.put("language.desc", "Choose your preferred language");
        english.put("currency", "Currency");
        english.put("currency.desc", "Default currency for financial calculations");
        english.put("timezone", "Time Zone");
        english.put("timezone.desc", "Your local time zone");
        english.put("notifications", "Notifications");
        english.put("email.notifications", "Email Notifications");
        english.put("email.notifications.desc", "Receive notifications via email");
        english.put("push.notifications", "Push Notifications");
        english.put("push.notifications.desc", "Show desktop notifications");
        english.put("display.appearance", "Display & Appearance");
        english.put("theme", "Theme");
        english.put("theme.desc", "Choose your preferred theme");
        english.put("font.size", "Font Size");
        english.put("font.size.desc", "Adjust text size for better readability");
        english.put("security.privacy", "Security & Privacy");
        english.put("auto.lock", "Auto-lock");
        english.put("auto.lock.desc", "Automatically lock after inactivity");
        english.put("data.management", "Data Management");
        english.put("auto.save", "Auto-save");
        english.put("auto.save.desc", "Automatically save changes");
        english.put("system", "System");
        english.put("performance.mode", "Performance Mode");
        english.put("performance.mode.desc", "Optimize for better performance");
        
        // French translations
        Map<String, String> french = new HashMap<>();
        french.put("app.title", "Gestionnaire Immobilier Intelligent Pro");
        french.put("dashboard", "Tableau de Bord");
        french.put("building.management", "Gestion des Bâtiments");
        french.put("buyer.management", "Gestion des Acheteurs");
        french.put("land.management", "Gestion des Terrains");
        french.put("permits", "Permis");
        french.put("maintenance", "Maintenance");
        french.put("reports", "Rapports");
        french.put("requests", "Demandes");
        french.put("settings", "Paramètres");
        french.put("logout", "Déconnexion");
        
        // Settings page in French
        french.put("settings.title", "Paramètres");
        french.put("settings.subtitle", "Gérez vos préférences et configurations d'application");
        french.put("general.settings", "Paramètres Généraux");
        french.put("language", "Langue");
        french.put("language.desc", "Choisissez votre langue préférée");
        french.put("currency", "Devise");
        french.put("currency.desc", "Devise par défaut pour les calculs financiers");
        french.put("timezone", "Fuseau Horaire");
        french.put("timezone.desc", "Votre fuseau horaire local");
        french.put("notifications", "Notifications");
        french.put("email.notifications", "Notifications Email");
        french.put("email.notifications.desc", "Recevoir des notifications par email");
        french.put("push.notifications", "Notifications Push");
        french.put("push.notifications.desc", "Afficher les notifications de bureau");
        french.put("display.appearance", "Affichage et Apparence");
        french.put("theme", "Thème");
        french.put("theme.desc", "Choisissez votre thème préféré");
        french.put("font.size", "Taille de Police");
        french.put("font.size.desc", "Ajustez la taille du texte pour une meilleure lisibilité");
        french.put("security.privacy", "Sécurité et Confidentialité");
        french.put("auto.lock", "Verrouillage Automatique");
        french.put("auto.lock.desc", "Verrouiller automatiquement après inactivité");
        french.put("data.management", "Gestion des Données");
        french.put("auto.save", "Sauvegarde Automatique");
        french.put("auto.save.desc", "Sauvegarder automatiquement les modifications");
        french.put("system", "Système");
        french.put("performance.mode", "Mode Performance");
        french.put("performance.mode.desc", "Optimiser pour de meilleures performances");
        
        // Arabic translations
        Map<String, String> arabic = new HashMap<>();
        arabic.put("app.title", "مدير العقارات الذكي المحترف");
        arabic.put("dashboard", "لوحة التحكم");
        arabic.put("building.management", "إدارة المباني");
        arabic.put("buyer.management", "إدارة المشترين");
        arabic.put("land.management", "إدارة الأراضي");
        arabic.put("permits", "التصاريح");
        arabic.put("maintenance", "الصيانة");
        arabic.put("reports", "التقارير");
        arabic.put("requests", "الطلبات");
        arabic.put("settings", "الإعدادات");
        arabic.put("logout", "تسجيل الخروج");
        
        // Settings page in Arabic
        arabic.put("settings.title", "الإعدادات");
        arabic.put("settings.subtitle", "إدارة تفضيلات التطبيق والتكوينات");
        arabic.put("general.settings", "الإعدادات العامة");
        arabic.put("language", "اللغة");
        arabic.put("language.desc", "اختر لغتك المفضلة");
        arabic.put("currency", "العملة");
        arabic.put("currency.desc", "العملة الافتراضية للحسابات المالية");
        arabic.put("timezone", "المنطقة الزمنية");
        arabic.put("timezone.desc", "منطقتك الزمنية المحلية");
        arabic.put("notifications", "الإشعارات");
        arabic.put("email.notifications", "إشعارات البريد الإلكتروني");
        arabic.put("email.notifications.desc", "تلقي الإشعارات عبر البريد الإلكتروني");
        arabic.put("push.notifications", "الإشعارات المنبثقة");
        arabic.put("push.notifications.desc", "عرض إشعارات سطح المكتب");
        arabic.put("display.appearance", "العرض والمظهر");
        arabic.put("theme", "السمة");
        arabic.put("theme.desc", "اختر سمتك المفضلة");
        arabic.put("font.size", "حجم الخط");
        arabic.put("font.size.desc", "ضبط حجم النص لقراءة أفضل");
        arabic.put("security.privacy", "الأمان والخصوصية");
        arabic.put("auto.lock", "القفل التلقائي");
        arabic.put("auto.lock.desc", "القفل التلقائي بعد عدم النشاط");
        arabic.put("data.management", "إدارة البيانات");
        arabic.put("auto.save", "الحفظ التلقائي");
        arabic.put("auto.save.desc", "حفظ التغييرات تلقائياً");
        arabic.put("system", "النظام");
        arabic.put("performance.mode", "وضع الأداء");
        arabic.put("performance.mode.desc", "تحسين للحصول على أداء أفضل");
        
        // Spanish translations
        Map<String, String> spanish = new HashMap<>();
        spanish.put("app.title", "Gestor de Propiedades Inteligente Pro");
        spanish.put("dashboard", "Panel de Control");
        spanish.put("building.management", "Gestión de Edificios");
        spanish.put("buyer.management", "Gestión de Compradores");
        spanish.put("land.management", "Gestión de Terrenos");
        spanish.put("permits", "Permisos");
        spanish.put("maintenance", "Mantenimiento");
        spanish.put("reports", "Informes");
        spanish.put("requests", "Solicitudes");
        spanish.put("settings", "Configuración");
        spanish.put("logout", "Cerrar Sesión");
        
        // Settings page in Spanish
        spanish.put("settings.title", "Configuración");
        spanish.put("settings.subtitle", "Gestiona tus preferencias y configuraciones de la aplicación");
        spanish.put("general.settings", "Configuración General");
        spanish.put("language", "Idioma");
        spanish.put("language.desc", "Elige tu idioma preferido");
        spanish.put("currency", "Moneda");
        spanish.put("currency.desc", "Moneda predeterminada para cálculos financieros");
        spanish.put("timezone", "Zona Horaria");
        spanish.put("timezone.desc", "Tu zona horaria local");
        spanish.put("notifications", "Notificaciones");
        spanish.put("email.notifications", "Notificaciones por Email");
        spanish.put("email.notifications.desc", "Recibir notificaciones por correo electrónico");
        spanish.put("push.notifications", "Notificaciones Push");
        spanish.put("push.notifications.desc", "Mostrar notificaciones de escritorio");
        spanish.put("display.appearance", "Pantalla y Apariencia");
        spanish.put("theme", "Tema");
        spanish.put("theme.desc", "Elige tu tema preferido");
        spanish.put("font.size", "Tamaño de Fuente");
        spanish.put("font.size.desc", "Ajustar el tamaño del texto para mejor legibilidad");
        spanish.put("security.privacy", "Seguridad y Privacidad");
        spanish.put("auto.lock", "Bloqueo Automático");
        spanish.put("auto.lock.desc", "Bloquear automáticamente después de inactividad");
        spanish.put("data.management", "Gestión de Datos");
        spanish.put("auto.save", "Guardado Automático");
        spanish.put("auto.save.desc", "Guardar cambios automáticamente");
        spanish.put("system", "Sistema");
        spanish.put("performance.mode", "Modo de Rendimiento");
        spanish.put("performance.mode.desc", "Optimizar para mejor rendimiento");
        
        translations.put("English", english);
        translations.put("French", french);
        translations.put("Français", french);
        translations.put("Arabic", arabic);
        translations.put("العربية", arabic);
        translations.put("Spanish", spanish);
        translations.put("Español", spanish);
    }
    
    public static String getText(String key) {
        Map<String, String> currentTranslations = translations.get(currentLanguage);
        if (currentTranslations != null && currentTranslations.containsKey(key)) {
            return currentTranslations.get(key);
        }
        
        // Fallback to English if translation not found
        Map<String, String> englishTranslations = translations.get("English");
        if (englishTranslations != null && englishTranslations.containsKey(key)) {
            return englishTranslations.get(key);
        }
        
        // Return key if no translation found
        return key;
    }
    
    public static void setLanguage(String language) {
        if (translations.containsKey(language)) {
            currentLanguage = language;
        }
    }
    
    public static String getCurrentLanguage() {
        return currentLanguage;
    }
    
    public static String[] getAvailableLanguages() {
        return new String[]{"English", "Français", "العربية", "Español"};
    }
    
    public static boolean isRTL() {
        return "Arabic".equals(currentLanguage) || "العربية".equals(currentLanguage);
    }
}