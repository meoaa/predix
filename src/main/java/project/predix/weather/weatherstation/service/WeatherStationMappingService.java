package project.predix.weather.weatherstation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.exception.WeatherStationNotMatchException;
import project.predix.weather.weatherstation.repository.WeatherStationMappingRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class WeatherStationMappingService {

    private final WeatherStationMappingRepository repository;

    public int resolveCode(String region1, String region2){
        return repository.findBestCode(region1, region2)
                .orElseThrow(() -> new WeatherStationNotMatchException(region1 + "/" + region2));
    }
}
