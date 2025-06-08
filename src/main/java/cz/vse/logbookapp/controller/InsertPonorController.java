package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Ponor;
import cz.vse.logbookapp.model.Uzivatel;
import cz.vse.logbookapp.model.Lokalita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class InsertPonorController {

    private static final Logger log = LoggerFactory.getLogger(InsertPonorController.class);

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

    @FXML


    private EntityManagerFactory emf;

    private Stage stage;
    private PonorController ponorController;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onSubmit() {
        String datumText = dateField.getValue().toString();
        String hloubkaText = depthTextField.getText();
        String dobaText = durationTextField.getText();
        String teplotaVodyText = waterTempTextField.getText();
        String poznamkaText = notesTextField.getText();

        try {
            LocalDate datum = LocalDate.parse(datumText);
            Double hloubka = Double.parseDouble(hloubkaText);
            Integer doba = Integer.parseInt(dobaText);
            Double teplotaVody = teplotaVodyText.isEmpty() ? null : Double.parseDouble(teplotaVodyText);

            EntityManager em = emf.createEntityManager();
            EntityTransaction transaction = em.getTransaction();

            transaction.begin();

            Ponor newPonor = new Ponor();
            newPonor.setDatum(datum);
            newPonor.setHloubka(hloubka);
            newPonor.setDoba(doba);
            newPonor.setTeplotaVody(teplotaVody);
            newPonor.setPoznamka(poznamkaText);

            // Set default Uzivatel and Lokalita (replace with actual logic)
            Uzivatel uzivatel = em.find(Uzivatel.class, 1L);
            Lokalita lokalita = em.find(Lokalita.class, 1L);
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
}