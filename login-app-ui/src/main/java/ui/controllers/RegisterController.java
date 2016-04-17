package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import network.IServerConnector;
import network.ServerConnector;
import ui.managers.NavigationManager;
import ui.validation.RequiredField;

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

    @FXML
    private RequiredField requiredFieldFName;
    @FXML
    private RequiredField requiredFieldSName;
    @FXML
    private RequiredField requiredFieldPassword;
    @FXML
    private RequiredField requiredFieldCPassword;

    private final String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{5,10}";

    private boolean hasErrors = false;

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
     * Show the usage policy
     */
    public void showPolicyView() {
        navManager.showPolicyScreen();
    }

    /**
     * Register a user.
     */
    public void register() {
        validateFields();

        if(!hasErrors) {
            String loginID = serverConnector.register(firstName.getText(), lastName.getText(), password.getText());
            if (loginID != null) {
                navManager.showRegistrationInfoScreen(loginID);
            }
        }
    }

    private void validateFields(){
        requiredFieldFName.eval();
        requiredFieldSName.eval();
        requiredFieldPassword.eval();
        requiredFieldCPassword.eval();
        if(!password.toString().matches(passwordPattern)){
            hasErrors = true;
            requiredFieldPassword.setHasError(true);
        }
        if(requiredFieldFName.getHasErrors() || requiredFieldSName.getHasErrors() || requiredFieldPassword.getHasErrors() || requiredFieldCPassword.getHasErrors()) {
            hasErrors = true;
        }
    }

    public void showRegisterView() {
        navManager.goBack();
    }
}
