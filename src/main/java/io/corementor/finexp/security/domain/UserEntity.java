package io.corementor.finexp.security.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import psychemesh.framework.domain.AbstractBaseEntity;

import java.util.*;

/**
 * The User entity class.
 *
 * @author BLAISE MUGISHA
 * @version 1.0
 */
@Entity
@Getter
@Setter
@Table(name = "sec_users", schema = "security")
public class UserEntity extends AbstractBaseEntity implements UserDetails {
    /**
     * The firstname.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;
    /**
     * The lastname.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * The password.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The email.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * The phone number.
     */
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /**
     * The role.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_has_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> role = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity roleEntity : role) {
            authorities.add(new SimpleGrantedAuthority(roleEntity.getRoleName()));
        }
        return authorities;

    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
