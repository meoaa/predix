package project.predix.sales.dto;

import lombok.Data;
import lombok.ToString;
import project.predix.sales.domain.SalesType;

import java.time.LocalDate;

@Data
@ToString
public class SalesCreateRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long amount;
    private int orderNum;
    private SalesType type;
    private String label;
}
