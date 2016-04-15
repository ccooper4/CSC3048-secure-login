package qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import qub.domain.User;
import qub.repositories.UserRepository;
import qub.util.LoginIdGenerator;
import util.EncryptedLogger;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    
    private EncryptedLogger log = new EncryptedLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    // For use by spring data
    protected UserServiceImpl() {}

    @Override
    public List<User> getUsersByFirstName(String name) {
        Assert.notNull(name, "The name must not be null");
        return new ArrayList<>(userRepository.findByFirstName(name));
    }

    public User saveUser(User user) {
        Assert.notNull(user, "The user must not be null");
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUserByLoginId(String loginId) {
        Assert.notNull(loginId, "The login id must not be null");
        return userRepository.findByLoginId(loginId);
    }

    public User createUser(String firstName, String lastName, String password) {
        User newUser = new User(firstName, lastName);

        // Generate a new user id
        String newLoginId = LoginIdGenerator.newId();

        // Ensure ID does not already exist
        while (getUserByLoginId(newLoginId) != null) {
            newLoginId = LoginIdGenerator.newId();
        }

        newUser.setLoginId(newLoginId);
        newUser.setPassword(password);

        saveUser(newUser);
        return newUser;
    }

}
