package io.corementor.finexp.security.service;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.security.domain.RoleEntity;
import io.corementor.finexp.security.domain.UserEntity;
import io.corementor.finexp.security.repository.IRoleRepository;
import io.corementor.finexp.security.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The User service class.
 *
 * @author BLAISE MUGISHA
 * @version 1.0
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    /**
     * The user repository.
     */
    private final IUserRepository userRepository;

    /**
     * The user query service
     */

    private final UserQueryServiceProcessor userQueryServiceProcessor;
    /**
     * The role repository
     */
    private final IRoleRepository roleRepository;
    /**
     * The role service
     */
    private final RoleService roleService;

    /**
     * The role query service
     */
    private final RoleQueryServiceProcessor roleQueryServiceProcessor;


    /**
     * The password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The admin email.
     */
    @Value("${admin.email}")
    private String adminEmail;
    /**
     * The admin password.
     */
    @Value("${admin.password}")
    private String adminPassword;
    /**
     * The admin role
     */
    @Value("${admin.role}")
    private String administratorRole;
    /**
     * The admin first name
     */
    @Value("${admin.firstname}")
    private String adminFirstName;
    /**
     * The admin last name
     */
    @Value("${admin.lastname}")
    private String adminLastName;
    /**
     * The admin phone number
     */
    @Value("${admin.phone}")
    private String adminPhoneNumber;

    /**
     * create user
     *
     * @param userEntity the user entity
     * @return response
     */
    public Response<UserEntity> createUser(UserEntity userEntity) {

        if (userEntity == null) {
            return new Response<>(IMessage.INVALID_INPUT);
        }


        Optional<UserEntity> optionalUser = userRepository.findUserEntityByEmail(userEntity.getEmail());
        Optional<UserEntity> optionalUserByPhoneNumber = userRepository.findUserEntityByPhoneNumber(userEntity.getPhoneNumber());
        if (optionalUser.isPresent() || optionalUserByPhoneNumber.isPresent()) {
            return new Response<>(IUserMessage.ENTRY_ALREADY_EXISTS);
        } else {
            if (userEntity.getRole() != null) {
                Set<RoleEntity> managedRoles = new HashSet<>();
                for (RoleEntity role : userEntity.getRole()) {
                    RoleEntity managedRole = roleRepository.findById(role.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Role not found"));
                    managedRoles.add(managedRole);
                }
                userEntity.setRole(managedRoles);
            }
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setCreatedAt(LocalDateTime.now());
            return new Response<>(userRepository.save(userEntity));
        }
    }

    /**
     * update user
     *
     * @param theUser the user entity
     * @return response
     */
    public Response<UserEntity> updateUser(UserEntity theUser) {
        try {
            if (theUser == null) {
                return new Response<>(IMessage.INVALID_INPUT);
            }


            Response<UserEntity> optionalUser = userQueryServiceProcessor.findUserByEntity(theUser);
            UserEntity found = optionalUser.getData();


            Optional.ofNullable(theUser.getFirstName()).ifPresent(found::setFirstName);
            Optional.ofNullable(theUser.getLastName()).ifPresent(found::setLastName);
            Optional.ofNullable(theUser.getEmail()).ifPresent(found::setEmail);
            Optional.ofNullable(theUser.getPhoneNumber()).ifPresent(found::setPhoneNumber);
            Optional.ofNullable(passwordEncoder.encode(theUser.getPassword())).ifPresent(found::setPassword);

            if (theUser.getRole() != null && !theUser.getRole().isEmpty()) {
                RoleEntity roleDto = theUser.getRole().stream().findFirst().get();
                RoleEntity theRole = roleQueryServiceProcessor.findByRoleNameAndState(roleDto.getRoleName()).getData();
//                Set<RoleEntity> roles =found.getRole();
//                roles.add(theRole);
//                found.setRole(roles);
                found.getRole().add(theRole);
            }


            return new Response<>(userRepository.save(found), IUserMessage.INFORMATION_UPDATED);
        } catch (Exception e) {
            e.printStackTrace();
            assert theUser != null;
            throw new ObjectNotFoundException(theUser, "Your user : {} is not found" + theUser.getFirstName());
        }
    }


    /**
     * Delete user
     *
     * @param userEntity the user entity
     * @return response
     */
    public Response<UserEntity> deleteUser(UserEntity userEntity) {
        Optional<UserEntity> optionalUser = userRepository.findById(userEntity.getId());
        if (optionalUser.isPresent()) {
            userEntity.setState(EEntityLifeCycle.INACTIVE);
            return new Response<>(userRepository.save(userEntity));
        } else {
            return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
        }
    }

    @Transactional
    public void createAdminUser() {
        Optional<UserEntity> existingAdmin = userRepository.findUserEntityByEmail(adminEmail);

        if (existingAdmin.isPresent()) {
            return;
        }

        log.info("------- Initial setup running: Creating admin user -------");

        UserEntity adminUser = new UserEntity();

        adminUser.setEmail(adminEmail.toLowerCase());
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setFirstName(adminFirstName.toLowerCase());
        adminUser.setLastName(adminLastName.toLowerCase());
        adminUser.setPhoneNumber(adminPhoneNumber.trim());
        adminUser.setState(EEntityLifeCycle.ACTIVE);
        adminUser.setCreatedAt(LocalDateTime.now());
        RoleEntity adminRole;
        try {
            adminRole = roleQueryServiceProcessor.findByRoleNameAndState(administratorRole).getData();
        } catch (ObjectNotFoundException ex) {
            adminRole = roleService.createRole(administratorRole).getData();
        }
        adminUser.setRole(Set.of(adminRole));

        UserEntity savedUser = userRepository.save(adminUser);
        log.info("Initial admin user created successfully with email: {}", savedUser.getEmail());
        log.info("Initial admin user created successfully with password: {}", adminPassword);
    }

}
