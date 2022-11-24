package demo08;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Country {
	
	private String name;

	@Autowired
	private String largestCity;
	
	@Autowired
	private String smallestCity;
	
	public Country(@Value("Romania") String name) {
		this.name = name;
		System.out.println("DEBUG: Country::.ctor");
	}
	
	public String getLargestCity() {
		return largestCity;
	}
	
	public String getSmallestCity() {
		return smallestCity;
	}
	
	@Override
	public String toString() {
		return String.format("The largest city in %s is %s.\n"
				+ "The smallest city in %s is %s.",
				name, largestCity, name, smallestCity);
	}
}
