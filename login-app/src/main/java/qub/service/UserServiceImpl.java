package qub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import qub.domain.User;
import qub.repositories.UserRepository;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    // For use by spring data
    protected UserServiceImpl() {}

    @Override
    public User getUserByEmail(String email) {
        Assert.notNull(email, "The email must not be null");

        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteUser(String email) {
        Assert.notNull(email, "The email must not be null");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.info("No user found with the email " + email);
        } else {
            userRepository.delete(user);
            log.info("Deleted user: " + user.toString());
        }
    }

    @Override
    public void updateUser(String email, User updatedUser) {
        Assert.notNull(email, "The email name must not be null");
        Assert.notNull(updatedUser, "The updated user must not be null");

        User originalUser = userRepository.findByEmail(email);

        if (originalUser == null) {
            log.info("No user found with the email " + email);
        } else {
            updatedUser.setId(originalUser.getId());
            userRepository.save(updatedUser);
            log.info("User: " + originalUser.toString() + " updated to " + updatedUser.toString());
        }
    }

    @Override
    public void createUser(String firstName, String lastName, String email) {
        Assert.notNull(firstName, "The first name must not be null");
        Assert.notNull(lastName, "The last name must not be null");
        Assert.notNull(email, "The email name must not be null");

        User user = userRepository.findByEmail(email);

        if (user != null) {
            log.info("User already exists: " + user.toString());
        } else {
            user = new User(firstName, lastName, email);

            userRepository.save(user);
            log.info("User created: " + user.toString());
        }
    }
}
