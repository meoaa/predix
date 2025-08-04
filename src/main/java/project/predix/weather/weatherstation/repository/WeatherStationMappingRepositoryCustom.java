package project.predix.weather.weatherstation.repository;

import java.util.Optional;

public interface WeatherStationMappingRepositoryCustom {
    Optional<Integer> findBestCode(String region1, String region2);
    Optional<Integer> findCodeByLocationName(String name);
    Optional<Integer> findCodeByRegionGroup(String group);
}
