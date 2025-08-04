package project.predix.weather.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "weather_daily")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class WeatherDaily {

    @EmbeddedId
    private WeatherDailyPk id;
    private Double avgTa; //평균기온
    private Double minTa; //최저기온
    private Double maxTa; //최고기온
    private Double rainMn; //일강수량
}
