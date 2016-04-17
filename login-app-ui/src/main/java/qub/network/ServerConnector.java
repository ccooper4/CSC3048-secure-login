package qub.network;

import model.AuthResult;
import model.Credential;
import model.UserInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import util.EncryptedLogger;
import util.StringConstants;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ServerConnector implements IServerConnector {

    private EncryptedLogger log = new EncryptedLogger(getClass());
    private RestTemplate restTemplate = new RestTemplate();
    private final String URI = "https://localhost:8080";

    private static String AUTH_TOKEN;

    public ServerConnector() {

        /**
         * Prevent self-signed certificate error.
         */
        try {
            DisableSSLCertCheck.disableChecks();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }

    public String register(String firstName, String lastName, String password) {

        UserInfo userInfo = new UserInfo(firstName, lastName, password);

        log.info("Sending registration request to server");
        String loginId = restTemplate.postForObject(URI + "/register", userInfo, String.class);

        return loginId;
    }

    public boolean logout() {
        List<String> values = new ArrayList<>();
        values.add(AUTH_TOKEN);

        HttpHeaders headers = new HttpHeaders();
        headers.put(StringConstants.TOKEN_HEADER_NAME, values);

        HttpEntity<Boolean> request = new HttpEntity<>(headers);

        log.info("Sending logout request to server");
        HttpEntity<Boolean> response = restTemplate.exchange(URI + "/signout", HttpMethod.GET, request, Boolean.class);
        boolean success = response.getBody();

        AUTH_TOKEN = "";
        return success;
    }

    public UserInfo getCurrentUser() {

        List<String> values = new ArrayList<>();
        values.add(AUTH_TOKEN);

        HttpHeaders headers = new HttpHeaders();
        headers.put(StringConstants.TOKEN_HEADER_NAME, values);

        HttpEntity request = new HttpEntity<>(headers);

        log.info("Sending current user request to server");
        HttpEntity<UserInfo> response = restTemplate.exchange(URI + "/currentUser", HttpMethod.GET, request, UserInfo.class);
        UserInfo userInfo = response.getBody();

        return userInfo;
    }

    public boolean login(String loginId, String password) {
        Credential credential = new Credential();

        credential.setUserName(loginId);
        credential.setPassWord(password);

        // Create the request
        HttpEntity<Credential> body = new HttpEntity<>(credential);
        log.info("Sending login request to server");
        HttpEntity<AuthResult> response = restTemplate.exchange(URI + "/login", HttpMethod.POST, body, AuthResult.class);

        AuthResult result = response.getBody();
        HttpHeaders headers = response.getHeaders();

        try {
            AUTH_TOKEN = headers.get(StringConstants.TOKEN_HEADER_NAME).get(0);
        } catch (Exception e) {
        }

        return result.isSuccess();
    }
}
