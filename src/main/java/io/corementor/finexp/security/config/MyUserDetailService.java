package io.corementor.finexp.security.config;


import io.corementor.finexp.security.domain.UserEntity;
import io.corementor.finexp.security.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The user detail service implementation.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    /**
     * The user repository.
     */
    private final IUserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repository.findUserEntityByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
