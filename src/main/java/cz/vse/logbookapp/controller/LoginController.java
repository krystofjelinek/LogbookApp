package cz.vse.logbookapp.controller;

import cz.vse.logbookapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class.getName());

    private EntityManagerFactory emf;

    @FXML
    public TextField usernameField;
    private Stage stage;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("logbookPU");
    }

    @FXML
    public void onLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        if (username == null || username.isEmpty()) {
            log.warn("Username-field is null or empty");
            usernameField.setStyle("-fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8; -fx-border-color: red;");
        } else {
            usernameField.setStyle("-fx-text-fill: black;");

            EntityManager em = emf.createEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            log.info("Attempting to log in user: " + username);
            try {
                List<Uzivatel> uzivatele = em.createQuery("SELECT u FROM Uzivatel u WHERE u.email = :email", Uzivatel.class)
                        .setParameter("email", username)
                        .getResultList();
                Uzivatel uzivatel;
                if (uzivatele.isEmpty()) {
                    log.warn("User " + username + " not found in the database.");
                    usernameField.setStyle("-fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8; -fx-border-color: red;");
                    usernameField.clear();
                    usernameField.setPromptText("Username not found");
                    return;
                } else {
                    uzivatel = uzivatele.getFirst();
                }
                log.info("User " + uzivatel.getEmail() + " found in the database.");
                log.info("User " + uzivatel.getEmail() + " logged in successfully.");
                transaction.commit();
                em.close();

                // Looks like it crashes here if the user is found
                // Load main window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/logbook-view.fxml"));
                Parent root = loader.load();

                // Pass user to main controller
                PonorController controller = loader.getController();
                controller.setUzivatel(uzivatel);
                controller.setEmf(emf);
                controller.initializeData();

                Stage mainStage = new Stage();
                mainStage.setTitle("Logbook - " + uzivatel.getEmail());
                mainStage.setScene(new Scene(root));
                mainStage.showAndWait();

                // Close login window
                stage.close();
            } catch (Exception e) {
                log.warn("User " + username + " not found in the database.");
                log.error("Error during login: ", e);
                em.close();
                usernameField.setStyle("-fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8; -fx-border-color: red;");
                usernameField.clear();
                usernameField.setPromptText("Username not found");
            } finally {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                em.close();
            }
        }
    }

    public void setStage(Stage loginStage) {
        this.stage = loginStage;
    }
}
