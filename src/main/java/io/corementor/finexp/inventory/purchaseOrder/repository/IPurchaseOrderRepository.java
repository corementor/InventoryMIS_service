package io.corementor.finexp.inventory.purchaseOrder.repository;

 import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import psychemesh.framework.common.util.EEntityLifeCycle;


import java.util.List;
import java.util.UUID;

/**
 * The Interface IPurchaseOrder Repository
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Repository
public interface IPurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, UUID>, JpaSpecificationExecutor<PurchaseOrderEntity> {


    List<PurchaseOrderEntity> findAllByState(EEntityLifeCycle state);
}
