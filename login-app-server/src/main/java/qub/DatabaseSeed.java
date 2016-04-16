package qub;

import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import qub.service.IUserService;

/**
 * Load the default users into the in memory database upon application startup.
 */
@Component
public class DatabaseSeed implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private IUserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        // Create users if they do not exist
        String password = "test";

        if (userService.getUsersByFirstName("Andrew").isEmpty()) {
            userService.createAdminUser("Andrew", "Fletcher", password);
        }

        if (userService.getUsersByFirstName("Chris").isEmpty()) {
            userService.createAdminUser("Chris", "Cooper", password);
        }

        if (userService.getUsersByFirstName("David").isEmpty()) {
            userService.createAdminUser("David", "Fee", password);
        }

        if (userService.getUsersByFirstName("Rory").isEmpty()) {
            userService.createAdminUser("Rory", "Powell", password);
        }

        if (userService.getUsersByFirstName("Fred").isEmpty()) {
            userService.createUser("Fred", "Fredricks", password);
        }
    }
}