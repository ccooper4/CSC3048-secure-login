package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.managers.NavigationManager;

/**
 * Controls the main application screen
 */
public class MainViewController {

    private NavigationManager navManager;

    @FXML
    private Button logoutButton;

    public void setNavigationManager(NavigationManager loginManager) {
        this.navManager = loginManager;
    }

    public void logout() {

        // perform logout logic.

        navManager.showLoginScreen();
    }
}