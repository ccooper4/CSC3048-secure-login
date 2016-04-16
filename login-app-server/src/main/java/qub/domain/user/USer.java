package qub.domain.user;

import qub.domain.BaseEntity;

import javax.persistence.*;

/**
 * A StandardUser of the application
 */
@Entity
@Inheritance
public class User extends BaseEntity {

    private String loginId;
    private String password;

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
        return String.format("StandardUser[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

}

