package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Lokalita;
import cz.vse.logbookapp.model.Ponor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

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

    private EntityManagerFactory emf;

    private PonorController controller;

    private static final Logger log = LoggerFactory.getLogger(PonorEditController.class);

    /**
     * Updates the UI components with the details of the Ponor object.
     * This method sets the values of the date, location, depth, duration, etc. fields
     * @param ponor
     */
    public void setPonor(Ponor ponor) {
        dateField.setValue(ponor.getDatum());
        lokalitaComboBox.setValue(ponor.getLokalita().getNazev());
        lokalitaComboBox.setItems(FXCollections.observableArrayList(getLokalitaList().keySet()));
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
     * Retrieves a list of Lokalita (Dive Sites) from the database and returns them as an ObservableMap.
     * The keys of the map are the names of the Lokalita, and the values are the Lokalita objects.
     * @return ObservableMap containing Lokalita names as keys and Lokalita objects as values.
     */
    public ObservableMap<String, Lokalita> getLokalitaList() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        List<Lokalita> lokalitaList = em.createQuery("SELECT l FROM Lokalita l", Lokalita.class).getResultList();
        transaction.commit();
        em.close();
        ObservableMap<String, Lokalita> lokalitaMap = FXCollections.observableHashMap();
        for (Lokalita lokalita : lokalitaList) {
            lokalitaMap.put(lokalita.getNazev(), lokalita);
        }
        return lokalitaMap;
    }

    /**
     * Retrieves a Lokalita (Dive Site) by its name from the database.
     * @param nazev The name of the Lokalita to retrieve.
     * @return The Lokalita object corresponding to the given name.
     */
    public Lokalita getLokalita(String nazev) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Lokalita lokalita = em.createQuery("SELECT l FROM Lokalita l WHERE l.nazev = :nazev", Lokalita.class)
                .setParameter("nazev", nazev)
                .getSingleResult();
        transaction.commit();
        em.close();
        return lokalita;
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
            return;
        } else {
            dateField.setStyle("-fx-border-color: none;");
        }
        if (lokalitaComboBox.getValue() == null) {
            lokalitaComboBox.setStyle("-fx-border-color: red;");
            return;
        } else {
            lokalitaComboBox.setStyle("-fx-border-color: none;");
        }
        if (depthTextField.getText().isEmpty() || Double.parseDouble(depthTextField.getText()) <= 0) {
            depthTextField.setStyle("-fx-border-color: red;");
            return;
        } else {
            depthTextField.setStyle("-fx-border-color: none;");
        }
        if (durationTextField.getText().isEmpty() || Integer.parseInt(durationTextField.getText()) <= 0) {
            durationTextField.setStyle("-fx-border-color: red;");
            return;
        } else {
            durationTextField.setStyle("-fx-border-color: none;");
        }
        if (waterTempTextField.getText().isEmpty() || Double.parseDouble(waterTempTextField.getText()) < 0) {
            waterTempTextField.setStyle("-fx-border-color: red;");
            return;
        } else {
            waterTempTextField.setStyle("-fx-border-color: none;");
        }
        if (notesTextField.getText().isEmpty()) {
            notesTextField.setStyle("-fx-border-color: red;");
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
            ponor.setLokalita(getLokalita(lokalitaComboBox.getValue()));

            // Save the updated Ponor object to the database
            EntityManager em = emf.createEntityManager();
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();
                em.merge(ponor);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Error saving Ponor: ", e);
            } finally {
                em.close();
            }
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
        }
    }

    /**
     * Sets the EntityManagerFactory for this controller.
     * This is used to create EntityManager instances for database operations.
     * @param emf The EntityManagerFactory to set.
     */
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Sets the PonorController for this controller.
     * This allows the PonorEditController to interact with the PonorController.
     * @param ponorController The PonorController to set.
     */
    public void setPonorController(PonorController ponorController) {
        this.controller = ponorController;
    }
}
