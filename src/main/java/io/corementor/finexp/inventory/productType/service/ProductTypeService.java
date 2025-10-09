package io.corementor.finexp.inventory.productType.service;


import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.inventory.common.util.ESequencePrefix;
import io.corementor.finexp.inventory.common.util.ESequenceType;
import io.corementor.finexp.inventory.common.util.SequenceNumberGeneratorUtil;
import io.corementor.finexp.inventory.productType.domain.ProductTypeEntity;
import io.corementor.finexp.inventory.productType.repository.IProductTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.util.Optional;

/**
 * The Class ProductTypeService.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductTypeService {
    /**
     * The Product Type Repository
     */
    private final IProductTypeRepository productTypeRepository;
    /**
     * The sequence number generator util.
     */
    private final SequenceNumberGeneratorUtil sequenceNumberGeneratorUtil;
    /**
     * The product type query service
     */
    private final ProductTypeQueryService productTypeQueryService;

    /**
     * create product type
     *
     * @param theProductType the ProductTypeEntity
     * @return response
     */

    public Response<ProductTypeEntity> createProductType(ProductTypeEntity theProductType) {
        if (theProductType == null) {
            return new Response<>(IMessage.INVALID_INPUT);
        }

        try {

            String productTypeCode = sequenceNumberGeneratorUtil.getIdentifier(
                    ESequenceType.PRODUCT_TYPE, ESequencePrefix.PT);
            theProductType.setProductCode(productTypeCode);

            ProductTypeEntity productType = productTypeRepository.save(theProductType);
            return new Response<>(productType, IUserMessage.INFORMATION_SAVED);
        } catch (Exception ex) {
            log.error("Error creating product type: {}", ex.getMessage(), ex);
            return new Response<>(IUserMessage.INFORMATION_NOT_SAVED);
        }
    }

    /**
     * update product type
     *
     * @param theProductType the ProductTypeEntity
     * @return response
     */
    public Response<ProductTypeEntity> updateProductType(ProductTypeEntity theProductType) {
        if (theProductType == null) {
            return new Response<>(IMessage.INVALID_INPUT);
        }

        try {

            ProductTypeEntity existingProductType = productTypeQueryService.findProductTypeById(theProductType.getId()).getData();


            Optional.ofNullable(theProductType.getProductName()).ifPresent(existingProductType::setProductName);
            Optional.ofNullable(theProductType.getDescription()).ifPresent(existingProductType::setDescription);
            Optional.ofNullable(theProductType.getSize()).ifPresent(existingProductType::setSize);
            Optional.ofNullable(theProductType.getUnitPrice()).ifPresent(existingProductType::setUnitPrice);
            Optional.ofNullable(theProductType.getSellUnitPrice()).ifPresent(existingProductType::setSellUnitPrice);


            ProductTypeEntity productType = productTypeRepository.save(existingProductType);
            return new Response<>(productType, IUserMessage.INFORMATION_UPDATED);
        } catch (Exception ex) {
            log.error("Error creating product type: {}", ex.getMessage(), ex);
            return new Response<>(IUserMessage.ENTITY_IS_NOT_UPDATEABLE);
        }
    }

    /**
     * Delete product type
     *
     * @param theProductType the Product type
     * @return response
     */
    public Response<ProductTypeEntity> deleteProductType(ProductTypeEntity theProductType) {
        if (theProductType == null) {
            return new Response<>(IMessage.INVALID_INPUT);
        }

        try {

            ProductTypeEntity existingProductType = productTypeQueryService.findProductTypeById(theProductType.getId()).getData();
            existingProductType.setState(EEntityLifeCycle.INACTIVE);
            ProductTypeEntity productType = productTypeRepository.save(existingProductType);
            return new Response<>(productType, IUserMessage.INFORMATION_DELETED);
        } catch (Exception ex) {
            log.error("Error creating product type: {}", ex.getMessage(), ex);
            return new Response<>(IUserMessage.INFORMATION_NOT_SAVED);
        }
    }
}
