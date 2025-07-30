package project.predix.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import project.predix.sales.domain.Sales;
import project.predix.sales.domain.SalesType;
import project.predix.store.domain.entity.Store;

public interface SalesRepository extends JpaRepository<Sales, Long> {

    @Modifying(flushAutomatically = true)
    @Query("""
        delete from Sales s
        where s.store.id = :storeId
          and s.type      = :type
    """)
    int deleteByStoreIdAndType(@Param("storeId") Long storeId,
                               @Param("type") SalesType type);
}
