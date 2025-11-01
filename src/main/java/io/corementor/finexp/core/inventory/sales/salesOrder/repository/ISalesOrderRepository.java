package io.corementor.finexp.core.inventory.sales.salesOrder.repository;

import io.corementor.finexp.core.inventory.sales.salesOrder.domain.SalesOrderEntity;
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
 * The Interface Sale Order Repository
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Repository
public interface ISalesOrderRepository extends JpaRepository<SalesOrderEntity, UUID>, JpaSpecificationExecutor<SalesOrderEntity> {
    /**
     * Find all sales order by state
     *
     * @param state state
     * @return List
     */
    List<SalesOrderEntity> findAllByState(EEntityLifeCycle state);

    /**
     * Find all sales order with active items
     * @param state the EEntityLifeCycle
     * @return List of Sales order entity
     */
    @Query("""
            SELECT DISTINCT so
            FROM SalesOrderEntity so
            LEFT JOIN FETCH so.orderItems poi
            WHERE so.state = :state
              AND (poi.state = :state OR poi IS NULL)
            """)
    List<SalesOrderEntity> findAllActiveOrdersWithActiveItems(@Param("state") EEntityLifeCycle state);

    /**
     * Find sales order by code
     *
     * @param saleCode saleCode
     * @return Optional value of Sales order entity
     */
    Optional<SalesOrderEntity> findBySaleCode(String saleCode);

    /**
     * Find sales order by product order item entity
     * @param itemId the UUID
     * @return Optional value of Sales order entity
     */
    @Query("SELECT so FROM SalesOrderEntity so JOIN so.orderItems soi WHERE soi.id=:itemId")
    Optional<SalesOrderEntity> findSalesOrderByProductOrderItemEntity(@Param("itemId") UUID itemId);

}
