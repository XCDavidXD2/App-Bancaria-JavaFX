module org.example.javafx_proyectobancario_david_carlos {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.javafx_proyectobancario_david_carlos to javafx.fxml;
    exports org.example.javafx_proyectobancario_david_carlos;
}