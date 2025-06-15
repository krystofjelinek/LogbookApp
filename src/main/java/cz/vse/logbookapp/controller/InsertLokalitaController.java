package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Lokalita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertLokalitaController {

    @FXML
    public TextField nazevField;

    @FXML
    public ComboBox<String> zemeField;

    @FXML
    public TextField hloubkaField;

    @FXML
    public ComboBox<String> typField;

    @FXML
    public TextArea popisField;

    @FXML
    public Button saveButton;

    @FXML
    public Button cancelButton;

    private EntityManagerFactory emf;

    private PonorController controller;

    private Stage stage;

    private static final Logger log = LoggerFactory.getLogger(InsertLokalitaController.class);

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void setPonorController(PonorController ponorController) {
        this.controller = ponorController;
    }

    public void initialize() {
        // Initialize the ComboBoxes with values
        zemeField.getItems().addAll("Czechia", "Slovakia", "Poland", "Germany", "Austria", "Hungary", "Italy", "France", "Spain", "Other");
        typField.getItems().addAll("Sea", "Lake", "River", "Cave", "Other");

        // Set default values if needed
        zemeField.setValue("Czechia");
        typField.setValue("Lake");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onSave(ActionEvent actionEvent) {

        String nazev;
        if (nazevField.getText().isEmpty()) {
            nazevField.setStyle("-fx-border-color: red;");
            log.error("Název lokality nesmí být prázdný");
            return;
        } else {
            nazevField.setStyle("-fx-border-color: none;");
            nazev = nazevField.getText();
        }

        String zeme;
        if (zemeField.getValue().isEmpty()) {
            zemeField.setStyle("-fx-border-color: red;");
            log.error("Země lokality musí být vybrána");
            return;
        } else {
            zemeField.setStyle("-fx-border-color: none;");
            zeme = zemeField.getValue();
        }

        String hloubka;
        if (hloubkaField.getText().isEmpty() || Double.parseDouble(hloubkaField.getText()) <= 0) {
            hloubkaField.setStyle("-fx-border-color: red;");
            log.error("Hloubka lokality musí být větší než 0");
            return;
        } else {
            hloubkaField.setStyle("-fx-border-color: none;");
            hloubka = hloubkaField.getText();
        }

        String typ;
        if (typField.getValue() == null) {
            typField.setStyle("-fx-border-color: red;");
            log.error("Typ lokality musí být vybrán");
            return;
        } else {
            typField.setStyle("-fx-border-color: none;");
            typ = typField.getValue();
        }

        String popis;
        if (popisField.getText().isEmpty()) {
            popisField.setStyle("-fx-border-color: red;");
            log.error("Popis lokality nesmí být prázdný");
            return;
        } else {
            popisField.setStyle("-fx-border-color: none;");
            popis = popisField.getText();
        }

        log.info("Saving new Lokalita: {}, {}, {}, {}, {}", nazev, zeme, hloubka, typ, popis);
        // Here you would typically save the new location to the database
        // For example:
        Lokalita lokalita = new Lokalita(nazev, zeme, Double.parseDouble(hloubka), typ, popis);
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(lokalita);
        transaction.commit();
        log.info("Lokalita saved successfully: {}", lokalita);
        em.close();
        // After saving, you might want to update the controller or close the stage
        if (stage != null) {
            stage.close();
            log.debug("InsertLokalita popup closed after saving");
        }
        if (controller != null) {
            controller.onLokalityButtonClick(new ActionEvent());
            // Assuming this method refreshes the list of locations
            log.debug("Lokalita list refreshed in PonorController after saving new Lokalita");
        }
    }

    public void onCancel(ActionEvent actionEvent) {
        if (stage != null) {
            stage.close(); // Close the popup without saving
            log.debug("Cancel button clicked, closing InsertLokalita popup");
        }
    }
}
