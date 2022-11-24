package demo05;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Country {

	private String name;
	private City capital;
	
	public Country(@Value("Romania") String name) {
		this.name = name;
		System.out.println("DEBUG: Country::.ctor");
	}
	
	public String getName() {
		return name;
	}
	
	public City getCapital() {
		return capital;
	}
	
	@Autowired
	public void setCapital(City capital) {
		this.capital = capital;
		capital.setCountry(this);
		System.out.println("DEBUG: Country::setCapital");
	}
	
	@Override
	public String toString() {
		return String.format("%s has the capital city of %s.",
				name, capital.getName());
	}
}
