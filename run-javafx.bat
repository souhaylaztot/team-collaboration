@echo off
echo Compiling JavaFX Application...

javac --module-path "lib" --add-modules javafx.controls,javafx.fxml -d "target/classes" -cp "src/main/java" src/main/java/com/propertymanager/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Running JavaFX Application...
    java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "target/classes" com.propertymanager.RequestCenter
) else (
    echo Compilation failed!
    pause
)