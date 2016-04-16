package ui.register;

import javafx.scene.Scene;
import util.EncryptedLogger;


/**
 * Manages control flow for registrations
 */
public class RegisterManager {

    private Scene scene;
    private EncryptedLogger log = new EncryptedLogger(RegisterManager.class);

    public RegisterManager(Scene scene) {
        this.scene = scene;
    }

    public void showRegisteredScreen() {
        // TODO
    }
}
