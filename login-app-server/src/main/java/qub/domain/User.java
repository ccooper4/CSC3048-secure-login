package qub.domain;

import qub.util.UserRole;

import javax.persistence.*;

/**
 * A User of the application
 */
@Entity
public class User extends BaseEntity {

    private String loginId;
    private String password;

    private String role;

    private String firstName;
    private String lastName;

    // For use by spring data
    protected User() {}

    /**
     * Constructor.
     * @param firstName The first name.
     * @param lastName  The last name.
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(UserRole userRole) {
        setRole(userRole.toString());
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, firstName='%s', lastName='%s']",
                                id, firstName, lastName);
    }

}
