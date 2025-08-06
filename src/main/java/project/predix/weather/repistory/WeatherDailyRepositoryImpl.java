package project.predix.weather.repistory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.predix.weather.domain.WeatherDaily;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WeatherDailyRepositoryImpl implements WeatherDailyRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final NamedParameterJdbcTemplate template;

    @Override
    @Transactional
    public void bulkUpsert(List<WeatherDaily> list) {
        if (list.isEmpty()) return;

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO tb_weather_daily (station_id, base_date, avg_ta, min_ta, max_ta, sum_rn) VALUES ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        for(int i = 0; i < list.size(); i++){
            WeatherDaily w = list.get(i);
            sql
                    .append("(:station")
                    .append(i)
                    .append(", :date")
                    .append(i)
                    .append(", :avg")
                    .append(i)
                    .append(", :min")
                    .append(i)
                    .append(", :max")
                    .append(i)
                    .append(", :sum")
                    .append(i).append(")");
            if(i <list.size() - 1) sql.append(",");

            params.addValue("station" + i, w.getStationId());
            params.addValue("date" + i, w.getBaseDate());
            params.addValue("avg" + i, w.getAvgTa());
            params.addValue("min" + i, w.getMinTa());
            params.addValue("max" + i, w.getMaxTa());
            params.addValue("sum" + i, w.getSumRn());
        }
        sql.append(" ON DUPLICATE KEY UPDATE avg_ta = VALUES(avg_ta), min_ta = VALUES(min_ta), max_ta = VALUES(max_ta), sum_rn = VALUES(sum_rn)");
        template.update(sql.toString(), params);
    }
}
