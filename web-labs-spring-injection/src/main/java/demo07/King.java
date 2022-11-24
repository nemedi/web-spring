package demo07;

public class King extends Person {

	public King(String name) {
		super(name);
		System.out.println("DEBUG: King::.ctor");
	}

	public String toString() {
		return String.format("King %s", getName());
	};
}
