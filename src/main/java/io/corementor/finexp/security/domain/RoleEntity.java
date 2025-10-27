package io.corementor.finexp.security.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import psychemesh.framework.domain.AbstractBaseEntity;

/**
 * The User entity class.
 * @author Blaise Mugisha
 * @version 1.0
 */
@Entity
@Getter
@Setter
@Table(name = "roles", schema = "security")
public class RoleEntity extends AbstractBaseEntity {
    @Column(name = "role_name", nullable = false,unique = true)
    private String roleName;
}
