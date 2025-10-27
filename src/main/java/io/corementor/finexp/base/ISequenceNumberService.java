package io.corementor.finexp.base;


import io.corementor.finexp.inventory.common.util.ESequenceType;

/**
 * The Interface ISequenceNumberService.
 *
 * @author Blaise Mugisha.
 * @version 1.0
 */

public interface ISequenceNumberService {
    String NAME="sequenceNumberService";
    Long getNextSequenceNumber(ESequenceType type);
}
