package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Ponor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for displaying details of a Ponor (Dive).
 * This controller is responsible for showing the details of a selected Ponor
 * in a separate window.
 */
public class PonorDetailController {

    @FXML
    private Label datumLabel;

    @FXML
    private Label lokalitaLabel;

    @FXML
    private Label hloubkaLabel;

    @FXML
    private Label dobaLabel;

    @FXML
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
     * Sets the Ponor details in the UI.
     *
     * @param ponor The Ponor object containing the details to display.
     */
    public void setPonor(Ponor ponor) {
        datumLabel.setText("Date: " + ponor.getDatum());
        lokalitaLabel.setText("Dive site: " + ponor.getLokalita().getNazev());
        hloubkaLabel.setText("Depth: " + ponor.getHloubka() + " m");
        dobaLabel.setText("Duration: " + ponor.getDoba() + " min");
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