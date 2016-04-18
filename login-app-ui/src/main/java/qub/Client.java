package qub;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import qub.ui.managers.NavigationManager;

import java.io.IOException;

/**
 * The frontend Application class.
 */
public class Client extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Scene scene = new Scene(new StackPane(), 600, 400);
        scene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("css/style.css").toExternalForm());

        NavigationManager navManager = new NavigationManager(scene);
        navManager.showLoginScreen();

        stage.setScene(scene);
        stage.show();
    }

}
