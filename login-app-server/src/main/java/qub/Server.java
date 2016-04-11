package qub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import qub.networking.ClientConnection;
import qub.networking.IGetServerClientEvents;
import qub.networking.IListeningServer;
import qub.service.IUserService;
import java.util.Scanner;

import static util.EncryptedLogger.*;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class Server implements CommandLineRunner, IGetServerClientEvents {

    @Autowired
    IUserService userService;

    @Autowired
    IListeningServer listeningServer;

    public static void main(String[] args) {
        SpringApplication.run(Server.class);
    }

    @Override
    public void run(String... strings) throws Exception {

        listeningServer.subscribeToNewClientEvents(this);

        listeningServer.startListening();

        Scanner consoleInput = new Scanner(System.in);
        boolean closeServer = false;

        info("CSC3048 - Login Application - Server started");

        while (!closeServer) {

            info("Server is listening. Type quit and press Enter to stop the server.");

            String nextConsoleInput = consoleInput.nextLine();

            if (nextConsoleInput != null && nextConsoleInput.equals("quit")) {
                closeServer = true;
            }
        }

        listeningServer.stopListening();

        info("CSC3048 - Login Application - Server stopped.");
    }

    /**
     * Handles the event that is raised when a new client connects to the listening server.
     * @param connection The object by which the new client can be accessed.
     */
    @Override
    public void NewClient(ClientConnection connection) {

        info("Handling new client.");

    }
}
