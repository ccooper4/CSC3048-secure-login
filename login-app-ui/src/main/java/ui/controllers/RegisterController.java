package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import network.IServerConnector;
import network.ServerConnector;
import ui.managers.NavigationManager;

/**
 * Controls the registration screen
 */
public class RegisterController {

    private IServerConnector serverConnector = new ServerConnector();
    private NavigationManager navManager;

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;

    /**
     * Set the login manager.
     * @param navManager  The navigation manager.
     */
    public void setNavigationManager(NavigationManager navManager) {
        this.navManager = navManager;
    }

    /**
     * Cancel the registration and navigate back to the login screen.
     */
    public void cancelRegistration() {
        navManager.showLoginScreen();
    }

    /**
     * Register a user.
     */
    public void register() {
        serverConnector.register(firstName.getText(), lastName.getText(), password.getText());
    }
}
