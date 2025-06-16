package cz.vse.logbookapp;

import cz.vse.logbookapp.controller.LoginController;
import cz.vse.logbookapp.dao.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/logbookapp/view/login.fxml"));
        Parent root = loader.load();
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(root));
        LoginController controller = loader.getController();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("logbookPU");
        UzivatelDao uzivatelDao = new UzivatelDaoImpl(emf);
        PonorDao ponorDao = new PonorDaoImpl(emf);
        LokalitaDao lokalitaDao = new LokalitaDaoImpl(emf);

        controller.setUzivatelDao(uzivatelDao);
        controller.setPonorDao(ponorDao);
        controller.setLokalitaDao(lokalitaDao);

        controller.setStage(loginStage);

        loginStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}