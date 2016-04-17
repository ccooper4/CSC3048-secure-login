package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ui.managers.NavigationManager;

public class RegistrationInfoController {

    private NavigationManager navManager;

    @FXML
    private TextField loginID;

    /**
     * Set the login manager.
     * @param navManager  The navigation manager.
     */
    public void setNavigationManager(NavigationManager navManager) {
        this.navManager = navManager;
    }

    /**
     * Proceed to the login screen.
     */
    public void proceed() {
        navManager.showLoginScreen(loginID.getText());
    }

    /**
     * Set the login ID.
     * @param loginID   The login to display.
     */
    public void setLoginID(String loginID) {
        this.loginID.setText(loginID);
    }
}
