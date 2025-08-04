package project.predix.weather.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class WeatherDailyPk {
    private int stnCode; //기상관측소 코드
    private LocalDate obsDate; //관측 일자
}
