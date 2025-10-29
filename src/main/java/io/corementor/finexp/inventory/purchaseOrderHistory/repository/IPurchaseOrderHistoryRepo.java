package io.corementor.finexp.inventory.purchaseOrderHistory.repository;

import io.corementor.finexp.inventory.purchaseOrderHistory.domain.PurchaseOrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * The Interface Purchase Order History Repo
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Repository
public interface IPurchaseOrderHistoryRepo extends JpaRepository<PurchaseOrderHistoryEntity, UUID>, JpaSpecificationExecutor<PurchaseOrderHistoryEntity> {
    List<PurchaseOrderHistoryEntity> findByPurchaseOrderIdOrderByCreatedAtDesc(UUID purchaseOrderId);
}
