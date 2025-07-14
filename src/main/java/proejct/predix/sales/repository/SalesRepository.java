package proejct.predix.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proejct.predix.sales.domain.Sales;

public interface SalesRepository extends JpaRepository<Sales, Long> {
}
