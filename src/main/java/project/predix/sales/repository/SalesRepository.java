package project.predix.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.predix.sales.domain.Sales;
import project.predix.sales.domain.SalesType;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Long> {

    @Modifying(flushAutomatically = true)
    @Query("""
        delete from Sales s
        where s.store.id = :storeId
          and s.type      = :type
    """)
    int deleteByStoreIdAndType(@Param("storeId") Long storeId,
                               @Param("type") SalesType type);

    List<Sales> findAllByStoreIdOrderByStartDate(long storeId);

    List<Sales> findAllByStoreIdAndTypeOrderByStartDate(long storeId, SalesType type);

}
