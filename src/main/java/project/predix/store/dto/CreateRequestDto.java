package project.predix.store.dto;

import lombok.Data;
import project.predix.store.domain.enums.BusinessCategory;

import java.time.LocalDate;

@Data
public class CreateRequestDto {
    private String storeName;
    private BusinessCategory category;
    private String address;
    private String codeB;
    private String codeH;
    private String region1;
    private String region2;
    private String region3;
    private String region3h;
    private String roadAddress;
    private String roadName;
    private LocalDate since;
    private String x;
    private String y;
}
