package cz.vse.logbookapp;

import cz.vse.logbookapp.controller.LoginController;
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
        controller.setStage(loginStage);
        loginStage.show();


       /* FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("logbook-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Divers`s Logbook");
        stage.setScene(scene);
        stage.show();*/
    }

    public static void main(String[] args) {
        launch();
    }
}