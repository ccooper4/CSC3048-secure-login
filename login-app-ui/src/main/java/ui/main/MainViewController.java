package ui.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.login.LoginManager;

/**
 * Controls the main application screen
 */
public class MainViewController {

    @FXML
    private Button logoutButton;

    public void initManager(final LoginManager loginManager) {
        logoutButton.setOnAction(event -> loginManager.logout());
    }
}