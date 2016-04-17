package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.UserInfo;
import network.IServerConnector;
import network.ServerConnector;
import ui.managers.NavigationManager;
import ui.validation.RequiredField;

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
        validateFields();
        if(!hasErrors)
        {
            boolean success = serverConnector.login(user.getText(), password.getText());
            UserInfo userInfo = serverConnector.getCurrentUser();
            if (success) {
                navManager.showHomeScreen(userInfo.getFirstName() + " " + userInfo.getLastName());
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

    private void validateFields(){
        requiredFieldPassword.eval();
        requiredFieldUName.eval();

        if(requiredFieldPassword.getHasErrors() || requiredFieldUName.getHasErrors()) {
            hasErrors = true;
        }
    }

    //endregion

}
