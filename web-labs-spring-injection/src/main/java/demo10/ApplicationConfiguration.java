package demo10;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
 
	@Bean
    public FibonacciFactoryBean createFibonaccyFactory() {
		System.out.println("DEBUG: ApplicationConfiguration::createFibonaccyFactory");
        return new FibonacciFactoryBean();
    }
    
}
