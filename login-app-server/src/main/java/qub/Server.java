package qub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;
import org.springframework.util.SystemPropertyUtils;
import qub.domain.User;
import qub.networking.ClientConnection;
import qub.networking.IGetServerClientEvents;
import qub.networking.IListeningServer;
import qub.service.IUserService;

import java.util.Scanner;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class Server implements CommandLineRunner, IGetServerClientEvents {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

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

        log.info("CSC3048 - Login Application - Server started");

        while (!closeServer) {

            log.info("Server is listening. Enter quit and press Enter to stop the server.");

            String nextConsoleInput = consoleInput.nextLine();

            if (nextConsoleInput != null && nextConsoleInput == "quit") {
                closeServer = true;
            }
        }

        listeningServer.stopListening();

        log.info("CSC3048 - Login Application - Server stopped.");


    }

    /**
     * Handles the event that is raised when a new client connects to the listening server.
     * @param connection The object by which the new client can be accessed.
     */
    @Override
    public void NewClient(ClientConnection connection) {

        log.info("Handling new client.");

    }
}
