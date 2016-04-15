package qub.domain;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    protected void setId(Long id) {
        this.id = id;
    }

    protected Long getId() {
        return id;
    }

}
