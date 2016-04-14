package qub;

import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import qub.domain.User;
import qub.repositories.UserRepository;

/**
 * Load the default users into the in memory database upon application startup.
 */
@Component
public class DatabaseSeed implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    private Logger log = Logger.getLogger(DatabaseSeed.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        // Create users if they do not exist

        if (userRepository.findByFirstName("Andrew").isEmpty()) {
            User andrew = new User("Andrew", "Fletcher", "afletcher@test.com");
            userRepository.save(andrew);
        }

        if (userRepository.findByFirstName("Chris").isEmpty()) {
            User chris = new User("Chris", "Cooper", "ccooper@test.com");
            userRepository.save(chris);
        }

        if (userRepository.findByFirstName("David").isEmpty()) {
            User david = new User("David", "Fee", "dfee@test.com");
            userRepository.save(david);
        }

        if (userRepository.findByFirstName("Rory").isEmpty()) {
            User rory = new User("Rory", "Powell", "rpowell@test.com");
            userRepository.save(rory);
        }
    }
}