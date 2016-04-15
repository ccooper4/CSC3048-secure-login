package qub;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import qub.domain.User;
import qub.repositories.UserRepository;
import qub.service.IUserService;
import qub.util.LoginIdGenerator;

/**
 * Load the default users into the in memory database upon application startup.
 */
@Component
public class DatabaseSeed implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private IUserService userService;

    private Logger log = Logger.getLogger(DatabaseSeed.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        // Create users if they do not exist
        String password = "test";

        if (userService.getUsersByFirstName("Andrew").isEmpty()) {
            userService.createUser("Andrew", "Fletcher", password);
        }

        if (userService.getUsersByFirstName("Chris").isEmpty()) {
            userService.createUser("Chris", "Cooper", password);
        }

        if (userService.getUsersByFirstName("David").isEmpty()) {
            userService.createUser("David", "Fee", password);
        }

        if (userService.getUsersByFirstName("Rory").isEmpty()) {
            userService.createUser("Rory", "Powell", password);
        }
    }
}