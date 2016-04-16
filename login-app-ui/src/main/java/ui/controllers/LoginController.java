package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import network.IServerConnector;
import network.ServerConnector;
import ui.managers.NavigationManager;

/**
 * Controls the login screen
 */
public class LoginController {

    //region Private fields

    private IServerConnector serverConnector = new ServerConnector();
    private NavigationManager navManager;

    @FXML
    private TextField user;

    @FXML
    private TextField password;

    //endregion

    //region Public methods

    public void setNavigationManager(NavigationManager navManager) {
        this.navManager = navManager;
    }

    public void showRegisterView() {
        navManager.showRegistrationScreen();
    }

    public void login() {
        boolean success = serverConnector.login(user.getText(), password.getText());
        if (success) {
            navManager.showHomeScreen();
        }
    }

    /**
     * Show the usage policy
     */
    public void showPolicyView() {
        navManager.showPolicyScreen();
    }

    public void setUserField(String loginID) {
        this.user.setText(loginID);
    }

    //endregion

}
