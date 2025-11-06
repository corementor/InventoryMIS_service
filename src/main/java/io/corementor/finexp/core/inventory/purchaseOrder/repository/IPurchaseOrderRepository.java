package io.corementor.finexp.core.inventory.purchaseOrder.repository;

import io.corementor.finexp.common.EOrderHistoryStatus;
import io.corementor.finexp.core.inventory.purchaseOrder.domain.PurchaseOrderEntity;
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
 * The Interface IPurchaseOrder Repository
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Repository
public interface IPurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, UUID>, JpaSpecificationExecutor<PurchaseOrderEntity> {

    /**
     * Find all active orders with active items
     *
     * @param state EEntityLifeCycle
     * @return List of Purchase order entity
     */
    @Query("""
            SELECT DISTINCT po
            FROM PurchaseOrderEntity po
            LEFT JOIN FETCH po.orderItems poi
            WHERE po.state = :state
              AND (poi.state = :state OR poi IS NULL)
            """)
    List<PurchaseOrderEntity> findAllActiveOrdersWithActiveItems(@Param("state") EEntityLifeCycle state);

    /**
     * Find purchase orders by purchase code
     *
     * @param purchaseCode the purchase code
     * @return response
     */

    Optional<PurchaseOrderEntity> findByPurchaseCode(String purchaseCode);

    /**
     * Find purchase orders by product order item entity
     *
     * @param itemId the UUID
     * @return Optional value of Purchase order entity
     */

    @Query("SELECT po FROM PurchaseOrderEntity po JOIN po.orderItems poi WHERE poi.id=:itemId")
    Optional<PurchaseOrderEntity> findPurchaseOrderEntityByProductOrderItemEntity(@Param("itemId") UUID itemId);

    /**
     * Count All By state
     *
     * @param state EEntityLifeCycle
     * @return Integer
     */
    int countAllByState(EEntityLifeCycle state);

    /**
     * Count All By status and state
     *
     * @param status EEntityLifeCycle
     * @param state  EEntityLifeCycle
     * @return Integer
     */
    int countAllByStatusAndState(EOrderHistoryStatus status, EEntityLifeCycle state);
}
