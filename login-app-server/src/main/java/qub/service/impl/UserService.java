package qub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import qub.domain.IssuedToken;
import qub.domain.user.AdminUser;
import qub.domain.user.User;
import qub.domain.user.StandardUser;
import qub.repositories.IssuedTokenRepository;
import qub.repositories.UserRepository;
import qub.service.ICryptoHashingService;
import qub.service.IUserService;
import qub.util.LoginIdGenerator;
import util.EncryptedLogger;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {
    
    private EncryptedLogger log = new EncryptedLogger(getClass());

    /**
     * The crypto service.
     */
    @Autowired
    private ICryptoHashingService cryptoHashingService;

    @Autowired
    private UserRepository userRepository;

    /**
     * The token repository.
     */
    @Autowired
    private IssuedTokenRepository tokenRepository;

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
        log.info("Saving user: " + user.toString());
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUserByLoginId(String loginId) {
        Assert.notNull(loginId, "The login id must not be null");
        log.info("Retrieving user with id: " + loginId);
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

    /**
     * Removes the specified token from the database.
     * @param token The token
     */
    @Override
    public void deleteTokenFromRepository(IssuedToken token) {

        tokenRepository.delete(token);

    }

    private User createUser(User user, String password) {
        // Generate a new user id
        String newLoginId = LoginIdGenerator.newId();

        // Ensure ID does not already exist
        while (getUserByLoginId(newLoginId) != null) {
            newLoginId = LoginIdGenerator.newId();
        }

        user.setLoginId(newLoginId);
        user.setPassword(cryptoHashingService.hashString(password));

        saveUser(user);
        return user;
    }
}
