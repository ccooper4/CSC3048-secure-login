package qub.networking;

import networking.IServerSideFunctions;

import java.net.Socket;

/**
 * Represents the connection to a client.
 */
public class ClientConnection implements IServerSideFunctions {

    //region Fields

    /**
     * The internal client socket that is wrapped by this object.
     */
    private Socket clientSocket;

    //endregion

    //region Constructor

    /**
     * Constructs a ClientConnection with the specified socket.
     * @param socket The client socket.
     */
    public ClientConnection(Socket socket) {
        this.clientSocket = socket;
    }

    //endregion

}
