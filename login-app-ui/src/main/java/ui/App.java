package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.login.LoginManager;

import java.io.IOException;

/**
 * The frontend Application class.
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Scene scene = new Scene(new StackPane(), 500, 300);

        LoginManager loginManager = new LoginManager(scene);
        loginManager.showLoginScreen();

        stage.setScene(scene);
        stage.show();
    }

}
