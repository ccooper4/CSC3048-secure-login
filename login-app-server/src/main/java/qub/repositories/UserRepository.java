package qub.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import qub.domain.user.User;

import java.util.Collection;

@Transactional
public interface UserRepository<T extends User> extends CrudRepository<T, Long> {

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

    User findByLoginId(@Param("loginId") String loginId);
}