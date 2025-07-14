package proejct.predix.store.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import proejct.predix.member.domain.Member;
import proejct.predix.sales.domain.Sales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="tb_store")
public class Store {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "store",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Sales> sales = new ArrayList<>();

    @Column(name="store_name")
    private String name;

    @JoinColumn(name = "member_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Store(String name,
                 String category) {
        this.name = name;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    /* 연관관계 메서드 */
    public void changeMember(Member member){
        if(this.member != null){
            this.member.assignStore(null);
        }
        this.member = member;
        if(member != null && this.member.getStore() != this){
            member.assignStore(this);
        }
    }

    public void assignSales(Sales sales){
        if(!this.sales.contains(sales)){
            this.sales.add(sales);
        }
        if(sales.getStore() != this){
            sales.addStore(this);
        }
    }
}
