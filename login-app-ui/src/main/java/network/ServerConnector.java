package network;

import model.AuthResult;
import model.Credential;
import org.springframework.web.client.RestTemplate;

public class ServerConnector implements IServerConnector {

    private RestTemplate restTemplate = new RestTemplate();
    private final String URI = "https://localhost:8443";

    public ServerConnector() {
    }

    public boolean register(String firstName, String lastName, String password) {
        return false;
    }

    public boolean login(String loginId, String password) {
        Credential credential = new Credential();

        credential.setUserName(loginId);
        credential.setPassWord(password);

        AuthResult authResult = restTemplate.postForObject(URI + "/login", credential, AuthResult.class);
        return authResult.isSuccess();
    }
}
