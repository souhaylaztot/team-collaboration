module smartpropertymanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Export base package
    exports com.smartpropertymanager;
    opens com.smartpropertymanager to javafx.fxml;

    // Export and open components package
    exports com.smartpropertymanager.components;
    opens com.smartpropertymanager.components to javafx.fxml;

    // Export controllers ONLY if you will use FXML controllers
    exports com.smartpropertymanager.controllers;
    opens com.smartpropertymanager.controllers to javafx.fxml;
}
