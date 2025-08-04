package project.predix.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.weather.domain.WeatherDaily;
import project.predix.weather.domain.WeatherDailyPk;

import java.time.LocalDate;
import java.util.List;

public interface WeatherDailyRepository extends JpaRepository<WeatherDaily, WeatherDailyPk> {
    List<WeatherDaily> findByIdStnCodeAndIdObsDateBetween(int code, LocalDate from, LocalDate to);
}
