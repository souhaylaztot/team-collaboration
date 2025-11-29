@echo off
echo Compiling Login Application...

javac --module-path "lib" --add-modules javafx.controls,javafx.fxml -d "target/classes" -cp "src/main/java" src/main/java/com/propertymanager/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Starting Login Page...
    java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "target/classes" -Djava.library.path=lib com.propertymanager.LoginPage
) else (
    echo Compilation failed!
    pause
)