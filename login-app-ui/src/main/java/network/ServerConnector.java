package network;

import model.AuthResult;
import model.Credential;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class ServerConnector implements IServerConnector {

    private RestTemplate restTemplate = new RestTemplate();
    private final String URI = "http://localhost:8080";

    public static String AUTH_TOKEN;

    public ServerConnector() {
    }

    public boolean register(String firstName, String lastName, String password) {
        return false;
    }

    public boolean logout() {
        AUTH_TOKEN = "";
        return true;
    }

    public boolean login(String loginId, String password) {
        Credential credential = new Credential();

        credential.setUserName(loginId);
        credential.setPassWord(password);

        // Create the request
        HttpEntity<Credential> body = new HttpEntity<>(credential);
        HttpEntity<AuthResult> response = restTemplate.exchange(URI + "/login", HttpMethod.POST, body, AuthResult.class);

        AuthResult result = response.getBody();
        HttpHeaders headers = response.getHeaders();

        AUTH_TOKEN = headers.get("AUTH-TOKEN").get(0);

        return result.isSuccess();
    }
}
