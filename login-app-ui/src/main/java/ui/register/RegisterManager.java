package ui.register;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import ui.Loader;
import util.EncryptedLogger;

import java.io.IOException;

/**
 * Manages control flow for registrations
 */
public class RegisterManager {
    private Scene scene;
    private EncryptedLogger log = new EncryptedLogger(RegisterManager.class);

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
            RegisterController controller = loader.getController();
            controller.initManager(this);
        } catch (IOException ex) {
           log.error("IOException", ex);
        }
    }
}
