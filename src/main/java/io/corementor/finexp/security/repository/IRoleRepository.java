package io.corementor.finexp.security.repository;

import io.corementor.finexp.security.domain.RoleEntity;
import io.corementor.finexp.security.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Role repository interface.
 *
 * @author BLAISE MUGISHA
 * @version 1.0
 */
@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, UUID>, JpaSpecificationExecutor<RoleEntity> {
    @Query("SELECT r FROM RoleEntity r WHERE r.roleName = :roleName AND r.state = :state")
    Optional<RoleEntity> findAllByRoleNameAndState(@Param("roleName") String roleName, EEntityLifeCycle state);

    List<RoleEntity> findAllByState(EEntityLifeCycle state);

}
