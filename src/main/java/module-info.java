module org.example.appbancariajavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.appbancariajavafx to javafx.fxml;
    exports org.example.appbancariajavafx;
}