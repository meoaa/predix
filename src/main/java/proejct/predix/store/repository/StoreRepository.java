package proejct.predix.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proejct.predix.store.domain.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
