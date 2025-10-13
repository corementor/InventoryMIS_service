package io.corementor.finexp.sales.salesOrder.repository;

import io.corementor.finexp.sales.salesOrder.domain.SalesOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import psychemesh.framework.common.util.EEntityLifeCycle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Interface Sale Order Repository
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Repository
public interface ISalesOrderRepository extends JpaRepository<SalesOrderEntity, UUID>, JpaSpecificationExecutor<SalesOrderEntity> {
    /**
     * Find all sales order by state
     * @param state state
     * @return List
     */
    List<SalesOrderEntity> findAllByState(EEntityLifeCycle state);

    /**
     * Find sales order by code
     * @param saleCode saleCode
     * @return Optional value of Sales order entity
     */
    Optional<SalesOrderEntity> findBySaleCode(String saleCode);

}
