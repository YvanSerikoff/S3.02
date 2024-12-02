module fr.univlille.iut.view {
    requires javafx.controls;
    requires com.opencsv;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires java.xml.crypto;

    exports fr.univlille.iut.controller;
    opens fr.univlille.iut.controller to javafx.fxml;
    opens fr.univlille.iut.model;
    exports fr.univlille.iut;
    opens fr.univlille.iut;
    exports fr.univlille.iut.model;
    exports fr.univlille.iut.view;
    opens fr.univlille.iut.view to javafx.fxml;
}
