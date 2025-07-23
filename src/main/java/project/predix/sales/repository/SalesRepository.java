package project.predix.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.sales.domain.Sales;
import project.predix.store.domain.entity.Store;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    void deleteByStore(Store store);
}
