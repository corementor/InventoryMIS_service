package io.corementor.finexp.sales.salesOrder.repository;

import io.corementor.finexp.sales.salesOrder.domain.SalesOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import psychemesh.framework.common.util.EEntityLifeCycle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Interface Sale Order Repository
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Repository
public interface ISalesOrderRepository extends JpaRepository<SalesOrderEntity, UUID>, JpaSpecificationExecutor<SalesOrderEntity> {

    List<SalesOrderEntity> findAllByState(EEntityLifeCycle state);

    Optional<SalesOrderEntity> findBySaleCode(String saleCode);

}
