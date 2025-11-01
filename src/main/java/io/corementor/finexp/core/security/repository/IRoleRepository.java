package io.corementor.finexp.core.security.repository;

import io.corementor.finexp.core.security.domain.RoleEntity;
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

    Optional<RoleEntity> findRoleEntityByIdAndState(UUID id, EEntityLifeCycle state);
}
