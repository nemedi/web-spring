package demo01;

public class Country {

	private String name;
	private String continent;
	private String capital;
	
	public Country(String name, String continent, String capital) {
		this.name = name;
		this.continent = continent;
		this.capital = capital;
		System.out.println("DEBUG: Country::.ctor");
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
	public void resetTo(String name, String continent, String capital) {
		this.name = name;
		this.continent = continent;
		this.capital = capital;
	}
	
	@Override
	public String toString() {
		return String.format("%s is located in %s and has the capital the city of %s.",
				name, continent, capital);
	}
}
