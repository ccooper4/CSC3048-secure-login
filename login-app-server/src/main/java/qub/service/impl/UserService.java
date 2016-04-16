package qub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import qub.domain.user.AdminUser;
import qub.domain.user.User;
import qub.domain.user.StandardUser;
import qub.repositories.UserRepository;
import qub.service.IUserService;
import qub.util.LoginIdGenerator;
import util.EncryptedLogger;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {
    
    private EncryptedLogger log = new EncryptedLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    // For use by spring data
    protected UserService() {}

    @Override
    public List<User> getUsersByFirstName(String name) {
        Assert.notNull(name, "The name must not be null");
        return new ArrayList<>(userRepository.findByFirstName(name));
    }

    @Override
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

    @Override
    public User createStandardUser(String firstName, String lastName, String password) {
        StandardUser standardUser = new StandardUser(firstName, lastName);
        return createUser(standardUser, password);
    }

    @Override
    public User createAdminUser(String firstName, String lastName, String password) {
        AdminUser adminUser = new AdminUser(firstName, lastName);
        return createUser(adminUser, password);
    }

    private User createUser(User user, String password) {
        // Generate a new user id
        String newLoginId = LoginIdGenerator.newId();

        // Ensure ID does not already exist
        while (getUserByLoginId(newLoginId) != null) {
            newLoginId = LoginIdGenerator.newId();
        }

        user.setLoginId(newLoginId);
        user.setPassword(password);

        saveUser(user);
        return user;
    }
}
