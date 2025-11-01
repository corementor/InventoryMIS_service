package io.corementor.finexp.core.inventory.sales.salesOrderHistory.repository;

import io.corementor.finexp.core.inventory.sales.salesOrderHistory.domain.SalesOrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * The Interface Sales Order History Repo
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Repository
public interface ISalesOrderHistoryRepo extends JpaRepository<SalesOrderHistoryEntity, UUID>, JpaSpecificationExecutor<SalesOrderHistoryEntity> {
    List<SalesOrderHistoryEntity> findBySalesOrderIdOrderByCreatedAtDesc(UUID salesOrderId);
}
