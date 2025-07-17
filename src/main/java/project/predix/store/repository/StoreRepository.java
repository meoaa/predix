package project.predix.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.store.domain.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
