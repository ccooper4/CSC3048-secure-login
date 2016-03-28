package qub.service;

import qub.domain.User;

public interface IUserService {

    User getUserByEmail(String email);

    void deleteUser(String email);

    void updateUser(String email, User updatedUser);

    void createUser(String firstName, String lastName, String email);

}
