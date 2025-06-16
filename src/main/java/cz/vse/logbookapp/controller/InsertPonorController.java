package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Ponor;
import cz.vse.logbookapp.model.Uzivatel;
import cz.vse.logbookapp.model.Lokalita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private EntityManagerFactory emf;

    private Stage stage;

    private PonorController ponorController;

    private Uzivatel uzivatel;

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
        lokalitaComboBox.setItems(getLokalitaList());
        dateField.setValue(LocalDate.now());
    }

    /**
     * Retrieves the list of Lokalita names from the database.
     * This method queries the database for all Lokalita entries and returns their names as an ObservableList.
     *
     * @return ObservableList of Lokalita names
     */
    public ObservableList<String> getLokalitaList() {
        EntityManager em = emf.createEntityManager();
        ObservableList<String> lokalitaNazvy = FXCollections.observableArrayList(
                em.createQuery("SELECT l.nazev FROM Lokalita l", String.class).getResultList()
        );
        em.close();
        return lokalitaNazvy;
    }

    /**
     * Retrieves the selected Lokalita from the ComboBox.
     * This method fetches the Lokalita object based on the selected name in the ComboBox.
     *
     * @return Lokalita object corresponding to the selected name, or null if no selection is made
     */
    public Lokalita getLokalita() {
        String selectedLokalita = lokalitaComboBox.getSelectionModel().getSelectedItem();
        if (selectedLokalita != null) {
            EntityManager em = emf.createEntityManager();
            Lokalita lokalita = em.createQuery("SELECT l FROM Lokalita l WHERE l.nazev = :nazev", Lokalita.class)
                    .setParameter("nazev", selectedLokalita)
                    .getSingleResult();
            em.close();
            return lokalita;
        }
        return null;
    }

    /**
     * Handles the submission of the new Ponor entry.
     * This method validates the input fields, creates a new Ponor object, and saves it to the database.
     * If validation fails, it highlights the respective fields in red and logs an error message.
     */
    @FXML
    private void onSubmit() {

        //Add validation for required fields
        Lokalita lokalita;
        if (getLokalita() == null) {
            lokalitaComboBox.setStyle("-fx-border-color: red;");
            log.error("Lokalita must be selected");
            return;
        } else {
            lokalitaComboBox.setStyle("-fx-border-color: none;");
            lokalita = getLokalita();
        }

        double hloubka;
        if (depthTextField.getText().isEmpty() || Double.parseDouble(depthTextField.getText()) <= 0) {
            depthTextField.setStyle("-fx-border-color: red;");
            log.error("Hloubka must not be empty");
            return;
        } else {
            depthTextField.setStyle("-fx-border-color: none;");
            hloubka = Double.parseDouble(depthTextField.getText());
        }

        LocalDate datum;
        if (dateField.getValue() == null) {
            dateField.setStyle("-fx-border-color: red;");
            log.error("Datum must not be empty");
            return;
        } else {
            dateField.setStyle("-fx-border-color: none;");
            datum = LocalDate.parse(dateField.getValue().toString());
        }

        int doba;
        if (durationTextField.getText().isEmpty() || Integer.parseInt(durationTextField.getText()) <= 0) {
            durationTextField.setStyle("-fx-border-color: red;");
            log.error("Doba must not be empty and must be greater than 0");
            return;
        } else {
            durationTextField.setStyle("-fx-border-color: none;");
            doba = Integer.parseInt(durationTextField.getText());
        }

        double teplotaVody;
        if (waterTempTextField.getText().isEmpty()) {
            waterTempTextField.setStyle("-fx-border-color: red;");
            log.error("Teplota vody must not be empty");
            return;
        } else {
            waterTempTextField.setStyle("-fx-border-color: none;");
            teplotaVody = Double.parseDouble(waterTempTextField.getText());
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

        try {

            EntityManager em = emf.createEntityManager();
            EntityTransaction transaction = em.getTransaction();

            transaction.begin();

            Ponor newPonor = new Ponor();
            newPonor.setLokalita(lokalita);
            newPonor.setDatum(datum);
            newPonor.setHloubka(hloubka);
            newPonor.setDoba(doba);
            newPonor.setTeplotaVody(teplotaVody);
            newPonor.setPoznamka(poznamka);
            newPonor.setUzivatel(uzivatel);
            newPonor.setLokalita(lokalita);

            em.persist(newPonor);

            transaction.commit();
            em.close();

            log.info("New Ponor added successfully: {}", newPonor);
            stage.close(); // Close the popup
            ponorController.loadPonory(); // Refresh the list in PonorController
        } catch (Exception e) {
            log.error("Failed to add new Ponor", e);
        }
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
     * Sets the EntityManagerFactory for this controller.
     * This method is used to inject the EntityManagerFactory dependency.
     *
     * @param emf EntityManagerFactory instance
     */
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
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
}