package demo08;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ApplicationConfiguration {
 
	@Bean("largestCity")
    public String getLargestCity() {
		System.out.println("DEBUG: ApplicationConfiguration::getLargestCity");
        return "Bucharest";
    }
    
	@Bean("smallestCity")
    public String getSmallestCity() {
		System.out.println("DEBUG: ApplicationConfiguration::getSmallestCity");
        return "Tusnad";
    }
 
}
