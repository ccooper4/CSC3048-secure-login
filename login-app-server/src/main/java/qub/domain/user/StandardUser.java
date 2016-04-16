package qub.domain.user;

import javax.persistence.Entity;

/**
 * A standard user of the application
 */
@Entity
public class StandardUser extends User {

    protected StandardUser() {}

    public StandardUser(String first, String last) {
        super(first, last);
    }
}
