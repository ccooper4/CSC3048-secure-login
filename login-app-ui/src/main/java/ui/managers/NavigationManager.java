package ui.managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import ui.Loader;
import ui.controllers.LoginController;
import ui.controllers.HomeController;
import ui.controllers.RegisterController;
import util.EncryptedLogger;

import java.io.IOException;

public class NavigationManager {

    private EncryptedLogger log = new EncryptedLogger(getClass());
    private Scene scene;
    private String view;

    public NavigationManager(Scene scene) {
        this.scene = scene;
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = Loader.getFXML("login");
            scene.setRoot(loader.load());

            LoginController controller = loader.getController();
            controller.setNavigationManager(this);
            view = "login";
        } catch (IOException ex) {
            log.error("IOException", ex);
        }
    }

    public void showHomeScreen() {
        try {
            FXMLLoader loader = Loader.getFXML("home");
            scene.setRoot(loader.load());

            HomeController controller = loader.getController();
            controller.setNavigationManager(this);
            view = "home";
        } catch (IOException ex) {
            log.error("IOException", ex);
        }
    }

    public void showRegistrationScreen() {
        try {
            FXMLLoader loader = Loader.getFXML("register");
            scene.setRoot(loader.load());

            RegisterController controller = loader.getController();
            controller.setNavigationManager(this);
            view = "register";
        } catch (IOException ex) {
            log.error("IOException", ex);
        }
    }

    public void goBack() {
        try {
            FXMLLoader loader = Loader.getFXML(view);
            scene.setRoot(loader.load());
            if(view == "register"){
                RegisterController controller = loader.getController();
                controller.setNavigationManager(this);
            }

            else {
                LoginController controller = loader.getController();
                controller.setNavigationManager(this);
            }

        } catch (IOException ex) {
            log.error("IOException", ex);
        }
    }

    public void showPolicyScreen() {
        try {
            FXMLLoader loader = Loader.getFXML("policy");
            scene.setRoot(loader.load());
            RegisterController controller = loader.getController();
            controller.setNavigationManager(this);

        } catch (IOException ex) {
            log.error("IOException", ex);
        }
    }
}
