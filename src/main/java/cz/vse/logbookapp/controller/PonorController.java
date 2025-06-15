package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Lokalita;
import cz.vse.logbookapp.model.Ponor;
import cz.vse.logbookapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PonorController {

    private static final Logger log = LoggerFactory.getLogger(PonorController.class);
    @FXML
    public Button add;
    @FXML
    public Button addLokalita;

    @FXML
    public Label userNameLabel;
    @FXML
    private VBox ponorContainer;

    private EntityManagerFactory emf;
    private Uzivatel uzivatel;

    @FXML
    public void initializeData() {
        setUserNameLabel();
        loadPonory();
    }

    private void setUserNameLabel() {
        if (uzivatel != null) {
            userNameLabel.setText(uzivatel.getJmeno());
        }
    }

    void loadPonory() {
        log.info("Loading Ponory for user with ID 1");
        ponorContainer.getChildren().clear(); // Clear the container before loading new Ponors
        EntityManager em = emf.createEntityManager();
        log.debug("Loading Ponory from: {}", emf.getProperties().get("jakarta.persistence.jdbc.url"));
        EntityTransaction transaction = em.getTransaction();
        log.info("Starting transaction");
        transaction.begin();
        log.info("Fetching Ponors for user with ID 1");
        List<Ponor> ponory = em.createQuery("SELECT p FROM Ponor p WHERE p.uzivatel.id = :uzivatelId", Ponor.class)
                .setParameter("uzivatelId", uzivatel.getId())
                .getResultList();
        log.info("Fetched {} Ponors", ponory.size());
        transaction.commit();
        for (Ponor ponor : ponory) {
            log.info("Processing Ponor with ID: {}", ponor.getId());
            HBox hBox = new HBox();
            hBox.setId("ponorDetail" + ponor.getId());
            hBox.setSpacing(20);
            hBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 5;");

            log.info("Ponor: {}", ponor);
            Label datumLabel = new Label(ponor.getDatum().toString());
            Label lokalitaLabel = new Label(ponor.getLokalita().getNazev());
            Label hloubkaLabel = new Label(ponor.getHloubka() + " m");
            Label dobaLabel = new Label(ponor.getDoba() + " min");

            Button viewButton = new Button("🔍");
            Button deleteButton = new Button("🗑");
            Button editButton = new Button("✏️");

            hBox.getChildren().addAll(datumLabel, lokalitaLabel, hloubkaLabel, dobaLabel, viewButton, editButton,deleteButton);
            ponorContainer.getChildren().add(hBox);

            deleteButton.setOnAction(event -> {
                log.info("Deleting Ponor with ID: {}", ponor.getId());
                EntityManager deleteEm = emf.createEntityManager(); // Create a new EntityManager
                EntityTransaction deleteTransaction = deleteEm.getTransaction();
                try {
                    deleteTransaction.begin();
                    Ponor ponorToDelete = deleteEm.find(Ponor.class, ponor.getId()); // Fetch the entity again
                    if (ponorToDelete != null) {
                        deleteEm.remove(ponorToDelete);
                    }
                    deleteTransaction.commit();
                    log.info("Ponor with ID: {} deleted successfully", ponor.getId());
                    ponorContainer.getChildren().remove(hBox);
                    loadPonory();
                } catch (Exception e) {
                    log.error("Failed to delete Ponor with ID: {}", ponor.getId(), e);
                    if (deleteTransaction.isActive()) {
                        deleteTransaction.rollback();
                    }
                } finally {
                    deleteEm.close(); // Close the new EntityManager
                }
            });

            viewButton.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/detail.fxml"));
                    VBox detailRoot = loader.load();

                    PonorDetailController controller = loader.getController();
                    controller.setPonor(ponor);

                    Stage detailStage = new Stage();
                    controller.setStage(detailStage);

                    detailStage.setTitle("Ponor Detail");
                    detailStage.setScene(new Scene(detailRoot));
                    detailStage.show();
                } catch (Exception e) {
                    log.error("Failed to open Ponor detail popup", e);
                }
            });

            editButton.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/edit-popup.fxml"));
                    VBox detailRoot = loader.load();

                    PonorEditController controller = loader.getController();
                    controller.setPonorController(this);
                    controller.setEntityManagerFactory(emf);
                    controller.setPonor(ponor);

                    Stage detailStage = new Stage();
                    controller.setStage(detailStage);

                    detailStage.setTitle("Ponor Detail");
                    detailStage.setScene(new Scene(detailRoot));
                    detailStage.show();
                } catch (Exception e) {
                    log.error("Failed to open Ponor detail popup", e);
                }
            });
        }
        em.close();
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        log.info("Add button clicked");
        // Add your logic here, e.g., opening a new form or dialog
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/insert-popup.fxml"));
            VBox addPonorRoot = loader.load();

            InsertPonorController controller = loader.getController();
            controller.setEntityManagerFactory(emf);
            controller.setPonorController(this);
            controller.setUzivatel(uzivatel); // Set the user for the controller
            controller.postInit();



            Stage addPonorStage = new Stage();
            addPonorStage.setTitle("Add New Ponor");
            addPonorStage.setScene(new Scene(addPonorRoot));
            addPonorStage.show();
            controller.setStage(addPonorStage);
        } catch (Exception e) {
            log.error("Failed to open Add Ponor popup", e);
        }
    }


    public void onPonoryButtonClick(ActionEvent actionEvent) {
        loadPonory();
    }

    public void onLokalityButtonClick(ActionEvent actionEvent) {
        ponorContainer.getChildren().clear();
        log.info("Loading Ponory for user with ID 1");
        ponorContainer.getChildren().clear(); // Clear the container before loading new Ponors
        EntityManager em = emf.createEntityManager();
        log.debug("Loading Ponory from: {}", emf.getProperties().get("jakarta.persistence.jdbc.url"));
        EntityTransaction transaction = em.getTransaction();
        log.info("Starting transaction");
        transaction.begin();
        log.info("Fetching Ponors for user with ID 1");
        List<Lokalita> lokalitas = em.createQuery("SELECT DISTINCT p FROM Lokalita p", Lokalita.class)
                .getResultList();
        log.info("Fetched {} Ponors", lokalitas.size());
        transaction.commit();
        for (Lokalita lokalita : lokalitas) {
            log.info("Processing Ponor with ID: {}", lokalita.getId());
            HBox hBox = new HBox();
            hBox.setId("ponorDetail" + lokalita.getId());
            hBox.setSpacing(20);
            hBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 5;");

            log.info("Ponor: {}", lokalita);
            Label datumLabel = new Label(lokalita.getNazev());
            Label lokalitaLabel = new Label(lokalita.getTypLokality());
            Label hloubkaLabel = new Label(lokalita.getZeme());

            Button viewButton = new Button("🔍");
            viewButton.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/lokalita-detail.fxml"));
                    VBox detailRoot = loader.load();

                    LokalitaDetailController controller = loader.getController();
                    controller.setLokalita(lokalita);

                    Stage detailStage = new Stage();
                    controller.setStage(detailStage);

                    detailStage.setTitle("Detail - " + lokalita.getNazev());
                    detailStage.setScene(new Scene(detailRoot));
                    detailStage.show();
                } catch (Exception e) {
                    log.error("Failed to open Lokalita detail popup", e);
                }
            });

            hBox.getChildren().addAll(datumLabel, lokalitaLabel, hloubkaLabel,viewButton);
            ponorContainer.getChildren().add(hBox);
        }
    }

    public void onAddLokalitaButtonClick(ActionEvent actionEvent) {
        log.info("Add Lokalita button clicked");
        // Add your logic here, e.g., opening a new form or dialog
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/insert-lokalita-popup.fxml"));
            VBox addLokalitaRoot = loader.load();

            InsertLokalitaController controller = loader.getController();
            controller.setEntityManagerFactory(emf);
            controller.setPonorController(this);

            Stage addLokalitaStage = new Stage();
            addLokalitaStage.setTitle("Add New Lokalita");
            addLokalitaStage.setScene(new Scene(addLokalitaRoot));
            addLokalitaStage.show();
            controller.setStage(addLokalitaStage);
        } catch (Exception e) {
            log.error("Failed to open Add Lokalita popup", e);
        }
    }

    public void setUzivatel(Uzivatel uzivatel) {
        log.info("Setting user for the session: {}", uzivatel.getEmail());
        this.uzivatel = uzivatel;
    }

    public void setEmf(EntityManagerFactory emf) {
        log.debug("Setting EntityManagerFactory for PonorController");
        this.emf = emf;
    }
}