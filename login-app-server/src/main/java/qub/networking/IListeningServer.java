package qub.networking;

/**
 * Outlines the interface for a listening server.
 */
public interface IListeningServer {

    /**
     * Instructs the server to start listening.
     */
    void startListening();

    /**
     * Instructs the server to stop listening.
     */
    void stopListening();

    /**
     * Provides a way of subscribing to receive a notifaction when a new client connects.
     * @param handler
     */
    void subscribeToNewClientEvents(IGetServerClientEvents handler);

}
