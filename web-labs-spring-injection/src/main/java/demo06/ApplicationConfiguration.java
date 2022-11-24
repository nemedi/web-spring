package demo06;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
	
	@Bean
	public City createCity() {
		System.out.println("DEBUG: ApplicationConfiguration:.createCity");
		return new City("Bucharest");
	}
	
	@Bean
	public Country createCountry(@Value("Romania") String name, City capital) {
		System.out.println("DEBUG: ApplicationConfiguration:.createCountry");
		Country country = new Country(name, capital);
		capital.setCountry(country);
		return country;
	}

}
