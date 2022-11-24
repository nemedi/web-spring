package demo04;

public class City {

	private String name;
	private Country country;
	
	public City(String name) {
		this.name = name;
		System.out.println("DEBUG: City::.ctor");
	}
	
	public String getName() {
		return name;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	@Override
	public String toString() {
		return String.format("City of %s belongs to %s.",
				name, country.getName());
	}
}
