package io.corementor.finexp.base;


import io.corementor.finexp.inventory.common.util.ESequenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * The Interface ISequenceNumberRepo.
 *
 * @author Jeremie Ukundwa Tuyisenge
 * @version 1.0
 */

@Repository
public interface ISequenceNumberRepo extends JpaRepository<SequenceNumber, UUID> {

    /**
     * Find sequence number by type.
     *
     * @param type the type
     * @return optional
     */
//    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<SequenceNumber> findSequenceNumberByType(ESequenceType type);
}
