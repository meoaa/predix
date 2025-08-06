package project.predix.weather.sync;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.weather.client.WeatherClient;
import project.predix.weather.domain.WeatherDaily;
import project.predix.weather.repistory.WeatherDailyRepository;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherSyncService {

    private final WeatherClient client;
    private final WeatherDailyRepository repository;

    private WeatherDaily map(JsonNode n){
        return WeatherDaily.builder()
                .stationId(n.get("stnId").asInt())
                .baseDate(LocalDate.parse(n.get("tm").asText().substring(0,10)))
                .avgTa(asDouble(n,"avgTa"))
                .minTa(asDouble(n, "minTa"))
                .maxTa(asDouble(n, "maxTa"))
                .sumRn(asDouble(n, "sumRn"))
                .build();
    }

    private Double asDouble(JsonNode n, String field){
        String v = n.get(field).asText();
        return v == null || v.isBlank() ? null : Double.valueOf(v);
    }

    @Transactional
    public void syncRecent(int stationId, int days){
        LocalDate to = LocalDate.now().minusDays(1);
        LocalDate from = to.minusDays(days - 1);
        pullAndUpsert(stationId, from, to);
    }

    @Transactional
    public void syncRange(int stationId, LocalDate from, LocalDate to) {
        pullAndUpsert(stationId, from, to);
    }

    private void pullAndUpsert(int stationId, LocalDate from, LocalDate to) {
        log.info("[WeatherSync] station={} range {} ~ {}", stationId, from, to);
        ArrayList<WeatherDaily> buffer = new ArrayList<>();
        for(int page = 1; ;page++){
            JsonNode body = client.fetchDaily(stationId, from, to, page);
            log.info("body : ", body);
            JsonNode items = body.at("/response/body/items/item");
            if(!items.isArray() || items.isEmpty()) break;
            items.forEach(j -> buffer.add(map(j)));
            if(buffer.size() >= 1000) {
                repository.bulkUpsert(buffer);
                log.info("▶︎ inserted {} rows (page {})", buffer.size(), page);
                buffer.clear();
            }
        }
        if(!buffer.isEmpty()) repository.bulkUpsert(buffer);
    }
}
