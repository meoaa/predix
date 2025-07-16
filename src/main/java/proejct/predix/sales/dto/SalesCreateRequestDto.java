package proejct.predix.sales.dto;

import lombok.Data;
import proejct.predix.sales.domain.SalesType;

import java.time.LocalDate;

@Data
public class SalesCreateRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long amount;
    private int orderNum;
    private SalesType type;
}
