package qub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import qub.service.IUserService;
import util.EncryptedLogger;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class Server implements CommandLineRunner {
    
    private EncryptedLogger log = new EncryptedLogger(getClass());

    @Autowired
    IUserService userService;

    public static void main(String[] args) {
        SpringApplication.run(Server.class);
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("Starting Application");
    }
}
