package io.corementor.finexp.inventory.productType.repository;

import io.corementor.finexp.inventory.productType.domain.ProductTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import psychemesh.framework.common.util.EEntityLifeCycle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Interface IProductTypeRepository.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Repository
public interface IProductTypeRepository  extends JpaRepository<ProductTypeEntity, UUID>, JpaSpecificationExecutor<ProductTypeEntity> {
    /**
     * The find all by state
     * @param state the state
     * @return  list
     */
    List<ProductTypeEntity> findAllByState(EEntityLifeCycle state);
    /**
     * The find By Id And State
     * @param state the state
     * @return  list
     */
    Optional<ProductTypeEntity> findByIdAndState(UUID id, EEntityLifeCycle state);
}
