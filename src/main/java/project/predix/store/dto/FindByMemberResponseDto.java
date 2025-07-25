package project.predix.store.dto;

import lombok.Data;
import project.predix.store.domain.entity.Store;
import project.predix.store.domain.enums.BusinessCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FindByMemberResponseDto {

    private Long id;
    private String storeName;
    private BusinessCategory category;
    private LocalDate since;
    private String address;
    private String roadAddress;
    private String x;
    private String y;

    private FindByMemberResponseDto(Long id, String storeName, BusinessCategory category, LocalDate since, String address, String roadAddress, String x, String y) {
        this.id = id;
        this.storeName = storeName;
        this.category = category;
        this.since = since;
        this.address = address;
        this.roadAddress = roadAddress;
        this.x = x;
        this.y = y;
    }

    public static FindByMemberResponseDto of(Store store){
        return new FindByMemberResponseDto(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getSince(),
                store.getAddress(),
                store.getRoadAddress(),
                store.getX(),
                store.getY()
        );
    }
}
