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

import java.util.List;


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

    public void setStage(Stage detailStage) {
        this.stage = detailStage;
    }

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

    @FXML
    public void onSave(ActionEvent actionEvent) {
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
                e.printStackTrace();
            } finally {
                em.close();
            }
            stage.close();
            controller.loadPonory();
        }
    }

    @FXML
    public void onCancel(ActionEvent actionEvent) {
        if (stage != null) {
            stage.close();
            controller.loadPonory();
        }
    }

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void setPonorController(PonorController ponorController) {
        this.controller = ponorController;
    }
}
