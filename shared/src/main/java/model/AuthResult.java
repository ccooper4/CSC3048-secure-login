package model;

import java.io.Serializable;

/**
 * Represents a user credential used for login.
 */
public class AuthResult implements Serializable {

    //region Private Fields

    /**
     * The Success flag.
     */
    private boolean success;

    /**
     * The error info.
     */
    private String error;

    //endregion

    //region Getters & Setters

    /**
     * Gets Is Success.
     * @return A true or false value.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success flag.
     * @param success The flag value.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets the error text.
     * @return The error text.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error text.
     * @param error The error text.
     */
    public void setError(String error) {
        this.error = error;
    }

    //endregion

}
