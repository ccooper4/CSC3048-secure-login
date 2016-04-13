package ui.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controls the login screen
 */
public class RegisterController {
    @FXML
    private TextField fName;
    @FXML
    private TextField sName;
    @FXML
    private TextField email;
    @FXML
    private Button registerButton;

    public void initialize() {
    }

    public void initManager(final RegisterManager registerManagerManager) {
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               //register
            }
        });
    }

}
