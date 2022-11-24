package demo06;

public class Country {

	private String name;
	private City capital;
	
	public Country(String name, City capital) {
		this.name = name;
		this.capital = capital;
		System.out.println("DEBUG: Country:.ctor");
	}
	
	public String getName() {
		return name;
	}
	
	public City getCapital() {
		return capital;
	}
	
	@Override
	public String toString() {
		return String.format("%s has the capital city of %s.",
				name, capital.getName());
	}
}
