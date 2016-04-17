package qub;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import qub.service.IUserService;
import util.EncryptedLogger;

/**
 * Load the default users into the in memory database upon application startup.
 */
@Component
public class DatabaseSeed implements ApplicationListener<ContextRefreshedEvent> {

    private EncryptedLogger log = new EncryptedLogger(DatabaseSeed.class);

    @Autowired
    private IUserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        // Create users if they do not exist
        String password = "test";

        if (userService.getUsersByFirstName("Andrew").isEmpty()) {
            log.info("Creating default user");
            userService.createAdminUser("Andrew", "Fletcher", password);
        }

        if (userService.getUsersByFirstName("Chris").isEmpty()) {
            log.info("Creating default user");
            userService.createAdminUser("Chris", "Cooper", password);
        }

        if (userService.getUsersByFirstName("David").isEmpty()) {
            log.info("Creating default user");
            userService.createAdminUser("David", "Fee", password);
        }

        if (userService.getUsersByFirstName("Rory").isEmpty()) {
            log.info("Creating default user");
            userService.createAdminUser("Rory", "Powell", password);
        }

        if (userService.getUsersByFirstName("Fred").isEmpty()) {
            log.info("Creating default user");
            userService.createStandardUser("Fred", "Fredricks", password);
        }
    }
}