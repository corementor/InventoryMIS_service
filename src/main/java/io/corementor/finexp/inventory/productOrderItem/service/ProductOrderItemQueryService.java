package io.corementor.finexp.inventory.productOrderItem.service;

import io.corementor.finexp.inventory.base.IMessage;
import io.corementor.finexp.inventory.productOrderItem.domain.ProductOrderItemEntity;
import io.corementor.finexp.inventory.productOrderItem.repository.IProductOrderItemRepository;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class ProductOrderItemQueryService.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOrderItemQueryService {
    /**
     * The Product order Item repository
     */
    private final IProductOrderItemRepository productOrderItemRepository;


    /**
     * Find purchase Item by id
     *
     * @param id the id
     * @return response
     */
    public Response<ProductOrderItemEntity> findProductOrderItemById(UUID id, EEntityLifeCycle state) {
        ProductOrderItemEntity productType = productOrderItemRepository.findByIdAndState(id, state)
                .orElseThrow(() -> new ObjectNotFoundException(IMessage.INFORMATION_NOT_FOUND, "Product type  object not found"));
        return new Response<>(productType, IMessage.INFORMATION_FOUND);
    }

    /**
     * Find all by Purchase order and state
     *
     * @param thePurchaseOrder the Purchase order
     * @return response
     */
    public Response<List<ProductOrderItemEntity>> findAllByPurchaseOrder(PurchaseOrderEntity thePurchaseOrder) {
        List<ProductOrderItemEntity> productOrderItemEntityList = productOrderItemRepository.findAllByPurchaseOrderEntityAndState(thePurchaseOrder, EEntityLifeCycle.ACTIVE);

        return
                new Response<>(productOrderItemEntityList, productOrderItemEntityList.isEmpty() ? IUserMessage.INFORMATION_NOT_FOUND : IUserMessage.INFORMATION_FOUND);
    }

    /**
     * find All Product Item
     *
     * @return response
     */
    public Response<List<ProductOrderItemEntity>> findAllProductOrderItems() {
        List<ProductOrderItemEntity> purchaseOrderList = productOrderItemRepository
                .findAllByState(EEntityLifeCycle.ACTIVE);

        return new Response<>(purchaseOrderList, IUserMessage.INFORMATION_FOUND);
    }

}
