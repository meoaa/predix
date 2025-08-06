package project.predix.weather.repistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.predix.weather.domain.WeatherDaily;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherDailyRepository extends JpaRepository<WeatherDaily, Long>, WeatherDailyRepositoryCustom {

    boolean existsByStationId(int stationId);

    @Query("select max(w.baseDate) from WeatherDaily w where w.stationId = :stationId")
    Optional<LocalDate> findMaxDateByStatinId(@Param("stationId") int stationId);
}
