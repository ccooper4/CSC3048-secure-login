package ui;

import javafx.fxml.FXMLLoader;
import java.net.URL;

public class Loader {

    public static FXMLLoader getFXML(String name) {
        URL url = Loader.class.getClassLoader().getResource("fxml/" + name + ".fxml");
        return new FXMLLoader(url);
    }
}
