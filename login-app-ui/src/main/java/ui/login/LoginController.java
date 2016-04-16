package ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import network.IServerConnector;
import network.ServerConnector;
import util.EncryptedLogger;

/**
 * Controls the login screen
 */
public class LoginController {

    //region Private fields

    private EncryptedLogger log = new EncryptedLogger(getClass());
    private IServerConnector serverConnector = new ServerConnector();

    @FXML
    private TextField user;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    //endregion

    //region Public methods

    public void initManager(final LoginManager loginManager) {

        // Set login action event
        loginButton.setOnAction(event -> {
            boolean success = login();
            if (success) {
                loginManager.showMainView();
            }
        });

        // Set register action event
        registerButton.setOnAction(event -> {
            loginManager.showRegisterView();
        });
    }

    //endregion

    //region Private methods

    private boolean login() {
        return serverConnector.login(user.getText(), password.getText());
    }

    //endregion

}
