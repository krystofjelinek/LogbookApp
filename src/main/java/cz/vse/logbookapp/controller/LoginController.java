package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.dao.LokalitaDao;
import cz.vse.logbookapp.dao.PonorDao;
import cz.vse.logbookapp.dao.UzivatelDao;
import cz.vse.logbookapp.model.Uzivatel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Controller for handling user login in the Logbook application.
 * This controller manages the login process, including user validation
 * and transitioning to the main application view upon successful login.
 */
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class.getName());


    @FXML
    public TextField usernameField;

    private Stage stage;

    private UzivatelDao uzivatelDao;

    private PonorDao ponorDao;
    private LokalitaDao lokalitaDao;


    public void setUzivatelDao(UzivatelDao uzivatelDao) {
        this.uzivatelDao = uzivatelDao;
    }

    public void setPonorDao(PonorDao ponorDao) {
        this.ponorDao = ponorDao;
    }
    /**
     * Handles the login action when the user clicks the login button.
     * Validates the username input and checks if the user exists in the database.
     * If successful, it loads the main application view and passes the user data.
     *
     * @param actionEvent The action event triggered by clicking the login button.
     */
    @FXML
    public void onLogin(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        if (username == null || username.isEmpty()) {
            log.warn("Username-field is null or empty");
            usernameField.setStyle("-fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8; -fx-border-color: red;");
            return;
        }
        usernameField.setStyle("-fx-text-fill: black;");
            Uzivatel uzivatel = uzivatelDao.findByEmail(username);
            if (uzivatel == null) {
                usernameField.setStyle("-fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8; -fx-border-color: red;");
                usernameField.clear();
                usernameField.setPromptText("Username not found");
            } else {
            log.info("User " + uzivatel.getEmail() + " found in the database.");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/logbook-view.fxml"));
            Parent root = loader.load();

            PonorController controller = loader.getController();
            controller.setUzivatel(uzivatel);
            controller.setUzivatelDao(uzivatelDao);
            controller.setPonorDao(ponorDao);
            controller.setLokalitaDao(lokalitaDao);
            controller.initializeData();


            Stage mainStage = new Stage();
            mainStage.setTitle("Logbook - " + uzivatel.getEmail());
            mainStage.setScene(new Scene(root));
            mainStage.show();

            // Only close the login stage after successful login
            stage.close();
            }
        }


    /** Sets the Stage for the login controller.
     * This method is used to set the stage that will be closed upon successful login.
     *
     * @param loginStage The Stage object representing the login window.
     */
    public void setStage(Stage loginStage) {
        this.stage = loginStage;
    }

    public void setLokalitaDao(LokalitaDao lokalitaDao) {
        this.lokalitaDao = lokalitaDao;
    }
}
