package io.corementor.finexp.rest.inventory;

import io.corementor.finexp.core.inventory.productType.domain.ProductTypeEntity;
import io.corementor.finexp.core.inventory.productType.service.ProductTypeQueryService;
import io.corementor.finexp.core.inventory.productType.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class ProductTypeResource.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/productType")
public class ProductTypeResource {
    /**
     * The ProductTypeService
     */
    private final ProductTypeService productTypeService;

    /**
     * The product type query service.
     */
    private final ProductTypeQueryService productTypeQueryService;


    /**
     * Create product resource .
     *
     * @param theProductType the purchase order
     * @return response
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Response<ProductTypeEntity> createProductType(@RequestBody ProductTypeEntity theProductType) {
        return productTypeService.createProductType(theProductType);
    }

    /**
     * Find product type order by id.
     *
     * @param id the id
     * @return response
     */
    @PostMapping("/search/criteria/id")
    @ResponseStatus(HttpStatus.OK)
    public Response<ProductTypeEntity> findProductTypeById(@RequestBody String id) {
        return productTypeQueryService.findProductTypeById(UUID.fromString(id));
    }

    /**
     * Find All product type order by id.
     *
     * @return response
     */
    @GetMapping("/search/criteria/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<ProductTypeEntity>> findAllProductTypeById() {
        return productTypeQueryService.findAllProductTypes();
    }

    /**
     * update product type
     *
     * @param theProductType the product type
     * @return response
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Response<ProductTypeEntity> updateProductType(@RequestBody ProductTypeEntity theProductType) {
        return productTypeService.updateProductType(theProductType);
    }

    /**
     * delete product
     *
     * @param theProductType the product type
     * @return response
     */

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public Response<ProductTypeEntity> deleteProductType(@RequestBody ProductTypeEntity theProductType) {
        return productTypeService.deleteProductType(theProductType);
    }


}
