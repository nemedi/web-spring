package demo01;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public Country createCountry() {
		System.out.println("DEBUG: ApplicationConfiguration::createCountry");
		return new Country("Romania", "Europe", "Bucharest");
	}
}
