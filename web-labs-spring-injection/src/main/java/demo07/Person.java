package demo07;

public abstract class Person {

	private String name;
	
	protected Person(String name) {
		this.name = name;
		System.out.println("DEBUG: Person::.ctor");
	}
	
	public String getName() {
		return name;
	}
}
