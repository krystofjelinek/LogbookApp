package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.dao.LokalitaDao;
import cz.vse.logbookapp.dao.PonorDao;
import cz.vse.logbookapp.dao.UzivatelDao;
import cz.vse.logbookapp.model.Lokalita;
import cz.vse.logbookapp.model.Ponor;
import cz.vse.logbookapp.model.Uzivatel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Controller for managing Ponors (dives) and Lokality (locations) in the application.
 * This controller handles the UI interactions for displaying, adding, editing, and deleting Ponors and Lokality.
 */
public class PonorController {

    private static final Logger log = LoggerFactory.getLogger(PonorController.class);

    private Uzivatel uzivatel;

    @FXML
    public Button add;

    @FXML
    public Button addLokalita;

    @FXML
    public Label userNameLabel;

    @FXML
    private VBox ponorContainer;

    PonorDao ponorDao;

    LokalitaDao lokalitaDao;
    private UzivatelDao uzivatelDao;


    /**
     * Initializes the PonorController, setting up the username label and loading the Ponors.
     */
    @FXML
    public void initializeData() {
        setUserNameLabel();
        loadPonory();
    }

    /**
     * Sets the username label based on the logged-in user's information.
     */
    private void setUserNameLabel() {
        if (uzivatel != null) {
            userNameLabel.setText(uzivatel.getJmeno());
        }
    }

