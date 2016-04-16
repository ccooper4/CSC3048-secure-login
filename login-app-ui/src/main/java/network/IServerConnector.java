package network;

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
    boolean register(String firstName, String lastName, String password);

    /**
     * Login a user to the system.
     * @param loginId   The login ID.
     * @param password  The password.
     */
    boolean login(String loginId, String password);

}
