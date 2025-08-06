package project.predix.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import project.predix.weather.config.WeatherProps;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherClient {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final WeatherProps props;
    /** WebClientConfig.weatherWebClient() 에서 주입 */
    private final WebClient weatherWebClient;

    private final XmlMapper xmlMapper = new XmlMapper();

    public JsonNode fetchDaily(int stnId, LocalDate from, LocalDate to, int page) {
        String serviceKey = props.getServiceKey();

        return weatherWebClient.get()
                .uri(b -> b.path(props.getPath())
//                        .queryParam("ServiceKey", props.getServiceKey())
                        .queryParam("serviceKey", "{serviceKey}")
                        .queryParam("dataType", "JSON")
                        .queryParam("dataCd", "ASOS")
                        .queryParam("dateCd", "DAY")
                        .queryParam("startDt", YYYYMMDD.format(from))
                        .queryParam("endDt",   YYYYMMDD.format(to))
                        .queryParam("stnIds",  stnId)
                        .queryParam("pageNo",  page)
                        .queryParam("numOfRows", props.getMaxRows())
                        .build(serviceKey))   // ServiceKey 중복 인코딩 방지
                .exchangeToMono(res -> {
                    MediaType ct = res.headers().contentType().orElse(MediaType.APPLICATION_JSON);
                    if (ct.isCompatibleWith(MediaType.APPLICATION_JSON)) {
                        return res.bodyToMono(JsonNode.class);
                    }
                    // XML fallback
                    return res.bodyToMono(String.class)
                            .map(this::xmlToJson);
                })
                .doOnSubscribe(s -> log.debug("[WeatherClient] >>> page={} stn={} {}~{}", page, stnId, from, to))
                .block();
    }

    private JsonNode xmlToJson(String xml) {
        try {
            return xmlMapper.readTree(xml.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XML response from KMA", e);
        }
    }
}
