package demo09;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ComponentScan
public class ApplicationConfiguration {
 
	@Bean
	@Order(3)
    public String getFirstCity() {
		System.out.println("DEBUG: ApplicationConfiguration::getFirstCity");
        return "Bucuresti";
    }
    
	@Bean
	@Order(1)
    public String getSecondCity() {
		System.out.println("DEBUG: ApplicationConfiguration::getSecondCity");
        return "Cluj";
    }
	
	@Bean
	@Order(2)
    public String getThirdCity() {
		System.out.println("DEBUG: ApplicationConfiguration::getThirdCity");
        return "Timisoara";
    }
 
}
