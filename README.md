# Property Manager - JavaFX Application

## Setup Instructions

### Prerequisites
- Java 17 or higher
- JavaFX SDK 17.0.2

### Installation
1. Download JavaFX SDK from https://gluonhq.com/products/javafx/
2. Extract JavaFX libraries to `lib/` folder
3. Compile and run using the provided batch files

### Running the Application
```cmd
run-javafx.bat
```

Or using Maven:
```cmd
mvn clean javafx:run
```

## Project Structure
- `src/main/java/com/propertymanager/` - Main application source code
- `lib/` - JavaFX runtime libraries
- `pom.xml` - Maven configuration

## Features
- Property management system
- JavaFX-based user interface
- Login system
- Dashboard with statistics
- Multiple management pages (Buildings, Buyers, Lands, etc.)