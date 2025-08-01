package project.predix.store.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.predix.member.domain.Member;
import project.predix.sales.domain.Sales;
import project.predix.store.domain.enums.BusinessCategory;
import project.predix.store.dto.CreateRequestDto;

import java.time.LocalDate;
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

    @OneToOne(mappedBy = "store")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 100)
    private BusinessCategory category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(length = 100)
    private String address;

    @Column(name="road_address", length=100)
    private String roadAddress;

    @Column(length = 20)
    private String region1;

    @Column(length = 20)
    private String region2;

    @Column(length = 20)
    private String region3;

    @Column(name ="h_region3", length = 30)
    private String hRegion3;

    @Column(name = "road_name", length =30)
    private String roadName;

    @Column(length = 30)
    private String x;

    @Column(length = 30)
    private String y;

    @Column(name="b_code", length = 30)
    private String bCode;

    @Column(name="h_code", length = 30)
    private String hCode;

    private LocalDate since;

    private Store(String name, BusinessCategory category, String address, String roadAddress, String region1, String region2, String region3, String hRegion3, String roadName, String x, String y, String bCode, String hCode, LocalDate since) {
        this.name = name;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.address = address;
        this.roadAddress = roadAddress;
        this.region1 = region1;
        this.region2 = region2;
        this.region3 = region3;
        this.hRegion3 = hRegion3;
        this.roadName = roadName;
        this.x = x;
        this.y = y;
        this.bCode = bCode;
        this.hCode = hCode;
        this.since = since;
    }
    public static Store of(CreateRequestDto dto){
        return new Store(
                dto.getStoreName(),
                dto.getCategory(),
                dto.getAddress(),
                dto.getRoadAddress(),
                dto.getRegion1(),
                dto.getRegion2(),
                dto.getRegion3(),
                dto.getRegion3h(),
                dto.getRoadName(),
                dto.getX(),
                dto.getY(),
                dto.getCodeB(),
                dto.getCodeH(),
                dto.getSince());
    }

    public Store(String name,
                 BusinessCategory category) {
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
