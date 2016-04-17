package qub.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import model.UserInfo;
import qub.network.IServerConnector;
import qub.network.ServerConnector;
import qub.ui.managers.NavigationManager;
import qub.ui.validation.RequiredField;

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

    @FXML
    private RequiredField requiredFieldUName;

    @FXML
    private RequiredField requiredFieldPassword;

    private boolean hasErrors = false;

    //endregion
    //region Public methods
    public void setNavigationManager(NavigationManager navManager) {
        this.navManager = navManager;
    }

    public void showRegisterView() {
        navManager.showRegistrationScreen();
    }

    public void login() {
        if (validateFields()) {
            boolean success = serverConnector.login(user.getText(), password.getText());            
            if (success) {
                UserInfo userInfo = serverConnector.getCurrentUser();
                navManager.showHomeScreen(userInfo.getFirstName() + " " + userInfo.getLastName());
            } else {
                navManager.showLoginScreen(user.getText());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("CSC3048 Group ISS1");
                alert.setHeaderText("Login Error");
                alert.setContentText("The provided username and/or password are not valid or you are timed out. Please check these details.");
                alert.showAndWait();
            }
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

    private boolean validateFields() {
        requiredFieldPassword.eval();
        requiredFieldUName.eval();

        if (requiredFieldPassword.getHasErrors() || requiredFieldUName.getHasErrors()) {
            return false;
        } else {
            return true;
        }
    }

    //endregion
}
