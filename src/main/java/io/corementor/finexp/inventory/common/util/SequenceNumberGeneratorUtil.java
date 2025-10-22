package io.corementor.finexp.inventory.common.util;

import io.corementor.finexp.base.ISequenceNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The class SequenceNumberGeneratorUtil.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SequenceNumberGeneratorUtil {

    /** The sequence number service. */
    private final ISequenceNumberService sequenceNumberService;

    /**
     * Number identifier generator
     *
     * @param type the type
     * @param prefix the prefix
     * @return string
     */
    public String getIdentifier(ESequenceType type, ESequencePrefix prefix) {
        return switch (type) {
            case PRODUCT_TYPE -> generateProductTypeCode(prefix);
            case PURCHASE_ORDER -> generatePurchaseOrderCode(prefix);
            case SALES_ORDER -> generateSalesOrderCode(prefix);
            default -> throw new IllegalArgumentException("Unsupported sequence type: " + type);
        };
    }

    /**
     * generate product type code
     * @param prefix the prefix
     * @return string
     */
    private String generateProductTypeCode(ESequencePrefix prefix) {
        long nextSequenceNumber = sequenceNumberService.getNextSequenceNumber(ESequenceType.PRODUCT_TYPE);
        return String.format("%s%03d", prefix, nextSequenceNumber);
    }

    /**
     * generate purchase order code
     * @param prefix the prefix
     * @return string
     */
    private String generatePurchaseOrderCode(ESequencePrefix prefix) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        String formattedYear = now.format(yearFormatter);
        long nextSequenceNumber = sequenceNumberService.getNextSequenceNumber(ESequenceType.PURCHASE_ORDER);
        return String.format("%s%s-%03d", prefix, formattedYear, nextSequenceNumber);
    }

    /**
     * generate sales order code
     * @param prefix the prefix
     * @return string
     */
    private String generateSalesOrderCode(ESequencePrefix prefix) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        String formattedYear = now.format(yearFormatter);
        long nextSequenceNumber = sequenceNumberService.getNextSequenceNumber(ESequenceType.SALES_ORDER);
        return String.format("%s%s-%03d", prefix, formattedYear, nextSequenceNumber);
    }

}
