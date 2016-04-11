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
public class UserLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    private Logger log = Logger.getLogger(UserLoader.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        User andrew = new User("Andrew", "Fletcher", "afletcher@test.com");
        User chris = new User("Chris", "Cooper", "ccooper@test.com");
        User david = new User("David", "Fee", "dfee@test.com");
        User rory = new User("Rory", "Powell", "rpowell@test.com");

        userRepository.save(andrew);
        userRepository.save(chris);
        userRepository.save(david);
        userRepository.save(rory);
    }
}