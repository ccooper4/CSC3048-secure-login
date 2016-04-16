package qub.service;

import qub.domain.user.User;

import java.util.List;

/**
 * Outlines a service for interacting with users.
 */
public interface IUserService {

    /**
     * Get a list of users for a given first name.
     * @param name  The first name.
     * @return      The users with that first name.
     */
    List<User> getUsersByFirstName(String name);

    /**
     * Save a user to the database.
     * @param user  The user to save.
     * @return      The saved user.
     */
    User saveUser(User user);

    /**
     * Get a user for a given login ID.
     * @param loginId   The login ID.
     * @return          The user with that login ID.
     */
    User getUserByLoginId(String loginId);

    /**
     * Create a new normal user.
     * @param firstName The first name.
     * @param lastName  The last name.
     * @param password  The password.
     * @return          The saved user.
     */
    User createStandardUser(String firstName, String lastName, String password);

    /**
     * Create a new Admin user.
     * @param firstName The first name.
     * @param lastName  The last name.
     * @param password  The password.
     * @return          The saved user.
     */
    User createAdminUser(String firstName, String lastName, String password);
}
