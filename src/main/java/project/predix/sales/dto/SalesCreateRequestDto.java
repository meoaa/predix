package project.predix.sales.dto;

import lombok.Data;
import project.predix.sales.domain.SalesType;

import java.time.LocalDate;

@Data
public class SalesCreateRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long amount;
    private int orderNum;
    private SalesType type;
}
