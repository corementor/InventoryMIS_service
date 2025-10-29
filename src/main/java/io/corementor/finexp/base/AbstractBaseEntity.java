package io.corementor.finexp.base;
import io.corementor.finexp.common.ELifeCycle;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * The abstract class AbstractBaseEntity.
 *
 * @author Jeremie Ukundwa Tuyisenge
 * @version 1.0
 */
@Deprecated
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractBaseEntity extends AuditEntity{

    /** The id. */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    /** The state. */
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ELifeCycle state=ELifeCycle.ACTIVE;
}
