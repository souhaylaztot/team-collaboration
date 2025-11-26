# Smart Property Manager Pro - JavaFX Application

A professional property management dashboard built with JavaFX.

## Features

- **Modern Dashboard UI** - Professional sidebar navigation and main content area
- **Stat Cards** - Display key metrics with trends
- **Charts** - Revenue & Expenses bar chart and Occupancy Rate visualization
- **Responsive Layout** - Adapts to different screen sizes
- **User-Friendly Interface** - Intuitive navigation with badges for notifications

## Project Structure

\`\`\`
src/main/java/com/smartpropertymanager/
├── Main.java                      # Main application entry point
└── components/
    ├── Sidebar.java              # Navigation sidebar component
    ├── DashboardContent.java      # Main dashboard content
    └── StatCard.java             # Reusable stat card component
\`\`\`

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## Installation & Running

1. **Clone/Extract the project**
   \`\`\`bash
   cd smart-property-manager
   \`\`\`

2. **Build the project**
   \`\`\`bash
   mvn clean package
   \`\`\`

3. **Run the application**
   \`\`\`bash
   mvn javafx:run
   \`\`\`

   Or run the JAR directly:
   \`\`\`bash
   java -jar target/smart-property-manager-1.0-SNAPSHOT-shaded.jar
   \`\`\`

## Development

### Building
\`\`\`bash
mvn clean install
\`\`\`

### Dependencies
- JavaFX 21.0.1 - Modern UI framework for Java
- Maven - Build and dependency management

## Features Overview

### Dashboard
- **Stat Cards**: Display Total Buildings, Total Properties, Total Buyers, and Land Properties
- **Charts**: Revenue & Expenses bar chart and Occupancy Rate donut chart
- **Header**: Search functionality, notifications, and user profile

### Navigation
- Sidebar menu with icons and notification badges
- Quick access to all main sections
- Settings and logout options

## Customization

### Colors
Edit the hex color codes in the component files:
- Primary: `#4CAF50` (Green)
- Secondary: `#1E40AF` (Blue)
- Accent: `#10B981` (Teal), `#F59E0B` (Orange), `#8B5CF6` (Purple)

### Data
Update static data in `DashboardContent.java` to connect to a real database or API.

## License
MIT License
