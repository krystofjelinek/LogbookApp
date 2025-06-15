package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Ponor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PonorDetailController {

    @FXML
    private Label datumLabel;

    @FXML
    private Label lokalitaLabel;

    @FXML
    private Label hloubkaLabel;

    @FXML
    private Label dobaLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPonor(Ponor ponor) {
        datumLabel.setText("Date: " + ponor.getDatum());
        lokalitaLabel.setText("Dive site: " + ponor.getLokalita().getNazev());
        hloubkaLabel.setText("Depth: " + ponor.getHloubka() + " m");
        dobaLabel.setText("Duration: " + ponor.getDoba() + " min");
    }

    @FXML
    private void onCloseButtonClick() {
        if (stage != null) {
            stage.close();
        }
    }


}