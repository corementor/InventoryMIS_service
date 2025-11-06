package io.corementor.finexp.orchestrator.inventory;

import io.corementor.finexp.core.inventory.productType.service.ProductTypeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The Class Product type orchestrator query processor
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class ProductTypeOrchestratorQueryProcessor {
    /**
     * The product type query service
     */
    private final ProductTypeQueryService productTypeQueryService;

    /**
     * count product types
     * @return int
     */
    public int countProductTypes(){
        return productTypeQueryService.countProductTypes();
    }


}
