package proejct.predix.sales.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import proejct.predix.store.domain.Store;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_sales_record")
public class Sales {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "sales_date")
    private LocalDate salesDate;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Sales(
                 LocalDate salesDate,
                 Long amount,
                 String category) {
        this.salesDate = salesDate;
        this.amount = amount;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    /* 연관관계 메서드*/
    public void addStore(Store store){
        this.store = store;
    }
}
