module se2203b.iGlobal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens se2203b.iGlobal to javafx.fxml;
    exports se2203b.iGlobal;
}