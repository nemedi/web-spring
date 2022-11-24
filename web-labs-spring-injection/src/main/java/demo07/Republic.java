package demo07;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Republic extends Country {
	
	public Republic(@Value("Romania") String name,
			@Autowired @Qualifier("president") Person president) {
		super(name);
		setHeadOfState(president);
		System.out.println("DEBUG: Republic::.ctor");
	}

}
