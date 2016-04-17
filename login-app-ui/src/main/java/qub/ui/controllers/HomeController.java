package qub.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import qub.network.IServerConnector;
import qub.network.ServerConnector;
import qub.ui.managers.NavigationManager;

/**
 * Controls the main application screen
 */
public class HomeController {

    private NavigationManager navManager;
    private IServerConnector serverConnector = new ServerConnector();

    @FXML
    private Text user;

    public void setNavigationManager(NavigationManager loginManager) {
        this.navManager = loginManager;
    }

    public void logout() {
        serverConnector.logout();
        navManager.showLoginScreen();
    }

    public void setUserText(String username) {
        this.user.setText(username);
    }
}