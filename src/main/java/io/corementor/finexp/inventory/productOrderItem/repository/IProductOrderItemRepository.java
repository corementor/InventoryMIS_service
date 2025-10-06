package io.corementor.finexp.inventory.productOrderItem.repository;

import io.corementor.finexp.inventory.productOrderItem.domain.ProductOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import psychemesh.framework.common.util.EEntityLifeCycle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Interface IProduct Order Item Repository
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Repository
public interface IProductOrderItemRepository extends JpaRepository<ProductOrderItemEntity, UUID>, JpaSpecificationExecutor<ProductOrderItemEntity> {
   
    List<ProductOrderItemEntity> findAllByState(EEntityLifeCycle state);


    Optional<ProductOrderItemEntity> findByIdAndState(UUID id, EEntityLifeCycle state);

    List<ProductOrderItemEntity> findByPurchaseOrderEntityId(UUID purchaseOrderId);
    @Query("SELECT COALESCE(SUM(poi.totalPriceWithTax), 0) FROM ProductOrderItemEntity poi WHERE poi.purchaseOrderEntity.id = :purchaseOrderId")
    BigDecimal calculateTotalPriceByPurchaseOrderId(@Param("purchaseOrderId") UUID purchaseOrderId);
}
