package demo07;

public class President extends Person {

	public President(String name) {
		super(name);
		System.out.println("DEBUG: President::.ctor");
	}
	
	@Override
	public String toString() {
		return String.format("President %s", getName());
	}
}
