package demo11;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class City {

	private String name;
	private Country country;
	
	public City(@Value("Bucharest") String name, @Autowired @Lazy Country country) {
		this.name = name;
		this.country = country;
	}
	
	public String getName() {
		return name;
	}
	
	public Country getCountry() {
		return country;
	}
	
	@Override
	public String toString() {
		return String.format("%s belongs to %s.",
				name, country.getName());
	}
}
