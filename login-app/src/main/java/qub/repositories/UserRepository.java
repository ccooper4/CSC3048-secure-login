package qub.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import qub.domain.User;

import java.util.Collection;

@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * This method will find an {@link User} instance in the database by its first name.
     *
     * It is not implemented, its working code will be automatically generated from its
     * signature by Spring Data JPA.
     *
     * The @param tag is used to match to the field in the {@link User} domain object
     *
     * The @param tag can be excluded if the parameter matches the field already but it
     * is good practice to leave it in in case of future modifications.
     */
    Collection<User> findByFirstName(@Param("firstName") String first);

    Collection<User> findByLastName(@Param("lastName") String last);

    User findByEmail(@Param("email") String email);

}
