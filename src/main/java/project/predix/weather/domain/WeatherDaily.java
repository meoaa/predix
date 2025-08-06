package project.predix.weather.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_weather_daily",
        uniqueConstraints = @UniqueConstraint(name = "uk_station_date",
        columnNames = {"station_id", "base_date"}))
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)

public class WeatherDaily {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_id", nullable = false)
    private int stationId;

    @Column(name = "base_date", nullable = false)
    private LocalDate baseDate;

    @Column(name = "avg_ta")
    private Double avgTa;

    @Column(name = "min_ta")
    private Double minTa;

    @Column(name = "max_ta")
    private Double maxTa;

    @Column(name = "sum_rn")
    private Double sumRn;

    @Builder
    private WeatherDaily(int stationId, LocalDate baseDate, Double avgTa, Double minTa, Double maxTa, Double sumRn) {
        this.stationId = stationId;
        this.baseDate = baseDate;
        this.avgTa = avgTa;
        this.minTa = minTa;
        this.maxTa = maxTa;
        this.sumRn = sumRn;
    }
}
