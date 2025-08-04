package project.predix.weather.weatherstation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "weather_station_mapping")
public class WeatherStationMapping {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_code")
    private int stationCode;

    @Column(name = "location_name", length = 50)
    private String locationName;

    @Column(name = "agency", length = 50)
    private String agency;

    @Column(name = "region_group", length = 50)
    private String regionGroup;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
