package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.dao.LokalitaDao;
import cz.vse.logbookapp.dao.PonorDao;
import cz.vse.logbookapp.model.Ponor;
import cz.vse.logbookapp.model.Uzivatel;
import cz.vse.logbookapp.model.Lokalita;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;

/**
 * Controller for inserting a new Ponor (Dive) entry in the application.
 * This controller handles the UI interactions for inserting a new Ponor entry,
 * including validation and saving the entry to the database.
 */
public class InsertPonorController {

    private static final Logger log = LoggerFactory.getLogger(InsertPonorController.class);

    @FXML
    public ComboBox<String> lokalitaComboBox;

    @FXML
    public TextField depthTextField;

    @FXML
    public DatePicker dateField;

    @FXML
    public TextField durationTextField;

    @FXML
    public TextField waterTempTextField;

    @FXML
    public TextField notesTextField;

    @FXML
    public Button cancelButton;

    @FXML
    public Button saveButton;

    private Stage stage;

    private PonorController ponorController;

    private Uzivatel uzivatel;
    private PonorDao ponorDao;
    private LokalitaDao lokalitaDao;

    /**
     * Sets the stage for this controller.
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller after the FXML has been loaded.
     */
    public void postInit() {
        log.debug("Initializing InsertPonorController");
        lokalitaComboBox.setItems(FXCollections.observableArrayList(lokalitaDao.findAllMap().keySet()));
        dateField.setValue(LocalDate.now());
    }


    /**
     * Retrieves the selected Lokalita from the ComboBox.
     * This method fetches the Lokalita object based on the selected name in the ComboBox.
     *
     * @return Lokalita object corresponding to the selected name, or null if no selection is made
     */
    public Lokalita getLokalita() {
        String selectedLokalita = lokalitaComboBox.getSelectionModel().getSelectedItem();
        return lokalitaDao.findByNazev(selectedLokalita);
    }

    /**
     * Handles the submission of the new Ponor entry.
     * This method validates the input fields, creates a new Ponor object, and saves it to the database.
     * If validation fails, it highlights the respective fields in red and logs an error message.
     */
    @FXML
    private void onSubmit() {

        Lokalita lokalita;
        if (lokalitaDao.findByNazev(lokalitaComboBox.getValue()) == null) {
            lokalitaComboBox.setStyle("-fx-border-color: red;");
            log.error("Lokalita must be selected");
            return;
        } else {
            lokalitaComboBox.setStyle("-fx-border-color: none;");
            lokalita = lokalitaDao.findByNazev(lokalitaComboBox.getValue());
        }

        double hloubka;
        try {
            hloubka = Double.parseDouble(depthTextField.getText());
            if (hloubka <= 0) throw new NumberFormatException();
            depthTextField.setStyle("-fx-border-color: none;");
        } catch (NumberFormatException e) {
            depthTextField.setStyle("-fx-border-color: red;");
            log.error("Hloubka must not be empty and must be a positive number");
            return;
        }

        LocalDate datum;
        if (dateField.getValue() == null) {
            dateField.setStyle("-fx-border-color: red;");
            log.error("Datum must not be empty");
            return;
        } else {
            dateField.setStyle("-fx-border-color: none;");
            datum = dateField.getValue();
        }

        int doba;
        try {
            doba = Integer.parseInt(durationTextField.getText());
            if (doba <= 0) throw new NumberFormatException();
            durationTextField.setStyle("-fx-border-color: none;");
        } catch (NumberFormatException e) {
            durationTextField.setStyle("-fx-border-color: red;");
            log.error("Doba must not be empty and must be a positive integer");
            return;
        }

        double teplotaVody;
        try {
            teplotaVody = Double.parseDouble(waterTempTextField.getText());
            if (teplotaVody < 0) throw new NumberFormatException();
            waterTempTextField.setStyle("-fx-border-color: none;");
        } catch (NumberFormatException e) {
            waterTempTextField.setStyle("-fx-border-color: red;");
            log.error("Teplota vody must not be empty and must be a non-negative number");
            return;
        }

        String poznamka;
        if (notesTextField.getText().isEmpty()) {
            notesTextField.setStyle("-fx-border-color: red;");
            log.error("Poznamka must not be empty");
            return;
        } else {
            notesTextField.setStyle("-fx-border-color: none;");
            poznamka = notesTextField.getText();
        }

        Ponor newPonor = new Ponor();
        newPonor.setLokalita(lokalita);
        newPonor.setDatum(datum);
        newPonor.setHloubka(hloubka);
        newPonor.setDoba(doba);
        newPonor.setTeplotaVody(teplotaVody);
        newPonor.setPoznamka(poznamka);
        newPonor.setUzivatel(uzivatel);
        newPonor.setLokalita(lokalita);

        ponorDao.save(newPonor);

        stage.close();
        ponorController.loadPonory();
    }

    /**
     * Handles the cancellation of the Ponor insertion.
     * This method closes the popup without saving any changes.
     */
    @FXML
    private void onCancel() {
        log.info("Insert Ponor cancelled");
        if (stage != null) {
            stage.close(); // Close the popup without saving
        }
    }

    /**
     * Sets the PonorController for this controller.
     * This allows the InsertPonorController to interact with the PonorController.
     *
     * @param ponorController The PonorController to set
     */
    public void setPonorController(PonorController ponorController) {
        this.ponorController = ponorController;
    }

    /**
     * Sets the Uzivatel (User) for this controller.
     * This method is used to inject the current user into the controller,
     * allowing the Ponor entry to be associated with the user.
     *
     * @param uzivatel The Uzivatel instance representing the current user
     */
    public void setUzivatel(Uzivatel uzivatel) {
        log.debug("Setting Uzivatel in InsertPonorController: {}", uzivatel.getEmail());
        this.uzivatel = uzivatel;
    }

    public void setPonorDao(PonorDao ponorDao) {
        this.ponorDao = ponorDao;
    }

    public void setLokalitaDao(LokalitaDao lokalitaDao) {
        this.lokalitaDao = lokalitaDao;
    }
}