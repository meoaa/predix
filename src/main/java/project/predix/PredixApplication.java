package project.predix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PredixApplication {

	public static void main(String[] args) {
		SpringApplication.run(PredixApplication.class, args);
	}

}
