package network;

import model.AuthResult;
import model.Credential;
import model.UserInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import util.StringConstants;

import java.util.ArrayList;
import java.util.List;

public class ServerConnector implements IServerConnector {

    private RestTemplate restTemplate = new RestTemplate();
    private final String URI = "http://localhost:8080";

    public static String AUTH_TOKEN;

    public ServerConnector() {
    }

    public String register(String firstName, String lastName, String password) {

        UserInfo userInfo = new UserInfo(firstName, lastName, password);

        String loginId = restTemplate.postForObject(URI + "/register", userInfo, String.class);

        return loginId;
    }

    public void logout() {
        AUTH_TOKEN = "";
//        HttpEntity<AuthResult> response = restTemplate.exchange(URI + "/login", HttpMethod.POST, AuthResult.class);
    }

    public UserInfo getCurrentUser() {

        List<String> values = new ArrayList<>();
        values.add(AUTH_TOKEN);

        HttpHeaders headers = new HttpHeaders();
        headers.put(StringConstants.TOKEN_HEADER_NAME, values);

        HttpEntity request = new HttpEntity<>(headers);

        HttpEntity<UserInfo> response  = restTemplate.exchange(URI + "/currentUser", HttpMethod.GET, request, UserInfo.class);
        UserInfo userInfo = response.getBody();

        return userInfo;
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

        AUTH_TOKEN = headers.get(StringConstants.TOKEN_HEADER_NAME).get(0);

        return result.isSuccess();
    }
}
