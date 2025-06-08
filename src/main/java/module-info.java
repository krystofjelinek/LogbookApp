module cz.vse.logbookapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.slf4j;
    requires static lombok;

    opens cz.vse.logbookapp to javafx.fxml;
    opens cz.vse.logbookapp.model to org.hibernate.orm.core;
    exports cz.vse.logbookapp;
    exports cz.vse.logbookapp.controller;
    exports  cz.vse.logbookapp.model;
    opens cz.vse.logbookapp.controller to javafx.fxml;
}