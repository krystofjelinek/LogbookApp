package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Lokalita;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LokalitaDetailController {

    @FXML
    private Label nazevLabel;

    @FXML
    private Label popisLabel;

    @FXML
    private Label typLabel;

    @FXML
    private Label hloubkaLabel;

    @FXML
    private Label zemeLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLokalita(Lokalita lokalita) {
        nazevLabel.setText(lokalita.getNazev());
        popisLabel.setText("Description: " + lokalita.getPopis());
        hloubkaLabel.setText("Max depth: " + lokalita.getMaxHloubka() + " m");
        typLabel.setText("Type: " + lokalita.getTypLokality());
        zemeLabel.setText("Country: " + lokalita.getZeme());

    }

    @FXML
    private void onCloseButtonClick() {
        if (stage != null) {
            stage.close();
        }
    }
}