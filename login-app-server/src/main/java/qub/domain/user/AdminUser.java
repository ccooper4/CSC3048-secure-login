package qub.domain.user;

import javax.persistence.Entity;

/**
 * An admin user of the application.
 */
@Entity
public class AdminUser extends User {

    protected AdminUser() {}

    public AdminUser(String first, String last) {
        super(first, last);
    }
}
