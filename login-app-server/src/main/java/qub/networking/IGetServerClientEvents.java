package qub.networking;

/**
 * Outlines the interface via which an object can be notified about a new client.
 */
public interface IGetServerClientEvents {

    /**
     * Notifies a listener of a new client.
     * @param connection The object by which the new client can be accessed.
     */
    void NewClient(ClientConnection connection);

}
