package qub.networking;

import org.springframework.stereotype.Service;
import util.EncryptedLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static util.EncryptedLogger.*;

/**
 * Implements a threaded server that can listen on a specified port.
 */
@Service
public class ListeningServer implements IListeningServer {

    //region Fields
    private EncryptedLogger log = new EncryptedLogger(getClass());

    /**
     * The port to listen on.
     */
    private int listenPort;

    /**
     * A flag indicating if the server is listening for connections.
     */
    private boolean listening;

    /**
     * Gets the listening socket wrapped by this Listning Server.
     */
    private ServerSocket internalSocket;

    /**
     * Gets the list of objects that we should notify when we accept a new client.
     */
    private List<IGetServerClientEvents> subscribedNewClientObjects;

    //endregion

    //region Getters & Setters

    /**
     * Gets the configured listen port.
     * @return The listen port.
     */
    public int getListenPort() {
        return listenPort;
    }

    /**
     * Gets a flag indicating if the server is listening.
     * @return A boolean.
     */
    public boolean getListening() {
        return listening;
    }

    //endregion

    //region Constructor

    /**
     * Constructs the listening server to listen on the specified port.
     * @param port The port.
     */
    public ListeningServer(int port) {
        this.listenPort = port;

        subscribedNewClientObjects = new ArrayList<IGetServerClientEvents>();


        try {
            internalSocket = new ServerSocket(port);
        } catch (IOException e) {

            log.error("A socket could not be created on port " + port, e);

        }
    }

    /**
     * Constructs the listening server to listen on the default port (9000);
     */
    public ListeningServer() {
        this(9000);
    }

    //endregion

    //region Methods

    /**
     * Instructs the server to start listening.
     */
    @Override
    public void startListening() {

        if (!listening) {

            listening = true;

            ListenThread listenThreadRunnable = new ListenThread();

            Thread listenThread = new Thread(listenThreadRunnable);
            listenThread.start();

            log.info("Server started listening on port " + listenPort);
        }

    }

    @Override
    public void stopListening() {
        try {
            internalSocket.close();
        } catch (IOException e) {
            log.error("Could not close the internal socket.", e);
        }
    }

    /**
     * Provides a method that allows subscribes to register to receive new client events.
     * @param handler The handler for the event.
     */
    @Override
    public void subscribeToNewClientEvents(IGetServerClientEvents handler) {
        subscribedNewClientObjects.add(handler);
    }

    //endregion


    //region Listen Runnable Sub Class

    /**
     * Provides a thread implementation that can listen for connections.
     */
    public class ListenThread implements Runnable {

        /**
         * Runs the listen thread.
         */
        @Override
        public void run() {

            log.info("Listening for connections on new thread.");

            while (true) {

                try {
                    Socket clientSocket = internalSocket.accept();

                    log.info("Accepting connection from: " + clientSocket.getInetAddress().toString());

                    AcceptThread handlerThreadRunnable = new AcceptThread(clientSocket);

                    Thread handlerThread = new Thread(handlerThreadRunnable);
                    handlerThread.start();

                    log.info("Started new Accept Thread to handle request.");

                } catch (IOException e) {
                    log.error("An log.error occurred whilst trying to accept a connection from a client.", e);

                }

            }

        }
    }

    //endregion

    //region Accept Runnable Sub Class

    /**
     * Provides a thread implementation that can accept connections, without blocking the listener.
     */
    public class AcceptThread implements Runnable {

        /**
         * The client socket.
         */
        private Socket clientSocket;

        /**
         * Constructs a new accept thread with the specified client.
         * @param client
         */
        public AcceptThread(Socket client) {
            clientSocket = client;
        }

        /**
         * Runs the accept thread.
         */
        @Override
        public void run() {

            log.info("Handling newly accepted client.");

            ClientConnection connection = new ClientConnection(clientSocket);

            for (IGetServerClientEvents subscriber : subscribedNewClientObjects) {
                subscriber.NewClient(connection);
            }

        }
    }

    //endregion

}
