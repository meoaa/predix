package project.predix.sales.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.predix.sales.dto.SalesUpsertRequestDto;
import project.predix.store.domain.entity.Store;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_sales_record", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"store_id", "type", "start_date"})
})
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

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SalesType type;

    private String label;

    public Sales(Long amount) {
        this.amount = amount;
    }

    private Sales(
            Long amount,
            LocalDate startDate,
            LocalDate endDate,
            SalesType type,
            String label) {
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.label = label;
        this.createdAt = LocalDateTime.now();
    }

    public static Sales of(SalesUpsertRequestDto dto){
        return new Sales(
                dto.getAmount(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getType(),
                dto.getLabel());
    }

    /**
     * 매출 정보를 업데이트 하는 메서드
     * @param amount 새로운 매출액
     */
    public void updateAmount(Long amount) {
        this.amount = amount;
    }

    /* 연관관계 메서드*/
    public void addStore(Store store){
        this.store = store;
    }
}
