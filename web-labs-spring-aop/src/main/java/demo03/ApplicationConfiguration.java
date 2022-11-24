package demo03;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class ApplicationConfiguration {

	@Bean
	@Scope("prototype")
	public RandomNumber generateRandomNumber() {
		return new RandomNumber();
	}
}
