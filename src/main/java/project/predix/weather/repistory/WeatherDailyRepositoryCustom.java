package project.predix.weather.repistory;

import project.predix.weather.domain.WeatherDaily;

import java.util.List;

public interface WeatherDailyRepositoryCustom {
    void bulkUpsert(List<WeatherDaily> list);
}
