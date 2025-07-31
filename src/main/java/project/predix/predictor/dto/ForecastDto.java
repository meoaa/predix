package project.predix.predictor.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ForecastDto {

    @JsonAlias("ds")
    private LocalDate ds;

    @JsonAlias("yhat")
    private double yhat;

    @JsonAlias("yhat_lower")
    private double yhatLower;

    @JsonAlias("yhat_upper")
    private double yhatUpper;
}
