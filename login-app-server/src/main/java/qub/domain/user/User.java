package qub.domain.user;

import java.util.Date;
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
    private int loginAttemptsSinceLastUnsuccessful;
    private Date lockedOutUntil;
    private Date lastSuccessfulLogin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<IssuedToken> issuedTokens;

    // For use by spring data
    protected User() {
    }

    //endregion

    //region Constructor
    /**
     * Constructor.
     *
     * @param firstName The first name.
     * @param lastName The last name.
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginAttemptsSinceLastUnsuccessful = 0;
        this.lockedOutUntil = null;
    }

    //endregion

    //region Getters & Setters
    /**
     * Gets the login id.
     *
     * @return The login id.
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * Sets the login id.
     *
     * @param loginId The login id.
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Gets the password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the last name.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName The last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the first name.
     *
     * @return Gets the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName The first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the time this user is locked out until.
     *
     * @return Gets the time this user is locked out until.
     */
    public Date getLockedOutUntil() {
        return lockedOutUntil;
    }

    /**
     * Sets the locked out until datetime. 5 minutes from now
     *
     */
    public void setLockedOut() {
        this.lockedOutUntil = new Date(System.currentTimeMillis() + (5 * 60 * 1000));
    }

    /**
     * gets the amount of unsuccessful login attempts since the last successful
     * one
     *
     * @return the amount of unsuccessful login attempts since the last
     * successful one
     */
    public int getLoginAttemptsSinceLastUnsuccessful() {
        return loginAttemptsSinceLastUnsuccessful;
    }

    /**
     * increments the amount of unsuccessful login attempts since the last
     * successful one
     */
    public void incrementLoginAttemptsSinceLastUnsuccessful() {
        this.loginAttemptsSinceLastUnsuccessful = getLoginAttemptsSinceLastUnsuccessful() + 1;
        if (this.getLoginAttemptsSinceLastUnsuccessful() >= 3) {
            this.setLockedOut();
            this.resetLoginAttemptsSinceLastUnsuccessful();
        }
    }

    /**
     * resets the amount of unsuccessful login attempts to 0
     */
    public void resetLoginAttemptsSinceLastUnsuccessful() {
        this.loginAttemptsSinceLastUnsuccessful = 0;
    }

    /**
     * Gets the list of issued tokens.
     *
     * @return A set of issued tokens.
     */
    public Set<IssuedToken> getIssuedTokens() {
        return issuedTokens;
    }

    /**
     * Adds a new issued token to the entity.
     *
     * @param issuedToken The issued token.
     */
    public void addIssuedToken(IssuedToken issuedToken) {
        if (issuedTokens == null) {
            issuedTokens = new HashSet<>();
        }

        issuedTokens.add(issuedToken);
    }

    /**
     * Remove a previously issued token from a user given the token ID.
     *
     * @param tokenId The ID of the token to remove from the user
     */
    public IssuedToken removeIssuedToken(String tokenId) {
        IssuedToken tokenToRemove = null;
        if (issuedTokens != null) {
            for (IssuedToken token : issuedTokens) {
                if (token.getTokenId().equals(tokenId)) {
                    tokenToRemove = token;
                    break;
                }
            }

            if (tokenToRemove != null) {
                issuedTokens.remove(tokenToRemove);
                tokenToRemove.setUser(null);
            }
        }

        return tokenToRemove;
    }

    /**
     * Gets the last succssful login date.
     * @return
     */
    public Date getLastSuccessfulLogin() {
        return lastSuccessfulLogin;
    }

    /**
     * Sets the last successful login date.
     * @param lastSuccessfulLogin
     */
    public void setLastSuccessfulLogin(Date lastSuccessfulLogin) {
        this.lastSuccessfulLogin = lastSuccessfulLogin;
    }

    //endregion

    //region Methods
    @Override
    public String toString() {
        return String.format("StandardUser[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName, loginAttemptsSinceLastUnsuccessful, lockedOutUntil);
    }

    //endregion
}
