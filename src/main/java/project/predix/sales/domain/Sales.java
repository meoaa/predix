package project.predix.sales.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.predix.sales.dto.SalesCreateRequestDto;
import project.predix.store.domain.entity.Store;

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

    @Column(name = "amount")
    private Long amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "order_num")
    private int orderNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SalesType type;

    public Sales(Long amount) {
        this.amount = amount;
    }

    private Sales(
            Long amount,
            LocalDate startDate,
            LocalDate endDate,
            int orderNum,
            SalesType type) {
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.orderNum = orderNum;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

    public static Sales of(SalesCreateRequestDto dto){
        return new Sales(
                dto.getAmount(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getOrderNum(),
                dto.getType());
    }

    /* 연관관계 메서드*/
    public void addStore(Store store){
        this.store = store;
    }
}
