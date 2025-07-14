package proejct.predix.sales.repository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import proejct.predix.member.domain.Member;
import proejct.predix.sales.domain.Sales;
import proejct.predix.store.domain.Store;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class SalesRepositoryTest {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private EntityManager em;

    private Member member;
    private Store store;

    @BeforeEach
    public void setUp(){
        this.member = new Member("username", "password", "email", "nickname");
        em.persist(member);

        this.store = new Store("store", "category");
        member.assignStore(store);

        em.persist(store);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("매출 생성 테스트")
    public void createSalesTest(){
        log.info("매출 생성 테스트");
        Sales sale = new Sales( LocalDate.now(), 250000L, "category");
        salesRepository.save(sale);

        em.flush();
        em.clear();

        Sales findSale = salesRepository.findById(sale.getId()).get();

        assertThat(findSale.getId()).isEqualTo(sale.getId());
        assertThat(findSale.getSalesDate()).isEqualTo(sale.getSalesDate());
        assertThat(findSale.getAmount()).isEqualTo(sale.getAmount());
        assertThat(findSale.getCategory()).isEqualTo(sale.getCategory());
    }

    @Test
    @DisplayName("매출 전체 조회 테스트")
    public void findAllSalesTesT(){
        log.info("매출 전체 조회 테스트");
        Sales sale1 = new Sales( LocalDate.now(), 250000L, "category");
        Sales sale2 = new Sales(LocalDate.now(), 250000L, "test");

        salesRepository.save(sale1);
        salesRepository.save(sale2);
        em.flush();
        em.clear();

        List<Sales> findAllSales = salesRepository.findAll();
        long count = salesRepository.count();

        assertThat(findAllSales.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("매출 삭제 테스트")
    public void deleteSalesTest(){
        log.info("매출 삭제 테스트");
        Sales sale1 = new Sales( LocalDate.now(), 250000L, "category");
        Sales sale2 = new Sales( LocalDate.now(), 250000L, "test");
        salesRepository.save(sale1);
        salesRepository.save(sale2);

        em.flush();
        em.clear();

        List<Sales> findAllSales = salesRepository.findAll();
        long count = salesRepository.count();

        assertThat(findAllSales.size()).isEqualTo(count);

        salesRepository.delete(sale1);

        em.flush();
        em.clear();

        List<Sales> sales = salesRepository.findAll();
        assertThat(sales.size()).isNotEqualTo(count);
        assertThat(sales.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("연관관계 생성 테스트")
    public void createMappedByStoreTest(){
        log.info("연관관계 생성 테스트");
        Sales sales = new Sales(LocalDate.now(), 10000L, "category");
        store.assignSales(sales);
        Sales savedSales = salesRepository.save(sales);

        em.flush();
        em.clear();

        assertThat(savedSales.getId()).isEqualTo(sales.getId());
        assertThat(savedSales.getStore().getId()).isEqualTo(store.getId());
        assertThat(savedSales.getStore().getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("연관관계 삭제 테스트")
    public void deleteMappedByStoreTest(){
        log.info("연관관계 삭제 테스트");
        Sales sales = new Sales(LocalDate.now(), 10000L, "category");
        store.assignSales(sales);
        salesRepository.save(sales);

        Member member2 = new Member("username2", "pw", "email2", "nickname2");
        em.persist(member2);
        Store store2 = new Store("store2", "category2");
        member2.assignStore(store2);
        em.persist(store2);

        Sales sales2 = new Sales(LocalDate.now(), 100000L, "category2");
        store2.assignSales(sales2);
        salesRepository.save(sales2);

        em.flush();
        em.clear();

        Store foundStore = em.find(Store.class, store2.getId());
        foundStore.getSales().clear();
        em.remove(foundStore);

        em.flush();
        em.clear();

        long count2 = salesRepository.count();
        assertThat(count2).isEqualTo(1);
    }
}