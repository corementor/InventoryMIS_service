package io.corementor.finexp.core.inventory.productType.service;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.core.inventory.productType.domain.ProductTypeEntity;
import io.corementor.finexp.core.inventory.productType.repository.IProductTypeRepository;
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
 * The Class ProductTypeQueryService.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductTypeQueryService {
    /**
     * The Product Type Repository
     */
    private final IProductTypeRepository productTypeRepository;


    /**
     * Find purchase order by id
     *
     * @param id the id
     * @return response
     */
    public Response<ProductTypeEntity> findProductTypeById(UUID id) {
        ProductTypeEntity productType = productTypeRepository.findByIdAndState(id, EEntityLifeCycle.ACTIVE)
                .orElseThrow(() -> new ObjectNotFoundException(IMessage.INFORMATION_NOT_FOUND, "Product type  object not found"));
        return new Response<>(productType, IMessage.INFORMATION_FOUND);
    }

    /**
     * find All Product Types
     *
     * @return response
     */
    public Response<List<ProductTypeEntity>> findAllProductTypes() {
        List<ProductTypeEntity> purchaseOrderList = productTypeRepository
                .findAllByState(EEntityLifeCycle.ACTIVE);

        return new Response<>(purchaseOrderList, IUserMessage.INFORMATION_FOUND);
    }

    /**
     * count product types
     *
     * @return int
     */
    public int countProductTypes() {
        return
                productTypeRepository.countAllByState(EEntityLifeCycle.ACTIVE);
    }
}
