package ui.register;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ui.login.LoginManager;

/**
 * Controls the login screen
 */
public class RegisterController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Button registerButton;
    @FXML
    private Button cancelButton;

    public void initialize() {
    }

    public void initManager(final LoginManager loginManager) {
        cancelButton.setOnAction(event -> {
            loginManager.showLoginScreen();
        });
    }

    public void register() {
        // Set register action event
        registerButton.setOnAction(event -> {

            // TODO: Do some registration
        });
    }




}
