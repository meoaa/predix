package project.predix.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.store.domain.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
