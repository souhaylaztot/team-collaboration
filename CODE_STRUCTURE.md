# Code Structure Overview

## Main Application Files (26 Java Files)

### Core Application
- `MainApp.java` - Main application entry point with theme system
- `LoginPage.java` - Authentication system with user roles
- `DatabaseManager.java` - PostgreSQL connection management

### UI Pages
- `DashboardPage.java` - Analytics dashboard with charts
- `BuildingsPage.java` - Property management with CRUD operations
- `BuyersPage.java` - Customer management with payment tracking
- `LandsPage.java` - Land property management
- `PermitsPage.java` - Construction permit workflow
- `MaintenancePage.java` - Maintenance request system
- `RequestsPage.java` - Customer support with messaging
- `TransportationPage.java` - Vehicle and logistics management
- `ReportsPage.java` - Analytics and reporting
- `NotificationCenter.java` - System notifications
- `SettingsPage.java` - Application settings
- `AdminProfilePage.java` - Admin user profile
- `UserProfilePage.java` - Regular user profile

### UI Components
- `Sidebar.java` - Navigation sidebar with theme support
- `TopBar.java` - Top navigation with search and theme toggle
- `ThemeManager.java` - Dark/Light theme management system
- `StatCard.java` - Reusable statistics card component

### Data Models
- `Building.java` - Building entity with apartments list
- `Apartment.java` - Apartment/unit entity
- `Buyer.java` - Customer entity with JavaFX properties
- `Land.java` - Land property entity

### Utility Classes
- `CreateAccountPage.java` - User registration
- `TestApp.java` - Testing utilities

## Database Schema
- `database_schema.sql` - Complete PostgreSQL schema with 15+ tables

## Key Technical Features

### 1. Theme System (`ThemeManager.java`)
```java
public class ThemeManager {
    private static boolean isDarkMode = false;
    
    public static String getBackground() {
        return isDarkMode ? "#0B141A" : "#ffffff";
    }
    
    public static void toggleTheme() {
        isDarkMode = !isDarkMode;
    }
}
```

### 2. Database Connection (`DatabaseManager.java`)
```java
public class DatabaseManager {
    private static HikariDataSource dataSource;
    
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/property_manager");
        config.setUsername("postgres");
        config.setPassword("4426");
        dataSource = new HikariDataSource(config);
    }
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
```

### 3. JavaFX Properties (`Buyer.java`)
```java
public class Buyer {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty purchaseAmount;
    
    // Property getters for TableView binding
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
}
```

### 4. CRUD Operations Example (`BuildingsPage.java`)
- Create: Add new buildings with validation
- Read: Display buildings in responsive grid
- Update: Edit building information
- Delete: Remove buildings with confirmation

### 5. Advanced UI Components
- **TableView with custom columns** (BuyersPage)
- **Charts and analytics** (DashboardPage, ReportsPage)
- **Responsive layouts** (ScrollPane + TilePane)
- **Custom dialogs** (Add/Edit forms)
- **Real-time search and filtering**

## Architecture Patterns

### MVC Pattern
- **Model**: Data classes (Building, Buyer, Land, etc.)
- **View**: JavaFX UI pages and components
- **Controller**: Event handlers and business logic

### Singleton Pattern
- `ThemeManager` - Global theme state
- `DatabaseManager` - Connection pool management

### Observer Pattern
- Theme changes notify all components
- Property bindings for real-time UI updates

## Code Quality Metrics
- **26 Java classes** - Well-organized structure
- **15+ database tables** - Comprehensive data model
- **CRUD operations** - Full data management
- **Error handling** - Try-catch blocks throughout
- **Responsive UI** - Adaptive layouts
- **Theme system** - Professional dark/light modes