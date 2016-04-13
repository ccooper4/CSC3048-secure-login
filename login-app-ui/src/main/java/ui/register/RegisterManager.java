package ui.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import util.LogUtils;
import ui.Loader;
import ui.main.MainViewController;

import java.io.IOException;

/**
 * Manages control flow for registrations
 */
public class RegisterManager {
    private Scene scene;

    public RegisterManager(Scene scene) {
        this.scene = scene;
    }

    public void register() {
        showRegisteredScreen();
    }

    public void showRegisteredScreen() {
        try {
            FXMLLoader loader = Loader.getFXML("register");
            scene.setRoot(loader.load());
            LoginController controller = loader.<RegisterController>getController();
            controller.initManager(this);
        } catch (IOException ex) {
            LogUtils.getInstance().error("IOException", ex);
        }
    }
}
