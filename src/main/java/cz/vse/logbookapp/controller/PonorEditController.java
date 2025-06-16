package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.dao.LokalitaDao;
import cz.vse.logbookapp.dao.PonorDao;
import cz.vse.logbookapp.model.Ponor;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for editing a Ponor (Dive) entry in the application.
 * This controller handles the UI interactions for editing a Ponor entry,
 * including validation and saving changes to the database.
 */
public class PonorEditController {

    @FXML
    public TextField depthTextField;

    @FXML
    public TextField durationTextField;

    @FXML
    public TextField waterTempTextField;

    @FXML
    public TextField notesTextField;

    @FXML
    public DatePicker dateField;

    @FXML
    public ComboBox<String> lokalitaComboBox;

    @FXML
    public Button save;

    @FXML
    public Button cancel;

    private Ponor ponor;

    @FXML
    private Stage stage;

    private PonorController controller;

    private static final Logger log = LoggerFactory.getLogger(PonorEditController.class);
    private PonorDao ponorDao;
    private LokalitaDao lokalitaDao;

    /**
     * Updates the UI components with the details of the Ponor object.
     * This method sets the values of the date, location, depth, duration, etc. fields
     * @param ponor
     */
    public void setPonor(Ponor ponor) {
        dateField.setValue(ponor.getDatum());
        lokalitaComboBox.setValue(ponor.getLokalita().getNazev());
        lokalitaComboBox.setItems(FXCollections.observableArrayList(lokalitaDao.findAllMap().keySet()));
        depthTextField.setText(String.valueOf(ponor.getHloubka()));
        durationTextField.setText(String.valueOf(ponor.getDoba()));
        waterTempTextField.setText(String.valueOf(ponor.getTeplotaVody()));
        notesTextField.setText(ponor.getPoznamka());
        this.ponor = ponor;
    }

    /**
     * Sets the stage for this controller.
     * @param detailStage The Stage object to set.
     */
    public void setStage(Stage detailStage) {
        this.stage = detailStage;
    }

    /**
     * Handles the save action when the user clicks the save button.
     * Validates the input fields and updates the Ponor object in the database.
     * If validation fails, it highlights the invalid fields.
     * @param actionEvent The action event triggered by clicking the save button.
     */
    @FXML
    public void onSave(ActionEvent actionEvent) {
        if (dateField.getValue() == null) {
            dateField.setStyle("-fx-border-color: red;");
            log.warn("Date field is empty");
            return;
        } else {
            dateField.setStyle("-fx-border-color: none;");
        }
        if (lokalitaComboBox.getValue() == null) {
            lokalitaComboBox.setStyle("-fx-border-color: red;");
            log.warn("Lokalita field is empty");
            return;
        } else {
            lokalitaComboBox.setStyle("-fx-border-color: none;");
        }
        if (depthTextField.getText().isEmpty() || Double.parseDouble(depthTextField.getText()) <= 0) {
            depthTextField.setStyle("-fx-border-color: red;");
            log.warn("Depth field is empty or invalid");
            return;
        } else {
            depthTextField.setStyle("-fx-border-color: none;");
        }
        if (durationTextField.getText().isEmpty() || Integer.parseInt(durationTextField.getText()) <= 0) {
            durationTextField.setStyle("-fx-border-color: red;");
            log.warn("Duration field is empty or invalid");
            return;
        } else {
            durationTextField.setStyle("-fx-border-color: none;");
        }
        if (waterTempTextField.getText().isEmpty() || Double.parseDouble(waterTempTextField.getText()) < 0) {
            waterTempTextField.setStyle("-fx-border-color: red;");
            log.warn("Water temperature field is empty or invalid");
            return;
        } else {
            waterTempTextField.setStyle("-fx-border-color: none;");
        }
        if (notesTextField.getText().isEmpty()) {
            notesTextField.setStyle("-fx-border-color: red;");
            log.warn("Notes field is empty");
            return;
        } else {
            notesTextField.setStyle("-fx-border-color: none;");
        }

        if (ponor != null) {
            ponor.setDatum(dateField.getValue());
            ponor.setHloubka(Double.parseDouble(depthTextField.getText()));
            ponor.setDoba(Integer.parseInt(durationTextField.getText()));
            ponor.setTeplotaVody(Double.parseDouble(waterTempTextField.getText()));
            ponor.setPoznamka(notesTextField.getText());
            ponor.setLokalita(lokalitaDao.findByNazev(lokalitaComboBox.getValue()));

            ponorDao.update(ponor);
            log.info("Ponor updated: {}", ponor.getDatum());

            stage.close();
            controller.loadPonory();
        }
    }

    /**
     * Handles the cancel action when the user clicks the cancel button.
     * Closes the current stage and reloads the Ponor list in the controller.
     * @param actionEvent The action event triggered by clicking the cancel button.
     */
    @FXML
    public void onCancel(ActionEvent actionEvent) {
        if (stage != null) {
            stage.close();
            controller.loadPonory();
            log.info("Ponor edit cancelled, stage closed");
        }
    }

    /**
     * Sets the PonorController for this controller.
     * This allows the PonorEditController to interact with the PonorController.
     * @param ponorController The PonorController to set.
     */
    public void setPonorController(PonorController ponorController) {
        this.controller = ponorController;
    }

    public void setPonorDao(PonorDao ponorDao) {
        this.ponorDao = ponorDao;
    }

    public void setLokalitaDao(LokalitaDao lokalitaDao) {
        this.lokalitaDao = lokalitaDao;
    }
}
