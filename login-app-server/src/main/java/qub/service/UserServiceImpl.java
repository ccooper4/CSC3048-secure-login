package qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import qub.domain.User;
import qub.repositories.UserRepository;

import static util.EncryptedLogger.*;

@Service
public class UserServiceImpl implements IUserService {

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
            info("No user found with the email " + email);
        } else {
            userRepository.delete(user);
            info("Deleted user: " + user.toString());
        }
    }

    @Override
    public void updateUser(String email, User updatedUser) {
        Assert.notNull(email, "The email name must not be null");
        Assert.notNull(updatedUser, "The updated user must not be null");

        User originalUser = userRepository.findByEmail(email);

        if (originalUser == null) {
            info("No user found with the email " + email);
        } else {
            updatedUser.setId(originalUser.getId());
            userRepository.save(updatedUser);
            info("User: " + originalUser.toString() + " updated to " + updatedUser.toString());
        }
    }

    @Override
    public void createUser(String firstName, String lastName, String email) {
        Assert.notNull(firstName, "The first name must not be null");
        Assert.notNull(lastName, "The last name must not be null");
        Assert.notNull(email, "The email name must not be null");

        User user = userRepository.findByEmail(email);

        if (user != null) {
            info("User already exists: " + user.toString());
        } else {
            user = new User(firstName, lastName, email);

            userRepository.save(user);
            info("User created: " + user.toString());
        }
    }
}
