package demo07;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ApplicationConfiguration {
	
	@Bean("president")
	public Person createPresident(@Value("Klaus Iohannis") String name) {
		System.out.println("DEBUG: ApplicationConfiguration::createPresident");
		return new President(name);
	}
	
	@Bean("king")
	public Person createKing(@Value("Harald the Fifth") String name) {
		System.out.println("DEBUG: ApplicationConfiguration::createKing");
		return new King(name);
	}
	
}
