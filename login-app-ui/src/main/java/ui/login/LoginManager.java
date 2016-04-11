package ui.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import util.LogUtils;
import ui.Loader;
import ui.main.MainViewController;

import java.io.IOException;

/**
 * Manages control flow for logins
 */
public class LoginManager {
    private Scene scene;

    public LoginManager(Scene scene) {
        this.scene = scene;
    }

    /**
     * Callback method invoked to notify that a user has been authenticated.
     * Will show the main application screen.
     */
    public void authenticated(String sessionID) {
        showMainView(sessionID);
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
            LoginController controller = loader.<LoginController>getController();
            controller.initManager(this);
        } catch (IOException ex) {
            LogUtils.getInstance().error("IOException", ex);
        }
    }

    private void showMainView(String sessionID) {
        try {
            FXMLLoader loader = Loader.getFXML("mainview");
            scene.setRoot(loader.load());

            MainViewController controller = loader.<MainViewController>getController();
            controller.initSessionID(this, sessionID);
        } catch (IOException ex) {
            LogUtils.getInstance().error("IOException", ex);
        }
    }
}