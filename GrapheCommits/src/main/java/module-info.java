module com.example.graphecommits {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.graphecommits to javafx.fxml;
    exports com.example.graphecommits;
}