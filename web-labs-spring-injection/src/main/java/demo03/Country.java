package demo03;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Country {

	private String name;
	private String continent;
	private String capital;
	
	public Country(@Value("Romania") String name,
			@Value("Europe") String continent,
			@Value("Bucharest") String capital) {
		this.name = name;
		this.continent = continent;
		this.capital = capital;
		System.out.println("Country::.ctor");
	}
	public String getName() {
		return name;
	}
	public String getContinent() {
		return continent;
	}
	public String getCapital() {
		return capital;
	}
	
	@Override
	public String toString() {
		return String.format("%s is located in %s and has the capital the city of %s.",
				name, continent, capital);
	}
}
