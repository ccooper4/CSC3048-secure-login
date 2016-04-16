package ui.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import util.EncryptedLogger;
import ui.Loader;
import ui.main.MainViewController;

import java.io.IOException;

/**
 * Manages control flow for logins
 */
public class LoginManager {

    private EncryptedLogger log = new EncryptedLogger(getClass());
    private Scene scene;

    public LoginManager(Scene scene) {
        this.scene = scene;
    }

    /**
     * Callback method invoked to notify that a user has logged out of the main application.
     * Will show the login application screen.
     */
    public void logout() {
        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = Loader.getFXML("login");
            scene.setRoot(loader.load());
            LoginController controller = loader.getController();
            controller.initManager(this);
        } catch (IOException ex) {
            log.error("IOException", ex);
        }
    }

    public void showMainView() {
        try {
            FXMLLoader loader = Loader.getFXML("mainview");
            scene.setRoot(loader.load());

            MainViewController controller = loader.getController();
            controller.initManager(this);
        } catch (IOException ex) {
            log.error("IOException", ex);
        }
    }
}
