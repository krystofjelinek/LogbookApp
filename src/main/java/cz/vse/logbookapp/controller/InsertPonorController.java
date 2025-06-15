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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void postInit() {
        log.info("Initializing InsertPonorController");
        lokalitaComboBox.setItems(getLokalitaList());
        dateField.setValue(LocalDate.now());
    }

    public ObservableList<String> getLokalitaList() {
        EntityManager em = emf.createEntityManager();
        ObservableList<String> lokalitaNazvy = FXCollections.observableArrayList(
                em.createQuery("SELECT l.nazev FROM Lokalita l", String.class).getResultList()
        );
        em.close();
        return lokalitaNazvy;
    }

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

    @FXML
    private void onCancel() {
        log.info("Insert Ponor cancelled");
        if (stage != null) {
            stage.close(); // Close the popup without saving
        }
    }

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void setPonorController(PonorController ponorController) {
        this.ponorController = ponorController;
    }

    public void setUzivatel(Uzivatel uzivatel) {
        log.debug("Setting Uzivatel in InsertPonorController: {}", uzivatel.getEmail());
        this.uzivatel = uzivatel;
    }
}