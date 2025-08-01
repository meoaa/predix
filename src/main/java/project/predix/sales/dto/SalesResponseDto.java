package project.predix.sales.dto;


import lombok.Data;
import lombok.ToString;
import project.predix.sales.domain.Sales;
import project.predix.sales.domain.SalesType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
public class SalesResponseDto {
    private Long id;
    private Long amount;

    private LocalDateTime createdAt;

    private LocalDate startDate;

    private LocalDate endDate;

    private int orderNum;

    private SalesType type;

    private String label;

    private SalesResponseDto(Long id,
                             Long amount,
                             LocalDateTime createdAt,
                             LocalDate startDate,
                             LocalDate endDate,
                             SalesType type,
                             String label) {
        this.id = id;
        this.amount = amount;
        this.createdAt = createdAt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.label = label;
    }

    public static SalesResponseDto of(Sales sales){
        return new SalesResponseDto(
                sales.getId(),
                sales.getAmount(),
                sales.getCreatedAt(),
                sales.getStartDate(),
                sales.getEndDate(),
                sales.getType(),
                sales.getLabel());
    }
}
