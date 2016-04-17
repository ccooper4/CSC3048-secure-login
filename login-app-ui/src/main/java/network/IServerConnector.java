package network;

import model.UserInfo;

/**
 * Outlines the interface for the client communications with the server.
 */
public interface IServerConnector {

    /**
     * Register a user to the system.
     * @param firstName The first name.
     * @param lastName  The last name.
     * @param password  The password.
     */
    String register(String firstName, String lastName, String password);

    /**
     * Login a user to the system.
     * @param loginId   The login ID.
     * @param password  The password.
     */
    boolean login(String loginId, String password);

    /**
     * Logout the current user from the system.
     * @return
     */
    void logout();

    /**
     * Get the current logged in user.
     * @return  The user info.
     */
    UserInfo getCurrentUser();
}
