package demo11;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// get bean from context with circular dependencies
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			City city = context.getBean(City.class);
			Country country = context.getBean(Country.class);
			System.out.println(city);
			System.out.println(country);
		}
	}

}
