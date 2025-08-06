package project.predix.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.predix.member.domain.Member;
import project.predix.store.domain.entity.Store;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByMember(Member member);

    @Query("select distinct s.stationCode from Store s where s.stationCode is not null")
    List<Integer> findDistinctStationIds();
}
