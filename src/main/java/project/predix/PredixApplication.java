package project.predix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PredixApplication {

	public static void main(String[] args) {
		SpringApplication.run(PredixApplication.class, args);
	}

}