    /**
     * Loads the Ponors for the logged-in user and populates the UI with their details.
     * It fetches the Ponors from the database and displays them in a VBox.
     */
    void loadPonory() {
        log.info("Loading Ponory for user with ID: {}", uzivatel.getId());
        ponorContainer.getChildren().clear();
        List<Ponor> ponory = ponorDao.findAllByUser(uzivatel);
        for (Ponor ponor : ponory) {
            log.info("Processing Ponor with ID: {}", ponor.getId());
            HBox hBox = new HBox();
            hBox.setId("ponorDetail" + ponor.getId());
            hBox.setSpacing(28);
            hBox.setPadding(new Insets(18, 24, 18, 24));
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setStyle(
                    "-fx-background-color: #fff;" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, #b0b8c1, 8, 0.12, 0, 2);" +
                            "-fx-border-color: #e0e6ed;" +
                            "-fx-border-width: 1;"
            );

            Label datumLabel = new Label(ponor.getDatum().toString());
            datumLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a3a4d;");

            Label lokalitaLabel = new Label(ponor.getLokalita().getNazev());
            lokalitaLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #4a5a6a;");

            Label hloubkaLabel = new Label(ponor.getHloubka() + " m");
            hloubkaLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #4a5a6a;");

            Label dobaLabel = new Label(ponor.getDoba() + " min");
            dobaLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #4a5a6a;");

            Button viewButton = new Button("View");
            viewButton.setStyle(
                    "-fx-background-color: #3b7ddd;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 6 18;"
            );

            Button editButton = new Button("Edit");
            editButton.setStyle(
                    "-fx-background-color: #ffc107;" +
                            "-fx-text-fill: #2a3a4d;" +
                            "-fx-font-size: 16px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 6 18;"
            );

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle(
                    "-fx-background-color: #e74c3c;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 6 18;"
            );

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hBox.getChildren().addAll(datumLabel, lokalitaLabel, hloubkaLabel, dobaLabel, spacer, viewButton, editButton, deleteButton);
            ponorContainer.getChildren().add(hBox);

            deleteButton.setOnAction(event -> {
                log.info("Deleting Ponor with ID: {}", ponor.getId());
                ponorDao.delete(ponor.getId());
                loadPonory();
            });

            viewButton.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/detail.fxml"));
                    VBox detailRoot = loader.load();

                    PonorDetailController controller = loader.getController();
                    controller.setPonor(ponor);

                    Stage detailStage = new Stage();
                    controller.setStage(detailStage);

                    detailStage.setTitle("Ponor Detail" + " - " + ponor.getDatum().toString());
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
                    controller.setLokalitaDao(lokalitaDao);
                    controller.setPonorController(this);
                    controller.setPonor(ponor);
                    controller.setPonorDao(ponorDao);


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
    }

    /**
     * Handles the click event for the "Add" button, opening a popup to add a new Ponor.
     *
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void onAddButtonClick(ActionEvent event) {
        log.info("Add button clicked");
        // Add your logic here, e.g., opening a new form or dialog
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/insert-popup.fxml"));
            VBox addPonorRoot = loader.load();

            InsertPonorController controller = loader.getController();
            controller.setPonorController(this);
            controller.setUzivatel(uzivatel);
            controller.setPonorDao(ponorDao);
            controller.setLokalitaDao(lokalitaDao);
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

    /** Handles the click event for the "Ponory" button, loading the Ponors for the user.
     *
     * @param actionEvent The ActionEvent triggered by the button click.
     */
    public void onPonoryButtonClick(ActionEvent actionEvent) {
        loadPonory();
    }

    /**
     * Handles the click event for the "Lokality" button, loading the Lokality for the user.
     *
     * @param actionEvent The ActionEvent triggered by the button click.
     */
    public void onLokalityButtonClick(ActionEvent actionEvent) {
        ponorContainer.getChildren().clear();

        List<Lokalita> lokalitas = lokalitaDao.findAll();

        for (Lokalita lokalita : lokalitas) {
            log.info("Processing Lokalita with ID: {}", lokalita.getId());
            HBox hBox = new HBox();
            hBox.setId("lokalitaDetail" + lokalita.getId());
            hBox.setSpacing(28);
            hBox.setPadding(new Insets(18, 24, 18, 24));
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setStyle(
                    "-fx-background-color: #fff;" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, #b0b8c1, 8, 0.12, 0, 2);" +
                            "-fx-border-color: #e0e6ed;" +
                            "-fx-border-width: 1;"
            );

            Label datumLabel = new Label(lokalita.getNazev());
            datumLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a3a4d;");

            Label lokalitaLabel = new Label(lokalita.getTypLokality());
            lokalitaLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #4a5a6a;");

            Label hloubkaLabel = new Label(lokalita.getZeme());
            hloubkaLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #4a5a6a;");

            Button viewButton = new Button("🔍");
            viewButton.setStyle(
                    "-fx-background-color: #3b7ddd;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 6 18;"
            );
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

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hBox.getChildren().addAll(datumLabel, lokalitaLabel, hloubkaLabel, spacer, viewButton);
            ponorContainer.getChildren().add(hBox);
        }
    }

    /**
     * Handles the click event for the "Add Lokalita" button, opening a popup to add a new Lokalita.
     *
     * @param actionEvent The ActionEvent triggered by the button click.
     */
    public void onAddLokalitaButtonClick(ActionEvent actionEvent) {
        log.info("Add Lokalita button clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/insert-lokalita-popup.fxml"));
            VBox addLokalitaRoot = loader.load();

            InsertLokalitaController controller = loader.getController();
            controller.setPonorController(this);
            controller.setLokalitaDao(lokalitaDao);

            Stage addLokalitaStage = new Stage();
            addLokalitaStage.setTitle("Add New Lokalita");
            addLokalitaStage.setScene(new Scene(addLokalitaRoot));
            addLokalitaStage.show();
            controller.setStage(addLokalitaStage);
        } catch (Exception e) {
            log.error("Failed to open Add Lokalita popup", e);
        }
    }

    /**
     * Sets the user for the PonorController.
     *
     * @param uzivatel The user to set.
     */
    public void setUzivatel(Uzivatel uzivatel) {
        log.info("Setting user for the session: {}", uzivatel.getEmail());
        this.uzivatel = uzivatel;
    }

    public void setUzivatelDao(UzivatelDao uzivatelDao) {
        this.uzivatelDao = uzivatelDao;
    }

    public void setPonorDao(PonorDao ponorDao) {
        log.info("Setting PonorDao for the session");
        this.ponorDao = ponorDao;
    }

    public void setLokalitaDao(LokalitaDao lokalitaDao) {
        log.info("Setting LokalitaDao for the session");
        this.lokalitaDao = lokalitaDao;
    }
}