package project.predix.sales.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SalesChartDataDto {
    private List<String> labels;
    private List<Long> data;
    private String title;
}
