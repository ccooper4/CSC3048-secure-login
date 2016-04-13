package ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import util.EncryptedLogger;

/**
 * Controls the login screen
 */
public class LoginController {

    private EncryptedLogger log = new EncryptedLogger(getClass());

    @FXML
    private TextField user;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;

    public void initialize() {
    }

    public void initManager(final LoginManager loginManager) {
        loginButton.setOnAction(event -> {
            String sessionID1 = authorize();
            if (sessionID1 != null) {
                loginManager.authenticated(sessionID1);
            }
        });
    }

    /**
     * Check authorization credentials.
     * <p>
     * If accepted, return a sessionID for the authorized session
     * otherwise, return null.
     */
    private String authorize() {
        if ("open".equals(user.getText()) && "sesame".equals(password.getText())) {
            log.info("User: " + user.getText() + " logged in");
            return generateSessionID();
        } else {
            return null;
        }
    }

    private static int sessionID = 0;

    private String generateSessionID() {
        sessionID++;
        return "xyzzy - session " + sessionID;
    }
}
