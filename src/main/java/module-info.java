module com.genvetclinic {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.genvetclinic.controllers;
    opens com.genvetclinic.models to javafx.base;

    exports com.genvetclinic;
    exports com.genvetclinic.controllers;
}
