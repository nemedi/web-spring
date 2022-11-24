package demo07;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Kingdom extends Country {
	
	public Kingdom(@Value("Norway") String name,
			@Autowired @Qualifier("king") Person president) {
		super(name);
		setHeadOfState(president);
		System.out.println("DEBUG: Kingdom::.ctor");
	}

}
