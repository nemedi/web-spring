package demo05;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ApplicationConfiguration {
	
	@Bean
	public City createCity() {
		System.out.println("DEBUG: ApplicationConfiguration::createCity");
		return new City("Bucharest");
	}

}
