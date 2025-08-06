package project.predix.weather.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "weather.kma")
@Getter @Setter
public class WeatherProps {
    private String baseUrl;
    private String path;
    private String serviceKey;
    private int maxRows = 999;
}
