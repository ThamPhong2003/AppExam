module com.example.demo4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires mysql.connector.java;

    opens com.example.demo4 to javafx.fxml;
    exports com.example.demo4;
    requires javafx.base;
    requires javafx.graphics;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    // add icon pack modules
}