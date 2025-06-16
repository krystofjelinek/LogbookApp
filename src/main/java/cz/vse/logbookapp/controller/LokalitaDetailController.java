package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Lokalita;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for displaying details of a Lokalita (Dive Site).
 * This controller is responsible for showing the details of a selected Lokalita
 * in a separate window.
 */
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

    /**
     * Sets the stage for this controller.
     *
     * @param stage The Stage object to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the Lokalita details in the UI.
     *
     * @param lokalita The Lokalita object containing the details to display.
     */
    public void setLokalita(Lokalita lokalita) {
        nazevLabel.setText(lokalita.getNazev());
        popisLabel.setText("Description: " + lokalita.getPopis());
        hloubkaLabel.setText("Max depth: " + lokalita.getMaxHloubka() + " m");
        typLabel.setText("Type: " + lokalita.getTypLokality());
        zemeLabel.setText("Country: " + lokalita.getZeme());

    }

    /**
     * Handles the close button click event.
     * Closes the stage if it is not null.
     */
    @FXML
    private void onCloseButtonClick() {
        if (stage != null) {
            stage.close();
        }
    }
}