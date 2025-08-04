package project.predix.weather.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WeatherApiClient {

    private final WebClient client;
    private final String serviceKey;

    public WeatherApiClient(@Value("${weather.kma.base-url}") String baseUrl,
                            @Value("${weather.kma.service-key}") String key,
                            WebClient.Builder builder){
        this.client = builder.baseUrl(baseUrl).build();
        this.serviceKey = key;
    }

    public List<DailyDto> fetchRange(int stn, String startDt, String endDt){
        return client.get()
                .uri(uri -> uri
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 999)
                        .queryParam("dataType", "JSON")
                        .queryParam("dataCd", "ASOS")
                        .queryParam("dateCd", "DAY")
                        .queryParam("startDt", startDt)
                        .queryParam("endDt", endDt)
                        .queryParam("stnIds", stn)
                        .build())
                .retrieve()

    }


}
