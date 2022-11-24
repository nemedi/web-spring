package demo07;

public abstract class Country {

	private String name;
	private Person headOfState;
	
	protected Country(String name) {
		this.name = name;
		System.out.println("DEBUG: Country::.ctor");
	}
	
	public String getName() {
		return name;
	}
	
	public Person getHeadOfState() {
		return headOfState;
	}
	
	public void setHeadOfState(Person headOfState) {
		this.headOfState = headOfState;
	}
	
	@Override
	public String toString() {
		return String.format("%s is ruled by %s.",
				name, headOfState);
	}
	
}
