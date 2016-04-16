package ui.controllers;

import network.IServerConnector;
import network.ServerConnector;
import ui.managers.NavigationManager;

/**
 * Controls the main application screen
 */
public class HomeController {

    private NavigationManager navManager;
    private IServerConnector serverConnector = new ServerConnector();

    public void setNavigationManager(NavigationManager loginManager) {
        this.navManager = loginManager;
    }

    public void logout() {
        serverConnector.logout();
        navManager.showLoginScreen();
    }
}