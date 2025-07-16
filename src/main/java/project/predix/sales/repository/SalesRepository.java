package project.predix.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.sales.domain.Sales;

public interface SalesRepository extends JpaRepository<Sales, Long> {
}
