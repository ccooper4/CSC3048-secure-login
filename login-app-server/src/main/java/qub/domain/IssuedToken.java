package qub.domain;

import qub.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Provides a reference list of all tokens issued for a user.
 */
@Entity
public class IssuedToken extends BaseEntity {

    //region Fields

    /**
     * The token id.
     */
    private String tokenId;

    @ManyToOne
    private User user;

    //endregion

    //region Getters & Setters.

    /**
     * Gets the token id.
     * @return The token id.
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * Sets the token id.
     * @param tokenId The token id.
     */
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * Gets the user.
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user.
     * @param user The user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    //endregion

}
