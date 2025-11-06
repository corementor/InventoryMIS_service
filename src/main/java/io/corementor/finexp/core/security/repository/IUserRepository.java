package io.corementor.finexp.core.security.repository;

import io.corementor.finexp.core.security.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import psychemesh.framework.common.util.EEntityLifeCycle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The User repository interface.
 *
 * @author BLAISE MUGISHA
 * @version 1.0
 */

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findUserEntityByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u  WHERE u.phoneNumber=:phoneNumber")
    Optional<UserEntity> findUserEntityByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    List<UserEntity> findAllByState(EEntityLifeCycle state);

    Optional<UserEntity> findByIdAndState(UUID id, EEntityLifeCycle eEntityLifeCycle);

    Optional<UserEntity> findUserEntityByIdAndState(UUID id, EEntityLifeCycle state);

    int countAllByState(EEntityLifeCycle state);
}
