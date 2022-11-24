package demo02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ApplicationConfiguration {

	@Bean
	@Scope("prototype")
	public Country createCountry() {
		System.out.println("DEBUG: ApplicationConfiguration::createCountry");
		return new Country("Romania", "Europe", "Bucharest");
	}
}
