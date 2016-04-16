package qub.service;

import qub.domain.user.User;

import java.util.List;

public interface IUserService {

    List<User> getUsersByFirstName(String name);

    User saveUser(User user);

    User getUserByLoginId(String loginId);

    User createUser(String firstName, String lastName, String password);

    User createAdminUser(String firstName, String lastName, String password);
}
