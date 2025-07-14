package proejct.predix.store.repository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import proejct.predix.member.domain.Member;
import proejct.predix.member.repository.MemberRepository;
import proejct.predix.store.domain.Store;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@Slf4j
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    private Member member1;
    private Member member2;

    @BeforeEach
    public void setup(){
        member1 =
                new Member("username",
                        "password",
                        "email",
                        "nickname");

        member2 =
                new Member("username2",
                        "password2",
                        "email2",
                        "nickname2");

        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("가게 생성 테스트")
    public void createStoreTest(){

        log.info("가게 생성 테스트");
        Store store = new Store("store", "cafe");
        member1.assignStore(store);

        Store savedStore = storeRepository.save(store);

        em.flush();
        em.clear();

        assertThat(savedStore.getId()).isEqualTo(store.getId());
        assertThat(savedStore.getMember().getId()).isEqualTo(member1.getId());
    }

    @Test
    @DisplayName("가게 조회 테스트")
    public void findStoreByIdTest(){
        log.info("가게 조회 테스트");
        Store store = new Store("store", "cafe");
        member1.assignStore(store);
        storeRepository.save(store);

        em.flush();
        em.clear();

        Store foundStore = storeRepository.findById(store.getId()).get();

        assertThat(foundStore.getId()).isEqualTo(store.getId());
        assertThat(foundStore.getMember().getId()).isEqualTo(member1.getId());
    }

    @Test
    @DisplayName("전체 가게 조회 테스트")
    public void findAllStoresTest(){
        log.info("전체 가게 조회 테스트");
        Store store1 = new Store("store1", "category1");
        Store store2 = new Store("store2", "category2");

        member1.assignStore(store1);
        member2.assignStore(store2);

        storeRepository.save(store1);
        storeRepository.save(store2);

        em.flush();
        em.clear();

        List<Store> foundAllStores = storeRepository.findAll();
        long count = storeRepository.count();

        assertThat(foundAllStores.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("가게 삭제 테스트")
    public void deleteStoreTest(){
        log.info("가게 삭제 테스트");
        Store store1 = new Store("store", "category");
        Store store2 = new Store("store", "category");
        member1.assignStore(store1);
        member2.assignStore(store2);

        storeRepository.save(store1);
        storeRepository.save(store2);

        storeRepository.delete(store1);

        em.flush();
        em.clear();

        List<Store> foundAllStore = storeRepository.findAll();

        assertThat(foundAllStore.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("멤버 삭제시 가게 삭제 테스트")
    public void deleteMemberWhenStoreDeleteTest(){
        log.info("멤버 삭제시 가게 삭제 테스트");
        Store store1 = new Store("store", "category");
        Store store2 = new Store("store2", "category");
        member1.assignStore(store1);
        member2.assignStore(store2);

        storeRepository.save(store1);
        storeRepository.save(store2);

        em.flush();
        em.clear();

        List<Store> foundAllStores1 = storeRepository.findAll();
        assertThat(foundAllStores1.size()).isEqualTo(2);

        memberRepository.delete(member1);

        em.flush();
        em.clear();

        List<Store> foundAllStores2 = storeRepository.findAll();
        assertThat(foundAllStores2.size()).isEqualTo(1);
    }
}