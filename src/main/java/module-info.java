module com.example.demo4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires mysql.connector.java;
    requires org.kordamp.ikonli.fontawesome5;
    opens com.example.demo4 to javafx.fxml;
    exports com.example.demo4;
    requires javafx.base;
    requires javafx.graphics;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires io;
    requires kernel;
    requires layout;
    // add icon pack modules
}