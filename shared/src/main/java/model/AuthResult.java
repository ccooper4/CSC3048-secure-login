package model;

import java.io.Serializable;

/**
 * Represents a user credential used for login.
 */
public class AuthResult implements Serializable {

    //region Private Fields

    /**
     * The username.
     */
    private String userName;

    /**
     * The password
     */
    private String passWord;

    //endregion

    //region Getters & Setters

    /**
     * Gets the username.
     * @return The username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username.
     * @param userName The new username.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password.
     * @return The password.
     */
    public String getPassWord() {
        return passWord;
    }


    /**
     * Sets the password.
     * @param passWord The password.
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    //endregion

}
