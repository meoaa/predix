package project.predix.weather.weatherstation.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.weather.weatherstation.domain.WeatherStationMapping;

public interface WeatherStationMappingRepository extends JpaRepository<WeatherStationMapping, Long>, WeatherStationMappingRepositoryCustom {
}
