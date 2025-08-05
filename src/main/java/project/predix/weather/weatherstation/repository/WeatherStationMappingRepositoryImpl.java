package project.predix.weather.weatherstation.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static project.predix.weather.weatherstation.domain.QWeatherStationMapping.weatherStationMapping;


@Repository
@RequiredArgsConstructor
public class WeatherStationMappingRepositoryImpl implements WeatherStationMappingRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Optional<Integer> findBestCode(String region1, String region2) {

        var rankExpr = new CaseBuilder()
                .when(weatherStationMapping.locationName.eq("서울").and(weatherStationMapping.locationName.eq(region1))).then(1)
                .when(weatherStationMapping.locationName.eq(region2)).then(2)
                .when(weatherStationMapping.regionGroup.eq(region1)).then(3)
                .otherwise(99);

        Integer code = jpaQueryFactory.select(weatherStationMapping.stationCode)
                .from(weatherStationMapping)
                .where(rankExpr.lt(99))
                .orderBy(rankExpr.asc())
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(code);
    }

    @Override
    public Optional<Integer> findCodeByLocationName(String name) {
        Integer code = jpaQueryFactory.select(weatherStationMapping.stationCode)
                .from(weatherStationMapping)
                .where(weatherStationMapping.locationName.eq(name))
                .fetchOne();

        return Optional.ofNullable(code);
    }

    @Override
    public Optional<Integer> findCodeByRegionGroup(String group) {
        Integer code = jpaQueryFactory.select(weatherStationMapping.stationCode)
                .from(weatherStationMapping)
                .where(weatherStationMapping.regionGroup.eq(group))
                .fetchOne();

        return Optional.ofNullable(code);
    }
}
