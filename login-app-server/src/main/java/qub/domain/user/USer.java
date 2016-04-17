package qub.domain.user;

import qub.domain.BaseEntity;
import qub.domain.IssuedToken;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A StandardUser of the application
 */
@Entity
@Inheritance
public class User extends BaseEntity {

    //region Fields

    private String loginId;
    private String password;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<IssuedToken> issuedTokens;

    // For use by spring data
    protected User() {}

    //endregion

    //region Constructor

    /**
     * Constructor.
     * @param firstName The first name.
     * @param lastName  The last name.
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //endregion

    //region Getters & Setters

    /**
     * Gets the login id.
     * @return The login id.
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * Sets the login id.
     * @param loginId The login id.
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Gets the password.
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * @param password The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the last name.
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     * @param lastName The last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the first name.
     * @return Gets the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     * @param firstName The first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the list of issued tokens.
     * @return A set of issued tokens.
     */
    public Set<IssuedToken> getIssuedTokens() {
        return issuedTokens;
    }

    /**
     * Adds a new issued token to the entity.
     * @param issuedToken The issued token.
     */
    public void addIssuedToken(IssuedToken issuedToken)
    {
        if (issuedTokens == null) {
            issuedTokens = new HashSet<>();
        }

        issuedTokens.add(issuedToken);
    }

    //endregion

    //region Methods

    @Override
    public String toString() {
        return String.format("StandardUser[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    //endregion

}

