module com.example.csia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.csia to javafx.fxml;
    exports com.example.csia;
}