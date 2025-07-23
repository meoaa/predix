package project.predix.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.member.domain.Member;
import project.predix.store.domain.entity.Store;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByMember(Member member);
}
